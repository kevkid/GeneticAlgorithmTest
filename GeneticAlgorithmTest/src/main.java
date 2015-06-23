import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;




public class main {
	static Tree t = null;
	static Node root = null;
	static int width, height;
	static Tree[] treeArray = new Tree[1000];
	static Tree[] eliteTrees = null;
	static Tree[] mutatedTrees = null;
	static Tree[] immigrateTrees = null;
	static Tree[] nextGenTrees = null;
	static Tree Fittest = null;
	static int treeDepth = 10;
	static double fitness = 0.0;
	static int genLim = 10000;
	static Random rand = new Random();
	static String prevTree = null;
	static int prevTreeCount = 0;
	static int mutationScaler = 1;
	static int treeCountThreshold = 3;
	static double[][] ans = {{-4, 96},{-3, 54},{-2, 24},{-1, 6},{0, 0},{1, 6},{2, 24},{3, 54},{4, 96}};//3x*2x
	//ans = {{-2, 4},{-1, 1},{0, 0},{1, 1},{2, 4}};// x^2
	//ans = {{-2, -8},{-1, -1},{0, 0},{1, 1},{2, 8}};//x^3
	//ans = { { -2, 16 }, { -1, 1 }, { 0, 0 }, { 1, 1 }, { 2, 16 } };// x^4

