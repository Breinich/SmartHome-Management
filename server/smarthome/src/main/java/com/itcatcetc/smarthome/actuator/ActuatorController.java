package com.itcatcetc.smarthome.actuator;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Actuator controller
 * To wrap your repository with a web layer, you must turn to Spring MVC.
 * The @RestController annotation marks the class as a controller where every method returns a domain object instead of a view
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/actuators")
public class ActuatorController {
    private final ActuatorService actuatorService;

    @Autowired
    public ActuatorController(@Valid ActuatorService actuatorService) {
        this.actuatorService = actuatorService;
    }


    // GET request to retrieve all actuators
    // Only users with role GUEST or HOMIE can access this endpoint (see SecurityConfig)
    // Returns a JSON array of all actuators
    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getActuators() {
        List<Actuator> list = actuatorService.getActuators();
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(list);
        } catch (Exception e) {
            json = list.toString();
        }
        return ResponseEntity.ok(json);
    }

    // POST request to register a new actuator
    // Only users with role HOMIE can access this endpoint (see SecurityConfig)
    // Returns nothing
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewActuator(@Valid @RequestBody Actuator actuator) {
        actuatorService.addNewActuator(actuator);
    }

    // DELETE request to delete an actuator
    // Only users with role HOMIE can access this endpoint (see SecurityConfig)
    // Returns nothing
    @DeleteMapping(path = "{actuatorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteActuator(@Valid @PathVariable("actuatorId") Integer actuatorId) {
        actuatorService.deleteActuator(actuatorId);
    }

    // PUT request to update an actuator
    // Only users with role HOMIE can access this endpoint (see SecurityConfig)
    // Returns nothing
    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateActuator(@Valid @RequestBody Actuator actuator) {
        actuatorService.updateActuator(actuator);
    }
}
