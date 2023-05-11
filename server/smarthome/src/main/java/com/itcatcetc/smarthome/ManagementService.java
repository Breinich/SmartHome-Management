package com.itcatcetc.smarthome;

import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.actuator.ActuatorRepository;
import com.itcatcetc.smarthome.actuator.command.ActuatorCommand;
import com.itcatcetc.smarthome.actuator.command.CommandDataRepository;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.sensor.SensorRepository;
import com.itcatcetc.smarthome.sensor.data.SensorData;
import com.itcatcetc.smarthome.sensor.data.SensorDataRepository;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class ManagementService {

    @Autowired
    private CommandDataRepository commandDataRepository;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private ActuatorRepository actuatorRepository;

    private final RestTemplate restTemplate;

    private final Random random = new Random();

    public ManagementService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    @Scheduled(fixedRate = 5000)
    public void checkCommands(){
        List<ActuatorCommand> commands = commandDataRepository.findAll();
        for (ActuatorCommand command : commands) {
            if(command.getStartDate().getTime() < System.currentTimeMillis()){

                //if command expired
                if(command.getExpirationDate().getTime() < System.currentTimeMillis()){
                    commandDataRepository.delete(command);
                    continue;
                }

                //if command will expire in 5 seconds
                if(command.getExpirationDate().getTime() + 5000 >= System.currentTimeMillis()){

                    for(Actuator actuator : actuatorRepository.findAllByRoomAndType(command.getRoom(), command.getConsequenceType())) {
                        sendPost(0, actuator);
                    }
                }
                else {

                    if (command.getPremiseType() == Type.NONE) {
                        doCommand(command);
                    } else {
                        int avg = getAvgValue(command.getRoom(), command.getPremiseType());

                        if ((command.isGreaterThan() && avg > command.getConsequenceValue()) ||
                                (!command.isGreaterThan() && avg < command.getConsequenceValue())) {
                            doCommand(command);
                        }
                    }
                }
            }
        }
    }

    private int getAvgValue(Room room, Type type){
        List<Sensor> sensors = sensorRepository.findAllByRoomAndType(room, type);

        int ct = 0;
        Integer avg = 0;

        for (Sensor sensor : sensors) {
            Optional<SensorData> data = sensorDataRepository.findTopBySensorOrderByTimestampDesc(sensor);

            if(data.isPresent()){
                ct++;
                avg += data.get().getValue();
            }
        }

        if(ct != 0)
            avg /= ct;

        return avg;
    }

    private void doCommand(ActuatorCommand command){
        int avg = getAvgValue(command.getRoom(), command.getPremiseType());

        Integer param = 0;

        if (avg < command.getConsequenceValue()) {
            param = 1;
        }
        else if (avg > command.getConsequenceValue()) {
            param = -1;
        }

        for(Actuator actuator : actuatorRepository.findAllByRoomAndType(command.getRoom(), command.getConsequenceType())) {
            sendPost(param, actuator);
        }
    }

    private String sendPost(Integer param, Actuator actuator){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Integer> entity = new HttpEntity<>(param, headers);

        return restTemplate.postForObject(actuator.getApiEndpoint(),entity, String.class);
    }

    @Scheduled(fixedRate = 2000)
    public void simulateSensors(){
        List<Sensor> sensors = sensorRepository.findAll();

        for (Sensor sensor : sensors) {
            Optional<SensorData> data = sensorDataRepository.findTopBySensorOrderByTimestampDesc(sensor);

            SensorData newData;

            if(data.isPresent()){
                newData = new SensorData(sensor.getType(), sensor, data.get().getValue() + (random.nextInt(10)) - 5);
            }
            else{
                newData = new SensorData(sensor.getType(), sensor, random.nextInt(100));
            }
            sensorDataRepository.save(newData);
        }
    }
}
