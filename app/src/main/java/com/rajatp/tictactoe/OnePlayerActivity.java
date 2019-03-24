package com.rajatp.tictactoe;

import android.content.Intent;
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
import java.util.List;
import java.util.Random;

public class OnePlayerActivity extends AppCompatActivity {

    int currentPos=0, i=0, actualPos=0, flag=0, end=0, flagWinner=0, winnerDecided=0, lock=0, possibilityFlag=0;
    List<String> gameData = new ArrayList<>();
    String player1="", player2="", lastChance="";
    TextView player1TV, player2TV, winner, txt, score1, score2;
    TableLayout tictactoe;
    Button restart;
    int scorePlayer1=0, scorePlayer2=0;
    Random randValue = new Random();
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mPlayer = MediaPlayer.create(this, R.raw.chanceplayednew);
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        for (i=0;i<9;i++) gameData.add(Integer.toString(i));
        player1 = (String) getIntent().getSerializableExtra("player1");
        player2 = "Android";
        Toast.makeText(OnePlayerActivity.this, player1 + " starts with X", Toast.LENGTH_SHORT).show();
        player1TV = (TextView) findViewById(R.id.player1Name);
        player1TV.setText("Player 1 is " + player1 + ", playing as X");
        player2TV = (TextView) findViewById(R.id.player2Name);
        player2TV.setText("Player 2 is " + player2 + ", playing as O");
        restart = (Button) findViewById(R.id.restartGame_button);
        restart.setVisibility(View.INVISIBLE);
        score1 = (TextView) findViewById(R.id.scoreP1);
        score2 = (TextView) findViewById(R.id.scoreP2);
        score1.setText(player1 + ": " + scorePlayer1);
        score2.setText(player2 + ": " + scorePlayer2);
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
            if (lock==0) {
                String posClicked = view.getTag().toString();
                String[] temp = posClicked.split("_");
                actualPos = Integer.valueOf(temp[1]);
                txt = (TextView) view;
                if (txt.getText().length() != 0) {
                    Toast.makeText(OnePlayerActivity.this, "Occupied", Toast.LENGTH_SHORT).show();
                } else {
                    txt.setText("X");
                    txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mPlayer.start();
                    gameData.remove(actualPos);
                    gameData.add(actualPos, "X");
                    lastChance = player1;
                    checkWinner();
                    if (flag != 1) {
                        currentPos++;
                        playCPU();
                    }
                }
            } else Toast.makeText(OnePlayerActivity.this, "Wait for your turn..", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(OnePlayerActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
    }

    public void playCPU() {
        lock=1;
        Toast.makeText(OnePlayerActivity.this, "Android is thinking..", Toast.LENGTH_SHORT).show();
        new CountDownTimer(1500, 500) {
            @Override
            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                int possibilityCheck=0;
                List <Integer> possibility = new ArrayList<>();
                possibility.clear();
                possibility = probablisticMove(gameData);

                if (possibility.size()==0) {
                    List <Integer> AndroidWin = new ArrayList<>();
                    AndroidWin.clear();
                    AndroidWin = chanceToWin(possibility, gameData);
                    if (AndroidWin.size()!=0) {
                        int rIndex = randValue.nextInt(AndroidWin.size());
                        int rvalue = AndroidWin.get(rIndex);
                        if(gameData.get(rvalue)=="X" || gameData.get(rvalue)=="O") playCPU();
                        else {
                            String tag = "pos_"+rvalue;
                            txt = tictactoe.findViewWithTag(tag);
                            txt.setText("O");
                            txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            mPlayer.start();
                            gameData.remove(rvalue);
                            gameData.add(rvalue, "O");
                            lastChance = player2;
                            checkWinner();
                            currentPos++;
                            lock=0;
                        }
                    } else {
                        playRandom();
                    }
                }
                else {
                    possibilityFlag=1;
                    for (int u=0;u<possibility.size();u++) {
                        if (gameData.get(possibility.get(u))=="X" || gameData.get(possibility.get(u))=="O") possibilityCheck++;
                    }
                    if (possibilityCheck==possibility.size()) {
                        List <Integer> AndroidWin = new ArrayList<>();
                        AndroidWin.clear();
                        AndroidWin = chanceToWin(possibility, gameData);
                        if (AndroidWin.size()!=0) {
                            int rIndex = randValue.nextInt(AndroidWin.size());
                            int rvalue = AndroidWin.get(rIndex);
                            if(gameData.get(rvalue)=="X" || gameData.get(rvalue)=="O") playCPU();
                            else {
                                String tag = "pos_"+rvalue;
                                txt = tictactoe.findViewWithTag(tag);
                                txt.setText("O");
                                txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                mPlayer.start();
                                gameData.remove(rvalue);
                                gameData.add(rvalue, "O");
                                lastChance = player2;
                                checkWinner();
                                currentPos++;
                                lock=0;
                            }
                        } else {
                            playRandom();
                        }
                    }
                }
                if (possibilityCheck<possibility.size() && possibilityFlag==1) {
                    List <Integer> AndroidWin = new ArrayList<>();
                    AndroidWin.clear();
                    AndroidWin = chanceToWin(possibility, gameData);
                    if (AndroidWin.size()!=0) {
                        int rIndex = randValue.nextInt(AndroidWin.size());
                        int rvalue = AndroidWin.get(rIndex);
                        if(gameData.get(rvalue)=="X" || gameData.get(rvalue)=="O") playCPU();
                        else {
                            String tag = "pos_"+rvalue;
                            txt = tictactoe.findViewWithTag(tag);
                            txt.setText("O");
                            txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            mPlayer.start();
                            gameData.remove(rvalue);
                            gameData.add(rvalue, "O");
                            lastChance = player2;
                            checkWinner();
                            currentPos++;
                            lock=0;
                        }
                    } else {
                        int rIndex = randValue.nextInt(possibility.size());
                        int rvalue = possibility.get(rIndex);
                        if (gameData.get(rvalue) == "X" || gameData.get(rvalue) == "O") playCPU();
                        else {
                            String tag = "pos_" + rvalue;
                            txt = tictactoe.findViewWithTag(tag);
                            txt.setText("O");
                            txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            mPlayer.start();
                            gameData.remove(rvalue);
                            gameData.add(rvalue, "O");
                            lastChance = player2;
                            checkWinner();
                            currentPos++;
                            lock = 0;
                        }
                    }
                }
            }
        }.start();
    }

