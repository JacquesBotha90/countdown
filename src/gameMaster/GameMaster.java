package gameMaster;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import lettersGame.LettersFunctions;
import numbersGame.NumbersFunctions;

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
    LettersFunctions.refreshLetterPiles();
    String[] letters = new String[9];
    int numChosen = 0;
    int numVowels = 0;
    int numConsonants = 0;
    while (numChosen < 9) {
      try {
        System.out.println("Would you like a vowel or a consonant (V/C)?");
        String choice = sc.nextLine();
        if (!choice.equalsIgnoreCase("c") && !choice.equalsIgnoreCase("v")) {
          throw new InputMismatchException();
        }
        if (numConsonants == 6 && choice.equalsIgnoreCase("c")) {
          throw new java.lang.Exception("Maximum number of consonants "
              + "reached. Please choose a vowel.");
        }
        if (numVowels == 5 && choice.equalsIgnoreCase("v")) {
          throw new java.lang.Exception("Maximum number of vowels "
              + "reached. Please choose a consonant.");
        }
        letters[numChosen++] = LettersFunctions.chooseLetter(choice);
        if (choice.equalsIgnoreCase("c")) {
          numConsonants++;
        } else {
          numVowels++;
        }
        System.out.print("Letters: ");
        for (String s : letters) {
          if (s == null) {
            break;
          }
          System.out.print(s.toUpperCase() + " ");
        }
        System.out.println(" ");
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter "
            + "either \"V\" or \"C\".");
      } catch (java.lang.Exception e) {
        System.out.println(e.getMessage());
      }
    }

    Thread t = new Thread() {
      public void run() {
        LettersFunctions.solve(letters);
      }
    };
    t.start();

    startTimer();
    ArrayList<String> playerWords = new ArrayList<String>();
    int maxLength = 0;

    for (Player p : players) {
      String word = "";
      try {
        System.out.println("");
        System.out.println(p.name + ", please enter your word:");
        word = sc.nextLine();
      } catch (InputMismatchException e) {
        System.out.println("Invalid input.");
        sc.nextLine();
      }
      if (LettersFunctions.validWords.contains(word.toLowerCase())) {
        System.out.println("Well done! That is a valid word.");
        playerWords.add(word);
        if (word.length() > maxLength) {
          maxLength = word.length();
        }
      } else {
        System.out.println("Sorry, that is not a valid word.");
        playerWords.add("");
      }
    }

    for (String w : playerWords) {
      if (w.length() == maxLength) {
        if (maxLength == 9) {
          players.get(playerWords.indexOf(w)).addPoints(18);
        } else {
          players.get(playerWords.indexOf(w)).addPoints(maxLength);
        }
      }
    }

    System.out.println("");

    System.out.println("The total number of valid words is: "
        + LettersFunctions.validWords.size());
    System.out.println("Highest scoring word(s):");
    for (String s : LettersFunctions.validWords) {
      if (s.length() == LettersFunctions.validWords.get(0).length()) {
        System.out.println(s);
      } else {
        break;
      }
    }

    System.out.println("");
  }

  /**
   * Controls a numbers round.
   * 
   * @param sc
   */
  public void numbersRound(Scanner sc) {
    int numBig = -1;

    while (numBig < 0 || numBig > 4) {
      try {
        System.out.println("How many big numbers would you like?");
        numBig = sc.nextInt();
        sc.nextLine();
        if (numBig < 0) {
          throw new java.lang.Exception("Invalid input. Please "
              + "enter a positive value.");
        }
        if (numBig > 4) {
          throw new java.lang.Exception("Invalid input. You are "
              + "allowed up to 4 big numbers.");
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number.");
        sc.nextLine();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    int[] numbers = NumbersFunctions.chooseNumbers(numBig, 6 - numBig);
    int target = NumbersFunctions.generateTarget();

    System.out.println("Your target is: " + target);

    System.out.print("Your numbers are: ");
    for (int i = 0; i < numbers.length; i++) {
      System.out.print(numbers[i] + " ");
    }
    System.out.println(" ");

    Thread t = new Thread() {
      public void run() {
        NumbersFunctions.solve(numbers, target);
      }
    };
    t.start();

    startTimer();
    ArrayList<Integer> playerValue = new ArrayList<Integer>();
    int closestDiff = 11;

    for (Player p : players) {
      int value = 0;
      try {
        System.out.println("");
        System.out.println(p.name + ", what value did you get:");
        value = sc.nextInt();
        sc.nextLine();
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number.");
        sc.nextLine();
      }
      if (Math.abs(target - value) <= 10
          && Math.abs(target - value) <= closestDiff) {
        closestDiff = Math.abs(target - value);
        playerValue.add(value);
      } else {
        playerValue.add(-1);
      }
    }

    for (Player p : players) {
      if (Math.abs(target - playerValue.get(players.indexOf(p))) == closestDiff) {
        System.out.println(" ");
        System.out.println(p.name + ", please enter your solution.");
        String playerSolution = "";
        try {
          playerSolution = sc.nextLine();
        } catch (InputMismatchException e) {
          System.out.println("Invalid input.");
          sc.nextLine();
        }
        int check = NumbersFunctions.checkSolution(playerSolution, numbers,
            playerValue.get(players.indexOf(p)));

        switch (check) {
        case 1:
          int points = 0;
          if (closestDiff == 0) {
            points = 10;
          } else if (closestDiff <= 5) {
            points = 7;
          } else {
            points = 5;
          }
          System.out.println("Well done! That is a valid solution.");
          p.addPoints(points);
          break;
        default:
          System.out.println("Unfortunately that solution is not correct. "
              + "No points awarded.");
          break;
        }
      }
    }

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
      System.out.print(timeLeft + " ");
      timeLeft--;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("Timer error.");
      }
    }
    System.out.println("Time's up!");
    System.out.println(" ");
  }

  public static void main(String[] args) {
    GameMaster gm = new GameMaster();
    gm.initiateGame();
  }

}
