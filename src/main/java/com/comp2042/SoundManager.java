package com.comp2042;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

@SuppressWarnings("unused")
public final class SoundManager {

    private static final SoundManager INSTANCE = new SoundManager();

    public static SoundManager getInstance() {
        return INSTANCE;
    }


    private MediaPlayer bgmPlayer;
    private double bgmVolume = 0.6;

    private final AudioClip placeBlockClip;
    private final AudioClip elimBlockClip;
    private final AudioClip rowElimClip;
    private final AudioClip playerDieClip;
    private final AudioClip gameEndClip;
    private final AudioClip buttonClickClip;
    private final AudioClip tntPlaceClip;
    private final AudioClip tntExplodeClip;
    private final AudioClip lavaClip;
    private final AudioClip zombieSpawnClip;
    private final AudioClip zombieDeathClip;
    private final AudioClip skeletonSpawnClip;

    private double placeBlockVolume   = 1.0;
    private double elimBlockVolume    = 1.0;
    private double rowElimVolume      = 0.8;
    private double playerDieVolume    = 1.0;
    private double gameEndVolume      = 1.0;
    private double buttonClickVolume  = 0.7;
    private double tntPlaceVolume     = 0.5;
    private double tntExplodeVolume   = 0.5;
    private double lavaVolume         = 0.3;
    private double zombieSpawnVolume  = 0.6;
    private double zombieDeathVolume  = 0.5;
    private double skeletonSpawnVolume= 0.2;

    private SoundManager() {
        try {
            URL bgmUrl = getClass().getResource("/sounds/bgm.mp3");
            if (bgmUrl != null) {
                Media media = new Media(bgmUrl.toExternalForm());
                bgmPlayer = new MediaPlayer(media);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.setVolume(bgmVolume);
            } else {
                System.out.println("BGM not found: /sounds/bgm.ogg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load SFX
        placeBlockClip    = loadClip("/sounds/place_block.mp3");
        elimBlockClip     = loadClip("/sounds/elim_block.mp3");
        rowElimClip       = loadClip("/sounds/row_elim.mp3");
        playerDieClip     = loadClip("/sounds/player_die.mp3");
        gameEndClip       = loadClip("/sounds/game_end.mp3");
        buttonClickClip   = loadClip("/sounds/button_click.mp3");
        tntPlaceClip      = loadClip("/sounds/tnt_place.mp3");
        tntExplodeClip    = loadClip("/sounds/tnt_explode.mp3");
        lavaClip          = loadClip("/sounds/lava.mp3");
        zombieSpawnClip   = loadClip("/sounds/zombie_spawn.mp3");
        zombieDeathClip   = loadClip("/sounds/zombie_death.mp3");
        skeletonSpawnClip = loadClip("/sounds/skeleton_spawn.mp3");
    }

    private AudioClip loadClip(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                return new AudioClip(url.toExternalForm());
            } else {
                System.out.println("Sound not found: " + path);
            }
        } catch (Exception e) {
            System.out.println("Error loading sound: " + path);
            e.printStackTrace();
        }
        return null;
    }


    public void playBgm() {
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(bgmVolume);
            bgmPlayer.play();
        }
    }

    public void setBgmVolume(double volume) {
        bgmVolume = clamp(volume);
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(bgmVolume);
        }
    }


    public void playPlaceBlock() {
        if (placeBlockClip != null) {
            placeBlockClip.setVolume(placeBlockVolume);
            placeBlockClip.play();
        }
    }

    public void playElimBlock() {
        if (elimBlockClip != null) {
            elimBlockClip.setVolume(elimBlockVolume);
            elimBlockClip.play();
        }
    }

    public void playRowElim() {
        if (rowElimClip != null) {
            rowElimClip.setVolume(rowElimVolume);
            rowElimClip.play();
        }
    }

    public void playPlayerDie() {
        if (playerDieClip != null) {
            playerDieClip.setVolume(playerDieVolume);
            playerDieClip.play();
        }
    }

    public void playGameEnd() {
        if (gameEndClip != null) {
            gameEndClip.setVolume(gameEndVolume);
            gameEndClip.play();
        }
    }

    public void playButtonClick() {
        if (buttonClickClip != null) {
            buttonClickClip.setVolume(buttonClickVolume);
            buttonClickClip.play();
        }
    }

    public void playTntPlace() {
        if (tntPlaceClip != null) {
            tntPlaceClip.setVolume(tntPlaceVolume);
            tntPlaceClip.play();
        }
    }

    public void playTntExplode() {
        if (tntExplodeClip != null) {
            tntExplodeClip.setVolume(tntExplodeVolume);
            tntExplodeClip.play();
        }
    }

    public void playLava() {
        if (lavaClip != null) {
            lavaClip.setVolume(lavaVolume);
            lavaClip.play();
        }
    }

    public void playZombieSpawn() {
        if (zombieSpawnClip != null) {
            zombieSpawnClip.setVolume(zombieSpawnVolume);
            zombieSpawnClip.play();
        }
    }

    public void playZombieDeath() {
        if (zombieDeathClip != null) {
            zombieDeathClip.setVolume(zombieDeathVolume);
            zombieDeathClip.play();
        }
    }

    public void playSkeletonSpawn() {
        if (skeletonSpawnClip != null) {
            skeletonSpawnClip.setVolume(skeletonSpawnVolume);
            skeletonSpawnClip.play();
        }
    }

    public void setPlaceBlockVolume(double v)   { placeBlockVolume   = clamp(v); }
    public void setElimBlockVolume(double v)    { elimBlockVolume    = clamp(v); }
    public void setRowElimVolume(double v)      { rowElimVolume      = clamp(v); }
    public void setPlayerDieVolume(double v)    { playerDieVolume    = clamp(v); }
    public void setGameEndVolume(double v)      { gameEndVolume      = clamp(v); }
    public void setButtonClickVolume(double v)  { buttonClickVolume  = clamp(v); }
    public void setTntPlaceVolume(double v)     { tntPlaceVolume     = clamp(v); }
    public void setTntExplodeVolume(double v)   { tntExplodeVolume   = clamp(v); }
    public void setLavaVolume(double v)         { lavaVolume         = clamp(v); }
    public void setZombieSpawnVolume(double v)  { zombieSpawnVolume  = clamp(v); }
    public void setZombieDeathVolume(double v)  { zombieDeathVolume  = clamp(v); }
    public void setSkeletonSpawnVolume(double v){ skeletonSpawnVolume= clamp(v); }


    private double clamp(double v) {
        if (v < 0.0) return 0.0;
        return Math.min(v, 1.0);
    }
}
