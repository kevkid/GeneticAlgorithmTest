import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ui extends JFrame {
    JPanel tp;
    Tree t;
    Node root;
    Parser p;
    double x = 5, y = 3;
    
    int sizeX = 1000;
    int sizeY = 700;
    int depth = 10;
    public ui() {
        super("Simple Tree");
        setSize(sizeX, sizeY);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tp = new TPanel();
        JButton genButton = new JButton("Generate Tree");
       
        tp.add(genButton);
        add(tp);
        
    }
public void drawTree(Node r, Parser par){
	root = r;
	p = par;
}


    class TPanel extends JPanel {
        public TPanel() {
            setPreferredSize(new Dimension(sizeX, sizeY));// fill whole frame
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(root != null){
            	Eval(root, g, sizeX/2, 100);
            	g.drawString("This is the evaluation: " + p.Eval(root), 15, 100);
            }
            	
        }
    }
    public static void Eval(Node input, Graphics g, int x, int y) {
    	g.drawString(input.data, x, y);
    	if (!input.leaf) {
    			g.drawLine(x, y + 5, x - 20, y + 20);
    			Eval(input.left, g, x - 30, y + 30);
    		if (!Utility.isUnary(input.data)) {
    			g.drawLine(x, y + 5, x + 20, y + 20);
    			Eval(input.right, g, x + 30, y + 30);
    		}
    	}

    }

}