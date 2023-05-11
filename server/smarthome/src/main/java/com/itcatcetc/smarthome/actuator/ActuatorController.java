package com.itcatcetc.smarthome.actuator;



import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/actuators")
public class ActuatorController {
    private final ActuatorService actuatorService;

    @Autowired
    public ActuatorController(ActuatorService actuatorService) {
        this.actuatorService = actuatorService;
    }

    @GetMapping
    @Secured({Role.GUEST, Role.HOMIE})
    public List<Actuator> getActuators(){
        return actuatorService.getActuators();
    }

    @PostMapping
    @Secured(Role.HOMIE)
    public void registerNewActuator(@RequestBody  Actuator actuator){
        actuatorService.addNewActuator(actuator);
    }

    @DeleteMapping(path = "{actuatorId}")
    @Secured(Role.HOMIE)
    public void deleteActuator(@PathVariable("actuatorId") Integer actuatorId){
        actuatorService.deleteActuator(actuatorId);
    }

    @PutMapping(path = "{actuatorId}")
    @Secured(Role.HOMIE)
    public void updateActuator(@PathVariable("actuatorId") Integer actuatorId, @RequestParam(required = false) String name,
                               @RequestParam(required = false) Type type, @RequestParam(required = false) Room room){
        actuatorService.updateActuator(actuatorId, name, type, room);
    }
}
