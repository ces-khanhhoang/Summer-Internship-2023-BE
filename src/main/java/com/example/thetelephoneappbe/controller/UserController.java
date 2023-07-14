package com.example.thetelephoneappbe.controller;

import com.example.thetelephoneappbe.DTO.ResultDTO;
import com.example.thetelephoneappbe.apiconstants.*;
import com.example.thetelephoneappbe.enums.Status;
import com.example.thetelephoneappbe.model.Role;
import com.example.thetelephoneappbe.model.Room;
import com.example.thetelephoneappbe.model.User;
import com.example.thetelephoneappbe.service.RoleService;
import com.example.thetelephoneappbe.service.RoomService;
import com.example.thetelephoneappbe.service.UserService;
import com.example.thetelephoneappbe.storage.StorageGame;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.thetelephoneappbe.enums.ERole.ROLE_USER;

@Controller
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private UserService userService;
    private RoomService roomService;
    private RoleService roleService;
    public static final String NAME_DUPLICATE = "nick name is duplicate";
    public static final String FULL_ROOM = "the room is full";
    public static final String NO_ROOM = "the room does not exist";
    public static final String CHOOSE_NUMBER = "choose number of player";
    public static final String PLAY_AGAIN = "play again new turn";
    public static final String PREFIX = "/topic/";
    List<StorageGame> storageGames = new ArrayList<>();
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    Gson gson = new Gson();

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(ApiCreateRoom.API_CREATE_ROOM)
    public ResponseEntity<String> createRoom(@PathVariable(ApiCreateRoom.PARAM_USER_NAME) String userName,
                                             @PathVariable(ApiCreateRoom.PARAM_ID_AVATAR) String id_avatar) {

        try {
        User user = userService.creatUser(userName, roomService, roleService,id_avatar);
        return ResponseEntity.ok(gson.toJson(gson.fromJson(user.toString(), Object.class)));
        }catch (Exception exception){
            return  ResponseEntity.ok(NAME_DUPLICATE);
        }

    }

    @PostMapping(ApiJoinRoom.API_JOIN_ROOM)
    public ResponseEntity<String> joinRoom(@PathVariable(ApiJoinRoom.PARAM_ID_ROOM) Long idRoom,
                                           @PathVariable(ApiJoinRoom.PARAM_USER_NAME) String userName,
                                           @PathVariable(ApiJoinRoom.PARAM_ID_AVATAR) String id_avatar) {

      try {
          Room roomPlay = roomService.getOneRoom(idRoom);
        if (roomPlay.getUsers().size() < roomPlay.getMaxPlayer()) {
                try {
                    User userNew = new User();
                    userNew.setId_image(id_avatar);
                    userNew.setNickname(userName);
                    Room room = roomService.getAllRoom().stream()
                                .filter(room1 -> room1.getId()
                                .equals(idRoom))
                                .findFirst()
                                .orElseThrow();

                    userNew.setRoom(room);
                    Role role = roleService.getAllRole().stream().filter(role1 -> role1.getName()
                            .equals(ROLE_USER)).findFirst().orElseThrow();

                    userNew.getRoles().add(role);
                    role.getUsers().add(userNew);
                    roomPlay.getUsers().add(userNew);
                    roomPlay.setStatus(Status.NEW.getValue());
                    userService.saveUser(userNew);
                    simpMessagingTemplate.convertAndSend(PREFIX + idRoom, gson.toJson(gson.fromJson(roomPlay.getUsers().toString(), Object.class)));

                    return ResponseEntity.ok(gson.toJson(gson.fromJson(roomPlay.getUsers().toString(), Object.class)));
                } catch (Exception exception) {
                    return ResponseEntity.ok(NAME_DUPLICATE);
                }

            } else {
                return ResponseEntity.ok(FULL_ROOM);
            }
        }
        catch (Exception exception) {
             return  ResponseEntity.ok(NO_ROOM);
      }

    }

    @PostMapping(ApiDeleteUserFromRoom.API_DELETE_USER_FROM_ROOM)
    public ResponseEntity<String> deleteUserFromRoom(@PathVariable(ApiDeleteUserFromRoom.PARAM_ID_ROOM) Long roomId,
                                                     @PathVariable(ApiDeleteUserFromRoom.PARAM_NICKNAME) String name) {

        Room playRoom = roomService.getOneRoom(roomId);
        User userToDelete = playRoom
                .getUsers().stream()
                .filter(user -> user.getNickname()
                .equals(name)).findFirst().orElseThrow();

        playRoom.getUsers().remove(userToDelete);
        roomService.SaveRoom(playRoom);
        userToDelete.setRoom(null);
        userService.saveUser(userToDelete);

        playRoom.setStatus(Status.NEW.getValue());
        simpMessagingTemplate.convertAndSend(
                PREFIX + roomId,
                gson.toJson(gson.fromJson(playRoom.getUsers().toString(), Object.class)));
        simpMessagingTemplate.convertAndSend(
                PREFIX + userToDelete.getNickname(),
                gson.toJson(gson.fromJson(userToDelete.getNickname(), Object.class)));

        return ResponseEntity.ok(gson.toJson(gson.fromJson(playRoom.getUsers().toString(), Object.class)));
    }

    @PostMapping(ApiStartGame.API_START_GAME)
    public ResponseEntity<String> start(@PathVariable(ApiStartGame.PARAM_ID_ROOM) Long idRoom
                                        ,@PathVariable(ApiStartGame.PARAM_MODE) String mode) {

        Room room = roomService.getOneRoom(idRoom);
        room.setLastPlay(LocalDateTime.now());
        if(mode.equals(Status.IN_PROGRESS.getValue())) {
            room.setStatus(Status.IN_PROGRESS.getValue());
        }
        if(mode.equals(Status.KNOCK_OFF.getValue())){
            room.setStatus(Status.KNOCK_OFF.getValue());
        }
        roomService.SaveRoom(room);
        List<User> users = room.getUsers();
        StorageGame storageGame = new StorageGame();
        room.getUsers().stream().forEach(user -> {storageGame.getResult().put(user.getNickname(), new LinkedHashMap<>());
        storageGame.getAvatar().add(user.getId_image());});
        storageGame.setIdRoom(idRoom);
        storageGames.add(storageGame);
        simpMessagingTemplate.convertAndSend(PREFIX + idRoom, gson.toJson(gson.fromJson(users.toString(), Object.class)));

        return ResponseEntity.ok(gson.toJson(gson.fromJson(users.toString(), Object.class)));
    }

    @PostMapping(ApiPlayGame.API_PLAY_GAME)
    public ResponseEntity<String> playGame(@PathVariable(ApiPlayGame.PARAM_ID_ROOM) Long idRoom,
                                           @PathVariable(ApiPlayGame.PARAM_NAME) String nickname,
                                           @PathVariable(ApiPlayGame.PARAM_DATA) String data,
                                           @PathVariable(ApiPlayGame.PARAM_TURN) Integer turn) {

        Room playRoom = roomService.getOneRoom(idRoom);
        StorageGame storageGamePlay = storageGames.stream()
                .filter(storageGame -> storageGame.getIdRoom().equals(idRoom))
                .reduce((first, second) -> second).orElseThrow();

        if (storageGamePlay.getResult().containsKey(nickname)) {
            Map<Integer, String> innerMap = storageGamePlay.getResult().get(nickname);
            innerMap.put(turn, data);
        }

        storageGamePlay.getNicknames().add(nickname);
        boolean areListsEqual = playRoom.getUsers().size() == storageGamePlay.getNicknames().size();
        if (areListsEqual) {
            storageGamePlay.setKeyNickName(new ArrayList<>(storageGamePlay.getResult().keySet()));
            for (Map<Integer, String> map : storageGamePlay.getResult().values()) {
                LinkedHashMap<Integer, String> subMap = new LinkedHashMap<>(map);
                if (subMap.containsKey(turn)) {
                    storageGamePlay.getValues().add(subMap.get(turn));
                }
            }
            Collections.rotate(storageGamePlay.getValues(), turn);
            storageGamePlay.getReceiver().addAll(storageGamePlay.getKeyNickName());
            Collections.rotate(storageGamePlay.getReceiver(), turn);

            if(!playRoom.getStatus().equals(Status.KNOCK_OFF.getValue())) {

                if (turn % 2 == 0) {
                    playRoom.setStatus(Status.DRAW.getValue());
                } else {
                    playRoom.setStatus(Status.WRITE.getValue());
                }
            }
            for (int i = 0; i < playRoom.getUsers().size(); i++) {
                String JSON = "{value:" + storageGamePlay.getValues().get(i) + ", receiver:" + storageGamePlay.getReceiver().get(i) + ", status:" + playRoom.getStatus() + ", number:" + playRoom.getUsers().size() + "}";
                simpMessagingTemplate.convertAndSend(PREFIX + storageGamePlay.getKeyNickName().get(i), gson.toJson(gson.fromJson(JSON, Object.class)));
            }

            storageGamePlay.setNicknames(new HashSet<>());
            storageGamePlay.setReceiver(new ArrayList<>());
            storageGamePlay.setKeyNickName(new ArrayList<>());
            storageGamePlay.setValues(new ArrayList<>());

        }
        return ResponseEntity.ok(gson.toJson(gson.fromJson(playRoom.getUsers().toString(), Object.class)));
    }

    @PostMapping(ApiGetResult.API_GET_RESULT)
    public ResponseEntity<String> getResult(@PathVariable(ApiGetResult.PARAM_NICKNAME) String nickname,
                                            @PathVariable(ApiGetResult.PARAM_ID_ROOM) Long idRoom) {
        StorageGame storageGamePlay = storageGames.stream()
                .filter(storageGame -> storageGame.getIdRoom().equals(idRoom))
                .reduce((first, second) -> second).orElseThrow();


        List<ResultDTO> resultDTOs = new ArrayList<>();
        List<String> nicks = new ArrayList<>(storageGamePlay.getResult().keySet());
        List<String> avatars = new ArrayList<>();
        avatars.addAll(storageGamePlay.getAvatar());
        Collections.rotate(avatars,nicks.indexOf(nickname));
        Collections.rotate(nicks, nicks.indexOf(nickname));
        Map<Integer, String> data = storageGamePlay.getResult().get(nickname);

        for (Map.Entry<Integer, String> entry : data.entrySet()) {
            ResultDTO resultDTO = new ResultDTO(nicks.get(entry.getKey()-1),entry.getValue(),avatars.get(entry.getKey()-1));
            resultDTOs.add(resultDTO);
        }
        for (String nick : nicks) {
            simpMessagingTemplate.convertAndSend(PREFIX + nick, gson.toJson(gson.fromJson(resultDTOs.toString(), Object.class)));
        }

        return ResponseEntity.ok(gson.toJson(gson.fromJson(resultDTOs.toString(), Object.class)));
    }

    @PostMapping(ApiPlayAgain.API_PLAY_AGAIN)
    public ResponseEntity<String> playAgain(@PathVariable(ApiPlayAgain.PARAM_ID_ROOM) Long idRoom){
        Room playRoom = roomService.getOneRoom(idRoom);
        playRoom.setStatus(Status.AGAIN.getValue());
        simpMessagingTemplate.convertAndSend(PREFIX + idRoom, gson.toJson(gson.fromJson(playRoom.getUsers().toString(), Object.class)));
        return ResponseEntity.ok(PLAY_AGAIN);

    }

    @PostMapping(ApiChooseMaxNumber.API_CHOOSE_MAX_NUMBER)
    public  ResponseEntity<String> numberPlay(@PathVariable(ApiChooseMaxNumber.PARAM_ID_ROOM) Long idRoom,
                                              @PathVariable(ApiChooseMaxNumber.PARAM_NUMBER) Integer number ) {

        Room playRoom = roomService.getOneRoom(idRoom);
        playRoom.setStatus(Status.MAX.getValue());
        playRoom.setMaxPlayer(number);
        roomService.SaveRoom(playRoom);
        simpMessagingTemplate.convertAndSend(PREFIX + idRoom, gson.toJson(gson.fromJson(playRoom.getUsers().toString(), Object.class)));
        return ResponseEntity.ok(CHOOSE_NUMBER);
    }


}

