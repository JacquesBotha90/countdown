package numbersGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * Class containing all functions required for numbers game.
 * 
 * @author BothaJ
 *
 */
public class NumbersFunctions {

  public static ArrayList<int[]> combinationsList = new ArrayList<int[]>();
  public static ArrayList<int[]> permutationsList = new ArrayList<int[]>();
  public static HashSet<String> tempSymbolicPermutations = new HashSet<String>();
  public static ArrayList<HashSet<String>> symPermutations = new ArrayList<HashSet<String>>();
  public static ArrayList<ArrayList<ArrayList<Integer>>> operatorLists = new ArrayList<ArrayList<ArrayList<Integer>>>();

  /**
   * Automatically generate symbolic permutations lists and convert those lists
   * to operator lists which can be interpreted by the stack calculator.
   */
  public static void generatePermutationsAndOperatorLists() {
    for (int i = 2; i < 7; i++) {
      symPermutations.add(generateSymbolicPermutations(i));
      operatorLists.add(generateOperatorList(i));
    }
  }

  /**
   * Randomly generate a target between 101 and 999 (inclusive).
   * 
   * @return
   */
  public static int generateTarget() {
    double target = 0;
    while (target <= 100) {
      target = Math.random() * 1000;
    }
    return (int) target;
  }

  /**
   * Randomly select numbers to use given choice of number of large and small
   * numbers. NOTE: At the moment, this class does not include any checks to
   * ensure that the total number is 6 or to limit the large numbers to 4 or
   * less.
   * 
   * @param largeNumbers
   * @param smallNumbers
   * @return
   */
  public static int[] chooseNumbers(int largeNumbers, int smallNumbers) {
    int[] lInts = { 25, 50, 75, 100 };
    int[] sInts = { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10 };
    ArrayList<Integer> largePool = new ArrayList<Integer>(0);
    ArrayList<Integer> smallPool = new ArrayList<Integer>(0);
    for (int num : lInts) {
      largePool.add(num);
    }
    for (int num : sInts) {
      smallPool.add(num);
    }
    Random random = new Random();
    int[] selectedNumbers = new int[6];
    for (int i = 0; i < 6; i++) {
      if (i < largeNumbers) {
        int r = random.nextInt(largePool.size());
        selectedNumbers[i] = largePool.get(r);
        largePool.remove(r);
      } else {
        int r = random.nextInt(smallPool.size());
        selectedNumbers[i] = smallPool.get(r);
        smallPool.remove(r);
      }
    }
    return selectedNumbers;
  }

  /**
   * Generate all combinations and permutations of the available numbers. Each
   * permutation is then combined with each possible operator list corresponding
   * with the number of numbers in the permutation. For each combination of
   * number permutation and operator list a Solution object is created, which
   * then checks whether that permutation/operator list pair can result in a
   * valid solution.
   * 
   * @param numbers
   * @param target
   */
  public static void solve(int[] numbers, int target) {
    combinationsList.clear();
    permutationsList.clear();

    for (int i = 2; i <= 6; i++) {
      generateCombinations(numbers, i, 0, new int[i]);
    }

    for (int[] i : combinationsList) {
      generatePermutations(0, i);
    }

    Solution.clearSolutions();

    for (int[] numberPermutation : permutationsList) {
      @SuppressWarnings("unchecked")
      ArrayList<ArrayList<Integer>> operatorList = (ArrayList<ArrayList<Integer>>) operatorLists
          .get(numberPermutation.length - 2).clone();

      for (ArrayList<Integer> operators : operatorList) {
        @SuppressWarnings("unused")
        Solution testSolution = new Solution(target, numberPermutation, operators);
      }
    }
  }

  /**
   * Recursively generate combinations of given numbers. For initial call
   * startPosition should be set to 0.
   * 
   * @param numbers
   * @param choose
   * @param startPosition
   * @param input
   */
  public static void generateCombinations(int[] numbers, int choose, int startPosition, int[] input) {
    if (choose == 0) {
      int[] tempResult = input.clone();
      combinationsList.add(tempResult);
      return;
    }
    for (int i = startPosition; i <= numbers.length - choose; i++) {
      input[input.length - choose] = numbers[i];
      generateCombinations(numbers, choose - 1, i + 1, input);
    }
  }

  /**
   * Recursively generate permutations of given numbers. For initial call
   * startPosition should be set to 0.
   * 
   * @param startPos
   * @param input
   */
  public static void generatePermutations(int startPos, int[] input) {
    if (startPos == input.length) {
      int[] tempResult = input.clone();
      permutationsList.add(tempResult);
      return;
    }
    for (int i = startPos; i < input.length; i++) {
      int[] clone = input.clone();
      int temp = clone[i];
      clone[i] = clone[startPos];
      clone[startPos] = temp;
      generatePermutations(startPos + 1, clone);
    }
  }

