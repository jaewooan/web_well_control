package ML_ERD;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Scrollbar;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JScrollBar;

class SimAuto extends JFrame{
	//============================================================================
	// Define (semi-local) variables and functions used in kick simulation
	// for the calculation of kick pressure at the Top of the each cell
	//=============================================================================
	//
	int mudCase = 0, iMove = 0, iContinue = 0;
	int grpint  = 0, timeNp  = 0, timeNpUse  = 0; 
	long timeStart =0;
	long timeNew =0, timeOld =0;
	int NpEnd  =0; 
	int iKmud  = 0, index2  = 0;
	int iTcirc  = 0, itKmud  = 0;
	int itKout  = 0, plotTime  = 0, Gwidth  = 0;
	int Gheit  = 0, Gleft  = 0, Gtop  = 0;    //7/11/02
	int Oheit  = 0, OLeft  = 0, OTop  = 0;
	int OWidth  = 0, iDClk  = 0, iGraph  = 0;
	int iShift =0;
	int pumpon = 0; //hs
	double Tdelay=0;
	double PbTry=0, px=0, xVert=0, xbVert=0, xKill=0, Pkick=0, kickVD=0, kickVDbtm=0, gasDen=0, gasVis=0, zz=0, Pbtm=0;
	double PVgain=0, SPP=0, pumpP=0, Pcasing=0, Pchoke=0;
	double Pbeff=0 , psia=0, QgDay=0, CHKpcent=0;  //, Tdelay; unused after we use Dt
	double[] volLqd = new double[MainDriver.Ntot];
	double volKmud=0, QtotMix=0, kickLoc=0, X=0, volkkmix=0;
	double[] ppks = new double[14];
	double[] stks = new double[14];
	double iCP=0, fCP =0;
	int[] pmoveTop = new int[10];
	int[] pmoveLeft = new int[10];
	int iHmove =0, iVmove =0;
	int enXpos =0, enYpos =0;
	//------------------------------------------- after 7/1/02, for ERD and ML
	int ifastRun =0;   //regular(1), else-fast run
	double mudLineP =0, mudFlowOut =0, gasFlowOut =0;
	//------------ for multilateral well control, after 7/29/2003
	double[] mlHgeff = new double[5];   //fraction, to account multiple kicks in ML
	double[] mlKickTopMD = new double[5];
	int iMLkickLoc=0;  //to track whether we have a kick form ML(1) or not(0)
	double mlPVgain =0;
	double[] mlQgTotMold = new double[5];
	int iMLflux =0;   //to track kick influx from ML to MP; with (1)/ without (0)
	double dpchoke_old =0, pkick=0;
	double totHr =0, totMin =0, totSec =0;
	double reset_Pump_Stroke = 0;
	double Start_Pump_Stroke =0;
	double Now_Pump_Stroke=0;
	double Killsheet_Pump_Stroke =0;
	boolean IsPPflag=true;
	double[] GasPropEX = new double[3];
	double[] getLinesEX = new double[5];
	double[] getDPinsideEX = new double[2];
	
	
	int SimWellPicControl = 0;	
	DecimalFormat format2 = new DecimalFormat(".");
	
	Timer TimerCirculation;
	int TimerCirculationOn = 0;	
	int TimerCircTaskFinished = 1;
	int TimerCircTask2Finished = 1;
	Timer TimerKickOut;
	int TimerKickOutOn=0;
	int TimerKickOutTaskFinished = 1;
	Timer TimerKillMud;
	int TimerKillMudTaskFinished = 1;
	int TimerKillMudOn=0;
	Timer TimerCheckBO;
	int TimerBOOn=0;
	
	private JPanel contentPane;	
	JPanel SimAccPnl = new JPanel();
	JLabel simAccTitle = new JLabel("Simulation Acceleration Ratio");
	JLabel simTimelbl = new JLabel("Times Faster Than Real Time(1 to 20)");
	JTextField txtSimRate = new JTextField();
	JSlider HScrollRate = new JSlider();
	JButton cmdPumpKill = new JButton("Pump Kill Mud");
	JButton cmdFixPump = new JButton("Fix Pump Failure");
	JPanel CHKPnl = new JPanel();
	JTextField txtCHKopen = new JTextField();
	Scrollbar HScrollCHK = new Scrollbar();
	JPanel DrlInfoPnl = new JPanel();
	JTextArea operationMsg = new JTextArea("Pause", 1, 100);
	JTextField txtVDtop = new JTextField();
	JTextField txtVDbottom = new JTextField();
	JTextField txtMDkickTop = new JTextField();
	JTextField txtMDkickBottom = new JTextField();
	JTextField txtPitGain = new JTextField();
	JTextField txtPumpP = new JTextField();
	JTextField txtPumpRate = new JTextField();
	JTextField txtReturnRate = new JTextField();
	JTextField txtTime = new JTextField();
	JTextField txtFormationP = new JTextField();
	JTextField txtBHP = new JTextField();
	JTextField txtCasingP = new JTextField();
	JTextField txtKickP = new JTextField();
	JTextField txtMudLineP = new JTextField();
	JTextField txtChokeP = new JTextField();
	JTextField txtSPP = new JTextField();
	
	private final JTextField txtGasRate = new JTextField();
	private final JLabel lblReturnGasRate = new JLabel("Mscf/Day,  Return Gas Rate");
	private final JTextField txtVolume = new JTextField();
	private final JLabel lblTotalMudVolume = new JLabel("bbls,  Total Mud Volume Pumped");
	private final JTextField txtStroke = new JTextField();
	private final JLabel lblNumberOfStrokes = new JLabel("Number of Strokes");
	
	MudGaugePanel pan1 = new MudGaugePanel();
	CHKGaugePanel pan2 = new CHKGaugePanel();
	SDPGaugePanel pan3 = new SDPGaugePanel();
		
	JTextField txtSIMmode = new JTextField();
	JTextField txtRateDiff = new JTextField();
	JTextField txtPchoke = new JTextField();
	JTextField txtPsp = new JTextField();
	private final JLabel MudRRdif = new JLabel("Mud Return Rate Difference");
	private final JLabel MudRRdif2 = new JLabel("    (gal/min)");
	private final JLabel CHKPR = new JLabel("Choke Pressure (psig)");
	private final JLabel STPPP = new JLabel("Standpipe Pressure (psig)");	
	private final JLabel Mud1 = new JLabel("0.0");
	private final JLabel Mud2 = new JLabel("250");
	private final JLabel Mud3 = new JLabel("500");
	private final JLabel Mud4 = new JLabel("750");	
	private final JLabel CHK1 = new JLabel("0.0");
	private final JLabel CHK2 = new JLabel("1000");
	private final JLabel CHK3 = new JLabel("2000");
	private final JLabel CHK4 = new JLabel("3000");	
	private final JLabel STP1 = new JLabel("0.0");
	private final JLabel STP2 = new JLabel("1000");
	private final JLabel STP3 = new JLabel("2000");
	private final JLabel STP4 = new JLabel("3000");
	
		
	int InforPnlSrtX = 12, InforPnlSrtY = 10, panIntv = 10;
	int operationMsgSizeY = 23, SimAccPnlSizeX = 306, SimAccPnlSizeY = 75;
	int CHKpnlSizeX = 400;
	int cmdBtnSizeX = 130, cmdBtnSizeY=35;
	int txtIntvX=12, txtSrtX = 5, txtSrtY=38, txtSizeX = 70, txtSizeY=18, txtIntvY=5;
	int txtLblXsize=194, txtLblYsize = txtSizeY;
	int panSizeX = 200, panSizeY = 160, pan1Xsrt = InforPnlSrtX+(txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize)+panIntv, pan1Ysrt=InforPnlSrtY+SimAccPnlSizeY+panIntv/2, xintv=10, yintv=10; 
	int labelFontSize=12, labelFontSize2=12, RateDiffSize=10;
	int ovalIntv =5, LineLength=ovalIntv+1, Radius=panSizeY/4;
	double tmpintMud = 0, tmpintCHK=0, tmpintSDP = 0;
	
	int frameXSize = 1200, frameYSize = 720;
	int GraphPnlXsize = (frameXSize-(pan1Xsrt+panSizeX+xintv*4))/2, GraphPnlYsize = 180;
    int GraphXLoc =40, GraphYLoc = 35;
    int GraphXsize = GraphPnlXsize-2*GraphXLoc;
    int GraphYsize = GraphPnlYsize-2*GraphYLoc+15;// 15 = label font size
    
	Sgraph[] Sgg = new Sgraph[5];           
	
	JLabel lblChokeOpen = new JLabel("Choke Open % by Diameter Ratio (0 to 100)");
	
	wellpic m3 = new wellpic();
	
	JMenuBar menuBar = new JMenuBar();
	JMenu mHelp = new JMenu("Helps      ");
	JMenu mMenus = new JMenu("Menus      ");
	JMenu mGraph = new JMenu("Graphs      ");
	JMenu mPause = new JMenu("Pause/Continue");
	JMenu mContinue = new JMenu("Continue");
	private final JMenuItem mItemHelp = new JMenuItem("Help");
	private final JMenuItem mItemBckToMain = new JMenuItem("Back to the Main Menu");
	private final JMenuItem mItemSrtWellKill = new JMenuItem("Start Well Kill");
	private final JMenu mWellbore = new JMenu("Wellbore");
	private final JMenu mSimSpd = new JMenu("Simulation Speed");
	private final JMenuItem mItemFstRun = new JMenuItem("Fast Run");
	private final JMenuItem mItemRglarRun = new JMenuItem("Regular Run");
	private final JMenuItem mItemShowWell = new JMenuItem("Show Wellbore");
	private final JMenuItem mItemWell = new JMenuItem("Hide Wellbore");
	private final JMenuItem mItmGrpHelp = new JMenuItem("Graph Help");
	private final JMenuItem mItmGrpIntv = new JMenuItem("Graph Interval");
	private final JMenuItem mItmPltGrp = new JMenuItem("Plot Graphs");
	private final JMenuItem mItmPause = new JMenuItem("Pause");
	private final JMenuItem mItmCont = new JMenuItem("Continue");
	
	ButtonGroup AutoManualGroup = new ButtonGroup();
	JRadioButton AutoSelect = new JRadioButton("Auto");
	JRadioButton ManualSelect = new JRadioButton("Manual");
	
	int TimerCirculationIntv = 1000;
	int TimerKickOutIntv = 2000;
	int TimerKillMudIntv = 4000;
	int dummy =0;
	
	int WPshowStatus = 0;//current status => 1 : show 0: hide 20130916 ajw
	private final JTextArea txtN2phase = new JTextArea();
	private final JMenuItem mItmResultsPlot = new JMenuItem("Results in plots");
	resultPlot rp;
	
	int Stroke1=0, Stroke2=0, TotalStroke=0;//added by jaewoo, 20140206
	
