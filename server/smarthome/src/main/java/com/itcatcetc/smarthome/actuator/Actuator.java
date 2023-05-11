package com.itcatcetc.smarthome.actuator;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table
public class Actuator {
    @Id
    @SequenceGenerator(
            name = "actuator_sequence",
            sequenceName = "actuator_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "actuator_sequence"
    )
    private Integer actuatorId;
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Pattern(regexp = "^https://(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))[a-z0-9/]*$")
    private String apiEndpoint;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", referencedColumnName = "roomId")
    private Room room;

    public Actuator() {

    }

    public Actuator(String name, Type type, Room room, String apiEndpoint) {
        this.name = name;
        this.type = type;
        this.room = room;
        this.apiEndpoint = apiEndpoint;
    }

    public Integer getActuatorId() {
        return actuatorId;
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

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Sensor{" +
                    "id=" + actuatorId +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
