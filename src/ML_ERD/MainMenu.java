package ML_ERD;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextArea;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

class MainMenu extends JFrame {

	private JPanel contentPane;	
	int dummy=0;
	simdis sim0;
	simdis sim_temp;
	int sim0AccumPrevent = 0;	
	textResult txtR;		
	resultPlot rp;
	inputData ip;
	Timer setVisibleTimer;
	int svTimerOn=0;
	int btnSrtX=10;
	int btnSrtY=10;
	int btnSizeX=400;
	int btnSizeY=60;
	int btnSizeX2=320;
	int btnSizeY2=35;
	int intvY = 20;
	int intvY2 =5;
	int pnlSizeX=500;
	int pnlSizeY=420;
	int pnlSrtX = btnSrtX*4+btnSizeX;
	int pnlSrtY = btnSrtY;
	int InfoSizeX = pnlSizeX;
	int InfoSizeY = 240;
	int InfoSrtX = pnlSrtX;
	int InfoSrtY = pnlSrtY+pnlSizeY+intvY2;
	int basicSizeX = btnSrtX*4+btnSizeX;
	//int basicSizeY = btnSrtY*8+btnSizeY*4+btnSizeY2*3+intvY*3+intvY2*3+btnSizeY+intvY;
	int basicSizeY = InfoSrtY+InfoSizeY+btnSrtY+60;
	int changeSizeX = pnlSrtX+pnlSizeX+btnSrtX+15;
	int changeSizeY = InfoSrtY+InfoSizeY+btnSrtY+60;

	JButton btnCase = new JButton("Select a Simulation Case");
	JButton btnSrtSim = new JButton("Run Drilling Simulation");
	JButton btnRunCase = new JButton("Run Well-control Simulation");
	JButton btnInputData = new JButton("Change Input Data");
	JButton btnAnalSim = new JButton("Analyze the Simulation");
	JButton btnAnalysis = new JButton("Analysis on Your Performance");
	JButton btnResultPlot = new JButton("Results in Plots");
	JButton btnResultInText = new JButton("Results in Text");
	JButton btnExit = new JButton("Stop the Program");

	JPanel pnlCase = new JPanel();
	JPanel pnlCase2 = new JPanel();

	JLabel lblSelectOneCase = new JLabel("Select a Simulation Case for Drilling and Well-Control");
	private JTextField txtTVD;
	private JTextField txtHD;
	private JTextField txtTMD;
	private JTextField txtPVgain;
	private JTextField txtSIDPP;
	private JTextField txtSICP;
	private JTextField txtCMW;
	private JTextField txtKillPump;
	private JTextField txtSPP;
	private JTextField txtStrokes;

	ButtonGroup ModeGroup = new ButtonGroup();
	JRadioButton optMode1 = new JRadioButton("Select from a Given Data Set");
	JRadioButton optMode2 = new JRadioButton("Use a Defined Case(Always Start from Drilling)");

	ButtonGroup OperationGroup = new ButtonGroup();
	JRadioButton optOper1 = new JRadioButton("Drilling and Well-control");
	JRadioButton optOper2 = new JRadioButton("Direct Well-control");

	ButtonGroup CaseGroup_dr = new ButtonGroup();
	JRadioButton optCase1_dr = new JRadioButton("Onshore Vertical Well");
	JRadioButton optCase2_dr = new JRadioButton("Onshore Directional Well");
	JRadioButton optCase3_dr = new JRadioButton("Onshore Horizontal Well");
	JRadioButton optCase4_dr = new JRadioButton("Offshore Shallow Water Depth, Vertical Well");
	JRadioButton optCase5_dr = new JRadioButton("Offshore Deep Water Depth, Vertical Well");
	JRadioButton optCase7_dr = new JRadioButton("Offshore Shallow Water Depth, Directional Well");
	JRadioButton optCase8_dr = new JRadioButton("Offshore Deep Water Depth, Directional Well");
	JRadioButton optCase9_dr = new JRadioButton("Offshore Shallow Water Depth, Horizontal Well");
	JRadioButton optCase10_dr = new JRadioButton("Offshore Ultra-deep Water Depth, Horizontal Well");

	ButtonGroup CaseGroup_wc = new ButtonGroup();
	JRadioButton optCase1_wc = new JRadioButton("Onshore Vertical Well with a Small Kick");
	JRadioButton optCase2_wc = new JRadioButton("Onshore Directional Well with a Large Kick");
	JRadioButton optCase3_wc = new JRadioButton("Onshore Horizontal Well with a Small Kick");
	JRadioButton optCase4_wc = new JRadioButton("Offshore Shallow Water Depth, Vertical Well with a Small Kick");
	JRadioButton optCase5_wc = new JRadioButton("Offshore Deep Water Depth, Vertical Well with a Small Kick");
	JRadioButton optCase6_wc = new JRadioButton("Offshore Deep Water Depth, Vertical Well with a Large Kick");
	JRadioButton optCase7_wc = new JRadioButton("Offshore Shallow Water Depth, Directional Well with a Small Kick");
	JRadioButton optCase8_wc = new JRadioButton("Offshore Shallow Water Depth, Directional Well with a Large Kick");
	JRadioButton optCase9_wc = new JRadioButton("Offshore Shallow Water Depth, Horizontal Well with a Small Kick");
	JRadioButton optCase10_wc = new JRadioButton("Offshore Ultra-deep Water Depth, Horizontal Well with a Small Kick");

	//JButton btnCaseSelect_wc = new JButton("For Well-control");
	//JButton btnCaseSelect_dr = new JButton("For Drilling");

	JPanel pnlOption = new JPanel();
	int lblModeXsrt=5, lblModeYsrt=5, lblModeXsize=180, lblModeYsize=23, lblModeYintv=2;

	int lblcaseXsize = 440;
	int lblcaseYsize = 23;
	int lblcaseXsrt = 40;
	int lblcaseYsrt = 5;
	int lblcaseYintv = 2;

	double[] getDPinsideEX = new double[2];
	wellpic wp = new wellpic();
	JTextField txtWD;
	
	JPanel pnlCaseInfor = new JPanel();
	JLabel lblKickInfor = new JLabel("Operation Related Information");	
	JLabel lblWellInfor = new JLabel("Well Related Information");
	JLabel lblTVD = new JLabel("ft, Total Vertical Depth");
	JLabel lblHD = new JLabel("ft, Horizontal Distance");
	JLabel lblFtTotalMeasured = new JLabel("ft, Total Measured Depth");
	JLabel lblPVgain = new JLabel("bbls, Pit Volume Gain");
	JLabel lblSIDPP = new JLabel("psig, SIDPP");
	JLabel lblSICP = new JLabel("psig, SICP");
	JLabel lblKMW = new JLabel("ppg, Current Mud Weight");
	JLabel lblKillPump = new JLabel("gpm, Kill Pump Rate");
	JLabel lblSPP = new JLabel("psig, SPP");
	JLabel lblStrokes = new JLabel("# of Strokes to the Bit");
	JLabel lblWD = new JLabel("ft, Water Depth");
	
