# Parallel-Betting-System

## Project Overview
The **Concurrent Betting Simulation** is a multi-threaded game made in Java where players place bets, roll dice, and compete for points. The game utilizes synchronization techniques to ensure controlled gameplay and concurrent player interactions.

## Features
- **Multi-threaded Gameplay**: Each player is represented by a separate thread.
- **Betting System**: Players place bets based on their available points.
- **Dice Rolls**: Players roll dice to determine their results and gain or lose points.
- **Synchronization**: Semaphores are used to manage access to shared resources and synchronize player actions.
- **Graceful Exit**: The game finishes with a thank-you message once the game ends.

## How to Use the Interface
When the game starts, you'll be presented with a simple text-based menu that allows you to interact with the game. Here are the available options:

1. **Select Your Player**: 
   - Players can choose from four characters: **Thomas**, **Jack**, **Michael**, and **Manuel**.
   - Select your player by typing the corresponding number for the player you wish to control.

2. **Place Your Bet**: 
   - Once you select your player, you will be prompted to place a bet based on your current available points. The game will display your points, and you can input the amount you wish to bet.
   - If you have enough points, your bet will be accepted.

3. **Roll the Dice**: 
   - After placing the bet, you'll roll the dice, and the result will be displayed.
   - The outcome of the roll will determine whether you win or lose the bet.

4. **View Game Status**: 
   - After each round, the game will display the results of the dice rolls and update the points for each player.

5. **Exit the Game**: 
   - You can exit the game at any time by selecting the "Exit" option. A thank-you message will be shown, and the game will end.

## Clone the repository:
   ```bash
   git clone https://github.com/ThomasFrentzel/Parallel-Betting-System
