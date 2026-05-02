package com.unnamed.matchmaking.cs16_matchmaking.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public enum TypeMatch{
    DEFAULT(0),
    COMPETITIVE(10),
    DEATHMATCH(32);

    private final Integer value;
}
