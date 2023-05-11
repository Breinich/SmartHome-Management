package com.itcatcetc.smarthome.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.sensor.data.SensorData;
import com.itcatcetc.smarthome.type.Type;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Sensor {
    @Id
    @SequenceGenerator(
            name = "sensor_sequence",
            sequenceName = "sensor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sensor_sequence"
    )
    private Integer sensorId;
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Pattern(regexp = "^(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))/[a-z0-9/]*$")
    private String apiEndpoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", referencedColumnName = "roomId")
    private Room room;

    @OneToMany(mappedBy="sensor", cascade = CascadeType.ALL)
    private List<SensorData> sensorDatas;

    public Sensor() {

    }

    public Sensor(String name, Type type, Room room, String apiEndpoint) {
        this.name = name;
        this.type = type;
        this.room = room;
        this.apiEndpoint = apiEndpoint;
        this.sensorDatas = new ArrayList<>();
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }


    public void addSensorData(SensorData sensorData){
        sensorDatas.add(sensorData);
    }


    public List<SensorData> getSensorDatas() {
        return sensorDatas;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "Sensor{" +
                    "id=" + sensorId +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
