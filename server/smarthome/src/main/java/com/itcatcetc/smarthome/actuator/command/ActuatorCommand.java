package com.itcatcetc.smarthome.actuator.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.login.user.User;
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
    private Integer dataId;

    private Type premiseType;
    private Integer premiseValue;

    private Type consequenceType;
    private Integer consequenceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user ;

    @Temporal(TemporalType.TIMESTAMP)
    private final Date timestamp = new Date(System.currentTimeMillis());

    private Date expirationDate;

    public ActuatorCommand() {
    }

    public ActuatorCommand(Type premiseType, Integer premiseValue, Type consequenceType, Integer consequenceValue, User user, Date expirationDate) {
        this.premiseType = premiseType;
        this.premiseValue = premiseValue;
        this.consequenceType = consequenceType;
        this.consequenceValue = consequenceValue;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    public Integer getDataId() {
        return dataId;
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
