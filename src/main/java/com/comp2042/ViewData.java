package com.comp2042;

public record ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition,
                       int playerX, int playerY) {

    @Override
    public int[][] brickData() {
        return MatrixOperations.copy(brickData);
    }

    @Override
    public int[][] nextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }
}
