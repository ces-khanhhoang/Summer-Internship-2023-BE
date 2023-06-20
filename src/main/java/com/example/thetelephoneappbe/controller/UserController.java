package com.example.thetelephoneappbe.controller;

import com.example.thetelephoneappbe.model.Role;
import com.example.thetelephoneappbe.model.Room;
import com.example.thetelephoneappbe.model.User;
import com.example.thetelephoneappbe.service.RoleService;
import com.example.thetelephoneappbe.service.RoomService;
import com.example.thetelephoneappbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import static com.example.thetelephoneappbe.enums.ERole.ROLE_HOST;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private RoomService roomService;
    private RoleService roleService;

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


    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user){
        user.setRoom(roomService.createRoom(new Room("New")));
        Role role = roleService.getAllRole().stream().filter(role1 -> role1.getName().equals(ROLE_HOST)).toList().get(0);
        user.getRoles().add(role);
        role.getUsers().add(user);
        return ResponseEntity.ok(userService.creatUser(user));
    }

    @PostMapping("/invite/{id_user}")
    public ResponseEntity<String> invite(@PathVariable("id_user") String id, @RequestBody User user){
        System.out.println(user.getId());
        return ResponseEntity.ok(user.getRoom().getId().toString());
    }

    @PostMapping("/join/{id_room}")
    public ResponseEntity<User> join(@PathVariable("id_room") String id, @RequestBody User user){
        Room room = roomService.getAllRoom().stream().filter(room1 -> room1.getId().equals(id)).toList().get(0);
        Role role = roleService.getAllRole().stream().filter(role1 -> role1.getName().equals(ROLE_HOST)).toList().get(0);
        user.getRoles().add(role);
        role.getUsers().add(user);
        return ResponseEntity.ok(userService.creatUser(user));
    }

}
