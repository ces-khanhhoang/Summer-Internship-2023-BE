package com.example.thetelephoneappbe.voice.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.thetelephoneappbe.voice.model.ReturnSignal;
import com.example.thetelephoneappbe.voice.model.SendingSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@Component
@CrossOrigin("*")
@Slf4j
public class SocketHandler {
    private final SocketIOServer server;
    private static final Map<String, SocketIOClient> users = new HashMap<>();
    private static final Map<String, String> rooms = new HashMap<>();
    private static final Map<String, List<String>> usersIdRooms = new HashMap<>(); 

    public SocketHandler(SocketIOServer server) {
        this.server = server;
        server.addListeners(this);
        server.start();
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("Client connected: " + client.getSessionId());
        users.put(client.getSessionId().toString(), client);

    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String roomId = rooms.get(client.getSessionId().toString());
        server.getRoomOperations(roomId).sendEvent("user left", client.getSessionId().toString());
        System.out.println("Client " + client.getSessionId() + " leave room " + roomId);
        users.remove(client.getSessionId().toString());
        rooms.remove(client.getSessionId().toString());
        usersIdRooms.get(roomId).remove(client.getSessionId().toString());
    }

    @OnEvent("disconnect all")
    public void onDisconnectAll(SocketIOClient client){
        String roomId = rooms.get(client.getSessionId().toString());
        for(String sessionId : usersIdRooms.get(roomId)){
            server.getClient(UUID.fromString(sessionId)).sendEvent("disconnectRoom");
        }
    }

    @OnEvent("join room")
    public void onJoinRoom(SocketIOClient client, String roomId){
        rooms.put(client.getSessionId().toString(),roomId);
        client.joinRoom(roomId);
        if(usersIdRooms.get(roomId) != null){
            client.sendEvent("all users", usersIdRooms.get(roomId));
            usersIdRooms.get(roomId).add(client.getSessionId().toString());
        }
        else{
            List<String> idUsers = new ArrayList<>();
            idUsers.add(client.getSessionId().toString());
            usersIdRooms.put(roomId, idUsers);
        }
    }

    @OnEvent("sending signal")
    public void onSendingSignal(SocketIOClient client, SendingSignal sendingSignal){
        SocketIOClient userToCall = users.get(sendingSignal.getUserToSignal());
        userToCall.sendEvent("user joined", sendingSignal);
    }

    @OnEvent("returning signal")
    public void onReturningSignal(SocketIOClient client,  SendingSignal sendingSignal){
        SocketIOClient userToCall = users.get(sendingSignal.getCallerID());
        userToCall.sendEvent("receiving returned signal", new ReturnSignal(sendingSignal.getSignal(),client.getSessionId().toString()));
    }
}