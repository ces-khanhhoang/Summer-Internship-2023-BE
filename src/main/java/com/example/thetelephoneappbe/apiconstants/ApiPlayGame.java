package com.example.thetelephoneappbe.apiconstants;

public class ApiPlayGame {
    public static final String PARAM_ID_ROOM = "id_room";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_DATA = "data";
    public static final String PARAM_TURN = "turn";
    public static final String API_PLAY_GAME =  "/done/{"+PARAM_ID_ROOM +"}/{" + PARAM_NAME+ "}/{"+ PARAM_DATA+"}/{"+ PARAM_TURN+"}";

}
