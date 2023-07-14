package com.example.thetelephoneappbe.service;

import com.example.thetelephoneappbe.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    User creatUser(String userName, RoomService roomService, RoleService roleService,String id_image) throws SQLException;
    void joinUser(Long idRoom, String userName, RoomService roomService, RoleService roleService);
    List<User> getUserByIdRoom(Long idRoom);
    public User saveUser(User user);


}
