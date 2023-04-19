package SmartHome.com.smarthome.Sensor;
import SmartHome.com.smarthome.Room.Room;
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
    private String type;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "roomId", referencedColumnName = "roomId")
    private Room room;

    public Sensor() {

    }

    public Sensor(Integer sensorId, String name, String type) {
        this.sensorId = sensorId;
        this.name = name;
        this.type = type;
    }

    public Sensor(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer id) {
        this.sensorId = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String room) {
        this.type = room;
    }

    public String toString() {
        return "Sensor{" +
                "id=" + sensorId +
                ", name='" + name + '\'' +
                ", room='" + type + '\'' +
                '}';
    }



}
