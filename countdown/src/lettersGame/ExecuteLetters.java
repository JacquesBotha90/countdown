package lettersGame;

public class ExecuteLetters {

  public static void main(String[] args) {
    // Start Timer
    double startTime = System.currentTimeMillis();

    // IMPORTANT: Has to be done at the start of the program, but only once.
    LettersFunctions.generateWordList();

    // Generate Letters, needs to be done every time the game is reset
    LettersFunctions.refreshLetterPiles();
    String[] letters = LettersFunctions.chooseLetters(4, 5);
    System.out.print("Letters: ");
    for (String l : letters) {
      System.out.print(l.toUpperCase() + " ");
    }
    System.out.println("");

    // Find all possible solutions
    LettersFunctions.solve(letters);
    System.out.println("Number of valid words: "
        + LettersFunctions.validWords.size());
    System.out.println("Length of longest word: "
        + LettersFunctions.validWords.get(0).length());
    System.out.println("Solutions are:");
    for (String s : LettersFunctions.validWords) {
      System.out.println(s);
    }

    // Stop Timer
    double stopTime = System.currentTimeMillis();
    double elapsedTime = stopTime - startTime;
    System.out.println("Elapsed Time: " + elapsedTime / 1000 + "s");
  }

}
