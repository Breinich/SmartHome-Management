package com.itcatcetc.smarthome.sensor.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.type.Type;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SensorDataServiceTest {

    private SensorDataService service;
    private SensorDataController controller;
    private SensorDataRepository repository;
    private SensorData sensorData;
    private ObjectMapper mapper;
    private Sensor sensor;


    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        repository = Mockito.mock(SensorDataRepository.class);
        service = new SensorDataService(repository);
        controller = new SensorDataController(service);

        Room room = new Room("Living Room");
        sensor = new Sensor("Lamp", Type.LIGHT, room, "https://123.123.133.3/");
        sensorData = new SensorData(Type.LIGHT, sensor, 42);

        Field f1 = sensorData.getClass().getDeclaredField("dataId");
        f1.setAccessible(true);
        f1.set(sensorData, 1);

        mapper = new ObjectMapper();
    }

    /**
     * Test getting all sensor data
     */
    @Test
    public void testGetData() {
        ArrayList<SensorData> list = new ArrayList<>();
        list.add(sensorData);
        Mockito.when(repository.findAll()).thenReturn(list);

        String ret = controller.getData().getBody();

        List<SensorData> retList;
        try{
            retList = mapper.readValue(ret, mapper.getTypeFactory().constructCollectionType(List.class, SensorData.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, retList.size());
    }

    /**
     * Test adding new sensor data
     */
    @Test
    public void registerNewData() {
        SensorData newData = new SensorData(Type.LIGHT, sensor, 42);

        Mockito.when(repository.save(newData)).thenReturn(newData);
        controller.registerNewData(newData);
        Mockito.verify(repository, Mockito.times(1)).save(newData);
    }

    /**
     * Test deleting sensor data
     */
    @Test
    public void deleteData() {
        Mockito.when(repository.existsById(sensorData.getDataId())).thenReturn(true);
        controller.deleteData(sensorData.getDataId());
        Mockito.verify(repository, Mockito.times(1)).deleteById(sensorData.getDataId());
    }
}