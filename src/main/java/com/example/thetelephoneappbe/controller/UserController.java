package com.example.thetelephoneappbe.controller;

import com.example.thetelephoneappbe.model.Role;
import com.example.thetelephoneappbe.model.Room;
import com.example.thetelephoneappbe.model.User;
import com.example.thetelephoneappbe.service.RoleService;
import com.example.thetelephoneappbe.service.RoomService;
import com.example.thetelephoneappbe.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import static com.example.thetelephoneappbe.enums.ERole.ROLE_HOST;

@Controller
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private UserService userService;
    private RoomService roomService;
    private RoleService roleService;
    Gson gson = new Gson();

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @Autowired
    public void setRoomService(RoomService roomService){
        this.roomService = roomService;
    }

    @Autowired
    public void setRoleService(RoleService roleService){this.roleService = roleService;}


    @PostMapping("/create/{user_name}")
    public ResponseEntity<String> create(@PathVariable("user_name") String userName){
        User user = new User();
        user.setNickname(userName);
        user.setRoom(roomService.createRoom(new Room("NEW")));
        Role role = roleService.getAllRole().stream().filter(role1 -> role1.getName().equals(ROLE_HOST)).toList().get(0);
        user.getRoles().add(role);
        role.getUsers().add(user);
        userService.creatUser(user);
        return ResponseEntity.ok(gson.toJson(gson.fromJson(user.toString(), Object.class)));
    }

}
