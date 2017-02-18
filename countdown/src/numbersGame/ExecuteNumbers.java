package numbersGame;

public class ExecuteNumbers {

  public static void main(String[] args) {
    // Start Timer
    double startTime = System.currentTimeMillis();

    // IMPORTANT: Has to be done at the start of the program, but only once.
    NumbersFunctions.generatePermutationsAndOperatorLists();

    // Generate Target
    int target = NumbersFunctions.generateTarget();
    System.out.println("Target: " + target);

    // Generate Numbers
    int[] numbers = NumbersFunctions.chooseNumbers(2, 4);
    System.out.print("Numbers: ");
    for (int num : numbers) {
      System.out.print(num + " ");
    }
    System.out.println("");

    // Find all possible solutions
    NumbersFunctions.solve(numbers, target);
    System.out.println("Number of independent solutions: "
        + Solution.postfixSolutions.size());
    System.out.println("Solutions are:");
    for (String s : Solution.postfixSolutions) {
      // Postfix solutions
      // System.out.println(s);

      // Infix solutions
      System.out.println(Solution.postfixToInfixMap.get(s));
    }

    // Stop Timer
    double stopTime = System.currentTimeMillis();
    double elapsedTime = stopTime - startTime;
    System.out.println("Elapsed Time: " + elapsedTime / 1000 + "s");

  }
}
