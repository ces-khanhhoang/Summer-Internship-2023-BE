package com.example.thetelephoneappbe.service;

import com.example.thetelephoneappbe.model.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(Room room);
    List<Room> getAllRoom();
}
