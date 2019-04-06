package com.rajatp.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myGameRef;
    ValueEventListener onlineData;
    RelativeLayout tempLayout, tempLayout2, tempLayout3;
    Button twoPlay, onePlay, multiPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempLayout = (RelativeLayout) findViewById(R.id.temp1);
        tempLayout2 = (RelativeLayout) findViewById(R.id.temp2);
        tempLayout3 = (RelativeLayout) findViewById(R.id.temp3);
        twoPlay = (Button) findViewById(R.id.twoPlayer_button);
        onePlay = (Button) findViewById(R.id.onePlayer_button);
        multiPlay = (Button) findViewById(R.id.multiPlayer_button);
        tempLayout.setVisibility(View.GONE);
        tempLayout2.setVisibility(View.GONE);
        tempLayout3.setVisibility(View.GONE);
    }

    public void startTwoPlayers(View view) {
        twoPlay.setVisibility(View.GONE);
        onePlay.setVisibility(View.GONE);
        multiPlay.setVisibility(View.GONE);
        tempLayout.setVisibility(View.VISIBLE);
    }

    public void startOnePlayer(View view) {
        twoPlay.setVisibility(View.GONE);
        onePlay.setVisibility(View.GONE);
        multiPlay.setVisibility(View.GONE);
        tempLayout2.setVisibility(View.VISIBLE);
    }

    public void startmultiPlayer(View view) {
        twoPlay.setVisibility(View.GONE);
        onePlay.setVisibility(View.GONE);
        multiPlay.setVisibility(View.GONE);
        tempLayout3.setVisibility(View.VISIBLE);
    }

    public void startPlayer2Game(View view) {
        EditText name1 = (EditText) findViewById(R.id.player1Details);
        EditText name2 = (EditText) findViewById(R.id.player2Details);
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra("player1", name1.getText().toString());
        intent.putExtra("player2", name2.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startPlayer1Game(View view) {
        EditText name1 = (EditText) findViewById(R.id.onePlayerDetails);
        if (name1.getText().toString().equalsIgnoreCase("crazy")) {
            Intent intent = new Intent(getApplicationContext(), CPUvsCPUActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            Intent intent = new Intent(getApplicationContext(), OnePlayerActivity.class);
            intent.putExtra("player1", name1.getText().toString());
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public void startmultiPlayerGame(View view) {
        final EditText localPlayer = (EditText) findViewById(R.id.localplayerDetails);
        final EditText gameRoom = (EditText) findViewById(R.id.roomDetails);
        myGameRef = database.getReference(gameRoom.getText().toString());
        String tempPlayerKey = myGameRef.push().getKey();
        myGameRef.child(tempPlayerKey).setValue(localPlayer.getText().toString());
        Intent intent = new Intent(getApplicationContext(), OnlineMultiplayerActivity.class);
        intent.putExtra("gameRoom", gameRoom.getText().toString());
        intent.putExtra("localPlayer", localPlayer.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
