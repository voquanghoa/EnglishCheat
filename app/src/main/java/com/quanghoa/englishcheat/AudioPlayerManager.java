package com.quanghoa.englishcheat;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by voqua on 3/23/2016.
 */
public class AudioPlayerManager {
    public void play(String url) throws IOException {
        final MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDataSource(url);
        player.prepare();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });
        player.start();
    }
}
