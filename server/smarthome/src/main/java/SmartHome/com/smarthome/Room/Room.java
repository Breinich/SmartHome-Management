package SmartHome.com.smarthome.Room;

import SmartHome.com.smarthome.Actuator.Actuator;
import SmartHome.com.smarthome.Sensor.Sensor;
import jakarta.persistence.*;

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
    private String name;

    @OneToMany(mappedBy="room", cascade = CascadeType.ALL)
    private List<Sensor> sensors;

    @OneToMany(mappedBy="room", cascade = CascadeType.ALL)
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

    public void addSensor(Sensor sensor){
        sensors.add(sensor);
    }
    public void addActuator(Actuator actuator){
        actuators.add(actuator);
    }

    public String toString() {
     return "Room{" +
                "id=" + roomId +
                ", name='" + name + '\'' +
                '}';
    }


    public List<Sensor> getSensors() {
        return sensors;
    }
    public List<Actuator> getActuators() {
        return actuators;
    }
}
