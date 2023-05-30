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


    /**
     * GET request to retrieve all actuators
     * Only users with role GUEST or HOMIE can access this endpoint (see SecurityConfig)
     * Returns a JSON array of all actuators
     */
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

    /**
     * GET request to retrieve an actuator by id
     * @param actuator the actuator to retrieve
     */
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewActuator(@Valid @RequestBody Actuator actuator) {
        try {
            actuatorService.addNewActuator(actuator);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * DELETE request to delete an actuator by id
     * @param actuatorId the id of the actuator to delete
     */
    @DeleteMapping(path = "{actuatorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteActuator(@Valid @PathVariable("actuatorId") Integer actuatorId) {
        try {
            actuatorService.deleteActuator(actuatorId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * PUT request to update an actuator
     * @param actuator the actuator to update
     */
    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateActuator(@Valid @RequestBody Actuator actuator) {
        try{
            actuatorService.updateActuator(actuator);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
