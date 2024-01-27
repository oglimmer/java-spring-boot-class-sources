package com.oglimmer.kniffel.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlayerData {
    private String name;
    private int score; // total score so far
}