  /**
   * Generates all possible permutations of numbers and operations symbolically
   * using a recursive function for the given number of numbers (for example,
   * for 4 numbers the lists will be {NNNNOOO, NNNONOO, NNNOONO, NNONNOO,
   * NNONONO}). These lists are referred to as operator lists. The symbolic
   * lists are stored in a static class variable.
   * 
   * @param num
   * @return
   */
  public static HashSet<String> generateSymbolicPermutations(int num) {
    tempSymbolicPermutations.clear();
    String symString = "";
    for (int i = 0; i < num - 2; i++) {
      symString = "N" + symString + "O";
    }
    symPermutation("", symString);

    @SuppressWarnings("unchecked")
    HashSet<String> finalSet = (HashSet<String>) tempSymbolicPermutations.clone();

    // Checks the generated symbolic operator lists and removes all invalid
    // options.
    for (String p : tempSymbolicPermutations) {
      int prevN = 2;
      int prevO = 0;
      for (int i = 2; i < p.length(); i++) {
        if (p.charAt(i) == 'N') {
          prevN++;
        } else {
          if (prevN - 1 > prevO) {
            prevO++;
          } else {
            prevO++;
            finalSet.remove(p);
          }
        }
      }
    }
    return finalSet;
  }

  /**
   * Recursively generate symbolic operator lists. For initial call prefix
   * should be "" and str should be a String containing two fewer numbers than
   * required and an equal number of operators (for example, for 4 numbers str
   * should be "NNOO").
   * 
   * @param prefix
   * @param str
   */
  private static void symPermutation(String prefix, String str) {
    int n = str.length();
    if (n == 0) {
      tempSymbolicPermutations.add("NN" + prefix + "O");
    } else {
      for (int i = 0; i < n; i++)
        symPermutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
    }
  }

