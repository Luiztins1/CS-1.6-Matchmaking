package com.unnamed.matchmaking.cs16_matchmaking.model.enums;

public enum MatchState {
    COLD{
        @Override
        public boolean currentState(MatchState nextState){
            return nextState == WAITING || nextState == CANCELED;
        }
    },

    WAITING{
        @Override
        public boolean currentState(MatchState nextState){
            return nextState == STARTING || nextState == CANCELED;
        }
    },

    STARTING{
        @Override
        public boolean currentState(MatchState nextState){
            return nextState == READY_MATCH || nextState == CANCELED;
        }
    },

    READY_MATCH{
        @Override
        public boolean currentState(MatchState nextState){
            return nextState == FINISHED || nextState == CANCELED || nextState == WAITING;
        }
    },

    FINISHED,
    CANCELED;

    public boolean currentState(MatchState nextState) {
        return false;
    }
}
