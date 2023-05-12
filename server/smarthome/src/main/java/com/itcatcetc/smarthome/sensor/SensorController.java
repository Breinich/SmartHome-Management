package com.itcatcetc.smarthome.sensor;

import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public List<Sensor> getSensors(){
        return sensorService.getSensors();
    }

    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewSensor(@Valid @RequestBody  Sensor sensor){
        sensorService.addNewSensor(sensor);
    }

    @DeleteMapping(path = "{sensorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteSensor(@Valid @PathVariable("sensorId") Integer sensorId){
        sensorService.deleteSensor(sensorId);
    }

    @PutMapping(path = "{sensorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void updateSensor(@Valid @PathVariable("sensorId") Integer sensorId, @RequestParam(required = false) String name,
                             @RequestParam(required = false) Type type, @RequestParam(required = false) Room room,
                             @RequestParam(required = false) String apiUrl){
        sensorService.updateSensor(sensorId, name, type, room, apiUrl);
    }
}
