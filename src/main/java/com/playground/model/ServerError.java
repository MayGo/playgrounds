package com.playground.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerError {
    private LocalDateTime timestamp;
    private String error;

    public ServerError(String error) {

        this.timestamp = LocalDateTime.now();
        this.error = error;
    }

}