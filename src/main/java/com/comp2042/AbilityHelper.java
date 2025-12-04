package com.comp2042;

public final class AbilityHelper {


    private static final int STEVE_BLOCK_VALUE = 4;
    private static final int EMPTY = 0;

    private AbilityHelper() {

    }

    public static void useAbilityOne(int[][] board, PlayerState player) {
        if (board == null || player == null) {
            return;
        }

        int height = board.length;
        if (height == 0) {
            return;
        }
        int width = board[0].length;

        int px = player.getX();
        int py = player.getY();

        if (px < 0 || px >= width || py < 0 || py >= height) {
            return;
        }

        if (player.getCharacterType() == CharacterType.STEVE) {
            applySteveAbilityOne(board, px, py);
        } else if (player.getCharacterType() == CharacterType.ALEX) {
            applyAlexAbilityOne(board, px, py);
        }
    }


    private static void applySteveAbilityOne(int[][] board, int px, int py) {
        if (board[py][px] == EMPTY) {
            board[py][px] = STEVE_BLOCK_VALUE;
        }
    }

    private static void applyAlexAbilityOne(int[][] board, int px, int py) {
        if (board[py][px] != EMPTY) {
            board[py][px] = EMPTY;
        }
    }
}
