package com.example.thetelephoneappbe.voice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendingSignal {
    private String userToSignal;
    private SignalData signal;
    private String callerID;

    @Override
    public String toString() {
        return "SendingSignal{" +
                "userToSignal='" + userToSignal + '\'' +
                ", signal=" + signal +
                ", callerID='" + callerID + '\'' +
                '}';
    }
}
