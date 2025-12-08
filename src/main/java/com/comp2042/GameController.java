package com.comp2042;

public class GameController implements InputEventListener {

    private final Board board;
    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        this.viewGuiController = c;
        this.board = new SimpleBoard(25, 10);

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        refreshCooldowns();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            SoundManager.getInstance().playPlaceBlock();

            clearRow = board.clearRows();
            if (clearRow.linesRemoved() > 0) {
                board.getScore().add(clearRow.scoreBonus());
                SoundManager.getInstance().playRowElim();
            }
            if (board.createNewBrick()) {
                SoundManager.getInstance().playGameEnd();
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.eventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        refreshCooldowns();
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(board.getViewData());
        refreshCooldowns();
    }

    @Override
    public void onPlayerMove(int dx, int dy) {
        boolean moved = board.movePlayer(dx, dy);

        // If player died (touch skeleton/zombie), end game
        if (board instanceof SimpleBoard sb && sb.isPlayerDead()) {
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
            viewGuiController.refreshBrick(board.getViewData());
            viewGuiController.gameOver();
            return;
        }

        if (moved) {
            ViewData viewData = board.getViewData();
            viewGuiController.refreshBrick(viewData);
        }
        refreshCooldowns();
    }

    @Override
    public void onAbilityOne() {
        board.useAbilityOne();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(board.getViewData());
        refreshCooldowns();
    }

    @Override
    public void onAbilityTwo() {
        board.useAbilityTwo();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(board.getViewData());
        refreshCooldowns();
    }

    private void refreshCooldowns() {
        if (board instanceof SimpleBoard sb) {
            viewGuiController.updateCooldowns(sb.getPlayerState());
        }
    }
}
