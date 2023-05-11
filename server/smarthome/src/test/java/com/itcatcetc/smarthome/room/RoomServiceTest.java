package com.itcatcetc.smarthome.room;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomServiceTest {
    private RoomRepository repository;
    private RoomController controller;
    private Room room;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        repository = Mockito.mock(RoomRepository.class);
        RoomService service = new RoomService(repository);
        controller = new RoomController(service);

        room = new Room("Living Room");

        Field f1 = room.getClass().getDeclaredField("roomId");
        f1.setAccessible(true);
        f1.set(room, 1);
    }

    /**
     * Test adding a new room
     */
    @Test
    public void testAddNewRoom() {
        Mockito.when(repository.findRoomByName(room.getName())).thenReturn(Optional.empty());
        controller.registerNewRoom(room);
        Mockito.verify(repository, Mockito.times(1)).save(room);
    }

    /**
     * Test adding same named room
     */
    @Test(expected = IllegalStateException.class)
    public void testAddSameRoom() {
        Mockito.when(repository.findRoomByName(room.getName())).thenReturn(Optional.of(room));
        controller.registerNewRoom(room);
    }

    /**
     * Test deleting a room
     */
    @Test
    public void testDeleteRoom() {
        Mockito.when(repository.existsById(room.getRoomId())).thenReturn(true);
        controller.deleteRoom(room.getRoomId());
        Mockito.verify(repository, Mockito.times(1)).deleteById(room.getRoomId());
    }

    /**
     * Test deleting a room that doesn't exist
     */
    @Test(expected = IllegalStateException.class)
    public void testDeleteRoomThatDoesntExist() {
        Mockito.when(repository.existsById(room.getRoomId())).thenReturn(false);
        controller.deleteRoom(room.getRoomId());
    }

    /**
     * Test updating a room
     */
    @Test
    public void testUpdateRoom() {
        Mockito.when(repository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        Mockito.when(repository.findRoomByName("Bedroom")).thenReturn(Optional.empty());
        controller.updateRoom(room.getRoomId(), "Bedroom");
        Assert.assertEquals("Bedroom", room.getName());
    }

    /**
     * Test updating a room that doesn't exist
     */
    @Test(expected = IllegalStateException.class)
    public void testUpdateRoomThatDoesntExist() {
        Mockito.when(repository.findById(room.getRoomId())).thenReturn(Optional.empty());
        controller.updateRoom(room.getRoomId(), "Bedroom");
    }

    /**
     * Test updating a room with a name that already exists
     */
    @Test(expected = IllegalStateException.class)
    public void testUpdateRoomWithSameName() {
        Room room2 = new Room("Bedroom");
        Mockito.when(repository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        Mockito.when(repository.findRoomByName("Bedroom")).thenReturn(Optional.of(room2));
        controller.updateRoom(room.getRoomId(), "Bedroom");
    }

    /**
     * Test updating a room with a null name
     */
    @Test
    public void testUpdateRoomWithNullName() {
        Mockito.when(repository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        controller.updateRoom(room.getRoomId(), null);
        Assert.assertEquals("Living Room", room.getName());
    }

    /**
     * Test getting all rooms
     */
    @Test
    public void testGetRooms() {
        controller.getRooms();
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    /**
     * Test getting all sensors in a room
     */
    @Test
    public void testGetSensorsByRoomId() {
        controller.getSensorsByRoomId(room.getRoomId());
        Mockito.verify(repository, Mockito.times(1)).findSensorsByRoomId(room.getRoomId());
    }


}
