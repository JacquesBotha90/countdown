package numbersGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class Solution {

  public static final String[] OPERATORS = { "+", "-", "*", "/" };

  public static ArrayList<String> postfixSolutions = new ArrayList<String>();
  public static ArrayList<String> infixSolutions = new ArrayList<String>();
  public static HashMap<String, String> stackRecordMap = new HashMap<>();
  public static HashMap<String, ArrayList<String>> operatorRecordMap = new HashMap<>();
  public static HashMap<String, ArrayList<Integer>> numbersMap = new HashMap<>();
  public static HashMap<String, String> postfixToInfixMap = new HashMap<>();
  public int target;
  public ArrayList<Integer> numbers;
  public ArrayList<Integer> operators;

  /**
   * Constructor for solution given the target, numbers and corresponding
   * numeric operator list. Automatically calls the recursive solver to
   * determine whether a valid solution is possible.
   * 
   * @param target
   * @param numbers
   * @param operators
   */
  public Solution(int target, int[] numbers, ArrayList<Integer> operators) {
    this.target = target;
    this.numbers = new ArrayList<Integer>();
    for (int i : numbers) {
      this.numbers.add(i);
    }
    this.operators = operators;
    recursiveSolver(new Stack<Integer>(), new ArrayList<String>(), 0, 0, "");
  }

  /**
   * Recursively check whether current combination of numbers and numeric
   * operator list can result in a valid solution. Recursion occurs 4 times on
   * each level with each operator being replaced with {+,-,*,/} in turn. The
   * stackRecord keeps track of the interim results in the stack calculator
   * which is used at the end of the calculation to determine whether the
   * solution is a duplicate.
   * 
   * @param st
   * @param previousOperators
   * @param operatorIndex
   * @param numberIndex
   * @param stackRecord
   */
  @SuppressWarnings("unchecked")
  public void recursiveSolver(Stack<Integer> st,
      ArrayList<String> previousOperators, int operatorIndex, int numberIndex,
      String stackRecord) {
    if (operatorIndex == operators.size()) {
      int result = st.pop();
      if (result == target) {
        solutionToPostfixString(previousOperators, stackRecord);
      }
    } else if (operators.get(operatorIndex) != 0) {
      for (int i = 0; i < operators.get(operatorIndex); i++) {
        st.push(numbers.get(numberIndex + i));
      }
      recursiveSolver(st, previousOperators, operatorIndex + 1, numberIndex
          + operators.get(operatorIndex), stackRecord);
    } else {
      int right = st.pop();
      int left = st.pop();
      ArrayList<String> newOperators;

      // Add
      Stack<Integer> sta = (Stack<Integer>) st.clone();
      newOperators = (ArrayList<String>) previousOperators.clone();
      newOperators.add("+");
      int additionResult = left + right;
      // Check intermediate result
      sta.push(additionResult);
      String sra = stackRecord + additionResult + " ";
      recursiveSolver(sta, newOperators, operatorIndex + 1, numberIndex, sra);

      // Subtract
      Stack<Integer> sts = (Stack<Integer>) st.clone();
      newOperators = (ArrayList<String>) previousOperators.clone();
      newOperators.add("-");
      int subtractionResult = left - right;
      // Check intermediate result
      if (subtractionResult <= 0) {
        // End recursion loop
      } else if (subtractionResult == right) {
        // End recursion loop
      } else {
        sts.push(subtractionResult);
        String srs = stackRecord + subtractionResult + " ";
        recursiveSolver(sts, newOperators, operatorIndex + 1, numberIndex, srs);
      }

      // Multiply
      Stack<Integer> stm = (Stack<Integer>) st.clone();
      newOperators = (ArrayList<String>) previousOperators.clone();
      newOperators.add("*");
      int multiplicationResult = left * right;
      // Check intermediate result
      if (left == 1 || right == 1) {
        // End recursion loop
      } else {
        stm.push(multiplicationResult);
        String srm = stackRecord + multiplicationResult + " ";
        recursiveSolver(stm, newOperators, operatorIndex + 1, numberIndex, srm);
      }

      // Divide
      Stack<Integer> std = (Stack<Integer>) st.clone();
      newOperators = (ArrayList<String>) previousOperators.clone();
      newOperators.add("/");
      int divisionResult = left / right;
      // Check intermediate result
      if (right == 1) {
        // End recursion loop
      } else if (divisionResult == right) {
        // End recursion loop
      } else if (left % right != 0) {
        // End recursion loop
      } else {
        std.push(divisionResult);
        String srd = stackRecord + divisionResult + " ";
        recursiveSolver(std, newOperators, operatorIndex + 1, numberIndex, srd);
      }
    }

  }

  /**
   * Converts the operators used and numbers into a postfix solution string
   * format. Then adds result to answer after performing a duplicate check using
   * the operators used and the stackRecord.
   * 
   * @param operatorsUsed
   * @param stackRec
   */
  @SuppressWarnings("unchecked")
  public void solutionToPostfixString(ArrayList<String> operatorsUsed,
      String stackRec) {
    String solString = "";
    int numCounter = 0;
    int opsCounter = 0;
    for (int command : operators) {
      if (command != 0) {
        for (int i = 0; i < command; i++) {
          String num = Integer.toString(numbers.get(numCounter));
          solString = solString + num + " ";
          numCounter++;
        }
      } else {
        solString = solString + operatorsUsed.get(opsCounter) + " ";
        opsCounter++;
      }
    }

    if (postfixSolutions.isEmpty()) {
      postfixSolutions.add(solString);
      stackRecordMap.put(solString, stackRec);
      numbersMap.put(solString, numbers);
      operatorRecordMap.put(solString, operatorsUsed);
      ArrayList<String> temp = new ArrayList<String>();
      temp.addAll(Arrays.asList(solString.split("\\s+")));
      solutionToInfixString(temp, solString);
    } else {
      // check for equivalent results that already exist
      boolean equivalentPresent = false;
      for (String s : postfixSolutions) {
        // check for identical stackrecords using the same numbers
        ArrayList<Integer> num1 = (ArrayList<Integer>) numbersMap.get(s)
            .clone();
        ArrayList<Integer> num2 = (ArrayList<Integer>) numbers.clone();
        Collections.sort(num1);
        Collections.sort(num2);
        if (stackRec.equals(stackRecordMap.get(s))
            && num1.toString().equals(num2.toString())) {
          equivalentPresent = true;
        }

        // Check for identical numbers and operators used in different order
        ArrayList<String> op1 = (ArrayList<String>) operatorRecordMap.get(s)
            .clone();
        ArrayList<String> op2 = (ArrayList<String>) operatorsUsed.clone();
        Collections.sort(op1);
        Collections.sort(op2);
        if (op1.toString().equals(op2.toString())
            && num1.toString().equals(num2.toString())) {
          equivalentPresent = true;
        }

      }
      if (equivalentPresent == false) {
        postfixSolutions.add(solString);
        stackRecordMap.put(solString, stackRec);
        numbersMap.put(solString, numbers);
        operatorRecordMap.put(solString, operatorsUsed);
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(Arrays.asList(solString.split("\\s+")));
        solutionToInfixString(temp, solString);
      }
    }
  }

  /**
   * Recursively convert postfix solution string to a string which shows the
   * infix solution with proper bracketing.
   * 
   * @param postfixString
   * @param pfs
   */
  @SuppressWarnings("unchecked")
  public static void solutionToInfixString(ArrayList<String> postfixString,
      String pfs) {
    if (postfixString.size() == 1) {
      infixSolutions.add(postfixString.get(0));
      postfixToInfixMap.put(pfs, postfixString.get(0));
    }
    for (int i = 0; i < postfixString.size(); i++) {
      if (Arrays.asList(OPERATORS).contains(postfixString.get(i))) {
        String newString = "(" + postfixString.get(i - 2) + " "
            + postfixString.get(i) + " " + postfixString.get(i - 1) + ")";
        ArrayList<String> modifiedPostfixString = (ArrayList<String>) postfixString
            .clone();
        modifiedPostfixString.remove(i - 2);
        modifiedPostfixString.remove(i - 2);
        modifiedPostfixString.remove(i - 2);
        modifiedPostfixString.add(i - 2, newString);
        solutionToInfixString(modifiedPostfixString, pfs);
        break;
      }
    }
  }

  /**
   * Clears all stored solutions for when the program is run using new numbers.
   */
  public static void clearSolutions() {
    postfixSolutions.clear();
    infixSolutions.clear();
    stackRecordMap.clear();
  }
}
