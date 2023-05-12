package com.itcatcetc.smarthome.actuator;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/actuators")
public class ActuatorController {
    private final ActuatorService actuatorService;

    @Autowired
    public ActuatorController(@Valid ActuatorService actuatorService) {
        this.actuatorService = actuatorService;
    }

    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getActuators(){
        List<Actuator> list =  actuatorService.getActuators();
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
    public void registerNewActuator(@Valid @RequestBody  Actuator actuator){
        actuatorService.addNewActuator(actuator);
    }

    @DeleteMapping(path = "{actuatorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteActuator(@Valid @PathVariable("actuatorId") Integer actuatorId){
        actuatorService.deleteActuator(actuatorId);
    }

    @PutMapping(path = "{actuatorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void updateActuator(@Valid @PathVariable("actuatorId") Integer actuatorId, @RequestParam(required = false) String name,
                               @RequestParam(required = false) Type type, @RequestParam(required = false) Room room,
                               @RequestParam(required = false) String apiUrl){
        actuatorService.updateActuator(actuatorId, name, type, room, apiUrl);
    }
}
