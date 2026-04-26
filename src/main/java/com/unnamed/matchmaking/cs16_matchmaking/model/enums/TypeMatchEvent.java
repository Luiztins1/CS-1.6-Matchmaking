package com.unnamed.matchmaking.cs16_matchmaking.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeMatchEvent {
    DEFAULT(0),
    COMPETITIVE(10),
    DEATHMATCH(32);

    private final Integer value;
}
