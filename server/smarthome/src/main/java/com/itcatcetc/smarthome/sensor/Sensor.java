package com.itcatcetc.smarthome.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.sensor.data.SensorData;
import com.itcatcetc.smarthome.type.Type;

import jakarta.persistence.*;
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
    private String name;
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", referencedColumnName = "roomId")
    private Room room;

    @OneToMany(mappedBy="sensor", cascade = CascadeType.ALL)
    private List<SensorData> sensorDatas;

    public Sensor() {

    }

    public Sensor(String name, Type type, Room room) {
        this.name = name;
        this.type = type;
        this.room = room;
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

    public void addSensorData(SensorData sensorData){
        sensorDatas.add(sensorData);
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

    public List<SensorData> getSensorDatas() {
        return sensorDatas;
    }



}
