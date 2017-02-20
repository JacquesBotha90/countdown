package gameMaster;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import numbersGame.NumbersFunctions;
import lettersGame.LettersFunctions;

/**
 * Class that controls game flow and player details.
 * 
 * @author BothaJ
 *
 */
public class GameMaster {
  private ArrayList<Player> players = new ArrayList<Player>();

  /**
   * Starts an instance of the game, controls game setup input and structures
   * the game.
   */
  public void initiateGame() {
    // Run once-off setup for letters and numbers functions.
    LettersFunctions.generateWordList();
    NumbersFunctions.generatePermutationsAndOperatorLists();

    System.out.println("Welcome to Countdown!");
    Scanner sc = null;
    try {
      sc = new Scanner(System.in);
      int num = 0;
      while (num <= 0) {
        try {
          System.out.println("How many people will be playing?");
          num = sc.nextInt();
          sc.nextLine();
        } catch (InputMismatchException e) {
          System.out.println("Invalid input. Please enter a number.");
          sc.nextLine();
        }
      }
      for (int i = 1; i <= num; i++) {
        String namei = "";
        try {
          System.out.println("Please enter your name Player " + i + ":");
          namei = sc.nextLine();
        } catch (InputMismatchException e) {
          System.out.println("Invalid input. Please enter a valid name.");
          sc.nextLine();
        }
        players.add(new Player(namei));
      }
      int totalRounds = 0;
      while (totalRounds <= 0) {
        try {
          System.out.println("How many rounds would you like to play?");
          totalRounds = sc.nextInt();
          sc.nextLine();
        } catch (InputMismatchException e) {
          System.out.println("Invalid input. Please enter a number.");
          sc.nextLine();
        }
      }
      for (int i = 1; i <= totalRounds; i++) {
        System.out.println("Round " + i + " Letters Game:");
        lettersRound(sc);
        System.out.println("The scores after that round are:");
        printScores();
        System.out.println("Round " + i + " Numbers Game:");
        numbersRound(sc);
        System.out.println("The scores after that round are:");
        printScores();
      }
      System.out.println("The Conundrum:");
      conundrumRound(sc);
      System.out.println("The final scores:");

    } finally {
      sc.close();
    }

  }

  /**
   * Controls a letters round.
   * 
   * @param sc
   */
  public void lettersRound(Scanner sc) {
    startTimer();
  }

  /**
   * Controls a numbers round.
   * 
   * @param sc
   */
  public void numbersRound(Scanner sc) {

  }

  /**
   * Controls the conundrum round.
   * 
   * @param sc
   */
  public void conundrumRound(Scanner sc) {

  }

  /**
   * Print player scores.
   */
  public void printScores() {
    for (Player p : players) {
      System.out.println(p.name + ": " + p.getScore());
    }
    System.out.println("");
  }

  /**
   * Start a timer for 30 seconds. NOTE: inelegant solution which may be subject
   * to drift, needs to be refined.
   */
  public void startTimer() {
    System.out.println("");
    System.out.println("Your time starts now.");
    int timeLeft = 5;
    while (timeLeft > 0) {
      System.out.print(timeLeft + "\r");
      timeLeft--;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("Timer error.");
      }
    }
    System.out.println("Time's up!");
  }

  public static void main(String[] args) {
    GameMaster gm = new GameMaster();
    gm.initiateGame();
  }

}
