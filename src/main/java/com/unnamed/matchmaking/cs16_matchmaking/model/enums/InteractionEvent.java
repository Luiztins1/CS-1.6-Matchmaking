package com.unnamed.matchmaking.cs16_matchmaking.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum InteractionEvent {
    ENTER(true),
    EXIT(false),
    MATCHING(true);

    private final boolean b;

}