    public void checkWinner() {
        if (gameData.get(0)==gameData.get(1) && gameData.get(1)==gameData.get(2)) {
            flag=1;
            end=1;
            highlight(0,1,2);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(3)==gameData.get(4) && gameData.get(4)==gameData.get(5)) {
            flag=1;
            end=1;
            highlight(3,4,5);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(6)==gameData.get(7) && gameData.get(7)==gameData.get(8)) {
            flag=1;
            end=1;
            highlight(6,7,8);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(0)==gameData.get(3) && gameData.get(3)==gameData.get(6)) {
            flag=1;
            end=1;
            highlight(0,3,6);
            winnnerDisp();
            winnerDecided=1;

        }
        if (gameData.get(1)==gameData.get(4) && gameData.get(4)==gameData.get(7)) {
            flag=1;
            end=1;
            highlight(1,4,7);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(2)==gameData.get(5) && gameData.get(5)==gameData.get(8)) {
            flag=1;
            end=1;
            highlight(2,5,8);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(0)==gameData.get(4) && gameData.get(4)==gameData.get(8)) {
            flag=1;
            end=1;
            highlight(0,4,8);
            winnnerDisp();
            winnerDecided=1;
        }
        if (gameData.get(2)==gameData.get(4) && gameData.get(4)==gameData.get(6)) {
            flag=1;
            end=1;
            highlight(2,4,6);
            winnnerDisp();
            winnerDecided=1;
        }
        if (currentPos==8 && end==0){
            winnnerDisp();
            flag=1;
        }
    }

