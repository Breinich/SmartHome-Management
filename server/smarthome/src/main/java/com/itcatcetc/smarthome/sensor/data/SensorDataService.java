package com.itcatcetc.smarthome.sensor.data;

import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.sensor.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    public SensorData getLatestDataBySensorId(Integer sensorId) {
        Optional<SensorData> dataOptional =  sensorDataRepository.findTopBySensor_SensorIdOrderByTimestampDesc(sensorId);
        if (dataOptional.isPresent()) {
            return dataOptional.get();
        } else {
            return null;
        }
    }
}
