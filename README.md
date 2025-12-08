# COMP2042 Coursework - Jiang Yuze 20593889

## GitHub Repository
https://github.com/mtdgz/CW2025

## Compilation Instructions
1. Open the project in IntelliJ IDEA (Community or Ultimate).
2. Ensure **JDK 21** and **Maven** are correctly installed and configured.
3. Open the **Maven** tool window on the right sidebar.
4. Navigate to `Plugins` => `javafx` => `javafx:run`.
5. Double-click to build and launch the application.

## Implemented and Working Properly
* **7-Bag Randomization System:** Replaced the original purely random generator with a "7-Bag" system. This ensures a balanced distribution of tetrominoes (one of each type per cycle) to prevent RNG extremes (e.g., receiving three 'Z' blocks in a row), thereby adhering to modern Tetris guidelines and improving fairness.
* **RPG Character System:** Implemented a character selection feature (Steve/Alex) with unique active skills. This introduces a strategic layer to the gameplay, requiring players to manage resources and cooldowns rather than just placing blocks.
* **Interactive PvE Environment (Zombie Swamp):** Created a dynamic hazard system where Skeletons and Bosses interact with the board. The player entity must avoid contact with enemies while using line clears and skills (TNT and Lava) to defeat them for bonus points.

## Implemented but Not Working Properly
* **Animated Home Background:** The home screen features a looping MP4 video background. On some hardware configurations, this may occasionally stutter or lag due to JavaFX media player rendering overhead. It functions correctly 90% of the time but has minor performance inconsistencies.

## Features Not Implemented
* **Complex Time/Vision Skills:** Initially planned for "Time Stop" or "Next Piece Vision" skills. These were discarded in favor of TNT and Lava abilities to better align with the "Minecraft" thematic consistency.
* **Projectile-Shooting Boss:** Originally planned for the Boss to actively shoot projectiles at the player. This was de-scoped due to time constraints and the complexity of collision detection, in favor of the current proximity-based Skeleton summoning mechanic which preserved the core Tetris gameplay flow.

## New Java Classes
* **GameConfig.java:** Global configuration class that manages the selected character, difficulty settings, and fall speed adjustments.
* **CharacterType.java:** Enum defining character choices (Steve/Alex), including their specific driving abilities and skin assets.
* **Difficulty.java:** Enum defining game difficulty modes (Peaceful, Normal, Hardcore) to control game rules.
* **PlayerState.java:** Data class that tracks real-time player statistics including position, lines cleared, boss HP, and skill cooldowns.
* **AbilityHelper.java:** Helper class that encapsulates and implements the logic for character-specific special abilities.
* **SoundManager.java:** Centralized audio manager handling background music (BGM) and all sound effects with volume control.
* **GameOverPanel.java:** Custom UI component creating a Minecraft-style "You Died!" overlay with options to respawn or return to the title screen.
* **DifficultyController.java:** Controller class for the new difficulty selection screen.
* **CharacterSelectController.java:** Controller class for the character selection screen.
* **RandomBrickGenerator.java:** Implementation of the 7-Bag randomization logic to ensure fair piece distribution.

## Modified Java Classes
* **Main.java:** Modified to launch the application into a Home Menu instead of directly opening the Game Board.
* **HomeController.java:** Updated to control the home menu, featuring a looping MP4 animated background and navigation logic for game start, character select, and difficulty screens.
* **GuiController.java:** Extended to handle player rendering, character skins, ability input (Q/E), cooldown UI displays, the home button, and integration with the GameOverPanel.
* **GameController.java:** Updated to coordinate player movement, abilities, skeleton spawning, difficulty-based speed changes, sound effects, and "You Died" state handling.
* **SimpleBoard.java:** Heavily extended to support player entity logic, boss mechanics (2x2 zombie), skeleton enemies, TNT, lava, ability effects, combat scoring, and full game reset logic.

## Unexpected Problems
* **Resource Pathing:** Encountered significant issues with `getResource` returning null for images and video files after compilation. Resolved by restructuring the resources folder and standardizing file path references.
* **Git Workflow:** Faced initial challenges with branching strategies and merge conflicts when managing experimental features, which required spending time learning proper Git reversion and merging techniques.
* **Enemy Generation Logic:** The logic for spawning skeletons without overlapping existing blocks was complex. Required multiple refactors of the collision detection system to ensure enemies didn't spawn inside walls.