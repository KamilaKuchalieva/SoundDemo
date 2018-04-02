package com.kamila.sounddemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private MediaPlayer mediaPlayer;
    private boolean playing = false;
    private SeekBar seekBarVolumeControl, seekBarSeek;
    private AudioManager audioManager;
    private int maxVolume, currentVolume, audioLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton  = findViewById(R.id.playButton);

        mediaPlayer = MediaPlayer.create(this, R.raw.laugh);

        audioLength = mediaPlayer.getDuration();

        // this will allow us to play with the audio system of the device
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // get the max value for music/sound (media) stream of the device
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        // get the current volume
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBarSeek = findViewById(R.id.seekBarSeek);
        seekBarSeek.setMax(audioLength);
        // update the seeBarSeek with the location of our audio file,
        // wait 0 seconds before starting the timer and then run it every half on a second
        // timer will run every second and show the current position of the audio (using seekBarSeek)
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBarSeek.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 500);

        seekBarVolumeControl = findViewById(R.id.seekBarVolume);
        seekBarVolumeControl.setMax(maxVolume);
        seekBarVolumeControl.setProgress(currentVolume);


        // set the volume control notifications
        seekBarVolumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                // set the audio volume according to the user interaction with the seek bar
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        });

        seekBarSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // update the audio streaming position according to the users interaction with the seek bar
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });

        // when the audio is done playing, change the text button and set the 'playing' to false
        mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp){
                playing = false;
                playButton.setText("PLAY");
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playing){
                    playAudio();
                }else{
                    pauseAudio();
                }
            }
        });
    }

    public void playAudio(){
        mediaPlayer.start();
        playing = true;
        playButton.setText("PAUSE");
    }

    public void pauseAudio(){
        mediaPlayer.pause();
        playing = false;
        playButton.setText("PLAY");
    }
}
