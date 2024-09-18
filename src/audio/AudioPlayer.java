package audio;

import javax.sound.sampled.*;
import static utilz.LoadSave.GetAudio;
import static utilz.Constants.AudioConstants.*;

public class AudioPlayer {
    private static AudioPlayer instance;

    private Clip[] songs, soundEffects;
    private int currentSongID;

    private float volume = DEFAULT_VOLUME;
    private boolean songMuted, soundEffectMuted;

    private AudioPlayer() {
        loadAudios();
        playSoundEffect(HOME);
    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

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

    public void setVolume(float volume) {
        this.volume = volume;
        setSongVolume();
        setSoundEffectVolume();
    }

    private void setSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);

        // Calculate the gain value (make volume consistent across all songs)
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void setSoundEffectVolume() {

        for (Clip c : soundEffects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);

            // Calculate the gain value (make volume consistent across all sound effects)
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    public void stopSong() {
        if (songs[currentSongID].isActive())
            songs[currentSongID].stop();
    }

    public void startSong() {
        if (!songs[currentSongID].isActive())
            songs[currentSongID].start();
    }

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

    public void playPlayingSong() {
        stopSong();
        currentSongID = PLAYING_SONG;
        setSongVolume();
        songs[currentSongID].setFramePosition(0);
        songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSoundEffect(int soundEffectID) {
        soundEffects[soundEffectID].start();
        soundEffects[soundEffectID].setFramePosition(0);
    }

    public void toggleSongMute() {
        this.songMuted = !songMuted;

        for (Clip c : songs) {
            BooleanControl muteControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(songMuted);
        }
    }

    public void toggleSoundEffectMute() {
        soundEffectMuted = !soundEffectMuted;

        for (Clip c : soundEffects) {
            BooleanControl muteControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(soundEffectMuted);
        }
    }
}