  /**
   * Convert the symbolic operator lists into numeric operator lists which can
   * be interpreted by the stack calculator (for example, the symbolic lists for
   * 4 numbers {NNNNOOO, NNNONOO, NNNOONO, NNONNOO, NNONONO} will be converted
   * into {4000, 30100, 30010, 20200, 201010}).
   * 
   * @param num
   * @return
   */
  public static ArrayList<ArrayList<Integer>> generateOperatorList(int num) {
    @SuppressWarnings("unchecked")
    HashSet<String> symList = (HashSet<String>) symPermutations.get(num - 2).clone();
    ArrayList<ArrayList<Integer>> operatorList = new ArrayList<ArrayList<Integer>>();

    for (String str : symList) {
      int counter = 0;
      ArrayList<Integer> internalElement = new ArrayList<Integer>();
      for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) == 'N') {
          counter++;
        } else if (str.charAt(i) == 'O') {
          if (counter == 0) {
            internalElement.add(0);
          } else {
            internalElement.add(counter);
            counter = 0;
            internalElement.add(0);
          }
        }
      }
      operatorList.add(internalElement);
    }
    return operatorList;

  }

  /**
   * Clear combinations and permutations list if program is run again using new
   * numbers.
   */
  public static void clearValues() {
    combinationsList.clear();
    permutationsList.clear();
  }

  /**
   * This method checks the player's solution and returns an appropriate code.
   * Code definitions are given below.
   * 
   * @param solution
   *          String with player's solutions
   * @param givenNumbers
   *          The numbers the player was allowed to use.
   * @param target
   *          The value the player declared
   * @return Various return codes may be given through: <br>
   *         1 -> Players solution is valid <br>
   *         -1 -> Player used an invalid character <br>
   *         -2 -> Player used invalid numbers <br>
   *         -3 -> Operator is not placed between two numbers <br>
   *         -4 -> Incorrect brackets <br>
   *         -5 -> Error occurred in calculation <br>
   *         -6 -> Players solution does not equal target <br>
   */
  public static int checkSolution(String solution, int[] givenNumbers, int target) {
    // First check if the player used only allowed characters
    if (!solution.matches("[0-9()+-/*//]+")) {
      return -1;
    }
    // Then check if the player used the correct numbers.
    String[] splitSolution = solution.split("[()+-/*//]");
    ArrayList<Integer> numbersGiven = new ArrayList<>();
    for (int i : givenNumbers) {
      numbersGiven.add(i);
    }
    ArrayList<Integer> numbersUsed = new ArrayList<Integer>();
    for (String s : splitSolution) {
      if (!s.equals("")) {
        numbersUsed.add(Integer.parseInt(s));
      }
    }
    for (int i : numbersUsed) {
      if (numbersGiven.contains(new Integer(i))) {
        numbersGiven.remove(new Integer(i));
      } else {
        return -2;
      }
    }
    // Finally check if the player's answer equals his declared value
    solution = solution.replaceAll(" ", "");
    ArrayList<String> charactersList = new ArrayList<String>(
        Arrays.asList(solution.split("((?<=[()+-/*//])|(?=[()+-/*//]))")));
    try {
      int playerAnswer = parseSolution(charactersList);
      if (target == playerAnswer) {
        return 1;
      } else {
        return -6;
      }
    } catch (Exception e) {
      return Integer.parseInt(e.getMessage());
    }
  }

  /**
   * This function parses the solution given by the player and returns the
   * result.
   * 
   * @param solution
   *          String containing player's solution.
   * @return
   * @throws Exception
   *           Throws an exception with an error code if an error is found in
   *           the calculation.
   */
  public static int parseSolution(ArrayList<String> charactersList) throws Exception {
    if (charactersList.contains("(") || charactersList.contains(")")) {
      while (charactersList.contains("(") && charactersList.contains(")")) {
        boolean foundPair = false;
        int openIndex = -1;
        int closeIndex = -1;
        for (int i = 0; i < charactersList.size(); i++) {
          if (charactersList.get(i).equals("(")) {
            openIndex = i;
          } else if (charactersList.get(i).equals(")")) {
            closeIndex = i;
            foundPair = true;
            break;
          }
        }
        if (foundPair && openIndex != -1) {
          ArrayList<String> innerTerm = new ArrayList<>();
          for (int i = openIndex + 1; i < closeIndex; i++) {
            innerTerm.add(charactersList.get(i));
          }
          int nestedResult = parseSolution(innerTerm);
          for (int i = 0; i <= closeIndex - openIndex; i++) {
            charactersList.remove(openIndex);
          }
          charactersList.add(openIndex, String.valueOf(nestedResult));
        } else {
          throw new Exception() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
              return "-4";
            }
          };
        }
      }
    }
    performOperation(charactersList, "*/");
    performOperation(charactersList, "+-");
    if (charactersList.size() == 1) {
      return Integer.parseInt(charactersList.get(0));
    } else {
      throw new Exception() {
        private static final long serialVersionUID = 1L;

        @Override
        public String getMessage() {
          return "-5";
        }
      };
    }
  }

  /**
   * This method will perform all the arithmetic operations of the same level of
   * precedence and replace the characters in the characterList with the result.
   * 
   * @param charactersList
   *          All the characters in the solution.
   * @param operator
   *          A string containing two operations to be performed with the same
   *          level of precedence.
   * @throws Exception
   *           An exception is thrown with return code -3 (as a String message)
   *           when an operator is not placed between two numbers.
   */
  public static void performOperation(ArrayList<String> charactersList, String operator) throws Exception {
    String[] operators = operator.split("");
    boolean test = charactersList.contains(operators[0]) || charactersList.contains(operators[1]);
    while (test) {
      int index = 0;
      if (charactersList.indexOf(operators[0]) == -1) {
        index = charactersList.indexOf(operators[1]);
      } else if (charactersList.indexOf(operators[1]) == -1) {
        index = charactersList.indexOf(operators[0]);
      } else {
        index = Math.min(charactersList.indexOf(operators[0]), charactersList.indexOf(operators[1]));
      }

      if (index != 0 && index != charactersList.size() - 1 && charactersList.get(index - 1).matches("[0-9]+")
          && charactersList.get(index + 1).matches("[0-9]+")) {
        int result = 0;
        if (charactersList.get(index).equals("*")) {
          result = Integer.parseInt(charactersList.get(index - 1)) * Integer.parseInt(charactersList.get(index + 1));
        } else if (charactersList.get(index).equals("/")) {
          result = Integer.parseInt(charactersList.get(index - 1)) / Integer.parseInt(charactersList.get(index + 1));
        } else if (charactersList.get(index).equals("+")) {
          result = Integer.parseInt(charactersList.get(index - 1)) + Integer.parseInt(charactersList.get(index + 1));
        } else if (charactersList.get(index).equals("-")) {
          result = Integer.parseInt(charactersList.get(index - 1)) - Integer.parseInt(charactersList.get(index + 1));
        }

        charactersList.remove(index - 1);
        charactersList.remove(index - 1);
        charactersList.remove(index - 1);
        charactersList.add(index - 1, String.valueOf(result));

        test = charactersList.contains(operators[0]) || charactersList.contains(operators[1]);
      } else {
        throw new Exception() {
          private static final long serialVersionUID = 1L;

          @Override
          public String getMessage() {
            return "-3";
          }
        };
      }
    }
  }
}
