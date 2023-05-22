package com.itcatcetc.smarthome.actuator.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.itcatcetc.smarthome.login.user.User;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table
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
    private Integer commandId;

    @ManyToOne
    @NotNull
    private Room room;
    @Enumerated(EnumType.STRING)
    private Type premiseType;
    // true: premise is true, if the average value of the sensor data is greater than the premiseValue
    // false: premise is true, if the average value of the sensor data is less than the premiseValue
    private boolean greaterThan;
    private Integer premiseValue;
    @Enumerated(EnumType.STRING)
    private Type consequenceType;
    @NotNull
    private Integer consequenceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @NotNull
    private User user ;

    @Temporal(TemporalType.TIMESTAMP)
    private final Date timestamp = new Date(System.currentTimeMillis());

    private Date expirationDate;



    private Date startDate;

    public ActuatorCommand() {
    }

    public Integer getCommandId() {
        return commandId;
    }

    public Type getPremiseType() {
        return premiseType;
    }

    public Integer getPremiseValue() {
        return premiseValue;
    }

    public Type getConsequenceType() {
        return consequenceType;
    }

    public @NotNull Integer getConsequenceValue() {
        return consequenceValue;
    }

    public @NotNull User getUser() {
        return user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setPremiseType(Type premiseType) {
        this.premiseType = premiseType;
    }

    public void setPremiseValue(Integer premiseValue) {
        this.premiseValue = premiseValue;
    }

    public void setConsequenceType(Type consequenceType) {
        this.consequenceType = consequenceType;
    }

    public void setConsequenceValue(@NotNull Integer consequenceValue) {
        this.consequenceValue = consequenceValue;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(boolean greaterThan) {
        this.greaterThan = greaterThan;
    }

    public @NotNull Room getRoom() {
        return room;
    }
    public void setRoom(@NotNull Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate6Module()
                .enable(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS)
                .enable(Hibernate6Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL));
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "ActuatorCommand{" +
                    "dataId=" + commandId +
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
