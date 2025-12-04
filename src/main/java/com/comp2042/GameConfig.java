package com.comp2042;

public final class GameConfig {
    private static CharacterType selectedCharacter = CharacterType.STEVE;
    private static Difficulty selectedDifficulty = Difficulty.NORMAL;



    public static CharacterType getSelectedCharacter(){
        return selectedCharacter;
    }

    public static void setSelectedCharacter(CharacterType characterType){
        if (characterType != null){
            selectedCharacter = characterType;
        }
    }

    public static Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public static void setSelectedDifficulty(Difficulty difficulty) {
        selectedDifficulty = difficulty;
    }
}
