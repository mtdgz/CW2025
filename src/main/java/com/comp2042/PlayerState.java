package com.comp2042;

public class PlayerState {
    private CharacterType characterType;
    private int x;
    private int y;
    //totem
    private boolean hasTotem = true;

    //skill cd
    private long placeBlockCooldownEnd;
    private long tntCooldownEnd;
    private long destroyBlockCooldownEnd;
    private long lavaCooldownEnd;

    //lines cleared and boss
    private int totalLinesCleared;
    private boolean bossSpawned;
    private int bossHp = 100;

    public PlayerState(CharacterType characterType, int startX, int startY){
        this.characterType = characterType;
        this.x = startX;
        this.y = startY;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //character
    public CharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
    }

    //totem
    public boolean hasTotem(){
        return hasTotem;
    }

    public void consumeTotem(){
        this.hasTotem = false;
    }

    //lines/boss
    public int getTotalLinesCleared(){
        return totalLinesCleared;
    }

    public void addLinesCleared(int delta){
        this.totalLinesCleared += delta;
    }

    public boolean isBossSpawned(){
        return bossSpawned;
    }

    public void setBossSpawned(boolean bossSpawned) {
        this.bossSpawned = bossSpawned;
    }

    public int getBossHp() {
        return bossHp;
    }

    public void damageBoss(int amount){
        bossHp = Math.max(0, bossHp-amount);
    }

    public boolean isBossDead(){
        return bossHp <= 0;
    }
    //cd time
    public long getDestroyBlockCooldownEnd() {
        return destroyBlockCooldownEnd;
    }

    public void setDestroyBlockCooldownEnd(long destroyBlockCooldownEnd) {
        this.destroyBlockCooldownEnd = destroyBlockCooldownEnd;
    }

    public long getPlaceBlockCooldownEnd() {
        return placeBlockCooldownEnd;
    }

    public void setPlaceBlockCooldownEnd(long placeBlockCooldownEnd) {
        this.placeBlockCooldownEnd = placeBlockCooldownEnd;
    }

    public long getLavaCooldownEnd() {
        return lavaCooldownEnd;
    }

    public void setLavaCooldownEnd(long lavaCooldownEnd) {
        this.lavaCooldownEnd = lavaCooldownEnd;
    }

    public long getTntCooldownEnd() {
        return tntCooldownEnd;
    }

    public void setTntCooldownEnd(long tntCooldownEnd) {
        this.tntCooldownEnd = tntCooldownEnd;
    }
}