	FrmAnalysis FrmA;

	MainMenu() {

		MainDriver.FrmAnalysisOn = 0;
		MainDriver.plot=0;
		MainDriver.MainMenuVisible=0;
		setTitle("Main Menu: Designed by J. Choe");
		setIconImage(MainDriver.icon.getImage());
		btnSrtSim.setVisible(true);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				e.getWindow().dispose();
				e.getWindow().setVisible(false);
				btnSrtSim.setEnabled(true);
				menuClose();
			}
		});



		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 0, basicSizeX, basicSizeY);
		//setBounds(200, 0, changeSizeX, changeSizeY);
		MainDriver.iCase = 1;
		MainDriver.iCaseSelection = 0; //140708 AJW		

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Show");
		menuBar.add(mnNewMenu);

		JMenuItem mntmTrajectory = new JMenuItem("Trajectory");
		mntmTrajectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sim0.m5.setVisible(true);
			}
		});
		mnNewMenu.add(mntmTrajectory);


		JMenuItem mntmWellbore = new JMenuItem("Wellbore");
		mntmWellbore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(MainDriver.iWshow == 0){
					//wp = new wellpic();
					wp.form_Load();
					wp.setVisible(true);
					MainDriver.iWshow = 1;
				}				
			}
		});
		mnNewMenu.add(mntmWellbore);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "This program is carefully designed for easy use.  Use your common senses.";
				msg = msg + "\n" +"  Click once to activate menus and commands, unless otherwise specified.";
				msg = msg + "\n" +"  If you have any warning sign and ignore it, you can have unexpected results.";
				msg = msg + "\n" +"  This program has default 10 default simulation cases for easy simulation run.";
				msg = msg + "\n" +"  Therefore, you can directly run the program by clicking the Select";
				msg = msg + "\n" +"  the Simulation Case command button, selecting one case, and clicking";
				msg = msg + "\n" +"  the Run the Case command button. All plots are available after executing";
				msg = msg + "\n" +"  the program by clicking the Results in Plots command button.";
				msg = msg + "\n" +"  You can check your performance of the well control by clicking";
				msg = msg + "\n" +"  the Analysis on Your Performance command button.";
				JOptionPane.showMessageDialog(null, msg, "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmHelp);

		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnSrtSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MainDriver.ManualOn==0 && MainDriver.drillSimOn==0 && MainDriver.AutoOn==0 && MainDriver.inputOn==0){
					initialize();
					MainDriver.iCaseSelection = 1;

					if(MainDriver.icase_mode==0){
						if(MainDriver.icase_selected==0){
							if(MainDriver.iCase == 1){
								MainModule.DefaultData_case1_dr();    //Set Default variable
							}
							else if(MainDriver.iCase == 2){
								MainModule.DefaultData_case2_dr();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 3){
								MainModule.DefaultData_case3_dr();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 4){
								MainModule.DefaultData_case4_dr();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 5){
								MainModule.DefaultData_case5_dr();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 7){
								MainModule.DefaultData_case7_dr();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 8){
								MainModule.DefaultData_case8_dr();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 9){
								MainModule.DefaultData_case9_dr();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 10){
								MainModule.DefaultData_case10_dr();    //Set MainModule.Default variable
							}
						}
						if(MainDriver.icase_selected==11){
							if(MainDriver.iCase == 1){
								MainModule.DefaultData_case1_wc();    //Set Default variable
							}
							else if(MainDriver.iCase == 12){
								MainModule.DefaultData_case2_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 13){
								MainModule.DefaultData_case3_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 14){
								MainModule.DefaultData_case4_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 15){
								MainModule.DefaultData_case5_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 16){
								MainModule.DefaultData_case6_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 17){
								MainModule.DefaultData_case7_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 18){
								MainModule.DefaultData_case8_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 19){
								MainModule.DefaultData_case9_wc();    //Set MainModule.Default variable
							}
							else if(MainDriver.iCase == 20){
								MainModule.DefaultData_case10_wc();    //Set MainModule.Default variable
							}
						}
					}

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						wp.dispose();
						MainDriver.iWshow = 0;
					}


					utilityModule.OBM_Composition_SK();				
					if(MainDriver.ManualOn==1){
						sim0.Sm1.setVisible(true);
					}
					else if(MainDriver.AutoOn==1){
						sim0.Sa1.setVisible(true);
					}				
					else if(MainDriver.drillSimOn==1){
						sim0.setVisible(true);
					}

					else if(MainDriver.drillSimOn==0){
						MainModule.getGeometry();
						MainModule.SetControl();
						MainDriver.iKsheet=1;	
						sim0 = new simdis();
						sim0.Form_load();
						if(MainDriver.iHresv==1) sim0.TimeDrlIntv=5000;//È÷È÷simdis.TimeDrlIntv=2000; //=2000 ajw 140217 ±³¼ö´Ô Áö½Ã·Î ¼öÁ¤, 2000ÀÌ ¿ø·¡ ¸ÂÀ½.
						else sim0.TimeDrlIntv=5000;//È÷È÷simdis.TimeDrlIntv=5000; //=5000 ajw 140217 ±³¼ö´Ô Áö½Ã·Î ¼öÁ¤.
						sim0AccumPrevent=1;
						MainDriver.drillSimOn=1;
						sim0.setVisible(true);
					}

					MainDriver.set0_Starttime = 0;
					MainDriver.set0_Finishtime = 0;

					optMode1.setEnabled(false);
					optMode2.setEnabled(false);
					optOper1.setEnabled(false);
					optOper2.setEnabled(false);

					optCase1_wc.setEnabled(false);
					optCase2_wc.setEnabled(false);
					optCase3_wc.setEnabled(false);
					optCase4_wc.setEnabled(false);
					optCase5_wc.setEnabled(false);
					optCase6_wc.setEnabled(false);
					optCase7_wc.setEnabled(false);
					optCase8_wc.setEnabled(false);
					optCase9_wc.setEnabled(false);
					optCase10_wc.setEnabled(false);

					optCase1_dr.setEnabled(false);
					optCase2_dr.setEnabled(false);
					optCase3_dr.setEnabled(false);
					optCase4_dr.setEnabled(false);
					optCase5_dr.setEnabled(false);
					optCase7_dr.setEnabled(false);
					optCase8_dr.setEnabled(false);
					optCase9_dr.setEnabled(false);
					optCase10_dr.setEnabled(false);
				}

				else{
					if(MainDriver.ManualOn==1){
						sim0.Sm1.setVisible(true);
					}
					else if(MainDriver.AutoOn==1){
						sim0.Sa1.setVisible(true);
					}				
					else if(MainDriver.drillSimOn==1){
						sim0.setVisible(true);
					}
				}

			}
		});


		btnCase.setBounds(btnSrtX, btnSrtY, btnSizeX, btnSizeY);
		btnInputData.setBounds(btnSrtX, btnSrtY+btnSizeY+intvY, btnSizeX, btnSizeY);		
		btnSrtSim.setBounds(btnSrtX, btnSrtY+2*(btnSizeY+intvY), btnSizeX, btnSizeY);
		btnRunCase.setBounds(btnSrtX, btnSrtY+3*(btnSizeY+intvY), btnSizeX, btnSizeY);		
		btnAnalSim.setBounds(btnSrtX, btnSrtY+4*(btnSizeY+intvY), btnSizeX, btnSizeY);		
		btnAnalysis.setBounds(btnSizeX+btnSrtX-btnSizeX2, btnSrtY+btnSizeY*5+intvY*4+intvY2, btnSizeX2, btnSizeY2);
		btnResultPlot.setBounds(btnSizeX+btnSrtX-btnSizeX2, btnSrtY+btnSizeY*5+intvY*4+btnSizeY2*1+2*intvY2, btnSizeX2, btnSizeY2);		
		btnResultInText.setBounds(btnSizeX+btnSrtX-btnSizeX2, btnSrtY+btnSizeY*5+intvY*4+btnSizeY2*2+3*intvY2, btnSizeX2, btnSizeY2);		
		btnExit.setBounds(btnSrtX, btnSrtY+btnSizeY*5+intvY*5+btnSizeY2*3+3*intvY2, btnSizeX, btnSizeY);		

		btnCase.setFont(new Font("±¼¸²", Font.BOLD, 25));
		btnSrtSim.setFont(new Font("±¼¸²", Font.BOLD, 25));
		btnRunCase.setFont(new Font("±¼¸²", Font.BOLD, 25));
		btnInputData.setFont(new Font("±¼¸²", Font.BOLD, 25));
		btnAnalSim.setFont(new Font("±¼¸²", Font.BOLD, 25));
		btnAnalysis.setFont(new Font("±¼¸²", Font.BOLD, 18));
		btnResultPlot.setFont(new Font("±¼¸²", Font.BOLD, 18));
		btnResultInText.setFont(new Font("±¼¸²", Font.BOLD, 18));
		btnExit.setFont(new Font("±¼¸²", Font.BOLD, 25));

		contentPane.add(btnCase);	
		contentPane.add(btnSrtSim);
		contentPane.add(btnRunCase);
		contentPane.add(btnInputData);
		contentPane.add(btnAnalSim);
		contentPane.add(btnAnalysis);
		contentPane.add(btnResultPlot);
		contentPane.add(btnResultInText);
		contentPane.add(btnExit);





		/*btnResultInPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dummy=MainModule.SaveAsFile();				
			}
		});*/ //In the offline mode, this program can produce the result text file. But in the online environment, due to the internet security, applet programs can not make any files in clients' computers.


		btnResultInText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				txtR = new textResult("Results in text");
				dummy = MainModule.SaveInTextArea(txtR);
				txtR.setVisible(true);
				//dummy=MainModule.SaveAsFile();	
			}
		});

		btnResultPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MainDriver.plot==0){
					rp = new resultPlot();
					rp.menuSimulation.setEnabled(false);
					rp.setVisible(true);
					MainDriver.plot=1;
				}
			}
		});

		btnInputData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0 && MainDriver.drillSimOn==0 && MainDriver.AutoOn==0 && MainDriver.inputOn==0){
					MainDriver.inputOn=1;
					ip = new inputData();
					ip.setVisible(true);
					ip.menuToSimulation.setEnabled(false);
					ip.menuMain.setEnabled(true);
					
					optMode1.setEnabled(false);
					optMode2.setEnabled(false);
					optOper1.setEnabled(false);
					optOper2.setEnabled(false);
					
					optCase1_wc.setEnabled(false);
					optCase2_wc.setEnabled(false);
					optCase3_wc.setEnabled(false);
					optCase4_wc.setEnabled(false);
					optCase5_wc.setEnabled(false);
					optCase6_wc.setEnabled(false);
					optCase7_wc.setEnabled(false);
					optCase8_wc.setEnabled(false);
					optCase9_wc.setEnabled(false);
					optCase10_wc.setEnabled(false);

					optCase1_dr.setEnabled(false);
					optCase2_dr.setEnabled(false);
					optCase3_dr.setEnabled(false);
					optCase4_dr.setEnabled(false);
					optCase5_dr.setEnabled(false);
					optCase7_dr.setEnabled(false);
					optCase8_dr.setEnabled(false);
					optCase9_dr.setEnabled(false);
					optCase10_dr.setEnabled(false);
				}
				else{
					ip.setVisible(true);
				}
			}
		});

		btnInputData.setVisible(true);


		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				btnSrtSim.setEnabled(true);
				menuClose();
			}
		});


		btnCase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0 && MainDriver.drillSimOn==0 && MainDriver.AutoOn==0 && MainDriver.inputOn==0){
					initialize();	
					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}
					btnInputData.setEnabled(true);
					btnSrtSim.setEnabled(true);
					
					MainModule.DefaultData_case1_dr();
					MainDriver.iCase = 1;
					MainDriver.iCaseSelection = 0;			
					optMode1.setSelected(true);
					optOper1.setSelected(true);
					optCase1_dr.setSelected(true);
					
					setBounds(200, 0, changeSizeX, changeSizeY);

					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case1_dr();
					
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblSICP.setText("psig, SICP");
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;

					MainDriver.icase_mode=0;
					MainDriver.icase_selected=0;
					
					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					optMode1.setEnabled(true);
					optMode2.setEnabled(true);
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);
					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optMode1.setSelected(true);
					optOper1.setSelected(true);					
					optCase1_dr.setSelected(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);

				}
			}
		});
		// for drilling

		optCase1_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=1;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case1_dr();
					//MainModule.DefaultData_Comparison();
					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Nagasawa2();

					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					//lblSICP.setText("psig, SPP");
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					
				}
			}
		});

		optCase2_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=2;
					MainModule.DefaultData_case2_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Comparison();
					//MainModule.DefaultData_Nagasawa2();
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					//lblStrokes.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase3_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=3;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case3_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase4_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=4;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case4_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase5_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=5;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case5_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});


		optCase7_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}					

					MainDriver.iCase=7;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case7_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}

			}
		});

		optCase8_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}


					MainDriver.iCase=8;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case8_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					
					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase9_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=9;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case9_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase10_dr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=10;

					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case10_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		//

		// for well control
		optCase1_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=11;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case1_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Nagasawa2();

					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					
					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase2_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=12;
					MainModule.DefaultData_case2_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Comparison();
					//MainModule.DefaultData_Nagasawa2();
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					
					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase3_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=13;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case3_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase4_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=14;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case4_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase5_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=15;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case5_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase6_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=16;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case6_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					//MainModule.DefaultData_test();
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}

			}
		});

		optCase7_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}					

					MainDriver.iCase=17;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case7_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}

			}
		});

		optCase8_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}


					MainDriver.iCase=18;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case8_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase9_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=19;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case9_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optCase10_wc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=20;

					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case10_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					
					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));
					

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		//
		wp.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent arg0) {
				wp.widthX = wp.getWidth();
				wp.heightX = wp.getHeight();
				wp.form_Load();
				wp.pp.setBounds(0, 0, wp.widthX-wp.frameXintvSize*2, wp.heightX-wp.frameYintvSize1-wp.frameYintvSize2);
			}
		});	


		btnRunCase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0 && MainDriver.drillSimOn==0 && MainDriver.AutoOn==0 && MainDriver.inputOn==0){
					initialize();
					MainDriver.iCaseSelection = 1;

					if(MainDriver.icase_selected==0){
						if(MainDriver.iCase == 1){
							MainModule.DefaultData_case1_dr();    //Set Default variable
							optCase1_dr.setSelected(true);
						}
						else if(MainDriver.iCase == 2){
							MainModule.DefaultData_case2_dr();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 3){
							MainModule.DefaultData_case3_dr();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 4){
							MainModule.DefaultData_case4_dr();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 5){
							MainModule.DefaultData_case5_dr();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 7){
							MainModule.DefaultData_case7_dr();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 8){
							MainModule.DefaultData_case8_dr();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 9){
							MainModule.DefaultData_case9_dr();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 10){
							MainModule.DefaultData_case10_dr();    //Set MainModule.Default variable
						}
					}

					else if(MainDriver.icase_selected==1){ // direct well control
						if(MainDriver.iCase == 11){
							MainModule.DefaultData_case1_wc();    //Set Default variable
							optCase1_wc.setSelected(true);
						}
						else if(MainDriver.iCase == 12){
							MainModule.DefaultData_case2_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 13){
							MainModule.DefaultData_case3_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 14){
							MainModule.DefaultData_case4_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 15){
							MainModule.DefaultData_case5_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 16){
							MainModule.DefaultData_case6_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 17){
							MainModule.DefaultData_case7_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 18){
							MainModule.DefaultData_case8_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 19){
							MainModule.DefaultData_case9_wc();    //Set MainModule.Default variable
						}
						else if(MainDriver.iCase == 20){
							MainModule.DefaultData_case10_wc();    //Set MainModule.Default variable
						}
					}

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						wp.dispose();
						MainDriver.iWshow = 0;
					}

					MainModule.getGeometry();
					MainModule.setMDvd();			    
					MainModule.getGeometry();
					MainModule.SetControl();

					MainDriver.iKsheet = 1;

					sim0 = new simdis();
					sim0.Case_Load();
					sim0.setVisible(false);

					sim0.m5.setVisible(false);
					sim0.m5.dispose();

					//................ After retriving shutin data, operation conditions can be different (including CL, KL selections)
					MainDriver.iBOP = 0;   //BOP is closed for calculation purpose: needed for shutin data retrieval
					sim0.CalChkVol();  //calculate inside choke volume and total outside volume

					//------------------// modofication to match Nickens// results
					//...... 99 psi for k = 2500 md, 82.5 psif for k = 300 md, 52.5 psi for k = 50 md
					//				    pb = 0.052 * Kmud * Vdepth + 14.7 //+ 52.5, BUT, need to set back after the comparison  //7/11/02
					//				                                               //to match with Nickens results !
					//
					//
					sim0.Sm1.m3.paintIndex2=0; // 20130917 ajw
					sim0.Sa1.m3.paintIndex2=0;

					MainDriver.imode = 1;
					sim0.Sm1.Form_Load();
					MainDriver.iCHKcontrol = 1;
					MainDriver.imode = 2;					
					sim0.Sm1.AutoSelect.setSelected(true);
					//Sm1.txtSIMmode.setText("Manual");
					MainDriver.Method = 1;    //always start with Driller//s Method
					MainDriver.gMudCirc = MainDriver.gMudOld;
					MainDriver.Cmud = MainDriver.oMud;
					sim0.Sm1.setVisible(true);			    	
					sim0.Sa1.dispose();
					MainDriver.AutoOn=0;
					MainDriver.ManualOn=1;			    	

					//Unload DrillSI    //unload for the several runs		    
					sim0.menuClose();

					MainDriver.set0_Starttime = 0;
					MainDriver.set0_Finishtime = 0;
					
					optMode1.setEnabled(false);
					optMode2.setEnabled(false);
					optOper1.setEnabled(false);
					optOper2.setEnabled(false);
					
					optCase1_dr.setEnabled(false);
					optCase2_dr.setEnabled(false);
					optCase3_dr.setEnabled(false);
					optCase4_dr.setEnabled(false);
					optCase5_dr.setEnabled(false);
					optCase7_dr.setEnabled(false);
					optCase8_dr.setEnabled(false);
					optCase9_dr.setEnabled(false);
					optCase10_dr.setEnabled(false);

					optCase1_wc.setEnabled(false);
					optCase2_wc.setEnabled(false);
					optCase3_wc.setEnabled(false);
					optCase4_wc.setEnabled(false);
					optCase5_wc.setEnabled(false);
					optCase6_wc.setEnabled(false);
					optCase7_wc.setEnabled(false);
					optCase8_wc.setEnabled(false);
					optCase9_wc.setEnabled(false);
					optCase10_wc.setEnabled(false);
				}
				
				else{
					sim0.Sm1.setVisible(true);	
				}
			}
		});		


		pnlCase.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		pnlCase.setBounds(pnlSrtX, pnlSrtY, pnlSizeX, pnlSizeY);
		contentPane.add(pnlCase);
		pnlCase.setLayout(null);
		lblSelectOneCase.setFont(new Font("±¼¸²", Font.BOLD, 18));
		lblSelectOneCase.setBounds(10, 10, pnlSizeX-20, 35);
		lblSelectOneCase.setHorizontalAlignment(SwingConstants.CENTER);
		pnlOption.setBounds(lblModeXsrt*8, 55, pnlSizeX-16*lblModeXsrt, lblModeYsrt+(lblModeYsize+lblModeYintv)*3+lblModeYintv);
		pnlOption.setLayout(null);
		pnlOption.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlCase2.setBounds(5, 45+lblModeYsrt+(lblModeYsize+lblModeYintv)*4+lblModeYintv, lblcaseXsrt+lblcaseXsize, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*10);
		pnlCase2.setLayout(null);

		pnlCase.add(lblSelectOneCase);	
		pnlCase.add(pnlCase2);
		pnlCase.add(pnlOption);
		pnlOption.add(optMode1);
		pnlOption.add(optMode2);
		pnlOption.add(optOper1);
		pnlOption.add(optOper2);

		optMode1.setFont(new Font("±¼¸²", Font.BOLD, 13));
		optMode2.setFont(new Font("±¼¸²", Font.BOLD, 13));
		optOper1.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		optOper2.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		optMode1.setBounds(lblModeXsrt, lblModeYsrt, lblModeXsize*2, lblModeYsize);
		optOper1.setBounds((pnlSizeX-16*lblModeXsrt)/2-(lblModeXsize+lblModeYintv*10), lblModeYsrt+lblModeYsize, lblModeXsize, lblModeYsize);
		optOper2.setBounds((pnlSizeX-16*lblModeXsrt)/2+lblModeYintv*10, lblModeYsrt+lblModeYsize, lblModeXsize, lblModeYsize);
		optMode2.setBounds(lblModeXsrt, lblModeYsrt+(lblModeYsize+lblModeYintv)*2+lblModeYintv, lblModeXsize*2, lblModeYsize);

		ModeGroup.add(optMode1);
		ModeGroup.add(optMode2);
		OperationGroup.add(optOper1);
		OperationGroup.add(optOper2);

		optMode1.setSelected(true);
		optOper1.setSelected(true);

		// drilling cases
		optCase1_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase1_dr.setBounds(lblcaseXsrt, lblcaseYsrt, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase1_dr);

		optCase2_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase2_dr.setBounds(lblcaseXsrt, lblcaseYsrt+lblcaseYsize+lblcaseYintv, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase2_dr);

		optCase3_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase3_dr.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*2, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase3_dr);

		optCase4_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase4_dr.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*3, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase4_dr);

		optCase5_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase5_dr.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*4, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase5_dr);

		optCase7_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase7_dr.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*5, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase7_dr);

		optCase8_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase8_dr.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*6, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase8_dr);

		optCase9_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase9_dr.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*7, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase9_dr);

		optCase10_dr.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase10_dr.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*8, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase10_dr);

		CaseGroup_dr.add(optCase1_dr);
		CaseGroup_dr.add(optCase2_dr);
		CaseGroup_dr.add(optCase3_dr);
		CaseGroup_dr.add(optCase4_dr);
		CaseGroup_dr.add(optCase5_dr);
		CaseGroup_dr.add(optCase7_dr);
		CaseGroup_dr.add(optCase8_dr);
		CaseGroup_dr.add(optCase9_dr);
		CaseGroup_dr.add(optCase10_dr);
		//

		// well control cases
		optCase1_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase1_wc.setBounds(lblcaseXsrt, lblcaseYsrt, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase1_wc);

		optCase2_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase2_wc.setBounds(lblcaseXsrt, lblcaseYsrt+lblcaseYsize+lblcaseYintv, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase2_wc);

		optCase3_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase3_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*2, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase3_wc);

		optCase4_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase4_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*3, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase4_wc);

		optCase5_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase5_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*4, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase5_wc);

		optCase6_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase6_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*5, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase6_wc);

		optCase7_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase7_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*6, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase7_wc);

		optCase8_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase8_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*7, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase8_wc);

		optCase9_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase9_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*8, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase9_wc);

		optCase10_wc.setFont(new Font("±¼¸²", Font.PLAIN, 13));
		optCase10_wc.setBounds(lblcaseXsrt, lblcaseYsrt+(lblcaseYsize+lblcaseYintv)*9, lblcaseXsize, lblcaseYsize);
		pnlCase2.add(optCase10_wc);

		CaseGroup_wc.add(optCase1_wc);
		CaseGroup_wc.add(optCase2_wc);
		CaseGroup_wc.add(optCase3_wc);
		CaseGroup_wc.add(optCase4_wc);
		CaseGroup_wc.add(optCase5_wc);
		CaseGroup_wc.add(optCase6_wc);
		CaseGroup_wc.add(optCase7_wc);
		CaseGroup_wc.add(optCase8_wc);
		CaseGroup_wc.add(optCase9_wc);
		CaseGroup_wc.add(optCase10_wc);
		//

		optMode1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					MainDriver.icase_mode=0;
					MainDriver.icase_selected=0;
					
					lblKickInfor.setText("Operation Related Information");

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					optOper1.setSelected(true);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);

					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}
					
					optCase1_dr.setSelected(true);
					optOper1.setSelected(true);
					MainDriver.iCase=1;
					
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case1_dr();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Nagasawa2();

					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true); // # of Strokes to the bit
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true); // # of Strokes to the bit
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					lblSICP.setText("# of Strokes to the Bit");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					//lblStrokes.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					//txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}			
			}
		});

		optMode2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				if(MainDriver.ManualOn==0){
					MainDriver.icase_mode=1;
					MainDriver.icase_selected=0;
					
					lblKickInfor.setText("Kick Related Information");
					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					optOper1.setEnabled(false);
					optOper2.setEnabled(false);

					optCase1_dr.setEnabled(false);
					optCase2_dr.setEnabled(false);
					optCase3_dr.setEnabled(false);
					optCase4_dr.setEnabled(false);
					optCase5_dr.setEnabled(false);
					optCase7_dr.setEnabled(false);
					optCase8_dr.setEnabled(false);
					optCase9_dr.setEnabled(false);
					optCase10_dr.setEnabled(false);

					optCase1_wc.setEnabled(false);
					optCase2_wc.setEnabled(false);
					optCase3_wc.setEnabled(false);
					optCase4_wc.setEnabled(false);
					optCase5_wc.setEnabled(false);
					optCase6_wc.setEnabled(false);
					optCase7_wc.setEnabled(false);
					optCase8_wc.setEnabled(false);
					optCase9_wc.setEnabled(false);
					optCase10_wc.setEnabled(false);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}
					
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					
					if(MainDriver.iCase==1) MainModule.DefaultData_case1_dr();
					else if(MainDriver.iCase==2) MainModule.DefaultData_case2_dr();
					else if(MainDriver.iCase==3) MainModule.DefaultData_case3_dr();
					else if(MainDriver.iCase==4) MainModule.DefaultData_case4_dr();
					else if(MainDriver.iCase==5) MainModule.DefaultData_case5_dr();
					else if(MainDriver.iCase==7) MainModule.DefaultData_case7_dr();
					else if(MainDriver.iCase==8) MainModule.DefaultData_case8_dr();
					else if(MainDriver.iCase==9) MainModule.DefaultData_case9_dr();
					else if(MainDriver.iCase==10) MainModule.DefaultData_case10_dr();
					else{
						optCase1_dr.setSelected(true);
						optOper1.setSelected(true);
						MainDriver.iCase=1;
						MainModule.DefaultData_case1_dr();						
					}
					
					if(MainDriver.imud==1) utilityModule.OBM_Composition_SK();
					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Nagasawa2();

					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	
					

					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblSICP.setText("# of Strokes to the Bit");
					lblKMW.setText("psig, SPP");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					//lblStrokes.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					//txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));

					//double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					//txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optOper1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){
					MainDriver.icase_selected=0;

					btnSrtSim.setEnabled(true);
					btnRunCase.setEnabled(false);
					
					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);

					optCase1_wc.setEnabled(false);
					optCase2_wc.setEnabled(false);
					optCase3_wc.setEnabled(false);
					optCase4_wc.setEnabled(false);
					optCase5_wc.setEnabled(false);
					optCase6_wc.setEnabled(false);
					optCase7_wc.setEnabled(false);
					optCase8_wc.setEnabled(false);
					optCase9_wc.setEnabled(false);
					optCase10_wc.setEnabled(false);
					
					optCase1_dr.setVisible(true);
					optCase2_dr.setVisible(true);
					optCase3_dr.setVisible(true);
					optCase4_dr.setVisible(true);
					optCase5_dr.setVisible(true);
					optCase7_dr.setVisible(true);
					optCase8_dr.setVisible(true);
					optCase9_dr.setVisible(true);
					optCase10_dr.setVisible(true);

					optCase1_wc.setVisible(false);
					optCase2_wc.setVisible(false);
					optCase3_wc.setVisible(false);
					optCase4_wc.setVisible(false);
					optCase5_wc.setVisible(false);
					optCase6_wc.setVisible(false);
					optCase7_wc.setVisible(false);
					optCase8_wc.setVisible(false);
					optCase9_wc.setVisible(false);
					optCase10_wc.setVisible(false);
					
					optMode1.setSelected(true);
					optCase1_dr.setSelected(true);
					
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=1;
					
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case1_dr();
					
					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Nagasawa2();

					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblWD.setVisible(true);
					
					lblKickInfor.setVisible(true);
					lblPVgain.setVisible(true); // Mud weight
					lblSIDPP.setVisible(true); // Pump rate
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(false);
					lblStrokes.setVisible(false);
					
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(false);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(false);
					txtStrokes.setVisible(false);
					
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					
					lblKickInfor.setText("Operation Related Information");	
					lblPVgain.setText("ppg, Current Mud Weight");
					lblSIDPP.setText("gpm, Mud Pump Rate");
					lblKMW.setText("psig, SPP");
					txtCMW.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					lblSICP.setText("# of Strokes to the Bit");
					//lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					//lblSPP.setText("psig, SPP");
					//lblStrokes.setText("# of Strokes to the Bit");
					//lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.oMud));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Qdrill));
					//txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					//txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					//txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtSICP.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		optOper2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.ManualOn==0){		
					
					MainDriver.icase_selected=1;

					btnSrtSim.setEnabled(false);
					btnRunCase.setEnabled(true);

					optCase1_dr.setEnabled(false);
					optCase2_dr.setEnabled(false);
					optCase3_dr.setEnabled(false);
					optCase4_dr.setEnabled(false);
					optCase5_dr.setEnabled(false);
					optCase7_dr.setEnabled(false);
					optCase8_dr.setEnabled(false);
					optCase9_dr.setEnabled(false);
					optCase10_dr.setEnabled(false);

					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);
					
					optCase1_dr.setVisible(false);
					optCase2_dr.setVisible(false);
					optCase3_dr.setVisible(false);
					optCase4_dr.setVisible(false);
					optCase5_dr.setVisible(false);
					optCase7_dr.setVisible(false);
					optCase8_dr.setVisible(false);
					optCase9_dr.setVisible(false);
					optCase10_dr.setVisible(false);

					optCase1_wc.setVisible(true);
					optCase2_wc.setVisible(true);
					optCase3_wc.setVisible(true);
					optCase4_wc.setVisible(true);
					optCase5_wc.setVisible(true);
					optCase6_wc.setVisible(true);
					optCase7_wc.setVisible(true);
					optCase8_wc.setVisible(true);
					optCase9_wc.setVisible(true);
					optCase10_wc.setVisible(true);
					
					optCase1_wc.setSelected(true);
					
					optMode1.setSelected(true);
					initialize();

					if(MainDriver.iWshow == 1){
						wp.setVisible(false);
						MainDriver.iWshow = 0;
					}

					MainDriver.iCase=11;
					MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
					MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
					MainModule.DefaultData_case1_wc();

					// for rock composition in input data
					for(int i=0; i<5; i++){
						MainDriver.layerVDfrom[i] =MainDriver.DepthCasing;
						MainDriver.layerVDto[i] = MainDriver.Vdepth;
						MainDriver.layerROP[i] = 60;
						MainDriver.layerRPM[i] = 60;
						MainDriver.layerWOB[i] = 40;//kips

						MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;
						MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];;

						MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
						MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
						MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					}
					//MainModule.DefaultData_Nagasawa1();
					//MainModule.DefaultData_Nagasawa2();

					MainModule.getGeometry();
					MainModule.setMDvd();				
					MainModule.getGeometry();
					MainModule.SetControl();
					MainDriver.iKsheet = 1;

					sim_temp = new simdis();
					sim_temp.Case_Load();	
					getDPinsideEX = utilityModule.getDPinside(MainDriver.Pform, MainDriver.Qkill, 0);
					MainDriver.Slow_PumpP = getDPinsideEX[1] - (MainDriver.SIDPP-14.7);				
					sim_temp.menuClose();	

					lblKickInfor.setVisible(true);
					lblWellInfor.setVisible(true);
					lblTVD.setVisible(true);
					lblHD.setVisible(true);
					lblFtTotalMeasured.setVisible(true);
					lblPVgain.setVisible(true);
					lblSIDPP.setVisible(true);
					lblSICP.setVisible(true);
					lblKMW.setVisible(true);
					lblKillPump.setVisible(true);
					lblSPP.setVisible(true);
					lblStrokes.setVisible(true);
					lblWD.setVisible(true);
					
					txtTVD.setVisible(true);
					txtHD.setVisible(true);
					txtTMD.setVisible(true);
					txtWD.setVisible(true);
					txtPVgain.setVisible(true);
					txtSIDPP.setVisible(true);
					txtSICP.setVisible(true);
					txtSPP.setVisible(true);
					txtCMW.setVisible(true);
					txtKillPump.setVisible(true);
					txtSPP.setVisible(true);
					txtStrokes.setVisible(true);
					
					lblKickInfor.setText("Kick Related Information");	
					lblWellInfor.setText("Well Related Information");
					lblTVD.setText("ft, Total Vertical Depth");
					lblHD.setText("ft, Horizontal Distance");
					lblFtTotalMeasured.setText("ft, Total Measured Depth");
					lblPVgain.setText("bbls, Pit Volume Gain");
					lblSIDPP.setText("psig, SIDPP");
					lblSICP.setText("psig, SICP");
					lblKMW.setText("ppg, Current Mud Weight");
					lblKillPump.setText("gpm, Kill Pump Rate");
					lblSPP.setText("psig, SPP");
					lblStrokes.setText("# of Strokes to the Bit");
					lblWD.setText("ft, Water Depth");
					
					txtTVD.setText((new DecimalFormat("###,##0")).format(MainDriver.Vdepth)); 
					txtHD.setText((new DecimalFormat("###,##0")).format(MainDriver.Hdisp));
					txtTMD.setText((new DecimalFormat("###,##0")).format(MainDriver.TMD[MainDriver.NwcS-1]));
					txtWD.setText((new DecimalFormat("###,##0")).format(MainDriver.Dwater));
					txtPVgain.setText((new DecimalFormat("#,##0.0#")).format(MainDriver.Vpit[MainDriver.NpSi]));
					txtSIDPP.setText((new DecimalFormat("###,##0")).format(MainDriver.SIDPP - 14.7));
					txtSICP.setText((new DecimalFormat("###,##0")).format(MainDriver.SICP - 14.7));
					txtSPP.setText((new DecimalFormat("###,##0")).format(MainDriver.Slow_PumpP));
					txtCMW.setText((new DecimalFormat("#0.0#")).format(MainDriver.oMud));

					double temp = (int)(MainDriver.VOLinn * (MainDriver.spMin1 + MainDriver.spMin2) / (MainDriver.Qcapacity1 * MainDriver.spMin1 + MainDriver.Qcapacity2 * MainDriver.spMin2));
					txtStrokes.setText((new DecimalFormat("###,##0")).format(temp));
					txtKillPump.setText((new DecimalFormat("##,##0")).format(MainDriver.Qkill));

					MainDriver.temp_KMW = MainDriver.oMud + MainDriver.KICKintens;
					MainDriver.temp_ICP = MainDriver.SIDPP-14.7+MainDriver.Slow_PumpP; 
					MainDriver.temp_FCP = MainDriver.Slow_PumpP*MainDriver.temp_KMW/MainDriver.oMud;
				}
			}
		});

		
		pnlCaseInfor.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		pnlCaseInfor.setBounds(InfoSrtX, InfoSrtY, InfoSizeX, InfoSizeY);
		contentPane.add(pnlCaseInfor);
		pnlCaseInfor.setLayout(null);

		
		lblWellInfor.setFont(new Font("±¼¸²", Font.BOLD, 18));
		lblWellInfor.setBounds(12, 4, 240, 35);
		pnlCaseInfor.add(lblWellInfor);

		txtTVD = new JTextField();
		txtTVD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTVD.setBackground(Color.YELLOW);
		txtTVD.setBounds(22, 40, 64, 21);
		pnlCaseInfor.add(txtTVD);
		txtTVD.setColumns(10);

		txtHD = new JTextField();
		txtHD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHD.setColumns(10);
		txtHD.setBackground(Color.YELLOW);
		txtHD.setBounds(255, 40, 64, 21);
		pnlCaseInfor.add(txtHD);

		txtTMD = new JTextField();
		txtTMD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTMD.setColumns(10);
		txtTMD.setBackground(Color.YELLOW);
		txtTMD.setBounds(22, 65, 64, 21);
		pnlCaseInfor.add(txtTMD);

		
		lblTVD.setBounds(90, 40, 132, 21);
		pnlCaseInfor.add(lblTVD);

		
		lblHD.setBounds(323, 40, 132, 21);
		pnlCaseInfor.add(lblHD);
		
		
		lblFtTotalMeasured.setBounds(90, 65, 147, 21);
		pnlCaseInfor.add(lblFtTotalMeasured);

		
		lblKickInfor.setFont(new Font("±¼¸²", Font.BOLD, 18));
		lblKickInfor.setBounds(12, 90, 300, 35);
		pnlCaseInfor.add(lblKickInfor);

		txtPVgain = new JTextField();
		txtPVgain.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPVgain.setColumns(10);
		txtPVgain.setBackground(Color.YELLOW);
		txtPVgain.setBounds(22, 126, 64, 21);
		pnlCaseInfor.add(txtPVgain);

		
		lblPVgain.setBounds(90, 126, 140, 21);
		pnlCaseInfor.add(lblPVgain);

		txtSIDPP = new JTextField();
		txtSIDPP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSIDPP.setColumns(10);
		txtSIDPP.setBackground(Color.YELLOW);
		txtSIDPP.setBounds(255, 126, 64, 21);
		pnlCaseInfor.add(txtSIDPP);
		
		lblSIDPP.setBounds(323, 126, 132, 21);
		pnlCaseInfor.add(lblSIDPP);

		txtSICP = new JTextField();
		txtSICP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSICP.setColumns(10);
		txtSICP.setBackground(Color.YELLOW);
		txtSICP.setBounds(22, 176, 64, 21);
		pnlCaseInfor.add(txtSICP);

		
		lblSICP.setBounds(90, 176, 132, 21);
		pnlCaseInfor.add(lblSICP);

		txtCMW = new JTextField();
		txtCMW.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCMW.setColumns(10);
		txtCMW.setBackground(Color.YELLOW);
		txtCMW.setBounds(22, 151, 64, 21);
		pnlCaseInfor.add(txtCMW);

		
		lblKMW.setBounds(90, 151, 156, 21);
		pnlCaseInfor.add(lblKMW);

		txtKillPump = new JTextField();
		txtKillPump.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKillPump.setColumns(10);
		txtKillPump.setBackground(Color.YELLOW);
		txtKillPump.setBounds(255, 151, 64, 21);
		pnlCaseInfor.add(txtKillPump);
		
		lblKillPump.setBounds(323, 151, 132, 21);
		pnlCaseInfor.add(lblKillPump);

		txtSPP = new JTextField();
		txtSPP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSPP.setColumns(10);
		txtSPP.setBackground(Color.YELLOW);
		txtSPP.setBounds(255, 176, 64, 21);
		pnlCaseInfor.add(txtSPP);

		
		lblSPP.setBounds(323, 176, 132, 21);
		pnlCaseInfor.add(lblSPP);

		txtStrokes = new JTextField();
		txtStrokes.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStrokes.setColumns(10);
		txtStrokes.setBackground(Color.YELLOW);
		txtStrokes.setBounds(22, 201, 64, 21);
		pnlCaseInfor.add(txtStrokes);
		
		lblStrokes.setBounds(89, 201, 156, 21);
		pnlCaseInfor.add(lblStrokes);

		txtWD = new JTextField();
		txtWD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWD.setColumns(10);
		txtWD.setBackground(Color.YELLOW);
		txtWD.setBounds(255, 65, 64, 21);
		pnlCaseInfor.add(txtWD);
		
		lblWD.setBounds(323, 65, 132, 21);
		pnlCaseInfor.add(lblWD);


		btnAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MainDriver.FrmAnalysisOn==0){
					FrmA = new FrmAnalysis();
					FrmA.setVisible(true);
					MainDriver.FrmAnalysisOn=1;
				}
			}
		});

	}	

	void menuClose(){
		this.dispose();
		this.setVisible(false);
		try{
			MainDriver.MainMenuOn=0;
			MainDriver.ManualOn=0;
			MainDriver.AutoOn=0;
			MainDriver.drillSimOn=0;

			optCase1_wc.setEnabled(true);
			optCase2_wc.setEnabled(true);
			optCase3_wc.setEnabled(true);
			optCase4_wc.setEnabled(true);
			optCase5_wc.setEnabled(true);
			optCase6_wc.setEnabled(true);
			optCase7_wc.setEnabled(true);
			optCase8_wc.setEnabled(true);
			optCase9_wc.setEnabled(true);
			optCase10_wc.setEnabled(true);

			optCase1_dr.setEnabled(true);
			optCase2_dr.setEnabled(true);
			optCase3_dr.setEnabled(true);
			optCase4_dr.setEnabled(true);
			optCase5_dr.setEnabled(true);
			optCase7_dr.setEnabled(true);
			optCase8_dr.setEnabled(true);
			optCase9_dr.setEnabled(true);
			optCase10_dr.setEnabled(true);

			if(MainDriver.iWshow==1){
				wp.setVisible(false);
				wp.dispose();
				MainDriver.iWshow=0;
			}
			if(svTimerOn==1){
				setVisibleTimer.cancel();
				svTimerOn=0;
			}

			sim0.Sm1.menuClose();
			sim0.Sa1.menuClose();
			sim0.menuClose();			
			sim0.dispose();
			sim0.setVisible(false);	

			if(MainDriver.FrmAnalysisOn==1){
				FrmA.dispose();
				FrmA.setVisible(false);
				MainDriver.FrmAnalysisOn=0;
			}
			if(MainDriver.plot==1){
				MainDriver.plot=0;
				rp.dispose();
				rp.setVisible(false);
			}
		}catch(Exception e){

		}
	}

	class detectTask extends TimerTask{
		public void run(){
			if(MainDriver.ManualOn==0 && MainDriver.drillSimOn==0 && MainDriver.AutoOn==0 && MainDriver.inputOn==0){
				if(MainDriver.icase_mode==0){
					
					optMode1.setEnabled(true);
					optMode2.setEnabled(true);
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
					
					optCase1_wc.setEnabled(true);
					optCase2_wc.setEnabled(true);
					optCase3_wc.setEnabled(true);
					optCase4_wc.setEnabled(true);
					optCase5_wc.setEnabled(true);
					optCase6_wc.setEnabled(true);
					optCase7_wc.setEnabled(true);
					optCase8_wc.setEnabled(true);
					optCase9_wc.setEnabled(true);
					optCase10_wc.setEnabled(true);

					optCase1_dr.setEnabled(true);
					optCase2_dr.setEnabled(true);
					optCase3_dr.setEnabled(true);
					optCase4_dr.setEnabled(true);
					optCase5_dr.setEnabled(true);
					optCase7_dr.setEnabled(true);
					optCase8_dr.setEnabled(true);
					optCase9_dr.setEnabled(true);
					optCase10_dr.setEnabled(true);
				}
				else if(MainDriver.icase_mode==1){
					
					optMode1.setEnabled(true);
					optMode2.setEnabled(true);
					optOper1.setEnabled(true);
					optOper2.setEnabled(true);
				}
			}
			if(MainDriver.MainMenuVisible==1 && MainDriver.MainMenuOn==1){
				if(MainDriver.ManualOn==0) {
					setVisible(true);
				}
				MainDriver.MainMenuVisible=0;
			}
		}
	}



	public void initialize(){
		//  Initialize all dimensional variables zero
		MainDriver.iDone = 0;
		double psia = 14.7;
		for(int i=0; i<MainDriver.Npt; i++){
			MainDriver.xTop[i] = 0;  MainDriver.xBot[i] = 0;   MainDriver.Ppump[i] = psia; MainDriver.rhoK[i] = 0;
			MainDriver.Vpit[i] = 0;  MainDriver.VOLcir[i] = 0; MainDriver.Stroke[i] = 0; MainDriver.pxTop[i] = psia;
			MainDriver.Pchk[i] = psia; MainDriver.Psp[i] = psia;   MainDriver.Pcsg[i] = psia;  MainDriver.TTsec[i] = 0;
			MainDriver.Pb2p[i] = 0;  MainDriver.CHKopen[i] = 0; MainDriver.QmcfDay[i] = 0;
			MainDriver.mudflow[i] = 0; MainDriver.gasflow[i] = 0; MainDriver.PmLine[i] = 0;
		}
		for(int i = 0; i< MainDriver.Ntot; i++){
			MainDriver.Hgnd[i] = 0;  MainDriver.Pnd[i] = psia; MainDriver.Xnd[i] = 0;
			MainDriver.pvtZb[i] = 0; MainDriver.volL[i] = 0;
		}

		for(int i = 0; i < MainDriver.Nwc; i++){
			MainDriver.TMD[i] = 0;   MainDriver.TVD[i] = 0;   MainDriver.Do2p[i] = 0; MainDriver.Di2p[i] = 0;
			MainDriver.DiDS[i] = 0;  MainDriver.ang2p[i] = 0;  MainDriver.DiDS[i] = 0;  //A2p[i] = 0;
			MainDriver.volDS[i] = 0; MainDriver.VOLann[i] = 0;
		}

		MainDriver.fileDat = " ";   //default file name before retrieving
	}


	static void setVisible(){
	}
}
