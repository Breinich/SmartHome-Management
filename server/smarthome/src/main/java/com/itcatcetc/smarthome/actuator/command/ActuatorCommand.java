package com.itcatcetc.smarthome.actuator.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.login.user.User;
import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.type.Type;
import jakarta.persistence.*;

import java.util.Date;

public class ActuatorCommand {
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
    private Type premiseType;
    private Integer premiseValue;

    private Type consequenceType;
    private Integer consequenceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user ;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date(System.currentTimeMillis());

    private Date expirationDate;

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "ActuatorCommand{" +
                    "dataId=" + dataId +
                    ", premiseType=" + premiseType +
                    ", premiseValue=" + premiseValue +
                    ", consequenceType=" + consequenceType +
                    ", consequenceValue=" + consequenceValue +
                    ", user=" + user +
                    ", timestamp=" + timestamp +
                    ", expirationDate=" + expirationDate +
                    '}';
        }
    }
}
