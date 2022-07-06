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
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JScrollBar;

class SimManual extends JFrame{
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
	double PbTry=0, xKill=0, px=0, xVert=0, xbVert=0, gasDen=0, gasVis=0, zz=0, Pbtm=0;
	double PVgain=0, SPP=0, pumpP=0, Pcasing=0, Pchoke=0;
	double Pbeff=0 , psia=0, QgDay=0, CHKpcent=0;  //, Tdelay; unused after we use Dt
	double[] volLqd = new double[MainDriver.Ntot];
	double volKmud=0, QtotMix=0, X=0, volkkmix=0;
	double[] ppks = new double[1000];
	double[] stks = new double[1000];
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
	int iMLX=0;  //to track whether we have a kick form ML(1) or not(0)
	double mlPVgain =0;
	double[] mlQgTotMold = new double[5];
	int iMLflux =0;   //to track kick influx from ML to MP; with (1)/ without (0)
	double dpchoke_old =0;
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
	int TimerCircTask2Finished = 1;	
	
	Timer TimerCheckBO;
	int TimerBOOn=0;
	
	int TimerRPOn=0;
	Timer TimerCheckRP;
	
	private JPanel contentPane;	
	JPanel SimAccPnl = new JPanel();
	JLabel simAccTitle = new JLabel("Simulation Acceleration Ratio");
	JLabel simTimelbl = new JLabel("Times Faster Than Real Time(1 to 20)");
	JTextField txtSimRate = new JTextField();
	JSlider HScrollRate = new JSlider();
	//JButton BtnSrtKill = new JButton("Start Kick Circulation Out");
	JButton cmdPumpKill = new JButton("Pump Kill Mud");
	JButton cmdFixPump = new JButton("Fix Pump Failure");
	JPanel CHKPnl = new JPanel();
	JTextField txtCHKopen = new JTextField();
	Scrollbar HScrollCHK = new Scrollbar();
	JPanel DrlInfoPnl = new JPanel();
	JTextArea operationMsg = new JTextArea("", 1, 100);
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
	
	MudGaugePanel MudGpnl = new MudGaugePanel();
	CHKGaugePanel CHKGpnl = new CHKGaugePanel();
	SDPGaugePanel SDPGpnl = new SDPGaugePanel();
	PumpPGaugePanel PumpPGpnl = new PumpPGaugePanel();
	
	JTextField txtSIMmode = new JTextField();
	JTextField txtRateDiff = new JTextField();
	JTextField txtPchoke = new JTextField();
	JTextField txtPsp = new JTextField();
	JTextField txtPumpGP = new JTextField();
	private final JLabel MudRRdif = new JLabel("Mud Return Rate Difference");
	private final JLabel MudRRdif2 = new JLabel("    (gal/min)");
	private final JLabel CHKPR = new JLabel("Choke Pressure(psig)");
	private final JLabel STPPP = new JLabel("Standpipe Pressure(psig)");
	private final JLabel lblSPP = new JLabel("Pump Pressure(psig)");
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
	private final JLabel SPP1 = new JLabel("0.0");
	private final JLabel SPP2 = new JLabel("1000");
	private final JLabel SPP3 = new JLabel("2000");
	private final JLabel SPP4 = new JLabel("3000");
	
	
		
	int InforPnlSrtX = 12, InforPnlSrtY = 10, panIntv = 10;
	int operationMsgSizeY = 23, SimAccPnlSizeY = 65;
	int CHKpnlSizeX = 290;
	int cmdBtnSizeX = 128, cmdBtnSizeY=25;
	int txtIntvX=12, txtSrtX = 5, txtSrtY=38, txtSizeX = 70, txtSizeY=18, txtIntvY=5;
	int txtLblXsize=194, txtLblYsize = txtSizeY;
	int panSizeX = 210, panSizeY = 190, pan1Xsrt = InforPnlSrtX+(txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize)+panIntv, pan1Ysrt = SimAccPnlSizeY+2*panIntv, xintv=10, yintv=10; 
	int GraphPnlSrtY = panIntv;
	int labelFontSize=12, labelFontSize2=12, RateDiffSize=12;
	int ovalIntv =5, LineLength=ovalIntv+1, Radius=panSizeY/4;
	double tmpintMud = 0, tmpintCHK=0, tmpintSDP = 0, tmpintPumpP=0;
	
	int btnSizeX = 127, btnSizeY = 30;
	private final JLabel lblPumpStk = new JLabel("Pump Stroke: ");
	private final JTextField txtStks1 = new JTextField();
	private final JTextField txtStks2 = new JTextField();
	private final JLabel lblPumpStk2 = new JLabel("Strokes");
	int pnlPumpRateSizeX = 2*btnSizeX+panIntv;
	int SimAccPnlSizeX = pnlPumpRateSizeX;
	int lblSelMudPumpSrtY =5; 
	int radioBtnSizeX = 85;
	int radioBtnSizeY = 23;	
	int txtPumpRSizeX = 50;
	int txtPumpRSizeY = 18;
	int ScrollSizeX = pnlPumpRateSizeX-lblSelMudPumpSrtY*2;
	int pnlPumpRateSizeY = panSizeY;		
	JScrollBar pumpScroll1 = new JScrollBar();
	JScrollBar pumpScroll2 = new JScrollBar();
	JPanel PnlPumpRate = new JPanel();		
	JLabel lblSelMudPump = new JLabel("Selection of Mud Pump");
	
	ButtonGroup PumpTypeGroup = new ButtonGroup();
	JRadioButton optPump1 = new JRadioButton("Pump #1");
	JRadioButton optPump2 = new JRadioButton("Pump #2");
	JSeparator sepPnlPump1 = new JSeparator();
	JLabel lblPumpRate = new JLabel("Pump Rate:");
	JTextField txtPumpR1 = new JTextField();
	JTextField txtPumpR2 = new JTextField();
	JLabel lblPumpRate2 = new JLabel("SPM");
	JButton btnSetStkZero1 = new JButton("Set stroke zero");
	JButton btnSetStkZero2 = new JButton("Set stroke zero");
	
	int frameXSize = 1100, frameYSize = 770;
	int GraphPnlXsize = pnlPumpRateSizeX, GraphPnlYsize = 167;
    int GraphXLoc =40, GraphYLoc = 35;
    int GraphXsize = GraphPnlXsize-2*GraphXLoc;
    int GraphYsize = GraphPnlYsize-2*GraphYLoc+15;// 15 = label font size
    int GraphYsize2 = panSizeY-2*GraphYLoc+15;
    
	Sgraph[] Sgg = new Sgraph[4];           
	Sgraph2 Sgg0 = new Sgraph2();
	
	JLabel lblChokeOpen = new JLabel("Choke Open % by Area Ratio (0 to 100)");
	
	wellpic m3 = new wellpic();
	
	JMenuBar menuBar = new JMenuBar();
	JMenu mHelp = new JMenu("Helps      ");
	JMenu mMenus = new JMenu("Menus      ");
	JMenu mGraph = new JMenu("Graphs    ");
	JMenu mPause = new JMenu("Pause/Continue");
	JMenu mContinue = new JMenu("Continue");
	private final JMenuItem mItemHelp = new JMenuItem("Helps");
	private final JMenuItem mItemBckToMain = new JMenuItem("Back to the Main Menu");
	private final JMenuItem mItemSrtWellKill = new JMenuItem("Start Kick Circulation Out");
	private final JMenu mWellbore = new JMenu("Wellbore");
	private final JMenuItem mItemShowWell = new JMenuItem("Show Wellbore");
	private final JMenuItem mItemWell = new JMenuItem("Hide Wellbore");
	private final JMenuItem mItmGrpHelp = new JMenuItem("Graph Help");
	private final JMenuItem mItmGrpIntv = new JMenuItem("Graph Interval");
	private final JMenuItem mItmPltGrp = new JMenuItem("Plot Graphs");
	private final JMenuItem mItmPause = new JMenuItem("Pause");
	private final JMenuItem mItmCont = new JMenuItem("Continue");
	
	ButtonGroup AutoManualGroup = new ButtonGroup();
	JRadioButton AutoSelect = new JRadioButton("Automatic by the computer");
	JRadioButton ManualSelect = new JRadioButton("Manual by the user");
	
	int TimerCirculationIntv = 1000;
	int TimerKickOutIntv = 2000;
	int TimerKillMudIntv = 2000; //20140217 AJW
	int dummy =0;
	
	int pumpFailure1=0;
	int pumpFailure2=0;//0: normal // 1: broken
	
	int WPshowStatus = 0;//current status => 1 : show 0: hide 20130916 ajw
	private final JTextArea txtN2phase = new JTextArea();
	
	resultPlot rp;
	
	double Stroke1=0, Stroke2=0, TotalStroke=0;
	double KillMudStroke1=0, KillMudStroke2=0, TotalKillMudStroke=0;
	double QcapacityEq = 0;
	int panIntvCalculated = ((txtSrtY+24*txtSizeY+28*txtIntvY)-3*panSizeY)/2; // ADDED BY jw 20140310
	private final JPanel pnlKMW = new JPanel();
	private final JLabel lblInput = new JLabel("Input Panel");
	private final JLabel lblEnterThreeKey = new JLabel("Enter 3 key elements and press OK button");
	private final JTextField txtKMW = new JTextField();
	private final Scrollbar scrlKMW = new Scrollbar();
	
	double Kmud_standard = MainDriver.Kmud; // 20140522 AJW
	
	//int pnlKMWSizeX = 320; //20140707 ajw
	int pnlKMWSizeX = 264;
	int pnlKMWSizeY = 163; //20140707 ajw
	private JTextField txtICP;
	private JTextField txtFCP;
	double totFri = 0;
    double temp_oMud = MainDriver.oMud;
	
