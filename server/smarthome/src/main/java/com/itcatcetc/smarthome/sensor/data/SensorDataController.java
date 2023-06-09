package com.itcatcetc.smarthome.sensor.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * a controller class for sensor data
 * CRUD operations to reach the database
 * To wrap your repository with a web layer, you must turn to Spring MVC.
 * The @RestController annotation marks the class as a controller where every method returns a domain object instead of a view
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/sensordata")
public class SensorDataController {
    private final SensorDataService sensorDataService;

    /**
     * constructor
     * @param sensorDataService the service that handles the business logic
     */
    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    /**
     * get the latest data of each sensor
     * @return a list of sensor data in JSON
     */
    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getData() {
        List<SensorData> data = sensorDataService.getData();
        String res;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            res = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            res = data.toString();
        }
        return ResponseEntity.ok(res);
    }

    /**
     * register a new sensor data
     * @param sensorData
     * only the users with HOMIE role can add a new sensor data
     */
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewData(@Valid @RequestBody SensorData sensorData) {
        try{
            sensorDataService.addNewData(sensorData);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * delete a sensor data by id
     * @param dataId
     * only the users with HOMIE role can delete a sensor data
     */
    @DeleteMapping(path = "{dataId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteData(@Valid @PathVariable("dataId") Integer dataId) {
        try {
            sensorDataService.deleteData(dataId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * get the latest data of a sensor by sensor id
     * @param sensorId the id of the sensor
     * @return a list of sensor data in JSON
     */
    @GetMapping(path = "sensor/{sensorId}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getLatestDataBySensorId(@PathVariable("sensorId") Integer sensorId) {
        SensorData data = sensorDataService.getLatestDataBySensorId(sensorId);

        String res;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            res = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            res = data.toString();
        }
        return ResponseEntity.ok(res);
    }

}
