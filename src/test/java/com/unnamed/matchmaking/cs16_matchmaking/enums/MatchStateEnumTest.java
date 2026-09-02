package com.unnamed.matchmaking.cs16_matchmaking.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MatchStateEnumTest {

    @Test
    void shouldMatchStateColdForWaiting(){
        MatchState currentState = MatchState.COLD;
        MatchState nextState = MatchState.WAITING;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldMatchStateWaitingForCold(){
        MatchState currentState = MatchState.WAITING;
        MatchState nextState = MatchState.COLD;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldMatchStateColdForCanceled(){
        MatchState currentState = MatchState.COLD;
        MatchState nextState = MatchState.CANCELED;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }


    @Test
    void shouldReturnCancelMatchStateWhenStateNotWaitingFor(){
        MatchState currentState = MatchState.COLD;
        MatchState nextState = MatchState.READY_MATCH;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isFalse();
    }

    @Test
    void shouldMatchStateWaitingForReadyMatch(){
        MatchState currentState = MatchState.WAITING;
        MatchState nextState = MatchState.READY_MATCH;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldMatchStateWaitingForCanceled(){
        MatchState currentState = MatchState.WAITING;
        MatchState nextState = MatchState.CANCELED;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldReturnCancelMatchStateWhenStateNotWaitingForWaiting(){
        MatchState currentState = MatchState.WAITING;
        MatchState nextState = MatchState.FINISHED;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isFalse();
    }

    @Test
    void shouldMatchStateReadyMatchForFinished(){
        MatchState currentState = MatchState.READY_MATCH;
        MatchState nextState = MatchState.FINISHED;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldMatchStateReadyMatchForCanceled(){
        MatchState currentState = MatchState.READY_MATCH;
        MatchState nextState = MatchState.CANCELED;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldMatchStateWaitingForReadyMatchRe(){
        MatchState currentState = MatchState.READY_MATCH;
        MatchState nextState = MatchState.WAITING;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldReturnCancelMatchStateWhenStateNotWaitingForReadyMatch(){
        MatchState currentState = MatchState.READY_MATCH;
        MatchState nextState = MatchState.COLD;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isFalse();
    }

    @Test
    void shouldMatchStateFinishedForCold(){
        MatchState currentState = MatchState.FINISHED;
        MatchState nextState = MatchState.COLD;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }

    @Test
    void shouldReturnCancelMatchStateWhenStateNotFinishedForFi(){
        MatchState currentState = MatchState.FINISHED;
        MatchState nextState = MatchState.WAITING;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isFalse();
    }

    @Test
    void shouldMatchStateFinishedForCanceled(){
        MatchState currentState = MatchState.FINISHED;
        MatchState nextState = MatchState.CANCELED;

        boolean changeState = currentState.currentState(nextState);

        assertThat(changeState).isTrue();
    }


    @Test
    void shouldMatchStateCurrentState(){
        MatchState currentState = MatchState.CANCELED;

        boolean state = currentState.currentState(currentState);

        assertThat(state).isFalse();
    }
}
