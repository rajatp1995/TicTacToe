package com.rajatp.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class GameActivity extends AppCompatActivity {

    int currentPos=0, i=0, end=0, flagWinner=0, winnerDecided=0, gameCounter=1;
    List<String> gameData = new ArrayList<>();
    String player1="", player2="", lastChance="";
    TextView player1TV, player2TV, winner, score1, score2, current;
    TableLayout tictactoe;
    Button restart;
    int scorePlayer1=0, scorePlayer2=0;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mPlayer = MediaPlayer.create(this, R.raw.chanceplayednew);
        for (i=0;i<9;i++) gameData.add(Integer.toString(i));
        player1 = (String) getIntent().getSerializableExtra("player1");
        player2 = (String) getIntent().getSerializableExtra("player2");
        Toast.makeText(GameActivity.this, player1 + " starts with X", Toast.LENGTH_SHORT).show();
        player1TV = (TextView) findViewById(R.id.player1Name);
        player1TV.setText("Player 1 is " + player1 + ", playing as X");
        player2TV = (TextView) findViewById(R.id.player2Name);
        player2TV.setText("Player 2 is " + player2 + ", playing as O");
        restart = (Button) findViewById(R.id.restartGame_button);
        restart.setVisibility(View.GONE);
        current = (TextView) findViewById(R.id.currentTurn);
        current.setText("Current Turn: " + player1);
        score1 = (TextView) findViewById(R.id.scoreP1);
        score2 = (TextView) findViewById(R.id.scoreP2);
        score1.setText(player1 + ": " + scorePlayer1 + " out of " + (gameCounter-1));
        score2.setText(player2 + ": " + scorePlayer2 + " out of " + (gameCounter-1));
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        for(int j=0;j<9;j++){
            String tagTemp = "pos_" + j;
            TextView tempTX = (TextView) tictactoe.findViewWithTag(tagTemp);
            tempTX.setBackground(getResources().getDrawable(R.drawable.one));
            tempTX.setText("");
        }
    }

    public void chancePlayed (View view) {
        if (flagWinner==0) {
            String posClicked = view.getTag().toString();
            String[] temp = posClicked.split("_");
            int actualPos = Integer.valueOf(temp[1]);
            Log.i("ActualPos", Integer.toString(actualPos));
            TextView txt = (TextView) view;
            if (txt.getText().length() != 0) {
                Toast.makeText(GameActivity.this, "Occupied", Toast.LENGTH_SHORT).show();
            } else {
                if (currentPos % 2 == 0) {
                    txt.setText("X");
                    txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mPlayer.start();
                    gameData.remove(actualPos);
                    gameData.add(actualPos, "X");
                    lastChance = player1;
                    current.setText("Current Turn: " + player2);
                } else {
                    txt.setText("O");
                    txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mPlayer.start();
                    gameData.remove(actualPos);
                    gameData.add(actualPos, "Y");
                    lastChance = player2;
                    current.setText("Current Turn: " + player1);
                }
                checkWinner();
                currentPos++;
            }
        }
        else {
            Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkWinner() {
        if (gameData.get(0)==gameData.get(1) && gameData.get(1)==gameData.get(2)) {
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
            winnerDecided=1;
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
            winnerDecided=1;
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
        else winner.setText(lastChance + " won the game");
        flagWinner=1;
        score1.setText(player1 + ": " + scorePlayer1 + " out of " + gameCounter);
        score2.setText(player2 + ": " + scorePlayer2 + " out of " + gameCounter);
        restart.setVisibility(View.VISIBLE);
        current.setVisibility(View.GONE);
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        if (winnerDecided==0) {
            if (end != 0) {
                if (lastChance == player1) scorePlayer1++;
                if (lastChance == player2) scorePlayer2++;
                score1.setText(player1 + ": " + scorePlayer1 + " out of " + gameCounter);
                score2.setText(player2 + ": " + scorePlayer2 + " out of " + gameCounter);
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
        gameCounter++;
        currentPos=0; i=0; end=0; flagWinner=0; winnerDecided=0;
        gameData.clear();
        for (i=0;i<9;i++) gameData.add(Integer.toString(i));
        restart.setVisibility(View.GONE);
        current.setVisibility(View.VISIBLE);
        winner = (TextView) findViewById(R.id.winnerDisplay);
        winner.setText("Game in progress..");
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        for(int j=0;j<9;j++){
            String tagTemp = "pos_" + j;
            TextView tempTX = (TextView) tictactoe.findViewWithTag(tagTemp);
            tempTX.setBackground(getResources().getDrawable(R.drawable.one));
            tempTX.setText(null);
        }
        current.setText("Current Turn: " + player1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
