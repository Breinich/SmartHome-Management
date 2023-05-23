package com.itcatcetc.smarthome.room;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.sensor.Sensor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Room {
    @Id
    @SequenceGenerator(
            name = "room_sequence",
            sequenceName = "room_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "room_sequence"
    )
    private Integer roomId;
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String name;
    @Pattern(regexp ="^(living_room.jpg|bedroom.jpg|lobby.jpg|kitchen.jpg|pantry.jpg|bathroom.jpg|toilet.jpg|garage.jpg)$")
    private String coverPhoto;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sensor> sensors;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Actuator> actuators;

    public Room() {

    }

    public Room(String name) {
        this.name = name;
        this.sensors = new ArrayList<>();
        this.actuators = new ArrayList<>();
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate6Module()
                .enable(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS)
                .enable(Hibernate6Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL));
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "Room{" +
                    "id=" + roomId +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
