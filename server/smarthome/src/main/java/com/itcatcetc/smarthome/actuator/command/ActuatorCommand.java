package com.itcatcetc.smarthome.actuator.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.login.user.User;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.persistence.*;

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
    private Room room;
    @Enumerated(EnumType.STRING)
    private Type premiseType;
    private boolean greaterThan;
    private Integer premiseValue;
    @Enumerated(EnumType.STRING)
    private Type consequenceType;
    private Integer consequenceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user ;

    @Temporal(TemporalType.TIMESTAMP)
    private final Date timestamp = new Date(System.currentTimeMillis());

    private Date expirationDate;



    private Date startDate;

    public ActuatorCommand() {
    }

    public ActuatorCommand(Room room, Type premiseType, boolean greaterThan, Integer premiseValue, Type consequenceType,
                           Integer consequenceValue, User user, Date expirationDate, Date startDate) {
        this.room = room;
        this.premiseType = premiseType;
        this.greaterThan = greaterThan;
        this.premiseValue = premiseValue;
        this.consequenceType = consequenceType;
        this.consequenceValue = consequenceValue;
        this.user = user;
        this.expirationDate = expirationDate;
        this.startDate = startDate;
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

    public Integer getConsequenceValue() {
        return consequenceValue;
    }

    public User getUser() {
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

    public void setConsequenceValue(Integer consequenceValue) {
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

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
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
