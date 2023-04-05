package SmartHome.com.smarthome.Room;
import jakarta.persistence.*;

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


    public Room() {

    }

    public Room(Integer roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public Room(String name) {
        this.name = name;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer id) {
        this.roomId = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Room{" +
                "id=" + roomId +
                ", name='" + name + '\'' +
                '}';
    }



}
