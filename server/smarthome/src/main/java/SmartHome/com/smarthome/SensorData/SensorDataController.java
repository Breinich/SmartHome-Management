package SmartHome.com.smarthome.SensorData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/sensordata")
public class SensorDataController {
    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping
    public List<SensorData> getDatas(){
        return sensorDataService.getDatas();
    }

    @PostMapping
    public void registerNewData(@RequestBody SensorData sensorData){
        sensorDataService.addNewData(sensorData);
    }

    @DeleteMapping(path = "{dataId}")
    public void deleteData(@PathVariable("dataId") Integer dataId){
        sensorDataService.deleteData(dataId);
    }

    @PutMapping(path = "{dataId}")
    public void updateData(@PathVariable("dataId") Integer dataId, @RequestParam(required = false) Integer value){
        sensorDataService.updateData(dataId);
    }
}
