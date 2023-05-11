package com.itcatcetc.smarthome.sensor.data;

import com.itcatcetc.smarthome.login.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    @Secured({Role.GUEST, Role.HOMIE})
    public List<SensorData> getDatas(){
        return sensorDataService.getDatas();
    }

    @PostMapping
    @Secured(Role.HOMIE)
    public void registerNewData(@Valid @RequestBody SensorData sensorData){
        sensorDataService.addNewData(sensorData);
    }

    @DeleteMapping(path = "{dataId}")
    @Secured(Role.HOMIE)
    public void deleteData(@Valid @PathVariable("dataId") Integer dataId){
        sensorDataService.deleteData(dataId);
    }

    @PutMapping(path = "{dataId}")
    @Secured(Role.HOMIE)
    public void updateData(@Valid @PathVariable("dataId") Integer dataId, @RequestParam(required = false) Integer value){
        sensorDataService.updateData(dataId);
    }
}
