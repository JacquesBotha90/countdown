package lettersGame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Class containing all functions required for letters game.
 * 
 * @author BothaJ
 *
 */
public class LettersFunctions {

  /**
   * A simple object to store the scrambled and unscrambled word for the
   * conundrum.
   * 
   * @author BothaJ
   *
   */
  public static class ConundrumSolution {
    public String word;
    public String scrambledWord;

    public ConundrumSolution(String unscrambled, String scrambled) {
      this.word = unscrambled;
      this.scrambledWord = scrambled;
    }
  }

  public static HashSet<String> wordList = new HashSet<String>();
  public static ArrayList<String> vowelPile = new ArrayList<String>();
  public static ArrayList<String> consonantPile = new ArrayList<String>();
  public static ArrayList<String[]> combinationsList = new ArrayList<String[]>();
  public static ArrayList<String[]> permutationsList = new ArrayList<String[]>();
  public static ArrayList<String> validWords = new ArrayList<String>();
  public static ArrayList<String> nineLetterWords = new ArrayList<String>();

  /**
   * Generates the lists of valid words.
   */
  public static void generateWordList() {
    ClassLoader classLoader = LettersFunctions.class.getClassLoader();
    File file = new File(classLoader.getResource("words.txt").getFile());
    Path path = file.toPath();
    List<String> lines = new ArrayList<String>();
    try {
      lines = Files.readAllLines(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (String word : lines) {
      wordList.add(word);
      if (word.length() == 9) {
        nineLetterWords.add(word);
      }
    }
  }

  /**
   * Refresh letter piles to the coundown standard set.
   */
  public static void refreshLetterPiles() {
    // Generate vowel pile
    vowelPile.addAll(Collections.nCopies(15, "a"));
    vowelPile.addAll(Collections.nCopies(21, "e"));
    vowelPile.addAll(Collections.nCopies(13, "i"));
    vowelPile.addAll(Collections.nCopies(13, "o"));
    vowelPile.addAll(Collections.nCopies(5, "u"));

    // Generate consonant pile
    consonantPile.addAll(Collections.nCopies(2, "b"));
    consonantPile.addAll(Collections.nCopies(3, "c"));
    consonantPile.addAll(Collections.nCopies(6, "d"));
    consonantPile.addAll(Collections.nCopies(2, "f"));
    consonantPile.addAll(Collections.nCopies(3, "g"));
    consonantPile.addAll(Collections.nCopies(2, "h"));
    consonantPile.addAll(Collections.nCopies(1, "j"));
    consonantPile.addAll(Collections.nCopies(1, "k"));
    consonantPile.addAll(Collections.nCopies(5, "l"));
    consonantPile.addAll(Collections.nCopies(4, "m"));
    consonantPile.addAll(Collections.nCopies(8, "n"));
    consonantPile.addAll(Collections.nCopies(4, "p"));
    consonantPile.addAll(Collections.nCopies(1, "q"));
    consonantPile.addAll(Collections.nCopies(9, "r"));
    consonantPile.addAll(Collections.nCopies(9, "s"));
    consonantPile.addAll(Collections.nCopies(9, "t"));
    consonantPile.addAll(Collections.nCopies(1, "v"));
    consonantPile.addAll(Collections.nCopies(1, "w"));
    consonantPile.addAll(Collections.nCopies(1, "x"));
    consonantPile.addAll(Collections.nCopies(1, "y"));
    consonantPile.addAll(Collections.nCopies(1, "z"));
  }

  /**
   * Randomly draws 9 letters from letter piles given the number of vowels and
   * consonants. NOTE: At the moment there is no functionality to check that the
   * numbers of vowels and consonants add to 9 or to limit the maximum number of
   * vowels to 5.
   * 
   * @param vowels
   * @param consonants
   * @return
   */
  public static String[] chooseLetters(int vowels, int consonants) {

    Random random = new Random();
    String[] selectedLetters = new String[9];
    for (int i = 0; i < 9; i++) {
      if (i < vowels) {
        int r = random.nextInt(vowelPile.size());
        selectedLetters[i] = vowelPile.get(r);
        vowelPile.remove(r);
      } else {
        int r = random.nextInt(consonantPile.size());
        selectedLetters[i] = consonantPile.get(r);
        consonantPile.remove(r);
      }
    }
    return selectedLetters;
  }

  /**
   * Returns a single letter based on the user's choice.
   * 
   * @param choice
   *          Must be either "c" or "v", case insensitive.
   * @return
   */
  public static String chooseLetter(String choice) {

    Random random = new Random();
    String selected;
    switch (choice) {
    case "c":
    case "C": {
      int r = random.nextInt(consonantPile.size());
      selected = consonantPile.get(r);
      consonantPile.remove(r);
      break;
    }
    case "v":
    case "V": {
      int r = random.nextInt(vowelPile.size());
      selected = vowelPile.get(r);
      vowelPile.remove(r);
      break;
    }
    default: {
      selected = "";
      break;
    }
    }
    return selected;
  }

  /**
   * Given the 9 chosen letters, generate all combinations and permutations and
   * determine which ones are valid words.
   * 
   * @param letters
   */
  public static void solve(String[] letters) {
    combinationsList.clear();
    permutationsList.clear();

    for (int i = 9; i >= 3; i--) {
      generateCombinations(letters, i, 0, new String[i]);
    }

    for (String[] i : combinationsList) {
      generatePermutations(0, i);
    }

    validWords.clear();

    for (String[] perm : permutationsList) {
      String word = "";
      for (String s : perm) {
        word = word + s;
      }
      if (wordList.contains(word)) {
        if (!validWords.contains(word)) {
          validWords.add(word);
        }
      }
    }

  }

  /**
   * Recursively generate all combinations of the given letters. For initial
   * call startPosition should be set to 0;
   * 
   * @param letters
   * @param choose
   * @param startPosition
   * @param input
   */
  public static void generateCombinations(String[] letters, int choose,
      int startPosition, String[] input) {
    if (choose == 0) {
      String[] tempResult = input.clone();
      combinationsList.add(tempResult);
      return;
    }
    for (int i = startPosition; i <= letters.length - choose; i++) {
      input[input.length - choose] = letters[i];
      generateCombinations(letters, choose - 1, i + 1, input);
    }
  }

  /**
   * Recursively generate all permutations of a given String array. For the
   * initial call startingPosition should be set to 0;
   * 
   * @param startingPosition
   * @param input
   */
  public static void generatePermutations(int startingPosition, String[] input) {
    if (startingPosition == input.length) {
      String[] tempResult = input.clone();
      permutationsList.add(tempResult);
      return;
    }
    for (int i = startingPosition; i < input.length; i++) {
      String temp = input[i];
      input[i] = input[startingPosition];
      input[startingPosition] = temp;
      generatePermutations(startingPosition + 1, input);
    }
  }

  /**
   * Randomly select a 9 letter word and shuffle the letters. Both the original
   * word and the scrambled word is passed.
   * 
   * @return
   */
  public static ConundrumSolution runConundrum() {
    Random randomizer = new Random();
    String word = nineLetterWords
        .get(randomizer.nextInt(nineLetterWords.size()));
    ArrayList<String> letters = new ArrayList<String>();
    letters.addAll(Arrays.asList(word.split("")));
    String scrambled = "";
    while (!letters.isEmpty()) {
      int r = randomizer.nextInt(letters.size());
      scrambled += letters.get(r);
      letters.remove(r);
    }
    return new ConundrumSolution(word, scrambled);
  }

}
