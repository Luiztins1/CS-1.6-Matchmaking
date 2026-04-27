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
            return nextState == READY_MATCH || nextState == COLD || nextState == CANCELED;
        }
    },

    READY_MATCH{
        @Override
        public boolean currentState(MatchState nextState){
            return nextState == FINISHED || nextState == CANCELED || nextState == WAITING;
        }
    },

    FINISHED{
        @Override
        public boolean currentState(MatchState nextState) {
            return nextState == COLD || nextState == CANCELED;
        }
    },

    CANCELED;

    public boolean currentState(MatchState nextState) {
        return false;
    }
}
