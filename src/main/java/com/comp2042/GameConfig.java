package com.comp2042;

public final class GameConfig {
    private static CharacterType selectedCharacter = CharacterType.STEVE;

    private GameConfig(){

    }

    public static CharacterType getSelectedCharacter(){
        return selectedCharacter;
    }

    public static void setSelectedCharacter(CharacterType characterType){
        if (characterType != null){
            selectedCharacter = characterType;
        }
    }
}
