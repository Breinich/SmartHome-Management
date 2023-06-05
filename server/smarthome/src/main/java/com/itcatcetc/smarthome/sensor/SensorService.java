package com.itcatcetc.smarthome.sensor;


import com.itcatcetc.smarthome.sensor.data.SensorData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * a service class for sensor
 * interface responsible for business logic
 */
@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class SensorService {

    private final SensorRepository sensorRepository;

    /**
     * constructor
     * @param sensorRepository autowired by Spring
     */
    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * get all sensors
     * @return List<Sensor>
     */
    public List<Sensor> getSensors() {
        return sensorRepository.findAll();
    }

    /**
     * add new sensor
     * can't add a sensor with the same name
     * @param sensor the sensor to add
     */
    public void addNewSensor(Sensor sensor) {
        Optional<Sensor> sensorOptional = sensorRepository.findByName(sensor.getName());
        if (sensorOptional.isPresent()) {
            throw new IllegalArgumentException("name taken");
        }

        sensorRepository.save(sensor);

    }

    /**
     * delete a sensor by id
     * @param sensorId the id of the sensor to delete
     */
    public void deleteSensor(Integer sensorId) {
        boolean exists = sensorRepository.existsById(sensorId);
        if (!exists) {
            throw new IllegalArgumentException("sensor with id " + sensorId + " does not exists");
        }
        sensorRepository.deleteById(sensorId);
    }


    /**
     * update a sensor by id
     * @param newSensor the new sensor data
     */
    @Transactional
    public void updateSensor(Sensor newSensor) {
        Sensor sensor = sensorRepository.findById(newSensor.getSensorId()).orElseThrow(() -> new IllegalArgumentException("sensor with id " + newSensor.getSensorId() + " does not exists"));

        if (newSensor.getName() != null && newSensor.getName().length() > 0 && !Objects.equals(sensor.getName(), newSensor.getName())) {
            Optional<Sensor> sensorOptional = sensorRepository.findByName(newSensor.getName());
            if (sensorOptional.isPresent()) {
                throw new IllegalArgumentException("name taken");
            }
            sensor.setName(newSensor.getName());
        }

        if (newSensor.getType() != null && !Objects.equals(sensor.getType(), newSensor.getType())) {
            sensor.setType(newSensor.getType());
        }

        if (newSensor.getRoom() != null && !Objects.equals(sensor.getRoom(), newSensor.getRoom())) {
            sensor.setRoom(newSensor.getRoom());
        }

        if (newSensor.getApiEndpoint() != null && newSensor.getApiEndpoint().length() > 0 && !Objects.equals(sensor.getApiEndpoint(), newSensor.getApiEndpoint())) {
            sensor.setApiEndpoint(newSensor.getApiEndpoint());
        }
    }

    /**
     * get all sensor datas of a sensor
     * @param sensorId the id of the sensor
     * @param startTime the start time
     * @param endTime the end time
     * @return List<SensorData>
     */
    public List<SensorData> getSensorDatasInTimeRange(Integer sensorId, Date startTime, Date endTime) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new IllegalArgumentException("sensor with id " + sensorId + " does not exists"));
        return sensor.getSensorDatasInTimeRange(startTime, endTime);
    }
}
