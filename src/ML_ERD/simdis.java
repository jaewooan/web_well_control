package ML_ERD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Scrollbar;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
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
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;


class simdis extends JFrame {

	static int IPmax = 2020;
	static int iRotary=0, iPGwarn=0, iPenet=0;
	static int iStable=0;
	static int iSound=0, SIcount=0, nHorizon=0;
	static int nSlip=0, timePumpOff=0;

	//
	static double DrotTab=0, HrotTab=0;
	static double vdBit=utilityModule.getVD(MainDriver.TMD[MainDriver.NwcS-1]-MainDriver.mdBitOff);
	static double hdBit=0;
	static double mdBit=0;
	static double AeobRad=0, psia=14.7;
	static double ROP=0, Qcirc=0, Qt=0, PVgain=0, pumpP=0, standpipeP=0, Pcasing=0, Pbeff=0, Pchoke=0;
	static double kickVDbottom=0;    //bottom of the kick in vertical depth, ft
	static double xNull=0, P2form=0, Pkick=0, holeMD=0, QtotVol=0, Qgas=0, QgDay=0, PcsgSI=0;
	static double gasVis=0, gasDen=0, zz=0;
	static double HgCal=0, kickMD=0, kickVD=0, kickLoc=0, QgTotMscf=0, QgOldMscf=0, surfTen=0;
	static double vSlip=0, dxSlip=0, v2Hold=0;
	static double xDrill=0;
	static double[] Tpenet = new double[IPmax];
	static double[] Hpenet = new double[IPmax];
	static double[] volGtmp = new double[IPmax];
	static double[] volLtmp = new double[IPmax];
	static double[] gortmp = new double[IPmax];
	static double Pdiff=0;
	static int[] pmoveTop = new int[15];
	static int[] pmoveLeft = new int[15];
	//
	//---------------------------- additional variable after May, 1999., 7/12/02
	static double Cgi=0;    //1/psi, initial gas compressibility
	static int iROP =0;  //ratation (1), no rotation(0)
	static int iPumpOn1 =0, iPumpOn2 =0;  //with pump on(1), pump off(0)
	static int timeNp=0;   //to save output file
	static int iShowData=0;  //to save and open shutin data for multiple runs
	static int iTimerWarn=0, iTimerDrill=0, iTimerShutin=0;
	static int iTimerTrip=0, iTimerStrip=0;
	static String fileSID ="";
	static int iShutinData=0;  //to save and open shutin data file (1), otherwise(0)
	static double mudLineP =0, mudFlowOut=0, gasFlowOut=0; //add mudflowout, gasflowout again 
	static int iDataChange=0;   //to check input data change after we retrive shut in data
	//---------------------------- additional variable after Jan. 2003 for ML & ERD
	static int iTripAuto =0;   //connection type; auto(0)/manual(1)
	static int iRunTrip =0;    //run trip (1)/ pause trip (0)
	static int iTripCon =0;    //control variable for connection time simulation; during connection (-99)
	static int iTripPOH =0;    //control parameter for surge(1) and swab(-1)
	//         array variables for plotting purpose
	static int nDataPlot =0;   //total number of data points for plotting purpose
	static double[] arrayTTsec = new double[IPmax];
	static double[] arrayDPss = new double[IPmax];
	static double[] arrayVel = new double[IPmax];
	static double[] arrayAcc = new double[IPmax];
	static double[] arrayPcsg = new double[IPmax];
	static double[] arrayPbeff = new double[IPmax];
	static int iTripRatio =0;
	static int ConnTimeDelay =0;  //connection time delay for manual control
	static double Ktrip=0, VtripOld=0, VtripMax=0;        //ft/s, Vtrip, dirll string trip velocity
	static double totalTrip=0, delTrip=0;     //for trip length calculation  2003/7/12
	static double standTrip=0; //, standTripOld
	static double standTime=0, standTimeOld=0;   //time duration for stand trip, sec
	static double standLength=0;
	static double delDSvolTrip=0;   //DS volume removed; tripped out(+)
	static double accTrip=0;   //ft/s^2, drill string acceleration
	static double curVel=0, curVelOld=0, curAcc=0;  //current trip acc. and vel.
	static double surgeRate=0, surgeFrac=0;   //surge flow rate and surge fraction through annulus
	static double DPssCasing=0, DPaccCasing=0;//DP due to trip to calc. casing seat pressure
	static double VOLinTripTank=0;  //remaining volume in the trip tank
	static double mlVolume=0;   //bbls, ML volume to calculate p. buildup
	static double mlPVgain=0;   //bbls, pit volume gain in ML
	static double PVgainOld=0, mlPVgainOld=0;
	static double mlQgTotMscf=0;   //Mscf, total kick influx from MLs
	static double mlQgDay=0;             //total influx rate in Mscf/day
	static double DPincML=0;       //psi, p. increase during buildup
	static double DPmudDrop=0;    //psi, DP drop due to improper well fillup
	static double VOLdrop =0;      //bbls, total volume of mud dropped
	static double DPbleed =0;      //well pressure increase by snub/ stripping
	static double fluxTimeSec =0;    //time for gas kick flow
	static double DPsafety=0;  //for min. pressure increase to prevent additional kick

	//Added by Ty
	static double tbottom =0;
	static double[] deltaT = new double[IPmax];

	//20130812 ajw
	static int TimeDrlIntv=0;	

	Timer TimerDrill;
	Timer TimerSI;
	Timer TimerWarn;

	private JPanel contentPane;
	static JTextArea lblPause = new JTextArea("Operation", 1, 100);	

	static int TimerDrillOn = 0;
	static int TimerSIOn = 0;
	static int TimerWarnOn = 0;
	int TimerIPOn=0;
	int TimerBOOn = 0;
	int TimerRPOn=0;

	static int SITaskOn=1;
	static int TDTaskOn=1;
	int TDTaskFinishedIndex = 1;//finish =0, start 1
	static int WarnTaskOn=1;
	int DrillbtnOn=0;
	int PumpbtnOn=0;



	//20130813 ajw
	static double tmpintMud=0, tmpintCHK=0, tmpintSDP=0, tmpintPumpP =0; //  it is made to save the GaugeValue in DrawMudGauge Function in VB. 

	//20130814
	//static wellpic Sm1.m3 = new wellpic();
	static blowout m4 = new blowout();
	static WellTrajt m5 = new WellTrajt();

	MudGaugePanel pan1 = new MudGaugePanel();
	CHKGaugePanel pan2 = new CHKGaugePanel();
	SDPGaugePanel pan3 = new SDPGaugePanel();
	BlowOutPanel BlowOutPnl = new BlowOutPanel();

	JButton btnSrtSim = new JButton("Start Simulation");
	JButton btnPumpOn1 = new JButton("Pump on");
	JButton btnPumpOn2 = new JButton("Pump on");
	JButton btnDrillOn = new JButton("Start Bit Rotation");
	JButton btnDrillOff = new JButton("Stop Bit Rotation");
	JButton btnPumpOff1 = new JButton("Pump Off");
	JButton btnPumpOff2 = new JButton("Pump Off");
	JButton btnShutIn = new JButton("Shut in the Well");
	JButton cmdKill = new JButton("Kill the Well");
	private JLabel SimAccelRatio;
	private final JLabel DrillingInfo = new JLabel("Drilling Information");
	private final JLabel TotElapsedTm = new JLabel("Total Elapsed Time");
	private final JLabel lblhrMin = new JLabel("(hr : min : sec)");
	private final JLabel lblMudComp = new JLabel("Mud Compressibility is considered !!");
	private final JLabel lblDepthMud = new JLabel("Depth & Mud Information");
	private final JTextField txtVDbit = new JTextField();
	private final JTextField txtHDbit = new JTextField();
	private final JTextField txtROP = new JTextField();
	private final JTextField txtPumpRate = new JTextField();
	private final JTextField txtReturnRate = new JTextField();
	private final JTextField txtGasRate = new JTextField();
	private final JTextField txtPitGain = new JTextField();
	private final JTextField txtKillMudWt = new JTextField();
	private final JLabel lblPressureInfromation = new JLabel("Pressure Infromation");
	private final JTextField txtTime = new JTextField();
	private final JTextField txtFormationP = new JTextField();
	private final JTextField txtBHP = new JTextField();
	private final JTextField txtCasingP = new JTextField();
	private final JTextField txtMudLine = new JTextField();
	private final JTextField txtChokeP = new JTextField();
	private final JTextField txtSPP = new JTextField();
	private final JTextField txtPumpP = new JTextField();
	private final JLabel lblDMI1 = new JLabel("ft,  True Verti. Depth of the BIT");
	private final JLabel lblMDI2 = new JLabel("ft,  Horiz. Displacement of the BIT");
	private final JLabel lblMDI3 = new JLabel("ft/hr,  Rate of Penetration(ROP)");
	private final JLabel lblBDI4 = new JLabel("gal/min,  Pump Rate");
	private final JLabel lblMDI5 = new JLabel("gal/min,  Return Mud Rate");
	private final JLabel lblGasReturnRate = new JLabel("Mscf/day,  Gas Return Rate");
	private final JLabel lblMDI7 = new JLabel("bbls,  Pit Volume Gain");
	private final JLabel lblMDI8 = new JLabel("Pit Gain Warning Sign");
	private final JButton btnWarn = new JButton("Sound Off");
	private final JLabel lblDMI9 = new JLabel("ppg,  Kill Mud Weight");

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

	JLabel lblTimesFasterThan = new JLabel("  Times Faster Than Real Time(1 to 10)");
	JSlider slider = new JSlider();
	JTextField txtSimRate = new JTextField();
	JTextField txtRateDiff = new JTextField();
	JTextField txtPchoke = new JTextField();
	JTextField txtPsp = new JTextField();
	JTextField txtPp = new JTextField();
	JPanel InformationPane = new JPanel();
	private final JPanel ChokeControlPan = new JPanel();
	private final JLabel lblChokeControlMethod = new JLabel("Choke Control Method");
	JPanel ChokeKillLines = new JPanel();
	JRadioButton optAutomatic = new JRadioButton("Automatic (by the computer)");
	JRadioButton optManual = new JRadioButton("Manual (by the user)");

	JRadioButton optChoke = new JRadioButton("Choke Line Only");
	JRadioButton optKill = new JRadioButton("Kill Line Only");
	JRadioButton optBoth = new JRadioButton("Both Lines");

	ButtonGroup ChokeControlGroup = new ButtonGroup();
	ButtonGroup ChokeKillGroup = new ButtonGroup();


	int btnSizeX = 130, btnSizeY = 25;
	int panIntv = 10;
	int panSizeX = 200, panSizeY = 180, pan1Xsrt = 15, pan1Ysrt=10+btnSizeY+panIntv, yintv=10;
	int simAccPnlSizeX = 2*btnSizeX+panIntv, simAccPnlSizeY = 75;	
	int txtIntvX=12, txtSrtX = 5, txtSrtY1=38, txtSizeX = 70, txtSizeY=18, txtIntvY=5;
	int txtLblXsize=194, txtLblYsize = txtSizeY;
	int labelFontSize=11, labelFontSize2=11, RateDiffSize=11;
	int ovalIntv =5, LineLength=10, Radius=45;
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenuItem mItmPause = new JMenuItem("Pause");
	private final JMenuItem mItmConti = new JMenuItem("Continue");
	private final JMenuItem mItmHelp = new JMenuItem("Helps");
	private final JMenu mFiles = new JMenu("Files        ");
	private final JMenu mMenus = new JMenu("Menus      ");
	private final JMenuItem menuShowData = new JMenuItem("Show Input Data");
	private final JMenuItem menuSaveSID = new JMenuItem("Save Shutin Data");
	private final JMenuItem menuOpenSID = new JMenuItem("Open Shutin Data");
	private final JMenuItem mBacktoMain = new JMenuItem("Back to Main Menu");
	private final JMenu mClrSelect = new JMenu("Color Selection");
	private final JMenuItem ClrKick = new JMenuItem("Kick");
	private final JMenuItem ClrKill = new JMenuItem("Kill Mud");
	private final JMenuItem ClrMudInUse = new JMenuItem("Mud in Use");
	private final JMenuItem clrWb = new JMenuItem("Wellbore Background");
	private final JMenuItem mSound = new JMenuItem("Sound On/Off");
	private final JMenuItem mSrtSim = new JMenuItem("Start Simuliaton");
	private final JMenu mWellbore = new JMenu("Wellbore");
	private final JMenuItem mWbTraject = new JMenuItem("Trajectory");
	private final JMenuItem mWbHide = new JMenuItem("Hide");
	private final JMenuItem mWbShow = new JMenuItem("Show");
	JButton cmdOk = new JButton("Kick Circ. Out");

	static SimAuto Sa1;
	static SimManual Sm1;

	JTextArea txtN2phase = new JTextArea();

	JFrame colorPal = new JFrame("Color");
	JColorChooser Colorchooser;
	Color SelectedColor = Color.red;

	textResult txtR;
	textLoad txtL;
	int dummy=0;

	PumpGaugePanel pan4 = new PumpGaugePanel();
	private final JLabel lblPumpStk = new JLabel("Pump Stroke: ");
	private final JTextField txtStks1 = new JTextField();
	private final JTextField txtStks2 = new JTextField();
	private final JLabel lblPumpStk2 = new JLabel("Strokes");
	int pnlPumpRateSizeX = 2*btnSizeX+panIntv;

	int lblSelMudPumpSrtY =5; 
	int radioBtnSizeX = 85;
	int radioBtnSizeY = 23;	
	int txtPumpRSizeX = 50;
	int txtPumpRSizeY = 18;
	int ScrollSizeX = pnlPumpRateSizeX-lblSelMudPumpSrtY*2;
	//int pnlPumpRateSizeY = lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*3+20;	
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
	JTextField txtPumpStatus1 = new JTextField();
	JTextField txtPumpStatus2 = new JTextField();
	JLabel lblPumpRate2 = new JLabel("SPM");
	JButton btnSetStkZero1 = new JButton("Set stroke zero");
	JButton btnSetStkZero2 = new JButton("Set stroke zero");
	private final JMenu menuHelps = new JMenu("Helps      ");
	private final JMenu menuPause = new JMenu("Pause / Continue");
	private final JMenu menuContinue = new JMenu("Continue");

	static double Stroke1=0;
	static double Stroke2=0;
	static double TotalStroke=0;
	Timer TimerCheckIP;
	Timer TimerCheckBO;
	Timer TimerCheckRP;

	inputData ip;
	resultPlot rp;
	private final JMenuItem mntmResultsInPlot = new JMenuItem("Results in Plot");

	static int SIDTimerWarn = 0;
	static int SIDTimerDrill = 0;
	static int SIDTimerShutin = 0;
	static int SIDSm1TRTaskOn = 0;
	static int SIDSa1TRTaskOn = 0;


	//
	double volfree = 0;
	double volsol = 0;
	double volsolsc = 0;
	double volliquid = 0;
	double PVTZ_Gas_ex=0;
	double PVTZ_free_ex=0;
	double PVTZ_sol_ex=0;
	double Dendiffex=0;

	//
	JPanel pnlControl = new JPanel();
	JLabel lblControl = new JLabel("Control Panel");
	JLabel lblRPM = new JLabel("Rotation Speed:");
	JTextField txtRPM_control = new JTextField();
	JLabel lblRPM2 = new JLabel("RPM");
	JScrollBar RPMScroll = new JScrollBar();

	JLabel lblWOB = new JLabel("Weight On Bit:");
	JLabel lblWOB2 = new JLabel("kips");
	JTextField txtWOB = new JTextField();
	JScrollBar WOBScroll = new JScrollBar();

	JSeparator sepPnlCont1 = new JSeparator();
	JSeparator sepPnlCont2 = new JSeparator();

	int RPM_min = 0;
	int RPM_max = 310;
	int WOB_min = 0;
	int WOB_max = 210;

	String msgSymptom="";
	String[] msgSol = new String[3];
	Random random;
	int seed = 0;
	static int iBtm = 0;
	static int i_msg = 0;
	
	JMenu mSolution = new JMenu("Drilling Problems and Solutions");
	JMenuItem mSolution1 = new JMenuItem("Solve Mechanical Pipe Stuck");
	JMenuItem mSolution2 = new JMenuItem("Solve Differential Pipe Stuck");
	JMenuItem mSolution3 = new JMenuItem("Solve Lost Circulation");
	
	double solCumTime = 0;
	
	simdis() {
		setTitle("SNU Well Control Simulator: Drilling, Kicking, Kick Detection, and Kick Confinement");
		TimerDrillOn = 0;
		TimerSIOn = 0;
		MainDriver.SItaskOn2=0;
		TimerWarnOn = 0;

		SITaskOn=1;
		TDTaskOn=1;
		TDTaskFinishedIndex = 1;//finish =0, start 1
		WarnTaskOn=1;
		DrillbtnOn=0;
		PumpbtnOn=0;
		MainDriver.iWshow=0;
		txtL = new textLoad("Load");
		MainDriver.reset_Stroke1=0;
		MainDriver.reset_Stroke2=0;

		//MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;		

		if(MainDriver.iHuschel==1){
			MainDriver.Qdrill = 530;
		}
		else if(MainDriver.iHuschel==2){
			MainDriver.Qdrill = 450;
		}
		else{
			MainDriver.Qdrill = 337.088811730091; //ok
		}

		pumpScroll1.setMaximum(150);
		pumpScroll2.setMaximum(150);
		pumpScroll1.setValue(0);
		pumpScroll2.setValue(0);
		txtPumpR1.setBackground(Color.YELLOW);
		txtPumpR2.setBackground(Color.YELLOW);
		txtPumpR1.setText((new DecimalFormat("##0")).format(0));
		txtPumpR2.setText((new DecimalFormat("##0")).format(0));
		txtPumpR1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPumpR2.setHorizontalAlignment(SwingConstants.RIGHT);

		pumpScroll1.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				MainDriver.spMinD1 = pumpScroll1.getValue();
				txtPumpR1.setText((new DecimalFormat("###0")).format(MainDriver.spMinD1));
				if(MainDriver.iHuschel==1){
					MainDriver.Qdrill = 530;
				}
				else if(MainDriver.iHuschel==2){
					MainDriver.Qdrill = 450;
				}
				else{
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
				}				
				if(MainDriver.i_ROPVERSION==2 && MainDriver.gTcum>0){
					MainDriver.ROPen = propertyModule.calcROP(MainDriver.RPM_now, Pbeff, MainDriver.WOB_now, MainDriver.Qdrill, vdBit); // 1) ROP 갱신
				}				
				//MainDriver.Qdrill = 337.088811730091; //히히
			}
		});

		pumpScroll2.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				MainDriver.spMinD2 = pumpScroll2.getValue();
				txtPumpR2.setText((new DecimalFormat("###0")).format(MainDriver.spMinD2));
				if(MainDriver.iHuschel==1){
					MainDriver.Qdrill = 530;
				}
				else if(MainDriver.iHuschel==2){
					MainDriver.Qdrill = 450;
				}
				else{
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
				}
				if(MainDriver.i_ROPVERSION==2 && MainDriver.gTcum>0){
					MainDriver.ROPen = propertyModule.calcROP(MainDriver.RPM_now, Pbeff, MainDriver.WOB_now, MainDriver.Qdrill, vdBit); // 1) ROP 갱신
				}
			}
		});

		RPMScroll.setMaximum(RPM_max);
		WOBScroll.setMaximum(WOB_max);

		RPMScroll.setValue(0);
		WOBScroll.setValue(0);

		RPMScroll.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				MainDriver.RPM_now = RPMScroll.getValue();
				txtRPM_control.setText((new DecimalFormat("##0")).format(MainDriver.RPM_now));
				if(MainDriver.i_ROPVERSION==2 && MainDriver.gTcum>0){
					MainDriver.ROPen = propertyModule.calcROP(MainDriver.RPM_now, Pbeff, MainDriver.WOB_now, Qcirc, vdBit); // 1) ROP 갱신
				}

				//if(MainDriver.Qdrill<100 && MainDriver.gTcum>0){
				//	if(TDTaskOn==1 || SITaskOn==1) MainDriver.iProblem = 2;
				//}//나주엥 problem 적용!
			}
		});


		WOBScroll.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				MainDriver.WOB_now = WOBScroll.getValue();
				txtWOB.setText((new DecimalFormat("##0")).format(MainDriver.WOB_now));

				if(MainDriver.i_ROPVERSION==2 && MainDriver.gTcum>0){
					MainDriver.ROPen = propertyModule.calcROP(MainDriver.RPM_now, Pbeff, MainDriver.WOB_now, Qcirc, vdBit); // 1) ROP 갱신
				}

				//if(MainDriver.Qdrill<100 && MainDriver.gTcum>0){
				//	if(TDTaskOn==1 || SITaskOn==1) MainDriver.iProblem = 2;
				//}//나주엥 problem 적용!
			}
		});

		MainDriver.RPM_now  = MainDriver.RPM_Base;
		MainDriver.WOB_now  = MainDriver.WOB_Base;
		MainDriver.Torque_now  = MainDriver.Torque_Base;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				e.getWindow().dispose();
				e.getWindow().setVisible(false);
				menuClose();
				MainDriver.iWshow = 0;                
			}
		});		

		UIManager.put ("ColorChooser.nameText", "Color");
		UIManager.put ("ColorChooser.okText", "OK");
		UIManager.put ("ColorChooser.cancelText", "Cancel");
		UIManager.put ("ColorChooser.resetText", "Reset");
		UIManager.put ("ColorChooser.previewText", "Preview");
		UIManager.put ("ColorChooser.swatchesRecentText", "Recent:");
		UIManager.put ("ColorChooser.swatchesNameText", "Swatches");
		UIManager.put ("ColorChooser.rgbBlueText", "Blue");
		UIManager.put ("ColorChooser.rgbRedText", "Red");
		UIManager.put ("ColorChooser.rgbGreenText", "Green");	
		UIManager.put("ColorChooser.sampleText", "Sample text");

		Sa1= new SimAuto();
		Sm1= new SimManual();

		setBounds(Sm1.m3.widthX, 0, pan1Xsrt+btnSizeX*4+simAccPnlSizeX+panIntv*7, 10+simAccPnlSizeY+panIntv*5+panSizeY*3+btnSizeY*3);
		setJMenuBar(menuBar);

		///mHelp.setHorizontalAlignment(SwingConstants.CENTER);
		//mHelp.setPreferredSize(new Dimension(30, 20));
		//mHelp.setMinimumSize(new Dimension(30, 20));
		//mPause.setHorizontalAlignment(SwingConstants.CENTER);
		//mPause.setPreferredSize(new java.awt.Dimension(30, 20));
		//mConti.setPreferredSize(new java.awt.Dimension(30, 20));


		mItmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String helpContent;
				if(MainDriver.igERD == 1){    //1/21/03
					helpContent = "  This is for simulation of drilling and well control"
							+"\n"+" You can load/unload the wellbore profile and trajectory from the top menu."
							+"\n"+" In order to start the simulation, select trip related options"
							+"\n"+" and click the Run Start Simulation command button."
							+"\n"+" Based on your trip conditions you may have kicks from multiple points."
							+"\n"+" After kick detection and confinement by well shut in,"
							+"\n"+" you need to go back to the bottom using Snub/Strip button."
							+"\n"+" Then, you can kill the well using conventional approach."
							+"\n"+"     ^.^ Good Luck to YOU ! ^.^ ";
				}
				else{
					helpContent =  "  This is for simulation of drilling and well control"
							+"\n"+" From the Wellbore menu, you can load/unload the trajectory and wellbore profile which you can move or adjust its size."
							+"\n"+" Now you can choose your favorite colors for old mud, kill mud, kick, and wellbore background."
							+"\n"+" In order to start the simulation, click the Start Simulation menu under Menus from the top of"
							+"\n"+" the screen or click the Start Simulation button. Then follow the Commands in sequence for well control."
							+"\n"+" After reaching the target depth, you may have a kick. Then, you have to detect the kick and control it by shutting the well in."
							+"\n"+" After well stabilzation, you can kill the well using driller's or engineer's method."
							+"\n"+"     ^.^ Good Luck to YOU ! ^.^ ";
				}
				JOptionPane.showMessageDialog(null, helpContent, "Help",JOptionPane.INFORMATION_MESSAGE);
			}
		});

		menuHelps.add(mItmHelp);		
		menuBar.add(menuHelps);

		menuBar.add(mMenus);
		menuOpenSID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {					
				txtL.setVisible(true);				
				if(TimerWarnOn==1){
					iTimerWarn = 1;
					WarnTaskOn=0;
					SIDTimerWarn=1;
				}
				if (TimerDrillOn==1){
					iTimerDrill = 1;
					TDTaskOn=0;
					SIDTimerDrill=1;
				}
				if (TimerSIOn==1){
					iTimerShutin = 1; 
					SITaskOn=0;
					MainDriver.SItaskOn2=0;
					SIDTimerShutin=1;
				}

				if(iROP == 1) {
					Sm1.m3.TRTaskOn=0;
					SIDSm1TRTaskOn=1;
					Sa1.m3.TRTaskOn=0;
					SIDSa1TRTaskOn=1;
				}
				//if(MainDriver.iWshow == 1) Sm1.m3.setVisible(true);

			}
		});

		mFiles.add(menuOpenSID);
		menuSaveSID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtR = new textResult("Save");

				if(TimerWarnOn==1){
					iTimerWarn = 1;
					WarnTaskOn=0;
				}
				if (TimerDrillOn==1){
					iTimerDrill = 1;
					TDTaskOn=0;
				}
				if (TimerSIOn==1){
					iTimerShutin = 1; 
					SITaskOn=0;
					MainDriver.SItaskOn2=0;
				}

				if(iShutinData!=0){
					if(iROP == 1){
						Sm1.m3.TRTaskOn=0;
						Sa1.m3.TRTaskOn=0;
					}
					if(MainDriver.iWshow == 1) Sm1.m3.setVisible(true);				    
				}

				MainDriver.operMsg = "Operations: Pause";
				lblPause.setText(MainDriver.operMsg); //menuPause_click, 20140123 ajw

				iShutinData=1;
				if(MainDriver.imud==0) set2Pcal();
				else OBM_set2Pcal();

				MainDriver.iBOP=0;
				CalChkVol();
				MainDriver.SICP = Pchoke;

				SaveShutinData(txtR);
				txtR.setVisible(true);
			}
		});

		mFiles.add(menuSaveSID);
		menuShowData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TimerIPOn==0){
					ip = new inputData();
					ip.setVisible(true);
					ip.menuToSimulation.setEnabled(true);
					iDataChange = 1;			    
					MainDriver.MenuSelected=2;
					TimerCheckIP = new Timer();
					TimerCheckIP.schedule(new CheckTask(), 0, 500);
					TimerIPOn=1;
				}
				else{
					ip.setVisible(true);
					ip.menuToSimulation.setEnabled(true);
					iDataChange = 1;			    
					MainDriver.MenuSelected=2;
				}
			}
		});

		mFiles.add(menuShowData);
		mFiles.setHorizontalAlignment(SwingConstants.RIGHT);

		menuBar.add(mFiles);

		mMenus.add(mWellbore);
		mWbShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iWshow=1;
				Sm1.m3.setVisible(true);
			}
		});

		mWellbore.add(mWbShow);
		mWbHide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sm1.m3.setVisible(false);
				MainDriver.iWshow=0;
			}
		});

		mWellbore.add(mWbHide);
		mWbTraject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m5.setVisible(true);
			}
		});



		mWellbore.add(mWbTraject);
		mSrtSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MainDriver.gTcum <= 0.5){// if(gTcum>0.5) Then Exit Sub
					iDataChange = 0;
					if(MainDriver.igERD == 1){
						//TimerTrip.setEnabled = True
						btnPumpOn1.setEnabled(false);
						btnPumpOff1.setEnabled(false);
						btnPumpOn2.setEnabled(false);
						btnPumpOff2.setEnabled(false);
						btnDrillOn.setEnabled(false);
						btnDrillOff.setEnabled(false);
						cmdKill.setEnabled(false);
						MainDriver.operMsg ="Tripping simulation is ready ...";
						lblPause.setText(MainDriver.operMsg);
					}
					else{
						if(TimerDrillOn==0){
							TimerDrill = new Timer();
							TimerDrill.schedule(new TDTask(), 2000, TimeDrlIntv);//TimerDrill.setEnabled = True //히히
							//TimerDrill.schedule(new TDTask(), 2000, 2000);

							TimerDrillOn=1;
						}
						MainDriver.operMsg = "Well control simulation is on ...";
						lblPause.setText(MainDriver.operMsg);						
					}
					//MainDriver.iWshow=1;				
					//if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true); 20131022 ajw
					btnSrtSim.setEnabled(false);
					mSrtSim.setEnabled(false);
				}
			}});

		mMenus.add(mSrtSim);
		mSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iSound==1){
					btnWarn.setText("Sound On");
					iSound = 0;
				}
				else{
					btnWarn.setText("Sound Off");			
					iSound = 1;
				}
				if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
				Sm1.m3.showKickIndex=1;	
				Sa1.m3.showKickIndex=1;	
			}
		});

		mMenus.add(mSound);		
		mMenus.add(mClrSelect);		

		clrWb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Colorchooser = new JColorChooser();
				SelectedColor = Colorchooser.showDialog(simdis.this, "Color", Color.RED);
				if (SelectedColor != null){
					MainDriver.WellColor = SelectedColor;
					Sm1.m3.pp.setBackground(MainDriver.WellColor);
					Sa1.m3.pp.setBackground(MainDriver.WellColor);
					Sm1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sm1.m3.repaint();
					Sa1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sa1.m3.repaint();
				}
				setVisible(true);

			}
		});

		mClrSelect.add(clrWb);
		ClrMudInUse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Colorchooser = new JColorChooser();
				SelectedColor = Colorchooser.showDialog(simdis.this, "Color", Color.RED);
				if (SelectedColor != null){
					MainDriver.MudColor = SelectedColor;
					Sm1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sm1.m3.repaint();
					Sa1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sa1.m3.repaint();
				}
				setVisible(true);
			}
		});

		mClrSelect.add(ClrMudInUse);
		ClrKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Colorchooser = new JColorChooser();
				SelectedColor = Colorchooser.showDialog(simdis.this, "Color", Color.RED);
				if (SelectedColor != null){
					MainDriver.KillColor = SelectedColor;
					Sm1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sm1.m3.repaint();
					Sa1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sa1.m3.repaint();
				}
				setVisible(true);
			}
		});

		mClrSelect.add(ClrKill);
		ClrKick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Colorchooser = new JColorChooser();
				SelectedColor = Colorchooser.showDialog(simdis.this, "Color", Color.RED);
				if (SelectedColor != null){
					MainDriver.KickColor = SelectedColor;
					Sm1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sm1.m3.repaint();
					Sa1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.
					Sa1.m3.repaint();
				}
				setVisible(true);
			}
		});

		mClrSelect.add(ClrKick);
		mBacktoMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dummy=0;
				String s = "Return to the Main Menu ? If then, you have to start from the beginning of this form.";
				dummy = JOptionPane.showConfirmDialog(null, s,"Return to Main Menu", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);// yes =0, no =1
				if(dummy==0){
					if(TimerWarnOn==1){
						TimerWarn.cancel();
						TimerWarnOn=0;
					}
					if(TimerDrillOn==1){
						TimerDrill.cancel();
						TimerDrillOn=0;
					}
					if(TimerSIOn==1){
						TimerSI.cancel();
						TimerSIOn=0;
					}					
					if(Sm1.m3.TimerRotOn==1){
						Sm1.m3.TimerRot.cancel();
						Sm1.m3.TimerRotOn=0;
					}
					if(Sa1.m3.TimerRotOn==1){
						Sa1.m3.TimerRot.cancel();
						Sa1.m3.TimerRotOn=0;
					}

					MainDriver.iWshow = 0;
					Sm1.m3.dispose();
					Sm1.m3.setVisible(false);

					Sa1.m3.dispose();
					Sa1.m3.setVisible(false);
					menuClose();
				}
			}
		});

		mntmResultsInPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
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

		mMenus.add(mntmResultsInPlot);

		mMenus.add(mBacktoMain);
		mItmPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// This is reprogrammed by Choe on 7/12/02 (at Texas A&M).
				// This is easy because we use timer interval, gDelT, delX, etc.

				if(TimerWarnOn==1){
					iTimerWarn = 1;
					WarnTaskOn=0;
				}
				if (TimerDrillOn==1){
					iTimerDrill = 1;
					TDTaskOn=0;
				}
				if (TimerSIOn==1){
					iTimerShutin = 1; 
					SITaskOn=0;
					MainDriver.SItaskOn2=0;
				}
				if(iROP == 1) {
					Sm1.m3.TRTaskOn=0;
					Sa1.m3.TRTaskOn=0;
				}

				if(MainDriver.iWshow == 1){
					Sm1.m3.setVisible(true);
				}

				MainDriver.operMsg = "Operations: Pause";
				lblPause.setText(MainDriver.operMsg);
			}
		});


		menuBar.add(mSolution);		
		mSolution.add(mSolution1);
		mSolution.add(mSolution2);
		mSolution.add(mSolution3);

		//0:mech 1:diff 2:mud loss 3: cutting
		mSolution1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				if(MainDriver.gTcum-solCumTime>3*60 || solCumTime==0){
					if(MainDriver.iProblem_occured[0]==1){ // Mechanical pipe stuck
						MainDriver.iProblem_occured[0]=0;
						MainDriver.iProblem[0]=0;
						solCumTime=0;
						for(int i=MainDriver.NwcS-1; i<MainDriver.NwcE; i++){
							MainDriver.f_cutting[i]=0;
						}
						iROP=1;
					}
					else solCumTime = MainDriver.gTcum;
				}
			}
		});
		mSolution2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.gTcum-solCumTime>3*60 || solCumTime==0){
					if(MainDriver.iProblem_occured[1]==1){ // Differential pipe stuck
						MainDriver.iProblem_occured[1]=0;
						MainDriver.iProblem[1]=0;
						solCumTime=0;
						iROP=1;
					}
					else solCumTime = MainDriver.gTcum;
				}
			}
		});
		mSolution3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.gTcum-solCumTime>3*60 || solCumTime==0){
					if(MainDriver.iProblem_occured[2]==1){ // Lost Circulation
						MainDriver.iProblem_occured[2]=0;
						MainDriver.iProblem[2]=0;
						solCumTime=0;
					}
					else solCumTime = MainDriver.gTcum;
				}
			}
		});

		menuBar.add(menuPause);

		//menuBar.add(menuContinue);
		menuPause.add(mItmPause);
		menuPause.add(mItmConti);		

		mItmConti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(iShutinData == 0){  //inactive if we open SID data, 7/16/02
					if (WarnTaskOn == 0) WarnTaskOn = 1;
					if (TDTaskOn == 0) TDTaskOn = 1;
					if (SITaskOn == 0) SITaskOn = 1;
					//if iTimerTrip = 1 Then TimerTrip.Enabled = True
					//if iTimerStrip = 1 Then TimerStrip.Enabled = True

					iTimerWarn = 0;
					iTimerDrill = 0;
					iTimerShutin = 0;
					iTimerTrip = 0; 
					iTimerStrip = 0;
					if (iROP == 1){
						Sm1.m3.TRTaskOn=1;
						Sa1.m3.TRTaskOn=1;
					}
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					MainDriver.operMsg = "Operations:";
					lblPause.setText(MainDriver.operMsg);
				}
			}
		});
		mItmConti.setHorizontalAlignment(SwingConstants.LEFT);


		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);	
		contentPane.setLayout(null);
		//asdfpan2.setBounds(pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize+panIntv+(pnlPumpRateSizeX)/2-panSizeX/2, 10+simAccPnlSizeY+panIntv*2+panSizeY , panSizeX, panSizeY);
		ChokeControlPan.setBounds(pan1Xsrt+btnSizeX*4+panIntv*4, 10+btnSizeY*2+5*3+23+panIntv+pnlPumpRateSizeY+4, pnlPumpRateSizeX, 87);
		//pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize+panIntv*2+(pnlPumpRateSizeX)/2-panSizeX/2+panSizeX, 10+simAccPnlSizeY+panIntv
		ChokeControlPan.setLayout(null);
		ChokeControlPan.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		ChokeControlPan.setBackground(SystemColor.menu);		
		ChokeControlPan.setVisible(false);
		
		
		int intve = 4;

		pnlControl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		getContentPane().add(pnlControl);
		pnlControl.setBounds(pan1Xsrt+btnSizeX*4+panIntv*4, 10+btnSizeY*2+5*3+23+panIntv+pnlPumpRateSizeY+4, pnlPumpRateSizeX, btnSizeY+txtPumpRSizeY*4+19+intve+10);
		pnlControl.setLayout(null);
		lblControl.setHorizontalAlignment(SwingConstants.CENTER);
		lblControl.setFont(new Font("굴림", Font.BOLD, 15));
		lblControl.setBounds((pnlPumpRateSizeX-150)/2, 0, 150, btnSizeY);
		pnlControl.add(lblControl);

		lblRPM.setHorizontalAlignment(SwingConstants.LEFT);		
		lblRPM.setFont(new Font("굴림", Font.PLAIN, 12));
		lblRPM.setBounds(lblSelMudPumpSrtY, btnSizeY+5+intve, 90, txtPumpRSizeY);
		pnlControl.add(lblRPM);	
		lblRPM2.setFont(new Font("굴림", Font.PLAIN, 12));
		lblRPM2.setBounds(lblSelMudPumpSrtY+90+txtPumpRSizeX+3, btnSizeY+5+intve, 45, txtPumpRSizeY);
		pnlControl.add(lblRPM2);

		txtRPM_control.setBackground(Color.YELLOW);
		txtRPM_control.setText((new DecimalFormat("##0")).format(0));
		txtRPM_control.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRPM_control.setBounds(lblSelMudPumpSrtY+90, btnSizeY+5+intve, txtPumpRSizeX, txtPumpRSizeY);
		pnlControl.add(txtRPM_control);

		pnlControl.add(RPMScroll);
		RPMScroll.setOrientation(JScrollBar.HORIZONTAL);
		RPMScroll.setBounds((pnlPumpRateSizeX-ScrollSizeX)/2, btnSizeY+txtPumpRSizeY+8+intve, ScrollSizeX, txtPumpRSizeY);

		sepPnlCont1.setBounds(2, btnSizeY+txtPumpRSizeY*2+13+intve, pnlPumpRateSizeX-2, 2);
		pnlControl.add(sepPnlCont1);

		lblWOB.setHorizontalAlignment(SwingConstants.LEFT);		
		lblWOB.setFont(new Font("굴림", Font.PLAIN, 12));
		lblWOB.setBounds(lblSelMudPumpSrtY, btnSizeY+txtPumpRSizeY*2+20+intve, 90, txtPumpRSizeY);
		pnlControl.add(lblWOB);	
		lblWOB2.setFont(new Font("굴림", Font.PLAIN, 12));
		lblWOB2.setBounds(lblSelMudPumpSrtY+90+txtPumpRSizeX+3, btnSizeY+txtPumpRSizeY*2+20+intve, 80, txtPumpRSizeY);
		pnlControl.add(lblWOB2);

		txtWOB.setBackground(Color.YELLOW);
		txtWOB.setText((new DecimalFormat("##0")).format(0));
		txtWOB.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWOB.setBounds(lblSelMudPumpSrtY+90, btnSizeY+txtPumpRSizeY*2+20+intve, txtPumpRSizeX, txtPumpRSizeY);
		pnlControl.add(txtWOB);

		pnlControl.add(WOBScroll);
		WOBScroll.setOrientation(JScrollBar.HORIZONTAL);
		WOBScroll.setBounds((pnlPumpRateSizeX-ScrollSizeX)/2, btnSizeY+txtPumpRSizeY*3+23+intve, ScrollSizeX, txtPumpRSizeY);
		

		contentPane.add(ChokeControlPan);
		lblChokeControlMethod.setHorizontalAlignment(SwingConstants.CENTER);
		lblChokeControlMethod.setFont(new Font("굴림", Font.BOLD, 15));
		lblChokeControlMethod.setBounds(0, 10, 264, 15);

		ChokeControlPan.add(lblChokeControlMethod);
		optAutomatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//MainDriver.iCHKcontrol = 1;			  // iMode  =2  
				MainDriver.iCHKcontrol2 = 1; //151109 AJW
				MainDriver.iCHKcontrol = 2; // iMode =1
				MainDriver.Hdrled = 0; 
				MainDriver.iDone = 0;
				for(int i = 0; i<=iPenet; i++){
					MainDriver.Hdrled = MainDriver.Hdrled + Hpenet[i];
				}
				if(MainDriver.Hdrled == 0){
					MainDriver.Hdrled = vdBit - MainDriver.Vdepth;
				}
			}
		});

		optAutomatic.setBounds(18, 36, 196, 20);
		ChokeControlPan.add(optAutomatic);
		optManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				MainDriver.iCHKcontrol2 = 2; //151109 AJW
				MainDriver.iCHKcontrol = 2; // iMode =1
				MainDriver.Hdrled = 0; 
				MainDriver.iDone = 0;
				for(int i = 0; i<=iPenet; i++){
					MainDriver.Hdrled = MainDriver.Hdrled + Hpenet[i];
				}
				if(MainDriver.Hdrled == 0){
					MainDriver.Hdrled = vdBit - MainDriver.Vdepth;
				}
			}
		});

		optManual.setBounds(18, 56, 200, 20);
		ChokeControlPan.add(optManual);

		ChokeControlGroup.add(optAutomatic);
		ChokeControlGroup.add(optManual);		
		btnSrtSim.setBounds(pan1Xsrt, 10, btnSizeX, btnSizeY);
		contentPane.add(btnSrtSim);
		lblPause.setBackground(Color.YELLOW);
		lblPause.setEditable(false);

		lblPause.setBounds(pan1Xsrt, 10+btnSizeY*2+5*2, 300, 23);
		contentPane.add(lblPause);						

		btnDrillOn.setEnabled(true);
		btnDrillOn.setBounds(pan1Xsrt+btnSizeX+panIntv, 10, btnSizeX, btnSizeY);
		contentPane.add(btnDrillOn);
		btnDrillOff.setEnabled(true);
		btnDrillOff.setBounds(pan1Xsrt+btnSizeX+panIntv, 10+btnSizeY+5, btnSizeX, btnSizeY);
		contentPane.add(btnDrillOff);
		contentPane.add(pan3);


		//pan3.setBounds(pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize+panIntv+(pnlPumpRateSizeX)/2-panSizeX/2, 10+simAccPnlSizeY+panIntv*5+btnSizeY+panSizeY+110, panSizeX, panSizeY);//20140217 ajw


		btnShutIn.setEnabled(true);		
		contentPane.add(btnShutIn);
		cmdKill.setEnabled(true);		
		contentPane.add(cmdKill);	

		contentPane.add(pan1); //Mud return rate
		pan3.setBounds((pan1Xsrt+btnSizeX*4+panIntv*4+pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize)/2-panSizeX/2, 10+btnSizeY*2+5*3+23, panSizeX, panSizeY);		
		pan2.setBounds((pan1Xsrt+btnSizeX*4+panIntv*4+pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize)/2-panSizeX/2, 10+btnSizeY*2+5*3+23+panIntv+panSizeY+4 , panSizeX, panSizeY);
		pan1.setBounds((pan1Xsrt+btnSizeX*4+panIntv*4+pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize)/2-panSizeX/2, 10+btnSizeY*2+5*3+23+panIntv*2+panSizeY*2+8 , panSizeX, panSizeY);
		PnlPumpRate.setBounds(pan1Xsrt+btnSizeX*4+panIntv*4, 10+btnSizeY*2+5*3+23, pnlPumpRateSizeX, pnlPumpRateSizeY);

		btnShutIn.setBounds(pan1Xsrt+btnSizeX*2+panIntv*2, 10, btnSizeX, btnSizeY);
		//btnShutIn.setBounds(600, 400, btnSizeX, btnSizeY); //임시
		cmdKill.setBounds(pan1Xsrt+btnSizeX*3+panIntv*3, 10, btnSizeX, btnSizeY);
		//cmdKill.setBounds(750, 400, btnSizeX, btnSizeY);


		contentPane.add(pan2); // Choke pressure			

		pan4.setBounds(pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize+panIntv+(pnlPumpRateSizeX)/2-panSizeX/2, 10+simAccPnlSizeY+panIntv, panSizeX, panSizeY);
		contentPane.add(pan4);
		pan4.setVisible(false);//20140217 ajw 

		PnlPumpRate.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));		
		PnlPumpRate.setBackground(SystemColor.menu);				
		//PnlPumpRate.setBounds(pan1Xsrt+txtIntvX*3+txtSizeX+txtLblXsize+panIntv, 10+simAccPnlSizeY+panIntv*2+panSizeY, pnlPumpRateSizeX, pnlPumpRateSizeY);

		contentPane.add(PnlPumpRate);
		PnlPumpRate.setLayout(null);		

		lblSelMudPump.setOpaque(false);
		lblSelMudPump.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelMudPump.setFont(new Font("굴림", Font.BOLD, 15));
		lblSelMudPump.setBounds(2, 5, 262, 15);
		PnlPumpRate.add(lblSelMudPump);		


		//default setting

		PumpTypeGroup.add(optPump1);
		PumpTypeGroup.add(optPump2);		
		txtPumpStatus1.setBackground(Color.YELLOW);
		txtPumpStatus1.setHorizontalAlignment(SwingConstants.CENTER);

		txtPumpStatus1.setText("Off");
		txtPumpStatus2.setText("Off");
		txtPumpStatus2.setBackground(Color.YELLOW);
		txtPumpStatus2.setHorizontalAlignment(SwingConstants.CENTER);

		if(MainDriver.spMinD1!=0){
			optPump1.setSelected(true);
			btnPumpOn1.setVisible(true);
			btnPumpOn2.setVisible(false);
			btnPumpOff1.setVisible(true);
			btnPumpOff2.setVisible(false);
			txtPumpR1.setVisible(true);
			txtPumpR2.setVisible(false);
			pumpScroll1.setVisible(true);
			pumpScroll2.setVisible(false);
			txtStks1.setVisible(true);
			txtStks2.setVisible(false);
			txtPumpStatus1.setVisible(true);
			txtPumpStatus2.setVisible(false);
		}
		else{
			optPump2.setSelected(true);
			btnPumpOn1.setVisible(false);
			btnPumpOn2.setVisible(true);
			btnPumpOff1.setVisible(false);
			btnPumpOff2.setVisible(true);
			txtPumpR1.setVisible(false);
			txtPumpR2.setVisible(true);
			pumpScroll1.setVisible(false);
			pumpScroll2.setVisible(true);
			txtStks1.setVisible(false);
			txtStks2.setVisible(true);
			txtPumpStatus1.setVisible(false);
			txtPumpStatus2.setVisible(true);
		}



		optPump1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnPumpOn1.setVisible(true);
				btnPumpOn2.setVisible(false);
				btnPumpOff1.setVisible(true);
				btnPumpOff2.setVisible(false);
				txtPumpR1.setVisible(true);
				txtPumpR2.setVisible(false);
				pumpScroll1.setVisible(true);
				pumpScroll2.setVisible(false);
				txtStks1.setVisible(true);
				txtStks2.setVisible(false);
				pumpScroll1.setValue((int)(MainDriver.spMinD1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMinD2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
				txtPumpStatus1.setVisible(true);
				txtPumpStatus2.setVisible(false);
				btnSetStkZero1.setVisible(true);
				btnSetStkZero2.setVisible(false);
				if(iPumpOn1==1) txtPumpStatus1.setText("On");
				else txtPumpStatus1.setText("Off");
				if(iPumpOn2==1) txtPumpStatus2.setText("On");
				else txtPumpStatus2.setText("Off");				
				if(MainDriver.iHuschel==1){
					MainDriver.Qdrill = 530;
				}
				else if(MainDriver.iHuschel==2){
					MainDriver.Qdrill = 450;
				}
				else{
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
				}
				//MainDriver.Qdrill = 337.088811730091; //히히
			}
		});


		optPump2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPumpOn1.setVisible(false);
				btnPumpOn2.setVisible(true);
				btnPumpOff1.setVisible(false);
				btnPumpOff2.setVisible(true);
				txtPumpR1.setVisible(false);
				txtPumpR2.setVisible(true);
				pumpScroll1.setVisible(false);
				pumpScroll2.setVisible(true);
				txtStks1.setVisible(false);
				txtStks2.setVisible(true);
				pumpScroll1.setValue((int)(MainDriver.spMinD1 + 0.05));
				pumpScroll2.setValue((int)(MainDriver.spMinD2 + 0.05));
				txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
				txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
				txtPumpStatus1.setVisible(false);
				txtPumpStatus2.setVisible(true);
				btnSetStkZero1.setVisible(false);
				btnSetStkZero2.setVisible(true);
				if(iPumpOn1==1) txtPumpStatus1.setText("On");
				else txtPumpStatus1.setText("Off");
				if(iPumpOn2==1) txtPumpStatus2.setText("On");
				else txtPumpStatus2.setText("Off");
				if(MainDriver.iHuschel==1){
					MainDriver.Qdrill = 530;
				}
				else if(MainDriver.iHuschel==2){
					MainDriver.Qdrill = 450;
				}
				else{
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
				}
			}
		});

		if(MainDriver.spMinD1==0 && MainDriver.spMinD2>0) btnPumpOn2.setSelected(true);

		btnPumpOn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// start pumping
				if(iShutinData != 1){// if(ishutinData==1) Then Exit Sub				    
					if (TimerDrillOn==0){
						String Msg=  "You can start the simulation by clicking Start Simulation menu under Menus.";
						JOptionPane.showMessageDialog(null, Msg);
						if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);

					}
					else if(iPumpOn1==0){
						MainDriver.iBOP=1; //20140218 ajw
						iPumpOn1 = 1;   //pump on(1), pump off(0)
						txtPumpStatus1.setText("On");
						timePumpOff = 0;
						if(MainDriver.iHuschel==1){
							MainDriver.Qdrill = 530;
						}
						else if(MainDriver.iHuschel==2){
							MainDriver.Qdrill = 450;
						}
						else{
							MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
							Qcirc = MainDriver.Qdrill;
						}
						//MainDriver.Qdrill = 337.088811730091; //히히
						Qcirc = MainDriver.Qdrill;

						pumpScroll1.setValue((int)(MainDriver.spMinD1 + 0.05));
						pumpScroll2.setValue((int)(MainDriver.spMinD2 + 0.05));
						txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
						txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
						txtPumpRate.setText((new DecimalFormat("##,##0")).format(0.5 * MainDriver.Qdrill));

						if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
						if (iROP == 1 && Sm1.m3.TimerRotOn==0){
							Sm1.m3.TimerRot = new Timer();
							Sm1.m3.TimerRot.schedule(Sm1.m3.new TRTask(), 0, TimeDrlIntv);//WellPic.TimerRot.setEnabled = True	
							Sm1.m3.TimerRotOn=1;
						}
						//btnPumpOn1.setEnabled(false);	
						btnPumpOff1.setEnabled(true);
						if(DrillbtnOn*iPumpOn1==1){							
							btnDrillOff.setEnabled(true);
							cmdKill.setEnabled(true);
							btnShutIn.setEnabled(true);	
						}
					}	
				}
			}
		});

		btnPumpOn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// start pumping
				if(iShutinData != 1){// if(ishutinData==1) Then Exit Sub				    
					if (TimerDrillOn==0){
						String Msg=  "You can start the simulation by clicking Start Simulation menu under Menus.";
						JOptionPane.showMessageDialog(null, Msg);
						if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);

					}
					else if(iPumpOn2==0){
						MainDriver.iBOP=1; //20140218 ajw
						iPumpOn2 = 1;   //pump on(1), pump off(0)
						txtPumpStatus2.setText("On");
						timePumpOff = 0;

						if(MainDriver.iHuschel==1){
							MainDriver.Qdrill = 530;
						}
						else if(MainDriver.iHuschel==2){
							MainDriver.Qdrill = 450;
						}
						else{
							MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
							Qcirc = MainDriver.Qdrill;
						}						

						pumpScroll1.setValue((int)(MainDriver.spMinD1 + 0.05));
						pumpScroll2.setValue((int)(MainDriver.spMinD2 + 0.05));
						txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
						txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
						txtPumpRate.setText((new DecimalFormat("##,##0")).format(0.5 * MainDriver.Qdrill));

						if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
						if (iROP == 1 && Sm1.m3.TimerRotOn==0){
							Sm1.m3.TimerRot = new Timer();
							Sm1.m3.TimerRot.schedule(Sm1.m3.new TRTask(), 0, TimeDrlIntv);//WellPic.TimerRot.setEnabled = True	
							Sm1.m3.TimerRotOn=1;
						}
						//btnPumpOn2.setEnabled(false);	
						btnPumpOff2.setEnabled(true);
						if(DrillbtnOn*iPumpOn2==1){
							btnDrillOff.setEnabled(true);
							cmdKill.setEnabled(true);
							btnShutIn.setEnabled(true);	
						}
					}	
				}
			}
		});

		optPump1.setFont(new Font("굴림", Font.PLAIN, 12));		
		optPump1.setBounds((pnlPumpRateSizeX-radioBtnSizeX*2-panIntv*2)/2, lblSelMudPumpSrtY+18, radioBtnSizeX, radioBtnSizeY);
		PnlPumpRate.add(optPump1);		
		optPump1.setOpaque(false);

		optPump2.setFont(new Font("굴림", Font.PLAIN, 12));
		optPump2.setBounds((pnlPumpRateSizeX-radioBtnSizeX*2-panIntv*2)/2+radioBtnSizeX+2*panIntv, lblSelMudPumpSrtY+18, radioBtnSizeX, radioBtnSizeY);
		optPump2.setOpaque(false);
		PnlPumpRate.add(optPump2);		

		sepPnlPump1.setBounds(2, lblSelMudPumpSrtY+15+radioBtnSizeY+2, pnlPumpRateSizeX-2, 2);
		PnlPumpRate.add(sepPnlPump1);		

		btnPumpOn1.setEnabled(true);
		btnPumpOn1.setBounds((pnlPumpRateSizeX-btnSizeX*2)/2, lblSelMudPumpSrtY+15+radioBtnSizeY+4, btnSizeX, btnSizeY);
		PnlPumpRate.add(btnPumpOn1);
		btnPumpOff1.setEnabled(true);
		btnPumpOff1.setBounds((pnlPumpRateSizeX-btnSizeX*2)/2+btnSizeX, lblSelMudPumpSrtY+15+radioBtnSizeY+4, btnSizeX, btnSizeY);
		PnlPumpRate.add(btnPumpOff1);
		btnPumpOn2.setEnabled(true);
		btnPumpOn2.setBounds((pnlPumpRateSizeX-btnSizeX*2)/2, lblSelMudPumpSrtY+15+radioBtnSizeY+4, btnSizeX, btnSizeY);
		PnlPumpRate.add(btnPumpOn2);
		btnPumpOff2.setEnabled(true);
		btnPumpOff2.setBounds((pnlPumpRateSizeX-btnSizeX*2)/2+btnSizeX, lblSelMudPumpSrtY+15+radioBtnSizeY+4, btnSizeX, btnSizeY);
		PnlPumpRate.add(btnPumpOff2);
		// Pump pressure						
		lblPumpRate.setHorizontalAlignment(SwingConstants.LEFT);

		lblPumpRate.setFont(new Font("굴림", Font.PLAIN, 12));
		lblPumpRate.setBounds(lblSelMudPumpSrtY, lblSelMudPumpSrtY+18+radioBtnSizeY+btnSizeY+2+8, 80, txtPumpRSizeY);
		PnlPumpRate.add(lblPumpRate);		

		txtPumpR1.setBounds(lblSelMudPumpSrtY+80, lblSelMudPumpSrtY+18+radioBtnSizeY+btnSizeY+2+8, txtPumpRSizeX, txtPumpRSizeY);
		PnlPumpRate.add(txtPumpR1);	
		txtPumpR2.setBounds(lblSelMudPumpSrtY+80, lblSelMudPumpSrtY+18+radioBtnSizeY+btnSizeY+2+8, txtPumpRSizeX, txtPumpRSizeY);
		PnlPumpRate.add(txtPumpR2);	

		lblPumpRate2.setFont(new Font("굴림", Font.PLAIN, 12));
		lblPumpRate2.setBounds(lblSelMudPumpSrtY+80+txtPumpRSizeX+3, lblSelMudPumpSrtY+18+radioBtnSizeY+btnSizeY+2+8, 45, txtPumpRSizeY);
		PnlPumpRate.add(lblPumpRate2);		

		txtPumpStatus1.setBounds((pnlPumpRateSizeX-btnSizeX*2)/2+2*btnSizeX-30, lblSelMudPumpSrtY+18+radioBtnSizeY+btnSizeY+2+8, 30, txtPumpRSizeY);
		PnlPumpRate.add(txtPumpStatus1);	
		txtPumpStatus2.setBounds((pnlPumpRateSizeX-btnSizeX*2)/2+2*btnSizeX-30, lblSelMudPumpSrtY+18+radioBtnSizeY+btnSizeY+2+8, 30, txtPumpRSizeY);
		PnlPumpRate.add(txtPumpStatus2);

		pumpScroll1.setOrientation(JScrollBar.HORIZONTAL);
		pumpScroll1.setBounds((pnlPumpRateSizeX-ScrollSizeX)/2, lblSelMudPumpSrtY+25+radioBtnSizeY+btnSizeY+txtPumpRSizeY+5, ScrollSizeX, txtPumpRSizeY);
		PnlPumpRate.add(pumpScroll1);
		pumpScroll2.setOrientation(JScrollBar.HORIZONTAL);
		pumpScroll2.setBounds((pnlPumpRateSizeX-ScrollSizeX)/2, lblSelMudPumpSrtY+25+radioBtnSizeY+btnSizeY+txtPumpRSizeY+5, ScrollSizeX, txtPumpRSizeY);
		PnlPumpRate.add(pumpScroll2);
		lblPumpStk.setFont(new Font("굴림", Font.PLAIN, 12));

		lblPumpStk.setHorizontalAlignment(SwingConstants.LEFT);		
		lblPumpStk.setBounds(lblSelMudPumpSrtY, lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*2+10, 80, txtPumpRSizeY);

		PnlPumpRate.add(lblPumpStk);
		txtStks1.setBackground(Color.YELLOW);
		txtStks1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStks1.setBounds(lblSelMudPumpSrtY+80, lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*2+10, txtPumpRSizeX, txtPumpRSizeY);		
		PnlPumpRate.add(txtStks1);
		txtStks2.setBounds(lblSelMudPumpSrtY+80, lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*2+10, txtPumpRSizeX, txtPumpRSizeY);	
		txtStks2.setBackground(Color.YELLOW);
		txtStks2.setHorizontalAlignment(SwingConstants.RIGHT);
		PnlPumpRate.add(txtStks2);

		lblPumpStk2.setFont(new Font("굴림", Font.PLAIN, 12));
		lblPumpStk2.setHorizontalAlignment(SwingConstants.LEFT);
		lblPumpStk2.setBounds(lblSelMudPumpSrtY+80+txtPumpRSizeX+3, lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*2+10, 45, txtPumpRSizeY);

		PnlPumpRate.add(lblPumpStk2);		
		btnSetStkZero1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.reset_Stroke1=Stroke1;
				txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));				   
			}
		});
		btnSetStkZero2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.reset_Stroke2=Stroke2;
				txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));
			}
		});

		btnSetStkZero1.setBounds(pnlPumpRateSizeX/2-80, lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*3+13, 160, 25);
		PnlPumpRate.add(btnSetStkZero1);	

		btnSetStkZero2.setBounds(pnlPumpRateSizeX/2-80, lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*3+13, 160, 25);
		PnlPumpRate.add(btnSetStkZero2);

		btnPumpOff1.setEnabled(false);
		btnPumpOff2.setEnabled(false);

		btnDrillOff.setEnabled(false);
		cmdKill.setEnabled(false);
		btnShutIn.setEnabled(false);


		JPanel SimAccPnl = new JPanel();
		SimAccelRatio = new JLabel("Simulation Acceleration Ratio");

		SimAccPnl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		SimAccPnl.setBackground(SystemColor.menu);

		SimAccPnl.setBounds(pan1Xsrt+btnSizeX*4+panIntv*4, 10, simAccPnlSizeX, simAccPnlSizeY);
		contentPane.add(SimAccPnl);
		SimAccPnl.setLayout(null);				


		SimAccelRatio.setFont(new Font("굴림", Font.BOLD, 12));
		SimAccelRatio.setHorizontalAlignment(SwingConstants.CENTER);
		SimAccelRatio.setBounds(12, 0, 214, 15);
		SimAccPnl.add(SimAccelRatio);
		lblTimesFasterThan.setFont(new Font("굴림", Font.PLAIN, 11));

		lblTimesFasterThan.setHorizontalAlignment(SwingConstants.LEFT);
		lblTimesFasterThan.setBounds(22, 16, 254, 15);
		SimAccPnl.add(lblTimesFasterThan);
		txtSimRate.setBackground(Color.YELLOW);
		txtSimRate.setHorizontalAlignment(SwingConstants.CENTER);

		txtSimRate.setBounds(12, 36, 40, 24);
		SimAccPnl.add(txtSimRate);
		slider.setFont(new Font("굴림", Font.PLAIN, 7));
		slider.setMinimum(1);
		slider.setBounds(60, 35, 200, 30);
		SimAccPnl.add(slider);
		slider.setMajorTickSpacing(1);
		slider.setMaximum(10);
		slider.setValue(1);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(arg0.getSource()==slider){
					MainDriver.SimRate = slider.getValue();
					txtSimRate.setText(Integer.toString(MainDriver.SimRate));
				}
			}
		});

		pan1.setLayout(null);
		pan2.setLayout(null);
		pan3.setLayout(null);
		BlowOutPnl.setLayout(null);

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

		txtRateDiff.setBackground(Color.yellow);
		MudRRdif.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		MudRRdif.setHorizontalAlignment(SwingConstants.CENTER);
		MudRRdif.setBounds(0, 0, panSizeX, labelFontSize);
		MudRRdif2.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		MudRRdif2.setHorizontalAlignment(SwingConstants.LEFT);
		MudRRdif2.setBounds(0, labelFontSize, panSizeX, labelFontSize+1);
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
		txtPchoke.setBackground(Color.yellow);
		pan2.add(txtPchoke);			

		CHKPR.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		CHKPR.setHorizontalAlignment(SwingConstants.CENTER);
		CHKPR.setBounds(0, 0, panSizeX, labelFontSize+1);		
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

		pan2.add(CHKPR);
		pan2.add(CHK1);
		pan2.add(CHK2);
		pan2.add(CHK3);
		pan2.add(CHK4);


		pan3.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		pan4.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		txtPsp.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtPsp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtPsp.setText("");
		txtPsp.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtPsp.setEditable(true);
		txtPsp.setBackground(Color.yellow);
		pan3.add(txtPsp);			

		STPPP.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		STPPP.setHorizontalAlignment(SwingConstants.CENTER);
		STPPP.setBounds(0, 0, panSizeX, labelFontSize+1);
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

		pan3.add(STPPP);
		pan3.add(STP1);
		pan3.add(STP2);
		pan3.add(STP3);
		pan3.add(STP4);

		pan4.setLayout(null);
		lblSPP.setFont(new Font("굴림", Font.BOLD, labelFontSize));
		lblSPP.setHorizontalAlignment(SwingConstants.CENTER);
		lblSPP.setBounds(0, 0, panSizeX, labelFontSize+1);
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

		pan4.add(lblSPP);
		pan4.add(SPP1);
		pan4.add(SPP2);
		pan4.add(SPP3);
		pan4.add(SPP4);		

		txtPp.setFont(new Font("굴림", Font.PLAIN, RateDiffSize));
		txtPp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		txtPp.setText("");
		txtPp.setBounds(panSizeX-RateDiffSize*7, panSizeY-RateDiffSize*2-5, RateDiffSize*6, RateDiffSize*2);
		txtPp.setEditable(true);
		pan4.add(txtPp);

		InformationPane.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		InformationPane.setBounds(pan1Xsrt, 10+btnSizeY*2+5*3+23, txtIntvX*3+txtSizeX+txtLblXsize, txtSrtY1+23*txtLblYsize+23*txtIntvY);
		contentPane.add(InformationPane);
		InformationPane.setLayout(null);
		DrillingInfo.setFont(new Font("굴림", Font.BOLD, 17));
		DrillingInfo.setHorizontalAlignment(SwingConstants.CENTER);


		InformationPane.add(DrillingInfo);		
		InformationPane.add(TotElapsedTm);	

		InformationPane.add(lblhrMin);
		lblMudComp.setFont(new Font("굴림", Font.BOLD, 12));
		lblMudComp.setForeground(Color.RED);


		InformationPane.add(lblMudComp);
		lblDepthMud.setForeground(Color.BLUE);
		lblDepthMud.setHorizontalAlignment(SwingConstants.CENTER);
		lblDepthMud.setFont(new Font("굴림", Font.BOLD, 17));


		InformationPane.add(lblDepthMud);
		txtVDbit.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtVDbit);
		txtHDbit.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtHDbit);
		txtROP.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtROP);
		txtPumpRate.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtPumpRate);
		txtReturnRate.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtReturnRate);
		txtGasRate.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtGasRate);
		txtPitGain.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtPitGain);
		txtKillMudWt.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtKillMudWt);
		lblPressureInfromation.setHorizontalAlignment(SwingConstants.CENTER);
		lblPressureInfromation.setForeground(Color.BLUE);
		lblPressureInfromation.setFont(new Font("굴림", Font.BOLD, 17));


		InformationPane.add(lblPressureInfromation);
		txtTime.setBackground(Color.YELLOW);
		txtTime.setHorizontalAlignment(SwingConstants.CENTER);


		InformationPane.add(txtTime);
		txtFormationP.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtFormationP);
		txtBHP.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtBHP);
		txtCasingP.setHorizontalAlignment(SwingConstants.RIGHT);

		InformationPane.add(txtCasingP);
		txtMudLine.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtMudLine);
		txtChokeP.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtChokeP);
		txtSPP.setHorizontalAlignment(SwingConstants.RIGHT);

		InformationPane.add(txtSPP);
		txtPumpP.setHorizontalAlignment(SwingConstants.RIGHT);


		InformationPane.add(txtPumpP);

		InformationPane.add(lblDMI1);


		InformationPane.add(lblMDI2);


		InformationPane.add(lblMDI3);


		InformationPane.add(lblBDI4);


		InformationPane.add(lblMDI5);


		InformationPane.add(lblGasReturnRate);


		InformationPane.add(lblMDI7);


		InformationPane.add(lblMDI8);
		btnWarn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (iSound==1){
					btnWarn.setText("Sound On");
					iSound = 0;
				}
				else{
					btnWarn.setText("Sound Off");			
					iSound = 1;
				}
				if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
				Sm1.m3.showKickIndex=1;
				Sa1.m3.showKickIndex=1;
			}
		});


		InformationPane.add(btnWarn);


		InformationPane.add(lblDMI9);


		InformationPane.add(BlowOutPnl);

		JLabel lblPI1 = new JLabel("psig,  Pump Pressure");

		InformationPane.add(lblPI1);

		JLabel lblPI2 = new JLabel("psig,  Stand-Pipe Pressure");

		InformationPane.add(lblPI2);

		JLabel lblPI3 = new JLabel("psig,  Surface Choke Pressure");

		InformationPane.add(lblPI3);

		JLabel lblPI4 = new JLabel("psig,  Pressure at Mud Line ");

		InformationPane.add(lblPI4);

		JLabel lblPI6 = new JLabel("psig,  Casing Seat Pressure");

		InformationPane.add(lblPI6);

		JLabel lblPI7 = new JLabel("psig,  BottomHole Pressure");

		InformationPane.add(lblPI7);

		JLabel lblPI8 = new JLabel("psig,  Formation Pressure");

		InformationPane.add(lblPI8);

		JLabel lblPI5 = new JLabel("         (or Sea Floor)");

		InformationPane.add(lblPI5);

		txtTime.setBounds(txtSrtX, txtSrtY1, txtSizeX, txtSizeY);
		txtVDbit.setBounds(txtSrtX, txtSrtY1+4*txtSizeY+2*txtIntvY + txtIntvY, txtSizeX, txtSizeY);
		txtHDbit.setBounds(txtSrtX, txtSrtY1+5*txtSizeY+4*txtIntvY, txtSizeX, txtSizeY);
		txtROP.setBounds(txtSrtX, txtSrtY1+6*txtSizeY+5*txtIntvY, txtSizeX, txtSizeY);
		txtPumpRate.setBounds(txtSrtX, txtSrtY1+7*txtSizeY+6*txtIntvY, txtSizeX, txtSizeY);
		txtReturnRate.setBounds(txtSrtX, txtSrtY1+8*txtSizeY+7*txtIntvY, txtSizeX, txtSizeY);
		txtGasRate.setBounds(txtSrtX, txtSrtY1+9*txtSizeY+8*txtIntvY, txtSizeX, txtSizeY);
		txtPitGain.setBounds(txtSrtX, txtSrtY1+10*txtSizeY+9*txtIntvY, txtSizeX, txtSizeY);
		BlowOutPnl.setBounds(txtSrtX,txtSrtY1+11*txtLblYsize+10*txtIntvY, 70, 40);
		txtKillMudWt.setBounds(txtSrtX, txtSrtY1+13*txtSizeY+11*txtIntvY, txtSizeX, txtSizeY);

		txtPumpP.setBounds(txtSrtX, txtSrtY1+15*txtSizeY+14*txtIntvY, txtSizeX, txtSizeY);
		txtSPP.setBounds(txtSrtX, txtSrtY1+16*txtSizeY+15*txtIntvY, txtSizeX, txtSizeY);
		txtChokeP.setBounds(txtSrtX, txtSrtY1+17*txtSizeY+16*txtIntvY, txtSizeX, txtSizeY);
		txtMudLine.setBounds(txtSrtX, txtSrtY1+18*txtSizeY+17*txtIntvY, txtSizeX, txtSizeY);
		txtCasingP.setBounds(txtSrtX, txtSrtY1+20*txtSizeY+18*txtIntvY, txtSizeX, txtSizeY);
		txtBHP.setBounds(txtSrtX, txtSrtY1+21*txtSizeY+19*txtIntvY, txtSizeX, txtSizeY);
		txtFormationP.setBounds(txtSrtX, txtSrtY1+22*txtSizeY+20*txtIntvY, txtSizeX, txtSizeY);		

		DrillingInfo.setBounds((txtIntvX*3+txtSizeX+txtLblXsize)/2-180/2, 10, 180, 18);

		TotElapsedTm.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1, 143, txtLblYsize);
		lblhrMin.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+txtLblYsize, 143, txtLblYsize);
		lblMudComp.setBounds(txtSrtX, txtSrtY1+2*txtLblYsize, 257, 15);
		lblDepthMud.setBounds((txtIntvX*3+txtSizeX+txtLblXsize)/2-240/2, txtSrtY1+3*txtLblYsize+2*txtIntvY, 240, 15);

		lblDMI1.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+4*txtLblYsize+3*txtIntvY, txtLblXsize, txtLblYsize);
		lblMDI2.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+5*txtLblYsize+4*txtIntvY, txtLblXsize, txtLblYsize);
		lblMDI3.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+6*txtLblYsize+5*txtIntvY, txtLblXsize, txtLblYsize);
		lblBDI4.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+7*txtLblYsize+6*txtIntvY, txtLblXsize, txtLblYsize);
		lblMDI5.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+8*txtLblYsize+7*txtIntvY, txtLblXsize, txtLblYsize);
		lblGasReturnRate.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+9*txtLblYsize+8*txtIntvY, txtLblXsize, txtLblYsize);
		lblMDI7.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+10*txtLblYsize+9*txtIntvY, txtLblXsize, txtLblYsize);		
		lblMDI8.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+11*txtLblYsize+10*txtIntvY, txtLblXsize, txtLblYsize);	

		btnWarn.setBounds(txtSrtX+txtSizeX+txtIntvX+txtLblXsize/2-80, txtSrtY1+12*txtLblYsize+10*txtIntvY, 90, 18);		
		lblDMI9.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+13*txtLblYsize+11*txtIntvY, txtLblXsize, txtLblYsize);	

		lblPressureInfromation.setBounds((txtIntvX*3+txtSizeX+txtLblXsize)/2-200/2, txtSrtY1+14*txtLblYsize+12*txtIntvY, 200, 30);		
		lblPI1.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+15*txtLblYsize+14*txtIntvY, txtLblXsize, txtLblYsize);
		lblPI2.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+16*txtLblYsize+15*txtIntvY, txtLblXsize, txtLblYsize);
		lblPI3.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+17*txtLblYsize+16*txtIntvY, txtLblXsize, txtLblYsize);
		lblPI4.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+18*txtLblYsize+17*txtIntvY, txtLblXsize, txtLblYsize);
		lblPI5.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+19*txtLblYsize+17*txtIntvY, txtLblXsize, txtLblYsize);
		lblPI6.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+20*txtLblYsize+18*txtIntvY, txtLblXsize, txtLblYsize);
		lblPI7.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+21*txtLblYsize+19*txtIntvY, txtLblXsize, txtLblYsize);
		lblPI8.setBounds(txtSrtX+txtSizeX+txtIntvX, txtSrtY1+22*txtLblYsize+20*txtIntvY, txtLblXsize, txtLblYsize);


		ChokeKillLines.setBounds(pan1Xsrt+btnSizeX*4+panIntv*4, 10+btnSizeY*2+5*3+23+panIntv*2+pnlPumpRateSizeY+87+8, pnlPumpRateSizeX, 117);
		ChokeKillLines.setLayout(null);
		ChokeKillLines.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		ChokeKillLines.setBackground(SystemColor.menu);

		contentPane.add(ChokeKillLines);
		ChokeKillLines.setVisible(false);

		JLabel lblChokekillLinesOpen = new JLabel("Choke/Kill Lines Open Status");
		lblChokekillLinesOpen.setHorizontalAlignment(SwingConstants.CENTER);
		lblChokekillLinesOpen.setFont(new Font("굴림", Font.BOLD, 15));
		lblChokekillLinesOpen.setBounds(0, 10, 264, 15);
		ChokeKillLines.add(lblChokekillLinesOpen);
		optChoke.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iKill = 0;
				MainDriver.iChoke = 1;
				cmdOk.setVisible(true);
			}
		});

		optChoke.setBounds(18, 36, 127, 20);
		ChokeKillLines.add(optChoke);		
		optKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iKill = 1;
				MainDriver.iChoke = 0;
				cmdOk.setVisible(true);
			}
		});

		optKill.setBounds(18, 56, 110, 20);
		ChokeKillLines.add(optKill);		
		optBoth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iKill = 1;
				MainDriver.iChoke = 1;
				cmdOk.setVisible(true);
			}
		});

		optBoth.setBounds(18, 76, 110, 20);
		ChokeKillLines.add(optBoth);

		ChokeKillGroup.add(optChoke);
		ChokeKillGroup.add(optKill);
		ChokeKillGroup.add(optBoth);

		cmdOk.setBounds(130, 61, 112, 30);
		ChokeKillLines.add(cmdOk);


		cmdOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// If igERD = 1 Then
				//				          MsgBox "This is END of current update for multilateral well control !", 0, msgTitle
				//				          Exit Sub    //2003/8/20, currently set
				// End If
				if(TimerDrillOn==1){
					TimerDrill.cancel();
					TimerDrillOn=0;
				}
				if(TimerSIOn==1) {
					TimerSI.cancel();
					TimerSIOn=0;
					MainDriver.SItaskOn2=0;
				}
				if(TimerWarnOn==1){
					TimerWarn.cancel();
					TimerWarnOn=0;
				}
				if(iShutinData == 0){ //for multiple runs after retriving input data
					if(MainDriver.imud==0) {
						set2Pcal();
						//AdjustCell();
					}
					else OBM_set2Pcal();			    	
				}
				//................ After retriving shutin data, operation conditions can be different (including CL, KL selections)
				MainDriver.iBOP = 0;   //BOP is closed for calculation purpose: needed for shutin data retrieval
				CalChkVol();  //calculate inside choke volume and total outside volume

				//------------------// modofication to match Nickens// results
				//...... 99 psi for k = 2500 md, 82.5 psif for k = 300 md, 52.5 psi for k = 50 md
				//				    pb = 0.052 * Kmud * Vdepth + 14.7 //+ 52.5, BUT, need to set back after the comparison  //7/11/02
				//				                                               //to match with Nickens results !
				//
				if(iDataChange != 0 || iShutinData == 1)  get_SIDcalculations(); //input data has been changed or retrive a new SID ! (7/15/02)
				//required calculations for multiple runs !
				MainDriver.MethodOrig = MainDriver.Method;
				//
				Sm1.m3.paintIndex2=0; // 20130917 ajw
				Sa1.m3.paintIndex2=0;

				if(MainDriver.iProblem[3]==1){
					MainDriver.oMud = MainDriver.oMud_save;
					MainDriver.gMudOld = 0.052*MainDriver.oMud_save;					
				}
				
				if(MainDriver.iCHKcontrol == 1){
					MainDriver.imode = 2;
					Sa1.Form_Load();
					Sa1.setVisible(true);			    	
					Sm1.dispose();
					MainDriver.AutoOn=1;
					MainDriver.ManualOn=0;
				}

				else{
					MainDriver.imode = 1;
					Sm1.Form_Load();
					//Sm1.ManualSelect.setSelected(true);
					//Sm1.txtSIMmode.setText("Manual");
					MainDriver.Method = 1;    //always start with Driller//s Method					
					MainDriver.gMudCirc = MainDriver.gMudOld;
					MainDriver.Cmud = MainDriver.oMud;
					Sm1.setVisible(true);			    	
					Sa1.dispose();
					MainDriver.AutoOn=0;
					MainDriver.ManualOn=1;		
					MainDriver.volPump2 = MainDriver.volPump; //140826 ajw
					MainDriver.volPump = 0;
				}
				//Unload DrillSI    //unload for the several runs

				menuClose();
			}
		});

		txtN2phase.setBounds(319, 204, 81, 24);
		//contentPane.add(txtN2phase);

		MainDriver.iWshow=1;
		btnSrtSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				if(MainDriver.gTcum <= 0.5){// if(gTcum>0.5) Then Exit Sub
					iDataChange = 0;
					if(MainDriver.igERD == 1){
						//TimerTrip.setEnabled = True
						btnPumpOn1.setEnabled(false);
						btnPumpOff1.setEnabled(false);
						btnPumpOn2.setEnabled(false);
						btnPumpOff2.setEnabled(false);
						btnDrillOn.setEnabled(false);
						btnDrillOff.setEnabled(false);
						cmdKill.setEnabled(false);
						MainDriver.operMsg ="Tripping simulation is ready ...";
						lblPause.setText(MainDriver.operMsg);
					}
					else{
						if(TimerDrillOn==0){
							TimerDrill = new Timer();
							TimerDrill.schedule(new TDTask(), 2000, TimeDrlIntv);//TimerDrill.setEnabled = True
							//TimerDrill.schedule(new TDTask(), 2000, 1);//히히
							TimerDrillOn=1;
						}
						MainDriver.operMsg = "Well control simulation is on ...";
						lblPause.setText(MainDriver.operMsg);						
					}
					//MainDriver.iWshow=1;				
					//if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true); //20131022 ajw
					btnSrtSim.setEnabled(false);
					mSrtSim.setEnabled(false);			
				}
			}});			

		btnDrillOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//... modified just to start to rotate a rotary table
				if(iShutinData != 1 && DrillbtnOn==0 && TimerDrillOn!=0){
					iROP = 1;
					//if (iWshow == 1) Sm1.m3.setVisible(true);					
					DrillAssign();					
					if (Sm1.m3.TimerRotOn==0){
						Sm1.m3.TimerRot = new Timer();
						Sm1.m3.TimerRot.schedule(Sm1.m3.new TRTask(), 0, TimeDrlIntv);
						Sm1.m3.TimerRotOn=1;
					}					
					DrillbtnOn=1;
					MainDriver.iBOP=1; //20140218 ajw
					//btnDrillOn.setEnabled(false);					
					if(DrillbtnOn*iPumpOn1==1){
						btnPumpOff1.setEnabled(true);
						btnDrillOff.setEnabled(true);
						cmdKill.setEnabled(true);
						btnShutIn.setEnabled(true);
					}
					if(DrillbtnOn*iPumpOn2==1){
						btnPumpOff2.setEnabled(true);
						btnDrillOff.setEnabled(true);
						cmdKill.setEnabled(true);
						btnShutIn.setEnabled(true);
					}
					RPMScroll.setValue((int)MainDriver.RPM_now);
					WOBScroll.setValue((int)MainDriver.WOB_now);
				}				
			}});

		btnDrillOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(iShutinData != 1){
					iROP = 0;
					txtROP.setText(((new DecimalFormat("##0.0#")).format(0.5 * MainDriver.ROPen)));
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					if (Sm1.m3.TimerRotOn==1){
						Sm1.m3.TimerRot.cancel();
						Sm1.m3.TimerRotOn=0;
					}
					DrillbtnOn=0;

					iPumpOn1 = 0;
					txtPumpStatus1.setText("Off");
					MainDriver.spMinD1=0;
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
					
					pumpScroll1.setValue((int)(MainDriver.spMinD1 + 0.05));
					pumpScroll2.setValue((int)(MainDriver.spMinD2 + 0.05));

					MainDriver.RPM_now = 0;
					MainDriver.Torque_now = 0;

					RPMScroll.setValue((int)MainDriver.RPM_now);

					txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
					txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
					txtPumpRate.setText((new DecimalFormat("##,##0")).format(0.5 * MainDriver.Qdrill));
					double qtmp = Qt - 0.5 * MainDriver.Qdrill;
					qtmp = Math.max(qtmp, 0);
					if(MainDriver.iProblem_occured[2]==1) qtmp = qtmp - MainDriver.Q_loss;
					txtReturnRate.setText((new DecimalFormat("#,##0")).format(qtmp));
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					if (iROP == 1 && Sm1.m3.TimerRotOn==1){
						Sm1.m3.TimerRot.cancel();
						Sm1.m3.TimerRotOn=0;
						//Sm1.m3.TimerRot2.schedule(Sm1.m3.Task1, 0, 1000);					   
						//WellPic.TimerRot.setEnabled = True  //?
					}
					DrillAssign();
				}
			}});

		btnPumpOff1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(iShutinData != 1){
					iPumpOn1 = 0;
					txtPumpStatus1.setText("Off");
					MainDriver.spMinD1=0;
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;

					pumpScroll1.setValue((int)(MainDriver.spMinD1 + 0.05));
					pumpScroll2.setValue((int)(MainDriver.spMinD2 + 0.05));
					txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
					txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
					txtPumpRate.setText((new DecimalFormat("##,##0")).format(0.5 * MainDriver.Qdrill));
					double qtmp = Qt - 0.5 * MainDriver.Qdrill;
					qtmp = Math.max(qtmp, 0);
					if(MainDriver.iProblem_occured[2]==1) qtmp = qtmp - MainDriver.Q_loss;
					txtReturnRate.setText((new DecimalFormat("#,##0")).format(qtmp));
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					if (iROP == 1 && Sm1.m3.TimerRotOn==1){
						Sm1.m3.TimerRot.cancel();
						Sm1.m3.TimerRotOn=0;
						//Sm1.m3.TimerRot2.schedule(Sm1.m3.Task1, 0, 1000);					   
						//WellPic.TimerRot.setEnabled = True  //?
					}
					DrillAssign();		
				}
			}
		});	

		btnPumpOff2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(iShutinData != 1){
					iPumpOn2 = 0;
					txtPumpStatus2.setText("Off");
					MainDriver.spMinD2=0;
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1+ MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;

					pumpScroll1.setValue((int)(MainDriver.spMinD1 + 0.05));
					pumpScroll2.setValue((int)(MainDriver.spMinD2 + 0.05));
					txtPumpR1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
					txtPumpR2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
					txtPumpRate.setText((new DecimalFormat("##,##0")).format(0.5 * MainDriver.Qdrill));
					double qtmp = Qt - 0.5 * MainDriver.Qdrill;
					qtmp = Math.max(qtmp, 0);
					if(MainDriver.iProblem_occured[2]==1) qtmp = qtmp - MainDriver.Q_loss;
					txtReturnRate.setText((new DecimalFormat("#,##0")).format(qtmp));
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					if (iROP == 1 && Sm1.m3.TimerRotOn==1){
						Sm1.m3.TimerRot.cancel();
						Sm1.m3.TimerRotOn=0;
						//Sm1.m3.TimerRot2.schedule(Sm1.m3.Task1, 0, 1000);					   
						//WellPic.TimerRot.setEnabled = True  //?
					}
					DrillAssign();
				}
			}
		});	

		btnShutIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//...... simply shut in the well for tripping simulation; 2003. 7. 11.
				//if(MainDriver.iBOP !=0){				    	
				//
				if(iROP!=0 || iPumpOn1 != 0 || iPumpOn2!=0){
					String Msg= "Pump is still on or you are still drilling ! Not good conditions for well shut in";//, 0, msgTitle
					JOptionPane.showMessageDialog(null, Msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
					//if (iWshow = 1) Then WellPic.Show
					//if (iROP = 1) Then WellPic.TimerRot.setEnabled = True
				}
				else{
					MainDriver.iBOP = 0;				    	
					//if (iWshow = 1) Then WellPic.Show;
					DrillAssign();
				}

			}
		});			

		cmdKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double DPmin =0;   //7/31/2003				
				if(iShutinData != 1){					

					//.......................... set DP max. from 1 to 5 psi
					if (MainDriver.igERD == 0 && Pdiff < -4.99) {				    	
						String Msg= "The well has not yet stablized within 5 psi ===> You might have wrong shut-in data";
						JOptionPane.showMessageDialog(null, Msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
						if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					}
					else{
						if (MainDriver.igERD == 1){
							//...... assign some values of necessary for output display
							Qcirc = 0; Qt = 0; P2form = MainDriver.Pform;   //assign formation p.
							timeNp = 2;  //do not save the result
							ShowResult();
							txtSimRate.setText(Integer.toString(MainDriver.SimRate));
							txtKillMudWt.setText((new DecimalFormat("##.0#")).format(MainDriver.Kmud*100/100));
							//SSTabTrip.setVisible(false)
							//DPmin = Val(txtDPsafety.Text);  //it should be greater than DP_fric_bot for smallest KOP
							if (DPmin < 0) DPmin = 0;    //ignore negative value; Set this for the final version !
							MainDriver.Pb =  (MainDriver.Pb + DPmin);    //this is a practical BHP guideline for ML WC
						}
						else{
							MainDriver.iChoke = 1; MainDriver.iKill = 0;  //default CL, KL status, 7/14/02
							//MainDriver.iCHKcontrol = 1;   //default is automatic control
							MainDriver.iCHKcontrol = 2;   //default is manual control 151109 AJW
							MainDriver.iCHKcontrol2 = 1; // real default is automatic hh
							
							setKillConditions();
							menuSaveSID.setEnabled(true);    //to save and open shutin data for multiple runs.
							menuShowData.setEnabled(true);   //to save and open shutin data for multiple runs.
						}
					}

				}

				//
			}
		}
				);				

		txtL.OKbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				dummy = CheckShutinData();
				if(dummy==1){
					dummy=OpenShutinData();
					iShutinData = 1;
					setKillConditions();
					menuShowData.setEnabled(true);

					MainDriver.iBOP=0;
					iROP=0;
					MainDriver.iWshow=0;
					timeNp=2;
					iPumpOn1=0;
					iPumpOn2=0;
					Qcirc=0;
					Qt=0;
					pumpP=psia;

					MainDriver.gTcum = MainDriver.TTsec[MainDriver.NpSi];
					TotalStroke=Stroke1+Stroke2;
					MainDriver.Stroke[MainDriver.NpSi] = (int)TotalStroke;
					MainDriver.StrokePump1[MainDriver.NpSi] = (int)Stroke1;
					MainDriver.StrokePump2[MainDriver.NpSi] = (int)Stroke2;
					standpipeP = MainDriver.SIDPP;
					Pchoke = MainDriver.Pchk[MainDriver.NpSi];  //consistent with SID data rather than sicp
					P2form = MainDriver.Pform;
					QtotVol = MainDriver.Vpiti;
					MainDriver.Vpiti = QtotVol;
					MainDriver.Np = MainDriver.NpSi;
					//obm의 경우는 녹아있기 때문에 이걸 없앨 수 없다
					//If pvtZb(N2phase) < 0.000002 Then N2phase = N2phase - 1  //to prevent very small amount
					//
					txtSimRate.setText("1");
					txtKillMudWt.setText((new DecimalFormat("##.0#")).format(MainDriver.Kmud));
					ShowResult();
					//........ calculate (drilling rate and) kill rate since day are not updated or you need to open it.
					lblPause.setText("Shut-in data are retrieved !");
					Sm1.m3.form_Load();
					Sa1.m3.form_Load();
					SIDTimerWarn = 0;
					SIDTimerDrill = 0;
					SIDTimerShutin = 0;
					SIDSm1TRTaskOn = 0;
					SIDSa1TRTaskOn = 0;
					txtL.menuClose();
				}
				else if(dummy==0){					
					txtL.menuClose();
				}
			}
		});

		txtL.Applybutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dummy = CheckShutinData();				
				if(dummy==1){
					dummy = OpenShutinData();
					iShutinData = 1;
					setKillConditions();
					menuShowData.setEnabled(true);

					MainDriver.iBOP=0;
					iROP=0;
					MainDriver.iWshow=0;
					timeNp=2;
					iPumpOn1=0;
					iPumpOn2=0;
					Qcirc=0;
					Qt=0;
					pumpP=psia;

					MainDriver.gTcum = MainDriver.TTsec[MainDriver.NpSi];
					TotalStroke=Stroke1+Stroke2;
					MainDriver.Stroke[MainDriver.NpSi] = (int)TotalStroke;
					MainDriver.StrokePump1[MainDriver.NpSi] = (int)Stroke1;
					MainDriver.StrokePump2[MainDriver.NpSi] = (int)Stroke2;
					standpipeP = MainDriver.SIDPP;
					Pchoke = MainDriver.Pchk[MainDriver.NpSi];  //consistent with SID data rather than sicp
					P2form = MainDriver.Pform;
					QtotVol = MainDriver.Vpiti;
					MainDriver.Vpiti = QtotVol;
					MainDriver.Np = MainDriver.NpSi;
					//obm의 경우는 녹아있기 때문에 이걸 없앨 수 없다
					//If pvtZb(N2phase) < 0.000002 Then N2phase = N2phase - 1  //to prevent very small amount
					//
					txtSimRate.setText("1");
					txtKillMudWt.setText((new DecimalFormat("##.0#")).format(MainDriver.Kmud));
					ShowResult();
					//........ calculate (drilling rate and) kill rate since day are not updated or you need to open it.
					Sm1.m3.form_Load();
					Sa1.m3.form_Load();
					lblPause.setText("Shut-in data are retrieved !");
					SIDTimerWarn = 0;
					SIDTimerDrill = 0;
					SIDTimerShutin = 0;
					SIDSm1TRTaskOn = 0;
					SIDSa1TRTaskOn = 0;
				}
			}
		});

		txtL.Cancelbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {					
				txtL.menuClose();
			}
		});

		set_Default();
		setConventionalERD();
	}

	int CheckShutinData(){
		String strTmp =txtL.textArea.getText();
		double timeMinute =0;
		Scanner sc = new Scanner(strTmp);
		sc= new Scanner(strTmp);
		sc.useDelimiter("\\s* \\s*");
		sc.useDelimiter("\\s*\t\\s*");
		int dummyInt=0;
		int dummyN2phase=0;
		int dummyNpSi=0;
		double dummyDouble=0;
		//----------------------------------  Read input data from a file specified.
		//		
		//
		//Line Input #8, strTmp: Line Input #8, strTmp: Line Input #8, strTmp    //ignore top 3 lines
		//-----
		//Line Input #8, strTmp //===>> Control Data"

		try{
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			//-----Fluid properties and bit nozzle data
			dummyInt = sc.nextInt();
			sc.nextLine();
			dummyInt = sc.nextInt();
			sc.nextLine();
			dummyInt = sc.nextInt();
			sc.nextLine();
			dummyInt = sc.nextInt();
			sc.nextLine();
			/*MainDriver.iPump dummyInt = sc.nextInt();
			sc.nextLine();*/
			dummyInt = sc.nextInt();
			sc.nextLine();
			dummyInt = sc.nextInt();
			sc.nextLine();
			//---------------------------------------------------------------------- after 7/1/02: ML and ERD
			dummyInt = sc.nextInt(); //iMudComp for mud compressibility
			sc.nextLine();
			dummyInt = sc.nextInt(); //iBlowout for blowout animation
			sc.nextLine();
			dummyInt = sc.nextInt(); //hs
			sc.nextLine();
			dummyInt = sc.nextInt(); //hs
			sc.nextLine();
			dummyInt = sc.nextInt(); //TY
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			//-----Fluid properties and bit nozzle data		
			dummyInt = sc.nextInt();
			sc.nextLine();
			dummyDouble = sc.nextDouble();
			sc.nextLine();
			dummyDouble = sc.nextDouble();
			sc.nextLine();
			if(MainDriver.Model == 0){
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
			}
			dummyDouble = sc.nextDouble(); sc.nextLine();
			if(MainDriver.iMudComp== 1){
				dummyDouble = sc.nextDouble(); sc.nextLine();  //mudComp
				//    MainDriver.MudComp = Val(Left(strtmp, 10)) * 0.000001 //mudComp
			}
			dummyDouble = sc.nextDouble(); sc.nextLine();
			if(MainDriver.Model== 2){
				dummyDouble = sc.nextDouble(); sc.nextLine();
			}
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();  //dnoz(1)
			dummyDouble = sc.nextDouble(); sc.nextLine();  //dnoz(2)
			dummyDouble = sc.nextDouble(); sc.nextLine();  //dnoz(3)
			dummyDouble = sc.nextDouble(); sc.nextLine();  //dnoz(4)
			sc.nextLine(); 
			sc.nextLine(); 
			//-----
			//===>> Well geometry and directional data"
			dummyInt = sc.nextInt(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			dummyDouble = sc.nextDouble(); sc.nextLine();
			//
			if(MainDriver.iOnshore == 2){ //Line input offshore data//
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
			}
			//
			MainDriver.iCduct =sc.nextInt(); sc.nextLine();
			if(MainDriver.iCduct == 1){
				dummyDouble = sc.nextDouble(); sc.nextLine();  //DepthCduct:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //ODcduct
			}
			//
			MainDriver.iSurfCsg =sc.nextInt(); sc.nextLine();  //iSurfCsg
			if(MainDriver.iSurfCsg == 1){
				dummyDouble = sc.nextDouble(); sc.nextLine();  //DepthSurfCsg:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //ODSurfCsg
			}
			// Directional data information
			// When new data file is retrieved, we need to calculate:
			//   angEOB, xHold, DepthKOP (only type 4), Rbur, R2bur, etc
			//
			if(MainDriver.iWell >= 1 && MainDriver.iWell <= 4){
				dummyDouble = sc.nextDouble(); sc.nextLine();  //DepthKOP:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //bur:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //bur2
				if(MainDriver.iWell == 4){
					dummyDouble = sc.nextDouble(); sc.nextLine();  //angeob
					dummyDouble = sc.nextDouble(); sc.nextLine();  //xhold
				}
				dummyDouble = sc.nextDouble(); sc.nextLine();  //ang2eob:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //hdisp:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //x2hold
			}
			sc.nextLine();
			sc.nextLine();
			//===>> Shut-in and formation properties data"
			dummyDouble = sc.nextDouble(); sc.nextLine();  //kickintens:
			dummyDouble = sc.nextDouble(); sc.nextLine();  //pitwarn:
			dummyDouble = sc.nextDouble(); sc.nextLine();  //perm
			dummyDouble = sc.nextDouble(); sc.nextLine();  //skins:
			dummyDouble = sc.nextDouble(); sc.nextLine();  //porosity:
			if(MainDriver.igERD == 1){
				MainDriver.gPayLength =sc.nextInt(); sc.nextLine();  //ropen:
			}
			else{
				dummyDouble = sc.nextDouble(); sc.nextLine();  //ropen:
			}
			//--------------- 8/01/02
			dummyInt = sc.nextInt(); sc.nextLine();  //iFG
			if(MainDriver.iFG== 0){
				dummyInt = sc.nextInt(); sc.nextLine();  //iFGnum
				for(int i = 0; i<MainDriver.iFGnum; i++){
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();	          
					sc.nextLine();
				}
			}
			//
			if(MainDriver.iWell == 4 && Math.abs(MainDriver.ang2EOB - 90) < 0.01){
				dummyInt = sc.nextInt(); sc.nextLine();  //ihresv
			}
			sc.nextLine();
			sc.nextLine();

			//===>> Pump data and other information"
			/*MainDriver.DiaLiner dummyDouble = sc.nextDouble(); sc.nextLine();  //DiaLiner
			if(MainDriver.iPump == 2){
				MainDriver.Drod dummyDouble = sc.nextDouble(); sc.nextLine();  //drod
				}
			StrokeLength dummyDouble = sc.nextDouble(); sc.nextLine();  //strokeLength:
			MainDriver.Effcy dummyDouble = sc.nextDouble(); sc.nextLine();*/  //effcy:
			dummyDouble = sc.nextDouble(); sc.nextLine();  //spmind:
			dummyDouble = sc.nextDouble(); sc.nextLine();  //spmin
			dummyDouble = sc.nextDouble(); sc.nextLine();  //spmind:
			dummyDouble = sc.nextDouble(); sc.nextLine();  //spmin
			dummyDouble = sc.nextDouble(); sc.nextLine();  //stroke1
			dummyDouble = sc.nextDouble(); sc.nextLine();  //stroke2
			//
			dummyInt = sc.nextInt(); sc.nextLine();  //itype
			if(MainDriver.iType >= 1 && MainDriver.iType <= 4){
				dummyDouble = sc.nextDouble(); sc.nextLine();  //heqval:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //LengthSurfLine:
				dummyDouble = sc.nextDouble(); sc.nextLine();  //DiaSurfLine
			}
			dummyDouble = sc.nextDouble(); sc.nextLine();  //DchkControl
			//-----
			//hs
			if(MainDriver.iHeattrans == 1){
				sc.nextLine();  //===>> Heat Transfer Data"
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
			}
			if(MainDriver.imud == 1){
				sc.nextLine(); //===>> Mud Data"
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyInt = sc.nextInt(); sc.nextLine();
				//
				//
				if(MainDriver.ibaseoil == 4){
					for(int i = 0; i<=16; i++){
						dummyDouble = sc.nextDouble(); sc.nextLine();
					}
					dummyDouble = sc.nextDouble(); sc.nextLine();
				}
				//
			}
			//----
			//   Trip and ML related data, 8/4/2003
			if(MainDriver.igERD == 1){
				sc.nextLine();
				sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyInt = sc.nextInt(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine();
				dummyInt = sc.nextInt(); sc.nextLine();
				sc.nextLine();
				for(int ii = 0; ii<MainDriver.igMLnumber - 1; ii++){
					dummyInt = sc.nextInt(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					sc.nextLine();
				}
				sc.nextLine();
				for(int ii = 0; ii<(MainDriver.igMLnumber - 1); ii++){
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();  
					dummyDouble = sc.nextDouble();  
					dummyDouble = sc.nextDouble();  
					dummyDouble = sc.nextDouble();  
					dummyDouble = sc.nextDouble(); 
					sc.nextLine();
				}
				//...... open additional data for kick influx and related calculations

				sc.nextLine();
				for(int ii = 0; ii<(MainDriver.igMLnumber - 1); ii++){
					dummyDouble = sc.nextDouble();  
					dummyDouble = sc.nextDouble();  
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					sc.nextLine();
				}
			}  //..... of ERD = 1
			//
			sc.nextLine();
			sc.nextLine();
			//......... Additional control or operational data
			//===>> Operational data"
			dummyInt = sc.nextInt(); sc.nextLine();  //iCHKcontrol:
			dummyInt = sc.nextInt(); sc.nextLine();  //ichoke:
			dummyInt = sc.nextInt(); sc.nextLine();  //ikill
			sc.nextLine();   //one blank line
			//
			//-------------------------------------------------------------------------------------- 7/13/02
			// read additional shutin data for multiple runs
			//			   
			sc.nextLine(); 
			sc.nextLine(); 
			sc.nextLine(); 
			sc.nextLine(); 

			dummyDouble = sc.nextDouble(); sc.nextLine();    //pb, Bottomhole Pressure maintained, psia"
			dummyDouble = sc.nextDouble(); sc.nextLine();    //Formation Pressure, psia"
			dummyDouble = sc.nextDouble(); sc.nextLine();    //Formation Over-Pressure, psi"
			dummyDouble = sc.nextDouble(); sc.nextLine();  //Kmud, Kill Mud Weight, ppg"
			dummyDouble = sc.nextDouble(); sc.nextLine();  //Cmud, Circulating Mud Weight, ppg"
			dummyDouble = sc.nextDouble(); sc.nextLine();   //q, Pump1 capacity, bbl/str"
			dummyDouble = sc.nextDouble(); sc.nextLine();   //q, Pump2 capacity, bbl/str"
			dummyDouble = sc.nextDouble(); sc.nextLine(); //sidpp, Shut-in Drill Pipe Pressure, psia"
			dummyDouble = sc.nextDouble(); sc.nextLine();  //sicp, Shut-in Casing Pressure, psia"
			dummyDouble = sc.nextDouble(); sc.nextLine();  //Pcasing, Casing seat pressure, psia"
			dummyDouble = sc.nextDouble(); sc.nextLine();    //Vpiti, initial mixure volume, bbls"
			dummyDouble = sc.nextDouble(); sc.nextLine();   //PVgain, Net pit volume gain, bbls"
			dummyDouble = sc.nextDouble(); sc.nextLine(); //PVgain, Net pit volume gain in MLs, bbls"
			dummyDouble = sc.nextDouble(); sc.nextLine();   //dxSlip, kick migration distance, ft"
			dummyDouble = sc.nextDouble(); sc.nextLine();    //vdBit, Vertical depth of the well, ft"
			dummyDouble = sc.nextDouble(); sc.nextLine();    //hdBit, Horizonal departure of the well, ft"
			dummyDouble = sc.nextDouble(); sc.nextLine();   //Qdrill, Pump circ. rate while drilling, gpm"
			dummyDouble = sc.nextDouble(); sc.nextLine();    //Qkill, Pump circ. rate while well kill, gpm"

			//
			//---------------------Directional Data Print Out !

			if(MainDriver.iWell >= 1){
				sc.nextLine(); 
				sc.nextLine();
				dummyDouble = sc.nextDouble(); sc.nextLine(); //Rbur, Radius of Curvature @ 1st build, ft"
			}
			if(MainDriver.iWell >= 3){
				dummyDouble = sc.nextDouble(); sc.nextLine(); //R2bur, Radius of Curvature @ 2nd build, ft"
			}
			//
			sc.nextLine();
			dummyInt = sc.nextInt(); sc.nextLine();   //NwcS, Index for the starting of well geometry data"
			dummyInt = sc.nextInt(); sc.nextLine();   //NwcE, Index for the end of well geometry data"
			sc.nextLine();
			//
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();//3 description lines

			for(int i = MainDriver.NwcE-1; i>=MainDriver.NwcS-1; i--){
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				sc.nextLine();
			}
			//....... 2-phase Kick information from well shut in and well stabilization !
			//-----------------------------------------------------------------------------------------------------
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			dummyN2phase = sc.nextInt(); sc.nextLine();   //n2phase, Total number of 2-phase mixture slug"
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			if(MainDriver.imud == 0){ //WBM
				for(int i = 0; i<dummyN2phase; i++){
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble(); 
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					sc.nextLine();
				}
			}
			else{ //OBM
				for(int i = 0; i<dummyN2phase; i++){
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					dummyDouble = sc.nextDouble();
					sc.nextLine();
				}
			}
			//-----------------------------------------------------------------------------------------------------
			//.............................. ........ read final results with time
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			dummyNpSi = (int)sc.nextDouble(); sc.nextLine();   //Npsi, Data saving index of the results"			   
			//
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			for(int i = 0; i<=dummyNpSi; i++){
				dummyDouble = sc.nextDouble();   	
				dummyDouble = timeMinute * 60;
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				sc.nextLine();
			}
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			//		    
			for(int i = 0; i<=dummyNpSi; i++){
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				sc.nextLine();
				//        ttSec(i) = timeMinute * 60#
			}
			//
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			for(int i = 0; i<=dummyNpSi; i++){
				dummyDouble = sc.nextDouble();
				dummyInt = (int)(sc.nextDouble());
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				dummyDouble = sc.nextDouble();
				sc.nextLine();			    	
			}
			return 1;
		}catch(Exception e){
			String msgTmp = "";
			msgTmp = "Shutin data file format has been changed and reading errors have occurred."+"\n";
			msgTmp = msgTmp + "  You should not use this data and also you need to check all input data"+"\n";
			msgTmp = msgTmp + "  from the Main Menu. The sub quits here.";
			//    Resume Next   //it may gives too many error messages !
			JOptionPane.showMessageDialog(null, msgTmp, "Warning", JOptionPane.INFORMATION_MESSAGE);
			return 0;
		}
	}

	int OpenShutinData(){
		//		
		String strTmp =txtL.textArea.getText();
		double timeMinute =0;
		Scanner sc = new Scanner(strTmp);
		sc= new Scanner(strTmp);
		sc.useDelimiter("\\s* \\s*");
		sc.useDelimiter("\\s*\t\\s*");
		//----------------------------------  Read input data from a file specified.
		//		
		//
		//Line Input #8, strTmp: Line Input #8, strTmp: Line Input #8, strTmp    //ignore top 3 lines
		//-----
		//Line Input #8, strTmp //===>> Control Data"

		try{
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			//-----Fluid properties and bit nozzle data
			MainDriver.igERD= sc.nextInt();
			sc.nextLine();
			MainDriver.iOnshore= sc.nextInt();
			sc.nextLine();
			MainDriver.Method= sc.nextInt();
			sc.nextLine();
			MainDriver.Model= sc.nextInt();
			sc.nextLine();
			/*MainDriver.iPump= sc.nextInt();
			sc.nextLine();*/
			MainDriver.iDelp= sc.nextInt();
			sc.nextLine();
			MainDriver.iZfact= sc.nextInt();
			sc.nextLine();
			//---------------------------------------------------------------------- after 7/1/02: ML and ERD
			MainDriver.iMudComp= sc.nextInt(); //iMudComp for mud compressibility
			sc.nextLine();
			MainDriver.iBlowout= sc.nextInt(); //iBlowout for blowout animation
			sc.nextLine();
			MainDriver.imud= sc.nextInt(); //hs
			sc.nextLine();
			MainDriver.iHeattrans= sc.nextInt(); //hs
			sc.nextLine();
			MainDriver.imultikick= sc.nextInt(); //TY
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			//-----Fluid properties and bit nozzle data		
			MainDriver.iData= sc.nextInt();
			sc.nextLine();
			MainDriver.S600= sc.nextDouble();
			sc.nextLine();
			MainDriver.S300= sc.nextDouble();
			sc.nextLine();
			if(MainDriver.Model == 0){
				MainDriver.SS100 =sc.nextDouble(); sc.nextLine();
				MainDriver.SS3 =sc.nextDouble(); sc.nextLine();
			}
			MainDriver.oMud =sc.nextDouble(); sc.nextLine();
			if(MainDriver.iMudComp== 1){
				MainDriver.MudComp =sc.nextDouble(); sc.nextLine();  //mudComp
				//    MainDriver.MudComp = Val(Left(strtmp, 10)) * 0.000001 //mudComp
			}
			MainDriver.RenC =sc.nextDouble(); sc.nextLine();
			if(MainDriver.Model== 2){
				MainDriver.Ruf =sc.nextDouble(); sc.nextLine();
			}
			MainDriver.gasGravity =sc.nextDouble(); sc.nextLine();
			MainDriver.fCO2 =sc.nextDouble(); sc.nextLine();
			MainDriver.fH2S =sc.nextDouble(); sc.nextLine();
			MainDriver.Tsurf =sc.nextDouble(); sc.nextLine();
			MainDriver.Tgrad =sc.nextDouble(); sc.nextLine();
			MainDriver.Dnoz[0] =sc.nextDouble(); sc.nextLine();  //dnoz(1)
			MainDriver.Dnoz[1] =sc.nextDouble(); sc.nextLine();  //dnoz(2)
			MainDriver.Dnoz[2] =sc.nextDouble(); sc.nextLine();  //dnoz(3)
			MainDriver.Dnoz[3] =sc.nextDouble(); sc.nextLine();  //dnoz(4)
			sc.nextLine(); 
			sc.nextLine(); 
			//-----
			//===>> Well geometry and directional data"
			MainDriver.iWell =sc.nextInt(); sc.nextLine();
			MainDriver.Vdepth =sc.nextDouble(); sc.nextLine();
			MainDriver.DepthCasing =sc.nextDouble(); sc.nextLine();
			MainDriver.IDcasing =sc.nextDouble(); sc.nextLine();
			MainDriver.DiaHole =sc.nextDouble(); sc.nextLine();
			MainDriver.doDP =sc.nextDouble(); sc.nextLine();
			MainDriver.diDP =sc.nextDouble(); sc.nextLine();
			MainDriver.LengthHWDP =sc.nextDouble(); sc.nextLine();
			MainDriver.doHWDP =sc.nextDouble(); sc.nextLine();
			MainDriver.diHWDP =sc.nextDouble(); sc.nextLine();
			MainDriver.LengthDC =sc.nextDouble(); sc.nextLine();
			MainDriver.doDC =sc.nextDouble(); sc.nextLine();
			MainDriver.diDC =sc.nextDouble(); sc.nextLine();
			//
			if(MainDriver.iOnshore == 2){ //Line input offshore data//
				MainDriver.Dwater =sc.nextDouble(); sc.nextLine();
				MainDriver.tWgrad =sc.nextDouble(); sc.nextLine();
				MainDriver.Driser =sc.nextDouble(); sc.nextLine();
				MainDriver.Dchoke =sc.nextDouble(); sc.nextLine();
				MainDriver.Dkill =sc.nextDouble(); sc.nextLine();
			}
			//
			MainDriver.iCduct =sc.nextInt(); sc.nextLine();
			if(MainDriver.iCduct == 1){
				MainDriver.DepthCduct =sc.nextDouble(); sc.nextLine();  //DepthCduct:
				MainDriver.ODcduct =sc.nextDouble(); sc.nextLine();  //ODcduct
			}
			//
			MainDriver.iSurfCsg =sc.nextInt(); sc.nextLine();  //iSurfCsg
			if(MainDriver.iSurfCsg == 1){
				MainDriver.DepthSurfCsg =sc.nextDouble(); sc.nextLine();  //DepthSurfCsg:
				MainDriver.ODsurfCsg =sc.nextDouble(); sc.nextLine();  //ODSurfCsg
			}
			// Directional data information
			// When new data file is retrieved, we need to calculate:
			//   angEOB, xHold, DepthKOP (only type 4), Rbur, R2bur, etc
			//
			if(MainDriver.iWell >= 1 && MainDriver.iWell <= 4){
				MainDriver.DepthKOP =sc.nextDouble(); sc.nextLine();  //DepthKOP:
				MainDriver.BUR =sc.nextDouble(); sc.nextLine();  //bur:
				MainDriver.BUR2 =sc.nextDouble(); sc.nextLine();  //bur2
				if(MainDriver.iWell == 4){
					MainDriver.angEOB =sc.nextDouble(); sc.nextLine();  //angeob
					MainDriver.xHold =sc.nextDouble(); sc.nextLine();  //xhold
				}
				MainDriver.ang2EOB =sc.nextDouble(); sc.nextLine();  //ang2eob:
				MainDriver.Hdisp =sc.nextDouble(); sc.nextLine();  //hdisp:
				MainDriver.x2Hold =sc.nextDouble(); sc.nextLine();  //x2hold
			}
			sc.nextLine();
			sc.nextLine();
			//===>> Shut-in and formation properties data"
			MainDriver.KICKintens =sc.nextDouble(); sc.nextLine();  //kickintens:
			MainDriver.pitWarn =sc.nextDouble(); sc.nextLine();  //pitwarn:
			MainDriver.Perm =sc.nextDouble(); sc.nextLine();  //perm
			MainDriver.Skins =sc.nextDouble(); sc.nextLine();  //skins:
			MainDriver.Porosity =sc.nextDouble(); sc.nextLine();  //porosity:
			if(MainDriver.igERD == 1){
				MainDriver.gPayLength =sc.nextInt(); sc.nextLine();  //ropen:
			}
			else{
				MainDriver.ROPen =sc.nextDouble(); sc.nextLine();  //ropen:
			}
			//--------------- 8/01/02
			MainDriver.iFG = sc.nextInt(); sc.nextLine();  //iFG
			if(MainDriver.iFG== 0){
				MainDriver.iFGnum = sc.nextInt(); sc.nextLine();  //iFGnum
				for(int i = 0; i<MainDriver.iFGnum; i++){
					MainDriver.PPdepth[i] = sc.nextDouble();
					MainDriver.PoreP[i] = sc.nextDouble();
					MainDriver.FracP[i] = sc.nextDouble();	          
					sc.nextLine();
				}
			}
			//
			if(MainDriver.iWell == 4 && Math.abs(MainDriver.ang2EOB - 90) < 0.01){
				MainDriver.iHresv = sc.nextInt(); sc.nextLine();  //ihresv
			}
			sc.nextLine();
			sc.nextLine();

			//===>> Pump data and other information"
			/*MainDriver.DiaLiner = sc.nextDouble(); sc.nextLine();  //DiaLiner
			if(MainDriver.iPump == 2){
				MainDriver.Drod = sc.nextDouble(); sc.nextLine();  //drod
				}
			StrokeLength = sc.nextDouble(); sc.nextLine();  //strokeLength:
			MainDriver.Effcy = sc.nextDouble(); sc.nextLine();*/  //effcy:
			MainDriver.spMinD1 = sc.nextDouble(); sc.nextLine();  //spmind:
			MainDriver.spMin1 = sc.nextDouble(); sc.nextLine();  //spmin
			MainDriver.spMinD2 = sc.nextDouble(); sc.nextLine();  //spmind:
			MainDriver.spMin2 = sc.nextDouble(); sc.nextLine();  //spmin
			Stroke1 = sc.nextDouble(); sc.nextLine();  //stroke1
			Stroke2 = sc.nextDouble(); sc.nextLine();  //stroke2
			//
			MainDriver.iType = sc.nextInt(); sc.nextLine();  //itype
			if(MainDriver.iType >= 1 && MainDriver.iType <= 4){
				MainDriver.Heqval = sc.nextDouble(); sc.nextLine();  //heqval:
				MainDriver.LengthSurfLine = sc.nextDouble(); sc.nextLine();  //LengthSurfLine:
				MainDriver.DiaSurfLine = sc.nextDouble(); sc.nextLine();  //DiaSurfLine
			}
			MainDriver.DchkControl = sc.nextDouble(); sc.nextLine();  //DchkControl
			//-----
			//hs
			if(MainDriver.iHeattrans == 1){
				sc.nextLine();  //===>> Heat Transfer Data"
				MainDriver.TconF = sc.nextDouble(); sc.nextLine();
				MainDriver.SheatF = sc.nextDouble(); sc.nextLine();
				MainDriver.HtrancF = sc.nextDouble(); sc.nextLine();
				MainDriver.TconM = sc.nextDouble(); sc.nextLine();
				MainDriver.SheatM = sc.nextDouble(); sc.nextLine();
				MainDriver.HtrancM = sc.nextDouble(); sc.nextLine();
				MainDriver.InjmudT = sc.nextDouble(); sc.nextLine();
			}
			if(MainDriver.imud == 1){
				sc.nextLine(); //===>> Mud Data"
				MainDriver.foil = sc.nextDouble(); sc.nextLine();
				MainDriver.fbrine = sc.nextDouble(); sc.nextLine();
				MainDriver.fadditive = sc.nextDouble(); sc.nextLine();
				MainDriver.ibaseoil = sc.nextInt(); sc.nextLine();
				//
				//
				if(MainDriver.ibaseoil == 4){
					for(int i = 0; i<=16; i++){
						MainDriver.Mfrac_C[i + 7] = sc.nextDouble(); sc.nextLine();
					}
					MainDriver.Mfrac_C[24] = sc.nextDouble(); sc.nextLine();
				}
				//
			}
			//----
			//   Trip and ML related data, 8/4/2003
			if(MainDriver.igERD == 1){
				sc.nextLine();
				sc.nextLine();
				MainDriver.gTripTankVolume = sc.nextDouble(); sc.nextLine();
				MainDriver.gTripTankHeight = sc.nextDouble(); sc.nextLine();
				MainDriver.gConnTime = sc.nextDouble(); sc.nextLine();
				MainDriver.gJointLength = sc.nextDouble(); sc.nextLine();
				MainDriver.igJointNumber = sc.nextInt(); sc.nextLine();
				MainDriver.gBleedPressure = sc.nextDouble(); sc.nextLine();
				MainDriver.gBleedTime = sc.nextDouble(); sc.nextLine();
				MainDriver.igMLnumber = sc.nextInt(); sc.nextLine();
				sc.nextLine();
				for(int ii = 0; ii<MainDriver.igMLnumber - 1; ii++){
					MainDriver.mlPlug[ii] = sc.nextInt(); 
					MainDriver.mlKOP[ii] = sc.nextDouble(); 
					MainDriver.mlKOPvd[ii] = sc.nextDouble(); 
					MainDriver.mlKOPang[ii] = sc.nextDouble(); 
					MainDriver.mlBUR[ii] = sc.nextDouble(); 
					MainDriver.mlEOB[ii] = sc.nextDouble(); 
					MainDriver.mlHold[ii] = sc.nextDouble(); 
					MainDriver.mlBUR2nd[ii] = sc.nextDouble(); 
					MainDriver.mlEOB2nd[ii] = sc.nextDouble(); 
					MainDriver.mlHold2nd[ii] = sc.nextDouble(); 
					sc.nextLine();
				}
				sc.nextLine();
				for(int ii = 0; ii<(MainDriver.igMLnumber - 1); ii++){
					MainDriver.mlDia[ii] = sc.nextDouble();
					MainDriver.mlPform[ii] = sc.nextDouble();  
					MainDriver.mlPerm[ii] = sc.nextDouble();  
					MainDriver.mlPorosity[ii] = sc.nextDouble();  
					MainDriver.mlSkin[ii] = sc.nextDouble();  
					MainDriver.mlHeff[ii] = sc.nextDouble(); 
					sc.nextLine();
				}
				//...... open additional data for kick influx and related calculations

				sc.nextLine();
				for(int ii = 0; ii<(MainDriver.igMLnumber - 1); ii++){
					MainDriver.mlMD[ii] = sc.nextDouble();  
					MainDriver.mlTVD[ii] = sc.nextDouble();  
					MainDriver.mlQgTotM[ii] = sc.nextDouble(); 
					MainDriver.mlCti[ii] = sc.nextDouble(); 
					MainDriver.mlBg[ii] = sc.nextDouble(); 
					sc.nextLine();
				}
			}  //..... of ERD = 1
			//
			sc.nextLine();
			sc.nextLine();
			//......... Additional control or operational data
			//===>> Operational data"
			MainDriver.iCHKcontrol = sc.nextInt(); sc.nextLine();  //iCHKcontrol:
			MainDriver.iChoke = sc.nextInt(); sc.nextLine();  //ichoke:
			MainDriver.iKill = sc.nextInt(); sc.nextLine();  //ikill
			sc.nextLine();   //one blank line
			//
			//-------------------------------------------------------------------------------------- 7/13/02
			// read additional shutin data for multiple runs
			//			   
			sc.nextLine(); 
			sc.nextLine(); 
			sc.nextLine(); 
			sc.nextLine(); 

			MainDriver.Pb = sc.nextDouble(); sc.nextLine();    //pb, Bottomhole Pressure maintained, psia"
			MainDriver.Pform = sc.nextDouble(); sc.nextLine();    //Formation Pressure, psia"
			MainDriver.overP = sc.nextDouble(); sc.nextLine();    //Formation Over-Pressure, psi"
			MainDriver.Kmud = sc.nextDouble(); sc.nextLine();  //Kmud, Kill Mud Weight, ppg"
			MainDriver.Cmud = sc.nextDouble(); sc.nextLine();  //Cmud, Circulating Mud Weight, ppg"
			MainDriver.Qcapacity1 = sc.nextDouble(); sc.nextLine();   //q, Pump1 capacity, bbl/str"
			MainDriver.Qcapacity2 = sc.nextDouble(); sc.nextLine();   //q, Pump2 capacity, bbl/str"
			MainDriver.SIDPP = sc.nextDouble(); sc.nextLine(); //sidpp, Shut-in Drill Pipe Pressure, psia"
			MainDriver.SICP = sc.nextDouble(); sc.nextLine();  //sicp, Shut-in Casing Pressure, psia"
			Pcasing = sc.nextDouble(); sc.nextLine();  //Pcasing, Casing seat pressure, psia"
			MainDriver.Vpiti = sc.nextDouble(); sc.nextLine();    //Vpiti, initial mixure volume, bbls"
			PVgain = sc.nextDouble(); sc.nextLine();   //PVgain, Net pit volume gain, bbls"
			mlPVgain = sc.nextDouble(); sc.nextLine(); //PVgain, Net pit volume gain in MLs, bbls"
			dxSlip = sc.nextDouble(); sc.nextLine();   //dxSlip, kick migration distance, ft"
			vdBit = sc.nextDouble(); sc.nextLine();    //vdBit, Vertical depth of the well, ft"
			hdBit = sc.nextDouble(); sc.nextLine();    //hdBit, Horizonal departure of the well, ft"
			MainDriver.Qdrill = sc.nextDouble(); sc.nextLine();   //Qdrill, Pump circ. rate while drilling, gpm"
			MainDriver.Qkill = sc.nextDouble(); sc.nextLine();    //Qkill, Pump circ. rate while well kill, gpm"

			//
			//---------------------Directional Data Print Out !

			if(MainDriver.iWell >= 1){
				sc.nextLine(); 
				sc.nextLine();
				MainDriver.Rbur = sc.nextDouble(); sc.nextLine(); //Rbur, Radius of Curvature @ 1st build, ft"
			}
			if(MainDriver.iWell >= 3){
				MainDriver.R2bur = sc.nextDouble(); sc.nextLine(); //R2bur, Radius of Curvature @ 2nd build, ft"
			}
			//
			sc.nextLine();
			MainDriver.NwcS = sc.nextInt(); sc.nextLine();   //NwcS, Index for the starting of well geometry data"
			MainDriver.NwcE = sc.nextInt(); sc.nextLine();   //NwcE, Index for the end of well geometry data"
			sc.nextLine();
			//
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();//3 description lines

			for(int i = MainDriver.NwcE-1; i>=MainDriver.NwcS-1; i--){
				MainDriver.TMD[i] = sc.nextDouble();
				MainDriver.TVD[i] = sc.nextDouble();
				MainDriver.ang2p[i] = sc.nextDouble();
				MainDriver.DiDS[i] = sc.nextDouble();
				MainDriver.Do2p[i] = sc.nextDouble();
				MainDriver.Di2p[i] = sc.nextDouble(); 
				sc.nextLine();
			}
			//....... 2-phase Kick information from well shut in and well stabilization !
			//-----------------------------------------------------------------------------------------------------
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			MainDriver.N2phase = sc.nextInt(); sc.nextLine();   //n2phase, Total number of 2-phase mixture slug"
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			if(MainDriver.imud == 0){ //WBM
				for(int i = 0; i<MainDriver.N2phase; i++){
					MainDriver.Xnd[i] = sc.nextDouble();
					MainDriver.Pnd[i] = sc.nextDouble();
					MainDriver.pvtZb[i] = sc.nextDouble(); 
					MainDriver.Hgnd[i] = sc.nextDouble(); 
					MainDriver.volL[i] = sc.nextDouble();
					MainDriver.delta_T[i] = sc.nextDouble();
					MainDriver.volG[i] = sc.nextDouble();
					sc.nextLine();
				}
			}
			else{ //OBM
				for(int i = 0; i<MainDriver.N2phase; i++){
					MainDriver.Xnd[i] = sc.nextDouble();
					MainDriver.Pnd[i] = sc.nextDouble();
					MainDriver.PVTZ_Gas[i] = sc.nextDouble();
					MainDriver.Hgnd[i] = sc.nextDouble();
					MainDriver.volL[i] = sc.nextDouble();
					MainDriver.volG[i] = sc.nextDouble();
					MainDriver.delta_T[i] = sc.nextDouble();
					MainDriver.gor[i] = sc.nextDouble();
					sc.nextLine();
				}
			}
			//-----------------------------------------------------------------------------------------------------
			//.............................. ........ read final results with time
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			MainDriver.NpSi = (int)sc.nextDouble(); sc.nextLine();   //Npsi, Data saving index of the results"
			//
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			for(int i = 0; i<=MainDriver.NpSi; i++){
				timeMinute = sc.nextDouble();
				MainDriver.TTsec[i] = timeMinute * 60;
				MainDriver.xTop[i] = sc.nextDouble();
				MainDriver.xBot[i] = sc.nextDouble();
				MainDriver.pxTop[i] = sc.nextDouble();
				MainDriver.Vpit[i] = sc.nextDouble();
				MainDriver.rhoK[i] = sc.nextDouble();
				sc.nextLine();
			}
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			//		    
			for(int i = 0; i<=MainDriver.NpSi; i++){
				timeMinute = sc.nextDouble();
				MainDriver.Ppump[i] = sc.nextDouble();
				MainDriver.Psp[i] = sc.nextDouble();
				MainDriver.Pchk[i] = sc.nextDouble();
				MainDriver.Pcsg[i] = sc.nextDouble();
				MainDriver.Pb2p[i] = sc.nextDouble();
				MainDriver.PmLine[i] = sc.nextDouble();
				sc.nextLine();
				//        ttSec(i) = timeMinute * 60#
			}
			//
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();
			sc.nextLine();

			for(int i = 0; i<=MainDriver.NpSi; i++){
				timeMinute = sc.nextDouble();
				MainDriver.Stroke[i] = (int)(sc.nextDouble());
				MainDriver.VOLcir[i] = sc.nextDouble();
				MainDriver.CHKopen[i] = sc.nextDouble();
				MainDriver.QmcfDay[i] = sc.nextDouble();
				MainDriver.mudflow[i] = sc.nextDouble();
				MainDriver.gasflow[i] = sc.nextDouble();
				sc.nextLine();			    	
			}
			return 1;
		}catch(Exception e){
			String msgTmp = "";
			msgTmp = "Shutin data file format has been changed and reading errors have occurred."+"\n";
			msgTmp = msgTmp + "  You should not use this data and also you need to check all input data"+"\n";
			msgTmp = msgTmp + "  from the Main Menu. The sub quits here.";
			//    Resume Next   //it may gives too many error messages !
			JOptionPane.showMessageDialog(null, msgTmp, "Warning", JOptionPane.INFORMATION_MESSAGE);
			return 0;
		}
	}


	void SaveShutinData(textResult txtExamp){
		//----------------------------------  print the input data as a file and retrieve later
		//                                    for multiple runs.    

		String s="";

		txtExamp.textArea.append("=== INPUT and Shut in data for Well Control Simulator by Dr. Choe ==="+"\n");
		txtExamp.textArea.append("=== Warning ===>>> Do NOT change data and format using any editor ==="+"\n");
		txtExamp.textArea.append("---------------------------------------------------------------------"+"\n"+"\n");
		txtExamp.textArea.append("===>> Control Data"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.igERD)+"\t"+"\t"+"Well trajectory type (1:multilateral, 0:conventional)"+"\n");  //8/4/03
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iOnshore)+"\t"+"\t"+"Rig location (1:onshore, 2:offshore)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.Method)+"\t"+"\t"+"Selected method (1:driller, 2:wait and weight)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.Model)+"\t"+"\t"+"Fluid model (1:Power law, 2:Bingham, 3:Newtonian)"+"\n");
		//txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iPump)+"\t"+"\t"+"Pump type (2:duplex, 3:triplex)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iDelp)+"\t"+"\t"+"Friction loss (1:condsider, 2:ignore)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iZfact)+"\t"+"\t"+"Gas deviation (1:condsider, 2:ignore)"+"\n");
		//............. for ERD and ML   //after 7/1/02
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iMudComp)+"\t"+"\t"+"Use of mud compressibility on well stabilization (1:used, 0:unused)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iBlowout)+"\t"+"\t"+"Use of blowout animation (1:used, 0:unused)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.imud)+"\t"+"\t"+"Mud type (1:OBM, 0:WBM)"+"\n"); //hs
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iHeattrans)+"\t"+"\t"+"Heat transfer (1:consider, 2:ignore)"+"\n"); //hs
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.imultikick)+"\t"+"\t"+"Consider Multikick (1:consider, 0:ignore)"+"\n"+"\n"); //TY
		//-----
		txtExamp.textArea.append( "===>> Fluid properties and bit nozzle data"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iData)+"\t"+"\t"+"Mud data type (1:Shear stress readings, 2:PV & YP)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.##")).format(MainDriver.S600)+"\t"+"\t"+"Shear stress @ 600 rpm"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.##")).format(MainDriver.S300)+"\t"+"\t"+"Shear Stress @ 300 rpm"+"\n");
		if(MainDriver.Model== 0){   //API RP13D
			txtExamp.textArea.append( (new DecimalFormat("###0.##")).format(MainDriver.SS100)+"\t"+"\t"+"Shear stress @ 100 rpm"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("###0.##")).format(MainDriver.SS3)+"\t"+"\t"+"Shear Stress @ 3 rpm"+"\n");
		}
		txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.oMud)+"\t"+"\t"+"Old mud density, ppg"+"\n");
		if(MainDriver.iMudComp == 1){
			txtExamp.textArea.append( (new DecimalFormat("####.0##")).format(MainDriver.MudComp * 1000000)+ "E-06"+"\t"+"\t"+"Mud compressibility, 1/psi"+"\n");
		}
		txtExamp.textArea.append( (new DecimalFormat("####0.##")).format(MainDriver.RenC)+"\t"+"\t"+"Critical Reynolds number"+"\n");
		if(MainDriver.Model== 2){
			txtExamp.textArea.append( (new DecimalFormat("#0.######")).format(MainDriver.Ruf)+"\t"+"\t"+"Roughness of pipe, in"+"\n");
		}
		txtExamp.textArea.append( (new DecimalFormat("#0.###")).format(MainDriver.gasGravity)+"\t"+"\t"+"Specific gravity of gas, (air=1)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0.###")).format(MainDriver.fCO2)+"\t"+"\t"+"Mole fraction of CO2, fraction"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0.###")).format(MainDriver.fH2S)+"\t"+"\t"+"Mole fraction of H2S, fraction"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.Tsurf)+"\t"+"\t"+"Surface temperature, //F "+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0.###")).format(MainDriver.Tgrad)+"\t"+"\t"+"Mud temperature gradient, //F/100 ft"+"\n");
		//
		txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.Dnoz[0])+"\t"+"\t"+"Bit nozzle 1 size, /32nd in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.Dnoz[1])+"\t"+"\t"+"Bit nozzle 2 size, /32nd in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.Dnoz[2])+"\t"+"\t"+"Bit nozzle 3 size, /32nd in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.Dnoz[3])+"\t"+"\t"+"Bit nozzle 4 size, /32nd in"+"\n"+"\n");
		//-----
		txtExamp.textArea.append( "===>> Well geometry and directional data"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iWell)+"\t"+"\t"+"Well type (0:Vertical, 1:B, 2:BH, 3:BHB, 4:BHBH)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.#")).format(MainDriver.Vdepth)+"\t"+"\t"+"True vertical depth of the well, ft"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.#")).format(MainDriver.DepthCasing)+"\t"+"\t"+"Measured depth of casing seat, ft"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.IDcasing)+"\t"+"\t"+"ID of last casing, in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.DiaHole)+"\t"+"\t"+"Diameter of open hole, in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.doDP)+"\t"+"\t"+"OD of drill pipe, in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.diDP)+"\t"+"\t"+"ID of drill pipe, in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.#")).format(MainDriver.LengthHWDP)+"\t"+"\t"+"Measured length of HWDP, ft"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.doHWDP)+"\t"+"\t"+"OD of HeviWate DP, in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.diHWDP)+"\t"+"\t"+"ID of HeviWate DP, in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.#")).format(MainDriver.LengthDC)+"\t"+"\t"+"Measured length of DC, ft"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.doDC)+"\t"+"\t"+"OD of drill collars, in"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.diDC)+"\t"+"\t"+"ID of drill collars, in"+"\n");
		//
		if(MainDriver.iOnshore== 2){ //print offshore data; Moved tWgrad
			txtExamp.textArea.append( (new DecimalFormat("####0.#")).format(MainDriver.Dwater)+"\t"+"\t"+"Depth of water, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("#0.###")).format(MainDriver.tWgrad)+"\t"+"\t"+"Sea water temperature grad., //F/100 ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.Driser)+"\t"+"\t"+"ID of marine riser, in"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.Dchoke)+"\t"+"\t"+"ID of choke line, in"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.Dkill)+"\t"+"\t"+"ID of kill line, in"+"\n");
		}
		//
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iCduct)+"\t"+"\t"+"Use of conduct (1:used, 0:unused)"+"\n");
		if(MainDriver.iCduct == 1){
			txtExamp.textArea.append( (new DecimalFormat("#####0.##")).format(MainDriver.DepthCduct)+"\t"+"\t"+"Depth of conductor seat, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.ODcduct)+"\t"+"\t"+"OD of conductor, in"+"\n");
		}
		//
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iSurfCsg)+"\t"+"\t"+"Use of surf. casing (1:used, 0:unused)"+"\n");
		if(MainDriver.iSurfCsg== 1){
			txtExamp.textArea.append( (new DecimalFormat("#####0.##")).format(MainDriver.DepthSurfCsg)+"\t"+"\t"+"Depth of surf. csg seat, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.ODsurfCsg)+"\t"+"\t"+"OD of surface casing seat, in"+"\n");
		}
		// Directional data in(new DecimalFormat()).formation
		//   When new data file is retrieved, we need to calculate:
		//   angEOB, xHold, DepthKOP (only type 4), Rbur, R2bur, etc
		//
		if(MainDriver.iWell >= 1 && MainDriver.iWell <= 4){
			txtExamp.textArea.append( (new DecimalFormat("####0.##")).format(MainDriver.DepthKOP)+"\t"+"\t"+"Kick off point, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.BUR)+"\t"+"\t"+"First build up rate, deg./100 ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.BUR2)+"\t"+"\t"+"second build up rate, deg./100 ft"+"\n");
			if(MainDriver.iWell == 4){
				txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.angEOB)+"\t"+"\t"+"Angle at the end of first build, deg."+"\n");
				txtExamp.textArea.append( (new DecimalFormat("####0.##")).format(MainDriver.xHold)+"\t"+"\t"+"Length of first hold, ft"+"\n");
			}
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.ang2EOB)+"\t"+"\t"+"Angle at the end of second build, deg."+"\n");
			txtExamp.textArea.append( (new DecimalFormat("####0.##")).format(MainDriver.Hdisp)+"\t"+"\t"+"Horizontal distance at EOB, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("####0.##")).format(MainDriver.x2Hold)+"\t"+"\t"+"Length of second hold, ft"+"\n");
		}
		//-----
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append( "===>> Shut-in and formation properties data"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0.####")).format(MainDriver.KICKintens)+"\t"+"\t"+"Kick intensity, ppg"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.##")).format(MainDriver.pitWarn)+"\t"+"\t"+"Pit warning level, bbls"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("####0.###")).format(MainDriver.Perm)+"\t"+"\t"+"formation permeability, md"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.###")).format(MainDriver.Skins)+"\t"+"\t"+"formation skin factor"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0.###")).format(MainDriver.Porosity)+"\t"+"\t"+"formation porosity, fraction"+"\n");
		if(MainDriver.igERD == 1){   //8/4/03
			txtExamp.textArea.append( (new DecimalFormat("#####.0#")).format(MainDriver.gPayLength)+"\t"+"\t"+"Drilled pay zone length, ft"+"\n");
		}
		else{
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.ROPen)+"\t"+"\t"+"Rate of penetration, ft/hr"+"\n");
		}

		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iFG)+"\t"+"\t"+"Type of fracture gradient (0:input, 1:Eaton, 2:Barker)"+"\n");
		if(MainDriver.iFG == 0){
			txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iFGnum)+"\t"+"\t"+"Number of input data (Depth BML (ft), PP & Frac. P (psig))"+"\n");
			for(int i = 0; i<MainDriver.iFGnum; i++){
				txtExamp.textArea.append( (new DecimalFormat("#####0.#")).format(MainDriver.PPdepth[i])+"\t"+"\t"+(new DecimalFormat("#####0")).format(MainDriver.PoreP[i])+"\t"+"\t"+(new DecimalFormat("#####0")).format(MainDriver.FracP[i])+"\t"+"\n");
			}
		}

		if(MainDriver.iWell == 4 && Math.abs(MainDriver.ang2EOB - 90) < 0.01){  //only horizontal well not ERD
			txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iHresv)+"\t"+"\t"+"Type of horizontal well (0:locally HP, 1:homo)"+"\n");
		}
		//-----
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append( "===>> Pump data and other information"+"\n");
		/*txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.DiaLiner)+"\t"+"\t"+"Liner size of pump, in"+"\n");
	    if(MainDriver.iPump== 2){
	    	txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.Drod)+"\t"+"\t"+"Rod size of pump, in"+"\n");
	    	}
	    txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(StrokeLength)+"\t"+"\t"+"Stroke length of pump, in"+"\n");
	    txtExamp.textArea.append( (new DecimalFormat("#0.####")).format(MainDriver.Effcy)+"\t"+"\t"+"Pump efficiency, fraction"+"\n");*/
		txtExamp.textArea.append( (new DecimalFormat("###0.#")).format(MainDriver.spMinD1)+"\t"+"\t"+"Pump 1 drilling rate, st/min"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.#")).format(MainDriver.spMin1)+"\t"+"\t"+"Pump 1 kill rate, st/min"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.#")).format(MainDriver.spMinD2)+"\t"+"\t"+"Pump 2 drilling rate, st/min"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.#")).format(MainDriver.spMin2)+"\t"+"\t"+"Strokes # at Pump 2 kill rate, st/min"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.#")).format(Stroke1)+"\t"+"\t"+"Strokes # at Pump 1 drilling rate, st"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.#")).format(Stroke2)+"\t"+"\t"+"Strokes # at Pump 2 drilling rate, st"+"\n");
		//
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iType)+"\t"+"\t"+"Type of surf. conn. (0:ignore, 1-4:equiv. length)"+"\n");
		if(MainDriver.iType >= 1 && MainDriver.iType <= 4){
			txtExamp.textArea.append( (new DecimalFormat("###0.#")).format(MainDriver.Heqval)+"\t"+"\t"+"3 in. equivalent length, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("###0.##")).format(MainDriver.LengthSurfLine)+"\t"+"\t"+"Length of surface line to stand pipe, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.DiaSurfLine)+"\t"+"\t"+"ID of surface line to stand pipe, in"+"\n");
		}
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.DchkControl)+"\t"+"\t"+"ID of surface choke valve, in"+"\n");
		//-----
		if(MainDriver.iHeattrans == 1){
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append( "===>> Heat Transfer Data"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.TconF)+"\t"+"\t"+"Thermal conductivity of (new DecimalFormat()).formation, Btu/ft-//F-hr"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.SheatF)+"\t"+"\t"+"Specific heat of (new DecimalFormat()).formation, Btu/lbm-//F"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.HtrancF)+"\t"+"\t"+"Heat transfer coefficient of (new DecimalFormat()).formation, Btu/hr-//F-ft2"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.TconM)+"\t"+"\t"+"Thermal conductivity of mud, Btu/ft-//F-hr"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.SheatM)+"\t"+"\t"+"Specific heat of mud, Btu/lbm-//F"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.HtrancM)+"\t"+"\t"+"Heat transfer coefficient of mud, Btu/hr-//F-ft2"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.##")).format(MainDriver.InjmudT)+"\t"+"\t"+"Injection mud temperature, //F"+"\n");
		}
		//----
		if(MainDriver.imud == 1){
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append( "===>> Mud Data"+"\n");
			txtExamp.textArea.append((new DecimalFormat("0.###")).format(MainDriver.foil)+"\t"+"\t"+"Volume fraction of base oil in OBM"+"\n");
			txtExamp.textArea.append((new DecimalFormat("0.###")).format(MainDriver.fbrine)+"\t"+"\t"+"Volume fraction of brine in OBM"+"\n");
			txtExamp.textArea.append((new DecimalFormat("0.###")).format(MainDriver.fadditive)+"\t"+"\t"+"Volume fraction of additives in OBM"+"\n");
			txtExamp.textArea.append((new DecimalFormat("#0")).format(MainDriver.ibaseoil)+"\t"+"\t"+"Base oil type (0:No.2 diesel, 1: Mentor 28, 2: Conoco LVT, 4:User input)"+"\n");
			//hs
			if(MainDriver.ibaseoil == 4){
				for(int i = 0; i<=16; i++){
					txtExamp.textArea.append( (new DecimalFormat("##.###")).format(MainDriver.Mfrac_C[i + 7])+"\t"+"C "+(i + 7)+" composition"+"\n");
				}
				txtExamp.textArea.append( (new DecimalFormat("##.###")).format(MainDriver.Mfrac_C[24])+"\t"+"\t"+"C 25+ composition"+"\n");
			}
		}
		//----- Tripping related data, 8/3/2003
		if(MainDriver.igERD == 1){
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append( "===>> Multilateral & Trip data"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.0")).format(MainDriver.gTripTankVolume)+"\t"+"\t"+"Trip tank volume, bbls"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.0")).format(MainDriver.gTripTankHeight)+"\t"+"\t"+"Trip tank height, bbls"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("###0.0")).format(MainDriver.gConnTime)+"\t"+"\t"+"Trip joint connection time, sec"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0.0")).format(MainDriver.gJointLength)+"\t"+"\t"+"Trip joint lenght, ft"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("##0")).format(MainDriver.igJointNumber)+"\t"+"\t"+"Trip joint number per stand"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("###0.0")).format(MainDriver.gBleedPressure)+"\t"+"\t"+"Minimum p. increase before bleeding, psi"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("###0.0")).format(MainDriver.gBleedTime)+"\t"+"\t"+"Time duration for p. bleeding, sec"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.igMLnumber)+"\t"+"\t"+"Number of multilateral trajectories"+"\n");
			txtExamp.textArea.append("Plug "+"\t"+"\t"+"KOP MD"+"\t"+"\t"+"  TVD "+"\t"+"\t"+"Angle"+"\t"+"\t"+" BUR  "+"\t"+"\t"+"Angle"+"\t"+"\t"+"Hold "+"\t"+"\t"+" BUR2 "+"\t"+"\t"+"Angle2"+"\t"+"\t"+"Hold2"+"\n");
			txtExamp.textArea.append("(1/0)"+"\t"+"\t"+"  ft  "+"\t"+"\t"+"  ft  "+"\t"+"\t"+"deg. "+"\t"+"\t"+"/100ft"+"\t"+"\t"+"deg. "+"\t"+"\t"+" ft  "+"\t"+"\t"+"/100ft"+"\t"+"\t"+" deg. "+"\t"+"\t"+" ft  "+"\n");
			txtExamp.textArea.append("--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\n");
			for(int ii = 0; ii<MainDriver.igMLnumber - 1; ii++){
				txtExamp.textArea.append((new DecimalFormat("#0")).format(MainDriver.mlPlug[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.0")).format(MainDriver.mlKOP[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.0")).format(MainDriver.mlKOPvd[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("##0.0#")).format(MainDriver.mlKOPang[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("##0.0#")).format(MainDriver.mlBUR[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("##0.0#")).format(MainDriver.mlEOB[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.0")).format(MainDriver.mlHold[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("##0.0#")).format(MainDriver.mlBUR2nd[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("##0.0#")).format(MainDriver.mlEOB2nd[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.0")).format(MainDriver.mlHold2nd[ii])+"\t");
			}
			txtExamp.textArea.append("--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"-------------"+"\n");
			txtExamp.textArea.append("Hole ID"+"\t"+"\t"+"Form. P"+"\t"+"\t"+"Perm. "+"\t"+"\t"+"Poros."+"\t"+"\t"+"Skins "+"\t"+"\t"+"Flow Interval"+"\n");
			txtExamp.textArea.append(" inch  "+"\t"+"\t"+"  psig "+"\t"+"\t"+" md   "+"\t"+"\t"+"fract."+"\t"+"\t"+"D_less"+"\t"+"\t"+"      ft     "+"\n");
			txtExamp.textArea.append("--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"-------------"+"\n");
			for(int ii = 0; ii<=(MainDriver.igMLnumber - 1); ii++){
				txtExamp.textArea.append((new DecimalFormat("##0.0##")).format(MainDriver.mlDia[ii])+"\t");
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.0")).format(MainDriver.mlPform[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("###0.0#")).format(MainDriver.mlPerm[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("0.0##")).format(MainDriver.mlPorosity[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("##0.0#")).format(MainDriver.mlSkin[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("###0.0#")).format(MainDriver.mlHeff[ii])+"\t"+"\n");
			}
			//...... save additional data for kick influx and related calculations
			txtExamp.textArea.append("--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\n");
			txtExamp.textArea.append(" ML MD "+"\t"+"\t"+" ML TVD"+"\t"+"\t"+"Qg_tot"+"\t"+"\t"+" Cti  "+"\t"+"\t"+" Bg   "+"\n");
			txtExamp.textArea.append("  ft   "+"\t"+"\t"+"  ft   "+"\t"+"\t"+" Mscf "+"\t"+"\t"+" 1/psi"+"\t"+"\t"+"rb/scf"+"\n");
			txtExamp.textArea.append("--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\t"+"\t"+"--------"+"\n");

			for(int ii = 0; ii<=(MainDriver.igMLnumber - 1); ii++){
				txtExamp.textArea.append((new DecimalFormat("#####.0#")).format(MainDriver.mlMD[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.0#")).format(MainDriver.mlTVD[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("######0.0#")).format(MainDriver.mlQgTotM[ii]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("###0.0##")).format(MainDriver.mlCti[ii] * 1000000)+"E-6");
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("###0.0##")).format(MainDriver.mlBg[ii] * 1000000)+"E-6"+"\t"+"\n");
			}
		}   //..... of ERD = 1
		//
		//......................Additional control or operational data
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append( "===>> Operational data"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iCHKcontrol)+"\t"+"\t"+"Choke control (1:auto, 2:manual)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iChoke)+"\t"+"\t"+"Choke valve status (1:open, 0:close)"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.iKill)+"\t"+"\t"+"Kill valve status (1:open, 0:close)"+"\n");
		txtExamp.textArea.append("\n");  //one blank line at the end of data file
		//---------------------------------------------------------------------------------------- 7/13/02
		// Additional shutin data for multiple runs
		//
		txtExamp.textArea.append("\n"+"\n");
		txtExamp.textArea.append( "--- Results from Initial Calculations ---"+"\n");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.##")).format(MainDriver.Pb)+"\t"+"\t"+"Bottomhole Pressure Maintained, psia"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.##")).format(MainDriver.Pform)+"\t"+"\t"+"(new DecimalFormat()).formation Pressure, psia"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.0##")).format(MainDriver.overP)+"\t"+"\t"+"(new DecimalFormat()).formation Over-Pressure, psi"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.Kmud)+"\t"+"\t"+"Kill Mud Weight, ppg"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.###")).format(MainDriver.Cmud)+"\t"+"\t"+"Circulating Mud Weight, ppg"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.####")).format(MainDriver.Qcapacity1)+"\t"+"\t"+"Pump1 capacity, bbl/str"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("##0.####")).format(MainDriver.Qcapacity2)+"\t"+"\t"+"Pump2 capacity, bbl/str"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("####0.##")).format(MainDriver.SIDPP)+"\t"+"\t"+"Shut-in Drill Pipe Pressure, psia"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("####0.##")).format(MainDriver.SICP)+"\t"+"\t"+"Shut-in Casing Pressure, psia"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.0#")).format(Pcasing)+"\t"+"\t"+"Casing seat pressure, psia"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.0##")).format(MainDriver.Vpiti)+"\t"+"\t"+"Initial mixture volume gain at shut in, bbls"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("####0.0#")).format(PVgain)+"\t"+"\t"+"Net pit volume gain, bbls"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("####0.0#")).format(mlPVgain)+"\t"+"\t"+"Net pit volume gain in MLs, bbls"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("###0.0##")).format(dxSlip)+"\t"+"\t"+"Kick migration distance, ft"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.0#")).format(vdBit)+"\t"+"\t"+"Vertical depth of the well, ft"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.0#")).format(hdBit)+"\t"+"\t"+"Horizontal departure of the well, ft"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.0#")).format(MainDriver.Qdrill)+"\t"+"\t"+"Pump circ. rate while drilling, gpm"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#####0.0#")).format(MainDriver.Qkill)+"\t"+"\t"+"Pump circ. rate while well kill, gpm"+"\n");
		//
		//---------------------Directional Data print Out !
		if(MainDriver.iWell >= 1){
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append( "--- Directional Wellbore Information ---"+"\n");
			txtExamp.textArea.append( (new DecimalFormat("#####0.##")).format(MainDriver.Rbur)+"\t"+"\t"+"Radius of Curvature @ 1st build, ft"+"\n");
		}
		if(MainDriver.iWell >= 3){
			txtExamp.textArea.append( (new DecimalFormat("#####0.##")).format(MainDriver.R2bur)+"\t"+"\t"+"Radius of Curvature @ 2nd build, ft"+"\n");
		}
		//
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.NwcS)+"\t"+"\t"+"Index for the starting of well geometry data"+"\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.NwcE)+"\t"+"\t"+"Index for the end of well geometry data"+"\n");
		txtExamp.textArea.append("\n");
		//
		txtExamp.textArea.append("Total MD"    +"\t"+"True VD "+"\t"+"  Angle  "+"\t"+" ID of DS "+"\t"+"OD of Ann"+"\t"+"ID of Ann"+"\n");
		txtExamp.textArea.append("    (ft)    "+"\t"+"    (ft)    "+"\t"+"(degree)"+"\t"+"  (inch)  "+"\t"+"  (inch)  "+"\t"+" (inch)  "+"\n");
		txtExamp.textArea.append("--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"----------"+"\t"+"---------"+"\t"+"---------"+"\n");
		for(int i = MainDriver.NwcE-1; i>=MainDriver.NwcS-1; i--){
			txtExamp.textArea.append((new DecimalFormat("#####0.#")).format(MainDriver.TMD[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.#")).format(MainDriver.TVD[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.##")).format(MainDriver.ang2p[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.###")).format(MainDriver.DiDS[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.###")).format(MainDriver.Do2p[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.###")).format(MainDriver.Di2p[i])+"\t"+"\n");
		}
		//....... 2-phase Kick in(new DecimalFormat()).formation from well shut in and well stabilization !
		//-----------------------------------------------------------------------------------------------------	    
		txtExamp.textArea.append("\n"+"\n");
		txtExamp.textArea.append("--- Results from Initial Shut in and Well Stabilization ---"+"\n");
		txtExamp.textArea.append("-----------------------------------------------------------"+"\n");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.N2phase)+"\t"+"\t"+"Total number of 2-phase mixture slug"+"\n");
		txtExamp.textArea.append("\n");
		//
		txtExamp.textArea.append("Kick Loc"+"\t"+" Kick Px "+"\t"+"PVTZ@bot"+"\t"+"  Gas Hg  "+"\t"+"Liq Volum"+"\t"+"Gas Volume"+"\t"+" Delta t   "+"\t"+"Gas Oil Ratio"+"\n");
		txtExamp.textArea.append("    (ft)    "+"\t"+"  (psia)  "+"\t"+" (moles)"+"\t"+"  (fract.) "+"\t"+"  (bbls)  "+"\t"+"  (bbls)  "+"\t"+"  (sec)   "+"\t"+"  (scf/stb)  "+"\n");
		txtExamp.textArea.append("--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"----------"+"\t"+"---------"+"\t"+"----------"+"\t"+"--------"+"\t"+"-------------"+"\n");
		if(MainDriver.imud == 0){ //WBM
			for(int i = 0; i<MainDriver.N2phase; i++){
				txtExamp.textArea.append((new DecimalFormat("#####0.00")).format(MainDriver.Xnd[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Pnd[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000#")).format(MainDriver.pvtZb[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#0.0000")).format(MainDriver.Hgnd[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000")).format(MainDriver.volL[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000")).format(MainDriver.volG[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000")).format(MainDriver.delta_T[i])+"\t"+"\n");
			}
		}
		else{  //OBM
			for(int i = 0; i<MainDriver.N2phase; i++){
				txtExamp.textArea.append((new DecimalFormat("#####0.00")).format(MainDriver.Xnd[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Pnd[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000#")).format(MainDriver.PVTZ_Gas[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#0.0000")).format(MainDriver.Hgnd[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000")).format(MainDriver.volL[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000")).format(MainDriver.volG[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000")).format(MainDriver.delta_T[i]));
				txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("####0.000")).format(MainDriver.gor[i])+"\t"+"\n");
			}
		}
		//-----------------------------------------------------------------------------------------------------
		//.............................. ........ print out final results
		txtExamp.textArea.append("\n"+"\n");
		txtExamp.textArea.append("--- Results from SNU/TAMU Well Control Simulator ---"+"\n");
		txtExamp.textArea.append("----------------------------------------------------"+"\n");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append( (new DecimalFormat("#0")).format(MainDriver.NpSi)+"\t"+"\t"+"Data saving index of the results"+"\n"+"\n");
		//
		txtExamp.textArea.append("  Time   "+"\t"+"   Xtop   "+"\t"+"  Xbotm "+"\t"+"Px@Top  "+"\t"+" Pit Vol  "+"\t"+"Kick Density"+"\n");
		txtExamp.textArea.append(" (mins)  "+"\t"+"    (ft)    "+"\t"+"    (ft)    "+"\t"+"  (psia)  "+"\t"+"  (bbls)  "+"\t"+"    (ppg)   "+"\n");
		txtExamp.textArea.append("--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"------------"+"\n");
		for(int i = 0; i<=MainDriver.NpSi; i++){
			txtExamp.textArea.append((new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.xTop[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.xBot[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.pxTop[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.00")).format(MainDriver.Vpit[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.00")).format(MainDriver.rhoK[i])+"\t"+"\n");
		}
		//
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("  Time   "+"\t"+" Pump P "+"\t"+"Stand PP"+"\t"+" Choke P"+"\t"+"CsgSeat P"+"\t"+"   BHP   "+"\t"+"P@mudline"+"\n");
		txtExamp.textArea.append("  (mins) "+"\t"+"  (psia)  "+"\t"+" (psia)   "+"\t"+"  (psia)  "+"\t"+"  (psia)  "+"\t"+"  (psia)  "+"\t"+"  (psia)"+"\n");
		txtExamp.textArea.append("--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"---------"+"\t"+"--------"+"\t"+"---------"+"\n");
		for(int i = 0; i<=MainDriver.NpSi; i++){
			txtExamp.textArea.append((new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Ppump[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Psp[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Pchk[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Pcsg[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Pb2p[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.PmLine[i])+"\t"+"\n");
		}
		//
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("  Time   "+"\t"+" Strokes "+"\t"+" Vol Circ"+"\t"+"ChK Open "+"\t"+"  InFlux  "+"\t"+"Mud Rate"+"\t"+"Gas Rate"+"\n");
		txtExamp.textArea.append("  (mins) "+"\t"+"    (#)    "+"\t"+"  (bbls)  "+"\t"+"Dia Ratio"+"\t"+" (Mcf/D)"+"\t"+"  (gpm)  "+"\t"+"(Mcf/D) "+"\n");
		txtExamp.textArea.append("--------"+"\t"+"--------"+"\t"+"--------"+"\t"+"---(%)---"+"\t"+"--------"+"\t"+"--------"+"\t"+"--------"+"\n");
		for(int i = 0; i<=MainDriver.NpSi; i++){
			txtExamp.textArea.append((new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.Stroke[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.00")).format(MainDriver.VOLcir[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.CHKopen[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.QmcfDay[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.mudflow[i]));
			txtExamp.textArea.append("\t"+"\t"+(new DecimalFormat("#####0.0")).format(MainDriver.gasflow[i])+"\t"+"\n");
		}
		txtExamp.textArea.append("/end/"+"\n");
	}

	void setKillConditions(){
		MainDriver.iWshow = 0;
		pnlControl.setVisible(false);
		ChokeControlPan.setVisible(true);
		ChokeKillLines.setVisible(true);
		if (MainDriver.igERD == 1){   //allowed automatic control only
			optAutomatic.setSelected(true);
			optManual.setEnabled(false);
		}
		else{
			optAutomatic.setSelected(true);
			optManual.setSelected(false);
		}
		if(MainDriver.iOnshore == 2){  //offshore case
			optChoke.setSelected(true);
			optKill.setSelected(false);
			optBoth.setSelected(false);
			optKill.setVisible(true);
			optBoth.setVisible(true);
		}
		else{   //onshore wells
			optChoke.setSelected(true);
			optKill.setVisible(false); 
			optBoth.setVisible(false);
		}
	}

	void setConventionalERD(){
		int iPos=0;
		//...... settings for ML and Tripping simulation
		//SSTabTrip.setVisible(false)
		String DrillSI_Caption = "SNU/TAMU Well Control Simulator; Drilling, Kicking, Kick Detection, and Kick Confinement";
		//
		double[] gasPropEX = new double[3];
		double zb=0;
		double[] porefracP = new double[2];

		ChokeKillLines.setVisible(false);
		ChokeControlPan.setVisible(false);
		//Shape2.setVisible(false)
		//
		double pBit = MainDriver.gMudOld * vdBit;
		if(MainDriver.iProblem[3]==1 || MainDriver.iProblem[2]==1){
			porefracP = propertyModule.calcPoreFrac(vdBit);
			P2form = porefracP[0];
			pBit = porefracP[0];
		}
		txtVDbit.setText("0.0");
		txtHDbit.setText("0.0");

		txtROP.setText( "0.0");
		if(MainDriver.iProblem[3]==1){
			txtRPM_control.setText("0"); // 150204 AJW
			txtWOB.setText("0"); // 150204 AJW
		}

		txtPumpRate.setText( "0.0");
		txtReturnRate.setText( "0.0");
		txtPitGain.setText( "0.0");
		txtPumpP.setText( "0.0");
		txtSPP.setText( "0.0");
		txtFormationP.setText((new DecimalFormat("###,##0")).format(pBit-14.7));
		txtBHP.setText((new DecimalFormat("###,##0")).format(pBit-14.7));
		txtCasingP.setText((new DecimalFormat("###,##0")).format(Pcasing));
		txtChokeP.setText( "0.0");
		txtTime.setText( "0:00:00");
		txtKillMudWt.setText( " "  );//later assign kill mud weight = Str$(cut2d(kmud))
		//------------------------- After July 1, 2002; for multilateral and extended reach drilling
		MainDriver.SimRate = 1;   //simulation rate
		txtSimRate.setText( Integer.toString(MainDriver.SimRate));
		txtGasRate.setText( "0.0");
		xDrill = 0;  //drilled depth
		//------------------------------------------------------------------------------------------
		iRotary = 0;
		DrotTab = 0.5 * (MainDriver.doDP + MainDriver.IDcasing); 
		HrotTab = -MainDriver.Vdepth / 40;
		//
		DrillAssign();
		//   getCti
		//--- to check the under ground blow out; calculate the fracture gradient
		CalcFracP();   //8/01/02 - now we use Pore and Frac pressure arraies; PPdepth(), PoreP(), FracP()
		//--- calculate the horizontal well volume
		v2Hold = 0;
		if(MainDriver.iHresv == 1){  //homogeneous horizontal reservoir
			double tmpx =  (MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.x2Hold - 0.5);
			iPos = utilityModule.Xposition(tmpx);
			for(int i = MainDriver.NwcS; i<iPos+1; i++){
				v2Hold = v2Hold + MainDriver.VOLann[i];
				nSlip = i;
			}
		}
		Tpenet[0] = 0;
		MainDriver.igMLnumber = 0;   //for multiple runs, 8/3/2003
		//
	}

	void menuClose(){		
		if(TimerWarnOn==1){
			TimerWarn.cancel();
			TimerWarnOn=0;
		}
		if(TimerDrillOn==1){
			TimerDrill.cancel();
			TimerDrillOn=0;
		}
		if(TimerSIOn==1){
			TimerSI.cancel();
			TimerSIOn=0;
			MainDriver.SItaskOn2=0;
		}

		if(Sm1.m3.TimerRotOn==1){
			Sm1.m3.TimerRot.cancel();
			Sm1.m3.TimerRotOn=0;
		}

		MainDriver.SItaskOn2=0;
		MainDriver.iWshow = 0;
		Sm1.m3.dispose();
		Sm1.m3.setVisible(false);
		Sa1.m3.dispose();
		Sa1.m3.setVisible(false);
		MainDriver.drillSimOn=0;
		MainDriver.MainMenuVisible=1;
		this.dispose();
		this.setVisible(false);
		iROP=0;
	}

	void Form_load(){		
		setIconImage(MainDriver.icon.getImage());

		SIDTimerWarn = 0;
		SIDTimerDrill = 0;
		SIDTimerShutin = 0;
		SIDSm1TRTaskOn = 0;
		SIDSa1TRTaskOn = 0;

		MainDriver.spMinD1=0;
		MainDriver.spMinD2=0; //20140308 ajw

		MainDriver.BlowOutPnlVisible=0;
		MainDriver.MenuSelected=0; // 0: to the main menu, 1: to the simulation panel, 2: Not selected yet.
		MainDriver.BlowOutOccurred=0; //0: not occurred 1: occurred
		MainDriver.RPMenuSelected = 0; // 0: to the main menu, 1: to the simulation panel, 2: Not selected yet.
		iShutinData=0;
		iDataChange=0;
		IPmax = 2020;
		iRotary=0;
		iPGwarn=0; iPenet=0;
		iStable=0;
		iSound=0; SIcount=0; nHorizon=0;
		nSlip=0; timePumpOff=0;
		DrotTab=0; HrotTab=0;
		vdBit=0; hdBit=0;
		AeobRad=0; psia=14.7;
		ROP=0; Qcirc=0; Qt=0; PVgain=0; pumpP=0; standpipeP=0; Pcasing=0; Pbeff=0; Pchoke=0;
		kickVDbottom=0;    //bottom of the kick in vertical depth; ft
		xNull=0; Pkick=0; holeMD=0; QtotVol=0; Qgas=0; QgDay=0; PcsgSI=0;
		gasVis=0; gasDen=0; zz=0;
		HgCal=0; kickMD=0; kickVD=0; kickLoc=0; QgTotMscf=0; QgOldMscf=0; surfTen=0;
		vSlip=0; dxSlip=0; v2Hold=0;
		xDrill=0;
		Pdiff=0;
		//
		//---------------------------- additional variable after May; 1999.; 7/12/02
		Cgi=0;    //1/psi; initial gas compressibility
		iROP =0;  //ratation (1); no rotation(0)
		iPumpOn1 =0; iPumpOn2 =0;  //with pump on(1); pump off(0)
		timeNp=0;   //to save output file
		iShowData=0;  //to save and open shutin data for multiple runs
		iTimerWarn=0; iTimerDrill=0; iTimerShutin=0;
		iTimerTrip=0; iTimerStrip=0;
		fileSID ="";
		iShutinData=0;  //to save and open shutin data file (1); otherwise(0)
		mudLineP =0; mudFlowOut=0; gasFlowOut=0; //add mudflowout; gasflowout again 
		iDataChange=0;   //to check input data change after we retrive shut in data
		//---------------------------- additional variable after Jan. 2003 for ML & ERD
		iTripAuto =0;   //connection type; auto(0)/manual(1)
		iRunTrip =0;    //run trip (1)/ pause trip (0)
		iTripCon =0;    //control variable for connection time simulation; during connection (-99)
		iTripPOH =0;    //control parameter for surge(1) and swab(-1)
		//         array variables for plotting purpose
		nDataPlot =0;   //total number of data points for plotting purpose
		iTripRatio =0;
		ConnTimeDelay =0;  //connection time delay for manual control
		Ktrip=0; VtripOld=0; VtripMax=0;        //ft/s; Vtrip; dirll string trip velocity
		totalTrip=0; delTrip=0;     //for trip length calculation  2003/7/12
		standTrip=0; //; standTripOld
		standTime=0; standTimeOld=0;   //time duration for stand trip; sec
		standLength=0;
		delDSvolTrip=0;   //DS volume removed; tripped out(+)
		accTrip=0;   //ft/s^2; drill string acceleration
		curVel=0; curVelOld=0; curAcc=0;  //current trip acc. and vel.
		surgeRate=0; surgeFrac=0;   //surge flow rate and surge fraction through annulus
		DPssCasing=0; DPaccCasing=0;//DP due to trip to calc. casing seat pressure
		VOLinTripTank=0;  //remaining volume in the trip tank
		mlVolume=0;   //bbls; ML volume to calculate p. buildup
		mlPVgain=0;   //bbls; pit volume gain in ML
		PVgainOld=0; mlPVgainOld=0;
		mlQgTotMscf=0;   //Mscf; total kick influx from MLs
		mlQgDay=0;             //total influx rate in Mscf/day
		DPincML=0;       //psi; p. increase during buildup
		DPmudDrop=0;    //psi; DP drop due to improper well fillup
		VOLdrop =0;      //bbls; total volume of mud dropped
		DPbleed =0;      //well pressure increase by snub/ stripping
		fluxTimeSec =0;    //time for gas kick flow
		DPsafety=0;  //for min. pressure increase to prevent additional kick

		//Added by Ty
		tbottom =0;

		//20130812 ajw
		TimeDrlIntv=0;	
		lblPause.setText("Operation");	

		TimerDrillOn = 0;
		TimerSIOn = 0;
		MainDriver.SItaskOn2=0;
		TimerWarnOn = 0;
		TimerBOOn = 0;
		TimerIPOn = 0;
		TimerRPOn = 0;

		SITaskOn=1;
		TDTaskOn=1;
		TDTaskFinishedIndex = 1;//finish =0; start 1
		WarnTaskOn=1;
		DrillbtnOn=0;
		PumpbtnOn=0;	

		MainDriver.SItaskOn2=0;
		//20130813 ajw
		tmpintMud=0; tmpintCHK=0; tmpintSDP=0; tmpintPumpP =0; //  it is made to save the GaugeValue in DrawMudGauge Function in VB. 

		//20130814
		//static wellpic Sm1.m3 = new wellpic();

		panIntv = 10;
		panSizeX = 200; panSizeY = 180; pan1Xsrt = 15; pan1Ysrt=10+btnSizeY+panIntv; yintv=10;
		simAccPnlSizeX = 2*btnSizeX+panIntv; simAccPnlSizeY = 75;	
		txtIntvX=12; txtSrtX = 5; txtSrtY1=38; txtSizeX = 70; txtSizeY=18; txtIntvY=5;
		txtLblXsize=194; txtLblYsize = txtSizeY;
		labelFontSize=11; labelFontSize2=11; RateDiffSize=11;
		ovalIntv =5; LineLength=10; Radius=45;

		dummy=0;		

		lblSelMudPumpSrtY =5; 
		radioBtnSizeX = 85;
		radioBtnSizeY = 23;	
		txtPumpRSizeX = 50;
		txtPumpRSizeY = 18;
		ScrollSizeX = pnlPumpRateSizeX-lblSelMudPumpSrtY*2;
		//pnlPumpRateSizeY = lblSelMudPumpSrtY+30+radioBtnSizeY+btnSizeY+txtPumpRSizeY*3+20;	
		pnlPumpRateSizeY = panSizeY;		
		Stroke1=0; Stroke2=0; TotalStroke=0;

		TimerDrillOn = 0;
		TimerSIOn = 0;
		MainDriver.SItaskOn2=0;
		TimerWarnOn = 0;

		SITaskOn=1;
		TDTaskOn=1;
		TDTaskFinishedIndex = 1;//finish =0, start 1
		WarnTaskOn=1;
		DrillbtnOn=0;
		PumpbtnOn=0;
		MainDriver.iWshow=0;

		MainDriver.reset_Stroke1=0;
		MainDriver.reset_Stroke2=0;
		MainDriver.BlowOutOccurred=0;

		MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;	//히히
		//MainDriver.Qdrill = 337.088811730091;

		pumpScroll1.setMaximum(150);
		pumpScroll2.setMaximum(150);

		optPump1.setSelected(true);
		pumpScroll1.setValue(0);
		pumpScroll2.setValue(0);
		txtPumpR1.setText((new DecimalFormat("##0")).format(0));
		txtPumpR2.setText((new DecimalFormat("##0")).format(0));
		MainDriver.spMinD1 = pumpScroll1.getValue();
		MainDriver.spMinD2 = pumpScroll2.getValue();

		btnPumpOn1.setVisible(true);
		btnPumpOn2.setVisible(false);
		btnPumpOff1.setVisible(true);
		btnPumpOff2.setVisible(false);
		txtPumpR1.setVisible(true);
		txtPumpR2.setVisible(false);
		pumpScroll1.setVisible(true);
		pumpScroll2.setVisible(false);
		txtStks1.setVisible(true);
		txtStks2.setVisible(false);
		txtPumpStatus1.setVisible(true);
		txtPumpStatus2.setVisible(false);
		btnSetStkZero1.setVisible(true);
		btnSetStkZero2.setVisible(false);				

		Sa1= new SimAuto();
		Sm1= new SimManual();

		setBounds(Sm1.m3.widthX, 0, pan1Xsrt+btnSizeX*4+simAccPnlSizeX+panIntv*7, 10+simAccPnlSizeY+panIntv*5+panSizeY*3+btnSizeY*3);

		set_Default();	

		if(iPumpOn1==1) txtPumpStatus1.setText("On");
		else txtPumpStatus1.setText("Off");
		if(iPumpOn2==1) txtPumpStatus2.setText("On");
		else txtPumpStatus2.setText("Off");			
		MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
		//MainDriver.Qdrill = 337.088811730091; //히히
		//if igERD = 1 Then //not considering Multilateral
		//   setMLtrip
		//else
		setConventionalERD();
		
		if(MainDriver.iProblem[3]==1){ //cutting
			P2form = propertyModule.calcPoreFrac(vdBit)[0];
		}
		else{ // no loss, no cutting
			P2form = MainDriver.gMudOld * vdBit + psia;   //=Pbeff  //just assume hydrostatic p.
		}
	}

	static void getCti(){ // calculate Tb and total compressibility
		// where tbrankin, gasvisi, cti are global variables
		MainDriver.TbRankin = utilityModule.temperature(MainDriver.Vdepth);
		double Cform = 0.000003, Cwater = 0.000003, Swc = 0.25;
		double prup5=0, prlow5=0, zbup5=0, zblow5=0;
		double Presv = MainDriver.Pform, zb=0;
		double[] propEX = propertyModule.GasProp(Presv, MainDriver.TbRankin);//, gasVis, gasDen, zb);
		gasVis = propEX[0];
		gasDen = propEX[1];
		zb = propEX[2];

		MainDriver.gasVisi =  gasVis;
		prup5 = Presv + 5.0; 
		prlow5 = Presv - 5.0;
		zbup5 = propertyModule.Zfact(prup5, MainDriver.TbRankin);
		zblow5 = propertyModule.Zfact(prlow5, MainDriver.TbRankin);
		Cgi = 1 / Presv - 0.1 * (zbup5 - zblow5) / zb;
		MainDriver.Cti =  (Cform + Cwater * Swc + Cgi * (1.0 - Swc));
		MainDriver.fvfGas =  (0.00504 * zb * MainDriver.TbRankin / Presv);   // Gas FVF in rb/scf
		//	                                              // global variable
		surfTen = propertyModule.surfT(MainDriver.Pform, MainDriver.TbRankin);
	}

	static void getCti2(double depth, double pressure){ // calculate Tb and total compressibility
		// where tbrankin, gasvisi, cti are global variables
		//MainDriver.TbRankin = utilityModule.temperature(MainDriver.Vdepth);
		MainDriver.TbRankin = utilityModule.temperature(depth);
		double Cform = 0.000003, Cwater = 0.000003, Swc = 0.25;
		double prup5=0, prlow5=0, zbup5=0, zblow5=0;
		double Presv = pressure, zb=0;
		double[] propEX = propertyModule.GasProp(Presv, MainDriver.TbRankin);//, gasVis, gasDen, zb);
		gasVis = propEX[0];
		gasDen = propEX[1];
		zb = propEX[2];

		MainDriver.gasVisi =  gasVis;
		prup5 = Presv + 5.0; 
		prlow5 = Presv - 5.0;
		zbup5 = propertyModule.Zfact(prup5, MainDriver.TbRankin);
		zblow5 = propertyModule.Zfact(prlow5, MainDriver.TbRankin);
		Cgi = 1 / Presv - 0.1 * (zbup5 - zblow5) / zb;
		MainDriver.Cti =  (Cform + Cwater * Swc + Cgi * (1.0 - Swc));
		MainDriver.fvfGas =  (0.00504 * zb * MainDriver.TbRankin / Presv);   // Gas FVF in rb/scf
		//		                                              // global variable
		surfTen = propertyModule.surfT(pressure, MainDriver.TbRankin);
	}


	void CalcFracP(){
		//.... this is old ARD version to calculate PP and FP
		//
		//	    vdcsg = MainDriver.TVD[iCsg)
		//	    if ifG = 0 Then
		//	       PfgCsg = 0.052 * fgMud * vdcsg
		//	       PfgBHP = 0.052 * fgMud * Vdepth
		//	    else
		//	       if iOnshore = 1 Then
		//	          PfgCsg = eaton(vdcsg)
		//	          PfgBHP = eaton(Vdepth)
		//	       else
		//	          PfgCsg = prentice(vdcsg)
		//	          PfgBHP = prentice(Vdepth)
		//	       End if
		//	    End if
		//--- to check the under ground blow out; calculate the fracture gradient
		//	    at the last casing point and at the bottom.   7/2/97
		//--------------------------------------------- 8/01/02
		//   This is only valid for offshore well
		//   global variables; Pfgcsg, Pfgbhp, pore_casing, pore_bhp
		//
		//-------------------- The following will worl since we have all data in array by Sub RDcontrol()
		double vdcsg = MainDriver.TVD[MainDriver.iCsg];
		MainDriver.PfgCsg = propertyModule.Ditpl(MainDriver.PPdepth, MainDriver.FracP, vdcsg - MainDriver.Dwater, MainDriver.iFGnum);        //depth is BML in PPdepth()
		MainDriver.PfgBHP = propertyModule.Ditpl(MainDriver.PPdepth, MainDriver.FracP, MainDriver.Vdepth - MainDriver.Dwater, MainDriver.iFGnum);
		MainDriver.pore_Casing = propertyModule.Ditpl(MainDriver.PPdepth, MainDriver.PoreP, vdcsg - MainDriver.Dwater, MainDriver.iFGnum);   //depth is BML in PPdepth()
		MainDriver.pore_BHP = propertyModule.Ditpl(MainDriver.PPdepth, MainDriver.FracP, MainDriver.Vdepth - MainDriver.Dwater, MainDriver.iFGnum);
	}

	void set_Default(){	   
		double[] result = new double[2];
		btnSrtSim.setEnabled(true);
		//--- initialize the control variables;   7/11/2003
		Pdiff = -20.5;
		MainDriver.timeCsg = 0;
		MainDriver.timeBHP = 0;
		//....... calculate the initial conditions
		if(MainDriver.igERD == 1) MainDriver.timeToTD = 0;   //we assumed that we have drilled gPayLength as TD

		if(MainDriver.iHuschel==1){
			//MainDriver.mdBitOff = (1500-1257)/0.3048;
		}
		else if(MainDriver.iHuschel==2){
			//MainDriver.mdBitOff = (4500-4014)/0.3048;
		}

		AeobRad = MainDriver.ang2EOB * MainDriver.radConv;  //later use 2.5 ft or 5 mins
		//
		vdBit = MainDriver.Vdepth - MainDriver.mdBitOff * Math.cos(AeobRad);
		hdBit = MainDriver.Hdisp - MainDriver.mdBitOff * Math.sin(AeobRad);   


		if (hdBit < 0.002) hdBit = 0;
		//
		psia = 14.7;
		holeMD = MainDriver.TMD[MainDriver.NwcS-1];	    
		Pcasing = MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + psia;
		MainDriver.gTcum = 0;
		xNull = 0;
		PVgain = 0;
		mlPVgain = 0;
		Qgas = 0; QtotVol = 0;  QgDay = 0;  QgOldMscf = 0;
		iPumpOn1 = 0;      //pump on(1), off(0)
		iPumpOn2 = 0;      //pump on(1), off(0)
		iROP = 0;         //no rotation(0), rotation(1)
		timeNp = 0;
		timePumpOff = 0;
		iTimerWarn = 0; iTimerDrill = 0; iTimerShutin = 0;   //for Pause/Continue menu
		iTimerTrip = 0; iTimerStrip = 0;
		iShowData = 0; iShutinData = 0; iDataChange = 0;
		gasFlowOut = 0; mudFlowOut = 0;
		mudLineP = MainDriver.gMudOld * MainDriver.Dwater + psia;
		txtMudLine.setText((new DecimalFormat("###,##0")).format(mudLineP - 14.7));
		gasDen = 0;
		MainDriver.iWshow = 0; iSound = 1; iStable = 0; SIcount = 0;
		iPGwarn = 0; iPenet = -1;


		//
		txtRateDiff.setText( "0.0");
		txtPchoke.setText( "0.0");
		txtPsp.setText( "0.0");
		txtPp.setText("0.0");

		//Added by TY
		if(MainDriver.ibaseoil == 0){ //No.2 Diesel
			MainDriver.OBMdensity = 6.9358; //ppg
			MainDriver.OBMwt = 204.2; //lb/lb-mole
		}
		else if(MainDriver.ibaseoil == 1){ //Mentor28
			MainDriver.OBMdensity = 7.1168;
			MainDriver.OBMwt = 252;
		}
		else{ //Conoco LVT
			MainDriver.OBMdensity = 6.7426;
			MainDriver.OBMwt = 177.35;
		}

		//
		getCti();       //calc. gas FVF and properties
		//........................................................... assign the static data
		kickVD = 0; kickVDbottom = 0; MainDriver.Xb = MainDriver.TMD[MainDriver.NwcS-1];
		pumpP = psia; standpipeP = psia; Pbeff = MainDriver.gMudOld * vdBit + 14.7;
		if(MainDriver.iProblem[3]==1){
			propertyModule.calcBaseProp(vdBit);
			MainDriver.CF_Base = propertyModule.calcCF(MainDriver.WOB_Base, vdBit);
		}
		Pkick = psia;
		Pchoke = psia; Qt = 0;   //this is necessary to calculate mudline p., 7/17/02
		dxSlip = 0;               //slip distance for continuous shut-in
		MainDriver.Np = 0;
		MainDriver.CHKopen[0] = 100;
		MainDriver.PmLine[0] =  mudLineP;
		MainDriver.Pb2p[0] =  Pbeff; 
		MainDriver.Pcsg[0] =  Pcasing;

		menuSaveSID.setEnabled(false);
		menuShowData.setEnabled(false);

		if(MainDriver.iMudComp==1) lblMudComp.setText("Mud compressibility is considered !!");
		else lblMudComp.setText("Mud compressibility is NOT considered !!");

		if(MainDriver.iCHKcontrol == 2){
			optManual.setSelected(true);
			optAutomatic.setSelected(false);
		}
		else{
			optAutomatic.setSelected(true);
			optManual.setSelected(false);
		}

		if(MainDriver.iKill == 1 && MainDriver.iChoke == 1){
			optChoke.setSelected(false);
			optKill.setSelected(false);
			optBoth.setSelected(true);
		}
		else if (MainDriver.iKill == 0 && MainDriver.iChoke == 1){
			optKill.setSelected(false);
			optBoth.setSelected(false);
			optChoke.setSelected(true);
		}
		else{
			optChoke.setSelected(false);
			optBoth.setSelected(false);
			optKill.setSelected(true);
		}
		Stroke1=0;
		Stroke2=0;
		TotalStroke = Stroke1+Stroke2;
		txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));
		txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));
	}

	public static void setVisible(){

	}


	class SITask extends TimerTask{
		double KickVDold = 0, xmdloc=0, xLoc=0, Xb=0;
		double DPinc = 0;
		int dummy=0;
		double KickVDMax=0;
		double hxt = 0, xbcell = 0;
		public void run(){			//			
			if(SITaskOn==1&&TDTaskFinishedIndex==0){
				
				P2form = MainDriver.Pform;
				MainDriver.SItaskOn2=1;				
				SIcount = SIcount + 1;
				MainDriver.gDelT = TimeDrlIntv * 0.001 * MainDriver.SimRate;
				//MainDriver.gDelT =  (5000 * 0.001 * MainDriver.SimRate);//히히
				MainDriver.gTcum = MainDriver.gTcum + MainDriver.gDelT;
				//----------------------- Continuous Shutin --------------------------
				if (SIcount == 1) ContSI();
				//Modified by TY
				//In shut in situation, DPinc should be increased continuously due to gas migration
				//DPinc = 0#
				//dxSlip = 0

				MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
				if(MainDriver.iHuschel==1){
					MainDriver.Qdrill = 530;
				}
				else if(MainDriver.iHuschel==2){
					MainDriver.Qdrill = 450;
				}
				else{
					//MainDriver.Qdrill = 337.088811730091; //히히
				}
				Qcirc = MainDriver.Qdrill;
				MainDriver.volPump = MainDriver.volPump + Qcirc * MainDriver.gDelT / (42 * 60);

				Stroke1=(int)(Stroke1+MainDriver.gDelT/60*MainDriver.spMinD1*iPumpOn1);
				Stroke2=(int)(Stroke2+MainDriver.gDelT/60*MainDriver.spMinD2*iPumpOn2);
				TotalStroke = Stroke1 + Stroke2;

				txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));
				txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));

				KickVDMax=kickVD;
				//else KickVDMax=MainDriver.xVDupSI;

				if (nHorizon != 1 && KickVDMax>1){
					xmdloc = holeMD - kickMD;
					dxSlip = dxSlip + vSlip * MainDriver.gDelT;
					xLoc = holeMD - kickMD - dxSlip;
					KickVDold = kickVD;
					kickVD=utilityModule.getVD(xLoc);
					
					hxt = utilityModule.getTopH(PVgain, xLoc);
		    	    xbcell = xLoc + hxt; // xcell : top location of the cell, xbcell : bottom location of the cell
					kickVDbottom =utilityModule.getVD(xbcell);
					//Modified by TY
					//In shut in situation, DPinc should be increased continuously due to gas migration					
					//DPinc = MainDriver.gMudOld * (KickVDold - kickVD); 
					//if (DPinc < 0) DPinc = 0;
					DPinc = DPinc + MainDriver.gMudOld * (KickVDold - kickVD); 
					if (DPinc < 0) DPinc = 0;
					Xb = xLoc;
				}
				else if(KickVDMax<=1){
					String s = "You shut in the well too long without any action.";
					JOptionPane.showMessageDialog(null, s, "Warning", JOptionPane.INFORMATION_MESSAGE);//일단 얘도 pass
					menuClose();
				}

				//------------------------------------------- add the pressure incresement
				pumpP = psia;
				Pbeff = MainDriver.Pform + DPinc;
				standpipeP = MainDriver.SIDPP + DPinc;
				Pcasing = PcsgSI + DPinc;
				Pchoke = MainDriver.SICP + DPinc;
				timeNp = 2;   //i.e., do not save the results !
				ShowResult();
				if(propertyModule.FractGrad(Pcasing, Pbeff)<0){;     //check possible formation fracture
				if(TimerWarnOn==1){
					TimerWarn.cancel();
					TimerWarnOn=0;
				}
				if(TimerDrillOn==1){
					TimerDrill.cancel();
					TimerDrillOn=0;
				}
				if(TimerSIOn==1){
					TimerSI.cancel();
					TimerSIOn=0;
					MainDriver.SItaskOn2=0;
				}				
				if(Sm1.m3.TimerRotOn==1){
					Sm1.m3.TimerRot.cancel();
					Sm1.m3.TimerRotOn=0;
				}			
				MainDriver.iWshow = 0;

				if(TimerBOOn==0){
					TimerCheckBO = new Timer();
					TimerCheckBO.schedule(new CheckBlowoutTask(), 0, 50);
					TimerBOOn=1;
				}
				}
				if (SIcount %2 == 1) DrillAssign();	
			}

			MainDriver.t_problem[1] = MainDriver.t_problem[1]+MainDriver.gDelT;
			if(MainDriver.t_problem[1]-MainDriver.t_problem[0]>=MainDriver.tstep_problem){
				checkProblem(); //140912 ajw
				MainDriver.t_problem[0]=MainDriver.t_problem[1];
			}
			// Problem
			if(MainDriver.iProblem_occured[2]==1){
				double h_bottom = mdBit;
				if(h_bottom>MainDriver.layerVDto[MainDriver.layer_mud_loss]) h_bottom = MainDriver.layerVDto[MainDriver.layer_mud_loss];
				double h_top = utilityModule.getMD(MainDriver.layerVDfrom[MainDriver.layer_mud_loss]);
				double h_target = h_bottom - h_top;
				double perm_target = MainDriver.layerPerm[MainDriver.layer_mud_loss];	
				if(h_target>100) h_target=100;//ft
				MainDriver.MD_Loss_center = (h_bottom+h_top)/2;
				MainDriver.MD_Loss_length = h_target;
				MainDriver.Q_loss = 7.08*Math.pow(10, -3)*perm_target*h_target*(Pbeff-P2form)/MainDriver.visL/Math.log(MainDriver.R_reservoir/(MainDriver.DiaHole/12/2)); // bbl/day
				MainDriver.Q_loss = MainDriver.Q_loss*42/24/60; //gpm
				MainDriver.V_loss = MainDriver.V_loss + MainDriver.Q_loss/42/60*MainDriver.gDelT;//bbl
				//MainDriver.Q_loss = MainDriver.Q_loss*MainDriver.gDelT/(60*60*24);
			}
			else{
				MainDriver.Q_loss = 0;//GPM
				MainDriver.V_loss = 0;//bbl
			}
		}
	}


	class TDTask extends TimerTask{
		//.......... interval 1000 is equivalent 1 sec !   7/12/02
		int NpumpOff=0, dummy=1, dummy2=0;// 0 : fail in wellkick calculation 1; success!!
		double pxOld=0, pxMid=0, delKick=0, sPVgainOld=0, compVol=0, DPincrease=0, xmdloc=0, avgden=0;
		int srtNum = 0;
		double xLoc=0, KickVDold=0, DPinc=0, Xb=0, mdBit=0;
		double[] getDPinsideEX = new double[2];
		double[] porefracP = new double[2];
		double poreP=0;
		double fracP=0;
		double[] vs_ff =  new double[2];
		double vslip_cutting=0;
		double[] Q_in = new double[MainDriver.Nwc];
		double[] Q_out = new double[MainDriver.Nwc];
		double RAD_CONVERT = 3.141592 / 180;
		double d1=0, d2 = 0;
		double Torque = MainDriver.Torque_Base;
		double RPM = MainDriver.RPM_Base;
		double WOB = MainDriver.WOB_Base;
		double vel_a=0;
		int temp_NwcS = 0;
		double intv_record = 5*60;
		int pos_Top = 0;
		int pos_cut_up = 0;
		int pos_cut_down = 0;
		double[] D_result_ex = new double[MainDriver.sizeResult];
		double[] VD_result_ex = new double[MainDriver.sizeResult];		
		double[] P_hydro_ex = new double[MainDriver.sizeResult];
		double[] P_fric_ex = new double[MainDriver.sizeResult];
		double TopVD = 0;
		double ed=0, CON=0, ren=0, ff_mud=0, P_fric=0, P_hydro=0;
		double Qcirc_sec = 0, Q_in_mud=0, Q_out_mud=0;
		double h_cutting = 0;
		double temp=0;

		public void run(){
			TDTaskFinishedIndex = 1;
			dummy = (int)(MainDriver.spMinD1*MainDriver.spMinD1 + MainDriver.spMinD2*MainDriver.spMinD2);
			dummy2 = iPumpOn1*iPumpOn1+iPumpOn2*iPumpOn2;

			if (MainDriver.iBOP==0 && xDrill < MainDriver.mdBitOff && TDTaskOn==1){ //20140218 for shutin before kick occrrence	
				if(MainDriver.iHuschel==1){
					MainDriver.gDelT = 10;
					MainDriver.Qdrill = 530;
				}
				else if(MainDriver.iHuschel==2){
					MainDriver.gDelT = 10;//2
					MainDriver.Qdrill = 450;
				}
				else{
					MainDriver.gDelT = TimeDrlIntv * 0.001 * MainDriver.SimRate; //sec
					MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2; //히히
					//MainDriver.Qdrill = 337.088811730091; //히히
				}
				//MainDriver.gDelT =  (5000 * 0.001 * MainDriver.SimRate);//히히
				MainDriver.gTcum = MainDriver.gTcum + MainDriver.gDelT;   // total time from drilling
				timeNp = (int) (timeNp + MainDriver.gDelT);
				Stroke1=(int)(Stroke1+MainDriver.gDelT/60*MainDriver.spMinD1*iPumpOn1);
				Stroke2=(int)(Stroke2+MainDriver.gDelT/60*MainDriver.spMinD2*iPumpOn2);
				TotalStroke = Stroke1 + Stroke2;

				txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));
				txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));

				Qcirc = 0; Qgas = 0; Qt = 0;
				Pbeff = MainDriver.Pform + 0.001; 
				MainDriver.SICP = (Pkick - MainDriver.gMudOld * kickVD);

				SIcount = SIcount + 1;

				//----------------------- Continuous Shutin --------------------------
				if (SIcount == 1) ContSI();

				Qcirc = MainDriver.Qdrill;
				MainDriver.volPump = MainDriver.volPump + Qcirc * MainDriver.gDelT / (42 * 60);

				Stroke1=(int)(Stroke1+MainDriver.gDelT/60*MainDriver.spMinD1*iPumpOn1);
				Stroke2=(int)(Stroke2+MainDriver.gDelT/60*MainDriver.spMinD2*iPumpOn2);
				TotalStroke = Stroke1 + Stroke2;

				txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));
				txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));

				//------------------------------------------- add the pressure incresement

				//히히
				double tempPform = 0, bulk_density = 0, depthTmp=0;
				pumpP = psia;
				depthTmp = vdBit - MainDriver.Dwater;
				bulk_density = 5.3 * Math.pow(depthTmp, 0.1356);
				tempPform = calcPoreP(vdBit); //Assign pore & fracture pressures based on bulk density: John Barker AADE 97 paper
				Pbeff = MainDriver.gMudOld * vdBit +  psia;
				P2form = Pbeff;				
				if(MainDriver.iProblem[3]==1 || MainDriver.iProblem[2]==1){
					porefracP = propertyModule.calcPoreFrac(vdBit);
					P2form = porefracP[0];
				}
				standpipeP = 14.7;
				Pchoke = standpipeP;
				Pcasing = Pchoke + MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg];				
				timeNp = 2;   //i.e., do not save the results !
				ShowResult();
				if(propertyModule.FractGrad(Pcasing, Pbeff)<0){;     //check possible formation fracture
				if(TimerWarnOn==1){
					TimerWarn.cancel();
					TimerWarnOn=0;
				}
				if(TimerDrillOn==1){
					TimerDrill.cancel();
					TimerDrillOn=0;
				}
				if(TimerSIOn==1){
					TimerSI.cancel();
					TimerSIOn=0;
					MainDriver.SItaskOn2=0;
				}				
				if(Sm1.m3.TimerRotOn==1){
					Sm1.m3.TimerRot.cancel();
					Sm1.m3.TimerRotOn=0;
				}			
				MainDriver.iWshow = 0;

				if(TimerBOOn==0){
					TimerCheckBO = new Timer();
					TimerCheckBO.schedule(new CheckBlowoutTask(), 0, 50);
					TimerBOOn=1;
				}
				}
				if (SIcount %2 == 1) DrillAssign();		
			}

			else if(TDTaskOn==1){
				if(MainDriver.iHuschel==1){
					MainDriver.gDelT = 10;
				}
				else if(MainDriver.iHuschel==2){
					MainDriver.gDelT = 10;//2;
				}
				else{
					MainDriver.gDelT = TimeDrlIntv * 0.001 * MainDriver.SimRate; //sec
				}
				//MainDriver.gDelT =  (5000 * 0.001 * MainDriver.SimRate);//히히
				MainDriver.gTcum = MainDriver.gTcum + MainDriver.gDelT;   // total time from drilling
				timeNp = (int) (timeNp + MainDriver.gDelT);
				//					    timeSim = Hour(Now) * 3600 + Minute(Now) * 60 + Second(Now)
				//					    MainDriver.gTcum = (timeSim - timeStart)
				//
				iRotary++;
				if (iRotary > 100) iRotary = 1;				
				// ROP = MainDriver.ROPen; // ROP3 AJW				


				MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 * iPumpOn1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2 * iPumpOn2;
				//MainDriver.Qdrill = 337.088811730091/50.0*200.0; //히히
				Qcirc = MainDriver.Qdrill;
				MainDriver.volPump = MainDriver.volPump + Qcirc * MainDriver.gDelT / (42 * 60);
				
				Stroke1=(int)(Stroke1+MainDriver.gDelT/60*MainDriver.spMinD1*iPumpOn1);
				Stroke2=(int)(Stroke2+MainDriver.gDelT/60*MainDriver.spMinD2*iPumpOn2);
				TotalStroke = Stroke1 + Stroke2;

				txtStks1.setText((new DecimalFormat("###,##0")).format(Stroke1-MainDriver.reset_Stroke1));
				txtStks2.setText((new DecimalFormat("###,##0")).format(Stroke2-MainDriver.reset_Stroke2));
				
				if(MainDriver.i_ROPVERSION==2 && MainDriver.gTcum==MainDriver.gDelT){
					MainDriver.ROPen = propertyModule.calcROP(RPM, Pbeff,  WOB,  Qcirc, vdBit); // 1) ROP 갱신
				}
				
				if(xDrill < MainDriver.mdBitOff) ROP = 0.5 * MainDriver.ROPen;   // ROP1 Kick 도달위치 닫기 전

				if((iPumpOn1==0 && iPumpOn2 == 0)||(MainDriver.spMinD1==0 && MainDriver.spMinD2==0)){   //rotating with pump off-ROP should decrease !
					timePumpOff = timePumpOff + (int)MainDriver.gDelT;
					NpumpOff = (int)(timePumpOff / 30);  //90% decrease every 30 sec
					if(MainDriver.iHuschel<1){
						ROP = ROP * Math.pow(0.9, NpumpOff); // ROP 2 펌프징지시
					}
				}

				xDrill = xDrill + iROP * ROP * MainDriver.gDelT / 3600;	 // 20140219 ajw		



				// no cutting 기록을 위해..

				//mdBit = MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff;
				//vdBit =  (MainDriver.Vdepth  - MainDriver.mdBitOff * Math.cos(AeobRad));					 
				//hdBit =  (MainDriver.Hdisp - MainDriver.mdBitOff * Math.sin(AeobRad));


				MainDriver.P_ann[0][MainDriver.NwcE]=14.7;
				MainDriver.D_result[0][MainDriver.NwcE]=MainDriver.TMD[MainDriver.NwcE-1];

				if(MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff < MainDriver.TMD[MainDriver.NwcS]){
					temp_NwcS = MainDriver.NwcS+1;
				}
				else{
					temp_NwcS = MainDriver.NwcS;
				}

				for(int i=MainDriver.NwcE-1; i>=temp_NwcS; i--){
					// Friction pressure loss 구하기 : 암석의 ff도 고려
					double ff_mud=0, ed=0, CON=0, ren=0, Vcon=0, P_fric=0, P_hydro=0;
					d2 = MainDriver.Do2p[i];
					d1 = MainDriver.Di2p[i];
					vel_a = Qcirc / 2.448 / (d2 * d2 - d1 * d1);
					ed = MainDriver.Ruf / (d2 - d1);							    
					CON = 1;					    
					if (d1 > 0.002) CON = 0.816496581;
					ren = propertyModule.pRen(d2, d1, vel_a, MainDriver.oMud_save);
					ff_mud=propertyModule.getf(ed, ren);
					if(i>temp_NwcS){
						P_fric = ff_mud * MainDriver.oMud_save * vel_a*vel_a / (25.81 * CON * (d2 - d1))*(MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
						P_hydro = 0.052*MainDriver.oMud_save*(MainDriver.TVD[i - 1] - MainDriver.TVD[i]);
						MainDriver.D_result[0][i]=MainDriver.TMD[i-1];
					}
					else{
						P_fric = ff_mud * MainDriver.oMud_save * vel_a*vel_a / (25.81 * CON * (d2 - d1))*(MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff - MainDriver.TMD[i]);
						P_hydro = 0.052*MainDriver.oMud_save*(utilityModule.getVD(MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff) - MainDriver.TVD[i]);
						MainDriver.D_result[0][i]=MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff;
					}
					MainDriver.P_ann[0][i]=MainDriver.P_ann[0][i+1]+P_hydro+P_fric;
					MainDriver.P_ann_ex[i-1] = MainDriver.P_ann[0][i];
				}
				
				// 150818

				if (xDrill < MainDriver.mdBitOff){
					
					Qgas = 0; // mode 1: moving grid
					Pkick = psia;
					mdBit = MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff + xDrill;
					vdBit =  (MainDriver.Vdepth + (xDrill - MainDriver.mdBitOff) * Math.cos(AeobRad));					 
					hdBit =  (MainDriver.Hdisp + (xDrill - MainDriver.mdBitOff) * Math.sin(AeobRad));
					if(MainDriver.iProblem[3]==1){
						// 기본변수 초기화
						MainDriver.vdbit_now = vdBit;													
						Qcirc_sec = Qcirc/42/60; // gpm -> bbl/sec
						Q_in_mud = Qcirc_sec*MainDriver.gDelT; // bbl
						MainDriver.V_ann_cutting_tot = MainDriver.VOLout;							
						MainDriver.P_ann_ex[MainDriver.NwcE-1] = 14.7;

						/*if(MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff + xDrill < MainDriver.TMD[MainDriver.NwcS]){
							iBtm = utilityModule.Xposition(MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff + xDrill)+1;
							for(int i=MainDriver.NwcS; i<=iBtm-1; i++){
								MainDriver.V_ann_cutting_tot = MainDriver.V_ann_cutting_tot - MainDriver.VOLann[i];
								MainDriver.TMD_cutting[i-1] = 0;
								MainDriver.TVD_cutting[i-1] = 0;
								MainDriver.V_ann_cutting[i] = 0;
								MainDriver.Area_ann[i] = MainDriver.VOLann[i]*5.6145/(MainDriver.TMD[i-1]-MainDriver.TMD[i]); // ft2
							}						
							MainDriver.V_ann_cutting_tot = MainDriver.V_ann_cutting_tot - MainDriver.VOLann[iBtm]*(MainDriver.TMD[iBtm-1]-mdBit)/(MainDriver.TMD[iBtm-1]-MainDriver.TMD[iBtm]);
							MainDriver.TMD_cutting[iBtm-1] = mdBit;
							MainDriver.TVD_cutting[iBtm-1] = utilityModule.getVD(MainDriver.TMD_cutting[iBtm-1]);						
							MainDriver.V_ann_cutting[iBtm] = MainDriver.VOLann[iBtm]*(MainDriver.TMD_cutting[iBtm-1]-MainDriver.TMD[iBtm])/(MainDriver.TMD[iBtm-1]-MainDriver.TMD[iBtm]); //bbl
							MainDriver.Area_ann[iBtm] = MainDriver.V_ann_cutting[iBtm]*5.6145/(MainDriver.TMD_cutting[iBtm-1]-MainDriver.TMD_cutting[iBtm]); // ft2
						}

						else{
							MainDriver.V_ann_cutting_tot = MainDriver.V_ann_cutting_tot - MainDriver.VOLann[MainDriver.NwcS]*(MainDriver.TMD[MainDriver.NwcS-1]-mdBit)/(MainDriver.TMD[MainDriver.NwcS-1]-MainDriver.TMD[MainDriver.NwcS]);
							MainDriver.TMD_cutting[MainDriver.NwcS] = MainDriver.TMD[MainDriver.NwcS];
							MainDriver.TMD_cutting[MainDriver.NwcS-1] = mdBit;
							MainDriver.TVD_cutting[MainDriver.NwcS] = utilityModule.getVD(MainDriver.TMD_cutting[MainDriver.NwcS]);
							MainDriver.TVD_cutting[MainDriver.NwcS-1] = utilityModule.getVD(MainDriver.TMD_cutting[MainDriver.NwcS-1]);
							MainDriver.V_ann_cutting[MainDriver.NwcS+1] = MainDriver.VOLann[MainDriver.NwcS+1];
							MainDriver.V_ann_cutting[MainDriver.NwcS] = MainDriver.VOLann[MainDriver.NwcS]*(MainDriver.TMD_cutting[MainDriver.NwcS-1]-MainDriver.TMD[MainDriver.NwcS])/(MainDriver.TMD[MainDriver.NwcS-1]-MainDriver.TMD[MainDriver.NwcS]); //bbl
							MainDriver.Area_ann[MainDriver.NwcS+1] = MainDriver.V_ann_cutting[MainDriver.NwcS+1]*5.6145/(MainDriver.TMD_cutting[MainDriver.NwcS]-MainDriver.TMD_cutting[MainDriver.NwcS+1]); // ft2
							MainDriver.Area_ann[MainDriver.NwcS] = MainDriver.V_ann_cutting[MainDriver.NwcS]*5.6145/(MainDriver.TMD_cutting[MainDriver.NwcS-1]-MainDriver.TMD_cutting[MainDriver.NwcS]); // ft2
							//if(MainDriver.f_cutting[MainDriver.NwcS]==0) MainDriver.f_cutting[MainDriver.NwcS] = MainDriver.f_cutting[MainDriver.NwcS+1];
							iBtm = MainDriver.NwcS;
						}*/

						if(MainDriver.gTcum > MainDriver.gDelT){
							MainDriver.NwcS_cutting_old = MainDriver.NwcS_cutting;
							MainDriver.NwcE_cutting_old = MainDriver.NwcE_cutting;
							for(int i=MainDriver.NwcS_cutting-1; i<MainDriver.NwcE_cutting; i++){
								MainDriver.TMD_cutting_old[i] = MainDriver.TMD_cutting[i];
								MainDriver.V_cutting_old[i] = MainDriver.V_cutting[i];
								MainDriver.f_cutting_old[i] = MainDriver.f_cutting[i];
								MainDriver.v_real_cutting_old[i] = MainDriver.v_real_cutting[i];
								MainDriver.dis_cut_from_top_old[i] = MainDriver.dis_cut_from_top[i];									
							}
						}

						MainModule.setMDvd_cutting(vdBit, mdBit);

						// 위치 변화에 따른 일부 값 보
						double mslope = 0, max = 0;
						if(MainDriver.gTcum > MainDriver.gDelT){
							for(int i=MainDriver.NwcE_cutting-1; i>=MainDriver.NwcS_cutting; i--){
								MainDriver.V_cutting[i] = 0; MainDriver.v_real_cutting[i]=0; MainDriver.dis_cut_from_top[i]=0;
								for(int j=MainDriver.NwcE_cutting_old-1; j>=MainDriver.NwcS_cutting_old; j--){
									mslope=0;
									max = MainDriver.TMD_cutting_old[j];
									if(MainDriver.TMD_cutting_old[j]<MainDriver.TopCutting && MainDriver.TMD_cutting_old[j-1]>MainDriver.TopCutting) max = MainDriver.TopCutting;

									if(MainDriver.TMD_cutting[i]>=max && MainDriver.TMD_cutting[i]<MainDriver.TMD_cutting_old[j-1]){
										if(MainDriver.TMD_cutting[i-1]>=MainDriver.TMD_cutting_old[j-1]){ // 새로운 셀 기준으로 예전 셀이 위에 걸쳐있음
											mslope=(MainDriver.TMD_cutting_old[j-1]-MainDriver.TMD_cutting[i])/(MainDriver.TMD_cutting_old[j-1]-max);}
										else{
											mslope=(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/(MainDriver.TMD_cutting_old[j-1]-max);}
									}
									else if(MainDriver.TMD_cutting[i-1]>max&&MainDriver.TMD_cutting[i]<=max){
										if(MainDriver.TMD_cutting[i-1]>MainDriver.TMD_cutting_old[j-1]){
											mslope = 1;}
										else{
											mslope = (MainDriver.TMD_cutting[i-1]-max)/(MainDriver.TMD_cutting_old[j-1]-max);}
									}

									MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + MainDriver.V_cutting_old[j]*mslope;
									MainDriver.v_real_cutting[i] = MainDriver.v_real_cutting[i] + MainDriver.v_real_cutting_old[j]*mslope;
									MainDriver.dis_cut_from_top[i] = MainDriver.dis_cut_from_top[i] + MainDriver.dis_cut_from_top_old[j]*mslope;

								}				
								MainDriver.f_cutting[i] = MainDriver.V_cutting[i]/MainDriver.V_Cut_mix[i];

								if(MainDriver.TMD_cutting[i]<MainDriver.TopCutting && MainDriver.TMD_cutting[i-1]>MainDriver.TopCutting){
									MainDriver.v_real_cutting[i] = MainDriver.v_real_cutting[i];
									MainDriver.dis_cut_from_top[i] = MainDriver.dis_cut_from_top[i];
								}
								else{
									MainDriver.v_real_cutting[i] = MainDriver.v_real_cutting[i]/(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]);
									MainDriver.dis_cut_from_top[i] = MainDriver.dis_cut_from_top[i]/(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]);
								}
							}					
						}
						iBtm = MainDriver.NwcS_cutting;						
						d1 = MainDriver.Di2p_cut[MainDriver.NwcS_cutting]/12; //ft
						d2 = MainDriver.Do2p_cut[MainDriver.NwcS_cutting]/12; //ft

						Q_in[iBtm] = ROP*MainDriver.gDelT/3600*(Math.PI/4*Math.pow(d2,2)) / 5.6145; // ft/hr * ft^2 * hr / 5.6145 = bbl	
						//
						// 위치계산
						if(MainDriver.gTcum == MainDriver.gDelT){ // 초기 킥 유입시
							MainDriver.V_cutting_tot=0;
							double V_ann_cum = 0;
							for(int i=iBtm; i<MainDriver.NwcE_cutting; i++){
								
								V_ann_cum = V_ann_cum + MainDriver.V_ann_cutting[i];
								d1 = MainDriver.Di2p_cut[i]; //inch
								d2 = MainDriver.Do2p_cut[i]; //inch
								if(Math.abs(1-MainDriver.f_cutting[i])<0.0001) vel_a = 0;
								else vel_a = (Qcirc_sec*5.6145)/MainDriver.Area_ann[i];///(1-MainDriver.f_cutting[i]); // ft/s
								
								if(MainDriver.iProblem_occured[2]==1){
									if(MainDriver.TMD_cutting[i]>MainDriver.MD_Loss_center && MainDriver.TMD_cutting[i-1]<=MainDriver.MD_Loss_center){ // 현재 셀이 loss 구간을 포함하는 경우
										vel_a = ((Qcirc_sec-MainDriver.Q_loss/2)*5.6145)/MainDriver.Area_ann[i];///(1-MainDriver.f_cutting[i]); // ft/s
									}
									if(MainDriver.TMD_cutting[i]<MainDriver.MD_Loss_center){ // 현재 셀이 loss 구간 위에 있는 경우
										vel_a = ((Qcirc_sec-MainDriver.Q_loss)*5.6145)/MainDriver.Area_ann[i];///(1-MainDriver.f_cutting[i]); // ft/s
									}
									if(vel_a<0){
										System.out.println("vel_a<0");
										vel_a=0.001;
									}
								}
								
								if(Q_in[i]+Q_in_mud<=V_ann_cum){
									if(i==MainDriver.NwcS_cutting){
										MainDriver.TopCutting = MainDriver.TMD_cutting[MainDriver.NwcS_cutting-1] - (Q_in[i]+Q_in_mud)*5.6145/MainDriver.Area_ann[i];		
									}
									else{
										MainDriver.TopCutting = MainDriver.TMD_cutting[i-1] - ((Q_in[i]+Q_in_mud)-MainDriver.V_ann_cutting[i-1])*5.6145/MainDriver.Area_ann[i];		
									}
									if(Q_in_mud+Q_in[i]==0){
										MainDriver.f_cutting[i] = 0;
										MainDriver.f_cutting_ini = 0;
									}
									else{
										MainDriver.f_cutting[i] = Q_in[i]/(Q_in_mud+Q_in[i]);
										MainDriver.f_cutting_ini = Q_in[i]/(Q_in_mud+Q_in[i]);
									}
									MainDriver.v_real_cutting[i] = (MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting)/MainDriver.gDelT;
									MainDriver.vslip_cutting[i] = vel_a -MainDriver.v_real_cutting[i];
									if(MainDriver.vslip_cutting[i]<0) MainDriver.vslip_cutting[i]=0;
									vs_ff = propertyModule.Calc_vslip_cut(vel_a, d1, d2, i, (MainDriver.ang2p_cutting[i-1]+MainDriver.ang2p_cutting[i])/2);
									MainDriver.vfall_cutting[i] = vs_ff[2];
									MainDriver.oMudCutting[i] = MainDriver.f_cutting[i]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i])*MainDriver.oMud_save; 
									MainDriver.V_cutting[i] = (MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting) * MainDriver.f_cutting[i] * MainDriver.Area_ann[i]/5.6145;
									MainDriver.V_Mud[i] = (MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting) * (1-MainDriver.f_cutting[i]) * MainDriver.Area_ann[i]/5.6145;	
									MainDriver.V_Cut_mix[i] = MainDriver.V_cutting[i] + MainDriver.V_Mud[i];

									//vs_ff = propertyModule.Calc_vslip_cut(vel_a, d1, d2, i, (MainDriver.ang2p[i-1]+MainDriver.ang2p[i])/2);									
									//MainDriver.vsl_cutting[i] = vs_ff[1];									

									break;
								}
								else{
									MainDriver.TopCutting = MainDriver.TMD_cutting[i];
									if(Q_in_mud+Q_in[i]==0) MainDriver.f_cutting[i] = 0;
									else MainDriver.f_cutting[i] = Q_in[i]/(Q_in_mud+Q_in[i]);
									MainDriver.oMudCutting[i] = MainDriver.f_cutting[i]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i])*MainDriver.oMud_save; 
									MainDriver.V_cutting[i] = (MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting) * MainDriver.f_cutting[i] * MainDriver.Area_ann[i]/5.6145;
									MainDriver.V_Mud[i] = (MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting) * (1-MainDriver.f_cutting[i]) * MainDriver.Area_ann[i]/5.6145;	
									MainDriver.V_Cut_mix[i] = MainDriver.V_cutting[i] + MainDriver.V_Mud[i];

									//vs_ff = propertyModule.Calc_vslip_cut(vel_a, d1, d2, i, (MainDriver.ang2p[i-1]+MainDriver.ang2p[i])/2);									
									//MainDriver.vsl_cutting[i] = vs_ff[1];
									MainDriver.v_real_cutting[i] = (MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting)/MainDriver.gDelT;
									MainDriver.vslip_cutting[i] = vel_a -MainDriver.v_real_cutting[i];
									if(MainDriver.vslip_cutting[i]<0) MainDriver.vslip_cutting[i]=0;
									vs_ff = propertyModule.Calc_vslip_cut(vel_a, d1, d2, i, (MainDriver.ang2p_cutting[i-1]+MainDriver.ang2p_cutting[i])/2);
									MainDriver.vfall_cutting[i] = vs_ff[2];
								}
								MainDriver.V_cutting_tot = MainDriver.V_cutting_tot + MainDriver.V_cutting[i]; // 누적 cutting
							}
						}

						else if(MainDriver.gTcum>MainDriver.gDelT && MainDriver.TopCutting>0){ // 킥 mixture가 생성 후 아직 top에 도달하지 않은 시점
							//f_cutting 설정
							if(MainDriver.f_cutting_ini==0 && (Q_in_mud+Q_in[iBtm])!=0) MainDriver.f_cutting_ini = Q_in[iBtm]/(Q_in_mud+Q_in[iBtm]);
							pos_Top = utilityModule.Xposition_cutting(MainDriver.TopCutting)+1; // cell의 상부
							for(int i=iBtm; i<=pos_Top; i++){ // cutting으로 채워져 있음
								//vslip 계산
								d1 = MainDriver.Di2p_cut[i]; //inch
								d2 = MainDriver.Do2p_cut[i]; //inch

								if(Math.abs(1-MainDriver.f_cutting[i])<0.0001) vel_a = 0;
								else vel_a = (Qcirc_sec*5.6145)/MainDriver.Area_ann[i];///(1-MainDriver.f_cutting[i]); // ft/s
								
								vs_ff = propertyModule.Calc_vslip_cut(vel_a, d1, d2, i, (MainDriver.ang2p_cutting[i-1]+MainDriver.ang2p_cutting[i])/2);
								MainDriver.vslip_cutting[i] = vs_ff[0];
								MainDriver.vsl_cutting[i] = vs_ff[1];
								MainDriver.f_local[i] = vs_ff[5];
								MainDriver.v_average[i] = vs_ff[3];
								MainDriver.Area_local[i] = vs_ff[4];

								MainDriver.v_real_cutting[i] = MainDriver.v_average[i]-MainDriver.vslip_cutting[i];
								if(MainDriver.f_cutting[i]==0) MainDriver.v_real_cutting[i] = 0;

								if(i<pos_Top){ // cutting 경계 아래 부분
									Q_out[i] = (MainDriver.v_average[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT*MainDriver.Area_local[i]*MainDriver.f_local[i]/5.6145; // ft3 -> bbl
									Q_out_mud = Q_out[i]*(1-MainDriver.f_cutting[i])/MainDriver.f_cutting[i];

									if(MainDriver.V_cutting[i]<Q_out[i]){
										Q_out[i] = MainDriver.V_cutting[i];
										MainDriver.V_cutting[i] = 0;									
									}
									else MainDriver.V_cutting[i] = MainDriver.V_cutting[i] - Q_out[i];

									if((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT>(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]) && i>iBtm){
										MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i]*(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT);
										Q_out[i] = Q_out[i]+Q_in[i]*(1-(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT));
									}
									else{
										MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i];										
									}
									MainDriver.V_Cut_mix[i] = MainDriver.V_ann_cutting[i];
									MainDriver.V_Mud[i] = MainDriver.V_Cut_mix[i]-MainDriver.V_cutting[i];
									MainDriver.f_cutting[i] = MainDriver.V_cutting[i]/MainDriver.V_Cut_mix[i];
									MainDriver.oMudCutting[i] = MainDriver.f_cutting[i]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i])*MainDriver.oMud_save;
									Q_in[i+1] = Q_out[i];
								}
								else{
									//Q_out[i] = Q_out[i] * (1- (MainDriver.TopCutting - MainDriver.TMD_cutting[i]) / ((vel_a-MainDriver.vslip_cutting[i])*MainDriver.gDelT)); // 실제로 전체 애뉼러스 중 cutting이 있는 건 일부니까 그만큼의 포션을 곱해줘야 함
									//MainDriver.V_cutting[i] = MainDriver.V_cutting[i] - Q_out[i];
									//if(MainDriver.V_cutting[i]<0) MainDriver.V_cutting[i] = 0;


									if(MainDriver.TopCutting - (MainDriver.v_average[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT < MainDriver.TMD_cutting[i]){ // kick mixture가 다음 cell로 넘어감
										Q_out[i] = (MainDriver.TMD_cutting[i]-(MainDriver.TopCutting-(MainDriver.v_average[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT))*MainDriver.Area_local[i]*MainDriver.f_local[i]/5.6145; // ft3 -> bbl // 이게 분출되냐 안되냐는 아래에서 판단									
										if(MainDriver.V_cutting[i]<Q_out[i]){
											Q_out[i] = MainDriver.V_cutting[i];
											MainDriver.V_cutting[i] = 0;										
										}
										else{
											MainDriver.V_cutting[i] = MainDriver.V_cutting[i] - Q_out[i];
										}

										if((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT>(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]) && i>iBtm){
											MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i]*(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT);
											Q_out[i] = Q_out[i]+Q_in[i]*(1-(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT));
										}
										else{
											MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i];										
										}

										MainDriver.V_Cut_mix[i] = MainDriver.V_ann_cutting[i];
										MainDriver.V_Mud[i] = MainDriver.V_Cut_mix[i]-MainDriver.V_cutting[i];									
										MainDriver.f_cutting[i] = MainDriver.V_cutting[i]/MainDriver.V_Cut_mix[i];
										MainDriver.oMudCutting[i] = MainDriver.f_cutting[i]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i])*MainDriver.oMud_save;
										if(MainDriver.Area_local[i+1]==0) MainDriver.TopCutting = MainDriver.TMD_cutting[i] - 5.6145*Q_out[i]/MainDriver.f_local[i]/MainDriver.Area_local[i];
										else MainDriver.TopCutting = MainDriver.TMD_cutting[i] - 5.6145*Q_out[i]/MainDriver.f_local[i]/MainDriver.Area_local[i];
										if(MainDriver.TopCutting<0){
											MainDriver.TopCutting=0;
										}
										else{
											MainDriver.V_cutting[i+1] = Q_out[i];
											MainDriver.f_cutting[i+1] = MainDriver.f_cutting[i];
											MainDriver.V_Cut_mix[i+1] = MainDriver.V_cutting[i+1] / MainDriver.f_cutting[i+1];
											MainDriver.V_Mud[i+1] = MainDriver.V_Cut_mix[i+1] * (1-MainDriver.f_cutting[i+1]);
											MainDriver.oMudCutting[i+1] = MainDriver.f_cutting[i+1]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i+1])*MainDriver.oMud_save;
										}									
									}
									else{ // kick mixture가 아직 상승중
										Q_out[i]=0;
										MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i];
										MainDriver.TopCutting = MainDriver.TopCutting - (MainDriver.v_average[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT;
										MainDriver.V_Cut_mix[i] = (MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting)*MainDriver.Area_ann[i]/5.6145;
										MainDriver.V_Mud[i] = MainDriver.V_Cut_mix[i]-MainDriver.V_cutting[i];	
										if(MainDriver.V_Cut_mix[i]==0) MainDriver.f_cutting[i] = 0;
										else MainDriver.f_cutting[i] = MainDriver.V_cutting[i]/MainDriver.V_Cut_mix[i];
										MainDriver.oMudCutting[i] = MainDriver.f_cutting[i]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i])*MainDriver.oMud_save;
									}
									Q_in[i+1] = Q_out[i];									
								}
							}
						}

						else{ // 상부도달
							pos_Top = MainDriver.NwcE_cutting-1; // cell의 상부
							for(int i=iBtm; i<=pos_Top; i++){
								//vslip 계산
								d1 = MainDriver.Di2p_cut[i]; //inch
								d2 = MainDriver.Do2p_cut[i]; //inch

								if(Math.abs(1-MainDriver.f_cutting[i])<0.0001) vel_a = 0;
								else vel_a = (Qcirc_sec*5.6145)/MainDriver.Area_ann[i];///(1-MainDriver.f_cutting[i]); // ft/s
								
								vs_ff = propertyModule.Calc_vslip_cut(vel_a, d1, d2, i, (MainDriver.ang2p_cutting[i-1]+MainDriver.ang2p_cutting[i])/2);
								MainDriver.vslip_cutting[i] = vs_ff[0];
								MainDriver.vsl_cutting[i] = vs_ff[1];
								MainDriver.f_local[i] = vs_ff[5];
								MainDriver.v_average[i] = vs_ff[3];
								MainDriver.Area_local[i] = vs_ff[4];
								MainDriver.v_real_cutting[i] = MainDriver.v_average[i]-MainDriver.vslip_cutting[i];
								if(MainDriver.f_cutting[i]==0) MainDriver.v_real_cutting[i] = 0;
								MainDriver.vfall_cutting[i] = vs_ff[2];

								if(i==iBtm){
									h_cutting = Q_in[i]/MainDriver.f_cutting_ini*5.6145/MainDriver.Area_ann[i];
									if(h_cutting<MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]){
										Q_out[i] = (MainDriver.v_average[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT*MainDriver.Area_local[i]*MainDriver.f_local[i]/5.6145; // ft3 -> bbl
									}
									else{ // 주입 cutting이 맨 아래 block을 넘어가는 경우
										Q_out[i] = (MainDriver.v_average[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT*MainDriver.Area_local[i]*MainDriver.f_local[i]/5.6145; // ft3 -> bbl
										Q_out[i] = Q_out[i] + Q_in[i] * (1-(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/h_cutting);
										Q_in[i] = Q_in[i] * (MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/h_cutting;
									}								
									if(MainDriver.V_cutting[i]<Q_out[i]){
										Q_out[i] = MainDriver.V_cutting[i] + Q_in[i] * (h_cutting/ (MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])-1);
										MainDriver.V_cutting[i] = 0;
									}
									else{
										MainDriver.V_cutting[i] = MainDriver.V_cutting[i] - Q_out[i];
									}
									MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i];
									MainDriver.V_Cut_mix[i] = MainDriver.V_ann_cutting[i];
									MainDriver.V_Mud[i] = MainDriver.V_Cut_mix[i]-MainDriver.V_cutting[i];
									MainDriver.f_cutting[i] = MainDriver.V_cutting[i]/MainDriver.V_Cut_mix[i];
									MainDriver.oMudCutting[i] = MainDriver.f_cutting[i]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i])*MainDriver.oMud_save;
									Q_in[i+1] = Q_out[i];
								}	
								else{
									Q_out[i] = (MainDriver.v_average[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT*MainDriver.Area_local[i]*MainDriver.f_local[i]/5.6145; // ft3 -> bbl
									if(MainDriver.V_cutting[i]<Q_out[i]){
										Q_out[i] = MainDriver.V_cutting[i];
										MainDriver.V_cutting[i] = 0;										
									}
									else{
										MainDriver.V_cutting[i] = MainDriver.V_cutting[i] - Q_out[i];
									}

									if((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT>(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]) && i>iBtm){
										MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i]*(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT);
										Q_out[i] = Q_out[i]+Q_in[i]*(1-(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i])/((MainDriver.v_average[i-1]-MainDriver.vslip_cutting[i-1])*MainDriver.gDelT));
									}
									else{
										MainDriver.V_cutting[i] = MainDriver.V_cutting[i] + Q_in[i];										
									}
									MainDriver.V_Cut_mix[i] = MainDriver.V_ann_cutting[i];
									MainDriver.V_Mud[i] = MainDriver.V_Cut_mix[i]-MainDriver.V_cutting[i];
									MainDriver.f_cutting[i] = MainDriver.V_cutting[i]/MainDriver.V_Cut_mix[i];
									MainDriver.oMudCutting[i] = MainDriver.f_cutting[i]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[i])*MainDriver.oMud_save;
									Q_in[i+1] = Q_out[i];
								}
							}
						}
						
						//cutting 내부 위치 update
						for(int i=iBtm; i<=pos_Top; i++){
							for(int j=0; j<MainDriver.n_area_intv; j++){
								if(i==iBtm) MainDriver.V_cutting_area_intv[i][j] = MainDriver.V_cutting_area_intv[i][j]+Q_in[i]*MainDriver.f_cutting_area_intv_ini[i][j]-Q_out[i]*MainDriver.f_cutting_area_intv[i][j];
								else MainDriver.V_cutting_area_intv[i][j] = MainDriver.V_cutting_area_intv[i][j]+Q_in[i]*MainDriver.f_cutting_area_intv[i-1][j]-Q_out[i]*MainDriver.f_cutting_area_intv[i][j];								
							}
						}

						// 압력 계산
						MainDriver.V_cutting_tot=0;
						for(int i=MainDriver.NwcE-1; i>=iBtm; i--){
							if(i>pos_Top || MainDriver.V_cutting[i]==0){
								if(MainDriver.dis_cut_from_top[i]==0) MainDriver.dis_cut_from_top[i] = 0; // ft
							}
							else if(i<=pos_Top){
								if(i==MainDriver.NwcS) MainDriver.dis_cut_from_top[i] = MainDriver.dis_cut_from_top[i]*(1-Q_in[i]/MainDriver.V_cutting[i])+0*Q_in[i]/MainDriver.V_cutting[i]; // ft
								else MainDriver.dis_cut_from_top[i] = MainDriver.dis_cut_from_top[i]*(1-Q_in[i]/MainDriver.V_cutting[i])+MainDriver.dis_cut_from_top[i-1]*Q_in[i]/MainDriver.V_cutting[i]; // ft
								if(MainDriver.dis_cut_from_top[i]<0) MainDriver.dis_cut_from_top[i]=0;
							}

							if(MainDriver.TMD_cutting[i-1]<=MainDriver.TopCutting){ // mixture 상부에 있는 cell
								d1 = MainDriver.Di2p_cut[i]; //inch
								d2 = MainDriver.Do2p_cut[i]; //inch

								if(Math.abs(1-MainDriver.f_cutting[i])<0.0001) vel_a = 0;
								else vel_a = (Qcirc_sec*5.6145)/MainDriver.Area_ann[i]/(1-MainDriver.f_cutting[i]); // ft/s

								MainDriver.v_real_cutting[i] = 0;
								utilityModule.getDP_cutting(Qcirc/(1-MainDriver.f_cutting[i]), MainDriver.TMD_cutting[i-1], MainDriver.oMud_save); // mixture 위에서의 friction loss										
								P_fric_ex[i] = MainDriver.DPtop;
								utilityModule.getDP_cutting(Qcirc/(1-MainDriver.f_cutting[i]), MainDriver.TMD_cutting[i], MainDriver.oMud_save); // mixture 위에서의 friction loss
								P_fric_ex[i] = P_fric_ex[i]-MainDriver.DPtop;
								P_hydro_ex[i] = 0.052*MainDriver.oMud_save*(MainDriver.TVD_cutting[i-1]-MainDriver.TVD_cutting[i]);
								MainDriver.P_ann_ex[i-1] = MainDriver.P_ann_ex[i] + P_fric_ex[i] + P_hydro_ex[ i];
							}

							else if(MainDriver.TMD_cutting[i]<=MainDriver.TopCutting){ // mixture 해당, 순수 mud 상부와 함께 존재
								TopVD = utilityModule.getVD(MainDriver.TopCutting);
								d1 = MainDriver.Di2p_cut[i]; //inch
								d2 = MainDriver.Do2p_cut[i]; //inch

								if(Math.abs(1-MainDriver.f_cutting[i])<0.0001) vel_a = 0;
								else vel_a = (Qcirc_sec*5.6145)/MainDriver.Area_ann[i]/(1-MainDriver.f_cutting[i]); // ft/s

								// 순수 MUD만 존재하는 구역
								ed = MainDriver.Ruf / (d2 - d1);							    
								CON = 1;
								if (d1 > 0.002) CON = 0.816496581;
								ren = propertyModule.pRen(d2, d1, vel_a, MainDriver.oMud_save);
								ff_mud=propertyModule.getf(ed, ren);
								P_fric_ex[i] = ff_mud * MainDriver.oMud_save * vel_a*vel_a / (25.81 * CON * (d2 - d1))*(MainDriver.TopCutting-MainDriver.TMD_cutting[i]);

								// 혼재하는 구역
								//P_fric_ex[i] = P_fric_ex[i]+ff_mud * MainDriver.oMud_save * vel_a*vel_a / (25.81 * CON * (d2 - d1))*(MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting);								
								ren = propertyModule.pRen(d2, d1, vel_a, MainDriver.oMud_save);
								ff_mud=propertyModule.getf(ed, ren);
								P_fric_ex[i] = P_fric_ex[i]+ff_mud * MainDriver.oMud_save* vel_a*vel_a / (25.81 * CON * (d2 - d1))*(MainDriver.TMD_cutting[i-1]-MainDriver.TopCutting);
								P_fric_ex[i] = P_fric_ex[i]+((MainDriver.dens_cutting*42/5.6145)*Math.pow(MainDriver.vslip_cutting[i],2)*MainDriver.A_cutting/2*MainDriver.vsl_cutting[i])*MainDriver.V_cutting[i]/MainDriver.Vol_particle_cutting/(Math.PI/4*(d2*d2-d1*d1))/32.17;

								P_hydro_ex[i] = 0.052*MainDriver.oMudCutting[i]*(MainDriver.TVD_cutting[i-1]-MainDriver.TVD_cutting[i]);
								MainDriver.P_ann_ex[i-1] = MainDriver.P_ann_ex[i] + P_fric_ex[i] + P_hydro_ex[i];
							}
							else{ //mixture 아래(cutting zone)										
								d1 = MainDriver.Di2p_cut[i]; //inch
								d2 = MainDriver.Do2p_cut[i]; //inch
								if(Math.abs(1-MainDriver.f_cutting[i])<0.0001) vel_a = 0;
								else vel_a = (Qcirc_sec*5.6145)/MainDriver.Area_ann[i]/(1-MainDriver.f_cutting[i]); // ft/s
								if(MainDriver.f_cutting[i]==0) MainDriver.v_real_cutting[i] = 0;

								ed = MainDriver.Ruf / (d2 - d1);							    
								CON = 1;
								if (d1 > 0.002) CON = 0.816496581;
								ren = propertyModule.pRen(d2, d1, vel_a, MainDriver.oMud_save);

								ff_mud=propertyModule.getf(ed, ren);
								//P_fric_ex[i] = ff_mud * MainDriver.oMud_save * vel_a*vel_a / (25.81 * CON * (d2 - d1))*(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]);								
								ren = propertyModule.pRen(d2, d1, vel_a, MainDriver.oMud_save);
								ff_mud=propertyModule.getf(ed, ren);
								P_fric_ex[i] = ff_mud * MainDriver.oMud_save * vel_a*vel_a / (25.81 * CON * (d2 - d1))*(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]);
								P_fric_ex[i] = P_fric_ex[i]+((MainDriver.dens_cutting*42/5.6145)*Math.pow(MainDriver.vslip_cutting[i],2)*MainDriver.A_cutting/2*MainDriver.vsl_cutting[i])*MainDriver.V_cutting[i]/MainDriver.Vol_particle_cutting/(Math.PI/4*(d2*d2-d1*d1))/32.17;

								P_hydro_ex[i] = 0.052*MainDriver.oMudCutting[i]*(MainDriver.TVD_cutting[i-1]-MainDriver.TVD_cutting[i]);
								MainDriver.P_ann_ex[i-1] = MainDriver.P_ann_ex[i] + P_fric_ex[i] + P_hydro_ex[i];
							}
							ren = propertyModule.pRen(d2, d1, vel_a, MainDriver.oMud_save);
							MainDriver.V_cutting_tot = MainDriver.V_cutting_tot+MainDriver.V_cutting[i];
						}

						double v_a_t=0, v_c_t=0;

						for(int i=MainDriver.NwcE-1; i>=iBtm; i--){
							v_a_t = v_a_t + MainDriver.V_ann_cutting[i];
							v_c_t = v_c_t + MainDriver.V_Cut_mix[i]*MainDriver.f_cutting[i];
						}
						v_c_t = v_c_t/v_a_t;
						
						wellDrill();
						
						if(MainDriver.gTcum>=intv_record*MainDriver.i_ex){
							//MainDriver.P_ann[(int)(MainDriver.gTcum/intv_record)+1][MainDriver.NwcE-1]=14.7;
							//MainDriver.D_result[(int)(MainDriver.gTcum/intv_record)+1][MainDriver.NwcE-1]=MainDriver.TMD_cutting[MainDriver.NwcE-1];
							MainDriver.v_ac[(int)(MainDriver.gTcum/intv_record)+1] = MainDriver.V_cutting_tot;
							MainDriver.f_tot[(int)(MainDriver.gTcum/intv_record)+1]=v_c_t;
							
							if(mdBit < MainDriver.TMD_cutting[MainDriver.NwcS_cutting]){
								temp_NwcS = MainDriver.NwcS_cutting+1;
							}
							else{
								temp_NwcS = MainDriver.NwcS_cutting;
							}

							for(int i=MainDriver.NwcE_cutting-2; i>=temp_NwcS-1; i--){
								//MainDriver.P_ann[(int)(MainDriver.gTcum/intv_record)+1][i]=MainDriver.P_ann_ex[i];
								//MainDriver.D_result[(int)(MainDriver.gTcum/intv_record)+1][i]=MainDriver.TMD_cutting[i];
							}
							porefracP = propertyModule.calcPoreFrac(vdBit);
							poreP = porefracP[0];
							fracP = porefracP[1];
							MainDriver.time_ex[MainDriver.i_ex]=MainDriver.gTcum;
							MainDriver.bhp_ex[MainDriver.i_ex]=Pbeff;
							MainDriver.fx_ex[MainDriver.i_ex]=MainDriver.fc_tot;
							MainDriver.den_ex[MainDriver.i_ex]=MainDriver.TopCutting;//=MainDriver.oMud;
							MainDriver.ROP_ex[MainDriver.i_ex]=ROP;
							MainDriver.depth_ex[MainDriver.i_ex]=mdBit;
							MainDriver.pore_ex[MainDriver.i_ex]=poreP;
							MainDriver.frac_ex[MainDriver.i_ex]=fracP;
							MainDriver.i_ex = MainDriver.i_ex + 1;
						} // mode 2: fixed gridsp
					}
					
					else{
						Qgas = 0;
						Pkick = psia;
						vdBit =  (MainDriver.Vdepth + (xDrill - MainDriver.mdBitOff) * Math.cos(AeobRad));
						hdBit =  (MainDriver.Hdisp + (xDrill - MainDriver.mdBitOff) * Math.sin(AeobRad));
						wellDrill();
					}
					
					
					if(MainDriver.iProblem[3]==1){
						//MainDriver.Pform = Pbeff + 300;
						//MainDriver.overP = MainDriver.Pform - (0.052 * MainDriver.oMud * MainDriver.Vdepth + 14.7);
						//MainDriver.KICKintens = MainDriver.overP/(0.052*MainDriver.Vdepth);
						//MainDriver.Pb = MainDriver.Pform;
						//MainDriver.Kmud = MainDriver.oMud + MainDriver.KICKintens;
						//MainDriver.overP = MainDriver.Pform - (0.052 * MainDriver.oMud_save * MainDriver.Vdepth + 14.7);
						//MainDriver.SIDPP = MainDriver.overP + psia;
						//MainDriver.gMudKill = 0.052 * MainDriver.Kmud;
						//MainDriver.gMudCirc = MainDriver.gMudKill;
						//if(MainDriver.Method==1) MainDriver.gMudCirc = MainDriver.gMudOld;
						//MainDriver.Cmud = MainDriver.gMudCirc / 0.052;
					}
				}

				else{ // kick 발생
					if(iPenet==-1){
						MainDriver.v_ac[(int)(MainDriver.gTcum/intv_record)+1] = MainDriver.V_cutting_tot;
						
						
						porefracP = propertyModule.calcPoreFrac(vdBit);
						poreP = porefracP[0];
						fracP = porefracP[1];
						MainDriver.time_ex[MainDriver.i_ex]=MainDriver.gTcum;
						MainDriver.bhp_ex[MainDriver.i_ex]=Pbeff;
						MainDriver.fx_ex[MainDriver.i_ex]=MainDriver.fc_tot;
						MainDriver.den_ex[MainDriver.i_ex]=MainDriver.TopCutting;//=MainDriver.oMud;
						MainDriver.ROP_ex[MainDriver.i_ex]=ROP;
						MainDriver.depth_ex[MainDriver.i_ex]=mdBit;
						MainDriver.pore_ex[MainDriver.i_ex]=poreP;
						MainDriver.frac_ex[MainDriver.i_ex]=fracP;
						MainDriver.i_ex = MainDriver.i_ex + 1;
					}
					P2form = MainDriver.Pform;					
					
					iPenet = iPenet + 1;
					if(iPenet==0) Tpenet[iPenet]=0;
					else Tpenet[iPenet] = Tpenet[iPenet-1]+MainDriver.gDelT; // 20140219 ajw total time in second, each interval
					//Tpenet[iPenet] = MainDriver.gTcum - MainDriver.timeToTD; // total time in second, each interval
					//
					//---------------------- The following needs to be changed for correct simulation
					//				                       March 13, 1999
					//  Hpenet[iPenet) =ROP * (MainDriver.gTcum - Tpenet[iPenet - 1)) / 3600     //This was wrong !
					if(iPenet==0) Hpenet[iPenet] =ROP * Tpenet[iPenet]/ 3600;
					else Hpenet[iPenet] =ROP * (Tpenet[iPenet] - Tpenet[iPenet - 1]) / 3600;
					if (MainDriver.iHresv == 1) Hpenet[0] = MainDriver.x2Hold;
					vdBit = MainDriver.Vdepth + (xDrill - MainDriver.mdBitOff) * Math.cos(AeobRad);
					hdBit = MainDriver.Hdisp + (xDrill - MainDriver.mdBitOff) * Math.sin(AeobRad);
					if(MainDriver.iProblem[3]==1){					
						MainDriver.vdbit_now = vdBit;
						d1 = MainDriver.Di2p[MainDriver.NwcS]/12; //ft
						d2 = MainDriver.Do2p[MainDriver.NwcS]/12; //ft
						Q_in[MainDriver.NwcS] = ROP*MainDriver.gDelT/3600*(Math.PI/4*Math.pow(d2,2)) / 5.6145; // ft/hr * ft^2 * hr / 5.6145 = bbl
						MainDriver.V_cutting_tot=0;
						for(int i=MainDriver.NwcS; i<MainDriver.NwcE; i++){
							d1 = MainDriver.Di2p[i]; //inch
							d2 = MainDriver.Do2p[i]; //inch
							if(Math.abs(1-MainDriver.f_cutting[i])<0.0001) vel_a = 0;
							else vel_a = (Qcirc_sec*5.6145)/MainDriver.Area_ann[i]/(1-MainDriver.f_cutting[i]); // ft/s
							MainDriver.vslip_cutting[i] = propertyModule.Calc_vslip_cut(vel_a, d1, d2, i, (MainDriver.ang2p[i-1]+MainDriver.ang2p[i])/2)[0];
							MainDriver.vslip_cutting[i] = MainDriver.vslip_cutting[i]*Math.cos(MainDriver.ang2p[i]*RAD_CONVERT);
							MainDriver.v_real_cutting[i] = vel_a-MainDriver.vslip_cutting[i];
							if(MainDriver.f_cutting[i]==0) MainDriver.v_real_cutting[i] = 0;
							MainDriver.vfall_cutting[i] = vs_ff[2];

							Q_out[i] = ((Qcirc_sec*5.6145)/MainDriver.Area_ann[i]-MainDriver.vslip_cutting[i])*MainDriver.gDelT; // ft
							Q_out[i] = Q_out[i]*MainDriver.Area_ann[i]/5.6145; // ft3 -> bbl
							MainDriver.V_cutting[i] = MainDriver.V_cutting[i]-Q_out[i];
							if(MainDriver.V_cutting[i]<0){
								MainDriver.V_cutting[i]=0;
							}
							MainDriver.V_cutting[i] = MainDriver.V_cutting[i]+Q_in[i];
							Q_in[i+1] = Q_out[i];
							MainDriver.f_cutting[i] = MainDriver.V_cutting[i] / MainDriver.V_ann_cutting[i];
							MainDriver.V_cutting_tot = MainDriver.V_cutting_tot + MainDriver.V_cutting[i];
						}
						MainDriver.fc_tot = MainDriver.V_cutting_tot / MainDriver.VOLout;
						MainDriver.oMud = MainDriver.fc_tot*MainDriver.dens_cutting + (1-MainDriver.fc_tot)*MainDriver.oMud_save;
						porefracP = propertyModule.calcPoreFrac(vdBit);
						poreP = porefracP[0];
						fracP = porefracP[1];
					}
					dummy = wellKick();
				}
				//          5/10/99, 6/28/02
				// Consider mud compressibility in SI pressure buildup				
				ShowResult();					    
				if (iStable == 1) txtKillMudWt.setText((new DecimalFormat("##.0#")).format(MainDriver.Kmud));

				MainDriver.t_problem[1] = MainDriver.t_problem[1]+MainDriver.gDelT;
				if(MainDriver.t_problem[1]-MainDriver.t_problem[0]>=MainDriver.tstep_problem){
					checkProblem(); //140912 ajw
					MainDriver.t_problem[0]=MainDriver.t_problem[1];
				}

				// Problem Occurence

				// 1. Mechanical pipe stuck
				if(MainDriver.iProblem_occured[0]==1){					
					iROP = 0;
					txtROP.setText(((new DecimalFormat("##0.0#")).format(0)));
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					if (Sm1.m3.TimerRotOn==1){
						Sm1.m3.TimerRot.cancel();
						Sm1.m3.TimerRotOn=0;
					}
					MainDriver.RPM_now = 0;
					MainDriver.Torque_now = 0;
					RPMScroll.setValue((int)MainDriver.RPM_now);
				}

				// 2. Differential pipe stuck
				else if(MainDriver.iProblem_occured[1]==1){
					iROP = 0;
					txtROP.setText(((new DecimalFormat("##0.0#")).format(0)));
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					if (Sm1.m3.TimerRotOn==1){
						Sm1.m3.TimerRot.cancel();
						Sm1.m3.TimerRotOn=0;
					}

					MainDriver.RPM_now = 0;
					MainDriver.Torque_now = 0;

					RPMScroll.setValue((int)MainDriver.RPM_now);
				}
				
				else{					
					MainDriver.dP_pump_mech = 0;
					
					RPMScroll.setValue((int)MainDriver.RPM_now);
					if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true);
					if (iROP == 1 && Sm1.m3.TimerRotOn==0){
						Sm1.m3.TimerRot = new Timer();
						Sm1.m3.TimerRot.schedule(Sm1.m3.new TRTask(), 0, TimeDrlIntv);//WellPic.TimerRot.setEnabled = True	
						Sm1.m3.TimerRotOn=1;
					}
				}

				// 3. Mud loss
				if(MainDriver.iProblem_occured[2]==1){
					double h_bottom = mdBit;
					if(h_bottom>MainDriver.layerVDto[MainDriver.layer_mud_loss]) h_bottom = MainDriver.layerVDto[MainDriver.layer_mud_loss];
					double h_top = utilityModule.getMD(MainDriver.layerVDfrom[MainDriver.layer_mud_loss]);
					double h_target = h_bottom - h_top;
					double perm_target = MainDriver.layerPerm[MainDriver.layer_mud_loss];	
					MainDriver.MD_Loss_center = (h_bottom+h_top)/2;
					if(h_target>100) h_target=100;//ft
					MainDriver.MD_Loss_center = (h_bottom+h_top)/2;
					MainDriver.MD_Loss_length = h_target;
					MainDriver.Q_loss = 7.08*Math.pow(10, -3)*perm_target*h_target*(Pbeff-P2form)/MainDriver.visL/Math.log(MainDriver.R_reservoir/(MainDriver.DiaHole/12/2)); // bbl/day
					if(MainDriver.Q_loss<0) MainDriver.Q_loss=0;
					MainDriver.Q_loss = MainDriver.Q_loss*42/24/60; //gpm
					MainDriver.V_loss = MainDriver.V_loss + MainDriver.Q_loss/42/60*MainDriver.gDelT;//bbl
					//MainDriver.Q_loss = MainDriver.Q_loss*MainDriver.gDelT/(60*60*24);
				}
				else{
					MainDriver.Q_loss = 0;//GPM
					MainDriver.V_loss = 0;//bbl
				}

				// 4. Drilling cutting
				if(MainDriver.iProblem[3]==1){
					Torque = MainDriver.Torque_now;
					RPM = MainDriver.RPM_now;
					WOB = MainDriver.WOB_now;
					porefracP = propertyModule.calcPoreFrac(vdBit);
					poreP = porefracP[0];
					fracP = porefracP[1];
					P2form = poreP;
					if(MainDriver.gTcum%3600==0 && MainDriver.iHuschel>0){
						Pbeff = MainDriver.P_ann[(int)(MainDriver.gTcum/intv_record)][MainDriver.NwcS];
					}					
				}


				// Option. ROP VARIATiON
				if(MainDriver.i_ROPVERSION==2 && iROP==1){
					MainDriver.ROPen = propertyModule.calcROP(RPM, Pbeff,  WOB,  Qcirc, vdBit); // 1) ROP 갱신
				}

				// Problem occurence
			}
			TDTaskFinishedIndex = 0;	
		}
	}

	static double calcPoreP(double recentLoc){
		//.... separate pore and fracture pressures calculation sub
		//     This can be called from outside.
		//     Jan. 24, 2003

		double target_depth=0, depthTmp=0, delta_depth=0, bulk_density=0, p_overburden=0, p_seafloor=0, p_seawater=0, poisson=0, poreP=0;

		p_seafloor = 0.052 * MainDriver.swDensity * MainDriver.Dwater;

		MainDriver.PPdepth[0] = 0; 
		MainDriver.PoreP[0] = 0;
		MainDriver.FracP[0] = 0; //이것도 배열 인덱스 배당이 헤깔림...

		//   If iFG = 1 Or iFG = 2 Then    //Eaton//s method (1)
		//   ElseIf ifg = 2 Then    //John Barker//s method (2)
		target_depth = MainDriver.Vdepth;
		//
		delta_depth = (target_depth - MainDriver.Dwater) * 0.1;  //10 intervals
		depthTmp = 0.00000002;        //Jan. 19, 2003

		for(int i=0; i<12; i++){     //Assign pore & fracture pressures based on bulk density: John Barker AADE 97 paper
			bulk_density = 5.3 * Math.pow(depthTmp, 0.1356);
			p_overburden = p_seafloor + 0.052 * bulk_density * depthTmp;
			poreP = 0.8 * p_overburden;   //80 % of overburden pressure		 
			if(depthTmp==recentLoc) return poreP;
			p_seawater = 0.052 * MainDriver.swDensity * (depthTmp + MainDriver.Dwater);
			if(depthTmp<recentLoc && recentLoc<=depthTmp+delta_depth) depthTmp=recentLoc;
			else depthTmp = depthTmp + delta_depth;

		}
		if(depthTmp==recentLoc) return poreP;
		else return -1;
	}

	static void ContSI(){ // calculate the slip velocity
		//
		double avgAng=0, angle=0;
		double vs2=0, vs3=0;
		double hg3 = HgCal, sten = surfTen, xo = 0, gden = gasDen;
		double hktot=0;// ???? what is this?
		nHorizon = 0; vSlip = 0; 

		if (MainDriver.iWell==4 && MainDriver.ang2EOB > 87.5) nHorizon = 1; // GoTo 555
		//c............... effective variables are used to calculate kick height
		else{
			avgAng = 0.5 * (MainDriver.ang2p[MainDriver.iHWDP] + MainDriver.ang2p[MainDriver.iHWDP + 1]);

			//----------------------- Continuous Shutin --------------------------
			// Kick moves upward based on bubble slip velocity in a static colummn.
			// Slip velocity is based on Hasan//s correlation.
			// This calculation is also based on average of mid point.
			//

			vs2=utilityModule.slipShutin(MainDriver.DiaHole, MainDriver.doDP, MainDriver.oMud, gden, sten, avgAng, hg3, xo)[0];
			angle = MainDriver.ang2p[MainDriver.iHWDP - 1];
			if (hktot < (MainDriver.LengthDC + MainDriver.LengthHWDP))  vs3=utilityModule.slipShutin(MainDriver.DiaHole, MainDriver.doHWDP, MainDriver.oMud, gden, sten, angle, hg3, xo)[0];
			else vs3 = utilityModule.slipShutin(MainDriver.DiaHole, MainDriver.doDP, MainDriver.oMud, gden, sten, angle, hg3, xo)[0];//, xo, vs3, Vgf)
			//........................................ set the maximum velocity 1.0 = 3600 ft/hr
			vSlip = 0.5 * (vs2 + vs3); 
			vSlip = Math.min(vSlip, 1);
		}
		//555 dummy = 0
		//... check the effective kick mixture greater than annular hold section
		if (nHorizon == 1 && QtotVol > v2Hold){
			//....................... we should consider gas slip at the top
			//		                        no gas slip at the bottom in horizontal section
			nHorizon = 2;
			angle = 0.5 * (MainDriver.ang2p[nSlip] + MainDriver.ang2p[nSlip + 1]);
			vs3=utilityModule.slipShutin(MainDriver.DiaHole, MainDriver.doDP, MainDriver.oMud, gden, sten, angle, hg3, xo)[1];
			vSlip = Math.min(vs3, 1);
		}
		//
	}

	static void wellDrill(){
		//
		//-------------------- While Drilling -------------------------------------
		//.................... calculate flowing BHP and casing seat pressure before TD
		//             Now, make the general solution from vertical to horizontal !		
		//
		double mdBit=0;
		double[] getDPinsideEX = new double[2];
		double[] pb_OBM = new double[3];
		double iVDbit=0;	
		double pp1=0, pp2=0, pperr=1, iter=0;
		double Pbeff1=0;
		double[] getDP2Ex = new double[3];
		double[] OBMdensityCalc_Drilling = new double[2];
		double[] porefracP = new double[2];
		
		Qt = Qcirc + Qgas;		
		
		if(MainDriver.iProblem[3]==1){ //cutting			
			Pcasing = MainDriver.P_ann_ex[MainDriver.iCsg_cutting];			
			porefracP = propertyModule.calcPoreFrac(vdBit);
			Pbeff = MainDriver.P_ann_ex[iBtm-1];
			P2form = porefracP[0];
			getDPinsideEX = utilityModule.getDPinside_cutting(Pbeff, Qcirc, xNull);
		}
		else if(MainDriver.iProblem_occured[2]==0){ // no loss, no cutting
			utilityModule.getDP(Qt, MainDriver.DepthCasing, MainDriver.oMud);   //calculate casing seat p. depending flow rate
			Pcasing = MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + MainDriver.DPtop + psia;
			
			mdBit = MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff + xDrill;
			utilityModule.getDP(Qt, mdBit, MainDriver.oMud);
			Pbeff = MainDriver.gMudOld * vdBit + MainDriver.DPtop + psia;
			P2form = MainDriver.gMudOld * vdBit + psia;   //=Pbeff  //just assume hydrostatic p.
			getDPinsideEX = utilityModule.getDPinside(Pbeff, Qcirc, xNull);
		}
		else if(MainDriver.iProblem_occured[2]==1){ // mud loss, no cutting
			double VD_loss_center = utilityModule.getVD(MainDriver.MD_Loss_center);
			double P_loss_center = 0;
			double Q_real = Qt - MainDriver.Q_loss;
			double temp = 0;
			if(Q_real<0) Q_real = 0;

			utilityModule.getDP(Q_real, MainDriver.DepthCasing, MainDriver.oMud);   //calculate casing seat p. depending flow rate
			Pcasing = MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + MainDriver.DPtop + psia;

			utilityModule.getDP(Q_real, MainDriver.MD_Loss_center, MainDriver.oMud);   //calculate casing seat p. depending flow rate
			P_loss_center = MainDriver.gMudOld * VD_loss_center + MainDriver.DPtop + psia;

			mdBit = MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.mdBitOff + xDrill;
			Pbeff = MainDriver.gMudOld * vdBit + MainDriver.DPtop + psia;
			utilityModule.getDP(Qt, mdBit, MainDriver.oMud);
			Pbeff =Pbeff + MainDriver.DPtop;
			utilityModule.getDP(Qt, MainDriver.MD_Loss_center, MainDriver.oMud);
			Pbeff =Pbeff - MainDriver.DPtop;

			P2form = MainDriver.gMudOld * vdBit + psia;   //=Pbeff  //just assume hydrostatic p.
			getDPinsideEX = utilityModule.getDPinside(Pbeff, Qcirc, xNull);
			//utilityModule.getDP(Qt, mdBit, MainDriver.oMud);
			//temp = MainDriver.gMudOld * vdBit + MainDriver.DPtop + psia;
		}		
		standpipeP = getDPinsideEX[0];
		pumpP = getDPinsideEX[1];
		Pchoke = 14.7;
	}

	void warning(){
		String s = "Gas influx rate is so small that variables are out of "+"\n"+"array dimensions defined in the program. "+"\n"+ " Please check formation properties then try again !";
		JOptionPane.showMessageDialog(null, s, "Warning", JOptionPane.INFORMATION_MESSAGE);
		this.dispose();
	}

	int wellKick(){
		//.... further modified after 2003/7/18
		//....for considering OBM usage
		//Modified by ty
		double sPVgainOld =0;     //for p. buildup calculation
		//------------------------ Kick is taking place at the bottom --------
		double qgt = 0, tsecGas=0, hft=0, QgMscfd=0;
		double qgtbbl=0, qgtmp=0, qlpump=0, xmdloc=0, qgtbbl_gas=0, qgrgpm=0;
		double pxOld=0, pxMid=0, delKick=0, compVol=0, DPincrease=0, hvdeff=0, avgden=0, zb=0, tx2=0;
		double[] GasPropEX = new double[3];
		double oilDensity_1phase = 0;
		double oilkickDensity_mixture  = 0;
		double mwTot = 0;
		MainDriver.R=10.73;

		if (MainDriver.iBOP == 0){
			Qcirc = 0; Qgas = 0; Qt = 0;
		}

		for(int i = 0; i<iPenet+1; i++){
			if(i==0) tsecGas = Tpenet[iPenet] - 0.5 * (Tpenet[i] + 0); // exposed time in sec
			else tsecGas = Tpenet[iPenet] - 0.5 * (Tpenet[i] + Tpenet[i - 1]);
			hft = Hpenet[i];                             // each interval	
			QgMscfd = utilityModule.GASinflux(Pbeff, tsecGas, hft);    // flow rate in Mscf/Day
			//QgMscfd = 2000
			qgrgpm = 29.166667 * QgMscfd * MainDriver.fvfGas;		// convert to reservoir volume in gal/min

			qgt = qgt + qgrgpm;   //total influx in gpm
			//qgt = qgrgpm
			//qgt = 168   //gpm for Santos case
		}

		if(iPenet==0) qgtbbl = qgt * Tpenet[iPenet] / (42 * 60);
		else qgtbbl = qgt * (Tpenet[iPenet] - Tpenet[iPenet - 1]) / (42 * 60);
		QgDay = qgt / (29.166667 * MainDriver.fvfGas);     // convert to SC Mscf/D
		//----------------------------------------------------- for two-phase calculation
		if(iPenet==0) qgtmp = 42 * 60 * qgtbbl / (Tpenet[iPenet] + 0.000001);
		else qgtmp = 42 * 60 * qgtbbl / (Tpenet[iPenet] - Tpenet[iPenet - 1] + 0.000001);
		volGtmp[iPenet] = qgtbbl;               //for 2-phase calculation

		//Added by TY
		//for assigning liq volumes after pump-off
		if(iPenet==0) deltaT[iPenet] = Tpenet[iPenet];
		else  deltaT[iPenet] = Tpenet[iPenet] - Tpenet[iPenet - 1];
		if(iPenet==0) qlpump = Qcirc * Tpenet[iPenet] / (42 * 60);   //in bbls
		else qlpump = Qcirc * (Tpenet[iPenet] - Tpenet[iPenet - 1]) / (42 * 60);

		if (iPenet <= 1) volLtmp[iPenet] = qlpump + 0.5 * v2Hold;
		else volLtmp[iPenet] = qlpump;

		//Modified by ty
		tbottom = utilityModule.temperature(MainDriver.Vdepth);
		GasPropEX = propertyModule.GasProp(Pbeff, tbottom);//, gasVis, gasDen, zb)
		gasVis = GasPropEX[0];
		gasDen = GasPropEX[1];
		zb = GasPropEX[2];
		MainDriver.fvfGas = 0.00504 * zb * tbottom / Pbeff;

		//
		//------------------------------------------------------------------------------------ OBM case
		//assume methane gas kick
		//methane density: 0.0417 lbm/ft^3 @ SC, molecular weight: 16.04 lbm/lbm-mole
		if(MainDriver.imud == 1){
			if(volLtmp[iPenet] == 0){ //gas influx after pump-off
				MainDriver.gasmole[iPenet] = volGtmp[iPenet] / MainDriver.fvfGas * 0.0417 / 16.04;
				MainDriver.freegasmole[iPenet] = MainDriver.gasmole[iPenet];
				qgtbbl_gas = (MainDriver.freegasmole[iPenet] * MainDriver.R * zb * tbottom / Pbeff) / 5.615; //pit volume gain only because of free gas	, reservoir condition	
				//GoTo 1848
			}
			else{		    		
				gortmp[iPenet] = volGtmp[iPenet] / MainDriver.fvfGas / (volLtmp[iPenet]);//temporary gas-oil based mud ratio
				MainDriver.gasmole[iPenet] = volGtmp[iPenet] / MainDriver.fvfGas * 0.0417 / 16.04;

				//Calculate gas solubility
				tx2 = tbottom - 460; //tbottom: rankine / tx2: fahrenheit
				MainDriver.Rs = utilityModule.calcRs2(Pbeff, tx2); //Solubility calc by PVTi
				MainDriver.Rs = MainDriver.Rs * MainDriver.foil; //foil: fraction of baseoil in OBM
				MainDriver.freegasmole[iPenet] = (volLtmp[iPenet]) * (gortmp[iPenet] - MainDriver.Rs) * 0.0417 / 16.04;
				//
				//if Rs > gortemp @ given condition
				if(MainDriver.freegasmole[iPenet] < 0) MainDriver.freegasmole[iPenet] = 0;
				//
				MainDriver.solgasmole[iPenet] = MainDriver.gasmole[iPenet] - MainDriver.freegasmole[iPenet];
				//------------------------------------------------------------------------------- Applying PREOS
				MainDriver.OBMmole[iPenet] = (volLtmp[iPenet] * MainDriver.foil) * MainDriver.OBMdensity * 42 / MainDriver.OBMwt; //calculate #-mole of OBM
				MainDriver.OBMFrac[iPenet] = (MainDriver.OBMmole[iPenet] / ((MainDriver.solgasmole[iPenet]) + MainDriver.OBMmole[iPenet])) / 100;
				MainDriver.GasFrac[iPenet] = (0.01 - MainDriver.OBMFrac[iPenet]) * 100;
				//
				MainDriver.OBMFr = MainDriver.OBMFrac[iPenet];
				MainDriver.GasFr = MainDriver.GasFrac[iPenet];
				//
				MainDriver.mole_solgas = MainDriver.solgasmole[iPenet];
				MainDriver.mole_OBM = MainDriver.OBMmole[iPenet];
				//
				if(MainDriver.mud_calc==0){ //preos
					MainDriver.V_cont = utilityModule.PREOS(Pbeff, tbottom, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);//, V_cont)
					MainDriver.V_cont_ref = utilityModule.PREOS(Pbeff, tbottom, 0, 0.01, 0, MainDriver.mole_OBM);//, V_cont_ref)
					//
					MainDriver.Vsol[iPenet] = (MainDriver.V_cont - MainDriver.V_cont_ref); //swelling of OBM
					MainDriver.Vsol[iPenet] = MainDriver.Vsol[iPenet] * volLtmp[iPenet] * MainDriver.foil / MainDriver.V_cont;
				}
				else{
					//utilityModule.OBM_Fraction(0, 0.01); //solution gas mole, obm gas mole
					//oilDensity_1phase = utilityModule.calcSKdensity(tbottom, Pbeff);
					//MainDriver.V_cont_ref = 
					utilityModule.OBM_Fraction(MainDriver.GasFr, MainDriver.OBMFr); //solution gas mole, obm gas mole
					oilkickDensity_mixture  = utilityModule.calcSKdensity(tbottom, Pbeff-14.7); //ppg
					mwTot = MainDriver.mole_solgas * 16.04 + MainDriver.OBMwt * MainDriver.mole_OBM; //lb
					//
					//MainDriver.Vsol[iPenet] = 
					MainDriver.Vsol[iPenet] = mwTot / oilkickDensity_mixture / 42 - volLtmp[iPenet] * MainDriver.foil;
				}
				//
				//MainDriver.Den_tmp = (MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1] * volLtmp[iPenet] * 42 + MainDriver.solgasmole[iPenet] * 16.04) / (volLtmp[iPenet] + MainDriver.Vsol[iPenet]) / 42;

				MainDriver.Den_tmp = (MainDriver.oMud * volLtmp[iPenet] * 42 + MainDriver.solgasmole[iPenet] * 16.04) / (volLtmp[iPenet] + MainDriver.Vsol[iPenet]) / 42;
				//MainDriver.Dendiff[iPenet] = (MainDriver.Den_tmp - MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1]); //Density difference 
				MainDriver.Dendiff[iPenet] = (MainDriver.Den_tmp - MainDriver.oMud); //Density difference 
				//
				if(volLtmp[iPenet] == 0) MainDriver.Vsol[iPenet] = 0;
				qgtbbl = (MainDriver.freegasmole[iPenet] * MainDriver.R * zb * tbottom / Pbeff) / 5.615 + MainDriver.Vsol[iPenet]; //pit gain: swelling of OBM + freegas expansion
				//
				volGtmp[iPenet] = MainDriver.freegasmole[iPenet] * MainDriver.R * zb * tbottom / Pbeff / 5.615; //assign new freegas amount
				qgtbbl_gas = (MainDriver.freegasmole[iPenet] * MainDriver.R * zb * tbottom / Pbeff) / 5.615; //pit volume gain only because of free gas	, reservoir condition	
				//
			}
		}
		//------------------------------------------------------------------------------------ End of OBM case
		//1848 dummy = 0


		if (iPenet > IPmax - 21) {
			//warning();
			String s = "Gas influx rate is so small that variables are out of "+"\n"+"array dimensions defined in the program. "+"\n"+ " Please check formation properties then try again !";
			JOptionPane.showMessageDialog(null, s, "Warning", JOptionPane.INFORMATION_MESSAGE);//일단 얘도 pass
			menuClose();
			return 0;
		}
		//----------------------------------------------------------------------------
		if(MainDriver.imud == 0) QgTotMscf = QgOldMscf + 0.001 * qgtbbl / MainDriver.fvfGas;   // in Mscf
		else if(MainDriver.imud == 1) QgTotMscf = QgOldMscf + 0.001 * qgtbbl_gas / MainDriver.fvfGas;   // in Mscf

		Pdiff = -20.5;

		//-------------------------------------------------------------------------------
		if (MainDriver.iBOP == 0){
			Qcirc = 0; Qgas = 0; Qt = 0;
			pxOld = 0.5 * (Pkick + Pbeff); // Middle point
			//
			//       MainDriver.MudComp = 0.000006 //1/psi, typical value = 6.0E-6; Now used as INPUT data - 7/9/02
			if (MainDriver.iMudComp == 0){  //without considering mud compressibility
				if (QgOldMscf < 0.000001) QgOldMscf = 0.000001;
				pxMid = pxOld * QgTotMscf / QgOldMscf;           // from real gas law
			}
			else{
				delKick = (QgTotMscf - QgOldMscf) * 1000 * MainDriver.fvfGas;    //in rbbls
				sPVgainOld = QgOldMscf * 1000 * MainDriver.fvfGas;                //in rbbls
				compVol = Cgi * sPVgainOld + MainDriver.MudComp * (MainDriver.VOLinn + MainDriver.VOLout - sPVgainOld);
				DPincrease = delKick / compVol;
				pxMid = pxOld + DPincrease;
			}
			Pbeff = Pbeff + (pxMid - pxOld); 
			Pkick = Pkick + (pxMid - pxOld);
			Pdiff = Pbeff - MainDriver.Pform;
			//	pbeff가 문제임 pdiff가 0보다커야되나 해야지 kill이 작동하는데 pbeff가 pform보다 작게 측정 가면갈수록 더작아지기때문에 문제가 생긴다... 집중력 하강으로 ㅜㅜ ggg...		
			if (Pbeff - MainDriver.Pform > -0.09){
				iStable = 1; QgDay = 0;

				if(TimerDrillOn==1){
					TimerDrill.cancel();
					TimerDrillOn=0;
				}

				if(TimerSIOn==0){
					TimerSI = new Timer();
					TimerSI.schedule(new SITask(), 0, TimeDrlIntv); 		//히히			
					//TimerSI.schedule(new SITask(), 0, 2000); 		//히히			
					TimerSIOn=1;
				}			

				Pkick = Pkick - (Pbeff - MainDriver.Pform);
				Pbeff = MainDriver.Pform + 0.001; 
				MainDriver.SICP =  (Pkick - MainDriver.gMudOld * kickVD);
			}
			//-----------------
			double pxAvg = 0.5 * (Pkick + Pbeff);
			double tempMid = utilityModule.temperature(0.5f * (MainDriver.Vdepth + kickVD));
			double[] gaspex = new double[3];
			gaspex=propertyModule.GasProp(pxAvg, tempMid);//, gasVis, gasDen, zz)
			gasVis = gaspex[0];
			gasDen = gaspex[1];
			zz=gaspex[2];	    
			Pchoke = Pkick - MainDriver.gMudOld * kickVD;
			if(MainDriver.iProblem[3]==0) standpipeP = Pbeff - MainDriver.gMudOld * MainDriver.Vdepth;
			else standpipeP = Pbeff - 0.052 * MainDriver.oMud_save * MainDriver.Vdepth; 
			pumpP = psia;  // because no pumping !
			if (standpipeP < 14.7) standpipeP = 14.7;
			xmdloc = holeMD - kickMD;
			if (xmdloc > MainDriver.DepthCasing) Pcasing = MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + Pchoke;
			else{
				avgden = MainDriver.oMud * (1 - HgCal) + gasDen * HgCal;
				Pcasing = MainDriver.gMudOld * kickVD + 0.052 * avgden * (MainDriver.TVD[MainDriver.iCsg] - kickVD) + Pchoke;
			}
			if (iStable == 1) PcsgSI = Pcasing;
			QgOldMscf = QgTotMscf;
			return 2;
		}

		if(MainDriver.imud==0) PVgain = PVgain + qgtbbl;
		//else if(MainDriver.imud==1) PVgain = PVgain + qgtbbl*MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1]/MainDriver.oMud;//20140318 ajw surface condition
		else if(MainDriver.imud==1) PVgain = PVgain + qgtbbl;//20140318 ajw surface condition

		if(iShutinData == 0 && PVgain >= MainDriver.pitWarn && TimerWarnOn == 0){
			TimerWarnOn=1;				
			TimerWarn = new Timer();
			TimerWarn.schedule(new WarnTask(), 0, TimeDrlIntv);
		}

		Qgas = 42 * 60 * PVgain / (MainDriver.gTcum - MainDriver.timeToTD + 1.001);
		if (Qgas > MainDriver.Qdrill * 2.5 && MainDriver.Qdrill!=0){
			if(MainDriver.imud==0) Qgas = MainDriver.Qdrill * 2.5;   //to avoid overflow due to high Qgas
			else if(MainDriver.imud==1) Qgas = MainDriver.Qdrill * 2.5;
		}

		if(MainDriver.imud==0) Qt = Qcirc+Qgas;
		else Qt = Qcirc + Qgas; //Qcirc : pump cond., Qgas: surface cond. => Qt: surface cond.

		//...................... calculate gas fraction based on kick influx and circulation volume.
		//                       It should valid even when pump is off !
		if(MainDriver.imud==0) QtotVol = QtotVol + volLtmp[iPenet] + 1.2 * qgtbbl; // volltmp: pump condition, qgtbbl: reservoir condition, Qtotvol: reservoir condition 
		//else QtotVol = QtotVol + volLtmp[iPenet]*MainDriver.pumpDensity/MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1] + 1.2 * qgtbbl;
		else QtotVol = QtotVol + volLtmp[iPenet] + 1.2 * qgtbbl;

		if (QtotVol < 0.00001) HgCal = 0;
		else{
			if(MainDriver.imud==0) HgCal = PVgain / QtotVol;    //wbm case, 20% for gas migration
			else{//obm case
				//if(iPumpOn1==0 && iPumpOn2==0) HgCal = (PVgain*MainDriver.oMud/MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1]) / QtotVol; //20140318 ajw, PVgain : surface condition, qtotvol: reservoir condition.
				if(iPumpOn1==0 && iPumpOn2==0) HgCal = (PVgain*MainDriver.oMud/MainDriver.oMud) / QtotVol; //20140318 ajw, PVgain : surface condition, qtotvol: reservoir condition.
				else HgCal = qgtbbl_gas / QtotVol; //qgtbbl_gas: reservoir condition
			}
		}

		//
		//    HgCal = Qgas / qt   //this is valid only const circ. rate and constant gas influx.
		//.......................... modify the maximum effective gas fraction
		//                           from 0.0 to 0.4 to 0.8 at maximum
		//    if (MainDriver.iHresv = 1) Then   //max 0.8 - 0.6
		if (HgCal > 0.6) HgCal = 0.6 + (HgCal - 0.6) * 0.25 / 0.4;
		//    else                   //max 0.85 - 0.4
		//       if (hgcal > .4) Then hgcal = .4 + (hgcal - .4) * .4 / .6
		//    End if
		if (HgCal <= 0) HgCal = 0;
		//    txtMainDriver.SimRate.setText( Str(format(HgCal, "0."))  //to debug Hg
		//...................... calculate the kick height during drilling
		double hkdrl = utilityModule.getBotH(QtotVol, holeMD);
		double xmid =0;

		xmdloc = holeMD - hkdrl; 
		xmid = holeMD - 0.5 * hkdrl;
		if (xmdloc < 0.1 * holeMD) {
			String msg = "Your control is BAD or you spend too much time here ! ===> BLOWOUT is expected";
			JOptionPane.showMessageDialog(null, msg, "BlowOut!", JOptionPane.INFORMATION_MESSAGE);		//---------------------------       Show the BLOWOUT !		       

			m4.setVisible(true);

			if(TimerDrillOn==1){
				TimerDrill.cancel();
				TimerDrillOn=0;
			}		       
			menuClose();

			//MainMenu.Show
			//BlowOut.TimerBO.setEnabled = True =>blow out!!! important=>  i think this function has to go to the blowout module...
			return 0;
		}
		//.............. calculate the average properties for kick and velocity
		kickVD = utilityModule.getVD(xmdloc);
		kickMD = hkdrl;    //save for the future usage
		kickVDbottom = vdBit;   //Top & Bottom of the KICK !
		hvdeff = 0.5 * (MainDriver.Vdepth + kickVD);
		//.................................. computer pump pressure from BHP

		//Added by TY
		//delta_rho: average reduced mud density
		double delta_rho = 0;
		for(int i = iPenet; i>0; i--){
			delta_rho = delta_rho + MainDriver.Dendiff[iPenet];
		}
		if(iPenet == 0) delta_rho = 0;
		else delta_rho = delta_rho / iPenet;

		//Modified by TY
		//B.H.P calc considering reduced OBM density
		double[] calcex = new double[3];

		if(MainDriver.imud == 0) calcex = utilityModule.calcPeff2(psia, Qt, Qt, Qcirc, Qgas, MainDriver.oMud, xmdloc, hkdrl, HgCal);//, pkick, Pbeff, gasDen) //WBM
		else calcex = utilityModule.calcPeff(psia, Qt, Qt, Qcirc, Qgas, MainDriver.oMud, xmdloc, hkdrl, HgCal, delta_rho); //OBM

		Pkick = calcex[0];
		Pbeff = calcex[1];
		gasDen = calcex[2];		    

		double[] getdpex = new double[2];		    
		if(MainDriver.iProblem[3]==0) getdpex = utilityModule.getDPinside(Pbeff, Qcirc, xNull);//, standpipeP, pumpP)
		else getdpex = utilityModule.getDPinside_cutting(Pbeff, Qcirc, xNull);
		standpipeP = getdpex[0];
		pumpP = getdpex[1];
		if(MainDriver.gTcum>999){
			dummy=1;
		}
		if (xmdloc > MainDriver.DepthCasing) {
			utilityModule.getDP(Qt, MainDriver.DepthCasing, MainDriver.oMud);
			Pcasing = MainDriver.gMudOld * MainDriver.TVD[MainDriver.iCsg] + MainDriver.DPtop + psia;
		}
		else{
			utilityModule.getDP(Qt, xmdloc, MainDriver.oMud);
			avgden = MainDriver.oMud * (1 - HgCal) + gasDen * HgCal;
			Pcasing = MainDriver.gMudOld * kickVD + MainDriver.DPtop + 0.052 * avgden * (MainDriver.TVD[MainDriver.iCsg] - kickVD);
		}
		QgOldMscf = QgTotMscf;
		return 1;
		//
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
		double GaugeValue=tmpintCHK;	
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

	class PumpGaugePanel extends JPanel{			//15,70,150,150
		double GaugeValue=tmpintPumpP;
		double DrawAngle=0, Xcenter = 0 , Ycenter=0, Xoriginal=0;
		double Xcalculated =0;
		double Ycalculated =0;
		double RAD_CONVERT = 3.141592 / 180;			

		//    Line1.BorderWidth = 3

		PumpGaugePanel(){
			this.setBackground(new Color(240,240,240));
		}

		public void paint(Graphics g){
			super.paint(g);				
			GaugeValue = tmpintPumpP;				
			g.setColor(Color.BLACK);
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
		double GaugeValue=tmpintSDP;
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

	class BlowOutPanel extends JPanel{

		BlowOutPanel(){
			this.setBackground(new Color(240,240,240));
		}

		public void paint(Graphics g){
			super.paint(g);
			if(iPGwarn==0 || TimerWarnOn==0){
				g.setColor(Color.BLACK);
				g.drawOval(35-16, 20-16, 32, 32);				
			}

			else if (iPGwarn % 2 == 0){
				//Shape1.Visible = False;
				//Shape2.Visible = True;
				//Shape2.FillColor = QBColor(10); //light green(10), yellow(6)
				g.setColor(Color.GREEN);
				g.fillOval(27, 20, 16, 16);
			}

			else{
				//Shape2.Visible = False;
				//Shape1.Visible = True;
				//Shape1.FillColor = QBColor(12); //red color(4); light red(12)
				g.setColor(Color.RED);
				g.fillOval(35-16, 20-16, 32, 32);
			}

		}
	}

	class WarnTask extends TimerTask{
		public void run(){
			if(WarnTaskOn==1){
				iPGwarn = iPGwarn + 1;
				if (iPGwarn > 100) iPGwarn = 1;				
				if (iSound != 0) Toolkit.getDefaultToolkit().beep();
				BlowOutPnl.repaint();
			}
		}
	}


	void ShowResult(){
		String msgTmp="";

		//
		// Specify current operations  7/15/02
		msgTmp = "Operations : ";
		if(iPumpOn1 == 1 || iPumpOn2==1) msgTmp = msgTmp + " Pumping,";
		if(iROP == 1) msgTmp = msgTmp + " Drilling,";
		if (MainDriver.iBOP == 0) msgTmp = msgTmp + " Well shutin";
		lblPause.setText(msgTmp);
		//
		double ivary = iRotary % 3;   // Just change the ROP by 0.5 %
		double rop2 = iROP * ROP * (1 + (1 - ivary) * 0.005);
		if (rop2 < 0.001) rop2 = 0;	    
		if (hdBit <= 0.001) hdBit = 0;
		if (MainDriver.iBOP == 0) pumpP = psia;

		//ROP=rop2;

		//........... this is necessary, but we use qt and 0.0 for simplicity. 7/16/02
		//    mudFlowOut = qt    //actually, we need to follow qt in detail to save mudFlowOut
		//    gasFlowOut = 0    //use its default value in reality
		//    There will be no blowout unless you SI the well for long and long time
		//
		if(propertyModule.FractGrad(Pcasing, Pbeff)<0){;     //check possible formation fracture
		if(TimerWarnOn==1){
			TimerWarn.cancel();
			TimerWarnOn=0;
		}
		if(TimerDrillOn==1){
			TimerDrill.cancel();
			TimerDrillOn=0;
		}
		if(TimerSIOn==1){
			TimerSI.cancel();
			TimerSIOn=0;
			MainDriver.SItaskOn2=0;
		}				
		if(Sm1.m3.TimerRotOn==1){
			Sm1.m3.TimerRot.cancel();
			Sm1.m3.TimerRotOn=0;
		}			
		MainDriver.iWshow = 0;

		if(TimerBOOn==0){
			TimerCheckBO = new Timer();
			TimerCheckBO.schedule(new CheckBlowoutTask(), 0, 50);
			TimerBOOn=1;
		}
		}
		//
		if(MainDriver.iProblem[3]==1){
			txtWOB.setText((new DecimalFormat("##0")).format(MainDriver.WOB_now));
			txtRPM_control.setText((new DecimalFormat("##0")).format(MainDriver.RPM_now));

			WOBScroll.setValue((int)(MainDriver.WOB_now));
			RPMScroll.setValue((int)(MainDriver.RPM_now));
		}
		txtN2phase.setText(Integer.toString(MainDriver.N2phase)); //편의
		txtVDbit.setText((new DecimalFormat("###,##0.0#")).format(vdBit));
		txtHDbit.setText((new DecimalFormat("###,##0.0#")).format(hdBit));
		txtROP.setText((new DecimalFormat("##,##0.00")).format(rop2));
		txtPumpRate.setText((new DecimalFormat("##,##0")).format(Qcirc)); // mud pump rate at pumps.
		txtReturnRate.setText((new DecimalFormat("#,##0")).format(Qt-MainDriver.Q_loss)); // mud return at the surface.
		//txtGasRate.setText((new DecimalFormat(""/)).format(gasFlowOut));//there will be no gas on his STAGE !
		txtGasRate.setText( "0.0" );   
		txtPitGain.setText((new DecimalFormat("#,##0.0#")).format(PVgain + mlPVgain-MainDriver.V_loss));			
		txtPumpP.setText((new DecimalFormat("###,##0.0")).format(pumpP - psia));
		txtSPP.setText((new DecimalFormat("###,##0.0")).format(standpipeP - psia));
		txtFormationP.setText((new DecimalFormat("###,##0")).format(P2form - psia));
		txtBHP.setText((new DecimalFormat("###,##0")).format(Pbeff - psia));
		txtCasingP.setText((new DecimalFormat("###,##0")).format(Pcasing - psia));
		txtChokeP.setText((new DecimalFormat("###,##0.0")).format(Pchoke - psia)); //output text
		//
		utilityModule.getDP(Qt, MainDriver.Dwater, MainDriver.oMud);   //calculate mud line p. depending flow rate
		mudLineP = MainDriver.gMudOld * MainDriver.Dwater + MainDriver.DPtop * MainDriver.Pcon + Pchoke;
		txtMudLine.setText((new DecimalFormat("###,##0")).format(mudLineP - psia));
		//....... Since you do not have a gauge in VB v.6, you have to draw its gauge
		//        using a circle shape and a line shape objects
		//
		tmpintMud = Qt - Qcirc; 
		if (tmpintMud > 990) tmpintMud = 990;
		//...............................    gaugeMud.setSelected = tmpint  //in gpm
		pan1.repaint(); //Call DrawMudGauge(1, tmpint)    //return mud rate diffeRenCe
		txtRateDiff.setText((new DecimalFormat("##,##0")).format(tmpintMud));//text
		tmpintCHK = Pchoke - psia;
		if (tmpintCHK > 3960) tmpintCHK = 3960;
		pan2.repaint();//Call DrawMudGauge(2, tmpint)   //surface choke pressure
		txtPchoke.setText((new DecimalFormat("###,##0")).format(tmpintCHK));//text
		tmpintSDP = standpipeP - psia;
		if (tmpintSDP > 3960) tmpintSDP = 3960;
		pan3.repaint();//Call DrawMudGauge(3, tmpint)   //standpipe pressure
		txtPsp.setText((new DecimalFormat("###,##0")).format(tmpintSDP));//text
		tmpintPumpP = pumpP - psia;
		if (tmpintPumpP > 3960) tmpintPumpP = 3960;
		pan4.repaint();//Call DrawMudGauge(3, tmpint)   //standpipe pressure
		txtPp.setText((new DecimalFormat("###,##0")).format(tmpintPumpP));//text
		//
		if (iROP == 1 && Sm1.m3.TimerRotOn==0) {
			Sm1.m3.TimerRot = new Timer();
			Sm1.m3.TimerRot.schedule(Sm1.m3.new TRTask(), 0, TimeDrlIntv);
			Sm1.m3.TimerRotOn=1;
		}
		//if (MainDriver.iWshow == 1) Sm1.m3.setVisible(true); // removed by jw 20140305
		//------------- the easy way to calculate total time
		int totHr = (int)(MainDriver.gTcum / 3600);
		if (totHr < 1) totHr = 0;
		int totMin = (int)((MainDriver.gTcum - totHr * 3600) / 60);
		if (totMin < 1) totMin = 0;
		int totSec = (int)(MainDriver.gTcum - totHr * 3600 - totMin * 60);

		txtTime.setText( (int)totHr + ":" +(int)totMin + ":" + (int)totSec);

		Sm1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.						
		Sm1.m3.repaint();		

		Sa1.m3.showKickIndex=1; //showKick();  //---draw the kick in the vertical wellbore It exists in a wellpic paint function.			
		Sa1.m3.repaint();	
		if(timeNp > 29) DrillAssign();    //save for every 30 sec.				
	}

	void DrillAssign(){ // total 13 variables assigned
		timeNp = 0;   //7/12/02
		MainDriver.Np = MainDriver.Np + 1;
		if(MainDriver.Np > MainDriver.Npt - 150){   //to prevent long SI That causes out of array.
			String MsgBox = "You spent too much time.  Arrays are out of Range !";
			JOptionPane.showMessageDialog(null, MsgBox);
			menuClose();
		}
		//
		MainDriver.xTop[MainDriver.Np] =  kickVD;
		MainDriver.xBot[MainDriver.Np] =  kickVDbottom; 
		MainDriver.pxTop[MainDriver.Np] =  Pkick;
		MainDriver.rhoK[MainDriver.Np] =  gasDen;
		MainDriver.Psp[MainDriver.Np] =  standpipeP;
		MainDriver.Pcsg[MainDriver.Np] =  Pcasing;
		MainDriver.Pb2p[MainDriver.Np] =  Pbeff; 
		MainDriver.TTsec[MainDriver.Np] = MainDriver.gTcum;
		MainDriver.Pchk[MainDriver.Np] =  Pchoke;  
		MainDriver.Vpit[MainDriver.Np] =  (PVgain + mlPVgain);
		MainDriver.QmcfDay[MainDriver.Np] =  (QgDay + mlQgDay);
		MainDriver.mudflow[MainDriver.Np] =  Qt; 
		MainDriver.gasflow[MainDriver.Np] = 0;   //by default !, 7/16/02
		MainDriver.VOLcir[MainDriver.Np] = 0;
		MainDriver.Ppump[MainDriver.Np] =  pumpP;
		MainDriver.Stroke[MainDriver.Np] = (int)TotalStroke;
		MainDriver.StrokePump1[MainDriver.Np] = (int)Stroke1;
		MainDriver.StrokePump2[MainDriver.Np] = (int)Stroke2;
		if (iPumpOn1 == 0 && iPumpOn2==0) MainDriver.Ppump[MainDriver.Np] =  psia;
		MainDriver.CHKopen[MainDriver.Np] = 100; 
		if (MainDriver.iBOP == 0) MainDriver.CHKopen[MainDriver.Np] = 0;   //BOP is closed !
		MainDriver.PmLine[MainDriver.Np] =  mudLineP;   //it is calculated in each sub.
	}

	static void OBM_set2Pcal(){
		//...assining values in OBM case, Modified by TY
		int iLow=0, iUp=0, n2btm=0;
		int dummy=0;
		double[] gasPropEX = new double[3];
		double qGtot=0, qLtot=0, qLinc=0, xmoved=0, xLocNew=0, Qmove=0, qtop=0, Qbtm=0, area2=0, xVDup=0, Ptop=0, Tb=0, zb=0;
		double wkbTot = 0, wkbtmp=0, avgden=0, xgdown=0, volktot = 0, xVDdown=0;
		double gortemp=0;

		MainDriver.R = 10.73; //Universal gas constant
		//------------------------ Set Up two-phase calculations --------------
		//   assign the location and in-situ liquid fraction consider all possible
		//          conbinations & get grid information for 2-phase

		MainDriver.Xstart = MainDriver.TMD[MainDriver.NwcS-1];
		//Modified by Ty
		dxSlip = 0; //free gas가 trap되어 있다고 가정

		if (nHorizon == 0) MainDriver.Xstart = MainDriver.Xstart - dxSlip;
		MainDriver.Nstart = 1;
		for(int i = iPenet; i>-1; i--){
			qGtot = volGtmp[i]; 
			qLtot = volLtmp[i];
			qLinc = MainDriver.Vsol[i]; //Vdiff(i): swelling volume
			gortemp = gortmp[i]; //gas-oil ratio
			//
			MainDriver.tmp_solgas = MainDriver.solgasmole[i];
			MainDriver.tmp_freegas = MainDriver.freegasmole[i];
			MainDriver.tmp_gas = MainDriver.gasmole[i];
			MainDriver.tmp_T = deltaT[i];
			//For assigning values for OBM calc.
			dummy = utilityModule.node2p(qGtot, qLtot, qLinc, gortemp);
			//-------------------------------------------------------------------------------------------
			xmoved = MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.Xstart;
			//
			if (xmoved >= MainDriver.x2Hold && nHorizon == 2){
				nHorizon = 1;
				xLocNew = MainDriver.Xstart - dxSlip;
				iLow = utilityModule.Xposition(MainDriver.Xstart);
				iUp = utilityModule.Xposition(xLocNew);
				if (iLow == iUp) Qmove = (Math.pow(MainDriver.Do2p[iUp + 1], 2) - Math.pow(MainDriver.Di2p[iUp + 1], 2)) * dxSlip;
				else if (iLow + 1 == iUp){
					qtop = (Math.pow(MainDriver.Do2p[iUp + 1], 2) - Math.pow(MainDriver.Di2p[iUp + 1], 2)) * (MainDriver.TMD[iUp] - xLocNew);
					Qbtm = (Math.pow(MainDriver.Do2p[iUp], 2) - Math.pow(MainDriver.Di2p[iUp], 2)) * (MainDriver.Xstart - MainDriver.TMD[iUp]);
					Qmove = qtop + Qbtm;
				}
				else{
					area2 = Math.pow(MainDriver.Do2p[iLow + 1], 2) - Math.pow(MainDriver.Di2p[iLow + 1], 2);
					Qmove = area2 * (MainDriver.Xstart - MainDriver.TMD[iLow + 1]); // modified by jw 20131125 aera -> area
					for( int j = (iLow + 2); j<=iUp; j++){
						area2 = Math.pow(MainDriver.Do2p[j], 2) - Math.pow(MainDriver.Di2p[j], 2);
						Qmove = Qmove + area2 * (MainDriver.TMD[j - 1] - MainDriver.TMD[j]);
					}
					area2 = Math.pow(MainDriver.Do2p[iUp + 1], 2) - Math.pow(MainDriver.Di2p[iUp + 1], 2);
					Qmove = Qmove + area2 * (MainDriver.TMD[iUp] - xLocNew);
				}
				Qmove = MainDriver.C12 * Qmove;
				qGtot = 0;
				qLtot = Qmove;
				//For assigning values for OBM calc.
				dummy = utilityModule.node2p(qGtot, qLtot, qLinc, gortemp);
			}
			//
		}
		//..................... assign initial P, V, T, Z-factor, and wt for the
		//                      calculation of gas volume, DP etc
		n2btm = 1; 
		MainDriver.N2phase = MainDriver.Ncount;
		kickVD = utilityModule.getVD(MainDriver.Xstart);
		xVDup = kickVD;
		Ptop = Pchoke + MainDriver.gMudOld * kickVD;
		wkbTot = 0; 
		volktot = 0;		    

		for(int i = MainDriver.N2phase-1; i>0; i--){
			Tb = utilityModule.temperature(xVDup);
			gasPropEX = propertyModule.GasProp(Ptop, Tb);
			gasVis = gasPropEX[0];
			gasDen = gasPropEX[1];
			zb = gasPropEX[2];

			//Modified by TY		        
			//Call calcRs(Ptop, Tb, Gasgravity, Rs, Rsk, RsM, Rsn)		    
			wkbtmp = 42 * gasDen * MainDriver.volG[i]; //Wkbtmp= 각 Slug mixture 층에 포함되어 있는 가스킥의 무게 (pound 단위)
			wkbTot = wkbTot + wkbtmp;        

			MainDriver.volGold[i] = MainDriver.volG[i]; //old value
			MainDriver.pvtZb[i] = MainDriver.PVTZ_free[i];
			MainDriver.Pnd[i] = Ptop;

			MainDriver.volG[i] = MainDriver.PVTZ_free[i] * (zb * Tb) * MainDriver.R / Ptop / 5.615; //volG[i]; volume of freegas
			volktot = volktot + MainDriver.volG[i];

			if(MainDriver.volG[i] + MainDriver.volL[i] == 0) MainDriver.Hgnd[i] = 0; //Hgnd[i]; free gas fraction
			else MainDriver.Hgnd[i] = MainDriver.volG[i] / (MainDriver.volG[i] + MainDriver.volL[i]);

			avgden = MainDriver.oMud * (1 - MainDriver.Hgnd[i]) + gasDen * MainDriver.Hgnd[i];
			xgdown = MainDriver.Xnd[i-1];
			xVDdown =  utilityModule.getVD(xgdown);
			Ptop = Ptop + 0.052 * avgden * (xVDdown - xVDup);
			xVDup = xVDdown;
		}		    

		MainDriver.Pnd[0] = Ptop;
		kickVDbottom = xVDdown;  // assign the vertical depth

		// ----------- Assign values for the next calculations ---------------------
		MainDriver.NpSi =MainDriver.Np;
		MainDriver.Vpiti = QtotVol;		    
	}

	static void set2Pcal(){
		//... modified to consider ERD and ML, too,  2003/7/22
		int iLow;
		int iUp;
		int dummy=0;
		double[] gasPropEX = new double[3];
		double qGtot=0, qLtot=0, xmoved=0, xLocNew=0, Qmove=0, qtop=0, Qbtm=0, area2=0, xVDup=0, Ptop=0, Tb=0, zb=0;
		double wkbTot = 0, Wkbtmp=0, avgden=0, xgdown=0, volktot = 0, xVDdown=0;

		//------------------------ Set Up two-phase calculations --------------
		//   assign the location and in-situ liquid fraction consider all possible
		//		          conbinations & get grid information for 2-phase
		MainDriver.Xstart = MainDriver.TMD[MainDriver.NwcS-1];
		if (nHorizon == 0) MainDriver.Xstart = MainDriver.Xstart - dxSlip;
		MainDriver.Nstart = 1;
		for(int i = iPenet; i>-1; i--){
			qGtot = volGtmp[i]; 
			qLtot = volLtmp[i];
			MainDriver.tmp_T = deltaT[i];
			dummy = utilityModule.node2pW(qGtot, qLtot, MainDriver.tmp_T);//node2p(Xstart, qGtot, qLtot, Nstart, Ncount)   //volG() is assigned !
			xmoved = MainDriver.TMD[MainDriver.NwcS-1] - MainDriver.Xstart;
			//
			if (xmoved >= MainDriver.x2Hold && nHorizon == 2){     // calculate Vol_L in mid
				nHorizon = 1;
				xLocNew =  MainDriver.Xstart - dxSlip;
				iLow = utilityModule.Xposition(MainDriver.Xstart);
				iUp = utilityModule.Xposition(xLocNew);
				if(iLow == iUp) Qmove =  ((Math.pow(MainDriver.Do2p[iUp + 1],2) - Math.pow(MainDriver.Di2p[iUp + 1], 2)) * dxSlip);
				else if ((iLow + 1) == iUp){
					qtop =  ((Math.pow(MainDriver.Do2p[iUp + 1], 2) - Math.pow(MainDriver.Di2p[iUp + 1], 2)) * (MainDriver.TMD[iUp] - xLocNew));
					Qbtm =  ((Math.pow(MainDriver.Do2p[iUp] , 2) - Math.pow(MainDriver.Di2p[iUp] , 2)) * (MainDriver.Xstart - MainDriver.TMD[iUp]));
					Qmove = qtop + Qbtm;
				}
				else{
					area2 =  (Math.pow(MainDriver.Do2p[iLow + 1] , 2) - Math.pow(MainDriver.Di2p[iLow + 1] , 2));
					Qmove = area2 * (MainDriver.Xstart - MainDriver.TMD[iLow + 1]);
					for(int j = (iLow + 2); j < iUp + 1; j++){
						area2 =  (Math.pow(MainDriver.Do2p[j] , 2) - Math.pow(MainDriver.Di2p[j] , 2));
						Qmove = Qmove + area2 * (MainDriver.TMD[j - 1] - MainDriver.TMD[j]);
					}
					area2 =  (Math.pow(MainDriver.Do2p[iUp + 1] , 2) - Math.pow(MainDriver.Di2p[iUp + 1] , 2));
					Qmove = Qmove + area2 * (MainDriver.TMD[iUp] - xLocNew);
				}
				Qmove = MainDriver.C12 * Qmove;
				qGtot = 0;
				qLtot = Qmove;
				dummy = utilityModule.node2pW(qGtot, qLtot, MainDriver.tmp_T);
			}
		}
		//..................... assign initial P, V, T, Z-factor, and wt for the
		//		                      calculation of gas volume, DP etc
		int n2btm = 1;
		MainDriver.N2phase = MainDriver.Ncount;
		kickVD = utilityModule.getVD(MainDriver.Xstart);
		xVDup = kickVD;
		Ptop = Pchoke + MainDriver.gMudOld * kickVD;		    
		for(int i = (MainDriver.N2phase-1); i>0; i--){
			Tb = utilityModule.temperature(xVDup);
			gasPropEX = propertyModule.GasProp(Ptop, Tb);
			gasVis = gasPropEX[0];
			gasDen = gasPropEX[1];
			zb = gasPropEX[2];
			MainDriver.Pnd[i] = Ptop;
			MainDriver.pvtZb[i] = Ptop * MainDriver.volG[i] / (zb * Tb);
			Wkbtmp = 42 * gasDen * MainDriver.volG[i];
			wkbTot = wkbTot + Wkbtmp;
			volktot = volktot + MainDriver.volG[i];
			avgden = MainDriver.oMud * (1 - MainDriver.Hgnd[i]) + gasDen * MainDriver.Hgnd[i];
			xgdown = MainDriver.Xnd[i - 1];
			xVDdown = utilityModule.getVD(xgdown);
			Ptop = Ptop + 0.052 * avgden * (xVDdown - xVDup);
			xVDup = xVDdown;
		}		    
		MainDriver.Pnd[0] = Ptop;
		kickVDbottom = xVDdown;  // assign the vertical depth
		//
		// ----------- Assign values for the next calculations ---------------------
		MainDriver.NpSi = MainDriver.Np;
		MainDriver.Vpiti =  QtotVol;
	}

	void AdjustCell(){
		//--- adjust the cell size (maximun 20) to accelerate the simulation time
		//		    specially very low formation permeability and ROP etc that
		//		    cause very small gas influx rate
		int ic=0;
		int iModify=0;
		int iSum =0;
		double Xtmp = 0;
		double sumPVT = 0, sumGas = 0, sumMud = 0;
		//
		Xtmp = MainDriver.volG[MainDriver.N2phase-1] + MainDriver.volL[MainDriver.N2phase-1];
		if(Xtmp < 0.00002) MainDriver.N2phase = MainDriver.N2phase - 1;
		ic = MainDriver.N2phase-1;
		if (MainDriver.N2phase > 20){
			iModify = (int)(MainDriver.N2phase / 20) + 1;
			ic = 0;
			iSum = 0;                //the new counting
			sumPVT = 0; sumGas = 0; sumMud = 0;
			for(int i = 1; i<MainDriver.N2phase; i++){
				iSum = iSum + 1;
				sumPVT = sumPVT + MainDriver.pvtZb[i];
				sumGas = sumGas + MainDriver.volG[i];
				sumMud = sumMud + MainDriver.volL[i];
				if(MainDriver.Hgnd[i] < 0.0001 || iSum == iModify){
					ic = ic + 1;
					MainDriver.Pnd[ic] = MainDriver.Pnd[i];
					MainDriver.Xnd[ic] = MainDriver.Xnd[i];
					MainDriver.pvtZb[ic] =  sumPVT;
					MainDriver.volL[ic] =  sumMud;
					MainDriver.volG[ic] =  sumGas;
					MainDriver.Hgnd[ic] =  (sumGas / (sumMud + sumGas + 0.00002));
					iSum = 0;
					sumPVT = 0; sumGas = 0; sumMud = 0;
				}
			}

			if(MainDriver.Hgnd[MainDriver.N2phase-1] > 0.0001 && iSum < iModify){
				ic = ic + 1;
				MainDriver.Pnd[ic] = MainDriver.Pnd[MainDriver.N2phase];
				MainDriver.Xnd[ic] = MainDriver.Xnd[MainDriver.N2phase];
				MainDriver.pvtZb[ic] =  sumPVT;
				MainDriver.volL[ic] =  sumMud;
				MainDriver.volG[ic] =  sumGas;
				if((sumMud + sumGas) < 0.00002) sumMud = 0.00002;  //to prevent /0
				MainDriver.Hgnd[ic] =  (sumGas / (sumMud + sumGas));
			}
		}
		MainDriver.N2phase = ic+1; //20040205    
		//.... check whether there is zero gas volume and mud volume, which causes /0, 8/6/2003, 1/30/2004
		//		     we may need to search for until non-zero cell was found, 2/5/2004
		for(int ii = ic; ii>1; ii--){ 
			MainDriver.N2phase = ii+1;
			if(MainDriver.pvtZb[ii] > 0.0002) break;
		}
	}

	void CalChkVol(){
		//............ calculate area(sq in), volume of DS and annulus (bbls)
		//		             use the general approach:nwcs-starting , nwce-ending 
		//............................ calculate surface connections
		//		    nwce+2 =   12: surface line from pump to stand pipe
		//		    nwce+1 =   11: stand pipe, rotary hose, swivel, kelly for DP
		//.............................. calculate total annalus volume, bbls
		// need to calculate after BOP and choke/kill lines status specifying
		double[] getLinesEX = new double[5];
		double Qeff, capcty, volEff, d1, d2;
		if(MainDriver.iOnshore != 1){			   
			getLinesEX = utilityModule.getLines(0);//, d2, d1, Qeff, capcty, volEff) //index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
			Qeff = getLinesEX[0];
			capcty = getLinesEX[1];
			volEff = getLinesEX[2];
			d1 = getLinesEX[3];
			d2 = getLinesEX[4];
			//   A2p(NwcE) = 0.25 * pai * (d2 * d2)
			MainDriver.VOLout = MainDriver.VOLout - MainDriver.VOLann[MainDriver.NwcE-1] + volEff;
			MainDriver.VOLann[MainDriver.NwcE-1] = volEff;
			MainDriver.Do2p[MainDriver.NwcE-1] = d2;
			MainDriver.Di2p[MainDriver.NwcE-1] = d1;
			//
		}
	}

	void get_SIDcalculations(){
		//.... this sub is directly obtained from sub getGeometry & sub setControl
		//		     Just do only required calculations and avoid redundancy
		//		     (i.e., reading values are not required to calculate again.)   //8/6/2003
		// On Error GoTo ErrorStuff
		//
		double d1=0, d2=0, temp=0;
		try{
			if (MainDriver.iOnshore == 1) MainDriver.Dwater = 0;
			// Well geometry data for vertical, B, BH, BHB, and BHBH cases
			// This is NOT considered for multi[le runs ! - We should not change well geometry.
			//-------------------------------------------------------------------------------------
			//........... set the control variables
			MainDriver.Pcon = 0; 
			if (MainDriver.iDelp== 1) MainDriver.Pcon = 1;
			//................. velocity of sound = 340 m/sec = 1117 ft/s
			//						                                    = 4890 ft/sec in the water @ S.C.
			//   Vsound = 4890 * Sqr(8.33 / oMud) * 0.5    //use the half velocity
			// Model = 0; API RP13D, 1;Power-law, 2;Bjngham plastic, 3;Newtonian
			//
			MainDriver.visL = MainDriver.S600 - MainDriver.S300;
			if (MainDriver.Model == 1) MainDriver.visL = MainDriver.S300;
			MainDriver.pN =  Math.log10(MainDriver.S600 / MainDriver.S300) / Math.log10(2.0);   //pn & pk are required for all time
			MainDriver.pK =  510 * MainDriver.S300 / Math.pow(511, MainDriver.pN);
			//............ calculate area(sq in), volume of DS and annulus (bbls)
			//						             use the general approach;Nwcs-starting , Nwce-ending 
			MainDriver.VOLinn = 0;
			MainDriver.VOLout = 0; 
			MainDriver.iChoke = 1;   //set the temporary value
			for(int i = MainDriver.NwcS; i<=MainDriver.NwcE-1; i++){
				d2 = MainDriver.Do2p[i]; 
				d1 = MainDriver.Di2p[i];
				temp = (d2 * d2 - d1 * d1);
				MainDriver.VOLann[i] = MainDriver.C12 * temp * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
				d2 = MainDriver.DiDS[i];
				MainDriver.volDS[i] =  MainDriver.C12 * d2 * d2 * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
				MainDriver.VOLinn = MainDriver.VOLinn + MainDriver.volDS[i];
				MainDriver.VOLout = MainDriver.VOLout + MainDriver.VOLann[i];
			}
			//............................ calculate surface connections
			//						    Nwce+2 =   12; surface line from pump to stand pipe
			//						    Nwce+1 =   11; stand pipe, rotary hose, swivel, kelly for DP
			propertyModule.surfEqData();
			temp = MainDriver.DiaSP * MainDriver.DiaSP * MainDriver.LengthSP + Math.pow(MainDriver.DiaHose, 2) * MainDriver.LengthHose + Math.pow(MainDriver.DiaSwivel, 2) * MainDriver.LengthSwivel + Math.pow(MainDriver.DiaKelly, 2) * MainDriver.LengthKelly;
			MainDriver.volDS[MainDriver.NwcE] = MainDriver.C12 * temp;
			MainDriver.volDS[MainDriver.NwcE + 1] = MainDriver.C12 * MainDriver.DiaSurfLine * MainDriver.DiaSurfLine * MainDriver.LengthSurfLine;
			MainDriver.VOLinn = MainDriver.VOLinn + MainDriver.volDS[MainDriver.NwcE] + MainDriver.volDS[MainDriver.NwcE + 1];
			//------------------ Initial Calculation and Assignment are Completed
			MainDriver.gMudOld = 0.052 * MainDriver.oMud;
			MainDriver.gMudKill = 0.052 * MainDriver.Kmud;
			MainDriver.gMudCirc = 0.052 * MainDriver.Cmud;
		} catch(Exception e){
			//ErrorStuff;
			String s = "Input data and their format have been changed and they are not compatible."
					+"\n" + "  You have to review and correct all input data using the Change Input Data command"
					+"\n" + "  from the Main Menu. The sub quits here.";
			JOptionPane.showMessageDialog(null, s, "Warbubg",JOptionPane.INFORMATION_MESSAGE);
		}

	}

	void Case_Load(){
		set_Default();
		if(MainDriver.igERD == 1){
			//MainModule.setMLtrip();
		}
		else{
			setConventionalERD();
		}

		menuCaseSelect();

		if(MainDriver.iCaseSelection==1){
			Sm1= new SimManual();
		}
	}				

	void menuCaseSelect(){
		//..... 140617 AJW,  allocating the data when one case is selected in the main menu.
		//..... It will be similar to menuOpenSID_click subprogram.
		double[] porefracP = new double[2];
		iShutinData = 1;
		iDataChange = 0;
		OpenCaseData();

		//.............. show some related information to output boxes !
		MainDriver.iBOP = 0;  //BOP is closed for calculation purpose
		iROP = 0; MainDriver.iWshow = 0; timeNp = 2; iPumpOn1 = 0; iPumpOn2 = 0; //pump is off
		Qcirc = 0; Qt = 0; pumpP = psia;
		MainDriver.gTcum = MainDriver.TTsec[MainDriver.NpSi];
		standpipeP = MainDriver.SIDPP; Pchoke = MainDriver.Pchk[MainDriver.NpSi];  //consistent with SID data rather than sicp
		P2form = MainDriver.Pform;
		if(MainDriver.iProblem[3]==1 || MainDriver.iProblem[2]==1){
			porefracP = propertyModule.calcPoreFrac(vdBit);
			P2form = porefracP[0];
		}
		QtotVol = MainDriver.Vpiti; MainDriver.Vpiti = QtotVol;
		MainDriver.Np = MainDriver.NpSi;
		//obm의 경우는 녹아있기 때문에 이걸 없앨 수 없다
		//If pvtZb(N2phase) < 0.000002 Then N2phase = N2phase - 1  //to prevent very small amount
		//
		ShowResult();
		//
	}

	double[] PbCalc_SICP(double pK, double kickLoc){
		//  Calculates B.H.P. when annulus pressure is given. - new AWC version
		//  No Slip and Top properties as an average properties for Simplicity !
		// 'Added by TY for automatic chk control
		double avgden=0, ql=0, Qg=0;
		double volTot =0, volkx=0, voltmp=0, HgCal=0;
		double zx=0, hxt=0, dpmud=0, dpfri=0, DPdiff=0;
		//.................................... assign the top cell conditions
		double xNull = 0, xxx=0, wkbtmp=0;	    
		double pxcell = pK; 
		double xcell = kickLoc, xbcell=0, xbvdcell=0;
		double xvdcell=utilityModule.getVD(xcell);	    
		double tx = utilityModule.temperature(xvdcell), tx2=0;
		double surfTen = propertyModule.surfT(pxcell, tx);
		double QtotMix = 0;
		double[] GasPropEX =  new double[2];
		double dpkick = 0;
		double SICP_tmp = 0;
		double Pbtm = 0;
		double[] result = new double[2];
		int iXb = MainDriver.N2phase-1;
		double oilkickDensity_mixture  = 0; //ppg
		double mwTot = 0; 
		//
		Pcasing = 0;
		PVgain = 0;
		QtotMix = 0;
		volfree = 0;
		volsol = 0;
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
				MainDriver.PVTZ_free[i] = MainDriver.volL[i] * (MainDriver.gor[i] - MainDriver.Rs) * 0.0417 / 16.04; //freegas mole
				if(MainDriver.PVTZ_free[i] < 0) MainDriver.PVTZ_free[i] = 0;
				MainDriver.PVTZ_sol[i] = MainDriver.PVTZ_Gas[i] - MainDriver.PVTZ_free[i]; //solution gas mole
				//.................................. calculate the pressure at the top
				volkx = MainDriver.PVTZ_free[i] * tx * zx * MainDriver.R / pxcell; //pit gain by free gas volume(ft^3)
				volkx = volkx / 5.615; //pit gain by free gas volume(bbls)

				//---------------------------------------------------------------------------------------------------Applying PREOS
				MainDriver.OBMmole[i] = (MainDriver.volL[i] * MainDriver.foil) * MainDriver.OBMdensity * 42 / MainDriver.OBMwt;
				//
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
					MainDriver.V_cont = utilityModule.PREOS(pxcell, tx, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);
					MainDriver.V_cont_ref = utilityModule.PREOS(pxcell, tx, 0, 0.01, 0, MainDriver.mole_OBM);
					MainDriver.Vsol[i] = (MainDriver.V_cont - MainDriver.V_cont_ref);
					MainDriver.Vsol[i] = MainDriver.Vsol[i] * MainDriver.volL[i] * MainDriver.foil / MainDriver.V_cont;
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
			else volkx = MainDriver.volG[i]; //WBM CASE
			MainDriver.volG[i] = volkx;
			voltmp = MainDriver.volL[i] + MainDriver.Vsol[i];
			volTot = volkx + voltmp;
			volsol = volsol + MainDriver.Vsol[i];
			volfree = volfree + MainDriver.volG[i];
			hxt =  utilityModule.getTopH(volTot, xcell);
			xbcell = xcell + hxt; // xcell : top location of the cell, xbcell : bottom location of the cell
			xbvdcell=utilityModule.getVD(xbcell);				    
			//
			avgden = (MainDriver.oMud + MainDriver.Dendiff[i]) * (1 - volkx / (MainDriver.volL[i] + MainDriver.volG[i])) + gasDen * volkx / (MainDriver.volL[i] + MainDriver.volG[i]); //density change due to low-density gas dissolution
			dpmud = 0.052 * avgden * (xbvdcell - xvdcell);

			if(xvdcell<MainDriver.DepthCasing){
				if(MainDriver.DepthCasing < xbvdcell) Pcasing = Pcasing + 0.052 * avgden * (MainDriver.DepthCasing - xvdcell);
				else{
					Pcasing = Pcasing + dpmud;
				}
			}

			MainDriver.Pnd[i] =  pxcell;
			pxcell = pxcell + dpmud;
			xcell = xbcell;
			PVgain = PVgain + MainDriver.Vsol[i] + MainDriver.volG[i];
		}
		MainDriver.Pnd[0] = pxcell;			    
		xvdcell=utilityModule.getVD(kickLoc);
		if(MainDriver.imud==1 && MainDriver.iOilComp==1){ //추후에 반영할 것.			    	
			SICP_tmp = utilityModule.getDP_kill_auto(0, kickLoc, 0, pK, MainDriver.oMud);
			if(xvdcell<MainDriver.DepthCasing) Pcasing = pK + Pcasing;
			else Pcasing = utilityModule.getDP_kill_auto(0, kickLoc, MainDriver.DepthCasing, pK, MainDriver.oMud);
		}
		else{
			SICP_tmp = pK - 0.052 * MainDriver.oMud * xvdcell;
			if(xvdcell<MainDriver.DepthCasing) Pcasing = pK + Pcasing;
			else Pcasing = SICP_tmp + 0.052*MainDriver.oMud*MainDriver.DepthCasing;
		}			    
		Pbtm = pxcell;			    

		result[0] = SICP_tmp;
		result[1] = Pbtm;
		return result;
	}

	void OpenCaseData(){
		double Hg_Temp = 0;
		double avgden = 0;	    
		double hhm = 0;
		double kickLocation = 0;
		double h_kicktop=0;
		double h_kick=0;
		double h_kickintv_btm=0;
		double h_kickintv_top=0;
		double h_intervalkick=0;
		double gortemp = 0;
		double voltot = 0;
		double rs=0;
		double err=0;
		double tx = 0;
		double tb = 0;
		double zx = 0;
		double[] GasPropEX = new double[2];
		double Vpiti_temp = 0;
		double totGasMole = 0;

		mlPVgain = 0; //PVgain, Net pit volume gain in MLs, bbls"
		dxSlip = 0;  //dxSlip, kick migration distance, ft"
		vdBit = MainDriver.Vdepth;    //vdBit, Vertical depth of the well, ft"
		hdBit = MainDriver.Hdisp;    //hdBit, Horizonal departure of the well, ft"
		MainDriver.N2phase = 6;   //n2phase, Total number of 2-phase mixture slug + 1"

		//..... Input data ....
		if(MainDriver.iCase ==1){
			Hg_Temp = 0.1;
			PVgain = 2;  //PVgain, Net pit volume gain, bbls"
		}
		else if(MainDriver.iCase ==2){
			Hg_Temp = 0.4;
			PVgain = 20;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==3){
			Hg_Temp = 0.15;
			PVgain = 5;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==4){
			Hg_Temp = 0.1;
			PVgain = 3;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==5){
			Hg_Temp = 0.1;
			PVgain = 2;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==6){	    		
			Hg_Temp = 0.4;
			PVgain = 20;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==7){
			Hg_Temp = 0.1;
			PVgain = 2;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==8){
			Hg_Temp = 0.4;
			PVgain = 20;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==9){
			Hg_Temp = 0.1;
			PVgain = 3;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==10){
			Hg_Temp = 0.1;
			PVgain = 3;  //PVgain, Net pit volume gain, bbls"
		}
		if(MainDriver.iCase ==11){
			Hg_Temp = 0.1;
			PVgain = 2;  //PVgain, Net pit volume gain, bbls"
		}
		else if(MainDriver.iCase ==12){
			Hg_Temp = 0.4;
			PVgain = 20;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==13){
			Hg_Temp = 0.15;
			PVgain = 5;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==14){
			Hg_Temp = 0.1;
			PVgain = 3;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==15){
			Hg_Temp = 0.1;
			PVgain = 2;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==16){	    		
			Hg_Temp = 0.4;
			PVgain = 20;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==17){
			Hg_Temp = 0.1;
			PVgain = 2;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==18){
			Hg_Temp = 0.4;
			PVgain = 20;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==19){
			Hg_Temp = 0.1;
			PVgain = 3;  //PVgain, Net pit volume gain, bbls"
		}
		else if( MainDriver.iCase ==20){
			Hg_Temp = 0.1;
			PVgain = 3;  //PVgain, Net pit volume gain, bbls"
		}
		//--------------------	    	
		MainDriver.R = 10.73;

		if(MainDriver.imud == 0){ //WBM
			MainDriver.Vpiti = PVgain / Hg_Temp;    //Vpiti, initial mixure volume, bbls"		        
			hhm = utilityModule.getBotH(MainDriver.Vpiti, holeMD);
			kickLocation = holeMD - hhm;
			h_kicktop = utilityModule.getVD(kickLocation);
			h_kick = MainDriver.Vdepth - h_kicktop;
			gortemp = PVgain / MainDriver.fvfGas / (PVgain/Hg_Temp-PVgain);

			MainDriver.Hgnd[0] = 0; 
			MainDriver.volG[0] = 0;
			MainDriver.volL[0] = 0; 
			MainDriver.Xnd[0] = MainDriver.TMD[MainDriver.NwcS-1];
			MainDriver.delta_T[0] = 0;	    		
			for(int i = 2; i<=MainDriver.N2phase; i++){
				MainDriver.Hgnd[i-1] = Hg_Temp;
				MainDriver.volG[i-1] = PVgain / (MainDriver.N2phase - 1); // divide total kick into equal grids.
				MainDriver.volL[i-1] = MainDriver.volG[i-1] * (1 - MainDriver.Hgnd[i-1]) / MainDriver.Hgnd[i-1];
				MainDriver.delta_T[i-1] = MainDriver.volL[i-1] * 42 / MainDriver.Qdrill * 60;

				hhm = utilityModule.getBotH(MainDriver.volG[i-1] + MainDriver.volL[i-1], MainDriver.Xnd[i - 2]);
				MainDriver.Xnd[i-1] = MainDriver.Xnd[i - 2] - hhm;
			}
			MainDriver.SICP = calcSICP(PVgain, Hg_Temp)[0]; //sicp, Shut-in Casing Pressure, psia"
		}
		/*else{ //OBM
	    		MainDriver.Hgnd[0] = 0; 
	    		MainDriver.volG[0] = 0;
	    		MainDriver.volL[0] = 0; 
	    		MainDriver.Vfree[0] = 0;
	    		MainDriver.Xnd[0] = MainDriver.TMD[MainDriver.NwcS-1];
	    		MainDriver.gor[0] = 0; 
	    		MainDriver.PVTZ_Gas[0] = 0;	    		
	    		MainDriver.delta_T[0] = 0;
	    		MainDriver.Vsol[0]=0;	

	    		utilityModule.OBM_Composition_PREOS();
	    		MainDriver.SICP = calcCompositeMole_OBM(PVgain); //sicp, Shut-in Casing Pressure, psia"	  
	    		}*/
		else{ //OBM	    		
			// gasmole setting	    		
			Vpiti_temp = PVgain / Hg_Temp;    //Vpiti, initial mixure volume, bbls"		        
			hhm = utilityModule.getBotH(Vpiti_temp, holeMD);
			kickLocation = holeMD - hhm;
			h_kicktop = utilityModule.getVD(kickLocation);
			h_kick = MainDriver.Vdepth - h_kicktop;
			gortemp = PVgain / MainDriver.fvfGas / (PVgain/Hg_Temp-PVgain);

			MainDriver.Hgnd[0] = 0; 
			MainDriver.volG[0] = 0;
			MainDriver.volL[0] = 0; 
			MainDriver.Xnd[0] = MainDriver.TMD[MainDriver.NwcS-1];
			MainDriver.delta_T[0] = 0;	    	
			MainDriver.Dendiff[0] = 0;
			MainDriver.Vfree[0] = 0;
			MainDriver.gor[0] = 0; 
			MainDriver.PVTZ_Gas[0] = 0;	    		
			MainDriver.delta_T[0] = 0;
			MainDriver.Vsol[0]=0;
			volliquid  = 0;
			for(int i = 2; i<=MainDriver.N2phase; i++){
				MainDriver.Dendiff[i-1] = 0;
				MainDriver.Hgnd[i-1] = Hg_Temp;
				MainDriver.Vfree[i-1] = 0;
				MainDriver.gor[i-1] = 0; 
				MainDriver.PVTZ_Gas[i-1] = 0;	    		
				MainDriver.delta_T[i-1] = 0;
				MainDriver.Vsol[i-1]=0;
				MainDriver.volG[i-1] = PVgain / (MainDriver.N2phase - 1); // divide total kick into equal grids.
				MainDriver.volL[i-1] = MainDriver.volG[i-1] * (1 - MainDriver.Hgnd[i-1]) / MainDriver.Hgnd[i-1];
				MainDriver.delta_T[i-1] = MainDriver.volL[i-1] * 42 / MainDriver.Qdrill * 60;
				volliquid = MainDriver.volL[i-1] + volliquid;
				hhm = utilityModule.getBotH(MainDriver.volG[i-1] + MainDriver.volL[i-1], MainDriver.Xnd[i - 2]);
				MainDriver.Xnd[i-1] = MainDriver.Xnd[i - 2] - hhm;	    			
			}
			MainDriver.imud = 0;
			MainDriver.SICP = calcSICP(PVgain, Hg_Temp)[0]; //sicp, Shut-in Casing Pressure, psia"
			totGasMole=0;
			for(int i = 2; i<=MainDriver.N2phase; i++){
				h_kickintv_top = utilityModule.getVD(MainDriver.Xnd[i - 1]);
				tx = utilityModule.temperature(h_kickintv_top);
				GasPropEX = propertyModule.GasProp(MainDriver.Pnd[i-1], tx);
				zx = GasPropEX[2]; //get z-factor of gas
				totGasMole = totGasMole + MainDriver.Pnd[i-1] * MainDriver.volG[i-1] * 5.6145 / (zx * tx* MainDriver.R);
			}	    		
			//

			MainDriver.Hgnd[0] = 0; 
			MainDriver.volG[0] = 0;
			MainDriver.volL[0] = 0; 
			MainDriver.Vfree[0] = 0;
			MainDriver.Xnd[0] = MainDriver.TMD[MainDriver.NwcS-1];
			MainDriver.gor[0] = 0; 
			MainDriver.PVTZ_Gas[0] = 0;	    		
			MainDriver.delta_T[0] = 0;
			MainDriver.Vsol[0]=0;	
			MainDriver.imud = 1;
			if(MainDriver.mud_calc==0) utilityModule.OBM_Composition_PREOS();
			else if(MainDriver.mud_calc==1) utilityModule.OBM_Composition_SK();
			PVgain = calcCompositeMole_OBM(PVgain, Hg_Temp, totGasMole); //sicp, Shut-in Casing Pressure, psia"	 
			PVgain = 0;
			for(int i = 2; i<=MainDriver.N2phase; i++){
				MainDriver.volL[i-1] = volliquid/(MainDriver.N2phase-1); 
				MainDriver.Vsol[i-1] = volsol/(MainDriver.N2phase-1);
				MainDriver.volG[i-1] = volfree/(MainDriver.N2phase-1); //PVTZ_free_ex MainDriver.volG[i-1] * 5.615 / MainDriver.fvfGas * 0.0417 / 16.04;
				MainDriver.Hgnd[i-1] = MainDriver.volG[i-1]/(MainDriver.volL[i-1]+MainDriver.Vsol[i-1]+MainDriver.volG[i-1]); 
				voltot = MainDriver.volL[i-1]+MainDriver.Vsol[i-1]+MainDriver.volG[i-1];
				hhm = utilityModule.getBotH(voltot, MainDriver.Xnd[i-2]);		
				MainDriver.Xnd[i-1] = MainDriver.Xnd[i-2]-hhm;
				MainDriver.PVTZ_Gas[i-1] = (PVTZ_free_ex+PVTZ_sol_ex)/(MainDriver.N2phase-1);	    	
				MainDriver.gor[i-1] = MainDriver.PVTZ_Gas[i-1]*16.04/0.0417/MainDriver.volL[i-1]; 
				MainDriver.delta_T[i-1] = MainDriver.volL[i-1] * 42 / MainDriver.Qdrill * 60;		
				MainDriver.Dendiff[i-1] = Dendiffex;
				PVgain = PVgain + MainDriver.Vsol[i-1] + MainDriver.volG[i-1];

			}

			MainDriver.SICP = calcSICP_OBM(PVgain, Hg_Temp)[0]; //sicp, Shut-in Casing Pressure, psia"
		}

		//....... 2-phase Kick information from well shut in and well stabilization !
		//-----------------------------------------------------------------------------------------------------
		if(MainDriver.imud == 0){ //WBM
			MainDriver.pvtZb[0] = 0;
			totGasMole = 0;
			for(int i = 2; i<=MainDriver.N2phase; i++){
				h_kickintv_top = utilityModule.getVD(MainDriver.Xnd[i - 1]);
				tx = utilityModule.temperature(h_kickintv_top);
				GasPropEX = propertyModule.GasProp(MainDriver.Pnd[i-1], tx);
				zx = GasPropEX[2]; //get z-factor of gas
				MainDriver.pvtZb[i-1] = MainDriver.Pnd[i-1] * MainDriver.volG[i-1] / (zx * tx);
				totGasMole = totGasMole + MainDriver.Pnd[i-1] * MainDriver.volG[i-1] * 5.6145 / (zx * tx* MainDriver.R);
			}
		}
		else{ //OBM	   
			MainDriver.Vdiff_old[0] = 0;
			MainDriver.Kicknd[0] = MainDriver.Xnd[0];

			for(int i = 2; i<=MainDriver.N2phase; i++){
				h_kickintv_top = utilityModule.getVD(MainDriver.Xnd[i - 1]);
				tx = utilityModule.temperature(h_kickintv_top);
				GasPropEX = propertyModule.GasProp(MainDriver.Pnd[i-1], tx);
				zx = GasPropEX[2]; //get z-factor of gas
				MainDriver.pvtZb[i-1] = MainDriver.Pnd[i-1] * MainDriver.Vfree[i-1] / (zx * tx);	    			
			}
		}

		//.............................. ........ read final results with time
		MainDriver.NpSi = 1;   //Npsi, Data saving index of the results"
		//
		MainDriver.xTop[MainDriver.NpSi] = utilityModule.getVD(MainDriver.Xnd[MainDriver.N2phase-1]);
		MainDriver.xBot[MainDriver.NpSi] = utilityModule.getVD(MainDriver.Xnd[0]);
		MainDriver.pxTop[MainDriver.NpSi] = MainDriver.Pnd[MainDriver.N2phase-1];
		MainDriver.Vpit[MainDriver.NpSi] = PVgain + mlPVgain;

		tb = utilityModule.temperature(h_kicktop);
		GasPropEX = propertyModule.GasProp(MainDriver.pxTop[MainDriver.NpSi], tb); //get z-factor of gas
		gasVis = GasPropEX[0];
		gasDen = GasPropEX[1];
		zx = GasPropEX[2];
		MainDriver.rhoK[MainDriver.NpSi] = gasDen;
		MainDriver.TTsec[MainDriver.NpSi] = 0;

		MainDriver.Ppump[0] = 14.7;
		MainDriver.Ppump[MainDriver.NpSi] = 14.7;
		MainDriver.Psp[0] = MainDriver.SIDPP;
		MainDriver.Psp[MainDriver.NpSi] = MainDriver.SIDPP;
		MainDriver.Pchk[0] = MainDriver.SICP;
		MainDriver.Pchk[MainDriver.NpSi] = MainDriver.SICP;
		MainDriver.Pcsg[0] = Pcasing;
		MainDriver.Pcsg[MainDriver.NpSi] = Pcasing;
		MainDriver.Pb2p[MainDriver.NpSi] = MainDriver.Pform;
		MainDriver.PmLine[0] = MainDriver.gMudOld * MainDriver.Dwater + MainDriver.Pchk[MainDriver.NpSi];
		MainDriver.PmLine[MainDriver.NpSi] = MainDriver.gMudOld * MainDriver.Dwater + MainDriver.Pchk[MainDriver.NpSi];

		MainDriver.StrokePump1[MainDriver.NpSi] = 0;
		MainDriver.StrokePump2[MainDriver.NpSi] = 0;
		MainDriver.Stroke[MainDriver.NpSi] = MainDriver.StrokePump1[MainDriver.NpSi] + MainDriver.StrokePump2[MainDriver.NpSi];
		MainDriver.VOLcir[MainDriver.NpSi] = 0;
		MainDriver.CHKopen[MainDriver.NpSi] = 0;
		MainDriver.QmcfDay[MainDriver.NpSi] = 0;
		MainDriver.mudflow[MainDriver.NpSi] = 0;
		MainDriver.gasflow[MainDriver.NpSi] = 0;	    
	}

	void checkProblem(){
		String msgProblem = "Drilling problem occurs! You should choose the right solution.";
		int i=0;
		double pp_now = 0;
		seed = seed+1;
		i = seed%100+1;
		random = new Random(i);
		int randomNum = random.nextInt(99);
		double rand = randomNum/100.0;
		double ratio = 0;

		if(MainDriver.iProblem[0]==1){
			ratio = MainDriver.Qdrill/(MainDriver.Qcapacity1 * 42 * 60);
			if(ratio<0.2) pp_now = MainDriver.probability_problem[0] * (1-ratio/4);
			else if(ratio<=1.8) pp_now = MainDriver.probability_problem[0] * (0.95-(ratio-0.2)*9.0/16.0);
			else if(ratio<=2) pp_now = MainDriver.probability_problem[0] * (0.05-(ratio-1.8)/4);
			else pp_now = 0;
			if(rand<=pp_now || MainDriver.iProblem_occured[0]==1){				
				MainDriver.iProblem_occured[0]=1;				
				return;
			}
		}
		if(MainDriver.iProblem[1]==1){
			pp_now = MainDriver.probability_problem[1] * (1-xDrill / MainDriver.mdBitOff);
			if(rand<=pp_now || MainDriver.iProblem_occured[1]==1){
				MainDriver.iProblem_occured[1]=1;
				return;
			}
		}
		else if(MainDriver.iProblem[2]==1){
			if(vdBit<=MainDriver.layerVDto[MainDriver.layer_mud_loss] && vdBit>MainDriver.layerVDfrom[MainDriver.layer_mud_loss] || MainDriver.iProblem_occured[2]==1){
				MainDriver.iProblem_occured[2]=1;
				return;
			}
			else{

			}
		}
	}

	double[] calcSICP_OBM(double gasVol, double Hg){
		double avgd = 0;
		double[] result = new double[2];
		double P_kick_top = 0;
		double h_kick = 0;
		double Vol_KickMix = 0;
		double Pbottom_eff = 0;
		double SICP_temp = 0;
		double px1 = 0;
		double px2 = 0;
		double Err = 0;
		double hhm = 0;
		double kickLocation = 0;
		double h_kicktop=0;
		double f1=0;
		double f2=0;
		double Pbottom_calc=0;
		int iter = 0;

		Vol_KickMix = volsol+volliquid+volfree;
		hhm = utilityModule.getBotH(Vol_KickMix, holeMD);
		kickLocation = holeMD - hhm;			
		h_kicktop = utilityModule.getVD(kickLocation);
		h_kick = MainDriver.Vdepth - h_kicktop;

		px1 = 14.7;
		px2 = MainDriver.Pform;
		f1 = 1;
		f2 = -1;
		Pbottom_calc = 0;
		iter = 0;
		Err = 1;

		while(Math.abs(Err) > 0.00000001 && iter < 51){
			iter = iter + 1;
			P_kick_top = (px1 + px2) / 2;
			if(iter==26){
				double dummy=1;
			}
			result = PbCalc_SICP(P_kick_top, kickLocation);

			SICP_temp =result[0];
			Pbottom_eff = result[1];

			Err = (MainDriver.Pform - Pbottom_eff) / MainDriver.Pform;
			if (f1 * Err < 0){        //BiSection method
				px2 = P_kick_top;
				f2 = MainDriver.Pform - Pbottom_calc;
			}
			else{
				px1 = P_kick_top; 
				f1 = MainDriver.Pform - Pbottom_calc;
			}

			if (iter >= 50){
				P_kick_top = P_kick_top + MainDriver.Pform - Pbottom_calc;
			}

			if(P_kick_top < 14.6){
				P_kick_top = 14.7;
			}
			Err = Math.abs(MainDriver.Pform - Pbottom_eff)/MainDriver.Pform;
		}
		result[0] = SICP_temp;
		result[1] = avgd;
		return result;
	}

	double[] calcSICP(double gasVol, double Hg){
		double avgd = 0;
		double[] result = new double[2];
		double P_kick_top = 0;
		double h_kick = 0;
		double Vol_KickMix = 0;
		double Pbottom_eff = 0;
		double SICP_temp = 0;
		double px1 = 0;
		double px2 = 0;
		double Err = 0;
		double hhm = 0;
		double kickLocation = 0;
		double h_kicktop=0;
		double f1=0;
		double f2=0;
		double Pbottom_calc=0;
		int iter = 0;

		Vol_KickMix = gasVol / Hg;
		hhm = utilityModule.getBotH(Vol_KickMix, holeMD);
		kickLocation = holeMD - hhm;			
		h_kicktop = utilityModule.getVD(kickLocation);
		h_kick = MainDriver.Vdepth - h_kicktop;

		px1 = 14.7;
		px2 = MainDriver.Pform;
		f1 = 1;
		f2 = -1;
		Pbottom_calc = 0;
		iter = 0;
		Err = 1;

		while(Math.abs(Err) > 0.001 && iter < 51){
			iter = iter + 1;
			P_kick_top = (px1 + px2) / 2;
			result = PbCalc_SICP(P_kick_top, kickLocation);

			SICP_temp =result[0];
			Pbottom_eff = result[1];

			Err = (MainDriver.Pform - Pbottom_eff) / MainDriver.Pform;
			if (f1 * Err < 0){        //BiSection method
				px2 = P_kick_top;
				f2 = MainDriver.Pform - Pbottom_calc;
			}
			else{
				px1 = P_kick_top; 
				f1 = MainDriver.Pform - Pbottom_calc;
			}

			if (iter >= 50){
				P_kick_top = P_kick_top + MainDriver.Pform - Pbottom_calc;
			}

			if(P_kick_top < 14.6){
				P_kick_top = 14.7;
			}
			Err = Math.abs(MainDriver.Pform - Pbottom_eff)/MainDriver.Pform;
		}
		result[0] = SICP_temp;
		result[1] = avgd;
		return result;
	}

	double calcCompositeMole_OBM(double PVgain, double Hg_Temp, double totGmole){
		double P_kick_mid = 0;
		double Pbottom_eff = 0;
		double px1 = 0;
		double px2 = 0;
		double Err = 0;
		double Err2 = 0;
		double hhm = 0;			
		double f1=0;
		double f2=0;
		double Pbottom_calc=0;
		int iter = 0;
		double xcell=0; // top of the kick
		double xvdcell=0;
		double xvdcell2=0;
		double tx = 0;
		double tx2 = 0;
		double pxcell = 0;
		double right = 0;
		double left = 0;
		double iter1=0;
		double OBMmoleex=0;
		double OBMFracex=0;
		double GasFracex=0;
		double voltot=0;
		double hxt=0;
		double xbcell=0;
		double xbvdcell=0;
		double avgden=0;
		double dpmud=0;
		double err3=0;
		double iter3=0;
		double left1=0;
		double right1=0;
		double zx = 0;
		double[] GasPropEX =  new double[2];
		double zb=0;
		double oilkickDensity_mixture  =0; //ppg
		double mwTot = 0;

		Err = 1;
		Err=1;
		iter1=0;
		left = 0;
		right = MainDriver.TMD[MainDriver.NwcS-1];

		while(Math.abs(Err)>0.0001 && iter1 < 201){
			iter1 = iter1 + 1;
			// setting properties of the top cell
			xcell = (left+right)/2; //center of the kick
			xvdcell=utilityModule.getVD(xcell);
			tx = utilityModule.temperature(xvdcell);
			MainDriver.R = 10.73; //universal gas constant
			tx2 = tx - 460; //tx: rankine / tx2: fahrenheit
			//Call calcRs(pxcell, tx, 0.5537, Rs, Rsk, RsM, Rsn, ibaseoil) //gas solubility calc. by O//bryan//s
			iter = 0;
			Err2 = 1;

			px1 = MainDriver.Pform-3000;
			px2 = MainDriver.Pform;
			f1 = 1;
			f2 = -1;
			Pbottom_calc = 0;

			while(Math.abs(Err2) > 0.0001 && iter < 201){
				iter = iter + 1;
				xcell = (left+right)/2; //center of the kick
				P_kick_mid = (px1 + px2) / 2;
				pxcell = P_kick_mid;

				MainDriver.Rs = utilityModule.calcRs2(pxcell, tx2); //gas solubility calc. by PVTi sfc/stb
				if(MainDriver.Rs < 0) MainDriver.Rs = 0;

				GasPropEX = propertyModule.GasProp(pxcell, tx);//, gasVis, gasDen, zx)
				gasVis = GasPropEX[0];
				gasDen = GasPropEX[1];
				zx = GasPropEX[2];					

				volsolsc = MainDriver.Rs*MainDriver.foil*volliquid/5.615;
				OBMmoleex = (volliquid * MainDriver.foil) * MainDriver.OBMdensity * 42 / MainDriver.OBMwt; // liquid mole
				PVTZ_sol_ex = volsolsc * 5.615 * 0.0417 / 16.04; //solutiongas mole
				PVTZ_free_ex = totGmole - PVTZ_sol_ex;

				if(totGmole<PVTZ_sol_ex){
					PVTZ_sol_ex=totGmole;
					PVTZ_free_ex = 0;
				}					

				OBMFracex = (OBMmoleex / (PVTZ_sol_ex + OBMmoleex)) / 100;
				GasFracex = (0.01 - OBMFracex) * 100;
				//
				MainDriver.OBMFr = OBMFracex;
				MainDriver.GasFr = GasFracex;
				//
				MainDriver.mole_solgas = PVTZ_sol_ex;
				MainDriver.mole_OBM = OBMmoleex;
				//
				if(MainDriver.mud_calc==0){
					/*MainDriver.V_cont = utilityModule.PREOS(pxcell, tx, MainDriver.GasFr, MainDriver.OBMFr, MainDriver.mole_solgas, MainDriver.mole_OBM);
						MainDriver.V_cont_ref = utilityModule.PREOS(pxcell, tx, 0, 0.01, 0, MainDriver.mole_OBM);
						volsol = (MainDriver.V_cont - MainDriver.V_cont_ref);
						volsol = volsol * volliquid * MainDriver.foil / MainDriver.V_cont;*/
					utilityModule.OBM_Fraction(MainDriver.GasFr, MainDriver.OBMFr); //solution gas mole, obm gas mole
					oilkickDensity_mixture  = utilityModule.calcPREOSdensity_2p(tx, pxcell-14.7, MainDriver.GasFr, MainDriver.OBMFr); //ppg
					mwTot = MainDriver.mole_solgas * 16.04 + MainDriver.OBMwt * MainDriver.mole_OBM; //lb\
					volsol = mwTot / oilkickDensity_mixture / 42 - volliquid * MainDriver.foil;
				}
				else{
					utilityModule.OBM_Fraction(MainDriver.GasFr, MainDriver.OBMFr); //solution gas mole, obm gas mole
					oilkickDensity_mixture  = utilityModule.calcSKdensity(tx, pxcell-14.7); //ppg
					mwTot = MainDriver.mole_solgas * 16.04 + MainDriver.OBMwt * MainDriver.mole_OBM; //lb\
					volsol = mwTot / oilkickDensity_mixture / 42 - volliquid * MainDriver.foil;
				}

				//err3 = PVgain - (volsol+volfree);
				//err3 = PVgain - (volsolsc * MainDriver.fvfGas*5.615 +volfree);

				volfree = PVTZ_free_ex / pxcell / 5.615 *MainDriver.R *tx*zx;
				MainDriver.Vpiti = volsol + volfree + volliquid;
				hhm = utilityModule.getBotH(MainDriver.Vpiti, holeMD);
				xvdcell2 = holeMD - hhm/2;

				MainDriver.Den_tmp = (MainDriver.oMud * volliquid * 42 + PVTZ_sol_ex * 16.04) / (volliquid + volsol) / 42;
				Dendiffex = MainDriver.Den_tmp - MainDriver.oMud; //density change

				voltot = volfree + volliquid + volsol;

				hhm = utilityModule.getBotH(voltot, holeMD);
				xbcell = xcell + hhm/2; 
				xvdcell=utilityModule.getVD(xcell);
				xbvdcell=utilityModule.getVD(xbcell);				    
				//
				avgden = (MainDriver.oMud + Dendiffex) * (1 - volfree / (volliquid + PVgain)) + gasDen * volfree / (volliquid + PVgain); //density change due to low-density gas dissolution
				dpmud = 0.052 * avgden * (xbvdcell - xvdcell);

				Pbottom_eff = pxcell + dpmud;
				Err2 = (MainDriver.Pform - Pbottom_eff) / MainDriver.Pform;
				if (f1 * Err2 < 0){        //BiSection method
					px2 = P_kick_mid;
					//f2 = MainDriver.Pform - Pbottom_calc;
				}
				else{
					px1 = P_kick_mid; 
					//f1 = MainDriver.Pform - Pbottom_calc;
				}					

				if(P_kick_mid < 14.6){
					P_kick_mid = 14.7;
				}
				else if(P_kick_mid>MainDriver.Pform){
					P_kick_mid = MainDriver.Pform;
				}
				Err2 = Math.abs(MainDriver.Pform - Pbottom_eff) / MainDriver.Pform;					
			}
			Err = holeMD - xbcell;
			if(Err>0){
				left = xcell;
			}
			else if(Err<0){
				right =xcell;
			}
		}
		PVgain = volsol+volfree;
		return PVgain;
	}

	static double[] get_mlDelVD(int iML, double volGiven){//  0 : delMD, 1: delVD, 2: angleTop
		//   sub to calculate del_MD, del_VD, and angle from the bottom of ML
		//   You can call this twice to calc. any del_VD in the ML trajectory
		//   iML:i-th trajectory, volGiven in bbls, delVD in ft from the end of ML trajectory
		//   2003/7/17

		double volHold2 = 0, volBur2 = 0, volHold = 0, volBur = 0;
		double MLcap = 0, mlR2 = 0, mlRone=0;
		double angKOP = 0, angRad = 0, ang2rad = 0, rad2=0;
		double[] result = new double[3];
		double delMD=0, delVD=0, angleTop=0;

		mlRone = 18000 / (MainDriver.pai * MainDriver.mlBUR[iML]);
		if (Math.abs(MainDriver.mlBUR2nd[iML]) < 0.0002) mlR2 = 0;
		else mlR2 = 18000 / (MainDriver.pai * MainDriver.mlBUR2nd[iML]);

		angKOP = MainDriver.mlKOPang[iML] * MainDriver.radConv;
		angRad = MainDriver.mlEOB[iML] * MainDriver.radConv;
		ang2rad = MainDriver.mlEOB2nd[iML] * MainDriver.radConv;
		//
		MLcap = Math.pow(MainDriver.mlDia[iML], 2) / 1029.4;
		delMD = volGiven / MLcap;
		volHold2 = MainDriver.mlHold2nd[iML] * MLcap;
		volBur2 = volHold2 + mlR2 * (ang2rad - angRad) * MLcap;
		volHold = volBur2 + MainDriver.mlHold[iML] * MLcap;
		volBur = volHold + mlRone * (angRad - angKOP) * MLcap;

		if(volGiven < volHold2){
			delVD = delMD * Math.cos(ang2rad);
			angleTop = MainDriver.mlEOB2nd[iML];       //7/30/2003, needed for well kill op.
		}
		else if(volGiven < volBur2){
			rad2 = ang2rad - (delMD - MainDriver.mlHold2nd[iML]) / mlR2;
			delVD = MainDriver.mlHold2nd[iML] * Math.cos(ang2rad) + mlR2 * (Math.sin(ang2rad) - Math.sin(rad2));
			angleTop = rad2 / MainDriver.radConv;
		}
		else if (volGiven < volHold){
			delVD = MainDriver.mlHold2nd[iML] * Math.cos(ang2rad) + mlR2 * (Math.sin(ang2rad) - Math.sin(angRad));
			delVD = delVD + (volGiven - volBur2) / MLcap * Math.cos(angRad);
			angleTop = MainDriver.mlEOB[iML];
		}
		else if (volGiven < volBur) {
			rad2 = angRad - (volGiven - volHold) / MLcap / mlRone;
			delVD = MainDriver.mlHold2nd[iML] * Math.cos(ang2rad) + mlR2 * (Math.sin(ang2rad) - Math.sin(angRad));
			delVD = delVD + MainDriver.mlHold[iML] * Math.cos(angRad) + mlRone * (Math.sin(angRad) - Math.sin(rad2));
			angleTop = rad2 / MainDriver.radConv;
		}
		else{
			delVD = MainDriver.mlTVD[iML] - MainDriver.mlKOPvd[iML];
			angleTop = MainDriver.mlKOPang[iML];
		}

		result[0]=delMD;
		result[1]=delVD;
		result[2]=angleTop;

		return result;
	}

	class CheckTask extends TimerTask{				
		public void run(){
			if(MainDriver.MenuSelected==0){
				setVisible(false);
				MainDriver.iWshow=0;
				Sm1.m3.setVisible(false);
				menuClose();
				TimerIPOn=0;
				MainDriver.MenuSelected=2;
				TimerCheckIP.cancel();
			}
			else if(MainDriver.MenuSelected==1){
				setVisible(true);
				TimerIPOn=0;
				MainDriver.MenuSelected=2;
				TimerCheckIP.cancel();
			} // o: to the main menu, 1: to the simulation panel.)				
		}
	}

	class CheckBlowoutTask extends TimerTask{
		public void run(){
			if(MainDriver.BlowOutOccurred==1){
				setVisible(false);
				MainDriver.iWshow=0;
				Sm1.m3.setVisible(false);
				menuClose();
				if(TimerBOOn==1){
					TimerBOOn=-1;
					TimerCheckBO.cancel();
				}					
			}
		}
	}

	class CheckRPTask extends TimerTask{
		public void run(){
			if(MainDriver.RPMenuSelected==0){
				setVisible(false);
				MainDriver.iWshow=0;
				Sm1.m3.setVisible(false);
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

	static class textLoad extends JFrame {

		private JPanel contentPane;
		JPanel contentPane2;
		JPanel contentPane3;
		TextArea textArea = new TextArea("", 10000, 10000, TextArea.SCROLLBARS_BOTH);
		JButton OKbutton = new JButton("OK");
		JButton Applybutton = new JButton("Apply");
		JButton Cancelbutton = new JButton("Cancel");

		textLoad(String title) {
			setTitle(title);
			setIconImage(MainDriver.icon.getImage());
			addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{		            	
					e.getWindow().dispose();
					e.getWindow().setVisible(false);
					menuClose();
				}
			});		

			addComponentListener(new ComponentAdapter() {			
				public void componentResized(ComponentEvent arg0) {
					contentPane.setBounds(0, 0, getWidth(), getHeight());
					contentPane2.setBounds(0, 0, contentPane.getWidth()-20, getHeight()-100);//735
					contentPane3.setBounds(0, getHeight()-100, contentPane.getWidth()-20, 60);
				}
			});

			setBounds(100, 100, 750, 430);
			contentPane = new JPanel();
			contentPane.setBounds(0, 0, getWidth(), getHeight());
			contentPane.setLayout(null);
			setContentPane(contentPane);

			contentPane2 = new JPanel();
			contentPane2.setBounds(0, 0, contentPane.getWidth()-20, getHeight()-100);//735
			getContentPane().add(contentPane2);
			contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane2.setLayout(new BorderLayout(0,0));
			textArea.setBounds(0, 0, contentPane2.getWidth(), contentPane2.getHeight()-30);
			textArea.setFont(new Font("COURIER", Font.PLAIN, 12));
			contentPane2.add(textArea);

			contentPane3 = new JPanel();
			contentPane3.setBounds(0, getHeight()-100, contentPane.getWidth()-20, 60);
			getContentPane().add(contentPane3);

			textArea.setFont(new Font("COURIER", Font.PLAIN, 12));
			contentPane3.setLayout(null);

			OKbutton.setBounds(contentPane.getWidth()-330, 15, 90, 30);				
			Applybutton.setBounds(contentPane.getWidth()-230, 15, 90, 30);
			Cancelbutton.setBounds(contentPane.getWidth()-130, 15, 90, 30);
			contentPane3.add(OKbutton);
			contentPane3.add(Applybutton);
			contentPane3.add(Cancelbutton);
		}

		void menuClose(){				
			if(TimerWarnOn==1 && SIDTimerWarn==1 && WarnTaskOn == 0){
				iTimerWarn = 0;
				WarnTaskOn=1;
			}
			if (TimerDrillOn==1 &&SIDTimerDrill==1 && TDTaskOn==0){
				iTimerDrill = 0;
				TDTaskOn=1;
			}
			if (TimerSIOn==1 && SIDTimerShutin==1 && SITaskOn==0){
				iTimerShutin = 0; 
				SITaskOn=1;
				MainDriver.SItaskOn2=1;
			}
			if(iROP == 0 && SIDSa1TRTaskOn==1 && SIDSm1TRTaskOn==1) {
				Sm1.m3.TRTaskOn=1;
				Sa1.m3.TRTaskOn=1;
			}
			this.dispose();
			this.setVisible(false);
		}						
	}
}