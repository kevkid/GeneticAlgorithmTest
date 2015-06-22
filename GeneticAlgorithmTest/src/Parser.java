import java.math.BigDecimal;
import java.text.DecimalFormat;


public class Parser {
	double x = 0, y = 0;
	static DecimalFormat df = new DecimalFormat("####.######;-####.######");
	public Parser(double xin, Double yin){
		x = xin;
		y = yin;
	}
	public double Eval(Node input){
		double operandA = 0;
		double operandB = 0;
		double result = 0;
		if(input.leaf){
			if(input.data.equals("x")){
				return x;
			}
			else{
				return y;
			}
			 
		}
		operandA = Eval(input.left);
		if(!Utility.isUnary(input.data))
			operandB = Eval(input.right);
		switch(input.data){
		case "+":
			result = operandA + operandB;
			break;
		case "-":
			result = operandA - operandB;
			break;
		case "*":
			result = operandA * operandB;
			break;
		case "/":
			result = operandA / operandB;
			break;
		case "sin":
			result = Math.sin(operandA);
			break;
		case "cos":
			result = Math.cos(operandA);
			break;
		case "tan":
			result = Math.tan(operandA);
			break;
		case "sec":
			result = 1/Math.cos(operandA);
			break;
		case "csc":
			result = 1/Math.sin(operandA);
			break;
		case "cot":
			result =  Math.cos(operandA)/Math.sin(operandA);
			break;
		case "log":
			result = Math.log(operandA);
			break;
		case "^2":
			result = operandA*operandA;
			break;
		case "sqrt":
			result = Math.sqrt(operandA);
			break;
		}
		
		if(Double.isNaN(result)|| Double.isInfinite(result))//protected operation
			result = 1;
		return result;
	}


	public double fitness(Node root, double[][] ans){
		double result = 0;//how many tests it fails...
		//double evaluation;
		double ansTemp;
		double evalTemp;
		for(int index = 0; index < ans.length; index++){
			x = (double)(ans[index][0]);
			y = 0.0;//(double)(answerArray[index][1]);
			//evaluation = Eval(root);
			evalTemp = Double.valueOf(df.format(Eval(root)));
			ansTemp = Double.valueOf(df.format(ans[index][1]));
			result += evalTemp-ansTemp;//perfect score should be a difference of 0
		}
		
		return result;
	}
	public void setXY(int inX, int inY){
		x = inX;
		y = inY;
	}
	private static BigDecimal truncateDecimal(double x,int numberofDecimals)
	{
	    if ( x > 0) {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
	    } else {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
	    }
	}
}