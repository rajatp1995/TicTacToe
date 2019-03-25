package com.rajatp.tictactoe;

public class OnlineGameData {
    String onlineData;
    String currentTurn;
    String currentState;

    public OnlineGameData() {
    }

    public String getOnlineData() {
        return onlineData;
    }

    public void setOnlineData(String onlineData) {
        this.onlineData = onlineData;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}
