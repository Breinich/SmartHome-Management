package com.itcatcetc.smarthome.actuator.command;

import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.sensor.data.SensorData;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/command")
public class ActuatorCommandController {
    private final CommandDataService commandDataService;

    @Autowired
    public ActuatorCommandController(CommandDataService commandDataService) {
        this.commandDataService = commandDataService;
    }

    @GetMapping
    @Secured({Role.GUEST, Role.HOMIE})
    public List<ActuatorCommand> getDatas(){
        return commandDataService.getDatas();
    }

    @PostMapping
    @Secured(Role.HOMIE)
    public void registerNewData(@RequestBody ActuatorCommand command){
        commandDataService.addNewData(command);
    }

    @DeleteMapping(path = "{dataId}")
    @Secured(Role.HOMIE)
    public void deleteData(@PathVariable("dataId") Integer dataId){
        commandDataService.deleteData(dataId);
    }

    @PutMapping(path = "{dataId}")
    @Secured(Role.HOMIE)
    public void updateData(@PathVariable("dataId") Integer dataId, @RequestParam(required = false) Type premiseType,
                           @RequestParam(required = false) Integer premiseValue, @RequestParam(required = false) Type consequenceType,
                           @RequestParam(required = false) Integer consequenceValue, @RequestParam(required = false) Date expiryDate){
        commandDataService.updateData(dataId, premiseType, premiseValue, consequenceType, consequenceValue, expiryDate);
    }
}
