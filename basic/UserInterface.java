package basic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafxdraw.Draw;
import knapsack.KnapsackGroup;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

public class UserInterface implements Files {
	private JFrame MainFrame;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	JPanel MainMenu;
	JPanel ParcelPanel;
	JPanel PentominoPanel;
	
	JPanel rightSide;
	static JPanel leftSide;
	
	JPanel rightPanel;
	JPanel leftPanel;

	JLabel calculate;

	Color gradient1 = new Color(212,207,201);
	Color gradient2 = new Color(52,71,84);
	
	Font myFont= new Font("SansSerif", Font.BOLD, 30);
	Font myFont2= new Font("SansSerif", Font.PLAIN, 20);
	Font myFont3= new Font("SansSerif", Font.BOLD, 20);
	Color myColor= new Color(255,255,255);

	private int x,y;
	private String Title;
	public UserInterface(int x, int y, String Title) {
		this.x = x;
		this.y = y;
		this.Title = Title;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				MainFrame= new JFrame(Title);
				MainFrame.setSize(x, y);
				SetUpMainMenu();
				SetUpParcelMenu();
				SetUpPentominoMenu();
				MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				MainFrame.setResizable(false);
				MainFrame.setVisible(true);
			}
		};
		
		SwingUtilities.invokeLater(r);
	}

	public void reset(){
//		MainFrame = new JFrame(Title);
//		MainFrame.setSize(x, y);
//		SetUpMainMenu();
//		SetUpParcelMenu();
//		SetUpPentominoMenu();
//		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		MainFrame.setResizable(false);
//		MainFrame.setVisible(true);
		MainFrame.setVisible(false);
		MainFrame=null;
		new UserInterface();
	}
	public UserInterface() {
		this(750, 750,"Parcelminoes");
	}
	
	public void SetUpMainMenu() {
		
		MainMenu= new JPanel(){
			@Override
            public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
				final BufferedImage image = ImageIO.read(new File(Files.MAIN_BG));
				g.drawImage(image, 0, 0, null);
			} catch (IOException e) {
				
				Point p1 = new Point(10, getHeight() - 10);
	            Point p2 = new Point(getWidth() - 10, 10);
	            
	            GradientPaint gp = new GradientPaint( p1, gradient1,  p2, gradient2, true);

	            Graphics2D g2 = (Graphics2D) g;
	            g2.setPaint(gp);
	            g.fillRect(0, 0, getWidth(), getHeight());
			}
            
			}
		};
		
		MainMenu.setBorder(new EmptyBorder(MainFrame.getHeight()/3, MainFrame.getWidth()/4, MainFrame.getHeight()/4, MainFrame.getWidth()/4));
		
		JButton Parcel_Button= new JButton("Parcel ");
		Parcel_Button.setContentAreaFilled(false);
		Parcel_Button.setHorizontalAlignment(SwingConstants.LEADING);
		Parcel_Button.setBorder(null);
		Parcel_Button.addActionListener(new ParcelListener());
		Parcel_Button.setFont(myFont);
		Parcel_Button.setForeground(myColor);
		
		JButton Pentomino_Button = new JButton("Pentomino");
		Pentomino_Button.setContentAreaFilled(false);
		Pentomino_Button.setHorizontalAlignment(SwingConstants.LEADING);
		Pentomino_Button.setBorder(null);
		Pentomino_Button.addActionListener(new PentominoListener());
		Pentomino_Button.setFont(myFont);
		Pentomino_Button.setForeground(myColor);
		
		
		JPanel menuPanel = new JPanel(new GridLayout(0, 1, 20, 20));
		menuPanel.setOpaque(false);
		menuPanel.add(Parcel_Button);
		menuPanel.add(Pentomino_Button);
		MainMenu.add(menuPanel);
		MainFrame.add(MainMenu);
	}
	
	public void SetUpParcelMenu() {
		ParcelPanel= new JPanel(){
			@Override
            public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
				final BufferedImage image = ImageIO.read(new File(Files.BG));
				g.drawImage(image, 0, 0, null);
			} catch (IOException e) {
				
				Point p1 = new Point(10, getHeight() - 10);
	            Point p2 = new Point(getWidth() - 10, 10);
	            
	            GradientPaint gp = new GradientPaint( p1, gradient1,  p2, gradient2, true);

	            Graphics2D g2 = (Graphics2D) g;
	            g2.setPaint(gp);
	            g.fillRect(0, 0, getWidth(), getHeight());
			}
            
			}
		};
		ParcelPanel.setLayout(null);
		
		JButton Back_Button = new JButton("Back");
		Back_Button.setContentAreaFilled(false);
		Back_Button.setBorder(null);
		Back_Button.addActionListener(new BackListenerParcel());
		Back_Button.setFont(myFont);
		Back_Button.setForeground(myColor);
		ParcelPanel.add(Back_Button);
		Back_Button.setBounds(5, 640, 100, 100);
		
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
	    
	    formatter.setCommitsOnValidEdit(true);
	    
	    JFormattedTextField vA_Value = new JFormattedTextField(formatter);vA_Value.setBounds(0,0,40,40);vA_Value.setBounds(335,285,50,50);vA_Value.setText("3");vA_Value.setFont(myFont2);
	    JFormattedTextField vB_Value = new JFormattedTextField(formatter);vB_Value.setBounds(50,50,40,40);vB_Value.setBounds(435,285,50,50);vB_Value.setText("4");vB_Value.setFont(myFont2);
	    JFormattedTextField vC_Value = new JFormattedTextField(formatter);vC_Value.setBounds(100,100,40,40);vC_Value.setBounds(535,285,50,50);vC_Value.setText("5");vC_Value.setFont(myFont2);
	    
	    JFormattedTextField vA_Quantity = new JFormattedTextField(formatter);vA_Quantity.setBounds(150,150,40,40);vA_Quantity.setBounds(335,365,50,50);vA_Quantity.setText("100");vA_Quantity.setFont(myFont2);
	    JFormattedTextField vB_Quantity = new JFormattedTextField(formatter);vB_Quantity.setBounds(200,200,40,40);vB_Quantity.setBounds(435,365,50,50);vB_Quantity.setText("100");vB_Quantity.setFont(myFont2);
	    JFormattedTextField vC_Quantity = new JFormattedTextField(formatter);vC_Quantity.setBounds(250,250,40,40);vC_Quantity.setBounds(535,365,50,50);vC_Quantity.setText("100");vC_Quantity.setFont(myFont2);
	    
	    JLabel Parcel_Label= new JLabel("Parcel:");Parcel_Label.setFont(myFont);Parcel_Label.setForeground(myColor);Parcel_Label.setBounds(170,188,150,80);
	    JLabel Quantity_Label= new JLabel("Quantity:");Quantity_Label.setFont(myFont);Quantity_Label.setForeground(myColor);Quantity_Label.setBounds(170,348,150,80);
	    JLabel Value_Label= new JLabel("Value:");Value_Label.setFont(myFont);Value_Label.setForeground(myColor);Value_Label.setBounds(170,268,150,80);
	
	    JLabel A_Label= new JLabel("A");A_Label.setFont(myFont);A_Label.setForeground(myColor);A_Label.setBounds(350,188,150,80);
	    JLabel B_Label= new JLabel("B");B_Label.setFont(myFont);B_Label.setForeground(myColor);B_Label.setBounds(450,188,150,80);
	    JLabel C_Label= new JLabel("C");C_Label.setFont(myFont);C_Label.setForeground(myColor);C_Label.setBounds(550,188,150,80);
	    
	    JCheckBox show = new JCheckBox("Show Steps "); show.setBounds(300, 400, 200, 100);show.setOpaque(false);show.setFont(myFont3);show.setForeground(myColor);
		
	    ParcelPanel.add(show);
		
	    ParcelPanel.add(vA_Value);
	    ParcelPanel.add(vB_Value);
	    ParcelPanel.add(vC_Value);
	    
	    ParcelPanel.add(vA_Quantity);
	    ParcelPanel.add(vB_Quantity);
	    ParcelPanel.add(vC_Quantity);
		
		ParcelPanel.add(Parcel_Label);
		ParcelPanel.add(Quantity_Label);
		ParcelPanel.add(Value_Label);
		
		ParcelPanel.add(A_Label);
		ParcelPanel.add(B_Label);
		ParcelPanel.add(C_Label);
		
		JButton Apply_Button = new JButton("Continue");
		Apply_Button.setContentAreaFilled(false);
		Apply_Button.setBorder(null);
		Apply_Button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ParcelSettings x = new ParcelSettings(Integer.parseInt(vA_Value.getText()), Integer.parseInt(vB_Value.getText()), Integer.parseInt(vC_Value.getText()), Integer.parseInt(vA_Quantity.getText()), Integer.parseInt(vB_Quantity.getText()), Integer.parseInt(vC_Quantity.getText()),show.isSelected());
				MainFrame.remove(ParcelPanel);
				resultWindow(x);
				feedToAlgorithm(x);
				displayKnapsack();
			}});
		Apply_Button.setFont(myFont);
		Apply_Button.setForeground(myColor);
		ParcelPanel.add(Apply_Button);
		Apply_Button.setBounds(580, 640, 150, 100);
		
		
	}
	
	public void SetUpPentominoMenu() {
		PentominoPanel= new JPanel(){
			@Override
            public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
				final BufferedImage image = ImageIO.read(new File(Files.BG));
				g.drawImage(image, 0, 0, null);
			} catch (IOException e) {
				
				Point p1 = new Point(10, getHeight() - 10);
	            Point p2 = new Point(getWidth() - 10, 10);
	            
	            GradientPaint gp = new GradientPaint( p1, gradient1,  p2, gradient2, true);

	            Graphics2D g2 = (Graphics2D) g;
	            g2.setPaint(gp);
	            g.fillRect(0, 0, getWidth(), getHeight());
			}
            
			}
		};
		
		PentominoPanel.setLayout(null);
		JButton Back_Button2 = new JButton("Back");
		Back_Button2.setContentAreaFilled(false);
		Back_Button2.setBorder(null);
		Back_Button2.addActionListener(new BackListenerPentomino());
		Back_Button2.setFont(myFont);
		Back_Button2.setForeground(myColor);
		PentominoPanel.add(Back_Button2);
		Back_Button2.setBounds(5, 640, 100, 100);
		

		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
	    
	    formatter.setCommitsOnValidEdit(true);
	    
	    
	    JFormattedTextField L_Value = new JFormattedTextField(formatter);L_Value.setBounds(335,285,50,50);L_Value.setText("3");L_Value.setFont(myFont2);
	    JFormattedTextField P_Value = new JFormattedTextField(formatter);P_Value.setBounds(435,285,50,50);P_Value.setText("4");P_Value.setFont(myFont2);
	    JFormattedTextField T_Value = new JFormattedTextField(formatter);T_Value.setBounds(535,285,50,50);T_Value.setText("5");T_Value.setFont(myFont2);
	    
	    JFormattedTextField L_Quantity = new JFormattedTextField(formatter);L_Quantity.setBounds(335,365,50,50);L_Quantity.setText("100");L_Quantity.setFont(myFont2);
	    JFormattedTextField P_Quantity = new JFormattedTextField(formatter);P_Quantity.setBounds(435,365,50,50);P_Quantity.setText("100");P_Quantity.setFont(myFont2);
	    JFormattedTextField T_Quantity = new JFormattedTextField(formatter);T_Quantity.setBounds(535,365,50,50);T_Quantity.setText("100");T_Quantity.setFont(myFont2);
	    
	    JLabel Pentomino_Label= new JLabel("Pentomino:");Pentomino_Label.setFont(myFont);Pentomino_Label.setForeground(myColor);Pentomino_Label.setBounds(120,188,200,80);
	    JLabel Quantity_Label= new JLabel("Quantity:");Quantity_Label.setFont(myFont);Quantity_Label.setForeground(myColor);Quantity_Label.setBounds(120,348,150,80);
	    JLabel Value_Label= new JLabel("Value:");Value_Label.setFont(myFont);Value_Label.setForeground(myColor);Value_Label.setBounds(120,268,150,80);
	
	    JLabel A_Label= new JLabel("L");A_Label.setFont(myFont);A_Label.setForeground(myColor);A_Label.setBounds(350,188,150,80);
	    JLabel B_Label= new JLabel("P");B_Label.setFont(myFont);B_Label.setForeground(myColor);B_Label.setBounds(450,188,150,80);
	    JLabel C_Label= new JLabel("T");C_Label.setFont(myFont);C_Label.setForeground(myColor);C_Label.setBounds(550,188,150,80);
	    
	    JCheckBox show = new JCheckBox("Show Steps "); show.setBounds(300, 400, 200, 100);show.setOpaque(false);show.setFont(myFont3);show.setForeground(myColor);
		
	    PentominoPanel.add(show);
	    
	    PentominoPanel.add(L_Value);
	    PentominoPanel.add(T_Value);
	    PentominoPanel.add(P_Value);
	    
	    PentominoPanel.add(L_Quantity);
	    PentominoPanel.add(T_Quantity);
	    PentominoPanel.add(P_Quantity);
		
	    PentominoPanel.add(Pentomino_Label);
	    PentominoPanel.add(Quantity_Label);
	    PentominoPanel.add(Value_Label);
		
	    PentominoPanel.add(A_Label);
	    PentominoPanel.add(B_Label);
	    PentominoPanel.add(C_Label);
			    
		JButton Apply_Button = new JButton("Continue");
		Apply_Button.setContentAreaFilled(false);
		Apply_Button.setBorder(null);
		Apply_Button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				PentominoSettings x = new PentominoSettings(Integer.parseInt(L_Value.getText()), Integer.parseInt(P_Value.getText()), Integer.parseInt(T_Value.getText()), Integer.parseInt(L_Quantity.getText()), Integer.parseInt(P_Quantity.getText()), Integer.parseInt(T_Quantity.getText()),show.isSelected());
				MainFrame.remove(PentominoPanel);
				resultWindow(x);
				feedToAlgorithm(x);
				displayKnapsack();

			}});
		Apply_Button.setFont(myFont);
		Apply_Button.setForeground(myColor);
		PentominoPanel.add(Apply_Button);
		Apply_Button.setBounds(580, 640, 150, 100);
		
		
	}
	
	public void resultWindow(Object j) {
		boolean parcel;
		
		if(j instanceof PentominoSettings) {
			parcel=false;
			}
		else {
			parcel=true;
			ParcelSettings myS= (ParcelSettings) j;
		}
		MainFrame.setLocation(0, 0);
		MainFrame.setSize((int)(screenSize.width*0.95),(int) (screenSize.height*0.95));MainFrame.setLayout(null);
		rightSide= new JPanel();rightSide.setBounds(0, 0, MainFrame.getWidth()/5, MainFrame.getHeight());rightSide.setLayout(null);
		leftSide= new JPanel();leftSide.setBounds(MainFrame.getWidth()/5,0, MainFrame.getWidth()-MainFrame.getWidth()/5, MainFrame.getHeight());
		MainFrame.add(rightSide);
		MainFrame.add(leftSide);
		rightSide.setBackground(gradient1);
		leftSide.setBackground(gradient2);
		
		setUpLeftSide();
		
		JButton BackToMenu = new JButton("Main Menu");BackToMenu.setFont(myFont3);
		
		rightSide.add(BackToMenu);BackToMenu.setBounds(0,rightSide.getHeight()-90,130,50);BackToMenu.setOpaque(false);BackToMenu.setBorder(null);BackToMenu.setBackground(null);
		BackToMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.setVisible(false);
				reset();
			}
			
		});
		
		
		if(!parcel) {
			
			PentominoSettings myS= (PentominoSettings) j;
			
			rightPanel= new JPanel();//rightPanel.setBorder(new EmptyBorder(rightSide.getHeight()/15,rightSide.getWidth()/10,rightSide.getHeight()/15,rightSide.getWidth()/10));
			JPanel rightArea = new JPanel(new BorderLayout());//new GridLayout(0, 1, 20, 20)
			rightPanel.setLayout(null);
			
			rightPanel.setOpaque(false);
			rightPanel.setOpaque(false);
			
			JLabel titel_L = new JLabel("Pentomino Knapsack");titel_L.setFont(myFont3);
			JLabel l = new JLabel("Pentomino");l.setFont(myFont3);
			JLabel lv = new JLabel("Value");lv.setFont(myFont3);
			JLabel lq = new JLabel("Quantity");lq.setFont(myFont3);
			JLabel l1 = new JLabel("L:");l1.setFont(myFont3);
			JLabel l2 = new JLabel("P:");l2.setFont(myFont3);
			JLabel l3 = new JLabel("T:");l3.setFont(myFont3);

			
			rightSide.add(titel_L);titel_L.setBounds(rightSide.getWidth()/5,10,rightSide.getWidth()*2/3,100);
			rightSide.add(l);l.setBounds(5, 100, rightSide.getWidth(), 100);
			rightSide.add(lv);lv.setBounds(20+rightSide.getWidth()/3, 100, rightSide.getWidth(), 100);
			rightSide.add(lq);lq.setBounds(rightSide.getWidth()*2/3, 100, rightSide.getWidth(), 100);
			
			rightSide.add(l1);l1.setBounds(40, 150, rightSide.getWidth(), 100);
			rightSide.add(l2);l2.setBounds(40, 200, rightSide.getWidth(), 100);
			rightSide.add(l3);l3.setBounds(40, 250, rightSide.getWidth(), 100);
			
			JLabel vA = new JLabel(""+myS.vL);JLabel qA = new JLabel(""+myS.qL);
			JLabel vB = new JLabel(""+myS.vP);JLabel qB = new JLabel(""+myS.qP);
			JLabel vC = new JLabel(""+myS.vT);JLabel qC = new JLabel(""+myS.qT);
			
			vA.setFont(myFont3);qA.setFont(myFont3);
			vB.setFont(myFont3);qB.setFont(myFont3);
			vC.setFont(myFont3);qC.setFont(myFont3);
			
			rightSide.add(vA);vA.setBounds(40+rightSide.getWidth()/3, 150, rightSide.getWidth(), 100);
			rightSide.add(vB);vB.setBounds(40+rightSide.getWidth()/3, 200, rightSide.getWidth(), 100);
			rightSide.add(vC);vC.setBounds(40+rightSide.getWidth()/3, 250, rightSide.getWidth(), 100);
			
			rightSide.add(qA);qA.setBounds(20+rightSide.getWidth()*2/3, 150, rightSide.getWidth(), 100);
			rightSide.add(qB);qB.setBounds(20+rightSide.getWidth()*2/3, 200, rightSide.getWidth(), 100);
			rightSide.add(qC);qC.setBounds(20+rightSide.getWidth()*2/3, 250, rightSide.getWidth(), 100);
			
			//rightSide.add(l4);
			//rightSide.add(l5);
			
			
			//rightPanel.add(rightArea);
			//rightSide.add(rightPanel);
			
		}
		else {
			ParcelSettings myS= (ParcelSettings) j;
			
			rightPanel= new JPanel();//rightPanel.setBorder(new EmptyBorder(rightSide.getHeight()/15,rightSide.getWidth()/10,rightSide.getHeight()/15,rightSide.getWidth()/10));
			JPanel rightArea = new JPanel(new BorderLayout());//new GridLayout(0, 1, 20, 20)
			rightPanel.setLayout(null);
			
			rightPanel.setOpaque(false);
			rightPanel.setOpaque(false);
			
			JLabel titel_L = new JLabel("Parcel Knapsack");titel_L.setFont(myFont3);
			JLabel l = new JLabel("Parcel");l.setFont(myFont3);
			JLabel lv = new JLabel("Value");lv.setFont(myFont3);
			JLabel lq = new JLabel("Quantity");lq.setFont(myFont3);
			JLabel l1 = new JLabel("A:");l1.setFont(myFont3);
			JLabel l2 = new JLabel("B:");l2.setFont(myFont3);
			JLabel l3 = new JLabel("C:");l3.setFont(myFont3);

			
			rightSide.add(titel_L);titel_L.setBounds(rightSide.getWidth()/5,10,rightSide.getWidth()*2/3,100);
			rightSide.add(l);l.setBounds(5, 100, rightSide.getWidth(), 100);
			rightSide.add(lv);lv.setBounds(20+rightSide.getWidth()/3, 100, rightSide.getWidth(), 100);
			rightSide.add(lq);lq.setBounds(rightSide.getWidth()*2/3, 100, rightSide.getWidth(), 100);
			
			rightSide.add(l1);l1.setBounds(20, 150, rightSide.getWidth(), 100);
			rightSide.add(l2);l2.setBounds(20, 200, rightSide.getWidth(), 100);
			rightSide.add(l3);l3.setBounds(20, 250, rightSide.getWidth(), 100);
			
			JLabel vA = new JLabel(""+myS.vA);JLabel qA = new JLabel(""+myS.qA);
			JLabel vB = new JLabel(""+myS.vB);JLabel qB = new JLabel(""+myS.qB);
			JLabel vC = new JLabel(""+myS.vC);JLabel qC = new JLabel(""+myS.qC);
			
			vA.setFont(myFont3);qA.setFont(myFont3);
			vB.setFont(myFont3);qB.setFont(myFont3);
			vC.setFont(myFont3);qC.setFont(myFont3);
			
			rightSide.add(vA);vA.setBounds(40+rightSide.getWidth()/3, 150, rightSide.getWidth(), 100);
			rightSide.add(vB);vB.setBounds(40+rightSide.getWidth()/3, 200, rightSide.getWidth(), 100);
			rightSide.add(vC);vC.setBounds(40+rightSide.getWidth()/3, 250, rightSide.getWidth(), 100);
			
			rightSide.add(qA);qA.setBounds(20+rightSide.getWidth()*2/3, 150, rightSide.getWidth(), 100);
			rightSide.add(qB);qB.setBounds(20+rightSide.getWidth()*2/3, 200, rightSide.getWidth(), 100);
			rightSide.add(qC);qC.setBounds(20+rightSide.getWidth()*2/3, 250, rightSide.getWidth(), 100);
			
			//rightSide.add(l4);
			//rightSide.add(l5);
			
			
			//rightPanel.add(rightArea);
			//rightSide.add(rightPanel);
		}
		
		refreshFrame();
		
	}
	
	public void setUpLeftSide() {
		leftPanel= new JPanel();leftPanel.setBorder(new EmptyBorder(leftSide.getHeight()/4,leftSide.getWidth()/4,leftSide.getHeight()/4,leftSide.getWidth()/4));
		calculate= new JLabel("Calculating Optimal Solution....");calculate.setFont(myFont);calculate.setForeground(myColor);
		leftPanel.add(calculate);
		leftPanel.setOpaque(false);

		leftSide.add(leftPanel);
		//calculate.setBounds(leftSide.getWidth()/2, leftSide.getHeight()/2, calculate.getWidth(), calculate.getHeight());
	}

	public void feedToAlgorithm(Object obj){}

	public void refreshFrame() {MainFrame.setVisible(false);MainFrame.setVisible(true);}
	
	class ParcelListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.remove(MainMenu);
			MainFrame.add(ParcelPanel);
			refreshFrame();
			
		}
	}


	
	class PentominoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.remove(MainMenu);
			MainFrame.add(PentominoPanel);
			refreshFrame();
		}
		
	}
	
	class BackListenerParcel implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.remove(ParcelPanel);
			MainFrame.add(MainMenu);
			refreshFrame();
			
		}	
	}
	
	class BackListenerPentomino implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.remove(PentominoPanel);
			MainFrame.add(MainMenu);
			refreshFrame();
			
		}	
	}

	private static final double WIDTH = 800;
	private static final double HEIGHT = 800;
	private static final int angle = 5;
	private static double anchorX;
	private static double anchorY;
	private static double anchorAngleX = 0;
	private static double anchorAngleY = 0;
	private static final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleY = new SimpleDoubleProperty(0);
	public static Group root ;
	public static Scene scene;
	public static void draw(JFXPanel jfxPanel) {
		System.out.println("Draw gets called");
		root = new Group();
		scene = new Scene(root, leftSide.getWidth(),leftSide.getHeight(),true);
		KnapsackGroup knapsackGroup = KnapsackGroup.example;
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.setTranslateZ(-500);
		root.getChildren().addAll(knapsackGroup);

		knapsackGroup.setTranslateX(WIDTH / 2 - 100);
		knapsackGroup.setTranslateY(HEIGHT / 2);
		scene.setCamera(camera);

		initMouseControl(knapsackGroup, scene);
		addEventHandler(scene, knapsackGroup);


		jfxPanel.setScene(scene);

	}

	private static void initMouseControl(KnapsackGroup knapsackGroup, Scene scene) {
		Rotate xRotate;
		Rotate yRotate;
		knapsackGroup.getTransforms().addAll(
				xRotate = new Rotate(0, Rotate.X_AXIS),
				yRotate = new Rotate(0, Rotate.Y_AXIS)

		);
		xRotate.angleProperty().bind(angleX);
		yRotate.angleProperty().bind(angleY);
		scene.setOnMousePressed(event -> {
			anchorX = event.getSceneX();
			anchorY = event.getSceneY();
			anchorAngleX = angleX.get();
			anchorAngleY = angleY.get();
		});
		scene.setOnMouseDragged(event -> {
			angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
			angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
		});
		scene.addEventHandler(ScrollEvent.SCROLL, event ->{
			double delta = event.getDeltaY();
			knapsackGroup.setTranslateZ(knapsackGroup.getTranslateZ() + delta);
		});
	}




	private static void addEventHandler(Scene primaryStage, KnapsackGroup group) {
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent t) {
						switch (t.getCode()) {
							case Z:
								KnapsackGroup g = new KnapsackGroup(KnapsackGroup.test2, 20);
								group.setParcelGroups(g.getParcelGroups());
								break;
							case Q:
								group.rotateByX(-angle);
								break;
							case E:
								group.rotateByX(angle);
								break;
							case W:
								group.rotateByZ(angle);
								break;
							case S:
								group.rotateByZ(-angle);
								break;
							case A:
								group.rotateByY(angle);
								break;
							case D:
								group.rotateByY(-angle);
								break;
							case NUMPAD4:
								group.translateXProperty().set((group.getTranslateX() - 10));
								System.out.println("Moving left" + group.translateXProperty().get());
								break;
							case NUMPAD6:
								group.translateXProperty().set((group.getTranslateX()) + 10);
								System.out.println("Moving right" + group.translateXProperty().get());
								break;
							case NUMPAD8:
								group.translateZProperty().set((group.getTranslateZ()) + 10);
								System.out.println("Moving forward" + group.translateXProperty().get());
								break;
							case NUMPAD2:
								group.translateZProperty().set((group.getTranslateZ()) - 10);
								System.out.println("Moving backward" + group.translateXProperty().get());
								break;
							default:
								break;
						}
					}
				}
		);

	}
	public void displayKnapsack(){
		leftPanel.setLayout(null);
		leftPanel.remove(calculate);
		leftPanel.setBorder(null);
		leftPanel.setOpaque(false);
		leftSide.setOpaque(false);

		final JFXPanel fxPanel = new JFXPanel();

		leftSide= new JPanel();
		leftSide.setBounds(MainFrame.getWidth()/5,0, MainFrame.getWidth()-MainFrame.getWidth()/5, MainFrame.getHeight());
		leftSide.add(fxPanel);
		MainFrame.add(leftSide);

		refreshFrame();

		Platform.runLater(
				new Runnable(){
					@Override
					public void run() {
						draw(fxPanel);
					}
				}
		);

	}
}
