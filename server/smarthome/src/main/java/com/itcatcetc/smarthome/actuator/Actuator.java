package com.itcatcetc.smarthome.actuator;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

// Actuator entity
// Actuators are smarthome devices that can be controlled by the user
// They can be used to control lights, blinds, etc.


@Entity
@Table
public class Actuator {
    // They have an id, a name, a type and an API endpoint
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
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$")
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;

    // The API endpoint is used to control the actuator
    // The API endpoint is a URL that can be used to send commands to the actuator
    @Pattern(regexp = "^https://(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))\\.(\\d|[1-9]\\d|1\\d\\d|2([0-4]\\d|5[0-5]))/[a-z0-9/]*$")
    private String apiEndpoint;

    // They are located in a room
    //In room can be multiple actuators
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getActuatorId() {
        return actuatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
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
        mapper.registerModule(new Hibernate6Module()
                .enable(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS)
                .enable(Hibernate6Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL));
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
