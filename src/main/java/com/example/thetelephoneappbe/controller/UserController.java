package com.example.thetelephoneappbe.controller;

import com.example.thetelephoneappbe.model.User;
import com.example.thetelephoneappbe.service.RoleService;
import com.example.thetelephoneappbe.service.RoomService;
import com.example.thetelephoneappbe.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private UserService userService;
    private RoomService roomService;
    private RoleService roleService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
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
        User user = userService.creatUser(userName, roomService, roleService);
        return ResponseEntity.ok(gson.toJson(gson.fromJson(user.toString(), Object.class)));
    }

    @PostMapping("/join/{id_room}/{user_name}")
    public ResponseEntity<String> join(@PathVariable("id_room") Long idRoom, @PathVariable("user_name") String userName){
        System.out.println("join room");
        userService.joinUser(idRoom, userName, roomService, roleService);
        List<User> users = userService.getUserByIdRoom(idRoom);
        simpMessagingTemplate.convertAndSend("/topic/" + idRoom , gson.toJson(gson.fromJson(users.toString(), Object.class)));
        return ResponseEntity.ok(gson.toJson(gson.fromJson(users.toString(), Object.class)));
    }
}
