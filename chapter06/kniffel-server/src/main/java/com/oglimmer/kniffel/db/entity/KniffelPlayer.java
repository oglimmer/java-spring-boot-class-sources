package com.oglimmer.kniffel.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class KniffelPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int score;
    /**
     * each bookingType can only be used once per game
     */
    @ElementCollection(targetClass = BookingType.class)
    @Enumerated(EnumType.STRING)
    private List<BookingType> usedBookingTypes = List.of();

    @ManyToOne
    private KniffelGame game;

    public KniffelPlayer() {
    }

    public KniffelPlayer(String name) {
        this.name = name;
    }
}
