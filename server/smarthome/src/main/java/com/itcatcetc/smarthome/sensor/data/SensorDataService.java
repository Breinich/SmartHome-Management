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
        Optional<SensorData> dataOptional= sensorDataRepository.findById(sensorData.getDataId());
        if(dataOptional.isPresent()) {
            throw new IllegalStateException("id taken");
        }

        sensorDataRepository.save(sensorData);
    }

    public void deleteData(Integer dataId) {
        boolean exists = sensorDataRepository.existsById(dataId);
        if(!exists){
            throw new IllegalStateException("Data with id " + dataId + " does not exists");
        }
        sensorDataRepository.deleteById(dataId);
    }
}
