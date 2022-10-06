package com.example.audioplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //initialization
      TextView playerPosition,playerDuration;
      SeekBar seekBar;
      Button btRewind,btPlay,btPause,btFf;
      MediaPlayer mediaPlayer;
      Handler handler;
      Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //assign
        playerPosition=findViewById(R.id.player_position);
        playerDuration=findViewById(R.id.player_duration);
        seekBar= findViewById(R.id.seek_bar);
        btRewind=findViewById(R.id.bt_rew);
        btPlay=findViewById(R.id.bt_play);
        btPause=findViewById(R.id.bt_pause);
        btFf=findViewById(R.id.bt_ff);
        buttonMethod();

    }
    public void buttonMethod(){
        handler= new Handler();
        //initialize media player
        mediaPlayer =  MediaPlayer.create(this,R.raw.iron_man);
        //initialize runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //handler post delay
                handler.postDelayed(this,500);
            }
        };
        //get duration from media player
        int duration = mediaPlayer.getDuration();
        //convert milliseconds to sec and min
        String sDuration = convertFormat(duration);
        // set duration to textView
        playerDuration.setText(sDuration);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // visibility gone to play button
                btPlay.setVisibility(view.GONE);
                //set pause button visible
                btPause.setVisibility(View.VISIBLE);
                //start media player
                mediaPlayer.start();
                //set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable,0);
            }
        });
        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set play button to visible
                btPlay.setVisibility(view.VISIBLE);
                //set pause button to gone
                btPause.setVisibility(View.GONE);
                //pause the media player
                mediaPlayer.pause();
                //remove handlers
                handler.removeCallbacks(runnable);
            }
        });
        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current position of the media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //get duration of the media player
                int duration =mediaPlayer.getDuration();
                //set condition
                if(mediaPlayer.isPlaying() && duration!=currentPosition){
                    //if media player is playing and media duration is not equal to current position
                    //fast forward current position
                    currentPosition=currentPosition+5000;
                    //set current position to Textview
                    playerPosition.setText(convertFormat(currentPosition));
                    //set seekbar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        btRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //set condition
                if(mediaPlayer.isPlaying() && currentPosition>5000){
                    //if media player is playing and current duration is greater than 5 sec
                    //rewind the current position
                    currentPosition=currentPosition-5000;
                    // current position to Textview
                    playerPosition.setText(convertFormat(currentPosition));
                    //set seekbar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
                // when seekbar is draged
                if(fromuser){
                    mediaPlayer.seekTo(progress);
                }
                //set position in textView
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //set pause button to gone
                btPause.setVisibility(View.GONE);
                //set play button to visible
                btPlay.setVisibility(View.VISIBLE);
                //set media player to initial position
                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),TimeUnit.MILLISECONDS.toSeconds(duration)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}