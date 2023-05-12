package com.itcatcetc.smarthome.actuator.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getDatas(){
        List<ActuatorCommand> list =  commandDataService.getData();
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(list);
        } catch (Exception e) {
            json = list.toString();
        }
        return ResponseEntity.ok(json);
    }

    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewData(@Valid @RequestBody ActuatorCommand command){
        commandDataService.addNewCommand(command);
    }

    @DeleteMapping(path = "{dataId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteData(@Valid @PathVariable("dataId") Integer dataId){
        commandDataService.deleteCommand(dataId);
    }

    @PutMapping(path = "{dataId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void updateData(@Valid @PathVariable("dataId") Integer dataId, @RequestParam(required = false) Room room,
                           @RequestParam(required = false) Type premiseType, @RequestParam(required = false) boolean greaterThan,
                           @RequestParam(required = false) Integer premiseValue, @RequestParam(required = false) Type consequenceType,
                           @RequestParam(required = false) Integer consequenceValue, @RequestParam(required = false) Date startDate,
                           @RequestParam(required = false) Date expiryDate){
        commandDataService.updateCommand(room, dataId, premiseType, greaterThan, premiseValue, consequenceType,
                consequenceValue,startDate, expiryDate);
    }
}
