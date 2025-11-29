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
    private static final int BLOCk_ZOMBIE = 9;
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
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
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
        createNewBrick();
    }
}
