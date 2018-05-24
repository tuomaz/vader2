package se.tuomas.vader2.service;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import se.tuomas.vader2.domain.GraphData;
import se.tuomas.vader2.domain.GraphPoint;
import se.tuomas.vader2.domain.SensorSample;
import se.tuomas.vader2.domain.SensorSampleRowMapper;
import se.tuomas.vader2.service.ProcessSampleDataService;

@Service
public class SensorSampleService {
    JdbcTemplate jdbcTemplate;

    @Autowired
    ProcessSampleDataService processSampleDataService;

    @Autowired
    public void setDataSourceImage(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(final SensorSample sample) {
        jdbcTemplate.update("insert into sample (ts, value, type, name) values (?, ?, ?, ?)", sample.getTimestamp(), sample.getValue(), sample.getType().toString(), sample.getName());
    }

    public SensorSample getOne() {
        SensorSample sample = jdbcTemplate.queryForObject(
                " select ts, updated, value, type, name from sample limit 1", new SensorSampleRowMapper());
        return sample;
    }

    public List<SensorSample> getRecent() {
        long currentTime = System.currentTimeMillis();
        long recent = currentTime - (1000 * 60 * 60 * 24 * 3);
        Date date = new Date(recent);
        return jdbcTemplate.query("select ts, updated, value, type, name from sample where ts > ? and id in (select * from (select max(id) from sample where ts > ? group by name) as ids)",
                new SensorSampleRowMapper(), date, date);
    }

    public SensorSample getLatestByName(final String name) {
        SensorSample sample = jdbcTemplate.queryForObject(
                " select ts, updated, value, type, name from sample where name = ? order by ts desc limit 1", new SensorSampleRowMapper(), name);
        return sample;
    }

    public List<SensorSample> getLatest100ByName(final String name) {
        List<SensorSample> samples = jdbcTemplate.query(
                "select ts, updated, value, type, name from sample where name = ? order by ts desc limit 100", new SensorSampleRowMapper(), name);
        return samples;
    }

    public List<SensorSample> getLatest24hByName(final String name) {
        List<SensorSample> samples = jdbcTemplate.query(
                "select ts, updated, value, type, name from sample where name = ? and ts > (now() - interval 1 day) order by ts asc", new SensorSampleRowMapper(), name);
        return samples;
    }

    public List<SensorSample> getByNameAndHours(final String name, final int hours) {
        List<SensorSample> samples = jdbcTemplate.query(
                "select ts, updated, value, type, name from sample where name = ? and ts > (now() - interval ? hour) order by ts asc", new SensorSampleRowMapper(), name, hours);
        return samples;
    }

    public List<SensorSample> getLatest() {
        // 180524: select ts, updated, value, type, name from sample where ts in (select max(ts) from sample group by name);
        // BEFORE: select s.ts, s.updated, s.value, s.type, s.name from sample s inner join (select name, max(ts) as Newest from sample group by name) s2 on s.name = s2.name and s.ts = s2.Newest
        List<SensorSample> samples = jdbcTemplate.query(
                "select ts, updated, value, type, name from sample where ts in (select max(ts) from sample group by name)", new SensorSampleRowMapper());
        return processSampleDataService.processSamples(samples);
    }
    
    public GraphData getGraphData(String sensor, int hours) {
        List<SensorSample> samples = getByNameAndHours(sensor, hours);
        GraphData gd = convertSamplesToGraphData(samples);
        gd.setKey(sensor);
        return gd;
    }
    
    private GraphData convertSamplesToGraphData(List<SensorSample> samples) {
        List<SensorSample> processedSamples = processSampleDataService.processSamples(samples);
        GraphData graphData = new GraphData(samples.size());
        graphData.setRealName(processedSamples.get(0).getRealName());
        for (SensorSample sample: samples) {
            GraphPoint gp = new GraphPoint((float) Math.round(sample.getValue() * 10) / 10, sample.getTimestamp());
            graphData.addPoint(gp);
        }
        return graphData;
    }
}
