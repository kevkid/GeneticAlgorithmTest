import java.util.Random;


public class Tree {
	static int strIndex = 0;
	static String doubleRegEx = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
	String[] operators = {"+", "-", "*", "/", "sin", "cos", "tan", "sec", "csc", "cot", "log", "^2", "sqrt", "e^", "v" };
	String[] terminalSet = {"n","d","x"};
	String[] var = {"x", "y"};
	int maxDepth;
	int numberOfChildren;
	double fitness;
	
	Node root;
	public Tree(int depth){//build tree when instantiating;
		strIndex = 0;
		root = build(depth);
		maxDepth = maxDepth(root);
		numberOfChildren = countChildren(root);
	}
	public Tree(Node inputRoot){
		strIndex = 0;
		root = inputRoot;
		maxDepth = maxDepth(root);
		numberOfChildren = countChildren(root);
	}
	public static int countChildren(Node node)
	{
	    if ( node == null )
	        return 0;
	    return 1 + countChildren(node.left) + countChildren(node.right);
	}
	public static int maxDepth(Node node) {
	    if (node == null) {
	        return (0);
	    } else {
	        // compute the depth of each subtree
	        int lDepth = maxDepth(node.left);
	        int rDepth = maxDepth(node.right);
	        // use the larger one
	        if (lDepth > rDepth)
	            return (lDepth + 1);
	        else
	            return (rDepth + 1);
	    }
	}
	
	//x and y are to be set on runtime
	public Node build(int depth) {
		boolean leaf = false;
		String value;
		Node node;
		String operator;
		boolean firstNode = true;
		if (depth > 1) {
			if(firstNode){
				operator = operators[new Random().nextInt(14)];//min tree depth of 2
				firstNode = false;
			}
			else
				operator = operators[new Random().nextInt(operators.length)];
		} else {
			operator = "v";// terminal set
		}
		if (operator.equals("v")) {
			leaf = true;
			operator = terminalSet[new Random().nextInt(terminalSet.length)];//Choose any of the terminal set
			Random r = new Random();
			switch(operator){
				case "n"://ints from -100 to 100
					operator = String.valueOf(-100 + r.nextInt(200));
					break;
				case "d"://doubles from -100 to 100
					operator = String.valueOf(-100 + (100 - -100) * r.nextDouble());
					break;
				case "x":
					operator = "x";
					break;
			}
		}

		if (depth > 0) {
			if (leaf) {
				value = operator;
				node = new Node(leaf, value);
			} else {
				value = operator;
				node = new Node(leaf, value);
				node.left = build(depth - 1);
				if (!Utility.isUnary(value))
					node.right = build(depth - 1);
			}
			return node;
		}
		return null;

	}

	
	public static Node build ( String[] input)
	   {
	      boolean  leaf;
	      String   value;
	      Node node;	      

	      if(input[strIndex].matches(doubleRegEx)){
	    	  leaf = true;
	      }
	      else if(input[strIndex].matches("^\\d+$")){
	    	 leaf = true;
	      }
	      else if(input[strIndex].equals("x")){
	    	  leaf = true;
	      }
	      else leaf = false;
	      
	      if ( leaf )
	      {
	         value = input[strIndex];
	         strIndex++;
	         node = new Node ( leaf, value );
	      }
	      else
	      {
		     value = input[strIndex];
		     strIndex++;
	         node = new Node ( leaf, value );
	         node.left  = build ( input);
	         if(!Utility.isUnary(value))
	        	 node.right = build ( input);
	      }
	      return node;
	   }

}
