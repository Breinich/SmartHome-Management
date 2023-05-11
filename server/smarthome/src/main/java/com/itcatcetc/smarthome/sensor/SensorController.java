package com.itcatcetc.smarthome.sensor;

import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/sensors")
public class SensorController {
    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    @Secured({Role.GUEST, Role.HOMIE})
    public List<Sensor> getSensors(){
        return sensorService.getSensors();
    }

    @PostMapping
    @Secured(Role.HOMIE)
    public void registerNewSensor(@RequestBody  Sensor sensor){
        sensorService.addNewSensor(sensor);
    }

    @DeleteMapping(path = "{sensorId}")
    @Secured(Role.HOMIE)
    public void deleteSensor(@PathVariable("sensorId") Integer sensorId){
        sensorService.deleteSensor(sensorId);
    }

    @PutMapping(path = "{sensorId}")
    @Secured(Role.HOMIE)
    public void updateSensor(@PathVariable("sensorId") Integer sensorId, @RequestParam(required = false) String name,
                             @RequestParam(required = false) Type type, @RequestParam(required = false) Room room){
        sensorService.updateSensor(sensorId, name, type, room);
    }
}
