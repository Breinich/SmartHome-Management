package SmartHome.com.smarthome.Sensor;
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
    private Integer id;
    private String name;
    private String room;

    public Sensor() {

    }

    public Sensor(Integer id, String name, String room) {
        this.id = id;
        this.name = name;
        this.room = room;
    }

    public Sensor(String name, String room) {
        this.name = name;
        this.room = room;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", room='" + room + '\'' +
                '}';
    }



}
