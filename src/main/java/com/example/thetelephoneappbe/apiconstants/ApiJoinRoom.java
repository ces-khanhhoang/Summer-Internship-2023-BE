package com.example.thetelephoneappbe.apiconstants;

public class ApiJoinRoom {
    public static final String PARAM_ID_ROOM = "id_room";
    public static final String PARAM_USER_NAME = "user_name";
    public static final String PARAM_ID_AVATAR = "id_avatar";

    public static final String API_JOIN_ROOM = "/join/{"+ PARAM_ID_ROOM +"}/{" + PARAM_USER_NAME+ "}/{" + PARAM_ID_AVATAR + "}";
}
