package com.itcatcetc.smarthome.actuator;


import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.persistence.*;

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
    private String name;
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", referencedColumnName = "roomId")
    private Room room;

    /*@OneToMany(mappedBy="sensor", cascade = CascadeType.ALL)
    private List<SensorData> sensorDatas; //majd actuatorcommand lesz*/

    public Actuator() {

    }

    public Actuator(String name, Type type, Room room) {
        this.name = name;
        this.type = type;
        this.room = room;
        //this.sensorDatas = new ArrayList<>();
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

   /* public void addSensorData(SensorData sensorData){
        sensorDatas.add(sensorData);
    }*/

    public String toString() {
        return "Sensor{" +
                "id=" + actuatorId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

   /* public List<SensorData> getSensorDatas() {
        return sensorDatas;
    }*/



}
