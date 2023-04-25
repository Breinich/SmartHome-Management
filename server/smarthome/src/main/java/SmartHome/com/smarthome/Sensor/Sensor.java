package SmartHome.com.smarthome.Sensor;
import SmartHome.com.smarthome.Room.Room;
import SmartHome.com.smarthome.Type.Type;
import jakarta.persistence.*;

@Entity
@Table
public class Sensor {
    @Id
    @SequenceGenerator(
            name = "sensor_sequence",
            sequenceName = "sensor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sensor_sequence"
    )
    private Integer sensorId;
    private String name;
    private Type type;

    //Amíg a room nem működik, addig kikommentezve
   ///*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", referencedColumnName = "roomId")
    private Room room;
    //*/

    public Sensor() {

    }

    public Sensor(String name, Type type, Room room) {
        this.name = name;
        this.type = type;
        this.room = room;
    }

    public Integer getSensorId() {
        return sensorId;
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

    public String toString() {
        return "Sensor{" +
                "id=" + sensorId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }



}
