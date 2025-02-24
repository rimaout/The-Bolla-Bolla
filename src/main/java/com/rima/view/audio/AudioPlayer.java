package com.rima.view.audio;

import javax.sound.sampled.*;

import static com.rima.view.utilz.Load.GetAudio;
import static com.rima.view.utilz.Constants.AudioConstants.*;

/**
 * Manages audio playback for the game, including songs and sound effects.
 * Implements the singleton pattern to ensure a single instance.
 */
public class AudioPlayer {
    private static AudioPlayer instance;

    private Clip[] songs, soundEffects;
    private int currentSongID;

    private float volume = DEFAULT_VOLUME; // value between 0.0 and 1.0
    private boolean songMuted, soundEffectMuted;

    /**
     * Private constructor to implement singleton pattern.
     * Loads audio files and plays the home sound effect.
     */
    private AudioPlayer() {
        loadAudios();
        playSoundEffect(HOME);
    }

    /**
     * Returns the singleton instance of the AudioPlayer.
     *
     * @return the singleton instance
     */
    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    /**
     * Loads audio files for songs and sound effects.
     */
    private void loadAudios() {
        // Load songs
        String[] songNames = {"song-intro-and-playing", "song-playing"};
        songs = new Clip[songNames.length];
        for (int i = 0; i < songNames.length; i++)
            songs[i] = GetAudio(songNames[i]);

        // Load sound effects
        String[] soundEffectNames = {"sfx-home", "sfx-jump", "sfx-player-death", "sfx-bubble-shoot", "sfx-enemy-bubble-pop", "sfx-water-flow", "sfx-lightning", "sfx-reward-collected", "sfx-powerup-collected", "sfx-game-over", "sfx-game-completed", "sfx-hurry-up"};
        soundEffects = new Clip[soundEffectNames.length];
        for (int i = 0; i < soundEffectNames.length; i++)
            soundEffects[i] = GetAudio(soundEffectNames[i]);

        setSoundEffectVolume();
    }

    /**
     * Sets the volume for both songs and sound effects.
     *
     * @param volume the volume level to set
     */
    public void setVolume(float volume) {
        this.volume = volume;
        setSongVolume();
        setSoundEffectVolume();
    }

    /**
     * Sets the volume for the songs.
     */
    private void setSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);

        // Calculate the gain value (make volume consistent across all songs)
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    /**
     * Sets the volume for all sound effects.
     */
    private void setSoundEffectVolume() {

        for (Clip c : soundEffects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);

            // Calculate the gain value (make volume consistent across all sound effects)
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    /**
     * Stops the current song.
     */
    public void stopSong() {
        songs[currentSongID].stop();
    }

    /**
     * Starts the current song if it's not already playing.
     */
    public void startSong() {
        if (!songs[currentSongID].isActive())
            songs[currentSongID].start();
    }

    /**
     * Plays the intro song and sets up a listener to play the main song after the intro ends.
     */
    public void playIntroSong() {
        currentSongID = INTO_AND_PLAYING_SONG;
        setSongVolume();

        // Create a listener to play the playing song after the intro song ends
        LineListener listener = new LineListener() {
            @Override
            public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                    songs[currentSongID].removeLineListener(this);
                    playPlayingSong();
                }
            }
        };

        // Add the listener to the intro song
        songs[currentSongID].addLineListener(listener);

        // Start playing the intro song
        songs[currentSongID].setFramePosition(0);
        songs[currentSongID].start();
    }

    /**
     * Plays the main playing song in a loop.
     */
    public void playPlayingSong() {
        stopSong();
        currentSongID = PLAYING_SONG;
        setSongVolume();
        songs[currentSongID].setFramePosition(0);
        songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Plays the specified sound effect.
     *
     * @param soundEffectID the ID of the sound effect to play
     */
    public void playSoundEffect(int soundEffectID) {
        soundEffects[soundEffectID].start();
        soundEffects[soundEffectID].setFramePosition(0);
    }
}