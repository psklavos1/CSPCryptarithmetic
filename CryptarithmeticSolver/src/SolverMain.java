import java.util.ArrayList;
import java.util.HashMap;

// Sources used for guidance: 
// https://www.geeksforgeeks.org/solving-cryptarithmetic-puzzles-backtracking-8/, help was taken from

// The efficiency could be increased significantly by implementing a heuristic at the selection of the next variable to be assigned
// or by verifying the correctness of the assignment at each stage decreasing the repetitions. 
public class SolverMain {
	static boolean solution_found = false;

	public static void main(String[] args) {
		// The Strings are passed now as constants for easier execution but it could be
		// easily arranged to take input from user
			
		// Assume The following inputs from user
		String s1 = "TO + TO = FOR";
		String s2 = "TOO + TOO = FOR";
		String s3 = "WON + WON = TOO";
		
		// function calls
		solver(s1, 6);
		solver(s2, 6);
		solver(s3, 10); 
	}

	public static void solver(String riddle, int system) {
		System.out.println("In system: " + system + ". for cryptarithmetic: " + riddle);
		
		// Initialize, solution not found 
		solution_found = false;
		
		// Needed structures that will be used in the recursive calls.
		ArrayList<Character> chars = new ArrayList<Character>();  					// An arrayList containing all the unique characters in the riddle
		HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>();    // A hashmap that connects each unique char to a numeric value
		boolean used_numbers[] = new boolean[system];								// A flag array that dictates which numbers have already been mapped.
		String op1, op2, res;

		// Make all upper case and remove spaces.
		riddle = riddle.toUpperCase().replaceAll("\\s", "");
		op1 = riddle.substring(0, riddle.indexOf("+"));
		riddle = riddle.substring(riddle.indexOf("+") + 1);
		op2 = riddle.substring(0, riddle.indexOf("="));
		res = riddle.substring(riddle.indexOf("=") + 1);

		// Fill the charList of unique characters
		addToCharList(chars, op1);
		addToCharList(chars, op2);
		addToCharList(chars, res);
		printCharList(chars);

		// Use a backtracking method to find valid assignments of the variables   
		backtrackingSearch(op1, op2, res, chars, hashMap, used_numbers, 0, system);
		if (!solution_found)
			System.out.println("No solution found");

		System.out.println();
	}

	// Helper function that prints the contents of the ArrayList
	public static void printCharList(ArrayList<Character> chars) {
		String str = "";
		for (int i = 0; i < chars.size(); i++)
			str += chars.get(i) + "\t";

		System.out.println("The list of characters contains the following elements:\n" + str);
	}

	// This function calls the recursive backtracking algorithm
	public static void backtrackingSearch(String op1, String op2, String res, ArrayList<Character> chars,
			HashMap<Character, Integer> hashMap, boolean[] used_numbers, int letters_assigned, int system) {
		recursiveBacktracking(op1, op2, res, chars, hashMap, used_numbers, letters_assigned, system);
	}
	
	// Assign a numeric value to a String using the mapping of the characters 
	public static int findNumber(String str, HashMap<Character, Integer> hashMap) {
		String num = "";
		for (int i = 0; i < str.length(); i++) {
			num += hashMap.get(str.charAt(i));
		}
		return Integer.parseInt(num);
	}
	
	// A recursive function that detects all the valid solutions of the riddle
	public static void recursiveBacktracking(String op1, String op2, String res, ArrayList<Character> chars,
			HashMap<Character, Integer> hashMap, boolean[] used_numbers, int letters_assigned, int system) {
				
		// If the assignment is complete
		if (letters_assigned == chars.size()) {
			int operand1 = findNumber(op1, hashMap);
			int operand2 = findNumber(op2, hashMap);
			int result = findNumber(res, hashMap);
			
			// If this is a valid solution
			if (operand1 + operand2 == result) {
				// Note that a solution has been detected
				solution_found = true;
				String str = "";
				for (int i = 0; i < chars.size(); i++) {
					str += chars.get(i) + ": " + hashMap.get(chars.get(i)) + "\t";
				}
				// print the solution
				System.out.println("Solution: " + str);
			}
			return;
		}
		
		// select the next chat to be assigned a value
		char char_to_assign = select(chars, letters_assigned);

		for (int i = 0; i < system; i++) {
			// Try all the non assigned values
			if (used_numbers[i] == false) {
				// map the selected character to the current number 
				hashMap.put(char_to_assign, i);
				used_numbers[i] = true;
				// Recurse further until a complete assignmet is made. Logic could be improved
				recursiveBacktracking(op1, op2, res, chars, hashMap, used_numbers, letters_assigned + 1, system);
				used_numbers[i] = false;
				hashMap.remove(char_to_assign);
			}
		}
	}
	
	
	// Function that selects the next character that willd be assigned - could be improved
	public static char select(ArrayList<Character> chars, int letters_assigned) {
		return chars.get(letters_assigned);
	}

	// Helper function that returns true if the given character is contained already in the list
	public static boolean inList(ArrayList<Character> chars, char c) {
		boolean flag = false;
		for (int i = 0; i < chars.size(); i++) {
			if (chars.get(i) == c)
				flag = true;
		}
		if (flag)
			return true;
		else
			return false;
	}
	
	// Insert a character to char list
	public static void addToCharList(ArrayList<Character> chars, String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!inList(chars, s.charAt(i))) {
				chars.add(s.charAt(i));
			}
		}
	}

}
