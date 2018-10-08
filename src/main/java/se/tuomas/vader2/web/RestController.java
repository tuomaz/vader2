package se.tuomas.vader2.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import se.tuomas.vader2.domain.GraphData;
import se.tuomas.vader2.domain.SensorSample;
import se.tuomas.vader2.service.SensorSampleService;

@RequestMapping("/rest")
@Controller
public class RestController {
    @Autowired
    SensorSampleService sss;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void addPet(@RequestBody final List<SensorSample> samples) {
        if (samples != null) {
            for (SensorSample sample : samples) {
                sss.save(sample);
            }
        }
    }

    /*
     * @ResponseBody
     *
     * @RequestMapping(value = "/sample/getOne", produces = "application/json") public SensorSample getOne() { return sss.getOne(); }
     *
     * @ResponseBody
     *
     * @RequestMapping(value = "/sample/getRecent", produces = "application/json") public List<SensorSample> getRecent() { return sss.getRecent(); }
     */

    @ResponseBody
    @RequestMapping(value = "/sample/get", produces = "application/json")
    public List<SensorSample> getLatest() {
        return sss.getCached();
    }
    
    @ResponseBody
    @RequestMapping(value = "/sample/get/graph/{name}/{hours}", produces = "application/json")
    public GraphData[] getGraphData(@PathVariable String name, @PathVariable int hours) {
        GraphData[] gd = new GraphData[1];
        gd[0] = sss.getGraphData(name, hours);
        return gd;
    }

}
