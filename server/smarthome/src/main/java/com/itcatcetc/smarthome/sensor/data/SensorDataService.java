package com.itcatcetc.smarthome.sensor.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SensorDataService
 * specifying the business logic
 */
@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    /**
     * constructor
     * @param sensorDataRepository autowired by Spring
     */
    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    /**
     * get the list of sensor data
     */
    public List<SensorData> getData() {
        return sensorDataRepository.findAll();
    }

    /**
     * add new sensor data
     * @param sensorData new sensor data
     */
    public void addNewData(SensorData sensorData) {
        if (sensorData.getDataId() != null) {
            throw new IllegalArgumentException("dataId must be null");
        }

        sensorDataRepository.save(sensorData);
    }

    /**
     * delete sensor data
     * @param dataId the id of the data to delete
     */
    public void deleteData(Integer dataId) {
        boolean exists = sensorDataRepository.existsById(dataId);
        if (!exists) {
            throw new IllegalArgumentException("Data with id " + dataId + " does not exists");
        }
        sensorDataRepository.deleteById(dataId);
    }
}