	SimAuto(){
		setTitle("SNU Well Control Simulator - Auto Control");
		setIconImage(MainDriver.icon.getImage());
		
		TimerCirculationOn = 0;	
		TimerKickOutOn=0;
		TimerKillMudOn=0;
		TimerCirculationIntv = 1000;
		TimerKickOutIntv = 2000;
		TimerKillMudIntv = 4000;
		AutoSelect.setVisible(false);
		ManualSelect.setVisible(false);
		addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                e.getWindow().dispose();
                e.getWindow().setVisible(false);
                MainDriver.iWshow = 0;
                menuClose();
            }
        });
		
		
		 m3.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent arg0) {
					m3.widthX = m3.getWidth();
					m3.heightX = m3.getHeight();
					m3.pp.setBounds(0, 0, m3.widthX-m3.frameXintvSize*2, m3.heightX-m3.frameYintvSize1-m3.frameYintvSize2);
				}
			});		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(m3.widthX, 0, frameXSize, frameYSize);		
		
		setJMenuBar(menuBar);		
		menuBar.add(mHelp);			
		mHelp.add(mItemHelp);
		menuBar.add(mMenus);	
		mMenus.add(mSimSpd);	
		mItemFstRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//   msgTmp$ = "if you use this option, you can NOT see updated plots nor kick drawings in the wellbore."
				//   + "\n" +"  However, you can see results displayed and all plots are available after the simulation."
				//   response = MsgBox(msgTmp$, 1, msgTitle)
				//   if response = vbCancel Then Exit Sub				//
				   ifastRun = 10;
				   TimerCirculationIntv = 500;
				   TimerKickOutIntv = 500;
				   TimerKillMudIntv = 500;
				   
				   if(ifastRun != 1){
						String msg = "If you use Fast Run option, drawings in the wellbore are NOT updated during the simulation.";
						dummy = JOptionPane.showConfirmDialog(null, msg, "Show Wellbore", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);// yes =0, no =1
						}
				   if(dummy==1){
					   operationMsg.setText("Fast Run Mode");
					   
					   if(TimerCirculationOn == 1){
						   TimerCirculation.cancel();
						   TimerCirculation = new Timer();
						   TimerCirculation.schedule(new TimerCircTask(), 0, TimerCirculationIntv);
					   }
					   
					   if(TimerKickOutOn == 1){
						   TimerKickOut.cancel();
						   TimerKickOut = new Timer();
						   TimerKickOut.schedule(new TimerKickOutTask(), 0, TimerKickOutIntv);
					   }
					   
					   if(TimerKillMudOn == 1){
						   TimerKillMud.cancel();
						   TimerKillMud = new Timer();
						   TimerKillMud.schedule(new TimerKillMudTask(), 0, TimerKillMudIntv);
					   }
				   }
				   
			}
		});
		mSimSpd.add(mItemFstRun);	
		mItemRglarRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				   ifastRun = 1;
				   TimerCirculationIntv = 1000;
				   TimerKickOutIntv = 2000;
				   TimerKillMudIntv = 4000;
				   operationMsg.setText("Regular Run Mode");
				   
				   if(TimerCirculationOn == 1){
					   TimerCirculation.cancel();
					   TimerCirculation = new Timer();
					   TimerCirculation.schedule(new TimerCircTask(), 0, TimerCirculationIntv);
				   }
				   
				   if(TimerKickOutOn == 1){
					   TimerKickOut.cancel();
					   TimerKickOut = new Timer();
					   TimerKickOut.schedule(new TimerKickOutTask(), 0, TimerKickOutIntv);
				   }
				   
				   if(TimerKillMudOn == 1){
					   TimerKillMud.cancel();
					   TimerKillMud = new Timer();
					   TimerKillMud.schedule(new TimerKillMudTask(), 0, TimerKillMudIntv);
				   }
				   
			}
		});
		mSimSpd.add(mItemRglarRun);		
		mMenus.add(mWellbore);		
		mItemShowWell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dummy=0;
				if(ifastRun != 1){
					String msg = "If you use Fast Run option, drawings in the wellbore are NOT updated during the simulation.";
					dummy = JOptionPane.showConfirmDialog(null, msg, "Show Wellbore", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);// yes =0, no =1
					}	
				if(dummy==0){
					WPshowStatus = 1;
					m3.setVisible(true); 
					MainDriver.iWshow = 1;
					if(MainDriver.Np == MainDriver.NpSi){						
						m3.QtotMixWP=QtotMix;
						m3.xDepthWP=MainDriver.Xb;
						m3.drawKickIndex=1;
						m3.drawOmudIndex=1;
						m3.drawWellboreIndex=1;
						m3.pp.repaint();
					}
				}
			}
		});
		mWellbore.add(mItemShowWell);		
		mItemWell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(WPshowStatus==1){
					m3.setVisible(false);
					WPshowStatus=0;
				}
				 MainDriver.iWshow = 0;
			}
		});
		mWellbore.add(mItemWell);		
		mItemSrtWellKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0 && WPshowStatus==0){
					m3.setVisible(true);
					WPshowStatus=1;
				}
			    if (ifastRun != 1 || MainDriver.iWshow == 0){
			    	m3.setVisible(false);
			    	WPshowStatus=0;
			    }
			    
			    if(MainDriver.iCHKcontrol==1){
			    	HScrollCHK.setValue(25);
				    txtCHKopen.setText("25");
				    if(TimerCirculationOn ==0){
				    	TimerCirculationOn = 1;
				    	TimerCirculation = new Timer();
				    	TimerCirculation.schedule(new TimerCircTask(), 1000, TimerCirculationIntv); 
				    	//TimerCirculation.schedule(new TimerCircTask(), 1000, 10); // 타이머 편의를 위해 조절, 위에 껏이 맞는 거임히히	
				    }
				    			    
				    if(ifastRun == 1) operationMsg.setText("Simulation starts as Regular Run mode !");
				    else operationMsg.setText("Simulation starts as Fast Run mode !");
			    }
			    
			    else{
			    	if(HScrollCHK.getValue()==0 && MainDriver.imode == 1){
			    		String msg = "Oh, Boy !  You made a serious mistake !";
			    		JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			    	}
			    	else{
			    		if(TimerCirculationOn ==0){
					    	TimerCirculationOn = 1;
					    	TimerCirculation = new Timer();
					    	TimerCirculation.schedule(new TimerCircTask(), 1000, TimerCirculationIntv);
					    	timeStart = System.currentTimeMillis()/1000;// sec
			    		}
			    	}
			    }
			    
			    MainDriver.QtMix = MainDriver.Qkill;
			    mudCase = -99;
			}
		});
		mMenus.add(mItemSrtWellKill);		
		mItemBckToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m3.dispose();
				m3.setVisible(false);
				MainDriver.iWshow=0;
				menuClose();
			}
		});
		mMenus.add(mItemBckToMain);
		menuBar.add(mGraph);				
		mItmGrpHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String HelpMsg =  "  From the Menus menu, you can load/unload the wellbore profile.  You can move or adjust the size of the"
				   	      + "\n" +" wellbore profile. In order to start the simulation click the Start Circulation menu under Menus menu."
				   	      + "\n" +" Then you will see kick movement and automatic choke control."
					      + "\n" +" You can change the Simulation Rate any time."
					      + "\n" +" You may use Fast Run option to have fast simulation rate. In this case, simulation results"
					      + "\n" +" are only displayed. No plot and drawing are updated. To see its result in plots, use Plot Graphs menu"
					      + "\n" +" under Graphs menu. At the end of simulation, all the results are available as usual."
					      + "\n" +" You can use the combination of Fast Run and adjustment of simulation ratio."
					      + "\n" +" However, if your simulation rate is too fast, you may loose some detailed results in case of small kicks,"
					      + "\n" +" small wellbore, fast pump rate, etc. Then rerun using a lower simulation rate.";
				JOptionPane.showMessageDialog(null, HelpMsg, "Help", JOptionPane.INFORMATION_MESSAGE);	
			}
		});
		mGraph.add(mItmGrpHelp);	
		
		mItmGrpIntv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int grpintdef = 0, xx=0;
			    grpintdef = grpint;
			       
				boolean check = true;  //20131022 ajw
				while(check){     
					String str = JOptionPane.showInputDialog("Enter time interval to update graph plots (in every 1 to 30 minutes)");     
					try{    						
						xx = Integer.parseInt(str); 
						check = false; //Exception이 안생기면 while문 빠져나감      
						}catch(NumberFormatException ex){ 
							xx = grpintdef;
							System.out.println("It isn't number.");   
							}   
					}
				if (MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0) m3.setVisible(true);
				if(xx != grpint){ 
					grpint = (int)(xx + 0.5);
					if(grpint > 30) grpint = 30;
					if(grpint < 1)  grpint = 1;
				}
			}
		});
		
		mGraph.add(mItmGrpIntv);		
		mItmPltGrp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlotG4();
				if(MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0) m3.setVisible(true);
			}
		});
		mGraph.add(mItmPltGrp);
		mItmResultsPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rp = new resultPlot();
				rp.setVisible(true);
			}
		});
		
		mGraph.add(mItmResultsPlot);
		menuBar.add(mPause);			
		mItmPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
			    iContinue = 1;
			    if (TimerCirculationOn == 1){
			    	iTcirc = 1;
			    	TimerCirculation.cancel();
			    	TimerCirculationOn = 0;
			    }
			    if (TimerKickOutOn == 1){
			    	itKout = 1;
			    	TimerKickOut.cancel();
			    	TimerKickOutOn = 0;
			    }
			    if (TimerKillMudOn == 1){
			    	itKmud = 1;
			    	TimerKillMud.cancel();
			    	TimerKillMudOn = 0;
			    }
			    if (MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0 && WPshowStatus==0){
			    	m3.setVisible(true);
			    	WPshowStatus=1;
			    }
			    operationMsg.setText("Simulation is Paused !");				
			}
		});
		mPause.add(mItmPause);
		mPause.add(mItmCont);
		//menuBar.add(mContinue);		
		mItmCont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    if (iContinue == 1){
			    	iContinue = 0;
			    	if (iTcirc == 1 && TimerCirculationOn==0){
			    		TimerCirculationOn=1;
			    		TimerCirculation = new Timer();
			    		TimerCirculation.schedule(new TimerCircTask(), 0, TimerCirculationIntv);	
			    	}
			    	if (itKout == 1 && TimerKickOutOn == 0){
			    		TimerKickOutOn = 1;
			    		TimerKickOut = new Timer();
			    		TimerKickOut.schedule(new TimerKickOutTask(), 0, TimerKickOutIntv);
			    		}
			    	if (itKmud == 1 && TimerKillMudOn == 0){
			    		TimerKillMudOn=1;
			    		TimerKillMud = new Timer();
			    		TimerKillMud.schedule(new TimerKillMudTask(), 0, TimerKillMudIntv);
			    		}
			    	iTcirc = 0;
			    	itKout = 0;
			    	itKmud = 0;
			    	if (MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0 && WPshowStatus==0){
			    		m3.setVisible(true);
			    		WPshowStatus=1;
			    	}
			    	operationMsg.setText("Simulation is Running again !");
			    	}
			}
		});
		
		
		mItemHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    String HelpMsg = "From the Menus menu, you can load/unload the wellbore profile.  You can move or adjust the size of the"
			    +"\n"+"  wellbore profile. In order to start the simulation click the Start Circulation menu under Menus menu."
			    +"\n"+"  Then you will see kick movement and automatic choke control."
			    +"\n"+"  You can change the Simulation Rate any time."
			    +"\n"+"  You may use Fast Run option to have fast simulation rate. In this case, simulation results"
			    +"\n"+"  are only displayed. No plot and drawing are updated. To see its result in plots, use Plot Graphs menu"
			    +"\n"+"  under Graphs menu. At the end of simulation, all the results are available as usual."
			    +"\n"+"  You can use the combination of Fast Run and adjustment of simulation ratio."
			    +"\n"+"  However, if your simulation rate is too fast, you may loose some detailed results in case of small kicks,"
			    +"\n"+"  small wellbore, fast pump rate, etc. Then rerun using a lower simulation rate."
			    +"\n"+"     ^.^ GOOD LUCK ! ^.^";			    
			    JOptionPane.showMessageDialog(null, HelpMsg, "Help", JOptionPane.INFORMATION_MESSAGE);			    
			}
		});
		
		
		
		
		contentPane =  new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);	
		contentPane.setLayout(null);					
		
		operationMsg.setBounds(InforPnlSrtX, InforPnlSrtY, txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize, operationMsgSizeY);
		SimAccPnl.setBounds(pan1Xsrt, InforPnlSrtY, SimAccPnlSizeX, SimAccPnlSizeY);
		CHKPnl.setBounds(pan1Xsrt+panIntv+SimAccPnlSizeX, InforPnlSrtY, CHKpnlSizeX, 75);
		cmdPumpKill.setBounds(pan1Xsrt+panIntv*2+SimAccPnlSizeX+CHKpnlSizeX, InforPnlSrtY, cmdBtnSizeX, cmdBtnSizeY);
		cmdFixPump.setBounds(pan1Xsrt+panIntv*2+SimAccPnlSizeX+CHKpnlSizeX, InforPnlSrtY+cmdBtnSizeY+panIntv/2, cmdBtnSizeX, cmdBtnSizeY);					
		DrlInfoPnl.setBounds(InforPnlSrtX, InforPnlSrtY+operationMsgSizeY+panIntv, txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize, txtSrtY+24*txtSizeY+28*txtIntvY);
		
		SimAccPnl.setLayout(null);
		SimAccPnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		SimAccPnl.setBackground(new Color(240, 240, 240));		
		contentPane.add(SimAccPnl);		
		
		simAccTitle.setHorizontalAlignment(SwingConstants.CENTER);
		simAccTitle.setFont(new Font("굴림", Font.BOLD, 12));
		simAccTitle.setBounds(12, 2, 214, 15);
		SimAccPnl.add(simAccTitle);		
		
		simTimelbl.setHorizontalAlignment(SwingConstants.LEFT);
		simTimelbl.setBounds(22, 21, 254, 15);
		SimAccPnl.add(simTimelbl);		
		txtSimRate.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtSimRate.setText("0");
		txtSimRate.setBounds(12, 38, 72, 20);
		SimAccPnl.add(txtSimRate);
		
		HScrollRate.setBounds(96, 34, 180, 35);
		HScrollRate.setSnapToTicks(true);
		HScrollRate.setPaintTicks(true);
		HScrollRate.setFont(new Font("굴림", Font.PLAIN, 11));
		HScrollRate.setValue(1);
		HScrollRate.setMinimum(1);
		HScrollRate.setMaximum(20);
		HScrollRate.setMajorTickSpacing(1);
		
		SimAccPnl.add(HScrollRate);
		HScrollRate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(e.getSource()==HScrollRate){
					MainDriver.SimRate = HScrollRate.getValue();
					txtSimRate.setText(Integer.toString(MainDriver.SimRate));
				}
			}
		});
		cmdPumpKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		
		contentPane.add(cmdPumpKill);		
		
		
		contentPane.add(cmdFixPump);
		
		CHKPnl.setLayout(null);
		CHKPnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		CHKPnl.setBackground(Color.LIGHT_GRAY);		
		contentPane.add(CHKPnl);
		
		JLabel lblChokeControlPanel = new JLabel("  Choke Control Panel");
		lblChokeControlPanel.setHorizontalAlignment(SwingConstants.LEFT);
		lblChokeControlPanel.setFont(new Font("굴림", Font.BOLD, 12));
		lblChokeControlPanel.setBounds(12, 2, 214, 15);
		CHKPnl.add(lblChokeControlPanel);
		
		
		lblChokeOpen.setHorizontalAlignment(SwingConstants.LEFT);
		lblChokeOpen.setBounds(22, 21, 254, 15);
		CHKPnl.add(lblChokeOpen);
		
		HScrollCHK.setValue(0);
		txtCHKopen.setHorizontalAlignment(SwingConstants.CENTER);
		txtCHKopen.setText("0");
		txtCHKopen.setBounds(12, 38, 72, 20);
		CHKPnl.add(txtCHKopen);		
		
		HScrollCHK.setBackground(Color.WHITE);
		HScrollCHK.setOrientation(Scrollbar.HORIZONTAL);
		HScrollCHK.setBounds(95, 40, 200, 24);
		CHKPnl.add(HScrollCHK);				
				
		AutoManualGroup.add(AutoSelect);
		AutoSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.iCHKcontrol = 1;
				MainDriver.imode = 2;
			}
		});
		
		AutoSelect.setBackground(Color.LIGHT_GRAY);
		AutoSelect.setBounds(300, 30, 80, 20);
		CHKPnl.add(AutoSelect);
		
		AutoManualGroup.add(ManualSelect);
		ManualSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iCHKcontrol = 2;
				MainDriver.imode = 1;
			}
		});
		
		ManualSelect.setBackground(Color.LIGHT_GRAY);
		ManualSelect.setBounds(300, 50, 80, 20);
		CHKPnl.add(ManualSelect);
		
		HScrollCHK.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				if(arg0.getSource()==HScrollCHK){
					txtCHKopen.setText(Integer.toString(HScrollCHK.getValue()));
				}
				if(MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0 && WPshowStatus==0){
					m3.setVisible(true);
					WPshowStatus=1;
				}
			}
		});
		
		operationMsg.setEditable(false);		
		contentPane.add(operationMsg);		
				
		DrlInfoPnl.setLayout(null);
		DrlInfoPnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));		
		contentPane.add(DrlInfoPnl);
		
		JLabel lblCurrentResults = new JLabel("Current Results");
		lblCurrentResults.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentResults.setFont(new Font("굴림", Font.BOLD, 17));
		DrlInfoPnl.add(lblCurrentResults);
		
		JLabel lblCR1 = new JLabel("Total Elapsed Time");		
		DrlInfoPnl.add(lblCR1);
		
		JLabel lblCR2 = new JLabel("(hr : min : sec)");
		DrlInfoPnl.add(lblCR2);
		
		JLabel lblDepthKick = new JLabel("Depth & Kick Volume");
		lblDepthKick.setHorizontalAlignment(SwingConstants.CENTER);
		lblDepthKick.setForeground(Color.BLUE);
		lblDepthKick.setFont(new Font("굴림", Font.BOLD, 17));		
		DrlInfoPnl.add(lblDepthKick);
		txtVDtop.setHorizontalAlignment(SwingConstants.RIGHT);
				
		txtVDtop.setText(".0");
		DrlInfoPnl.add(txtVDtop);
		txtVDbottom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtVDbottom.setText(".0");
		DrlInfoPnl.add(txtVDbottom);
		txtMDkickTop.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtMDkickTop.setText("0.0");
		DrlInfoPnl.add(txtMDkickTop);
		txtMDkickBottom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtMDkickBottom.setText("0.0");
		DrlInfoPnl.add(txtMDkickBottom);
		txtPitGain.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtPitGain.setText("0.0");
		DrlInfoPnl.add(txtPitGain);
		txtPumpP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtPumpP.setText("0.0");
		DrlInfoPnl.add(txtPumpP);
		txtPumpRate.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtPumpRate.setText("0.0");
		DrlInfoPnl.add(txtPumpRate);
		txtReturnRate.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtReturnRate.setText(" ");
		DrlInfoPnl.add(txtReturnRate);
		
		JLabel lblPresInfo = new JLabel("Pressure Infromation");
		lblPresInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPresInfo.setForeground(Color.BLUE);
		lblPresInfo.setFont(new Font("굴림", Font.BOLD, 17));		
		DrlInfoPnl.add(lblPresInfo);
		txtTime.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtTime.setText("0:00:00");
		DrlInfoPnl.add(txtTime);
		txtFormationP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtFormationP.setText(".0");
		DrlInfoPnl.add(txtFormationP);
		txtBHP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtBHP.setText(".0");
		DrlInfoPnl.add(txtBHP);
		txtCasingP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtCasingP.setText("14.7");
		DrlInfoPnl.add(txtCasingP);
		txtKickP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtKickP.setText(".0");
		DrlInfoPnl.add(txtKickP);
		txtMudLineP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtMudLineP.setText("0.0");
		DrlInfoPnl.add(txtMudLineP);
		txtChokeP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtChokeP.setText("0.0");
		DrlInfoPnl.add(txtChokeP);
		txtSPP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtSPP.setText("0.0");
		DrlInfoPnl.add(txtSPP);
		
		JLabel lblVerticalDepthTo = new JLabel("ft,  Vertical Depth to Kick Top");
		DrlInfoPnl.add(lblVerticalDepthTo);
		
		JLabel lblVdToThe = new JLabel("ft, VD to the Kick Bottom");
		DrlInfoPnl.add(lblVdToThe);
		
		JLabel lblMdToThe = new JLabel("ft,  MD to the Kick Top");
		DrlInfoPnl.add(lblMdToThe);
		
		JLabel lblMdToThe_1 = new JLabel("ft,  MD to the Kick Bottom");
		DrlInfoPnl.add(lblMdToThe_1);
		
		JLabel lblVolumeOfThe = new JLabel("bbls,  Vol. of the Kick in the Well");
		DrlInfoPnl.add(lblVolumeOfThe);
		
		JLabel lblPumpPressurePsig = new JLabel("psig,  Pump Pressure");
		DrlInfoPnl.add(lblPumpPressurePsig);
		
		JLabel lblPumpkillRateGpm = new JLabel("gpm,  Pump(Kill) Rate");
		DrlInfoPnl.add(lblPumpkillRateGpm);
		
		JLabel lblReturnMudRate = new JLabel("gpm,  Return Mud Rate");
		DrlInfoPnl.add(lblReturnMudRate);
		
		JLabel lblStandPipePressure = new JLabel("psig,  Stand Pipe Pressure");
		DrlInfoPnl.add(lblStandPipePressure);
		
		JLabel lblChokePressurePsig = new JLabel("psig,  Choke Pressure");		
		DrlInfoPnl.add(lblChokePressurePsig);
		
		JLabel lblPressureAtMud = new JLabel("psig,  Pressure at Mud Line");		
		DrlInfoPnl.add(lblPressureAtMud);
		
		JLabel lblPressureAtThe = new JLabel("psig,  Pressure at the Kick Top");		
		DrlInfoPnl.add(lblPressureAtThe);
		
		JLabel lblPsigCasingSeat = new JLabel("psig,  Casing Seat Pressure");		
		DrlInfoPnl.add(lblPsigCasingSeat);
		
		JLabel lblPsigBottomholePressure = new JLabel("psig,  BottomHole Pressure");		
		DrlInfoPnl.add(lblPsigBottomholePressure);
		
		JLabel lblPsigFormationPressure = new JLabel("psig,  Formation Pressure");		
		DrlInfoPnl.add(lblPsigFormationPressure);
		
		JLabel lblInTheWell = new JLabel("         in the Well");		
		DrlInfoPnl.add(lblInTheWell);
		
		JLabel lblPumpInformation = new JLabel("Pump Information");
		lblPumpInformation.setHorizontalAlignment(SwingConstants.CENTER);
		lblPumpInformation.setForeground(Color.BLUE);
		lblPumpInformation.setFont(new Font("굴림", Font.BOLD, 17));		
		DrlInfoPnl.add(lblPumpInformation);
		txtGasRate.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGasRate.setText(" ");
		
		DrlInfoPnl.add(txtGasRate);
				
		DrlInfoPnl.add(lblReturnGasRate);
		txtVolume.setHorizontalAlignment(SwingConstants.RIGHT);
		txtVolume.setText(" ");
		
		
		DrlInfoPnl.add(txtVolume);
		
		DrlInfoPnl.add(lblTotalMudVolume);
		txtStroke.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStroke.setText(" ");
		
		DrlInfoPnl.add(txtStroke);		
		DrlInfoPnl.add(lblNumberOfStrokes);
		
		lblCurrentResults.setBounds((txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize)/2-150/2, 10, 150, 18);
		txtTime.setBounds(txtSrtX, txtSrtY, txtSizeX, txtSizeY); lblCR1.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY, txtLblXsize, txtSizeY);
		lblCR2.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+txtSizeY, txtLblXsize, txtSizeY);
		
		lblDepthKick.setBounds((txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize)/2-200/2, txtSrtY+2*txtSizeY+2*txtIntvY, 200, 15);
		txtVDtop.setBounds(txtSrtX, txtSrtY+3*txtSizeY+3*txtIntvY, txtSizeX, txtSizeY); lblVerticalDepthTo.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+3*txtSizeY+3*txtIntvY, txtLblXsize, txtSizeY);
		txtVDbottom.setBounds(txtSrtX, txtSrtY+4*txtSizeY+4*txtIntvY, txtSizeX, txtSizeY); lblVdToThe.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+4*txtSizeY+4*txtIntvY, txtLblXsize, txtSizeY);	
		txtMDkickTop.setBounds(txtSrtX, txtSrtY+5*txtSizeY+5*txtIntvY, txtSizeX, txtSizeY);  lblMdToThe.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+5*txtSizeY+5*txtIntvY, txtLblXsize, txtSizeY);
		txtMDkickBottom.setBounds(txtSrtX, txtSrtY+6*txtSizeY+6*txtIntvY, txtSizeX, txtSizeY); lblMdToThe_1.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+6*txtSizeY+6*txtIntvY, txtLblXsize, txtSizeY);
		txtPitGain.setBounds(txtSrtX, txtSrtY+7*txtSizeY+7*txtIntvY, txtSizeX, txtSizeY); lblVolumeOfThe.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+7*txtSizeY+7*txtIntvY, txtLblXsize, txtSizeY);
		
		lblPumpInformation.setBounds((txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize)/2-180/2, txtSrtY+8*txtSizeY+9*txtIntvY, 180, 15);
		txtPumpP.setBounds(txtSrtX, txtSrtY+9*txtSizeY+10*txtIntvY, txtSizeX, txtSizeY); lblPumpPressurePsig.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+9*txtSizeY+10*txtIntvY, txtLblXsize, txtSizeY);
		txtPumpRate.setBounds(txtSrtX, txtSrtY+10*txtSizeY+11*txtIntvY, txtSizeX, txtSizeY); lblPumpkillRateGpm.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+10*txtSizeY+11*txtIntvY, txtLblXsize, txtSizeY);
		txtReturnRate.setBounds(txtSrtX, txtSrtY+11*txtSizeY+12*txtIntvY, txtSizeX, txtSizeY); lblReturnMudRate.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+11*txtSizeY+12*txtIntvY, txtLblXsize, txtSizeY);
		txtGasRate.setBounds(txtSrtX, txtSrtY+12*txtSizeY+13*txtIntvY, txtSizeX, txtSizeY);  lblReturnGasRate.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+12*txtSizeY+13*txtIntvY, txtLblXsize, txtSizeY);
		txtStroke.setBounds(txtSrtX, txtSrtY+13*txtSizeY+14*txtIntvY, txtSizeX, txtSizeY);  lblNumberOfStrokes.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+13*txtSizeY+14*txtIntvY, txtLblXsize, txtSizeY);
		txtVolume.setBounds(txtSrtX, txtSrtY+14*txtSizeY+15*txtIntvY, txtSizeX, txtSizeY);  lblTotalMudVolume.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+14*txtSizeY+15*txtIntvY, txtLblXsize, txtSizeY);
		
		lblPresInfo.setBounds((txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize)/2-200/2, txtSrtY+15*txtSizeY+17*txtIntvY, 200, 15);
		txtSPP.setBounds(txtSrtX, txtSrtY+16*txtSizeY+18*txtIntvY, txtSizeX, txtSizeY); lblStandPipePressure.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+16*txtSizeY+18*txtIntvY, txtLblXsize, txtSizeY);	
		txtChokeP.setBounds(txtSrtX, txtSrtY+17*txtSizeY+19*txtIntvY, txtSizeX, txtSizeY); lblChokePressurePsig.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+17*txtSizeY+19*txtIntvY, txtLblXsize, txtSizeY);
		txtMudLineP.setBounds(txtSrtX, txtSrtY+18*txtSizeY+20*txtIntvY, txtSizeX, txtSizeY); lblPressureAtMud.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+18*txtSizeY+20*txtIntvY, txtLblXsize, txtSizeY);
		txtKickP.setBounds(txtSrtX, txtSrtY+19*txtSizeY+21*txtIntvY, txtSizeX, txtSizeY);  lblPressureAtThe.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+19*txtSizeY+21*txtIntvY, txtLblXsize, txtSizeY);	lblInTheWell.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+20*txtSizeY+21*txtIntvY, txtLblXsize, txtSizeY);
		txtCasingP.setBounds(txtSrtX, txtSrtY+21*txtSizeY+22*txtIntvY, txtSizeX, txtSizeY); lblPsigCasingSeat.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+21*txtSizeY+22*txtIntvY, txtLblXsize, txtSizeY);
		txtBHP.setBounds(txtSrtX, txtSrtY+22*txtSizeY+23*txtIntvY, txtSizeX, txtSizeY); lblPsigBottomholePressure.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+22*txtSizeY+23*txtIntvY, txtLblXsize, txtSizeY);
		txtFormationP.setBounds(txtSrtX, txtSrtY+23*txtSizeY+24*txtIntvY, txtSizeX, txtSizeY); lblPsigFormationPressure.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY+23*txtSizeY+24*txtIntvY, txtLblXsize, txtSizeY);
		
		contentPane.add(pan1);
		pan1.setBounds(pan1Xsrt, pan1Ysrt, panSizeX, panSizeY);
		contentPane.add(pan2);
		pan2.setBounds(pan1Xsrt, pan1Ysrt+panSizeY+yintv , panSizeX, panSizeY);
		contentPane.add(pan3);
				
		for(int i=0; i<5; i++){
			Sgg[i] = new Sgraph();
			contentPane.add(Sgg[i]);
		}		
		Sgg[0].setBounds(pan1Xsrt+panSizeX+10, pan1Ysrt, GraphPnlXsize, GraphPnlYsize);	
		Sgg[1].setBounds(pan1Xsrt+panSizeX+10+GraphPnlXsize+10, pan1Ysrt, GraphPnlXsize, GraphPnlYsize);
		Sgg[2].setBounds(pan1Xsrt+panSizeX+10, pan1Ysrt+GraphPnlYsize+10, GraphPnlXsize, GraphPnlYsize);
		Sgg[3].setBounds(pan1Xsrt+panSizeX+10+GraphPnlXsize+10, pan1Ysrt+GraphPnlYsize+10, GraphPnlXsize, GraphPnlYsize);
		Sgg[4].setBounds(pan1Xsrt+panSizeX+10+GraphPnlXsize+10, pan1Ysrt+GraphPnlYsize+10+GraphPnlYsize+10, GraphPnlXsize, GraphPnlYsize);
		
		Sgg[0].lblTitle = new JLabel("Kill Pump Pressure(psig) vs. Stroke Number");
		Sgg[1].lblTitle = new JLabel("Pump Pressure(psig) vs. Time(min)");
		Sgg[2].lblTitle = new JLabel("Surface Choke Pressure(psig) vs. Time(Min)");
		Sgg[3].lblTitle = new JLabel("Casing Seat Pressure(psig) vs. Time(Min)");
		Sgg[4].lblTitle = new JLabel("Kick Volume in the Annulus (bbls) vs. Time(min)");
		
		for(int i=0; i<5; i++){
			Sgg[i].lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			Sgg[i].lblTitle.setBounds(GraphPnlXsize/2-300/2, 10, 300, 15);
			Sgg[i].add(Sgg[i].lblTitle);	
		}
		//삭제대상
		pan3.setBounds(pan1Xsrt, pan1Ysrt+2*panSizeY+2*yintv , panSizeX, panSizeY);
		
		pan1.setLayout(null);
		pan2.setLayout(null);
		pan3.setLayout(null);
		
		txtPsp.setHorizontalAlignment(SwingConstants.CENTER);
		txtPchoke.setHorizontalAlignment(SwingConstants.CENTER);
		txtRateDiff.setHorizontalAlignment(SwingConstants.CENTER);
		
		pan1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));				
		
		txtRateDiff.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtRateDiff.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtRateDiff.setText("");
		txtRateDiff.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtRateDiff.setEditable(true);
		pan1.add(txtRateDiff);		
		
		MudRRdif.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		MudRRdif.setHorizontalAlignment(SwingConstants.CENTER);
		MudRRdif.setBounds(0, 2, panSizeX, labelFontSize);
		MudRRdif2.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		MudRRdif2.setHorizontalAlignment(SwingConstants.LEFT);
		MudRRdif2.setBounds(0, 2+labelFontSize, panSizeX, labelFontSize+1);
		Mud1.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud1.setHorizontalAlignment(SwingConstants.CENTER);
		Mud1.setBounds(panSizeX/2-Radius-ovalIntv-LineLength-labelFontSize2*3, (panSizeY+2*labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		Mud2.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud2.setHorizontalAlignment(SwingConstants.CENTER);
		Mud2.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+2*labelFontSize)/2-Radius-ovalIntv-LineLength-labelFontSize2, labelFontSize2*4, labelFontSize2);
		Mud3.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud3.setHorizontalAlignment(SwingConstants.CENTER);
		Mud3.setBounds(panSizeX/2+Radius+ovalIntv+LineLength-labelFontSize2, (panSizeY+2*labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		Mud4.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud4.setHorizontalAlignment(SwingConstants.CENTER);
		Mud4.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+2*labelFontSize)/2+Radius+ovalIntv+LineLength, labelFontSize2*4, labelFontSize2);
		
		pan1.add(MudRRdif);
		pan1.add(MudRRdif2);
		pan1.add(Mud1);
		pan1.add(Mud2);
		pan1.add(Mud3);
		pan1.add(Mud4);
		
		
		pan2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		txtPchoke.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtPchoke.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtPchoke.setText("");
		txtPchoke.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtPchoke.setEditable(true);
		pan2.add(txtPchoke);			
		
		CHKPR.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		CHKPR.setHorizontalAlignment(SwingConstants.CENTER);
		CHKPR.setBounds(0, 2, panSizeX, labelFontSize+1);		
		CHK1.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK1.setHorizontalAlignment(SwingConstants.CENTER);
		CHK1.setBounds(panSizeX/2-Radius-ovalIntv-LineLength-labelFontSize2*3, (panSizeY+2*labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		CHK2.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK2.setHorizontalAlignment(SwingConstants.CENTER);
		CHK2.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+2*labelFontSize)/2-Radius-ovalIntv-LineLength-labelFontSize2, labelFontSize2*4, labelFontSize2);
		CHK3.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK3.setHorizontalAlignment(SwingConstants.CENTER);
		CHK3.setBounds(panSizeX/2+Radius+ovalIntv+LineLength-labelFontSize2, (panSizeY+2*labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		CHK4.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK4.setHorizontalAlignment(SwingConstants.CENTER);
		CHK4.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+2*labelFontSize)/2+Radius+ovalIntv+LineLength, labelFontSize2*4, labelFontSize2);
		
		pan2.add(CHKPR);
		pan2.add(CHK1);
		pan2.add(CHK2);
		pan2.add(CHK3);
		pan2.add(CHK4);
		
		
		pan3.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		txtPsp.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtPsp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtPsp.setText("");
		txtPsp.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtPsp.setEditable(true);
		pan3.add(txtPsp);			
		STPPP.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		STPPP.setHorizontalAlignment(SwingConstants.CENTER);
		STPPP.setBounds(0, 2, panSizeX, labelFontSize+1);
		STP1.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP1.setHorizontalAlignment(SwingConstants.CENTER);
		STP1.setBounds(panSizeX/2-Radius-ovalIntv-LineLength-labelFontSize2*3, (panSizeY+2*labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		STP2.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP2.setHorizontalAlignment(SwingConstants.CENTER);
		STP2.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+2*labelFontSize)/2-Radius-ovalIntv-LineLength-labelFontSize2, labelFontSize2*4, labelFontSize2);
		STP3.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP3.setHorizontalAlignment(SwingConstants.CENTER);
		STP3.setBounds(panSizeX/2+Radius+ovalIntv+LineLength-labelFontSize2, (panSizeY+2*labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		STP4.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP4.setHorizontalAlignment(SwingConstants.CENTER);
		STP4.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+2*labelFontSize)/2+Radius+ovalIntv+LineLength, labelFontSize2*4, labelFontSize2);
		
		pan3.add(STPPP);
		pan3.add(STP1);
		pan3.add(STP2);
		pan3.add(STP3);
		pan3.add(STP4);	
		
	}
	
	
		
	
	void PlotActualKillSheet(){
	//----- plot the pump pressure on the top of kill sheet !
	// 7/26/01
	//
		int nData = MainDriver.NwcE - MainDriver.NwcS + 5;
		int npData = Math.max(nData, MainDriver.iDone);
		Sgg[0].ColumnCount = 4;     //Two data set    5 is added on 6/26/97
		Sgg[0].RowCount = npData;       //total number of data
		Sgg[0].ColumnLabelCount = 2;
		//Sgg[0].Plot.Axis(VtChAxisIdY).CategoryScale.Auto = False
		//Sgg[0].Plot.Axis(VtChAxisIdY).ValueScale.Minimum = (int)((fCP - 199.5) / 10) * 10
	    //Sgg[0].Plot.Axis(VtChAxisIdY).ValueScale.Maximum = Int((iCP + 300.5) / 10) * 10
	//		
		Sgg[0].ColumnLabel = "Theoretical"; //이론적인 Kill sheet가 그려지는 부분
	    int icount3 = 0;
	    for(int i = (MainDriver.NwcE + 3); i> (MainDriver.NwcS - 2);i--){
	    	Sgg[0].Row = icount3;    //i - nwcs + 2
	        Sgg[0].Column = 0;
	        Sgg[0].SgxData[Sgg[0].Row] = stks[i];
	        Sgg[0].Column = 1;
	        Sgg[0].SgyData[Sgg[0].Row] = ppks[i] - 14.7;
	        icount3 = icount3 + 1;
	    }
	//
	//--- assign additional data to prevent assigning zero
	    if(MainDriver.iDone > nData){
	    	for(int i = icount3; i<MainDriver.iDone; i++){
	    		Sgg[0].Row = i;
	    		Sgg[0].Column = 0;
	    		Sgg[0].SgxData[Sgg[0].Row] = stks[MainDriver.NwcS - 2];
	    		Sgg[0].Column = 1;
	    		Sgg[0].SgyData[Sgg[0].Row] = fCP - 14.7;  //ppks(nwcs - 1) - 14.7
	    		}
	    	}
	//
	    Sgg[0].Column = 2;
	    Sgg[0].ColumnLabel = "Actual"; //실제적인 Kill sheet가 그려지는 부분
	    Sgg[0].sg2Use=1;
	    for(int i = 0; i<MainDriver.iDone; i++){
	    	Sgg[0].Row = i;
	    	Sgg[0].Column = 2;
	    	Sgg[0].Sgx2Data[Sgg[0].Row] = MainDriver.vKmudSt[i];
	    	Sgg[0].Column = 3;
	    	Sgg[0].Sgy2Data[Sgg[0].Row] = MainDriver.Pcontrol[i] - 14.7;
	    	}
	//--- assign additional data to prevent assigning zero
	    if(MainDriver.iDone < nData){
	    	for(int i = MainDriver.iDone; i<nData; i++){
	    		Sgg[0].Row = i;
	    		Sgg[0].Column = 2;
	    		Sgg[0].Sgx2Data[Sgg[0].Row] = MainDriver.vKmudSt[MainDriver.iDone-1];
	    		Sgg[0].Column = 3;
	    		Sgg[0].Sgy2Data[Sgg[0].Row] = MainDriver.Pcontrol[MainDriver.iDone-1] - 14.7;
	    	}
	    }
	    Sgg[0].calcGraphIntv();
	    Sgg[0].repaint();
	}
	
	
	void PlotG4(){
		double totTsec = 0, totTmin=0;
		int nData =0;
		plotTime = 0;
		if(MainDriver.Np >= 1){
	//
	//----- plot the pump pressure on the top of kill sheet !
		   if (volKmud <= MainDriver.VOLinn + 30.5 && MainDriver.iDone >= 2) PlotActualKillSheet();
	//
	// 7/25/01
		   for(int i = 1; i<5; i++){
			   Sgg[i].RowCount = MainDriver.Np+1;
			   Sgg[i].ColumnCount = 2;
			   }//
		   
		   for(int i = 0; i<MainDriver.Np+1; i++){
			   totTmin = MainDriver.TTsec[i] / 60;
			   Sgg[1].Row = i;    //plot pump pressure
			   Sgg[1].Column = 1; //=> it means x-data 
			   Sgg[1].SgxData[Sgg[1].Row] = totTmin;
			   Sgg[1].Column = 2; //=> it means y-data
			   Sgg[1].SgyData[Sgg[1].Row] = MainDriver.Ppump[i] - 14.7;
					//
			   Sgg[2].Row = i;    //plot pump pressure
			   Sgg[2].Column = 1; //=> it means x-data 
			   Sgg[2].SgxData[Sgg[2].Row] = totTmin;
			   Sgg[2].Column = 2; //=> it means y-data
			   Sgg[2].SgyData[Sgg[2].Row] = MainDriver.Pchk[i] - 14.7;
					//
			   Sgg[3].Row = i;    //plot pump pressure
			   Sgg[3].Column = 1; //=> it means x-data 
			   Sgg[3].SgxData[Sgg[3].Row] = totTmin;
			   Sgg[3].Column = 2; //=> it means y-data
			   Sgg[3].SgyData[Sgg[3].Row] = MainDriver.Pcsg[i] - 14.7;
					//
			   Sgg[4].Row = i;    //plot pump pressure
			   Sgg[4].Column = 1; //=> it means x-data 
			   Sgg[4].SgxData[Sgg[4].Row] = totTmin;
			   Sgg[4].Column = 2; //=> it means y-data
			   Sgg[4].SgyData[Sgg[4].Row] = MainDriver.Vpit[i];
		   }
		   for(int i = 1; i<5; i++){
			   Sgg[i].calcGraphIntv();
			   Sgg[i].repaint();
			   }//
	    //Sgg[1].Plot.UniformAxis = False
	   // Sgg[2].Plot.UniformAxis = False
	   // Sgg[3].Plot.UniformAxis = False
	   // Sgg[4].Plot.UniformAxis = False
	   }	   	   
	}
	
	void ShowResult(){
		double Tsurface=0, visSurface=0, denSurface=0, zSurface=0, gasFraction=0;
		double totHr =0, totMin=0, totSec=0, volMudLine=0;
		//
		MainDriver.volMix =  QtotMix;
		//----------------------------- calculate choke open percentage by Area
		double hgX = 0;
		
		plotTime = (int) (plotTime + MainDriver.gDelT);
	    if ((plotTime + 1) / 60 > grpint || mudCase >= 11) PlotG4();
	//................................... Show the Wellbore and Gauge
	    if (MainDriver.iWshow == 1){
	       if (MainDriver.Method == 2) m3.volkillmud = MainDriver.volPump;       //calculate kill mud volume pumped !			           
	       else m3.volkillmud = volKmud;   //It should be initialize or zero for multiple runs
	       m3.drawOmudIndex=1;
	       m3.drawKmudIndex=1;			       
	       //if (MainDriver.iHresv == 1 || MainDriver.igERD == 1) Call DrawMultiKick
	       //else Call DrawKick(QtotMix, Xb);
	       m3.xDepthWP = MainDriver.Xb;
	       m3.QtotMixWP = QtotMix;
	       m3.drawKickIndex=1;
	       m3.drawWellboreIndex=1;
	       m3.pp.repaint();
	    }
	    
	    if(PVgain < 0.00001 && MainDriver.iCHKcontrol==2){
	    	px = psia;
	    	gasDen = 0;
	    	MainDriver.Xb = 0;
	    	kickVD = 0;
	    }
	    
		if (MainDriver.Xb > 1.5 && kickLoc < 0.1) hgX = MainDriver.Hgnd[MainDriver.N2phase-1];
		if(MainDriver.iCHKcontrol==1){			
			CHKpcent = utilityModule.calcCHKOpen(MainDriver.QtMix, hgX, MainDriver.oMud, gasDen, Pchoke);
			HScrollCHK.setValue((int)(CHKpcent + 0.5001));
			txtCHKopen.setText(Integer.toString((int)(CHKpcent + 0.5001)));
			}
		//..... calculate kick volume in ML and kick influx from ML formation
		if(MainDriver.igERD == 1){
		      /* Call get_KICKinML
		       txtMLpitGain.setText = Format(mlPVgain, ",0.0")
		       For i = 0 To (MainDriver.igMLnumber - 1)
		           txtPbML(i).setText = Format(MainDriver.mlPbeff(i) - 14.7, ",0")
		       Next i*/
		}
		//-----------------------------assign the initial calculation results
		    if (kickLoc < 0) kickLoc = 0;
		    if (MainDriver.Xb < 0) MainDriver.Xb = 0;
		    if (MainDriver.Xb<0.5) PVgain=0;
		    if (kickVD < 0) kickVD = 0;
		    if (kickVDbtm < 0) kickVDbtm = 0;
		    if (MainDriver.Xb < 0.01) MainDriver.QtMix = MainDriver.Qkill;
		//
		    Stroke1=(int)(Stroke1+MainDriver.gDelT/60*MainDriver.spMin1);
			Stroke2=(int)(Stroke2+MainDriver.gDelT/60*MainDriver.spMin2);
			TotalStroke = Stroke1 + Stroke2;
			
		    txtN2phase.setText(Integer.toString(MainDriver.N2phase));
		    txtVDtop.setText((new DecimalFormat("###,##0")).format(kickVD));
		    txtVDbottom.setText((new DecimalFormat("###,##0")).format(kickVDbtm));
		    txtMDkickTop.setText((new DecimalFormat("###,##0")).format(kickLoc));
		    txtMDkickBottom.setText((new DecimalFormat("###,##0")).format(MainDriver.Xb));
		    txtPitGain.setText((new DecimalFormat("##,##0.0")).format(PVgain + mlPVgain));
		    txtVolume.setText((new DecimalFormat("##,##0.0")).format(MainDriver.volPump));
		    txtStroke.setText((new DecimalFormat("###,##0")).format(TotalStroke));
		    //reset_Pump_Stroke = (int)(MainDriver.volPump/MainDriver.Qcapacity)-Now_Pump_Stroke;
		    //Killsheet_Pump_Stroke = (int)(MainDriver.volPump/MainDriver.Qcapacity)-Start_Pump_Stroke;
		    
		    txtKickP.setText((new DecimalFormat("###,##0")).format(Pkick - psia));
		    txtSPP.setText((new DecimalFormat("###,##0")).format(SPP - psia));
		    txtChokeP.setText((new DecimalFormat("###,##0")).format(Pchoke - psia));
		    if(Pchoke < psia) txtChokeP.setText("0");        //7/16/02
		//
		    txtPumpP.setText((new DecimalFormat("###,##0")).format(Math.abs(pumpP - psia)));
		    txtCasingP.setText((new DecimalFormat("###,##0")).format(Pcasing - psia));
		    txtPumpRate.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
		    txtBHP.setText((new DecimalFormat("###,##0")).format(Pbeff - psia));
		    txtFormationP.setText((new DecimalFormat("###,##0")).format(MainDriver.Pform - psia));
		//
		    gasFlowOut = 0;                             //7/17/02
		    if (kickLoc < 0.001 && MainDriver.Xb > 0.5){          //Addition of Mud/Gas flow rate
		    	Tsurface = MainDriver.Tsurf + 460;
		    	GasPropEX = propertyModule.GasProp(Pkick, Tsurface); //visSurface, denSurface, zSurface)
		    	visSurface = GasPropEX[0];
		    	denSurface = GasPropEX[1];
		    	zSurface = GasPropEX[2];
		    	gasFraction = MainDriver.Hgnd[MainDriver.N2phase-1];
		    	mudFlowOut = MainDriver.QtMix * (1 - gasFraction);
		    	gasFlowOut = MainDriver.QtMix * gasFraction;         //gas flow in gpm at MainDriver.Pnd[MainDriver.N2phase) pressure
		    	gasFlowOut = gasFlowOut * Pkick / (14.7 * zSurface) * 60 * 24 / 7.48 * 0.001;   //Mscf/Day
		    }
		    else mudFlowOut = MainDriver.QtMix;
		    
		    txtReturnRate.setText((new DecimalFormat("##,##0")).format(mudFlowOut));
		    txtGasRate.setText((new DecimalFormat("#,###,##0.0#")).format(gasFlowOut));
		//
		    if (kickLoc > MainDriver.Dwater){   //addition of mud line pressure, 7/17/02
		         utilityModule.getDP(MainDriver.QtMix, MainDriver.Dwater, MainDriver.oMud);
		         mudLineP = Pchoke + MainDriver.gMudOld * MainDriver.Dwater + MainDriver.Pcon * MainDriver.DPtop;
		         }
		    else if (kickLoc < MainDriver.Dwater && MainDriver.Xb > MainDriver.Dwater) mudLineP = propertyModule.DitplDes(MainDriver.Dwater);
		    else{    //all kick passes the mud line
		    	volMudLine = MainDriver.volPump;
		    	if(MainDriver.volPump > (MainDriver.VOLinn + MainDriver.VOLout)) {
		//            if MainDriver.Method = 2 Then                //engineer//s MainDriver.Method
		//               volMudLine = MainDriver.VOLinn + MainDriver.VOLout   //kill mud is pumped
		//            else
		//               volMudLine = volKmud           //kll mud is pumped after one circ.
		//            End if
		            if(MainDriver.Method == 1) volMudLine = volKmud;   //this will be good enough !
		         }
		    	Pbtm = utilityModule.pxBottom(volMudLine, MainDriver.Dwater);
		    	mudLineP = Pbtm;
		    	if(Pchoke < psia) mudLineP = Pbtm + psia - Pchoke;
		    }
		    if(MainDriver.iOnshore == 1) mudLineP = Math.max(Pchoke, psia);
		    txtMudLineP.setText((new DecimalFormat("###,##0")).format(mudLineP - psia));
		//------------------------------------------------------------ calculate total circulation time
		    totHr = (int)(MainDriver.gTcum / 3600);
		    if (totHr < 1) totHr = 0;
		    totMin = (int)((MainDriver.gTcum - totHr * 3600) / 60);
		    if (totMin < 1) totMin = 0;
		    totSec = (int)((MainDriver.gTcum - totHr * 3600 - totMin * 60));
		    txtTime.setText(Integer.toString((int)totHr) + ":" + Integer.toString((int)totMin) + ":" + Integer.toString((int)totSec));
		//
		    
		//------------------------------------ update plots and drawings  //7/11/02
		    
		//------------------------------------ assign gauge value, 7/25/01, 7/11/02
		    
			tmpintMud = MainDriver.QtMix; 
			if (tmpintMud > 990) tmpintMud = 990;
			txtRateDiff.setText((new DecimalFormat("##,##0")).format(tmpintMud));//text
			pan1.repaint();    //return rate of mixture
			tmpintCHK = Pchoke - psia;
			if (tmpintCHK > 3960) tmpintCHK = 3960;
			txtPchoke.setText((new DecimalFormat("###,##0")).format(tmpintCHK));
			pan2.repaint();    //surface choke pressure
			tmpintSDP = SPP - psia; 
			if (tmpintSDP > 3960) tmpintSDP = 3960;    //standpipe pressure
			txtPsp.setText((new DecimalFormat("###,##0")).format(tmpintSDP));
			pan3.repaint();
			//
			// ---------- plot the graphs by checking the time in minutes
			if (MainDriver.iWshow == 1 && iDClk == 0 && WPshowStatus ==0){
				m3.setVisible(true);
			    WPshowStatus = 1;
			}
			
			double pcsgchk = Pcasing;
			double pbeffchk = Pbeff;
			if(pcsgchk>32000) pcsgchk = 32000;
			if(pbeffchk>32000) pbeffchk = 32000;
			
			if(propertyModule.FractGrad(pcsgchk, pbeffchk) < 0){     //check possible formation fracture
				if(TimerCirculationOn == 1){
					TimerCirculation.cancel();
					TimerCirculationOn = 0;
					}
				
				if(TimerKickOutOn == 1){
					TimerKickOut.cancel();
					TimerKickOutOn = 0;
					}
				
				if(TimerKillMudOn == 1){
					TimerKillMud.cancel();
					TimerKillMudOn = 0;
					}
				
				if(m3.TimerRotOn==1){
					m3.TimerRot.cancel();
					m3.TimerRotOn=0;
					}
				
				MainDriver.iWshow = 0;
				
				if(TimerBOOn==0){
					TimerCheckBO = new Timer();
					TimerCheckBO.schedule(new CheckBlowoutTask(), 0, 50);
					TimerBOOn=1;
				}
			}
					
			if(timeNp >= timeNpUse) AssignValue();		 
			}
	
	class CheckBlowoutTask extends TimerTask{
		public void run(){
			if(MainDriver.BlowOutOccurred==1){
				setVisible(false);
				MainDriver.iWshow=0;
				m3.setVisible(false);
				menuClose();
				if(TimerBOOn==1){
					TimerBOOn=-1;
					TimerCheckBO.cancel();
					}					
				}
			}
	}
	
	void AssignValue(){
	//.............  Assign the calculated values
		if(Math.abs(MainDriver.TTsec[MainDriver.Np] - MainDriver.gTcum) >= 0.5){
			timeNp = 0;
		    MainDriver.Np = MainDriver.Np + 1;
		    if(kickVD < 0) kickVD = 0;
		    if(kickVDbtm < 0) kickVDbtm = 0;
		//
		    MainDriver.xTop[MainDriver.Np] = kickVD;
		    MainDriver.xBot[MainDriver.Np] = kickVDbtm; 
		    MainDriver.pxTop[MainDriver.Np] = Pkick;
		    MainDriver.rhoK[MainDriver.Np] = gasDen;
		    MainDriver.Psp[MainDriver.Np] = SPP;
		    MainDriver.Pcsg[MainDriver.Np] = Pcasing;
		    MainDriver.Pb2p[MainDriver.Np] = Pbeff;
		    MainDriver.TTsec[MainDriver.Np] = MainDriver.gTcum;
		    MainDriver.Pchk[MainDriver.Np] = Pchoke;
		    MainDriver.Vpit[MainDriver.Np] = PVgain + mlPVgain;
		    MainDriver.QmcfDay[MainDriver.Np] = QgDay;
		    MainDriver.Ppump[MainDriver.Np] = pumpP;    
		    MainDriver.CHKopen[MainDriver.Np] = CHKpcent;
		    MainDriver.VOLcir[MainDriver.Np] = MainDriver.volPump; 
		    MainDriver.StrokePump1[MainDriver.Np] = Stroke1;
		    MainDriver.StrokePump2[MainDriver.Np] = Stroke2;
		    MainDriver.Stroke[MainDriver.Np] = TotalStroke;
		//
		//............. adjust wellbore pressures in surface choke pressure is negative, 7/11/02
		    MainDriver.gasflow[MainDriver.Np] = gasFlowOut;    //addition of variables, 7/17/02
		    MainDriver.mudflow[MainDriver.Np] = mudFlowOut;
		    MainDriver.PmLine[MainDriver.Np] = mudLineP;
		    
		    if(Pchoke < psia){       //negative choke pressure adjustment
		       MainDriver.Pchk[MainDriver.Np] =  psia; 
		       MainDriver.Psp[MainDriver.Np] =  SPP + psia - Pchoke;
		       MainDriver.Pcsg[MainDriver.Np] = Pcasing + psia - Pchoke;
		       MainDriver.Pb2p[MainDriver.Np] = Pbeff + psia - Pchoke;
		       MainDriver.Ppump[MainDriver.Np] = pumpP + psia - Pchoke;
		    }
		    
		 
		    //--- check the possible out of array
		    if(MainDriver.Np >= MainDriver.Npt - 2){
		    	String msg = "You spent too much time.  Arrays are out of Range !";
		    	JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
		       this.dispose();
		    }
		    
		}
	}	
	
	double calc_AnnulusVolBelow(double xLoc){
	//.....  sub to calculate annulus volume based on the given location.
	//  this is generally good and needed for kick addition form ML
	// xloc;ft, the given location,  volBelow;bbls, total volume in annulus
	//
		double capcty=0, Qflow=0;
		double result=0, volSum = 0, Htmp=0;
		int iPos=0;
	    iPos = utilityModule.Xposition(xLoc);   //identify the location of the given x
	//.............................. calculate MD by the given volume, bbls
	    Htmp = MainDriver.TMD[iPos] - xLoc;
	    for(int i = iPos; i> MainDriver.NwcS-2; i--){    // do 44 i
	     capcty = MainDriver.C12 * (Math.pow(MainDriver.Do2p[i + 1], 2) - Math.pow(MainDriver.Di2p[i + 1] , 2));
	//............................. inside marine riser or choke/kill line
	     if (i == 8){
	    	 getLinesEX = utilityModule.getLines(Qflow);//// 20130802 ajw : index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
	    	 capcty = getLinesEX[1];
	     }
	     volSum = volSum + capcty * Htmp;
	     Htmp = MainDriver.TMD[i - 1] - MainDriver.TMD[i];
	    }
	    result = volSum;
	    return result;
	}
	
	int get_KickGridNumber(double xLoc){
	//... sub to find grid cell number for KICK addition from ML, //7/30/2003
	//         MainDriver.Xnd[iLoc)< xLoc < MainDriver.Xnd[iLoc-1)
		int iLoc=0;
		for(int ii = (MainDriver.N2phase - 2); ii>-1; ii--){
	         if(xLoc < MainDriver.Xnd[ii]){
	            iLoc = ii + 1;
	            return iLoc;
	         }
	     }
	     iLoc = 1;
	     return iLoc;
	}

	
	void AddKickML(int iML, double volAdd){
	//.... sub to add kick volume form ML to main path,  //7/30/2003
		int N2phaseOld=0, iLoc=0, iPos=0, NcellAdd=0;
		double mudAdd=0, mudHt=0, tempML=0, mlGasVis=0, mlGasDen=0, mlZz=0, xbottom=0;
		double[] gasPEX = new double[3];
		double volBelow =0, volBelow2=0;
		
	    N2phaseOld = MainDriver.N2phase;
	    tempML = utilityModule.temperature(MainDriver.mlTVD[iML]);
	    gasPEX = propertyModule.GasProp(MainDriver.mlPform[iML], tempML);//, mlGasVis, mlGasDen, mlZz)//  0 : vis, 1 : den, 2:zz
	    mlGasVis = gasPEX[0];
	    mlGasDen = gasPEX[1];
	    mlZz = gasPEX[2];
	    
	    if(MainDriver.mlKOP[iML] < kickLoc){
	       volBelow = calc_AnnulusVolBelow(MainDriver.mlKOP[iML]);
	       volBelow2 = calc_AnnulusVolBelow(kickLoc);
	       MainDriver.volL[N2phaseOld] = volBelow - volBelow2;   //gas kick and mud volume in bbls
	       MainDriver.pvtZb[N2phaseOld] = 0;
	       mudAdd = MainDriver.QtMix * MainDriver.gDelT / 2520;  //=42*60
	       MainDriver.volL[N2phaseOld + 1] = mudAdd + volAdd * (1 - mlHgeff[iML]);
	       MainDriver.pvtZb[N2phaseOld + 1] = volAdd * mlHgeff[iML] * MainDriver.mlPform[iML] / (mlZz * tempML);
	       MainDriver.N2phase = N2phaseOld + 2;
	       iMLkickLoc = 1;
	//2/1/2004
	//        For kick top calculation, we have to consider kick mixture flow in mainbore
	       iPos = utilityModule.Xposition(MainDriver.mlKOP[iML]);
	       mudHt = 1029.4 * (mudAdd + volAdd) / (Math.pow(MainDriver.Do2p[iPos + 1], 2) - Math.pow(MainDriver.Di2p[iPos], 2));
	       kickLoc = MainDriver.mlKOP[iML] - mudHt;
	    }
	    else if(MainDriver.mlKOP[iML] < MainDriver.Xb) {
	    	iLoc = get_KickGridNumber(MainDriver.mlKOP[iML]);
	//---
	    	if (N2phaseOld < 10) {  //just use 10 mixture cells at maximum
	    		for(int i = MainDriver.N2phase-1; i>iLoc-1; i--){   //shift array by 1 for new kick !
	    			MainDriver.Xnd[i + 1] = MainDriver.Xnd[i]; MainDriver.Hgnd[i + 1] = MainDriver.Hgnd[i];
	    			MainDriver.volL[i + 1] = MainDriver.volL[i]; MainDriver.volG[i + 1] = MainDriver.volG[i];
	    			MainDriver.Pnd[i + 1] = MainDriver.Pnd[i];  MainDriver.pvtZb[i + 1] = MainDriver.pvtZb[i];
	    		}
	    		MainDriver.volL[iLoc] = volAdd * (1 - mlHgeff[iML]);
	    		MainDriver.pvtZb[iLoc] = volAdd * mlHgeff[iML] * MainDriver.mlPform[iML] / (mlZz * tempML);
	    		MainDriver.N2phase = N2phaseOld + 1;
	       }
	       else{
	// 1/31/2004
	//          just add gas and mud volume if grid number is more than 20
	    	   MainDriver.volL[iLoc] = MainDriver.volL[iLoc] + volAdd * (1 - mlHgeff[iML]);
	    	   MainDriver.pvtZb[iLoc] = MainDriver.pvtZb[iLoc] + volAdd * mlHgeff[iML] * MainDriver.mlPform[iML] / (mlZz * tempML);	         
	       }
	    }
	    else{
	    	volBelow2 = calc_AnnulusVolBelow(MainDriver.Xb);
	    	volBelow = calc_AnnulusVolBelow(MainDriver.mlKOP[iML]);
	//2/1/2004
	//        For kick bottom, we have to consider kick mixture flow in mainbore
	//        We also need to think about, mud volume below Xb to mlKOP
	//
	    	mudAdd = MainDriver.QtMix * MainDriver.gDelT / 2520;  //=42*60
	    	NcellAdd = 2;   //more than 0.95 bbls diffeMainDriver.RenCe
	    	if ((volBelow2 - volBelow - mudAdd) < 0.95) NcellAdd = 1;
	    	for(int i = MainDriver.N2phase + NcellAdd-1; i>1; i--){  //shift array by 1 or 2 for new kick !
	           MainDriver.Xnd[i] = MainDriver.Xnd[i - NcellAdd]; MainDriver.Hgnd[i] = MainDriver.Hgnd[i - NcellAdd];
	           MainDriver.volL[i] = MainDriver.volL[i - NcellAdd]; MainDriver.volG[i] = MainDriver.volG[i - NcellAdd];
	           MainDriver.Pnd[i] = MainDriver.Pnd[i - NcellAdd]; MainDriver.pvtZb[i] = MainDriver.pvtZb[i - NcellAdd];
	    	}
	       if(NcellAdd == 2){
	          MainDriver.volL[2] = volBelow2 - volBelow - mudAdd;
	          MainDriver.pvtZb[2] = 0;
	       }
	       MainDriver.Xnd[1] = MainDriver.mlKOP[iML];
	       MainDriver.volL[1] = mudAdd + volAdd * (1 - mlHgeff[iML]);
	       MainDriver.pvtZb[1] = volAdd * mlHgeff[iML] * MainDriver.mlPform[iML] / (mlZz * tempML);
	//--- assign the bottom value
	       MainDriver.Xnd[0] = xbottom; 
	       MainDriver.Hgnd[0] = 0; MainDriver.volL[0] = 0;
	       MainDriver.volG[0] = 0; MainDriver.Pnd[0] =  Pbeff;  MainDriver.pvtZb[0] = 0;
	       if(MainDriver.Xb < MainDriver.mlKOP[iML]) MainDriver.Xb = MainDriver.mlKOP[iML];        //new kickLoc is determined in timerKickOut()
	       if(N2phaseOld == 1) kickLoc = MainDriver.mlKOP[iML];    //influx after kick removal, 1/31/04
	       MainDriver.N2phase = N2phaseOld + 2;
	    }
	    operationMsg.setText("Kicks from Multilateral !");
	}
	
	void menuClose(){
		 if(TimerCirculationOn == 1){
			   TimerCirculation.cancel();
			   TimerCirculationOn = 0;
		   }
		   
		   if(TimerKickOutOn == 1){
			   TimerKickOut.cancel();
			   TimerKickOutOn = 0;
		   }
		   
		   if(TimerKillMudOn == 1){
			   TimerKillMud.cancel();
			   TimerKillMudOn = 0;
		   }
		   if(m3.TimerRotOn==1){
				m3.TimerRot.cancel();
				m3.TimerRotOn=0;
			}
		   
		   MainDriver.AutoOn=0;
		   this.dispose();
		   this.setVisible(false);
	}
	
	int calc_KickSlipML(double timeAdd){
	//.... sub to calculate kick migration velocity in ML, 7/30/2003
	//     timeAdd; sec, time duration
		int ic=0;
		double mlKickVol=0, xNull=0, vSlip=0, vGas=0, volAdd=0;
		double PchokeTmp=0, volBelowKick=0,  delMD=0, delVD=0, angTop=0, mlPtop=0, tempTop=0,  mlGasVis=0, mlGasDen=0, mlZz=0, mlSurfTen=0, mlHg=0, MscfAdd=0;
		double[] mlDelVDex = new double[3];
		double[] slipShutinEX = new double[2];
	//
		if(MainDriver.igERD != 1) return 0;
		//
		for(int ii = 0; ii<(MainDriver.igMLnumber - 1); ii++){         //ML pressure modification
	       if(MainDriver.mlPlug[ii] == 1) return 0;  //ML is plugged //44065  dummy = 0
	       mlKickVol = MainDriver.mlQgTotM[ii] * MainDriver.mlBg[ii] * 1000;
	       if(mlKickVol < 0.05) return 0;  //ignore for small gain
	       mlKickVol =  (mlKickVol / mlHgeff[ii]);	            
	       
	       PchokeTmp = Math.max(Pchoke, 14.7);
	       if(kickVD > MainDriver.mlKOPvd[ii]) MainDriver.mlPkop[ii] =  (PchokeTmp + MainDriver.gMudOld * MainDriver.mlKOPvd[ii]); //p. at KOP of each ML, w/o DP_friction loss
	       else MainDriver.mlPkop[ii] =  (PchokeTmp + MainDriver.gMudOld * kickVD + 0.052 * gasDen * (MainDriver.mlKOPvd[ii] - kickVD));
	       volBelowKick = mlKickTopMD[ii] * Math.pow(MainDriver.mlDia[ii], 2) / 1029.4;
	       ic = ii;
	       mlDelVDex = simdis.get_mlDelVD(ic, volBelowKick);//, delMD, delVD, angTop)//  0 : delMD, 1: delVD, 2: angleTop
	       delMD = mlDelVDex[0];
	       delVD = mlDelVDex[1];
	       angTop = mlDelVDex[2];
	       mlPtop = MainDriver.mlPkop[ii] + MainDriver.gMudOld * (MainDriver.mlTVD[ii] - MainDriver.mlKOPvd[ii] - delVD);
	       tempTop = utilityModule.temperature((MainDriver.mlTVD[ii] - delVD));
	       GasPropEX = propertyModule.GasProp(mlPtop, tempTop); // mlGasVis, mlGasDen, mlZz//  0 : vis, 1 : den, 2:zz
	       mlGasVis = GasPropEX[0];
	       mlGasDen = GasPropEX[1];
	       mlZz = GasPropEX[2];
	       mlSurfTen = propertyModule.surfT(mlPtop, tempTop);
	       xNull = 0;
	//       mlHg = Min(0.45, mlHgeff[ii])  //assuming slug flow
	       mlHg = mlHgeff[ii];   //use it//s value itself, 1/31/04
	       
	       slipShutinEX = utilityModule.slipShutin(MainDriver.mlDia[ii], xNull, MainDriver.oMud, mlGasDen, mlSurfTen, angTop, mlHg, xNull);//, vSlip, vGas)
	       vSlip = slipShutinEX[0];
	       vGas = slipShutinEX[1];
	       mlKickTopMD[ii] = mlKickTopMD[ii] + vSlip * timeAdd;
	       volAdd =  ((mlKickTopMD[ii] - MainDriver.mlMD[ii]) * Math.pow(MainDriver.mlDia[ii], 2) / 1029.4);
	       if(volAdd > 0.02){   //2/1/2004
	//       if mlKickTopMD[ii] > MainDriver.mlMD[ii] Then    //kick enters into main path
	//          volAdd = (mlKickTopMD[ii] - MainDriver.mlMD[ii]) * MainDriver.mlDia[ii] ^ 2 / 1029.4
	          if(volAdd > mlKickVol) volAdd = mlKickVol;
	          MscfAdd = MainDriver.mlQgTotM[ii] * volAdd / mlKickVol;
	          iMLflux = 1;   //kick addition from ML to main path (MP)
	          AddKickML(ic, volAdd);
	          MainDriver.mlQgTotM[ii] =  (MainDriver.mlQgTotM[ii] - MscfAdd);
	          mlKickTopMD[ii] = MainDriver.mlMD[ii];
	       }
		}
		return 1;
	}
	
	void PbCalc_circ(){
	//  Calculates B.H.P. when annulus pressure is given. - new AWC version
	//  No Slip and Top properties as an average properties for Simplicity !
		double avgden=0, ql=0, Qg=0;
		double volTot =0, voltmp=0, HgCal=0;
		double zx=0, hxt=0, dpmud=0, dpfri=0, DPdiff=0;
	//.................................... assign the top cell conditions
	    double xNull = 0, xxx=0;	    
	    double pxcell = Pkick; 
	    double xcell = kickLoc, xbcell=0, xbvdcell=0;
	    double xvdcell=utilityModule.getVD(xcell);
	    double surfTen = 0;
	    double tx2=0, wkbtmp=0;
	    
	    MainDriver.tx = utilityModule.temperature(xvdcell);
	    surfTen = propertyModule.surfT(MainDriver.pxTop[MainDriver.Np - 1], MainDriver.tx);
	//
	    PVgain = 0;
	    QtotMix = 0;
	    for(int i = MainDriver.N2phase-1; i>0; i--){
	    	xvdcell=utilityModule.getVD(xcell);
	    	MainDriver.tx = utilityModule.temperature(xvdcell);
	        GasPropEX = propertyModule.GasProp(pxcell, MainDriver.tx);//, gasVis, gasDen, zx)
	        gasVis = GasPropEX[0];
	        gasDen = GasPropEX[1];
	        zx = GasPropEX[2];
	        MainDriver.R = 10.73;
	        if(MainDriver.volL[i] == 0){
	          //volL(i) = VolL_default
	        	MainDriver.volL[i] = MainDriver.delta_T[i] * MainDriver.volL[MainDriver.N2phase-1] / MainDriver.delta_T[MainDriver.N2phase-1]; //assign liq. volume for the gas influx during stabilized period
	        }
	        //------------------------------------------------------------------------------------ OBM case
	        if(MainDriver.imud == 1){ //OBM case
	        	tx2 = MainDriver.tx - 460; //tx: rankine / tx2: fahrenheit
	        	//Call calcRs(pxcell, tx, 0.554, Rs, Rsk, RsM, Rsn, ibaseoil) //gas solubility calc. by O//bryan//s
	        	MainDriver.Rs = utilityModule.calcRs2(pxcell, tx2); //gas solubility calc. by PVTi	//
	        	if(MainDriver.Rs < 0) MainDriver.Rs = 0;
	        	
	        	if(MainDriver.volGold[i] == 0) MainDriver.volGold[i] = 0.001;
	        	wkbtmp = 42 * gasDen * MainDriver.volGold[i];
	        	//
	        	MainDriver.Rs = MainDriver.Rs * MainDriver.foil; //fractional solubility
	        	//
	        	MainDriver.PVTZ_free[i] = MainDriver.volL[i] * (MainDriver.gor[i] - MainDriver.Rs) * 0.0417 / 16.04; //freegas mole
	        	if(MainDriver.PVTZ_free[i] < 0) MainDriver.PVTZ_free[i] = 0;
	        	
	        	MainDriver.PVTZ_sol[i] = MainDriver.PVTZ_Gas[i] - MainDriver.PVTZ_free[i]; //solution gas mole
	        	//
	        	//.................................. calculate the pressure at the top
	        	MainDriver.volkx = MainDriver.PVTZ_free[i] * MainDriver.tx * zx * MainDriver.R / pxcell; //pit gain by free gas volume(ft^3)
	        	MainDriver.volkx = MainDriver.volkx / 5.615; //pit gain by free gas volume(bbls)
	        	
	        	MainDriver.OBMmole[i] = (MainDriver.volL[i] * MainDriver.foil) * MainDriver.OBMdensity * 42 / MainDriver.OBMwt;
		        MainDriver.OBMFrac[i] = (MainDriver.OBMmole[i] / (MainDriver.PVTZ_sol[i] + MainDriver.OBMmole[i])) / 100;
		        MainDriver.GasFrac[i] = (0.01 - MainDriver.OBMFrac[i]) * 100;
		        //
		        MainDriver.OBMFr = MainDriver.OBMFrac[i];
		        MainDriver.GasFr = MainDriver.GasFrac[i];
		        //
		        MainDriver.mole_solgas = MainDriver.PVTZ_sol[i];
		        MainDriver.mole_OBM = MainDriver.OBMmole[i];
		        //
		        MainDriver.V_cont = utilityModule.PREOS(pxcell, MainDriver.tx, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);
		        MainDriver.V_cont_ref=utilityModule.PREOS(pxcell, MainDriver.tx, 0, 0.01, 0, MainDriver.mole_OBM);
		        //
		        MainDriver.Vsol[i] = MainDriver.V_cont - MainDriver.V_cont_ref;
		        //MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.VolL[i] * MainDriver.foil / MainDriver.V_cont_ref
		        MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.volL[i] * MainDriver.foil / MainDriver.V_cont_ref;
		        //
		        MainDriver.Den_tmp = (MainDriver.oMud * MainDriver.volL[i] * 42 + MainDriver.solgasmole[i] * 16.04) / (MainDriver.volL[i] + MainDriver.Vsol[i]) / 42;
		        MainDriver.Dendiff[i] = MainDriver.Den_tmp - MainDriver.oMud; //density change
		        //
		        if(MainDriver.Dendiff[i] > 0) MainDriver.Dendiff[i] = 0;
	        } //------------------------------------------------------------------------------------ OBM case
	        else{ //WBM case
	        	MainDriver.volkx = MainDriver.pvtZb[i] * MainDriver.tx * zx / pxcell;
	        	MainDriver.Dendiff[i]=0;
	        	}
	        //---------------------------------------------------------------------------------------------------Applying PREOS
	        /*MainDriver.OBMmole[i] = (MainDriver.volL[i] * MainDriver.foil) * MainDriver.OBMdensity * 42 / MainDriver.OBMwt;
	        MainDriver.OBMFrac[i] = (MainDriver.OBMmole[i] / (MainDriver.PVTZ_sol[i] + MainDriver.OBMmole[i])) / 100;
	        MainDriver.GasFrac[i] = (0.01 - MainDriver.OBMFrac[i]) * 100;
	        //
	        MainDriver.OBMFr = MainDriver.OBMFrac[i];
	        MainDriver.GasFr = MainDriver.GasFrac[i];
	        //
	        MainDriver.mole_solgas = MainDriver.PVTZ_sol[i];
	        MainDriver.mole_OBM = MainDriver.OBMmole[i];
	        //
	        MainDriver.V_cont = utilityModule.PREOS(pxcell, MainDriver.tx, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);
	        MainDriver.V_cont_ref=utilityModule.PREOS(pxcell, MainDriver.tx, 0, 0.01, 0, MainDriver.mole_OBM);
	        //
	        MainDriver.Vsol[i] = MainDriver.V_cont - MainDriver.V_cont_ref;
	        //MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.VolL[i] * MainDriver.foil / MainDriver.V_cont_ref
	        MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.volL[i] * MainDriver.foil / MainDriver.V_cont;
	        //
	        MainDriver.Den_tmp = (MainDriver.oMud * MainDriver.volL[i] * 42 + MainDriver.solgasmole[i] * 16.04) / (MainDriver.volL[i] + MainDriver.Vsol[i]) / 42;
	        MainDriver.Dendiff[i] = MainDriver.Den_tmp - MainDriver.oMud; //density change
	        //
	        if(MainDriver.Dendiff[i] > 0) MainDriver.Dendiff[i] = 0;*/ // moved by jw, 20140206
	        //
	        voltmp = MainDriver.volL[i] + MainDriver.Vsol[i];
	        volTot = MainDriver.volkx + voltmp;
	        //---------------------------------------------------------------------------------------------------Applying PREOS
	        //.... if there are kicks from ML to MP(iMLflux = 1), ignore the restriction on max Hg
	        //     at the mixture front. this is also good for bottom kick loc. 2003/8/1
	        if(volTot < 0.00001) volTot = 0.00001;
	        HgCal = MainDriver.volkx / volTot;
	        if (HgCal >= 0.0001 && iMLflux != 1){ //Then GoTo 6789
	        	//................. set the constant gas fraction at the top ie. less than 0.45
	        	if (i > MainDriver.N2phase - MainDriver.nHgCon-1 && MainDriver.iHgMax == 1 && HgCal > 0.45){
	        		HgCal = MainDriver.HgndOld[i];
	        		if(HgCal < 0.00002) HgCal = 0.45;   //temperarily set 0.45 as maximum
	        		voltmp =MainDriver.volkx * (1 - HgCal) / HgCal;
	        		volTot = MainDriver.volkx + voltmp;
	        		}
	        	//...................................... set the maximum gas fraction
	        	if (HgCal > MainDriver.HgMax && MainDriver.iHgMax == 1){
	        		HgCal = MainDriver.HgMax;
	        		voltmp = MainDriver.volkx * (1 - MainDriver.HgMax) / MainDriver.HgMax;
	        		volTot = MainDriver.volkx + voltmp;
	        		}
	        	}
	        //6789 dummy = 0   //simple continue -------------------------------------------
	        hxt = utilityModule.getTopH(volTot, xcell);
	        xbcell = xcell + hxt;
	        xbvdcell = utilityModule.getVD(xbcell);
	        //Modified by Ty
	        avgden = (MainDriver.oMud + MainDriver.Dendiff[i]) * (1 - HgCal) + gasDen * HgCal; //density change due to low-density gas dissolution
	        dpmud = 0.052 * avgden * (xbvdcell - xvdcell);
	        ql = MainDriver.QtMix * voltmp / volTot;  //Q liquid
	        Qg = MainDriver.QtMix * MainDriver.volkx / volTot; // Q gas
	        utilityModule.get2pDP(xbcell, ql, Qg, HgCal, MainDriver.oMud + MainDriver.Dendiff[i], gasDen, gasVis);
	        dpfri = MainDriver.DPtop;
	        utilityModule.get2pDP(xcell, ql, Qg, HgCal, MainDriver.oMud + MainDriver.Dendiff[i], gasDen, gasVis);
	        dpfri = dpfri - MainDriver.DPtop;   // DP-fric in mixed zone
	        //............................................. assign calculated value
	        MainDriver.Pnd[i] =  pxcell;
	        MainDriver.Xnd[i] =  xcell;
	        MainDriver.volG[i] =  MainDriver.volkx;
	        volLqd[i] = voltmp;
	        MainDriver.Hgnd[i] =  HgCal;
	        pxcell = pxcell + dpmud + dpfri * MainDriver.Pcon;
	        xcell = xbcell;
	        PVgain = PVgain + MainDriver.volkx+MainDriver.Vsol[i]; 
	        QtotMix = QtotMix + volTot;
	        //
	        //56789 dummy = 0
	        }
	    MainDriver.Xnd[0] =  xbcell;
	    MainDriver.Pnd[0] =  pxcell;
	    xxx = MainDriver.TMD[MainDriver.NwcS-1];
	    MainDriver.Xb =  Math.min(xbcell, xxx);
	    Pbtm = utilityModule.pxBottom(MainDriver.volPump, MainDriver.Xb);
	    DPdiff = MainDriver.Pb - Pbtm; 
	    PbTry = pxcell + DPdiff;
	    if (Math.abs(MainDriver.Pb - PbTry) < 50.5) MainDriver.iHgMax = 1;
	}
	
	void PbCalc(){
	//  Calculates B.H.P. when annulus pressure is given. - new AWC version
	//  No Slip and Top properties as an average properties for Simplicity !
		double avgden=0, ql=0, Qg=0;
		double volTot =0, voltmp=0, HgCal=0;
		double zx=0, hxt=0, dpmud=0, dpfri=0, DPdiff=0;
	//.................................... assign the top cell conditions
	    double xNull = 0, xxx=0;	    
	    double pxcell = Pkick; 
	    double xcell = kickLoc, xbcell=0, xbvdcell=0;
	    double xvdcell=utilityModule.getVD(xcell);	    
	    double tx = utilityModule.temperature(xvdcell);
	    double surfTen = propertyModule.surfT(MainDriver.pxTop[MainDriver.Np - 1], tx);
	//
	    PVgain = 0;
	    QtotMix = 0;
	    for(int i = MainDriver.N2phase-1; i>0; i--){
	    	xvdcell=utilityModule.getVD(xcell);
	    	tx = utilityModule.temperature(xvdcell);
	        GasPropEX = propertyModule.GasProp(pxcell, tx);//, gasVis, gasDen, zx)
	        gasVis = GasPropEX[0];
	        gasDen = GasPropEX[1];
	        zx = GasPropEX[2];
	//.................................. calculate the pressure at the top
	        //Modified by TY
	        MainDriver.R = 10.73;
	        if(MainDriver.imud == 0) MainDriver.volkx = MainDriver.pvtZb[i]*tx*zx/pxcell;
	        else MainDriver.volkx = MainDriver.PVTZ_free[i]*tx*zx*MainDriver.R/pxcell/5.615;
	        
	        voltmp = MainDriver.volL[i];
	        volTot = MainDriver.volkx + voltmp;
	//
	//.... if there are kicks from ML to MP(iMLflux = 1), ignore the restriction on max Hg
	//     at the mixture front. this is also good for bottom kick loc. 2003/8/1
	       if(volTot < 0.00001) volTot = 0.00001;
	       HgCal = MainDriver.volkx / volTot;
	       if (HgCal >= 0.0001 && iMLflux != 1){// Then GoTo 6789
	    	   //................. set the constant gas fraction at the top ie. less than 0.45
	    	   if (i > MainDriver.N2phase - MainDriver.nHgCon-1 && MainDriver.iHgMax == 1 && HgCal > 0.45){
		          HgCal = MainDriver.HgndOld[i];
		          if(HgCal < 0.00002) HgCal = 0.45;   //temperarily set 0.45 as maximum
		          voltmp = MainDriver.volkx * (1 - HgCal) / HgCal;
		          volTot = MainDriver.volkx + voltmp;
	    	   }
		//...................................... set the maximum gas fraction
		       if (HgCal > MainDriver.HgMax && MainDriver.iHgMax == 1){
		    	   HgCal = MainDriver.HgMax;
		    	   voltmp = MainDriver.volkx * (1 - MainDriver.HgMax) / MainDriver.HgMax;
		    	   volTot = MainDriver.volkx + voltmp;
		       }
	       }
	//6789 dummy = 0   //simple continue -------------------------------------------
	       hxt =  utilityModule.getTopH(volTot, xcell);
	       xbcell = xcell + hxt; // xcell : top location of the cell, xbcell : bottom location of the cell
	       xbvdcell=utilityModule.getVD(xbcell);
	       avgden = gasDen * HgCal + MainDriver.oMud * (1 - HgCal);
	       dpmud = 0.052 * avgden * (xbvdcell - xvdcell);
	       ql = MainDriver.QtMix * voltmp / volTot;  //Q liquid
	       Qg = MainDriver.QtMix * MainDriver.volkx / volTot; // Q gas
	       utilityModule.get2pDP(xbcell, ql, Qg, HgCal, MainDriver.oMud, gasDen, gasVis);
	       dpfri = MainDriver.DPtop; //dp friction
	       utilityModule.get2pDP(xcell, ql, Qg, HgCal, MainDriver.oMud, gasDen, gasVis);
	       dpfri = dpfri - MainDriver.DPtop;   // DP-fric in mixed zone
	//............................................. assign calculated value
	       MainDriver.Pnd[i] =  pxcell;
	       MainDriver.Xnd[i] =  xcell;
	       MainDriver.volG[i] =  MainDriver.volkx;
	       volLqd[i] = voltmp; 
	       MainDriver.Hgnd[i] =  HgCal;
	       pxcell = pxcell + dpmud + dpfri * MainDriver.Pcon;
	       xcell = xbcell;
	       PVgain = PVgain + MainDriver.volkx; 
	       QtotMix = QtotMix + volTot;
	       }
	    MainDriver.Xnd[0] =  xbcell;
	    MainDriver.Pnd[0] =  pxcell;
	    xxx = MainDriver.TMD[MainDriver.NwcS-1];
	    MainDriver.Xb =  Math.min(xbcell, xxx);
	    Pbtm = utilityModule.pxBottom(MainDriver.volPump, MainDriver.Xb);
	    DPdiff = MainDriver.Pb - Pbtm; 
	    PbTry = pxcell + DPdiff;
	    if (Math.abs(MainDriver.Pb - PbTry) < 50.5) MainDriver.iHgMax = 1;
	//
	}
	
	void Pbcal2(){
	/*Dim iXb As Integer
	//  Calculates B.H.P. when annulus pressure is given. - new AWC version
	//  No Slip and Top properties as an average properties for Simplicity !
	//.................................... assign the top cell conditions
	//Modified by TY
	    xNull = 0#: pxcell = px: xcell = X
	    xvdcell = xVert: tx = temperature(xvdcell)
	    surfTen = surfT(px, tx)
	//
	    PVgain = 0#: QtotMix = 0#: volkkmix = 0#: iXb = N2phase
	    
	    For i = N2phase To 2 Step -1
	    
	       Call getVD(xcell, xvdcell)
	       tx = temperature(xvdcell)
	       Call GasProp(pxcell, tx, gasVis, gasDen, zx)
	    
	       If volL(i) = 0 Then
	           //volL(i) = VolL_default
	           //volL(i) = volL(N2phase)
	           volL(i) = delta_T(i) * volL(N2phase) / delta_T(N2phase)
	       End If
	    If imud = 1 Then //OBM case
	       R = 10.73 //Universal gas constant
	       tx2 = tx - 460
	       //Call calcRs(pxcell, tx, 0.5537, Rs, Rsk, RsM, Rsn, ibaseoil) //gas solubility calc. by O//bryan//s
	       Call calcRs2(pxcell, tx2, ibaseoil, Rs) //gas solubility calc. by PVTi
	       If Rs < 0 Then
	          Rs = 0
	       End If
	            If volGold(i) = 0 Then volGold(i) = 0.001
	            wkbtmp = 42# * gasDen * volGold(i)
	//
	       Rs = Rs * foil //fractional solubility
	//
	       PVTZ_free(i) = volL(i) * (gor(i) - Rs) * 0.0417 / 16.04 //freegas mole
	//
	               If PVTZ_free(i) < 0 Then
	                    PVTZ_free(i) = 0
	               End If
	//
	       PVTZ_sol(i) = PVTZ_Gas(i) - PVTZ_free(i) //solution gas mole
	       //.................................. calculate the pressure at the top
	       MainDriver.volkx = PVTZ_free(i) * tx * zx * R / pxcell
	       MainDriver.volkx = MainDriver.volkx / 5.615
	//---------------------------------------------------------------------------------------------------Applying PREOS
	       OBMmole(i) = (volL(i) * foil) * OBMdensity * 42 / OBMwt
	//
	       OBMFrac(i) = (OBMmole(i) / ((PVTZ_sol(i)) + OBMmole(i))) / 100
	       GasFrac(i) = (0.01 - OBMFrac(i)) * 100
	//
	       OBMFr = OBMFrac(i)
	       GasFr = GasFrac(i)
	//
	       mole_solgas = PVTZ_sol(i)
	       mole_OBM = OBMmole(i)
	//
	       Call PREOS(ibaseoil, pxcell, tx, GasFr, OBMFr, mole_solgas, mole_OBM, V_cont)
	       Call PREOS(ibaseoil, pxcell, tx, 0, 0.01, 0, mole_OBM, V_cont_ref)
	//
	       Vsol(i) = (V_cont - V_cont_ref)
	       Vsol(i) = Vsol(i) * volL(i) * foil / V_cont
	//
	       Den_tmp = (oMud * volL(i) * 42 + PVTZ_sol(i) * 16.04) / (volL(i) + Vsol(i)) / 42
	       Dendiff(i) = Den_tmp - oMud //density change
	          If Dendiff(i) > 0 Then
	              Dendiff(i) = 0
	          End If
	//---------------------------------------------------------------------------------------------------Applying PREOS
	    Else //WBM case
	       MainDriver.volkx = pvtZb(i) * tx * zx / pxcell
	    End If
	//
	    voltmp = volL(i) + Vsol(i)
	       
	    volTot = MainDriver.volkx + voltmp
	//
	       If MainDriver.volkx > 0.001 Then iXb = i - 1
	       volG(i) = MainDriver.volkx
	       //voltmp = volL(i): volTot = MainDriver.volkx + voltmp
	//       If (voltot < .00001 and i <> n2phase) Then
	//          hgcal = 0#
	//          pnd(i) = pnd(i + 1): xnd(i) = xnd(i + 1)
	//          vollqd(i) = voltmp: hgnd(i) = hgcal
	//          GoTo 56789
	//       End If
	       If (volTot < 0.00001) Then volTot = 0.00001
	       HgCal = MainDriver.volkx / volTot
	       If (HgCal < 0.0001) Then GoTo 6789
	//................. set the constant gas fraction at the top ie. less than 0.45
	       //Modified by TY
	       If (i > N2phase - nHgCon And HgCal > 0.45) Then
	          HgCal = HgndOld(i)
	          //voltmp = MainDriver.volkx * (1# - HgCal) / HgCal
	          //volTot = MainDriver.volkx + voltmp
	       End If
	//...................................... set the maximum gas fraction
	//Modified by TY
	       HgMax = 0.45
	       If (HgCal > HgMax) Then
	          HgCal = HgMax
	          //voltmp = MainDriver.volkx * (1# - HgMax) / HgMax
	          //volTot = MainDriver.volkx + voltmp
	       End If
	//...................................... ??
	6789 dummy = 0  //simple continue
	       Call getTopH(volTot, xcell, hxt)
	       Xbcell = xcell + hxt
	       Call getVD(Xbcell, xbvdcell)
	//--- check the location of kill mud pumped
	       rhoL = oMud + Dendiff(i) //density change due to low-density gas dissolution
	       If xcell >= (xKill - 0.01) Then
	          rhoL = Kmud: volkkmix = volkkmix + MainDriver.volkx
	       End If
	       avgden = (rhoL) * (1# - HgCal) + gasDen * HgCal
	       //avgden = gasDen * HgCal + rhoL * (1# - HgCal)
	       dpmud = 0.052 * avgden * (xbvdcell - xvdcell)
	       ql = QtMix * voltmp / volTot: Qg = QtMix * MainDriver.volkx / volTot
	       Call get2pDP(Xbcell, ql, Qg, HgCal, rhoL, gasDen, gasVis)
	       dpfri = DPtop
	       Call get2pDP(xcell, ql, Qg, HgCal, rhoL, gasDen, gasVis)
	       dpfri = dpfri - DPtop   // DP-fric in mixed zone
	//............................................. assign calculated value
	       Pnd(i) = pxcell: Xnd(i) = xcell
	       volLqd(i) = voltmp: Hgnd(i) = HgCal
	       pxcell = pxcell + dpmud + dpfri * Pcon: xcell = Xbcell
	       PVgain = Vsol(i) + PVgain + MainDriver.volkx: QtotMix = QtotMix + volTot
	56789  dummy = 0
	//
	    Next i
	//
	    If (Xbcell > TMD(NwcS)) Then Xbcell = TMD(NwcS)
	    Xnd(1) = Xbcell: Pnd(1) = pxcell
	    Xb = Xnd(iXb)
	    Call pxBottom(volKmud, Xbcell, Pbtm)  //new cell is added continuously
	    DPdiff = Pb - Pbtm
	    PbTry = pxcell + DPdiff
	//*/
	}
	
	void Golden_circ(){
		//  Golden section search to obtain annulus pressure by
		//  comparing calculated B.H.P. with true B.H.P.
		//Modified by TY
		int iLoc=0, iter=0;
		utilityModule.Bkup();
		
		double px1 = 14.7, f1 = 1, px2 = MainDriver.Pb, f2 = -1, ffpb=0, pxErr=0, delPx=0, delvol=0;
		double px1old =0, px2old = 0, padd =0, txvd=0, DPacc = 0, d1=0, d2=0, DPtotal=0;
		double tmp2=0, tmp3=0, xmdtmp=0;
		double Qeff = 0, cap2eff2 = 0, volEff = 0, psicon = 0;
		MainDriver.iHgMax = 0;
		
		while(true){
			iter = iter + 1;//10
			Pkick = 0.5 * (px2 + px1);
			PbCalc_circ();
			ffpb = MainDriver.Pb - PbTry;
			pxErr = Math.abs(ffpb / MainDriver.Pb);
			if (pxErr < 0.0001) break;
			if (f1 * ffpb < 0){        //BiSection Method
				px2 = Pkick;
				f2 = ffpb;
				}
			else{
				px1 = Pkick; 
				f1 = ffpb;
				}
			if (iter > 100){ //20131015 AJW ITER 50에서 100으로 수정
				Pkick = Pkick + MainDriver.Pb - PbTry;
				break;
				}
			if (Pkick < 14.6){
				Pkick = 14.7;
				PbCalc_circ();
				break;
				}
			delPx = Math.abs(px1 - px2);         // check Infinite loop
			if (delPx < 0.05){
				//		          MsgBox "Pkick does not converge ! then modify it !", 0, msgtitle
				if (Math.abs(px1 - 14.7) < 0.005){   //Pkick get min. p & Pbeff increase
					Pbeff = PbTry;
					break;
					}
				px1old = px1;
				px2old = px2;
				padd = Math.abs(MainDriver.Pb - PbTry);
				if (MainDriver.Pb > PbTry) px2 = px2 + 4 * padd;
				else px1 = px1 - 4 * padd;
				f1 = 1; 
				px1 = Math.max(px1, 14.7); 
				px2 = Math.min(px2, MainDriver.Pb);
				}
			//GoTo 10
			}
		//.......................................... calculate & assign values
		//5055  dummy = 0   // simple continue
		
		/*for(int ii = 1; ii<MainDriver.N2phase; ii++){
		    	  tmp2 = MainDriver.volL[ii]; 
		    	  tmp3 = volLqd[ii];
		    	  MainDriver.volL[ii] =  Math.max(tmp2, tmp3);
		      }*/
		
		kickVD = utilityModule.getVD(kickLoc);
		kickVDbtm=utilityModule.getVD(MainDriver.Xb);
		txvd = utilityModule.temperature(kickVD);
		GasPropEX = propertyModule.GasProp(Pkick, txvd);//
		gasVis = GasPropEX[0];
		gasDen = GasPropEX[1];
		zz = GasPropEX[2];
		
		//.............................. calculate current effective flow rate
		//                               and surface choke pressure
		delvol = (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np] + 0.02);
		delvol = propertyModule.range(delvol, 0, (MainDriver.Qkill * 2.5)); //20130909 delvel -> delvol
		delvol = delvol / 2.448;
		iLoc = utilityModule.Xposition(kickLoc);
		DPacc = 0; 
		xmdtmp = kickLoc - MainDriver.TMD[iLoc + 1];
		for(int i = (iLoc + 1); i<9; i++){
			d2 = MainDriver.Do2p[i];
			d1 = MainDriver.Di2p[i];
			DPacc = DPacc + xmdtmp * delvol / (d2 * d2 - d1 * d1);
			xmdtmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1];
			}
		//............................................. for choke or kill lines
		getLinesEX = utilityModule.getLines(MainDriver.QtMix);
		Qeff = getLinesEX[0];
		cap2eff2 = getLinesEX[1];
		volEff = getLinesEX[2];
		d1 = getLinesEX[3];
		d2 = getLinesEX[4];
		DPacc = DPacc + xmdtmp * delvol / (d2 * d2 - d1 * d1) * Qeff / MainDriver.QtMix;
		psicon = 0.001614678;
		DPacc = psicon * MainDriver.oMud * DPacc / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np] + 1);
		//............ calculate total pressure loss for top single phase region
		utilityModule.getDP(MainDriver.QtMix, kickLoc, MainDriver.oMud);
		DPtotal = MainDriver.gMudOld * kickVD + DPacc + MainDriver.DPtop * MainDriver.Pcon;
		//
		MainDriver.volPump = MainDriver.volPump + MainDriver.Qkill * MainDriver.gDelT / (42 * 60);
		Pchoke = Pkick - DPtotal;		
		//...................... calculate pump pressure and stand pipe pressure
		getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, MainDriver.volPump);
		SPP = getDPinsideEX[0];
		pumpP = getDPinsideEX[1];
		//....................................... calculate casing seat pressure
		if (MainDriver.DepthCasing > MainDriver.Xb){
			Pbtm=utilityModule.pxBottom(MainDriver.volPump, MainDriver.DepthCasing);
			Pcasing = Pbtm;
			}
		else if (MainDriver.DepthCasing > kickLoc) Pcasing = propertyModule.DitplDes(MainDriver.DepthCasing);
		else{
			utilityModule.getDP(MainDriver.QtMix, MainDriver.DepthCasing, MainDriver.oMud);
			Pcasing = Pchoke + MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + MainDriver.Pcon * MainDriver.DPtop;
			}
		//--- calculate Qt-mix for the next calculation, 7/17/02
		//........... old approach gives very small return rate increase since average from beginning
		//      MainDriver.QtMix = MainDriver.Qkill + (PVgain - MainDriver.Vpit[MainDriver.NpSi)) * 42 * 60 / (MainDriver.gTcum - Tdelay + 2)
		//
		
		if( MainDriver.Np <= MainDriver.NpSi + 10) MainDriver.QtMix = MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.NpSi]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.NpSi] + 1);
		else MainDriver.QtMix = MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np - 4]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np- 4]);
		
		MainDriver.QtMix = propertyModule.range(MainDriver.QtMix, MainDriver.Qkill, MainDriver.Qkill * 4.5);
		}
	
	
	void Golden(){
	//  Golden section search to obtain annulus pressure by
	//  comparing calculated B.H.P. with true B.H.P.
		int iLoc=0, iter=0;
	    utilityModule.Bkup();
	    
	    double px1 = 14.7, f1 = 1, px2 = MainDriver.Pb, f2 = -1, ffpb=0, pxErr=0, delPx=0, delvol=0;
	    double px1old =0, px2old = 0, padd =0, txvd=0, DPacc = 0, d1=0, d2=0, DPtotal=0;
	    double tmp2=0, tmp3=0, xmdtmp=0;
	    double Qeff = 0, cap2eff2 = 0, volEff = 0, psicon = 0;
	    MainDriver.iHgMax = 0;
	    while(true){	    	
		    iter = iter + 1;//10
		    Pkick = 0.5 * (px2 + px1);
		    PbCalc();
		    ffpb = MainDriver.Pb - PbTry;
		    pxErr = Math.abs(ffpb / MainDriver.Pb);
		    if (pxErr < 0.0001) break;
		    if (f1 * ffpb < 0){        //BiSection Method
	    		px2 = Pkick;
	    		f2 = ffpb;
	    	}
	       else{
	    	   px1 = Pkick; 
	    	   f1 = ffpb;
	       }
	       if (iter > 100){ //20131015 AJW ITER 50에서 100으로 수정
	             Pkick = Pkick + MainDriver.Pb - PbTry;
	             break;
	       }
	       if (Pkick < 14.6){
	          Pkick = 14.7;
	          PbCalc();
	          break;
	       }
	       delPx = Math.abs(px1 - px2);         // check Infinite loop
	       if (delPx < 0.05){
//	          MsgBox "Pkick does not converge ! then modify it !", 0, msgtitle
	          if (Math.abs(px1 - 14.7) < 0.005){   //Pkick get min. p & Pbeff increase
	             Pbeff = PbTry;
	             break;
	          }
	          px1old = px1;
	          px2old = px2;
	          padd = Math.abs(MainDriver.Pb - PbTry);
	          if (MainDriver.Pb > PbTry) px2 = px2 + 4 * padd;
	          else px1 = px1 - 4 * padd;
	          f1 = 1; 
	          px1 = Math.max(px1, 14.7); 
	          px2 = Math.min(px2, MainDriver.Pb);
	          }
		      //GoTo 10
	    }
	//.......................................... calculate & assign values
	//5055  dummy = 0   // simple continue
	      for(int ii = 1; ii<MainDriver.N2phase; ii++){
	    	  tmp2 = MainDriver.volL[ii]; 
	    	  tmp3 = volLqd[ii];
	    	  MainDriver.volL[ii] =  Math.max(tmp2, tmp3);
	      }
	      kickVD = utilityModule.getVD(kickLoc);
	      kickVDbtm=utilityModule.getVD(MainDriver.Xb);
	      txvd = utilityModule.temperature(kickVD);
	      GasPropEX = propertyModule.GasProp(Pkick, txvd);//
	      gasVis = GasPropEX[0];
	      gasDen = GasPropEX[1];
	      zz = GasPropEX[2];
	//.............................. calculate current effective flow rate
	//                               and surface choke pressure
	      delvol = (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np] + 0.02);
	      if(delvol>MainDriver.Qkill*2.5 || delvol<0){
	      delvol = propertyModule.range(delvol, 0, (MainDriver.Qkill * 2.5)); //20130909 delvel -> delvol
	      }
	      delvol = delvol / 2.448;
	      iLoc = utilityModule.Xposition(kickLoc);
	      DPacc = 0; 
	      xmdtmp = kickLoc - MainDriver.TMD[iLoc + 1];
	      for(int i = (iLoc + 1); i<9; i++){
	         d2 = MainDriver.Do2p[i];
	         d1 = MainDriver.Di2p[i];
	         DPacc = DPacc + xmdtmp * delvol / (d2 * d2 - d1 * d1);
	         xmdtmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1];
	      }
	//............................................. for choke or kill lines
	      getLinesEX = utilityModule.getLines(MainDriver.QtMix);
	      Qeff = getLinesEX[0];
	      cap2eff2 = getLinesEX[1];
	      volEff = getLinesEX[2];
	      d1 = getLinesEX[3];
	      d2 = getLinesEX[4];
	      DPacc = DPacc + xmdtmp * delvol / (d2 * d2 - d1 * d1) * Qeff / MainDriver.QtMix;
	      psicon = 0.001614678;
	      DPacc = psicon * MainDriver.oMud * DPacc / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np] + 1);
	//............ calculate total pressure loss for top single phase region
	      utilityModule.getDP(MainDriver.QtMix, kickLoc, MainDriver.oMud);
	      DPtotal = MainDriver.gMudOld * kickVD + DPacc + MainDriver.DPtop * MainDriver.Pcon;
	//
	      MainDriver.volPump = MainDriver.volPump + MainDriver.Qkill * MainDriver.gDelT / (42 * 60);
	      Pchoke = Pkick - DPtotal;
	//...................... calculate pump pressure and stand pipe pressure
	      getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, MainDriver.volPump);
	      SPP = getDPinsideEX[0];
	      pumpP = getDPinsideEX[1];
	//....................................... calculate casing seat pressure
	      if (MainDriver.DepthCasing > MainDriver.Xb){
	         Pbtm=utilityModule.pxBottom(MainDriver.volPump, MainDriver.DepthCasing);
	         Pcasing = Pbtm;
	         }
	      else if (MainDriver.DepthCasing > kickLoc) Pcasing = propertyModule.DitplDes(MainDriver.DepthCasing);
	      else{
	         utilityModule.getDP(MainDriver.QtMix, MainDriver.DepthCasing, MainDriver.oMud);
	         Pcasing = Pchoke + MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + MainDriver.Pcon * MainDriver.DPtop;
	      }
	//--- calculate Qt-mix for the next calculation, 7/17/02
	//........... old approach gives very small return rate increase since average from beginning
	//      MainDriver.QtMix = MainDriver.Qkill + (PVgain - MainDriver.Vpit[MainDriver.NpSi)) * 42 * 60 / (MainDriver.gTcum - Tdelay + 2)
	//
	      if( MainDriver.Np <= MainDriver.NpSi + 10) MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.NpSi]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.NpSi] + 1));
	      else MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np - 4]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np- 4]));
	      MainDriver.QtMix = propertyModule.range(MainDriver.QtMix, MainDriver.Qkill, MainDriver.Qkill * 4.5);
	      
	//
	}
	
	class TimerCircTask extends TimerTask{
		double timeAdd=0;
		double Xold =0, hgX=0, Xnew=0, Vgf=0;
		double[] getXnewex = new double[2];
		public void run(){
			//-----This is the main control program until kick top arrives at the surface
			//     This is also valid for ML wells.  7/30/2003
			//
			TimerCircTaskFinished = 1;
			timeAdd = (int) (TimerCirculationIntv * 0.001 * MainDriver.SimRate * ifastRun); //time interval from last step
			
			if (MainDriver.Np== MainDriver.NpSi){      // assign the very beginning of the Pumping
			//       timeadd = 2 * tmd(MainDriver.NwcS) / MainDriver.Vsound   //increase twice to reduce accel. loss, 7/11/02
				timeAdd = 30;   //set 30 sec to reduce accel. loss, 7/11/02
				timeNp = 62;
			}
			
			Xold =  kickLoc;
			Pbeff = MainDriver.Pb;
			hgX = MainDriver.Hgnd[MainDriver.N2phase-1];
			
			iMLkickLoc = 0;   //check possible kick flow from ML
			dummy = calc_KickSlipML(timeAdd);
			
			if(iMLkickLoc == 0){
				if(MainDriver.imud==0) getXnewex = utilityModule.GetXnew(Xold, timeAdd, hgX, MainDriver.QtMix);
				else getXnewex = utilityModule.GetXnew(Xold, timeAdd, 0, MainDriver.QtMix);
				Xnew = getXnewex[0];
				Vgf = getXnewex[1];
				kickLoc = Xnew;
			}
			
			timeNpUse = 60;   //time interval for data storage
			if (MainDriver.iOnshore == 2 && kickLoc < MainDriver.Dwater * 1.2) timeNpUse = 30;
			if (kickLoc < -0.01){
			       timeAdd = Xold / Vgf;
			       kickLoc = 0;    // KICK just arrives at the surface
			       mudCase = -1;
			       timeNp = 62;    //save the results
			}

			    MainDriver.gTcum = MainDriver.gTcum + timeAdd;   //total time from beginning of KILL
			    MainDriver.gDelT = timeAdd;           //time interval			    
			    if(MainDriver.gTcum > 5549){
			    	dummy=1;
			    }
			    Golden_circ();
			    
			    timeNp = (int) (timeNp + timeAdd);
			    ShowResult();
			//--------------------------------------------- Remove the KICK out of Hole
			    if (mudCase == -1){    //KICK just arrived at the SURFACE !
			       operationMsg.setText("Kick just arrived at the surface !");
			       if(TimerCirculationOn ==1){
			    	   TimerCirculationOn =0;
			    	   this.cancel();			    	   
			       }
			       if(TimerKickOutOn==0){
			    	   TimerKickOutOn=1;
			    	   TimerKickOut = new Timer();
			    	   TimerKickOut.schedule(new TimerKickOutTask(), 0, TimerKickOutIntv);  
			    	   //TimerKickOut.schedule(new TimerKickOutTask(), 0, 10);//타이머 조절 편의를 위해 조절  히히
			       }			       
			    }			   
			    TimerCircTaskFinished = 0;
			    }
		}
	
	int GetXkill(){
		double volEff=0, hkill=0;
		xKill = MainDriver.TMD[MainDriver.NwcS-1];
		if(volKmud < MainDriver.VOLinn) return -1;
		volEff = volKmud - MainDriver.VOLinn + volkkmix;  //(kick expansion in kill mud)
		////    voleff = volkmud - volinn
		hkill = utilityModule.getBotH(volEff, xKill);
		xKill = MainDriver.TMD[MainDriver.NwcS-1] - hkill;           //depth of kill mud
		return 0;
	}

	double NewN2ph(double timeAdd){ //sub to calculate the new n2phase
		int Remains =0;
		double delTsum=0, x2Top=0, x2Bot, hgX=0, timeint=0;
		
		if(MainDriver.N2phase == 1) return timeAdd;
		
		delTsum = 0; 
		for(int i = MainDriver.N2phase-1; i>0; i--){
			x2Top = MainDriver.Xnd[i];
			x2Bot = MainDriver.Xnd[i - 1];
			hgX = MainDriver.Hgnd[i - 1];
			timeint = utilityModule.calcDt(x2Top, x2Bot, hgX);
			delTsum = delTsum + timeint;
			//--- calculate the time based on one cell length & GT 2nd cell
			if(delTsum > timeAdd){
				Remains = (int)(delTsum - timeAdd);
				if(Remains <= 2) MainDriver.N2phase = i;
				else{
					MainDriver.N2phase = i+1;
					if(MainDriver.imud ==0){//wbm
						MainDriver.pvtZb[i] = MainDriver.pvtZb[i] * Remains / (timeint + 0.002);
					}
					else{//obm
						MainDriver.R=10.73;
						MainDriver.PVTZ_Gas[i] = MainDriver.PVTZ_Gas[i]*Remains/(timeint+0.002);
						}
					MainDriver.volL[i] = MainDriver.volL[i] * Remains / (timeint + 0.002);
					MainDriver.volG[i] = MainDriver.volG[i] * Remains / (timeint + 0.002);
					}
				return timeAdd;
				}
			}
		//--- all kick is removed effectively ! - assign old mud properties
		MainDriver.N2phase = 1;
		timeAdd = delTsum;
		return timeAdd;
		}
	
	void calcTime(double timeAdd){ //time to remove the required number of cells
		int Remains=0;     //modified 7-1-94, 2/5/2004
	    int delTsum = 0;  //; n2phold = MainDriver.N2phase
	    double timeint=0;
	    double x2Top=0, x2Bot=0, hgX=0;
	    for(int ic = MainDriver.N2phase-1; ic>1; ic--){
	    	x2Top = MainDriver.Xnd[ic];
	    	x2Bot = MainDriver.Xnd[ic - 1];
	    	hgX = MainDriver.Hgnd[ic - 1];
	    	timeint = utilityModule.calcDt(x2Top, x2Bot, hgX);
	    	delTsum = (int) (delTsum + timeint);
	//--- calculate the time based on one cell length & GT 2nd cell
	       if (delTsum > timeAdd || ic <= 2){
	          if (ic == MainDriver.N2phase-1 || ic == 2){  //mendatory one cell movement including MainDriver.N2phase = 3
	// 2/5/2004,  we need to consider big cells more than 100 bbls for multilateral applications
	             Remains = (int)(delTsum - timeAdd);
	             if(Remains <= (int)(timeAdd + 0.5)){  //whole cell removal
	            	 MainDriver.N2phase = ic;
	            	 timeAdd = delTsum;
	             }
	             else{   //partial removal corresponding to timeAdd
	                MainDriver.N2phase = ic+1;
	                MainDriver.pvtZb[ic] =  (MainDriver.pvtZb[ic] * Remains / (timeint + 0.002));
	                MainDriver.volL[ic] =  (MainDriver.volL[ic] * Remains / (timeint + 0.002));
	                MainDriver.volG[ic] =  (MainDriver.volG[ic] * Remains / (timeint + 0.002));
	             }
	          }
	          else{
	             MainDriver.N2phase = ic+1; 
	             timeAdd = delTsum - timeint;
	          }
	          break;//Exit For
	       }
	    }
	}	
	
	class TimerKillMudTask extends TimerTask{   // circulate the KILL mud for Driller's Method
		int indexi=0;
		double volAdd=0;
		int back1234=0;		
		
		public void run(){			
			//------------ active only MainDriver.Method = 1 (DM), not = 2 (EM) - AWC. version
			TimerKillMudTaskFinished = 1;
			if(TimerKickOutTaskFinished==0){
				if(back1234==0){
					MainDriver.Cmud = MainDriver.Kmud;
					MainDriver.gMudCirc =MainDriver.gMudKill;
					//------------------------ need to assign to show the calculated result
					kickLoc = 0; 
					MainDriver.Xb = 0; 
					kickVD = 0; 
					kickVDbtm = 0;
					Pkick = psia; 
					gasDen = 0;
					Pbeff = MainDriver.Pb;
					QgDay = 0;
					PVgain = 0; //; hgx = 0
					SPP = MainDriver.Psp[MainDriver.Np];
					pumpP = MainDriver.Ppump[MainDriver.Np];
					Pcasing = MainDriver.Pcsg[MainDriver.Np];
					Pchoke = MainDriver.Pchk[MainDriver.Np];
				}
				
				while(true){					
					iKmud = iKmud + 1; //1234
					indexi = (MainDriver.NwcE-1) + 3 - iKmud;
					
					if (indexi >= (MainDriver.NwcS-1) + 1){
						//........... surface line, from stand pipe to kelly, and inside DS
						//            only MainDriver.VOLcir[i), Psp, and MainDriver.Ppump change
						volAdd = MainDriver.volDS[indexi];
						if (volAdd < 0.01){//GoTo 1234
							back1234=1;
							break;
						}
			        volKmud = volKmud + volAdd;
			        getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volKmud);
			        SPP = getDPinsideEX[0];
			        pumpP = getDPinsideEX[1];
					}
					
					else if (indexi == (MainDriver.NwcS-1)){
						//........... kill mud just past bit nozzle
						//            there is pressure jump before and after passing nozzle
						volAdd = 0.1;
						volKmud = volKmud + volAdd;
						getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volKmud);
						SPP = getDPinsideEX[0];
						pumpP = getDPinsideEX[1];
						}
					
					else{
						//........... kill mud inside annulus; open hole, casing, and
						//            choke line or kill line, or both;only Volcir, Pchk, Pcsg change
						index2 = index2 + 1;
						volAdd = MainDriver.VOLann[index2];
						if (volAdd < 0.01){
							back1234=1;
							break;
							}
						if (index2 == 9){
							getLinesEX = utilityModule.getLines(MainDriver.Qkill);
							volAdd = getLinesEX[2];
						}						
						volKmud = volKmud + volAdd;
						Pchoke = utilityModule.pxBottom(volKmud, 0);
						Pcasing = utilityModule.pxBottom(volKmud, MainDriver.DepthCasing);
						}
					
					MainDriver.volPump = MainDriver.volPump + volAdd;
					MainDriver.gDelT = volAdd * 60 / (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2);
					MainDriver.gTcum = MainDriver.gTcum + MainDriver.gDelT;
					timeNp = 62;  //AssignValue  //save array output, 7/18/02
					ShowResult();
					
					if (index2 == (MainDriver.NwcE-1)){
						mudCase = 12;
						operationMsg.setText("Pumped two circulations !");
						String msg =  "Driller's Method has been completed by you. You did a GREAT job !.";
						JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
						
						if(TimerKillMudOn==1){
							TimerKillMudOn=0;
							TimerKillMud.cancel();
							}
						}
					back1234=0;
					break;
				}
			}
			TimerKillMudTaskFinished=0;
		}
	}

	class TimerKickOutTask extends TimerTask{
		//.....  Pumping out the kick fluid and old mud for on- or off- shore drilling
		//       by calculating cell movement by specific time
		//
		public void run(){
			TimerKickOutTaskFinished = 1;
	    	if(TimerCircTaskFinished == 0){
	    		double timeAdd = TimerKickOutIntv * 0.001 * MainDriver.SimRate * ifastRun;  //time interval from Timer
			    double Xold =0, hgX=0, timeint=0;
			    double[] getXnewex = new double[2];
			    double Xnew=0, Vgf=0, volPumpOld=0, volAdd=0;
			    
		    	kickLoc = 0;			    
			    timeNpUse = 60;
			    
		    	while(true){			    		
		    		if (MainDriver.iOnshore == 2) timeNpUse = 30;		    		
		    		if (MainDriver.N2phase >= 2){
			    		dummy = calc_KickSlipML(timeAdd);  //8/1/2003
			    		
			    		timeAdd=NewN2ph(timeAdd);
			    		MainDriver.gDelT = timeAdd;          //time interval
			    		Golden_circ();
			    		MainDriver.Xb = MainDriver.Xnd[0];
			    		if (MainDriver.N2phase == 2){
			    			mudCase = 1;
			    		}
			    		break;//GoTo 88080   // for the time control purpose
			    	}
		    		//............ check whether there is a kick from ML after removing all kick in MP
		    		dummy = calc_KickSlipML(timeAdd);
		    		
		    		if(MainDriver.N2phase >= 3){   //there is kick from ML
		    			mudCase = 0;
		    			// 1/31/2004
		    			//       make a simple run and then activate kick circ. timer again
		    			//
		    			Xold = kickLoc;
		    			Pbeff = MainDriver.Pb; 
		    			hgX = MainDriver.HgMax;
		    			MainDriver.N2phase = MainDriver.N2phase - 1;  //to remove liquid part above the kick
		    			
		    			if(MainDriver.imud==0) getXnewex = utilityModule.GetXnew(Xold, timeAdd, hgX, MainDriver.QtMix);
		    			else getXnewex = utilityModule.GetXnew(Xold, timeAdd, 0, MainDriver.QtMix);
		    			Xnew = getXnewex[0];
		    			Vgf = getXnewex[1];
		    			kickLoc = Xnew;
		    			//
		    			
		    			MainDriver.gTcum = MainDriver.gTcum + timeAdd;   //total time from beginning of KILL
		    			MainDriver.gDelT =  timeAdd;           //time interval
		    			
		    			Golden();
		    			timeNp = (int) (timeNp + timeAdd);
		    			ShowResult();
		    			//
		    			
		    			if(TimerCirculationOn==0){
		    				TimerCirculationOn=1;
		    				TimerCirculation = new Timer();
		    				TimerCirculation.schedule(new TimerCircTask(), 0, TimerCirculationIntv); 
		    				}
		    			
		    			if(TimerKickOutOn==1){
		    				TimerKickOut.cancel(); //Exit Sub   //no further calculations, just exit and re-calculate
		    				TimerKickOutOn=0;
		    				break;
		    				}
		    			}
		    		//........................ pumping out remaining kick out of hole
		    		
		    		if (mudCase == 1){  //only one cell remaining
		    			Xold = MainDriver.Xnd[0];
				    	hgX = MainDriver.Hgnd[1];
				    	timeint = utilityModule.calcDt(kickLoc, Xold, hgX);
				    	MainDriver.gDelT = timeint;
				    	MainDriver.volPump = MainDriver.volPump + MainDriver.Qkill * MainDriver.gDelT / (42 * 60);
				    	//
				    	MainDriver.Xb = 0; 
				    	kickVD = 0; 
				    	kickVDbtm = 0; 
				    	Pkick = psia; 
				    	gasDen = 0;
				    	PVgain = 0;
				    	QtotMix = 0;
				    	MainDriver.N2phase = 1;
				    	getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, MainDriver.volPump);
				    	SPP = getDPinsideEX[0];
				    	pumpP = getDPinsideEX[1];
				    	Pbtm =  utilityModule.pxBottom(MainDriver.volPump, MainDriver.DepthCasing); 
				    	Pcasing = Pbtm;
				    	Pbtm =  utilityModule.pxBottom(MainDriver.volPump, kickLoc);
				    	Pchoke = Pbtm;
				    	timeNp = 62;   //AssignValue
				    	operationMsg.setText("Pumped all kick out !");
				    	mudCase = 2;
				    	break;
				    	}
		    		//........................ pump circulating mud rest of annulus section
		    		//                         specify current location of circulating mud
		    		
				    volPumpOld = MainDriver.volPump;  //; hgx = 0
				    volAdd = MainDriver.Qkill * timeAdd / (42 * 60);
				    MainDriver.gDelT =  timeAdd;
				    
				    if ((volPumpOld + volAdd) >= (MainDriver.VOLinn + MainDriver.VOLout)){
				    	operationMsg.setText("Pumped one circulation !"); //check 1 circulation
				    	if (MainDriver.igERD == 1) {
				    		if (PVgain < 0.0002){  //no kick in the main path	
				    			timeNp = 62;    //save the results
				    			String msg =  "NO kick in the main path after one circulation. You did a GREAT job !.";
				    			JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
				    			if(TimerKickOutOn==1){
				    				TimerKickOutOn=0;
				    				TimerKickOut.cancel();				  
				    				}//one circulation only for ML
				    			}
				    		}
				    	
				    	else{
				    		MainDriver.gDelT =  ((MainDriver.VOLinn + MainDriver.VOLout - volPumpOld) * 60 / (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2));
				    		mudCase = 11;    //old mud removed completely
				    		if (MainDriver.Method == 1 && TimerKillMudOn==0){
				    			TimerKillMud = new Timer();
				    			TimerKillMudOn=1;
				    			TimerKillMud.schedule(new TimerKillMudTask(), 0, TimerKillMudIntv); 
				    			//TimerKillMud.schedule(new TimerKillMudTask(), 0, 10);//편의를 위해 타이머 조절히히
				    			}
				    		if(TimerKickOutOn==1){
				    			TimerKickOutOn=0;
				    			TimerKickOut.cancel();
				    			}		
				    		timeNp = 62;    //save the results
				    		}
				    	}
				    
				    MainDriver.volPump = MainDriver.volPump + MainDriver.Qkill * MainDriver.gDelT / (42 * 60);
				    getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, MainDriver.volPump);
				    SPP = getDPinsideEX[0];
				    pumpP = getDPinsideEX[1];
				    Pbtm=utilityModule.pxBottom(MainDriver.volPump, MainDriver.DepthCasing); 
				    Pcasing = Pbtm;
				    Pbtm = utilityModule.pxBottom(MainDriver.volPump, kickLoc);
				    Pchoke = Pbtm;
				//
				    if (mudCase == 11){
				    	volKmud = 0; 
				    	index2 = MainDriver.NwcS-1;
				    	if(MainDriver.Method == 2){
				    		String msg =  "Engineer's Method has been completed. You did a GREAT job !.";
				    		JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
				    		}
				    	}
				    break;
				    }
		    	
		    	//88080  dummy = 0   //simple continue, 8/3/2003
		    	if(TimerKickOutOn==1 || TimerKillMudOn==1){
		    		MainDriver.gTcum = MainDriver.gTcum + MainDriver.gDelT;
		    		timeNp = (int) (timeNp + MainDriver.gDelT);
		    		ShowResult();
		    		}
		    	}
	    	TimerKickOutTaskFinished = 0;
	    	}
		}
	
	int scx(double x, double Max, double Min, double intv){//intv = GraphSize-sepnum*sepintv
		int result = 0;		
		result = (int)((x-Min)/(Max-Min)*(GraphXsize-intv)+GraphXLoc);		
		return result;
	}
	
	int scy(double y, double Max, double Min, double intv){
		int result = 0;
		result = (int)((GraphYsize-intv)-(y-Min)/(Max-Min)*(GraphYsize-intv));
		result = result+GraphYLoc;//+15; //15=label font size
		return result;
	}
	
	class Sgraph extends JPanel{
		int ColumnCount = 0;
		int RowCount = 0;
	    int ColumnLabelCount = 0;
	    String ColumnLabel = "";
	    int Column =0, Row=0;	    
	    double sgXMax=1, sgXMin=1, sgYMax=1, sgYMin=1;
	    double sgX2Max=1, sgX2Min=1, sgY2Max=1, sgY2Min=1;
	    int sepXnum=6; int sepYnum=6;
	    int sg2Use=0;
	    double sepXIntv=1; double sepYIntv=1;
	    double sgxMaxAdj=1, sgyMaxAdj=1;
	    double sgxMinAdj=0, sgyMinAdj=0;
	    int filter = 0;// 
	    
	    double[] SgxData = new double[MainDriver.Npt+1];
	    double[] SgyData = new double[MainDriver.Npt+1];
	    double[] Sgx2Data = new double[MainDriver.Npt+1];
	    double[] Sgy2Data = new double[MainDriver.Npt+1];
	    JLabel lblTitle;
	    JLabel[] sgx = new JLabel[15]; // x-axis number tag
	    JLabel[] sgy = new JLabel[15]; // y-axis number tag
	    
		Sgraph(){
			this.setBackground(Color.white);
			this.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));						
			this.setLayout(null);
			for(int i=0; i<15; i++){
				sgx[i] = new JLabel("");
				sgy[i] = new JLabel("");
				add(sgx[i]);
				add(sgy[i]);
			}
		}
		
		public void paint(Graphics g){
			super.paint(g);
			Graphics2D g2d = (Graphics2D)g;
			int drawwidth = 1;
			g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 
			g.setColor(Color.BLACK);
			int intvX = GraphXsize/sepXnum;
			int intvY = GraphYsize/sepYnum;
			int xAdjCon=0, yAdjCon=0;
			
			for(int i=0; i<15; i++){
				sgx[i].setVisible(false);
				sgy[i].setVisible(false);
			}
			for(int i=0; i<GraphYsize+1; i=i+intvY){				
				g.drawLine(GraphXLoc-3, GraphYLoc+i, GraphXLoc+GraphXsize, GraphYLoc+i);//5 = the half vertical length of label. 11/2				
				int x=(int)(sepYnum-i/intvY); 
				if(filter == 1 && x>=0){
					sgy[x].setBounds(GraphXLoc-35, GraphYLoc+i-5, 35, 11);
					sgy[x].setVisible(true);
				}
			}
			
			for(int i=0; i<GraphXsize+1; i=i+intvX){
				g.drawLine(GraphXLoc+i, GraphYLoc, GraphXLoc+i, GraphYLoc+GraphYsize+3);
				int x=i/intvX;
				if(filter == 1 && x<=sepXnum){
					sgx[x].setBounds(GraphXLoc+i-18, GraphYLoc+GraphYsize+5, 35, 11);
					sgx[x].setVisible(true);
				}
			}
			
			xAdjCon = GraphXsize - sepXnum*intvX;
			yAdjCon = GraphYsize - sepYnum*intvY;
			
			drawwidth = 3;
			g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 
			g.setColor(Color.RED);
			for(int i=0; i<RowCount-1; i++){ // TOTAL NUMBER OF POINT = Rowcount , so the number of line is equal to RowCount-1
				g.drawLine(scx(SgxData[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(SgyData[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx(SgxData[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(SgyData[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
				if(sg2Use==1) g.drawLine(scx(Sgx2Data[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(Sgy2Data[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx(Sgx2Data[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(Sgy2Data[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
			}
			sg2Use=0;
		}
		
		void calcGraphIntv(){
			int dummy=0;	
			
			for(int i=0; i<RowCount; i++){
		    	if(i==0){
		        	sgXMax=SgxData[i];
		        	sgXMin=SgxData[i];
		        	sgYMax=SgyData[i];
		        	sgYMin=SgyData[i];
		        	
		        	if(sg2Use==1){
		        		sgX2Max=Sgx2Data[i];
			        	sgX2Min=Sgx2Data[i];
			        	sgY2Max=Sgy2Data[i];
			        	sgY2Min=Sgy2Data[i];
		        	}
		        }
		        
		        else{
		        	if(SgxData[i]>sgXMax) sgXMax=SgxData[i];
		        	if(SgxData[i]<sgXMin) sgXMin=SgxData[i];
		        	if(SgyData[i]>sgYMax) sgYMax=SgyData[i];
		        	if(SgyData[i]<sgYMin) sgYMin=SgyData[i];
		        	
		        	if(sg2Use==1){
		        		if(Sgx2Data[i]>sgX2Max) sgX2Max=Sgx2Data[i];
			        	if(Sgx2Data[i]<sgX2Min) sgX2Min=Sgx2Data[i];
			        	if(Sgy2Data[i]>sgY2Max) sgY2Max=Sgy2Data[i];
			        	if(Sgy2Data[i]<sgY2Min) sgY2Min=Sgy2Data[i];
		        	}
		        }
		    }
			
			if(sg2Use==1){
				if(sgX2Max>sgXMax) sgXMax = sgX2Max;
				if(sgX2Min<sgXMin) sgXMin = sgX2Min;
				if(sgY2Max>sgYMax) sgYMax = sgY2Max;
				if(sgY2Min<sgYMin) sgYMin = sgY2Min;
			}
			
			while(dummy==0){				
		    	if((sgXMax-sgXMin)/sepXIntv>=1 && (sgXMax-sgXMin)/sepXIntv<=10) dummy=1;
		    	else if((sgXMax-sgXMin)/sepXIntv<=0) dummy=1;
		    	else if((sgXMax-sgXMin)/sepXIntv<4) sepXIntv=sepXIntv/10;		    	
		    	else sepXIntv=sepXIntv*10;	    	
		    }
			
		    while(dummy==1&&sepYIntv!=0){		    
		    	if((sgYMax-sgYMin)/sepYIntv>=1 && (sgYMax-sgYMin)/sepYIntv<=10) dummy=0;
		    	else if((sgYMax-sgYMin)/sepYIntv<=0) dummy=0;
		    	else if((sgYMax-sgYMin)/sepYIntv<4) sepYIntv = sepYIntv/10;		    	
		    	else sepYIntv=sepYIntv*10;		    	
		    }
		    
		    while(dummy==0){
		    	if(sgXMax>=0){
		    		if(sgXMax%sepXIntv>0.01) sgxMaxAdj = (int)(sgXMax/sepXIntv+1)*sepXIntv;
		    		else sgxMaxAdj = (int)(sgXMax/sepXIntv)*sepXIntv;
		    	}
		    	else sgxMaxAdj = (int)(sgXMax/sepXIntv)*sepXIntv;
		    	
		    	if(sgXMin>=0) sgxMinAdj = (int)(sgXMin/sepXIntv)*sepXIntv;
		    	else sgxMinAdj = (int)(sgXMin/sepXIntv-1)*sepXIntv;
		    				    	
		    	sepXnum=(int) ((sgxMaxAdj-sgxMinAdj)/sepXIntv);
		    	if(sepXnum>=4 && sepXnum<11) dummy=1;
		    	else if(sepXnum<4) sepXIntv=sepXIntv/2;
		    	else sepXIntv+=sepXIntv;
		    }
		    
		    while(dummy==1){
		    	if(sgYMax>=0){
		    		if(sgYMax%sepYIntv>0.01) sgyMaxAdj = (int)(sgYMax/sepYIntv+1)*sepYIntv;
		    		else sgyMaxAdj = (int)(sgYMax/sepYIntv)*sepYIntv;
		    	}
		    	else sgyMaxAdj = (int)(sgYMax/sepYIntv)*sepYIntv;
		    	
		    	if(sgYMin>=0) sgyMinAdj = (int)(sgYMin/sepYIntv)*sepYIntv;
		    	else sgyMinAdj = (int)(sgYMin/sepYIntv-1)*sepYIntv;
		    	
		    	sepYnum=(int) ((sgyMaxAdj-sgyMinAdj)/sepYIntv);
		    	if(sepYnum>=4 && sepYnum<11) dummy=0;
		    	else if(sepYnum<4) sepYIntv=sepYIntv/2;
		    	else sepYIntv+=sepYIntv;
		    } // we will use sepXnum+1 labels and sepYnum+1 labels. 
			filter = 1;
			for(int i=0; i<sepXnum+1; i++){			
				sgx[i].setText(Integer.toString((int)(sgxMinAdj+i*sepXIntv)));
				sgx[i].setFont(new Font("굴림", Font.BOLD, 10));
				sgx[i].setHorizontalAlignment(SwingConstants.CENTER);
				
				add(sgx[i]);
				}
			for(int i=0; i<sepYnum+1; i++){
				sgy[i].setText(Integer.toString((int)(sgyMinAdj+i*sepYIntv)));
				sgy[i].setFont(new Font("굴림", Font.BOLD, 10));
				sgy[i].setHorizontalAlignment(SwingConstants.CENTER);
				add(sgy[i]);
				}
			dummy=0;
		}
	}
	
	void PlotKillSheet(){
	    double gmc2 = MainDriver.gMudCirc;
	    double cmud2 = MainDriver.Cmud;
	    double volks = 0, psp3 =0, pp3=0;
	    
	    MainDriver.gMudCirc = MainDriver.gMudKill;  
	    MainDriver.Cmud = MainDriver.Kmud;     // just plot the kill sheet	    
	    getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volks);
	    psp3 = getDPinsideEX[0];
	    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    iCP = pp3; 
	    ppks[MainDriver.NwcE + 3] = pp3; 
	    stks[MainDriver.NwcE + 3] = 0;
	    for(int i = (MainDriver.NwcE + 2); i>MainDriver.NwcS; i--){
	    	volks = volks + MainDriver.volDS[i-1];
	    	getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volks);
		    psp3 = getDPinsideEX[0];
		    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	        ppks[i] = pp3;
	        stks[i] = (int)(volks*(MainDriver.spMin1+MainDriver.spMin2) / (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)); // 20140205 ajw total pump1 & pump2 strokes 	        
	    }
	    fCP = pp3;
	    volks = volks + 0.1;  // just pass the BIT
	    getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volks);
	    
	    psp3 = getDPinsideEX[0];
	    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    
	    ppks[MainDriver.NwcS] = pp3; 
	    stks[MainDriver.NwcS] = (int)(volks*(MainDriver.spMin1+MainDriver.spMin2) / (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2));
	    
	    volks = volks + 10.5;    //10.5 bbls more for plotting purpose !
	    getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volks);
	    psp3 = getDPinsideEX[0];
	    pp3 = getDPinsideEX[1];
	    
	    
	    
	    ppks[MainDriver.NwcS - 1] = pp3;
	    stks[MainDriver.NwcS - 1] = (int)(volks*(MainDriver.spMin1+MainDriver.spMin2) / (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2));
	//
	//-------------------------------- specify the graph//s properties !
	//   There are some diffeMainDriver.RenCe in plotting between v.6 and v.3
	//
	    Sgg[0].ColumnCount = 2;
	    Sgg[0].RowCount = MainDriver.NwcE - MainDriver.NwcS + 5;  // total number of DATA-1 = (NWCE+2)-(NWCS-2)+1
	//    Sgraph0.Plot.Axis.AxisScale = Int((icp + 20.5) / 10) * 10
	//    Sgraph0.YAxisMin = Int((fcp - 19.5) / 10) * 10
	//    Sgraph0.YAxisTicks = 10
	    for(int i = (MainDriver.NwcS - 1); i<(MainDriver.NwcE+4); i++){
	        Sgg[0].Row = i - MainDriver.NwcS + 1;
	        Sgg[0].Column = 0;
	        Sgg[0].SgxData[Sgg[0].Row] = stks[i];	        
	        Sgg[0].Column = 1;
	        Sgg[0].SgyData[Sgg[0].Row] = ppks[i] - 14.7;
	    }	    
	    
	    //int sg0sepXnum=6; int sg0sepYnum=6;
	    //int sg0sepXIntv=1; int sg0sepYIntv=1;
	    Sgg[0].calcGraphIntv();
	    Sgg[0].repaint();
	    //Sgg[0].Plot.UniformAxis = False
	    MainDriver.gMudCirc =  gmc2; 
	    MainDriver.Cmud =  cmud2;
	
	}
	
	void Form_Load(){ // assign SI or Continuous SI data
		
		int j, ic;
		double[] mldelvd = new double[3];
		double delMD=0, delVD=0, angTop=0;
		

	    //SimAuto.WindowState = 2; TimerCirculation.Enabled = False		
	    MainDriver.iWshow = 0;     //temporary hide the wellbore
		if(MainDriver.iCHKcontrol==2) operationMsg.setVisible(false);
	//---------- assign the final SI conditions from 2-phase calculation
	    kickLoc = MainDriver.Xnd[MainDriver.N2phase-1];
	    MainDriver.Xb = MainDriver.Xnd[0];
	    kickVD = MainDriver.xTop[MainDriver.NpSi];
	    kickVDbtm = MainDriver.xBot[MainDriver.NpSi]; 
	    Pkick = MainDriver.pxTop[MainDriver.NpSi];
	    PVgain = MainDriver.Vpit[MainDriver.NpSi];
	    Pchoke = MainDriver.Pchk[MainDriver.NpSi]; 
	    Pcasing = MainDriver.Pcsg[MainDriver.NpSi]; 
	    Pbeff = MainDriver.Pb2p[MainDriver.NpSi];
	    SPP = MainDriver.Psp[MainDriver.NpSi]; 
	    pumpP = MainDriver.Ppump[MainDriver.NpSi];
	    gasDen = MainDriver.rhoK[MainDriver.NpSi];
	    Tdelay = MainDriver.TTsec[MainDriver.NpSi];
	    Stroke1 = MainDriver.StrokePump1[MainDriver.NpSi];
	    Stroke2 = MainDriver.StrokePump2[MainDriver.NpSi];
	    TotalStroke = MainDriver.Stroke[MainDriver.NpSi];
	    
	    MainDriver.volPump = 0;
	    volKmud = 0; 
	    MainDriver.QtMix = 0;
	    psia = 14.7;
	    volkkmix=0;
	//--------------------------------------------------------- after 7/11/02
	    MainDriver.SimRate = 5; //10   //set a default value for simulation rate
	    ifastRun = 1;   //This is the same as additional acceleration rate !
	    mudLineP = Pchoke + MainDriver.gMudOld * MainDriver.Dwater;
	    gasFlowOut = 0;
	    mudFlowOut = 0;
	    ShowResult();
	//    Np = MainDriver.NpSi      //This is necessary to save shutin information retrived.
	//-----------------------------------------------------------------------
	    txtPumpRate.setText("0.0"); 
	    txtCHKopen.setText("0");
	    txtSimRate.setText(Integer.toString(MainDriver.SimRate));
	    HScrollRate.setValue(MainDriver.SimRate);
	//
	//    Tdelay = MainDriver.TTsec[MainDriver.NpSi);
	    QtotMix = MainDriver.Vpiti;
	    QgDay = 0; 
	    MainDriver.volMix =  QtotMix;
	    grpint = 5; timeNp = 0; mudCase = 0;
	    PlotKillSheet();
	    
	    tmpintMud = 0;
	    pan1.repaint(); //Call DrawMudGauge(1, 0)    //return rate of mixture
	    tmpintCHK = Pchoke - psia;
	    pan2.repaint(); //Call DrawMudGauge(2, Pchoke - psia)    //return rate of mixture
	    tmpintSDP = SPP -psia;
	    pan3.repaint(); //Call DrawMudGauge(3, SPP - psia)    //return rate of mixture
	//--------- set the graph maximun
	    Gleft = 1920; Gtop = 1560;  //Gheight = 6735; gwidth = 9975
	    Gheit = MainDriver.screnHt - Gtop - MainDriver.screnMgin - 120;
	    Gwidth = MainDriver.screnWd - Gleft - 120;
	    Oheit = 1335; OWidth = 3495;  //; oleft = 8400
	    iDClk = 0; iKmud = 0;
	//---------------------------------------- Additional calculations
	    MainDriver.nHgCon = 2;    //trial value
	   for(int ii = 0; ii <MainDriver.igMLnumber; ii++){   //effective gas fraction in ML, 7/3/2003
	        if(MainDriver.mlPlug[ii] == 0){
	           mlHgeff[ii] = MainDriver.HgMax;
	           double mlKickVol = MainDriver.mlQgTotM[ii] * MainDriver.mlBg[ii] * 1000 / mlHgeff[ii];
	           ic = ii;
	           mldelvd = simdis.get_mlDelVD(ic, mlKickVol);//
	           delMD = mldelvd[0];
	           delVD = mldelvd[1];
	           angTop = mldelvd[2];
	           mlKickTopMD[ii] = delMD;
	           MainDriver.mlTsecGas[ii] = 0;      //initialize for additional kick influx calc.
	        }                    //during well kill procedures
	    }
	    mlPVgain = 0;  //initialize for multiple runs, 2/5/2004
	    //txtMLpitGain.setText( "
	    iMLflux = 0;	    

    	cmdFixPump.setVisible(false);
	    if(MainDriver.iCHKcontrol == 1) cmdPumpKill.setVisible(false);
	//.....                 assign formation pressures for multilateral
	    if(MainDriver.igERD == 1){
	       /*FrameML.Visible = True
	    For i = 0 To (MainDriver.igMLnumber - 1)
	       txtPfm(i).Visible = True
	       txtPbML(i).Visible = True
	       lblPfm(i).Visible = True
	       txtPfm(i).setText = Format(MainDriver.mlPform(i) - 14.7, ",0")
	    Next i*/
	    }
	//-------------------------------------------------------
	//.... 7/31/02, change plots background after changing form background
	    /*For i = 0 To 4
	        With Sgraph(i).Backdrop         //for background color change
	          .Fill.Style = VtFillStyleBrush
	          .Fill.Brush.FillColor.Set 0, 250, 100
	        End With
	    Next i
	//
	    vhright = Sgraph(4).Top + Sgraph(4).Height + MainDriver.screnMgin
	    hhright = Sgraph(4).Left + Sgraph(4).Width
	    VScrlscrn.Min = 0; VScrlscrn.Max = MainDriver.screnHt
	    VScrlscrn.SmallChange = 120; VScrlscrn.LargeChange = 1200
	    HScrlscrn.Min = 0; HScrlscrn.Max = MainDriver.screnWd
	    HScrlscrn.SmallChange = 120; HScrlscrn.LargeChange = 1200
	   if MainDriver.screnHt < vhright Then
	      VScrlscrn.Visible = True
	      VScrlscrn.Top = 0
	      VScrlscrn.Left = MainDriver.screnWd - VScrlscrn.Width
	      VScrlscrn.Height = MainDriver.screnHt - HScrlscrn.Height - MainDriver.screnMgin
	   End if
	   if MainDriver.screnWd < hhright Then
	      HScrlscrn.Visible = True
	      HScrlscrn.Top = MainDriver.screnHt - HScrlscrn.Height - MainDriver.screnMgin - 150
	      HScrlscrn.Left = 0
	      HScrlscrn.Width = MainDriver.screnWd - VScrlscrn.Width
	   End if
	//----- save the original positions; top and left, no change on height and width
	    For i = 0 To 3
	        pmoveTop(i) = SFrame(i).Top; pmoveLeft(i) = SFrame(i).Left
	    Next i
	    j = 0
	    For i = 4 To 8
	        pmoveTop(i) = Sgraph(j).Top; pmoveLeft(i) = Sgraph(j).Left
	        j = j + 1
	    Next i
	    pmoveTop(9) = lblPause.Top; pmoveLeft(9) = lblPause.Left
	    pmoveTop(10) = FrameML.Top; pmoveLeft(10) = FrameML.Left*/
	}
	
	/*
	 Private Sub AddKick()
//--- to account the multiple Kicks that may in old mud or in kill mud
Dim n2phOld As Integer
    n2phOld = N2phase
    VOLt = volG(0) + volL(0)   //gas kick and mud volume in bbls
    xbottom = TMD(NwcS); Xbcell = MainDriver.Xnd(1)
    Call getBotH(VOLt, xbottom, hhm)
    xtopnew = xbottom - hhm
//
    iShift = 2
    if volKmud < MainDriver.VOLinn Then
       volkkmix = 0
    Else
       volkkmix = volkkmix + volG(0)    //kill mud top moves as kick adding
       if (xKill > Xbcell) Then
          iShift = 3
       End if
    End if
    if (xtopnew <= Xbcell) Then
       xtopnew = Xbcell
       iShift = 1
    End if
//--- shift array variable to include the new kick !
    For i = (N2phase + iShift) To (iShift + 1) Step -1
        MainDriver.Xnd(i) = MainDriver.Xnd(i - iShift);   Hgnd(i) = Hgnd(i - iShift)
        volL(i) = volL(i - iShift); volG(i) = volG(i - iShift)
        Pnd(i) = Pnd(i - iShift)
        if imud = 0 Then
            pvtZb(i) = pvtZb(i - iShift)
        Else
            PVTZ_Gas(i) = PVTZ_Gas(i - iShift)
        End if
    Next i
    if iShift = 2 Then
//--- assign the new cell properties !; X-node and Volume of Liquid
//    pressure is not required - just assign any value !
       //Hgnd(3) = 0; volG(3) = 0; pvtZb(3) = 0; MainDriver.Xnd(3) = xbcell
       Hgnd(3) = 0; volG(3) = 0; MainDriver.Xnd(3) = Xbcell
       if imud = 0 Then
            pvtZb(3) = 0
       Else
            PVTZ_Gas(3) = 0
       End if
       
       Call GetVolume(Xbcell, xtopnew, volMid)
       volL(3) = volMid + 0.01 //just prevent dummy cell
    Elseif (iShift = 3) Then
       //Hgnd(4) = 0; volG(4) = 0; pvtZb(4) = 0; MainDriver.Xnd(4) = xbcell
       Hgnd(4) = 0; volG(4) = 0; MainDriver.Xnd(4) = Xbcell
       if imud = 0 Then
            pvtZb(4) = 0
       Else
            PVTZ_Gas(4) = 0
       End if
       Call GetVolume(Xbcell, xKill, volMid)
       volL(4) = volMid + 0.01   //just prevent dummy cell
       //Hgnd(3) = 0; volG(3) = 0; pvtZb(3) = 0; MainDriver.Xnd(3) = xKill
       Hgnd(3) = 0; volG(3) = 0; MainDriver.Xnd(3) = xKill
       if imud = 0 Then
            pvtZb(3) = 0
       Else
            PVTZ_Gas(3) = 0
       End if
       Call GetVolume(xKill, xtopnew, volMid)
       volL(3) = volMid + 0.01   //just prevent dummy cell
    End if
//--- assign the new kick properties in the kicl cell
12345 dummy = 0
    Call GasProp(Pbeff, TbRankin, gasvisb, gasdenb, zb)
    MainDriver.Xnd(2) = xtopnew;  Hgnd(2) = volG(0) / VOLt
    volL(2) = volL(0); volG(2) = volG(0); Pnd(2) = Pbeff
    //pvtZb(2) = Pbeff * volG(2) / (zb * TbRankin)
    if imud = 0 Then
    pvtZb(2) = Pbeff * volG(2) / (zb * TbRankin)
    Else
        R = 10.73
        PVTZ_Gas(2) = Pbeff * volG(2) / (zb * TbRankin) * R / 5.615
    End if
//--- assign the bottom value
    MainDriver.Xnd(1) = xbottom; Hgnd(1) = 0; volL(1) = 0
    //volG(1) = 0; Pnd(1) = Pbeff;  pvtZb(1) = 0
    volG(1) = 0; Pnd(1) = Pbeff
    if imud = 0 Then
        pvtZb(1) = 0
    Else
        PVTZ_Gas(1) = 0
    End if
    
    N2phase = n2phOld + iShift
    Xb = xbottom
End Sub

Private Sub AddOmud()
//--- to account the multiple Kicks that may in old mud or in kill mud
//    and to cilculate out the old mud for the complete well control
Dim n2phOld As Integer
    iShift = 0
    if volKmud > MainDriver.VOLinn Then Exit Sub
//--- assign the new cell properties !; X-node and Volume of Liquid
//    pressure is not required - just assign any value !
//   calculate the old mud volume based on geomerty because of slipped mud ! - 2/8/95
    xbottom = TMD(NwcS); xxx = MainDriver.Xnd(2)
    Call GetVolume(xxx, xbottom, volppslip)
//
//    volumeadd = qkill * MainDriver.gDelT / (42 * 60)     //this is incorrect due to slipped mud
    volumetot = volppslip
    volcrit = 0.1 * VOLout  //--- 10 old mud cells to save the time !
    if Hgnd(2) < 0.00001 And volumetot < volcrit Then
       Hgnd(2) = 0; volL(2) = volumetot    //only mud volume increases !
       volG(2) = 0; pvtZb(2) = 0
    Else
       n2phOld = N2phase
       For i = N2phase To 1 Step -1
          MainDriver.Xnd(i + 1) = MainDriver.Xnd(i); Hgnd(i + 1) = Hgnd(i)
          volL(i + 1) = volL(i); volG(i + 1) = volG(i)
          Pnd(i + 1) = Pnd(i);  pvtZb(i + 1) = pvtZb(i)
       Next i
//--- assign the new cell properties !; X-node and Volume of Liquid
//    pressure is not required - just assign any value !
//--- assign the new kick properties in the kicl cell
       xxx = MainDriver.Xnd(1)
       Call GetVolume(xxx, xbottom, volppslip)
       Hgnd(2) = 0; volL(2) = volppslip
       volG(2) = 0; pvtZb(2) = 0
       N2phase = n2phOld + 1
    End if
//--- assign the bottom value
     Hgnd(1) = 0; volL(1) = 0;  MainDriver.Xnd(1) = xbottom
     volG(1) = 0; pvtZb(1) = 0; Pnd(1) = Pbeff
End Sub


Private Sub Assign)
//  Assign the calculated values
    timeNp = 0
    Np = Np + 1
    MainDriver.xTop[Np) = xVert;   MainDriver.xBot[Np) = xbVert; MainDriver.pxTop[Np) = px
    rhoK(Np) = gasDen;  MainDriver.Psp[Np) = SPP
    MainDriver.Pcsg[Np) = Pcasing; MainDriver.Pb2p[Np) = Pbeff;  TTsec(Np) = gTcum
    MainDriver.Pchk[Np) = Pchoke;  MainDriver.Vpit[Np) = PVgain; QmcfDay(Np) = QgDay
    MainDriver.Ppump[Np) = pumpP;    CHKopen(Np) = CHKpcent
    VOLcir(Np) = MainDriver.volPump; Stroke(Np) = MainDriver.volPump / Qcapacity
//
//............. adjust wellbore pressures in surface choke pressure is negative, 7/11/02
    gasFlow(Np) = gasFlowOut    //addition of variables, 7/17/02
    mudFlow(Np) = mudFlowOut
    PmLine(Np) = mudLineP
//
//--- assign user//s choke pressure result as pump pressure observation !
    if (volKmud <= MainDriver.VOLinn + 20.5) And iDone >= 1 Then
       iDone = iDone + 1
       if iDone < 181 Then
         vKmudSt(iDone) = Int(volKmud / Qcapacity); Pcontrol(iDone) = pumpP
       End if
    End if
//--- check the possible out of array
    if Np >= Npt - 2 Then
       MsgBox "You spent too much time.  Arrays are out of Range !", vbSystemModal + 0, msgTitle
       Unload SimManual
    End if
End Sub

Private Sub Conversion_Click()
    if imode = 1 Then
       imode = 2 //Manual to Automatic
       txtSIMmode.setText( "Automatic"
    Else
       imode = 1 //Automatic to Manual
       txtSIMmode.setText( "Manual"
            End if
End Sub

Private Sub cmdStkZero_Click()
    //kb 130701
    //Reset_Pump_Stroke = 0
    Now_Pump_Stroke = Format(txtStroke.setText, ".00")
End Sub

Private Sub Form_Activate()
   iBOP = 0   //to prevent ibop = 1 reset when Main Menu is activated
End Sub

Private Sub l_remoteSock_DataArriByVal bytesTotal As Long)

    Dim Str_Buf As String
    Dim RequestType As Integer
    Dim CHKValue As Integer
    Dim ReqType
    l_remoteSock.GetData Str_Buf

    ReqType = Split(Str_Buf, ",")
    
  typ = ReqType(3)
if typ = "S" Then
    if ReqType(4) = "1" Then
    Else
        l_remoteSock.Close
    
        MainMenu.Show
        Unload SimManual
    End if
Elseif typ = "H" Then
    CHKValue = CInt(ReqType(19))
    HScrollCHK.value = CInt(ReqType(19))
    txtCHKopen.setText( CInt(ReqType(19))
    
   SimRate = CInt(ReqType(21))
   txtSimRate.setText( ReqType(21)
   HScrollRate.value = CInt(ReqType(21))
   
   
    if CInt(ReqType(20)) = 1 Then
        Call menuCirculation_Click
        Call menuContinue_Click
        //MsgBox ("menuCirculation_Click")
    Elseif CInt(ReqType(20)) = 0 Then
        Call menuPause_Click
        //MsgBox ("menuPause_Click")
    End if
    
    if CInt(ReqType(23)) = 1 Then
            //MsgBox ("cmdPumpKillMud_Click")
            if pumpon = 0 Then //HS
             Call cmdPumpKillMud_Click
             pumpon = 1
             IsPPflag = True
             
            End if
    End if
    
    //Added by TY
    if CInt(ReqType(24)) = 1 Then //Manual mode
        imode = 1
        optAutomatic2.value = False
        optManual2.value = True
        txtSIMmode.setText( "Currently choke is controlled by Manual mode"
    Else //Automatic mode
        imode = 2
        optAutomatic2.value = True
        optManual2.value = False
        txtSIMmode.setText( "Currently choke is controlled by Automatic mode"
    End if
    
    //Call HTouchCHK(CHKValue)
    //HScrollCHK.value = CHKValue
    //txtCHKopen.setText( ReqType(19)
    
    //Edded EJ
    
    if CInt(ReqType(18)) = 1 Then
        Call cmdStkZero_Click
    End if
        
//
End if
End Sub

 

 

Private Sub menuGraphInterval_Click()
Dim grpintdef As Variant
    grpintdef = grpint
    temp = "Enter time ionterval to update graph plots (in every 1 to 30 minutes)"
    xx = InputBox(temp, msgTitle, grpintdef)  //specify current value
//
 //   if (iWshow = 1 And idclk = 0) Then WellPic.Show
    if xx = "" Then Exit Sub
    grpint = Int(xx + 0.5)
//
    if grpint > 30 Then grpint = 30
    if grpint < 1 Then grpint = 1
End Sub

Private Sub menuGraphHelp_Click()
    msgTmp$ = "Double-Click on the Graph to Enlarge it, or Click once to Reduce it !"
    MsgBox msgTmp$, 0, msgTitle
End Sub

Private Sub menuHelpMain_Click()
    msgTmp$ = "From the Menus menu, you can load/unload the wellbore profile.  You can move or adjust the size of this wellbore profile."
    + "\n" +"  In order to start the simulator open choke valve and click the Start-Circulation sub menu under the Menus menu."
    + "\n" +"  Then you should adjust the choke valve using the horizontal scale bar in the Choke Control Panel for the successful well control."
    + "\n" +"  You could have multiple kicks or underground blowout depending on your actions."
    + "\n" +"  You can change the Simulation Rate any time, but a low rate is recommended !"
    + "\n" +"  ^.^ GOOD LUCK ! ^.^"
    MsgBox msgTmp$, 0, msgTitle
End Sub

Private Sub menuHideWellbore_Click()
    WellPic.Hide; iWshow = 0
End Sub

Private Sub menuGraphPlot_Click()
    PlotG4
   // if (iWshow = 1 And idclk = 0) Then WellPic.Show
End Sub

Private Sub menuContinue_Click()
    if (iContinue <> 1) Then
   //    if (iWshow = 1 And idclk = 0) Then WellPic.Show
       Exit Sub
    End if
//
    iContinue = 0
    if (iTcirc = 1) Then TimerCirc.Enabled = True
    iTcirc = 0
   // if (iWshow = 1 And idclk = 0) Then WellPic.Show
    timeOld = Hour(Now) * 3600 + Minute(Now) * 60 + Second(Now)
    operationMsg.setVisible(false);
End Sub

Private Sub menuPause_Click()
    iContinue = 1
    if (TimerCirc.Enabled = True) Then
        iTcirc = 1; TimerCirc.Enabled = False
    End if
   // if (iWshow = 1 And idclk = 0) Then WellPic.Show
    operationMsg.setVisible(true);
End Sub

Private Sub cmdPumpKillMud_Click() //start to pump kill mud
    if TimerCirc.Enabled = False Then
       MsgBox "You have the WRONG sequence !", vbSystemModal + 0, msgTitle  //메세지 수정함
    //   if (iWshow = 1 And idclk = 0) Then WellPic.Show
       Exit Sub
    End if
//
    //kb 130704
    Start_Pump_Stroke = Format(txtStroke.setText, ".00")
    
    volKmud = 0; iDone = 1; vKmudSt(1) = 0; Pcontrol(1) = pumpP  //pumpP is a pump pressure.
    Method = 2; gMudCirc = MainDriver.gMudKill; Cmud = Kmud   //Now start Engineer//s Method
    cmdPumpKillMud.setVisible(false);
    timeOld = Hour(Now) * 3600 + Minute(Now) * 60 + Second(Now)
 //   if (iWshow = 1 And idclk = 0) Then WellPic.Show
End Sub

Private Sub cmdFixPump_Click()
    TimerCirc.Enabled = True
    cmdFixPump.setVisible(false);
End Sub

Private Sub DrawMudGauge(GaugeIndex As Integer, GaugeValue) // As Long)
Dim DrawAngle, Xcenter, Ycenter, Radius, Xoriginal
//..... This is necessary for VB v. 6
//      Use a circle and a line to represent a gauge
// 7/25/2001
//    Line1.BorderWidth = 3
      Radius = 520
      Xcenter = 1320
//
      Select Case GaugeIndex
      Case 1   //mud return rate
         DrawAngle = 90 * GaugeValue / 250 * radConv    //radCov is a global variable
         Ycenter = 1200
         Xcalculated = Xcenter - Radius * Cos(DrawAngle)
         Ycalculated = Ycenter - Radius * Sin(DrawAngle)
         LineMud.X1 = Xcalculated
         LineMud.Y1 = Ycalculated
         LineMud.x2 = Xcenter
         LineMud.Y2 = Ycenter
      Case 2    //surface choke pressure
         DrawAngle = 90 * GaugeValue / 1000 * radConv
         Ycenter = 3360
         Xcalculated = Xcenter - Radius * Cos(DrawAngle)
         Ycalculated = Ycenter - Radius * Sin(DrawAngle)
         LineCHK.X1 = Xcalculated
         LineCHK.Y1 = Ycalculated
         LineCHK.x2 = Xcenter
         LineCHK.Y2 = Ycenter
      Case 3    //standpipe pressure
         DrawAngle = 90 * GaugeValue / 1000 * radConv
         Ycenter = 5520
         Xcalculated = Xcenter - Radius * Cos(DrawAngle)
         Ycalculated = Ycenter - Radius * Sin(DrawAngle)
         LineSP.X1 = Xcalculated
         LineSP.Y1 = Ycalculated
         LineSP.x2 = Xcenter
         LineSP.Y2 = Ycenter
      Case Else
      End Select
End Sub

Private Sub DrawMultiKick() //Draw multiple kicks !
    //Modified by TY
    //For manual & auto modes
    VOLkick = 0
    //For i = N2phase To 3 Step -1
    For i = N2phase To 2 Step -1
        if (Hgnd(i) > 0.001 Or PVTZ_Gas(i) > 0.001) Then
            //VOLkick = VOLkick + volG(i) + volL(i)
            VOLkick = VOLkick + volG(i) + volL(i) + Vsol(i)
        Else
            xbottom = MainDriver.Xnd(i)
            Call DrawKick(VOLkick, xbottom)
            VOLkick = 0
        End if
    Next i
    //xxx = xbcell
    
    //if iShift = 0 Then
        xxx = MainDriver.Xnd(1)
    //Else
    //xxx = MainDriver.Xnd(2)
    //End if
    Call DrawKick(VOLkick, xxx)
        
    if (iShift = 0) Then Exit Sub
//--- draw the new kick at the bottom only
//Multi kick이 처음에 발생할 때만 그려주는 코드..
//    if (hgnd(2) > .001) Then    //This is true if there is a kick ie ishift >=1
       VOLkick = volG(2) + volL(2)
       xbottom = TMD(NwcS)
       Call DrawKick(VOLkick, xbottom)
//    End if
End Sub

Private Sub EnLarge()
    iHmove = HScrlscrn.value; iVmove = VScrlscrn.value
    enXpos = pmoveLeft(iGraph + 4)
    enYpos = pmoveTop(iGraph + 4)
    pmoveLeft(iGraph + 4) = Gleft
    pmoveTop(iGraph + 4) = Gtop
//
    OTop = Sgraph(iGraph).Top; OLeft = Sgraph(iGraph).Left
    OHeit = Sgraph(iGraph).Height; OWidth = Sgraph(iGraph).Width
    idclk = 1
    sFrame(1).setVisible(false);
    For i = 0 To 4
        Sgraph(i).setVisible(false);
    Next i
//
    Sgraph(iGraph).Left = Gleft;   Sgraph(iGraph).Top = Gtop
    Sgraph(iGraph).Height = Gheit; Sgraph(iGraph).Width = Gwidth
    Sgraph(iGraph).Title.VtFont.Size = 16
    Sgraph(iGraph).setTextLengthType = 1  //fixed (0) & variable (1)
    Sgraph(iGraph).setVisible(true);  //visible should be the last command
End Sub
//Added EJ
Private Sub l_localSock_ConnectionRequest(ByVal requestID As Long)
    if l_localSock.State <> 0 Then l_localSock.Close
    l_localSock.Accept requestID
    localListen.setText( requestID & "listen 접속 성공"
    
End Sub
//Added EJ

//Added EJ
Public Function ParserData(aData As String) As Integer
    Dim ReqType
    ReqType = Split(aData, ",")
    //ParserData = CInt(ReqType(5))
End Function
//Added EJ
Public Sub ConnectOK(aData As String)
    Dim mConnect As TConnect
    Dim SendData As String
    
    mConnect.ETX = "ETX"
    mConnect.State = 1
    mConnect.RequsetType = 2
    mConnect.TargetIP = "127.0.0.1"
    mConnect.StartIP = "127.0.0.1"
    mConnect.SendSystem = "SIM"
    mConnect.Stamp = "WCS1"
    mConnect.STX = "STX"
    SendData = mConnect.STX & "," & mConnect.Stamp & "," & mConnect.SendSystem & "," & mConnect.StartIP & "," & mConnect.TargetIP & "," & mConnect.RequsetType & "," & mConnect.State & "," & mConnect.ETX
    l_localSock.SendData SendData
    
End Sub
//Added EJ
Public Sub STATUSOK(aData As String)
    Dim mConnect As TConnect
    Dim SendData As String
    mConnect.ETX = "ETX"
    mConnect.State = 1
    mConnect.RequsetType = 4
    mConnect.TargetIP = "127.0.0.1"
    mConnect.StartIP = "127.0.0.1"
    mConnect.SendSystem = "SIM"
    mConnect.Stamp = "WCS1"
    mConnect.STX = "STX"
    SendData = mConnect.STX & "," & mConnect.Stamp & "," & mConnect.SendSystem & "," & mConnect.StartIP & "," & mConnect.TargetIP & "," & mConnect.RequsetType & "," & mConnect.State & "," & mConnect.ETX
    l_localSock.SendData SendData
    
End Sub

Private Sub ConnectLocalSock()
    Dim i As Integer
    On Error Resume Next
    
       
    if l_localSock.State = sckListening Or l_localSock.State = sckConnected Then
        l_localSock.Close
  
    Else
        l_localSock.Close
     
        l_localSock.LocalPort = 8301
        //l_localSock.LocalPort = 8301
        l_localSock.Listen
        DoEvents
        if Err.Number <> 0 Then
            Err.Clear
            l_localSock.Close
            
        Else
            
        End if
    End if
    
End Sub

Private Sub Remote_Connect()
    Dim iCount As Integer
    if l_remoteSock.State = sckConnected Then
        l_remoteSock.Close
        Remote_Connect
    Else
        l_remoteSock.RemoteHost = "127.0.0.1"
        l_remoteSock.RemotePort = 8302
        l_remoteSock.Connect
        DoEvents
        
        Delay (0.1)
        if l_remoteSock.State = 6 Then
            remoteConnect.setText( "접속중"
            Delay (1)
        Elseif l_remoteSock.State = sckConnected Then
            remoteConnect.setText( "접속 완료"
        Else
            l_remoteSock.Close
            remoteConnect.setText( "접속 실패"
            
            ReConnect

        End if
    End if
End Sub
Private Sub l_remoteSock_Error(ByVal Number As Integer, Description As String, ByVal Scode As Long, ByVal Source As String, ByVal HelpFile As String, ByVal HelpContext As Long, CancelDisplay As Boolean)
ReConnect
End Sub
 Public Function ReConnect()
    l_remoteSock.Close
    remoteConnect.setText( "재접속중"
    Delay (1)
    
    Remote_Connect
 End Function
 Public Function Delay(xxx As Single) As Single
    Dim MyTimer As Double
    MyTimer = Timer
    Dim temp As String
    temp = CStr(Date) + CStr(Time)
    Do
    Loop Until Timer - MyTimer > xxx
 End Function
Private Sub Form_Load() // assign SI or Continuous SI data
IsPPflag = False
kickcount = 0 //hs
pumpon = 0 //hs

Dim j As Integer

    Remote_Connect
    //ConnectLocalSock
    SimManual.WindowState = 1 //Minimize added by TY
    
    txtSIMmode.setText( "Currently choke is controlled by Manual mode"
    //imode = 1
    imode = 2 //automatic
    //SimManual.WindowState = 2
    TimerCirc.Enabled = False
    operationMsg.setVisible(false);   //used as a message window, 7/17/02
//---------- assign the final SI conditions from 2-phase calculation
    X = MainDriver.Xnd[MainDriver.N2phase]); Xb = MainDriver.Xnd(1)); xVert = MainDriver.xTop[MainDriver.NpSi))
    xbVert = MainDriver.xBot[MainDriver.NpSi)); px = MainDriver.pxTop[MainDriver.NpSi)); PVgain = MainDriver.Vpit[MainDriver.NpSi))
    Pchoke = MainDriver.Pchk[MainDriver.NpSi)); Pcasing = MainDriver.Pcsg[MainDriver.NpSi)); Pbeff = MainDriver.Pb2p[MainDriver.NpSi))
    SPP = MainDriver.Psp[MainDriver.NpSi)); pumpP = MainDriver.Ppump[MainDriver.NpSi))
//    Tdelay = TTsec(MainDriver.NpSi))
    MainDriver.volPump = 0; volKmud = 0; MainDriver.QtMix = 0; psia = 14.7
    volkkmix = 0
//............................................... 7/17/02
    mudLineP = Pchoke + MainDriver.gMudOld * MainDriver.Dwater //sea btm pressure
    gasFlowOut = 0
    mudFlowOut = 0
    
    ShowResult
    txtPumpRate.setText( "0.0"; txtCHKopen.setText( "0"
//
//-------------------------------------------- modifications after 7/1/02 for ML and ERD.
    SimRate = 5  //10   //set a default value for simulation rate - 7/11/02
//    Np = MainDriver.NpSi      //This is necessary to save shutin information retrived; considered in DrillSI
//
    txtSimRate.setText( Str(SimRate); HScrollRate.value = SimRate
    //if imud = 0 Then
        QtotMix = MainDriver.Vpiti
    //Else
        //QtotMix = 0
        //For i = N2phase To 1
            //QtotMix = QtotMix + volG(i)
        //Next
    //End if
    QgDay = 0; volMix = QtotMix
    grpint = 5; timeNp = 0; mudCase = 0
    PlotKillSheet
    
    Call DrawMudGauge(1, 0)    //return rate of mixture
    Call DrawMudGauge(2, Pchoke - psia)    //surface choke pressure
    Call DrawMudGauge(3, SPP - psia)    //surface standpipe pressure
    
    cmdPumpKillMud.setVisible(true);; cmdFixPump.setVisible(false);
    iWshow = 1    //temperory hide the wellbore
//--------- set the graph maximun
    Gleft = 1920; Gtop = 1560  //gheit = 6735; gwidth = 9975
    Gheit = screnHt - Gtop - screnMgin - 120
    Gwidth = screnWd - Gleft - 120
    OHeit = 1335; OWidth = 3495  //; oleft = 8400
    idclk = 0
//---------------------------------------- Additional calculations
    nHgCon = 2  //just use this value
//---------------------------------------------------------
//.... 7/31/02, change plots background after changing form background
    For i = 0 To 4
        With Sgraph(i).Backdrop         //for background color change
          .Fill.Style = VtFillStyleBrush
          .Fill.Brush.FillColor.Set 0, 250, 100
        End With
    Next i
//
    //Mode 정보
    //SendData = "STX,WCS1,M,1,ETX"
    
    //Dim sData As String
    //Dim bData() As Byte
    //strLen = Len(SendData)
    //strLen = strLen + 4
    //Dim ba(0 To 3) As Byte
    //ba(0) = strLen Mod 256
    //For i = 1 To 3
    // if strLen \ (256 ^ i) > 255 Then
    //  ba(i) = 255
    // Else
    //  ba(i) = strLen \ (256 ^ i)
    //  End if
    //Next i
    //bData = StrConv(SendData, vbFromUnicode)
    //l_remoteSock.SendData ba
    //l_remoteSock.SendData bData
    
    
    vhright = Sgraph(4).Top + Sgraph(4).Height + screnMgin
    hhright = Sgraph(4).Left + Sgraph(4).Width
    VScrlscrn.Min = 0; VScrlscrn.Max = screnHt
    VScrlscrn.SmallChange = 120; VScrlscrn.LargeChange = 1200
    HScrlscrn.Min = 0; HScrlscrn.Max = screnWd
    HScrlscrn.SmallChange = 120; HScrlscrn.LargeChange = 1200
   if screnHt < vhright Then
    VScrlscrn.setVisible(true);
    VScrlscrn.Top = 0
    VScrlscrn.Left = screnWd - VScrlscrn.Width
    VScrlscrn.Height = screnHt - HScrlscrn.Height - screnMgin
   End if
   if screnWd < hhright Then
    HScrlscrn.setVisible(true);
    HScrlscrn.Top = screnHt - HScrlscrn.Height - screnMgin - 150
    HScrlscrn.Left = 0
    HScrlscrn.Width = screnWd - VScrlscrn.Width
   End if
//----- save the original positions; top and left, no change on height and width
    For i = 0 To 3
    pmoveTop(i) = sFrame(i).Top; pmoveLeft(i) = sFrame(i).Left
    Next i
    j = 0
    For i = 4 To 8
    pmoveTop(i) = Sgraph(j).Top; pmoveLeft(i) = Sgraph(j).Left
    j = j + 1
    Next i
    pmoveTop(9) = cmdPumpKillMud.Top; pmoveLeft(9) = cmdPumpKillMud.Left
    pmoveTop(10) = cmdFixPump.Top; pmoveLeft(10) = cmdFixPump.Left
    

    Send_Data
End Sub

Private Sub Form_Unload(Cancel As Integer)
    iWshow = 0; volMix = 0; Xb = 0
    TimerCirc.Enabled = False
End Sub

Private Sub GetVolume(Xup, Xdown, volMid)
Static iPos As Integer, iPup As Integer
//
// the new subroutine to calculate the volume in the annulus based on the
// given two points.  this is generally good.
// xup;the given top (MD), ft  volmid;total volume, bbls
//
    volMid = 0
    if Xup > (Xdown - 0.01) Then Exit Sub
    Call Xposition(Xdown, iPos)   //identify the location of the given bottom
    Call Xposition(Xup, iPup)
//.............................. calculate MD by the given volume, bbls
    Qflow = 0
    if iPos = iPup Then
       capcty = C12 * (Do2p(iPos + 1) ^ 2 - Di2p(iPos + 1) ^ 2)
       if (iPos = 9) Then Call getLines(Qflow, d2, d1, Qeff, capcty, volEff)
       volMid = capcty * (Xdown - Xup)
    Else
       Htmp = Xdown - TMD(iPos + 1)
       For i = (iPos + 1) To iPup
          capcty = C12 * (Do2p(i) ^ 2 - Di2p(i) ^ 2)
          volMid = volMid + capcty * Htmp
          Htmp = TMD(i) - TMD(i + 1)
       Next i
       capcty = C12 * (Do2p(iPup + 1) ^ 2 - Di2p(iPup + 1) ^ 2)
       if (iPup = 9) Then Call getLines(Qflow, d2, d1, Qeff, capcty, volEff)
       Htmp = TMD(iPup) - Xup
       volMid = volMid + capcty * Htmp
    End if
//
End Sub

Private Sub GetXkill()
    xKill = TMD(NwcS)
    if (volKmud < MainDriver.VOLinn) Then Exit Sub
    volEff = volKmud - MainDriver.VOLinn + volkkmix  //(kick expansion in kill mud)
////    voleff = volkmud - MainDriver.VOLinn
    Call getBotH(volEff, xKill, hkill)
    xKill = TMD(NwcS) - hkill           //depth of kill mud
End Sub

Private Sub Golden2()    //--- AWC version for the MultiPle Kicks
Static iLoc As Integer   //--- Modified on 7-04-94 !
//--- direct calculation of Px & BHP from DP@choke control,
//    DP_hydrostatic, DP_friction
    if CHKpcent = 0 Then       //check complete choke close !
       MsgBox "Pump failure ! Choke opening value is zero. You should open the choke.", vbSystemModal + 0, msgTitle
       TimerCirc.Enabled = False; cmdFixPump.setVisible(true);
       Exit Sub
    End if
//
    Call Bkup
    cd = 0.6
    rhoL = oMud
    if xKill < 0.2 Then rhoL = Kmud
    totArea = 0.25 * pai * (CHKpcent * DchkControl * 0.01) ^ 2  //open %
    if (X > 0.01) Then          //single-phase flow
       dpchoke = 0.00008311 * rhoL * MainDriver.QtMix * MainDriver.QtMix / (totArea * cd) ^ 2
    Else
       hgX = Hgnd[MainDriver.N2phase]
       kcpcv = 1.4
       rhom = gasDen * hgX + rhoL * (1 - hgX)
       yy = 1 - (0.41 + 0.35 * (CHKpcent) ^ 4 * 0.00000001) / kcpcv
       if (yy < 0.5) Then yy = 0.5 //DP is too big for small yy
       if (hgX < 0.01) Then yy = 1
       dpchoke = 0.00008311 * rhom * MainDriver.QtMix * MainDriver.QtMix / (totArea * cd * yy) ^ 2
    End if
    Pchoke = dpchoke + 14.7
//--- direct calculation of Px
//--------------------------------------------------------- acceleration term
      delvol = (PVgain - MainDriver.Vpit[Np - 1)) * 42 * 60 / (gTcum - TTsec(Np) + 1)
      Call range(delvel, 0, Qkill * 2.5)
      delvol2 = delvol / 2.448
      Call Xposition(X, iLoc)
      DPacc = 0; xmdtmp = X - TMD(iLoc + 1)
      For i = (iLoc + 1) To 9
         d2 = Do2p(i); d1 = Di2p(i)
         DPacc = DPacc + xmdtmp * delvol2 / (d2 * d2 - d1 * d1)
         xmdtmp = TMD(i) - TMD(i + 1)
      Next i
//............................................. for choke or kill lines
      Call getLines(MainDriver.QtMix, d2, d1, Qeff, cap2eff2, volEff)
      DPacc = DPacc + xmdtmp * delvol2 / (d2 * d2 - d1 * d1) * Qeff / MainDriver.QtMix
      psicon = 0.001614678
      DPacc = psicon * oMud * DPacc / (gTcum - TTsec(Np) + 1)
      if (X >= xKill - 0.01) Then  //use the fraction
         fract = xKill / (X + 0.1)
         dpaccel = DPacc * fract + DPacc * Kmud / oMud * (1 - fract)
         DPacc = dpaccel
      End if
//--------------------------------------------------------------------------
    Call getVD(X, xVert)
    Call getVD(xKill, xkillvd)
    if (xKill > X) Then
       Call getDP(MainDriver.QtMix, X, oMud)
       pxx = Pchoke + MainDriver.gMudOld * xVert + Pcon * DPtop
    Else
       Call getDP(MainDriver.QtMix, xKill, oMud)
       pxdp = DPtop
       Call getDP(MainDriver.QtMix, xKill, Kmud)
       pxdp = pxdp - DPtop
       Call getDP(MainDriver.QtMix, X, Kmud)
       pxdp = pxdp + DPtop
       pxx = Pchoke + MainDriver.gMudOld * xkillvd + MainDriver.gMudKill * (xVert - xkillvd) + Pcon * pxdp
    End if
    px = pxx + DPacc
    if N2phase >= 2 Then
       Call Pbcal2
       Call getVD(X, xVert)
       txvd = temperature(xVert)
       Call GasProp(px, txvd, gasVis, gasDen, zz)
    Else
       Call pxBottom(volKmud, 0, Pbtm)
       DPdiff = Pb - Pbtm; PbTry = Pchoke + DPdiff
       Xb = 0; xVert = 0; gasDen = 0
    End if
    Pbeff = PbTry  //no iteration is necessary - direct calculation
    
//.......................................... calculate other variables
      //For ii = 2 To N2phase
         //tmp2 = volL(ii); tmp3 = volLqd(ii); volL(ii) = Max(tmp2, tmp3)
      //Next ii
//...................... calculate pump pressure and stand pipe pressure
      Call getDPinside(Pbeff, Qkill, volKmud, SPP, pumpP)
//....................................... calculate casing seat pressure
      if (DepthCasing > Xb) Then
         Call pxBottom(volKmud, DepthCasing, Pbtm)
         DPdiff = Pb - Pbtm
         Pcasing = Pbeff - DPdiff
      Elseif (DepthCasing > X) Then
         Pcasing = DitplDes(DepthCasing)
      Else
         Call getDP(MainDriver.QtMix, DepthCasing, oMud)
         Pcasing = Pchoke + MainDriver.gMudOld * TVD(iCsg) + Pcon * DPtop
      End if
//
//--- calculate Qt-mix for the next calculation, 7/17/02
//........... old approach gives very small return rate increase since average from beginning
//      MainDriver.QtMix = Qkill + (PVgain - MainDriver.Vpit[MainDriver.NpSi)) * 42 * 60 / (gTcum - Tdelay + 2)
//      Call range(MainDriver.QtMix, Qkill, Qkill * 2.5)
      if Np <= MainDriver.NpSi + 10 Then
         MainDriver.QtMix = Qkill + (PVgain - MainDriver.Vpit[MainDriver.NpSi)) * 42 * 60 / (gTcum - TTsec(MainDriver.NpSi) + 1)
      Else
         //MainDriver.QtMix = Qkill + (PVgain - MainDriver.Vpit[Np - 4)) * 42 * 60 / (gTcum - TTsec(Np - 4))
         //Modified by TY
         MainDriver.QtMix1 = Qkill + (PVgain - MainDriver.Vpit[Np - 4)) * 42 * 60 / (gTcum - TTsec(Np - 4))
         MainDriver.QtMix2 = Qkill + (PVgain - MainDriver.Vpit[Np - 2)) * 42 * 60 / (gTcum - TTsec(Np - 2))
         MainDriver.QtMix = Min(MainDriver.QtMix1, MainDriver.QtMix2) //constraint chk pressure oscillation
      End if
      Call range(MainDriver.QtMix, Qkill, Qkill * 4.5)
//--- check the possible multiple kicks and assign in the node
//    these result will be accounted in the NEXT calculation !
//----------------------------------------------- 2nd kick
    volG(0) = 0; volL(0) = 0
    
    QgDay = GASinflux(Pbeff, MainDriver.gDelT, Hdrled)    // flow rate in Mscf/Day
    qgrgpm = 29.166667 * QgDay * fvfGas       // convert to reservoir volume
    qgtbbl = qgrgpm * MainDriver.gDelT / (42 * 60)
    
    // multikick 여부 설정하는 곳 //kb 130625
   // if imultikick = 1 Then
        iii = 0 //hs
        if (qgtbbl < 0.01) Then
            iii = iii + 1 //hs
            QgDay = 0
            Call AddOmud
        Else
            iii = iii + 1 //hs
            volG(0) = qgtbbl
            volL(0) = Qkill * MainDriver.gDelT / (60 * 42)
            Call AddKick
        End if
   // End if

End Sub

Private Sub HScrlscrn_Change()
Dim j As Integer
    iMove = HScrlscrn.value
    For i = 0 To 3
        sFrame(i).Move (pmoveLeft(i) - iMove)
    Next i
    j = 0
    For i = 4 To 8
        if (j <> iGraph) Or idclk = 0 Then
           Sgraph(j).Move (pmoveLeft(i) - iMove)
        End if
        j = j + 1
    Next i
    if idclk = 1 Then
       Sgraph(iGraph).Move (pmoveLeft(iGraph + 4) - iMove + iHmove)
    End if
    cmdPumpKillMud.Move (pmoveLeft(9) - iMove)
    cmdFixPump.Move (pmoveLeft(10) - iMove)
End Sub

Private Sub HScrollchk_Change()
    txtCHKopen.setText( Str$(HScrollCHK.value)
   // if (iWshow = 1 And idclk = 0) Then WellPic.Show
End Sub

Private Sub HScrollRate_Change()
    SimRate = HScrollRate.value)
    txtSimRate.setText( Str$(SimRate)
   // if (iWshow = 1 And idclk = 0) Then WellPic.Show
End Sub

Private Sub menuCirculation_Click()
    if HScrollCHK.value = 0 And imode = 1 Then //Manual mode인데 chk opening을 0으로 놓고 "Start circulation" 버튼을 누른 경우
       msgTmp$ = "Oh, Boy !  You made a serious mistake !"
       MsgBox msgTmp$, vbSystemModal + 0, msgTitle
       //if (iWshow = 1 And idclk = 0) Then WellPic.Show
       Exit Sub
    End if
//
  //  if (iWshow = 1 And idclk = 0) Then WellPic.Show
   // if (iWshow <> 1) Then Unload WellPic
    
    TimerCirc.Enabled = True
    timeStart = Hour(Now) * 3600 + Minute(Now) * 60 + Second(Now)
    MainDriver.QtMix = Qkill; timeOld = timeStart; mudCase = -99
End Sub

Private Sub menuMain_Click()
    MainMenu.Show
    Unload SimManual
    Unload WellPic
    Method = MethodOrig
End Sub

Private Sub menuShowWellbore_Click()
    WellPic.Show; iWshow = 1
    if (Np = MainDriver.NpSi) Then
       Call DrawKick(QtotMix, Xb)
       drawWellbore
    End if
End Sub

Private Sub NewN2ph(timeAdd) //sub to calculate the new n2phase
Dim n2phOld As Integer, Remains As Integer
//
    if N2phase = 1 Then Exit Sub
//
    delTsum = 0; n2phOld = N2phase
    For i = N2phase To 2 Step -1
       x2Top = MainDriver.Xnd(i); x2Bot = MainDriver.Xnd(i - 1); hgX = Hgnd(i - 1)
       Call calcDt(x2Top, x2Bot, hgX, timeint)
       delTsum = delTsum + timeint
//--- calculate the time based on one cell length & GT 2nd cell
       if (delTsum > timeAdd) Then
          Remains = Int(delTsum - timeAdd)
          if (Remains <= 2) Then
             N2phase = i - 1
          Else
             N2phase = i
             if imud = 0 Then //WBM
             pvtZb(i) = pvtZb(i) * Remains / (timeint + 0.002)
             Else //OBM
                R = 10.73
                //PVTZ_free(i) = PVTZ_free(i) * Remains / (timeint + 0.002)
                PVTZ_Gas(i) = PVTZ_Gas(i) * Remains / (timeint + 0.002)
             End if
             volL(i) = volL(i) * Remains / (timeint + 0.002)
             volG(i) = volG(i) * Remains / (timeint + 0.002)
          End if
          Exit Sub
       End if
    Next i
//--- all kick is removed effectively ! - assign old mud properties
    N2phase = 1; timeAdd = delTsum
//
End Sub

Private Sub Pbcal2()
Dim iXb As Integer
//  Calculates B.H.P. when annulus pressure is given. - new AWC version
//  No Slip and Top properties as an average properties for Simplicity !
//.................................... assign the top cell conditions
//Modified by TY
    xNull = 0; pxcell = px; xcell = X
    xvdcell = xVert; tx = temperature(xvdcell)
    surfTen = surfT(px, tx)
//
    PVgain = 0; QtotMix = 0; volkkmix = 0; iXb = N2phase
    
    For i = N2phase To 2 Step -1
    
       Call getVD(xcell, xvdcell)
       tx = temperature(xvdcell)
       Call GasProp(pxcell, tx, gasVis, gasDen, zx)
    
       if volL(i) = 0 Then
           //volL(i) = VolL_default
           volL(i) = volL[MainDriver.N2phase]
           //volL(i) = delta_T(i) * volL[MainDriver.N2phase] / delta_T[MainDriver.N2phase]
       End if
    if imud = 1 Then //OBM case
       R = 10.73 //Universal gas constant
       tx2 = tx - 460
       //Call calcRs(pxcell, tx, 0.5537, Rs, Rsk, RsM, Rsn, ibaseoil) //gas solubility calc. by O//bryan//s
       Call calcRs2(pxcell, tx2, ibaseoil, Rs) //gas solubility calc. by PVTi
       if Rs < 0 Then
          Rs = 0
       End if
            if volGold(i) = 0 Then volGold(i) = 0.001
            wkbtmp = 42 * gasDen * volGold(i)
//
       Rs = Rs * foil //fractional solubility
//
       PVTZ_free(i) = volL(i) * (gor(i) - Rs) * 0.0417 / 16.04 //freegas mole
//
               if PVTZ_free(i) < 0 Then
                    PVTZ_free(i) = 0
               End if
//
       PVTZ_sol(i) = PVTZ_Gas(i) - PVTZ_free(i) //solution gas mole
       //.................................. calculate the pressure at the top
       MainDriver.volkx = PVTZ_free(i) * tx * zx * R / pxcell
       MainDriver.volkx = MainDriver.volkx / 5.615
//---------------------------------------------------------------------------------------------------Applying PREOS
       OBMmole(i) = (volL(i) * foil) * OBMdensity * 42 / OBMwt
//
       OBMFrac(i) = (OBMmole(i) / ((PVTZ_sol(i)) + OBMmole(i))) / 100
       GasFrac(i) = (0.01 - OBMFrac(i)) * 100
//
       OBMFr = OBMFrac(i)
       GasFr = GasFrac(i)
//
       mole_solgas = PVTZ_sol(i)
       mole_OBM = OBMmole(i)
//
       Call PREOS(ibaseoil, pxcell, tx, GasFr, OBMFr, mole_solgas, mole_OBM, V_cont)
       Call PREOS(ibaseoil, pxcell, tx, 0, 0.01, 0, mole_OBM, V_cont_ref)
//
       Vsol(i) = (V_cont - V_cont_ref)
       Vsol(i) = Vsol(i) * volL(i) * foil / V_cont
//
       Den_tmp = (oMud * volL(i) * 42 + PVTZ_sol(i) * 16.04) / (volL(i) + Vsol(i)) / 42
       Dendiff(i) = Den_tmp - oMud //density change
          if Dendiff(i) > 0 Then
              Dendiff(i) = 0
          End if
//---------------------------------------------------------------------------------------------------Applying PREOS
    Else //WBM case
       MainDriver.volkx = pvtZb(i) * tx * zx / pxcell
    End if
//
    voltmp = volL(i) + Vsol(i)
       
    volTot = MainDriver.volkx + voltmp
//
       if MainDriver.volkx > 0.001 Then iXb = i - 1
       volG(i) = MainDriver.volkx
       //voltmp = volL(i); volTot = MainDriver.volkx + voltmp
//       if (voltot < .00001 and i <> n2phase) Then
//          hgcal = 0
//          pnd(i) = pnd(i + 1); MainDriver.Xnd(i) = MainDriver.Xnd(i + 1)
//          vollqd(i) = voltmp; hgnd(i) = hgcal
//          GoTo 56789
//       End if
       if (volTot < 0.00001) Then volTot = 0.00001
       HgCal = MainDriver.volkx / volTot
       if (HgCal < 0.0001) Then GoTo 6789
//................. set the constant gas fraction at the top ie. less than 0.45
       //Modified by TY
       if (i > N2phase - nHgCon And HgCal > 0.45) Then
          HgCal = HgndOld(i)
          //voltmp = MainDriver.volkx * (1 - HgCal) / HgCal
          //volTot = MainDriver.volkx + voltmp
       End if
//...................................... set the maximum gas fraction
//Modified by TY
       HgMax = 0.45
       if (HgCal > HgMax) Then
          HgCal = HgMax
          //voltmp = MainDriver.volkx * (1 - HgMax) / HgMax
          //volTot = MainDriver.volkx + voltmp
       End if
//...................................... ??
6789 dummy = 0  //simple continue
       Call getTopH(volTot, xcell, hxt)
       Xbcell = xcell + hxt
       Call getVD(Xbcell, xbvdcell)
//--- check the location of kill mud pumped
       rhoL = oMud + Dendiff(i) //density change due to low-density gas dissolution
       if xcell >= (xKill - 0.01) Then
          rhoL = Kmud; volkkmix = volkkmix + MainDriver.volkx
       End if
       avgden = (rhoL) * (1 - HgCal) + gasDen * HgCal
       //avgden = gasDen * HgCal + rhoL * (1 - HgCal)
       dpmud = 0.052 * avgden * (xbvdcell - xvdcell)
       ql = MainDriver.QtMix * voltmp / volTot; Qg = MainDriver.QtMix * MainDriver.volkx / volTot
       Call get2pDP(Xbcell, ql, Qg, HgCal, rhoL, gasDen, gasVis)
       dpfri = DPtop
       Call get2pDP(xcell, ql, Qg, HgCal, rhoL, gasDen, gasVis)
       dpfri = dpfri - DPtop   // DP-fric in mixed zone
//............................................. assign calculated value
       Pnd(i) = pxcell; MainDriver.Xnd(i) = xcell
       volLqd(i) = voltmp; Hgnd(i) = HgCal
       pxcell = pxcell + dpmud + dpfri * Pcon; xcell = Xbcell
       PVgain = Vsol(i) + PVgain + MainDriver.volkx; QtotMix = QtotMix + volTot
56789  dummy = 0
//
    Next i
//
    if (Xbcell > TMD(NwcS)) Then Xbcell = TMD(NwcS)
    MainDriver.Xnd(1) = Xbcell; Pnd(1) = pxcell
    Xb = MainDriver.Xnd(iXb)
    Call pxBottom(volKmud, Xbcell, Pbtm)  //new cell is added continuously
    DPdiff = Pb - Pbtm
    PbTry = pxcell + DPdiff
//
End Sub

Private Sub PlotKillSheet()
//Static ppks(13) As Single, stks(13) As Single
//   These are in global to plot kill sheet and pump pressure together.
//
    gmc2 = gMudCirc; cmud2 = Cmud; gMudCirc = MainDriver.gMudKill;  Cmud = Kmud  // just plot the kill sheet
    volks = 0
    Call getDPinside(Pb, Qkill, volks, psp3, pp3)
    iCP = pp3; ppks(NwcE + 3) = pp3; stks(NwcE + 3) = 0
    For i = (NwcE + 2) To (NwcS + 1) Step -1
        volks = volks + volDS(i)
        Call getDPinside(Pb, Qkill, volks, psp3, pp3)
        ppks(i) = pp3; stks(i) = Int(volks / Qcapacity)
    Next i
    pmin = pp3
    volks = volks + 0.1 // just pass the BIT
    Call getDPinside(Pb, Qkill, volks, psp3, pp3)
    ppks(NwcS) = pp3; stks(NwcS) = Int(volks / Qcapacity)
    fCP = pp3; volks = volks + 20.5  //20.5 bbls more for plotting purpose !
    ppks(NwcS - 1) = pp3; stks(NwcS - 1) = Int(volks / Qcapacity)
//
//-------------------------------- specify the graph//s properties !
//   There are some difference in plotting between v.6 and v.3
// 7/25/01
//
    Sgraph0.ColumnCount = 2
    Sgraph0.RowCount = NwcE - NwcS + 5  // total number of DATA
    For i = (NwcS - 1) To (NwcE + 3)
        Sgraph0.Row = i - NwcS + 2
        Sgraph0.Column = 1
        Sgraph0.Data = stks(i)
        Sgraph0.Column = 2
        Sgraph0.Data = ppks(i) - 14.7
    Next i
    Sgraph0.Plot.UniformAxis = False
    gMudCirc = gmc2;  Cmud = cmud2

End Sub

Private Sub Reduce()
    if (idclk <> 1) Then Exit Sub
    Sgraph(iGraph).setVisible(false);     //First, hide the graph
//
    pmoveLeft(iGraph + 4) = enXpos
    pmoveTop(iGraph + 4) = enYpos
    Sgraph(iGraph).Left = OLeft - (HScrlscrn.value - iHmove)
    Sgraph(iGraph).Top = OTop - (VScrlscrn.value - iVmove)
    Sgraph(iGraph).Height = OHeit
    Sgraph(iGraph).Width = OWidth
    Sgraph(iGraph).Title.VtFont.Size = 10   //font size
    Sgraph(iGraph).setTextLengthType = 1  //fixed (0) & variable (1)
//
    sFrame(1).setVisible(true);
    idclk = 0
    For i = 0 To 4
        Sgraph(i).setVisible(true);
    Next i
   //if iWshow = 1 Then WellPic.Show
//
End Sub


//Private Sub optAutomatic2_Click()
    //imode = 2
    //optAutomatic2.value = True
    //optManual2.value = False
    
    //txtSIMmode.setText( "Currently choke is controlled by Automatic mode"
    
//End Sub

//Private Sub optManual2_Click()
    //imode = 1
    //optAutomatic2.value = False
    //optManual2.value = True
    
    //txtSIMmode.setText( "Currently choke is controlled by Manual mode"
    
//End Sub

Private Sub Sgraph_Click(Index As Integer)
    Reduce
End Sub

Private Sub Sgraph_DblClick(Index As Integer)
    iGraph = Index
    EnLarge
End Sub

Private Sub txtCHKopen_GotFocus()
    lblCHKopen.Caption = "Use Scroll bar to Change value !!"
End Sub

Private Sub txtCHKopen_LostFocus()
    lblCHKopen.Caption = "Choke Open % by Diameter Ratio"
   // if (iWshow = 1 And idclk = 0) Then WellPic.Show
End Sub

Private Sub txtSimRate_GotFocus()
    lblSimRate.Caption = "Use Scroll bar to Change value !!"
End Sub

Private Sub txtSimRate_LostFocus()
    lblSimRate.Caption = "(Times Faster Than Real Time)"
  //  if (iWshow = 1 And idclk = 0) Then WellPic.Show
End Sub

//Added by EJ
Private Sub Send_Data()
Dim mState As TSendStatus
   Dim strLen As Long

   On Error Resume Next
        
        
    ////Eif-WC-03
    
    // -------------------------------------통신 수정 후 130405
    mState.STX = "STX"
    mState.Stamp = "WCS1"
    mState.RequsetType = "R"
    mState.ElapsedTime = Str$(totHr) + ";" + Str$(totMin) + ";" + Str$(totSec)
    mState.Pump_Rate = Format(txtPumpRate.setText, "0.00")
    mState.StandPipePressure = Format(txtSPP.setText, "0.00")
    mState.PumpPressure = Format(txtPumpP.setText, "0.00")
    mState.ChokePressure = Format(txtChokeP.setText, "0.00")
    mState.Pit_Gain = Format(PVgain + mlPVgain, "0.00") //mlPVgain을 어떻게 처리해야 할지를 모르겠다
    mState.Pump_Stroke = Format(txtStroke.setText, "0.00")
    mState.Current_Mud_Weight = "0"   //얘는 필요없음
    mState.Mud_Return_Rate = Format(txtReturnRate.setText, "0.00")
    mState.Mud_volume_pumped = Format(txtVolume.setText, "0.00")
    mState.CasingshoePressure = Format(txtCasingP.setText, "0.000")
    mState.BottomholePressure = Format(txtBHP.setText, "0.00")
    mState.Kick_top_Pressure = Format(txtKickP.setText, "0.00")
    mState.Mud_linepressure = Format(txtMudLineP.setText, "0.00")
    mState.KickVolume = Format(PVgain + mlPVgain, "0.00")
    mState.KickHeight = Format(txtVDbottom.setText - txtVDtop.setText, "0.00")
    mState.Kick_influxrate = Format(QgDay, "0.00")
    mState.ReturnFlowRate = Format(tmpint, "0.00") //얘도 확신은 없음
    mState.Gas_returnrate = Format(txtGasRate.setText, "0.00")
    //mState.Depth_kicktop = Format(txtVDtop.setText, "0.00")
    //mState.Depth_kickbottom = Format(txtVDbottom.setText, "0.00")
    mState.Killmudlocation = Format(KWMposition, "0.00")
    mState.KickDensity = Format(gasDen, "0.00")
    mState.Stroke_X_Array = ""
    mState.Stroke_Y_Array = ""
    mState.Actual_X_Array = ""
    mState.Actual_Y_Array = ""
    mState.Sendchkopen = txtCHKopen.setText //hs 130703
    mState.sendresetstk = Format(reset_Pump_Stroke, "0.00")
    mState.killsheetresetstk = Format(Killsheet_Pump_Stroke, "0.00")
    mState.HW1 = ""
    mState.HW2 = ""
    mState.ETX = "ETX"
    
    
//For i = 1 To Nofkick
For i = 1 To kickcount
 //mState.Depth_kicktop = mState.Depth_kicktop + Format(senddatakick_up(i), "0.00") + "||"
 //mState.Depth_kickbottom = mState.Depth_kickbottom + Format(senddatakick_down(i), "0.00") + "||"
 mState.Depth_kicktop = mState.Depth_kicktop + Format(kickup(i), "0.00") + "||"
 mState.Depth_kickbottom = mState.Depth_kickbottom + Format(kickdown(i), "0.00") + "||"
Next i
    
    if IsPPflag = True Then
        mState.PumpPressure1 = mState.PumpPressure
    Else
        mState.PumpPressure1 = "0"
    End if
        
    
    
    For i = (NwcS - 1) To (NwcE + 3)
        if i = NwcS - 1 Then
            mState.Stroke_X_Array = mState.Stroke_X_Array & stks(i)
            mState.Stroke_Y_Array = mState.Stroke_Y_Array & ppks(i) - 14.7
        Else
            mState.Stroke_X_Array = mState.Stroke_X_Array & "_" & stks(i)
            mState.Stroke_Y_Array = mState.Stroke_Y_Array & "_" & ppks(i) - 14.7
        End if
    Next i
    
          Y = Sgraph0.ChartData
        
        For j = LBound(Y, 1) To UBound(Y, 1)
            if j = LBound(Y, 1) Then
                mState.Actual_X_Array = mState.Actual_X_Array & Y(j, 3)
                mState.Actual_Y_Array = mState.Actual_Y_Array & Y(j, 4)
            //Debug.Print y(j, 4)
            Else
                mState.Actual_X_Array = mState.Actual_X_Array & "_" & Y(j, 3)
                mState.Actual_Y_Array = mState.Actual_Y_Array & "_" & Y(j, 4)
            End if
        Next j
      
    
    
    
  //워드파일 작성한 목록
    
    SendData = mState.STX & "," & mState.Stamp & "," & mState.RequsetType & "," & mState.ElapsedTime & "," & mState.Pump_Rate & "," _
               & mState.StandPipePressure & "," & mState.PumpPressure & "," & mState.ChokePressure & "," & mState.Pit_Gain & "," & mState.Pump_Stroke & "," _
               & mState.Current_Mud_Weight & "," & mState.Mud_Return_Rate & "," & mState.Mud_volume_pumped & "," & mState.CasingshoePressure & "," _
               & mState.BottomholePressure & "," & mState.Kick_top_Pressure & "," & mState.Mud_linepressure & "," & mState.KickVolume & "," _
               & mState.KickHeight & "," & mState.Kick_influxrate & "," & mState.ReturnFlowRate & "," & mState.Gas_returnrate & "," & mState.Depth_kicktop & "," _
               & mState.Depth_kickbottom & "," & mState.Killmudlocation & "," & mState.KickDensity & "," & mState.Stroke_X_Array & "," & mState.Stroke_Y_Array & "," _
               & mState.Actual_X_Array & "," & mState.Actual_Y_Array & "," & mState.HW1 & "," & mState.HW2 & "," & mState.Sendchkopen & "," & mState.sendresetstk & "," _
               & mState.killsheetresetstk & "," & mState.PumpPressure1 & "," & mState.ETX
    
    //sendchkvalue 추가해야함
    
    
    //SendData = mState.STX & "," & mState.Stamp & "," & mState.SendSystem & "," & mState.StartIP & "," & mState.TargetIP & "," & mState.RequsetType & "," _
              & mState.ApplyType & "," & mState.ElapsedTime & "," & mState.BopLockSwitch & "," & mState.AnnularBopSwitch & "," & mState.UpperPipeRam & "," _
              & mState.BlindRam & "," & mState.ShearRam & "," & mState.LowerPipeRam & "," & mState.ChokePressure & "," & mState.StandPipePressure & "," _
              & mState.ReturnFlowRate & "," & mState.PumpPressure & "," & mState.ChokePressure2 & "," & mState.ChokeOpen & "," & mState.ETX & "," _
              & mState.Start_Simulation & "," & mState.Pause & "," & mState.SimulationValue & "," & mState.Pump_Rate & "," _
              & mState.Old_Mud_Weight & "," & mState.Current_Mud_Weight & "," & mState.Pump_Stroke & "," & mState.Pump_Rate2 & "," & mState.Mud_Return_Rate & "," _
              & mState.Pit_Gain & "," & mState.Pump & "," & mState.Bit_Rotation & "," & mState.Set_Pump_Stroke_Zero & "," & mState.Kill_Mud_Weight_Value & "," _
              & mState.Start_Kill_Mud_Pumping & "," & mState.Start_Kill_operation & "," & mState.True_Vertical_Depth & "," & mState.Total_Measured_Depth & "," _
              & mState.Horizontal_Displacement & "," & mState.Pit_Gain_Warning_Sign & "," & mState.Form_Change & "," & mState.Stroke_X_Array & "," _
              & mState.Stroke_Y_Array & "," & mState.Actual_X_Array & "," & mState.Actual_Y_Array & "," & mState.V_depth & "," _
              & mState.Depth_KOP & "," & mState.R_BUR & "," & mState.R2_BUR & "," & mState.ang_EOB & "," & mState.ang2_EOB & "," _
              & mState.x_hold & "," & mState.x2_hold & "," & mState.Well_type & "," & mState.Depth_water
              
    
    
    
    
    Dim sData As String
    Dim bData() As Byte
    strLen = Len(SendData)
    strLen = strLen + 4
    Dim ba(0 To 3) As Byte
    ba(0) = strLen Mod 256
    For i = 1 To 3
     if strLen \ (256 ^ i) > 255 Then
      ba(i) = 255
     Else
      ba(i) = strLen \ (256 ^ i)
      End if
    Next i
    // 실제데이타를 추가한다.
    bData = StrConv(SendData, vbFromUnicode)
    l_remoteSock.SendData ba
    l_remoteSock.SendData bData //<-- 그냥 sData를 보내면 VB가 유니코드인줄 알고, ASCII로 변환해서
                           // 보내게 되는데, 이진화일은 깨짐.

End Sub

Private Sub TimerCirc_Timer()   //Modified 7-04-94 !
//--- control all the circulation with ONE Timer !
//    because kick happens any time during circulation !
Dim iSurface As Integer
//-----This is the main control program until kick top arrives at the surface
//
    kickcount = 0 //kb drillSI에서 넘어온 거 초기화
    
    timeNew = Hour(Now) * 3600 + Minute(Now) * 60 + Second(Now)
    timeAdd = (timeNew - timeOld) * SimRate // time interval from last step //멈췄다가 다시 Circulation 버튼을 누른 경우 timeOld를 기억하고 있다가 그때부터 지금까지 진행된 시간을 timeAdd에 배정!
    iSurface = 0; timeNpUse = 60
//
    if (Np = MainDriver.NpSi) Then      // assign the very beginning of the Pumping
//       timeadd = tmd(NwcS) / Vsound   //this gives to high accel. DP
       timeAdd = 30  //sec to reduce acceleration loss, 7/16/02
       timeNp = 62
    End if
    
    xold = X; Pbeff = Pb; hgX = Hgnd[MainDriver.N2phase]
    if imud = 0 Then
    Call GetXnew(xold, timeAdd, hgX, MainDriver.QtMix, xnew, Vgf)
    Else
        Call GetXnew(xold, timeAdd, 0, MainDriver.QtMix, xnew, Vgf) //constraint gas slip
    End if
    
    X = xnew
//
//---TimerKickout의 역할---------------------------------------------------
    if (X < -0.01 And mudCase = -99) Then
       timeAdd = xold / Vgf; X = 0     // KICK just arrives at the surface
       mudCase = -1
    End if
//
    if (mudCase = -1) Then   //- Remove the KICK out of Hole
       Call NewN2ph(timeAdd)
    End if
//
    if (X < -0.01) Then X = 0 //Top of the Kick passes the choke !
    gTcum = gTcum + timeAdd      //total time from beginning of KILL
    MainDriver.gDelT = timeAdd             //time interval
    timeOld = timeNew
//
    CHKpcent = HScrollCHK.value
    MainDriver.volPump = MainDriver.volPump + Qkill * MainDriver.gDelT / (42 * 60)     //in bbls
//
    if Method = 2 Then //when KM pumping button pushed
       volKmud = volKmud + Qkill * MainDriver.gDelT / (42 * 60)  //in bbls
    End if
    iShift = 0
    Call GetXkill
    if imode = 1 Then //manual chk mode
        Call Golden2 //new sub for AWC.for & AWC.bas for Multiple Kicks
    Else //automatic chk mode
            Call Golden
    End if
//
    timeNp = timeNp + timeAdd
    ShowResult
    
    //Call kick_location //kb
    
    Send_Data
    kickcount = 0 //kb
    
    
    if (timeNp >= timeNpUse) Then AssignVal
//
    if Xb < 0.1 And volKmud > (MainDriver.VOLinn + VOLout) Then
       TimerCirc.Enabled = False
       MsgBox "The Well has been Killed. You did a GREAT job !!", vbSystemModal + 0, msgTitle
       //if(mState.ETX != "ETX") l_remoteSock.SendData "stx,wcs1,m,The Well has been Killed. You did a GREAT job !!.,ext"
       
    
    End if
End Sub

Private Sub kick_location() //kb

    Nofkick = 1
    resetkick = 2
      
    senddatakick_up(Nofkick) = kickup(1)
    senddatakick_down(Nofkick) = kickdown(1)
    WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
    WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
      
    Dim startpoint As Integer //multikick전에 제대로 그려지도록 하기 위해
    if kickcount = 2 Then
        //startpoint = 1 //kb 130619
        kickcount = 3
        
    //Elseif kickcount > 2 Then
        //startpoint = 2 //kb 130619
    End if
    
      
    For i = 2 To (kickcount - 1)
        
        if (kickup(i + 1) < kickup(i)) Then
        //현재순서; 아래에서 위로 쌓이는 경우
            tmp = kickup(i) - kickdown(i + 1)
            if Abs(tmp) > 10 And kickup(i) <= kickup(i - 1) Then
                //이전순서; 아래에서 위로 쌓이는 경우
                
                Nofkick = Nofkick + 1
                
                senddatakick_up(Nofkick) = kickup(i)
                senddatakick_down(Nofkick) = kickdown(resetkick)
                
                WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                          
                resetkick = (i + 1)
                
                if resetkick = kickcount Then
                    Nofkick = Nofkick + 1
                    senddatakick_up(Nofkick) = kickup(resetkick)
                    senddatakick_down(Nofkick) = kickdown(resetkick)
                
                    WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                    WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                    Exit Sub
                End if
            Elseif Abs(tmp) > 10 And kickup(i) > kickup(i - 1) Then
            //이전순서; 위에서 아래로 쌓이는 경우
            
                Nofkick = Nofkick + 1
                
                senddatakick_up(Nofkick) = kickup(resetkick)
                senddatakick_down(Nofkick) = kickdown(i)
                
                WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                           
                resetkick = (i + 1)
                
                if resetkick = kickcount Then
                    Nofkick = Nofkick + 1
                    senddatakick_up(Nofkick) = kickup(resetkick)
                    senddatakick_down(Nofkick) = kickdown(resetkick)
                
                    WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                    WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                    Exit Sub
                End if
            End if
            
            if i = (kickcount - 1) Then
                Nofkick = Nofkick + 1
                senddatakick_up(Nofkick) = kickup(kickcount)
                senddatakick_down(Nofkick) = kickdown(resetkick)
                
                WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
             End if
                    
        Else
        //현재순서; 위에서 아래로 쌓이는 경우
            tmp = kickup(i + 1) - kickdown(i)
            if Abs(tmp) > 10 And kickup(i) <= kickup(i - 1) Then
                //이전순서; 아래에서 위로 쌓이는 경우
                
                Nofkick = Nofkick + 1
                
                senddatakick_up(Nofkick) = kickup(i)
                senddatakick_down(Nofkick) = kickdown(resetkick)
                
                WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                          
                resetkick = (i + 1)
                
                if resetkick = kickcount Then
                    Nofkick = Nofkick + 1
                    senddatakick_up(Nofkick) = kickup(resetkick)
                    senddatakick_down(Nofkick) = kickdown(resetkick)
                
                    WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                    WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                    Exit Sub
                End if
                
                
            Elseif Abs(tmp) > 10 And kickup(i) > kickup(i - 1) Then
                //이전순서; 위에서 아래로 쌓이는 경우
                
                Nofkick = Nofkick + 1
                //resetkick = (i + 1) //hs
                
                senddatakick_up(Nofkick) = kickup(resetkick)
                senddatakick_down(Nofkick) = kickdown(i)
                
                WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                
                resetkick = (i + 1)
                
                if resetkick = kickcount Then
                    Nofkick = Nofkick + 1
                    senddatakick_up(Nofkick) = kickup(resetkick)
                    senddatakick_down(Nofkick) = kickdown(resetkick)
                
                    WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                    WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                    Exit Sub
                End if
                
            End if
            
            
             if i = (kickcount - 1) Then
                Nofkick = Nofkick + 1
                senddatakick_up(Nofkick) = kickup(resetkick)
                senddatakick_down(Nofkick) = kickdown(kickcount)
                
                WellPic.Line (-d95, senddatakick_up(Nofkick))-(-d1, senddatakick_down(Nofkick)), KickColor, BF
                WellPic.Line (d95, senddatakick_up(Nofkick))-(d1, senddatakick_down(Nofkick)), KickColor, BF
                   
             End if
             
        End if
        
    Next i
    
    
End Sub

Public Sub HTouchCHK(value As Integer)
    if value = 0 Then
       msgTmp$ = "Oh, Boy !  You made a serious mistake !"
       MsgBox msgTmp$, vbSystemModal + 0, msgTitle
      // if (iWshow = 1 And idclk = 0) Then WellPic.Show
       Exit Sub
    End if
//
  //  if (iWshow = 1 And idclk = 0) Then WellPic.Show
   // if (iWshow <> 1) Then Unload WellPic
    
    TimerCirc.Enabled = True
    timeStart = Hour(Now) * 3600 + Minute(Now) * 60 + Second(Now)
    MainDriver.QtMix = Qkill; timeOld = timeStart; mudCase = -99

Dim iSurface As Integer
//-----This is the main control program until kick top arrives at the surface
//
    timeNew = Hour(Now) * 3600 + Minute(Now) * 60 + Second(Now)
    timeAdd = (timeNew - timeOld) * SimRate // time interval from last step
    iSurface = 0; timeNpUse = 60
//
    if (Np = MainDriver.NpSi) Then      // assign the very beginning of the Pumping
//       timeadd = tmd(NwcS) / Vsound   //this gives to high accel. DP
       timeAdd = 30  //sec to reduce acceleration loss, 7/16/02
       timeNp = 62
    End if
    xold = X; Pbeff = Pb; hgX = Hgnd[MainDriver.N2phase]
    Call GetXnew(xold, timeAdd, hgX, MainDriver.QtMix, xnew, Vgf)
    X = xnew
//
    if (X < -0.01 And mudCase = -99) Then
       timeAdd = xold / Vgf; X = 0     // KICK just arrives at the surface
       mudCase = -1
    End if
//
    if (mudCase = -1) Then   //- Remove the KICK out of Hole
       Call NewN2ph(timeAdd)
    End if
//
    if (X < -0.01) Then X = 0 //Top of the Kick passes the choke !
    gTcum = gTcum + timeAdd      //total time from beginning of KILL
    MainDriver.gDelT = timeAdd             //time interval
    timeOld = timeNew
//
    CHKpcent = value
    MainDriver.volPump = MainDriver.volPump + Qkill * MainDriver.gDelT / (42 * 60)     //in bbls
    if Method = 2 Then
       volKmud = volKmud + Qkill * MainDriver.gDelT / (42 * 60)  //in bbls
    End if
    iShift = 0
    Call GetXkill
    Call Golden2      // new sub for AWC.for & AWC.bas for Multiple Kicks
//
    timeNp = timeNp + timeAdd
    ShowResult
    if (timeNp >= timeNpUse) Then AssignVal
//
    if Xb < 0.1 And volKmud > (MainDriver.VOLinn + VOLout) Then
       TimerCirc.Enabled = False
       MsgBox "The Well has been Killed. You did a GREAT job !!", vbSystemModal + 0, msgTitle
    End if
End Sub

Private Sub VScrlscrn_Change()
Dim j As Integer
    iMove = VScrlscrn.value
    For i = 0 To 3
        objleft = sFrame(i).Left
        sFrame(i).Move objleft, (pmoveTop(i) - iMove)
    Next i
    j = 0
    For i = 4 To 8
        objleft = Sgraph(j).Left
        if (j <> iGraph) Or idclk = 0 Then
           Sgraph(j).Move objleft, (pmoveTop(i) - iMove)
        End if
        j = j + 1
    Next i
    if idclk = 1 Then
       objleft = Sgraph(iGraph).Left
       Sgraph(iGraph).Move objleft, (pmoveTop(iGraph + 4) - iMove + iVmove)
    End if
    objleft = cmdPumpKillMud.Left
    cmdPumpKillMud.Move objleft, (pmoveTop(9) - iMove)
    objleft = cmdFixPump.Left
    cmdFixPump.Move objleft, (pmoveTop(10) - iMove)
End Sub

Private Sub Golden()
//  Golden section search to obtain annulus pressure by
//  comparing calculated B.H.P. with true B.H.P.
//Added by Ty
//Sub for automatic chk control
Static iLoc As Integer, iter As Integer
    Call Bkup
    px1 = 14.7; f1 = 1; px2 = Pb; f2 = -1
    iter = 0;
    iHgMax = 0
//
10  iter = iter + 1
       px = 0.5 * (px2 + px1)
          Call PbCalc
       ffpb = Pb - PbTry; pxErr = Abs(ffpb / Pb)
       if (pxErr < 0.0002) Then GoTo 5055 // - use less criteria for fast run
//
       if (f1 * ffpb < 0) Then        //BiSection method
          px2 = px; f2 = ffpb
       Else
          px1 = px; f1 = ffpb
       End if
       if (iter > 50) Then
             px = px + Pb - PbTry
             GoTo 5055
       End if
       if px < 14.6 Then
          px = 14.7
       //Modified by TY
       if mudCase = -1 Then //KICK just arrives at the surface
          Call PbCalc2 //during kick out situation
       Else
          Call PbCalc //during kick circulation
       End if
          GoTo 5055
       End if
       delPx = Abs(px1 - px2)          // check Infinite loop
       if (delPx < 0.05) Then
//          MsgBox "px does not converge ! then modify it !", 0, msgtitle
          if Abs(px1 - 14.7) < 0.005 Then   //px get min. p & Pbeff increase
             Pbeff = PbTry
             GoTo 5055
          End if
          px1old = px1; px2old = px2; padd = Abs(Pb - PbTry)
          if (Pb > PbTry) Then
             px2 = px2 + 4 * padd
          Else
             px1 = px1 - 4 * padd
          End if
          f1 = 1; px1 = Max(px1, 14.7); px2 = Min(px2, Pb)
       End if
      GoTo 10
//
//.......................................... calculate & assign values
5055  dummy = 0   // simple continue
      //For ii = 2 To N2phase
         //tmp2 = volL(ii); tmp3 = volLqd(ii); volL(ii) = Max(tmp2, tmp3)
      //Next ii
//
      Call getVD(X, xVert)
      Call getVD(Xb, xbVert)
      txvd = temperature(xVert)
      Call GasProp(px, txvd, gasVis, gasDen, zz)
//.............................. calculate current effective flow rate
//                               and surface choke pressure
      delvol = (PVgain + mlPVgain - MainDriver.Vpit[Np)) * 42 * 60 / (gTcum - TTsec(Np) + 0.02)
      Call range(delvel, 0, Qkill * 2.5)
      delvol2 = delvol / 2.448
      Call Xposition(X, iLoc)
      DPacc = 0; xmdtmp = X - TMD(iLoc + 1)
      For i = (iLoc + 1) To 9
         d2 = Do2p(i); d1 = Di2p(i)
         DPacc = DPacc + xmdtmp * delvol2 / (d2 * d2 - d1 * d1)
         xmdtmp = TMD(i) - TMD(i + 1)
      Next i
//............................................. for choke or kill lines
      Call getLines(MainDriver.QtMix, d2, d1, Qeff, cap2eff2, volEff)
      DPacc = DPacc + xmdtmp * delvol2 / (d2 * d2 - d1 * d1) * Qeff / MainDriver.QtMix
      psicon = 0.001614678
      DPacc = psicon * oMud * DPacc / (gTcum - TTsec(Np) + 1)
//............ calculate total pressure loss for top single phase region
      Call getDP(MainDriver.QtMix, X, oMud)
      DPtotal = MainDriver.gMudOld * xVert + DPacc + DPtop * Pcon
//
      //Modified by TY
      //MainDriver.volPump = MainDriver.volPump + Qkill * MainDriver.gDelT / (42 * 60) ; in manual mode, MainDriver.volPump is assigned in Timercirc sub.
      Pchoke = px - DPtotal
//...................... calculate pump pressure and stand pipe pressure
      Call getDPinside(Pb, Qkill, volKmud, SPP, pumpP)
//....................................... calculate casing seat pressure
      if (DepthCasing > Xb) Then
         Call pxBottom(MainDriver.volPump, DepthCasing, Pbtm)
         Pcasing = Pbtm
      Elseif (DepthCasing > X) Then
         Pcasing = DitplDes(DepthCasing)
      Else
         Call getDP(MainDriver.QtMix, DepthCasing, oMud)
         Pcasing = Pchoke + MainDriver.gMudOld * TVD(iCsg) + Pcon * DPtop
      End if
//--- calculate Qt-mix for the next calculation, 7/17/02
//........... old approach gives very small return rate increase since average from beginning
//      MainDriver.QtMix = Qkill + (PVgain - MainDriver.Vpit[MainDriver.NpSi)) * 42 * 60 / (gTcum - Tdelay + 2)
//
      if Np <= MainDriver.NpSi + 10 Then
         MainDriver.QtMix = Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.NpSi)) * 42 * 60 / (gTcum - TTsec(MainDriver.NpSi) + 1)
      Else
         MainDriver.QtMix = Qkill + (PVgain + mlPVgain - MainDriver.Vpit[Np - 4)) * 42 * 60 / (gTcum - TTsec(Np - 4))
      End if
      Call range(MainDriver.QtMix, Qkill, Qkill * 4.5)
      
      QgDay = GASinflux(Pbeff, MainDriver.gDelT, Hdrled)
//
End Sub
Private Sub PbCalc2()
//  Calculates B.H.P. when annulus pressure is given. - new AWC version
//  No Slip and Top properties as an average properties for Simplicity !
Dim volTot As Single
//.................................... assign the top cell conditions
    xNull = 0; pxcell = px; xcell = X
    iXb = N2phase
    Call getVD(xcell, xvdcell)
    tx = temperature(xvdcell)
    surfTen = surfT(MainDriver.pxTop[Np - 1), tx)
//
    PVgain = 0; QtotMix = 0
//
    For i = N2phase To 2 Step -1
//
       Call getVD(xcell, xvdcell)
       tx = temperature(xvdcell)
       Call GasProp(pxcell, tx, gasVis, gasDen, zx)
//
       if imud = 0 Then //WBM case
            MainDriver.volkx = pvtZb(i) * tx * zx / pxcell
       Else //OBM case
            MainDriver.volkx = PVTZ_free(i) * tx * zx * R / pxcell / 5.615
       End if
//
       voltmp = volL(i)
       volTot = MainDriver.volkx + voltmp
       //Added by TY
       if MainDriver.volkx > 0.001 Then iXb = i - 1
       //.... if there are kicks from ML to MP(iMLflux = 1), ignore the restriction on max Hg
//     at the mixture front. this is also good for bottom kick loc. 2003/8/1
       if volTot < 0.00001 Then volTot = 0.00001
       HgCal = MainDriver.volkx / volTot
       if (HgCal < 0.0001 Or iMLflux = 1) Then GoTo 6789
//................. set the constant gas fraction at the top ie. less than 0.45
       if (i > N2phase - nHgCon And iHgMax = 1 And HgCal > 0.45) Then
          HgCal = HgndOld(i)
          if HgCal < 0.00002 Then HgCal = 0.45   //temperarily set 0.45 as maximum
          //voltmp = MainDriver.volkx * (1 - HgCal) / HgCal
          //volTot = MainDriver.volkx + voltmp
       End if
//...................................... set the maximum gas fraction
       if (HgCal > HgMax And iHgMax = 1) Then
          HgCal = HgMax
          //voltmp = MainDriver.volkx * (1 - HgMax) / HgMax
          //volTot = MainDriver.volkx + voltmp
       End if
//
6789 dummy = 0   //simple continue -------------------------------------------
//
       Call getTopH(volTot, xcell, hxt)
       Xbcell = xcell + hxt
       Call getVD(Xbcell, xbvdcell)
       
       //Modified by Ty
       //avgden = gasDen * HgCal + oMud * (1 - HgCal)
       avgden = oMud * (1 - HgCal) + gasDen * HgCal
       
       dpmud = 0.052 * avgden * (xbvdcell - xvdcell)
       
       ql = MainDriver.QtMix * voltmp / volTot; Qg = MainDriver.QtMix * MainDriver.volkx / volTot
       
       Call get2pDP(Xbcell, ql, Qg, HgCal, oMud + Dendiff(i), gasDen, gasVis)
       dpfri = DPtop
       Call get2pDP(xcell, ql, Qg, HgCal, oMud + Dendiff(i), gasDen, gasVis)
       dpfri = dpfri - DPtop   // DP-fric in mixed zone
//............................................. assign calculated value
       Pnd(i) = pxcell; MainDriver.Xnd(i) = xcell
       volG(i) = MainDriver.volkx; volLqd(i) = voltmp; Hgnd(i) = HgCal
       pxcell = pxcell + dpmud + dpfri * Pcon; xcell = Xbcell
       //Modified by Ty
       PVgain = PVgain + MainDriver.volkx; QtotMix = QtotMix + volTot
       //PVgain = Vsol_l + PVgain + MainDriver.volkx; QtotMix = QtotMix + volTot
//
56789 dummy = 0
//
   Next i
//
   //Modified by TY
   //if (xbcell > TMD(NwcS)) Then xbcell = TMD(NwcS)
   MainDriver.Xnd(1) = Xbcell; Pnd(1) = pxcell
   //xxx = TMD(NwcS)
   //Xb = Min(Xbcell, xxx)
   Xb = MainDriver.Xnd(iXb)
   //Call pxBottom(MainDriver.volPump, Xb, Pbtm)
   //Call pxBottom(volKmud, Xb, Pbtm)
   Call pxBottom(volKmud, Xbcell, Pbtm)
   DPdiff = Pb - Pbtm; PbTry = pxcell + DPdiff
   if (Abs(Pb - PbTry) < 50.5) Then iHgMax = 1


End Sub
Private Sub PbCalc()
//  Calculates B.H.P. when annulus pressure is given. - new AWC version
//  No Slip and Top properties as an average properties for Simplicity !
//Added by TY for automatic chk control
Dim volTot As Single
//.................................... assign the top cell conditions
    xNull = 0; pxcell = px; xcell = X
    iXb = N2phase
    Call getVD(xcell, xvdcell)
    tx = temperature(xvdcell)
    surfTen = surfT(MainDriver.pxTop[Np - 1), tx)
//
    PVgain = 0; QtotMix = 0
    
    For i = N2phase To 2 Step -1
       Call getVD(xcell, xvdcell)
       tx = temperature(xvdcell)
       Call GasProp(pxcell, tx, gasVis, gasDen, zx)
       if volL(i) = 0 Then
           //volL(i) = VolL_default
           volL(i) = volL[MainDriver.N2phase]
           //volL(i) = delta_T(i) * volL[MainDriver.N2phase] / delta_T[MainDriver.N2phase] //assign liq. volume for the gas influx during stabilized period
       End if
//------------------------------------------------------------------------------------ OBM case
if imud = 1 Then
       R = 10.73 //universal gas constant
       tx2 = tx - 460 //tx; rankine / tx2; fahrenheit
       //Call calcRs(pxcell, tx, 0.5537, Rs, Rsk, RsM, Rsn, ibaseoil) //gas solubility calc. by O//bryan//s
       Call calcRs2(pxcell, tx2, ibaseoil, Rs) //gas solubility calc. by PVTi
       if Rs < 0 Then
           Rs = 0
       End if
        if volGold(i) = 0 Then volGold(i) = 0.001
        wkbtmp = 42 * gasDen * volGold(i)
       Rs = Rs * foil //fractional solubility
//
       PVTZ_free(i) = volL(i) * (gor(i) - Rs) * 0.0417 / 16.04 //freegas mole
               if PVTZ_free(i) < 0 Then
                    PVTZ_free(i) = 0
               End if
       PVTZ_sol(i) = PVTZ_Gas(i) - PVTZ_free(i) //solution gas mole
//.................................. calculate the pressure at the top
       MainDriver.volkx = PVTZ_free(i) * tx * zx * R / pxcell //pit gain by free gas volume(ft^3)
       MainDriver.volkx = MainDriver.volkx / 5.615 //pit gain by free gas volume(bbls)
//---------------------------------------------------------------------------------------------------Applying PREOS
    OBMmole(i) = (volL(i) * foil) * OBMdensity * 42 / OBMwt
//
    OBMFrac(i) = (OBMmole(i) / ((PVTZ_sol(i)) + OBMmole(i))) / 100
    GasFrac(i) = (0.01 - OBMFrac(i)) * 100
//
    OBMFr = OBMFrac(i)
    GasFr = GasFrac(i)
//
    mole_solgas = PVTZ_sol(i)
    mole_OBM = OBMmole(i)
//
    Call PREOS(ibaseoil, pxcell, tx, GasFr, OBMFr, mole_solgas, mole_OBM, V_cont)
    Call PREOS(ibaseoil, pxcell, tx, 0, 0.01, 0, mole_OBM, V_cont_ref)
//
    Vsol(i) = (V_cont - V_cont_ref)
    Vsol(i) = Vsol(i) * volL(i) * foil / V_cont
//
    Den_tmp = (oMud * volL(i) * 42 + PVTZ_sol(i) * 16.04) / (volL(i) + Vsol(i)) / 42
    Dendiff(i) = Den_tmp - oMud //density change
//
    if Dendiff(i) > 0 Then
        Dendiff(i) = 0
    End if
//
Else //WBM case
    MainDriver.volkx = pvtZb(i) * tx * zx / pxcell
End if
//
    voltmp = volL(i) + Vsol(i)
    volTot = MainDriver.volkx + voltmp
    //Added by TY
    if MainDriver.volkx > 0.001 Then iXb = i - 1
//---------------------------------------------------------------------------------------------------Applying PREOS
//.... if there are kicks from ML to MP(iMLflux = 1), ignore the restriction on max Hg
//     at the mixture front. this is also good for bottom kick loc. 2003/8/1
       if volTot < 0.00001 Then volTot = 0.00001
       HgCal = MainDriver.volkx / volTot
       if (HgCal < 0.0001 Or iMLflux = 1) Then GoTo 6789
//................. set the constant gas fraction at the top ie. less than 0.45
       if (i > N2phase - nHgCon And iHgMax = 1 And HgCal > 0.45) Then
          HgCal = HgndOld(i)
          if HgCal < 0.00002 Then HgCal = 0.45   //temperarily set 0.45 as maximum
          //voltmp = MainDriver.volkx * (1 - HgCal) / HgCal
          //volTot = MainDriver.volkx + voltmp
       End if
//...................................... set the maximum gas fraction
       if (HgCal > HgMax And iHgMax = 1) Then
          HgCal = HgMax
          //voltmp = MainDriver.volkx * (1 - HgMax) / HgMax
          //volTot = MainDriver.volkx + voltmp
       End if
6789 dummy = 0   //simple continue -------------------------------------------
       Call getTopH(volTot, xcell, hxt)
       Xbcell = xcell + hxt
       Call getVD(Xbcell, xbvdcell)
       //Modified by Ty
       //avgden = gasDen * HgCal + oMud * (1 - HgCal)
       avgden = (oMud + Dendiff(i)) * (1 - HgCal) + gasDen * HgCal
       dpmud = 0.052 * avgden * (xbvdcell - xvdcell)
       ql = MainDriver.QtMix * voltmp / volTot; Qg = MainDriver.QtMix * MainDriver.volkx / volTot
       Call get2pDP(Xbcell, ql, Qg, HgCal, oMud + Dendiff(i), gasDen, gasVis)
       dpfri = DPtop
       Call get2pDP(xcell, ql, Qg, HgCal, oMud + Dendiff(i), gasDen, gasVis)
       dpfri = dpfri - DPtop   // DP-fric in mixed zone
//............................................. assign calculated value
       Pnd(i) = pxcell; MainDriver.Xnd(i) = xcell
       volG(i) = MainDriver.volkx; volLqd(i) = voltmp; Hgnd(i) = HgCal
       pxcell = pxcell + dpmud + dpfri * Pcon; xcell = Xbcell
       //Modified by Ty
       PVgain = PVgain + MainDriver.volkx + Vsol(i); QtotMix = QtotMix + volTot
       //PVgain = Vsol_l + PVgain + MainDriver.volkx; QtotMix = QtotMix + volTot
//
56789 dummy = 0
   Next i
//
   MainDriver.Xnd(1) = Xbcell; Pnd(1) = pxcell
   //xxx = TMD(NwcS)
   //Xb = Min(xbcell, xxx)
   Xb = MainDriver.Xnd(iXb)
   //Modified by TY
   //Call pxBottom(MainDriver.volPump, Xb, Pbtm)
   //Call pxBottom(volKmud, Xb, Pbtm) //change to the manual mode//s varible name
   Call pxBottom(volKmud, Xbcell, Pbtm)
   DPdiff = Pb - Pbtm; PbTry = pxcell + DPdiff
   if (Abs(Pb - PbTry) < 50.5) Then iHgMax = 1
//
End Sub

	 */
	
	class MudGaugePanel extends JPanel{			//15,70,150,150
		double GaugeValue=0;
		double DrawAngle=0, Xcenter = 0 , Ycenter=0, Xoriginal=0;
		double Xcalculated =0;
		double Ycalculated =0;
		double RAD_CONVERT = 3.141592 / 180;			
		    
		    //    Line1.BorderWidth = 3
		    
		MudGaugePanel(){
			this.setBackground(new Color(240,240,240));
		}
		
		public void paint(Graphics g){
			super.paint(g);				
			GaugeValue = tmpintMud;				
			g.setColor(Color.BLACK);
			DrawAngle = 90 * GaugeValue / 250 * RAD_CONVERT;
			Xcenter = panSizeX/2;
            Ycenter = (panSizeY+2*labelFontSize)/2;
            Xcalculated = Xcenter - Radius * Math.cos(DrawAngle);
            Ycalculated = Ycenter - Radius * Math.sin(DrawAngle);
            g.drawOval((int)(Xcenter-(Radius+ovalIntv)), (int)(Ycenter-(Radius+ovalIntv)), (int)(2*(Radius+ovalIntv)), (int)(2*(Radius+ovalIntv)));
            g.drawLine((int)Xcenter, (int)(Ycenter-(Radius+ovalIntv)), (int)Xcenter, (int)(Ycenter-(Radius+ovalIntv)-LineLength));
            g.drawLine((int)Xcenter, (int)(Ycenter+(Radius+ovalIntv)), (int)Xcenter, (int)(Ycenter+(Radius+ovalIntv)+LineLength));
            g.drawLine((int)(Xcenter+(Radius+ovalIntv)), (int)(Ycenter), (int)(Xcenter+(Radius+ovalIntv)+LineLength), (int)(Ycenter));
            g.drawLine((int)(Xcenter-(Radius+ovalIntv)), (int)(Ycenter), (int)(Xcenter-(Radius+ovalIntv)-LineLength), (int)(Ycenter));
            g.drawLine((int)Xcalculated, (int)Ycalculated, (int)Xcenter, (int)Ycenter);
					
		}
	}
	
	class CHKGaugePanel extends JPanel{//15, 230,150,150
		double GaugeValue=0;	
		double DrawAngle=0, Xcenter=0, Ycenter=0, Xoriginal=0;
		double Xcalculated =0;
		double Ycalculated =0;
		double RAD_CONVERT = 3.141592 / 180;
		
		CHKGaugePanel(){
			this.setBackground(new Color(240,240,240));
		}
		
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.BLACK);
			GaugeValue=tmpintCHK;
			DrawAngle = 90 * GaugeValue / 1000 * RAD_CONVERT;
			Xcenter = panSizeX/2;
            Ycenter = (panSizeY+2*labelFontSize)/2;
            Xcalculated = Xcenter - Radius * Math.cos(DrawAngle);
            Ycalculated = Ycenter - Radius * Math.sin(DrawAngle);
            g.drawOval((int)(Xcenter-(Radius+ovalIntv)), (int)(Ycenter-(Radius+ovalIntv)), (int)(2*(Radius+ovalIntv)), (int)(2*(Radius+ovalIntv)));
            g.drawLine((int)Xcenter, (int)(Ycenter-(Radius+ovalIntv)), (int)Xcenter, (int)(Ycenter-(Radius+ovalIntv)-LineLength));
            g.drawLine((int)Xcenter, (int)(Ycenter+(Radius+ovalIntv)), (int)Xcenter, (int)(Ycenter+(Radius+ovalIntv)+LineLength));
            g.drawLine((int)(Xcenter+(Radius+ovalIntv)), (int)(Ycenter), (int)(Xcenter+(Radius+ovalIntv)+LineLength), (int)(Ycenter));
            g.drawLine((int)(Xcenter-(Radius+ovalIntv)), (int)(Ycenter), (int)(Xcenter-(Radius+ovalIntv)-LineLength), (int)(Ycenter));
            g.drawLine((int)Xcalculated, (int)Ycalculated, (int)Xcenter, (int)Ycenter);
		}
	}	
	class SDPGaugePanel extends JPanel{//15,390,150,150
		double GaugeValue=0;
		double DrawAngle=0, Xcenter=0, Ycenter=0, Xoriginal=0;
		double Xcalculated =0;
		double Ycalculated =0;
		double RAD_CONVERT = 3.141592 / 180;
		
		SDPGaugePanel(){
			this.setBackground(new Color(240,240,240));
			}
		
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.BLACK);
			GaugeValue=tmpintSDP;
			DrawAngle = 90 * GaugeValue / 1000 * RAD_CONVERT;
			Xcenter = panSizeX/2;
            Ycenter = (panSizeY+2*labelFontSize)/2;
            Xcalculated = Xcenter - Radius * Math.cos(DrawAngle);
            Ycalculated = Ycenter - Radius * Math.sin(DrawAngle);
            g.drawOval((int)(Xcenter-(Radius+ovalIntv)), (int)(Ycenter-(Radius+ovalIntv)), (int)(2*(Radius+ovalIntv)), (int)(2*(Radius+ovalIntv)));
            g.drawLine((int)Xcenter, (int)(Ycenter-(Radius+ovalIntv)), (int)Xcenter, (int)(Ycenter-(Radius+ovalIntv)-LineLength));
            g.drawLine((int)Xcenter, (int)(Ycenter+(Radius+ovalIntv)), (int)Xcenter, (int)(Ycenter+(Radius+ovalIntv)+LineLength));
            g.drawLine((int)(Xcenter+(Radius+ovalIntv)), (int)(Ycenter), (int)(Xcenter+(Radius+ovalIntv)+LineLength), (int)(Ycenter));
            g.drawLine((int)(Xcenter-(Radius+ovalIntv)), (int)(Ycenter), (int)(Xcenter-(Radius+ovalIntv)-LineLength), (int)(Ycenter));
            g.drawLine((int)Xcalculated, (int)Ycalculated, (int)Xcenter, (int)Ycenter);
			}
		}
}