package com.itcatcetc.smarthome.sensor.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * a controller class for sensor data
 * CRUD operations to reach the database
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/sensordata")
public class SensorDataController {
    private final SensorDataService sensorDataService;

    /**
     * constructor
     * @param sensorDataService
     */
    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    /**
     * get all sensor data
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
     */
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewData(@Valid @RequestBody SensorData sensorData) {
        sensorDataService.addNewData(sensorData);
    }

    /**
     * delete a sensor data by id
     * @param dataId
     */
    @DeleteMapping(path = "{dataId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteData(@Valid @PathVariable("dataId") Integer dataId) {
        sensorDataService.deleteData(dataId);
    }
}