	//ans = {{-2, 0},{-1, -1},{0, 0},{1, 3},{2, 8}};//x^2+2x
	 //ans = {{-2.0, -0.9092974},{-1.0, -0.8414709},{0.0, 0.0},{1.0, 0.8414709},{2.0, 0.9092974}};//sin(x)
	// ans = {{-2.0, -0.4161468},{-1.0, 0.5403023},{0.0, 1.0},{1.0, 0.5403023},{2.0, -0.4161468}};//cos(x)
	//ans = {{-3, 0.010007503399554585},{-2, 0.5838531634528576},{-1, 1.5403023058681398},{0, 2.0},{1, 1.5403023058681398},{2, 0.5838531634528576},{3, 0.010007503399554585}};// cos x + 3x/2x
	//ans = {{-4, 95.34635637913638},{-3, 53.01000750339956},{-2, 23.583853163452858},{-1, 6.54030230586814},{0, 1.0},{1, 6.54030230586814},{2, 23.583853163452858},{3, 53.01000750339956},{4, 95.34635637913638}};//cos x + 3x*2x
	//ans = {{-4, 96},{-3, 54},{-2, 24},{-1, 6},{0, 0},{1, 6},{2, 24},{3, 54},{4, 96}};//3x*2x
	// int[][] ans = {{-2, -8},{-1, -1},{0, 0},{1, 1},{2, 8}};//x^3
	public static void main(String[] args) {
		//for(int i = -10; i <= 10; i++)
		//	System.out.print("{" + i + ", " + (i*i) + "},");
		//System.out.println("");
		long startTime = System.currentTimeMillis();
		//Generate Random set of Trees
		genRandomTrees(treeArray);//

		treeArray = getFitness(treeArray, ans);
		//get elite trees
		eliteTrees = getElite(treeArray, fitness);
		//get mutations
		mutatedTrees = mutate(eliteTrees, mutationScaler);
		//get immigrants
		immigrateTrees = immigrate(eliteTrees, mutationScaler);//probably can just be an number
		//create next generation to crossover
		nextGenTrees = nextGen(eliteTrees, mutatedTrees, immigrateTrees);
		//crossover
		int generation = 2;
		outerLoop:
		while(generation < genLim){
			crossover(nextGenTrees);
			//calculate fitness and get elite trees
			treeArray = getFitness(treeArray, ans);//set fitnesses for trees
			for(int index = 0; index < treeArray.length; index++){
				//System.out.println("This is the best fitness: " + treeArray[index].fitness + " This is the program: " + printTree(treeArray[index]) + " Generation: " + generation + " number of children: " + treeArray[index].numberOfChildren);
				if(treeArray[index].fitness == fitness){
					System.out.println(new Parser().fitness(treeArray[index].root, ans));
					Fittest = treeArray[index];
					break outerLoop;
				}
			}
			eliteTrees = getElite(treeArray, fitness);//get elite trees
			mutatedTrees = mutate(eliteTrees, mutationScaler);//get immigrants
			
			immigrateTrees = immigrate(eliteTrees, mutationScaler);//probably can just be an number
			
			nextGenTrees = nextGen(eliteTrees, mutatedTrees, immigrateTrees);//create next generation to crossover
			//Should go through mutation process and immigration process
			System.out.println("Generation: " + generation);
			generation++;
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		int sec = (int)TimeUnit.MILLISECONDS.toSeconds(totalTime);
		System.out.println("Number of seconds: " + sec);
		
		
		if(Fittest != null){
			ui u1 = new ui();
			u1.setVisible(true);
			u1.drawTree(Fittest.root, generation, sec);
			System.out.println("This is the best Tree: " + printTree(Fittest) + "it has fitness of: " + Fittest.fitness);	
		}
		else{
			System.out.println("No Fittest");
		}
		

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
		Parser p = new Parser();
		for(int index = 0; index < input.length; index++){
			input[index].fitness = p.fitness(input[index].root, ans);
		}
		return input;
	}
	private static Tree[] getElite(Tree[] input, double fitnessLevel){
		Tree[] leet = new Tree[input.length/10];
		double lowestDistance = Math.abs(input[0].fitness - 0);
		double distance;
		int closestIndex = 0;
		int countIndex = 0;//get smallest number
		outerLoop:
		while(countIndex < leet.length){//while the leet array hasnt filled up
			for(int index = 0; index < input.length; index++){
				distance = Math.abs(input[index].fitness) ;
				if(distance < lowestDistance){//check each tree for their fitness
					lowestDistance = distance;
					closestIndex = index;		
				}
			}
			leet[countIndex] = input[closestIndex];
			leet[countIndex].fitness = input[closestIndex].fitness;
			input[closestIndex].fitness = Double.MIN_VALUE;
			countIndex++;	
		}
		leet[0].fitness = new Parser().fitness(leet[0].root, ans);
		if(printTree(leet[0]).equals(prevTree)){
			if(prevTreeCount > treeCountThreshold){
				mutationScaler++;
				prevTreeCount = 0;//reset so we dont go crazy
			}
			prevTreeCount++;
		}
		else{
			mutationScaler = 1;
			prevTreeCount = 0;
			//mutationScaler--;
		}
		prevTree = printTree(leet[0]);
		System.out.println("This is the best fitness: " + leet[0].fitness + " This is the program: " + printTree(leet[0]) + " number of children: " + leet[0].numberOfChildren);
		return leet;
	}

	private static void crossover(Tree[] elites) {
		int r1, r2;
		Tree[] CrossOverResult = null;
		Parser p = new Parser();
		for(int index = 0; index < eliteTrees.length; index++){//Our surviors
			treeArray[index] = eliteTrees[index];
		}
		for (int index = eliteTrees.length; index < treeArray.length - 1; index = index + 2) {
			r1 = rand.nextInt(elites.length);
			r2 = rand.nextInt(elites.length);
			if(r1 < eliteTrees.length){
				r1 += eliteTrees.length;
			}
			if(r2 < eliteTrees.length){
				r2 += eliteTrees.length;
			}
			while(r2 == r1){
				r2 = rand.nextInt(elites.length);
				if(r2 < eliteTrees.length){
					r2 += eliteTrees.length;
				}
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
		}

	}//
	
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
	
	private static Tree[] mutate(Tree[] treeArray, int mutateScal){
		Tree[] Mutated = new Tree[treeArray.length*mutateScal];
		for(int outerIndex = 0; outerIndex < mutateScal; outerIndex++){
			for(int index = 0; index < treeArray.length; index++){
			Mutated[index+(treeArray.length*outerIndex)] = new Tree(GeneticOperations.copyTree(treeArray[index].root));
		}
		}
		
		Parser p = new Parser();
		for (int index = 0; index < Mutated.length; index++) {//mutate each of the elites
			if (rand.nextDouble() <= 1.10) {// 100% get mutated
				Mutated[index] = GeneticOperations.mutation(Mutated[index], rand.nextInt(treeDepth) + 2);
				Mutated[index].fitness = p.fitness(Mutated[index].root, ans);
			//System.out.println("This is the Mutation: Fitness: " + Mutated[index].fitness + " This is the program: " + printTree(Mutated[index]));
			}
		}
		System.out.println("Mutated Scaler = " + mutateScal);
		return Mutated;
		
	}
	private static Tree[] immigrate(Tree[] treeArray, int immigrateScal){
		Tree[] immigrated = new Tree[(treeArray.length*immigrateScal)/10];//1% of original population
		for (int index = 0; index < immigrated.length; index++) {
			immigrated[index] = new Tree(treeDepth);//generate random trees
			immigrated[index].fitness = new Parser().fitness(immigrated[index].root, ans);
		}
		return immigrated;
	}
	private static Tree[] nextGen(Tree[] elite, Tree[] mutated,Tree[] immigrants){
		ArrayList<Tree> nextGen = new ArrayList<Tree>();
		for (Tree tree : elite) {
			nextGen.add(tree);
		}
		for (Tree tree : mutated) {
			nextGen.add(tree);
		}
		for (Tree tree : immigrants) {
			nextGen.add(tree);
		}
		return nextGen.toArray(new Tree[nextGen.size()]);
	}
	
}


 

