package com.rajatp.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineMultiplayerActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myGameRef;
    DatabaseReference myGameRefPlay;
    ValueEventListener onlineData, onlinePlayData;
    int currentPos=0, i=0, end=0, flagWinner=0, winnerDecided=0, flagPlayerSet=0;
    List<String> gameData = new ArrayList<>();
    String player="", player1="", player2="", gameRoom="", currentTurn="", currentState="";
    TextView player1TV, player2TV, winner, score1, score2;
    TableLayout tictactoe;
    Button restart;
    int scorePlayer1=0, scorePlayer2=0;
    MediaPlayer mPlayer;
    List<String> listNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mPlayer = MediaPlayer.create(this, R.raw.chanceplayednew);
        for (i=0;i<9;i++) gameData.add(Integer.toString(i));
        gameRoom = (String) getIntent().getSerializableExtra("gameRoom");
        myGameRef = database.getReference(gameRoom);

        player = (String) getIntent().getSerializableExtra("localPlayer");
        player1TV = (TextView) findViewById(R.id.player1Name);
        player1TV.setText("Awaiting..");
        player2TV = (TextView) findViewById(R.id.player2Name);
        player2TV.setText("Awaiting..");
        restart = (Button) findViewById(R.id.restartGame_button);
        winner = (TextView) findViewById(R.id.winnerDisplay);
        restart.setVisibility(View.INVISIBLE);
        winner.setText("Game not yet started");

        onlineData = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNames.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    String pName = keyNode.getValue().toString();
                    listNames.add(pName);
                    startGame();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        myGameRef.addValueEventListener(onlineData);

    }

    public void startGame() {
        if (listNames.size() == 2) {
            myGameRef.removeEventListener(onlineData);
            String gameDataPath = gameRoom + "data";
            myGameRefPlay = database.getReference(gameDataPath);
            winner.setText("Game in progress");
            player1 = listNames.get(0);
            player2 = listNames.get(1);

            player1TV = (TextView) findViewById(R.id.player1Name);
            player1TV.setText("Player 1 is " + listNames.get(0) + ", playing as X");
            player2TV = (TextView) findViewById(R.id.player2Name);
            player2TV.setText("Player 2 is " + listNames.get(1) + ", playing as O");
            restart = (Button) findViewById(R.id.restartGame_button);
            restart.setVisibility(View.INVISIBLE);
            score1 = (TextView) findViewById(R.id.scoreP1);
            score2 = (TextView) findViewById(R.id.scoreP2);
            score1.setText(player1 + ": " + scorePlayer1);
            score2.setText(player2 + ": " + scorePlayer2);
            tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
            for (int j = 0; j < 9; j++) {
                String tagTemp = "pos_" + j;
                TextView tempTX = (TextView) tictactoe.findViewWithTag(tagTemp);
                tempTX.setBackground(getResources().getDrawable(R.drawable.one));
                tempTX.setText("");
            }
            listenForUpdates();
        }
    }

    public void listenForUpdates() {
        final OnlineGameData objGame = new OnlineGameData();
        onlinePlayData = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("currentTurn").exists()) {
                    objGame.setOnlineData("0,1,2,3,4,5,6,7,8");
                    objGame.setCurrentState("X");
                    objGame.setCurrentTurn(player1);
                    currentTurn=player1;
                    currentState="X";
                    myGameRefPlay.setValue(objGame);
                } else {
                    currentTurn = dataSnapshot.child("currentTurn").getValue().toString();
                    currentState = dataSnapshot.child("currentState").getValue().toString();
                    String data = dataSnapshot.child("onlineData").getValue().toString();
                    String[] dataToList = data.split(",");
                    gameData = new ArrayList<String>(Arrays.asList(dataToList));
                }
                createBoard();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        myGameRefPlay.addValueEventListener(onlinePlayData);
    }

    public void createBoard() {
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        for(int j=0;j<9;j++) {
            if (gameData.get(j).equals("X") || gameData.get(j).equals("O")) {
                String tagTemp = "pos_" + j;
                TextView tempTX = (TextView) tictactoe.findViewWithTag(tagTemp);
                tempTX.setBackground(getResources().getDrawable(R.drawable.one));
                tempTX.setText(gameData.get(j));
                tempTX.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                String tagTemp = "pos_" + j;
                TextView tempTX = (TextView) tictactoe.findViewWithTag(tagTemp);
                tempTX.setBackground(getResources().getDrawable(R.drawable.one));
                tempTX.setText("");
                tempTX.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
        checkWinner();
    }

    public void chancePlayed (View view) {
        OnlineGameData gameObj = new OnlineGameData();
        if (flagWinner == 0) {
            if (currentTurn.equals(player)) {
                String posClicked = view.getTag().toString();
                String[] temp = posClicked.split("_");
                int actualPos = Integer.valueOf(temp[1]);
                TextView txt = (TextView) view;
                if (txt.getText().length() != 0) {
                    Toast.makeText(OnlineMultiplayerActivity.this, "Occupied", Toast.LENGTH_SHORT).show();
                } else {
                    txt.setText(currentState);
                    txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mPlayer.start();
                    gameData.remove(actualPos);
                    gameData.add(actualPos, currentState);
                    String tempData="";
                    for(int p=0;p<gameData.size();p++) {
                        tempData = tempData + gameData.get(p) + ",";
                    }
                    gameObj.setOnlineData(tempData);

                    if (currentTurn.equals(player1)) {
                        currentTurn = player2;
                        gameObj.setCurrentTurn(player2);
                    } else {
                        currentTurn = player1;
                        gameObj.setCurrentTurn(player1);
                    }

                    if (currentState.equals("X")) {
                        currentState="O";
                        gameObj.setCurrentState("O");
                    } else {
                        currentState="X";
                        gameObj.setCurrentState("X");
                    }
                    myGameRefPlay.setValue(gameObj);
                }
            } else Toast.makeText(OnlineMultiplayerActivity.this, "Wait for your turn..", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(OnlineMultiplayerActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
    }

    public void checkWinner() {
        if (gameData.get(0)==gameData.get(1) && gameData.get(1)==gameData.get(2)) {
            Log.i("aaaa", "called");
            end=1;
            highlight(0,1,2);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(3)==gameData.get(4) && gameData.get(4)==gameData.get(5)) {
            end=1;
            highlight(3,4,5);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(6)==gameData.get(7) && gameData.get(7)==gameData.get(8)) {
            end=1;
            highlight(6,7,8);
            winnnerDisp();
        }
        if (gameData.get(0)==gameData.get(3) && gameData.get(3)==gameData.get(6)) {
            end=1;
            highlight(0,3,6);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(1)==gameData.get(4) && gameData.get(4)==gameData.get(7)) {
            end=1;
            highlight(1,4,7);
            winnnerDisp();
        }
        if (gameData.get(2)==gameData.get(5) && gameData.get(5)==gameData.get(8)) {
            end=1;
            highlight(2,5,8);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(0)==gameData.get(4) && gameData.get(4)==gameData.get(8)) {
            end=1;
            highlight(0,4,8);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(2)==gameData.get(4) && gameData.get(4)==gameData.get(6)) {
            end=1;
            highlight(2,4,6);
            winnnerDisp();
            winnerDecided=1;
        }
        if (currentPos==8 && end==0){
            winnnerDisp();
        }
    }

    public void winnnerDisp() {
        winner = (TextView) findViewById(R.id.winnerDisplay);
        if (end==0) winner.setText("Game ended in a tie");
        else winner.setText(currentTurn + " won the game");
        flagWinner=1;
        myGameRefPlay.removeEventListener(onlinePlayData);
        restart.setVisibility(View.VISIBLE);
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        if (winnerDecided==0) {
            if (end != 0) {
                if (currentTurn == player1) scorePlayer1++;
                if (currentTurn == player2) scorePlayer2++;
                score1.setText(player1 + ": " + scorePlayer1);
                score2.setText(player2 + ": " + scorePlayer2);
            }
        }
    }

    public void highlight (int h1, int h2, int h3) {
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        String tag1 = "pos_" + h1;
        String tag2 = "pos_" + h2;
        String tag3 = "pos_" + h3;
        TextView tempH1 = tictactoe.findViewWithTag(tag1);
        TextView tempH2 = tictactoe.findViewWithTag(tag2);
        TextView tempH3 = tictactoe.findViewWithTag(tag3);
        tempH1.setBackground(getResources().getDrawable(R.drawable.two));
        tempH2.setBackground(getResources().getDrawable(R.drawable.two));
        tempH3.setBackground(getResources().getDrawable(R.drawable.two));
    }

    public void restartGame(View view) {
        currentPos=0; i=0; end=0; flagWinner=0;
        gameData.clear();
        for (i=0;i<9;i++) gameData.add(Integer.toString(i));
        restart.setVisibility(View.INVISIBLE);
        winner = (TextView) findViewById(R.id.winnerDisplay);
        winner.setText("Game in progress..");
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        for(int j=0;j<9;j++){
            String tagTemp = "pos_" + j;
            TextView tempTX = (TextView) tictactoe.findViewWithTag(tagTemp);
            tempTX.setBackground(getResources().getDrawable(R.drawable.one));
            tempTX.setText(null);
        }
        startGame();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
