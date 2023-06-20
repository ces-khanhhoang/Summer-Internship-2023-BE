package com.example.thetelephoneappbe.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

import java.util.HashSet;

import java.util.Set;

@Entity
@Table(name = "room")



@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Long id;
    @Column(name = "status")
    private String status;


    public Room(){
    }

    public Room(String status){
        this.status = status;
    }

    @OneToMany(mappedBy = "room")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    Set<User> users = new HashSet<>();


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
        return "Room{" +
                "id=" + id +
                ", status='" + status + '\'' +

                '}';
    }

}


