package com.itcatcetc.smarthome.sensor;



import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
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
                    throw new IllegalStateException("name taken");
       }

       sensorRepository.save(sensor);

    }

    public void deleteSensor(Integer sensorId) {
        boolean exists = sensorRepository.existsById(sensorId);
        if(!exists){
            throw new IllegalStateException("sensor with id " + sensorId + " does not exists");
        }
        sensorRepository.deleteById(sensorId);
    }


    @Transactional
    public void updateSensor(Integer sensorId, String name, Type type, Room room, String apiUrl) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new IllegalStateException("sensor with id " + sensorId + " does not exists"));

        if(name!= null && name.length() > 0 && !Objects.equals(sensor.getName(), name)){
            Optional<Sensor> sensorOptional= sensorRepository.findByName(name);
            if(sensorOptional.isPresent()) {
                throw new IllegalStateException("name taken");
            }
            sensor.setName(name);
        }

        if(type!= null && !Objects.equals(sensor.getType(), type)){
            sensor.setType(type);
        }

        if(room!= null && !Objects.equals(sensor.getRoom(), room)){
            sensor.setRoom(room);
        }

        if(apiUrl!= null && apiUrl.length() > 0 && !Objects.equals(sensor.getApiEndpoint(), apiUrl)){
            sensor.setApiEndpoint(apiUrl);
        }
    }
}
