package com.example.thetelephoneappbe.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")

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


