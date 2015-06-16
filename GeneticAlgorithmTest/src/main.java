import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;




public class main {
	static Tree t = null;
	static Node root = null;
	static int width, height;
	static Tree[] treeArray = new Tree[10000];
	static Tree[] eliteTrees = null;
	static Tree Fittest = null;
	static int treeDepth = 10;
	static int fitness = Integer.MIN_VALUE;
	static int genLim = 1000;
	static Random rand = new Random();
	//static double[][] ans = {{-2, 4},{-1, 1},{0, 0},{1, 1},{2, 4}};// x^2
	//static double[][] ans = {{-2, -8},{-1, -1},{0, 0},{1, 1},{2, 8}};//x^3
	static double[][] ans = { { -2, -16 }, { -1, -1 }, { 0, 0 }, { 1, 1 }, { 2, 16 } };// x^4

	//static double[][] ans = {{-2, 0},{-1, -1},{0, 0},{1, 3},{2, 8}};//x^2+2x
	// double[][] ans = {{-2.0, -0.9092974},{-1.0, -0.8414709},{0.0, 0.0},{1.0,
	// 0.8414709},{2.0, 0.9092974}};//sin(x)
	// double[][] ans = {{-2.0, -0.4161468},{-1.0, 0.5403023},{0.0, 1.0},{1.0,
	// 0.5403023},{2.0, -0.4161468}};//cos(x)
	//static double[][] ans = {{-3, -9.99},{-2, -6.416},{-1, -2.46},{0, 1},{1, 3.5403},{2, 5.5839}, {3,8.01}};// cos x + 3x/2x
	// int[][] ans = {{-2, -8},{-1, -1},{0, 0},{1, 1},{2, 8}};//x^3
	public static void main(String[] args) {
		//Generate Random set of Trees
		genRandomTrees(treeArray);
//		for(int index = 0; index < treeArray.length; index++){
//			System.out.println("This is the best fitness: " + treeArray[index].fitness + " This is the program: " + printTree(treeArray[index]) 
//					+ " number of children: " + treeArray[index].numberOfChildren);		//get fitness
//		}
		treeArray = getFitness(treeArray, ans);
		//get elite trees
		eliteTrees = getElite(treeArray, ans.length);
		//crossover
		int generation = 2;
		outerLoop:
		while(fitness < ans.length && generation < genLim){
			crossover(eliteTrees);
			//calculate fitness and get elite trees
			treeArray = getFitness(treeArray, ans);//set fitnesses for trees
			for(int index = 0; index < treeArray.length; index++){
				if(treeArray[index].fitness == ans.length){
					Fittest = treeArray[index];
					break outerLoop;
				}
				System.out.println("This is the best fitness: " + treeArray[index].fitness + " This is the program: " + printTree(treeArray[index]) 
						+ " Generation: " + generation + " number of children: " + treeArray[index].numberOfChildren);
			}
			eliteTrees = getElite(treeArray, ans.length);//get elite trees
			System.out.println(generation);
			generation++;
		}
		
		
		ui u1 = new ui();
		u1.setVisible(true);
		u1.drawTree(Fittest.root, new Parser(0.0, 0.0));
		System.out.println("This is the best Tree: " + printTree(Fittest) + "it has fitness of: " + Fittest.fitness);

		
			
			
		
		// TODO Auto-generated method stub
		//String input = JOptionPane.showInputDialog("Give me an expression, use spaces");
		//System.out.println(input);
		//String[] inpArray = input.split(" ");
		//t = new Tree();
		//Node root = t.build(inpArray);
		//root = t.build(10);
		
		//System.out.println("done: " + Parser.Eval(root));
	}
	private static void genRandomTrees(Tree[] input){
		for(int index = 0; index < input.length; index++){
			input[index] = new Tree(treeDepth);
		}
	}
	private static String printTree(Tree currentFittest){
		ArrayList<String> s = Utility.flattenTree(currentFittest);
		s.trimToSize();
		StringBuilder fittest = new StringBuilder();
		for(int index = 0; index < s.size(); index++){
			fittest = fittest.append(s.get(index));
			fittest = fittest.append(" ");
		}
		return fittest.toString();
	}
	private static Tree[] getFitness(Tree[] input, double[][] ans){
		Parser p = new Parser(0.0, 0.0);
		for(int index = 0; index < input.length; index++){
			input[index].fitness = p.fitness(input[index].root, ans);
		}
		return input;
	}
	private static Tree[] getElite(Tree[] input, int fitnessLevel){
		Tree[] leet = new Tree[input.length/10];

		int countIndex = 0;//get smallest number
		outerLoop:
		while(fitnessLevel >= (-1*ans.length)){//while the fitness level is > -5
			for(int index = 0; index < input.length; index++){
				if(fitnessLevel == input[index].fitness){//check each tree for their fitness
					if(countIndex >= leet.length)
						break outerLoop;
					leet[countIndex] = input[index];
					countIndex++;
//					if(countIndex >= leet.length)
//						break outerLoop;
//					//areTreesDistinct(leet[leetIndex].root,input[index].root);
//					int leetIndex = 0;
//					boolean isDistinct = true;
//					for(leetIndex = 0; leetIndex < leet.length; leetIndex++){//go through each leet tree
//						if(leet[leetIndex] != null)
//							if(!(isDistinct = areTreesDistinct(leet[leetIndex].root,input[index].root))){
//								break;
//							}
//							
//					}
//					if(isDistinct){
//					leet[countIndex] = input[index];
//					countIndex++;
//					}
					
				}
			}
			fitnessLevel--;
		}
		return leet;
	}

	private static void crossover(Tree[] elites) {
		int r1, r2;
		Tree[] CrossOverResult = null;
		Parser p = new Parser(0.0, 0.0);
		for (int index = 0; index < treeArray.length - 1; index = index + 2) {// random
																	// mating
			r1 = rand.nextInt(elites.length);
			r2 = rand.nextInt(elites.length);
			while(r2 == r1){
				r2 = rand.nextInt(elites.length);
			}

			CrossOverResult = GeneticOperations.crossOver(elites[r1], elites[r2]);
			int temp = 0;
			if(CrossOverResult[0].numberOfChildren < CrossOverResult[1].numberOfChildren)
				temp = CrossOverResult[0].numberOfChildren;
			else
				temp = CrossOverResult[1].numberOfChildren;
			for(int i = 0; i<temp; i++){
			CrossOverResult = GeneticOperations.crossOver(CrossOverResult[0], CrossOverResult[1]);
				}
			treeArray[index] = CrossOverResult[0];
			treeArray[index + 1] = CrossOverResult[1];
			if (rand.nextDouble() <= 0.010) {// one percent get mutated
				//System.out.println("Mutating");
				int rm = rand.nextInt(treeArray.length);
				treeArray[rm] = GeneticOperations.mutation(treeArray[rm], rand.nextInt(treeDepth) + 2);
				treeArray[rm].fitness = p.fitness(treeArray[rm].root, ans);
				//System.out.println("This is the Mutation: Fitness: " + treeArray[rm].fitness + " This is the program: " + printTree(treeArray[rm]));
			}
		}

	}
	
	private static boolean areTreesDistinct(Node t1, Node t2){
		boolean result = false;
		if(t1.data.equals(t2.data)){
			if(t1.left != null && t2.left != null && (result = areTreesDistinct(t1.left, t2.left))){
				return result;
			}
			if(t1.right != null && t2.right != null && (result = areTreesDistinct(t1.right, t2.right))){
				return result;
			}
		}
		else
			return true;
		return result;			
		}
	
}


 

