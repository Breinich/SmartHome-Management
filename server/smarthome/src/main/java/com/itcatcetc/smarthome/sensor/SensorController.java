package com.itcatcetc.smarthome.sensor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.sensor.data.SensorData;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * a controller class for sensor
 * CRUD operations to reach the database
 * To wrap your repository with a web layer, you must turn to Spring MVC.
 * The @RestController annotation marks the class as a controller where every method returns a domain object instead of a view
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/sensors")
public class SensorController {
    private final SensorService sensorService;

    /**
     * constructor
     * @param sensorService the service that handles the business logic
     */
    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * get all sensors
     * @return a list of sensors in JSON
     */
    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getSensors() {
        List<Sensor> sensors = sensorService.getSensors();
        ObjectMapper objectMapper = new ObjectMapper();
        String res;
        try {
            res = objectMapper.writeValueAsString(sensors);
        } catch (JsonProcessingException e) {
            res = sensors.toString();
        }
        return ResponseEntity.ok(res);
    }


    /**
     * register a new sensor
     * @param sensor the sensor to be registered
     */
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewSensor(@Valid @RequestBody Sensor sensor) {
        try {
            sensorService.addNewSensor(sensor);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * delete a sensor by id
     * @param sensorId
     * only the users with HOMIE role can delete a sensor
     */
    @DeleteMapping(path = "{sensorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteSensor(@Valid @PathVariable("sensorId") Integer sensorId) {
        try{
            sensorService.deleteSensor(sensorId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * update a sensor
     * @param sensor
     * only the users with HOMIE role can update a sensor
     */
    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateSensor(@Valid @RequestBody Sensor sensor) {
        try{
            sensorService.updateSensor(sensor);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * get the sensor's data in a time range (from startTime to endTime)
     * @param sensorId the sensor's id
     * @return a list of sensor data in JSON
     */
    @GetMapping(path = "{sensorId}/data/{startTime}/{endTime}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getSensorDatasInTimeRange(@Valid @PathVariable("sensorId") Integer sensorId,
                                                            @PathVariable("startTime") Long start,
                                                            @PathVariable("endTime") Long end) {
        Date startTime = new Date(start);
        Date endTime = new Date(end);

        List<SensorData> data;
        try{
            data = sensorService.getSensorDatasInTimeRange(sensorId, startTime, endTime);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String res;
        try {
            res = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            res = data.toString();
        }
        return ResponseEntity.ok(res);
    }
}
