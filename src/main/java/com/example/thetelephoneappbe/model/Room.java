package com.example.thetelephoneappbe.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Entity
@Table(name = "room")

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Long id;
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "room")
    Set<User> users = new HashSet<>();

    public Room(){
    }

    public Room(String status){
        this.status = status;
    }

    @OneToMany(mappedBy = "room")
    private List<User> users1 = new ArrayList<>();

    @OneToMany(mappedBy = "room1")
    private List<Result> results = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "{" +
                "id = " + id +
                ", status = " + status +
                ", users = " + users +
                ", results = " + results +
                '}';
    }
}


