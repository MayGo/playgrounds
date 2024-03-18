package com.playground.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Kid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    private int age;

    private int ticketNumber;

    public Kid() {
    }

    public Kid(String name, int ticketNumber, int age) {
        this.name = name;
        this.ticketNumber = ticketNumber;
        this.age = age;
    }

    public Boolean acceptsWaitingInQueue() {
        return age > 10;
    }
}