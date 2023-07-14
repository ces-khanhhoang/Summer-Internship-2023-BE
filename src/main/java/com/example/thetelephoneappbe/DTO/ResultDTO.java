package com.example.thetelephoneappbe.DTO;



public class ResultDTO {
    private String namePlay;
    private String data;

    private String id_image;

    public ResultDTO() {
    }

    public ResultDTO(String namePlay, String data, String id_image) {
        this.namePlay = namePlay;
        this.data = data;
        this.id_image = id_image;
    }

    public String getId_image() {
        return id_image;
    }

    public void setId_image(String id_image) {
        this.id_image = id_image;
    }

    public String getNamePlay() {
        return namePlay;
    }

    public void setNamePlay(String namePlay) {
        this.namePlay = namePlay;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "namePlay:" + namePlay  +
                ", data:" + data +
                ", id_image:" + id_image +
                '}';
    }
}
