package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;
import java.awt.Point;
import java.util.Random;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final PlayerState playerState;

    private static final int BLOCK_EMPTY    = 0;
    private static final int BLOCK_SKELETON = 8;
    private static final int BLOCK_ZOMBIE   = 9;
    private static final int BLOCK_TNT      = 10;
    private static final int BLOCK_LAVA     = 11;

    private final Random rng = new Random();

    private int playerX;
    private int playerY;

    private int skeletonTickCounter = 0;


    private static class TntEntry {
        final int x;
        final int y;
        final long explodeAt;

        TntEntry(int x, int y, long explodeAt) {
            this.x = x;
            this.y = y;
            this.explodeAt = explodeAt;
        }
    }

    private final java.util.List<TntEntry> activeTnt = new java.util.ArrayList<>();
    private boolean hasActiveLava = false;

    private boolean playerDead = false;

    private boolean canPlace(int[][] shape, int x, int y) {
        for (int row = 0; row < shape.length; row++) {
            if (shape[row] == null) continue;
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 0) continue;
                int boardX = x + col;
                int boardY = y + row;

                if (boardY < 0) {
                    continue;
                }

                if (boardX < 0 || boardX >= width || boardY >= height) {
                    return false;
                }

                if (currentGameMatrix[boardY][boardX] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private int computeGhostY() {
        int[][] shape = brickRotator.getCurrentShape();
        int x = currentOffset.x;
        int ghostY = currentOffset.y;

        while (canPlace(shape, x, ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }

    public SimpleBoard(int height, int width) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[height][width];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();

        int startX = width / 2;
        int startY = 2;

        this.playerX = startX;
        this.playerY = startY;

        this.playerState = new PlayerState(GameConfig.getSelectedCharacter(), startX, startY);
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                (int) p.getX(),
                (int) p.getY()
        );
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            tickTnt();
            bossTick();
            resolveLava();
            return true;
        }
    }

    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                (int) p.getX(),
                (int) p.getY()
        );
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                (int) p.getX(),
                (int) p.getY()
        );
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                nextShape.getShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);

        int[][] shape = brickRotator.getCurrentShape();

        int boardWidth = currentGameMatrix[0].length;
        int brickWidth = shape[0].length;

        int spawnX = (boardWidth - brickWidth) / 2;
        int spawnY = 1;

        currentOffset = new Point(spawnX, spawnY);

        boolean conflict = MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );

        if (!conflict) {
            return false;
        }

        // When conflict on spawn => game over
        return handlePlayerDeath();
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int[][] currentShape = brickRotator.getCurrentShape();

        Brick nextBrick = brickGenerator.getNextBrick();
        int[][] nextShape;

        if (nextBrick != null
                && nextBrick.getShapeMatrix() != null
                && !nextBrick.getShapeMatrix().isEmpty()) {
            nextShape = nextBrick.getShapeMatrix().get(0);
        } else {
            nextShape = new int[4][4];
        }

        int ghostY = computeGhostY();

        return new ViewData(
                currentShape,
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextShape,
                ghostY,
                playerX,
                playerY
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();

        int linesRemoved = clearRow.getLinesRemoved();
        if (linesRemoved > 0) {
            playerState.addLinesCleared(linesRemoved);
            if (!playerState.isBossSpawned() && playerState.getTotalLinesCleared() >= 5) {
                // spawn once for now, change later if needed
                spawnZombieBoss();
                playerState.setBossSpawned(true);
            }

            if (playerState.isBossSpawned() && !playerState.isBossDead()) {
                damageBossWithSound(linesRemoved * 10);
            }
        }
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();

        int startX = width / 2;
        int startY = 2;
        playerX = startX;
        playerY = startY;

        playerState.resetForNewGame(startX,startY);

        // reset death & effects
        playerDead = false;
        activeTnt.clear();
        hasActiveLava = false;

        createNewBrick();
    }

    @Override
    public boolean movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
            return false;
        }

        int cell = currentGameMatrix[newY][newX];

        // Touch skeleton or zombie => death
        if (cell == BLOCK_SKELETON || cell == BLOCK_ZOMBIE) {
            SoundManager.getInstance().playPlayerDie();
            handlePlayerDeath();
            return false;
        }

        playerX = newX;
        playerY = newY;

        playerState.setX(newX);
        playerState.setY(newY);
        return true;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }


    public boolean isPlayerDead() {
        return playerDead;
    }

    private boolean handlePlayerDeath() {
        playerDead = true;
        return true;
    }

    private void damageBossWithSound(int amount) {
        if (amount <= 0) {
            return;
        }
        int before = playerState.getBossHp();
        if (before <= 0) {
            return;
        }

        playerState.damageBoss(amount);

        int after = playerState.getBossHp();
        if (after <= 0 && before > 0) {
            SoundManager.getInstance().playZombieDeath();
        } else {
            SoundManager.getInstance().playZombieDeath();
        }
    }

    private void spawnZombieBoss() {

        int baseY = height - 2;
        int spawnX = width / 2 - 1;

        if (spawnX < 0) {
            spawnX = 0;
        }
        if (spawnX + 1 >= width) {
            spawnX = Math.max(0, width - 2);
        }

        for (int dy = 0; dy < 2; dy++) {
            for (int dx = 0; dx < 2; dx++) {
                int bx = spawnX + dx;
                int by = baseY + dy;
                currentGameMatrix[by][bx] = BLOCK_ZOMBIE;
            }
        }

        SoundManager.getInstance().playZombieSpawn();
    }

    private void bossTick() {
        if (!playerState.isBossSpawned() || playerState.isBossDead()) {
            return;
        }

        java.util.List<Point> candidates = new java.util.ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = currentGameMatrix[y][x];

                if (v != BLOCK_EMPTY &&
                        v != BLOCK_ZOMBIE &&
                        v != BLOCK_SKELETON &&
                        v != BLOCK_TNT &&
                        v != BLOCK_LAVA) {
                    candidates.add(new Point(x, y));
                }
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        java.util.Collections.shuffle(candidates, rng);
        Point target = candidates.get(0);

        SoundManager.getInstance().playSkeletonSpawn();

        currentGameMatrix[target.y][target.x] = BLOCK_SKELETON;
    }

    public void onPieceLanded() {
        if (!playerState.isBossSpawned() || playerState.isBossDead()) {
            return;
        }

        java.util.List<Point> candidates = new java.util.ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = currentGameMatrix[y][x];
                if (v != BLOCK_EMPTY &&
                        v != BLOCK_ZOMBIE &&
                        v != BLOCK_SKELETON &&
                        v != BLOCK_TNT &&
                        v != BLOCK_LAVA) {
                    candidates.add(new Point(x, y));
                }
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        Point p = candidates.get(rng.nextInt(candidates.size()));
        currentGameMatrix[p.y][p.x] = BLOCK_SKELETON;
    }


    public void useAbilityOne() {
        long now = System.currentTimeMillis();
        if (playerState.getCharacterType() == CharacterType.STEVE) {
            if (now < playerState.getPlaceBlockCooldownEnd()) {
                return;
            }
            AbilityHelper.useAbilityOne(currentGameMatrix, playerState);
            SoundManager.getInstance().playPlaceBlock();
            playerState.setPlaceBlockCooldownEnd(now + 2500L);

            ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
            currentGameMatrix = clearRow.getNewMatrix();
            int linesRemoved = clearRow.getLinesRemoved();
            if (linesRemoved > 0) {
                SoundManager.getInstance().playRowElim();
                playerState.addLinesCleared(linesRemoved);
                if (!playerState.isBossSpawned() && playerState.getTotalLinesCleared() >= 5) {
                    spawnZombieBoss();
                    playerState.setBossSpawned(true);
                }
                if (playerState.isBossSpawned() && !playerState.isBossDead()) {
                    damageBossWithSound(linesRemoved * 10);
                }
            }

            playerState.setPlaceBlockCooldownEnd(now + 5000L);
        } else {
            if (now < playerState.getDestroyBlockCooldownEnd()) {
                return;
            }
            AbilityHelper.useAbilityOne(currentGameMatrix, playerState);
            SoundManager.getInstance().playElimBlock();
            ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
            currentGameMatrix = clearRow.getNewMatrix();

            int linesRemoved = clearRow.getLinesRemoved();
            if (linesRemoved > 0) {
                SoundManager.getInstance().playRowElim();
                score.add(clearRow.getScoreBonus());
                playerState.addLinesCleared(linesRemoved);
                if (!playerState.isBossSpawned() && playerState.getTotalLinesCleared() >= 5) {
                    spawnZombieBoss();
                    playerState.setBossSpawned(true);
                }
                if (playerState.isBossSpawned() && !playerState.isBossDead()) {
                    damageBossWithSound(linesRemoved * 10);
                }
            }

            playerState.setDestroyBlockCooldownEnd(now + 5000L);
        }
    }

    public void useAbilityTwo() {
        long now = System.currentTimeMillis();
        if (playerState.getCharacterType() == CharacterType.STEVE) {
            if (now < playerState.getTntCooldownEnd()) {
                return;
            }
            marktTntUnder();
            playerState.setTntCooldownEnd(now + 5000L);
        } else {
            if (now < playerState.getLavaCooldownEnd()) {
                return;
            }
            dropLavaColumn();
            playerState.setLavaCooldownEnd(now + 5000L);
        }
    }

    private void marktTntUnder() {
        int x = playerState.getX();
        int y = playerState.getY() + 1;

        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        int v = currentGameMatrix[y][x];

        if (v == BLOCK_ZOMBIE) {
            return;
        }

        if (v == BLOCK_TNT) {
            return;
        }

        currentGameMatrix[y][x] = BLOCK_TNT;
        long now = System.currentTimeMillis();
        activeTnt.add(new TntEntry(x, y, now + 2000L));
        SoundManager.getInstance().playTntPlace();
    }

    private void dropLavaColumn() {
        int x = playerState.getX();
        if (x < 0 || x >= width) {
            return;
        }
        boolean anyLava = false;

        for (int y = 0; y < height; y++) {
            int v = currentGameMatrix[y][x];

            if (v == BLOCK_EMPTY) {
                continue;
            }

            if (v == BLOCK_ZOMBIE) {
                damageBossWithSound(10);
                score.add(100);
                continue;
            }

            currentGameMatrix[y][x] = BLOCK_LAVA;
            anyLava = true;
        }
        if (anyLava) {
            SoundManager.getInstance().playLava();
        }
        hasActiveLava = true;
    }

    private void resolveLava() {
        if (!hasActiveLava) {
            return;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (currentGameMatrix[y][x] == BLOCK_LAVA) {
                    currentGameMatrix[y][x] = BLOCK_EMPTY;
                }
            }
        }
        hasActiveLava = false;
    }

    private void tickTnt() {
        if (activeTnt.isEmpty()) {
            return;
        }

        long now = System.currentTimeMillis();
        java.util.Iterator<TntEntry> it = activeTnt.iterator();

        while (it.hasNext()) {
            TntEntry t = it.next();
            if (now >= t.explodeAt) {
                explodeTnt(t.x, t.y);
                it.remove();
            }
        }
    }


    private void explodeTnt(int cx, int cy) {
        SoundManager.getInstance().playTntExplode();

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int x = cx + dx;
                int y = cy + dy;

                if (x < 0 || x >= width || y < 0 || y >= height) {
                    continue;
                }

                int v = currentGameMatrix[y][x];

                if (v == BLOCK_ZOMBIE) {
                    damageBossWithSound(15);
                    continue;
                }

                if (v == BLOCK_EMPTY) {
                    continue;
                }

                currentGameMatrix[y][x] = BLOCK_EMPTY;
            }
        }

        if (cx >= 0 && cx < width && cy >= 0 && cy < height &&
                currentGameMatrix[cy][cx] == BLOCK_TNT) {
            currentGameMatrix[cy][cx] = BLOCK_EMPTY;
        }
    }
}
