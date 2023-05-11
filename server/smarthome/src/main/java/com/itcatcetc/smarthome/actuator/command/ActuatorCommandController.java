package com.itcatcetc.smarthome.actuator.command;

import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.validation.Valid;
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
    public void registerNewData(@Valid @RequestBody ActuatorCommand command){
        commandDataService.addNewCommand(command);
    }

    @DeleteMapping(path = "{dataId}")
    @Secured(Role.HOMIE)
    public void deleteData(@Valid @PathVariable("dataId") Integer dataId){
        commandDataService.deleteCommand(dataId);
    }

    @PutMapping(path = "{dataId}")
    @Secured(Role.HOMIE)
    public void updateData(@Valid @PathVariable("dataId") Integer dataId, @RequestParam(required = false) Room room,
                           @RequestParam(required = false) Type premiseType, @RequestParam(required = false) boolean greaterThan,
                           @RequestParam(required = false) Integer premiseValue, @RequestParam(required = false) Type consequenceType,
                           @RequestParam(required = false) Integer consequenceValue, @RequestParam(required = false) Date startDate,
                           @RequestParam(required = false) Date expiryDate){
        commandDataService.updateCommand(room, dataId, premiseType, greaterThan, premiseValue, consequenceType,
                consequenceValue,startDate, expiryDate);
    }
}
