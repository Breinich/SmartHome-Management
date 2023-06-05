package com.itcatcetc.smarthome.actuator.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.type.Type;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Actuator command controller
 * To wrap your repository with a web layer, you must turn to Spring MVC.
 * The @RestController annotation marks the class as a controller where every method returns a domain object instead of a view
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/command")
public class ActuatorCommandController {
    private final CommandDataService commandDataService;

    @Autowired
    public ActuatorCommandController(CommandDataService commandDataService) {
        this.commandDataService = commandDataService;
    }

    /**
     * GET request to retrieve all commands
     * @return JSON array of all commands
     */
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

    /**
     * POST request to register a new command
     * @param command
     * only users with role HOMIE can access this endpoint (see SecurityConfig)
     */
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewData(@Valid @RequestBody ActuatorCommand command) {
        try {
            commandDataService.addNewCommand(command);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * DELETE request to delete a command
     * @param dataId
     * only users with role HOMIE can access this endpoint (see SecurityConfig)
     */
    @DeleteMapping(path = "{dataId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteData(@Valid @PathVariable("dataId") Integer dataId) {
        try {
            commandDataService.deleteCommand(dataId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * PUT request to update a command
     * @param command
     * only users with role HOMIE can access this endpoint (see SecurityConfig)
     */
    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateData(@RequestBody ActuatorCommand command) {
        try {
            commandDataService.updateCommand(command);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * GET request to retrieve all active commands of a specific room
     * @param roomId the id of the room
     * @param type the type of the command
     * @return JSON array of all active commands of a specific room
     */
    @GetMapping(path = "{roomId}/{type}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getActiveCommandsByRoomIdAndType(@PathVariable("roomId") Integer roomId, @PathVariable("type") Type type) {
        List<ActuatorCommand> list;
        try {
            list = commandDataService.getActiveCommandsByRoomIdAndType(roomId, type);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(list);
        } catch (Exception e) {
            json = list.toString();
        }
        return ResponseEntity.ok(json);
    }
}
