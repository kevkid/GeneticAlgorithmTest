import java.util.Random;



public class GeneticOperations {
	static int point = 0;
	public static Tree[] crossOver(Tree t1, Tree t2){
		//Copy parents
		//if subtree1 is leaf, make sure subtree 2 is not
		//return tree
		Tree parent1 = new Tree(copyTree(t1.root));
		Tree parent2 = new Tree(copyTree(t2.root));
		double leafPercent = new Random().nextDouble();
		int cPoint1 = new Random().nextInt(parent1.numberOfChildren);
		//cPoint1 = (cPoint1 < 0) ? 1 : cPoint1;
		int cPoint2 = new Random().nextInt(parent2.numberOfChildren);
		point = cPoint1;//can return subtree of leaf and try to add a tree to that sub tree leaving one with a tree of one node
		Node subtree1 = getSubtree(parent1.root);
		if(leafPercent < 0.11){
			while(subtree1.leaf){
				cPoint1 = new Random().nextInt(parent1.numberOfChildren);
				cPoint1 = (cPoint1 < 0) ? 1 : cPoint1;
				point = cPoint1;
				subtree1 = getSubtree(parent1.root);
			}	
		}
		
		point = cPoint2;
		Node subtree2 = getSubtree(parent2.root);
		
		leafPercent = new Random().nextDouble();
		if(leafPercent < 0.11){
			while(subtree2.leaf){
				cPoint2 = new Random().nextInt(parent2.numberOfChildren);
				cPoint2 = (cPoint2 < 0) ? 1 : cPoint2;
				point = cPoint2;
				subtree2 = getSubtree(parent2.root);
			}
		}
		if(subtree1.leaf){
			while(subtree2.leaf){
			cPoint2 = new Random().nextInt(parent2.numberOfChildren);
			cPoint2 = (cPoint2 < 0) ? 1 : cPoint2;
			point = cPoint2;
			subtree2 = getSubtree(parent2.root);
		}
		}
		point = 0;

		if(subtree2.leaf){
			while(subtree1.leaf){
				cPoint1 = new Random().nextInt(parent1.numberOfChildren);
				cPoint1 = (cPoint1 < 0) ? 1 : cPoint1;
				point = cPoint1;
				subtree1 = getSubtree(parent1.root);
			}	
		}


		point = cPoint2;
		addSubtree(parent1.root, subtree2);
		parent1.maxDepth = Tree.maxDepth(parent1.root);
		parent1.numberOfChildren = Tree.countChildren(parent1.root);
		point = cPoint1;
		addSubtree(parent2.root, subtree1);
		parent2.maxDepth = Tree.maxDepth(parent2.root);
		parent2.numberOfChildren = Tree.countChildren(parent2.root);
		point = 0;
		Tree[] results = {parent1, parent2};
		
		/*if(results[0].root.leaf || results[1].root.leaf){
		System.out.println("Leaf Detected after creating crossover");
	}*/
		
		return results;
	}
	public static Tree mutation(Tree t, int depth){
		//int cPoint = new Random().nextInt(t.numberOfChildren);
		if(t.root.leaf){//if its a leaf tree
			t = new Tree(depth);//Tree cannot be to large
		}
		else{
			addSubtree(t.root, randomSubtree((depth)));//Tree cannot be to large
			//addSubtree(t.root, randomSubtree(Tree.maxDepth(getSubtree(t.root))));
		}
		return t;
	}
	/*in order to get the sub tree we need to count the nodes in the same order of building
	 * left -> root -> right
	 * */
	private static Node getSubtree(Node n){
		Node subTree = null;
		if(point == 0){
			subTree = copyTree(n);
			point = 0;
		}
		else{
			if(subTree == null && n.left != null){
				point--;
				subTree = getSubtree(n.left);
			}
				
			if(subTree == null && n.right != null){
				point--;
				subTree = getSubtree(n.right);
			}
		}
		return subTree;
	}
	private static void addSubtree(Node n, Node sub){
		if(point == 0 && n != null && sub != null){
			n.data = sub.data;
			n.leaf = sub.leaf;
			n.left = sub.left;
			n.right = sub.right;
			point = 0;
		}
		else{
			if(n.left != null){
				point--;
				addSubtree(n.left, sub);
			}
				if(n.right != null){
				point--;
				addSubtree(n.right, sub);
				}
				
		}
	}
	public static Node copyTree(Node root){
		Node result = new Node();
		result.data = root.data;
		result.leaf = root.leaf;
		if(root.left != null)
			result.left = copyTree(root.left);
		if(root.right != null)
			result.right = copyTree(root.right);
		return result;
		
	}
	private static Node randomSubtree(int depth){
		Tree t = new Tree(depth);
		return t.root;
	}
}
