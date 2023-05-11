package com.itcatcetc.smarthome.actuator;

import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ActuatorService {

    private final ActuatorRepository actuatorRepository;

    @Autowired
    public ActuatorService(ActuatorRepository actuatorRepository) {
        this.actuatorRepository = actuatorRepository;
    }

    public List<Actuator> getActuators(){
        return actuatorRepository.findAll();
    }

    public void addNewActuator(Actuator actuator) {
        Optional<Actuator> actuatorOptional= actuatorRepository.findActuatorByName(actuator.getName());
        if(actuatorOptional.isPresent()) {
            throw new IllegalStateException("name taken");
        }

        actuatorRepository.save(actuator);

    }

    public void deleteActuator(Integer actuatorId) {
        boolean exists = actuatorRepository.existsById(actuatorId);
        if(!exists){
            throw new IllegalStateException("actuator with id " + actuatorId + " does not exists");
        }
        actuatorRepository.deleteById(actuatorId);
    }


    @Transactional
    public void updateActuator(Integer actuatorId, String name, Type type, Room room) {
        Actuator actuator = actuatorRepository.findById(actuatorId).orElseThrow(() -> new IllegalStateException("actuator with id " + actuatorId + " does not exists"));

        if(name!= null && name.length() > 0 && !Objects.equals(actuator.getName(), name)){
            Optional<Actuator> actuatorOptional= actuatorRepository.findActuatorByName(name);
            if(actuatorOptional.isPresent()) {
                throw new IllegalStateException("name taken");
            }
            actuator.setName(name);
        }

        if(type!= null && !Objects.equals(actuator.getType(), type)){
            actuator.setType(type);

        }

    }
}
