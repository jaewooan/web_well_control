package ML_ERD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

import javax.swing.border.BevelBorder;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JMenuItem;

class resultPlot extends JFrame {
	int MenuPnlXsrt = 0;
	int MenuPnlYsrt = 0;
	
	int ChartXsize = 300;
	int ChartYsize = 200;
	int ChartPnlXsize = ChartXsize*280/300;
	int ChartPnlYsize = ChartYsize;
	int btnXsrt = 10;
	int btnYsrt = 10;
	int btnXsize = 135;
	int btnYsize = 40;
	int CheckBoxXsrt = btnXsrt*2+btnXsize;
	int CheckBoxYsrt = 30;
	int CheckBoxXsize = 220;
	int CheckBoxYsize = 25;
	int MenuPnlXsize = ChartXsize*4;
	int MenuPnlYsize = CheckBoxYsize*4+10;
	int pnlChartXsize = 150;
	int pnlChartYsize = 100;
	int pnlChartXsrt = 2*btnXsrt + btnXsize + 4*CheckBoxXsize - 20;
	int pnlChartYsrt = 5;
	int lblChartXsrt = 25;
	int lblChartYsrt = 25;
	int lblChartXsize = 22;
	int lblChartYsize = 15;
		
	JMenuBar menuBar = new JMenuBar();	
	JPanel MenuPnl = new JPanel();
	JLabel lblSelect = new JLabel("Graph Selection");
	JButton btnCloseAll = new JButton("Close All Graphs");
	JPanel pnlChartSize = new JPanel();
	JButton btnShowAll = new JButton("Show All Graphs");	
	JTextArea txtCrtXsize = new JTextArea();
	JTextArea txtCrtYsize = new JTextArea();
	
	private final JCheckBox SDPChkPChkBox = new JCheckBox("STD Pres. & Chk Pres.");
	private final JCheckBox PumpPMudVChkBox = new JCheckBox("Pump P. & Mud Vol.");
	private final JCheckBox KickVChkBox = new JCheckBox("Kick Volume");
	private final JCheckBox CasingShoeBHPChkBox = new JCheckBox("Casing Shoe Pres. & BHP");
	private final JCheckBox KickTopPMudPChkBox = new JCheckBox("Pres. at the Kick Top & Mud Line");
	private final JCheckBox CHKOpnChkBox = new JCheckBox("Chk Open Percent");
	private final JCheckBox VrtDepthKickChkBox = new JCheckBox("Vertical Depth of the Kick");
	private final JCheckBox KickDensChkBox = new JCheckBox("Kick Density at the Kick Top");
	private final JCheckBox KickInfluxChkBox = new JCheckBox("Kick Influx Rate");
	private final JCheckBox MudGasReturnChkBox = new JCheckBox("Mud & Gas Return Rate");
	private final JCheckBox HeightKickChkBox = new JCheckBox("Heigth of Kick");
	private final JCheckBox KillPmpChkBox = new JCheckBox("Standard Kill Pump Pres.");
	
	createChartFrame SDPChkPChtFrm;
	createChartFrame PumpPMudVChtFrm;
	createChartFrame KickVChtFrm;
	createChartFrame CasingShoeBHPChtFrm;
	createChartFrame KickTopPMudPChtFrm;
	createChartFrame CHKOpnChtFrm;
	createChartFrame VrtDepthKickChtFrm;
	createChartFrame KickDensChtFrm;
	createChartFrame KickInfluxChtFrm;
	createChartFrame MudGasReturnChtFrm;
	createChartFrame HeightKickChtFrm;
	KillSheetChartFrame KillPmpChtFrm;
	
	Timer chkBoxTimer;
	
	private final JLabel lblChartSizeContrl = new JLabel("Chart Size");
	private final JLabel lblChartSizeX = new JLabel("X :");
	private final JLabel lblChartSizeY = new JLabel("Y :");
	private final JButton btnRearrange = new JButton("Rearrange Graphs");
	JMenu mMenu = new JMenu("Menus");
	private final JMenuItem mntmHelp = new JMenuItem("Help");
	private final JMenuItem mntmReset = new JMenuItem("Reset Graphs");
	JMenuItem menuMainmenu = new JMenuItem("Back to the Main Menu");
	JMenuItem menuSimulation = new JMenuItem("Back to Simulation");
	
	resultPlot() {
		setTitle("Plot the Results vs. Time (minutes)");
		setIconImage(MainDriver.icon.getImage());
		
		setBounds(0, 0, ChartXsize*4, MenuPnlYsize+60);  	
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.white);	
		
