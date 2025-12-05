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
        if(difficulty != null){
            selectedDifficulty = difficulty;
        }
    }

    public static int fallSpeed() {
        return switch (selectedDifficulty) {
            case PEACEFUL -> 650; // slow
            case NORMAL   -> 400; // default
            case HARDCORE -> 250; // fast
        };
    }
}
