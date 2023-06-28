package com.example.thetelephoneappbe.controller;

import com.example.thetelephoneappbe.model.Room;
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
import java.util.Optional;

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
        List<User> users =  roomService.getOneRoom(idRoom).getUsers();
        simpMessagingTemplate.convertAndSend("/topic/" + idRoom , gson.toJson(gson.fromJson(users.toString(), Object.class)));
        return ResponseEntity.ok(gson.toJson(gson.fromJson(users.toString(), Object.class)));
    }

    @PostMapping("/delete/{id_room}/{nickname}")
    public ResponseEntity<String> deleteUserFromRoom(@PathVariable("id_room")Long roomId, @PathVariable("nickname")String name) {

        Room playRoom = roomService.getOneRoom(roomId);
        User userToDelete = playRoom
                .getUsers().stream()
                .filter(user -> user.getNickname()
                .equals(name)).findFirst().orElseThrow();

        playRoom.getUsers().remove(userToDelete);
        roomService.SaveRoom(playRoom);
        userToDelete.setRoom(null);
        userService.saveUser(userToDelete);

        simpMessagingTemplate.convertAndSend(
                "/topic/" + roomId,
                gson.toJson(gson.fromJson(playRoom.getUsers().toString(), Object.class)));
        simpMessagingTemplate.convertAndSend(
                "/topic/" + userToDelete.getNickname(),
                gson.toJson(gson.fromJson(userToDelete.getNickname().toString() , Object.class)));

        return ResponseEntity.ok( gson.toJson(gson.fromJson(playRoom.getUsers().toString(), Object.class)));
    }

}