    public void winnnerDisp() {
        winner = (TextView) findViewById(R.id.winnerDisplay);
        if (end==0) winner.setText("Game ended in a tie");
        else winner.setText(lastChance + " won the game");
        flagWinner=1;
        restart.setVisibility(View.VISIBLE);
        tictactoe = (TableLayout) findViewById(R.id.ticTacToeTable);
        if (winnerDecided==0) {
            if (end != 0) {
                if (lastChance == player1) scorePlayer1++;
                if (lastChance == player2) scorePlayer2++;
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
        currentPos=0; i=0; actualPos=0; flag=0; end=0; flagWinner=0; winnerDecided=0; lock=0; possibilityFlag=0;
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
    }

    public List<Integer> probablisticMove (List<String> gameInfo) {
        List<Integer> possibleMoves = new ArrayList<>();
        possibleMoves.clear();

        for (int i=0;i<3;i++) {
            if (gameInfo.get(i)=="X" && gameInfo.get(i+3)=="X") possibleMoves.add(i+6);
            if (gameInfo.get(i+3)=="X" && gameInfo.get(i+6)=="X") possibleMoves.add(i);
            if (gameInfo.get(i)=="X" && gameInfo.get(i+6)=="X") possibleMoves.add(i+3);
        }

        for (int j=0;j<=6;j=j+3) {
            if (gameInfo.get(j)=="X" && gameInfo.get(j+1)=="X") possibleMoves.add(j+2);
            if (gameInfo.get(j+1)=="X" && gameInfo.get(j+2)=="X") possibleMoves.add(j);
            if (gameInfo.get(j)=="X" && gameInfo.get(j+2)=="X") possibleMoves.add(j+1);
        }

        if (gameInfo.get(0)=="X" && gameInfo.get(4)=="X") possibleMoves.add(8);
        if (gameInfo.get(4)=="X" && gameInfo.get(8)=="X") possibleMoves.add(0);
        if (gameInfo.get(0)=="X" && gameInfo.get(8)=="X") possibleMoves.add(4);

        if (gameInfo.get(2)=="X" && gameInfo.get(4)=="X") possibleMoves.add(6);
        if (gameInfo.get(6)=="X" && gameInfo.get(4)=="X") possibleMoves.add(2);
        if (gameInfo.get(2)=="X" && gameInfo.get(6)=="X") possibleMoves.add(4);

        return possibleMoves;
    }

    public void playRandom() {
        int rvalue = randValue.nextInt(9);
        if(gameData.get(rvalue)=="X" || gameData.get(rvalue)=="O") playCPU();
        else {
            String tag = "pos_"+rvalue;
            txt = tictactoe.findViewWithTag(tag);
            txt.setText("O");
            txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mPlayer.start();
            gameData.remove(rvalue);
            gameData.add(rvalue, "O");
            lastChance = player2;
            checkWinner();
            currentPos++;
            lock=0;
        }
    }

    public List<Integer> chanceToWin(List<Integer> possibility, List<String> gameInfo) {
        List<Integer> possibleMovesToWin = new ArrayList<>();
        possibleMovesToWin.clear();

        for (int i=0;i<3;i++) {
            if (gameInfo.get(i)=="O" && gameInfo.get(i+3)=="O") possibleMovesToWin.add(i+6);
            if (gameInfo.get(i+3)=="O" && gameInfo.get(i+6)=="O") possibleMovesToWin.add(i);
            if (gameInfo.get(i)=="O" && gameInfo.get(i+6)=="O") possibleMovesToWin.add(i+3);
        }

        for (int j=0;j<=6;j=j+3) {
            if (gameInfo.get(j)=="O" && gameInfo.get(j+1)=="O") possibleMovesToWin.add(j+2);
            if (gameInfo.get(j+1)=="O" && gameInfo.get(j+2)=="O") possibleMovesToWin.add(j);
            if (gameInfo.get(j)=="O" && gameInfo.get(j+2)=="O") possibleMovesToWin.add(j+1);
        }

        if (gameInfo.get(0)=="O" && gameInfo.get(4)=="O") possibleMovesToWin.add(8);
        if (gameInfo.get(4)=="O" && gameInfo.get(8)=="O") possibleMovesToWin.add(0);
        if (gameInfo.get(0)=="O" && gameInfo.get(8)=="O") possibleMovesToWin.add(4);

        if (gameInfo.get(2)=="O" && gameInfo.get(4)=="O") possibleMovesToWin.add(6);
        if (gameInfo.get(6)=="O" && gameInfo.get(4)=="O") possibleMovesToWin.add(2);
        if (gameInfo.get(2)=="O" && gameInfo.get(6)=="O") possibleMovesToWin.add(4);

        if(possibleMovesToWin.size()==1)
        {
            if (gameInfo.get(possibleMovesToWin.get(0))=="X" || gameInfo.get(possibleMovesToWin.get(0))=="O") {
                possibleMovesToWin.remove(0);
            }
        }

        if(possibleMovesToWin.size()==2)
        {
            if (gameInfo.get(possibleMovesToWin.get(0))=="X" || gameInfo.get(possibleMovesToWin.get(0))=="O") {
                possibleMovesToWin.remove(0);
            }
            if (gameInfo.get(possibleMovesToWin.get(0))=="X" || gameInfo.get(possibleMovesToWin.get(0))=="O") {
                possibleMovesToWin.remove(0);
            }
        }

        Log.i("Moves to Block", possibility.toString());
        Log.i("Moves to Win", possibleMovesToWin.toString());
        return possibleMovesToWin;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}