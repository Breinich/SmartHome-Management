package com.itcatcetc.smarthome;

import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.actuator.ActuatorRepository;
import com.itcatcetc.smarthome.actuator.command.ActuatorCommand;
import com.itcatcetc.smarthome.actuator.command.CommandDataRepository;
import com.itcatcetc.smarthome.login.email.EmailDetails;
import com.itcatcetc.smarthome.login.email.EmailService;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.sensor.SensorRepository;
import com.itcatcetc.smarthome.sensor.data.SensorData;
import com.itcatcetc.smarthome.sensor.data.SensorDataRepository;
import com.itcatcetc.smarthome.type.Type;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * ManagementService for scheduling commands and generating sensor data for simulation
 */
@Component
public class ManagementService {

    private static final boolean SENSOR_SIMULATION = true;
    private final RestTemplate restTemplate;
    private final Random random = new Random();
    @Autowired
    private CommandDataRepository commandDataRepository;
    @Autowired
    private SensorDataRepository sensorDataRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private ActuatorRepository actuatorRepository;
    @Autowired
    private EmailService emailService;

    public ManagementService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Check all commands
     */
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void checkCommands() {
        List<ActuatorCommand> commands = commandDataRepository.findAll();
        for (ActuatorCommand command : commands) {
            if (command.getStartDate().getTime() < System.currentTimeMillis()) {
                //if command has no expiration date
                if (command.getExpirationDate() == null)
                    checkConstraint(command);

                    //if command expired
                else if (command.getExpirationDate().getTime() < System.currentTimeMillis()) {

                    EmailDetails details = new EmailDetails();
                    details.setRecipient(command.getUser().getEmail());
                    details.setSubject(command.getCommandId() + " command expired for " + command.getRoom().getName());
                    details.setMsgBody("Your command with these details has expired:\n" + command + "\n\nBest,\nSmartHome Team");
                    emailService.sendSimpleMail(details);

                    commandDataRepository.delete(command);
                }

                //if command will expire in 5 seconds
                else if (command.getExpirationDate().getTime() + 5000 >= System.currentTimeMillis()) {

                    for (Actuator actuator : actuatorRepository.findAllByRoomAndType(command.getRoom(), command.getConsequenceType())) {
                        sendPost(0, actuator);
                    }
                }
                //if command is still valid
                else
                    checkConstraint(command);
            }
        }
    }

    /**
     * Check if command's constraint is true and in that case execute the command
     * @param command command to be executed
     */
    private void checkConstraint(ActuatorCommand command) {
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

    /**
     * Get average value of all sensors of a given type in a given room
     * @param room room to check
     * @param type type of sensors to check
     * @return average value of all sensors of a given type in a given room
     */
    private int getAvgValue(Room room, Type type) {
        List<Sensor> sensors = sensorRepository.findAllByRoomAndType(room, type);

        int ct = 0;
        Integer avg = 0;

        for (Sensor sensor : sensors) {
            Optional<SensorData> data = sensorDataRepository.findTopBySensorOrderByTimestampDesc(sensor);

            if (data.isPresent()) {
                ct++;
                avg += data.get().getValue();
            }
        }

        if (ct != 0)
            avg /= ct;

        return avg;
    }

    /**
     * Execute command
     * @param command command to be executed
     */
    private void doCommand(ActuatorCommand command) {
        int avg = getAvgValue(command.getRoom(), command.getPremiseType());

        Integer param = 0;

        if (avg < command.getConsequenceValue()) {
            param = 1;
        } else if (avg > command.getConsequenceValue()) {
            param = -1;
        }

        for (Actuator actuator : actuatorRepository.findAllByRoomAndType(command.getRoom(), command.getConsequenceType())) {
            sendPost(param, actuator);
        }
    }

    /**
     * Send POST request to actuator
     * @param param parameter to be sent
     * @param actuator actuator to send the request to
     * @return response from actuator
     */
    private String sendPost(Integer param, Actuator actuator) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Integer> entity = new HttpEntity<>(param, headers);
        try {
            return restTemplate.postForObject(actuator.getApiEndpoint(), entity, String.class);
        } catch (Exception e) {
            return "Actuator is not reachable!";
        }
    }

    /**
     * Generate sensor data for simulation
     */
    @Scheduled(fixedRate = 4000)
    public void simulateSensors() {
        if (!SENSOR_SIMULATION)
            return;

        List<Sensor> sensors = sensorRepository.findAll();

        for (Sensor sensor : sensors) {
            Optional<SensorData> data = sensorDataRepository.findTopBySensorOrderByTimestampDesc(sensor);

            SensorData newData;

            if (data.isPresent()) {
                newData = new SensorData(sensor.getType(), sensor, data.get().getValue() + (random.nextInt(11)) - 5);
                if(sensor.getType().equals(Type.LIGHT)){
                    if(newData.getValue() < 0)
                        newData.setValue(0);
                    else if(newData.getValue() > 100)
                        newData.setValue(100);
                }
                if(sensor.getType().equals(Type.TEMPERATURE)){
                    if(newData.getValue() < -20)
                        newData.setValue(-20);
                    else if(newData.getValue() > 70)
                        newData.setValue(70);
                }
            } else {
                newData = new SensorData(sensor.getType(), sensor, random.nextInt(101));
            }
            sensorDataRepository.save(newData);
        }
    }
}
