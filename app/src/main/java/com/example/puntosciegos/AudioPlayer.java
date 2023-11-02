package com.example.puntosciegos;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.RawRes;

public class AudioPlayer {
    private MediaPlayer audioPlayer;
    private boolean reproduciendoSensorIzquierdo = false;
    private boolean reproduciendoSensorAtras = false;
    private boolean reproduciendoSensorDerecho = false;

    public AudioPlayer(Context context, @RawRes int audioResourceId) {
        audioPlayer = MediaPlayer.create(context, audioResourceId);
    }

    private boolean playPausaAudio(float distancia) {
        if (distancia < 5) {
            if (audioPlayer.isPlaying()) {
                audioPlayer.stop();
            }
        }
        if (distancia >= 5 && distancia < 150) {
            if (!audioPlayer.isPlaying()) {
                audioPlayer.start();
            }
        }
        if (distancia >= 150 && distancia < 250) {
            if (!audioPlayer.isPlaying()) {
                audioPlayer.start();
            }
        }
        if (distancia >= 250 && distancia < 350) {
            if (audioPlayer.isPlaying()) {
                audioPlayer.stop();
            }
        }
        if (distancia >= 350) {
            if (audioPlayer.isPlaying()) {
                audioPlayer.stop();
            }
        }
        return audioPlayer.isPlaying();
    }

    protected void audioSensorIzquierdo(float distancia) {
        if (audioPlayer.isPlaying() && audioPlayer.getCurrentPosition() != 0) {
            return;
        }
        if (reproduciendoSensorAtras || reproduciendoSensorDerecho) {
            reproduciendoSensorIzquierdo = false;
        }
        reproduciendoSensorIzquierdo = playPausaAudio(distancia);
    }

    protected void audioSensorAtras(float distancia) {
        if (audioPlayer.isPlaying() && audioPlayer.getCurrentPosition() != 0) {
            return;
        }
        if (reproduciendoSensorIzquierdo || reproduciendoSensorDerecho) {
            reproduciendoSensorAtras = false;
        }
        reproduciendoSensorAtras = playPausaAudio(distancia);
    }
    protected void audioSensorDerecho(float distancia) {
        if (audioPlayer.isPlaying() && audioPlayer.getCurrentPosition() != 0) {
            return;
        }
        if (reproduciendoSensorAtras || reproduciendoSensorIzquierdo) {
            reproduciendoSensorDerecho = false;
        }
        reproduciendoSensorDerecho = playPausaAudio(distancia);
    }


    protected boolean isPlaying() {
        return audioPlayer.isPlaying();
    }
    public void release() {
        audioPlayer.release();
    }
}
