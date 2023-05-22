package com.itcatcetc.smarthome.sensor;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> getSensors(){
        return sensorRepository.findAll();
    }

    public void addNewSensor(Sensor sensor) {
       Optional<Sensor> sensorOptional= sensorRepository.findByName(sensor.getName());
       if(sensorOptional.isPresent()) {
                    throw new IllegalArgumentException("name taken");
       }

       sensorRepository.save(sensor);

    }

    public void deleteSensor(Integer sensorId) {
        boolean exists = sensorRepository.existsById(sensorId);
        if(!exists){
            throw new IllegalArgumentException("sensor with id " + sensorId + " does not exists");
        }
        sensorRepository.deleteById(sensorId);
    }


    @Transactional
    public void updateSensor(Sensor newSensor) {
        Sensor sensor = sensorRepository.findById(newSensor.getSensorId()).orElseThrow(() -> new IllegalArgumentException("sensor with id " + newSensor.getSensorId() + " does not exists"));

        if(newSensor.getName() != null && newSensor.getName().length() > 0 && !Objects.equals(sensor.getName(), newSensor.getName())){
            Optional<Sensor> sensorOptional= sensorRepository.findByName(newSensor.getName());
            if(sensorOptional.isPresent()) {
                throw new IllegalArgumentException("name taken");
            }
            sensor.setName(newSensor.getName());
        }

        if(newSensor.getType() != null && !Objects.equals(sensor.getType(), newSensor.getType())){
            sensor.setType(newSensor.getType());
        }

        if(newSensor.getRoom() != null && !Objects.equals(sensor.getRoom(), newSensor.getRoom())){
            sensor.setRoom(newSensor.getRoom());
        }

        if(newSensor.getApiEndpoint() != null && newSensor.getApiEndpoint().length() > 0 && !Objects.equals(sensor.getApiEndpoint(), newSensor.getApiEndpoint())){
            sensor.setApiEndpoint(newSensor.getApiEndpoint());
        }
    }

    public void getSensorDatasInTimeRange(Integer sensorId, Timestamp startTime, Timestamp endTime) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new IllegalArgumentException("sensor with id " + sensorId + " does not exists"));
        sensor.getSensorDatasInTimeRange(startTime, endTime);
    }
}
