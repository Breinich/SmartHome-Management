package com.itcatcetc.smarthome.actuator;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class ActuatorService {

    /**
     * ActuatorRepository is an interface that extends JpaRepository
     */
    private final ActuatorRepository actuatorRepository;


    /**
     * Constructor
     * @param actuatorRepository
     *
     */
    @Autowired
    public ActuatorService(ActuatorRepository actuatorRepository) {
        this.actuatorRepository = actuatorRepository;
    }

    /**
     *
     * @return List of actuators
     */

    public List<Actuator> getActuators() {
        return actuatorRepository.findAll();
    }

    public void addNewActuator(Actuator actuator) {
        Optional<Actuator> actuatorOptional = actuatorRepository.findByName(actuator.getName());
        if (actuatorOptional.isPresent()) {
            throw new IllegalArgumentException("name taken");
        }

        actuatorRepository.save(actuator);

    }

    public void deleteActuator(Integer actuatorId) {
        boolean exists = actuatorRepository.existsById(actuatorId);
        if (!exists) {
            throw new IllegalArgumentException("actuator with id " + actuatorId + " does not exists");
        }
        actuatorRepository.deleteById(actuatorId);
    }


    @Transactional
    public void updateActuator(Actuator newActuator) {
        Actuator actuator = actuatorRepository.findById(newActuator.getActuatorId()).orElseThrow(() -> new IllegalArgumentException("actuator with id " + newActuator.getActuatorId() + " does not exists"));

        if (newActuator.getName() != null && newActuator.getName().length() > 0 && !Objects.equals(actuator.getName(), newActuator.getName())) {
            Optional<Actuator> actuatorOptional = actuatorRepository.findByName(newActuator.getName());
            if (actuatorOptional.isPresent()) {
                throw new IllegalArgumentException("name taken");
            }
            actuator.setName(newActuator.getName());
        }

        if (newActuator.getType() != null && !Objects.equals(actuator.getType(), newActuator.getType())) {
            actuator.setType(newActuator.getType());
        }

        if (newActuator.getRoom() != null && !Objects.equals(actuator.getRoom(), newActuator.getRoom())) {
            actuator.setRoom(newActuator.getRoom());
        }

        if (newActuator.getApiEndpoint() != null && newActuator.getApiEndpoint().length() > 0 && !Objects.equals(actuator.getApiEndpoint(), newActuator.getApiEndpoint())) {
            actuator.setApiEndpoint(newActuator.getApiEndpoint());
        }

    }
}
