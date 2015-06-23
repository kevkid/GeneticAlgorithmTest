import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class Utility {
	public static boolean isUnary(String func){
		switch(func){
		case "sin":
			return true;
		case "cos":
			return true;
		case "tan":
			return true;
		case "sec":
			return true;
		case "csc":
			return true;
		case "cot":
			return true;
		case "log":
			return true;
		case "^2":
			return true;
		case "sqrt":
			return true;
		case "e^":
			return true;
			}
	return false;
		}
	
	public static ArrayList<String> flattenTree(Tree t){
		ArrayList<String> s = new ArrayList<String>();
		traverse(t.root, s);
		return s;
	}
	private static void traverse(Node n, ArrayList<String> s){
		s.add(n.data);
		if(n.left != null)
			traverse(n.left, s);
		if(n.right != null)
			traverse(n.right, s);
	}
}
