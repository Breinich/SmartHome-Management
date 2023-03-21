package SmartHome.com.smarthome.Sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/devices")
public class SensorController {
    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public List<Sensor> getSmartHomeDevices(){
        return sensorService.getSmartHomeDevices();
    }

    @PostMapping
    public void registerNewSensor(@RequestBody  Sensor sensor){
        sensorService.addNewSensor(sensor);
    }

    @DeleteMapping(path = "{sensorId}")
    public void deleteSensor(@PathVariable("sensorId") Integer sensorId){
        sensorService.deleteSensor(sensorId);
    }

    @PutMapping(path = "{sensorId}")
    public void updateSensor(@PathVariable("sensorId") Integer sensorId, @RequestParam(required = false) String name, @RequestParam(required = false) String room){
        sensorService.updateSensor(sensorId, name, room);
    }
}
