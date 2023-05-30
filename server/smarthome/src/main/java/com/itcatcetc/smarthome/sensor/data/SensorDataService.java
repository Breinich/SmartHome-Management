package com.itcatcetc.smarthome.sensor.data;

import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.sensor.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SensorDataService
 * specifying the business logic
 */
@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;

    /**
     * constructor
     * @param sensorDataRepository autowired by Spring
     */
    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository, SensorRepository sensorRepository){
        this.sensorDataRepository = sensorDataRepository;
        this.sensorRepository = sensorRepository;
    }

    /**
     * get the list of sensor data, the latest data of each sensor
     */
    public List<SensorData> getData() {
        ArrayList<SensorData> res = new ArrayList<>();
        List<Sensor> sensors = sensorRepository.findAll();
        for (Sensor sensor : sensors) {
            Optional<SensorData> data = sensorDataRepository.findTopBySensorOrderByTimestampDesc(sensor);
            data.ifPresent(res::add);
        }

        return res;
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

    public List<SensorData> getLatestDataBySensorId(Integer sensorId) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(sensorId);
        if (sensorOptional.isEmpty()) {
            throw new IllegalArgumentException("Sensor with id " + sensorId + " does not exists");
        }

        Sensor sensor = sensorOptional.get();
        Optional<SensorData> dataOptional = sensorDataRepository.findTopBySensorOrderByTimestampDesc(sensor);
        if (dataOptional.isEmpty()) {
            throw new IllegalArgumentException("No data for sensor with id " + sensorId);
        }

        SensorData data = dataOptional.get();
        return sensorDataRepository.findAllBySensorAndTimestamp(data.getSensor(), data.getTimestamp());
    }
}
