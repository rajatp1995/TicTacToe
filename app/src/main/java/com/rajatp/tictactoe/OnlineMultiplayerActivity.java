package com.rajatp.tictactoe;

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
    String player, player1="", player2="", gameRoom="", lastChance="";
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
            myGameRefPlay.child("onlineData").setValue("1,2,3,4,5,6,7,8,9");
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
        onlinePlayData = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.child("onlineData").getValue().toString();
                String[] dataToList = data.split(",");
                gameData = new ArrayList<String>(Arrays.asList(dataToList));
                if (!dataSnapshot.child("currentTurn").exists()) {
                    myGameRefPlay.child("currentTurn").setValue(player1);
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
                Log.i("aaaa", gameData.get(j));
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
    }



}
