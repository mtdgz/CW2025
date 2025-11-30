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

    private static final int BLOCK_EMPTY = 0;
    private static final int BLOCK_SKELETON= 8;
    private static final int BLOCK_ZOMBIE = 9;
    private static final int BLOCK_TNT = 10;
    private static final int BLOCK_LAVA = 11;

    private final Random rng = new Random();

    private boolean canPlace(int [][] shape, int x, int y){
        for(int row = 0; row < shape.length; row++){
            if (shape[row] == null) continue;
            for( int col = 0; col < shape[row].length; col++){
                if (shape[row][col] == 0) continue;
                int boardX = x + col;
                int boardY = y + row;

                if (boardY<0){
                continue;
                }

                    if (boardX < 0 || boardX >= width || boardY >= height) {
                    return false;
                }

                if (currentGameMatrix[boardY][boardX] != 0){
                    return false;
                }
            }
        }
        return true;
    }

    private int computeGhostY(){
        int [][] shape = brickRotator.getCurrentShape();
        int x = currentOffset.x;
        int ghostY = currentOffset.y;

        while(canPlace(shape, x, ghostY + 1)){
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
        this.playerState = new PlayerState(CharacterType.STEVE);
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;

            bossTick();

            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
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
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
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
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
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

        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());

        if(!conflict){
            return false;
        }

        return handlePlayerDeath();
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int[][] currentShape = brickRotator.getCurrentShape();

        com.comp2042.logic.bricks.Brick nextBrick = brickGenerator.getNextBrick();
        int[][] nextShape;

        if (nextBrick != null
        && nextBrick.getShapeMatrix() != null
        && !nextBrick.getShapeMatrix().isEmpty()){
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
                ghostY
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();

        int linesRemoved = clearRow.getLinesRemoved();
        if(linesRemoved > 0){
            playerState.addLinesCleared(linesRemoved);
            if (!playerState.isBossSpawned() && playerState.getTotalLinesCleared() >= 20) {
                spawnZombieBoss(); //spawn once for now, change later
                playerState.setBossSpawned(true);
            }

            if (playerState.isBossSpawned() && !playerState.isBossDead()){
                playerState.damageBoss(linesRemoved * 10);
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

        playerState.setX(width/2);
        playerState.setY(0);
        createNewBrick();
    }

    @Override
    public boolean movePlayer(int dx, int dy) {
        int newX = playerState.getX() + dx;
        int newY = playerState.getY() + dy;

        if (newX < 0 || newX >= width || newY < 0 || newY >= height){
        return false;
        }

        int cell = currentGameMatrix[newY][newX];

        if (cell == BLOCK_SKELETON || cell == BLOCK_ZOMBIE);{
            boolean gameOver = handlePlayerDeath();
        }
        playerState.setX(newX);
        playerState.setY(newY);
        return true;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    private boolean handlePlayerDeath() {
        if (playerState.hasTotem()) {
            playerState.consumeTotem();

            for (int y = 0; y < Math.min(2, height); y++){
                for(int x=0; x< width; x++){
                    currentGameMatrix[y][x] = BLOCK_EMPTY;
                }
            }
            return false;
        }else{
            return true;
        }
    }

    private void spawnZombieBoss(){
        int centerX = width / 2;
        int y = 2;

        for(int dy = 0 ; dy < 2; dy++){
            for (int dx = 0; dx < 2; dx++){
                int bx = centerX + dx;
                int by = y + dy;
                if (bx >= 0 && bx < width && by < height) {
                    currentGameMatrix[by][bx] = BLOCK_ZOMBIE;
                }
            }
        }
    }
    private void bossTick(){
        if(!playerState.isBossSpawned() || playerState.isBossDead()){
            return;
        }
        java.util.List<Point> candidates = new java.util.ArrayList<>();
        for (int y=0; y<height; y++){
            for(int x=0; x<width;x++){
                int v = currentGameMatrix[y][x];
                if (v != BLOCK_EMPTY && v != BLOCK_ZOMBIE && v != BLOCK_SKELETON){
                    candidates.add(new Point(x,y));
                }
            }
        }
        if (candidates.isEmpty()){
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
            placeSupportBlockUnder();
            playerState.setPlaceBlockCooldownEnd(now + 5000L);
        } else {
            if (now < playerState.getDestroyBlockCooldownEnd()) {
                return;
            }
            destroyBlockUnderPiece();
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
            playerState.setTntCooldownEnd(now + 10000L);
        } else {
            if (now < playerState.getLavaCooldownEnd()) {
                return;
            }
            dropLavaColumn();
            playerState.setLavaCooldownEnd(now + 50000L);
        }
    }
    private void marktTntUnder(){
        int x = playerState.getX();
        int y = playerState.getY() + 1;

        if (x<0 || x >= width || y <0 || y >= height){
            return;
        }

        if(currentGameMatrix[y][x] == BLOCK_EMPTY ||
                currentGameMatrix[y][x] == BLOCK_SKELETON ||
                currentGameMatrix[y][x] == BLOCK_TNT);
    }

    private void dropLavaColumn(){
        int x = playerState.getX();

        if(x<0 || x>= width){
            return;
        }
        for (int y = playerState.getY() + 1; y < height; y++){
            int v = currentGameMatrix[y][x];

            if (v == BLOCK_ZOMBIE){
                playerState.damageBoss(10);
            }
            if(v != BLOCK_EMPTY && v != BLOCK_SKELETON && v != BLOCK_ZOMBIE){
                break;
            }
            currentGameMatrix[y][x] = BLOCK_LAVA;
        }
    }
}