	SimManual(){
		setTitle("SNU Well Control Simulator - Manual Control");
		setIconImage(MainDriver.icon.getImage());
		
		TimerCirculationOn = 0;	
		TimerCirculationIntv = 1000;
		TimerKickOutIntv = 2000;
		TimerKillMudIntv = 4000;

		MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;		
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;		
		
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
		 
		 m3.addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {	                
	                e.getWindow().setVisible(false);
	                MainDriver.iWshow = 0;
	                WPshowStatus=0;
	            }
	        });
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(m3.widthX, 0, frameXSize, frameYSize);		
		
		setJMenuBar(menuBar);		
		menuBar.add(mHelp);			
		mHelp.add(mItemHelp);
		menuBar.add(mMenus);	
			
		mMenus.add(mWellbore);		
		mItemShowWell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dummy=0;				
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
				if(MainDriver.KMWsettingOn == 1){
					if (MainDriver.iWshow == 1 && iDClk == 0 && WPshowStatus==0){
						m3.setVisible(true);
						WPshowStatus=1;
						}
					
					if (ifastRun != 1 || MainDriver.iWshow == 0){
						m3.setVisible(false);
						WPshowStatus=0;
						}  
					
					if(MainDriver.spMin1==0 && MainDriver.spMin2==0){
						String msg = "Oh, Boy !  You made a serious mistake !";
						JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
						}
					
					else if(HScrollCHK.getValue()==0 && MainDriver.imode == 1){
						String msg = "Oh, Boy !  You made a serious mistake !";
						JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
						}		    
					
					else{
						if(TimerCirculationOn ==0){
							TimerCirculationOn = 1;
							TimerCirculation = new Timer();
							TimerCirculation.schedule(new TimerCircTask2(), 1000, TimerCirculationIntv);
							//TimerCirculation.schedule(new TimerCircTask2(), 1000, 10);//g히히
							timeStart = System.currentTimeMillis()/1000;// sec
							operationMsg.setText("Well control simulation is on..");							
							}
						}	
					MainDriver.QtMix = MainDriver.Qkill;
					timeOld = timeStart;
					mudCase = -99;	
					operationMsg.setVisible(true);
					}
				else{
					String msg =  " Oh, Boy !  You made a serious mistake !"
					   	      + "\n" +" In order to start kick removal, Set the Kill mud weight, ICP, FCP values in the Input Panel first."
					   	      + "\n" +" And then click the OK button.";
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
					}
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
				String HelpMsg =  " To see the results in plots, use the Plot Graphs menu under the Graphs menu"
					      + "\n" +" or you can see all plots using the Results In Plots menu.";
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
		
		JMenuItem mItmPlotResults = new JMenuItem("Results In Plots");
		mItmPlotResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(TimerRPOn==0){
					 rp = new resultPlot();
					 rp.setVisible(true);
					 rp.menuSimulation.setEnabled(true);
					 MainDriver.RPMenuSelected=2;
					 TimerCheckRP = new Timer();
					 TimerCheckRP.schedule(new CheckRPTask(), 0, 50);
					 TimerRPOn=1;
					 }
				 else{
					 rp.setVisible(true);
					 rp.menuSimulation.setEnabled(true);
					 MainDriver.RPMenuSelected=2;
					 }
			}
		});
		
		
		
		mGraph.add(mItmPlotResults);
		menuBar.add(mPause);			
		mItmPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			    
			    if(timeStart!=0){ //140721 to prevent pause/continue buttons from activating
			    	iContinue = 1;
			    	if (TimerCirculationOn == 1){
			    		TimerCirculation.cancel();
			    		TimerCirculationOn = 0;
			    		}
			    	if (MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0 && WPshowStatus==0){
			    		m3.setVisible(true);
			    		WPshowStatus=1;
			    		}
			    	operationMsg.setText("Simulation is Paused !");
			    	}
			    }
			});
		
		mPause.add(mItmPause);
		mPause.add(mItmCont);
		
		mItmCont.addActionListener(new ActionListener() { //140721 to prevent pause/continue buttons from activating
			public void actionPerformed(ActionEvent arg0) {
			    if (iContinue == 1 && timeStart!=0){
			    	iContinue = 0;
			    	if (TimerCirculationOn==0){
			    		TimerCirculationOn=1;
			    		TimerCirculation = new Timer();
			    		TimerCirculation.schedule(new TimerCircTask2(), 0, TimerCirculationIntv);
			    		}			
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
				
			    String HelpMsg = "  From the Menus menu, you can load/unload the wellbore profile. You can move or adjust the size of the"
			    +"\n"+"  wellbore profile. As the first step, you should provide kill mud weight, initial circulating pressure(ICP), "	
			    +"\n" +" and final circulating pressure(FCP) in the Input Panel. As a default, the current setting is an automatic control by the computer."
			    +"\n"+"  For the automatic control, the computer will adjust the choke automatically to maintain BHP required."
			    +"\n"+"  However, note that you have to open the choke valve before starting the menu for the manual control."
			    +"\n" +" Then you can start the simulation by clicking the Start Kick Circulation Out menu under the Menus menu."
			    +"\n" +" After that, you will see kick movement. You can change the Simulation Rate any time. To see the results in plots,"
			    +"\n" +" use the Plot Graphs menu under the Graphs menu or you can see all plots using the Results In Plots menu."
			    +"\n" +" At the end of simulation, all the results are available as usual. If your simulation rate is too fast,"
			    +"\n" +" you may lose some detailed results in cases of small kicks, a small wellbore, a fast pump rate, etc."
			    +"\n" +" Then rerun using a lower simulation rate."
			    +"\n"+"     ^.^ GOOD LUCK ! ^.^";			      
			    JOptionPane.showMessageDialog(null, HelpMsg, "Help", JOptionPane.INFORMATION_MESSAGE);			    
			}
		});
		
		
		
		
		contentPane =  new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);	
		contentPane.setLayout(null);					
		operationMsg.setBackground(Color.YELLOW);
		/*BtnSrtKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (MainDriver.iWshow == 1 && iDClk == 0 && WPshowStatus==0){
					m3.setVisible(true);
					WPshowStatus=1;
				}
				
			    if (ifastRun != 1 || MainDriver.iWshow == 0){
			    	m3.setVisible(false);
			    	WPshowStatus=0;
			    }  
			    
			    if(MainDriver.spMin1==0 && MainDriver.spMin2==0){
			    	String msg = "Oh, Boy !  You made a serious mistake !";
			    	JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			    	}
			    
			    else if(HScrollCHK.getValue()==0 && MainDriver.imode == 1){
			    	String msg = "Oh, Boy !  You made a serious mistake !";
			    	JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			    	}		    
			    
			    else{
			    	if(TimerCirculationOn ==0){
			    		TimerCirculationOn = 1;
			    		TimerCirculation = new Timer();
			    		TimerCirculation.schedule(new TimerCircTask2(), 1000, TimerCirculationIntv);
			    		//TimerCirculation.schedule(new TimerCircTask2(), 1000, 10);//g히히
			    		timeStart = System.currentTimeMillis()/1000;// sec
			    		}
			    	}		
			    MainDriver.QtMix = MainDriver.Qkill;
			    timeOld = timeStart;
			    mudCase = -99;
			}
		});
		BtnSrtKill.setFont(new Font("굴림", Font.BOLD, 12));
		
		
		BtnSrtKill.setBounds(InforPnlSrtX, InforPnlSrtY+5, 200, cmdBtnSizeY);*/
		operationMsg.setBounds(InforPnlSrtX, pan1Ysrt-panIntv-operationMsgSizeY, txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize, operationMsgSizeY);
		SimAccPnl.setBounds(pan1Xsrt+panSizeX+panIntv, InforPnlSrtY, SimAccPnlSizeX, SimAccPnlSizeY);
							
		DrlInfoPnl.setBounds(InforPnlSrtX, pan1Ysrt, txtSrtX+txtSizeX+2*txtIntvX+txtLblXsize, txtSrtY+24*txtSizeY+28*txtIntvY);
		
		
		pnlKMW.setLayout(null);
		pnlKMW.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		pnlKMW.setBackground(SystemColor.menu);
		pnlKMW.setBounds(pan1Xsrt+panSizeX+panIntv, pan1Ysrt+cmdBtnSizeY+2, pnlKMWSizeX, pnlKMWSizeY);
		contentPane.add(pnlKMW);
		lblInput.setFont(new Font("굴림", Font.BOLD, 15));
		lblInput.setBounds(16, 8, 96, 15);
		
		pnlKMW.add(lblInput);
		lblEnterThreeKey.setFont(new Font("굴림", Font.PLAIN, 12));
		lblEnterThreeKey.setHorizontalAlignment(SwingConstants.LEFT);
		lblEnterThreeKey.setBounds(16, 30, 245, 15);
		
		pnlKMW.add(lblEnterThreeKey);
		
		txtKMW.setText("0");
		txtKMW.setHorizontalAlignment(SwingConstants.CENTER);
		txtKMW.setBackground(Color.YELLOW);
		txtKMW.setBounds(77, 58, 50, 24);
		
		pnlKMW.add(txtKMW);
		scrlKMW.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				if(arg0.getSource()==scrlKMW){
					double aa=0;
					if(MainDriver.iProblem[3]==0) txtKMW.setText((new DecimalFormat("##.##")).format((double)(scrlKMW.getValue())/10+MainDriver.Cmud));
					else txtKMW.setText((new DecimalFormat("##.##")).format((double)(scrlKMW.getValue())/10+MainDriver.oMud_save));
				}
			}
		});
		scrlKMW.setVisibleAmount(0);
		scrlKMW.setOrientation(Scrollbar.HORIZONTAL);
		scrlKMW.setMaximum(201);
		scrlKMW.setBackground(Color.WHITE);
		scrlKMW.setBounds(135, 58, 120, 24);
		
		pnlKMW.add(scrlKMW);
		
		JLabel lblKMW = new JLabel("Kill Mud");
		lblKMW.setHorizontalAlignment(SwingConstants.CENTER);
		lblKMW.setBounds(2, 55, 78, 15);
		pnlKMW.add(lblKMW);
		
		JLabel lblKMW2 = new JLabel("Weight(ppg)");
		lblKMW2.setHorizontalAlignment(SwingConstants.CENTER);
		lblKMW2.setBounds(2, 70, 78, 15);
		pnlKMW.add(lblKMW2);
		
		JLabel lblICP = new JLabel("ICP(psig)");
		lblICP.setHorizontalAlignment(SwingConstants.CENTER);
		lblICP.setBounds(2, 101, 78, 16);
		pnlKMW.add(lblICP);
		
		txtICP = new JTextField();
		txtICP.setText("0");
		txtICP.setHorizontalAlignment(SwingConstants.CENTER);
		txtICP.setBackground(Color.WHITE);
		txtICP.setBounds(77, 97, 50, 24);
		pnlKMW.add(txtICP);
		
		JLabel lblFCP = new JLabel("FCP(psig)");
		lblFCP.setHorizontalAlignment(SwingConstants.CENTER);
		lblFCP.setBounds(125, 101, 78, 15);
		pnlKMW.add(lblFCP);
		
		txtFCP = new JTextField();
		txtFCP.setText("0");
		txtFCP.setHorizontalAlignment(SwingConstants.CENTER);
		txtFCP.setBackground(Color.WHITE);
		txtFCP.setBounds(200, 97, 50, 24);
		pnlKMW.add(txtFCP);
		
		JButton btnKMWOK = new JButton("OK");
		btnKMWOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				MainDriver.Kmud = Double.parseDouble(txtKMW.getText());
				MainDriver.test_KMW = MainDriver.Kmud;
				
				try{
					MainDriver.test_ICP = Double.parseDouble(txtICP.getText());
					MainDriver.test_FCP = Double.parseDouble(txtFCP.getText());
				}catch(Exception e){
					MainDriver.test_ICP = 0;
					MainDriver.test_FCP = 0;
					txtICP.setText("0");
					txtFCP.setText("0");
					}				
				
			    if(MainDriver.test_ICP>0 && MainDriver.test_FCP>0){
			    	pnlKMW.setVisible(false); // 140707 AJW
				    cmdPumpKill.setVisible(true);
				    cmdFixPump.setVisible(true);
				    PnlPumpRate.setVisible(true);
			    	MainDriver.KMWsettingOn=1;
			    	MainDriver.test_ICP_Theory = MainDriver.temp_ICP;
			    	MainDriver.test_FCP_Theory = MainDriver.temp_FCP;
			    	MainDriver.test_KMW_Theory = MainDriver.temp_KMW;
			    }
			    else{
			    	MainDriver.KMWsettingOn=0;
			    	String HelpMsg =  " The values of ICP and FCP should be positive."
						      + "\n" +" Check those values again and then press the OK button.";
					JOptionPane.showMessageDialog(null, HelpMsg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			    	}
			}
		});
		btnKMWOK.setBounds(93, 126, 78, 24);
		pnlKMW.add(btnKMWOK);
		
		SimAccPnl.setLayout(null);
		SimAccPnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		SimAccPnl.setBackground(new Color(240, 240, 240));		
		contentPane.add(SimAccPnl);		
		
		simAccTitle.setHorizontalAlignment(SwingConstants.CENTER);
		simAccTitle.setFont(new Font("굴림", Font.BOLD, 12));
		simAccTitle.setBounds(12, 2, 214, 15);
		simAccTitle.setOpaque(false);
		SimAccPnl.add(simAccTitle);		
		
		simTimelbl.setHorizontalAlignment(SwingConstants.LEFT);
		simTimelbl.setBounds(22, 17, 254, 15);
		simTimelbl.setOpaque(false);
		SimAccPnl.add(simTimelbl);		
		txtSimRate.setBackground(Color.YELLOW);
		txtSimRate.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtSimRate.setText("0");
		txtSimRate.setBounds(15, 35, 50, 20);
		SimAccPnl.add(txtSimRate);
		
		HScrollRate.setBounds(75, 30, 180, 35);
		HScrollRate.setOpaque(false);
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
		
		
				
		//contentPane.add(BtnSrtKill);		
		
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
		txtVDtop.setBackground(Color.YELLOW);
		txtVDtop.setHorizontalAlignment(SwingConstants.RIGHT);
				
		txtVDtop.setText("0.0");
		txtVDtop.setEditable(true);
		DrlInfoPnl.add(txtVDtop);
		txtVDbottom.setBackground(Color.YELLOW);
		txtVDbottom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtVDbottom.setText("0.0");
		txtVDbottom.setEditable(true);
		DrlInfoPnl.add(txtVDbottom);
		txtMDkickTop.setBackground(Color.YELLOW);
		txtMDkickTop.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtMDkickTop.setText("0.0");
		txtMDkickTop.setEditable(true);
		DrlInfoPnl.add(txtMDkickTop);
		txtMDkickBottom.setBackground(Color.YELLOW);
		txtMDkickBottom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtMDkickBottom.setText("0.0");
		txtMDkickBottom.setEditable(true);
		DrlInfoPnl.add(txtMDkickBottom);
		txtPitGain.setBackground(Color.YELLOW);
		txtPitGain.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtPitGain.setText("0.0");
		DrlInfoPnl.add(txtPitGain);
		txtPumpP.setBackground(Color.YELLOW);
		txtPumpP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtPumpP.setText("0.0");
		DrlInfoPnl.add(txtPumpP);
		txtPumpRate.setBackground(Color.YELLOW);
		txtPumpRate.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtPumpRate.setText("0.0");
		DrlInfoPnl.add(txtPumpRate);
		txtReturnRate.setBackground(Color.YELLOW);
		txtReturnRate.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtReturnRate.setText(" ");
		txtReturnRate.setEditable(true);
		DrlInfoPnl.add(txtReturnRate);
		
		JLabel lblPresInfo = new JLabel("Pressure Infromation");
		lblPresInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPresInfo.setForeground(Color.BLUE);
		lblPresInfo.setFont(new Font("굴림", Font.BOLD, 17));		
		DrlInfoPnl.add(lblPresInfo);
		txtTime.setBackground(Color.YELLOW);
		txtTime.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtTime.setText("0:00:00");
		txtTime.setEditable(true);
		DrlInfoPnl.add(txtTime);
		txtFormationP.setBackground(Color.YELLOW);
		txtFormationP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtFormationP.setText("0.0");
		txtFormationP.setEditable(true);
		DrlInfoPnl.add(txtFormationP);
		txtBHP.setBackground(Color.YELLOW);
		txtBHP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtBHP.setText("0.0");
		txtBHP.setEditable(true);
		DrlInfoPnl.add(txtBHP);
		txtCasingP.setBackground(Color.YELLOW);
		txtCasingP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtCasingP.setText("14.7");
		txtCasingP.setEditable(true);
		DrlInfoPnl.add(txtCasingP);
		txtKickP.setBackground(Color.YELLOW);
		txtKickP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtKickP.setText("0.0");
		txtKickP.setEditable(true);		
		DrlInfoPnl.add(txtKickP);
		txtMudLineP.setBackground(Color.YELLOW);
		txtMudLineP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtMudLineP.setText("0.0");
		txtMudLineP.setEditable(true);
		DrlInfoPnl.add(txtMudLineP);
		txtChokeP.setBackground(Color.YELLOW);
		txtChokeP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtChokeP.setText("0.0");
		txtChokeP.setEditable(true);
		DrlInfoPnl.add(txtChokeP);
		txtSPP.setBackground(Color.YELLOW);
		txtSPP.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtSPP.setText("0.0");
		txtSPP.setEditable(true);
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
		txtGasRate.setBackground(Color.YELLOW);
		txtGasRate.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGasRate.setText(" ");
		txtGasRate.setEditable(true);		
		
		DrlInfoPnl.add(txtGasRate);
				
		DrlInfoPnl.add(lblReturnGasRate);
		txtVolume.setBackground(Color.YELLOW);
		txtVolume.setHorizontalAlignment(SwingConstants.RIGHT);
		txtVolume.setText(" ");
		txtVolume.setEditable(true);
		
		
		DrlInfoPnl.add(txtVolume);
		
		DrlInfoPnl.add(lblTotalMudVolume);
		txtStroke.setBackground(Color.YELLOW);
		txtStroke.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStroke.setText(" ");
		txtStroke.setEditable(true);		
		
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
		
		SDPGpnl.setBounds(pan1Xsrt, pan1Ysrt, panSizeX, panSizeY);
		CHKGpnl.setBounds(pan1Xsrt, pan1Ysrt+panSizeY+panIntvCalculated , panSizeX, panSizeY);
		MudGpnl.setBounds(pan1Xsrt, pan1Ysrt+panSizeY*2+panIntvCalculated*2 , panSizeX, panSizeY);
		CHKPnl.setBounds(pan1Xsrt+panSizeX+panIntv, pan1Ysrt+panSizeY+panIntvCalculated, pnlPumpRateSizeX, pnlPumpRateSizeY);
		contentPane.add(CHKGpnl);			
		
		contentPane.add(PumpPGpnl);
		PumpPGpnl.setBounds(pan1Xsrt+pnlPumpRateSizeX/2-panSizeX/2, pan1Ysrt, panSizeX, panSizeY);				
		PumpPGpnl.setVisible(false); //20140217 ajw
		contentPane.add(SDPGpnl);	
		
		contentPane.add(MudGpnl);
		
		
		for(int i=0; i<4; i++){
			Sgg[i] = new Sgraph();
			contentPane.add(Sgg[i]);
		}
		Sgg0 = new Sgraph2();
		contentPane.add(Sgg0);
		
		Sgg0.sgYMaxLim = 100000; Sgg0.sgYMinLim = 0;
		Sgg[0].sgYMaxLim = 4000; Sgg[0].sgYMinLim = 0;
		Sgg[1].sgYMaxLim = 3000; Sgg[1].sgYMinLim = 0;
		Sgg[2].sgYMaxLim = 12000; Sgg[2].sgYMinLim = 500;
		Sgg[3].sgYMaxLim = 100; Sgg[3].sgYMinLim = 0;		
		
		Sgg0.setBounds(pan1Xsrt+panSizeX+panIntv, pan1Ysrt+panSizeY*2+panIntvCalculated*2, GraphPnlXsize, panSizeY);	
		Sgg[0].setBounds(pan1Xsrt+panSizeX+panIntv+pnlPumpRateSizeX+panIntv, GraphPnlSrtY, GraphPnlXsize, GraphPnlYsize);
		Sgg[1].setBounds(pan1Xsrt+panSizeX+panIntv+pnlPumpRateSizeX+panIntv, GraphPnlSrtY+GraphPnlYsize+1, GraphPnlXsize, GraphPnlYsize);
		Sgg[2].setBounds(pan1Xsrt+panSizeX+panIntv+pnlPumpRateSizeX+panIntv, GraphPnlSrtY+GraphPnlYsize*2+2, GraphPnlXsize, GraphPnlYsize);
		Sgg[3].setBounds(pan1Xsrt+panSizeX+panIntv+pnlPumpRateSizeX+panIntv, GraphPnlSrtY+GraphPnlYsize*3+16, GraphPnlXsize, GraphPnlYsize);
		
		Sgg0.lblTitle = new JLabel("Kill Pump Pressure(psig) vs. Stroke Number");
		Sgg[0].lblTitle = new JLabel("Pump Pressure(psig) vs. Time(min)");
		Sgg[1].lblTitle = new JLabel("Surface Choke Pressure(psig) vs. Time(Min)");
		Sgg[2].lblTitle = new JLabel("Casing Seat Pressure(psig) vs. Time(Min)");
		Sgg[3].lblTitle = new JLabel("Kick Vol. in the Annulus(bbls) vs. Time(min)");
		
		for(int i=0; i<4; i++){
			Sgg[i].lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			Sgg[i].lblTitle.setBounds(GraphPnlXsize/2-300/2, 10, 300, 15);
			Sgg[i].add(Sgg[i].lblTitle);	
		}
		Sgg0.lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		Sgg0.lblTitle.setBounds(GraphPnlXsize/2-300/2, 10, 300, 15);
		Sgg0.add(Sgg0.lblTitle);
		//삭제대상
		
		
		MudGpnl.setLayout(null);
		CHKGpnl.setLayout(null);
		SDPGpnl.setLayout(null);
		PumpPGpnl.setLayout(null);
		
		txtPsp.setHorizontalAlignment(SwingConstants.CENTER);
		txtPchoke.setHorizontalAlignment(SwingConstants.CENTER);
		txtRateDiff.setHorizontalAlignment(SwingConstants.CENTER);
		
		MudGpnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));				
		
		txtRateDiff.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtRateDiff.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtRateDiff.setText("");
		txtRateDiff.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtRateDiff.setEditable(true);
		txtRateDiff.setBackground(Color.yellow);
		MudGpnl.add(txtRateDiff);		
		
		MudRRdif.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		MudRRdif.setHorizontalAlignment(SwingConstants.CENTER);
		MudRRdif.setBounds(0, 2, panSizeX, labelFontSize);
		MudRRdif2.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		MudRRdif2.setHorizontalAlignment(SwingConstants.LEFT);
		MudRRdif2.setBounds(0, 2+labelFontSize, panSizeX, labelFontSize+1);
		Mud1.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud1.setHorizontalAlignment(SwingConstants.CENTER);
		Mud1.setBounds(panSizeX/2-Radius-ovalIntv-LineLength-labelFontSize2*3, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		Mud2.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud2.setHorizontalAlignment(SwingConstants.CENTER);
		Mud2.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2-Radius-ovalIntv-LineLength-labelFontSize2, labelFontSize2*4, labelFontSize2);
		Mud3.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud3.setHorizontalAlignment(SwingConstants.CENTER);
		Mud3.setBounds(panSizeX/2+Radius+ovalIntv+LineLength-labelFontSize2, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		Mud4.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		Mud4.setHorizontalAlignment(SwingConstants.CENTER);
		Mud4.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2+Radius+ovalIntv+LineLength, labelFontSize2*4, labelFontSize2);
		
		MudGpnl.add(MudRRdif);
		MudGpnl.add(MudRRdif2);
		MudGpnl.add(Mud1);
		MudGpnl.add(Mud2);
		MudGpnl.add(Mud3);
		MudGpnl.add(Mud4);		
		
		CHKGpnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		txtPchoke.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtPchoke.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtPchoke.setText("");
		txtPchoke.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtPchoke.setEditable(true);
		txtPchoke.setBackground(Color.YELLOW);
		CHKGpnl.add(txtPchoke);			
		
		CHKPR.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		CHKPR.setHorizontalAlignment(SwingConstants.CENTER);
		CHKPR.setBounds(0, 2, panSizeX, labelFontSize+1);		
		CHK1.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK1.setHorizontalAlignment(SwingConstants.CENTER);
		CHK1.setBounds(panSizeX/2-Radius-ovalIntv-LineLength-labelFontSize2*3, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		CHK2.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK2.setHorizontalAlignment(SwingConstants.CENTER);
		CHK2.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2-Radius-ovalIntv-LineLength-labelFontSize2, labelFontSize2*4, labelFontSize2);
		CHK3.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK3.setHorizontalAlignment(SwingConstants.CENTER);
		CHK3.setBounds(panSizeX/2+Radius+ovalIntv+LineLength-labelFontSize2, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		CHK4.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		CHK4.setHorizontalAlignment(SwingConstants.CENTER);
		CHK4.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2+Radius+ovalIntv+LineLength, labelFontSize2*4, labelFontSize2);
		
		CHKGpnl.add(CHKPR);
		CHKGpnl.add(CHK1);
		CHKGpnl.add(CHK2);
		CHKGpnl.add(CHK3);
		CHKGpnl.add(CHK4);
		
		CHKPnl.setLayout(null);
		CHKPnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		CHKPnl.setBackground(SystemColor.menu);		
		contentPane.add(CHKPnl);
		
		JLabel lblChokeControlPanel = new JLabel("Choke Control Panel");
		lblChokeControlPanel.setHorizontalAlignment(SwingConstants.CENTER);
		lblChokeControlPanel.setFont(new Font("굴림", Font.BOLD, 15));
		lblChokeControlPanel.setBounds(0, 15, 264, 15);
		CHKPnl.add(lblChokeControlPanel);
		
		
		lblChokeOpen.setHorizontalAlignment(SwingConstants.CENTER);
		lblChokeOpen.setBounds(0, 35, 264, 15);
		CHKPnl.add(lblChokeOpen);
		
		AutoManualGroup.add(AutoSelect);
		AutoSelect.setHorizontalAlignment(SwingConstants.LEFT);
		AutoSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.iCHKcontrol = 1;
				MainDriver.imode = 2;
			}
		});
		
		AutoSelect.setBackground(SystemColor.menu);
		AutoSelect.setBounds(10, 70, 190, 23);
		ManualSelect.setBounds(10, 95, 170, 23);
		CHKPnl.add(AutoSelect);
		
		AutoManualGroup.add(ManualSelect);
		ManualSelect.setHorizontalAlignment(SwingConstants.LEFT);
		ManualSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iCHKcontrol = 2;
				MainDriver.imode = 1;
			}
		});
		
		ManualSelect.setBackground(SystemColor.menu);
		
		CHKPnl.add(ManualSelect);
		
		HScrollCHK.setMaximum(1001); //140727 AJW
		//HScrollCHK.setMaximum(101);
		HScrollCHK.setVisibleAmount(0);
		txtCHKopen.setBackground(Color.YELLOW);
		txtCHKopen.setHorizontalAlignment(SwingConstants.CENTER);
		txtCHKopen.setText("0");
		txtCHKopen.setBounds(5, 140, 50, 24);
		CHKPnl.add(txtCHKopen);		
		
		HScrollCHK.setBackground(Color.WHITE);
		HScrollCHK.setOrientation(Scrollbar.HORIZONTAL);
		HScrollCHK.setBounds(60, 140, pnlPumpRateSizeX-10-55, 24);
		CHKPnl.add(HScrollCHK);				
				
		
		
		HScrollCHK.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				if(arg0.getSource()==HScrollCHK){
					//txtCHKopen.setText(Integer.toString(HScrollCHK.getValue()));
					double temp=0;					
					temp =HScrollCHK.getValue()/10.0;
					txtCHKopen.setText((new DecimalFormat("#0.0")).format(temp)); //140727 ajw
				}
				if(MainDriver.iWshow == 1 && ifastRun == 1 && iDClk == 0 && WPshowStatus==0){
					m3.setVisible(true);
					WPshowStatus=1;
				}
			}
		});
		
		
		SDPGpnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		txtPsp.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtPsp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtPsp.setText("");
		txtPsp.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtPsp.setEditable(true);
		txtPsp.setBackground(Color.YELLOW);
		SDPGpnl.add(txtPsp);			
		STPPP.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		STPPP.setHorizontalAlignment(SwingConstants.CENTER);
		STPPP.setBounds(0, 2, panSizeX, labelFontSize+1);
		STP1.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP1.setHorizontalAlignment(SwingConstants.CENTER);
		STP1.setBounds(panSizeX/2-Radius-ovalIntv-LineLength-labelFontSize2*3, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		STP2.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP2.setHorizontalAlignment(SwingConstants.CENTER);
		STP2.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2-Radius-ovalIntv-LineLength-labelFontSize2, labelFontSize2*4, labelFontSize2);
		STP3.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP3.setHorizontalAlignment(SwingConstants.CENTER);
		STP3.setBounds(panSizeX/2+Radius+ovalIntv+LineLength-labelFontSize2, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		STP4.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		STP4.setHorizontalAlignment(SwingConstants.CENTER);
		STP4.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2+Radius+ovalIntv+LineLength, labelFontSize2*4, labelFontSize2);
		
		SDPGpnl.add(STPPP);
		SDPGpnl.add(STP1);
		SDPGpnl.add(STP2);
		SDPGpnl.add(STP3);
		SDPGpnl.add(STP4);	
		
		PumpPGpnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		PumpPGpnl.setLayout(null);
		txtPumpGP.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtPumpGP.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtPumpGP.setBackground(Color.YELLOW);
		txtPumpGP.setText("");
		txtPumpGP.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtPumpGP.setEditable(true);
		PumpPGpnl.add(txtPumpGP);	
		
		lblSPP.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		lblSPP.setHorizontalAlignment(SwingConstants.CENTER);
		lblSPP.setBounds(0, 2, panSizeX, labelFontSize+1);
		SPP1.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		SPP1.setHorizontalAlignment(SwingConstants.CENTER);
		SPP1.setBounds(panSizeX/2-Radius-ovalIntv-LineLength-labelFontSize2*3, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		SPP2.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		SPP2.setHorizontalAlignment(SwingConstants.CENTER);
		SPP2.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2-Radius-ovalIntv-LineLength-labelFontSize2, labelFontSize2*4, labelFontSize2);
		SPP3.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		SPP3.setHorizontalAlignment(SwingConstants.CENTER);
		SPP3.setBounds(panSizeX/2+Radius+ovalIntv+LineLength-labelFontSize2, (panSizeY+labelFontSize)/2-labelFontSize2/2, labelFontSize2*4, labelFontSize2);
		SPP4.setFont(new Font("굴림", Font.PLAIN, labelFontSize2));
		SPP4.setHorizontalAlignment(SwingConstants.CENTER);
		SPP4.setBounds(panSizeX/2-labelFontSize2*2, (panSizeY+labelFontSize)/2+Radius+ovalIntv+LineLength, labelFontSize2*4, labelFontSize2);
		
		PumpPGpnl.add(lblSPP);
		PumpPGpnl.add(SPP1);
		PumpPGpnl.add(SPP2);
		PumpPGpnl.add(SPP3);
		PumpPGpnl.add(SPP4);	
		
		if(MainDriver.spMin1!=0){
			optPump1.setSelected(true);
			txtPumpR1.setVisible(true);
			txtPumpR2.setVisible(false);
			pumpScroll1.setVisible(true);
			pumpScroll2.setVisible(false);
			txtStks1.setVisible(true);
			txtStks2.setVisible(false);
		}
		else{
			optPump2.setSelected(true);
			txtPumpR1.setVisible(false);
			txtPumpR2.setVisible(true);
			pumpScroll1.setVisible(false);
			pumpScroll2.setVisible(true);
			txtStks1.setVisible(false);
			txtStks2.setVisible(true);
		}
		
		
		
		int lblPumpRateSrtY=lblSelMudPumpSrtY+15+radioBtnSizeY+5+5;
		
		pumpScroll1.setMinimum(1);
		
		pumpScroll1.setMaximum(150);
		pumpScroll2.setMaximum(150);
		pumpScroll1.setMinimum(0);
		pumpScroll2.setMinimum(0);
		pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
		pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
		txtPumpR1.setBackground(Color.YELLOW);
		txtPumpR1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
		txtPumpR2.setBackground(Color.YELLOW);
		txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
		txtPumpR2.setHorizontalAlignment(SwingConstants.RIGHT);
		
		pumpScroll1.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				if(pumpFailure1==0 || TimerCirculationOn==0){
					if(MainDriver.spMin1==0 && pumpScroll1.getValue()==0){
						pumpScroll1.setValue(1);
						MainDriver.spMin1=1;
						}
					else{
						MainDriver.spMin1 = pumpScroll1.getValue();
						}
					
					/*if(MainDriver.spMin1==0 && MainDriver.spMin2==0){
						MainDriver.spMin1=1;
						pumpScroll1.setValue(1);
					}*/
					txtPumpR1.setText((new DecimalFormat("###0")).format(MainDriver.spMin1));
					MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
					QcapacityEq = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2); //20140212 ajw
					
					if(MainDriver.spMin1==0 && MainDriver.spMin2==0 && pumpFailure1==0){
						if(TimerCirculationOn==1) pumpFailure1=1;
					}
					if ((pumpFailure2==1 &&  TimerCirculationOn==0 && pumpFailure1==0)){
			    		TimerCirculationOn=1;
			    		TimerCirculation = new Timer();
			    		TimerCirculation.schedule(new TimerCircTask2(), 0, TimerCirculationIntv);
			    		if( MainDriver.Np <= MainDriver.NpSi + 10) MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.NpSi]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.NpSi] + 1));
			    		else MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np - 4]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np- 4]));
			    		MainDriver.QtMix = propertyModule.range(MainDriver.QtMix, MainDriver.Qkill, MainDriver.Qkill * 4.5);
			    	}
				}
				
				/*if (volKmud==0){
					//PlotKillSheet();
					MainDriver.Qcapeq[0] = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2);
					MainDriver.KillVolChange[0]=0;
					MainDriver.QKillChange[0] = MainDriver.Qkill;
				}*/
			/*	else{
					if(Math.abs(QcapacityEq-MainDriver.Qcapeq[MainDriver.NumQcapeq])>0.000001){
						MainDriver.NumQcapeq = MainDriver.NumQcapeq+1;
						MainDriver.Qcapeq[MainDriver.NumQcapeq] = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2);
						MainDriver.StrokeChange[MainDriver.NumQcapeq]=TotalKillMudStroke;
						MainDriver.KillVolChange[MainDriver.NumQcapeq]=volKmud;
						MainDriver.QKillChange[MainDriver.NumQcapeq] = MainDriver.Qkill;		
 					}
					else if(MainDriver.spMin2!=0){
						MainDriver.NumQcapeq = MainDriver.NumQcapeq+1;
						MainDriver.Qcapeq[MainDriver.NumQcapeq] = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2);
						MainDriver.StrokeChange[MainDriver.NumQcapeq]=TotalKillMudStroke;
						MainDriver.KillVolChange[MainDriver.NumQcapeq]=volKmud;
						MainDriver.QKillChange[MainDriver.NumQcapeq] = MainDriver.Qkill;	
					}
					else{
						if(MainDriver.spMin2==0 && MainDriver.NumQcapeq==0) MainDriver.QKillChange[0] = MainDriver.Qkill;
					}
						
				}*/
			}
		});
		
		pumpScroll2.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				if(pumpFailure2==0 || TimerCirculationOn==0){
					if(MainDriver.spMin2==0 && pumpScroll2.getValue()==0){
						pumpScroll2.setValue(1);
						MainDriver.spMin2=1;
						}
					else{
						MainDriver.spMin2 = pumpScroll2.getValue();
						}
					/*if(MainDriver.spMin1==0 && MainDriver.spMin2==0){
						MainDriver.spMin2=1;
						pumpScroll2.setValue(1);
					}*/
					txtPumpR2.setText((new DecimalFormat("###0")).format(MainDriver.spMin2));
					MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
					QcapacityEq = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2); //20140212 ajw
					
					if(MainDriver.spMin1==0 && MainDriver.spMin2==0 && pumpFailure2==0){
						if(TimerCirculationOn==1) pumpFailure2=1;
						}
					if ((pumpFailure1==1 && TimerCirculationOn==0  && pumpFailure2==0)){
			    		TimerCirculationOn=1;
			    		TimerCirculation = new Timer();
			    		TimerCirculation.schedule(new TimerCircTask2(), 0, TimerCirculationIntv);
			    		if( MainDriver.Np <= MainDriver.NpSi + 10) MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.NpSi]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.NpSi] + 1));
			    		else MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np - 4]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np- 4]));
			    		MainDriver.QtMix = propertyModule.range(MainDriver.QtMix, MainDriver.Qkill, MainDriver.Qkill * 4.5);
			    		}
					}
				/*if (volKmud==0){
					PlotKillSheet();
					MainDriver.Qcapeq[0] = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2);
					MainDriver.KillVolChange[0]=0;
					MainDriver.QKillChange[0] = MainDriver.Qkill;
					}*/
			/*	else{
					if(Math.abs(QcapacityEq-MainDriver.Qcapeq[MainDriver.NumQcapeq])>0.000001){
						MainDriver.NumQcapeq = MainDriver.NumQcapeq+1;
						MainDriver.Qcapeq[MainDriver.NumQcapeq] = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2);
						MainDriver.StrokeChange[MainDriver.NumQcapeq]=TotalKillMudStroke;
						MainDriver.KillVolChange[MainDriver.NumQcapeq]=volKmud;
						MainDriver.QKillChange[MainDriver.NumQcapeq] = MainDriver.Qkill;		
 					}
					else if(MainDriver.spMin1!=0){
						MainDriver.NumQcapeq = MainDriver.NumQcapeq+1;
						MainDriver.Qcapeq[MainDriver.NumQcapeq] = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2);
						MainDriver.StrokeChange[MainDriver.NumQcapeq]=TotalKillMudStroke;
						MainDriver.KillVolChange[MainDriver.NumQcapeq]=volKmud;
						MainDriver.QKillChange[MainDriver.NumQcapeq] = MainDriver.Qkill;		
					}
					else{
						if(MainDriver.spMin1==0 && MainDriver.NumQcapeq==0) MainDriver.QKillChange[0] = MainDriver.Qkill;
					}
				}*/
			}
		});	
		
		PnlPumpRate.setBounds(pan1Xsrt+panSizeX+panIntv, pan1Ysrt+cmdBtnSizeY+2, pnlPumpRateSizeX, pnlPumpRateSizeY-cmdBtnSizeY-2);	
		
		PnlPumpRate.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		PnlPumpRate.setBackground(SystemColor.menu);
		
		PnlPumpRate.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));		
		PnlPumpRate.setBackground(SystemColor.menu);				
		
		//PnlPumpRate.setBounds(pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize+panIntv, 10+SimAccPnlSizeY+panIntv*2+panSizeY, pnlPumpRateSizeX, pnlPumpRateSizeY);
		contentPane.add(PnlPumpRate);
		PnlPumpRate.setLayout(null);		
		
		lblSelMudPump.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelMudPump.setFont(new Font("굴림", Font.BOLD, 15));
		lblSelMudPump.setBounds(0, 5, 264, 15);
		PnlPumpRate.add(lblSelMudPump);				
		
		PumpTypeGroup.add(optPump1);
		PumpTypeGroup.add(optPump2);	
		optPump1.setHorizontalAlignment(SwingConstants.CENTER);
		
		optPump1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtPumpR1.setVisible(true);
				txtPumpR2.setVisible(false);
				pumpScroll1.setVisible(true);
				pumpScroll2.setVisible(false);
				txtStks1.setVisible(true);
				txtStks2.setVisible(false);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(true);
				btnSetStkZero2.setVisible(false);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
			}
		});
		
		
		PnlPumpRate.add(optPump1);		
		optPump2.setHorizontalAlignment(SwingConstants.CENTER);
		optPump2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtPumpR1.setVisible(false);
				txtPumpR2.setVisible(true);
				pumpScroll1.setVisible(false);
				pumpScroll2.setVisible(true);
				txtStks1.setVisible(false);
				txtStks2.setVisible(true);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(false);
				btnSetStkZero2.setVisible(true);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
			}
		});
		optPump1.setBounds((pnlPumpRateSizeX-radioBtnSizeX*2-panIntv*2)/2-10, lblSelMudPumpSrtY+18, radioBtnSizeX, radioBtnSizeY);
		optPump2.setBounds((pnlPumpRateSizeX-radioBtnSizeX*2-panIntv*2)/2+radioBtnSizeX+2*panIntv+15, lblSelMudPumpSrtY+18, radioBtnSizeX, radioBtnSizeY);
		optPump1.setOpaque(false);
		optPump2.setOpaque(false);
		
		PnlPumpRate.add(optPump2);		
		sepPnlPump1.setForeground(Color.BLACK);
		
		sepPnlPump1.setBounds(2, lblSelMudPumpSrtY+15+radioBtnSizeY+2, pnlPumpRateSizeX-2, 2);
		PnlPumpRate.add(sepPnlPump1);						
		
		lblPumpRate.setBounds(lblSelMudPumpSrtY, lblPumpRateSrtY+2, 80, txtPumpRSizeY);
		PnlPumpRate.add(lblPumpRate);				
		
		txtPumpR1.setBounds(lblSelMudPumpSrtY+80, lblPumpRateSrtY+2, txtPumpRSizeX, txtPumpRSizeY);
		PnlPumpRate.add(txtPumpR1);		
		txtPumpR2.setBounds(lblSelMudPumpSrtY+80, lblPumpRateSrtY+2, txtPumpRSizeX, txtPumpRSizeY);
		PnlPumpRate.add(txtPumpR2);	
		
		
		lblPumpRate2.setBounds(lblSelMudPumpSrtY+80+txtPumpRSizeX+3, lblPumpRateSrtY+2, 45, txtPumpRSizeY);
		PnlPumpRate.add(lblPumpRate2);		
		
		pumpScroll1.setOrientation(JScrollBar.HORIZONTAL);
		pumpScroll1.setBounds((pnlPumpRateSizeX-ScrollSizeX)/2, lblPumpRateSrtY+txtPumpRSizeY+5, ScrollSizeX, txtPumpRSizeY);
		PnlPumpRate.add(pumpScroll1);
		pumpScroll2.setOrientation(JScrollBar.HORIZONTAL);
		pumpScroll2.setBounds((pnlPumpRateSizeX-ScrollSizeX)/2, lblPumpRateSrtY+txtPumpRSizeY+5, ScrollSizeX, txtPumpRSizeY);
		PnlPumpRate.add(pumpScroll2);
		
		lblPumpStk.setHorizontalAlignment(SwingConstants.LEFT);
		lblPumpStk.setBounds(lblSelMudPumpSrtY, lblPumpRateSrtY+txtPumpRSizeY*2+18, 80, txtPumpRSizeY);
		
		PnlPumpRate.add(lblPumpStk);
		txtStks1.setBackground(Color.YELLOW);
		txtStks1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtStks1.setBounds(lblSelMudPumpSrtY+80, lblPumpRateSrtY+txtPumpRSizeY*2+18, txtPumpRSizeX, txtPumpRSizeY);		
		PnlPumpRate.add(txtStks1);
		txtStks2.setBackground(Color.YELLOW);
		txtStks2.setBounds(lblSelMudPumpSrtY+80, lblPumpRateSrtY+txtPumpRSizeY*2+18, txtPumpRSizeX, txtPumpRSizeY);	
		txtStks2.setHorizontalAlignment(SwingConstants.RIGHT);
		PnlPumpRate.add(txtStks2);
		
		lblPumpStk2.setHorizontalAlignment(SwingConstants.LEFT);
		lblPumpStk2.setBounds(lblSelMudPumpSrtY+80+txtPumpRSizeX+3, lblPumpRateSrtY+txtPumpRSizeY*2+18, 45, txtPumpRSizeY);
		
		PnlPumpRate.add(lblPumpStk2);		
		
		btnSetStkZero1.setBounds(pnlPumpRateSizeX/2-80, lblPumpRateSrtY+txtPumpRSizeY*3+17+5, 160, 25);
		PnlPumpRate.add(btnSetStkZero1);			
		btnSetStkZero2.setBounds(pnlPumpRateSizeX/2-80, lblPumpRateSrtY+txtPumpRSizeY*3+17+5, 160, 25);
		PnlPumpRate.add(btnSetStkZero2);	
		
		cmdFixPump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				pumpFailure1=0;
				pumpFailure2=0;
				optPump1.setEnabled(true);
				optPump2.setEnabled(true);
				
				cmdFixPump.setVisible(false);
			}
		});		
		cmdFixPump.setBounds(pan1Xsrt+panSizeX+panIntv+cmdBtnSizeX+2, pan1Ysrt, cmdBtnSizeX, cmdBtnSizeY);
		contentPane.add(cmdFixPump);
		
		cmdPumpKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(TimerCirculationOn == 0){
					String warning = "You have the WRONG sequence ! ===> See the Help for the correct sequence.";
					if (MainDriver.iWshow == 1 && iDClk == 0) m3.setVisible(true);
				}
				else{
					volKmud = 0;
					MainDriver.iDone = 1; 
					MainDriver.vKmudSt[0] = 0;
					MainDriver.Pcontrol[0] = pumpP; //pumpP is a pump pressure.
					MainDriver.Method = 2;
					MainDriver.gMudKill = 0.052 * MainDriver.Kmud;
					MainDriver.gMudCirc = MainDriver.gMudKill;
					MainDriver.Cmud = MainDriver.Kmud;   //Now start Engineer's Method
					cmdPumpKill.setVisible(false);
					timeOld = (long) MainDriver.gTcum;
					if(MainDriver.iWshow == 1 && iDClk == 0) m3.setVisible(true);					
					MainDriver.set0_Starttime = MainDriver.gTcum;
				}
			}
		});
		
		cmdPumpKill.setBounds(pan1Xsrt+panSizeX+panIntv, pan1Ysrt, cmdBtnSizeX, cmdBtnSizeY);
		
		contentPane.add(cmdPumpKill);
		
		btnSetStkZero1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.reset_Stroke1=Stroke1;
				txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));		
				if(MainDriver.set0_Finishtime<=0) MainDriver.set0_Finishtime = MainDriver.gTcum;
			}
		});
		btnSetStkZero2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.reset_Stroke2=Stroke2;
				txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));
			}
		});
		
	}
	
	class CheckRPTask extends TimerTask{
		public void run(){
			if(MainDriver.RPMenuSelected==0){
				setVisible(false);
				MainDriver.iWshow=0;
				m3.setVisible(false);
				menuClose();
				TimerRPOn=0;
				MainDriver.RPMenuSelected=2;
				TimerCheckRP.cancel();
				}
		else if(MainDriver.RPMenuSelected==1){
				setVisible(true);
				TimerRPOn=0;
				MainDriver.RPMenuSelected=2;
				TimerCheckRP.cancel();
				} // o: to the main menu, 1: to the simulation panel.)			
			}
	}
	
	
	void PlotKillSheet(){
	    double gmc2 = MainDriver.gMudCirc;
	    double cmud2 = MainDriver.Cmud;
	    double volks = 0, psp3 =0, pp3=0, pmin=0;
	    double Cmud_standard = 0;
	    double gMudKill_standard = 0;
	    double gMudCirc_standard = 0;
	    
	    MainDriver.gMudCirc = 0.052 * Kmud_standard;
	    MainDriver.Cmud = Kmud_standard;// just plot the kill sheet
		
	    if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
	    else if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(MainDriver.Pb, MainDriver.initQkill, volks);
	    
	    psp3 = getDPinsideEX[0];
	    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    iCP = pp3; 
	    ppks[MainDriver.NwcE + 3] = pp3; 
	    stks[MainDriver.NwcE + 3] = 0;
	    for(int i = (MainDriver.NwcE + 2); i>MainDriver.NwcS; i--){
	    	volks = volks + MainDriver.volDS[i-1];
	    	if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
	    	else if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(MainDriver.Pb, MainDriver.initQkill, volks);
		    psp3 = getDPinsideEX[0];
		    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	        ppks[i] = pp3;
	        stks[i] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2));        
	    }
	    pmin = pp3;
	    volks = volks + 0.1;  // just pass the BIT
	    if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
	    else if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(MainDriver.Pb, MainDriver.initQkill, volks);
	    
	    psp3 = getDPinsideEX[0];
	    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    
	    ppks[MainDriver.NwcS] = pp3; 
	    stks[MainDriver.NwcS] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2));
	    
	    volks = volks + 20.5;    //10.5 bbls more for plotting purpose !	    
	    fCP = pp3;
	    ppks[MainDriver.NwcS - 1] = pp3;
	    stks[MainDriver.NwcS - 1] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2));
	//
	//-------------------------------- specify the graph//s properties !
	//   There are some diffeMainDriver.RenCe in plotting between v.6 and v.3
	//
	    Sgg0.ColumnCount = 2;
	    Sgg0.RowCount = MainDriver.NwcE - MainDriver.NwcS + 5;  // total number of DATA-1 = (NWCE+2)-(NWCS-2)+1
	//    Sgraph0.Plot.Axis.AxisScale = Int((icp + 20.5) / 10) * 10
	//    Sgraph0.YAxisMin = Int((fcp - 19.5) / 10) * 10
	//    Sgraph0.YAxisTicks = 10
	    for(int i = (MainDriver.NwcS - 1); i<(MainDriver.NwcE+4); i++){
	        Sgg0.Row = i - MainDriver.NwcS + 1;
	        Sgg0.Column = 0;
	        Sgg0.SgxData[Sgg0.Row] = stks[i];	        
	        Sgg0.Column = 1;
	        Sgg0.SgyData[Sgg0.Row] = ppks[i] - 14.7;
	    }	    
	    
	    //int sg0sepXnum=6; int sg0sepYnum=6;
	    //int sg0sepXIntv=1; int sg0sepYIntv=1;
	    Sgg0.calcGraphIntv(Sgg0.sgYMaxLim, Sgg0.sgYMinLim);	   
	    Sgg0.repaint();
	    //Sgg0.Plot.UniformAxis = False
	    MainDriver.gMudCirc =  gmc2; 
	    MainDriver.Cmud =  cmud2;	
	}
		
	
	void PlotActualKillSheet(){
	//----- plot the pump pressure on the top of kill sheet !
	// 7/26/01
	//
		double gmc2 = MainDriver.gMudCirc;
	    double cmud2 = MainDriver.Cmud;
	    double volks = 0, psp3 =0, pp3=0, pmin=0;
		int nData = MainDriver.NwcE - MainDriver.NwcS + 5 + MainDriver.NumQcapeq;
		int npData = Math.max(nData, MainDriver.iDone);
		int checkChangeNum=1;
		
		// Plot kill sheet
		//MainDriver.gMudCirc = MainDriver.gMudKill;  
	    //MainDriver.Cmud = MainDriver.Kmud;     // just plot the kill sheet
		MainDriver.Cmud = Kmud_standard;
		MainDriver.gMudCirc = 0.052*Kmud_standard;  
	       
	    if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
	    else if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(MainDriver.Pb, MainDriver.initQkill, volks);
	    psp3 = getDPinsideEX[0];
	    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    iCP = pp3; 
	    ppks[MainDriver.NwcE + 3 + 2*MainDriver.NumQcapeq] = pp3; 
	    stks[MainDriver.NwcE + 3 + 2*MainDriver.NumQcapeq] = 0;
	   // if(MainDriver.NumQcapeq==0){
	    	for(int i = (MainDriver.NwcE + 2); i>MainDriver.NwcS; i--){
		    	volks = volks + MainDriver.volDS[i-1];
		    	if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
			    else if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(MainDriver.Pb, MainDriver.initQkill, volks);
			    psp3 = getDPinsideEX[0];
			    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
		        ppks[i] = pp3;
		        stks[i] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2));        
		        }
	//    	}
	/*    else if(MainDriver.NumQcapeq>0){	    	
	    	for(int i = (MainDriver.NwcE + 2); i>MainDriver.NwcS; i--){	  
	    		dummy=0;
	    		while(checkChangeNum<=MainDriver.NumQcapeq){
	    			if(MainDriver.KillVolChange[checkChangeNum]>volks && MainDriver.KillVolChange[checkChangeNum]<volks+MainDriver.volDS[i-1]){
	    				getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], MainDriver.KillVolChange[checkChangeNum]);
	    				psp3 = getDPinsideEX[0];
	    			    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    			    checkChangeNum++;
	    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))] = pp3;
	    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))] = MainDriver.StrokeChange[checkChangeNum-1];
	    		        
	    		        getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], MainDriver.KillVolChange[checkChangeNum-1]);
	    				psp3 = getDPinsideEX[0];
	    			    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] = pp3;
	    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] = MainDriver.StrokeChange[checkChangeNum-1];
	    		        
	    		        if(MainDriver.KillVolChange[checkChangeNum]<=volks || MainDriver.KillVolChange[checkChangeNum]>=volks+MainDriver.volDS[i-1]){
	    		        	volks = volks + MainDriver.volDS[i-1];
		    		    	getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], volks);
		    			    psp3 = getDPinsideEX[0];
		    			    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
		    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = pp3;
		    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] + (int)((volks-MainDriver.KillVolChange[checkChangeNum-1])/MainDriver.Qcapeq[checkChangeNum-1]);
		    		        dummy=1;
		    		        break;		    		        
		    		        }
	    		        }
	    			
	    			else{
	    				volks = volks + MainDriver.volDS[i-1];
	    		    	getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], volks);
	    			    psp3 = getDPinsideEX[0];
	    			    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = pp3;
	    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] + (int)(MainDriver.volDS[i-1]/MainDriver.Qcapeq[checkChangeNum-1]);    
	    		        dummy=1;
	    		        break;	    		        
	    		        }
	    			}	    	
	    		if(checkChangeNum>MainDriver.NumQcapeq && dummy==0){
	    			volks = volks + MainDriver.volDS[i-1];
	    			getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], volks);
				    psp3 = getDPinsideEX[0];
				    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
			        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = pp3;
			        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] + (int)(MainDriver.volDS[i-1]/MainDriver.Qcapeq[checkChangeNum-1]);     
			        }
	    		}
	    	dummy=0;
	    	}*/ 
	    	
		//
		//-------------------------------- specify the graph//s properties !
		//   There are some diffeMainDriver.RenCe in plotting between v.6 and v.3
		//
		    Sgg0.ColumnCount = 2;
		    Sgg0.RowCount = MainDriver.NwcE - MainDriver.NwcS + 5; 
	    
	    pmin = pp3;
	    
	    volks = volks + 0.1;  // just pass the BIT
	    if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.initQkill, volks);
	    else if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(MainDriver.Pb, MainDriver.initQkill, volks);
	    
	    psp3 = getDPinsideEX[0];
	    pp3 = getDPinsideEX[1]; //SPP : 0 , pp2 : 1
	    
	    ppks[MainDriver.NwcS] = pp3; 
	    stks[MainDriver.NwcS] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2));
	    
	    volks = volks + 20.5;    //10.5 bbls more for plotting purpose !	    
	    fCP = pp3;
	    ppks[MainDriver.NwcS-1] = pp3; 
	    stks[MainDriver.NwcS-1] = (int)(volks*(MainDriver.initspMin1+MainDriver.initspMin2) / (MainDriver.Qcapacity1*MainDriver.initspMin1+MainDriver.Qcapacity2*MainDriver.initspMin2));
	//
	//-------------------------------- specify the graph//s properties !
	//   There are some diffeMainDriver.RenCe in plotting between v.6 and v.3
	//
	    
		Sgg0.ColumnCount = 4;     //Two data set    5 is added on 6/26/97
		Sgg0.RowCount = npData;       //total number of data
		Sgg0.ColumnLabelCount = 2;
		//Sgg0.Plot.Axis(VtChAxisIdY).CategoryScale.Auto = False
		//Sgg0.Plot.Axis(VtChAxisIdY).ValueScale.Minimum = (int)((fCP - 199.5) / 10) * 10
	    //Sgg0.Plot.Axis(VtChAxisIdY).ValueScale.Maximum = Int((iCP + 300.5) / 10) * 10
	//		
		Sgg0.ColumnLabel = "Theoretical"; //이론적인 Kill sheet가 그려지는 부분
	    int icount3 = 0;
	    for(int i = (MainDriver.NwcE + 3 + 2*MainDriver.NumQcapeq); i> (MainDriver.NwcS - 2);i--){
	    	Sgg0.Row = icount3;    //i - nwcs + 2
	        Sgg0.Column = 0;
	        Sgg0.SgxData[Sgg0.Row] = stks[i];	        
	        Sgg0.Column = 1;
	        Sgg0.SgyData[Sgg0.Row] = ppks[i] - 14.7;
	        icount3 = icount3 + 1;	        
	    }
	//
	//--- assign additional data to prevent assigning zero
	    if(MainDriver.iDone > nData){
	    	for(int i = icount3; i<MainDriver.iDone; i++){
	    		Sgg0.Row = i;
	    		Sgg0.Column = 0;
	    		Sgg0.SgxData[Sgg0.Row] = stks[MainDriver.NwcS - 1];
	    		Sgg0.Column = 1;
	    		Sgg0.SgyData[Sgg0.Row] = ppks[MainDriver.NwcS - 1] - 14.7;  //ppks(nwcs - 1) - 14.7
	    		}
	    	}
	//
	    Sgg0.Column = 2;
	    Sgg0.ColumnLabel = "Actual"; //실제적인 Kill sheet가 그려지는 부분
	    Sgg0.sg2Use=1;
	   for(int i = 0; i<MainDriver.iDone; i++){
	    	Sgg0.Row = i;
	    	Sgg0.Column = 2;
	    	Sgg0.Sgx2Data[Sgg0.Row] = MainDriver.vKmudSt[i];
	    	
	    	Sgg0.Column = 3;
	    	Sgg0.Sgy2Data[Sgg0.Row] = MainDriver.Pcontrol[i] - 14.7;
	    	if(Sgg0.Sgx2Data[Sgg0.Row]==0 && Sgg0.Sgy2Data[Sgg0.Row]==0 && i<MainDriver.iDone-1){ // 초기 시작시 0에서 시작하는 것을 방지
	    		Sgg0.Sgx2Data[Sgg0.Row]=MainDriver.vKmudSt[i+1];
	    		Sgg0.Sgy2Data[Sgg0.Row] = MainDriver.Pcontrol[i+1] - 14.7;
	    		}
	    	}
	//--- assign additional data to prevent assigning zero
	    if(MainDriver.iDone < nData){
	    	for(int i = MainDriver.iDone; i<nData; i++){
	    		Sgg0.Row = i;
	    		Sgg0.Column = 2;
	    		Sgg0.Sgx2Data[Sgg0.Row] = MainDriver.vKmudSt[MainDriver.iDone-1];
	    		Sgg0.Column = 3;
	    		Sgg0.Sgy2Data[Sgg0.Row] = MainDriver.Pcontrol[MainDriver.iDone-1] - 14.7;
	    		if(i>0){
		    		if(Sgg0.Sgx2Data[i]<Sgg0.Sgx2Data[i-1]){
		    			dummy=1;
		    		}
		    	}
	    	}
	    }
	    
	    MainDriver.gMudCirc = gmc2;
	    MainDriver.Cmud=cmud2; //140522 ajw
	    
	    Sgg0.calcGraphIntv(Sgg0.sgYMaxLim, Sgg0.sgYMinLim);
	    Sgg0.repaint();
	}
	
	
	void PlotG4(){
		double totTsec = 0, totTmin=0;
		int nData =0;
		plotTime = 0;
		if(MainDriver.Np >= 1){
	//
	//----- plot the pump pressure on the top of kill sheet !
		   if (volKmud <= MainDriver.VOLinn + 30.5 && MainDriver.iDone >= 2) PlotActualKillSheet();
		   if (volKmud==0) PlotKillSheet();
	//
	// 7/25/01
		   for(int i = 0; i<4; i++){			   
			   Sgg[i].RowCount = MainDriver.Np+1;
			   Sgg[i].ColumnCount = 2;
			   }//
		   
		   for(int i = 0; i<MainDriver.Np+1; i++){
			   totTmin = MainDriver.TTsec[i] / 60;
			   Sgg[0].Row = i;    //plot pump pressure
			   Sgg[0].Column = 1; //=> it means x-data 
			   Sgg[0].SgxData[Sgg[0].Row] = totTmin;
			   Sgg[0].Column = 2; //=> it means y-data
			   Sgg[0].SgyData[Sgg[0].Row] = MainDriver.Ppump[i] - 14.7;
					//
			   Sgg[1].Row = i;    //plot choke pressure
			   Sgg[1].Column = 1; //=> it means x-data 
			   Sgg[1].SgxData[Sgg[1].Row] = totTmin;
			   Sgg[1].Column = 2; //=> it means y-data
			   Sgg[1].SgyData[Sgg[1].Row] = MainDriver.Pchk[i] - 14.7;
					//
			   Sgg[2].Row = i;    //plot casing pressure
			   Sgg[2].Column = 1; //=> it means x-data 
			   Sgg[2].SgxData[Sgg[2].Row] = totTmin;
			   Sgg[2].Column = 2; //=> it means y-data
			   Sgg[2].SgyData[Sgg[2].Row] = MainDriver.Pcsg[i] - 14.7;
					//
			   Sgg[3].Row = i;    //plot kick volume
			   Sgg[3].Column = 1; //=> it means x-data 
			   Sgg[3].SgxData[Sgg[3].Row] = totTmin;
			   Sgg[3].Column = 2; //=> it means y-data
			   Sgg[3].SgyData[Sgg[3].Row] = MainDriver.Vpit[i];
		   }
		   for(int i = 0; i<4; i++){			   
			   Sgg[i].calcGraphIntv(Sgg[i].sgYMaxLim, Sgg[i].sgYMinLim);
			   Sgg[i].repaint();
			   }//
		   if(MainDriver.iCaseSelection==1) MainDriver.iCaseSelection=2;
	    //Sgg[0].Plot.UniformAxis = False
	   // Sgg[1].Plot.UniformAxis = False
	   // Sgg[2].Plot.UniformAxis = False
	   // Sgg[3].Plot.UniformAxis = False
	   }	   	   
	}
	
	void DrawMultiKick(){
		//Draw multiple kicks !
		//Modified by TY
		//For manual & auto modes
		double VOLkick = 0, xbottom=0, xxx=0;
		//For i = N2phase To 3 Step -1		
		m3.kickNumber=0;
		for(int i = MainDriver.N2phase-1; i>0; i--){
			if(MainDriver.Hgnd[i] > 0.001 || MainDriver.PVTZ_Gas[i] > 0.001) VOLkick = VOLkick + MainDriver.volG[i] + MainDriver.volL[i] + MainDriver.Vsol[i];
			else{
				m3.drawMultikickIndex=1;
				xbottom = MainDriver.Xnd[i];
				//Call DrawKick(VOLkick, xbottom);
				m3.multixDepthWP[m3.kickNumber] = xbottom;
				m3.multiQtotMixWP[m3.kickNumber] = VOLkick;	
				m3.kickNumber++;
				//m3.drawKickIndex=1;
				VOLkick = 0;
				}
			}
		//xxx = xbcell
		
		//If iShift = 0 Then
		xxx = MainDriver.Xnd[0];
		//Else
		//xxx = Xnd(2)
		//End If
        //Call DrawKick(VOLkick, xxx)
		m3.multixDepthWP[m3.kickNumber] = xxx;
		m3.multiQtotMixWP[m3.kickNumber] = VOLkick;		
		m3.kickNumber++;
		if(m3.drawMultikickIndex==0 && iShift==0){
			m3.xDepthWP = xxx;
			m3.QtotMixWP = VOLkick;	
		}
		//m3.drawKickIndex=1;
		
		if(iShift!=0){
			//--- draw the new kick at the bottom only
			//Multi kick이 처음에 발생할 때만 그려주는 코드..
			//    If (hgnd(2) > .001) Then    //This is true if there is a kick ie ishift >=1
			m3.drawMultikickIndex=1;
			VOLkick = MainDriver.volG[1] + MainDriver.volL[1];
			xbottom = MainDriver.TMD[MainDriver.NwcS-1];
			//Call DrawKick(VOLkick, xbottom);
			m3.multixDepthWP[m3.kickNumber] = xbottom;
			m3.multiQtotMixWP[m3.kickNumber] = VOLkick;	
			m3.kickNumber++;
			//m3.drawKickIndex=1;
			//    End If
			}
		}
	 
	void ShowResult(){
		double Tsurface=0, visSurface=0, denSurface=0, zSurface=0, gasFraction=0;
		double totHr =0, totMin=0, totSec=0, volMudLine=0, volkilleff=0;
		//
		MainDriver.volMix =  QtotMix;
		//----------------------------- calculate choke open percentage by Area
		double hgX = 0;
		
		plotTime = (int) (plotTime + MainDriver.gDelT);
	    //if ((plotTime + 1) / 60 > grpint || mudCase >= 11) PlotG4(); 편의상
		PlotG4();
	//................................... Show the Wellbore and Gauge
	    if (MainDriver.iWshow == 1){	       
	       m3.drawOmudIndex=1;
	       volkilleff = volKmud + volkkmix;
	       m3.volkillmud = volkilleff;
	       m3.drawKmudIndex=1;			       
	       //if (MainDriver.iHresv == 1 || MainDriver.igERD == 1) Call DrawMultiKick
	       //else Call DrawKick(QtotMix, Xb);
	       m3.QtotMixWP=QtotMix;
	       m3.xDepthWP=MainDriver.Xb;
	       m3.drawKickIndex=1;
	       DrawMultiKick();
	       m3.drawWellboreIndex=1;
	       m3.pp.repaint();
	    }
	    
	    if(PVgain < 0.00001){
	    	px = psia;
	    	gasDen = 0;
	    	MainDriver.Xb = 0;
	    	xVert = 0;
	    }
	    
		if(MainDriver.Xb > 1.5 && X < 0.1) hgX = MainDriver.Hgnd[MainDriver.N2phase-1];
		if(MainDriver.iCHKcontrol==1){
			CHKpcent = utilityModule.calcCHKOpen(MainDriver.QtMix, hgX, MainDriver.oMud, gasDen, Pchoke);
			HScrollCHK.setValue((int)((CHKpcent + 0.05001)*10));			//140727 AJW
			txtCHKopen.setText((new DecimalFormat("#0.0")).format((int)((CHKpcent + 0.05001)*10)/10.0));
			//txtCHKopen.setText(Integer.toString((int)(CHKpcent + 0.5001)));
			//HScrollCHK.setValue((int)(CHKpcent + 0.5001));
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
		if (X < 0) X = 0;
		if (MainDriver.Xb < 0) MainDriver.Xb = 0;
		
		if (MainDriver.Xb<0.5) PVgain=0;
		if (xVert < 0) xVert = 0;
		if (xbVert < 0) xbVert = 0;
		if (MainDriver.Xb < 0.01) MainDriver.QtMix = MainDriver.Qkill;
		
		xbVert = utilityModule.getVD(MainDriver.Xb); 
		
		MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;	
		if(MainDriver.gTcum>0){
			Stroke1 = Stroke1+MainDriver.gDelT/60*MainDriver.spMin1;
			Stroke2 = Stroke2+MainDriver.gDelT/60*MainDriver.spMin2;
			TotalStroke = Stroke1 + Stroke2;
			}
		
		txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));
		txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));
		
		if(volKmud<=0){
			KillMudStroke1=0;
			KillMudStroke2=0;
			TotalKillMudStroke=0;
		}
		else{
			KillMudStroke1=KillMudStroke1+MainDriver.gDelT/60*MainDriver.spMin1;
			KillMudStroke2=KillMudStroke2+MainDriver.gDelT/60*MainDriver.spMin2;
			TotalKillMudStroke=KillMudStroke1+KillMudStroke2;
		}
		//
		txtN2phase.setText(Integer.toString(MainDriver.N2phase));
		txtVDtop.setText((new DecimalFormat("###,##0")).format(xVert));
		txtVDbottom.setText((new DecimalFormat("###,##0")).format(xbVert));
		txtMDkickTop.setText((new DecimalFormat("###,##0")).format(X));
		txtMDkickBottom.setText((new DecimalFormat("###,##0")).format(MainDriver.Xb));
		txtPitGain.setText((new DecimalFormat("##,##0.0")).format(PVgain + mlPVgain));
		txtVolume.setText((new DecimalFormat("##,##0.0")).format(MainDriver.volPump));
		txtStroke.setText((new DecimalFormat("###,##0")).format(TotalStroke));
		//reset_Pump_Stroke = (int)(MainDriver.volPump/MainDriver.Qcapacity)-Now_Pump_Stroke;
		//Killsheet_Pump_Stroke = (int)(MainDriver.volPump/MainDriver.Qcapacity)-Start_Pump_Stroke;
		
		txtKickP.setText((new DecimalFormat("###,##0")).format(px - psia));
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
		if (X < 0.001 && MainDriver.Xb > 0.5){          //Addition of Mud/Gas flow rate
			Tsurface = MainDriver.Tsurf + 460;
			GasPropEX = propertyModule.GasProp(px, Tsurface); //visSurface, denSurface, zSurface)
			visSurface = GasPropEX[0];
			denSurface = GasPropEX[1];
			zSurface = GasPropEX[2];
			gasFraction = MainDriver.Hgnd[MainDriver.N2phase-1];
			mudFlowOut = MainDriver.QtMix * (1 - gasFraction);
			gasFlowOut = MainDriver.QtMix * gasFraction;         //gas flow in gpm at MainDriver.Pnd[MainDriver.N2phase) pressure
			gasFlowOut = gasFlowOut * px / (14.7 * zSurface) * 60 * 24 / 7.48 * 0.001;   //Mscf/Day
			}
		else mudFlowOut = MainDriver.QtMix;
		txtReturnRate.setText((new DecimalFormat("##,##0")).format(mudFlowOut));
		txtGasRate.setText((new DecimalFormat("#,###,##0.0#")).format(gasFlowOut));
		//
		if (X > MainDriver.Dwater){   //addition of mud line pressure, 7/17/02
			if(MainDriver.imud==1 && MainDriver.iOilComp==1){
				mudLineP = utilityModule.getDP_kill_auto(MainDriver.QtMix, MainDriver.Xb, MainDriver.Dwater, px, MainDriver.oMud);
			}
			else{
				utilityModule.getDP(MainDriver.QtMix, MainDriver.Dwater, MainDriver.oMud);
				mudLineP = Pchoke + MainDriver.gMudOld * MainDriver.Dwater + MainDriver.Pcon * MainDriver.DPtop;
			}			
			}
		else if (X < MainDriver.Dwater && MainDriver.Xb > MainDriver.Dwater) mudLineP = propertyModule.DitplDes(MainDriver.Dwater);
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
			if(MainDriver.imud==1 && MainDriver.iOilComp==1){
				Pbtm = utilityModule.pxBottom_densChange(volKmud, MainDriver.Dwater);
			}
			else{
				Pbtm = utilityModule.pxBottom(volMudLine, MainDriver.Dwater);				
			}			
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
			MudGpnl.repaint();    //return rate of mixture
			tmpintCHK = Pchoke - psia;
			if (tmpintCHK > 3960) tmpintCHK = 3960;
			txtPchoke.setText((new DecimalFormat("###,##0")).format(tmpintCHK));
			CHKGpnl.repaint();    //surface choke pressure
			tmpintSDP = SPP - psia; 
			if (tmpintSDP > 3960) tmpintSDP = 3960;    //standpipe pressure
			txtPsp.setText((new DecimalFormat("###,##0")).format(tmpintSDP));
			SDPGpnl.repaint();
			tmpintPumpP = pumpP - psia;
			if (tmpintPumpP > 3960) tmpintPumpP = 3960;
			txtPumpGP.setText((new DecimalFormat("###,##0")).format(tmpintPumpP));
			PumpPGpnl.repaint();    //surface choke pressure
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
			if(propertyModule.FractGrad(Pcasing, Pbeff)<0){;     //check possible formation fracture
				if(TimerCirculationOn == 1){
					TimerCirculation.cancel();
					TimerCirculationOn = 0;
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
	
	void AssignVal(){
	//.............  Assign the calculated values
		if(Math.abs(MainDriver.TTsec[MainDriver.Np] - MainDriver.gTcum) >= 0.5){
			timeNp = 0;
		    MainDriver.Np = MainDriver.Np + 1;
		    if(xVert < 0) xVert = 0;
		    if(xbVert < 0) xbVert = 0;
		//
		    MainDriver.xTop[MainDriver.Np] = xVert;
		    MainDriver.xBot[MainDriver.Np] = xbVert; 
		    MainDriver.pxTop[MainDriver.Np] = px;
		    MainDriver.rhoK[MainDriver.Np] = gasDen;
		    MainDriver.Psp[MainDriver.Np] = SPP;
		    MainDriver.Pcsg[MainDriver.Np] = Pcasing;
		    MainDriver.Pb2p[MainDriver.Np] = Pbeff;
		    MainDriver.TTsec[MainDriver.Np] = MainDriver.gTcum;
		    MainDriver.Pchk[MainDriver.Np] = Pchoke;
		    MainDriver.Vpit[MainDriver.Np] = PVgain;
		    MainDriver.QmcfDay[MainDriver.Np] = QgDay;
		    MainDriver.Ppump[MainDriver.Np] = pumpP;    
		    MainDriver.CHKopen[MainDriver.Np] = CHKpcent;
		    MainDriver.VOLcir[MainDriver.Np] = MainDriver.volPump; 
		    MainDriver.StrokePump1[MainDriver.Np] = (int)Stroke1;
		    MainDriver.StrokePump2[MainDriver.Np] = (int)Stroke2;
		    MainDriver.Stroke[MainDriver.Np] = (int)TotalStroke;
		//
		//............. adjust wellbore pressures in surface choke pressure is negative, 7/11/02
		    MainDriver.gasflow[MainDriver.Np] = gasFlowOut;    //addition of variables, 7/17/02
		    MainDriver.mudflow[MainDriver.Np] = mudFlowOut;
		    MainDriver.PmLine[MainDriver.Np] = mudLineP;
		   	    
		    //--- assign user's choke pressure result as pump pressure observation !
		    
		    if(volKmud <= MainDriver.VOLinn + 20.5 && MainDriver.iDone >= 1){		       
		       if(MainDriver.iDone < 181){
		    	   MainDriver.vKmudSt[MainDriver.iDone] = (int)TotalKillMudStroke;
		    	   if(MainDriver.vKmudSt[MainDriver.iDone]>1900){
			        	dummy=0;
			        }
		    	   MainDriver.Pcontrol[MainDriver.iDone] = pumpP;
		    	   MainDriver.iDone = MainDriver.iDone + 1;
		       }
		    }
		    else{
		    	dummy=1;
		    }
		    
		    MainDriver.PointRefPrior = MainDriver.PointRef; //140728
		    
		    if(Pbeff>MainDriver.Pform){ //140728
		    	MainDriver.PointRef = 1;
		    	if(MainDriver.iKickDetect==1){
		    		MainDriver.iKickDetect=0;
		    		MainDriver.kickTimeStart = 0;
		    		MainDriver.kickTimeFinish = 0;
		    		}
		    	}
		    else if(Math.abs((Pbeff-MainDriver.Pform)/MainDriver.Pform)<=0.0001){
		    	MainDriver.PointRef = 0;
		    	if(MainDriver.iKickDetect==1){
		    		MainDriver.iKickDetect=0;
		    		MainDriver.kickTimeStart = 0;
		    		MainDriver.kickTimeFinish = 0;
		    		}
		    }
		    else{
		    	MainDriver.PointRef = -1;
		    	if(MainDriver.iKickDetect==1){
		    		MainDriver.kickTimeFinish = MainDriver.gTcum; 
		    		if(MainDriver.kickTimeFinish >= MainDriver.kickTimeStart+180){
		    			MainDriver.MinusPoint = MainDriver.MinusPoint + 1;
		    			MainDriver.kickTimeStart = MainDriver.kickTimeFinish + 10000; // 140801 한번 점수가 추가된 후에 추가적으로 점수가 추가되는 것을 방지.
		    			}
		    		}
		    	else{
		    		MainDriver.iKickDetect = 1;
		    		MainDriver.kickTimeStart = MainDriver.gTcum;
		    		}		    	
		    }
		    
		    //--- check the possible out of array
		    if(MainDriver.Np >= MainDriver.Npt - 2){
		    	String msg = "You spent too much time.  Arrays are out of Range !";
		    	JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
		    	menuClose();
		    }		    
		}
	}		
	
	void menuClose(){
		 if(TimerCirculationOn == 1){
			   TimerCirculation.cancel();
			   TimerCirculationOn = 0;
		   }
		 MainDriver.ManualOn = 0;
		 MainDriver.MainMenuVisible=1;
		 m3.dispose();
		 m3.setVisible(false);
		 this.dispose();
		 this.setVisible(false);
	}
	
	void PbCalc2(){
		//  Calculates B.H.P. when annulus pressure is given. - new AWC version
		//  No Slip and Top properties as an average properties for Simplicity !
		double avgden=0, ql=0, Qg=0;
		double volTot =0, volkx=0, voltmp=0, HgCal=0;
		double zx=0, hxt=0, dpmud=0, dpfri=0, DPdiff=0;
	//.................................... assign the top cell conditions
	    double xNull = 0, xxx=0, wkbtmp=0;	    
	    double pxcell = px; 
	    double xcell = X, xbcell=0, xbvdcell=0;
	    double xvdcell=utilityModule.getVD(xcell);	    
	    double tx = utilityModule.temperature(xvdcell), tx2=0;
	    double surfTen = propertyModule.surfT(MainDriver.pxTop[MainDriver.Np - 1], tx);
	    int iXb = MainDriver.N2phase-1;
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
	        if(MainDriver.imud == 0) volkx = MainDriver.pvtZb[i] * tx * zx / pxcell; //WBM case
	        else volkx = MainDriver.PVTZ_free[i] * tx * zx * MainDriver.R / pxcell / 5.615; //OBM case
	        //
	        voltmp = MainDriver.volL[i];
	        volTot = volkx + voltmp;
	        //Added by TY
	        if(volkx > 0.001) iXb = i - 1;
	    	//
	    	//.... if there are kicks from ML to MP(iMLflux = 1), ignore the restriction on max Hg
	    	//     at the mixture front. this is also good for bottom kick loc. 2003/8/1
	    	       if(volTot < 0.00001) volTot = 0.00001;
	    	       HgCal = volkx / volTot;
	    	       if (HgCal >= 0.0001 && iMLflux != 1){// Then GoTo 6789
	    	    	   //................. set the constant gas fraction at the top ie. less than 0.45
	    	    	   if (i > MainDriver.N2phase - MainDriver.nHgCon-1 && MainDriver.iHgMax == 1 && HgCal > 0.45){
	    		          HgCal = MainDriver.HgndOld[i];
	    		          if(HgCal < 0.00002) HgCal = 0.45;   //temperarily set 0.45 as maximum
	    		          //voltmp = volkx * (1 - HgCal) / HgCal;
	    		          //volTot = volkx + voltmp;
	    	    	   }
	    		//...................................... set the maximum gas fraction
	    		       if (HgCal > MainDriver.HgMax && MainDriver.iHgMax == 1){
	    		    	   HgCal = MainDriver.HgMax;
	    		    	   //voltmp = volkx * (1 - MainDriver.HgMax) / MainDriver.HgMax;
	    		    	   //volTot = volkx + voltmp;
	    		       }
	    	       }
	    	//6789 dummy = 0   //simple continue -------------------------------------------
	    	       hxt =  utilityModule.getTopH(volTot, xcell);
	    	       xbcell = xcell + hxt; // xcell : top location of the cell, xbcell : bottom location of the cell
	    	       xbvdcell=utilityModule.getVD(xbcell);
	    	       
	    	       //'Modified by Ty
	    	       //avgden = gasDen * HgCal + MainDriver.oMud * (1 - HgCal);
	    	       avgden = MainDriver.oMud * (1 - HgCal) + gasDen * HgCal;
	    	       dpmud = 0.052 * avgden * (xbvdcell - xvdcell);
	    	       ql = MainDriver.QtMix * voltmp / volTot;  //Q liquid
	    	       Qg = MainDriver.QtMix * volkx / volTot; // Q gas
	    	       utilityModule.get2pDP(xbcell, ql, Qg, HgCal, MainDriver.oMud+MainDriver.Dendiff[i], gasDen, gasVis);
	    	       dpfri = MainDriver.DPtop; //dp friction
	    	       utilityModule.get2pDP(xcell, ql, Qg, HgCal, MainDriver.oMud+MainDriver.Dendiff[i], gasDen, gasVis);
	    	       dpfri = dpfri - MainDriver.DPtop;   // DP-fric in mixed zone
	    	//............................................. assign calculated value
	    	       MainDriver.Pnd[i] =  pxcell;
	    	       MainDriver.Xnd[i] =  xcell;
	    	       MainDriver.volG[i] =  volkx;
	    	       volLqd[i] = voltmp; 
	    	       MainDriver.Hgnd[i] =  HgCal;
	    	       pxcell = pxcell + dpmud + dpfri * MainDriver.Pcon;
	    	       xcell = xbcell;	    
	    	     //Modified by Ty
	    	       PVgain = PVgain + volkx; 
	    	       QtotMix = QtotMix + volTot;
	       }
	    MainDriver.Xnd[0] =  xbcell;
	    MainDriver.Pnd[0] =  pxcell;
	    //xxx = MainDriver.TMD[MainDriver.NwcS-1];
	    //MainDriver.Xb =  Math.min(xbcell, xxx);
	    MainDriver.Xb = MainDriver.Xnd[iXb];
	    //Modified by TY
	    //Pbtm = utilityModule.pxBottom(MainDriver.volPump, MainDriver.Xb);
	    
	    temp_oMud = MainDriver.oMud;
	    if(MainDriver.imud==1 && MainDriver.iOilComp==1){
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = MainDriver.oMud_save;
	    	Pbtm = utilityModule.pxBottom_densChange(volKmud, xbcell);
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = temp_oMud;
	    }
	    else{
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = MainDriver.oMud_save;
	    	Pbtm = utilityModule.pxBottom(volKmud, xbcell);
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = temp_oMud;
	    }	    
	    DPdiff = MainDriver.Pb - Pbtm; 
	    PbTry = pxcell + DPdiff;
	    if (Math.abs(MainDriver.Pb - PbTry) < 50.5) MainDriver.iHgMax = 1;
	//
	}
	
	void PbCalc(){
	//  Calculates B.H.P. when annulus pressure is given. - new AWC version
	//  No Slip and Top properties as an average properties for Simplicity !
	// 'Added by TY for automatic chk control
		double avgden=0, ql=0, Qg=0;
		double volTot =0, volkx=0, voltmp=0, HgCal=0;
		double zx=0, hxt=0, dpmud=0, dpfri=0, DPdiff=0;
	//.................................... assign the top cell conditions
	    double xNull = 0, xxx=0, wkbtmp=0;	    
	    double pxcell = px; 
	    double xcell = X, xbcell=0, xbvdcell=0;
	    double xvdcell=utilityModule.getVD(xcell);	    
	    double tx = utilityModule.temperature(xvdcell), tx2=0;
	    double surfTen = propertyModule.surfT(MainDriver.pxTop[MainDriver.Np - 1], tx);
	    double oilkickDensity_mixture=0;
		double mwTot = 0;
	    int iXb = MainDriver.N2phase-1;
	//
	    PVgain = 0;
	    QtotMix = 0;
	    totFri=0;
	    Pcasing = pxcell;
	    for(int i = MainDriver.N2phase-1; i>0; i--){
	    	xvdcell=utilityModule.getVD(xcell);
	    	tx = utilityModule.temperature(xvdcell);
	        GasPropEX = propertyModule.GasProp(pxcell, tx);//, gasVis, gasDen, zx)
	        gasVis = GasPropEX[0];
	        gasDen = GasPropEX[1];
	        zx = GasPropEX[2];
	        if (MainDriver.volL[i] == 0) MainDriver.volL[i]=MainDriver.delta_T[i]*MainDriver.volL[MainDriver.N2phase-1]/MainDriver.delta_T[MainDriver.N2phase-1];
	//.................................. calculate the pressure at the top
	        if(MainDriver.imud == 1){   	
	        	MainDriver.R = 10.73; //universal gas constant
	        	tx2 = tx - 460; //tx: rankine / tx2: fahrenheit
	        	//Call calcRs(pxcell, tx, 0.5537, Rs, Rsk, RsM, Rsn, ibaseoil) //gas solubility calc. by O//bryan//s
	        	MainDriver.Rs = utilityModule.calcRs2(pxcell, tx2); //gas solubility calc. by PVTi
	        	if(MainDriver.Rs < 0) MainDriver.Rs = 0;
	        	if(MainDriver.volGold[i] == 0) MainDriver.volGold[i] = 0.001;
	        	wkbtmp = 42 * gasDen * MainDriver.volGold[i];
	        	MainDriver.Rs = MainDriver.Rs * MainDriver.foil; //fractional solubility
	        	//
	        	
	        	MainDriver.PVTZ_free[i] = MainDriver.volL[i] * (MainDriver.gor[i] - MainDriver.Rs) * 0.0417 / 16.04; //지상 조건의 부피 계산 후 freegas mole
	        	if(MainDriver.PVTZ_free[i] < 0) MainDriver.PVTZ_free[i] = 0;
	        	MainDriver.PVTZ_sol[i] = MainDriver.PVTZ_Gas[i] - MainDriver.PVTZ_free[i]; //solution gas mole
	        	
	        	if(MainDriver.PVTZ_sol[i] < 0){
	        		MainDriver.PVTZ_sol[i] = 0;
	        	}
	        	
	        	//.................................. calculate the pressure at the top
	        	volkx = MainDriver.PVTZ_free[i] * tx * zx * MainDriver.R / pxcell; //pit gain by free gas volume(ft^3)
	        	volkx = volkx / 5.615; //pit gain by free gas volume(bbls)
	        	//---------------------------------------------------------------------------------------------------Applying PREOS
	        	MainDriver.OBMmole[i] = (MainDriver.volL[i] * MainDriver.foil) * MainDriver.OBMdensity * 42 / MainDriver.OBMwt;
	        	//
	        	MainDriver.OBMFrac[i] = (MainDriver.OBMmole[i] / ((MainDriver.PVTZ_sol[i]) + MainDriver.OBMmole[i])) / 100;
	        	MainDriver.GasFrac[i] = (0.01 - MainDriver.OBMFrac[i]) * 100;
	        	//
	        	MainDriver.OBMFr = MainDriver.OBMFrac[i];
	        	MainDriver.GasFr = MainDriver.GasFrac[i];
	        	//
	        	MainDriver.mole_solgas = MainDriver.PVTZ_sol[i];
	        	MainDriver.mole_OBM = MainDriver.OBMmole[i];
	        	//
	        	if(MainDriver.mud_calc==0){
	        		MainDriver.V_cont = utilityModule.PREOS(pxcell, tx, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);
	        		MainDriver.V_cont_ref = utilityModule.PREOS(pxcell, tx, 0, 0.01, 0, MainDriver.mole_OBM);
	        		//
	        		MainDriver.Vsol[i] = (MainDriver.V_cont - MainDriver.V_cont_ref);
	        		MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.volL[i] * MainDriver.foil / MainDriver.V_cont;
	        		//
	        		}
	        	else{        		
	        		utilityModule.OBM_Fraction(MainDriver.GasFr, MainDriver.OBMFr); //solution gas mole, obm gas mole
	    			oilkickDensity_mixture  = utilityModule.calcSKdensity(tx, pxcell-14.7); //ppg
	    			mwTot = MainDriver.mole_solgas * 16.04 + MainDriver.OBMwt * MainDriver.mole_OBM; //lb
		    		MainDriver.Vsol[i] = mwTot / oilkickDensity_mixture / 42 - MainDriver.volL[i] * MainDriver.foil;	
	        	}
	        	MainDriver.Den_tmp = (MainDriver.oMud * MainDriver.volL[i] * 42 + MainDriver.PVTZ_sol[i] * 16.04) / (MainDriver.volL[i] + MainDriver.Vsol[i]) / 42;
	        	MainDriver.Dendiff[i] = MainDriver.Den_tmp - MainDriver.oMud; //density change
	        	//
	        	if(MainDriver.Dendiff[i] > 0) MainDriver.Dendiff[i] = 0;
	        	}
	        else volkx = MainDriver.pvtZb[i] * tx * zx / pxcell; //WBM CASE
	              
	        voltmp = MainDriver.volL[i] + MainDriver.Vsol[i];
	        volTot = volkx + voltmp;
	        
	        //Added by TY
	        if(volkx > 0.001) iXb = i - 1;
	//
	//.... if there are kicks from ML to MP(iMLflux = 1), ignore the restriction on max Hg
	//     at the mixture front. this is also good for bottom kick loc. 2003/8/1
	       if(volTot < 0.00001) volTot = 0.00001;
	       HgCal = volkx / volTot;
	       if (HgCal >= 0.0001 && iMLflux != 1){// Then GoTo 6789
	    	   //................. set the constant gas fraction at the top ie. less than 0.45
	    	   if (i > MainDriver.N2phase - MainDriver.nHgCon-1 && MainDriver.iHgMax == 1 && HgCal > 0.45){
		          HgCal = MainDriver.HgndOld[i];
		          if(HgCal < 0.00002) HgCal = 0.45;   //temperarily set 0.45 as maximum
		          voltmp = volkx * (1 - HgCal) / HgCal;
		          volTot = volkx + voltmp;
	    	   }
		//...................................... set the maximum gas fraction
		       if (HgCal > MainDriver.HgMax && MainDriver.iHgMax == 1){
		    	   HgCal = MainDriver.HgMax;
		    	   voltmp = volkx * (1 - MainDriver.HgMax) / MainDriver.HgMax;
		    	   volTot = volkx + voltmp;
		       }
	       }
	//6789 dummy = 0   //simple continue -------------------------------------------
	       hxt =  utilityModule.getTopH(volTot, xcell);
	       xbcell = xcell + hxt; // xcell : top location of the cell, xbcell : bottom location of the cell
	       xbvdcell=utilityModule.getVD(xbcell);
	       
	       //'Modified by Ty
	       //avgden = gasDen * HgCal + MainDriver.oMud * (1 - HgCal);
	       avgden = (MainDriver.oMud + MainDriver.Dendiff[i]) * (1 - HgCal) + gasDen * HgCal;
	       dpmud = 0.052 * avgden * (xbvdcell - xvdcell);
	       ql = MainDriver.QtMix * voltmp / volTot;  //Q liquid
	       Qg = MainDriver.QtMix * volkx / volTot; // Q gas
	       utilityModule.get2pDP(xbcell, ql, Qg, HgCal, MainDriver.oMud+MainDriver.Dendiff[i], gasDen, gasVis);
	       dpfri = MainDriver.DPtop; //dp friction
	       utilityModule.get2pDP(xcell, ql, Qg, HgCal, MainDriver.oMud+MainDriver.Dendiff[i], gasDen, gasVis);
	       dpfri = dpfri - MainDriver.DPtop;   // DP-fric in mixed zone
	       totFri = totFri + dpfri;
	//............................................. assign calculated value
	       MainDriver.Pnd[i] =  pxcell;
	       MainDriver.Xnd[i] =  xcell;
	       MainDriver.volG[i] =  volkx;
	       volLqd[i] = voltmp; 
	       MainDriver.Hgnd[i] =  HgCal;
	       pxcell = pxcell + dpmud + dpfri * MainDriver.Pcon;
	       if(MainDriver.DepthCasing > X && MainDriver.DepthCasing>=xcell){
	    	   if(MainDriver.DepthCasing<xbcell) Pcasing = Pcasing + 0.052 * avgden * (MainDriver.DepthCasing - xvdcell) + dpfri * MainDriver.Pcon * (MainDriver.DepthCasing - xvdcell) / (xbvdcell - xvdcell);
	    	   else Pcasing = Pcasing + dpmud + dpfri * MainDriver.Pcon;
	       }
	       xcell = xbcell;
	       
	       //PVgain = PVgain + volkx; 
	       //Modified by Ty
	       PVgain = PVgain + volkx + MainDriver.Vsol[i];
	       QtotMix = QtotMix + volTot;
	       }
	    MainDriver.Xnd[0] =  xbcell;
	    MainDriver.Pnd[0] =  pxcell;
	    //xxx = MainDriver.TMD[MainDriver.NwcS-1];
	    //MainDriver.Xb =  Math.min(xbcell, xxx);
	    //MainDriver.Xb = MainDriver.Xnd[iXb];
	    MainDriver.Xb = Math.min(MainDriver.Xnd[0], MainDriver.TMD[MainDriver.NwcS-1]);
	    //Modified by TY
	    //Pbtm = utilityModule.pxBottom(MainDriver.volPump, MainDriver.Xb);
	    
	    temp_oMud = MainDriver.oMud;
	    if(MainDriver.imud==1 && MainDriver.iOilComp==1){
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = MainDriver.oMud_save;
	    	Pbtm = utilityModule.pxBottom_densChange(volKmud, xbcell);
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = temp_oMud;
	    }
	    else{
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = MainDriver.oMud_save;
	    	Pbtm = utilityModule.pxBottom(volKmud, xbcell);
	    	if(MainDriver.iProblem[3]==1) MainDriver.oMud = temp_oMud;
	    }	    
	    DPdiff = MainDriver.Pb - Pbtm; 
	    PbTry = pxcell + DPdiff;
	    if (Math.abs(MainDriver.Pb - PbTry) < 50.5) MainDriver.iHgMax = 1;
	//
	}
	
	void Pbcal2(){
		int iXb=0;
		//  Calculates B.H.P. when annulus pressure is given. - new AWC version
		//  No Slip and Top properties as an average properties for Simplicity !
		//.................................... assign the top cell conditions
		//Modified by TY
		int xNull = 0;
		double[] gasPropEX = new double[3];
		double pxcell = px, xcell = X, xbcell=0, xvdcell = xVert, xbvdcell=0;
		MainDriver.tx = utilityModule.temperature(xvdcell);
	    double surfTen = propertyModule.surfT(px, MainDriver.tx);
	    double zx=0, avgden=0, dpmud=0;
	    double volTot =0, voltmp=0, HgCal=0, hxt=0;
	    double tx2=0, wkbtmp=0, rhoL=0;
	    double dpfri=0, ql=0, Qg=0, DPdiff=0;
	    double oilkickDensity_mixture=0;
		double mwTot = 0;
	//
	    PVgain = 0;
	    QtotMix = 0;
	    volkkmix = 0;
	    iXb = MainDriver.N2phase-1;
	    
	    for(int i = MainDriver.N2phase-1; i>0; i--){
	    	xvdcell=utilityModule.getVD(xcell);
	    	MainDriver.tx = utilityModule.temperature(xvdcell);
	    	gasPropEX = propertyModule.GasProp(pxcell, MainDriver.tx);//, gasVis, gasDen, zx);
	    	gasVis = gasPropEX[0];
	    	if(gasVis>1000000000){
	    		dummy=1;
	    	}
	    	gasDen = gasPropEX[1];
	    	zx = gasPropEX[2];
	    	if(MainDriver.volL[i] == 0){
	           //volL(i) = VolL_default
	           //volL(i) = volL(N2phase)	    		
	    		MainDriver.volL[i] = MainDriver.delta_T[i] * MainDriver.volL[MainDriver.N2phase-1] / MainDriver.delta_T[MainDriver.N2phase-1];
	    		}
	    	if(MainDriver.imud == 1){ //OBM case	    		
	    		MainDriver.R = 10.73; //Universal gas constant
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
	        	//.................................. calculate the pressure at the top
	        	MainDriver.volkx = MainDriver.PVTZ_free[i] * MainDriver.tx * zx * MainDriver.R / pxcell; //pit gain by free gas volume(ft^3)
	        	MainDriver.volkx = MainDriver.volkx / 5.615; //pit gain by free gas volume(bbls)
	        	
	        	//---------------------------------------------------------------------------------------------------Applying PREOS
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
		        if(MainDriver.mud_calc==0){
	        		MainDriver.V_cont = utilityModule.PREOS(pxcell, MainDriver.tx, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);
	        		MainDriver.V_cont_ref = utilityModule.PREOS(pxcell, MainDriver.tx, 0, 0.01, 0, MainDriver.mole_OBM);
	        		//
	        		MainDriver.Vsol[i] = (MainDriver.V_cont - MainDriver.V_cont_ref);
	        		MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.volL[i] * MainDriver.foil / MainDriver.V_cont;
	        		//
	        		}
	        	else{	        		
	        		utilityModule.OBM_Fraction(MainDriver.GasFr, MainDriver.OBMFr); //solution gas mole, obm gas mole
	    			oilkickDensity_mixture  = utilityModule.calcSKdensity(MainDriver.tx, pxcell-14.7); //ppg
	    			mwTot = MainDriver.mole_solgas * 16.04 + MainDriver.OBMwt * MainDriver.mole_OBM; //lb
		    		MainDriver.Vsol[i] = mwTot / oilkickDensity_mixture / 42 - MainDriver.volL[i] * MainDriver.foil;	
	        	}
		        //
		        MainDriver.Den_tmp = (MainDriver.oMud * MainDriver.volL[i] * 42 + MainDriver.PVTZ_sol[i] * 16.04) / (MainDriver.volL[i] + MainDriver.Vsol[i]) / 42;
		        MainDriver.Dendiff[i] = MainDriver.Den_tmp - MainDriver.oMud; //density change
		        //
		        if(MainDriver.Dendiff[i] > 0) MainDriver.Dendiff[i] = 0;
	    		}
	    	//---------------------------------------------------------------------------------------------------Applying PREOS
	    	else MainDriver.volkx = MainDriver.pvtZb[i] * MainDriver.tx * zx / pxcell; //WBM Case
	    	
	    	voltmp = MainDriver.volL[i] + MainDriver.Vsol[i];
	    	volTot = MainDriver.volkx + voltmp;
	    	//
	    	if(MainDriver.volkx > 0.001) iXb = i - 1;
	    	MainDriver.volG[i] = MainDriver.volkx;
	    	//voltmp = volL(i): volTot = volkx + voltmp
	    	//       If (voltot < .00001 and i <> n2phase) Then
	    	//          hgcal = 0
	    	//          pnd(i) = pnd(i + 1): xnd(i) = xnd(i + 1)
	    	//          vollqd(i) = voltmp: hgnd(i) = hgcal
	    	//          GoTo 56789
	    	//       End If
	    	if(volTot < 0.00001) volTot = 0.00001;
	    	HgCal = MainDriver.volkx / volTot;
	    	if(HgCal >= 0.0001){
	    		//................. set the constant gas fraction at the top ie. less than 0.45
	    		//Modified by TY
	    		if (i > MainDriver.N2phase - MainDriver.nHgCon -1 && HgCal > 0.45){
	    			HgCal = MainDriver.HgndOld[i];
	    			}
	    		//voltmp = volkx * (1 - HgCal) / HgCal
	    		//volTot = volkx + voltmp
	    		//...................................... set the maximum gas fraction
	    		//Modified by TY
	    		MainDriver.HgMax = 0.45;
	    		if (HgCal > MainDriver.HgMax) HgCal = MainDriver.HgMax;
	    		//voltmp = volkx * (1 - HgMax) / HgMax
	    		//volTot = volkx + voltmp
	    		//...................................... ??
	    		}//6789 dummy = 0  //simple continue
	    	hxt = utilityModule.getTopH(volTot, xcell);
	    	xbcell = xcell + hxt;		        
	    	xbvdcell = utilityModule.getVD(xbcell);
	    	
	    	//--- check the location of kill mud pumped
	    	rhoL = MainDriver.oMud + MainDriver.Dendiff[i]; //density change due to low-density gas dissolution
	    	if(xcell > xKill){
	    		rhoL = MainDriver.Kmud;
	    		volkkmix = volkkmix + MainDriver.volkx;
	    		}
	    	avgden = (rhoL) * (1 - HgCal) + gasDen * HgCal;
	    	//avgden = gasDen * HgCal + rhoL * (1 - HgCal)
	    	dpmud = 0.052 * avgden * (xbvdcell - xvdcell);
	    	ql = MainDriver.QtMix * voltmp / volTot;
	    	Qg = MainDriver.QtMix * MainDriver.volkx / volTot;
	    	utilityModule.get2pDP(xbcell, ql, Qg, HgCal, rhoL, gasDen, gasVis);
	    	dpfri = MainDriver.DPtop;
	    	utilityModule.get2pDP(xcell, ql, Qg, HgCal, rhoL, gasDen, gasVis);
	    	dpfri = dpfri - MainDriver.DPtop;   // DP-fric in mixed zone
	    	//............................................. assign calculated value
	    	MainDriver.Pnd[i] = pxcell;
	    	MainDriver.Xnd[i] = xcell;
	    	volLqd[i] = voltmp;
	    	MainDriver.Hgnd[i] = HgCal;
	    	pxcell = pxcell + dpmud + dpfri * MainDriver.Pcon;	 
	    	if(pxcell>100000) pxcell=123456; //added by jaewoo 20140109
	    	xcell = xbcell;
	    	PVgain = MainDriver.Vsol[i] + PVgain + MainDriver.volkx;
	    	QtotMix = QtotMix + volTot;
	    	//	56789  dummy = 0
	    	//
	    	}
	    //
	    if (xbcell > MainDriver.TMD[MainDriver.NwcS-1]) xbcell = MainDriver.TMD[MainDriver.NwcS-1];
	    MainDriver.Xnd[0] = xbcell;
	    MainDriver.Pnd[0] = pxcell;
	    //MainDriver.Xb = MainDriver.Xnd[iXb];
	    MainDriver.Xb = Math.min(MainDriver.Xnd[0], MainDriver.TMD[MainDriver.NwcS-1]);
	    
	    temp_oMud = MainDriver.oMud;
	    if(MainDriver.iProblem[3]==1) MainDriver.oMud = MainDriver.oMud_save;
	    Pbtm = utilityModule.pxBottom(volKmud, xbcell);  //new cell is added continuously
	    if(MainDriver.iProblem[3]==1) MainDriver.oMud = temp_oMud;
	    
	    DPdiff = MainDriver.Pb - Pbtm;
	    PbTry = pxcell + DPdiff;
	    }
	
	void Pbcal2_SK(){
		int iXb=0;
		//  Calculates B.H.P. when annulus pressure is given. - new AWC version
		//  No Slip and Top properties as an average properties for Simplicity !
		//.................................... assign the top cell conditions
		//Modified by TY
		int xNull = 0;
		double[] gasPropEX = new double[3];
		double pxcell = px, xcell = X, xbcell=0, xvdcell = xVert, xbvdcell=0;
		MainDriver.tx = utilityModule.temperature(xvdcell);
	    double surfTen = propertyModule.surfT(px, MainDriver.tx);
	    double zx=0, avgden=0, dpmud=0;
	    double volTot =0, voltmp=0, HgCal=0, hxt=0;
	    double tx2=0, wkbtmp=0, rhoL=0;
	    double dpfri=0, ql=0, Qg=0, DPdiff=0;
	    double oilkickDensity_mixture=0;
		double mwTot = 0;
	//
	    PVgain = 0;
	    QtotMix = 0;
	    volkkmix = 0;
	    iXb = MainDriver.N2phase-1;
	    
	    for(int i = MainDriver.N2phase-1; i>0; i--){
	    	xvdcell=utilityModule.getVD(xcell);
	    	MainDriver.tx = utilityModule.temperature(xvdcell);
	    	gasPropEX = propertyModule.GasProp(pxcell, MainDriver.tx);//, gasVis, gasDen, zx);
	    	gasVis = gasPropEX[0];
	    	if(gasVis>1000000000){
	    		dummy=1;
	    	}
	    	gasDen = gasPropEX[1];
	    	zx = gasPropEX[2];
	    	if(MainDriver.volL[i] == 0){
	           //volL(i) = VolL_default
	           //volL(i) = volL(N2phase)	    		
	    		MainDriver.volL[i] = MainDriver.delta_T[i] * MainDriver.volL[MainDriver.N2phase-1] / MainDriver.delta_T[MainDriver.N2phase-1];
	    		}
	    	if(MainDriver.imud == 1){ //OBM case
	    		MainDriver.R = 10.73; //Universal gas constant
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
	        	//.................................. calculate the pressure at the top
	        	MainDriver.volkx = MainDriver.PVTZ_free[i] * MainDriver.tx * zx * MainDriver.R / pxcell; //pit gain by free gas volume(ft^3)
	        	MainDriver.volkx = MainDriver.volkx / 5.615; //pit gain by free gas volume(bbls)
	        	
	        	//---------------------------------------------------------------------------------------------------Applying PREOS
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
		        if(MainDriver.mud_calc==0){
	        		MainDriver.V_cont = utilityModule.PREOS(pxcell, MainDriver.tx, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);
	        		MainDriver.V_cont_ref = utilityModule.PREOS(pxcell, MainDriver.tx, 0, 0.01, 0, MainDriver.mole_OBM);
	        		//
	        		MainDriver.Vsol[i] = (MainDriver.V_cont - MainDriver.V_cont_ref);
	        		MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.volL[i] * MainDriver.foil / MainDriver.V_cont;
	        		//
	        		}
	        	else{	        		
	        		utilityModule.OBM_Fraction(MainDriver.GasFr, MainDriver.OBMFr); //solution gas mole, obm gas mole
	    			oilkickDensity_mixture  = utilityModule.calcSKdensity(MainDriver.tx, pxcell-14.7); //ppg
	    			mwTot = MainDriver.mole_solgas * 16.04 + MainDriver.OBMwt * MainDriver.mole_OBM; //lb
		    		MainDriver.Vsol[i] = mwTot / oilkickDensity_mixture / 42 - MainDriver.volL[i] * MainDriver.foil;	
	        	}
		        //
		        MainDriver.Den_tmp = (MainDriver.oMud * MainDriver.volL[i] * 42 + MainDriver.PVTZ_sol[i] * 16.04) / (MainDriver.volL[i] + MainDriver.Vsol[i]) / 42;
		        MainDriver.Dendiff[i] = MainDriver.Den_tmp - MainDriver.oMud; //density change
		        //
		        if(MainDriver.Dendiff[i] > 0) MainDriver.Dendiff[i] = 0;
	    	}
	    	//---------------------------------------------------------------------------------------------------Applying PREOS
	    	else MainDriver.volkx = MainDriver.pvtZb[i] * MainDriver.tx * zx / pxcell; //WBM Case
	    	
	    	voltmp = MainDriver.volL[i] + MainDriver.Vsol[i];
	    	volTot = MainDriver.volkx + voltmp;
	    	//
	    	if(MainDriver.volkx > 0.001) iXb = i - 1;
	    	MainDriver.volG[i] = MainDriver.volkx;
	    	//voltmp = volL(i): volTot = volkx + voltmp
	    	//       If (voltot < .00001 and i <> n2phase) Then
	    	//          hgcal = 0
	    	//          pnd(i) = pnd(i + 1): xnd(i) = xnd(i + 1)
	    	//          vollqd(i) = voltmp: hgnd(i) = hgcal
	    	//          GoTo 56789
	    	//       End If
	    	if(volTot < 0.00001) volTot = 0.00001;
	    	HgCal = MainDriver.volkx / volTot;
	    	if(HgCal >= 0.0001){
	    		//................. set the constant gas fraction at the top ie. less than 0.45
	    		//Modified by TY
	    		if (i > MainDriver.N2phase - MainDriver.nHgCon -1 && HgCal > 0.45){
	    			HgCal = MainDriver.HgndOld[i];
	    			}
	    		//voltmp = volkx * (1 - HgCal) / HgCal
	    		//volTot = volkx + voltmp
	    		//...................................... set the maximum gas fraction
	    		//Modified by TY
	    		MainDriver.HgMax = 0.45;
	    		if (HgCal > MainDriver.HgMax) HgCal = MainDriver.HgMax;
	    		//voltmp = volkx * (1 - HgMax) / HgMax
	    		//volTot = volkx + voltmp
	    		//...................................... ??
	    		}//6789 dummy = 0  //simple continue
	    	hxt = utilityModule.getTopH(volTot, xcell);
	    	xbcell = xcell + hxt;		        
	    	xbvdcell = utilityModule.getVD(xbcell);
	    	
	    	//--- check the location of kill mud pumped
	    	rhoL = MainDriver.oMud + MainDriver.Dendiff[i]; //density change due to low-density gas dissolution
	    	if(xcell > xKill){
	    		rhoL = MainDriver.Kmud;
	    		volkkmix = volkkmix + MainDriver.volkx;
	    		}
	    	avgden = (rhoL) * (1 - HgCal) + gasDen * HgCal;
	    	//avgden = gasDen * HgCal + rhoL * (1 - HgCal)
	    	dpmud = 0.052 * avgden * (xbvdcell - xvdcell);
	    	ql = MainDriver.QtMix * voltmp / volTot;
	    	Qg = MainDriver.QtMix * MainDriver.volkx / volTot;
	    	utilityModule.get2pDP(xbcell, ql, Qg, HgCal, rhoL, gasDen, gasVis);
	    	dpfri = MainDriver.DPtop;
	    	utilityModule.get2pDP(xcell, ql, Qg, HgCal, rhoL, gasDen, gasVis);
	    	dpfri = dpfri - MainDriver.DPtop;   // DP-fric in mixed zone
	    	//............................................. assign calculated value
	    	MainDriver.Pnd[i] = pxcell;
	    	MainDriver.Xnd[i] = xcell;
	    	volLqd[i] = voltmp;
	    	MainDriver.Hgnd[i] = HgCal;
	    	pxcell = pxcell + dpmud + dpfri * MainDriver.Pcon;	 
	    	if(pxcell>100000) pxcell=123456; //added by jaewoo 20140109
	    	xcell = xbcell;
	    	PVgain = MainDriver.Vsol[i] + PVgain + MainDriver.volkx;
	    	QtotMix = QtotMix + volTot;
	    	//	56789  dummy = 0
	    	//
	    	}
	    //
	    if (xbcell > MainDriver.TMD[MainDriver.NwcS-1]) xbcell = MainDriver.TMD[MainDriver.NwcS-1];
	    MainDriver.Xnd[0] = xbcell;
	    MainDriver.Pnd[0] = pxcell;
	    //MainDriver.Xb = MainDriver.Xnd[iXb];
	    MainDriver.Xb = Math.min(MainDriver.Xnd[0], MainDriver.TMD[MainDriver.NwcS-1]);
	    
	    temp_oMud = MainDriver.oMud;
	    if(MainDriver.iProblem[3]==1) MainDriver.oMud = MainDriver.oMud_save;
	    Pbtm = utilityModule.pxBottom(volKmud, xbcell);  //new cell is added continuously
	    if(MainDriver.iProblem[3]==1) MainDriver.oMud = temp_oMud;
	    
	    DPdiff = MainDriver.Pb - Pbtm;
	    PbTry = pxcell + DPdiff;
	    }
	
	int Golden2(){    //--- AWC version for the MultiPle Kicks
		int iLoc=0;   //--- Modified on 7-04-94 !
		double cd=0.6, rhoL=0, totArea=0, dpchoke=0, hgX=0, kcpcv=0, rhom=0, yy=0, delvol=0, delvol2=0, DPacc=0, xmdtmp=0, d1=0, d2=0;
		double Qeff=0, cap2eff2=0, volEff=0;
		double psicon = 0.001614678, fract =0, dpaccel=0, xkillvd=0, pxx=0, pxdp=0, txvd=0, DPdiff=0;
		double qgrgpm = 0, qgtbbl=0;
	//--- direct calculation of Px & BHP from DP@choke control,
	//    DP_hydrostatic, DP_friction
	    if(CHKpcent == 0){       //check complete choke close !
	    	String msg = "Pump failure !";
	    	JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
	    	if(TimerCirculationOn==1){
	    		TimerCirculationOn=0;
	    		TimerCirculation.cancel(); 
	    		cmdFixPump.setVisible(true);
	    		}
	    	optPump1.setEnabled(false);
	    	optPump2.setEnabled(false);
	    	pumpFailure1=1;
	    	pumpFailure2=1;
	    	return -1;
	    	}
	    
	    if(MainDriver.spMin1==0 && MainDriver.spMin2==0 && (pumpFailure1==1 || pumpFailure2==1)){	    	
	    	String msg = "";
	    	if(pumpFailure1==1 && pumpFailure2==0){
	    		msg = "Pump 1 failure !";
	    		optPump1.setEnabled(false);
	    		optPump2.setSelected(true);
	    		txtPumpR1.setVisible(false);
				txtPumpR2.setVisible(true);
				pumpScroll1.setVisible(false);
				pumpScroll2.setVisible(true);
				txtStks1.setVisible(false);
				txtStks2.setVisible(true);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(false);
				btnSetStkZero2.setVisible(true);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
	    		}
	    	
	    	if(pumpFailure1==0 && pumpFailure2==1){
	    		msg = "Pump 2 failure !";
	    		optPump2.setEnabled(false);
	    		optPump1.setSelected(true);
	    		txtPumpR1.setVisible(true);
				txtPumpR2.setVisible(false);
				pumpScroll1.setVisible(true);
				pumpScroll2.setVisible(false);
				txtStks1.setVisible(true);
				txtStks2.setVisible(false);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(true);
				btnSetStkZero2.setVisible(false);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
	    		}
	    	
	    	if(pumpFailure1==1 && pumpFailure2==1){
	    		msg = "Pump 1&2 failure !";
	    		optPump1.setEnabled(false);
	    		optPump2.setEnabled(false);
	    		optPump1.setSelected(true);
	    		txtPumpR1.setVisible(true);
				txtPumpR2.setVisible(false);
				pumpScroll1.setVisible(true);
				pumpScroll2.setVisible(false);
				txtStks1.setVisible(true);
				txtStks2.setVisible(false);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(true);
				btnSetStkZero2.setVisible(false);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
	    		}
	    	
	    	JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
	    	
	    	if(TimerCirculationOn==1){
	    		TimerCirculationOn=0;
	    		TimerCirculation.cancel(); 
	    		cmdFixPump.setVisible(true);
	    		}    	
	    	}
	    
	    utilityModule.Bkup();
	    rhoL = MainDriver.oMud;
	    if(xKill < 0.2) rhoL = MainDriver.Kmud;
	   
	    totArea = 0.25 * MainDriver.pai * CHKpcent * MainDriver.DchkControl * MainDriver.DchkControl * 0.01;  //open %
	    if(X > 0.01) dpchoke = 0.00008311 * rhoL * MainDriver.QtMix * MainDriver.QtMix / Math.pow((totArea * cd), 2); //single-phase flow	       
	    else{
	       hgX = MainDriver.Hgnd[MainDriver.N2phase-1];
	       kcpcv = 1.4;
	       rhom = gasDen * hgX + rhoL * (1 - hgX);
	       yy = 1 - (0.41 + 0.35 * Math.pow(CHKpcent/100, 2)) / kcpcv;// * 0.00000001
	       if(yy < 0.5) yy = 0.5; //DP is too big for small yy
	       if (hgX < 0.01) yy = 1;
	       dpchoke = 0.00008311 * rhom * MainDriver.QtMix * MainDriver.QtMix / Math.pow((totArea * cd * yy), 2);
	       }
	    Pchoke = dpchoke + 14.7;
	    //--- direct calculation of Px
	    //--------------------------------------------------------- acceleration term
	    delvol = (PVgain - MainDriver.Vpit[MainDriver.Np - 1]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np] + 1);
	    delvol = propertyModule.range(delvol, 0, MainDriver.Qkill * 2.5);
	    delvol2 = delvol / 2.448;
	    iLoc = utilityModule.Xposition(X);
	    DPacc = 0; 
	    xmdtmp = X - MainDriver.TMD[iLoc + 1];
	    for(int i = (iLoc + 1); i<9; i++){
	    	d2 = MainDriver.Do2p[i];
	    	d1 = MainDriver.Di2p[i];
	    	DPacc = DPacc + xmdtmp * delvol2 / (d2 * d2 - d1 * d1);
	    	xmdtmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1];
	    }
	    //............................................. for choke or kill lines
	    getLinesEX = utilityModule.getLines(MainDriver.QtMix); //index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
	    Qeff=getLinesEX[0];
	    cap2eff2=getLinesEX[1];
	    volEff = getLinesEX[2];
	    d1 = getLinesEX[3];
	    d2 = getLinesEX[4];
	    DPacc = DPacc + xmdtmp * delvol2 / (d2 * d2 - d1 * d1) * Qeff / MainDriver.QtMix;
	    DPacc = psicon * MainDriver.oMud * DPacc / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np] + 1);
	    if (X >= xKill - 0.01){  //use the fraction
	         fract = xKill / (X + 0.1);
	         dpaccel = DPacc * fract + DPacc * MainDriver.Kmud / MainDriver.oMud * (1 - fract);
	         DPacc = dpaccel;
	    }
	    //--------------------------------------------------------------------------
	    xVert = utilityModule.getVD(X);
	    xkillvd = utilityModule.getVD(xKill);
	    if (xKill > X){
	       utilityModule.getDP(MainDriver.QtMix, X, MainDriver.oMud);
	       pxx = Pchoke + MainDriver.gMudOld * xVert + MainDriver.Pcon * MainDriver.DPtop;
	    }
	    else{
	       utilityModule.getDP(MainDriver.QtMix, xKill, MainDriver.oMud);
	       pxdp = MainDriver.DPtop;
	       utilityModule.getDP(MainDriver.QtMix, xKill, MainDriver.Kmud);
	       pxdp = pxdp - MainDriver.DPtop;
	       utilityModule.getDP(MainDriver.QtMix, X, MainDriver.Kmud);
	       pxdp = pxdp + MainDriver.DPtop;
	       pxx = Pchoke + MainDriver.gMudOld * xkillvd + MainDriver.gMudKill * (xVert - xkillvd) + MainDriver.Pcon * pxdp;
	    }
	    px = pxx + DPacc;
	    
	    if(px>100000) px = 123456; // 20131216 ajw 
	    if(MainDriver.N2phase >= 2){
	       Pbcal2();
	       xVert = utilityModule.getVD(X);
	       txvd = utilityModule.temperature(xVert);
	       GasPropEX = propertyModule.GasProp(px, txvd);//, gasVis, gasDen, zz
	       gasVis = GasPropEX[0];
	       gasDen = GasPropEX[1];
	       zz = GasPropEX[2];
	    }
	    
	    else{
	    	Pbtm = utilityModule.pxBottom(volKmud, 0);
	    	DPdiff = MainDriver.Pb - Pbtm;
	    	PbTry = Pchoke + DPdiff;
	    	MainDriver.Xb = 0;
	    	xVert = 0;
	    	gasDen = 0;
	    	}
	    
	    Pbeff = PbTry;  //no iteration is necessary - direct calculation
	    
	    //.......................................... calculate other variables
	    //For ii = 2 To N2phase
	    //tmp2 = volL(ii): tmp3 = volLqd(ii): volL(ii) = Max(tmp2, tmp3)
	    //Next ii
	    //...................... calculate pump pressure and stand pipe pressure
	    if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(Pbeff, MainDriver.Qkill, volKmud);//, SPP, pumpP);
	    if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(Pbeff, MainDriver.Qkill, volKmud);//, SPP, pumpP);
	    SPP = getDPinsideEX[0];
	    pumpP = getDPinsideEX[1];
	    //....................................... calculate casing seat pressure
	    if (MainDriver.DepthCasing > MainDriver.Xb){
	    	temp_oMud = MainDriver.oMud;
		    if(MainDriver.iProblem[3]==1) MainDriver.oMud = MainDriver.oMud_save;
	    	Pbtm = utilityModule.pxBottom(volKmud, MainDriver.DepthCasing);
		    if(MainDriver.iProblem[3]==1) MainDriver.oMud = temp_oMud;
		    
	    	DPdiff = MainDriver.Pb - Pbtm;
	    	Pcasing = Pbeff - DPdiff;
	    	}
	    else if(MainDriver.DepthCasing > X) Pcasing = propertyModule.DitplDes(MainDriver.DepthCasing);
	    else{
	    	utilityModule.getDP(MainDriver.QtMix, MainDriver.DepthCasing, MainDriver.oMud);
	    	Pcasing = Pchoke + MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + MainDriver.Pcon * MainDriver.DPtop;
	    	}
	    //--- calculate Qt-mix for the next calculation, 7/17/02
	    //........... old approach gives very small return rate increase since average from beginning
	    //      Qtmix = Qkill + (PVgain - Vpit(NpSi)) * 42 * 60 / (gTcum - Tdelay + 2)
	    //      Call range(Qtmix, Qkill, Qkill * 2.5)
	    if(MainDriver.Np <= MainDriver.NpSi + 10) MainDriver.QtMix = MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.NpSi]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.NpSi] + 1);
	    else{
	    	//QtMix = Qkill + (PVgain - Vpit(Np - 4)) * 42 * 60 / (gTcum - TTsec(Np - 4))
	    	//Modified by TY
	    	MainDriver.Qtmix1 = MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np - 4]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np- 4]);
	    	MainDriver.Qtmix2 = MainDriver.Qkill + (PVgain - MainDriver.Vpit[MainDriver.Np - 2]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np - 2]);
	    	MainDriver.QtMix = Math.min(MainDriver.Qtmix1, MainDriver.Qtmix2); //constraint chk pressure oscillation
	    }
	    MainDriver.QtMix = propertyModule.range(MainDriver.QtMix, MainDriver.Qkill, MainDriver.Qkill * 4.5);
	    
	    //--- check the possible multiple kicks and assign in the node
	    //    these result will be accounted in the NEXT calculation !
	    //----------------------------------------------- 2nd kick
	    
	    MainDriver.volG[0] = 0;
	    MainDriver.volL[0] = 0;	    
	    if(MainDriver.gTcum >=489){
	    	dummy=1;
	    }
	    QgDay = utilityModule.GASinflux(Pbeff, MainDriver.gDelT, MainDriver.Hdrled);    // flow rate in Mscf/Day	    
	    qgrgpm = 29.166667 * QgDay * MainDriver.fvfGas;       // convert to reservoir volume
	    qgtbbl = qgrgpm * MainDriver.gDelT / (42 * 60);	    
	    
	    
	    if(MainDriver.imultikick != 0){ //0: ignore multikick
	    	if(qgtbbl < 0.01){
	    		QgDay = 0;
	    		dummy = AddOmud();
	    		}
	    	else{
	    		MainDriver.volG[0] = qgtbbl;
	    		MainDriver.volL[0] = MainDriver.Qkill * MainDriver.gDelT / (60 * 42);
	    		AddKick();
	    		}
	    	}
	    return 1;
	}
	
	class TimerCircTask2 extends TimerTask{
		double timeAdd=0, iSurface = 0;;
		double Xold =0, hgX=0, Xnew=0, Vgf=0;
		double[] getXnewex = new double[2];
		public void run(){
			//-----This is the main control program until kick top arrives at the surface
			//     This is also valid for ML wells.  7/30/2003
			//
			TimerCircTask2Finished = 1;
			timeAdd = TimerCirculationIntv * 0.001 * MainDriver.SimRate; //time interval from last step
			timeNpUse = 60;
			//timeNpUse = 5; //히히
			
			if(TimerCircTask2Finished == 1){
				if (MainDriver.Np== MainDriver.NpSi){      // assign the very beginning of the Pumping
					//       timeadd = 2 * tmd(MainDriver.NwcS) / MainDriver.Vsound   //increase twice to reduce accel. loss, 7/11/02
					timeAdd = 30;   //set 30 sec to reduce accel. loss, 7/11/02
					timeNp = 62;
					}
											
				Xold =  X;
				Pbeff = MainDriver.Pb;
				hgX = MainDriver.Hgnd[MainDriver.N2phase-1];
				
				if(MainDriver.imud == 0){
					getXnewex = utilityModule.GetXnew(Xold, timeAdd, hgX, MainDriver.QtMix);//, Xnew, Vgf)
					Xnew = getXnewex[0];					
					Vgf = getXnewex[1];
					}
				else{
					getXnewex = utilityModule.GetXnew(Xold, timeAdd, 0, MainDriver.QtMix);//, Xnew, Vgf)
					Xnew = getXnewex[0];
					Vgf = getXnewex[1];
					}	
				X = Xnew;
				if (X < -0.01 && mudCase == -99){
					timeAdd = Xold / Vgf;
					X = 0 ;    // KICK just arrives at the surface
					mudCase = -1;
					}
				
				if(mudCase == -1){
					timeAdd = NewN2ph(timeAdd);
					}
				
				if(X<-0.01) X=0;
				
				MainDriver.gTcum = MainDriver.gTcum + timeAdd;   //total time from beginning of KILL
				MainDriver.gDelT =  timeAdd;           //time interval				
								
				timeOld = timeNew;
				CHKpcent = HScrollCHK.getValue()/10.0; //140727 ajw
				//CHKpcent = HScrollCHK.getValue();
				MainDriver.volPump = MainDriver.volPump + MainDriver.Qkill * MainDriver.gDelT / (42.*60.);
				MainDriver.volPump2 = MainDriver.volPump2 + MainDriver.Qkill * MainDriver.gDelT / (42.*60.);
				
				if(MainDriver.Method ==2) volKmud = volKmud+ MainDriver.Qkill*MainDriver.gDelT/(42.*60.);
				iShift = 0;
				dummy = GetXkill();
				
				if(MainDriver.imode == 1) Golden2();
				else Golden();
				
				timeNp = (int) (timeNp + timeAdd);
				ShowResult();
				//--------------------------------------------- Remove the KICK out of Hole
				if(timeNp >= timeNpUse) AssignVal();
				
				if(MainDriver.Xb < 0.1 && volKmud > MainDriver.VOLinn + MainDriver.VOLout){
					if(TimerCirculationOn ==1){
						TimerCirculationOn =0;
						this.cancel();		
						}
					String msg =  "The Well has been Killed. You did a GREAT job  !!";
					JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
					}
				TimerCircTask2Finished = 0;
				}
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
		int n2phOld=0, Remains =0;
		double delTsum=0, x2Top=0, x2Bot, hgX=0, timeint=0;
		if(MainDriver.N2phase == 1) return timeAdd;
		
		delTsum = 0; 
		n2phOld = MainDriver.N2phase;
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
					if(MainDriver.imud ==0 ) MainDriver.pvtZb[i] = MainDriver.pvtZb[i] * Remains / (timeint + 0.002);//WBM
					else{//OBM
						MainDriver.R=10.73;
						MainDriver.PVTZ_Gas[i] = MainDriver.PVTZ_Gas[i] * Remains / (timeint + 0.002);
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
	
	void AddKick(){
		//--- to account the multiple Kicks that may in old mud or in kill mud
		int n2phOld =0;
		double VOLt = 0, xbottom=0, volMid=0, Xbcell=0, hhm=0, xtopnew=0;
		double gasvisb=0, gasdenb=0, zb=0;
	    n2phOld = MainDriver.N2phase;
	    VOLt = MainDriver.volG[0] + MainDriver.volL[0];   //gas kick and mud volume in bbls
	    xbottom = MainDriver.TMD[MainDriver.NwcS-1];
	    Xbcell = MainDriver.Xnd[0];
	    hhm = utilityModule.getBotH(VOLt, xbottom);
	    xtopnew = xbottom - hhm;
	    //
	    iShift = 2;
	    if(volKmud < MainDriver.VOLinn) volkkmix = 0;
	    else{
	    	volkkmix = volkkmix + MainDriver.volG[0];    //kill mud top moves as kick adding
	    	if(xKill > Xbcell) iShift = 3;
	    	}
	    if (xtopnew <= Xbcell){
	    	xtopnew = Xbcell;
	    	iShift = 1;
	    	}
	    //--- shift array variable to include the new kick !
	    for(int i = (MainDriver.N2phase - 1 + iShift); i> (iShift - 1); i--){
	        MainDriver.Xnd[i] = MainDriver.Xnd[i - iShift];   
	        MainDriver.Hgnd[i] = MainDriver.Hgnd[i - iShift];
	        MainDriver.volL[i] = MainDriver.volL[i - iShift];
	        MainDriver.volG[i] = MainDriver.volG[i - iShift];
	        MainDriver.Pnd[i] = MainDriver.Pnd[i - iShift];
	        MainDriver.delta_T[i] = MainDriver.delta_T[i-iShift]; //added by JW, 20140106
	        if(MainDriver.imud == 0) MainDriver.pvtZb[i] = MainDriver.pvtZb[i - iShift];
	        else MainDriver.PVTZ_Gas[i] = MainDriver.PVTZ_Gas[i - iShift];
	    }
	    if(iShift == 2){
	    	//--- assign the new cell properties !; X-node and Volume of Liquid
	    	//    pressure is not required - just assign any value !
	    	//Hgnd[2] = 0#; volG[2] = 0#; pvtZb[2] = 0#; Xnd[2] = xbcell
	    	MainDriver.Hgnd[2] = 0; 
	    	MainDriver.volG[2] = 0; 
	    	MainDriver.Xnd[2] = Xbcell;
	    	if(MainDriver.imud == 0) MainDriver.pvtZb[2] = 0;
	    	else MainDriver.PVTZ_Gas[2] = 0;
	    	volMid = GetVolume(Xbcell, xtopnew);
	    	MainDriver.volL[2] = volMid + 0.01; //just prevent dummy cell
	    	MainDriver.delta_T[2] = MainDriver.delta_T[MainDriver.N2phase-1]/MainDriver.volL[MainDriver.N2phase-1]*MainDriver.volL[2]; //added by JW, 20140106
	    }
	    else if(iShift == 3){
	    	//Hgnd[3] = 0#; volG[3] = 0#; pvtZb[3] = 0#; Xnd[3] = xbcell
	    	MainDriver.Hgnd[3] = 0; 
	    	MainDriver.volG[3] = 0; 
	    	MainDriver.Xnd[3] = Xbcell;
	    	if(MainDriver.imud == 0) MainDriver.pvtZb[3] = 0;
	    	else MainDriver.PVTZ_Gas[3] = 0;
	    	
	    	volMid = GetVolume(Xbcell, xKill);
	    	MainDriver.volL[3] = volMid + 0.01;   //just prevent dummy cell
	    	MainDriver.delta_T[3] = MainDriver.delta_T[MainDriver.N2phase-1]/MainDriver.volL[MainDriver.N2phase-1]*MainDriver.volL[3]; //added by JW, 20140106
	    	//Hgnd[2] = 0; volG[2] = 0; pvtZb[2] = 0; Xnd[2] = xKill
	    	MainDriver.Hgnd[2] = 0; 
	    	MainDriver.volG[2] = 0; 
	    	MainDriver.Xnd[2] = xKill;
	    	MainDriver.Pnd[2] = MainDriver.Pnd[3] + 0.052*MainDriver.oMud*(xKill-Xbcell); // added by jaewoo 140312
	    	if(MainDriver.imud == 0) MainDriver.pvtZb[2] = 0;
	    	else MainDriver.PVTZ_Gas[2] = 0;
	    	volMid = GetVolume(xKill, xtopnew);
	    	MainDriver.volL[2] = volMid + 0.01;   //just prevent dummy cell
	    	MainDriver.delta_T[2] = MainDriver.delta_T[MainDriver.N2phase-1]/MainDriver.volL[MainDriver.N2phase-1]*MainDriver.volL[2]; //added by JW, 20140106
	    	}
	    
	    //--- assign the new kick properties in the kicl cell
	    //12345 dummy = 0
	    GasPropEX = propertyModule.GasProp(Pbeff, MainDriver.TbRankin);//, gasvisb, gasdenb, zb)
	    gasvisb = GasPropEX[0];
	    gasdenb = GasPropEX[1];
	    zb = GasPropEX[2];
	    MainDriver.Xnd[1] = xtopnew;
	    MainDriver.Hgnd[1] = MainDriver.volG[0] / VOLt;
	    MainDriver.volL[1] = MainDriver.volL[0];
	    MainDriver.delta_T[1] = MainDriver.delta_T[MainDriver.N2phase-1]/MainDriver.volL[MainDriver.N2phase-1]*MainDriver.volL[1]; //added by JW, 20140106
	    MainDriver.volG[1] = MainDriver.volG[0];
	    MainDriver.Pnd[1] = Pbeff;
	    //pvtZb[1] = Pbeff * volG[1] / (zb * TbRankin)
	    if(MainDriver.imud == 0) MainDriver.pvtZb[1] = Pbeff * MainDriver.volG[1] / (zb * MainDriver.TbRankin);
	    else{
	    	MainDriver.R = 10.73;
	    	MainDriver.PVTZ_Gas[1] = Pbeff * MainDriver.volG[1] / (zb * MainDriver.TbRankin) * MainDriver.R / 5.615;
	    }
	    //--- assign the bottom value
	    MainDriver.Xnd[0] = xbottom; 
	    MainDriver.Hgnd[0] = 0; 
	    MainDriver.volL[0] = 0;
	    //volG[0] = 0; Pnd[0] = Pbeff;  pvtZb[0] = 0
	    MainDriver.volG[0] = 0; 
	    MainDriver.Pnd[0] = Pbeff;
	    MainDriver.delta_T[0] = MainDriver.gTcum-MainDriver.TTsec[MainDriver.Np]; //added by JW, 20140106
	    
	    if(MainDriver.imud == 0) MainDriver.pvtZb[0] = 0;
	    else MainDriver.PVTZ_Gas[0] = 0;
	    
	    MainDriver.N2phase = n2phOld + iShift;
	    MainDriver.Xb = xbottom;
	    }

	
	double GetVolume(double Xup, double Xdown){
		int iPos=0, iPup=0;
		double volMid=0, Qflow=0, capcty=0, d2=0, d1=0, Qeff=0, volEff=0,Htmp=0;
		//
		// the new subroutine to calculate the volume in the annulus based on the
		// given two points.  this is generally good.
		// xup:the given top (MD), ft  volmid:total volume, bbl		
		//
		volMid = 0;
		if(Xup > (Xdown - 0.01)) return 0;
		iPos = utilityModule.Xposition(Xdown);   //identify the location of the given bottom
	    iPup = utilityModule.Xposition(Xup);
	    //.............................. calculate MD by the given volume, bbls
	    Qflow = 0;
	    if(iPos == iPup){
	    	capcty = MainDriver.C12 * (Math.pow(MainDriver.Do2p[iPos + 1], 2) - Math.pow(MainDriver.Di2p[iPos + 1], 2));
	    	if(iPos == 8){
	    		getLinesEX = utilityModule.getLines(Qflow);//, d2, d1, Qeff, capcty, volEff)
	    		// 20130802 ajw : index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
		    	capcty = getLinesEX[1];
		    	}	    	
	    	volMid = capcty * (Xdown - Xup);
	    	}
	    else{
	       Htmp = Xdown - MainDriver.TMD[iPos + 1];
	       for(int i = (iPos + 1); i<=iPup; i++){
	    	   capcty = MainDriver.C12 * (Math.pow(MainDriver.Do2p[i], 2) - Math.pow(MainDriver.Di2p[i], 2));
	    	   volMid = volMid + capcty * Htmp;
	    	   Htmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1];
	    	   }
	       capcty = MainDriver.C12 * (Math.pow(MainDriver.Do2p[iPup + 1], 2) - Math.pow(MainDriver.Di2p[iPup + 1], 2));
	       if (iPup == 8){
	    	   getLinesEX = utilityModule.getLines(Qflow);//, d2, d1, Qeff, capcty, volEff)
	    	   capcty = getLinesEX[1];
	    	   }
	       Htmp = MainDriver.TMD[iPup] - Xup;
	       volMid = volMid + capcty * Htmp;
	       }
	    return volMid;
	    //
	    }
	
	int AddOmud(){
		//--- to account the multiple Kicks that may in old mud or in kill mud
		//    and to cilculate out the old mud for the complete well control
		int n2phOld=0;
		double xbottom =0, xxx=0, volppslip=0, volumetot=0, volcrit=0;
		iShift = 0;
		if(volKmud > MainDriver.VOLinn) return -1;
		//--- assign the new cell properties !: X-node and Volume of Liquid
		//    pressure is not required - just assign any value !
		//   calculate the old mud volume based on geomerty because of slipped mud ! - 2/8/95
		xbottom = MainDriver.TMD[MainDriver.NwcS-1];
		xxx = MainDriver.Xnd[1];
		volppslip =  GetVolume(xxx, xbottom);
		if(volppslip<0){
			 dummy=1;
		 }
		//
		//    volumeadd = qkill * gDelT / (42# * 60)     //this is incorrect due to slipped mud
		 volumetot = volppslip;
		 volcrit = 0.1 * MainDriver.VOLout;  //--- 10 old mud cells to save the time !
		 if(MainDriver.Hgnd[1] < 0.00001 && volumetot < volcrit){
			 MainDriver.Hgnd[1] = 0;
			 MainDriver.volL[1] = volumetot;    //only mud volume increases !
			 MainDriver.volG[1] = 0;
			 MainDriver.pvtZb[1] = 0;
			 }
		 else{
			 n2phOld = MainDriver.N2phase;
			 for(int i = MainDriver.N2phase-1; i>-1; i--){
				 MainDriver.Xnd[i+1] = MainDriver.Xnd[i];
				 MainDriver.Hgnd[i+1] = MainDriver.Hgnd[i];
				 MainDriver.volL[i+1] = MainDriver.volL[i]; 
				 MainDriver.volG[i+1] = MainDriver.volG[i];
				 MainDriver.Pnd[i+1] = MainDriver.Pnd[i];  
				 MainDriver.pvtZb[i+1] = MainDriver.pvtZb[i];
				 MainDriver.delta_T[i+1] = MainDriver.delta_T[i]; //added by JW, 20140106
				 }
			 //--- assign the new cell properties !: X-node and Volume of Liquid
			 //    pressure is not required - just assign any value !
			 //--- assign the new kick properties in the kicl cell
			 xxx = MainDriver.Xnd[0];
			 volppslip = GetVolume(xxx, xbottom);
			 if(volppslip<0){
				 dummy=1;
			 }
			 MainDriver.Hgnd[1] = 0;
			 MainDriver.volL[1] = volppslip;
			 MainDriver.volG[1] = 0;
			 MainDriver.pvtZb[1] = 0;
			 MainDriver.N2phase = n2phOld + 1;
			 }
		 //--- assign the bottom value
		 MainDriver.Hgnd[0] = 0;
		 MainDriver.volL[0] = 0;  
		 MainDriver.Xnd[0] = xbottom;
		 MainDriver.volG[0] = 0;
		 MainDriver.pvtZb[0] = 0;
		 MainDriver.Pnd[0] = Pbeff;		 
		 MainDriver.delta_T[0] = MainDriver.gTcum-MainDriver.TTsec[MainDriver.Np]; //added by JW, 20140106
		 
		 return 0;
	}
	
	void Golden(){
	//  Golden section search to obtain annulus pressure by
	//  comparing calculated B.H.P. with true B.H.P.
		//Added by Ty
		//Sub for automatic chk control
		int iLoc=0, iter=0;
	    utilityModule.Bkup();
	    
	    double px1 = 14.7, f1 = 1, px2 = MainDriver.Pb, f2 = -1, ffpb=0, pxErr=0, delPx=0, delvol=0;
	    double px1old =0, px2old = 0, padd =0, txvd=0, DPacc = 0, d1=0, d2=0, DPtotal=0;
	    double tmp2=0, tmp3=0, xmdtmp=0;
	    double Qeff = 0, cap2eff2 = 0, volEff = 0, psicon = 0;
	    MainDriver.iHgMax = 0;
	    
	    if(MainDriver.spMin1==0 && MainDriver.spMin2==0 && (pumpFailure1==1 || pumpFailure2==1)){	    	
	    	String msg = "";
	    	if(pumpFailure1==1 && pumpFailure2==0){
	    		msg = "Pump 1 failure !";
	    		optPump1.setEnabled(false);
	    		optPump2.setSelected(true);
	    		txtPumpR1.setVisible(false);
				txtPumpR2.setVisible(true);
				pumpScroll1.setVisible(false);
				pumpScroll2.setVisible(true);
				txtStks1.setVisible(false);
				txtStks2.setVisible(true);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(false);
				btnSetStkZero2.setVisible(true);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
	    		}
	    	
	    	if(pumpFailure1==0 && pumpFailure2==1){
	    		msg = "Pump 2 failure !";
	    		optPump2.setEnabled(false);
	    		optPump1.setSelected(true);
	    		txtPumpR1.setVisible(true);
				txtPumpR2.setVisible(false);
				pumpScroll1.setVisible(true);
				pumpScroll2.setVisible(false);
				txtStks1.setVisible(true);
				txtStks2.setVisible(false);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(true);
				btnSetStkZero2.setVisible(false);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
	    		}
	    	
	    	if(pumpFailure1==1 && pumpFailure2==1){
	    		msg = "Pump 1&2 failure !";
	    		optPump1.setEnabled(false);
	    		optPump2.setEnabled(false);
	    		optPump1.setSelected(true);
	    		txtPumpR1.setVisible(true);
				txtPumpR2.setVisible(false);
				pumpScroll1.setVisible(true);
				pumpScroll2.setVisible(false);
				txtStks1.setVisible(true);
				txtStks2.setVisible(false);
				pumpScroll1.setValue((int)(MainDriver.spMin1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMin2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				btnSetStkZero1.setVisible(true);
				btnSetStkZero2.setVisible(false);
				MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
	    		}
	    	
	    	JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
	    	
	    	if(TimerCirculationOn==1){
	    		TimerCirculationOn=0;
	    		TimerCirculation.cancel(); 
	    		cmdFixPump.setVisible(true);
	    		}    	
	    	}
	    
	    while(true){	    	
		    iter = iter + 1;//10
		    px = 0.5 * (px2 + px1);
		    if(iter==7){ //wbm 7 obm 22
		    	double dummy=0;
		    }
		    PbCalc();
		    ffpb = MainDriver.Pb - PbTry;
		    pxErr = Math.abs(ffpb / MainDriver.Pb);
		    if (pxErr < 0.0002) break;
		    if (f1 * ffpb < 0){        //BiSection Method
	    		px2 = px;
	    		f2 = ffpb;
	    	}
	       else{
	    	   px1 = px; 
	    	   f1 = ffpb;
	       }
	       if (iter > 100){ //20131015 AJW ITER 50에서 100으로 수정
	             px = px + MainDriver.Pb - PbTry;
	             break;
	       }
	       if (px < 14.6){
	    	   px = 14.7;
	    	   if(mudCase == -1){
	    	   PbCalc2();//KICK just arrives at the surface //during kick out situation
	    	   break;
	    	   }
	    	   else{
	    		   PbCalc(); //during kick circulation
	    		   break;
	    	   }
	       }
	       
	       delPx = Math.abs(px1 - px2);         // check Infinite loop
	       if (delPx < 0.05){
//	          MsgBox "px does not converge ! then modify it !", 0, msgtitle
	          if (Math.abs(px1 - 14.7) < 0.005){   //px get min. p & Pbeff increase
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
	    double liquid =0 ;
	    double gas =0 ;
	    for(int i=0; i<MainDriver.N2phase; i++){
	    	liquid = liquid + MainDriver.Vsol[i];
	    	gas = gas + MainDriver.volG[i];
	    }
	    //
	//.......................................... calculate & assign values
	//5055  dummy = 0   // simple continue
	      for(int ii = 1; ii<MainDriver.N2phase; ii++){
	    	  //tmp2 = MainDriver.volL[ii]; 
	    	  //tmp3 = volLqd[ii];
	    	  //MainDriver.volL[ii] =  Math.max(tmp2, tmp3);
	      }
	      xVert = utilityModule.getVD(X);	      
	      xbVert=utilityModule.getVD(MainDriver.Xb);
	      txvd = utilityModule.temperature(xVert);
	      GasPropEX = propertyModule.GasProp(px, txvd);//
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
	      iLoc = utilityModule.Xposition(X);
	      DPacc = 0; 
	      xmdtmp = X - MainDriver.TMD[iLoc + 1];
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
	      if(xVert<500){
	    	  dummy=1;
	      }
	      if(MainDriver.imud == 1 && MainDriver.iOilComp==1){
	    	  Pchoke = utilityModule.getDP_kill_auto(MainDriver.QtMix, X, 0, px, MainDriver.oMud);
	    	  Pchoke = Pchoke - DPacc;
	      }
	      else{
	    	  utilityModule.getDP(MainDriver.QtMix, X, MainDriver.oMud);
		      DPtotal = MainDriver.gMudOld * xVert + DPacc + MainDriver.DPtop * MainDriver.Pcon;
		      Pchoke = px - DPtotal; 
	      }
	      
	      
	      //...................... calculate pump pressure and stand pipe pressure
	      if(MainDriver.iProblem[3]==0) getDPinsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volKmud);
	      if(MainDriver.iProblem[3]==1) getDPinsideEX = utilityModule.getDPinside_cutting(MainDriver.Pb, MainDriver.Qkill, volKmud);
	      
	      SPP = getDPinsideEX[0];
	      pumpP = getDPinsideEX[1];
	      //....................................... calculate casing seat pressure
	      if(MainDriver.gTcum>72*60){
	    	  dummy=1;
	      }
	      if (MainDriver.DepthCasing > MainDriver.Xb){
	    	  if(MainDriver.imud==1 && MainDriver.iOilComp==1){
	    		  Pbtm=utilityModule.pxBottom_densChange(MainDriver.volPump, MainDriver.DepthCasing);
	 	          Pcasing = Pbtm;
	    	  }
	    	  else{
	    		  Pbtm=utilityModule.pxBottom(MainDriver.volPump, MainDriver.DepthCasing);
	 	          Pcasing = Pbtm;
	    	  }	         
	         }
	      else if (MainDriver.DepthCasing > X) {
	    	  if(MainDriver.imud==1 && MainDriver.iOilComp==1){
	    		  Pbtm= Pcasing;
	 	          Pcasing = Pbtm;
	    	  }
	    	  else{
	    		  Pcasing = propertyModule.DitplDes(MainDriver.DepthCasing);
	    	  }	    	  
	      }
	      else{
	    	  if(MainDriver.imud==1 && MainDriver.iOilComp==1){
	    		  Pcasing = utilityModule.getDP_kill_auto(MainDriver.QtMix, X, MainDriver.DepthCasing, px, MainDriver.oMud); //140916
	    		  Pbtm = Pcasing;
	    		  }
	    	  else{
	    		  utilityModule.getDP(MainDriver.QtMix, MainDriver.DepthCasing, MainDriver.oMud);
	 	          Pcasing = Pchoke + MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + MainDriver.Pcon * MainDriver.DPtop;
	 	          Pbtm = Pcasing;
	    	  }	         
	      }
	//--- calculate Qt-mix for the next calculation, 7/17/02
	//........... old approach gives very small return rate increase since average from beginning
	//      MainDriver.QtMix = MainDriver.Qkill + (PVgain - MainDriver.Vpit[MainDriver.NpSi)) * 42 * 60 / (MainDriver.gTcum - Tdelay + 2)
	//
	      if( MainDriver.Np <= MainDriver.NpSi + 10) MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.NpSi]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.NpSi] + 1));
	      else MainDriver.QtMix =  (MainDriver.Qkill + (PVgain + mlPVgain - MainDriver.Vpit[MainDriver.Np - 4]) * 42 * 60 / (MainDriver.gTcum - MainDriver.TTsec[MainDriver.Np- 4]));
	      MainDriver.QtMix = propertyModule.range(MainDriver.QtMix, MainDriver.Qkill, MainDriver.Qkill * 4.5f);
	      
	//
	}	
	
	
	int scx(double x, double Max, double Min, double intv){//intv = GraphSize-sepnum*sepintv
		int result = 0;		
		result = (int)((x-Min)/(Max-Min)*(GraphXsize-intv)+GraphXLoc);
		if(result>GraphXLoc+GraphXsize) result = GraphXLoc+GraphXsize-3;
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
	    double sgYMaxLim=1, sgYMinLim=0;
	    double sgX2Max=1, sgX2Min=1, sgY2Max=1, sgY2Min=1;
	    int sepXnum=6; int sepYnum=6;
	    int sg2Use=0;
	    double sepXIntv=1; double sepYIntv=1;
	    double sgxMaxAdj=1, sgyMaxAdj=1;
	    double sgxMinAdj=0, sgyMinAdj=0;
	    double temp = 0;
	    int temp_int = 0;
	    int filter = 0;// 
	    
	    double[] SgxData = new double[MainDriver.Npt+1];
	    double[] SgyData = new double[MainDriver.Npt+1];
	    double[] Sgx2Data = new double[MainDriver.Npt+1];
	    double[] Sgy2Data = new double[MainDriver.Npt+1];	    
	    double x1=0, x2=0, y1=0, y2=0;
	    
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
			try{
			super.paint(g);
			if(sg2Use==1) {
				dummy=1;
			}
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
			double dummy2=0;
			drawwidth = 3;
			g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 			
			for(int i=0; i<RowCount-1; i++){ // TOTAL NUMBER OF POINT = Rowcount , so the number of line is equal to RowCount-1				
				x1=SgxData[i];
				x2=SgxData[i+1];
				y1=SgyData[i];
				y2=SgyData[i+1];
				
				if(sgYMaxLim<SgyData[i] && sgYMaxLim>SgyData[i+1]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x1 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMaxLim-SgyData[i])+SgxData[i];
					y1 = sgYMaxLim;
				}
				else if(sgYMinLim>SgyData[i] && sgYMinLim<SgyData[i+1]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x1 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMinLim-SgyData[i+1])+SgxData[i+1];
					y1 = sgYMinLim;
				}
				
				if(sgYMaxLim<SgyData[i+1] && sgYMaxLim>SgyData[i]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x2 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMaxLim-SgyData[i+1])+SgxData[i+1];
					y2 = sgYMaxLim;
				}
				else if(sgYMinLim>SgyData[i+1] && sgYMinLim<SgyData[i]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x2 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMinLim-SgyData[i])+SgxData[i];
					y2 = sgYMinLim;
				}
				
				if((sgYMinLim>SgyData[i+1] && sgYMinLim>SgyData[i])||(sgYMaxLim<SgyData[i+1] && sgYMaxLim<SgyData[i])){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x1=-10000;
					x2=-10000;
					y1=-10000;
					y2=-10000;
				}
				g.setColor(Color.RED);
				double ex1=0, ex2=0;
				ex1=scx(x1, sgxMaxAdj, sgxMinAdj, xAdjCon);
				ex2=scx(x2, sgxMaxAdj, sgxMinAdj, xAdjCon);
				if(ex1>ex2){
					dummy=1;
				}
				
				g.drawLine(scx(x1, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y1, sgyMaxAdj, sgyMinAdj, yAdjCon), scx(x2, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y2, sgyMaxAdj, sgyMinAdj, yAdjCon));
				//g.drawLine(scx(SgxData[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(SgyData[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx(SgxData[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(SgyData[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
				
				x1=Sgx2Data[i];
				x2=Sgx2Data[i+1];
				y1=Sgy2Data[i];
				y2=Sgy2Data[i+1];
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				if(sgYMaxLim<Sgy2Data[i] && sgYMaxLim>Sgy2Data[i+1]){
					x1 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i])+Sgx2Data[i];
					y1 = sgYMaxLim;
				}
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				else if(sgYMinLim>Sgy2Data[i] && sgYMinLim<Sgy2Data[i+1]){
					x1 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMinLim-Sgy2Data[i+1])+Sgx2Data[i+1];
					y1 = sgYMinLim;
				}				
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				if(sgYMaxLim<Sgy2Data[i+1] && sgYMaxLim>Sgy2Data[i]){
					x2 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i+1])+Sgx2Data[i+1];
					y2 = sgYMaxLim;
				}
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				else if(sgYMinLim>Sgy2Data[i+1] && sgYMinLim<Sgy2Data[i]){
					x2 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMinLim-Sgy2Data[i])+Sgx2Data[i];
					y2 = sgYMinLim;
				}	
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				
				if((sgYMinLim>Sgy2Data[i+1] && sgYMinLim>Sgy2Data[i])||(sgYMaxLim<Sgy2Data[i+1] && sgYMaxLim<Sgy2Data[i])){
					x1=-10000;
					x2=-10000;
					y1=-10000;
					y2=-10000;
				}
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				
				g.setColor(Color.GREEN);
				g.drawLine(scx(x1, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y1, sgyMaxAdj, sgyMinAdj, yAdjCon), scx(x2, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y2, sgyMaxAdj, sgyMinAdj, yAdjCon));
				//g.drawLine(scx(Sgx2Data[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(Sgy2Data[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx(Sgx2Data[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(Sgy2Data[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
				//g.drawLine(scx(10*i+900, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(10*i+900, sgyMaxAdj, sgyMinAdj, yAdjCon), scx(i+900+10, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(i+900+10, sgyMaxAdj, sgyMinAdj, yAdjCon));
				/*if(sg2Use==1){
					g.setColor(Color.GREEN);
					//g.drawLine(scx(Sgx2Data[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(Sgy2Data[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx(Sgx2Data[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(Sgy2Data[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
					g.drawLine(scx(10*i+900, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(10*i+900, sgyMaxAdj, sgyMinAdj, yAdjCon), scx(i+900+10, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(i+900+10, sgyMaxAdj, sgyMinAdj, yAdjCon));
				}*/
			}
			sg2Use=0;
			}catch(Exception e){
				System.out.println("error in simmanual sgraph");
			}
		}
		
		void calcGraphIntv(double MaxLim, double MinLim){
			int dummy=0;
			sepXIntv = 1;
			sepYIntv = 1;
			
			if(MainDriver.spMin1>55){
				dummy=0;
			}
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
			
			if(sgYMax>MaxLim) sgYMax = MaxLim;
			if(sgYMin<MinLim) sgYMin = MinLim;
			
			if(MainDriver.iCaseSelection==1){ //140716 AJW 케이스 선택 후 SimManual로 들어갈 때 값들이 적어서 Intv가 0으로 계속 나와서 무한루프를 도는 것을 방지.
						
		    while(dummy==0&&sepYIntv!=0 && sgYMax!=sgYMin){// && sgYMin!=0){		    
		    	if((sgYMax-sgYMin)/sepYIntv>=1 && (sgYMax-sgYMin)/sepYIntv<=10) dummy=1;
		    	else if((sgYMax-sgYMin)/sepYIntv<=0) dummy=1;
		    	else if((sgYMax-sgYMin)/sepYIntv<4) sepYIntv = sepYIntv/10;		    	
		    	else sepYIntv=sepYIntv*10;		    	
		    }		      
		    
		    while(dummy==1 && sgYMax!=sgYMin){
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
				sgx[i].setText(Double.toString((int)((sgxMinAdj+i*sepXIntv)*2)));
				sgx[i].setFont(new Font("굴림", Font.BOLD, 10));
				sgx[i].setHorizontalAlignment(SwingConstants.CENTER);
				
				add(sgx[i]);
				}
			for(int i=0; i<sepYnum+1; i++){
				temp = (int)((sgyMinAdj+i*sepYIntv)*2);
				temp = temp/2;
				if(temp>100){
					temp_int = (int)temp;
					sgy[i].setText(Integer.toString(temp_int));
				}
				else{
					sgy[i].setText(Double.toString(temp));
				}
				sgy[i].setFont(new Font("굴림", Font.BOLD, 10));
				sgy[i].setHorizontalAlignment(SwingConstants.CENTER);
				add(sgy[i]);
				}
			if(sgYMax>sgyMaxAdj) sgyMaxAdj = temp;
			
			dummy=0;
			}
			else{ // normal 140716 AJW
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
			    
			    while(dummy==0 && sgXMax!=0){
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
			    
			    while(dummy==1 && sgYMax!=0){
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
					temp = (int)((sgyMinAdj+i*sepYIntv)*2);
					temp = temp/2;
					if(temp>100){
						temp_int = (int)temp;
						sgy[i].setText(Integer.toString(temp_int));
					}
					else{
						sgy[i].setText(Double.toString(temp));
					}
					sgy[i].setFont(new Font("굴림", Font.BOLD, 10));
					sgy[i].setHorizontalAlignment(SwingConstants.CENTER);
					add(sgy[i]);
					}
				dummy=0;
			}
		}
	}	
	
	int scx2(double x, double Max, double Min, double intv){//intv = GraphSize-sepnum*sepintv
		int result = 0;		
		result = (int)((x-Min)/(Max-Min)*(GraphXsize-intv)+GraphXLoc);		
		return result;
	}
	
	int scy2(double y, double Max, double Min, double intv){
		int result = 0;
		result = (int)((GraphYsize2-intv)-(y-Min)/(Max-Min)*(GraphYsize2-intv));
		result = result+GraphYLoc;//+15; //15=label font size
		return result;
	}
	
	class Sgraph2 extends JPanel{
		int ColumnCount = 0;
		int RowCount = 0;
	    int ColumnLabelCount = 0;
	    String ColumnLabel = "";
	    int Column =0, Row=0;	    
	    double sgXMax=1, sgXMin=1, sgYMax=1, sgYMin=1;
	    double sgYMaxLim=1, sgYMinLim=0;
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
	    double x1=0, x2=0, y1=0, y2=0;
	    
	    JLabel lblTitle;
	    JLabel[] sgx = new JLabel[15]; // x-axis number tag
	    JLabel[] sgy = new JLabel[15]; // y-axis number tag
	    
		Sgraph2(){
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
			try{
			super.paint(g);
			if(sg2Use==1) {
				dummy=1;
			}
			Graphics2D g2d = (Graphics2D)g;
			int drawwidth = 1;
			g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 
			g.setColor(Color.BLACK);
			int intvX = GraphXsize/sepXnum;
			int intvY = GraphYsize2/sepYnum;
			int xAdjCon=0, yAdjCon=0;
			
			for(int i=0; i<15; i++){
				sgx[i].setVisible(false);
				sgy[i].setVisible(false);
			}
			for(int i=0; i<GraphYsize2+1; i=i+intvY){				
				g.drawLine(GraphXLoc-3, GraphYLoc+i, GraphXLoc+GraphXsize, GraphYLoc+i);//5 = the half vertical length of label. 11/2				
				int x=(int)(sepYnum-i/intvY); 
				if(filter == 1 && x>=0){
					sgy[x].setBounds(GraphXLoc-35, GraphYLoc+i-5, 35, 11);
					sgy[x].setVisible(true);
				}
			}
			
			for(int i=0; i<GraphXsize+1; i=i+intvX){
				g.drawLine(GraphXLoc+i, GraphYLoc, GraphXLoc+i, GraphYLoc+GraphYsize2+3);
				int x=i/intvX;
				if(filter == 1 && x<=sepXnum){
					sgx[x].setBounds(GraphXLoc+i-18, GraphYLoc+GraphYsize2+5, 35, 11);
					sgx[x].setVisible(true);
				}
			}
			
			xAdjCon = GraphXsize - sepXnum*intvX;
			yAdjCon = GraphYsize2 - sepYnum*intvY;
			double dummy2=0;
			drawwidth = 3;
			g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 			
			for(int i=0; i<RowCount-1; i++){ // TOTAL NUMBER OF POINT = Rowcount , so the number of line is equal to RowCount-1				
				x1=SgxData[i];
				x2=SgxData[i+1];
				y1=SgyData[i];
				y2=SgyData[i+1];
				
				if(sgYMaxLim<SgyData[i] && sgYMaxLim>SgyData[i+1]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x1 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMaxLim-SgyData[i])+SgxData[i];
					y1 = sgYMaxLim;
				}
				else if(sgYMinLim>SgyData[i] && sgYMinLim<SgyData[i+1]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x1 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMinLim-SgyData[i+1])+SgxData[i+1];
					y1 = sgYMinLim;
				}
				
				if(sgYMaxLim<SgyData[i+1] && sgYMaxLim>SgyData[i]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x2 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMaxLim-SgyData[i+1])+SgxData[i+1];
					y2 = sgYMaxLim;
				}
				else if(sgYMinLim>SgyData[i+1] && sgYMinLim<SgyData[i]){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x2 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMinLim-SgyData[i])+SgxData[i];
					y2 = sgYMinLim;
				}
				
				if((sgYMinLim>SgyData[i+1] && sgYMinLim>SgyData[i])||(sgYMaxLim<SgyData[i+1] && sgYMaxLim<SgyData[i])){
					if(sgYMaxLim==1000){
						dummy2=1;
					}
					x1=-10000;
					x2=-10000;
					y1=-10000;
					y2=-10000;
				}
				g.setColor(Color.RED);
				double ex1=0, ex2=0;
				ex1=scx2(x1, sgxMaxAdj, sgxMinAdj, xAdjCon);
				ex2=scx2(x2, sgxMaxAdj, sgxMinAdj, xAdjCon);
				if(ex1>ex2){
					dummy=1;
				}
				
				g.drawLine(scx2(x1, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(y1, sgyMaxAdj, sgyMinAdj, yAdjCon), scx2(x2, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(y2, sgyMaxAdj, sgyMinAdj, yAdjCon));
				//g.drawLine(scx2(SgxData[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(SgyData[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx2(SgxData[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(SgyData[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
				
				x1=Sgx2Data[i];
				x2=Sgx2Data[i+1];
				y1=Sgy2Data[i];
				y2=Sgy2Data[i+1];
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				if(sgYMaxLim<Sgy2Data[i] && sgYMaxLim>Sgy2Data[i+1]){
					x1 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i])+Sgx2Data[i];
					y1 = sgYMaxLim;
				}
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				else if(sgYMinLim>Sgy2Data[i] && sgYMinLim<Sgy2Data[i+1]){
					x1 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMinLim-Sgy2Data[i+1])+Sgx2Data[i+1];
					y1 = sgYMinLim;
				}				
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				if(sgYMaxLim<Sgy2Data[i+1] && sgYMaxLim>Sgy2Data[i]){
					x2 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i+1])+Sgx2Data[i+1];
					y2 = sgYMaxLim;
				}
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				else if(sgYMinLim>Sgy2Data[i+1] && sgYMinLim<Sgy2Data[i]){
					x2 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMinLim-Sgy2Data[i])+Sgx2Data[i];
					y2 = sgYMinLim;
				}	
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				
				if((sgYMinLim>Sgy2Data[i+1] && sgYMinLim>Sgy2Data[i])||(sgYMaxLim<Sgy2Data[i+1] && sgYMaxLim<Sgy2Data[i])){
					x1=-10000;
					x2=-10000;
					y1=-10000;
					y2=-10000;
				}
				if(x1>x2+0.1 && sgYMaxLim==2000){
					dummy2=1;
				}
				
				g.setColor(Color.GREEN);
				g.drawLine(scx2(x1, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(y1, sgyMaxAdj, sgyMinAdj, yAdjCon), scx2(x2, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(y2, sgyMaxAdj, sgyMinAdj, yAdjCon));
				//g.drawLine(scx2(Sgx2Data[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(Sgy2Data[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx2(Sgx2Data[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(Sgy2Data[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
				//g.drawLine(scx2(10*i+900, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(10*i+900, sgyMaxAdj, sgyMinAdj, yAdjCon), scx2(i+900+10, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(i+900+10, sgyMaxAdj, sgyMinAdj, yAdjCon));
				/*if(sg2Use==1){
					g.setColor(Color.GREEN);
					//g.drawLine(scx2(Sgx2Data[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(Sgy2Data[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx2(Sgx2Data[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(Sgy2Data[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));
					g.drawLine(scx2(10*i+900, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(10*i+900, sgyMaxAdj, sgyMinAdj, yAdjCon), scx2(i+900+10, sgxMaxAdj, sgxMinAdj, xAdjCon), scy2(i+900+10, sgyMaxAdj, sgyMinAdj, yAdjCon));
				}*/
			}
			sg2Use=0;
			}catch(Exception e){
				System.out.println("error in simmanual sgraph");
			}
		}
		
		void calcGraphIntv(double MaxLim, double MinLim){
			int dummy=0;	
			
			if(MainDriver.spMin1>55){
				dummy=0;
			}
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
			
			if(sgYMax>MaxLim) sgYMax = MaxLim;
			if(sgYMin<MinLim) sgYMin = MinLim;
			
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
	
	void Form_Load(){ // assign SI or Continuous SI data
		
		int j, ic;
		double[] mldelvd = new double[3];
		double delMD=0, delVD=0, angTop=0;
		
		//txtSIMmode.Text = "Currently choke is controlled by Manual mode"
	    //SimAuto.WindowState = 2; TimerCirculation.Enabled = False
		
		pumpFailure1=0;
		pumpFailure2=0;
				
		TimerRPOn=0;
		MainDriver.RPMenuSelected=2;
		
		MainDriver.imode = 1;
		MainDriver.iCHKcontrol = 2;
	    MainDriver.iWshow = 0;     //temporary hide the wellbore
	    operationMsg.setVisible(true);
	//---------- assign the final SI conditions from 2-phase calculation
	    X = MainDriver.Xnd[MainDriver.N2phase-1];
	    MainDriver.Xb = MainDriver.Xnd[0];
	    xVert = MainDriver.xTop[MainDriver.NpSi];
	    xbVert = MainDriver.xBot[MainDriver.NpSi]; 
	    px = MainDriver.pxTop[MainDriver.NpSi];
	    PVgain = MainDriver.Vpit[MainDriver.NpSi];
	    Pchoke = MainDriver.Pchk[MainDriver.NpSi]; 
	    Pcasing = MainDriver.Pcsg[MainDriver.NpSi]; 
	    Pbeff = MainDriver.Pb2p[MainDriver.NpSi];
	    SPP = MainDriver.Psp[MainDriver.NpSi]; 
	    pumpP = MainDriver.Ppump[MainDriver.NpSi];
	    Stroke1 = MainDriver.StrokePump1[MainDriver.NpSi];
	    Stroke2 = MainDriver.StrokePump2[MainDriver.NpSi];
	    TotalStroke = MainDriver.Stroke[MainDriver.NpSi];
	   // gasDen = MainDriver.rhoK[MainDriver.NpSi]; //20131121 removed in Manual Mode, 131121 AJW
	   // Tdelay = MainDriver.TTsec[MainDriver.NpSi]; //20131121 removed in Manual Mode, 131121 AJW
	    
	    txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));
		txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));
		
		MainDriver.NumQcapeq=0;
		MainDriver.initspMin1=MainDriver.spMin1;
		MainDriver.initspMin2=MainDriver.spMin2;
		MainDriver.Qcapeq[0] = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2);
		MainDriver.KillVolChange[0]=0;
		MainDriver.QKillChange[0] = MainDriver.Qkill;		
		QcapacityEq = (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)/(MainDriver.spMin1+MainDriver.spMin2); //20140212 ajw
		
	    //MainDriver.volPump = 0; //removed by jw, 140205 
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
	    MudGpnl.repaint(); //Call DrawMudGauge(1, 0)    //return rate of mixture
	    tmpintCHK = Pchoke - psia;
	    CHKGpnl.repaint(); //Call DrawMudGauge(2, Pchoke - psia)    //return rate of mixture
	    tmpintSDP = SPP -psia;
	    SDPGpnl.repaint(); //Call DrawMudGauge(3, SPP - psia)    //return rate of mixture
	    tmpintPumpP = pumpP -psia;
	    PumpPGpnl.repaint(); //Call DrawMudGauge(3, SPP - psia)    //return rate of mixture
	//--------- set the graph maximun
	    cmdPumpKill.setVisible(true);
	    cmdFixPump.setVisible(false);
	    MainDriver.iWshow = 0;
	    m3.setVisible(false);
	    	    
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
	    pnlKMW.setVisible(true); // 140707 AJW
	    //SDPGpnl.setVisible(false);
	    //SimAccPnl.setVisible(false);
	    cmdPumpKill.setVisible(false);
	    cmdFixPump.setVisible(false);
	    PnlPumpRate.setVisible(false);
	    //CHKPnl.setVisible(false);
	    if(MainDriver.iProblem[3]==0) txtKMW.setText((new DecimalFormat("##.##")).format((double)MainDriver.oMud));
	    else txtKMW.setText((new DecimalFormat("##.##")).format((double)MainDriver.oMud_save));
	    MainDriver.KMWsettingOn=0;
	    timeStart= 0;
	    setVisible(true);
	    
	    if(MainDriver.iCHKcontrol2==1){
	    	AutoSelect.setSelected(true);
	    	MainDriver.iCHKcontrol = 1;
	    	MainDriver.imode = 2;
	    }
	    else{
	    	ManualSelect.setSelected(true);
	    	MainDriver.iCHKcontrol = 2;
	    	MainDriver.imode = 1;
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
	    operationMsg.setVisible(false);
	}

	class PumpPGaugePanel extends JPanel{//15, 230,150,150
		double GaugeValue=0;	
		double DrawAngle=0, Xcenter=0, Ycenter=0, Xoriginal=0;
		double Xcalculated =0;
		double Ycalculated =0;
		double RAD_CONVERT = 3.141592 / 180;
		
		PumpPGaugePanel(){
			this.setBackground(new Color(240,240,240));
		}
		
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.BLACK);
			GaugeValue=tmpintPumpP;
			DrawAngle = 90 * GaugeValue / 1000 * RAD_CONVERT;
			Xcenter = panSizeX/2;
            Ycenter = (panSizeY+labelFontSize)/2;
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
            Ycenter = (panSizeY+labelFontSize)/2;
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
            Ycenter = (panSizeY+labelFontSize)/2;
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
            Ycenter = (panSizeY+labelFontSize)/2;
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