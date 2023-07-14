package com.example.thetelephoneappbe.voice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignalData {
    private String type;
    private String sdp;

    @Override
    public String toString() {
        return "SignalData{" +
                "type='" + type + '\'' +
                ", sdp='" + sdp + '\'' +
                '}';
    }
}
