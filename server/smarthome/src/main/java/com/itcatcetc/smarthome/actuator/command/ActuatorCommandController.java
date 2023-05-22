package com.itcatcetc.smarthome.actuator.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/smarthome/command")
public class ActuatorCommandController {
    private final CommandDataService commandDataService;

    @Autowired
    public ActuatorCommandController(CommandDataService commandDataService) {
        this.commandDataService = commandDataService;
    }

    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getData() {
        List<ActuatorCommand> list = commandDataService.getData();
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
    public void registerNewData(@Valid @RequestBody ActuatorCommand command) {
        commandDataService.addNewCommand(command);
    }

    @DeleteMapping(path = "{dataId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteData(@Valid @PathVariable("dataId") Integer dataId) {
        commandDataService.deleteCommand(dataId);
    }

    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateData(@RequestBody ActuatorCommand command) {
        commandDataService.updateCommand(command);
    }
}
