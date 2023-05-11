package com.itcatcetc.smarthome.sensor.data;

import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.type.Type;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table
public class SensorData {
    @Id
    @SequenceGenerator(
            name = "data_sequence",
            sequenceName = "data_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "data_sequence"
    )
    private Integer dataId;
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorId", referencedColumnName = "sensorId")
    private Sensor sensor;
    private Integer value;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date(System.currentTimeMillis());

    public SensorData() {
    }

    public SensorData(Type type, Sensor sensor, Integer value) {
        this.type = type;
        this.sensor = sensor;
        this.value = value;
    }

    public Integer getDataId() {
        return dataId;
    }

    public Integer getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }
    public void setValue(Integer value) {
        this.value = value;
    }

    public String toString() {
        return "SensorData{" +
                "id=" + dataId +
                ", name='" + value +
                ", timestamp='" + timestamp.toString() +
                '\'' +
                '}';
    }



}

