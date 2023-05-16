package com.itcatcetc.smarthome.sensor.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public List<SensorData> getData(){
        return sensorDataRepository.findAll();
    }

    public void addNewData(SensorData sensorData) {
        if(sensorData.getDataId() != null){
            throw new IllegalArgumentException("dataId must be null");
        }

        sensorDataRepository.save(sensorData);
    }

    public void deleteData(Integer dataId) {
        boolean exists = sensorDataRepository.existsById(dataId);
        if(!exists){
            throw new IllegalArgumentException("Data with id " + dataId + " does not exists");
        }
        sensorDataRepository.deleteById(dataId);
    }
}
