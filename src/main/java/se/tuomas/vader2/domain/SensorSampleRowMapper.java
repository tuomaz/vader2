package se.tuomas.vader2.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SensorSampleRowMapper implements RowMapper<SensorSample> {
	public SensorSample mapRow(ResultSet rs, int rowNum) throws SQLException {
		SensorSample sample = new SensorSample();
		sample.setTimestamp(rs.getTimestamp("ts"));
		sample.setUpdated(rs.getTimestamp("updated"));
		sample.setValue(rs.getFloat("value"));
		sample.setType(SensorType.valueOf(rs.getString("type")));
		sample.setName(rs.getString("name"));
		return sample;
	}
}
