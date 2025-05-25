package com.myteam.rpgsurvivor.controller.system;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;

public class AudioManager implements Disposable {
    private static AudioManager instance;

    private Music menuMusic;
    private Music gameMusic;
    private Music currentMusic;

    private Sound btnClicked;
    private Sound atkSound;

    private float musicVolume = 0.7f;
    private float soundVolume = 0.8f;
    private boolean musicEnabled = true;
    private boolean soundEnabled = true;

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    private AudioManager() {
        loadAudio();
    }

    private void loadAudio() {
        try
        {
            menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/10- Power to the hero.mp3"));
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/gaming-sound-143716.mp3"));

            btnClicked = Gdx.audio.newSound(Gdx.files.internal("Music/robot_01-47250.mp3"));
            atkSound = Gdx.audio.newSound(Gdx.files.internal("Music/melee-attack-with-voice-1-104995.mp3"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }   

    public void playMenuMusic() {
        if (!musicEnabled || menuMusic == null) return;

        stopCurrentMusic();
        currentMusic = menuMusic;
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    public void playGameMusic() {
        if (!musicEnabled || gameMusic == null) return;

        stopCurrentMusic();
        currentMusic = gameMusic;
        gameMusic.setLooping(true);
        gameMusic.play();

    }

    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    public void resumeMusic() {
        if (currentMusic != null) {
            currentMusic.play();
        }
    }

    public void stopCurrentMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void playButtonClickSound() {
        if (soundEnabled && btnClicked != null) {
            btnClicked.play(soundVolume);
        }
    }

    public void playAtkSound() {
        if (soundEnabled && atkSound != null) {
            atkSound.play(soundVolume);
        }
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0f, Math.min(1f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(this.musicVolume);
        }
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = Math.max(0f, Math.min(1f, volume));
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopCurrentMusic();
        }
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public Sound getAtkSound() {
        return atkSound;
    }

    public void setAtkSound(Sound atkSound) {
        this.atkSound = atkSound;
    }

    public Sound getBtnClicked() {
        return btnClicked;
    }

    public void setBtnClicked(Sound btnClicked) {
        this.btnClicked = btnClicked;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
    }

    public Music getGameMusic() {
        return gameMusic;
    }

    public void setGameMusic(Music gameMusic) {
        this.gameMusic = gameMusic;
    }

    public Music getMenuMusic() {
        return menuMusic;
    }

    public void setMenuMusic(Music menuMusic) {
        this.menuMusic = menuMusic;
    }

    @Override
    public void dispose() {
        menuMusic.dispose();
        gameMusic.dispose();
        btnClicked.dispose();
        atkSound.dispose();
    }
}
