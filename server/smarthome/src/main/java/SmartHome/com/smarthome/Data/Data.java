package SmartHome.com.smarthome.Data;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class Data {
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

    private Timestamp timestamp;
    private Integer value;


    public Data() {

    }

    public Data(Integer dataId, Integer value, Timestamp timestamp) {
        this.dataId = dataId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Data(Integer value, Timestamp timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
    public Data(Integer value) {
        this.value = value;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer id) {
        this.dataId = id;
    }

    public Integer getValue() {
        return value;
    }


    public void setValue(Integer value) {
        this.value = value;
    }

    public String toString() {
        return "Data{" +
                "id=" + dataId +
                ", name='" + value +
                "timestamp='" + timestamp.toString() +
                '\'' +
                '}';
    }



}