		setJMenuBar(menuBar);			
		menuBar.add(mMenu);		
		mMenu.add(mntmHelp);
		mMenu.add(mntmReset);
		menuMainmenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.RPMenuSelected=0;
				MainDriver.MainMenuVisible=1;
				menuclose();
				menuSimulation.setEnabled(false);
			}
		});
		
		
		mMenu.add(menuMainmenu);		
		menuSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.RPMenuSelected=1;
				MainDriver.MainMenuVisible=0;
				menuclose();
				menuSimulation.setEnabled(false);
			}
		});
		mMenu.add(menuSimulation);
		
		MenuPnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		MenuPnl.setBackground(SystemColor.menu);		
		MenuPnl.setBounds(MenuPnlXsrt, MenuPnlYsrt, MenuPnlXsize-20, MenuPnlYsize);
		MenuPnl.setLayout(null);
		getContentPane().add(MenuPnl);
		
		SDPChkPChkBox.setBounds(CheckBoxXsrt, CheckBoxYsrt, CheckBoxXsize, CheckBoxYsize);		
		PumpPMudVChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize, CheckBoxYsrt, CheckBoxXsize, CheckBoxYsize);		
		KickVChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize*2, CheckBoxYsrt, CheckBoxXsize, CheckBoxYsize);		
		CasingShoeBHPChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize*3, CheckBoxYsrt, CheckBoxXsize-30, CheckBoxYsize);		
		KickTopPMudPChkBox.setBounds(CheckBoxXsrt, CheckBoxYsrt+CheckBoxYsize, CheckBoxXsize, CheckBoxYsize);		
		CHKOpnChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize, CheckBoxYsrt+CheckBoxYsize, CheckBoxXsize, CheckBoxYsize);		
		VrtDepthKickChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize*2, CheckBoxYsrt+CheckBoxYsize, CheckBoxXsize, CheckBoxYsize);		
		KickDensChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize*3, CheckBoxYsrt+CheckBoxYsize, CheckBoxXsize-30, CheckBoxYsize);			
		KickInfluxChkBox.setBounds(CheckBoxXsrt, CheckBoxYsrt+CheckBoxYsize*2, CheckBoxXsize, CheckBoxYsize);			
		MudGasReturnChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize, CheckBoxYsrt+CheckBoxYsize*2, CheckBoxXsize, CheckBoxYsize);		
		HeightKickChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize*2, CheckBoxYsrt+CheckBoxYsize*2, CheckBoxXsize, CheckBoxYsize);		
		KillPmpChkBox.setBounds(CheckBoxXsrt+CheckBoxXsize*3, CheckBoxYsrt+CheckBoxYsize*2, CheckBoxXsize-30, CheckBoxYsize);			
		
		lblSelect.setFont(new Font("±¼¸²", Font.BOLD, 14));
		lblSelect.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelect.setBounds(CheckBoxXsrt, 10, 135, 15);
		
		btnShowAll.setBounds(btnXsrt, btnYsrt, btnXsize, btnYsize);
		btnCloseAll.setBounds(btnXsrt, btnYsrt+btnYsize+10, btnXsize, btnYsize);
		
		pnlChartSize.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));		
		pnlChartSize.setBounds(pnlChartXsrt, pnlChartYsrt, pnlChartXsize, pnlChartYsize);		
		pnlChartSize.setLayout(null);		

		lblChartSizeContrl.setFont(new Font("±¼¸²", Font.BOLD, 14));
		lblChartSizeContrl.setHorizontalAlignment(SwingConstants.CENTER);
		lblChartSizeContrl.setBounds(pnlChartXsize/2-96/2, 2, 96, 15);		
		
		lblChartSizeX.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblChartSizeX.setBounds(lblChartXsrt, lblChartYsrt, lblChartXsize, lblChartYsize);		
		
		lblChartSizeY.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblChartSizeY.setBounds(lblChartXsrt, lblChartYsrt+lblChartYsize+10, lblChartXsize, lblChartYsize);			
		
		txtCrtXsize.setText("300");
		txtCrtXsize.setBounds(lblChartXsrt+lblChartXsize+20, lblChartYsrt, 58, lblChartYsize);				
		
		txtCrtYsize.setText("200");
		txtCrtYsize.setBounds(lblChartXsrt+lblChartXsize+20, lblChartYsrt+lblChartYsize+10, 58, lblChartYsize);			
		
		btnRearrange.setBounds(pnlChartXsize/2-138/2, lblChartYsrt+lblChartYsize*2+15, 138, 23);
		
		pnlChartSize.add(lblChartSizeContrl);
		pnlChartSize.add(lblChartSizeX);
		pnlChartSize.add(lblChartSizeY);	
		pnlChartSize.add(txtCrtXsize);
		pnlChartSize.add(txtCrtYsize);	
		pnlChartSize.add(btnRearrange);
		
		MenuPnl.add(lblSelect);		
		MenuPnl.add(SDPChkPChkBox);
		MenuPnl.add(PumpPMudVChkBox);		
		MenuPnl.add(KickVChkBox);		
		MenuPnl.add(CasingShoeBHPChkBox);
		MenuPnl.add(KickTopPMudPChkBox);
		MenuPnl.add(CHKOpnChkBox);
		MenuPnl.add(VrtDepthKickChkBox);
		MenuPnl.add(KickDensChkBox);
		MenuPnl.add(KickInfluxChkBox);
		MenuPnl.add(MudGasReturnChkBox);
		MenuPnl.add(HeightKickChkBox);
		MenuPnl.add(KillPmpChkBox);	
		MenuPnl.add(btnShowAll);
		MenuPnl.add(btnCloseAll);
		MenuPnl.add(pnlChartSize);
		
		plotGraph();			
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0){
				arg0.getWindow().dispose();
				arg0.getWindow().setVisible(false);				
				MainDriver.RPMenuSelected=1;
				MainDriver.MainMenuVisible=1;
				menuclose();
				}
			});	
		
		SDPChkPChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(SDPChkPChkBox.isSelected() == true) {
					SDPChkPChtFrm.openNum = 1;
					SDPChkPChtFrm.setVisible(true);
				}
				else{
					SDPChkPChtFrm.openNum = 0;
					SDPChkPChtFrm.setVisible(false);
				}
			}
		});
		
		PumpPMudVChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(PumpPMudVChkBox.isSelected() == true) {
					PumpPMudVChtFrm.openNum = 1;
					PumpPMudVChtFrm.setVisible(true);
				}
				else{
					PumpPMudVChtFrm.openNum = 0;
					PumpPMudVChtFrm.setVisible(false);
				}				
			}
		});
		
		KickVChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(KickVChkBox.isSelected() == true) {
					KickVChtFrm.openNum = 1;
					KickVChtFrm.setVisible(true);
				}
				else{
					KickVChtFrm.openNum = 0;
					KickVChtFrm.setVisible(false);
				}	
			}
		});
		
		CasingShoeBHPChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(CasingShoeBHPChkBox.isSelected() == true) {
					CasingShoeBHPChtFrm.openNum = 1;
					CasingShoeBHPChtFrm.setVisible(true);
				}
				else{
					CasingShoeBHPChtFrm.openNum = 0;
					CasingShoeBHPChtFrm.setVisible(false);
				}
			}
		});
				
		KickTopPMudPChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(KickTopPMudPChkBox.isSelected() == true) {
					KickTopPMudPChtFrm.openNum = 1;
					KickTopPMudPChtFrm.setVisible(true);
				}
				else{
					KickTopPMudPChtFrm.openNum = 0;
					KickTopPMudPChtFrm.setVisible(false);
				}
			}
		});
		
		CHKOpnChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(CHKOpnChkBox.isSelected() == true) {
					CHKOpnChtFrm.openNum = 1;
					CHKOpnChtFrm.setVisible(true);
				}
				else{
					CHKOpnChtFrm.openNum = 0;
					CHKOpnChtFrm.setVisible(false);
				}
			}
		});
		
		VrtDepthKickChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(VrtDepthKickChkBox.isSelected() == true) {
					VrtDepthKickChtFrm.openNum = 1;
					VrtDepthKickChtFrm.setVisible(true);
				}
				else{
					VrtDepthKickChtFrm.openNum = 0;
					VrtDepthKickChtFrm.setVisible(false);
				}
			}
		});		
		
		KickDensChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(KickDensChkBox.isSelected() == true) {
					KickDensChtFrm.openNum = 1;
					KickDensChtFrm.setVisible(true);
				}
				else{
					KickDensChtFrm.openNum = 0;
					KickDensChtFrm.setVisible(false);
				}
			}
		});
		
		KickInfluxChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(KickInfluxChkBox.isSelected() == true) {
					KickInfluxChtFrm.openNum = 1;
					KickInfluxChtFrm.setVisible(true);
				}
				else{
					KickInfluxChtFrm.openNum = 0;
					KickInfluxChtFrm.setVisible(false);
				}
			}
		});
		
		MudGasReturnChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MudGasReturnChkBox.isSelected() == true) {
					MudGasReturnChtFrm.openNum = 1;
					MudGasReturnChtFrm.setVisible(true);
				}
				else{
					MudGasReturnChtFrm.openNum = 0;
					MudGasReturnChtFrm.setVisible(false);
				}
			}
		});
		
		HeightKickChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(HeightKickChkBox.isSelected() == true) {
					HeightKickChtFrm.openNum = 1;
					HeightKickChtFrm.setVisible(true);
				}
				else{
					HeightKickChtFrm.openNum = 0;
					HeightKickChtFrm.setVisible(false);
				}
			}
		});
		
		KillPmpChkBox.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if(KillPmpChkBox.isSelected() == true) {
					KillPmpChtFrm.openNum = 1;
					KillPmpChtFrm.setVisible(true);
				}
				else{
					KillPmpChtFrm.openNum = 0;
					KillPmpChtFrm.setVisible(false);
				}
			}
		});
		
		btnShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChartPnlXsize = ChartXsize*280/300;
					ChartPnlYsize = ChartYsize;
					
					SDPChkPChtFrm.setBounds(0,MenuPnlYsize+60,ChartXsize,ChartYsize);
					PumpPMudVChtFrm.setBounds(ChartXsize,MenuPnlYsize+60,ChartXsize,ChartYsize);
					KickVChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60,ChartXsize,ChartYsize);
					CasingShoeBHPChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60,ChartXsize,ChartYsize);
					KickTopPMudPChtFrm.setBounds(0,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					CHKOpnChtFrm.setBounds(ChartXsize,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					VrtDepthKickChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					KickDensChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					KickInfluxChtFrm.setBounds(0,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					MudGasReturnChtFrm.setBounds(ChartXsize,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					HeightKickChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					KillPmpChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					
					if(MainDriver.iDone>0){
						SDPChkPChtFrm.cp0.resizePnl();
						PumpPMudVChtFrm.cp0.resizePnl();
						KickVChtFrm.cp0.resizePnl();
						CasingShoeBHPChtFrm.cp0.resizePnl();
						KickTopPMudPChtFrm.cp0.resizePnl();
						CHKOpnChtFrm.cp0.resizePnl();
						VrtDepthKickChtFrm.cp0.resizePnl();
						KickDensChtFrm.cp0.resizePnl();
						KickInfluxChtFrm.cp0.resizePnl();
						MudGasReturnChtFrm.cp0.resizePnl();
						HeightKickChtFrm.cp0.resizePnl();
						KillPmpChtFrm.cp0.resizeKillPnl();
					}	

				SDPChkPChtFrm.setVisible(true);
				PumpPMudVChtFrm.setVisible(true);
				KickVChtFrm.setVisible(true);
				CasingShoeBHPChtFrm.setVisible(true);
				KickTopPMudPChtFrm.setVisible(true);
				CHKOpnChtFrm.setVisible(true);
				VrtDepthKickChtFrm.setVisible(true);
				KickDensChtFrm.setVisible(true);
				KickInfluxChtFrm.setVisible(true);
				MudGasReturnChtFrm.setVisible(true);
				HeightKickChtFrm.setVisible(true);
				KillPmpChtFrm.setVisible(true);
				
				SDPChkPChtFrm.openNum = 1; //open 1, close 0
				PumpPMudVChtFrm.openNum = 1;
				KickVChtFrm.openNum = 1;
				CasingShoeBHPChtFrm.openNum = 1;
				KickTopPMudPChtFrm.openNum = 1;
				CHKOpnChtFrm.openNum = 1;
				VrtDepthKickChtFrm.openNum = 1;
				KickDensChtFrm.openNum = 1;
				KickInfluxChtFrm.openNum = 1;
				MudGasReturnChtFrm.openNum = 1;
				HeightKickChtFrm.openNum = 1;
				KillPmpChtFrm.openNum = 1;
				
				SDPChkPChkBox.setSelected(true);
				PumpPMudVChkBox.setSelected(true);
				KickVChkBox.setSelected(true);
				CasingShoeBHPChkBox.setSelected(true);
				KickTopPMudPChkBox.setSelected(true);
				CHKOpnChkBox.setSelected(true);
				VrtDepthKickChkBox.setSelected(true);
				KickDensChkBox.setSelected(true);
				KickInfluxChkBox.setSelected(true);
				MudGasReturnChkBox.setSelected(true);
				HeightKickChkBox.setSelected(true);
				KillPmpChkBox.setSelected(true);
			}
		});
		
		btnCloseAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SDPChkPChtFrm.setVisible(false);
				PumpPMudVChtFrm.setVisible(false);
				KickVChtFrm.setVisible(false);
				CasingShoeBHPChtFrm.setVisible(false);
				KickTopPMudPChtFrm.setVisible(false);
				CHKOpnChtFrm.setVisible(false);
				VrtDepthKickChtFrm.setVisible(false);
				KickDensChtFrm.setVisible(false);
				KickInfluxChtFrm.setVisible(false);
				MudGasReturnChtFrm.setVisible(false);
				HeightKickChtFrm.setVisible(false);
				KillPmpChtFrm.setVisible(false);
				
				SDPChkPChtFrm.openNum = 0; //open 1, close 0
				PumpPMudVChtFrm.openNum = 0;
				KickVChtFrm.openNum = 0;
				CasingShoeBHPChtFrm.openNum = 0;
				KickTopPMudPChtFrm.openNum = 0;
				CHKOpnChtFrm.openNum = 0;
				VrtDepthKickChtFrm.openNum = 0;
				KickDensChtFrm.openNum = 0;
				KickInfluxChtFrm.openNum = 0;
				MudGasReturnChtFrm.openNum = 0;
				HeightKickChtFrm.openNum = 0;
				KillPmpChtFrm.openNum = 0;
				
				SDPChkPChkBox.setSelected(false);
				PumpPMudVChkBox.setSelected(false);
				KickVChkBox.setSelected(false);
				CasingShoeBHPChkBox.setSelected(false);
				KickTopPMudPChkBox.setSelected(false);
				CHKOpnChkBox.setSelected(false);
				VrtDepthKickChkBox.setSelected(false);
				KickDensChkBox.setSelected(false);
				KickInfluxChkBox.setSelected(false);
				MudGasReturnChkBox.setSelected(false);
				HeightKickChkBox.setSelected(false);
				KillPmpChkBox.setSelected(false);
			}
		});
		
		btnRearrange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					ChartXsize = Integer.parseInt(txtCrtXsize.getText());
					ChartYsize = Integer.parseInt(txtCrtYsize.getText());
					ChartPnlXsize = ChartXsize*280/300;
					ChartPnlYsize = ChartYsize;
					
					SDPChkPChtFrm.setBounds(0,MenuPnlYsize+60,ChartXsize,ChartYsize);
					PumpPMudVChtFrm.setBounds(ChartXsize,MenuPnlYsize+60,ChartXsize,ChartYsize);
					KickVChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60,ChartXsize,ChartYsize);
					CasingShoeBHPChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60,ChartXsize,ChartYsize);
					KickTopPMudPChtFrm.setBounds(0,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					CHKOpnChtFrm.setBounds(ChartXsize,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					VrtDepthKickChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					KickDensChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
					KickInfluxChtFrm.setBounds(0,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					MudGasReturnChtFrm.setBounds(ChartXsize,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					HeightKickChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					KillPmpChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
					
					if(MainDriver.iDone>0){
						SDPChkPChtFrm.cp0.resizePnl();
						PumpPMudVChtFrm.cp0.resizePnl();
						KickVChtFrm.cp0.resizePnl();
						CasingShoeBHPChtFrm.cp0.resizePnl();
						KickTopPMudPChtFrm.cp0.resizePnl();
						CHKOpnChtFrm.cp0.resizePnl();
						VrtDepthKickChtFrm.cp0.resizePnl();
						KickDensChtFrm.cp0.resizePnl();
						KickInfluxChtFrm.cp0.resizePnl();
						MudGasReturnChtFrm.cp0.resizePnl();
						HeightKickChtFrm.cp0.resizePnl();
						KillPmpChtFrm.cp0.resizeKillPnl();
					}					
					
					SDPChkPChtFrm.setVisible(true);
					PumpPMudVChtFrm.setVisible(true);
					KickVChtFrm.setVisible(true);
					CasingShoeBHPChtFrm.setVisible(true);
					KickTopPMudPChtFrm.setVisible(true);
					CHKOpnChtFrm.setVisible(true);
					VrtDepthKickChtFrm.setVisible(true);
					KickDensChtFrm.setVisible(true);
					KickInfluxChtFrm.setVisible(true);
					MudGasReturnChtFrm.setVisible(true);
					HeightKickChtFrm.setVisible(true);
					KillPmpChtFrm.setVisible(true);
					
					SDPChkPChtFrm.openNum = 1; //open 1, close 0
					PumpPMudVChtFrm.openNum = 1;
					KickVChtFrm.openNum = 1;
					CasingShoeBHPChtFrm.openNum = 1;
					KickTopPMudPChtFrm.openNum = 1;
					CHKOpnChtFrm.openNum = 1;
					VrtDepthKickChtFrm.openNum = 1;
					KickDensChtFrm.openNum = 1;
					KickInfluxChtFrm.openNum = 1;
					MudGasReturnChtFrm.openNum = 1;
					HeightKickChtFrm.openNum = 1;
					KillPmpChtFrm.openNum = 1;
					
					SDPChkPChkBox.setSelected(true);
					PumpPMudVChkBox.setSelected(true);
					KickVChkBox.setSelected(true);
					CasingShoeBHPChkBox.setSelected(true);
					KickTopPMudPChkBox.setSelected(true);
					CHKOpnChkBox.setSelected(true);
					VrtDepthKickChkBox.setSelected(true);
					KickDensChkBox.setSelected(true);
					KickInfluxChkBox.setSelected(true);
					MudGasReturnChkBox.setSelected(true);
					HeightKickChkBox.setSelected(true);
					KillPmpChkBox.setSelected(true);					
					
				}catch (NumberFormatException err){
					String WarnContent="Please Write Numbers Correctly.";
					JOptionPane.showMessageDialog(null, WarnContent, "Warning", JOptionPane.INFORMATION_MESSAGE);	
					txtCrtXsize.setText(Integer.toString(ChartXsize));
					txtCrtYsize.setText(Integer.toString(ChartYsize));
					}
				}
		});	
		
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String helpContent = "  You can change the chart size by providing the x, y size in Chart Size Panel and clicking the Rearrange Graphs button."+"\n"+
						             "  If you want to renew the graphs, click the Reset Graphs button under Menus menu.";
				JOptionPane.showMessageDialog(null, helpContent, "Help", JOptionPane.INFORMATION_MESSAGE);	
			}
		});		
		
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SDPChkPChtFrm.dispose();
				PumpPMudVChtFrm.dispose();
				KickVChtFrm.dispose();
				CasingShoeBHPChtFrm.dispose();
				KickTopPMudPChtFrm.dispose();
				CHKOpnChtFrm.dispose();
				VrtDepthKickChtFrm.dispose();
				KickDensChtFrm.dispose();
				KickInfluxChtFrm.dispose();
				MudGasReturnChtFrm.dispose();
				HeightKickChtFrm.dispose();
				KillPmpChtFrm.dispose();
				
				SDPChkPChtFrm.setVisible(false);
				PumpPMudVChtFrm.setVisible(false);
				KickVChtFrm.setVisible(false);
				CasingShoeBHPChtFrm.setVisible(false);
				KickTopPMudPChtFrm.setVisible(false);
				CHKOpnChtFrm.setVisible(false);
				VrtDepthKickChtFrm.setVisible(false);
				KickDensChtFrm.setVisible(false);
				KickInfluxChtFrm.setVisible(false);
				MudGasReturnChtFrm.setVisible(false);
				HeightKickChtFrm.setVisible(false);
				KillPmpChtFrm.setVisible(false);	
				
				plotGraph();
			}
		});		
	}
	
	void plotGraph(){
		if(MainDriver.Np==0){
			SDPChkPChtFrm = new createChartFrame();
			PumpPMudVChtFrm = new createChartFrame();
			KickVChtFrm = new createChartFrame();
			CasingShoeBHPChtFrm = new createChartFrame();
			KickTopPMudPChtFrm = new createChartFrame();
			CHKOpnChtFrm = new createChartFrame();
			VrtDepthKickChtFrm = new createChartFrame();
			KickDensChtFrm = new createChartFrame();
			KickInfluxChtFrm = new createChartFrame();
			MudGasReturnChtFrm = new createChartFrame();
			HeightKickChtFrm = new createChartFrame();
			KillPmpChtFrm = new KillSheetChartFrame();
		}
		
		else{
			double tempPspg[] = new double[MainDriver.Np];
			double tempPchkg[] = new double[MainDriver.Np];
			double tempPpump[] = new double[MainDriver.Np];
			double tempPb2p[] = new double[MainDriver.Np];
			double tempPcsg[] = new double[MainDriver.Np];
			double tempPxTop[] = new double[MainDriver.Np];
			double tempPmLine[] = new double[MainDriver.Np];
			double kxHeit[] = new double[MainDriver.Np];
			
			for(int i=0; i<MainDriver.Np; i++){
				tempPspg[i] = MainDriver.Psp[i]-14.7;
				tempPchkg[i] = MainDriver.Pchk[i]-14.7;
				tempPpump[i] = MainDriver.Ppump[i]-14.7;
				tempPb2p[i] = MainDriver.Pb2p[i]-14.7;
				tempPcsg[i] = MainDriver.Pcsg[i]-14.7;
				tempPxTop[i] = MainDriver.pxTop[i]-14.7;
				tempPmLine[i] = MainDriver.PmLine[i]-14.7;
				kxHeit[i] = MainDriver.xBot[i]-MainDriver.xTop[i];
				if(kxHeit[i]<0.001) kxHeit[i]=0;
			}
			
			SDPChkPChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Time (Min)", "P.(psig)", "Standpipe and Choke Pressure (psig)", MainDriver.TTsec, tempPspg, tempPchkg, "Standpipe", "Choke");
			PumpPMudVChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Time (Min)", "P.(psig) or V(bbls)", "Pump Pressure (psig) and Mud Volume Pumped (bbls)", MainDriver.TTsec, tempPpump, MainDriver.VOLcir, "Pump Pressure", "Volume Pumped");
			KickVChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Kick Volume in the Well (bbls)", "Time (Min)", "V (bbls)", MainDriver.TTsec, MainDriver.Vpit, "Kick Volume");
			CasingShoeBHPChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Time (Min)", "P. (psig)", "Casing Shoe Pressure and BHP (psig)", MainDriver.TTsec, tempPb2p, tempPcsg, "BHP", "Casing Shoe");
			KickTopPMudPChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Time (Min)", "P. (psig)", "Pressures at the Kick Top and Mud Line (psig)", MainDriver.TTsec, tempPxTop, tempPmLine, "Kick Pressure", "P. @ Mudline");
			CHKOpnChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Choke Open Percent by Area", "Time (Min)", "Percent (%)", MainDriver.TTsec, MainDriver.CHKopen, "Open Percent");
			VrtDepthKickChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Time (Min)", "Depth (ft)", "Vertical Depth of the Kick in the Well (ft)", MainDriver.TTsec, MainDriver.xBot, MainDriver.xTop, "Bottom", "Top");
			KickDensChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Kick Density at Kick Top in the Well (ppg)", "Time (Min)", "Density (ppg)", MainDriver.TTsec, MainDriver.rhoK, "Kick Density");
			KickInfluxChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Kick Influx Rate (Mscf/Day)", "Time (Min)", "Rate (Mscf/Day)", MainDriver.TTsec, MainDriver.QmcfDay, "Kick Influx Rate");
			MudGasReturnChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Time (Min)", "Rate (Mscf/Day or gpm)", "Mud Return Rate (gpm) and Gas Return Rate (Mscf/Day)", MainDriver.TTsec, MainDriver.mudflow, MainDriver.gasflow, "Mud Return", "Gas Return");
			HeightKickChtFrm = new createChartFrame(ChartXsize, ChartYsize, "Height of Kick in the Well (ft)", "Time (Min)", "Height (ft)", MainDriver.TTsec, kxHeit, "Height of Kick in the well");
			KillPmpChtFrm = new KillSheetChartFrame(ChartXsize, ChartYsize);
		}		
		
		SDPChkPChtFrm.setBounds(0,MenuPnlYsize+60,ChartXsize,ChartYsize);
		PumpPMudVChtFrm.setBounds(ChartXsize,MenuPnlYsize+60,ChartXsize,ChartYsize);
		KickVChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60,ChartXsize,ChartYsize);
		CasingShoeBHPChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60,ChartXsize,ChartYsize);
		KickTopPMudPChtFrm.setBounds(0,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
		CHKOpnChtFrm.setBounds(ChartXsize,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
		VrtDepthKickChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
		KickDensChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60+ChartYsize,ChartXsize,ChartYsize);
		KickInfluxChtFrm.setBounds(0,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
		MudGasReturnChtFrm.setBounds(ChartXsize,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
		HeightKickChtFrm.setBounds(ChartXsize*2,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
		KillPmpChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
		
		SDPChkPChtFrm.setVisible(true);
		PumpPMudVChtFrm.setVisible(true);
		KickVChtFrm.setVisible(true);
		CasingShoeBHPChtFrm.setVisible(true);
		KickTopPMudPChtFrm.setVisible(true);
		CHKOpnChtFrm.setVisible(true);
		VrtDepthKickChtFrm.setVisible(true);
		KickDensChtFrm.setVisible(true);
		KickInfluxChtFrm.setVisible(true);
		MudGasReturnChtFrm.setVisible(true);
		HeightKickChtFrm.setVisible(true);
		KillPmpChtFrm.setVisible(true);		
		
		SDPChkPChtFrm.openNum = 1; //open 1, close 0
		PumpPMudVChtFrm.openNum = 1;
		KickVChtFrm.openNum = 1;
		CasingShoeBHPChtFrm.openNum = 1;
		KickTopPMudPChtFrm.openNum = 1;
		CHKOpnChtFrm.openNum = 1;
		VrtDepthKickChtFrm.openNum = 1;
		KickDensChtFrm.openNum = 1;
		KickInfluxChtFrm.openNum = 1;
		MudGasReturnChtFrm.openNum = 1;
		HeightKickChtFrm.openNum = 1;
		KillPmpChtFrm.openNum = 1;
		
		SDPChkPChkBox.setSelected(true);
		PumpPMudVChkBox.setSelected(true);
		KickVChkBox.setSelected(true);
		CasingShoeBHPChkBox.setSelected(true);
		KickTopPMudPChkBox.setSelected(true);
		CHKOpnChkBox.setSelected(true);
		VrtDepthKickChkBox.setSelected(true);
		KickDensChkBox.setSelected(true);
		KickInfluxChkBox.setSelected(true);
		MudGasReturnChkBox.setSelected(true);
		HeightKickChkBox.setSelected(true);
		KillPmpChkBox.setSelected(true);	
		KillPmpChtFrm.setBounds(ChartXsize*3,MenuPnlYsize+60+ChartYsize*2,ChartXsize,ChartYsize);
		KillPmpChtFrm.setVisible(true);		
		KillPmpChtFrm.openNum = 1;
		KillPmpChkBox.setSelected(true);
	}
	
	class ChkBoxTimerTask extends TimerTask{
		public void run(){
			if(SDPChkPChtFrm.openNum == 0) SDPChkPChkBox.setSelected(false);
			else SDPChkPChkBox.setSelected(true);
			if(PumpPMudVChtFrm.openNum == 0) PumpPMudVChkBox.setSelected(false);
			else PumpPMudVChkBox.setSelected(true);
			if(KickVChtFrm.openNum == 0) KickVChkBox.setSelected(false);
			else KickVChkBox.setSelected(true);
			if(CasingShoeBHPChtFrm.openNum == 0) CasingShoeBHPChkBox.setSelected(false);
			else CasingShoeBHPChkBox.setSelected(true);
			if(KickTopPMudPChtFrm.openNum == 0) KickTopPMudPChkBox.setSelected(false);
			else KickTopPMudPChkBox.setSelected(true);
			if(CHKOpnChtFrm.openNum == 0) CHKOpnChkBox.setSelected(false);
			else CHKOpnChkBox.setSelected(true); 
			if(VrtDepthKickChtFrm.openNum == 0) VrtDepthKickChkBox.setSelected(false);
			else VrtDepthKickChkBox.setSelected(true);
			if(KickDensChtFrm.openNum == 0) KickDensChkBox.setSelected(false);
			else KickDensChkBox.setSelected(true);
			if(KickInfluxChtFrm.openNum == 0) KickInfluxChkBox.setSelected(false);
			else KickInfluxChkBox.setSelected(true);
			if(MudGasReturnChtFrm.openNum == 0) MudGasReturnChkBox.setSelected(false);
			else MudGasReturnChkBox.setSelected(true);
			if(HeightKickChtFrm.openNum == 0) HeightKickChkBox.setSelected(false);
			else HeightKickChkBox.setSelected(true);
			if(KillPmpChtFrm.openNum == 0) KillPmpChkBox.setSelected(false);
			else KillPmpChkBox.setSelected(true);
			
			cancel();
		}
	}
	
	class createChartFrame extends JFrame{
		int openNum = 1;
		int chartFrameXsize = 300;
		int chartFrameYsize = 300;
		createChartPanel cp0;		
		
		createChartFrame(){
			setTitle("");
			setIconImage(MainDriver.icon.getImage());
			setLayout(null);
		}
		
		createChartFrame(int Xsize, int Ysize, String CT, String XaxT, String YaxT, double[] Xdata, double[] Ydata, String DT){
			setTitle(CT);
			setIconImage(MainDriver.icon.getImage());
			
			this.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent arg0) {
					chartFrameXsize = getWidth();
					chartFrameYsize = getHeight();
					ChartPnlXsize = chartFrameXsize*280/300;
					ChartPnlYsize = chartFrameYsize;
					cp0.setBounds(0, 0, ChartPnlXsize, ChartPnlYsize);
					cp0.resizePnl();
				}			
			});
			
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent arg0){					
					setVisible(false);
					openNum=0;
					chkBoxTimer = new Timer();
					chkBoxTimer.schedule(new ChkBoxTimerTask(), 0, 10);
					}
				});
			
			setLayout(null);
			chartFrameXsize = Xsize;
			chartFrameYsize = Ysize;		
			cp0 = new createChartPanel(Xdata, Ydata, CT, DT, XaxT, YaxT);			
			cp0.setBounds(0, 0, ChartPnlXsize, ChartPnlYsize);
			add(cp0);			
		}
		
		createChartFrame(int Xsize, int Ysize, String XaxT, String YaxT, String CT, double[] Xdata, double[] Ydata, double[] Ydata2, String DT, String DT2){
			setTitle(CT);
			setIconImage(MainDriver.icon.getImage());
			this.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent arg0) {
					chartFrameXsize = getWidth();
					chartFrameYsize = getHeight();
					ChartPnlXsize = chartFrameXsize*280/300;
					ChartPnlYsize = chartFrameYsize;
					cp0.setBounds(0, 0, ChartPnlXsize, ChartPnlYsize);
					cp0.resizePnl();
				}			
			});
			
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent arg0){					
					setVisible(false);
					openNum=0;
					chkBoxTimer = new Timer();
					chkBoxTimer.schedule(new ChkBoxTimerTask(), 0, 10);
					}
				});
			
			setLayout(null);
			chartFrameXsize = Xsize;
			chartFrameYsize = Ysize;		
			cp0 = new createChartPanel(XaxT, YaxT, CT, Xdata, Ydata, Ydata2, DT, DT2);			
			cp0.setBounds(0, 0, ChartPnlXsize, ChartPnlYsize);
			add(cp0);			
		}		
	}
	
	class createChartPanel extends JPanel{		
		DefaultXYDataset dataset =  new DefaultXYDataset();
		DefaultXYDataset dataset2 =  new DefaultXYDataset();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
		NumberAxis xAxis;
    	NumberAxis yAxis;
    	NumberAxis yAxis2;
    	
		String ChartTitle="";
		double[][] dataInput;
		double[][] dataInput2;
		String dataTitle = "";
		String dataTitle2 = "";
		String xAxisTitle = "x";
		String yAxisTitle = "y";
		ChartPanel cp;
		
		createChartPanel(){			
			setLayout(null);
		}
		
		createChartPanel(double[] Xdata, double[] Ydata, String CT, String DT, String XaxT, String YaxT){
			setLayout(null);
			ChartTitle = CT;
			dataTitle = DT;
			xAxisTitle = XaxT;
			yAxisTitle = YaxT;
			dataInput = new double[2][MainDriver.Np];
			
			for(int j=0; j<MainDriver.Np-1; j++){
				dataInput[0][j] = Xdata[j]/60; // Invert sec to min 20131112 ajw
				dataInput[1][j] = Ydata[j];
				if(dataInput[1][j]==0) dataInput[1][j]=0.001;
			}
			
			if(dataInput[0][MainDriver.Np-1]==0 && dataInput[1][MainDriver.Np-1]==0 && MainDriver.Np>2){
				dataInput[0][MainDriver.Np-1] = dataInput[0][MainDriver.Np-2]; // Invert sec to min 20131112 ajw
				dataInput[1][MainDriver.Np-1] = dataInput[1][MainDriver.Np-2];
			}
			
			renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
			dataset.addSeries(dataTitle, dataInput);
			NumberAxis xAxis = new NumberAxis(xAxisTitle);
	    	NumberAxis yAxis = new NumberAxis(yAxisTitle);
	    	XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
	    	plot.setOutlineVisible(false);
	    	
	    	JFreeChart chart = new JFreeChart(ChartTitle, plot);
	    	chart.removeLegend();
	    	
	    	cp = new ChartPanel(chart);
	    	cp.setBounds(0,0,ChartPnlXsize-10, ChartPnlYsize-40);
	    	add(cp);
		}
		
		createChartPanel(String XaxT, String YaxT, String CT, double[] Xdata, double[] Ydata, double[] Ydata2, String DT, String DT2){
			setLayout(null);
			ChartTitle = CT;
			dataTitle = DT;
			dataTitle2 = DT2;
			xAxisTitle = XaxT;
			yAxisTitle = YaxT;
			dataInput = new double[2][MainDriver.Np];
			dataInput2 = new double[2][MainDriver.Np];
						
			for(int j=0; j<MainDriver.Np-1; j++){
				dataInput[0][j] = Xdata[j]/60; // Invert sec to min 20131112 ajw
				dataInput[1][j] = Ydata[j];
				if(dataInput[1][j]==0) dataInput[1][j]=0.001;
				}	
			
			
			for(int j=1; j<MainDriver.Np-1; j++){
				dataInput2[0][j] = Xdata[j]/60;
				dataInput2[1][j] = Ydata2[j];
				if(dataInput2[1][j]==0) dataInput2[1][j]=0.001;
				}
			
			if(dataInput[0][MainDriver.Np-1]==0 && dataInput[1][MainDriver.Np-1]==0  && MainDriver.Np>2){
				dataInput[0][MainDriver.Np-1] = dataInput[0][MainDriver.Np-2]; // Invert sec to min 20131112 ajw
				dataInput[1][MainDriver.Np-1] = dataInput[1][MainDriver.Np-2];
				}
			if(dataInput2[0][MainDriver.Np-1]==0 && dataInput2[1][MainDriver.Np-1]==0 && MainDriver.Np>2){
				dataInput2[0][MainDriver.Np-1] = dataInput2[0][MainDriver.Np-2]; // Invert sec to min 20131112 ajw
				dataInput2[1][MainDriver.Np-1] = dataInput2[1][MainDriver.Np-2];
				}
			
			
			dataset.addSeries(dataTitle, dataInput);
			dataset2.addSeries(dataTitle2, dataInput2);
			
			NumberAxis xAxis = new NumberAxis(xAxisTitle);
	    	NumberAxis yAxis = new NumberAxis(yAxisTitle);
	    	NumberAxis yAxis2 = new NumberAxis(yAxisTitle);
	    	renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	renderer2.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	renderer.setSeriesPaint(0, Color.blue);
	    	renderer2.setSeriesPaint(0, Color.red);
	    	
	    	XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
	    	////plot.setRangeAxis(1, yAxis);
	    	plot.setDataset(1, dataset2);
	    	plot.setRenderer(1, renderer2);
	    	plot.mapDatasetToRangeAxis(2, 1);
	    	plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
	    	plot.setOutlineVisible(false);
	    	
	    	JFreeChart chart = new JFreeChart(ChartTitle, plot);	    	
	    	cp = new ChartPanel(chart);
	    	cp.setBounds(0,0, ChartPnlXsize-10, ChartPnlYsize-40);
	    	add(cp);
		}
		
		void resizePnl(){
			cp.setBounds(0,0, ChartPnlXsize-10, ChartPnlYsize-40);
		}
	}
	
	void menuclose(){
		SDPChkPChtFrm.dispose();
		PumpPMudVChtFrm.dispose();
		KickVChtFrm.dispose();
		CasingShoeBHPChtFrm.dispose();
		KickTopPMudPChtFrm.dispose();
		CHKOpnChtFrm.dispose();
		VrtDepthKickChtFrm.dispose();
		KickDensChtFrm.dispose();
		KickInfluxChtFrm.dispose();
		MudGasReturnChtFrm.dispose();
		HeightKickChtFrm.dispose();
		KillPmpChtFrm.dispose();
		
		SDPChkPChtFrm.setVisible(false);
		PumpPMudVChtFrm.setVisible(false);
		KickVChtFrm.setVisible(false);
		CasingShoeBHPChtFrm.setVisible(false);
		KickTopPMudPChtFrm.setVisible(false);
		CHKOpnChtFrm.setVisible(false);
		VrtDepthKickChtFrm.setVisible(false);
		KickDensChtFrm.setVisible(false);
		KickInfluxChtFrm.setVisible(false);
		MudGasReturnChtFrm.setVisible(false);
		HeightKickChtFrm.setVisible(false);
		KillPmpChtFrm.setVisible(false);
		
		SDPChkPChtFrm.openNum = 1; //open 1, close 0
		PumpPMudVChtFrm.openNum = 1;
		KickVChtFrm.openNum = 1;
		CasingShoeBHPChtFrm.openNum = 1;
		KickTopPMudPChtFrm.openNum = 1;
		CHKOpnChtFrm.openNum = 1;
		VrtDepthKickChtFrm.openNum = 1;
		KickDensChtFrm.openNum = 1;
		KickInfluxChtFrm.openNum = 1;
		MudGasReturnChtFrm.openNum = 1;
		HeightKickChtFrm.openNum = 1;
		KillPmpChtFrm.openNum = 1;
		
		SDPChkPChkBox.setSelected(false);
		PumpPMudVChkBox.setSelected(false);
		KickVChkBox.setSelected(false);
		CasingShoeBHPChkBox.setSelected(false);
		KickTopPMudPChkBox.setSelected(false);
		CHKOpnChkBox.setSelected(false);
		VrtDepthKickChkBox.setSelected(false);
		KickDensChkBox.setSelected(false);
		KickInfluxChkBox.setSelected(false);
		MudGasReturnChkBox.setSelected(false);
		HeightKickChkBox.setSelected(false);
		KillPmpChkBox.setSelected(false);
		
		MainDriver.plot=0;
		this.dispose();
		this.setVisible(false);
	}
	
	class KillSheetChartFrame extends JFrame{
		int openNum = 1;
		int chartFrameXsize = 300;
		int chartFrameYsize = 300;
		int nData=0;
		int npData=0;
		double gmc2=0;
		double cmud2=0;
		double volks=0;
		double[] getDPInsideEX= new double[2];
		double psp3=0, pp3=0, pmin=0, fCP=0;
		double iCP=0;
		double[] ppks = new double[1000];
		double[] stks = new double[1000];
		double[] xData;
		double[] xData2;
		double[] yData;
		double[] yData2;
		createKillSheetPanel cp0;
		int dummy=0;
		int checkChangeNum=1;
		
		KillSheetChartFrame(){		
			setTitle("");
			setIconImage(MainDriver.icon.getImage());
			setLayout(null);			
		}
		
		KillSheetChartFrame(int Xsize, int Ysize){		
			setTitle("Standard Kill Pump Pressure (psig)");
			setIconImage(MainDriver.icon.getImage());
			
			this.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent arg0) {
					chartFrameXsize = getWidth();
					chartFrameYsize = getHeight();
					ChartPnlXsize = chartFrameXsize*280/300;
					ChartPnlYsize = chartFrameYsize;
					cp0.setBounds(0, 0, ChartPnlXsize, ChartPnlYsize);
					cp0.resizeKillPnl();
				}			
			});
			
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent arg0){					
					setVisible(false);
					openNum=0;
					chkBoxTimer = new Timer();
					chkBoxTimer.schedule(new ChkBoxTimerTask(), 0, 10);
					}
				});
			
			setLayout(null);
			nData = MainDriver.NwcE - MainDriver.NwcS + 5 + MainDriver.NumQcapeq;				
			chartFrameXsize = Xsize;
			chartFrameYsize = Ysize;		
			if(MainDriver.iKsheet == 0) MainModule.SetControl();
			MainDriver.iKsheet = 1;
			gmc2 = MainDriver.gMudCirc;
			cmud2 = MainDriver.Cmud;
			//MainDriver.gMudCirc = MainDriver.gMudKill;
			//MainDriver.Cmud = MainDriver.Kmud;     // just plot the kill sheet
			MainDriver.Cmud = MainDriver.oMud+MainDriver.KICKintens;     // just plot the kill sheet
			MainDriver.gMudCirc = 0.052*(MainDriver.oMud+MainDriver.KICKintens);
			
			checkChangeNum=1;
			
			volks = 0;
			//getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[0], volks);//, psp3, pp3)
			getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);//, psp3, pp3)
			psp3 = getDPInsideEX[0];
			pp3 = getDPInsideEX[1];
			iCP = pp3;
			ppks[MainDriver.NwcE + 3 + 2*MainDriver.NumQcapeq] = pp3; 
		    stks[MainDriver.NwcE + 3 + 2*MainDriver.NumQcapeq] = 0;
		    
		    for(int i = (MainDriver.NwcE + 2); i>MainDriver.NwcS; i--){
		    	volks = volks + MainDriver.volDS[i-1];
		    	getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
			    psp3 = getDPInsideEX[0];
			    pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
		        ppks[i] = pp3;
		        stks[i] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2));        
		        }
		    		    
		     pmin = pp3;
			 volks = volks + 0.1;  // just pass the BIT
			 getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
			 psp3 = getDPInsideEX[0];
			 pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
			 ppks[MainDriver.NwcS] = pp3; 
			 stks[MainDriver.NwcS] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2)); 
			 fCP = pp3;
			 volks = volks + 20.5;   //20.5 bbls more for plotting purpose !
			 ppks[MainDriver.NwcS-1] = pp3;
			 stks[MainDriver.NwcS-1] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2)); 
			 MainDriver.gMudCirc = gmc2;
			 MainDriver.Cmud = cmud2;
			 
			 //-------------------------------- specify the graph//s properties !
			if(MainDriver.iDone == 0){
				 xData = new double[nData];
				 yData = new double[nData];
				 int rowNumber = 0;
				 for(int i = (MainDriver.NwcS - 1); i < (MainDriver.NwcE + 4); i++){
					 rowNumber = i - MainDriver.NwcS + 1;
					 xData[rowNumber] = stks[i];
					 yData[rowNumber] = ppks[i] - 14.7;
					 }
				 //----- plot the pump pressure on the top of kill sheet after manual choke control !
				 cp0 = new createKillSheetPanel(xData, yData, "Standard Kill Pump Pressure (psig)", "Theoretical", "Stroke", "P. (psig)");
				 cp0.setBounds(0, 0, ChartPnlXsize, ChartPnlYsize);
				 add(cp0);			 
				 }
			 else{
				 npData = Math.max(nData, MainDriver.iDone);
				 xData = new double[npData];
				 yData = new double[npData];
				 xData2 = new double[npData];
				 yData2 = new double[npData];
				 int icount3 = -1;
				 for(int i = (MainDriver.NwcE + 3); i>(MainDriver.NwcS - 2); i--){
					 icount3 = icount3 + 1;
					 xData[icount3] = stks[i];
					 yData[icount3] = ppks[i] - 14.7;
					 }
				 //--- assign additional data to prevent assigning zero
				 if(MainDriver.iDone > nData){
					 for(int i = icount3 + 1; i<MainDriver.iDone; i++){
						 xData[i] = stks[MainDriver.NwcS - 1];
						 yData[i] = fCP - 14.7;
						 }
					 }
				 for(int i = 0; i<MainDriver.iDone; i++){
					 xData2[i] = MainDriver.vKmudSt[i];
					 yData2[i] = MainDriver.Pcontrol[i] - 14.7;
					 }
				 //--- assign additional data to prevent assigning zero
				 if(MainDriver.iDone < nData){
					 for(int i = MainDriver.iDone; i<nData; i++){
						 xData2[i] = MainDriver.vKmudSt[MainDriver.iDone-1];
						 yData2[i] = MainDriver.Pcontrol[MainDriver.iDone-1] - 14.7;
						 }
					 }
				 cp0 = new createKillSheetPanel("Stroke", "P. (psig)", "Standard Kill Pump Pressure", xData, xData2, yData, yData2, "Theoretical", "Actual");		
				 cp0.setBounds(0, 0, ChartPnlXsize, ChartPnlYsize);
				 add(cp0);
				 }			
			 }
		}
	
	class createKillSheetPanel extends JPanel{			
		DefaultXYDataset dataset =  new DefaultXYDataset();
		DefaultXYDataset dataset2 =  new DefaultXYDataset();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
		NumberAxis xAxis;
		NumberAxis yAxis;
    	NumberAxis yAxis2;
    	String ChartTitle="";
		double[][] dataInput;
		double[][] dataInput2;
		String dataTitle = "";
		String dataTitle2 = "";
		String xAxisTitle = "x";
		String yAxisTitle = "y";
		ChartPanel cp;
		
		createKillSheetPanel(){			
			setLayout(null);
		}
		
		createKillSheetPanel(double[] Xdata, double[] Ydata, String CT, String DT, String XaxT, String YaxT){			
			setLayout(null);
			ChartTitle = CT;
			dataTitle = DT;
			xAxisTitle = XaxT;
			yAxisTitle = YaxT;
			dataInput = new double[2][Ydata.length];
			
			for(int j=0; j<Ydata.length; j++){
				dataInput[0][j] = Xdata[j]; // Invert sec to min 20131112 ajw
				dataInput[1][j] = Ydata[j];
				if(dataInput[1][j]==0) dataInput[1][j]=0.001;
			}
			
			dataset.addSeries(dataTitle, dataInput);
			renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
			renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
			xAxis = new NumberAxis(xAxisTitle);
	    	yAxis = new NumberAxis(yAxisTitle);
	    	XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
	    	plot.setOutlineVisible(false);
	    	JFreeChart chart = new JFreeChart(ChartTitle, plot);
	    	chart.removeLegend();
	    	
	    	cp = new ChartPanel(chart);
	    	cp.setBounds(0,0,ChartPnlXsize-10, ChartPnlYsize-40);
	    	add(cp);		
		}
		
		createKillSheetPanel(String XaxT, String YaxT, String CT, double[] Xdata, double[] Xdata2, double[] Ydata, double[] Ydata2, String DT, String DT2){ // For standard kill sheet
			setLayout(null);
			ChartTitle = CT;
			dataTitle = DT;
			dataTitle2 = DT2;
			xAxisTitle = XaxT;
			yAxisTitle = YaxT;
			
			
			dataInput = new double[2][Ydata.length];
			dataInput2 = new double[2][Ydata2.length];			
			
			for(int j=0; j<Ydata.length; j++){
				dataInput[0][j] = Xdata[j]; // Invert sec to min 20131112 ajw
				dataInput[1][j] = Ydata[j];
				if(dataInput[1][j]==0) dataInput[1][j]=0.001;
				}			
			
			for(int j=0; j<Ydata2.length; j++){				
				dataInput2[0][j] = Xdata2[j];
				dataInput2[1][j] = Ydata2[j];
				if(dataInput2[1][j]==0) dataInput2[1][j]=0.001;
				}			
			
			if(dataInput2[0][Ydata2.length-1]==0 && dataInput2[1][Ydata2.length-1]==0){
				dataInput2[0][Ydata2.length-1] = dataInput2[0][Ydata2.length-2]; // Invert sec to min 20131112 ajw
				dataInput2[1][Ydata2.length-1] = dataInput2[0][Ydata2.length-2];
				}
			
			dataset.addSeries(dataTitle, dataInput);
			dataset2.addSeries(dataTitle2, dataInput2);
			
			NumberAxis xAxis = new NumberAxis(xAxisTitle);
			NumberAxis xAxis2 = new NumberAxis(xAxisTitle);
	    	NumberAxis yAxis = new NumberAxis(yAxisTitle);
	    	NumberAxis yAxis2 = new NumberAxis(yAxisTitle);
	    	renderer.setSeriesPaint(0, Color.red);
	    	renderer2.setSeriesPaint(0, Color.blue);
	    	renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	renderer2.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	
	    	XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
	    	//plot.setDomainAxis(1, xAxis);
	    	//plot.setRangeAxis(1, yAxis);
	    	plot.setDataset(1, dataset2);
	    	plot.setRenderer(1, renderer2);
	    	plot.mapDatasetToRangeAxis(2, 1);
	    	plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
	    	plot.setOutlineVisible(false);
	    	JFreeChart chart = new JFreeChart(ChartTitle, plot);	    	
	    	
	    	cp = new ChartPanel(chart);
	    	cp.setBounds(0,0,ChartPnlXsize-10, ChartPnlYsize-40);
	    	add(cp);
		}
		
		void resizeKillPnl(){			
			cp.setBounds(0,0,ChartPnlXsize-10, ChartPnlYsize-40);
		}
	}
}
