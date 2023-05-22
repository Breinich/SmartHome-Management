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

@RestController
@RequestMapping(path = "api/v1/smarthome/sensors")
public class SensorController {
    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

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

    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewSensor(@Valid @RequestBody Sensor sensor) {
        sensorService.addNewSensor(sensor);
    }

    @DeleteMapping(path = "{sensorId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteSensor(@Valid @PathVariable("sensorId") Integer sensorId) {
        sensorService.deleteSensor(sensorId);
    }

    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateSensor(@Valid @RequestBody Sensor sensor) {
        sensorService.updateSensor(sensor);
    }

    @GetMapping(path = "{sensorId}/data/{startTime}/{endTime}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getSensorDatasInTimeRange(@Valid @PathVariable("sensorId") Integer sensorId,
                                                            @PathVariable("startTime") Long start,
                                                            @PathVariable("endTime") Long end) {
        Date startTime = new Date(start);
        Date endTime = new Date(end);

        List<SensorData> data = sensorService.getSensorDatasInTimeRange(sensorId, startTime, endTime);

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
