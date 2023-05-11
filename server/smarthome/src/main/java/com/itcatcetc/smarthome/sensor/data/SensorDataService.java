package com.itcatcetc.smarthome.sensor.data;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public List<SensorData> getDatas(){
        return sensorDataRepository.findAll();
    }

    public void addNewData(SensorData sensorData) {
       /* Optional<Data> dataOptional= dataRepository.findDataById(data.getDataId());
        if(dataOptional.isPresent()) {
            throw new IllegalStateException("id taken");
        }*/

        sensorDataRepository.save(sensorData);

    }

    public void deleteData(Integer dataId) {
        boolean exists = sensorDataRepository.existsById(dataId);
        if(!exists){
            throw new IllegalStateException("Data with id " + dataId + " does not exists");
        }
        sensorDataRepository.deleteById(dataId);
    }


    @Transactional
    public void updateData(Integer dataId) {
        SensorData sensorData = sensorDataRepository.findById(dataId).orElseThrow(() -> new IllegalStateException("data with id " + dataId + " does not exists"));

    }
}
