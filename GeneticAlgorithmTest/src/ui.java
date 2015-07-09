import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
public class ui extends JFrame {
    JPanel tp,chartPanel;
    Tree t;
    Node root;
    Parser p;
    double x = 0, y = 0;
    int generation = 0;
    int timeSeconds = 0;
    double fitness = Double.MIN_VALUE;
    int sizeX = 1000;
    int sizeY = 1000;
    int depth = 10;
    public ui() {
        super("Simple Tree");
        setSize(sizeX, sizeY);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tp = new TPanel();
        tp.setLayout(new BorderLayout());
        chartPanel = new TPanel();
        chartPanel.setLayout(new BorderLayout());
        //JButton genButton = new JButton("Generate Tree");
        //tp.add(genButton);
        
        add(tp);
        add(chartPanel);
        
    }
    public ChartPanel GraphTree(Node n, Node ansNode){
    	XYSeriesCollection dataSet = new XYSeriesCollection();
    	XYSeries series = new XYSeries("Approximation");
    	XYSeries actual = new XYSeries("Actual");
    	Parser p = new Parser();
    	for(double index = -20; index < 20; index+=0.1){
    		series.add(index, p.TreeOutputAtPoint(n,index));
    		actual.add(index, p.TreeOutputAtPoint(ansNode, index));
    	}
    	dataSet.addSeries(series);
    	dataSet.addSeries(actual);
    	JFreeChart jc = ChartFactory.createXYLineChart("Tree Graph", "Input", "Output",dataSet,
    		PlotOrientation.VERTICAL, true, true, false);
    	ChartPanel cp = new ChartPanel(jc);
    	cp.setPreferredSize(new java.awt.Dimension(200, 500));
    	cp.setVisible(true);
    	cp.setMouseWheelEnabled(true);
    	cp.setDomainZoomable(true);
    	cp.setRangeZoomable(true);
    	chartPanel.add(cp, BorderLayout.SOUTH);
    	chartPanel.validate();
    	chartPanel.removeAll();
    	
    	//chartPanel.revalidate();
    	//tp.repaint();
    	return cp;
    }
public void drawTree(Node r, int gen, double fit, int timeSec){
	root = r;
	p = new Parser();
	generation = gen;
	timeSeconds = timeSec;
	fitness = fit;
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
            	g.drawString("Generation: " + generation, 15, 100);
            	g.drawString("Time it took: " + timeSeconds + " seconds", 15, 120);
            	g.drawString("Fitness: " + fitness, 15, 140);
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