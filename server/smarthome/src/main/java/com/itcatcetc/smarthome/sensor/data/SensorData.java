package com.itcatcetc.smarthome.sensor.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.type.Type;
import jakarta.persistence.*;

import java.util.Date;

/**
 * a class for sensor data
 * a sensor data is a record of a sensor in each timestamp
 */
@Entity
@Table
public class SensorData {
    @Temporal(TemporalType.TIMESTAMP)
    private final Date timestamp = new Date(System.currentTimeMillis());
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
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorId", referencedColumnName = "sensorId")
    private Sensor sensor;
    private Integer value;

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

    public void setValue(Integer value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate6Module()
                .enable(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS)
                .enable(Hibernate6Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL));
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "SensorData{" +
                    "id=" + dataId +
                    ", name='" + value +
                    ", timestamp='" + timestamp.toString() +
                    '\'' +
                    '}';
        }
    }


}

