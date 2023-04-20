package SmartHome.com.smarthome.Sensor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceTest {

    private SensorRepository repository;
    private SensorController controller;
    Sensor sensor;

    /**
     * Set up the test by mocking the repository and creating a controller and  a sensor
     * The sensor's id is set to 1 with reflection, because it is not set by the database
     */
    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        repository = Mockito.mock(SensorRepository.class);
        SensorService service = new SensorService(repository);
        controller = new SensorController(service);

        sensor = new Sensor(
                "Lamp",
                "Living Room"
        );

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

        Assert.assertEquals(2, Mockito.mockingDetails(repository).getInvocations().size());

        List<Sensor> ret = controller.getSmartHomeDevices();

        Assert.assertEquals(1, ret.size());
    }

    /**
     * Test adding a new sensor by havinng already one sensor in the database with the same name
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
                "Living Room"
        );

        Mockito.when(repository.findAll()).thenReturn(new ArrayList<Sensor>());

        List<Sensor> ret = controller.getSmartHomeDevices();
        Assert.assertEquals(0, ret.size());

        List<Sensor> list = new ArrayList<>();
        list.add(sensor);
        list.add(sensor2);

        Mockito.when(repository.findAll()).thenReturn(list);

        List<Sensor> list2 = controller.getSmartHomeDevices();

        Assert.assertEquals(2, list2.size());
    }

    /**
     * Test deleting a sensor
     */
    @Test
    public void testDeleteSensor() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Mockito.when(repository.existsById(sensor.getSensorId())).thenReturn(true);

        controller.deleteSensor(sensor.getSensorId());

        Assert.assertEquals(2, Mockito.mockingDetails(repository).getInvocations().size());
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
        String type = "uj tipus";
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(sensor));
        Mockito.when(repository.findSensorByName(name)).thenReturn(Optional.empty());

        controller.updateSensor(id, name, type);

        // TODO: expected is based on implementation, it should be updated if the implementation changes
        int expected = 2;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());

        name = null;

        controller.updateSensor(id, name, type);
        expected += 2;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());

        name = "Lamp";

        controller.updateSensor(id, name, type);
        expected += 2;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());

        type = null;

        controller.updateSensor(id, name, type);
        expected += 1;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());

        type = "Living Room";

        controller.updateSensor(id, name, type);
        expected += 1;
        Assert.assertEquals(expected, Mockito.mockingDetails(repository).getInvocations().size());
    }
}
