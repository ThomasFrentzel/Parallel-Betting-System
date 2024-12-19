import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class GameMain {

    // Semaphores and synchronization variables
    private static Semaphore barrier1 = new Semaphore(0);
    private static Semaphore barrier2 = new Semaphore(1);
    private static Semaphore mutex = new Semaphore(1);
    private static int[] counter = new int[1];
    private static int[] numPlayers = new int[1];
    private static int[] rolls = new int[4];
    private static boolean[] gameOver = new boolean[1];

    private static Player[] players;

    public static void main(String[] args) {
        // Initialize scanner for user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Game of Bets!");

        // Select player
        System.out.println("Select your player (1) Thomas (2) Jack (3) Michael (4) Manuel:");
        int playerSelection = scanner.nextInt() - 1; // Adjusting for 0-based indexing

        // Create and initialize players
        players = new Player[4];
        for (int i = 0; i < 4; i++) {
            players[i] = new Player(barrier1, barrier2, mutex, counter, numPlayers, gameOver, rolls, i, getPlayerName(i), 10);
        }

        // Game initialization
        counter[0] = 0;
        numPlayers[0] = 4;
        gameOver[0] = false;

        // Start player threads
        for (Player player : players) {
            new Thread(player).start();
        }

        // Game loop: allowing player to interact with the game
        while (!gameOver[0]) {
            showMenu(scanner);

            // Wait for player to interact with the game
            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    placeBet(scanner, playerSelection);
                    break;
                case 2:
                    viewGameStatus();
                    break;
                case 3:
                    System.out.println("Exiting game...");
                    gameOver[0] = true;
                    break;
                default:
                    System.out.println("Invalid selection, please try again.");
            }
        }

        // Print the "Thank you for playing" message
        System.out.println("\nThank you for playing!");

        // Ensure all threads are completed
        for (Player player : players) {
            try {
                // Wait for each player's thread to finish
                Thread.sleep(1000); // Allow a small delay to let threads finish gracefully
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Close scanner
        scanner.close();
        System.exit(0); // Explicitly indicate normal termination
    }

    // Show the player's menu
    private static void showMenu(Scanner scanner) {
        System.out.println("\nWhat would you like to do?");
        System.out.println("(1) Place a Bet");
        System.out.println("(2) View Current Game Status");
        System.out.println("(3) Quit Game");
        System.out.print("Select an action: ");
    }

    // Handle the bet placing
    private static void placeBet(Scanner scanner, int playerSelection) {
        Random gen = new Random();
        Player player = players[playerSelection];

        System.out.println("\n" + player.getPlayerName() + ", you currently have " + player.getPoints() + " points.");
        System.out.print("Enter the amount of points you want to bet: ");
        int betAmount = scanner.nextInt();

        if (betAmount <= 0 || betAmount > player.getPoints()) {
            System.out.println("Invalid bet amount. You have " + player.getPoints() + " points available.");
        } else {
            player.placeBet(betAmount);
            System.out.println(player.getPlayerName() + " placed a bet of " + betAmount + " points.");
            System.out.println("Rolling the dice...");
            // Simulate rolling the dice and calculating the result slowly
            showRollingResult(player);
        }
    }

    // View the game status (points of each player)
    private static void viewGameStatus() {
        System.out.println("\nGame Status:");
        for (Player player : players) {
            System.out.println(player.getPlayerName() + " - Points: " + player.getPoints());
        }
    }

    // Simulate rolling the dice and display results
    private static void showRollingResult(Player player) {
        try {
            Thread.sleep(1000); // Wait for 1 second for suspense
            System.out.println("Rolling the dice...");
            Thread.sleep(1000);
            int roll = player.rollDice();
            System.out.println(player.getPlayerName() + " rolled a " + roll + ".");
            Thread.sleep(1000);

            player.calculateResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Get player name by index
    private static String getPlayerName(int index) {
        switch (index) {
            case 0: return "Thomas";
            case 1: return "Jack";
            case 2: return "Michael";
            case 3: return "Manuel";
            default: return "Unknown";
        }
    }

    // Player class
    static class Player implements Runnable {
        private Semaphore barrier1, barrier2, mutex;
        private int[] counter, numPlayers, rolls;
        private boolean[] gameOver;
        private int playerIndex, points, gamble;
        private String playerName;

        public Player(Semaphore barrier1, Semaphore barrier2, Semaphore mutex, int[] counter, int[] numPlayers, boolean[] gameOver, int[] rolls, int playerIndex, String playerName, int points) {
            this.barrier1 = barrier1;
            this.barrier2 = barrier2;
            this.mutex = mutex;
            this.counter = counter;
            this.numPlayers = numPlayers;
            this.gameOver = gameOver;
            this.rolls = rolls;
            this.playerIndex = playerIndex;
            this.playerName = playerName;
            this.points = points;
        }

        @Override
        public void run() {
            try {
                while (!gameOver[0]) {
                    barrier1.acquire();
                    barrier2.release();
                    // Simulate betting rounds and dice rolls for all players
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Place a bet
        public void placeBet(int gamble) {
            this.gamble = gamble;
            rolls[playerIndex] = new Random().nextInt(10) + 1; // Simulate rolling the dice
        }

        // Calculate bet result (win or lose)
        public void calculateResult() {
            if (rolls[playerIndex] == getBestPoints()) {
                points += gamble; // Win
                System.out.println(playerName + " won the bet! New points: " + points);
            } else {
                points -= gamble; // Lose
                if (points <= 0) {
                    gameOver[0] = true;
                    System.out.println(playerName + " lost all points. Game Over.");
                } else {
                    System.out.println(playerName + " lost the bet. New points: " + points);
                }
            }
        }

        // Get the best dice roll among all players
        private int getBestPoints() {
            int best = -1;
            for (int i = 0; i < numPlayers[0]; i++) {
                if (rolls[i] > best) {
                    best = rolls[i];
                }
            }
            return best;
        }

        // Roll the dice and return the result
        public int rollDice() {
            return new Random().nextInt(10) + 1;
        }

        // Get player name
        public String getPlayerName() {
            return playerName;
        }

        // Get player points
        public int getPoints() {
            return points;
        }
    }
}
