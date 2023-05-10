package SmartHome.com.smarthome.Actuator;

import SmartHome.com.smarthome.Room.Room;
import SmartHome.com.smarthome.Type.Type;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Actuator> getActuators(){
        return actuatorService.getActuators();
    }

    @PostMapping
    public void registerNewActuator(@RequestBody  Actuator actuator){
        actuatorService.addNewActuator(actuator);
    }

    @DeleteMapping(path = "{actuatorId}")
    public void deleteActuator(@PathVariable("actuatorId") Integer actuatorId){
        actuatorService.deleteActuator(actuatorId);
    }

    @PutMapping(path = "{actuatorId}")
    public void updateActuator(@PathVariable("actuatorId") Integer actuatorId, @RequestParam(required = false) String name,
                             @RequestParam(required = false) Type type, @RequestParam(required = false) Room room){
        actuatorService.updateActuator(actuatorId, name, type, room);
    }
}
