package com.itcatcetc.smarthome.sensor;

import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceTest {

    private SensorRepository repository;
    private SensorController controller;
    private Sensor sensor;
    private Room room;

    /**
     * Set up the test by mocking the repository and creating a controller and  a sensor
     * The sensor's id is set to 1 with reflection, because it is not set by the database
     */
    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        repository = Mockito.mock(SensorRepository.class);
        SensorService service = new SensorService(repository);
        controller = new SensorController(service);

        room = new Room("Living Room");
        sensor = new Sensor(
                "Lamp",
                Type.TEMPERATURE,
                room,
                "");
        //room.addSensor(sensor);

        Field f1 = sensor.getClass().getDeclaredField("sensorId");
        f1.setAccessible(true);
        f1.set(sensor, 1);
    }

    /**
     * Test adding a new sensor
     */
    @Test
    public void testAddNewSensor() {
        Mockito.when(repository.findSensorByName(sensor.getName())).thenReturn(Optional.empty());

        List<Sensor> list = new ArrayList<>();
        list.add(sensor);

        Mockito.when(repository.findAll()).thenReturn(list);

        controller.registerNewSensor(sensor);

        Mockito.verify(repository, Mockito.times(1)).save(sensor);

        List<Sensor> ret = controller.getSensors();

        Assert.assertEquals(1, ret.size());
    }

    /**
     * Test adding a new sensor by having already one sensor in the database with the same name
     */
    @Test(expected = IllegalStateException.class)
    public void testAddNewSensorWithExistingName() {
        Mockito.when(repository.findSensorByName(sensor.getName())).thenReturn(Optional.of(sensor));

        controller.registerNewSensor(sensor);
    }

    /**
     * Test getting all sensors
     */
    @Test
    public void testGetAllSensors() {
        Sensor sensor2 = new Sensor(
                "Lamp2",
                Type.HUMIDITY,
                room, "");

        Mockito.when(repository.findAll()).thenReturn(new ArrayList<>());

        List<Sensor> ret = controller.getSensors();
        Assert.assertEquals(0, ret.size());

        List<Sensor> list = new ArrayList<>();
        list.add(sensor);
        list.add(sensor2);

        Mockito.when(repository.findAll()).thenReturn(list);

        List<Sensor> list2 = controller.getSensors();

        Assert.assertEquals(2, list2.size());
    }

    /**
     * Test deleting a sensor
     */
    @Test
    public void testDeleteSensor() {
        Mockito.when(repository.existsById(sensor.getSensorId())).thenReturn(true);

        controller.deleteSensor(sensor.getSensorId());

        Mockito.verify(repository, Mockito.times(1)).deleteById(sensor.getSensorId());
    }

    /**
     * Test updating a sensor with different set of parameters:
     * 1. name and type are different
     * 2. name is null
     * 3. name is the same as the existing one
     * 4. type is null
     * 5. name and type are the same as the existing ones
     */
    @Test
    public void testUpdateSensor() {
        Integer id = sensor.getSensorId();
        String name = "uj";
        Type type = Type.HUMIDITY;
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(sensor));
        Mockito.when(repository.findSensorByName(name)).thenReturn(Optional.empty());

        controller.updateSensor(id, name, type, room, "");

        // TODO: expected is based on implementation, it should be updated if the implementation changes
        int expected = 2;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());

        controller.updateSensor(id, null, type, room, "");
        expected += 1;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());

        name = "uj";

        controller.updateSensor(id, name, type, room, "");
        expected += 1;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());


        controller.updateSensor(id, name, null, room, "");
        expected += 1;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());

        type = Type.TEMPERATURE;

        controller.updateSensor(id, name, type, room, "");
        expected += 1;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());
    }
}
