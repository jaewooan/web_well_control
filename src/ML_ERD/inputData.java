package ML_ERD;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Choice;
import java.awt.List;

import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.text.DecimalFormat;

import javax.swing.JTextArea;
import javax.swing.JSlider;
import javax.swing.JList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.JScrollBar;

import java.awt.event.AdjustmentEvent;
import java.awt.Checkbox;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import ML_ERD.WellTrajt.paintPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Scrollbar;

class inputData extends JFrame {

	//7/28/2001 .... Declare all form level variables
	int badData=0;
	int dummy=0;
	double pViscos=0, Yp=0; //As Single
	int iBUR=0, referIndex=0;
	//Dim q2 As Single   //to update pump rates directly for multiple runs  //7/15/02
	int[] pmoveTop, pmoveLeft= new int[1];
	int FrameXsize = 800, FrameYsize=700;//620 for Contents, 50 for buttons	
	int FormLoadIndex = 0;
	double temp=0, temp2=0;

	JTabbedPane JTabInputData = new JTabbedPane();

	// Menu
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnHelp = new JMenu("Helps      ");
	private final JMenuItem mntmHelp = new JMenuItem("Helps");
	private final JMenu mnMenus = new JMenu("Menus      ");
	JMenuItem menuMain = new JMenuItem("Back to the Main Menu");
	JMenuItem menuToSimulation = new JMenuItem("Back to Simulation");
	private final JMenu mnReaddata = new JMenu("Read_Data    ");
	private final JMenuItem mntmShownData = new JMenuItem("Shown Data");
	private final JMenuItem mntmAllData = new JMenuItem("All Data");
	private final JMenu mnShow = new JMenu("Show      ");
	private final JMenuItem mntmWellTrajectory = new JMenuItem("Well Trajectory");
	private final JMenuItem mntmWellbore = new JMenuItem("Wellbore");
	//

	//Option Panel
	JPanel PnlOption= new JPanel();
	private final JPanel PnlOptWellLoc = new JPanel();
	private final JPanel PnlOptNumWell = new JPanel();
	private final JPanel PnlOptMud = new JPanel();
	private final JPanel PnlOptMethod = new JPanel();
	private final JPanel PnlOptFric = new JPanel();
	private final JPanel PnlOptHeat = new JPanel();
	private final JPanel PnlOptFluid = new JPanel();
	private final JPanel PnlOptMudComp = new JPanel();
	private final JPanel PnlOptMultiKick = new JPanel();
	private final JPanel PnlOptGasDev = new JPanel();
	private final JPanel PnlOptBlowout = new JPanel();
	private final JLabel lblOptWellLoc = new JLabel("Well Location");
	private final JLabel lblOptNumWell = new JLabel("Number of Well Trajectories");
	private final JLabel lblOptMud = new JLabel("Mud Type");
	private final JLabel lblOptMethod = new JLabel("Selected Method");
	private final JLabel lblOptFric = new JLabel("<html>Friction Pressure Loss<br>   in the Annulus</html>");
	private final JLabel lblOptHeat = new JLabel("Heat Transfer");
	private final JLabel lblOptFluid = new JLabel("Fluid Model");
	private final JLabel lblOptMudComp = new JLabel("<html>Mud Compressibility<br>for Well Stabilization</html>");
	private final JLabel lblOptMultiKick = new JLabel("Multi-kick option");
	private final JLabel lblOptGasDev = new JLabel("Gas Deviation Factor");
	private final JLabel lblOptBlowout = new JLabel("Blowout Animation");
	JLabel lblOptEngMethod = new JLabel("(Wait and Weight Method)");

	ButtonGroup WellLocGroup = new ButtonGroup();
	JRadioButton optOnshore = new JRadioButton("Onshore");
	JRadioButton optOffshore = new JRadioButton("Offshore");

	ButtonGroup SingleMultiWellGroup = new ButtonGroup();
	JRadioButton optERD = new JRadioButton("Single Well ( including ERD well)");
	JRadioButton optML = new JRadioButton("Multi-Lateral well");

	ButtonGroup MudTypeGroup = new ButtonGroup();
	JRadioButton optOBM = new JRadioButton("OBM");
	JRadioButton optWBM = new JRadioButton("WBM");

	ButtonGroup MethodGroup = new ButtonGroup();
	JRadioButton optDrillerMethod = new JRadioButton("Driller's Method");
	JRadioButton optEngMethod = new JRadioButton("Engineer's Method");

	ButtonGroup FrictionGroup = new ButtonGroup();
	JRadioButton optWithDP = new JRadioButton("Consider");
	JRadioButton optWithoutDP = new JRadioButton("Ignore");

	ButtonGroup HeatGroup = new ButtonGroup();
	JRadioButton optWithHT = new JRadioButton("Consider");
	JRadioButton optWithoutHT = new JRadioButton("Ignore");

	ButtonGroup FluidModelGroup = new ButtonGroup();
	JRadioButton optAPIRP13D = new JRadioButton("API RP 13D");
	JRadioButton optPower = new JRadioButton("Power-law");
	JRadioButton optNewtonian = new JRadioButton("Newtonian");
	JRadioButton optBingham = new JRadioButton("Bingham plastic");

	ButtonGroup MudCompGroup = new ButtonGroup();
	JRadioButton OptWithMudComp = new JRadioButton("Consider");
	JRadioButton optWithoutMudComp = new JRadioButton("Ignore");

	ButtonGroup MultiKickGroup = new ButtonGroup();
	JRadioButton optwithmultikick = new JRadioButton("Consider");
	JRadioButton optwithoutmultikick = new JRadioButton("Ignore");

	ButtonGroup GasDeviationGroup = new ButtonGroup();
	JRadioButton optRealGas = new JRadioButton("Consider");
	JRadioButton optIdealGas = new JRadioButton("Ignore");

	ButtonGroup BlououtGroup = new ButtonGroup();
	JRadioButton optWithBlowout = new JRadioButton("Consider");
	JRadioButton optWithoutBlowout = new JRadioButton("Ignore");
	JLabel lblOptCaution = new JLabel("<html>Options selected are updated simultaneously withoud any reading commands.<br>It is also highly recommended to specify all the options before you enter other data.</html>");
	//
	JPanel PnlInstruct = new JPanel();

	// Fluid and Bit Data
	JPanel PnlFluidBit = new JPanel();
	JPanel PnlPump = new JPanel();
	JPanel PnlWellGeometry = new JPanel();	
	private final JPanel PnlFluidData = new JPanel();
	private final JPanel PnlBitNozzle = new JPanel();
	private final JPanel PnlGasKick = new JPanel();
	private final JLabel lblFluidData = new JLabel("Fluid Data");
	private final JLabel lblBitNozzle = new JLabel("Bit Nozzle Diameter, in/32nd");
	private final JLabel lblGasKick = new JLabel("Gas Kick Data");
	private final JPanel pnlDataType = new JPanel();
	private final JLabel lblDataType = new JLabel("Mud Property Input Data Type");
	ButtonGroup ShearPlasticGroup = new ButtonGroup();
	private final JRadioButton optShearRate = new JRadioButton("Shear Stress Reading");
	private final JRadioButton optViscosity = new JRadioButton("Plastic viscosity and Yield Stress");
	private final JTextField txtSS600 = new JTextField();
	private final JTextField txtSS300 = new JTextField();
	private final JTextField txtSS100 = new JTextField();
	private final JTextField txtSS3 = new JTextField();
	private final JTextField txtMudDensity = new JTextField();
	private final JTextField txtMudComp = new JTextField();
	private final JTextField txtCritReyNumber = new JTextField();
	private final JTextField txtRoughness = new JTextField();
	private final JTextField txtSurfaceTemp = new JTextField();
	private final JTextField txtTempGrad = new JTextField();
	private final JLabel lblSS600 = new JLabel("Shear Stress Reading at 600 rpm");
	private final JLabel lblSS300 = new JLabel("Shear Stress Reading at 300 rpm");
	private final JLabel lblSS100 = new JLabel("Shear Stress Reading at 100 rpm");
	private final JLabel lblSS3 = new JLabel("Shear Stress Reading at 3 rpm");
	private final JLabel lblMudDensity = new JLabel("ppg, Old Mud Density");
	private final JLabel lblMudComp = new JLabel("E-06 1/psi, Mud Compressibility");
	private final JLabel lblCritReyNumber = new JLabel("Critical Reynolds Number");
	private final JLabel lblRoughness = new JLabel("<html>inch, Absolute Roughness of<br>Drill String</html>");
	private final JLabel lblSurfaceTemp = new JLabel("'F, Surface Temperature");
	private final JLabel lblTempGrad = new JLabel("<html>'F/100 ft<br>Mud Temperature Gradient</html>");
	private final JTextField txtNozzleDia0 = new JTextField();
	private final JTextField txtNozzleDia1 = new JTextField();
	private final JTextField txtNozzleDia2 = new JTextField();
	private final JTextField txtNozzleDia3 = new JTextField();
	private final JTextField txtGasGravity = new JTextField();
	private final JTextField txtCO2fraction = new JTextField();
	private final JTextField txtH2Sfraction = new JTextField();
	private final JLabel lblGasGravity = new JLabel("Gas Specific Gravity (Air = 1.0)");
	private final JLabel lblCO2fraction = new JLabel("Mole Fraction of CO2 in the Gas Kick");
	private final JLabel lblH2Sfraction = new JLabel("Mole Fraction of H2S in the Gas Kick");

	//

	// Pump Data
	private final JLabel lblWarning = new JLabel("All text boxes with yellow background are NOT input boxes. They just display related information only.");
	private final JPanel pnlPumpType = new JPanel();
	private final JPanel pnlPumpCirc = new JPanel();
	private final JPanel pnlTypeSurfCon = new JPanel();
	private final JPanel FrameSurfaceEq = new JPanel();
	private final JLabel lblPumpType = new JLabel("Pump Type");
	private final JLabel lblPumpCirc = new JLabel("Expected Pump Circulation Rates");
	private final JLabel lblTypeSurfCon = new JLabel("Type of Surface Connections");
	private final JLabel lblSurfConData = new JLabel("Surface Connection Data");
	ButtonGroup PumpTypeGroup = new ButtonGroup();
	//private final JRadioButton optDuplex = new JRadioButton("Pump #1");
	//private final JRadioButton optTriplex = new JRadioButton("Pump #2");

	private final JRadioButton optPump1 = new JRadioButton("Pump #1");
	private final JRadioButton optPump2 = new JRadioButton("Pump #2");

	private final JTextField txtQcapacity1 = new JTextField();
	private final JTextField txtQcapacity2 = new JTextField();
	//private final JTextField txtLinerDia = new JTextField();
	//private final JTextField txtRodDia = new JTextField();
	//private final JTextField txtStrokeLength = new JTextField();
	//private final JTextField txtPumpEff = new JTextField();

	private final JLabel lblQcapacity = new JLabel("bbl/stroke, Pump Output");
	//private final JLabel lblLinerDia = new JLabel("bbl/stroke, Pump Output");
	//private final JLabel lblRodDia = new JLabel("inch, Rod Diameter");
	//private final JLabel lblStrokeLength = new JLabel("inch, Stroke Length");
	//private final JLabel lblPumpEff = new JLabel("fraction, Pump Efficiency");

	//JSlider sldPumpRate = new JSlider();
	//JTextField txtDrillSPM = new JTextField();
	//JTextField txtDrillRate = new JTextField();	
	//JSlider sldKillPumpRate = new JSlider();
	//private final JTextField txtKillSPM = new JTextField();
	//private final JTextField txtKillRate = new JTextField();

	JSlider sldPumpRate1 = new JSlider();
	JTextField txtDrillSPM1 = new JTextField();
	JTextField txtDrillRate1 = new JTextField();	
	JSlider sldKillPumpRate1 = new JSlider();
	private final JTextField txtKillSPM1 = new JTextField();
	private final JTextField txtKillRate1 = new JTextField();

	JSlider sldPumpRate2 = new JSlider();
	JTextField txtDrillSPM2 = new JTextField();
	JTextField txtDrillRate2 = new JTextField();	
	JSlider sldKillPumpRate2 = new JSlider();
	private final JTextField txtKillSPM2 = new JTextField();
	private final JTextField txtKillRate2 = new JTextField(); // Added by jw, 20140205

	JLabel lblDrillSPM = new JLabel("Pump Strokes Per Minute");
	JLabel lblDrillRate = new JLabel("gpm, Flow Rate\r\n");
	private final JLabel lblWhileDrilling = new JLabel("While Drilling");
	private final JLabel lblKillOper = new JLabel("While Kill Operation");
	private final JLabel lblKillSPM = new JLabel("Pump Strokes Per Minute");
	private final JLabel lblKillRate = new JLabel("gpm, Flow Rate\r\n");	
	private final JComboBox comboSurfaceConn = new JComboBox();
	private final JLabel lblSurfWarning = new JLabel("<html>Surface connection data are available<br>in terms of equivalent 3-inch ID pipe.</html>");
	private final JTextField txtIDtoSP = new JTextField();
	private final JTextField txtLengthToSP = new JTextField();
	private final JTextField txtSPid = new JTextField();
	private final JTextField txtSPlength = new JTextField();
	private final JTextField txtHoseID = new JTextField();
	private final JTextField txtHoseLength = new JTextField();
	private final JTextField txtSwiveliD = new JTextField();
	private final JTextField txtSwivelLength = new JTextField();
	private final JTextField txtKellyID = new JTextField();
	private final JTextField txtKellyLength = new JTextField();
	private final JTextField txtEquivLength = new JTextField();
	private final JLabel lblDtoSP = new JLabel("inch, ID of Pipe from the Pump to Standpipe");
	private final JLabel lblLengthToS = new JLabel("ft, Length of Pipe from the Pump to SP");
	private final JLabel lblSPid = new JLabel("inch, ID of Standpipe(SP)");
	private final JLabel lblSPlength = new JLabel("ft, Length of Standpipe");
	private final JLabel lblHoseID = new JLabel("inch, ID of Drilling Hose");
	private final JLabel lblHoseLength = new JLabel("ft, Length of Drilling Hose");
	private final JLabel lblSwiveliD = new JLabel("inch, ID of Swivel and Gooseneck");
	private final JLabel lblSwivelLength = new JLabel("ft, Length of Swivel and Gooseneck");
	private final JLabel lblKellyID = new JLabel("inch, ID of Kelly");
	private final JLabel lblKellyLength = new JLabel("ft, Length of Kelly");
	private final JLabel lblEquivLength = new JLabel("ft, Length of Equivalent of 3-inch ID pipe");
	//

	// Well Geometry Data
	private final JLabel lblWellGeometryWarning = new JLabel("All text boxes with yellow background are NOT input boxes. They just display related information only.");
	private final JPanel pnlTypeWellTrajt = new JPanel();
	private final JPanel pnlWellDrillString = new JPanel();
	private final JPanel frameHorizWell = new JPanel();
	private final JPanel frameDirect = new JPanel();
	private final JLabel lblTypeWellTrajt = new JLabel("Type of Well Trajectory");
	private final JLabel lblWellDrillString = new JLabel("Well and Drill String Data");
	private final JLabel lblHorizWell = new JLabel("Type of Horizontal Well");
	private final JLabel lblDirect = new JLabel("Directional Data");
	private final JComboBox comboWellTrajt = new JComboBox();
	private final JTextField txtVdepth = new JTextField();
	private final JTextField txtMDcasing = new JTextField();
	private final JTextField txtCasingID = new JTextField();
	private final JLabel lblVdepth = new JLabel("ft, Well Vertical Depth (VD)");
	private final JLabel lblMDcasing = new JLabel("<html>ft, Measured Depth (MD)<br>&nbsp;&nbsp;of the Last Casing Seat</html>");
	private final JLabel lblCasingID = new JLabel("inch, ID of the Last Casing");
	private final JTextField txtHoleDia = new JTextField();
	private final JLabel lblHoleDia = new JLabel("inch, Diameter of Open Hole");
	private final JTextField txtDPoD = new JTextField();
	private final JLabel lblDPoD = new JLabel("inch, OD of Drill Pipe (DP)");
	private final JTextField txtDPiD = new JTextField();
	private final JLabel lblDPiD = new JLabel("inch, ID of Drill Pipe");
	private final JTextField txtHWDPlength = new JTextField();
	private final JLabel lblHWDPlength = new JLabel("ft, Length of Heviwate Drill Pipe");
	private final JTextField txtHWDPoD = new JTextField();
	private final JLabel lblHWDPoD = new JLabel("inch, OD of Heviwate Drill Pipe");
	private final JTextField txtHWDPiD = new JTextField();
	private final JLabel lblHWDPiD = new JLabel("inch, ID of Heviwate Drill Pipe");
	private final JTextField txtDClength = new JTextField();
	private final JLabel lblDClength = new JLabel("ft, Length of Drill Collars (DC)");
	private final JTextField txtDCoD = new JTextField();
	private final JLabel lblDCoD = new JLabel("inch, OD of Drill Collars");
	private final JTextField txtDCiD = new JTextField();
	private final JLabel lblDCiD = new JLabel("inch, ID of Drill Collars");
	ButtonGroup HorizTypeGroup = new ButtonGroup();
	ButtonGroup WhatToSpecifyGroup = new ButtonGroup();
	private final JRadioButton optLocalHP = new JRadioButton("Locally High Pressured Zone");
	private final JRadioButton optHomoRes = new JRadioButton("Homogeneous Reservoir");
	private final JRadioButton optSetHD = new JRadioButton("Specify Horizonal Distance to the Target Depth");
	private final JRadioButton optSetBUR = new JRadioButton("Specify BUR or Angle(s) at the End of Build (EOB)");
	private final JLabel lblTrajt = new JLabel("Specify Well Trajectory Type");
	private final JTextField txtDepthKOP = new JTextField();
	private final JLabel lblDepthKOP = new JLabel("ft, Depth to the Kick Off Point (KOP)");
	private final JTextField txtFirstBUR = new JTextField();
	private final JLabel lblFirstBUR = new JLabel("deg/100 ft, First Build-Up Rate(BUR)");
	private final JTextField txtSecondBUR = new JTextField();
	private final JLabel lblSecondBUR = new JLabel("deg/100 ft, Second BUR");
	private final JTextField txtAngleEOB = new JTextField();
	private final JLabel lblAngleEOB = new JLabel("degree, Angle at the End of Second Build");
	private final JTextField txtHDtoTD = new JTextField();
	private final JLabel lblHDtoTD = new JLabel("ft, Horizontal Distance to the Target Depth");
	private final JTextField txtFinalHoldLength = new JTextField();
	private final JLabel lblFinalHoldLength = new JLabel("ft, Length of the Final Hold Section");
	private final JTextField txtHorizLength = new JTextField();
	private final JLabel lblHorizLength = new JLabel("ft, Length of the Horizontal Hold Section");
	//

	// Example of Well Trajectory
	JPanel PnlExWellTrajt = new JPanel();
	ImagePanel ExTrjtPnl = new ImagePanel();
	//	

	// Choke & Formation Data
	JPanel PnlChokeForm = new JPanel();
	private final JPanel PnlFormProp = new JPanel();
	private final JTextField txtROP = new JTextField();
	private final JLabel lblROP = new JLabel("ft/hr, Rate of Penetration (ROP)");
	private final JPanel FrameTrip = new JPanel();
	private final JLabel lblStandLength = new JLabel("ft, Stand Length for Tripping");
	private final JTextField txtPbleed = new JTextField();
	private final JLabel lblPbleed = new JLabel("psi, Min. Pressure Increase before Bleeding off");
	private final JTextField txtTbleed = new JTextField();
	private final JLabel lblTbleed = new JLabel("sec, Average Bleeding Time");
	JPanel pnlShutinData = new JPanel();
	JTextField txtKickIntens = new JTextField();
	JTextField txtPGwarning = new JTextField();
	JLabel lblKickIntens = new JLabel("ppg, Kick Intensity (over-balance)");
	JLabel lblPGwarning = new JLabel("bbls, Pit Gain Warning Level");
	JTextField txtKillMudWt = new JTextField();
	JLabel lblKillMudWt = new JLabel("ppg, Calculated Kill Mud Weight");
	JTextField txtSIDPP = new JTextField();
	JLabel lblSIDPP = new JLabel("psi, Expected SIDPP");
	JPanel pnlSurfChoke = new JPanel();
	JLabel lblEquivIDChkValve = new JLabel("Equivalent ID of the Choke Valve, inch");
	Scrollbar HscrlCHK = new Scrollbar();
	JTextField txtCHKdia = new JTextField();
	JLabel lblShutinData = new JLabel("Shut-In Data");
	JLabel lblFormProp = new JLabel("Formation Properties");
	JLabel lblSurfChoke = new JLabel("Surface Choke Valve");
	JLabel lblTripData = new JLabel("Trip Data");
	private final JLabel lblChkFormWarning = new JLabel("<html>Negative kick intensity (i.e., formation over-balance) is suggested for multilaterals to simulate tripping and kick.<br>Old mud will be used for kill fluid in the case of negative kick intensity for safety as a practical guideline.</html>");
	JTextField txtPerm = new JTextField();
	JLabel lblPerm = new JLabel("md, Permeability");
	JTextField txtPorosity = new JTextField();
	JLabel lblPorosity = new JLabel("fraction, Porosity");
	JTextField txtSkin = new JTextField();
	JLabel lblSkin = new JLabel("dimensionless, Skin Factor (S)");
	JTextField txtTripTankVolume = new JTextField();
	JLabel lblTripTankVolume = new JLabel("bbls, Trip Tank Volume");
	JTextField txtTripTankHeight = new JTextField();
	JLabel lblTripTankHeight = new JLabel("ft, Trip Tank Height");
	JTextField txtConnTime = new JTextField();
	JLabel lblConnTime = new JLabel("sec, Minimum Connection Time");
	JTextField txtJointLength = new JTextField();
	JLabel lblJointLength = new JLabel("ft, Drill-String Joint Length");
	JTextField txtJointNumber = new JTextField();
	JLabel lblJointNumber = new JLabel("Number of Joints per Stand");
	JTextField txtStandLength = new JTextField();
	//

	// Casing and Offshore Data Panel
	JPanel PnlCasingOff = new JPanel();
	JPanel frameOffshore = new JPanel();
	private final JLabel lblOffshoreRelatedData = new JLabel("Offshore related data are available when you choose \"Offshore\" option from the option data.");
	private final JPanel PnlCondSurfCsg = new JPanel();
	private final JLabel lblCondSurfCsg = new JLabel("Conductor or Surface Casing Data");
	JCheckBox checkConductor = new JCheckBox("Conductor Used");
	JTextField txtCductDepth = new JTextField();
	JLabel lblCductDepth = new JLabel("ft, Depth of the Conductor Seat from RKB");
	JTextField txtCductOD = new JTextField();
	JLabel lblCductOD = new JLabel("inch, OD of the Conductor");
	JCheckBox checkScasing = new JCheckBox("Surface Casing Used");
	JTextField txtScasingDepth = new JTextField();
	JLabel lblScasingDepth = new JLabel("ft, Depth of the Surface Casing Seat from RKB");
	JTextField txtScasingOD = new JTextField();
	JLabel lblScasingOD = new JLabel("inch, OD of the Surface Casing");
	private final JLabel lblOffshore = new JLabel("Offshore Data");
	JLabel lblKilliD = new JLabel("inch, ID of the Kill Line Attached");
	JTextField txtKilliD = new JTextField();
	JLabel lblChokeID = new JLabel("inch, ID of the Choke Line Attached");
	JTextField txtChokeID = new JTextField();
	JLabel lblRiserID = new JLabel("inch, ID of the Marine Riser");
	JTextField txtRiserID = new JTextField();
	JLabel lblWaterTempGrad = new JLabel("'F/100 ft, Water Temperature Gradient");
	JTextField txtWaterTempGrad = new JTextField();
	JLabel lblWaterDepth = new JLabel("ft, Water Depth");
	JTextField txtWaterDepth = new JTextField();

	// Pore and Fracture Pressure Data
	JPanel PnlPoreFractureP = new JPanel();
	private final JLabel lblPoreFracWarn = new JLabel("<html>If you use Ben Eaton's method or John Barker's method for onshore wells,<br>you may have higher values for pore and fracture pressures.</html>");
	private final JPanel pnlPoreFractP = new JPanel();
	private final JLabel lblPoreFractP = new JLabel("Pore and Fracture Pressures");
	private final JPanel pnlPlotType = new JPanel();
	ButtonGroup WhatToPlotMethod = new ButtonGroup();
	private final JRadioButton optPressure = new JRadioButton("Plot the graph in terms of Pressure (psig) vs. Depth (ft)");
	private final JRadioButton optEMW = new JRadioButton("Plot the graph in terms of Equivalent Mud Weight (ppg) vs. Depth (ft)");
	private final JLabel lblDepth2 = new JLabel("BML, ft");
	private final JLabel lblPP = new JLabel("Pore Press,");
	private final JLabel lblPP2 = new JLabel("psig");
	private final JLabel lblFP2 = new JLabel("psig");
	ButtonGroup PoreFracturePMethodGroup = new ButtonGroup();
	JRadioButton optEaton = new JRadioButton("Ben Eaton's method(World Oil, Oct, 1997)");
	JRadioButton optBarker = new JRadioButton("John Barker's method");
	JRadioButton optUser = new JRadioButton("User Input");
	JButton cmdUpdate = new JButton("Update the Graph");
	JSeparator Line1 = new JSeparator();
	JLabel lblDepth = new JLabel("Depth");
	JLabel lblFP = new JLabel("Frac. Press,");
	JSeparator linePP = new JSeparator();
	JTextField[] txtDepth = new JTextField[11];
	JTextField[] txtPoreP = new JTextField[11];
	JTextField[] txtFracP = new JTextField[11];
	private final Scrollbar VscrollPP = new Scrollbar();
	private final JLabel lblChart = new JLabel("Pore and Fracture Pressures vs. Depth");
	Sgraph3 chartPoreP;	
	//

	// Multilateral Data
	JPanel PnlMultilateral = new JPanel(); // This panel will be added later 2013 12 17 ajw
	JLabel lblNote = new JLabel("Note:");	
	JLabel lblNote2 = new JLabel("<html>You can access and modify the data in this tab when you select Multi-Lateral well from the Option Data.<br>Your input data should be compatible with all the data in Well Geometry Data Tab.</html>");
	JLabel lblNote3 = new JLabel("<html>It will be a good idea to utilize well trajectory calculation ability (Show trajectory menu) of the program<br>while changing trajectory data in the Well Geometry Data Tab for each lateral trajectory of interest.</html>");
	JLabel lblMultilateralTrajectoryData = new JLabel("Multilateral Trajectory Data");
	JPanel pnlMultiTrajt = new JPanel();
	JLabel lblML = new JLabel("lblML");
	JSlider sldML = new JSlider();
	JLabel lblNumMult = new JLabel("Number of Multilaterals");
	JLabel lblNote4 = new JLabel("<html>Note: Calculated TVD and angle at the<br>KOP are calculated based on the last<br>updated data.</html>\r\n ");
	JSeparator MultilateralSp1 = new JSeparator();
	JTextField[] txtNum = new JTextField[12];
	JCheckBox[] chkPlugged = new JCheckBox[6];
	JTextField[] txtKOP = new JTextField[6];
	JTextField[] txtKOPvd = new JTextField[6];
	JTextField[] txtKOPangle = new JTextField[6];
	JTextField[] txtBUR = new JTextField[6];
	JTextField[] txtEOBangle = new JTextField[6];
	JTextField[] txtHold = new JTextField[6];
	JTextField[] txtBUR2nd = new JTextField[6];
	JTextField[] txtEOB2nd = new JTextField[6];
	JTextField[] txtHold2nd = new JTextField[6];
	JLabel lblpluggedisolated = new JLabel("<html>Plugged/<br>isolated</html>\r\n ");
	JLabel lblKOP = new JLabel("<html>Measure<br>KOP, ft</html>\r\n ");
	JLabel lblNumber = new JLabel("Number");
	JLabel lblKOPvd = new JLabel("<html>TVD at<br>KOP, ft</html>\r\n ");
	JLabel lblKOPangle = new JLabel("<html>Angle at<br>KOP, deg.</html>\r\n ");
	JLabel lblBUR = new JLabel("<html>&nbsp;1st BUR<br>deg./100 ft</html>\r\n ");
	JLabel lblEOBangle = new JLabel("<html>Angle at<br>EOB, deg.</html>\r\n ");
	JLabel lblHold = new JLabel("<html>1st Hold<br>Length, ft</html>\r\n ");
	JLabel lblBUR2nd = new JLabel("<html>2nd BUR,<br>deg/100 ft</html>\r\n ");
	JLabel lblEOB2nd = new JLabel("<html>Angle at<br>EOB, deg.</html>\r\n ");
	JLabel lblHold2nd = new JLabel("<html>2nd Hold<br>Length, ft</html>\r\n ");
	JLabel lblNumber2 = new JLabel("Number");
	JLabel lblDia = new JLabel("<html>MD of the<br>ML Sect., ft</html>\r\n ");
	JLabel lblDiaMD = new JLabel("<html>MD of the<br>1st Sect., ft</html>\r\n ");
	JLabel lblDia2nd = new JLabel("<html>Hole Dia. of<br>last Sect., in.</html>\r\n ");
	JLabel lblMLperm = new JLabel("<html>Permeability<br>&nbsp&nbsp;&nbsp;, md</html>\r\n ");
	JLabel lblMLporosity = new JLabel("<html>Porosity,<br>fraction</html>\r\n ");
	JLabel lblMLskin = new JLabel("<html>Damage<br>&nbsp;&nbsp;Skin</html>\r\n ");
	JLabel lblMLheff = new JLabel("<html>Effect. Length for<br>Flow in Reservoir, ft</html>\r\n ");
	JLabel lblPform = new JLabel("<html>Formation<br>Press., psig</html>\r\n ");
	JTextField[] txtDia = new JTextField[6];
	JTextField[] txtDiaMD = new JTextField[6];
	JTextField[] txtDia2nd = new JTextField[6];
	JTextField[] txtPform = new JTextField[6];
	JTextField[] txtMLperm = new JTextField[6];
	JTextField[] txtMLporosity = new JTextField[6];
	JTextField[] txtMLskin = new JTextField[6];
	JTextField[] txtMLheff = new JTextField[6];
	JSeparator separator_1 = new JSeparator();
	//

	// Mud Data
	JPanel PnlMudData = new JPanel();
	JPanel PnlHeatTransfer = new JPanel();	
	private final JPanel PnlMudOilType = new JPanel();
	private final JLabel lblMudOilType = new JLabel("Base Oil Type");
	ButtonGroup OilMudGroup = new ButtonGroup();
	private final JRadioButton optdiesel = new JRadioButton("No. 2 Diesel");
	private final JRadioButton optmentor = new JRadioButton("Mentor 28");
	private final JRadioButton optconoco = new JRadioButton("Conoco LVT");
	private final JRadioButton optuseroil = new JRadioButton("User input");
	private final JPanel pnlMudOBMComp = new JPanel();
	private final JLabel lblMudOBMComp = new JLabel("OBM composition");
	private final JTextField txtbaseoilf = new JTextField();
	private final JTextField txtbrinef = new JTextField();
	private final JTextField txtAdditivef = new JTextField();
	private final JLabel lblBaseOil = new JLabel("Vol. fraction of base oil in OBM");
	private final JLabel lblBrine = new JLabel("Vol. fraction of brine in OBM");
	private final JLabel lblAdditives = new JLabel("Vol. fraction of additives");
	private final JPanel pnlMudOilComp = new JPanel();
	private final JLabel lblMudOilComp = new JLabel("Baseoil composition");
	private final JLabel lblC9 = new JLabel("C9");
	private final JLabel lblC10 = new JLabel("C10");
	private final JLabel lblC11 = new JLabel("C11");
	private final JLabel lblC12 = new JLabel("C12");
	private final JLabel lblC13 = new JLabel("C13");
	private final JLabel lblC14 = new JLabel("C14");
	private final JLabel lblC15 = new JLabel("C15");
	private final JLabel lblC16 = new JLabel("C16");
	private final JLabel lblC17 = new JLabel("C17");
	private final JLabel lblC18 = new JLabel("C18");
	private final JLabel lblC19 = new JLabel("C19");
	private final JLabel lblC20 = new JLabel("C20");
	private final JLabel lblC21 = new JLabel("C21");
	private final JLabel lblC8 = new JLabel("C8");
	private final JLabel lblC22 = new JLabel("C22");
	private final JLabel lblC23 = new JLabel("C23");
	private final JLabel lblC24 = new JLabel("C24");
	private final JLabel lblC25 = new JLabel("C25+");
	private final JTextField txtbasecom8 = new JTextField();
	private final JTextField txtbasecom9 = new JTextField();
	private final JTextField txtbasecom10 = new JTextField();
	private final JTextField txtbasecom11 = new JTextField();
	private final JTextField txtbasecom12 = new JTextField();
	private final JTextField txtbasecom13 = new JTextField();
	private final JTextField txtbasecom14 = new JTextField();
	private final JTextField txtbasecom15 = new JTextField();
	private final JTextField txtbasecom16 = new JTextField();
	private final JTextField txtbasecom17 = new JTextField();
	private final JTextField txtbasecom18 = new JTextField();
	private final JTextField txtbasecom19 = new JTextField();
	private final JTextField txtbasecom20 = new JTextField();
	private final JTextField txtbasecom21 = new JTextField();
	private final JTextField txtbasecom22 = new JTextField();
	private final JTextField txtbasecom25 = new JTextField();
	private final JTextField txtbasecom23 = new JTextField();
	private final JTextField txtbasecom24 = new JTextField();		
	//

	// Heat Transfer
	JPanel PnlFormHeatProp = new JPanel();
	JTextField txtTconF = new JTextField();
	JTextField txtSheatF = new JTextField();
	JTextField txtHtrancF = new JTextField();
	JLabel lblTconF = new JLabel("Btu/ft-'F-hr, Thermal conductivity");
	JLabel lblSheatF = new JLabel("Btu/lbm-'F, Specific heat capacity");
	JLabel lblCoeff = new JLabel("Btu/hr-'F-ft^2, Heat transfer coeff.");
	JPanel PnlMudHeatProp = new JPanel();
	JTextField txtTconM = new JTextField();
	JTextField txtSheatM = new JTextField();
	JTextField txtHtrancM = new JTextField();
	JTextField txtInjmudT = new JTextField();
	JLabel lblTconM = new JLabel("Btu/ft-'F-hr, Thermal conductivity");
	JLabel lblSheatM = new JLabel("Btu/lbm-'F, Specific heat capacity");
	JLabel lblHtrancM = new JLabel("Btu/hr-'F-ft^2, Heat transfer coeff.");
	JLabel lblInjmudT = new JLabel("'F, Injection mud temperature");
	JLabel lblFormHeatProp = new JLabel("Formation heat properties");
	JLabel lblMudHeatProp = new JLabel("Mud heat properties");
	//

	// Instructor panel
	JPanel PnlResProp = new JPanel();
	JPanel PnlDrillingProblem = new JPanel();
	JLabel lblResProp = new JLabel("Geology Formation Data and Default Data for Simulation");
	JLabel lblDrillingProblem = new JLabel("Drilling Problem Information");
	JLabel lblNumLayer = new JLabel("Number of Layers");
	JSlider sldLayer = new JSlider();
	
	JTextField txtNumLayer = new JTextField();
	JLabel lblRock = new JLabel("Rock");
	JLabel lblRock2 = new JLabel("Type");	
	JLabel lblDep = new JLabel("Vertical Depth");
	JLabel lblDep2 = new JLabel("From");
	JLabel lblDep3 = new JLabel("To");
	JLabel lblDep4 = new JLabel("(ft)");
	JLabel lblDep5 = new JLabel("(ft)");
	JLabel lblROP0 = new JLabel("Rate of");
	JLabel lblROP2 = new JLabel("Penetration");
	JLabel lblROP3 = new JLabel("(ft/hr)");
	JLabel lblRPM = new JLabel("Rotational");
	JLabel lblRPM2 = new JLabel("Speed");
	JLabel lblRPM3 = new JLabel("(RPM)");
	JLabel lblWOB = new JLabel("Weight");
	JLabel lblWOB2 = new JLabel("On Bit");
	JLabel lblWOB3 = new JLabel("(1,000 lbf)");
	JLabel lblPorePres = new JLabel("Pore");
	JLabel lblPorePres2 = new JLabel("Pressure");
	JLabel lblPorePres3 = new JLabel("(psia)");
	JLabel lblFracPres = new JLabel("Fracture");
	JLabel lblFracPres2 = new JLabel("Pressure");
	JLabel lblFracPres3 = new JLabel("(psia)");
	JLabel lblPerm0 = new JLabel("Permeability");
	JLabel lblPerm2 = new JLabel("(mD)");
	JLabel lblPoro = new JLabel("Porosity");
	JLabel lblPoro2 = new JLabel("(fraction)");
	JLabel lblMudLossSelection = new JLabel("Lost Circulation");		
	JLabel lblMudLossSelection2 = new JLabel("Zone");
	
	JComboBox[] comboRock = new JComboBox[5];
	JTextField[] txtDepFrom = new JTextField[5];	
	JTextField[] txtDepTo = new JTextField[5];	
	JTextField[] txtROP_Base = new JTextField[5];
	JTextField[] txtRPM_Base = new JTextField[5];
	JTextField[] txtWOB_Base = new JTextField[5];
	JTextField[] txtFracP_Base = new JTextField[5];
	JTextField[] txtPoreP_Base = new JTextField[5];
	JTextField[] txtPerm_Base = new JTextField[5];
	JTextField[] txtPoro_Base = new JTextField[5];

	JLabel lblProblems = new JLabel("Drilling Problems");
	JLabel lblOnOff = new JLabel("On/Off");
	JLabel lblPossibility = new JLabel("Probability");
	JSeparator SpProblem = new JSeparator();
	JLabel lblProblem1 = new JLabel("Mechanical Pipe Stuck");
	JLabel lblProblem2 = new JLabel("Differential Pipe Stuck");
	JLabel lblProblem3 = new JLabel("Lost Circulation");
	JLabel lblProblem4 = new JLabel("Cuttings Accumulation");
	JLabel lblTstep = new JLabel("Time Interval For Possible");
	JLabel lblTstep2 = new JLabel("Drill Problems (min)");
	
	JRadioButton[] optOnG = new JRadioButton[4];
	JRadioButton[] optOffG = new JRadioButton[4];
	JTextField[] txtProb = new JTextField[2];
	JTextField txtTstep_Problem = new JTextField("10");
	ButtonGroup GroupMudLoss = new ButtonGroup();
	JRadioButton[] optLossLayer = new JRadioButton[5];
	
	ButtonGroup GroupOnOff1 = new ButtonGroup();
	JSeparator SpResProp = new JSeparator();
	ButtonGroup GroupOnOff2 = new ButtonGroup();
	ButtonGroup GroupOnOff3 = new ButtonGroup();
	ButtonGroup GroupOnOff4 = new ButtonGroup();
	JSeparator SpProblem2 = new JSeparator();
	
	JLabel lblBitLocation = new JLabel("Bit Location Data");
	JPanel pnlBitLoc = new JPanel();
	JLabel lblBitMD = new JLabel("ft, Measured Depth");
	JLabel lblBitVD = new JLabel("ft, Vertical Depth");
	JLabel lblBitLoc2 = new JLabel("On bottom condition;");
	JLabel lblBitLoc3 = new JLabel("Bit is on the bottom!");
	
	//
	
	// 10 OK, Apply, Cancel Buttons
	JButton[] OKButton = new JButton[11];
	JButton[] ApplyButton = new JButton[11];
	JButton[] CancelButton = new JButton[11];
	//
	PnlWellTrajt PnlWellTrjt = new PnlWellTrajt();

	Border lineBorder = BorderFactory.createLineBorder(Color.black, 1);
	Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);    //TextArea¿¡ lineBorder(°ËÁ¤Å×µÎ¸®), emptyBorder(¿©¹é)·Î ±¸¼ºµÈ º¹ÇÕ °æ°è¼±À» ¼³Á¤ÇÕ´Ï´Ù.
	//ta.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));

	WellTrajt TrjtEX = new WellTrajt();
	wellpic wellpicEX = new wellpic();
	JTextField txtBitMD = new JTextField("0");
	JTextField txtBitVD = new JTextField("0");

	inputData(){
		
		addWindowListener(new WindowAdapter() {			@Override
			public void windowActivated(WindowEvent arg0) {
			if(FormLoadIndex==1){
				Assign_Options();
				Assign_Fluid_Bit();
				Assign_Pump();
				Assign_Cduct_Offshore();
				Assign_Formation();
				Assign_WellGeometry();
				assign_Pore_Fracture();
				//Assign_Multilateral();
				//Added by ty
				Assign_mud();
				Assign_Heattransfer();
			}
		}
		});

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				e.getWindow().dispose();
				e.getWindow().setVisible(false);
				if(MainDriver.drillSimOn==1||MainDriver.AutoOn==1||MainDriver.ManualOn==1){
					MainDriver.MenuSelected=1;
					MainDriver.MainMenuVisible=0;
				}
				else if(MainDriver.MainMenuOn==1){
					MainDriver.MainMenuVisible=1;
				}
				menuclose();
			}
		});

		addComponentListener(new ComponentAdapter() {			
			public void componentResized(ComponentEvent arg0) {				
				PnlWellTrjt.PanelSizeX = getWidth();
				PnlWellTrjt.PanelSizeY= getHeight()-120;
				PnlWellTrjt.PicSizeX=PnlWellTrjt.PanelSizeX/2;
				PnlWellTrjt.PicSizeY=PnlWellTrjt.PanelSizeY;
				PnlWellTrjt.contentPane.setBounds(0, 0, PnlWellTrjt.PanelSizeX, PnlWellTrjt.PanelSizeY);
				PnlWellTrjt.pntpnl.setBounds(0, 0, PnlWellTrjt.PicSizeX, PnlWellTrjt.PicSizeY);
				PnlWellTrjt.wtFrame0.setBounds(PnlWellTrjt.PanelSizeX-360, 60, 340, 99);
				PnlWellTrjt.wtFrame1.setBounds(PnlWellTrjt.PanelSizeX-360, 169, 340, 149);
				PnlWellTrjt.wtFrame2.setBounds(PnlWellTrjt.PanelSizeX-360, 328, 340, 121);
				PnlWellTrjt.txtVD.setBounds(PnlWellTrjt.PanelSizeX-210-100, 10, 80, 18);
				PnlWellTrjt.lblVerticalDepthFt.setBounds(PnlWellTrjt.PanelSizeX-210, 10, 190, 18);
				PnlWellTrjt.txtHD.setBounds(PnlWellTrjt.PanelSizeX-210-100, 32, 80, 18);
				PnlWellTrjt.lblHorizonMouse.setBounds(PnlWellTrjt.PanelSizeX-210, 32, 190, 18);
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent arg0) {
				//WellTrajt.WindowState = 2 // maximize
						PnlWellTrjt.setScale();   //set a scale for ERD and ML
						PnlWellTrjt.pntpnl.repaint();
								//if(MainDriver.igERD == 1) DrawMultilateral   //to draw multilateral trajectories
						//
						PnlWellTrjt.wtFrame1.setVisible(false);
						PnlWellTrjt.wtFrame2.setVisible(false);
						PnlWellTrjt.wttext0.setText(PnlWellTrjt.format2.format(MainDriver.Vdepth));
						PnlWellTrjt.wttext2.setText(PnlWellTrjt.format2.format(MainDriver.Hdisp));
						if (MainDriver.iWell == 0) {
							PnlWellTrjt.tMDwell = MainDriver.Vdepth; 
							PnlWellTrjt.wttext2.setText( "0.0");
						}
						if (MainDriver.iWell >= 1){  // draw KOP & First Build Section
							PnlWellTrjt.wtFrame1.setVisible(true);
							PnlWellTrjt.tMDwell = MainDriver.DepthKOP + MainDriver.Rbur * MainDriver.angEOB * MainDriver.radConv;
						}
						if (MainDriver.iWell >= 2) PnlWellTrjt.tMDwell = PnlWellTrjt.tMDwell + MainDriver.xHold;   // draw First Hold Section
						if (MainDriver.iWell >= 3){
							PnlWellTrjt.wtFrame2.setVisible(true);
							PnlWellTrjt.tMDwell = PnlWellTrjt.tMDwell + MainDriver.R2bur * (MainDriver.ang2EOB - MainDriver.angEOB) * MainDriver.radConv;
						}
						if (MainDriver.iWell == 4) PnlWellTrjt.tMDwell = PnlWellTrjt.tMDwell + MainDriver.x2Hold;
						PnlWellTrjt.wttext3.setText( PnlWellTrjt.format2.format(MainDriver.DepthKOP));
						PnlWellTrjt.wttext4.setText( PnlWellTrjt.format2.format(MainDriver.BUR));
						PnlWellTrjt.wttext5.setText( PnlWellTrjt.format2.format(MainDriver.Rbur));
						PnlWellTrjt.wttext6.setText( PnlWellTrjt.format2.format(MainDriver.angEOB));
						PnlWellTrjt.wttext7.setText( PnlWellTrjt.format2.format(MainDriver.xHold));
						if (MainDriver.iWell == 1) PnlWellTrjt.wttext7.setText( "0.0");
						PnlWellTrjt.wttext8.setText( PnlWellTrjt.format2.format(MainDriver.BUR2));
						PnlWellTrjt.wttext9.setText( PnlWellTrjt.format2.format(MainDriver.R2bur));
						PnlWellTrjt.wttext10.setText( PnlWellTrjt.format2.format(MainDriver.ang2EOB));
						PnlWellTrjt.wttext11.setText( PnlWellTrjt.format2.format(MainDriver.x2Hold));
						if (MainDriver.iWell == 3) PnlWellTrjt.wttext11.setText( "0.0");
						PnlWellTrjt.wttext1.setText(PnlWellTrjt.format2.format(PnlWellTrjt.tMDwell));
			}
		});			

		setTitle("Current Input Data in Use");
		setIconImage(MainDriver.icon.getImage());
		setBounds(0, 0, FrameXsize, FrameYsize);
		
		MainDriver.Vdepth_casing = utilityModule.getVD(MainDriver.DepthCasing);
		for(int i=0; i<5; i++){
			MainDriver.layerROP[i] = 60;
			MainDriver.layerRPM[i] = 60;
			MainDriver.layerWOB[i] = 40;//kips			
			MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
			MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
			MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
			//MainDriver.layerVDfrom[i] =(MainDriver.Vdepth-MainDriver.DepthCasing)/5*(i-1)+MainDriver.DepthCasing;
			//MainDriver.layerVDto[i] = (MainDriver.Vdepth-MainDriver.DepthCasing)/5*i+MainDriver.DepthCasing;
			MainDriver.layerVDfrom[i] =(MainDriver.Vdepth-MainDriver.Vdepth_casing)/5*(i-1)+MainDriver.Vdepth_casing;
			MainDriver.layerVDto[i] = (MainDriver.Vdepth-MainDriver.Vdepth_casing)/5*i+MainDriver.Vdepth_casing;
			MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];
			MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[1];
			
			comboRock[i] = new JComboBox();			
			
			txtDepFrom[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerVDfrom[i]));
			txtDepTo[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerVDto[i]));
			txtROP_Base[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerROP[i]));
			txtRPM_Base[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerRPM[i]));
			txtWOB_Base[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerWOB[i])); // 1,000 lbm
			
			txtFracP_Base[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerFracP[i]));
			txtFracP_Base[i].setBackground(Color.YELLOW);
			txtPoreP_Base[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerPoreP[i]));
			txtPoreP_Base[i].setBackground(Color.YELLOW);
			txtPerm_Base[i] = new JTextField((new DecimalFormat("######.0")).format(MainDriver.layerPerm[i])); //md
			txtPoro_Base[i] = new JTextField((new DecimalFormat("0.000")).format(MainDriver.layerPoro[i])); //fraction			
		}
		for(int i=0; i<11; i++){
			OKButton[i] = new JButton("OK");
			OKButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						read_Fluid_Bit();
						read_Pump();
						dummy = read_WellGeometry();						
						read_Formation();
						read_Cduct_Offshore();
						read_Pore_Fracture();
						//read_Multilateral();
						read_Mud();
						read_Heattransfer();			     
						read_Instructor();
						
						check_Fluid_Bit();
						check_Pump();
						dummy = check_WellGeometry();
						check_Formation();
						check_Cduct_Offshore();
						check_Pore_Fracture();
						//check_Multilateral();
						if(MainDriver.imud == 1) check_Muddata();
						read_WellTrajt();
						menuclose();
					}
					catch(Exception ee){
						String msg = "Check your input data again!";
						JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			ApplyButton[i] = new JButton("Apply");
			ApplyButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						read_Fluid_Bit();
						read_Pump();
						dummy = read_WellGeometry();						
						read_Formation();
						read_Cduct_Offshore();
						read_Pore_Fracture();
						//read_Multilateral();
						read_Mud();
						read_Heattransfer();							
						read_Instructor();
						
						check_Fluid_Bit();
						check_Pump();
						dummy = check_WellGeometry();
						check_Formation();
						check_Cduct_Offshore();
						check_Pore_Fracture();
						//check_Multilateral();
						if(MainDriver.imud == 1) check_Muddata();
						read_WellTrajt(); // added by jaewoo 140114
					}catch(Exception ee){
						String msg = "Check your input data again!";
						JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			CancelButton[i] = new JButton("Initialization");
			CancelButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						Assign_Options();
						Assign_Fluid_Bit();
						Assign_Pump();
						Assign_Cduct_Offshore();
						Assign_Formation();
						Assign_WellGeometry();
						assign_Pore_Fracture();
						//Assign_Multilateral();
						//Added by ty
						Assign_mud();
						Assign_Heattransfer();
					}catch(Exception ee){
						String msg = "Check your input data again!";
						JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}

		//Set the each tab panel		
		OptionPanelSetting();
		FluidBitPanelSetting();
		PumpPanelSetting();
		WellGeometryPanelSetting();
		ExTrjtSetting();
		MudDataPanelSetting();
		ChokeFormPanelSetting();
		CsgOffPanelSetting();
		PoreFracturePSetting();
		MultilateralPnlSetting();
		HeatTransferSetting();
		InstructPanelSetting();

		JTabInputData.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);		
		JTabInputData.setFont(new Font("±¼¸²", Font.BOLD, 14));
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Instruction</td></tr></table></body></html>" , null, PnlInstruct);//1
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Options</td></tr></table></body></html>" , null, PnlOption);//2
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Fluid and Bit Data</td></tr></table></body></html>", null, PnlFluidBit);//3
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Pump Data</td></tr></table></body></html>", null, PnlPump);//4
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Well Geometry Data</td></tr></table></body></html>", null, PnlWellGeometry);//5
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Example of Well Trajectory</td></tr></table></body></html>", null, PnlExWellTrajt);//6
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Well Trajectory</td></tr></table></body></html>", null, PnlWellTrjt);//7
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Choke and Formation Data</td></tr></table></body></html>", null, PnlChokeForm);//8
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Casing and Offshore Data</td></tr></table></body></html>", null, PnlCasingOff);//9
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Pore and Fracture Pressure Data</td></tr></table></body></html>", null, PnlPoreFractureP);//10
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Mud Data</td></tr></table></body></html>", null, PnlMudData);//11
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Heat Transfer Data</td></tr></table></body></html>", null, PnlHeatTransfer);//12
		JTabInputData.addTab("<html><body><table width='110'><tr><td align = center>Multilateral Data</td></tr></table></body></html>", null, PnlMultilateral);//13

		JTabInputData.setBackgroundAt(12, Color.gray);
		
		//Menu Setting
		setJMenuBar(menuBar);		
		menuBar.add(mnHelp);		
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = "This program is carefully designed to cover many options and their combinations."+"\n"+
						"  You can change part or all of INPUT data in WHITE input boxes."+"\n"+
						"  Yellow or RED input boxes only display calculation results or warning message for wrong data."+"\n"+
						"  Options are updated immediately, but you have to read input data in input boxes using Read_Data menu"+"\n"+
						"  to update your data. Otherwise, the program will keep previous data."+"\n"+
						"  You can access Multilateral(ML) Tap after choosing ML trajectory option."+"\n"+
						"  The OK button will update the input data and close the window while the Apply button will only update the input data."+"\n"+
						"  You can also view wellbore profile and well trajectory using the Show menu.";
				JOptionPane.showMessageDialog(null, msg, "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});		



		mnHelp.add(mntmHelp);		
		menuBar.add(mnMenus);		
		menuMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(badData == 1){
					String msg = "There is at least one data that is not compatible with the current input data. Check all data carefully !";
					JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					MainDriver.MenuSelected=0;
					MainDriver.MainMenuVisible=1;
					menuclose();
					menuToSimulation.setEnabled(false);
				}
			}});
		mnMenus.add(menuMain);		
		menuToSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.MenuSelected=1;
				MainDriver.MainMenuVisible=0;
				menuclose();
				menuToSimulation.setEnabled(false);
			}
		});
		mnMenus.add(menuToSimulation);		
		menuBar.add(mnReaddata);		

		mntmShownData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(JTabInputData.getSelectedComponent() == PnlFluidBit){
						read_Fluid_Bit();
						check_Fluid_Bit();
					}
					else if(JTabInputData.getSelectedComponent() == PnlPump){
						read_Pump();
						check_Pump();
					}
					else if(JTabInputData.getSelectedComponent() == PnlWellGeometry){
						read_WellGeometry();						
						check_WellGeometry();
						read_WellTrajt(); // added by jaewoo 140114
					}
					else if(JTabInputData.getSelectedComponent() == PnlChokeForm){
						read_Formation();
						check_Formation();
					}
					else if(JTabInputData.getSelectedComponent() == PnlCasingOff){
						read_Cduct_Offshore();
						check_Cduct_Offshore();
					}
					else if(JTabInputData.getSelectedComponent() == PnlPoreFractureP){
						read_Pore_Fracture();
						check_Pore_Fracture();
					}					
					else if(JTabInputData.getSelectedComponent() == PnlMultilateral){
						//read_Multilateral();
						//check_Multilateral();
					}
					else if(JTabInputData.getSelectedComponent() == PnlOption){	
						//........ updated simultaneously without reading the data !
						//Added by Ty
						dummy=0;
					}
					else if(JTabInputData.getSelectedComponent() == PnlMudData){
						read_Mud();					
						if(MainDriver.imud == 1) check_Muddata();
					}
					else if(JTabInputData.getSelectedComponent() == PnlHeatTransfer) read_Heattransfer();
					else JOptionPane.showMessageDialog(null, "Unknown Data Category for Data Input !! ??", "Message", JOptionPane.INFORMATION_MESSAGE);
				}catch(Exception ee){
					String msg = "Check your input data again!";
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		mnReaddata.add(mntmShownData);		
		mntmAllData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{			
					read_Fluid_Bit();
					read_Pump();
					dummy = read_WellGeometry();					
					read_Formation();
					read_Cduct_Offshore();
					read_Pore_Fracture();
					//read_Multilateral();
					read_Mud();
					read_Heattransfer();			     

					check_Fluid_Bit();
					check_Pump();
					dummy = check_WellGeometry();
					check_Formation();
					check_Cduct_Offshore();
					check_Pore_Fracture();
					//check_Multilateral();
					if(MainDriver.imud == 1) check_Muddata();
					read_WellTrajt();
				}catch(Exception ee){
					String msg = "Check your input data again!";
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnReaddata.add(mntmAllData);		
		menuBar.add(mnShow);		
		mntmWellTrajectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				check_WellGeometry();
				//check_Multilateral
				if(badData == 0) TrjtEX.setVisible(true);
			}
		});



		mnShow.add(mntmWellTrajectory);		
		mntmWellbore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				check_WellGeometry();
				if(badData==0) wellpicEX.setVisible(true);
			}
		});
		mnShow.add(mntmWellbore);
		//
		Form_Load();
	}


	void menuclose(){
		this.dispose();
		this.setVisible(false);
		MainDriver.inputOn=0;
		TrjtEX.dispose();
		TrjtEX.setVisible(false);
		wellpicEX.dispose();
		wellpicEX.setVisible(false);
	}

	void HeatTransferSetting(){
		//½Ã
		int CompSrtX = 30, CompSrtY = 40, CompIntvX=5, CompIntvY = 5, txtSizeY=20, CompSizeX = 70, CompSizeY = 20, labelSizeX=200;
		int HeatTransferSrtX = 20, HeatTransferSrtY=30, HeatTransferSizeX=350, HeatTransferSizeY=400;

		PnlHeatTransfer.setLayout(null);

		OKButton[7].setBounds(400,510,100,23);
		ApplyButton[7].setBounds(510,510,100,23);
		CancelButton[7].setBounds(620,510,100,23);
		PnlHeatTransfer.add(OKButton[7]);
		PnlHeatTransfer.add(ApplyButton[7]);
		//PnlHeatTransfer.add(CancelButton[7]);

		lblFormHeatProp.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblFormHeatProp.setBounds(HeatTransferSrtX+5, HeatTransferSrtY-7, 169, 15);
		lblFormHeatProp.setOpaque(true);
		PnlHeatTransfer.add(lblFormHeatProp);
		PnlFormHeatProp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlFormHeatProp.setBounds(HeatTransferSrtX, HeatTransferSrtY, HeatTransferSizeX, HeatTransferSizeY);
		PnlHeatTransfer.add(PnlFormHeatProp);
		PnlFormHeatProp.setLayout(null);

		txtTconF.setBounds(CompSrtX, CompSrtY, CompSizeX, txtSizeY);
		txtTconF.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));

		PnlFormHeatProp.add(txtTconF);
		lblTconF.setHorizontalAlignment(SwingConstants.LEFT);
		lblTconF.setBounds(CompSrtX+CompSizeX+CompIntvX*2, CompSrtY, labelSizeX, CompSizeY);
		PnlFormHeatProp.add(lblTconF);

		txtSheatF.setBounds(CompSrtX, CompSrtY+CompSizeY+3*CompIntvY, CompSizeX, txtSizeY);
		txtSheatF.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlFormHeatProp.add(txtSheatF);
		lblSheatF.setHorizontalAlignment(SwingConstants.LEFT);
		lblSheatF.setBounds(CompSrtX+CompSizeX+CompIntvX*2, CompSrtY+CompSizeY+3*CompIntvY, labelSizeX, CompSizeY);
		PnlFormHeatProp.add(lblSheatF);

		txtHtrancF.setBounds(CompSrtX, CompSrtY+CompSizeY*2+6*CompIntvY, CompSizeX, txtSizeY);
		txtHtrancF.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlFormHeatProp.add(txtHtrancF);	
		lblCoeff.setHorizontalAlignment(SwingConstants.LEFT);
		lblCoeff.setBounds(CompSrtX+CompSizeX+CompIntvX*2, CompSrtY+CompSizeY*2+6*CompIntvY, labelSizeX, CompSizeY);
		PnlFormHeatProp.add(lblCoeff);

		lblMudHeatProp.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblMudHeatProp.setBounds(HeatTransferSrtX +HeatTransferSizeX+4*CompIntvX+5, HeatTransferSrtY-7, 132, 15);
		lblMudHeatProp.setOpaque(true);
		PnlHeatTransfer.add(lblMudHeatProp);
		PnlMudHeatProp.setLayout(null);
		PnlMudHeatProp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlMudHeatProp.setBounds(HeatTransferSrtX +HeatTransferSizeX+4*CompIntvX, HeatTransferSrtY, HeatTransferSizeX, HeatTransferSizeY);
		PnlHeatTransfer.add(PnlMudHeatProp);

		txtTconM.setBounds(CompSrtX, CompSrtY, CompSizeX, txtSizeY);
		txtTconM.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlMudHeatProp.add(txtTconM);
		lblTconM.setHorizontalAlignment(SwingConstants.LEFT);
		lblTconM.setBounds(CompSrtX+CompSizeX+CompIntvX*2, CompSrtY, labelSizeX, CompSizeY);
		PnlMudHeatProp.add(lblTconM);
		txtSheatM.setBounds(CompSrtX, CompSrtY+CompSizeY+3*CompIntvY, CompSizeX, txtSizeY);
		txtSheatM.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlMudHeatProp.add(txtSheatM);
		lblSheatM.setHorizontalAlignment(SwingConstants.LEFT);
		lblSheatM.setBounds(CompSrtX+CompSizeX+CompIntvX*2, CompSrtY+CompSizeY+3*CompIntvY, labelSizeX, CompSizeY);
		PnlMudHeatProp.add(lblSheatM);
		txtHtrancM.setBounds(CompSrtX, CompSrtY+CompSizeY*2+6*CompIntvY, CompSizeX, txtSizeY);
		txtHtrancM.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlMudHeatProp.add(txtHtrancM);
		lblHtrancM.setHorizontalAlignment(SwingConstants.LEFT);
		lblHtrancM.setBounds(CompSrtX+CompSizeX+CompIntvX*2, CompSrtY+CompSizeY*2+6*CompIntvY, labelSizeX, CompSizeY);
		PnlMudHeatProp.add(lblHtrancM);
		txtInjmudT.setBounds(CompSrtX, CompSrtY+CompSizeY*3+8*CompIntvY, CompSizeX, txtSizeY);
		txtInjmudT.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlMudHeatProp.add(txtInjmudT);
		lblInjmudT.setHorizontalAlignment(SwingConstants.LEFT);
		lblInjmudT.setBounds(CompSrtX+CompSizeX+CompIntvX*2, CompSrtY+CompSizeY*3+8*CompIntvY, labelSizeX, CompSizeY);
		PnlMudHeatProp.add(lblInjmudT);		

		getContentPane().setLayout(new GridLayout(1, 1));
		getContentPane().add(JTabInputData);
	}

	void MultilateralPnlSetting(){
		int CompSrtX = 10, CompSrtY = 20, CompIntvX=5, CompIntvY = 5, miniIntvY=2, lblSizeX=60, lblSizeX2=75, lblSizeX3=65, lblSizeY=32, CompSizeX = 50, CompSizeY = 16;
		int pnlMultiTrajtSrtX = 10, pnlMultiTrajtSrtY=85, pnlMultiTrajtSizeX=2*CompSrtX+11*lblSizeX+CompIntvX*10, pnlMultiTrajtSizeY=14*CompSizeY+lblSizeY*2+2*CompSrtY+16*CompIntvY;
		int UpperTxtSrtY = 0, UpperTxtSrtY2 = 0;

		PnlMultilateral.setLayout(null);		
		lblNote.setForeground(Color.RED);
		lblNote.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblNote.setBounds(12, 10, 35, 15);
		PnlMultilateral.add(lblNote);
		lblNote2.setForeground(Color.BLUE);
		lblNote2.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblNote2.setBounds(59, 10, 695, 30);
		PnlMultilateral.add(lblNote2);		
		lblNote3.setForeground(Color.BLACK);
		lblNote3.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblNote3.setBounds(59, 40, 686, 30);
		PnlMultilateral.add(lblNote3);

		OKButton[8].setBounds(400,510,100,23);
		ApplyButton[8].setBounds(510,510,100,23);
		CancelButton[8].setBounds(620,510,100,23);
		PnlMultilateral.add(OKButton[8]);
		PnlMultilateral.add(ApplyButton[8]);
		//PnlMultilateral.add(CancelButton[8]);

		lblMultilateralTrajectoryData.setForeground(Color.BLACK);
		lblMultilateralTrajectoryData.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblMultilateralTrajectoryData.setOpaque(true);
		lblMultilateralTrajectoryData.setBounds(pnlMultiTrajtSrtX+5, pnlMultiTrajtSrtY-7, 179, 15);
		PnlMultilateral.add(lblMultilateralTrajectoryData);				
		pnlMultiTrajt.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlMultiTrajt.setBounds(pnlMultiTrajtSrtX, pnlMultiTrajtSrtY, pnlMultiTrajtSizeX, pnlMultiTrajtSizeY);
		PnlMultilateral.add(pnlMultiTrajt);
		pnlMultiTrajt.setLayout(null);

		lblML.setBounds(CompSrtX, CompSrtY, 31, 15);
		pnlMultiTrajt.add(lblML);		

		for(int i=0; i<6; i++){
			txtNum[i] = new JTextField();			
			txtNum[i+6] = new JTextField();
			chkPlugged[i] = new JCheckBox();
			txtKOP [i] = new JTextField();
			txtKOPvd [i] = new JTextField();
			txtKOPangle [i] = new JTextField();
			txtBUR [i] = new JTextField();
			txtEOBangle [i] = new JTextField();
			txtHold [i] = new JTextField();
			txtBUR2nd [i] = new JTextField();
			txtEOB2nd [i] = new JTextField();
			txtHold2nd [i] = new JTextField();
			txtDia [i] = new JTextField();
			txtDiaMD [i] = new JTextField();
			txtDia2nd [i] = new JTextField();
			txtPform [i] = new JTextField();
			txtMLperm [i] = new JTextField();
			txtMLporosity [i] = new JTextField();
			txtMLskin [i] = new JTextField();
			txtMLheff [i] = new JTextField();

			txtNum[i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));			
			txtNum[i+6].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			chkPlugged[i] = new JCheckBox();
			txtKOP [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtKOPvd [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtKOPangle [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtBUR [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtEOBangle [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtHold [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtBUR2nd [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtEOB2nd [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtHold2nd [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtDia [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtDiaMD [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtDia2nd [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtPform [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtMLperm [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtMLporosity [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtMLskin [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			txtMLheff [i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));			

			pnlMultiTrajt.add(txtNum[i]);
			pnlMultiTrajt.add(txtNum[i+6]);
			pnlMultiTrajt.add(chkPlugged[i]);
			pnlMultiTrajt.add(txtKOP [i]);
			pnlMultiTrajt.add(txtKOPvd [i]);
			pnlMultiTrajt.add(txtKOPangle [i]);
			pnlMultiTrajt.add(txtBUR [i]);
			pnlMultiTrajt.add(txtEOBangle [i]);
			pnlMultiTrajt.add(txtHold [i]);
			pnlMultiTrajt.add(txtBUR2nd [i]);
			pnlMultiTrajt.add(txtEOB2nd [i]);
			pnlMultiTrajt.add(txtHold2nd [i]);
			pnlMultiTrajt.add(txtDia [i]);
			pnlMultiTrajt.add(txtDiaMD [i]);
			pnlMultiTrajt.add(txtDia2nd [i]);
			pnlMultiTrajt.add(txtPform [i]);
			pnlMultiTrajt.add(txtMLperm [i]);
			pnlMultiTrajt.add(txtMLporosity [i]);
			pnlMultiTrajt.add(txtMLskin [i]);
			pnlMultiTrajt.add(txtMLheff [i]);
		}

		sldML.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {				
				lblML.setText(Integer.toString(sldML.getValue()));
				int MLnumber = sldML.getValue();
				//
				for(int i = 0; i<MLnumber; i++){
					txtNum[i].setVisible(true); 
					txtNum[i+6].setVisible(true);
					chkPlugged[i].setVisible(true);
					txtKOPvd[i].setVisible(true); 
					txtKOPangle[i].setVisible(true);
					txtKOP[i].setVisible(true); 
					txtKOPvd[i].setVisible(true); 
					txtKOPangle[i].setVisible(true);
					txtBUR[i].setVisible(true); 
					txtEOBangle[i].setVisible(true);
					txtHold[i].setVisible(true);
					txtBUR2nd[i].setVisible(true); 
					txtEOB2nd[i].setVisible(true);
					txtHold2nd[i].setVisible(true);
					txtDia[i].setVisible(true);
					txtDiaMD[i].setVisible(false);
					txtDia2nd[i].setVisible(false);
					//txtDiaMD[i].setVisible(true); txtDia2nd[i].setVisible(true)
					txtPform[i].setVisible(true); 
					txtMLperm[i].setVisible(true);
					txtMLporosity[i].setVisible(true);
					txtMLskin[i].setVisible(true); 
					txtMLheff[i].setVisible(true);
				}
				for(int i = MLnumber; i<=5; i++){
					txtNum[i].setVisible(false); 
					txtNum[i+6].setVisible(false); 
					chkPlugged[i].setVisible(false);
					txtKOPvd[i].setVisible(false); 
					txtKOPangle[i].setVisible(false);
					txtKOP[i].setVisible(false); 
					txtKOPvd[i].setVisible(false); 
					txtKOPangle[i].setVisible(false);
					txtBUR[i].setVisible(false);
					txtEOBangle[i].setVisible(false);
					txtHold[i].setVisible(false);
					txtBUR2nd[i].setVisible(false); 
					txtEOB2nd[i].setVisible(false);
					txtHold2nd[i].setVisible(false);
					txtDia[i].setVisible(false);
					txtDiaMD[i].setVisible(false);
					txtDia2nd[i].setVisible(false);
					//txtDiaMD[i].setVisible(false); txtDia2nd[i].setVisible(false)
					txtPform[i].setVisible(false);
					txtMLperm[i].setVisible(false);
					txtMLporosity[i].setVisible(false);
					txtMLskin[i].setVisible(false);
					txtMLheff[i].setVisible(false);
				}
			}
		});

		sldML.setPaintTicks(true);
		sldML.setValue(3);
		sldML.setMajorTickSpacing(1);
		sldML.setMinimum(1);
		sldML.setMaximum(6);

		sldML.setBounds(CompSrtX+lblML.getWidth()+CompIntvX, CompSrtY, 118, 22);
		pnlMultiTrajt.add(sldML);		

		lblNumMult.setBounds(CompSrtX+lblML.getWidth()+CompIntvX*2+sldML.getWidth(), CompSrtY, 135, 15);
		pnlMultiTrajt.add(lblNumMult);		

		lblNote4.setForeground(Color.BLUE);
		lblNote4.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblNote4.setBounds(0, 0, 254, 45);
		lblNote4.setBounds(pnlMultiTrajtSizeX-lblNote4.getWidth()-2*CompIntvX, 5, 254, 45);
		pnlMultiTrajt.add(lblNote4);		

		// upper label
		UpperTxtSrtY=CompSrtY+sldML.getHeight()+CompIntvY*3;
		lblNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumber.setForeground(Color.BLACK);
		lblNumber.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblNumber.setBounds(CompSrtX, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblNumber);		
		lblpluggedisolated.setHorizontalAlignment(SwingConstants.CENTER);
		lblpluggedisolated.setForeground(Color.BLACK);
		lblpluggedisolated.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblpluggedisolated.setBounds(CompSrtX+lblSizeX+CompIntvX, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblpluggedisolated);				
		lblKOP.setHorizontalAlignment(SwingConstants.CENTER);
		lblKOP.setForeground(Color.BLACK);
		lblKOP.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblKOP.setBounds(CompSrtX+(lblSizeX+CompIntvX)*2, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblKOP);					
		lblKOPvd.setHorizontalAlignment(SwingConstants.CENTER);
		lblKOPvd.setForeground(Color.BLACK);
		lblKOPvd.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblKOPvd.setBounds(CompSrtX+(lblSizeX+CompIntvX)*3, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblKOPvd);				
		lblKOPangle.setHorizontalAlignment(SwingConstants.CENTER);
		lblKOPangle.setForeground(Color.BLACK);
		lblKOPangle.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblKOPangle.setBounds(CompSrtX+(lblSizeX+CompIntvX)*4, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblKOPangle);		
		lblBUR.setHorizontalAlignment(SwingConstants.CENTER);
		lblBUR.setForeground(Color.BLACK);
		lblBUR.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblBUR.setBounds(CompSrtX+(lblSizeX+CompIntvX)*5, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblBUR);				
		lblEOBangle.setHorizontalAlignment(SwingConstants.CENTER);
		lblEOBangle.setForeground(Color.BLACK);
		lblEOBangle.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblEOBangle.setBounds(CompSrtX+(lblSizeX+CompIntvX)*6, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblEOBangle);			
		lblHold.setHorizontalAlignment(SwingConstants.CENTER);
		lblHold.setForeground(Color.BLACK);
		lblHold.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblHold.setBounds(CompSrtX+(lblSizeX+CompIntvX)*7, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblHold);			
		lblBUR2nd.setHorizontalAlignment(SwingConstants.CENTER);
		lblBUR2nd.setForeground(Color.BLACK);
		lblBUR2nd.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblBUR2nd.setBounds(CompSrtX+(lblSizeX+CompIntvX)*8, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblBUR2nd);			
		lblEOB2nd.setHorizontalAlignment(SwingConstants.CENTER);
		lblEOB2nd.setForeground(Color.BLACK);
		lblEOB2nd.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblEOB2nd.setBounds(CompSrtX+(lblSizeX+CompIntvX)*9, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblEOB2nd);		
		lblHold2nd.setHorizontalAlignment(SwingConstants.CENTER);
		lblHold2nd.setForeground(Color.BLACK);
		lblHold2nd.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblHold2nd.setBounds(CompSrtX+(lblSizeX+CompIntvX)*10, UpperTxtSrtY, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblHold2nd);		
		MultilateralSp1.setBounds(CompSrtX-2, UpperTxtSrtY+lblSizeY+2, pnlMultiTrajtSizeX-CompSrtX, 2);
		pnlMultiTrajt.add(MultilateralSp1);		

		for(int j=0; j<6; j++){
			txtNum[j].setBounds(CompSrtX+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			chkPlugged[j].setBounds(CompSrtX+lblSizeX*3/2-CompSizeY/2-1, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeY+2, CompSizeY);
			txtKOP[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*2+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtKOPvd[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*3+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtKOPangle[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*4+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtBUR[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*5+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtEOBangle[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*6+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtHold[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*7+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtBUR2nd[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*8+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtEOB2nd[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*9+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);
			txtHold2nd[j].setBounds(CompSrtX+(lblSizeX+CompIntvX)*10+lblSizeX/2-CompSizeX/2, UpperTxtSrtY+lblSizeY+CompIntvY*(j+1)+CompSizeY*j, CompSizeX, CompSizeY);

			referIndex=j;
			txtKOP[j].getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent documentEvent) {
					printIt(documentEvent);
				}
				public void insertUpdate(DocumentEvent documentEvent) {
					printIt(documentEvent);
				}
				public void removeUpdate(DocumentEvent documentEvent) {
					printIt(documentEvent);
				}
				private void printIt(DocumentEvent documentEvent) {
					double mdKOP=0, vdKOP=0, angleKOP=0;
					try{
						mdKOP = Double.parseDouble(txtKOP[referIndex].getText());
						MainModule.getGeometry();
						MainModule.setMDvd();
						vdKOP = utilityModule.getVD(mdKOP);
						angleKOP = utilityModule.getAngle(mdKOP);
						txtKOPvd[referIndex].setText((new DecimalFormat("#####0.0#")).format(vdKOP));
						txtKOPangle[referIndex].setText((new DecimalFormat("##0.00#")).format(angleKOP)); 
					} catch(Exception e){
						vdKOP = 0;
						angleKOP = 0;
						txtKOPvd[referIndex].setText((new DecimalFormat("#####0.0#")).format(vdKOP));
						txtKOPangle[referIndex].setText((new DecimalFormat("##0.00#")).format(angleKOP)); 				
					}			
				}});
		}
		//		

		UpperTxtSrtY2 = UpperTxtSrtY+lblSizeY+CompIntvY*8+CompSizeY*6;
		lblNumber2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumber2.setForeground(Color.BLACK);
		lblNumber2.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblNumber2.setBounds(CompSrtX, UpperTxtSrtY2, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblNumber2);		
		lblDia.setHorizontalAlignment(SwingConstants.CENTER);
		lblDia.setForeground(Color.BLACK);
		lblDia.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblDia.setBounds(CompSrtX+lblSizeX+CompIntvY, UpperTxtSrtY2, lblSizeX3, lblSizeY);
		pnlMultiTrajt.add(lblDia);		
		lblDiaMD.setHorizontalAlignment(SwingConstants.CENTER);
		lblDiaMD.setForeground(Color.BLACK);
		lblDiaMD.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblDiaMD.setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY), UpperTxtSrtY2, lblSizeX3, lblSizeY);
		pnlMultiTrajt.add(lblDiaMD);		
		lblDia2nd.setHorizontalAlignment(SwingConstants.CENTER);
		lblDia2nd.setForeground(Color.BLACK);
		lblDia2nd.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblDia2nd.setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2, UpperTxtSrtY2, lblSizeX2, lblSizeY);
		pnlMultiTrajt.add(lblDia2nd);		
		lblPform.setHorizontalAlignment(SwingConstants.CENTER);
		lblPform.setForeground(Color.BLACK);
		lblPform.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblPform.setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY), UpperTxtSrtY2, lblSizeX2, lblSizeY);
		pnlMultiTrajt.add(lblPform);
		lblMLperm.setHorizontalAlignment(SwingConstants.CENTER);
		lblMLperm.setForeground(Color.BLACK);
		lblMLperm.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblMLperm.setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*2, UpperTxtSrtY2, lblSizeX2, lblSizeY);
		pnlMultiTrajt.add(lblMLperm);		
		lblMLporosity.setHorizontalAlignment(SwingConstants.CENTER);
		lblMLporosity.setForeground(Color.BLACK);
		lblMLporosity.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblMLporosity.setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3, UpperTxtSrtY2, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblMLporosity);		
		lblMLskin.setHorizontalAlignment(SwingConstants.CENTER);
		lblMLskin.setForeground(Color.BLACK);
		lblMLskin.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblMLskin.setBounds(CompSrtX+(lblSizeX+CompIntvY)*2+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3, UpperTxtSrtY2, lblSizeX, lblSizeY);
		pnlMultiTrajt.add(lblMLskin);		
		lblMLheff.setHorizontalAlignment(SwingConstants.CENTER);
		lblMLheff.setForeground(Color.BLACK);
		lblMLheff.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblMLheff.setBounds(CompSrtX+(lblSizeX+CompIntvY)*3+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3, UpperTxtSrtY2, 112, lblSizeY);
		pnlMultiTrajt.add(lblMLheff);		
		separator_1.setBounds(CompSrtX-2, UpperTxtSrtY2+lblSizeY+2, pnlMultiTrajtSizeX-CompSrtX, 2);
		pnlMultiTrajt.add(separator_1);

		for(int i=0; i<6; i++){
			/*txtNum[i+6].setBounds(CompSrtX+lblSizeX/2-CompSizeX/2, UpperTxtSrtY2+lblSizeY+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtDia[i].setBounds(CompSrtX+lblSizeX+CompIntvY, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, lblSizeX3, CompSizeY);
			txtDiaMD[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY), lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, lblSizeX3, lblSizeY);
			txtDia2nd[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, lblSizeX2, lblSizeY);
			txtPform[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY), lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, lblSizeX2, lblSizeY);
			txtMLperm[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, lblSizeX2, lblSizeY);
			txtMLporosity[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, lblSizeX, lblSizeY);
			txtMLskin[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)*2+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, lblSizeX, lblSizeY);
			txtMLheff[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)*3+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, 112, lblSizeY);*/

			txtNum[i+6].setBounds(CompSrtX+lblSizeX/2-CompSizeX/2, UpperTxtSrtY2+lblSizeY+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtDia[i].setBounds(CompSrtX+lblSizeX+CompIntvY+lblSizeX3/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtDiaMD[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)+lblSizeX3/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtDia2nd[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+lblSizeX2/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtPform[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)+lblSizeX2/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtMLperm[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*2+lblSizeX2/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtMLporosity[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3+lblSizeX/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtMLskin[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)*2+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3+lblSizeX/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtMLheff[i].setBounds(CompSrtX+(lblSizeX+CompIntvY)*3+(lblSizeX3+CompIntvY)*2+(lblSizeX2+CompIntvY)*3+112/2-CompSizeX/2, lblSizeY+UpperTxtSrtY2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
		}

	}

	void PoreFracturePSetting(){
		int CompSrtX = 10, CompSrtY = 10, CompSizeX = 60, CompSizeY = 20, CompIntvX=5, CompIntvY = 5, miniIntvY=2, lblSizeX = 80, lblSizeY=14;
		int PoreFracturePSrtX = 10, PoreFracturePSrtY=60, PoreFracturePSizeX=2*CompSrtX+7*CompIntvX+lblSizeX*3;
		int PoreFracturePSizeY=2*CompSrtY+45+CompSizeY*2+lblSizeY*2+2+CompIntvY*11+CompSizeY*11;
		int PlotTypeSrtX = PoreFracturePSrtX+PoreFracturePSizeX+20, PlotTypeSrtY=PoreFracturePSrtY, PlotTypeSizeX = 440, PlotTypeSizeY = +CompSizeY*2+3*CompIntvY;
		int GraphPnlXsize = PlotTypeSizeX, GraphPnlYsize = 325+30;
		int GraphXLoc =45, GraphYLoc = 35;
		int GraphColumeLabelSizeY=30;
		int GraphXsize = GraphPnlXsize-2*GraphXLoc;
		int GraphYsize = GraphPnlYsize-2*GraphYLoc+15-GraphColumeLabelSizeY;// 15 = label font size*/

		PnlPoreFractureP.setLayout(null);
		lblPoreFracWarn.setForeground(Color.BLUE);
		lblPoreFracWarn.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblPoreFracWarn.setBounds(242, 10, 489, 34);

		OKButton[9].setBounds(400,510,100,23);
		ApplyButton[9].setBounds(510,510,100,23);
		CancelButton[9].setBounds(620,510,100,23);
		PnlPoreFractureP.add(OKButton[9]);
		PnlPoreFractureP.add(ApplyButton[9]);
		//PnlPoreFractureP.add(CancelButton[9]);

		// Pore and Fracture Pressures
		lblPoreFractP.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblPoreFractP.setBounds(PoreFracturePSrtX+5, PoreFracturePSrtY-7, 190, 15);
		lblPoreFractP.setOpaque(true);
		PnlPoreFractureP.add(lblPoreFractP);
		PnlPoreFractureP.add(lblPoreFracWarn);
		pnlPoreFractP.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlPoreFractP.setBounds(PoreFracturePSrtX, PoreFracturePSrtY, PoreFracturePSizeX, PoreFracturePSizeY);		
		PnlPoreFractureP.add(pnlPoreFractP);
		pnlPoreFractP.setLayout(null);
		PoreFracturePMethodGroup.add(optEaton);
		optEaton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//............ This has been updated. 7/31/02
				//    iFG = 1
				//    txtFGinput.Enabled = False  ': lblFGinput.Visible = False
				//
				MainDriver.iFG = 1;
				//.................................. plot pore and fracture pressures
				if(optEMW.isSelected()==true) dummy = plot_Fracture(2, chartPoreP);   //1:pressure, 2:EMW
				else dummy = plot_Fracture(1, chartPoreP);   //1:pressure, 2:EMW
				//
				lblDepth.setVisible(false);
				lblPP.setVisible(false); 
				lblFP.setVisible(false);
				lblDepth2.setVisible(false);
				lblPP2.setVisible(false); 
				lblFP2.setVisible(false);
				linePP.setVisible(false); 
				VscrollPP.setVisible(false);
				for(int i = 0; i< VscrollPP.getValue(); i++){
					txtDepth[i].setVisible(false);
					txtPoreP[i].setVisible(false);
					txtFracP[i].setVisible(false);
				}
			}
		});
		optEaton.setFont(new Font("±¼¸²", Font.PLAIN, 11));
		optEaton.setBounds(CompSrtX, CompSrtY+10, 270, CompSizeY);
		pnlPoreFractP.add(optEaton);	
		PoreFracturePMethodGroup.add(optBarker);
		optBarker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//............ This has been updated. 7/31/02
				MainDriver.iFG = 2;
				//.................................. plot pore and fracture pressures
				if(optEMW.isSelected() == true) dummy = plot_Fracture(2, chartPoreP);   //1:pressure, 2:EMW
				else dummy = plot_Fracture(1, chartPoreP);   //1:pressure, 2:EMW
				//
				lblDepth.setVisible(false); 
				lblPP.setVisible(false); 
				lblFP.setVisible(false);
				lblDepth2.setVisible(false); 
				lblPP2.setVisible(false); 
				lblFP2.setVisible(false);
				linePP.setVisible(false);
				VscrollPP.setVisible(false);
				for(int i = 0; i<VscrollPP.getValue(); i++){
					txtDepth[i].setVisible(false);
					txtPoreP[i].setVisible(false);
					txtFracP[i].setVisible(false);
				}
			}
		});
		optBarker.setFont(new Font("±¼¸²", Font.PLAIN, 11));
		optBarker.setBounds(CompSrtX, CompSrtY+CompSizeY+10, 145, CompSizeY);
		pnlPoreFractP.add(optBarker);		
		optUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//... 7/31/02
				MainDriver.iFG = 0;
				//    assign_Pore_Fracture

				lblDepth.setVisible(true);
				lblPP.setVisible(true);
				lblFP.setVisible(true);
				lblDepth2.setVisible(true);
				lblPP2.setVisible(true);
				lblFP2.setVisible(true);
				linePP.setVisible(true);
				VscrollPP.setVisible(true);
				VscrollPP.setValue(3);
				VscrollPP.setValue(4);
				VscrollPP.setValue(MainDriver.iFGnum);

				int NppData = VscrollPP.getValue();
				for(int i = 1; i <= NppData; i++){
					txtDepth[i - 1].setVisible(true);
					txtPoreP[i - 1].setVisible(true);
					txtFracP[i - 1].setVisible(true);
					txtDepth[i - 1].setText((new DecimalFormat("#####0.0")).format(MainDriver.PPdepth[i-1])); 
					txtPoreP[i - 1].setText((new DecimalFormat("#####0.0")).format(MainDriver.PoreP[i-1]));
					txtFracP[i - 1].setText((new DecimalFormat("#####0.0")).format(MainDriver.FracP[i-1]));
				}
				for(int i = NppData; i<= 10; i++){
					txtDepth[i].setVisible(false);
					txtPoreP[i].setVisible(false);
					txtFracP[i].setVisible(false);
				}


				//
				//    For i = 0 To VscrollPP.Value - 1
				//        txtDepth(i).Visible = True
				//        txtPoreP(i).Visible = True
				//        txtFracP(i).Visible = True
				//    Next i
			}
		});
		optUser.setFont(new Font("±¼¸²", Font.PLAIN, 11));
		optUser.setBounds(CompSrtX, CompSrtY+2*CompSizeY+10, 83, CompSizeY);
		PoreFracturePMethodGroup.add(optUser);
		pnlPoreFractP.add(optUser);		
		cmdUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				read_Pore_Fracture();
				//.................................. plot pore nd fracture pressures
				if(optEMW.isSelected() == true) dummy=plot_Fracture(2, chartPoreP);   //1:pressure, 2:EMW
				else dummy = plot_Fracture(1, chartPoreP);   //1:pressure, 2:EMW
			}
		});
		cmdUpdate.setFont(new Font("±¼¸²", Font.BOLD, 11));
		cmdUpdate.setBounds(CompSrtX+CompIntvX*10+70, CompSrtY+CompSizeY*2+10, 160, 25);
		pnlPoreFractP.add(cmdUpdate);

		Line1.setBounds(0, CompSrtY+39+CompSizeY*2, PoreFracturePSizeX-2, 2);
		pnlPoreFractP.add(Line1);
		lblDepth.setHorizontalAlignment(SwingConstants.CENTER);

		lblDepth.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblDepth.setBounds(CompSrtX, CompSrtY+45+CompSizeY*2, lblSizeX, lblSizeY);
		pnlPoreFractP.add(lblDepth);
		lblDepth2.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblDepth2.setHorizontalAlignment(SwingConstants.CENTER);
		lblDepth2.setBounds(CompSrtX, CompSrtY+45+CompSizeY*2+lblSizeY, lblSizeX, lblSizeY);
		pnlPoreFractP.add(lblDepth2);
		lblPP.setHorizontalAlignment(SwingConstants.CENTER);
		lblPP.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblPP.setBounds(CompSrtX+lblSizeX+CompIntvX*2, CompSrtY+45+CompSizeY*2, lblSizeX, lblSizeY);
		pnlPoreFractP.add(lblPP);
		lblPP2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPP2.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblPP2.setBounds(CompSrtX+lblSizeX+CompIntvX*2, CompSrtY+45+CompSizeY*2+lblSizeY, lblSizeX, lblSizeY);
		pnlPoreFractP.add(lblPP2);		
		lblFP.setHorizontalAlignment(SwingConstants.CENTER);
		lblFP.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblFP.setBounds(CompSrtX+(lblSizeX+CompIntvX*2)*2, CompSrtY+45+CompSizeY*2, lblSizeX, lblSizeY);		
		pnlPoreFractP.add(lblFP);
		lblFP2.setHorizontalAlignment(SwingConstants.CENTER);
		lblFP2.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblFP2.setBounds(CompSrtX+(lblSizeX+CompIntvX*2)*2, CompSrtY+45+CompSizeY*2+lblSizeY, lblSizeX, lblSizeY);		
		pnlPoreFractP.add(lblFP2);

		linePP.setBounds(CompSrtX, CompSrtY+45+CompSizeY*2+lblSizeY*2, 3*lblSizeX+CompIntvX*4, 2);
		pnlPoreFractP.add(linePP);		

		for(int i=0; i<11; i++){
			txtDepth[i] = new JTextField();
			txtDepth[i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			pnlPoreFractP.add(txtDepth[i]);
			txtDepth[i].setBounds(CompSrtX+lblSizeX/2-CompSizeX/2, CompSrtY+45+CompSizeY*2+lblSizeY*2+2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtPoreP[i] = new JTextField();
			txtPoreP[i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			pnlPoreFractP.add(txtPoreP[i]);
			txtPoreP[i].setBounds(CompSrtX+lblSizeX+CompIntvX*2+lblSizeX/2-CompSizeX/2, CompSrtY+45+CompSizeY*2+lblSizeY*2+2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
			txtFracP[i] = new JTextField();
			txtFracP[i].setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
			pnlPoreFractP.add(txtFracP[i]);
			txtFracP[i].setBounds(CompSrtX+(lblSizeX+CompIntvX*2)*2+lblSizeX/2-CompSizeX/2, CompSrtY+45+CompSizeY*2+lblSizeY*2+2+CompIntvY*(i+1)+CompSizeY*i, CompSizeX, CompSizeY);
		}
		VscrollPP.setVisibleAmount(1);

		VscrollPP.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				//    iFGnum = VscrollPP.Value
				int NppData = VscrollPP.getValue();
				for(int i = 1; i <= NppData; i++){
					txtDepth[i - 1].setVisible(true);
					txtPoreP[i - 1].setVisible(true);
					txtFracP[i - 1].setVisible(true);
					txtDepth[i - 1].setText((new DecimalFormat("#####0.0")).format(MainDriver.PPdepth[i-1])); 
					txtPoreP[i - 1].setText((new DecimalFormat("#####0.0")).format(MainDriver.PoreP[i-1]));
					txtFracP[i - 1].setText((new DecimalFormat("#####0.0")).format(MainDriver.FracP[i-1]));
				}
				for(int i = NppData; i<= 10; i++){
					txtDepth[i].setVisible(false);
					txtPoreP[i].setVisible(false);
					txtFracP[i].setVisible(false);
				}
			}
		});

		VscrollPP.setValue(5);
		VscrollPP.setMinimum(3);
		VscrollPP.setMaximum(12);

		VscrollPP.setBounds(CompSrtX+3*lblSizeX+CompIntvX*4+2, CompSrtY+45+CompSizeY*2+lblSizeY*2+2, 17, 11*(CompIntvY+CompSizeY));		
		pnlPoreFractP.add(VscrollPP);
		//

		// Select the terms of graphs
		pnlPlotType.setLayout(null);
		pnlPlotType.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlPlotType.setBounds(PlotTypeSrtX, PlotTypeSrtY, PlotTypeSizeX, PlotTypeSizeY);		
		PnlPoreFractureP.add(pnlPlotType);
		WhatToPlotMethod.add(optPressure);
		optPressure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dummy = plot_Fracture(1, chartPoreP);
			}
		});
		optPressure.setBounds(CompSrtX, CompIntvY, 337, CompSizeY);		
		pnlPlotType.add(optPressure);
		WhatToPlotMethod.add(optEMW);
		optEMW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dummy = plot_Fracture(2, chartPoreP);
			}
		});
		optEMW.setBounds(CompSrtX, CompSizeY+2*CompIntvY, 411, CompSizeY);		
		pnlPlotType.add(optEMW);
		//

		// Plot
		lblChart.setHorizontalAlignment(SwingConstants.CENTER);
		lblChart.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblChart.setBounds(PlotTypeSrtX+PlotTypeSizeX/2-260/2, PlotTypeSrtY+PlotTypeSizeY, 260, 16);

		PnlPoreFractureP.add(lblChart);
		chartPoreP = new Sgraph3(GraphXsize, GraphYsize, GraphXLoc, GraphYLoc);
		chartPoreP.setBounds(PlotTypeSrtX, PlotTypeSrtY+PlotTypeSizeY+15, GraphPnlXsize, GraphPnlYsize);	

		chartPoreP.ColumnLabel[0] = new JLabel("Pore");
		chartPoreP.ColumnLabel[0].setBounds(60, GraphYLoc+GraphYsize+10, 40, 20);
		chartPoreP.add(chartPoreP.ColumnLabel[0]);		

		chartPoreP.ColumnLabel[1] = new JLabel("Frac.");
		chartPoreP.ColumnLabel[1].setBounds(60+100, GraphYLoc+GraphYsize+10, 40, 20);
		chartPoreP.add(chartPoreP.ColumnLabel[1]);		

		chartPoreP.ColumnLabel[2] = new JLabel("Annulus");
		chartPoreP.ColumnLabel[2].setBounds(60+100+100, GraphYLoc+GraphYsize+10, 60, 20);
		chartPoreP.add(chartPoreP.ColumnLabel[2]);

		PnlPoreFractureP.add(chartPoreP);
		//

	}

	void read_Pore_Fracture(){
		//------------  Assing INPUT variables
		// 7/31/02
		if (MainDriver.iFG== 0){  //user input up t0 11 points
			MainDriver.iFGnum = VscrollPP.getValue();
			for(int i = 0; i<MainDriver.iFGnum; i++){
				MainDriver.PPdepth[i] = Double.parseDouble(txtDepth[i].getText());
				MainDriver.PoreP[i] = Double.parseDouble(txtPoreP[i].getText());
				MainDriver.FracP[i] = Double.parseDouble(txtFracP[i].getText());
			}
		}
	}

	int check_Pore_Fracture(){
		badData = 0;
		//
		if (MainDriver.iFG == 0){  //user input up tp 11 points
			for(int i = 0; i<MainDriver.iFGnum - 1; i++){
				if(MainDriver.PPdepth[i] > MainDriver.PPdepth[i + 1]){
					String msg = "Input depth data for pore and fracture pressures are not in sequence !";
					JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
					badData = 1;
					return -1;
				}
			}
		}
		return 0;
	}

	class Sgraph3 extends JPanel{
		int ColumnCount = 0;
		int RowCount = 0;
		int ColumnLabelCount = 0;
		JLabel[] ColumnLabel = new JLabel[3];
		int Column =0, Row=0;	    
		int GraphXsize=100, GraphYsize=100, GraphXLoc = 1, GraphYLoc =1;	    
		double sgYMaxLim=1, sgYMinLim=0;
		double sgXMax=1, sgXMin=1, sgYMax=1, sgYMin=1;
		double sgX2Max=1, sgX2Min=1, sgY2Max=1, sgY2Min=1;
		double sgX3Max=1, sgX3Min=1, sgY3Max=1, sgY3Min=1;
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
		double[] Sgx3Data = new double[MainDriver.Npt+1];
		double[] Sgy3Data = new double[MainDriver.Npt+1];

		double x1=0, x2=0, y1=0, y2=0;

		JLabel lblTitle;
		JLabel[] sgx = new JLabel[15]; // x-axis number tag
		JLabel[] sgy = new JLabel[15]; // y-axis number tag

		Sgraph3(int SizeX, int SizeY, int LocX, int LocY){
			this.setBackground(Color.white);
			this.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));						
			this.setLayout(null);
			for(int i=0; i<15; i++){
				sgx[i] = new JLabel("");
				sgy[i] = new JLabel("");
				add(sgx[i]);
				add(sgy[i]);
			}
			GraphXsize=SizeX;
			GraphYsize=SizeY;
			GraphXLoc =LocX;
			GraphYLoc =LocY;
		}

		public void paint(Graphics g){
			try{
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
						sgy[x].setBounds(GraphXLoc-43, GraphYLoc+i-5, 43, 11);
						sgy[x].setVisible(true);
					}
				}

				for(int i=0; i<GraphXsize+1; i=i+intvX){
					g.drawLine(GraphXLoc+i, GraphYLoc, GraphXLoc+i, GraphYLoc+GraphYsize+3);
					int x=i/intvX;
					if(filter == 1 && x<=sepXnum){
						sgx[x].setBounds(GraphXLoc+i-18, GraphYLoc-5-15, 43, 11);
						sgx[x].setVisible(true);
					}
				}

				xAdjCon = GraphXsize - sepXnum*intvX;
				yAdjCon = GraphYsize - sepYnum*intvY;

				drawwidth = 3;
				g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 	

				g.setColor(Color.RED);
				g.drawLine(40, GraphYLoc+GraphYsize+10+10, 50, GraphYLoc+GraphYsize+10+10);
				g.setColor(Color.BLUE);
				g.drawLine(40+100, GraphYLoc+GraphYsize+10+10, 40+100+10, GraphYLoc+GraphYsize+10+10);
				g.setColor(Color.PINK);
				g.drawLine(40+100*2, GraphYLoc+GraphYsize+10+10, 40+100*2+10, GraphYLoc+GraphYsize+10+10);

				for(int i=0; i<RowCount-1; i++){ // TOTAL NUMBER OF POINT = Rowcount , so the number of line is equal to RowCount-1				
					x1=SgxData[i];
					x2=SgxData[i+1];
					y1=SgyData[i];
					y2=SgyData[i+1];

					if(sgYMaxLim<SgyData[i] && sgYMaxLim>SgyData[i+1]){
						x1 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMaxLim-SgyData[i])+SgxData[i];
						y1 = sgYMaxLim;
					}
					else if(sgYMinLim>SgyData[i] && sgYMinLim<SgyData[i+1]){
						x1 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMinLim-SgyData[i+1])+SgxData[i+1];
						y1 = sgYMinLim;
					}

					if(sgYMaxLim<SgyData[i+1] && sgYMaxLim>SgyData[i]){
						x2 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMaxLim-SgyData[i+1])+SgxData[i+1];
						y2 = sgYMaxLim;
					}
					else if(sgYMinLim>SgyData[i+1] && sgYMinLim<SgyData[i]){
						x2 = (SgxData[i+1]-SgxData[i])/(SgyData[i+1]-SgyData[i])*(sgYMinLim-SgyData[i])+SgxData[i];
						y2 = sgYMinLim;
					}

					if((sgYMinLim>SgyData[i+1] && sgYMinLim>SgyData[i])||(sgYMaxLim<SgyData[i+1] && sgYMaxLim<SgyData[i])){
						x1=-100000;
						x2=-100000;
						y1=-100000;
						y2=-100000;
					}
					g.setColor(Color.RED);				

					g.drawLine(scx(x1, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y1, sgyMaxAdj, sgyMinAdj, yAdjCon), scx(x2, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y2, sgyMaxAdj, sgyMinAdj, yAdjCon));
					//g.drawLine(scx(SgxData[i], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(SgyData[i], sgyMaxAdj, sgyMinAdj, yAdjCon), scx(SgxData[i+1], sgxMaxAdj, sgxMinAdj, xAdjCon), scy(SgyData[i+1], sgyMaxAdj, sgyMinAdj, yAdjCon));

					x1=Sgx2Data[i];
					x2=Sgx2Data[i+1];
					y1=Sgy2Data[i];
					y2=Sgy2Data[i+1];

					if(sgYMaxLim<Sgy2Data[i] && sgYMaxLim>Sgy2Data[i+1]){
						x1 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i])+Sgx2Data[i];
						y1 = sgYMaxLim;
					}
					else if(sgYMinLim>Sgy2Data[i] && sgYMinLim<Sgy2Data[i+1]){
						x1 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i+1])+Sgx2Data[i+1];
						y1 = sgYMinLim;
					}				

					if(sgYMaxLim<Sgy2Data[i+1] && sgYMaxLim>Sgy2Data[i]){
						x2 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i+1])+Sgx2Data[i+1];
						y2 = sgYMaxLim;
					}
					else if(sgYMinLim>Sgy2Data[i+1] && sgYMinLim<Sgy2Data[i]){
						x2 = (Sgx2Data[i+1]-Sgx2Data[i])/(Sgy2Data[i+1]-Sgy2Data[i])*(sgYMaxLim-Sgy2Data[i])+Sgx2Data[i];
						y2 = sgYMinLim;
					}	

					if((sgYMinLim>Sgy2Data[i+1] && sgYMinLim>Sgy2Data[i])||(sgYMaxLim<Sgy2Data[i+1] && sgYMaxLim<Sgy2Data[i])){
						x1=-100000;
						x2=-100000;
						y1=-100000;
						y2=-100000;
					}
					g.setColor(Color.BLUE);					
					g.drawLine(scx(x1, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y1, sgyMaxAdj, sgyMinAdj, yAdjCon), scx(x2, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y2, sgyMaxAdj, sgyMinAdj, yAdjCon));

					x1=Sgx3Data[i];
					x2=Sgx3Data[i+1];
					y1=Sgy3Data[i];
					y2=Sgy3Data[i+1];

					if(sgYMaxLim<Sgy3Data[i] && sgYMaxLim>Sgy3Data[i+1]){
						x1 = (Sgx3Data[i+1]-Sgx3Data[i])/(Sgy3Data[i+1]-Sgy3Data[i])*(sgYMaxLim-Sgy3Data[i])+Sgx3Data[i];
						y1 = sgYMaxLim;
					}
					else if(sgYMinLim>Sgy3Data[i] && sgYMinLim<Sgy3Data[i+1]){
						x1 = (Sgx3Data[i+1]-Sgx3Data[i])/(Sgy3Data[i+1]-Sgy3Data[i])*(sgYMaxLim-Sgy3Data[i+1])+Sgx3Data[i+1];
						y1 = sgYMinLim;
					}				

					if(sgYMaxLim<Sgy3Data[i+1] && sgYMaxLim>Sgy3Data[i]){
						x2 = (Sgx3Data[i+1]-Sgx3Data[i])/(Sgy3Data[i+1]-Sgy3Data[i])*(sgYMaxLim-Sgy3Data[i+1])+Sgx3Data[i+1];
						y2 = sgYMaxLim;
					}
					else if(sgYMinLim>Sgy3Data[i+1] && sgYMinLim<Sgy3Data[i]){
						x2 = (Sgx3Data[i+1]-Sgx3Data[i])/(Sgy3Data[i+1]-Sgy3Data[i])*(sgYMaxLim-Sgy3Data[i])+Sgx3Data[i];
						y2 = sgYMinLim;
					}	

					if((sgYMinLim>Sgy3Data[i+1] && sgYMinLim>Sgy3Data[i])||(sgYMaxLim<Sgy3Data[i+1] && sgYMaxLim<Sgy3Data[i])){
						x1=-100000;
						x2=-100000;
						y1=-100000;
						y2=-100000;
					}
					g.setColor(Color.PINK);				
					g.drawLine(scx(x1, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y1, sgyMaxAdj, sgyMinAdj, yAdjCon), scx(x2, sgxMaxAdj, sgxMinAdj, xAdjCon), scy(y2, sgyMaxAdj, sgyMinAdj, yAdjCon));
				}
				sg2Use=0;
			}catch(Exception e){
				System.out.println("error in Pore and Fractrue Pressures vs. Depth sgraph");
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

		void calcGraphIntv(double MaxLim, double MinLim){
			int dummy=0;	

			for(int i=0; i<RowCount; i++){
				if(i==0){
					sgXMax=SgxData[i];
					sgXMin=SgxData[i];
					sgYMax=SgyData[i];
					sgYMin=SgyData[i];		        	

					sgX2Max=Sgx2Data[i];
					sgX2Min=Sgx2Data[i];
					sgY2Max=Sgy2Data[i];
					sgY2Min=Sgy2Data[i];	

					sgX3Max=Sgx3Data[i];
					sgX3Min=Sgx3Data[i];
					sgY3Max=Sgy3Data[i];
					sgY3Min=Sgy3Data[i];
				}

				else{
					if(SgxData[i]>sgXMax) sgXMax=SgxData[i];
					if(SgxData[i]<sgXMin) sgXMin=SgxData[i];
					if(SgyData[i]>sgYMax) sgYMax=SgyData[i];
					if(SgyData[i]<sgYMin) sgYMin=SgyData[i];

					if(Sgx2Data[i]>sgX2Max) sgX2Max=Sgx2Data[i];
					if(Sgx2Data[i]<sgX2Min) sgX2Min=Sgx2Data[i];
					if(Sgy2Data[i]>sgY2Max) sgY2Max=Sgy2Data[i];
					if(Sgy2Data[i]<sgY2Min) sgY2Min=Sgy2Data[i];

					if(Sgx3Data[i]>sgX3Max) sgX3Max=Sgx3Data[i];
					if(Sgx3Data[i]<sgX3Min) sgX3Min=Sgx3Data[i];
					if(Sgy3Data[i]>sgY3Max) sgY3Max=Sgy3Data[i];
					if(Sgy3Data[i]<sgY3Min) sgY3Min=Sgy3Data[i];
				}		        
			}

			sgXMax = MaxMinFind(sgXMax, sgX2Max, sgX3Max)[0];
			sgXMin = MaxMinFind(sgXMin, sgX2Min, sgX3Min)[2];
			sgYMax = MaxMinFind(sgYMax, sgY2Max, sgY3Max)[0];
			sgYMin = MaxMinFind(sgYMin, sgY2Min, sgY3Min)[2];						

			if(sgYMax>MaxLim) sgYMax = MaxLim;
			if(sgYMin<MinLim) sgYMin = MinLim;

			while(dummy==0){				
				if((sgXMax-sgXMin)/sepXIntv>1 && (sgXMax-sgXMin)/sepXIntv<=10) dummy=1;
				else if((sgXMax-sgXMin)/sepXIntv<=0) dummy=1;
				else if((sgXMax-sgXMin)/sepXIntv<=1) sepXIntv=sepXIntv/10;		    	
				else sepXIntv=sepXIntv*10;	    	
			}

			while(dummy==1&&sepYIntv!=0){		    
				if((sgYMax-sgYMin)/sepYIntv>1 && (sgYMax-sgYMin)/sepYIntv<=10) dummy=0;
				else if((sgYMax-sgYMin)/sepYIntv<=0) dummy=0;
				else if((sgYMax-sgYMin)/sepYIntv<=1) sepYIntv = sepYIntv/10;		    	
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
				else sgyMinAdj = (int)(sgYMin/sepYIntv)*sepYIntv;

				sepYnum=(int) ((sgyMaxAdj-sgyMinAdj)/sepYIntv);
				if(sepYnum>=4 && sepYnum<11) dummy=0;
				else if(sepYnum<4) sepYIntv=sepYIntv/2;
				else sepYIntv+=sepYIntv;
			} // we will use sepXnum+1 labels and sepYnum+1 labels. 
			filter = 1;
			for(int i=0; i<sepXnum+1; i++){			
				sgx[i].setText(Integer.toString((int)(sgxMinAdj+i*sepXIntv)));
				sgx[i].setFont(new Font("±¼¸²", Font.BOLD, 10));
				sgx[i].setHorizontalAlignment(SwingConstants.CENTER);

				add(sgx[i]);
			}
			for(int i=0; i<sepYnum+1; i++){
				sgy[i].setText(Integer.toString((int)(sgyMinAdj+i*sepYIntv)));
				sgy[i].setFont(new Font("±¼¸²", Font.BOLD, 10));
				sgy[i].setHorizontalAlignment(SwingConstants.CENTER);
				add(sgy[i]);
			}
			dummy=0;
		}

		double[] MaxMinFind(double a1, double a2, double a3){
			double max=0, mid=0, min=0;
			double[] result = new double[3];
			if(a1>=a2){
				max=a1;
				min=a2;
			}
			else{
				max=a2;
				min=a1;
			}
			if(a3>=max) max=a3;
			else{
				if(a3<min) min = a3;
			}
			mid = a1+a2+a3-max-min;
			result[0]=max;
			result[1]=mid;
			result[2]=min;
			return result;
		}		
	}	

	int plot_Fracture(int iEMW, Sgraph3 sg3){ //aligning the Data
		// iEMW = 2 plot in equivalent mud weight (EMW), ppg
		//        1 plot in pressure, psig
		//
		//........... a sub to calculate and plot pore and fracture pressures
		// 7/31/02 @TAMU
		//
		int dataPoint = 11;
		double p_seafloor=0, EMW_conversion=0, p_annulus=0;
		//Dim swDensity As double
		//
		//   swDensity = 8.6  //ppg, assignned on 7/31/02, global variable on 1/23/03
		p_seafloor = 0.052 * MainDriver.swDensity * MainDriver.Dwater; //TY ±×¸² ±×¸± ¶§ º¯ÇÏ´Â º¯¼ö
		//
		MainDriver.PPdepth[0] = 0; 
		MainDriver.PoreP[0] = 0;
		MainDriver.FracP[0] = 0;

		if(iEMW == 1) lblChart.setText("Pore and Fracture Pressures vs. Depth");
		else if(iEMW == 2) lblChart.setText("Equivalent Mud Weight vs. Depth");

		if(MainDriver.iFG == 1 || MainDriver.iFG == 2){    //Eaton//s method (1)
			calcPoreFracture();
		}
		else{                   //user input
			read_Pore_Fracture();
			check_Pore_Fracture();
			if(badData != 0) return -1;
			dataPoint = MainDriver.iFGnum;
		}
		//----------------------------7/31/02
		//..................................................................... assign data into chart cells
		sg3.ColumnCount = 6;           //3 data set: PP, FP, Wellbore P
		//chartPoreP.AutoIncrement = False     //disable automatic incresement
		sg3.ColumnLabelCount = 1;
		//chartPoreP.Title.VtFont.Size = 12		
		//g.drawLine(40+100*2, GraphYLoc+GraphYsize+10+9, 10, 2);

		//.... Put chart title outside of the plot to save space: 1/23/03 @TAMU
		//        If iEMW = 2 Then
		//           chartPoreP.TitleText = "Equivalent Mud Weight (ppg) vs. Depth (ft)"
		//        Else
		//           chartPoreP.TitleText = "Pressures (psig) vs. Depth (ft)"
		//        End If
		//

		if(MainDriver.iOnshore== 1){   //onshore case - use the given data directly
			sg3.RowCount = dataPoint;  //total number of data

			sg3.Row = 1;
			//sg3.Column = 1         //first column
			sg3.SgxData[0] = MainDriver.PoreP[0];    //specify x data
			//sg3.Column = 2         //second column
			sg3.SgyData[0] = 0;
			//sg3.Column = 3         //3rd column
			sg3.Sgx2Data[0] = MainDriver.FracP[0];    //specify x data
			//sg3.Column = 4         //4th column
			sg3.Sgy2Data[0] = 0;   //specify y data
			//sg3.Column = 5         //5th column
			sg3.Sgx3Data[0] = 0;   //specify x data
			//sg3.Column = 6         //6th column
			sg3.Sgy3Data[0] = 0;   //specify y data
			//

			if(iEMW == 2){
				EMW_conversion = 0.052 * MainDriver.PPdepth[1];
				//sg3.Column = 1         //first column
				sg3.SgxData[0] = MainDriver.PoreP[1] / EMW_conversion;   //specify x data
				//sg3.Column = 3         //3rd column
				sg3.Sgx2Data[0] = MainDriver.FracP[1] / EMW_conversion;   //specify x data
				//sg3.Column = 5         //5th column
				sg3.Sgx3Data[0] = MainDriver.oMud;   //specify x data   TY ÀÌ ºÎºÐ ±×·¡ÇÁ ±×¸± ¶§ º¯ÇÏ´Â º¯¼ö
			}

			for(int i = 1; i<dataPoint; i++){
				EMW_conversion = 1;
				if(iEMW == 2) EMW_conversion = 0.052 * MainDriver.PPdepth[i];
				sg3.Row = i+1;
				//sg3.Column = 1         //first column
				sg3.SgxData[i] = MainDriver.PoreP[i] / EMW_conversion;   //specify x data
				//sg3.Column = 2         //second column
				sg3.SgyData[i] = -(int)MainDriver.PPdepth[i];  //specify y data
				//sg3.Column = 3         //3rd column
				sg3.Sgx2Data[i] = MainDriver.FracP[i] / EMW_conversion;   //specify x data
				//sg3.Column = 4         //4th column
				sg3.Sgy2Data[i] = -(int)MainDriver.PPdepth[i];   //specify y data
				//sg3.Column = 5         //3rd column
				p_annulus = 0.052 * MainDriver.oMud * MainDriver.PPdepth[i];
				sg3.Sgx3Data[i] = p_annulus / EMW_conversion;  //specify x data
				//sg3.Column = 6         //4th column
				sg3.Sgy3Data[i] = -(int)MainDriver.PPdepth[i];   //specify y data
			}
		}

		else{   //.... for offshore wells - consider water depth
			sg3.RowCount = dataPoint + 1;  //total number of data + one extra point for sea water

			sg3.Row = 1;
			//sg3.Column = 1         //first column
			sg3.SgxData[0] = 0;   //specify x data
			//sg3.Column = 2         //second column
			sg3.SgyData[0] = 0;
			//sg3.Column = 3         //3rd column
			sg3.Sgx2Data[0] = 0;    //specify x data
			//sg3.Column = 4         //4th column
			sg3.Sgy2Data[0] = 0;   //specify y data
			//sg3.Column = 5         //5th column
			sg3.Sgx3Data[0] = 0;   //specify x data
			//sg3.Column = 6         //6th column
			sg3.Sgy3Data[0] = 0;   //specify y data
			//
			if(iEMW == 2){
				//sg3.Column = 1         //first column
				sg3.SgxData[0] = MainDriver.swDensity;   //specify x data
				//sg3.Column = 3         //3rd column
				sg3.Sgx2Data[0] = MainDriver.swDensity;   //specify x data
				//sg3.Column = 5         //5th column
				sg3.Sgx3Data[0] = MainDriver.oMud;   //specify x data
			}

			for(int i = 1; i<dataPoint; i++){
				EMW_conversion = 1;
				if(iEMW == 2) EMW_conversion = 0.052 * MainDriver.PPdepth[i];
				sg3.Row = i+1;
				//sg3.Column = 1         //first column
				sg3.SgxData[i] = MainDriver.PoreP[i] / EMW_conversion;   //specify x data
				//sg3.Column = 2         //second column
				sg3.SgyData[i] = -(int)MainDriver.PPdepth[i];  //specify y data
				//sg3.Column = 3         //3rd column
				sg3.Sgx2Data[i] = MainDriver.FracP[i] / EMW_conversion;   //specify x data
				//sg3.Column = 4         //4th column
				sg3.Sgy2Data[i] = -(int)MainDriver.PPdepth[i];   //specify y data
				//sg3.Column = 5         //3rd column
				p_annulus = 0.052 * MainDriver.oMud * MainDriver.PPdepth[i];
				sg3.Sgx3Data[i] = p_annulus / EMW_conversion; //specify x data
				//sg3.Column = 6         //4th column
				sg3.Sgy3Data[i] = -(int)MainDriver.PPdepth[i];   //specify y data
			}

			EMW_conversion = 1;

			for(int i = 0; i<dataPoint; i++){
				if(iEMW == 2) EMW_conversion = 0.052 * (MainDriver.Dwater + MainDriver.PPdepth[i]);
				sg3.Row = i + 1;
				//sg3.Column = 1         //first column
				sg3.SgxData[i+1] = MainDriver.PoreP[i] / EMW_conversion;   //specify x data
				//sg3.Column = 2         //second column
				sg3.SgyData[i+1] = -(int)MainDriver.Dwater - (int)MainDriver.PPdepth[i]; //specify y data
				//sg3.Column = 3         //3rd column
				sg3.Sgx2Data[i+1] = MainDriver.FracP[i] / EMW_conversion;   //specify x data
				//sg3.Column = 4         //4th column
				sg3.Sgy2Data[i+1] = -(int)MainDriver.Dwater - (int)MainDriver.PPdepth[i];  //specify y data
				//sg3.Column = 5         //3rd column
				p_annulus = 0.052 * MainDriver.oMud * (MainDriver.PPdepth[i] + MainDriver.Dwater);
				sg3.Sgx3Data[i+1] = p_annulus / EMW_conversion;  //specify x data
				//sg3.Column = 6         //4th column
				sg3.Sgy3Data[i+1] = -(int)MainDriver.Dwater - (int)MainDriver.PPdepth[i];  //specify y data  //TY ±×·¡ÇÁ ±×¸±¶§ º¯ÇÏ´Â º¯¼ö
			}
		}
		sg3.sgYMaxLim=1000;
		sg3.sgYMinLim=-15000;
		sg3.calcGraphIntv(sg3.sgYMaxLim, sg3.sgYMinLim);
		sg3.repaint();

		return 0;
		//
		//sg3.Plot.UniformAxis = False
		//sg3.Visible = True
		//sg3.Legend.Location.LocationType = VtChLocationTypeBottom*/
		//        sg3.Plot.Axis(VtChAxisIdY).ValueScale.Maximum = 0
		//        sg3.Plot.Axis(VtChAxisIdY).ValueScale.Minimum = -30000
		//        sg3.Plot.Axis(VtChAxisIdX).ValueScale.Maximum = 12000
		//        sg3.Plot.Axis(VtChAxisIdX).ValueScale.Minimum = 0
		//
	}

	void ExTrjtSetting(){		
		PnlExWellTrajt.setLayout(null);
		ExTrjtPnl.setBounds(FrameXsize/2-320,60,600,370);
		//ExTrjtPnl.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlExWellTrajt.add(ExTrjtPnl);		
		ExTrjtPnl.setVisible(true);
		PnlExWellTrajt.setVisible(true);
	}

	class ImagePanel extends JPanel{
		ImagePanel(){
			setLayout(null);			
		}

		public void paint(Graphics g){
			super.paint(g);
			g.drawImage(MainDriver.img, 0, 0, 610, 380, this);
		}
	}

	void Form_Load(){		
		for(int i = 0; i<=10; i++){
			txtDepth[i].setText("0");
			txtPoreP[i].setText("0"); 
			txtFracP[i].setText("0");
		}
		//------------------- modifications after Dec. 10, 2002
		badData = 0;
		for(int i = 0; i<=5; i++){
			txtNum[i].setText(Integer.toString(i + 1));
			txtNum[i + 6].setText(Integer.toString(i + 1));
			txtKOPvd[i].setText("0");
			txtKOPangle[i].setText("0");
			//
			txtKOP[i].setText("0"); 
			txtKOPvd[i].setText("0"); 
			txtKOPangle[i].setText("0");
			txtBUR[i].setText("0"); 
			txtEOBangle[i].setText("0"); 
			txtHold[i].setText("0");
			txtBUR2nd[i].setText("0");
			txtEOB2nd[i].setText("0"); 
			txtHold2nd[i].setText("0");
			txtDia[i].setText("0"); 
			txtDiaMD[i].setText("0");
			txtDia2nd[i].setText("0");
			txtPform[i].setText("0"); 
			txtMLperm[i].setText("0"); 
			txtMLporosity[i].setText("0");
			txtMLskin[i].setText("0"); 
			txtMLheff[i].setText("0");
		}
		txtFinalHoldLength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtFinalHoldLength.setText("0");



		Assign_Options();
		Assign_Fluid_Bit();
		Assign_Pump();
		Assign_Cduct_Offshore();
		Assign_Formation();
		Assign_WellGeometry();
		assign_Pore_Fracture();
		//Assign_Multilateral();
		//Added by ty
		Assign_mud();
		Assign_Heattransfer();
		FormLoadIndex=1;
		
		if(MainDriver.icase_mode==0){
			JTabInputData.setEnabledAt(1, false);
			JTabInputData.setBackgroundAt(1, Color.gray);
			JTabInputData.setEnabledAt(2, false);
			JTabInputData.setBackgroundAt(2, Color.gray);
			JTabInputData.setEnabledAt(3, false);
			JTabInputData.setBackgroundAt(3, Color.gray);
			JTabInputData.setEnabledAt(4, false);
			JTabInputData.setBackgroundAt(4, Color.gray);
			JTabInputData.setEnabledAt(5, false);
			JTabInputData.setBackgroundAt(5, Color.gray);
			JTabInputData.setEnabledAt(6, false);
			JTabInputData.setBackgroundAt(6, Color.gray);
			JTabInputData.setEnabledAt(7, false);
			JTabInputData.setBackgroundAt(7, Color.gray);
			JTabInputData.setEnabledAt(8, false);
			JTabInputData.setBackgroundAt(8, Color.gray);
			JTabInputData.setEnabledAt(9, false);
			JTabInputData.setBackgroundAt(9, Color.gray);
		}
		else{
			JTabInputData.setEnabledAt(1, true);
			JTabInputData.setBackgroundAt(1, new Color(240,240,240));
			JTabInputData.setEnabledAt(2, true);
			JTabInputData.setBackgroundAt(2, new Color(240,240,240));
			JTabInputData.setEnabledAt(3, true);
			JTabInputData.setBackgroundAt(3, new Color(240,240,240));
			JTabInputData.setEnabledAt(4, true);
			JTabInputData.setBackgroundAt(4, new Color(240,240,240));
			JTabInputData.setEnabledAt(5, true);
			JTabInputData.setBackgroundAt(5, new Color(240,240,240));
			JTabInputData.setEnabledAt(6, true);
			JTabInputData.setBackgroundAt(6, new Color(240,240,240));
			JTabInputData.setEnabledAt(7, true);
			JTabInputData.setBackgroundAt(7, new Color(240,240,240));
			JTabInputData.setEnabledAt(8, true);
			JTabInputData.setBackgroundAt(8, new Color(240,240,240));
			JTabInputData.setEnabledAt(9, true);
			JTabInputData.setBackgroundAt(9, new Color(240,240,240));
		}
		
		if(optML.isSelected()==true){
			JTabInputData.setEnabledAt(12, true);
			JTabInputData.setBackgroundAt(12, new Color(240,240,240));			
		}
		else{
			JTabInputData.setEnabledAt(12, false);
			JTabInputData.setBackgroundAt(12, Color.gray);
		}
		if(optOBM.isSelected()==true){
			JTabInputData.setEnabledAt(10, true);
			JTabInputData.setBackgroundAt(10, new Color(240,240,240));
		}
		else{
			JTabInputData.setEnabledAt(10, false);
			JTabInputData.setBackgroundAt(10, Color.gray);
		}
		if(optWithHT.isSelected()==true){
			JTabInputData.setEnabledAt(11, true);
			JTabInputData.setBackgroundAt(11, new Color(240,240,240));
		}
		else{
			JTabInputData.setEnabledAt(11, false);
			JTabInputData.setBackgroundAt(11, Color.gray);
		}
	}

	void check_Muddata(){
		double sum_base=0, sum_mud=0;
		String msg="";
		badData = 0;

		sum_base = sum_base + Double.parseDouble(txtbasecom8.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom9.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom10.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom11.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom12.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom13.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom14.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom15.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom16.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom17.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom18.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom19.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom20.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom21.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom22.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom23.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom24.getText());
		sum_base = sum_base + Double.parseDouble(txtbasecom25.getText());

		if(sum_base != 100){
			msg =  "Sum of the base oil's composition is not equal to 100 !";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		sum_mud = Double.parseDouble(txtbaseoilf.getText()) + Double.parseDouble(txtbrinef.getText()) + Double.parseDouble(txtAdditivef.getText());

		if (sum_mud != 1){
			msg =  "Sum of the oil based mud's composition is not equal to 1 !";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
	}

	void check_Multilateral(){
		//...... check multilateral data   //Jan. 2, 2003
		//       Possibly allow negative build-up and build-up angle of haiger than 90 degree.
		//
		String msg = "";
		double xAngle=0;
		for(int i = 0; i<=(MainDriver.igMLnumber - 1); i++){
			if(MainDriver.mlKOP[i] < MainDriver.Dwater + 25.5 ){
				msg = "KOP for multilateral is less than water depth or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(Math.abs(MainDriver.mlBUR[i]) < 0.08 ){
				msg = "First BUR for multilateral is zero or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlHold[i] < -0.08 || MainDriver.mlHold2nd[i] < -0.08 ){
				msg = "Hold length after build for multilateral is negative or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			xAngle = utilityModule.getAngle(MainDriver.mlKOP[i]);

			if(MainDriver.mlBUR[i] > 0 && MainDriver.mlEOB[i] < xAngle ){
				msg = "Angle BUR and angle at the EOB are not consistent !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(Math.abs(MainDriver.mlEOB[i]) > Math.abs(MainDriver.mlEOB2nd[i]) ){
				msg = "Angle at the end of 2nd build (in absolute value) should be equal or greater than that of the first for ML !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlEOB[i] * MainDriver.mlEOB2nd[i] < 0 ){
				msg = "Angles at the end of build are not consistent !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlDia[i] < 1.4 ){   //Or mlDia2nd[i] < 1.3 ){
				msg = "Hole diameter for multilateral is is too small or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlDiaMD[i] < -0.002 ){
				msg = "Measured length of the first uniform diameter section is negative or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlPform[i] < 14.7 ){
				msg = "Formation pressure for ML is too small or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlPerm[i] < 0.0001 || MainDriver.mlPerm[i] > 500000 ){
				msg = "Formation permeability in md for ML is too small or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlPorosity[i] < 0.01 || MainDriver.mlPorosity[i] > 0.65 ){
				msg = "Formation porosity for ML is too small or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.mlHeff[i] < -0.02 || MainDriver.mlHeff[i] > 1000.5 ){
				msg = "Effective flow length for kick influx for ML is too small or out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}
	}

	void check_Cduct_Offshore(){
		badData = 0;
		String msg = "";

		if (MainDriver.iCduct == 1) {
			if (MainDriver.ODcduct <= MainDriver.IDcasing) {
				msg = "Conductor diameter is less than casing diameter";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if (MainDriver.DepthCduct >= MainDriver.DepthCasing) {
				msg = "Conductor setting depth is too deep: Deeper than the last casing seat";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}

		if (MainDriver.iSurfCsg == 1) {
			if (MainDriver.ODsurfCsg <= MainDriver.IDcasing) {
				msg = "Surface casing diameter is less than last casing ID";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if (MainDriver.DepthSurfCsg >= MainDriver.DepthCasing) {
				msg = "Surface casing setting depth is too deep: Deeper than the last casing seat";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}

		if (MainDriver.iSurfCsg == 1 && MainDriver.iCduct == 1) {
			if (MainDriver.ODsurfCsg >= MainDriver.ODcduct) {
				msg = "Surface casing diameter should be less than conductor diameter";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if (MainDriver.DepthSurfCsg <= MainDriver.DepthCduct) {
				msg = "Surface casing setting depth is shallower than that of the conductor";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}

		if (MainDriver.iOnshore == 2) {              //check offshore data
			if (MainDriver.Dwater < 9.5) {
				msg = "Water depth is out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if (MainDriver.iWell != 0 && MainDriver.Dwater > MainDriver.DepthKOP) {
				msg = "KOP is less than Water depth !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if (MainDriver.Dwater > MainDriver.DepthCasing) {
				msg = "Casing setting depth is smaller than water depth !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if (MainDriver.Dwater > MainDriver.DepthCduct && MainDriver.iCduct == 1) {
				msg = "Conductor setting depth is smaller than water depth !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if (MainDriver.Dwater > MainDriver.DepthSurfCsg && MainDriver.iSurfCsg == 1) {
				msg = "Surface casing setting depth is smaller than water depth !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}

			if (MainDriver.Driser <= MainDriver.doDP + 1.5) {
				msg = "OD of marine riser is so small";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}

			if (MainDriver.Dchoke <= 0 || MainDriver.Dkill <= 0) {
				msg = "ID of choke or kill lines are not positive"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}

			if (MainDriver.Dchoke > 15 || MainDriver.Dkill > 15) {
				msg = "ID of choke or kill lines are out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}

			if (MainDriver.tWgrad > 4 || MainDriver.Tgrad < -4) {
				msg = "Water temperature gradient is out of acceptable range";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}
	}

	void check_Formation(){
		badData = 0;
		temp2=0;
		double overPrs=0;
		String msg="";

		if(MainDriver.pitWarn < 0.5 || MainDriver.pitWarn > 300.5) {
			msg = "Pit gain warining level is out of acceptable range !"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		temp2 = MainDriver.oMud + MainDriver.KICKintens;

		if(temp2 > 32.5) {
			msg = "Kick intensity (ie. formation over pressure) is too high"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		overPrs = 0.052 * MainDriver.KICKintens * MainDriver.Vdepth;
		if(MainDriver.igERD == 1){
			if(MainDriver.KICKintens > 4.5 || MainDriver.KICKintens < -4.5) {
				msg = "Kick intensity for tripping and kick simulation is out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}
		else{
			if(MainDriver.KICKintens > 10 || MainDriver.KICKintens < 0.01) {
				msg = "Kick intensity is out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(overPrs < 0.5) {
				msg = "No kick may be expected due to small over-balance. This is a just warning !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		// check the formation property ranges
		if(MainDriver.Perm < 0.01 || MainDriver.Perm > 8000.5) {
			msg = "Formation permeability is out of acceptable range !";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		if(MainDriver.Porosity < 0.01 || MainDriver.Porosity > 0.7) {
			msg = "Formation porosity is out of acceptable range !";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		if(MainDriver.Skins < -30.5 || MainDriver.Skins > 30.5) {
			msg = "Formation skin is out of acceptable range !";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		if(MainDriver.igERD == 1){   //for multilateral wells
			if(MainDriver.gPayLength < 0.1 || MainDriver.gPayLength > 999.9) {
				msg = "Effective formation length for kick influx is out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.gTripTankVolume < 0.2 || MainDriver.gTripTankVolume > 100.2) {
				msg = "Trip tank volume is out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.gTripTankHeight < 0.99 || MainDriver.gTripTankHeight > 50.02) {
				msg = "Trip tank volume is out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.gConnTime < 9.5 || MainDriver.gConnTime > 90.2) {
				msg = "Joint connection time in seccond is out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.gJointLength < 9.5 || MainDriver.gJointLength > 50.2) {
				msg = "Each joint length is out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.igJointNumber < 1 || MainDriver.igJointNumber > 9) {
				msg = "Number of joints per stand (trip) is out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.gBleedPressure < 9.5 || MainDriver.gBleedPressure > 500.5) {
				msg = "Min. pressure increase for stripping/ snubbing is out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.gBleedTime < 9.5 || MainDriver.gBleedTime > 500.5) {
				msg = "Bleeding time in sec. is out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}

		else{
			if(MainDriver.ROPen==0) MainDriver.ROPen=60;
			if(MainDriver.ROPen < 2 || MainDriver.ROPen > 2000) {
				msg = "ROP is out of acceptable range !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}
		//    if(iFG = 0) {  //user input fracture gradient
		//      if(fgMud < (oMud + MainDriver.KICKintens + 0.5) || fgMud > 22.5) {
		//         msg = "Formation fracture gradient is out of acceptable range !"; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		//         badData = 1;
		//      }
		//    }
	}


	int check_WellGeometry(){
		badData = 0;
		double h = MainDriver.Vdepth; 
		double sumHt = MainDriver.LengthDC + MainDriver.LengthHWDP + MainDriver.Dwater + 10;
		double depmin=0;
		String msg = "";
		if(MainDriver.Vdepth < sumHt) {
			msg = "Length of DC or HWDP is out of acceptable range !";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.iWell >= 1 && MainDriver.iWell <= 3) {
			if(MainDriver.Vdepth < MainDriver.DepthKOP) {
				msg = "Well depth is less than the depth of KOP";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}

		if(MainDriver.Vdepth > 100000) {
			msg = "Don't you think well depth is too deep ?";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Vdepth <= 0 || MainDriver.DepthCasing <= 0) {
			msg = "Well depth or casing seat depth should be positive";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.Vdepth < MainDriver.DepthCasing + MainDriver.LengthDC) {			
			msg = "Casing Shoe Depth is too deep";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.LengthDC > 0.5 * MainDriver.Vdepth){// || MainDriver.LengthHWDP > 0.5 * MainDriver.Vdepth) { //150907
			msg = "Length of DC or HWDP is too long compared to well vertical depth for this simulator";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.LengthDC <= 0) {
			msg = "Length of drill collar is not positive";
			badData = 1;
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		// check the hevywate drill pipe
		if(MainDriver.LengthHWDP <= 0) {
			msg = "Length of HWDP should be positive";
			badData = 1;
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.doHWDP <= 0 || MainDriver.diHWDP <= 0) {
			msg = "Diameter of HWDP should be positive";
			badData = 1;
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.doHWDP <= MainDriver.diHWDP) {
			msg = "OD of HWDP should be greater than ID";
			badData = 1;
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.doHWDP >= MainDriver.DiaHole) {			
			msg = "OD of HWDP should be less than open hole dia.";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		// check the diameter range to find any connection problems
		if(MainDriver.doHWDP < MainDriver.diDP || MainDriver.diHWDP > MainDriver.doDP) {
			msg = "You will have drill-string connection problems. "+"\n"+"Check ID & OD of drill-pipe or HWDP. However, this is not an error !";
			//      badData = 1; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);   //this is not necessary error !
		}

		if(MainDriver.doDC < MainDriver.diHWDP || MainDriver.diDC > MainDriver.doHWDP) {
			msg = "You will have drill-string connection problems. " + "\n" + "Check ID & OD of HWDP or drill-collor. However, this is not an error !";			
			//      badData = 1; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}
		//

		if(MainDriver.IDcasing <= 0 || MainDriver.DiaHole <= 0) {
			msg = "Diameters of casing or hole should be positive";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.IDcasing < MainDriver.DiaHole) {
			msg = "Diameter of casing is usually greater than that of open hole. However, this is not an error !";
			//      badData = 1; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);   //not necessary error !
		}

		if(MainDriver.IDcasing > 50.5) {
			msg = "Diameter of casing is out of acceptable range !";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.doDP <= 0 || MainDriver.diDP <= 0) {
			msg = "OD or ID of DP should be positive";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.doDC <= 0 || MainDriver.diDC <= 0) {
			msg = "O.D. or I.D. of DC should be positive";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}
		if(MainDriver.doDP <= MainDriver.diDP) {
			msg = "OD of DP should be greater than ID";
			badData = 1;
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}
		if(MainDriver.doDC <= MainDriver.diDC) {
			msg = "OD of DC should be greater than ID";
			badData = 1;
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);	
		}

		if(MainDriver.doDP >= MainDriver.DiaHole) {
			msg = "OD of DP should be less than the dia. of open hole";
			badData = 1;
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.doDC >= MainDriver.DiaHole) {
			msg = "OD of DC should be less than the dia. of open hole";
			badData = 1; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		}

		if(MainDriver.iWell == 0) return -1;
		// check the basic input data for directional data
		depmin = 10; 
		if(MainDriver.iOnshore == 2) depmin = MainDriver.Dwater;
		if(MainDriver.iWell >= 1 && MainDriver.iWell <= 3) {
			if(MainDriver.DepthKOP < depmin) {
				msg = "KOP is out of acceptable range !"+"\n"+
						"("+(new DecimalFormat("##0.##")).format(depmin)+" < Depth to the KOP < "+(new DecimalFormat("##0.##")).format(MainDriver.Vdepth-5.5)+")";
				badData = 1; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
			if(MainDriver.DepthKOP >= MainDriver.Vdepth - 5.5) {
				msg = "KOP is out of acceptable range !"+"\n"+
						"("+(new DecimalFormat("##0.##")).format(depmin)+" < Depth to the KOP < "+(new DecimalFormat("##0.##")).format(MainDriver.Vdepth-5.5)+")";
				badData = 1; JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		switch(MainDriver.iWell){
		case 2:
			if(MainDriver.BUR <= 0) {
				msg = "The first BUR should be positive";
				badData = 1; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
			if(iBUR == 0) {
				if(MainDriver.Hdisp <= 0) {
					msg = "HD to TD should be positive";
					badData = 1; 
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else{
				if(MainDriver.angEOB <= 0) {
					msg = "Angle at EOB should be positive";
					badData = 1; 
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}   
			break;

		case 3:
			if(MainDriver.BUR <= 0 || MainDriver.BUR2 <= 0) {
				msg = "BUR(s) should be positive";
				badData = 1; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
			if(iBUR == 0) {
				if(MainDriver.Hdisp <= 0) {
					msg = "Horizontal distance to TD should be positive";
					badData = 1;
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else{
				if(MainDriver.angEOB <= 0) {
					msg = "Angle at EOB should be positive";
					badData = 1; 
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
				if(MainDriver.xHold <= 0) {
					msg = "Hold length should be positive";
					badData = 1; 
					JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			break;

		case 4:
			if(MainDriver.BUR <= 0 || MainDriver.BUR2 <= 0) {
				msg = "BUR(s) should be positive";
				badData = 1; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
			if(MainDriver.angEOB <= 0 || MainDriver.ang2EOB <= 0) {
				msg = "Angle(s) at EOB should be positive";
				badData = 1; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
			if(MainDriver.xHold < 0 || MainDriver.x2Hold < 0) {
				msg = "Hold length(s) are negative";
				badData = 1; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}			
			if(MainDriver.ang2EOB <= MainDriver.angEOB) {
				msg = "Final angle is smaller than the first hold angle !";
				badData = 1; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
			break;

		default:
		}
		//

		if(MainDriver.iWell != 0 && badData == 0)  Calc_CheckDD();
		return 0;
	}

	void read_WellTrajt(){ // added by jaewoo 140114
		PnlWellTrjt.setScale();   //set a scale for ERD and ML
		PnlWellTrjt.pntpnl.repaint();
		//if(MainDriver.igERD == 1) DrawMultilateral   //to draw multilateral trajectories
		//
		PnlWellTrjt.wtFrame1.setVisible(false);
		PnlWellTrjt.wtFrame2.setVisible(false);
		PnlWellTrjt.wttext0.setText(PnlWellTrjt.format2.format(MainDriver.Vdepth));
		PnlWellTrjt.wttext2.setText(PnlWellTrjt.format2.format(MainDriver.Hdisp));
		if (MainDriver.iWell == 0) {
			PnlWellTrjt.tMDwell = MainDriver.Vdepth; 
			MainDriver.Hdisp=0;
			PnlWellTrjt.wttext2.setText( "0.0");
		}
		if (MainDriver.iWell >= 1){  // draw KOP & First Build Section
			PnlWellTrjt.wtFrame1.setVisible(true);
			PnlWellTrjt.tMDwell = MainDriver.DepthKOP + MainDriver.Rbur * MainDriver.angEOB * MainDriver.radConv;
		}
		if (MainDriver.iWell >= 2) PnlWellTrjt.tMDwell = PnlWellTrjt.tMDwell + MainDriver.xHold;   // draw First Hold Section
		if (MainDriver.iWell >= 3){
			PnlWellTrjt.wtFrame2.setVisible(true);
			PnlWellTrjt.tMDwell = PnlWellTrjt.tMDwell + MainDriver.R2bur * (MainDriver.ang2EOB - MainDriver.angEOB) * MainDriver.radConv;
		}
		if (MainDriver.iWell == 4) PnlWellTrjt.tMDwell = PnlWellTrjt.tMDwell + MainDriver.x2Hold;
		PnlWellTrjt.wttext3.setText( PnlWellTrjt.format2.format(MainDriver.DepthKOP));
		PnlWellTrjt.wttext4.setText( PnlWellTrjt.format2.format(MainDriver.BUR));
		PnlWellTrjt.wttext5.setText( PnlWellTrjt.format2.format(MainDriver.Rbur));
		PnlWellTrjt.wttext6.setText( PnlWellTrjt.format2.format(MainDriver.angEOB));
		PnlWellTrjt.wttext7.setText( PnlWellTrjt.format2.format(MainDriver.xHold));
		if (MainDriver.iWell == 1) PnlWellTrjt.wttext7.setText( "0.0");
		PnlWellTrjt.wttext8.setText( PnlWellTrjt.format2.format(MainDriver.BUR2));
		PnlWellTrjt.wttext9.setText( PnlWellTrjt.format2.format(MainDriver.R2bur));
		PnlWellTrjt.wttext10.setText( PnlWellTrjt.format2.format(MainDriver.ang2EOB));
		PnlWellTrjt.wttext11.setText( PnlWellTrjt.format2.format(MainDriver.x2Hold));
		if (MainDriver.iWell == 3) PnlWellTrjt.wttext11.setText( "0.0");
		PnlWellTrjt.wttext1.setText(PnlWellTrjt.format2.format(PnlWellTrjt.tMDwell));
	}

	int calcBuild(){
		String msg = "";
		double x4 = MainDriver.Hdisp;
		double dd=0, angval=0, depmin=10;
		if (MainDriver.iOnshore == 2) depmin = MainDriver.Dwater;
		if (MainDriver.DepthKOP < depmin){
			msg = "KOP is too shallow ==> Check Input DATA"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		if(iBUR == 0){    // kick off point is specified
			if(MainDriver.Hdisp <= 0){
				msg = "Horizontal distance to TD should be positive"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
				return -1;
			}
			dd = MainDriver.Vdepth - MainDriver.DepthKOP;
			MainDriver.Rbur = (dd * dd + x4 * x4) / 2 / x4;
			MainDriver.BUR = 18000 / (MainDriver.pai * MainDriver.Rbur);
			if(MainDriver.Hdisp > MainDriver.Rbur){
				msg = "HD is too big for continuous Build !"+"\n"+
						"( HD <= "+ (new DecimalFormat("##0.##")).format(MainDriver.Rbur)+" )"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
				return -1;
			}
		}
		else{                  // build up ratio is specified
			if(MainDriver.BUR <= 0){
				msg = "The first BUR should be positive"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1; 
				return -1;
			}
			MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR); // PI*R*BUR/180 = 100 ft
			if(MainDriver.Rbur < (MainDriver.Vdepth - MainDriver.DepthKOP - 0.5)){
				temp = 18000/(MainDriver.pai*(MainDriver.Vdepth-MainDriver.DepthKOP));
				msg = "BUR is too big or KOP is too shallow !"+"\n"
						+"(BUR<="+(new DecimalFormat("##0.##")).format(temp)+" or KOP>="+(new DecimalFormat("##0.##")).format(MainDriver.Vdepth-MainDriver.Rbur)+")"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);

				badData = 1; 
				return -1;
			}
		}

		angval = (MainDriver.Vdepth - MainDriver.DepthKOP) / MainDriver.Rbur;
		MainDriver.angEOB = utilityModule.aasin(angval) / MainDriver.radConv;
		MainDriver.Hdisp = MainDriver.Rbur * (1 - Math.cos(MainDriver.angEOB * MainDriver.radConv));
		MainDriver.ang2EOB = MainDriver.angEOB;
		return 0;
		//
	}

	int calcBHBHold(){
		String msg="";
		MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR); 
		MainDriver.R2bur = 18000 / (MainDriver.pai * MainDriver.BUR2);
		if(MainDriver.BUR <= 0){
			msg = "The first BUR should be positive"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1; 
			return -1;
		}
		if(MainDriver.BUR2 <= 0){
			msg = "The second BUR should be positive"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1; 
			return -1;
		}
		double angRad = MainDriver.angEOB * MainDriver.radConv;   
		double ang2rad = MainDriver.ang2EOB * MainDriver.radConv;
		//
		double vdarc = MainDriver.Rbur * Math.sin(angRad) + MainDriver.R2bur * (Math.sin(ang2rad) - Math.sin(angRad));
		double delVD = vdarc + MainDriver.xHold * Math.cos(angRad) + MainDriver.x2Hold * Math.cos(ang2rad);
		MainDriver.DepthKOP = MainDriver.Vdepth - delVD;
		double depmin = 10; 
		if (MainDriver.iOnshore == 2) depmin = MainDriver.Dwater;
		if (MainDriver.DepthKOP < depmin){
			msg = "KOP is too shallow ==> Check Input DATA"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		MainDriver.HDeob = MainDriver.Rbur * (1 - Math.cos(angRad)) + MainDriver.R2bur * (Math.cos(angRad) - Math.cos(ang2rad));
		MainDriver.HDeob = MainDriver.HDeob + MainDriver.xHold * Math.sin(angRad);
		MainDriver.Hdisp = MainDriver.HDeob + MainDriver.x2Hold * Math.sin(ang2rad);
		//

		return 0;
		//
	}

	int calcBHBuild(){
		String msg="";
		double ang2rad=0, d4=0, x4=0, dd=0, xx=0, rr=0, Xtmp=0, angval=0, angRad=0, delVD=0, tmpang=0, Hdispc=0;
		double depmin=10;
		if (MainDriver.iOnshore == 2) depmin = MainDriver.Dwater;
		if (MainDriver.DepthKOP < depmin){
			msg = "KOP is too shallow ==> Check Input DATA"+"\n"+"(Depth of KOP >= "+(new DecimalFormat("##0.##")).format(depmin)+")"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		if(MainDriver.BUR <= 0){
			msg = "The first BUR should be positive"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1; 
			return -1;
		}
		if(MainDriver.BUR2 <= 0){
			msg = "The second BUR should be positive"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1; 
			return -1;
		}
		MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR);
		MainDriver.R2bur = 18000 / (MainDriver.pai * MainDriver.BUR2);

		if (iBUR == 0){
			ang2rad = MainDriver.ang2EOB * MainDriver.radConv;
			d4 = MainDriver.Vdepth + MainDriver.R2bur * (1 - Math.sin(ang2rad));
			x4 = MainDriver.Hdisp + MainDriver.R2bur * Math.cos(ang2rad);
			rr = MainDriver.Rbur - MainDriver.R2bur; 
			dd = d4 - MainDriver.DepthKOP - MainDriver.R2bur;
			xx = x4 - MainDriver.Rbur;
			Xtmp = dd * dd + xx * xx - rr * rr;
			if (Xtmp < 0){
				msg = "The first hold length is negative; HD is too small or KOP is too deep !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1; 
				return -1;
			}
			MainDriver.xHold = Math.sqrt(Xtmp);
			angval = (rr * dd + xx * MainDriver.xHold) / (rr * rr + Math.pow(MainDriver.xHold, 2));
			MainDriver.angEOB = utilityModule.aasin(angval) / MainDriver.radConv;
		}
		else{
			angRad = MainDriver.angEOB * MainDriver.radConv;
			delVD = MainDriver.Vdepth - MainDriver.DepthKOP - MainDriver.Rbur * Math.sin(angRad) - MainDriver.xHold * Math.cos(angRad);
			if (delVD < 0.5){
				msg = "KOP is too deep, or hold angle or length are too big !"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1; 
				return -1;
			}
			tmpang = delVD / MainDriver.R2bur + Math.sin(angRad);
			MainDriver.ang2EOB = utilityModule.aasin(tmpang) / MainDriver.radConv;
			Hdispc = MainDriver.Rbur * (1 - Math.cos(angRad)) + MainDriver.R2bur * (Math.cos(angRad) - Math.cos(MainDriver.ang2EOB * MainDriver.radConv));
			MainDriver.Hdisp = Hdispc + MainDriver.xHold * Math.sin(angRad);
		}
		return 0;
	}

	int calcBHold(){
		double rr=0, dd=0, xx=0, Xtmp=0, angval=0, angcosval=0, angRad=0, delvdh=0;
		double depmin=10;
		double Discriminant = 0, root=0;
		String msg ="";
		if (MainDriver.iOnshore == 2) depmin = MainDriver.Dwater;
		if (MainDriver.DepthKOP < depmin){
			msg = "KOP is too shallow ==> Check Input DATA"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		MainDriver.BUR2 = 0; 
		MainDriver.R2bur = 0;
		if(MainDriver.BUR <= 0){
			msg = "The first BUR should be positive"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1; 
			return -1;
		}
		MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR);		
		rr = MainDriver.Rbur - MainDriver.R2bur;
		dd = MainDriver.Vdepth - MainDriver.DepthKOP - MainDriver.R2bur;
		msg = "HD is too small or KOP is too deep !";

		if(iBUR == 0){
			if(MainDriver.Hdisp <= 0){
				msg = "Horizontal distance to TD should be positive"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
				return -1;
			}
			xx = MainDriver.Hdisp - MainDriver.Rbur;
			Xtmp = dd * dd + xx * xx - rr * rr; // Derive a hold length
			if (Xtmp < 0.5) {
				temp = MainDriver.Rbur+Math.sqrt(0.5-dd*dd+rr*rr);
				temp2= MainDriver.Vdepth-Math.sqrt(0.5-xx*xx+rr*rr);
				msg=msg+"\n"+
						"( HD >= "+(new DecimalFormat("##0.##")).format(temp)+" or Depth to the KOP <= "+(new DecimalFormat("##0.##")).format(temp2)+" )";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1; 
				return -1;
			}			
			MainDriver.xHold = Math.sqrt(Xtmp);
			angval = (rr * dd + xx * MainDriver.xHold) / (rr * rr + Math.pow(MainDriver.xHold,2));
			MainDriver.angEOB = utilityModule.aasin(angval) / MainDriver.radConv;
		}
		else{
			if (MainDriver.angEOB > 87){
				msg = "Angle at EOB is too big ===> use Horizontal well !"+"\n"+"(Angle at EOB<=87)"; 
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1; 
				return -1;
			}
			angRad = MainDriver.angEOB * MainDriver.radConv;
			delvdh = MainDriver.Vdepth - MainDriver.DepthKOP - MainDriver.Rbur * Math.sin(angRad);
			MainDriver.xHold = delvdh / Math.cos(angRad);		
			if (MainDriver.xHold < 0.5) {
				msg = "KOP is too deep or BUR is too small";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1; 
				return -1;
			}
			MainDriver.Hdisp = MainDriver.Rbur * (1 - Math.cos(angRad)) + MainDriver.xHold * Math.sin(angRad);
		}
		Discriminant = Math.pow(MainDriver.Hdisp+MainDriver.R2bur,2)-4*(MainDriver.R2bur*MainDriver.Hdisp+MainDriver.xHold*(MainDriver.Vdepth-MainDriver.DepthKOP-MainDriver.R2bur)); // Discriminate this cosin value is negative 20140116 ajw
		angcosval = (MainDriver.xHold*(MainDriver.Vdepth - MainDriver.R2bur)+(MainDriver.R2bur-MainDriver.Rbur)*(MainDriver.Hdisp-MainDriver.Rbur))/(Math.pow(MainDriver.xHold,1)+Math.pow(MainDriver.R2bur-MainDriver.Rbur, 2));
		if(Discriminant>0 && angcosval<0){
			root = (MainDriver.Hdisp+MainDriver.R2bur+Math.sqrt(Discriminant))/(2*1); // RBUR should be bigger than root. 
			msg = "This geometry isn't possible. BUR is too small or Horizontal distance to TD is too big.";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
			return -1;
		}

		double temp1 = Math.abs(MainDriver.Vdepth-MainDriver.DepthKOP)-Math.abs(MainDriver.xHold*Math.cos(MainDriver.angEOB*MainDriver.radConv)+MainDriver.Rbur*Math.sin(MainDriver.angEOB*MainDriver.radConv));
		double temp2 = Math.abs(MainDriver.Hdisp-MainDriver.Rbur)-Math.abs(MainDriver.xHold*Math.sin(MainDriver.angEOB*MainDriver.radConv)-MainDriver.Rbur*Math.cos(MainDriver.angEOB*MainDriver.radConv));
		MainDriver.ang2EOB = MainDriver.angEOB;
		return 0;
	}

	void Calc_CheckDD(){
		// calculate the directional well geometry and its compatibility
		// calculate wellbore path based on input data (Nov.2, OGJ, 1992)
		//
		String msg="";
		MainDriver.Rbur = 0;
		MainDriver.R2bur = 0;
		switch(MainDriver.iWell){		
		case 1:    //continuous build (DD type 3)
			dummy = calcBuild();
			break;

		case 2:    //build-hold (DD type 1)
			dummy = calcBHold();
			break;

		case 3:    //horizontal or near horizontal with 2 build
			dummy = calcBHBuild();
			break;

		default: //horizontal or near horiz. with 2 build & hold section
			dummy = calcBHBHold();
		}
		//
		if(MainDriver.angEOB < 0 || MainDriver.angEOB > 90.05){
			msg =  "Hold angle calculated is out of acceptable range !"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
		if (MainDriver.ang2EOB > 90.5){
			msg =  " Final Hold angle calculated is out of acceptable range !"; 
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);		
			badData = 1;
		}
	}

	void check_Pump(){
		// 7/29/2001
		// Check the input data for pump and surface connection type data

		badData = 0;
		String msg="";
		/* if(MainDriver.strokeLength > 25 || MainDriver.strokeLength <= 0.001) {
	     msg = "Stroke length is out of acceptable range !";
	     JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
	     badData = 1;
	  }
	  if(MainDriver.DiaLiner > 15 || MainDriver.DiaLiner < 0.001) {
	     msg = "Pump liner MainDriver.Diameter is out of acceptable range !";
	     JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
	     badData = 1;
	  }*/
		/*if(MainDriver.iPump == 2 && MainDriver.DiaLiner <= MainDriver.Drod) {
	     msg = "MainDriver.Diameter of the liner should be greater than that of rod";
	     JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
	     badData = 1;
	  }*/ //removed by jw, 20140205
		/*  if(MainDriver.Effcy < 0.3 || MainDriver.Effcy > 1) {
	     msg = "Pump efficiency is usually between 1.0 and 0.5";
	     JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
	     badData = 1;
	  }*/
		if(MainDriver.spMinD1 < MainDriver.spMin1) {
			msg = "Kill rate is usually less than Drilling rate";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			//     badData = 1;   //It//s not necessary wrong !
		}

		if(MainDriver.spMinD2 < MainDriver.spMin2) {
			msg = "Kill rate is usually less than Drilling rate";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			//     badData = 1;   //It//s not necessary wrong !
		}

		if((MainDriver.spMin1 <= 0 || MainDriver.spMinD1 <= 0)&&(MainDriver.spMin2 <= 0 || MainDriver.spMinD2 <= 0)) {
			msg = " of strokes per minute should be positive";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		} // added by jw, 20140205


		if(MainDriver.iType != 0) {
			if(MainDriver.DiaSurfLine > 10) {
				msg = "ID of the surface line is out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.DiaSurfLine <= 0) {
				msg = "ID of the surface line is not positive !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.LengthSurfLine > 1000 || MainDriver.LengthSurfLine < 0) {
				msg = "Length of the surface line is out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}
	}

	void check_Fluid_Bit(){
		badData = 0;
		String msg="";
		double dsum=0, tmpMudComp=0;
		// Check the input data

		if(MainDriver.S600 > 210){
			msg = "Shear stress reading at 600 is out of normal rangr";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.S600 <= MainDriver.S300){
			msg = "Shear stress reading at 600 rpm is less than that of at 300";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Model == 0 ){
			if(MainDriver.SS100 >= MainDriver.S300){
				msg = "Shear stress reading at 300 rpm is less than that of at 100";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.SS3 >= MainDriver.SS100){
				msg = "Shear stress reading at 3 rpm is less than that of at 100";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(MainDriver.SS3 < 0.05){
				msg = "Shear stress reading at 3 rpm is too small&&out of acceptable range !";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
		}

		if(MainDriver.Model == 2 && 2 * MainDriver.S300 <= MainDriver.S600){
			msg = "Shrear stress reading at 300 rpm is too small that yield stress is negative";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}		

		if(MainDriver.iData == 2){
			if(pViscos <= 0){
				msg = "Plastic viscosity is non-positive";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}
			if(Yp <= 0){
				msg = "Yield point is non-positive";
				JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
				badData = 1;
			}			  
		}

		if(MainDriver.oMud < 8){
			msg = "Mud weight is too small";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.oMud > 20){
			msg = "Mud weight is so large that it will break the formation";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.gasGravity < 0.3){
			msg = "Gas S.G. is too small";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.gasGravity > 3){
			msg = "Gas S.G. is too big";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.RenC < 2000||MainDriver.RenC > 4000){
			msg = "Critical Reynold//s number is usually between 4000&&2000";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Dnoz[0] <= 0&&MainDriver.Dnoz[1] <= 0&&MainDriver.Dnoz[2] <= 0&&MainDriver.Dnoz[3] <= 0){
			msg = "There should have at least one non-zero nozzle";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Dnoz[0] < 0||MainDriver.Dnoz[1] < 0||MainDriver.Dnoz[2] < 0||MainDriver.Dnoz[3] < 0){
			msg = "Nozzle diameter(s) is negative";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		dsum = (MainDriver.Dnoz[0]*MainDriver.Dnoz[0] + MainDriver.Dnoz[1]*MainDriver.Dnoz[1] + MainDriver.Dnoz[2]*MainDriver.Dnoz[2] + MainDriver.Dnoz[3]*MainDriver.Dnoz[3]) / 32 / 32;
		if(dsum > MainDriver.DiaHole * MainDriver.DiaHole * 0.95){
			msg = "Nozzle diameter is too large";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Ruf < 0){
			msg = "Roughness data is negative";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Ruf > 0.05){
			msg = "Roughness is too big";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Tsurf > 110||MainDriver.Tsurf < 0){
			msg = "Surface temperature is out of acceptable range !";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		if(MainDriver.Tgrad > 4||MainDriver.Tgrad < 0){
			msg = "Mud temperature gradient is out of acceptable range";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}

		//
		tmpMudComp = MainDriver.MudComp * 1000000;
		if(tmpMudComp > 8000 || tmpMudComp < 0.01){
			msg = "Mud compressibility is out of acceptable range&&note that unit is E-06 1/psi";
			JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
			badData = 1;
		}
	}


	void read_Heattransfer(){
		//...... read heat transfer properties
		MainDriver.TconF = Double.parseDouble(txtTconF.getText());
		MainDriver.SheatF = Double.parseDouble(txtSheatF.getText());
		MainDriver.HtrancF = Double.parseDouble(txtHtrancF.getText());
		MainDriver.TconM = Double.parseDouble(txtTconM.getText());
		MainDriver.SheatM = Double.parseDouble(txtSheatM.getText());
		MainDriver.HtrancM = Double.parseDouble(txtHtrancM.getText());
		MainDriver.InjmudT = Double.parseDouble(txtInjmudT.getText());

	}

	void read_Mud(){
		//...... read mud properties
		MainDriver.foil = Double.parseDouble(txtbaseoilf.getText());
		if(MainDriver.imud==1) utilityModule.OBM_Composition_SK();
		txtbaseoilf.setText((new DecimalFormat("0.000")).format(MainDriver.foil));
		txtbrinef.setText((new DecimalFormat("0.000")).format(MainDriver.fbrine));
		txtAdditivef.setText((new DecimalFormat("0.000")).format(MainDriver.fadditive));
		//hs
		MainDriver.Mfrac_C[7] = Double.parseDouble(txtbasecom8.getText());
		MainDriver.Mfrac_C[8] = Double.parseDouble(txtbasecom9.getText());
		MainDriver.Mfrac_C[9] = Double.parseDouble(txtbasecom10.getText());
		MainDriver.Mfrac_C[10] = Double.parseDouble(txtbasecom11.getText());
		MainDriver.Mfrac_C[11] = Double.parseDouble(txtbasecom12.getText());
		MainDriver.Mfrac_C[12] = Double.parseDouble(txtbasecom13.getText());
		MainDriver.Mfrac_C[13] = Double.parseDouble(txtbasecom14.getText());
		MainDriver.Mfrac_C[14] = Double.parseDouble(txtbasecom15.getText());
		MainDriver.Mfrac_C[15] = Double.parseDouble(txtbasecom16.getText());
		MainDriver.Mfrac_C[16] = Double.parseDouble(txtbasecom17.getText());
		MainDriver.Mfrac_C[17] = Double.parseDouble(txtbasecom18.getText());
		MainDriver.Mfrac_C[18] = Double.parseDouble(txtbasecom19.getText());
		MainDriver.Mfrac_C[19] = Double.parseDouble(txtbasecom20.getText());
		MainDriver.Mfrac_C[20] = Double.parseDouble(txtbasecom21.getText());
		MainDriver.Mfrac_C[21] = Double.parseDouble(txtbasecom22.getText());
		MainDriver.Mfrac_C[22] = Double.parseDouble(txtbasecom23.getText());
		MainDriver.Mfrac_C[23] = Double.parseDouble(txtbasecom24.getText());
		MainDriver.Mfrac_C[24] = Double.parseDouble(txtbasecom25.getText());
	}

	void read_Multilateral(){
		//---- assign input data for multilateral wells
		MainDriver.igMLnumber = sldML.getValue();
		for(int i = 0; i<=(MainDriver.igMLnumber - 1); i++){
			if(chkPlugged[i].isSelected()==true) MainDriver.mlPlug[i] = 1;
			else MainDriver.mlPlug[i] = 0;
			MainDriver.mlKOP[i] = Double.parseDouble(txtKOP[i].getText());
			MainDriver.mlKOPvd[i]  = Double.parseDouble(txtKOPvd[i].getText());       //read for future use!
			MainDriver.mlKOPang[i] = Double.parseDouble(txtKOPangle[i].getText());   //2003/7/16
			MainDriver.mlBUR[i] = Double.parseDouble(txtBUR[i].getText()); 
			MainDriver.mlEOB[i] = Double.parseDouble(txtEOBangle[i].getText());
			MainDriver.mlHold[i] = Double.parseDouble(txtHold[i].getText());
			MainDriver.mlBUR2nd[i] = Double.parseDouble(txtBUR2nd[i].getText()); MainDriver.mlEOB2nd[i] = Double.parseDouble(txtEOB2nd[i].getText());
			MainDriver.mlHold2nd[i] = Double.parseDouble(txtHold2nd[i].getText());
			MainDriver.mlDia[i] = Double.parseDouble(txtDia[i].getText());
			//mlDiaMD[i] = Double.parseDouble(txtDiaMD[i].getText());    //just assume uniform wellbore
			//mlDia2nd[i] = Double.parseDouble(txtDia2nd[i].getText());  //2003/7/29
			MainDriver.mlPform[i] = Double.parseDouble(txtPform[i].getText()) + 14.7;
			MainDriver.mlPerm[i] = Double.parseDouble(txtMLperm[i].getText());
			MainDriver.mlPorosity[i] = Double.parseDouble(txtMLporosity[i].getText());
			MainDriver.mlSkin[i] = Double.parseDouble(txtMLskin[i].getText()); MainDriver.mlHeff[i] = Double.parseDouble(txtMLheff[i].getText());
		}
	}


	void read_Cduct_Offshore(){
		// 7/29/2001
		//.... read conductor and surface casing data, and offshore data if any
		//
		MainDriver.DepthCduct = Double.parseDouble(txtCductDepth.getText()); 
		MainDriver.ODcduct = Double.parseDouble(txtCductOD.getText());   // conductor data
		MainDriver.DepthSurfCsg = Double.parseDouble(txtScasingDepth.getText());
		MainDriver.ODsurfCsg = Double.parseDouble(txtScasingOD.getText());   // surface casing data
		if (MainDriver.iOnshore == 2){
			MainDriver.Dwater = Double.parseDouble(txtWaterDepth.getText());
			MainDriver.tWgrad = Double.parseDouble(txtWaterTempGrad.getText());
			MainDriver.Driser = Double.parseDouble(txtRiserID.getText());
			MainDriver.Dchoke = Double.parseDouble(txtChokeID.getText());
			MainDriver.Dkill = Double.parseDouble(txtKilliD.getText());
		}
		//
	}

	void read_Formation(){
		//...Assing INPUT variables
		MainDriver.KICKintens = Double.parseDouble(txtKickIntens.getText()); 
		MainDriver.pitWarn = Double.parseDouble(txtPGwarning.getText());
		MainDriver.Perm = Double.parseDouble(txtPerm.getText());  
		MainDriver.Porosity = Double.parseDouble(txtPorosity.getText());
		MainDriver.Skins = Double.parseDouble(txtSkin.getText());
		if(MainDriver.igERD == 1){
			MainDriver.gPayLength = Integer.parseInt(txtROP.getText());
			MainDriver.gTripTankVolume = Double.parseDouble(txtTripTankVolume.getText());
			MainDriver.gTripTankHeight = Double.parseDouble(txtTripTankHeight.getText());
			MainDriver.gConnTime = Double.parseDouble(txtConnTime.getText());
			MainDriver.gJointLength = Double.parseDouble(txtJointLength.getText());
			MainDriver.igJointNumber = Integer.parseInt(txtJointNumber.getText());
			MainDriver.gBleedPressure = Double.parseDouble(txtPbleed.getText());
			MainDriver.gBleedTime = Double.parseDouble(txtTbleed.getText());
		}
		else MainDriver.ROPen = Double.parseDouble(txtROP.getText());
		//
	}

	int read_WellGeometry(){
		// Assign INPUT data - consider directional data
		MainDriver.Vdepth = Double.parseDouble(txtVdepth.getText());
		MainDriver.DepthCasing = Double.parseDouble(txtMDcasing.getText());
		MainDriver.LengthDC = Double.parseDouble(txtDClength.getText());
		MainDriver.IDcasing = Double.parseDouble(txtCasingID.getText());
		MainDriver.DiaHole = Double.parseDouble(txtHoleDia.getText());
		MainDriver.doDP = Double.parseDouble(txtDPoD.getText());
		MainDriver.diDP = Double.parseDouble(txtDPiD.getText());
		MainDriver.doDC = Double.parseDouble(txtDCoD.getText());
		MainDriver.diDC = Double.parseDouble(txtDCiD.getText());

		MainDriver.LengthHWDP = Double.parseDouble(txtHWDPlength.getText());
		MainDriver.doHWDP = Double.parseDouble(txtHWDPoD.getText());
		MainDriver.diHWDP = Double.parseDouble(txtHWDPiD.getText());
		MainDriver.DepthCduct = Double.parseDouble(txtCductDepth.getText());
		MainDriver.ODcduct = Double.parseDouble(txtCductOD.getText());   // conductor data
		MainDriver.DepthSurfCsg = Double.parseDouble(txtScasingDepth.getText());
		MainDriver.ODsurfCsg = Double.parseDouble(txtScasingOD.getText());   // surface casing data

		if (MainDriver.iWell == 0) return -1;
		//
		if(MainDriver.iWell >= 1 && MainDriver.iWell <= 3) MainDriver.DepthKOP = Double.parseDouble(txtDepthKOP.getText());

		switch(MainDriver.iWell){  //visible 21, 22 or 25
		case 1:
			if(iBUR == 0) MainDriver.Hdisp = Double.parseDouble(txtHDtoTD.getText());
			else MainDriver.BUR = Double.parseDouble(txtFirstBUR.getText());
			break;

		case 2:
			MainDriver.BUR = Double.parseDouble(txtFirstBUR.getText());
			if (iBUR == 0) MainDriver.Hdisp = Double.parseDouble(txtHDtoTD.getText());
			else MainDriver.angEOB = Double.parseDouble(txtAngleEOB.getText());
			break;

		case 3:
			MainDriver.BUR = Double.parseDouble(txtFirstBUR.getText());
			MainDriver.BUR2 = Double.parseDouble(txtSecondBUR.getText());
			if(iBUR == 0){
				MainDriver.Hdisp = Double.parseDouble(txtHDtoTD.getText());
				MainDriver.ang2EOB = Double.parseDouble(txtAngleEOB.getText());
			}
			else{
				MainDriver.angEOB = Double.parseDouble(txtAngleEOB.getText());
				MainDriver.xHold = Double.parseDouble(txtHDtoTD.getText());
			}
			break;

		default:
			MainDriver.BUR = Double.parseDouble(txtFirstBUR.getText());
			MainDriver.BUR2 = Double.parseDouble(txtSecondBUR.getText());
			MainDriver.angEOB = Double.parseDouble(txtAngleEOB.getText());
			MainDriver.xHold = Double.parseDouble(txtHDtoTD.getText());
			MainDriver.ang2EOB = Double.parseDouble(txtFinalHoldLength.getText());
			MainDriver.x2Hold = Double.parseDouble(txtHorizLength.getText());
		}
		return 0;

		//
	}

	void read_Pump(){
		// 7/29/2001
		//...... read pump and surface connection type data
		// Set the INPUT value
		// MainDriver.DiaLiner = Double.parseDouble(txtLinerDia.getText());
		// MainDriver.strokeLength = Double.parseDouble(txtStrokeLength.getText());
		// MainDriver.Effcy = Double.parseDouble(txtPumpEff.getText());
		// if(MainDriver.iPump == 2) MainDriver.Drod = Double.parseDouble(txtRodDia.getText());
		// else MainDriver.Drod = 0;
		MainDriver.Qcapacity1 =Double.parseDouble(txtQcapacity1.getText());
		MainDriver.Qcapacity2 =Double.parseDouble(txtQcapacity2.getText()); //20140205 ajw
		if(MainDriver.Qcapacity1==0.134) MainDriver.Qcapacity1=0.133765401480195;
		if(MainDriver.Qcapacity2==0.134) MainDriver.Qcapacity2=0.133765401480195;
		////    DchkControl = Double.parseDouble(txtSecondBUR)
		//MainDriver.spMinD = sldPumpRate.getValue(); 
		//MainDriver.spMin = sldKillPumpRate.getValue();

		MainDriver.spMinD1 = sldPumpRate1.getValue(); 
		MainDriver.spMin1 = sldKillPumpRate1.getValue();
		MainDriver.spMinD2 = sldPumpRate2.getValue(); 
		MainDriver.spMin2 = sldKillPumpRate2.getValue();

		MainDriver.DiaSurfLine = Double.parseDouble(txtIDtoSP.getText());
		MainDriver.LengthSurfLine = Double.parseDouble(txtLengthToSP.getText());
		SurfaceEquip();
		if (MainDriver.iType != 0){
			//      DiaSurfLine = Double.parseDouble(txtIDtoSP) LengthSurfLine = Double.parseDouble(txtLengthToSP)
			if (MainDriver.DiaSurfLine <= 0.0001) MainDriver.DiaSurfLine = 3;          // if not, real/0.0 happens in DpDl
			if (MainDriver.LengthSurfLine < 0) MainDriver.LengthSurfLine = 0;
		}
		//------------------------------------------------------ 7/15/02
		//      Qdrill = q2 * 42# * spminD Qkill = q2 * 42# * spmin

	}

	void read_Fluid_Bit(){
		// 7/28/2001
		//...... read fluid properties data and bit nozzle data
		//
		if(MainDriver.iData== 2){
			pViscos = Double.parseDouble(txtSS600.getText());
			Yp = Double.parseDouble(txtSS300.getText());
			MainDriver.S600 = 2 * pViscos + Yp;
			MainDriver.S300 = Yp + pViscos;
		}
		else{
			MainDriver.S600 = Double.parseDouble(txtSS600.getText());
			MainDriver.S300 = Double.parseDouble(txtSS300.getText());
		}
		//
		MainDriver.oMud = Double.parseDouble(txtMudDensity.getText());
		MainDriver.RenC = Double.parseDouble(txtCritReyNumber.getText());
		MainDriver.Ruf = Double.parseDouble(txtRoughness.getText());
		MainDriver.gasGravity = Double.parseDouble(txtGasGravity.getText());   
		MainDriver.fCO2 = Double.parseDouble(txtCO2fraction.getText());  
		MainDriver.fH2S = Double.parseDouble(txtH2Sfraction.getText());
		MainDriver.Tsurf = Double.parseDouble(txtSurfaceTemp.getText()); 
		MainDriver.Tgrad = Double.parseDouble(txtTempGrad.getText());
		if(MainDriver.Model == 3)  MainDriver.S600 = 2 *  MainDriver.S300;
		if(MainDriver.iMudComp == 1)  MainDriver.MudComp = Double.parseDouble(txtMudComp.getText())* 0.000001; 
		if(MainDriver.Model == 0){
			MainDriver.SS100 = Double.parseDouble(txtSS100.getText()); 
			MainDriver.SS3 = Double.parseDouble(txtSS3.getText());
		}
		//	   
		MainDriver.Dnoz[0] = Double.parseDouble(txtNozzleDia0.getText());
		MainDriver.Dnoz[1] = Double.parseDouble(txtNozzleDia1.getText());
		MainDriver.Dnoz[2] = Double.parseDouble(txtNozzleDia2.getText());
		MainDriver.Dnoz[3] = Double.parseDouble(txtNozzleDia3.getText());
		//
	}

	void Form_Activate(){
		Assign_Options();
		Assign_Fluid_Bit();
		Assign_Pump();
		Assign_Cduct_Offshore();
		Assign_Formation();
		Assign_WellGeometry();
		assign_Pore_Fracture();
		//Assign_Multilateral();
		//Added by ty
		Assign_mud();
		Assign_Heattransfer();
	}

	void Assign_Heattransfer(){
		//-------------------------------------------Default data display
		txtTconF.setText((new DecimalFormat("##0.##")).format(MainDriver.TconF));
		txtSheatF.setText((new DecimalFormat("##0.###")).format(MainDriver.SheatF)); 
		txtHtrancF.setText((new DecimalFormat("##0.##")).format(MainDriver.HtrancF)); 
		txtTconM.setText((new DecimalFormat("##0.##")).format(MainDriver.TconM)); 
		txtSheatM.setText((new DecimalFormat("##0.##")).format(MainDriver.SheatM)); 
		txtHtrancM.setText((new DecimalFormat("##0.##")).format(MainDriver.HtrancM));
		txtInjmudT.setText((new DecimalFormat("##0.##")).format(MainDriver.InjmudT)); 
	}

	void Assign_Multilateral(){
		sldML.setValue(MainDriver.igMLnumber);
		for(int i = 0; i<=(MainDriver.igMLnumber - 1); i++){
			if(MainDriver.mlPlug[i] == 0) chkPlugged[i].setSelected(false);
			txtKOP[i].setText(Double.toString(MainDriver.mlKOP[i]));
			txtBUR[i].setText(Double.toString(MainDriver.mlBUR[i]));
			txtEOBangle[i].setText(Double.toString(MainDriver.mlEOB[i])); 
			txtHold[i].setText(Double.toString(MainDriver.mlHold[i]));
			txtBUR2nd[i].setText(Double.toString(MainDriver.mlBUR2nd[i]));
			txtEOB2nd[i].setText(Double.toString(MainDriver.mlEOB2nd[i])); 
			txtHold2nd[i].setText(Double.toString(MainDriver.mlHold2nd[i]));
			txtDia[i].setText(Double.toString(MainDriver.mlDia[i]));
			//.... assume unifrom ML for simplicity, 2003/7/16
			//txtDiaMD[i].setText(mlDiaMD[i]); txtDia2nd[i].setText(mlDia2nd[i]
			txtPform[i].setText((new DecimalFormat("######.0#")).format(MainDriver.mlPform[i] - 14.7));     //2003/7/16
			txtMLperm[i].setText(Double.toString(MainDriver.mlPerm[i])); txtMLporosity[i].setText(Double.toString(MainDriver.mlPorosity[i]));
			txtMLskin[i].setText(Double.toString(MainDriver.mlSkin[i])); txtMLheff[i].setText(Double.toString(MainDriver.mlHeff[i]));
		}
	}

	void assign_Pore_Fracture(){
		// 7/31/02 - further modification for ERD and ML
		//-------------- assign pore and fracture data and related options
		//
		if(MainDriver.iFG == 0) optUser.setSelected(true);
		if(MainDriver.iFG == 1) optEaton.setSelected(true);
		if(MainDriver.iFG == 2) optBarker.setSelected(true);
		//
		//.................................. plot pore and fracture pressures
		if(optEMW.isSelected()==true) dummy = plot_Fracture(2, chartPoreP);   //1pressure, 2EMW
		else{
			optPressure.setSelected(true);
			dummy = plot_Fracture(1,chartPoreP);   //1pressure, 2EMW	    
		}
		//
		if(MainDriver.iFG == 1 || MainDriver.iFG == 2){
			MainDriver.iFGnum = 11;
			lblDepth.setVisible(false); 
			lblDepth2.setVisible(false); 
			lblPP.setVisible(false); 
			lblPP2.setVisible(false); 
			lblFP.setVisible(false);
			lblFP2.setVisible(false);
			VscrollPP.setVisible(false); 
			linePP.setVisible(false);
			for(int i = 0; i<=10; i++){
				txtDepth[i].setVisible(false);
				txtPoreP[i].setVisible(false);
				txtFracP[i].setVisible(false);
			}
		}
		else{
			lblDepth.setVisible(true); 
			lblDepth2.setVisible(true); 
			lblPP.setVisible(true); 
			lblPP2.setVisible(true); 
			lblFP.setVisible(true);
			lblFP2.setVisible(true);
			VscrollPP.setVisible(true);
			linePP.setVisible(true);
			VscrollPP.setValue(MainDriver.iFGnum);
			for(int i = 0; i<=MainDriver.iFGnum - 1; i++){
				txtDepth[i].setText((new DecimalFormat("#####0.0")).format(MainDriver.PPdepth[i]));
				txtPoreP[i].setText((new DecimalFormat("#####0.0")).format(MainDriver.PoreP[i]));
				txtFracP[i].setText((new DecimalFormat("#####0.0")).format(MainDriver.FracP[i]));
			}
			for(int i = MainDriver.iFGnum; i<=10 ; i++){    //this is necessary to update any change
				txtDepth[i].setText("0");
				txtPoreP[i].setText("0");
				txtFracP[i].setText("0");
			}
		}/*
	//!                     Jan. 19, 1999.
	// for the chartPoreP - pore, fracture, and annulus pressure
	//    With chartPoreP
	//       .Top = 120
	//       .Left = 120
	//       .Height = 5655
	//       .Width = 4935
	//       .setVisible(false);          //show the graph always
	//    End With
	//
	//.......7/31/02, apparently this works !
	/*    With chartPoreP.Backdrop         //for background color change
	       .Fill.Style = VtFillStyleBrush
	       .Fill.Brush.FillColor.Set 0, 250, 100
	 //      .Fill.Brush.FillColor.Red = 0
	 //      .Fill.Brush.FillColor.Green = 150
	 //      .Fill.Brush.FillColor.blue = 50
	 //      .Frame.Style = VtFrameStyledoubleLine
	//       .Shadow.Style = VtShadowStyleNull
	    End With*/ //AJW 20131226
	}

	void Assign_WellGeometry(){
		// add items in the comboWellTrajt
		/*comboWellTrajt.ComboItems.Clear
	     comboWellTrajt.ComboItems.Add = "Vertical"
	     comboWellTrajt.ComboItems.Add = "Build"
	     comboWellTrajt.ComboItems.Add = "B_Hold"
	     comboWellTrajt.ComboItems.Add = "B_H_Build"
	     comboWellTrajt.ComboItems.Add = "B_H_B_Hold"
		 */
		//
		if (MainDriver.iWell == 0) comboWellTrajt.setSelectedItem("Vertical");
		else if (MainDriver.iWell == 1) comboWellTrajt.setSelectedItem("Build");
		else if (MainDriver.iWell == 2) comboWellTrajt.setSelectedItem("B_Hold");
		else if (MainDriver.iWell == 3) comboWellTrajt.setSelectedItem("B_H_Build");
		else comboWellTrajt.setSelectedItem("B_H_B_Hold");

		if(MainDriver.iHresv == 1){
			optHomoRes.setSelected(true);
			//optLocalHP.value = False
		}
		else{
			optLocalHP.setSelected(true);
			//optHomoRes.value = False
		}

		if(iBUR == 0){
			optSetHD.setSelected(true);
		}
		else{
			optSetBUR.setSelected(true);	    	
		}

		//frameHorizWell.setEnabled(false); // not effective in java
		optLocalHP.setEnabled(false);
		optHomoRes.setEnabled(false);
		txtVdepth.setHorizontalAlignment(SwingConstants.RIGHT);

		// Set input data or default data
		txtVdepth.setText((new DecimalFormat("#####0.#")).format(MainDriver.Vdepth)); 
		txtMDcasing.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMDcasing.setText((new DecimalFormat("#####0.#")).format(MainDriver.DepthCasing)); 
		txtDClength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDClength.setText((new DecimalFormat("####0.#")).format(MainDriver.LengthDC)); 
		txtCasingID.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCasingID.setText((new DecimalFormat("##0.###")).format(MainDriver.IDcasing)); 
		txtHoleDia.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHoleDia.setText((new DecimalFormat("##0.###")).format(MainDriver.DiaHole));
		txtDPoD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDPoD.setText((new DecimalFormat("##0.###")).format(MainDriver.doDP));  
		txtDPiD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDPiD.setText((new DecimalFormat("##0.###")).format(MainDriver.diDP)); 
		txtDCoD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDCoD.setText((new DecimalFormat("##0.###")).format(MainDriver.doDC));  
		txtDCiD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDCiD.setText((new DecimalFormat("##0.###")).format(MainDriver.diDC)); 
		txtHWDPoD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHWDPoD.setText((new DecimalFormat("##0.###")).format(MainDriver.doHWDP)); 
		txtHWDPiD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHWDPiD.setText((new DecimalFormat("##0.###")).format(MainDriver.diHWDP)); 
		txtHWDPlength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHWDPlength.setText((new DecimalFormat("####0.#")).format(MainDriver.LengthHWDP));
		txtHorizLength.setHorizontalAlignment(SwingConstants.RIGHT);
		//
		txtHorizLength.setText("0");
		if (MainDriver.iWell == 4 && (int)(MainDriver.ang2EOB + 0.001) == 90) frameHorizWell.setEnabled(true);
		dummy = check_DirectalData();	    
	}

	void Assign_Formation(){
		// Set input data or default data
		HscrlCHK.setValue((int)(MainDriver.DchkControl + 0.05));
		txtCHKdia.setHorizontalAlignment(SwingConstants.CENTER);
		txtCHKdia.setText((new DecimalFormat("#0")).format(MainDriver.DchkControl)); 
		txtKickIntens.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKickIntens.setText((new DecimalFormat("#0.###")).format(MainDriver.KICKintens)); 
		txtPGwarning.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPGwarning.setText((new DecimalFormat("##0.#")).format(MainDriver.pitWarn));
		txtPerm.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPerm.setText((new DecimalFormat("####0.###")).format(MainDriver.Perm)); 
		txtPorosity.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPorosity.setText((new DecimalFormat("0.###")).format(MainDriver.Porosity));
		txtSkin.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSkin.setText((new DecimalFormat("###0.###")).format(MainDriver.Skins)); 

		if(MainDriver.igERD == 1){
			FrameTrip.setVisible(true);
			lblTripData.setVisible(true);
			txtROP.setText((new DecimalFormat("###0.#")).format(MainDriver.gPayLength)); 
			lblROP.setText("ft, Drilled Pay Zone Length");
			txtTripTankVolume.setText((new DecimalFormat("###.0#")).format(MainDriver.gTripTankVolume)); 
			txtTripTankHeight.setText((new DecimalFormat("###.0#")).format(MainDriver.gTripTankHeight)); 
			txtConnTime.setText((new DecimalFormat("##0")).format(MainDriver.gConnTime)); 
			txtJointLength.setText((new DecimalFormat("###.0#")).format(MainDriver.gJointLength)); 
			txtJointNumber.setText((new DecimalFormat("#0")).format(MainDriver.igJointNumber)); 
			txtPbleed.setText((new DecimalFormat("##0.0")).format(MainDriver.gBleedPressure)); 
			txtTbleed.setText((new DecimalFormat("##0")).format(MainDriver.gBleedTime)); 
		}
		else{
			FrameTrip.setVisible(false);
			lblTripData.setVisible(false);
			txtROP.setText((new DecimalFormat("##0.#")).format(MainDriver.ROPen)); 
			lblROP.setText("ft/hr, Rate of Penetration (ROP)");
		}
	}

	void Assign_Cduct_Offshore(){
		//7/21/2001
		// ..... assign offshore data and conductor and surface casing data
		//       These are used only for well drawing purpose !
		//
		if(MainDriver.iCduct == 1){
			txtCductDepth.setText(Double.toString(MainDriver.DepthCduct)); 
			txtCductOD.setText(Double.toString(MainDriver.ODcduct));
			txtCductDepth.setVisible(true);
			txtCductOD.setVisible(true);
			lblCductDepth.setVisible(true); 
			lblCductOD.setVisible(true);
			checkConductor.setText("Conductor Used");
			checkConductor.setSelected(true);  //checked
		}
		else{
			txtCductDepth.setVisible(false); 
			txtCductOD.setVisible(false);
			lblCductDepth.setVisible(false);
			lblCductOD.setVisible(false);
			checkConductor.setText("Conductor Unused");
			checkConductor.setSelected(false);  //unchecked
		}
		//
		if (MainDriver.iSurfCsg == 1){
			txtScasingDepth.setText(Double.toString(MainDriver.DepthSurfCsg));
			txtScasingOD.setText(Double.toString(MainDriver.ODsurfCsg));
			txtScasingDepth.setVisible(true);
			txtScasingOD.setVisible(true);
			lblScasingDepth.setVisible(true);
			lblScasingOD.setVisible(true);
			checkScasing.setText("Surface Casing Used");
			checkScasing.setSelected(true);  //checked
		}
		else{
			txtScasingDepth.setVisible(false);
			txtScasingOD.setVisible(false);
			lblScasingDepth.setVisible(false);
			lblScasingOD.setVisible(false);
			checkScasing.setText("Surface Casing Unused");
			checkScasing.setSelected(false);  //checked
		}
		if (MainDriver.iOnshore == 1) {
			frameOffshore.setVisible(false);
			lblOffshore.setVisible(false);
		}
		else{
			frameOffshore.setVisible(true);
			lblOffshore.setVisible(true);
			txtWaterDepth.setText((new DecimalFormat("#####0.#")).format(MainDriver.Dwater));
			txtWaterTempGrad.setText((new DecimalFormat("#0.###")).format(MainDriver.tWgrad));
			txtRiserID.setText((new DecimalFormat("##0.###")).format(MainDriver.Driser));
			txtChokeID.setText((new DecimalFormat("##0.###")).format(MainDriver.Dchoke));
			txtKilliD.setText((new DecimalFormat("##0.###")).format(MainDriver.Dkill));
		}
	}

	void Assign_Pump(){

		//sldPumpRate.setValue((int)(MainDriver.spMinD + 0.05));
		//sldKillPumpRate.setValue((int)(MainDriver.spMin + 0.05));

		sldPumpRate1.setValue((int)(MainDriver.spMinD1 + 0.05));
		sldKillPumpRate1.setValue((int)(MainDriver.spMin1 + 0.05));
		sldPumpRate2.setValue((int)(MainDriver.spMinD2 + 0.05));
		sldKillPumpRate2.setValue((int)(MainDriver.spMin2 + 0.05));
		//

		if (MainDriver.iType == 1) comboSurfaceConn.setSelectedItem("Eqv_100 ft");
		else if (MainDriver.iType == 2) comboSurfaceConn.setSelectedItem("Eqv_150 ft");
		else if (MainDriver.iType == 3) comboSurfaceConn.setSelectedItem("Eqv_250 ft");
		else if (MainDriver.iType == 4) comboSurfaceConn.setSelectedItem("Eqv_650 ft");
		else comboSurfaceConn.setSelectedItem("Ignored");

		// Set default data or input data if there is any change
		/*txtLinerDia.setText((new DecimalFormat("#0.#")).format(MainDriver.DiaLiner));
	    txtRodDia.setText((new DecimalFormat("#0.#")).format(MainDriver.Drod));
	    txtStrokeLength.setText((new DecimalFormat("##0.#")).format(MainDriver.strokeLength));
	    txtPumpEff.setText((new DecimalFormat("0.####")).format(MainDriver.Effcy)); 
	    txtDrillSPM.setText((new DecimalFormat("##0")).format(MainDriver.spMinD));
	    txtKillSPM.setText((new DecimalFormat("##0")).format(MainDriver.spMin)); */
		txtQcapacity1.setHorizontalAlignment(SwingConstants.RIGHT);

		txtQcapacity1.setText((new DecimalFormat("######0.###")).format(MainDriver.Qcapacity1));
		txtQcapacity2.setText((new DecimalFormat("######0.###")).format(MainDriver.Qcapacity2));
		txtQcapacity2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDrillSPM1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDrillSPM1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
		txtKillSPM1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKillSPM1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
		txtDrillSPM2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDrillSPM2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
		txtKillSPM2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKillSPM2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));

		calcRate();
		SurfaceEquip();
	}

	void Assign_Fluid_Bit(){
		if(MainDriver.iData == 1) optShearRate.setSelected(true); //shear stress readings
		else optViscosity.setSelected(true);
		txtRoughness.setHorizontalAlignment(SwingConstants.RIGHT);

		// Set default data or input data if there is any change
		txtRoughness.setText((new DecimalFormat("0.######")).format(MainDriver.Ruf)); 
		txtGasGravity.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGasGravity.setText((new DecimalFormat("#0.###")).format(MainDriver.gasGravity)); 
		txtCO2fraction.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCO2fraction.setText((new DecimalFormat("0.###")).format(MainDriver.fCO2));
		txtH2Sfraction.setHorizontalAlignment(SwingConstants.RIGHT);
		txtH2Sfraction.setText((new DecimalFormat("0.###")).format(MainDriver.fH2S));
		txtMudDensity.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMudDensity.setText((new DecimalFormat("##0.##")).format(MainDriver.oMud));
		txtCritReyNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCritReyNumber.setText((new DecimalFormat("####0")).format(MainDriver.RenC));
		txtSurfaceTemp.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSurfaceTemp.setText((new DecimalFormat("##0.#")).format(MainDriver.Tsurf));
		txtTempGrad.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTempGrad.setText((new DecimalFormat("#0.###")).format(MainDriver.Tgrad));
		txtMudComp.setHorizontalAlignment(SwingConstants.RIGHT);
		//
		txtMudComp.setText((new DecimalFormat("####.##")).format(MainDriver.MudComp * 1000000));
		txtSS100.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSS100.setText((new DecimalFormat("##0.#")).format(MainDriver.SS100));
		txtSS3.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSS3.setText((new DecimalFormat("##0.#")).format(MainDriver.SS3));
		txtNozzleDia0.setHorizontalAlignment(SwingConstants.CENTER);
		//
		txtNozzleDia0.setText((new DecimalFormat("##0.#")).format(MainDriver.Dnoz[0])); 
		txtNozzleDia1.setHorizontalAlignment(SwingConstants.CENTER);
		txtNozzleDia1.setText((new DecimalFormat("##0.#")).format(MainDriver.Dnoz[1])); 
		txtNozzleDia2.setHorizontalAlignment(SwingConstants.CENTER);
		txtNozzleDia2.setText((new DecimalFormat("##0.#")).format(MainDriver.Dnoz[2])); 
		txtNozzleDia3.setHorizontalAlignment(SwingConstants.CENTER);
		txtNozzleDia3.setText((new DecimalFormat("##0.#")).format(MainDriver.Dnoz[3])); 

		//
		if(MainDriver.Model == 2) txtRoughness.setEnabled(true);
		else txtRoughness.setEnabled(false);

		if (MainDriver.iData == 1){
			txtSS600.setText((new DecimalFormat("##0.#")).format(MainDriver.S600));
			txtSS300.setText((new DecimalFormat("##0.#")).format(MainDriver.S300));
			lblSS600.setText("Shear Stress Reading at 600 rpm");
			lblSS300.setText("Shear Stress Reading at 300 rpm");
		}

		else{
			pViscos = MainDriver.S600 - MainDriver.S300;
			Yp = MainDriver.S300 - pViscos;
			txtSS600.setText((new DecimalFormat("##0.#")).format(pViscos));
			txtSS300.setText((new DecimalFormat("##0.#")).format(Yp));	
			lblSS600.setText("cp, Plastic Viscosity");
			lblSS300.setText("lbf/100 sq. ft, Yield Stress");
		}
	}


	void Assign_Options(){
		//7/28/2001
		if(MainDriver.iOnshore == 1) optOnshore.setSelected(true);
		else optOffshore.setSelected(true);

		if(MainDriver.Method == 1) optDrillerMethod.setSelected(true);
		else optEngMethod.setSelected(true);

		if(MainDriver.Model == 0) optAPIRP13D.setSelected(true);
		else if(MainDriver.Model == 1) optPower.setSelected(true);
		else if(MainDriver.Model == 2) optBingham.setSelected(true);
		else optNewtonian.setSelected(true);	

		if(MainDriver.iDelp == 1) optWithDP.setSelected(true);
		else optWithoutDP.setSelected(true);

		if(MainDriver.iZfact == 2) optIdealGas.setSelected(true);	
		else optRealGas.setSelected(true);

		//-------------------------------------------- June 28, 2002
		if(MainDriver.iMudComp ==1) OptWithMudComp.setSelected(true);
		else optWithoutMudComp.setSelected(true);



		if(MainDriver.igERD ==0) optERD.setSelected(true);
		else optML.setSelected(true);

		if(MainDriver.iBlowout == 1) optWithBlowout.setSelected(true);	
		else optWithoutBlowout.setSelected(true);

		// Added by TY

		if(MainDriver.imud == 0) optWBM.setSelected(true);
		else optOBM.setSelected(true);		

		//Annulus ¿Âµµ °è»ê ½Ã¿¡ Heat transfer¸¦ °í·ÁÇÏ´ÂÁö ¿©ºÎ
		if(MainDriver.iHeattrans == 1) optWithHT.setSelected(true);
		else optWithoutHT.setSelected(true);	

		if(MainDriver.imultikick == 1) optwithmultikick.setSelected(true);
		else optwithoutmultikick.setSelected(true);	

	}	

	void CsgOffPanelSetting(){		
		int CompSrtX = 15, CompSrtY = 20, CompSizeX = 60, CompSizeY = 23, CompIntvX=5, CompIntvY = 5, miniIntvY=2;
		int CondSurfCsgSrtX = 10, CondSurfCsgSrtY=60, CondSurfCsgSizeX=360, CondSurfCsgSizeY=2*CompSrtY+6*CompSizeY+9*CompIntvY;
		int FrameOffshoreCsgSrtX = CondSurfCsgSrtX+CondSurfCsgSizeX+20, FrameOffshoreCsgSrtY=CondSurfCsgSrtY, FrameOffshoreCsgSizeX=330, FrameOffshoreCsgSizeY=2*CompSrtY+5*CompSizeY+4*CompIntvY;

		PnlCasingOff.setLayout(null);
		lblOffshoreRelatedData.setForeground(Color.BLUE);
		lblOffshoreRelatedData.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblOffshoreRelatedData.setBounds(12, 10, 617, 14);		
		PnlCasingOff.add(lblOffshoreRelatedData);

		// Offshore Data
		lblOffshore.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblOffshore.setBounds(FrameOffshoreCsgSrtX+10, FrameOffshoreCsgSrtY-7, 89, 15);
		lblOffshore.setOpaque(true);
		PnlCasingOff.add(lblOffshore);
		frameOffshore.setLayout(null);		
		frameOffshore.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frameOffshore.setBounds(FrameOffshoreCsgSrtX, FrameOffshoreCsgSrtY, FrameOffshoreCsgSizeX, FrameOffshoreCsgSizeY);
		PnlCasingOff.add(frameOffshore);		
		txtWaterDepth.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWaterDepth.setText("0");

		txtWaterDepth.setBounds(CompSrtX, CompSrtY, CompSizeX, CompSizeY);
		txtWaterDepth.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameOffshore.add(txtWaterDepth);

		lblWaterDepth.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY, 100, CompSizeY);
		frameOffshore.add(lblWaterDepth);
		txtWaterTempGrad.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWaterTempGrad.setText("0");

		txtWaterTempGrad.setBounds(CompSrtX, CompSrtY+CompSizeY+CompIntvY, CompSizeX, CompSizeY);
		txtWaterTempGrad.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameOffshore.add(txtWaterTempGrad);

		lblWaterTempGrad.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+CompSizeY+CompIntvY, 240, CompSizeY);
		frameOffshore.add(lblWaterTempGrad);
		txtRiserID.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRiserID.setText("0");

		txtRiserID.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*2, CompSizeX, CompSizeY);
		txtRiserID.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameOffshore.add(txtRiserID);

		lblRiserID.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*2, 154, CompSizeY);
		frameOffshore.add(lblRiserID);
		txtChokeID.setHorizontalAlignment(SwingConstants.RIGHT);
		txtChokeID.setText("0");

		txtChokeID.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*3, CompSizeX, CompSizeY);
		txtChokeID.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameOffshore.add(txtChokeID);

		lblChokeID.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*3, 199, CompSizeY);
		frameOffshore.add(lblChokeID);
		txtKilliD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKilliD.setText("0");

		txtKilliD.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*4, CompSizeX, CompSizeY);
		txtKilliD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameOffshore.add(txtKilliD);


		lblKilliD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*4, 199, CompSizeY);
		frameOffshore.add(lblKilliD);
		//

		//Conductor or Surface Casing Data
		lblCondSurfCsg.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblCondSurfCsg.setBounds(CondSurfCsgSrtX+10, CondSurfCsgSrtY-7, 225, 15);
		lblCondSurfCsg.setOpaque(true);
		PnlCondSurfCsg.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlCondSurfCsg.setBounds(CondSurfCsgSrtX, CondSurfCsgSrtY, CondSurfCsgSizeX, CondSurfCsgSizeY);
		PnlCondSurfCsg.setLayout(null);
		PnlCasingOff.add(lblCondSurfCsg);
		PnlCasingOff.add(PnlCondSurfCsg);		
		checkConductor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkConductor.isSelected() == true){
					txtCductDepth.setVisible(true); 
					txtCductOD.setVisible(true);
					lblCductDepth.setVisible(true);
					lblCductOD.setVisible(true);
					checkConductor.setText("Conductor Used");
					MainDriver.iCduct = 1;
				}
				else{
					txtCductDepth.setVisible(false);
					txtCductOD.setVisible(false);
					lblCductDepth.setVisible(false);
					lblCductOD.setVisible(false);
					checkConductor.setText("Conductor Unused");
					MainDriver.iCduct = 0;
				}
			}
		});

		checkConductor.setFont(new Font("±¼¸²", Font.BOLD, 11));
		checkConductor.setBounds(CompSrtX, CompSrtY, 200, CompSizeY);
		PnlCondSurfCsg.add(checkConductor);
		txtCductDepth.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCductDepth.setText("0");

		txtCductDepth.setBounds(CompSrtX+CompIntvX, CompSrtY+CompSizeY+CompIntvY, CompSizeX, CompSizeY);
		txtCductDepth.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlCondSurfCsg.add(txtCductDepth);

		lblCductDepth.setBounds(CompSrtX+CompSizeX+2*CompIntvX, CompSrtY+CompSizeY+CompIntvY, 250, CompSizeY);
		PnlCondSurfCsg.add(lblCductDepth);
		txtCductOD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCductOD.setText("0");

		txtCductOD.setBounds(CompSrtX+CompIntvX, CompSrtY+2*CompSizeY+2*CompIntvY, CompSizeX, CompSizeY);
		txtCductOD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlCondSurfCsg.add(txtCductOD);

		lblCductOD.setBounds(CompSrtX+CompSizeX+2*CompIntvX, CompSrtY+2*CompSizeY+2*CompIntvY, 146, CompSizeY);
		PnlCondSurfCsg.add(lblCductOD);
		checkScasing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkScasing.isSelected() == true){
					txtScasingDepth.setVisible(true);
					txtScasingOD.setVisible(true);
					lblScasingDepth.setVisible(true);
					lblScasingOD.setVisible(true);
					checkScasing.setText("Surface Casing Used");
					MainDriver.iSurfCsg = 1;
				}
				else{
					txtScasingDepth.setVisible(false); 
					txtScasingOD.setVisible(false);
					lblScasingDepth.setVisible(false);
					lblScasingOD.setVisible(false);
					checkScasing.setText("Surface Casing Unused");
					MainDriver.iSurfCsg = 0;
				}
			}
		});

		checkScasing.setFont(new Font("±¼¸²", Font.BOLD, 11));
		checkScasing.setBounds(CompSrtX, CompSrtY+3*CompSizeY+6*CompIntvY, 210, CompSizeY);
		PnlCondSurfCsg.add(checkScasing);
		txtScasingDepth.setHorizontalAlignment(SwingConstants.RIGHT);
		txtScasingDepth.setText("0");

		txtScasingDepth.setBounds(CompSrtX+CompIntvX, CompSrtY+4*CompSizeY+7*CompIntvY, CompSizeX, CompSizeY);
		txtScasingDepth.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlCondSurfCsg.add(txtScasingDepth);

		lblScasingDepth.setBounds(CompSrtX+CompSizeX+2*CompIntvX, CompSrtY+4*CompSizeY+7*CompIntvY, 258, CompSizeY);
		PnlCondSurfCsg.add(lblScasingDepth);
		txtScasingOD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtScasingOD.setText("0");

		txtScasingOD.setBounds(CompSrtX+CompIntvX, CompSrtY+5*CompSizeY+8*CompIntvY, CompSizeX, CompSizeY);
		txtScasingOD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlCondSurfCsg.add(txtScasingOD);

		lblScasingOD.setBounds(CompSrtX+CompSizeX+2*CompIntvX, CompSrtY+5*CompSizeY+8*CompIntvY, 175, CompSizeY);
		PnlCondSurfCsg.add(lblScasingOD);		
		//				
		OKButton[0].setBounds(400,510,100,23);
		ApplyButton[0].setBounds(510,510,100,23);
		CancelButton[0].setBounds(620,510,100,23);
		PnlCasingOff.add(OKButton[0]);
		PnlCasingOff.add(ApplyButton[0]);
		//PnlCasingOff.add(CancelButton[0]);
	}

	void ChokeFormPanelSetting(){
		int CompSrtX = 15, CompSrtY = 25, CompSizeX = 60, CompSizeY = 23, CompIntvX=5, CompIntvY = 5, miniIntvY=2;
		int ShutInSrtX = 10, ShutInSrtY=70, ShutInSizeX=300, ShutInSizeY=2*CompSrtY+4*CompSizeY+3*CompIntvY;
		int FormPropSrtX = ShutInSrtX, FormPropSrtY=ShutInSrtY+ShutInSizeY+20, FormPropSizeX=ShutInSizeX, FormPropSizeY=2*CompSrtY+4*CompSizeY+3*CompIntvY;
		int SurfChkSrtX = ShutInSrtX, SurfChkSrtY=FormPropSrtY+FormPropSizeY+20, SurfChkSizeX=ShutInSizeX, SurfChkSizeY=2*CompSrtY+2*CompSizeY+1*CompIntvY;
		int TripDataSrtX = ShutInSrtX+ShutInSizeX+20, TripDataSrtY=ShutInSrtY, TripDataSizeX=370, TripDataSizeY=2*CompSrtY+8*CompSizeY+9*CompIntvY;		

		lblChkFormWarning.setForeground(Color.BLUE);
		lblChkFormWarning.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblChkFormWarning.setBounds(ShutInSrtX, ShutInSrtX, 745, 32);		
		PnlChokeForm.add(lblChkFormWarning);
		PnlChokeForm.setLayout(null);

		OKButton[1].setBounds(400,510,100,23);
		ApplyButton[1].setBounds(510,510,100,23);
		CancelButton[1].setBounds(620,510,100,23);
		PnlChokeForm.add(OKButton[1]);
		PnlChokeForm.add(ApplyButton[1]);
		//PnlChokeForm.add(CancelButton[1]);

		// Trip Data
		lblTripData.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblTripData.setBounds(TripDataSrtX+10, TripDataSrtY-7, 60, 15);
		lblTripData.setOpaque(true);
		PnlChokeForm.add(lblTripData);
		FrameTrip.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FrameTrip.setBounds(TripDataSrtX, TripDataSrtY, TripDataSizeX, TripDataSizeY);		
		PnlChokeForm.add(FrameTrip);
		FrameTrip.setLayout(null);
		txtTripTankVolume.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTripTankVolume.setText("0");

		txtTripTankVolume.setBounds(CompSrtX, CompSrtY, CompSizeX, CompSizeY);
		txtTripTankVolume.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtTripTankVolume);		
		lblTripTankVolume.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY, 267, CompSizeY);
		FrameTrip.add(lblTripTankVolume);
		txtTripTankHeight.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTripTankHeight.setText("0");
		txtTripTankHeight.setBounds(CompSrtX, CompSrtY+CompSizeY+CompIntvY, CompSizeX, CompSizeY);
		txtTripTankHeight.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtTripTankHeight);		
		lblTripTankHeight.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+CompSizeY+CompIntvY, 267, CompSizeY);
		FrameTrip.add(lblTripTankHeight);		
		txtConnTime.setHorizontalAlignment(SwingConstants.RIGHT);
		txtConnTime.setText("0");
		txtConnTime.setBounds(CompSrtX, CompSrtY+2*CompSizeY+3*CompIntvY, CompSizeX, CompSizeY);
		txtConnTime.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtConnTime);		
		lblConnTime.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+2*CompSizeY+3*CompIntvY, 267, CompSizeY);
		FrameTrip.add(lblConnTime);		
		txtJointLength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtJointLength.setText("0");
		txtJointLength.setBounds(CompSrtX, CompSrtY+3*CompSizeY+4*CompIntvY, CompSizeX, CompSizeY);
		txtJointLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtJointLength);		
		lblJointLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+3*CompSizeY+4*CompIntvY, 267, CompSizeY);
		FrameTrip.add(lblJointLength);		
		txtJointNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		txtJointNumber.setText("0");
		txtJointNumber.setBounds(CompSrtX, CompSrtY+4*CompSizeY+5*CompIntvY, CompSizeX, CompSizeY);
		txtJointNumber.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtJointNumber);		
		lblJointNumber.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+4*CompSizeY+5*CompIntvY, 267, CompSizeY);		
		FrameTrip.add(lblJointNumber);		
		txtStandLength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStandLength.setText("0");
		txtStandLength.setBackground(Color.YELLOW);
		txtStandLength.setBounds(CompSrtX, CompSrtY+5*CompSizeY+6*CompIntvY, CompSizeX, CompSizeY);
		txtStandLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtStandLength);
		lblStandLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+5*CompSizeY+6*CompIntvY, 267, CompSizeY);		
		FrameTrip.add(lblStandLength);
		txtPbleed.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPbleed.setText("0");
		txtPbleed.setBounds(CompSrtX, CompSrtY+6*CompSizeY+8*CompIntvY, CompSizeX, CompSizeY);		
		txtPbleed.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtPbleed);
		lblPbleed.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+6*CompSizeY+8*CompIntvY, 267, CompSizeY);		
		FrameTrip.add(lblPbleed);
		txtTbleed.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTbleed.setText("0");
		txtTbleed.setBounds(CompSrtX, CompSrtY+7*CompSizeY+9*CompIntvY, CompSizeX, CompSizeY);	
		txtTbleed.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameTrip.add(txtTbleed);
		lblTbleed.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+7*CompSizeY+9*CompIntvY, 267, CompSizeY);		
		FrameTrip.add(lblTbleed);	

		txtJointLength.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void insertUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void removeUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			private void printIt(DocumentEvent documentEvent) {
				double tmpValue = 0;
				try{
					tmpValue = Double.parseDouble(txtJointLength.getText()) * Double.parseDouble(txtJointNumber.getText());
					txtStandLength.setText((new DecimalFormat("###.0#")).format(tmpValue)); 
				} catch(Exception e){
					tmpValue=0;					
				}			
			}});

		txtJointNumber.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void insertUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void removeUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			private void printIt(DocumentEvent documentEvent) {
				double tmpValue = 0;
				try{
					tmpValue = Double.parseDouble(txtJointLength.getText()) * Double.parseDouble(txtJointNumber.getText());
					txtStandLength.setText((new DecimalFormat("###.0#")).format(tmpValue)); 
				} catch(Exception e){
					tmpValue=0;					
				}			
			}});
		//		

		// Surface Choke Valve
		lblSurfChoke.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblSurfChoke.setBounds(SurfChkSrtX+10, SurfChkSrtY-7, 137, 15);
		lblSurfChoke.setOpaque(true);
		PnlChokeForm.add(lblSurfChoke);
		pnlSurfChoke.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlSurfChoke.setLayout(null);
		pnlSurfChoke.setBounds(SurfChkSrtX, SurfChkSrtY, SurfChkSizeX, SurfChkSizeY);
		PnlChokeForm.add(pnlSurfChoke);

		lblEquivIDChkValve.setBounds(CompSrtX, CompSrtY, 215, CompSizeY);
		pnlSurfChoke.add(lblEquivIDChkValve);
		txtCHKdia.setText("4");

		txtCHKdia.setBackground(Color.YELLOW);
		txtCHKdia.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtCHKdia.setBounds(CompSrtX, CompSrtY+CompSizeY+CompIntvY, CompSizeX, CompSizeY);
		pnlSurfChoke.add(txtCHKdia);
		HscrlCHK.setVisibleAmount(1);
		HscrlCHK.setForeground(Color.WHITE);
		HscrlCHK.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				MainDriver.DchkControl = HscrlCHK.getValue();
				txtCHKdia.setText((new DecimalFormat("#0")).format(MainDriver.DchkControl));
			}
		});

		HscrlCHK.setBackground(Color.WHITE);		
		HscrlCHK.setValue(4);
		HscrlCHK.setMinimum(1);
		HscrlCHK.setMaximum(6);
		HscrlCHK.setOrientation(JScrollBar.HORIZONTAL);
		HscrlCHK.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+CompSizeY+CompIntvY, 200, 23);
		pnlSurfChoke.add(HscrlCHK);		
		//

		// Formation Properties
		lblFormProp.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblFormProp.setOpaque(true);
		lblFormProp.setBounds(FormPropSrtX+10, FormPropSrtY-7, 137, 15);
		PnlChokeForm.add(lblFormProp);
		PnlFormProp.setLayout(null);
		PnlFormProp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlFormProp.setBounds(FormPropSrtX, FormPropSrtY, FormPropSizeX, FormPropSizeY);		
		PnlChokeForm.add(PnlFormProp);		

		txtPerm.setBounds(CompSrtX, CompSrtY, CompSizeX, CompSizeY);
		txtPerm.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlFormProp.add(txtPerm);

		lblPerm.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY, 96, CompSizeY);
		PnlFormProp.add(lblPerm);

		txtPorosity.setBounds(CompSrtX, CompSrtY+CompSizeY+CompIntvY, CompSizeX, CompSizeY);
		txtPorosity.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlFormProp.add(txtPorosity);

		lblPorosity.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+CompSizeY+CompIntvY, 120, CompSizeY);
		PnlFormProp.add(lblPorosity);

		txtSkin.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*2, CompSizeX, CompSizeY);
		txtSkin.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlFormProp.add(txtSkin);

		lblSkin.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*2, 176, 23);
		PnlFormProp.add(lblSkin);
		txtROP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtROP.setText("0");
		txtROP.setBackground(Color.WHITE);
		txtROP.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtROP.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*3, CompSizeX, CompSizeY);		
		PnlFormProp.add(txtROP);
		lblROP.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*3, 176, CompSizeY);		
		PnlFormProp.add(lblROP);
		//		

		// Shut-In Data		
		lblShutinData.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblShutinData.setBounds(ShutInSrtX+10, ShutInSrtY-7, 82, 15);
		lblShutinData.setOpaque(true);
		PnlChokeForm.add(lblShutinData);
		pnlShutinData.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlShutinData.setBounds(ShutInSrtX, ShutInSrtY, ShutInSizeX, ShutInSizeY);
		PnlChokeForm.add(pnlShutinData);
		pnlShutinData.setLayout(null);

		txtKickIntens.setBounds(CompSrtX, CompSrtY, CompSizeX, CompSizeY);
		txtKickIntens.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlShutinData.add(txtKickIntens);
		lblKickIntens.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY, 193, CompSizeY);
		pnlShutinData.add(lblKickIntens);
		txtPGwarning.setBounds(CompSrtX, CompSrtY+CompSizeY+CompIntvY, CompSizeX, CompSizeY);
		txtPGwarning.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlShutinData.add(txtPGwarning);		
		lblPGwarning.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY), 193, CompSizeY);
		pnlShutinData.add(lblPGwarning);		
		txtKillMudWt.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKillMudWt.setText("0");
		txtKillMudWt.setBackground(Color.YELLOW);
		txtKillMudWt.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtKillMudWt.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*2, CompSizeX, CompSizeY);
		pnlShutinData.add(txtKillMudWt);		
		lblKillMudWt.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*2, 180, CompSizeY);
		pnlShutinData.add(lblKillMudWt);		
		txtSIDPP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSIDPP.setText("0");
		txtSIDPP.setBackground(Color.YELLOW);
		txtSIDPP.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtSIDPP.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*3, CompSizeX, CompSizeY);
		pnlShutinData.add(txtSIDPP);
		lblSIDPP.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*3, 117, CompSizeY);
		pnlShutinData.add(lblSIDPP);

		txtKickIntens.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void insertUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void removeUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			private void printIt(DocumentEvent documentEvent) {
				try{
					calcKmud();
				} catch(Exception e){			
				}			
			}});
		//	
	}

	void calcKmud(){
		//..... avoid to calculate and update global variables (by using the variable)
		//      for intermediate calculations   '1/19/03
		//
		double KmudTmp = 0, overPrs=0;
		KmudTmp = MainDriver.oMud + Double.parseDouble(txtKickIntens.getText());
		txtKillMudWt.setText((new DecimalFormat("##0.##")).format(KmudTmp));
		overPrs = 0.052 * Double.parseDouble(txtKickIntens.getText()) * MainDriver.Vdepth;
		txtSIDPP.setText((new DecimalFormat("###0.#")).format(overPrs));
		if(overPrs < 0.05) txtSIDPP.setText("0.0");
		//
	}

	void WellGeometryPanelSetting(){
		int CompSrtX = 15, CompSrtY = 20, CompSizeX = 60, CompSizeY = 23, CompIntvX=5, CompIntvY = 5, miniIntvY=2;
		int TypeWellTrajtSrtX = 10, TypeWellTrajtSrtY=40, TypeWellTrajtSizeX=280, TypeWellTrajtSizeY=70;
		int WellDrillStringSrtX = TypeWellTrajtSrtX, WellDrillStringSrtY = TypeWellTrajtSrtY+TypeWellTrajtSizeY+20, WellDrillStringSizeX = TypeWellTrajtSizeX, WellDrillStringSizeY=CompSrtY+13*CompIntvY+12*CompSizeY+5*miniIntvY;
		int HorizWellSrtX = TypeWellTrajtSrtX+TypeWellTrajtSizeX+30, HorizWellSrtY=TypeWellTrajtSrtY, HorizWellSizeX=350, HorizWellSizeY=TypeWellTrajtSizeY;
		int DirectSrtX = HorizWellSrtX, DirectSrtY=WellDrillStringSrtY, DirectSizeX=HorizWellSizeX, DirectSizeY = 2*CompSrtX+10*CompSizeY+10*CompIntvY;		
		int optBtnSizeY=20;

		PnlWellGeometry.setLayout(null);
		lblWellGeometryWarning.setForeground(Color.BLUE);
		lblWellGeometryWarning.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblWellGeometryWarning.setBounds(TypeWellTrajtSrtX, 10, 685, 15);
		PnlWellGeometry.add(lblWellGeometryWarning);		

		OKButton[2].setBounds(400,510,100,23);
		ApplyButton[2].setBounds(510,510,100,23);
		CancelButton[2].setBounds(620,510,100,23);
		PnlWellGeometry.add(OKButton[2]);
		PnlWellGeometry.add(ApplyButton[2]);
		//PnlWellGeometry.add(CancelButton[2]);

		// Directional Data
		lblDirect.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblDirect.setBounds(DirectSrtX+10, DirectSrtY-7, 104, 15);
		lblDirect.setOpaque(true);
		PnlWellGeometry.add(lblDirect);
		frameDirect.setLayout(null);
		frameDirect.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frameDirect.setBounds(DirectSrtX, DirectSrtY, DirectSizeX, DirectSizeY);		
		WhatToSpecifyGroup.add(optSetHD);
		WhatToSpecifyGroup.add(optSetBUR);
		PnlWellGeometry.add(frameDirect);
		optSetHD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iBUR = 0;
				dummy = check_DirectalData();
			}
		});
		optSetHD.setBounds(CompSrtX, CompSrtY, 289, optBtnSizeY);		
		frameDirect.add(optSetHD);
		optSetBUR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iBUR = 1;
				dummy = check_DirectalData();
			}
		});
		optSetBUR.setBounds(CompSrtX, CompSrtY+optBtnSizeY, 309, optBtnSizeY);		
		frameDirect.add(optSetBUR);

		lblTrajt.setForeground(Color.BLUE);
		lblTrajt.setFont(new Font("±¼¸²", Font.BOLD, 10));
		lblTrajt.setBounds(CompSrtX, CompSrtY+CompIntvY+2*CompSizeY+miniIntvY, 280, 15);

		frameDirect.add(lblTrajt);
		txtDepthKOP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDepthKOP.setBounds(CompSrtX, CompSrtY+2*CompIntvY+3*CompSizeY+miniIntvY, CompSizeX, CompSizeY);
		txtDepthKOP.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameDirect.add(txtDepthKOP);
		lblDepthKOP.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+2*CompIntvY+3*CompSizeY+miniIntvY, 240, CompSizeY);

		frameDirect.add(lblDepthKOP);
		txtFirstBUR.setHorizontalAlignment(SwingConstants.RIGHT);
		txtFirstBUR.setText("0");
		txtFirstBUR.setBounds(CompSrtX, CompSrtY+3*CompIntvY+4*CompSizeY+miniIntvY, CompSizeX, CompSizeY);
		txtFirstBUR.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameDirect.add(txtFirstBUR);
		lblFirstBUR.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+3*CompIntvY+4*CompSizeY+miniIntvY, 240, CompSizeY);

		frameDirect.add(lblFirstBUR);
		txtSecondBUR.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSecondBUR.setText("0");
		txtSecondBUR.setBounds(CompSrtX, CompSrtY+4*CompIntvY+5*CompSizeY+miniIntvY, CompSizeX, CompSizeY);
		txtSecondBUR.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameDirect.add(txtSecondBUR);
		lblSecondBUR.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+4*CompIntvY+5*CompSizeY+miniIntvY, 240, CompSizeY);

		frameDirect.add(lblSecondBUR);
		txtAngleEOB.setHorizontalAlignment(SwingConstants.RIGHT);
		txtAngleEOB.setText("0");
		txtAngleEOB.setBounds(CompSrtX, CompSrtY+5*CompIntvY+6*CompSizeY+miniIntvY, CompSizeX, CompSizeY);
		txtAngleEOB.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameDirect.add(txtAngleEOB);
		lblAngleEOB.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+5*CompIntvY+6*CompSizeY+miniIntvY, 270, CompSizeY);

		frameDirect.add(lblAngleEOB);
		txtHDtoTD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHDtoTD.setText("0");
		txtHDtoTD.setBounds(CompSrtX, CompSrtY+6*CompIntvY+7*CompSizeY+miniIntvY, CompSizeX, CompSizeY);
		txtHDtoTD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameDirect.add(txtHDtoTD);
		lblHDtoTD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+6*CompIntvY+7*CompSizeY+miniIntvY, 240, CompSizeY);

		frameDirect.add(lblHDtoTD);
		txtFinalHoldLength.setBounds(CompSrtX, CompSrtY+7*CompIntvY+8*CompSizeY+miniIntvY, CompSizeX, CompSizeY);
		txtFinalHoldLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtFinalHoldLength.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void insertUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			public void removeUpdate(DocumentEvent documentEvent) {
				printIt(documentEvent);
			}
			private void printIt(DocumentEvent documentEvent) {
				try{
					if(MainDriver.iWell == 4 && (int)(Double.parseDouble(txtFinalHoldLength.getText()) + 0.01) == 90){
						optLocalHP.setEnabled(true);
						optHomoRes.setEnabled(true);
					}
					else{
						optLocalHP.setEnabled(false);
						optHomoRes.setEnabled(false);
					}
				}catch(Exception e){}
			}
		});


		frameDirect.add(txtFinalHoldLength);
		lblFinalHoldLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+7*CompIntvY+8*CompSizeY+miniIntvY, 270, CompSizeY);

		frameDirect.add(lblFinalHoldLength);
		txtHorizLength.setBounds(CompSrtX, CompSrtY+8*CompIntvY+9*CompSizeY+miniIntvY, CompSizeX, CompSizeY);
		txtHorizLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		frameDirect.add(txtHorizLength);
		lblHorizLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+8*CompIntvY+9*CompSizeY+miniIntvY, 240, CompSizeY);

		frameDirect.add(lblHorizLength);		
		//		

		// Type of Horizontal Well
		lblHorizWell.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblHorizWell.setBounds(HorizWellSrtX+10, HorizWellSrtY-7, 153, 15);
		lblHorizWell.setOpaque(true);
		PnlWellGeometry.add(lblHorizWell);
		frameHorizWell.setLayout(null);
		frameHorizWell.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frameHorizWell.setBounds(HorizWellSrtX, HorizWellSrtY, HorizWellSizeX, HorizWellSizeY);

		HorizTypeGroup.add(optLocalHP);
		HorizTypeGroup.add(optHomoRes);
		PnlWellGeometry.add(frameHorizWell);
		optLocalHP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iHresv = 0;
			}
		});
		optLocalHP.setBounds(CompSrtX, CompSrtY, 191, optBtnSizeY);		
		frameHorizWell.add(optLocalHP);
		optHomoRes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iHresv = 0;
			}
		});
		optHomoRes.setBounds(CompSrtX, CompSrtY+optBtnSizeY, 165, optBtnSizeY);		
		frameHorizWell.add(optHomoRes);
		//		

		// Well and Drill String Data
		lblWellDrillString.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblWellDrillString.setBounds(WellDrillStringSrtX+10, WellDrillStringSrtY-7, 164, 15);
		lblWellDrillString.setOpaque(true);
		PnlWellGeometry.add(lblWellDrillString);

		pnlWellDrillString.setLayout(null);
		pnlWellDrillString.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlWellDrillString.setBounds(WellDrillStringSrtX, WellDrillStringSrtY, WellDrillStringSizeX, WellDrillStringSizeY);		
		PnlWellGeometry.add(pnlWellDrillString);

		txtVdepth.setBounds(CompSrtX, CompSrtY, CompSizeX, CompSizeY);	
		txtVdepth.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtVdepth);
		lblVdepth.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY, 149, CompSizeY);		
		pnlWellDrillString.add(lblVdepth);
		txtMDcasing.setBounds(CompSrtX, CompSrtY+1*CompIntvY+CompSizeY, CompSizeX, CompSizeY);		
		txtMDcasing.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtMDcasing);	
		lblMDcasing.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+1*CompIntvY+CompSizeY, 144, 34);
		pnlWellDrillString.add(lblMDcasing);
		txtCasingID.setBounds(CompSrtX, CompSrtY+3*CompIntvY+2*CompSizeY, CompSizeX, CompSizeY);	
		txtCasingID.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtCasingID);
		lblCasingID.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+3*CompIntvY+2*CompSizeY, 150, CompSizeY);		
		pnlWellDrillString.add(lblCasingID);
		txtHoleDia.setBounds(CompSrtX, CompSrtY+5*CompIntvY+3*CompSizeY, CompSizeX, CompSizeY);		
		txtHoleDia.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtHoleDia);
		lblHoleDia.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+5*CompIntvY+3*CompSizeY, 159, CompSizeY);		
		pnlWellDrillString.add(lblHoleDia);
		txtDPoD.setBounds(CompSrtX, CompSrtY+6*CompIntvY+4*CompSizeY, CompSizeX, CompSizeY);	
		txtDPoD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtDPoD);
		lblDPoD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+6*CompIntvY+4*CompSizeY, 159, CompSizeY);		
		pnlWellDrillString.add(lblDPoD);
		txtDPiD.setBounds(CompSrtX, CompSrtY+6*CompIntvY+5*CompSizeY+miniIntvY, CompSizeX, CompSizeY);		
		txtDPiD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtDPiD);
		lblDPiD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+6*CompIntvY+5*CompSizeY+miniIntvY, 111, CompSizeY);		
		pnlWellDrillString.add(lblDPiD);
		txtHWDPlength.setBounds(CompSrtX, CompSrtY+8*CompIntvY+6*CompSizeY+miniIntvY, CompSizeX, CompSizeY);		
		txtHWDPlength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtHWDPlength);
		lblHWDPlength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+8*CompIntvY+6*CompSizeY+miniIntvY, 175, CompSizeY);		
		pnlWellDrillString.add(lblHWDPlength);
		txtHWDPoD.setBounds(CompSrtX, CompSrtY+9*CompIntvY+7*CompSizeY+miniIntvY, CompSizeX, CompSizeY);		
		txtHWDPoD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtHWDPoD);
		lblHWDPoD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+9*CompIntvY+7*CompSizeY+miniIntvY, 175, CompSizeY);		
		pnlWellDrillString.add(lblHWDPoD);
		txtHWDPiD.setBounds(CompSrtX, CompSrtY+9*CompIntvY+8*CompSizeY+2*miniIntvY, CompSizeX, CompSizeY);		
		txtHWDPiD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtHWDPiD);
		lblHWDPiD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+9*CompIntvY+8*CompSizeY+2*miniIntvY, 175, CompSizeY);		
		pnlWellDrillString.add(lblHWDPiD);
		txtDClength.setBounds(CompSrtX, CompSrtY+11*CompIntvY+9*CompSizeY+2*miniIntvY, CompSizeX, CompSizeY);
		txtDClength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtDClength);
		lblDClength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+11*CompIntvY+9*CompSizeY+2*miniIntvY, 166, CompSizeY);		
		pnlWellDrillString.add(lblDClength);
		txtDCoD.setBounds(CompSrtX, CompSrtY+12*CompIntvY+10*CompSizeY+2*miniIntvY, CompSizeX, CompSizeY);		
		txtDCoD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtDCoD);
		lblDCoD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+12*CompIntvY+10*CompSizeY+2*miniIntvY, 132, CompSizeY);		
		pnlWellDrillString.add(lblDCoD);
		txtDCiD.setBounds(CompSrtX, CompSrtY+12*CompIntvY+11*CompSizeY+3*miniIntvY, CompSizeX, CompSizeY);	
		txtDCiD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlWellDrillString.add(txtDCiD);
		lblDCiD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+12*CompIntvY+11*CompSizeY+3*miniIntvY, 126, CompSizeY);		
		pnlWellDrillString.add(lblDCiD);		
		//

		//Type of Well Trajectory		
		lblTypeWellTrajt.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblTypeWellTrajt.setBounds(TypeWellTrajtSrtX+10, TypeWellTrajtSrtY-7, 154, 15);
		lblTypeWellTrajt.setOpaque(true);
		PnlWellGeometry.add(lblTypeWellTrajt);		
		pnlTypeWellTrajt.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlTypeWellTrajt.setBounds(TypeWellTrajtSrtX, TypeWellTrajtSrtY, TypeWellTrajtSizeX, TypeWellTrajtSizeY);		
		PnlWellGeometry.add(pnlTypeWellTrajt);
		pnlTypeWellTrajt.setLayout(null);

		comboWellTrajt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboWellTrajt.getSelectedItem() == "Vertical") MainDriver.iWell = 0;
				else if (comboWellTrajt.getSelectedItem() == "Build") MainDriver.iWell = 1;
				else if (comboWellTrajt.getSelectedItem() == "B_Hold") MainDriver.iWell = 2;
				else if (comboWellTrajt.getSelectedItem() == "B_H_Build") MainDriver.iWell = 3;
				else MainDriver.iWell = 4;
				dummy = check_DirectalData();
			}
		});
		DefaultComboBoxModel a1 = new DefaultComboBoxModel();
		comboWellTrajt.setModel(new DefaultComboBoxModel(new String[] {"Vertical", "Build", "B_Hold", "B_H_Build", "B_H_B_Hold"}));
		comboWellTrajt.setBounds(60, 30, 150, 20);		
		pnlTypeWellTrajt.add(comboWellTrajt);
		//		
	}

	int check_DirectalData(){
		//7/30/2001
		//..... check directional data and assign directional data with corresponding labels !
		//
		lblTrajt.setVisible(false); 
		frameDirect.setVisible(false);
		optLocalHP.setEnabled(false);
		optHomoRes.setEnabled(false);
		if(MainDriver.iWell == 0) return -1;
		if(MainDriver.iWell == 4 && (int)(Double.parseDouble(txtFinalHoldLength.getText()) + 0.01) == 90){
			optLocalHP.setEnabled(false);
			optHomoRes.setEnabled(false);
		}

		// set all properties true & check the type of well

		frameDirect.setVisible(true);
		lblTrajt.setVisible(true);	    
		txtDepthKOP.setVisible(true); 
		lblDepthKOP.setVisible(true);
		txtFirstBUR.setVisible(true); 
		lblFirstBUR.setVisible(true);
		txtSecondBUR.setVisible(true); 
		lblSecondBUR.setVisible(true);
		txtAngleEOB.setVisible(true); 
		lblAngleEOB.setVisible(true);
		txtHDtoTD.setVisible(true); 
		lblHDtoTD.setVisible(true);
		txtFinalHoldLength.setVisible(true);
		lblFinalHoldLength.setVisible(true);
		txtHorizLength.setVisible(false); 
		lblHorizLength.setVisible(false);
		lblHDtoTD.setText("ft, Horizontal Distance to TD");	    
		txtDepthKOP.setText((new DecimalFormat("#####0.##")).format(MainDriver.DepthKOP)); 
		optSetHD.setVisible(true);
		//
		/*MainDriver.BUR=((new DecimalFormat("##0.##")).format(MainDriver.BUR));
	    BUR2.setText((new DecimalFormat(MainDriver.BUR2, "##0.##")
	    angEOB.setText((new DecimalFormat(MainDriver.angEOB, "##0.##") ang2EOB.setText((new DecimalFormat(MainDriver.ang2EOB, "##0.##")
	    xHold.setText((new DecimalFormat(MainDriver.xHold, "#####0.#") x2Hold.setText((new DecimalFormat(MainDriver.x2Hold, "#####0.#")
	    VDeob.setText((new DecimalFormat(MainDriver.VDeob, "#####0.#") HDeob.setText((new DecimalFormat(MainDriver.HDeob, "#####0.#")
	    Hdisp.setText((new DecimalFormat(MainDriver.Hdisp, "#####0.#")*/
		//
		switch(MainDriver.iWell){  //visible 21, 22 or 25
		case 1:
			lblTrajt.setText("Vertical and Continuous Build Trajectory");
			txtSecondBUR.setVisible(false); 
			lblSecondBUR.setVisible(false);
			txtAngleEOB.setVisible(false); 
			lblAngleEOB.setVisible(false);
			txtFinalHoldLength.setVisible(false); 
			lblFinalHoldLength.setVisible(false);

			if (iBUR == 0){  //specity horizontal displacement
				txtFirstBUR.setVisible(false); 
				lblFirstBUR.setVisible(false);
				txtHDtoTD.setText((new DecimalFormat("####0.#")).format(MainDriver.Hdisp));
			}
			else{                // specify abgle at EOB or Hold length
				txtHDtoTD.setVisible(false); 
				lblHDtoTD.setVisible(false);
				txtFirstBUR.setText((new DecimalFormat("##0.##")).format(MainDriver.BUR));
			}
			break;

		case 2:   //Then visible 21, 22, 24 or 25
			lblTrajt.setText("Vertical and Build-Hold Trajectory");
			txtSecondBUR.setVisible(false); 
			lblSecondBUR.setVisible(false);
			txtFinalHoldLength.setVisible(false); 
			lblFinalHoldLength.setVisible(false);
			txtFirstBUR.setText((new DecimalFormat("##0.##")).format(MainDriver.BUR));

			if(iBUR == 0){
				txtAngleEOB.setVisible(false);
				lblAngleEOB.setVisible(false);
				txtHDtoTD.setText((new DecimalFormat("####0.#")).format(MainDriver.Hdisp));
				lblAngleEOB.setText("degree, Angle at the End of the Second Build");
			}
			else{
				lblAngleEOB.setText("degree, Angle at the End of the First Build");
				txtAngleEOB.setText((new DecimalFormat("##0.##")).format(MainDriver.angEOB));
				txtHDtoTD.setVisible(false); 
				lblHDtoTD.setVisible(false);
			}
			break;

		case 3:   //Then //visible 21, 22, 23, 24, 25
			lblTrajt.setText("Vertical and Build-Hold-Build Trajectory");
			txtFinalHoldLength.setVisible(false); 
			lblFinalHoldLength.setVisible(false);
			txtFirstBUR.setText((new DecimalFormat("##0.##")).format(MainDriver.BUR)); 
			txtSecondBUR.setText((new DecimalFormat("##0.##")).format(MainDriver.BUR2)); 

			if(iBUR == 0){
				lblAngleEOB.setText("degree, Angle at the End of the Second Build");
				txtAngleEOB.setText((new DecimalFormat("##0.##")).format(MainDriver.ang2EOB));
				txtHDtoTD.setText((new DecimalFormat("####0.#")).format(MainDriver.Hdisp));
			}
			else{
				lblAngleEOB.setText("degree, Angle at the End of the First Build");
				lblHDtoTD.setText("ft, Length of the First Hold Section");
				txtAngleEOB.setText((new DecimalFormat("##0.##")).format(MainDriver.angEOB));
				txtHDtoTD.setText((new DecimalFormat("####0.#")).format(MainDriver.xHold));
			}
			break;

		default:                  //MainDriver.iWell = 4 visible 21 to 26
			lblTrajt.setText("V_BHBH including Extended reach wellbore");
			txtDepthKOP.setVisible(false); 
			lblDepthKOP.setVisible(false);
			txtHorizLength.setVisible(true);
			lblHorizLength.setVisible(true);
			optSetHD.setVisible(false);
			lblAngleEOB.setText("degree, Angle at the End of the First Build");
			lblHDtoTD.setText("ft, Length of the First Hold Section");
			lblFinalHoldLength.setText("degree, Angle at the End of the Second Build");
			lblHorizLength.setText("ft, Length of Final Hold Section");

			txtFirstBUR.setText((new DecimalFormat("##0.##")).format(MainDriver.BUR));
			txtSecondBUR.setText((new DecimalFormat("##0.##")).format(MainDriver.BUR2));
			txtAngleEOB.setText((new DecimalFormat("##0.##")).format(MainDriver.angEOB));
			txtHDtoTD.setText((new DecimalFormat("####0.#")).format(MainDriver.xHold));
			txtFinalHoldLength.setText((new DecimalFormat("##0.##")).format(MainDriver.ang2EOB));
			txtHorizLength.setText((new DecimalFormat("####0.#")).format(MainDriver.x2Hold));
		}
		return 0;
		//
	}

	void PumpPanelSetting(){
		int CompSrtX = 15, CompSrtY = 15, CompSizeX = 60, CompSizeY = 23, CompIntvX=5, CompIntvY = 5;
		int PumpTypeSizeX = 270, PumpTypeSizeY = CompSrtY+(CompSizeY+CompIntvY)*2+35;
		int PumpTypeSrtX = 30, PumpTypeSrtY= 40;
		int PumpCircSrtX = PumpTypeSrtX, PumpCircSrtY = PumpTypeSrtY+PumpTypeSizeY+20, PumpCircSizeX=PumpTypeSizeX, PumpCircSizeY = CompSrtY+16*2+31*2+9*CompIntvY+4*CompSizeY+10;
		int TypeSurfConSrtX =PumpTypeSrtX+PumpTypeSizeX+CompIntvX*4, TypeSurfConSrtY=PumpTypeSrtY;
		int TypeSurfConSizeX = 350, TypeSurfConSizeY = 2*CompSrtY+28;
		int SurfConDataSrtX =TypeSurfConSrtX, SurfConDataSrtY=TypeSurfConSrtY+TypeSurfConSizeY+4*CompIntvY;
		int SurfConDataSizeX=TypeSurfConSizeX, SurfConDataSizeY=2*CompSrtY+11*CompSizeY+15*CompIntvY;
		PnlPump.setLayout(null);
		lblWarning.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblWarning.setForeground(Color.BLUE);
		lblWarning.setBounds(10, 7, 685, 15);
		PnlPump.add(lblWarning);

		OKButton[3].setBounds(400,510,100,23);
		ApplyButton[3].setBounds(510,510,100,23);
		CancelButton[3].setBounds(620,510,100,23);
		PnlPump.add(OKButton[3]);
		PnlPump.add(ApplyButton[3]);
		//PnlPump.add(CancelButton[3]);

		// Surface Connection Data
		lblSurfConData.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblSurfConData.setBounds(SurfConDataSrtX+10, SurfConDataSrtY-7, 163, 15);
		lblSurfConData.setOpaque(true);
		PnlPump.add(lblSurfConData);		
		FrameSurfaceEq.setLayout(null);
		FrameSurfaceEq.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FrameSurfaceEq.setBounds(SurfConDataSrtX, SurfConDataSrtY, SurfConDataSizeX, SurfConDataSizeY);		
		PnlPump.add(FrameSurfaceEq);
		txtIDtoSP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIDtoSP.setText("0");

		txtIDtoSP.setBounds(CompSrtX, CompSrtY, CompSizeX, CompSizeY);		
		txtIDtoSP.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtIDtoSP);	
		txtLengthToSP.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLengthToSP.setText("0");
		txtLengthToSP.setBounds(CompSrtX, CompSrtY+CompSizeY+CompIntvY, CompSizeX, CompSizeY);		
		txtLengthToSP.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtLengthToSP);
		txtSPid.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSPid.setText("0");
		txtSPid.setBackground(Color.YELLOW);
		txtSPid.setBounds(CompSrtX, CompSrtY+2*CompSizeY+3*CompIntvY, CompSizeX, CompSizeY);
		txtSPid.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtSPid);
		txtSPlength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSPlength.setText("0");
		txtSPlength.setBackground(Color.YELLOW);
		txtSPlength.setBounds(CompSrtX, CompSrtY+3*CompSizeY+4*CompIntvY, CompSizeX, CompSizeY);	
		txtSPlength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtSPlength);
		txtHoseID.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHoseID.setText("0");
		txtHoseID.setBackground(Color.YELLOW);
		txtHoseID.setBounds(CompSrtX, CompSrtY+4*CompSizeY+6*CompIntvY, CompSizeX, CompSizeY);		
		txtHoseID.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtHoseID);
		txtHoseLength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHoseLength.setText("0");
		txtHoseLength.setBackground(Color.YELLOW);
		txtHoseLength.setBounds(CompSrtX, CompSrtY+5*CompSizeY+7*CompIntvY, CompSizeX, CompSizeY);		
		txtHoseLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtHoseLength);
		txtSwiveliD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSwiveliD.setText("0");
		txtSwiveliD.setBackground(Color.YELLOW);
		txtSwiveliD.setBounds(CompSrtX, CompSrtY+6*CompSizeY+9*CompIntvY, CompSizeX, CompSizeY);	
		txtSwiveliD.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtSwiveliD);
		txtSwivelLength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSwivelLength.setText("0");
		txtSwivelLength.setBackground(Color.YELLOW);
		txtSwivelLength.setBounds(CompSrtX, CompSrtY+7*CompSizeY+10*CompIntvY, CompSizeX, CompSizeY);	
		txtSwivelLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtSwivelLength);
		txtKellyID.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKellyID.setText("0");
		txtKellyID.setBackground(Color.YELLOW);
		txtKellyID.setBounds(CompSrtX, CompSrtY+8*CompSizeY+12*CompIntvY, CompSizeX, CompSizeY);	
		txtKellyID.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtKellyID);
		txtKellyLength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtKellyLength.setText("0");
		txtKellyLength.setBackground(Color.YELLOW);
		txtKellyLength.setBounds(CompSrtX, CompSrtY+9*CompSizeY+13*CompIntvY, CompSizeX, CompSizeY);	
		txtKellyLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtKellyLength);
		txtEquivLength.setHorizontalAlignment(SwingConstants.RIGHT);
		txtEquivLength.setText("0");
		txtEquivLength.setBackground(Color.YELLOW);
		txtEquivLength.setBounds(CompSrtX, CompSrtY+10*CompSizeY+15*CompIntvY, CompSizeX, CompSizeY);
		txtEquivLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		FrameSurfaceEq.add(txtEquivLength);

		lblDtoSP.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY, 250, 23);		
		FrameSurfaceEq.add(lblDtoSP);
		lblLengthToS.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+CompSizeY+CompIntvY, 216, CompSizeY);		
		FrameSurfaceEq.add(lblLengthToS);
		lblSPid.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+2*CompSizeY+3*CompIntvY, 150, CompSizeY);		
		FrameSurfaceEq.add(lblSPid);
		lblSPlength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+3*CompSizeY+4*CompIntvY, 150, CompSizeY);		
		FrameSurfaceEq.add(lblSPlength);
		lblHoseID.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+4*CompSizeY+6*CompIntvY, 140, CompSizeY);		
		FrameSurfaceEq.add(lblHoseID);
		lblHoseLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+5*CompSizeY+7*CompIntvY, 141, CompSizeY);		
		FrameSurfaceEq.add(lblHoseLength);
		lblSwiveliD.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+6*CompSizeY+9*CompIntvY, 195, CompSizeY);		
		FrameSurfaceEq.add(lblSwiveliD);
		lblSwivelLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+7*CompSizeY+10*CompIntvY, 200, CompSizeY);		
		FrameSurfaceEq.add(lblSwivelLength);
		lblKellyID.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+8*CompSizeY+12*CompIntvY, 90, CompSizeY);		
		FrameSurfaceEq.add(lblKellyID);
		lblKellyLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+9*CompSizeY+13*CompIntvY, 98, CompSizeY);		
		FrameSurfaceEq.add(lblKellyLength);
		lblEquivLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+10*CompSizeY+15*CompIntvY, 225, CompSizeY);		
		FrameSurfaceEq.add(lblEquivLength);		
		//

		// Type of Surface Connections
		lblTypeSurfCon.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblTypeSurfCon.setOpaque(true);
		lblTypeSurfCon.setBounds(TypeSurfConSrtX+10, TypeSurfConSrtY-7, 192, 15);		
		PnlPump.add(lblTypeSurfCon);

		pnlTypeSurfCon.setLayout(null);
		pnlTypeSurfCon.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlTypeSurfCon.setBounds(TypeSurfConSrtX, TypeSurfConSrtY, TypeSurfConSizeX, TypeSurfConSizeY);

		PnlPump.add(pnlTypeSurfCon);
		comboSurfaceConn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboSurfaceConn.getSelectedItem() == "Eqv_100 ft") MainDriver.iType = 1;
				else if(comboSurfaceConn.getSelectedItem() == "Eqv_150 ft") MainDriver.iType = 2;
				else if(comboSurfaceConn.getSelectedItem() == "Eqv_250 ft") MainDriver.iType = 3;
				else if(comboSurfaceConn.getSelectedItem() == "Eqv_650 ft") MainDriver.iType = 4;
				else MainDriver.iType = 0;
				SurfaceEquip();
			}
		});
		comboSurfaceConn.setModel(new DefaultComboBoxModel(new String[] {"Ignored", "Eqv_100 ft", "Eqv_150 ft", "Eqv_250 ft", "Eqv_650 ft"}));
		comboSurfaceConn.setBounds(CompSrtX, CompSrtY+CompIntvY, 85, 21);

		pnlTypeSurfCon.add(comboSurfaceConn);
		lblSurfWarning.setForeground(Color.BLUE);
		lblSurfWarning.setFont(new Font("±¼¸²", Font.PLAIN, 11));
		lblSurfWarning.setBounds(CompSrtX+85+2*CompIntvX, CompSrtY, 254, 28);		
		pnlTypeSurfCon.add(lblSurfWarning);
		//

		// Pump Circulation Rates
		lblPumpCirc.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblPumpCirc.setBounds(PumpCircSrtX+10, PumpCircSrtY-7, 230, 15);	
		lblPumpCirc.setOpaque(true);
		PnlPump.add(lblPumpCirc);		
		pnlPumpCirc.setLayout(null);
		pnlPumpCirc.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlPumpCirc.setBounds(PumpCircSrtX, PumpCircSrtY, PumpCircSizeX, PumpCircSizeY);		
		PnlPump.add(pnlPumpCirc);			
		lblWhileDrilling.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblWhileDrilling.setBounds(CompSrtX, CompSrtY+5, 87, 20);		
		pnlPumpCirc.add(lblWhileDrilling);
		sldPumpRate1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				MainDriver.spMinD1 = sldPumpRate1.getValue();
				txtDrillSPM1.setText((new DecimalFormat("###0")).format(MainDriver.spMinD1));
				calcRate();
			}
		});
		sldPumpRate2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				MainDriver.spMinD2 = sldPumpRate2.getValue();
				txtDrillSPM2.setText((new DecimalFormat("###0")).format(MainDriver.spMinD2));
				calcRate();
			}
		}); // added by jaewoo 20140205

		sldPumpRate1.setMajorTickSpacing(1);
		sldPumpRate1.setSnapToTicks(true);
		sldPumpRate1.setValue(60);
		sldPumpRate1.setMaximum(150);
		sldPumpRate1.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		sldPumpRate1.setBounds(CompSrtX, CompSrtY+16+5, 200, 31);
		pnlPumpCirc.add(sldPumpRate1);

		sldPumpRate2.setMajorTickSpacing(1);
		sldPumpRate2.setSnapToTicks(true);
		sldPumpRate2.setValue(0);
		sldPumpRate2.setMaximum(150);
		sldPumpRate2.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		sldPumpRate2.setBounds(CompSrtX, CompSrtY+16+5, 200, 31);
		pnlPumpCirc.add(sldPumpRate2);

		txtDrillSPM1.setBackground(Color.YELLOW);
		txtDrillSPM1.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtDrillSPM1.setBounds(CompSrtX, CompSrtY+16+31+CompIntvY, CompSizeX, CompSizeY);
		pnlPumpCirc.add(txtDrillSPM1);

		txtDrillSPM2.setBackground(Color.YELLOW);
		txtDrillSPM2.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtDrillSPM2.setBounds(CompSrtX, CompSrtY+16+31+CompIntvY, CompSizeX, CompSizeY);
		pnlPumpCirc.add(txtDrillSPM2);

		lblDrillSPM.setFont(new Font("Gulim", Font.PLAIN, 12));
		lblDrillSPM.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+16+31+CompIntvY, 144, CompSizeY);
		pnlPumpCirc.add(lblDrillSPM);
		txtDrillRate1.setHorizontalAlignment(SwingConstants.RIGHT);

		txtDrillRate1.setBackground(Color.YELLOW);
		txtDrillRate1.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtDrillRate1.setBounds(CompSrtX, CompSrtY+16+31+2*CompIntvY+CompSizeY, CompSizeX, CompSizeY);
		pnlPumpCirc.add(txtDrillRate1);	

		txtDrillRate2.setBackground(Color.YELLOW);
		txtDrillRate2.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtDrillRate2.setBounds(CompSrtX, CompSrtY+16+31+2*CompIntvY+CompSizeY, CompSizeX, CompSizeY);
		pnlPumpCirc.add(txtDrillRate2);	

		lblDrillRate.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblDrillRate.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+16+31+2*CompIntvY+CompSizeY, 89, CompSizeY);
		pnlPumpCirc.add(lblDrillRate);

		lblKillOper.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblKillOper.setBounds(CompSrtX, CompSrtY+16+31+6*CompIntvY+2*CompSizeY+5, 130, 16);		
		pnlPumpCirc.add(lblKillOper);

		sldKillPumpRate1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MainDriver.spMin1 = sldKillPumpRate1.getValue();
				txtKillSPM1.setText((new DecimalFormat("###0")).format(MainDriver.spMin1));
				calcRate();
			}
		});

		sldKillPumpRate1.setMajorTickSpacing(1);
		sldKillPumpRate1.setSnapToTicks(true);
		sldKillPumpRate1.setValue(30);
		sldKillPumpRate1.setMaximum(150);
		sldKillPumpRate1.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		sldKillPumpRate1.setBounds(CompSrtX, CompSrtY+16*2+31+6*CompIntvY+2*CompSizeY+5, 200, 31);
		pnlPumpCirc.add(sldKillPumpRate1);	

		sldKillPumpRate2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MainDriver.spMin2 = sldKillPumpRate2.getValue();
				txtKillSPM2.setText((new DecimalFormat("###0")).format(MainDriver.spMin2));
				calcRate();
			}
		});

		sldKillPumpRate2.setMajorTickSpacing(1);
		sldKillPumpRate2.setSnapToTicks(true);
		sldKillPumpRate2.setValue(0);
		sldKillPumpRate2.setMaximum(150);
		sldKillPumpRate2.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		sldKillPumpRate2.setBounds(CompSrtX, CompSrtY+16*2+31+6*CompIntvY+2*CompSizeY+5, 200, 31);
		pnlPumpCirc.add(sldKillPumpRate2);	

		txtKillSPM1.setBackground(Color.YELLOW);
		txtKillSPM1.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtKillSPM1.setBounds(CompSrtX, CompSrtY+16*2+31*2+7*CompIntvY+2*CompSizeY, CompSizeX, CompSizeY);		
		pnlPumpCirc.add(txtKillSPM1);
		txtKillSPM2.setBackground(Color.YELLOW);
		txtKillSPM2.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtKillSPM2.setBounds(CompSrtX, CompSrtY+16*2+31*2+7*CompIntvY+2*CompSizeY, CompSizeX, CompSizeY);		
		pnlPumpCirc.add(txtKillSPM2);		
		lblKillSPM.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblKillSPM.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+16*2+31*2+7*CompIntvY+2*CompSizeY, 144, CompSizeY);		
		pnlPumpCirc.add(lblKillSPM);
		txtKillRate1.setHorizontalAlignment(SwingConstants.RIGHT);

		txtKillRate1.setBackground(Color.YELLOW);
		txtKillRate1.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtKillRate1.setBounds(CompSrtX, CompSrtY+16*2+31*2+8*CompIntvY+3*CompSizeY, CompSizeX, CompSizeY);		
		pnlPumpCirc.add(txtKillRate1);
		txtKillRate2.setBackground(Color.YELLOW);
		txtKillRate2.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtKillRate2.setBounds(CompSrtX, CompSrtY+16*2+31*2+8*CompIntvY+3*CompSizeY, CompSizeX, CompSizeY);		
		pnlPumpCirc.add(txtKillRate2);
		lblKillRate.setFont(new Font("±¼¸²", Font.PLAIN, 12));
		lblKillRate.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+16*2+31*2+8*CompIntvY+3*CompSizeY, 89, CompSizeY);		
		pnlPumpCirc.add(lblKillRate);
		//

		// Pump Type		
		lblPumpType.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblPumpType.setBounds(PumpTypeSrtX+10, PumpTypeSrtY-7, 75, 15);
		lblPumpType.setOpaque(true);
		PnlPump.add(lblPumpType);

		pnlPumpType.setLayout(null);
		pnlPumpType.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlPumpType.setBounds(PumpTypeSrtX, PumpTypeSrtY, PumpTypeSizeX, PumpTypeSizeY);		
		PnlPump.add(pnlPumpType);

		/*PumpTypeGroup.add(optDuplex);
		PumpTypeGroup.add(optTriplex);
		optDuplex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iPump = 2;
				txtRodDia.setVisible(true);
				lblRodDia.setVisible(true);
				MainDriver.Drod=2.5;
				txtRodDia.setText((new DecimalFormat("#0.#")).format(MainDriver.Drod));
				calcRate();
			}
		});

		optDuplex.setBounds(CompSrtX, CompSrtY, 121, CompSizeY);		
		pnlPumpType.add(optDuplex);
		optTriplex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iPump = 3;
				MainDriver.Drod=0;
				txtRodDia.setVisible(false);
				lblRodDia.setVisible(false);
				calcRate();
			}
		});
		optTriplex.setBounds(CompSrtX, CompSrtY+CompSizeY+CompIntvY, 121, CompSizeY);		
		pnlPumpType.add(optTriplex);*/ //removed by jw, 20140205

		PumpTypeGroup.add(optPump1);
		PumpTypeGroup.add(optPump2);
		optPump1.setSelected(true);
		txtQcapacity1.setVisible(true);
		txtQcapacity2.setVisible(false);	
		txtDrillRate1.setVisible(true);
		txtKillRate1.setVisible(true);
		txtDrillRate2.setVisible(false);
		txtKillRate2.setVisible(false);
		sldPumpRate1.setVisible(true);
		sldKillPumpRate1.setVisible(true);
		sldPumpRate2.setVisible(false);
		sldKillPumpRate2.setVisible(false);
		txtDrillSPM1.setVisible(true);
		txtKillSPM1.setVisible(true);
		txtDrillSPM2.setVisible(false);
		txtKillSPM2.setVisible(false);

		optPump1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				txtQcapacity1.setVisible(true);
				txtQcapacity2.setVisible(false);	
				MainDriver.Qcapacity1 =Double.parseDouble(txtQcapacity1.getText());
				MainDriver.Qcapacity2 =Double.parseDouble(txtQcapacity2.getText()); //20140205 ajw
				txtQcapacity1.setText((new DecimalFormat("######0.###")).format(MainDriver.Qcapacity1));
				txtQcapacity2.setText((new DecimalFormat("######0.###")).format(MainDriver.Qcapacity2));
				txtDrillRate1.setVisible(true);
				txtKillRate1.setVisible(true);
				txtDrillRate2.setVisible(false);
				txtKillRate2.setVisible(false);
				sldPumpRate1.setVisible(true);
				sldKillPumpRate1.setVisible(true);
				sldPumpRate2.setVisible(false);
				sldKillPumpRate2.setVisible(false);
				txtDrillSPM1.setVisible(true);
				txtKillSPM1.setVisible(true);
				txtDrillSPM2.setVisible(false);
				txtKillSPM2.setVisible(false);
				txtDrillSPM1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
				txtKillSPM1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtDrillSPM2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
				txtKillSPM2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				calcRate();
			}
		});		
		optPump1.setBounds(CompSrtX+5, CompSrtY+5, 100, 20);		
		pnlPumpType.add(optPump1);

		optPump2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				txtQcapacity1.setVisible(false);
				txtQcapacity2.setVisible(true);	
				MainDriver.Qcapacity1 =Double.parseDouble(txtQcapacity1.getText());
				MainDriver.Qcapacity2 =Double.parseDouble(txtQcapacity2.getText()); //20140205 ajw
				txtQcapacity1.setText((new DecimalFormat("######0.###")).format(MainDriver.Qcapacity1));
				txtQcapacity2.setText((new DecimalFormat("######0.###")).format(MainDriver.Qcapacity2));
				txtDrillRate1.setVisible(false);
				txtKillRate1.setVisible(false);
				txtDrillRate2.setVisible(true);
				txtKillRate2.setVisible(true);
				sldPumpRate1.setVisible(false);
				sldKillPumpRate1.setVisible(false);
				sldPumpRate2.setVisible(true);
				sldKillPumpRate2.setVisible(true);
				txtDrillSPM1.setVisible(false);
				txtKillSPM1.setVisible(false);
				txtDrillSPM2.setVisible(true);
				txtKillSPM2.setVisible(true);
				txtDrillSPM1.setText((new DecimalFormat("##0")).format(MainDriver.spMinD1));
				txtKillSPM1.setText((new DecimalFormat("##0")).format(MainDriver.spMin1));
				txtDrillSPM2.setText((new DecimalFormat("##0")).format(MainDriver.spMinD2));
				txtKillSPM2.setText((new DecimalFormat("##0")).format(MainDriver.spMin2));
				calcRate();
			}
		});

		optPump2.setBounds(CompSrtX+5, CompSrtY+CompSizeY+5, 121, 20);		
		pnlPumpType.add(optPump2);

		txtQcapacity1.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*2+5, CompSizeX, CompSizeY);	
		txtQcapacity1.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlPumpType.add(txtQcapacity1);
		txtQcapacity1.setText("0");
		txtQcapacity2.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*2+5, CompSizeX, CompSizeY);	
		txtQcapacity2.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlPumpType.add(txtQcapacity2);
		txtQcapacity2.setText("0");

		lblQcapacity.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*2+5, 140, 23);		
		pnlPumpType.add(lblQcapacity);
		lblQcapacity.setVisible(true);
		/*txtRodDia.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*3, CompSizeX, CompSizeY);
		txtRodDia.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlPumpType.add(txtRodDia);
		txtRodDia.setText("0");*/

		/*txtStrokeLength.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*4, CompSizeX, CompSizeY);
		txtStrokeLength.setVisible(false);
		txtStrokeLength.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlPumpType.add(txtStrokeLength);
		txtStrokeLength.setText("0");
		lblStrokeLength.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*4, 120, 23);
		lblStrokeLength.setVisible(false);
		pnlPumpType.add(lblStrokeLength);
		txtPumpEff.setBounds(CompSrtX, CompSrtY+(CompSizeY+CompIntvY)*5, CompSizeX, CompSizeY);		
		txtPumpEff.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtPumpEff.setVisible(false);
		pnlPumpType.add(txtPumpEff);
		txtPumpEff.setText("0");
		lblPumpEff.setBounds(CompSrtX+CompSizeX+CompIntvX, CompSrtY+(CompSizeY+CompIntvY)*5, 160, CompSizeY);
		lblPumpEff.setVisible(false);
		pnlPumpType.add(lblPumpEff);*/
		//			
	}

	void SurfaceEquip(){
		MainModule.surfEqData();
		if(MainDriver.iType == 0){
			FrameSurfaceEq.setVisible(false);
			lblSurfConData.setVisible(false);
		}
		else{
			FrameSurfaceEq.setVisible(true);
			lblSurfConData.setVisible(true);
			txtIDtoSP.setText((new DecimalFormat("#0.###").format(MainDriver.DiaSurfLine)));
			txtLengthToSP.setText((new DecimalFormat("###0.##").format(MainDriver.LengthSurfLine)));
			txtSPid.setText((new DecimalFormat("#0.###").format(MainDriver.DiaSP)));
			txtSPlength.setText((new DecimalFormat("##0.#").format(MainDriver.LengthSP)));
			txtHoseID.setText((new DecimalFormat("#0.###").format(MainDriver.DiaHose)));
			txtHoseLength.setText((new DecimalFormat("##0.#").format(MainDriver.LengthHose)));
			txtSwiveliD.setText((new DecimalFormat("#0.###").format(MainDriver.DiaSwivel)));
			txtSwivelLength.setText((new DecimalFormat("##0.#").format(MainDriver.LengthSwivel)));
			txtKellyID.setText((new DecimalFormat("#0.###").format(MainDriver.DiaKelly))); 
			txtKellyLength.setText((new DecimalFormat("##0.#").format(MainDriver.LengthKelly)));
			txtEquivLength.setText((new DecimalFormat("##0.#").format(MainDriver.Heqval)));
		}
	}

	void calcRate(){
		//
		double qcap1=0, qcap2=0, dlin2=0, drod2=0, slen2=0, effc2=0, tempVal=0, q2=0, Qdrill2=0, Qkill2=0;
		double Qdrill1=0, Qkill1=0;
		try{
			qcap1 = Double.parseDouble(txtQcapacity1.getText());
			qcap2 = Double.parseDouble(txtQcapacity2.getText());
			/*dlin2 = Double.parseDouble(txtLinerDia.getText());
			drod2 = Double.parseDouble(txtRodDia.getText());	
			slen2 = Double.parseDouble(txtStrokeLength.getText());
			effc2 = Double.parseDouble(txtPumpEff.getText());*/
		}catch(Exception e){
			qcap1=0;
			qcap2=0;
		}
		//
		/*if(MainDriver.iPump == 2) tempVal = 2 * (2 * Math.pow(dlin2, 2) - Math.pow(drod2, 2));
		else tempVal = 3 * Math.pow(dlin2, 2);*/
		//q2 = MainDriver.C12 * tempVal * slen2 * effc2 / 12;

		Qdrill1 = qcap1 * 42 * MainDriver.spMinD1; 
		Qkill1 = qcap1 * 42 * MainDriver.spMin1;

		Qdrill2 = qcap2 * 42 * MainDriver.spMinD2; 
		Qkill2 = qcap2 * 42 * MainDriver.spMin2;

		txtDrillRate1.setText((new DecimalFormat("###0.##").format(Qdrill1)));
		txtKillRate1.setText((new DecimalFormat("###0.##").format(Qkill1)));
		txtDrillRate2.setText((new DecimalFormat("###0.##").format(Qdrill2)));
		txtKillRate2.setText((new DecimalFormat("###0.##").format(Qkill2)));
	}

	static void calcPoreFracture(){
		//.... separate pore and fracture pressures calculation sub
		//     This can be called from outside.
		//     Jan. 24, 2003

		double target_depth=0, depthTmp=0, delta_depth=0, bulk_density=0, p_overburden=0, p_seafloor=0, p_seawater=0, poisson=0;

		p_seafloor = 0.052 * MainDriver.swDensity * MainDriver.Dwater;

		MainDriver.PPdepth[0] = 0; 
		MainDriver.PoreP[0] = 0;
		MainDriver.FracP[0] = 0; //ÀÌ°Íµµ ¹è¿­ ÀÎµ¦½º ¹è´çÀÌ Çì±ò¸²...

		//   If iFG = 1 Or iFG = 2 Then    //Eaton//s method (1)
		//   ElseIf ifg = 2 Then    //John Barker//s method (2)

		target_depth = MainDriver.Vdepth;    //This is necessary for any new input file
		//
		delta_depth = (target_depth - MainDriver.Dwater) * 0.1;  //10 intervals
		depthTmp = 0.00000002;        //Jan. 19, 2003
		for(int i=0; i<11; i++){     //Assign pore & fracture pressures based on bulk density: John Barker AADE 97 paper
			bulk_density = 5.3 * Math.pow(depthTmp, 0.1356);
			MainDriver.PPdepth[i] = depthTmp;
			p_overburden = p_seafloor + 0.052 * bulk_density * depthTmp;
			MainDriver.PoreP[i] = 0.8 * p_overburden;   //80 % of overburden pressure
			MainDriver.FracP[i] = 0.9 * p_overburden;   //90 % of overburden pressure
			p_seawater = 0.052 * MainDriver.swDensity * (depthTmp + MainDriver.Dwater);
			if(MainDriver.PoreP[i]<p_seawater) MainDriver.PoreP[i]= p_seawater;
			if(MainDriver.FracP[i]<p_seawater) MainDriver.FracP[i] =  p_seawater;
			//
			if(MainDriver.iFG==1){    //Eaton//s method
				if(depthTmp < 5000) poisson = 0.3124642857 + 0.000057875 * depthTmp - 0.000000006089286 * Math.pow(depthTmp, 2);
				else poisson = 0.4260341387 + 0.0000072947129 * depthTmp - 0.0000000001882 * Math.pow(depthTmp, 2);

				if(poisson > 0.5) poisson = 0.5;
				if(poisson < 0.25) poisson = 0.25;
				//          frac_gradient = poisson / (1# - poisson) * (p_overburden - PoreP(i + 1)) / (depthTmp + Dwater) + PoreP(i + 1) / (depthTmp + Dwater)
				//          FracP(i + 1) = frac_gradient * (Dwater + depthTmp)
				MainDriver.FracP[i] =  (poisson / (1 - poisson) * (p_overburden - MainDriver.PoreP[i]) + MainDriver.PoreP[i]);
			}
			//... assign to the text boxes for PP & FP
			depthTmp = depthTmp + delta_depth;       
		}
	}

	void InstructPanelSetting(){
		int PnlResPropSrtX = 3, PnlResPropSrtY = 20, PnlResPropSizeX = 775;
		int ComponentSrtY=20, ComponentSizeX=60, ComponentSizeY = 23, ComponentIntvX = 5, ComponentIntvY = 5, ComponentSizeX2=125, ComponentSizeY2 = 16, ComponentSizeX3=70;
		int ComponentSrtX = (PnlResPropSizeX-(ComponentSizeX2+ComponentSizeX3*9+ComponentIntvX))/2;
		int PnlResPropSizeY = ComponentSrtY+ComponentSizeY+ComponentSizeY*5+ComponentSizeY2*3+ComponentIntvY*12;
		int PnlDrillingProblemSizeY = 200;
		int ComponentSrtX2 = 10;
		int ComponentSrtY2 = 20;
		int ComponentSizeX4 = 100;
		int PnlDrillingProblemSizeX = ComponentSrtX2*2+ComponentSizeX4*4;
		PnlInstruct.setLayout(null);	
		pnlBitLoc.setLayout(null);
		
		for(int i=0; i<4; i++){
			optOnG[i] = new JRadioButton("On");
			optOffG[i] = new JRadioButton("Off");
			if(i<2) txtProb[i] = new JTextField("0");
		}
		
		for(int i=0; i<5; i++){
			optLossLayer[i] = new JRadioButton(Integer.toString(i+1));
			PnlResProp.add(optLossLayer[i]);
			GroupMudLoss.add(optLossLayer[i]);
			if(MainDriver.numLayer<i+1) optLossLayer[i].setVisible(false);
			if(MainDriver.iProblem[2]==1) optLossLayer[i].setEnabled(true);
			else optLossLayer[i].setEnabled(false);
		}	
		
		optLossLayer[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.layer_mud_loss = 0;
			}});
		optLossLayer[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.layer_mud_loss = 1;
			}});
		optLossLayer[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.layer_mud_loss = 2;
			}});
		optLossLayer[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.layer_mud_loss = 3;
			}});
		optLossLayer[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.layer_mud_loss = 4;
			}});
		optLossLayer[0].setSelected(true);
		
		GroupOnOff1.add(optOnG[0]);
		GroupOnOff1.add(optOffG[0]);		
		GroupOnOff2.add(optOnG[1]);
		GroupOnOff2.add(optOffG[1]);		
		GroupOnOff3.add(optOnG[2]);
		GroupOnOff3.add(optOffG[2]);		
		GroupOnOff4.add(optOnG[3]);
		GroupOnOff4.add(optOffG[3]);
		PnlInstruct.add(lblResProp);
		PnlInstruct.add(PnlResProp);					
		PnlInstruct.add(OKButton[10]);
		PnlInstruct.add(ApplyButton[10]);
		PnlInstruct.add(lblDrillingProblem);
		PnlInstruct.add(PnlDrillingProblem);

		lblResProp.setBounds(PnlResPropSrtX+10, PnlResPropSrtY-10, 370, ComponentSizeY);
		lblResProp.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblResProp.setOpaque(true);
		PnlResProp.setBounds(PnlResPropSrtX, PnlResPropSrtY, PnlResPropSizeX, PnlResPropSizeY);
		PnlResProp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlResProp.setLayout(null);
		lblDrillingProblem.setBounds(PnlResPropSrtX+10, PnlResPropSrtY+PnlResPropSizeY+ComponentIntvY*3-10, 190, ComponentSizeY);
		lblDrillingProblem.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblDrillingProblem.setOpaque(true);
		PnlDrillingProblem.setBounds(PnlResPropSrtX, PnlResPropSrtY+PnlResPropSizeY+ComponentIntvY*3, PnlDrillingProblemSizeX, PnlDrillingProblemSizeY);
		PnlDrillingProblem.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlDrillingProblem.setLayout(null);
		sldLayer.setValue(1);		
		
		optOnG[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[0] = 1;
				txtProb[0].setEnabled(true);
				txtProb[0].setText((new DecimalFormat("0.00")).format(MainDriver.probability_problem[0]));
			}});
		
		optOnG[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[1] = 1;
				MainDriver.iProblem[3] = 1;
				txtProb[1].setEnabled(true);
				txtProb[1].setText((new DecimalFormat("0.00")).format(MainDriver.probability_problem[1]));
			}
		});
		optOnG[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[2] = 1;
				for(int i=0; i<5; i++) optLossLayer[i].setEnabled(true);
				MainDriver.Q_loss = 0; // bbl/day
				MainDriver.V_loss = 0;//bbl
				////txtprob[2].setEnabled(true);
				////txtprob[2].setText((new DecimalFormat("0.00")).format(MainDriver.probability_problem[2]));
			}
		});
		optOnG[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[3]=1;
			}
		});
		optOffG[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[0] = 0;
				txtProb[0].setEnabled(false);
				txtProb[0].setText((new DecimalFormat("0.00")).format(0));
			}
		});
		
		optOffG[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[1] = 0;
				txtProb[1].setEnabled(false);
				txtProb[1].setText((new DecimalFormat("0.00")).format(0));
			}
		});
		optOffG[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[2] = 0;
				for(int i=0; i<5; i++) optLossLayer[i].setEnabled(false);
				//txtprob[2].setEnabled(false);
				//txtprob[2].setText((new DecimalFormat("0.00")).format(0));
			}
		});
		optOffG[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iProblem[3]=0;
			}
		});
		

		comboRock[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboRock[0].getSelectedItem() == "Sandstone") MainDriver.layerRock[0] = 0;
				else if(comboRock[0].getSelectedItem() == "Shale") MainDriver.layerRock[0] = 1;
				else if(comboRock[0].getSelectedItem() == "Limestone") MainDriver.layerRock[0] = 2;
				MainDriver.layerPerm[0] = MainDriver.PermRock[MainDriver.layerRock[0]];
				MainDriver.layerPoro[0] = MainDriver.PoroRock[MainDriver.layerRock[0]];
				txtPerm_Base[0].setText((new DecimalFormat("######.00")).format(MainDriver.layerPerm[0])); //md
				txtPoro_Base[0].setText((new DecimalFormat("0.000")).format(MainDriver.layerPoro[0])); //fraction					
			}
		});
		
		comboRock[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboRock[1].getSelectedItem() == "Sandstone") MainDriver.layerRock[1] = 0;
				else if(comboRock[1].getSelectedItem() == "Shale") MainDriver.layerRock[1] = 1;
				else if(comboRock[1].getSelectedItem() == "Limestone") MainDriver.layerRock[1] = 2;
				MainDriver.layerPerm[1] = MainDriver.PermRock[MainDriver.layerRock[1]];
				MainDriver.layerPoro[1] = MainDriver.PoroRock[MainDriver.layerRock[1]];
				txtPerm_Base[1].setText((new DecimalFormat("######.00")).format(MainDriver.layerPerm[1])); //md
				txtPoro_Base[1].setText((new DecimalFormat("0.000")).format(MainDriver.layerPoro[1])); //fraction					
			}
		});
		
		comboRock[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboRock[2].getSelectedItem() == "Sandstone") MainDriver.layerRock[2] = 0;
				else if(comboRock[2].getSelectedItem() == "Shale") MainDriver.layerRock[2] = 1;
				else if(comboRock[2].getSelectedItem() == "Limestone") MainDriver.layerRock[2] = 2;
				MainDriver.layerPerm[2] = MainDriver.PermRock[MainDriver.layerRock[2]];
				MainDriver.layerPoro[2] = MainDriver.PoroRock[MainDriver.layerRock[2]];
				txtPerm_Base[2].setText((new DecimalFormat("######.00")).format(MainDriver.layerPerm[2])); //md
				txtPoro_Base[2].setText((new DecimalFormat("0.000")).format(MainDriver.layerPoro[2])); //fraction					
			}
		});
		
		comboRock[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboRock[3].getSelectedItem() == "Sandstone") MainDriver.layerRock[3] = 0;
				else if(comboRock[3].getSelectedItem() == "Shale") MainDriver.layerRock[3] = 1;
				else if(comboRock[3].getSelectedItem() == "Limestone") MainDriver.layerRock[3] = 2;
				MainDriver.layerPerm[3] = MainDriver.PermRock[MainDriver.layerRock[3]];
				MainDriver.layerPoro[3] = MainDriver.PoroRock[MainDriver.layerRock[3]];
				txtPerm_Base[3].setText((new DecimalFormat("######.00")).format(MainDriver.layerPerm[3])); //md
				txtPoro_Base[3].setText((new DecimalFormat("0.000")).format(MainDriver.layerPoro[3])); //fraction					
			}
		});
		
		comboRock[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboRock[4].getSelectedItem() == "Sandstone") MainDriver.layerRock[4] = 0;
				else if(comboRock[4].getSelectedItem() == "Shale") MainDriver.layerRock[4] = 1;
				else if(comboRock[4].getSelectedItem() == "Limestone") MainDriver.layerRock[4] = 2;
				MainDriver.layerPerm[4] = MainDriver.PermRock[MainDriver.layerRock[4]];
				MainDriver.layerPoro[4] = MainDriver.PoroRock[MainDriver.layerRock[4]];
				txtPerm_Base[4].setText((new DecimalFormat("######.00")).format(MainDriver.layerPerm[4])); //md
				txtPoro_Base[4].setText((new DecimalFormat("0.000")).format(MainDriver.layerPoro[4])); //fraction					
			}
		});

		
		sldLayer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				txtNumLayer.setText(Integer.toString(sldLayer.getValue()));
				MainDriver.numLayer = sldLayer.getValue();
				//
				for(int i = 0; i<MainDriver.numLayer; i++){
					comboRock[i].setVisible(true);	
					txtDepFrom[i].setVisible(true);	
					txtDepTo[i].setVisible(true);
					txtROP_Base[i].setVisible(true);
					txtRPM_Base[i].setVisible(true);
					txtWOB_Base[i].setVisible(true);
					txtFracP_Base[i].setVisible(true);
					txtPoreP_Base[i].setVisible(true);
					txtPerm_Base[i].setVisible(true);
					txtPoro_Base[i].setVisible(true);
					
					MainDriver.PermRock[0] = 500;
					MainDriver.PermRock[1] = 5;
					MainDriver.PermRock[2] = 500;
					MainDriver.PoroRock[0] = 0.25;
					MainDriver.PoroRock[1] = 0.25;
					MainDriver.PoroRock[2] = 0.25;
					
					// for rock composition in input data
					MainDriver.layerVDfrom[i] =(MainDriver.Vdepth-MainDriver.DepthCasing)/MainDriver.numLayer*i+MainDriver.DepthCasing;
					MainDriver.layerVDto[i] = (MainDriver.Vdepth-MainDriver.DepthCasing)/MainDriver.numLayer*(i+1)+MainDriver.DepthCasing;
					MainDriver.layerROP[i] = 60;
					MainDriver.layerRPM[i] = 60;
					MainDriver.layerWOB[i] = 40;//kips						
					MainDriver.layerRock[i] = 0; //0: sandstone, 1: shale, 2: limestone		
					MainDriver.layerPerm[i] = MainDriver.PermRock[MainDriver.layerRock[i]];
					MainDriver.layerPoro[i] = MainDriver.PoroRock[MainDriver.layerRock[i]];
					MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];
					MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[1];
					
					txtDepFrom[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerVDfrom[i]));
					txtDepTo[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerVDto[i]));
					txtFracP_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerFracP[i]));
					txtPoreP_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerPoreP[i]));
					txtPerm_Base[i].setText((new DecimalFormat("######.00")).format(MainDriver.layerPerm[i])); //md
					txtPoro_Base[i].setText((new DecimalFormat("0.000")).format(MainDriver.layerPoro[i])); //fraction					
					txtROP_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerROP[i]));
					txtRPM_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerRPM[i]));
					txtWOB_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerWOB[i])); // 1,000 lbm		
					
					if (MainDriver.layerRock[i] == 0) comboRock[i].setSelectedItem("Sandstone");
					else if (MainDriver.layerRock[i] == 1) comboSurfaceConn.setSelectedItem("Shale");
					else if (MainDriver.layerRock[i] == 2) comboSurfaceConn.setSelectedItem("Limestone");		
					
					optLossLayer[i].setVisible(true);	
				}
				if(MainDriver.layer_mud_loss>=MainDriver.numLayer){
					MainDriver.layer_mud_loss = MainDriver.numLayer-1;
					optLossLayer[MainDriver.layer_mud_loss].setSelected(true);
				}
				
				for(int i = MainDriver.numLayer; i<5; i++){
					comboRock[i].setVisible(false);	
					txtDepFrom[i].setVisible(false);	
					txtDepTo[i].setVisible(false);
					txtROP_Base[i].setVisible(false);
					txtRPM_Base[i].setVisible(false);
					txtWOB_Base[i].setVisible(false);
					txtFracP_Base[i].setVisible(false);
					txtPoreP_Base[i].setVisible(false);
					txtPerm_Base[i].setVisible(false);
					txtPoro_Base[i].setVisible(false);
					txtDepFrom[i].setText("0");
					txtDepTo[i].setText("0");
					optLossLayer[i].setVisible(false);
					
				}
			}
		});		
		

		PnlResProp.add(lblDep);
		PnlResProp.add(lblDep2);
		PnlResProp.add(lblDep3);
		PnlResProp.add(lblDep4);
		PnlResProp.add(lblDep5);
		txtNumLayer.setHorizontalAlignment(SwingConstants.CENTER);
		PnlResProp.add(txtNumLayer);
		PnlResProp.add(lblNumLayer);
		PnlResProp.add(sldLayer);
		PnlResProp.add(lblRock);
		PnlResProp.add(lblRock2);
		PnlResProp.add(lblROP0);
		PnlResProp.add(lblROP2);
		PnlResProp.add(lblROP3);
		PnlResProp.add(lblRPM);
		PnlResProp.add(lblRPM2);
		PnlResProp.add(lblRPM3);
		PnlResProp.add(lblWOB);
		PnlResProp.add(lblWOB2);
		PnlResProp.add(lblWOB3);
		PnlResProp.add(lblPorePres);
		PnlResProp.add(lblPorePres2);
		PnlResProp.add(lblPorePres3);		
		PnlResProp.add(lblFracPres);
		PnlResProp.add(lblFracPres2);
		PnlResProp.add(lblFracPres3);
		PnlResProp.add(lblPerm0);
		PnlResProp.add(lblPerm2);
		PnlResProp.add(lblPoro);
		PnlResProp.add(lblPoro2);
		PnlResProp.add(SpResProp);
		PnlResProp.add(comboRock[0]);
		PnlResProp.add(comboRock[1]);
		PnlResProp.add(comboRock[2]);
		PnlResProp.add(comboRock[3]);
		PnlResProp.add(comboRock[4]);
		PnlResProp.add(txtDepFrom[0]);
		PnlResProp.add(txtDepFrom[1]);
		PnlResProp.add(txtDepFrom[2]);
		PnlResProp.add(txtDepFrom[3]);
		PnlResProp.add(txtDepFrom[4]);
		PnlResProp.add(txtDepTo[0]);
		PnlResProp.add(txtDepTo[1]);
		PnlResProp.add(txtDepTo[2]);
		PnlResProp.add(txtDepTo[3]);
		PnlResProp.add(txtDepTo[4]);
		PnlResProp.add(txtROP_Base[0]);
		PnlResProp.add(txtROP_Base[1]);
		PnlResProp.add(txtROP_Base[2]);
		PnlResProp.add(txtROP_Base[3]);
		PnlResProp.add(txtROP_Base[4]);
		PnlResProp.add(txtRPM_Base[0]);
		PnlResProp.add(txtRPM_Base[1]);
		PnlResProp.add(txtRPM_Base[2]);
		PnlResProp.add(txtRPM_Base[3]);
		PnlResProp.add(txtRPM_Base[4]);
		PnlResProp.add(txtWOB_Base[0]);
		PnlResProp.add(txtWOB_Base[1]);
		PnlResProp.add(txtWOB_Base[2]);
		PnlResProp.add(txtWOB_Base[3]);
		PnlResProp.add(txtWOB_Base[4]);
		PnlResProp.add(txtFracP_Base[0]);
		PnlResProp.add(txtFracP_Base[1]);
		PnlResProp.add(txtFracP_Base[2]);
		PnlResProp.add(txtFracP_Base[3]);
		PnlResProp.add(txtFracP_Base[4]);
		PnlResProp.add(txtPoreP_Base[0]);
		PnlResProp.add(txtPoreP_Base[1]);
		PnlResProp.add(txtPoreP_Base[2]);
		PnlResProp.add(txtPoreP_Base[3]);
		PnlResProp.add(txtPoreP_Base[4]);
		PnlResProp.add(txtPerm_Base[0]);
		PnlResProp.add(txtPerm_Base[1]);
		PnlResProp.add(txtPerm_Base[2]);
		PnlResProp.add(txtPerm_Base[3]);
		PnlResProp.add(txtPerm_Base[4]);
		PnlResProp.add(txtPoro_Base[0]);
		PnlResProp.add(txtPoro_Base[1]);
		PnlResProp.add(txtPoro_Base[2]);
		PnlResProp.add(txtPoro_Base[3]);
		PnlResProp.add(txtPoro_Base[4]);
		PnlResProp.add(lblMudLossSelection);
		PnlResProp.add(lblMudLossSelection2);
		
		PnlDrillingProblem.add(lblProblems);
		PnlDrillingProblem.add(lblOnOff);
		PnlDrillingProblem.add(lblPossibility);
		PnlDrillingProblem.add(SpProblem);
		PnlDrillingProblem.add(lblProblem1);
		PnlDrillingProblem.add(lblProblem2);
		PnlDrillingProblem.add(lblProblem3);
		PnlDrillingProblem.add(lblProblem4);
		PnlDrillingProblem.add(lblTstep);
		PnlDrillingProblem.add(lblTstep2);
		PnlDrillingProblem.add(optOnG[0]);
		PnlDrillingProblem.add(optOffG[0]);
		PnlDrillingProblem.add(optOnG[1]);
		PnlDrillingProblem.add(optOffG[1]);
		PnlDrillingProblem.add(optOnG[2]);
		PnlDrillingProblem.add(optOffG[2]);
		PnlDrillingProblem.add(optOnG[3]);
		PnlDrillingProblem.add(optOffG[3]);
		PnlDrillingProblem.add(txtProb[0]);
		PnlDrillingProblem.add(txtProb[1]);
		//PnlDrillingProblem.add(//txtprob[2]);
		PnlDrillingProblem.add(SpProblem2);
		PnlDrillingProblem.add(txtTstep_Problem);
		
		PnlInstruct.add(lblBitLocation);
		PnlInstruct.add(pnlBitLoc);
		pnlBitLoc.add(lblBitMD);
		pnlBitLoc.add(lblBitVD);
		pnlBitLoc.add(lblBitLoc2);
		pnlBitLoc.add(lblBitLoc3);
		pnlBitLoc.add(txtBitMD);
		pnlBitLoc.add(txtBitVD);
		
		txtNumLayer.setBounds(ComponentSrtX, ComponentSrtY, ComponentSizeX, ComponentSizeY);
		txtNumLayer.setBackground(Color.YELLOW);
		sldLayer.setBounds(ComponentSrtX+ComponentSizeX+ComponentIntvX, ComponentSrtY-3, 150, ComponentSizeY+6);
		sldLayer.setMinimum(1);
		sldLayer.setMajorTickSpacing(1);
		sldLayer.setPaintTicks(true);
		sldLayer.setMaximum(5);
		lblNumLayer.setBounds(ComponentSrtX+ComponentSizeX+ComponentIntvX*2+150, ComponentSrtY, 120, ComponentSizeY);		
		lblMudLossSelection.setHorizontalAlignment(SwingConstants.CENTER);
		lblMudLossSelection2.setHorizontalAlignment(SwingConstants.CENTER);
		int dd=ComponentSrtX+ComponentSizeX+ComponentIntvX*2+330;
		lblMudLossSelection.setBounds(dd, ComponentSrtY+ComponentSizeY/2-14, 100, 14);
		lblMudLossSelection2.setBounds(dd, ComponentSrtY+ComponentSizeY/2, 100, 14);
		for(int i=0; i<5; i++){
			optLossLayer[i].setBounds(dd+100+20+40*i, ComponentSrtY, 40, ComponentSizeY);
			//opt.SETBOUNDS(LBLNUMLAYER±âÁØÀ¸·Î Âß ÇÏ°í ±× ¸Ç ¿À¸¥ÂÊ¿¡´Â µÎ ÁÙ·Î MUD LOSS / ZONE ÀÌ·¸°Ô ÇÏÀÚ)
		}
		
		lblRock.setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentIntvY*4, ComponentSizeX2, ComponentSizeY2);
		lblRock.setHorizontalAlignment(SwingConstants.CENTER);
		lblRock2.setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX2, ComponentSizeY2);
		lblRock2.setHorizontalAlignment(SwingConstants.CENTER);
		lblDep.setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentIntvY*4, ComponentSizeX3*2, ComponentSizeY2);
		lblDep.setHorizontalAlignment(SwingConstants.CENTER);
		lblDep2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblDep2.setHorizontalAlignment(SwingConstants.CENTER);
		lblDep3.setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX+ComponentSizeX3, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblDep3.setHorizontalAlignment(SwingConstants.CENTER);
		lblDep4.setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblDep4.setHorizontalAlignment(SwingConstants.CENTER);
		lblDep5.setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX+ComponentSizeX3, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblDep5.setHorizontalAlignment(SwingConstants.CENTER);
		lblROP0.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*2+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblROP0.setHorizontalAlignment(SwingConstants.CENTER);
		lblROP2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*2+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblROP2.setHorizontalAlignment(SwingConstants.CENTER);
		lblROP3.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*2+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblROP3.setHorizontalAlignment(SwingConstants.CENTER);
		lblRPM.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*3+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblRPM.setHorizontalAlignment(SwingConstants.CENTER);
		lblRPM2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*3+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblRPM2.setHorizontalAlignment(SwingConstants.CENTER);
		lblRPM3.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*3+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblRPM3.setHorizontalAlignment(SwingConstants.CENTER);
		lblWOB.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*4+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblWOB.setHorizontalAlignment(SwingConstants.CENTER);
		lblWOB2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*4+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblWOB2.setHorizontalAlignment(SwingConstants.CENTER);
		lblWOB3.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*4+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblWOB3.setHorizontalAlignment(SwingConstants.CENTER);
		lblPorePres.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*5+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblPorePres.setHorizontalAlignment(SwingConstants.CENTER);
		lblPorePres2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*5+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblPorePres2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPorePres3.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*5+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblPorePres3.setHorizontalAlignment(SwingConstants.CENTER);
		lblFracPres.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*6+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblFracPres.setHorizontalAlignment(SwingConstants.CENTER);
		lblFracPres2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*6+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblFracPres2.setHorizontalAlignment(SwingConstants.CENTER);
		lblFracPres3.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*6+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblFracPres3.setHorizontalAlignment(SwingConstants.CENTER);
		lblPerm0.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*7+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3+2, ComponentSizeY2);
		lblPerm0.setHorizontalAlignment(SwingConstants.CENTER);
		lblPerm2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*7+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblPerm2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPoro.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*8+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblPoro.setHorizontalAlignment(SwingConstants.CENTER);
		lblPoro2.setBounds(ComponentSrtX+ComponentSizeX2+ComponentSizeX3*8+ComponentIntvX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*2+ComponentIntvY*4, ComponentSizeX3, ComponentSizeY2);
		lblPoro2.setHorizontalAlignment(SwingConstants.CENTER);


		SpResProp.setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*5, ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*9, 2);
		comboRock[0].setModel(new DefaultComboBoxModel(new String[] {"Sandstone", "Shale", "Limestone"}));
		comboRock[0].setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX2, ComponentSizeY);		
		comboRock[1].setModel(new DefaultComboBoxModel(new String[] {"Sandstone", "Shale", "Limestone"}));
		comboRock[1].setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX2, ComponentSizeY);			
		comboRock[2].setModel(new DefaultComboBoxModel(new String[] {"Sandstone", "Shale", "Limestone"}));
		comboRock[2].setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX2, ComponentSizeY);			
		comboRock[3].setModel(new DefaultComboBoxModel(new String[] {"Sandstone", "Shale", "Limestone"}));
		comboRock[3].setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX2, ComponentSizeY);			
		comboRock[4].setModel(new DefaultComboBoxModel(new String[] {"Sandstone", "Shale", "Limestone"}));
		comboRock[4].setBounds(ComponentSrtX, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX2, ComponentSizeY);	

		txtDepFrom[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);	
		txtDepFrom[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepFrom[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepFrom[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepFrom[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepFrom[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepFrom[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepFrom[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepFrom[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepFrom[4].setHorizontalAlignment(SwingConstants.CENTER);

		txtDepTo[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);	
		txtDepTo[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepTo[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepTo[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepTo[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtDepTo[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepTo[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepTo[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepTo[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtDepTo[4].setHorizontalAlignment(SwingConstants.CENTER);
		
		txtROP_Base[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*2, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);	
		txtROP_Base[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*2, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtROP_Base[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*2, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtROP_Base[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*2, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtROP_Base[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*2, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtROP_Base[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtROP_Base[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtROP_Base[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtROP_Base[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtROP_Base[4].setHorizontalAlignment(SwingConstants.CENTER);

		txtRPM_Base[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*3, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);	
		txtRPM_Base[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*3, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtRPM_Base[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*3, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtRPM_Base[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*3, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtRPM_Base[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*3, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtRPM_Base[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtRPM_Base[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtRPM_Base[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtRPM_Base[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtRPM_Base[4].setHorizontalAlignment(SwingConstants.CENTER);

		txtWOB_Base[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*4, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);	
		txtWOB_Base[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*4, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtWOB_Base[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*4, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtWOB_Base[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*4, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtWOB_Base[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*4, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtWOB_Base[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtWOB_Base[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtWOB_Base[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtWOB_Base[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtWOB_Base[4].setHorizontalAlignment(SwingConstants.CENTER);

		
		txtPoreP_Base[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*5, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoreP_Base[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*5, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoreP_Base[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*5, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoreP_Base[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*5, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoreP_Base[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*5, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoreP_Base[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoreP_Base[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoreP_Base[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoreP_Base[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoreP_Base[4].setHorizontalAlignment(SwingConstants.CENTER);
		
		txtFracP_Base[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*6, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);	
		txtFracP_Base[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*6, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtFracP_Base[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*6, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtFracP_Base[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*6, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtFracP_Base[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*6, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtFracP_Base[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtFracP_Base[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtFracP_Base[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtFracP_Base[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtFracP_Base[4].setHorizontalAlignment(SwingConstants.CENTER);

		

		txtPerm_Base[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*7, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPerm_Base[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*7, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPerm_Base[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*7, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPerm_Base[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*7, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPerm_Base[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*7, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPerm_Base[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtPerm_Base[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtPerm_Base[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtPerm_Base[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtPerm_Base[4].setHorizontalAlignment(SwingConstants.CENTER);

		txtPoro_Base[0].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*8, ComponentSrtY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*6, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoro_Base[1].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*8, ComponentSrtY+ComponentSizeY+ComponentSizeY+ComponentSizeY2*3+ComponentIntvY*7, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoro_Base[2].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*8, ComponentSrtY+ComponentSizeY+ComponentSizeY*2+ComponentSizeY2*3+ComponentIntvY*8, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoro_Base[3].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*8, ComponentSrtY+ComponentSizeY+ComponentSizeY*3+ComponentSizeY2*3+ComponentIntvY*9, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoro_Base[4].setBounds(ComponentSrtX+ComponentSizeX2+ComponentIntvX*3/2+ComponentSizeX3*8, ComponentSrtY+ComponentSizeY+ComponentSizeY*4+ComponentSizeY2*3+ComponentIntvY*10, ComponentSizeX3-ComponentIntvY, ComponentSizeY);
		txtPoro_Base[0].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoro_Base[1].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoro_Base[2].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoro_Base[3].setHorizontalAlignment(SwingConstants.CENTER);
		txtPoro_Base[4].setHorizontalAlignment(SwingConstants.CENTER);

		lblProblems.setBounds(ComponentSrtX2, ComponentSrtY2, ComponentSizeX4*2, ComponentSizeY);
		lblProblems.setHorizontalAlignment(SwingConstants.CENTER);
		lblOnOff.setBounds(ComponentSrtX2+ComponentSizeX4*2, ComponentSrtY2, ComponentSizeX4, ComponentSizeY);
		lblOnOff.setHorizontalAlignment(SwingConstants.CENTER);
		lblPossibility.setBounds(ComponentSrtX2+ComponentSizeX4*3, ComponentSrtY2, ComponentSizeX4, ComponentSizeY);
		lblPossibility.setHorizontalAlignment(SwingConstants.CENTER);
		SpProblem.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY, ComponentSizeX4*4, 2);
		lblProblem1.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY+ComponentIntvX*1, ComponentSizeX4*2, ComponentSizeY);
		lblProblem1.setHorizontalAlignment(SwingConstants.CENTER);
		lblProblem2.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY*2+ComponentIntvX*1, ComponentSizeX4*2, ComponentSizeY);
		lblProblem2.setHorizontalAlignment(SwingConstants.CENTER);
		lblProblem3.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY*3+ComponentIntvX*1, ComponentSizeX4*2, ComponentSizeY);
		lblProblem3.setHorizontalAlignment(SwingConstants.CENTER);
		lblProblem4.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY*4+ComponentIntvX*1, ComponentSizeX4*2, ComponentSizeY);
		lblProblem4.setHorizontalAlignment(SwingConstants.CENTER);
		SpProblem2.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY*5+ComponentIntvX*1, ComponentSizeX4*4, 2);
		lblTstep.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY*5+ComponentIntvX*2+3, ComponentSizeX4*2, ComponentSizeY);
		lblTstep.setHorizontalAlignment(SwingConstants.CENTER);
		lblTstep2.setBounds(ComponentSrtX2, ComponentSrtY2+ComponentSizeY*6+ComponentIntvX*2-3, ComponentSizeX4*2, ComponentSizeY);
		lblTstep2.setHorizontalAlignment(SwingConstants.CENTER);
		
		optOnG[0].setBounds(ComponentSrtX2+ComponentSizeX4*2, ComponentSrtY2+ComponentSizeY+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOffG[0].setBounds(ComponentSrtX2+ComponentSizeX4*5/2, ComponentSrtY2+ComponentSizeY+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOnG[0].setHorizontalAlignment(SwingConstants.CENTER);
		optOffG[0].setHorizontalAlignment(SwingConstants.CENTER);
		optOnG[1].setBounds(ComponentSrtX2+ComponentSizeX4*2, ComponentSrtY2+ComponentSizeY*2+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOffG[1].setBounds(ComponentSrtX2+ComponentSizeX4*5/2, ComponentSrtY2+ComponentSizeY*2+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOnG[1].setHorizontalAlignment(SwingConstants.CENTER);
		optOffG[1].setHorizontalAlignment(SwingConstants.CENTER);
		optOnG[2].setBounds(ComponentSrtX2+ComponentSizeX4*2, ComponentSrtY2+ComponentSizeY*3+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOffG[2].setBounds(ComponentSrtX2+ComponentSizeX4*5/2, ComponentSrtY2+ComponentSizeY*3+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOnG[2].setHorizontalAlignment(SwingConstants.CENTER);
		optOffG[2].setHorizontalAlignment(SwingConstants.CENTER);
		optOnG[3].setBounds(ComponentSrtX2+ComponentSizeX4*2, ComponentSrtY2+ComponentSizeY*4+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOffG[3].setBounds(ComponentSrtX2+ComponentSizeX4*5/2, ComponentSrtY2+ComponentSizeY*4+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		optOnG[3].setHorizontalAlignment(SwingConstants.CENTER);
		optOffG[3].setHorizontalAlignment(SwingConstants.CENTER);
		lblOnOff.setBounds(ComponentSrtX2+ComponentSizeX4*2, ComponentSrtY2, ComponentSizeX4, ComponentSizeY);
		txtTstep_Problem.setBounds(ComponentSrtX2+ComponentSizeX4*5/2-ComponentSizeX4/4, ComponentSrtY2+ComponentSizeY*11/2+ComponentIntvX*2, ComponentSizeX4/2, ComponentSizeY);
		txtTstep_Problem.setHorizontalAlignment(SwingConstants.CENTER);
		for(int i=0; i<4; i++){
			if(MainDriver.iProblem[i] == 1){
				optOnG[i].setSelected(true);
				if(i<2){
					txtProb[i].setEnabled(true);
					txtProb[i].setText((new DecimalFormat("0.00")).format(MainDriver.probability_problem[i]));
				}
			}
			else{
				optOffG[i].setSelected(true);
				if(i<2){
					txtProb[i].setEnabled(false);
					txtProb[i].setText((new DecimalFormat("0.00")).format(0));
				}
			}
		}
		
		txtProb[0].setBounds(ComponentSrtX2+ComponentSizeX4*3+ComponentSizeX4*1/4, ComponentSrtY2+ComponentSizeY+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		txtProb[1].setBounds(ComponentSrtX2+ComponentSizeX4*3+ComponentSizeX4*1/4, ComponentSrtY2+ComponentSizeY*2+ComponentIntvX*1, ComponentSizeX4/2, ComponentSizeY);
		//txtprob[2].setBounds(ComponentSrtX2+ComponentSizeX4*3+ComponentSizeX4*1/4, ComponentSrtY2+ComponentSizeY*3+ComponentIntvX*4, ComponentSizeX4/2, ComponentSizeY);
		
		txtProb[0].setHorizontalAlignment(SwingConstants.RIGHT);
		txtProb[1].setHorizontalAlignment(SwingConstants.RIGHT);
		//txtprob[2].setHorizontalAlignment(SwingConstants.RIGHT);
		
		pnlBitLoc.setBounds(435, PnlResPropSrtY+PnlResPropSizeY+ComponentIntvY*3, 332, 128);
		lblBitLocation.setBounds(448, 291, 123, 23);
		lblBitMD.setBounds(145, 25, 140, 23);
		lblBitVD.setBounds(145, 57, 140, 23);
		lblBitLoc2.setBounds(12, 84, 146, 23);
		lblBitLoc3.setBounds(151, 84, 171, 23);
		txtBitMD.setBounds(26, 25, 107, 23);
		txtBitVD.setBounds(26, 57, 107, 23);
		
		lblBitLocation.setOpaque(true);
		lblBitLocation.setFont(new Font("±¼¸²", Font.BOLD, 12));		
		pnlBitLoc.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblBitMD.setVerticalAlignment(SwingConstants.BOTTOM);
		lblBitMD.setHorizontalAlignment(SwingConstants.LEFT);		
		lblBitVD.setHorizontalAlignment(SwingConstants.LEFT);
		lblBitLoc2.setHorizontalAlignment(SwingConstants.CENTER);
		lblBitLoc3.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblBitLoc3.setForeground(Color.RED);
		lblBitLoc3.setHorizontalAlignment(SwingConstants.CENTER);
		txtBitMD.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBitVD.setBackground(Color.YELLOW);
		txtBitVD.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtBitMD.setText((new DecimalFormat("######.0#")).format(MainDriver.TMD[MainDriver.NwcS-1]-MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600));
		txtBitVD.setText((new DecimalFormat("######.0#")).format(utilityModule.getVD(MainDriver.TMD[MainDriver.NwcS-1]-MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600)));
		
		
		OKButton[10].setBounds(400,510,100,23);
		ApplyButton[10].setBounds(510,510,100,23);
		CancelButton[10].setBounds(620,510,100,23);
	}
	
	void read_Instructor(){
		double temp_MD = 0;
		double temp_VD = 0;
		// input reading
		for(int i=0; i<2; i++) MainDriver.probability_problem[i] = Double.parseDouble(txtProb[i].getText());
		
		MainDriver.numLayer = sldLayer.getValue();
		//
		for(int i = 0; i<MainDriver.numLayer; i++){
			// for rock composition in input data
			MainDriver.layerVDfrom[i] = Double.parseDouble(txtDepFrom[i].getText());
			if(i>0 && MainDriver.layerVDfrom[i] != MainDriver.layerVDto[i-1]) MainDriver.layerVDfrom[i] = MainDriver.layerVDto[i-1];
			MainDriver.layerVDto[i] = Double.parseDouble(txtDepTo[i].getText());
			if(MainDriver.layerVDto[i] < MainDriver.layerVDfrom[i]) MainDriver.layerVDto[i] = MainDriver.layerVDfrom[i]+1;
			MainDriver.layerPoreP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[0];
			MainDriver.layerFracP[i] = propertyModule.calcPoreFrac((MainDriver.layerVDto[i]+MainDriver.layerVDfrom[i])/2)[1];
			MainDriver.layerROP[i] = Double.parseDouble(txtROP_Base[i].getText());
			MainDriver.layerRPM[i] = Double.parseDouble(txtRPM_Base[i].getText());
			MainDriver.layerWOB[i] = Double.parseDouble(txtWOB_Base[i].getText());
			MainDriver.layerPerm[i] = Double.parseDouble(txtPerm_Base[i].getText());
			MainDriver.layerPoro[i] =  Double.parseDouble(txtPoro_Base[i].getText());
			
			txtDepFrom[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerVDfrom[i]));
			txtDepTo[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerVDto[i]));
			txtFracP_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerFracP[i]));
			txtPoreP_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerPoreP[i]));
			txtPerm_Base[i].setText((new DecimalFormat("######.00")).format(MainDriver.layerPerm[i])); //md
			txtPoro_Base[i].setText((new DecimalFormat("0.000")).format(MainDriver.layerPoro[i])); //fraction					
			txtROP_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerROP[i]));
			txtRPM_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerRPM[i]));
			txtWOB_Base[i].setText((new DecimalFormat("######.0")).format(MainDriver.layerWOB[i])); // 1,000 lbm
		}
		
		temp_MD = Double.parseDouble(txtBitMD.getText());
		temp_VD = utilityModule.getVD(temp_MD);
		MainDriver.timeToTD = MainDriver.TMD[MainDriver.NwcS-1] - temp_MD;
		MainDriver.timeToTD = MainDriver.timeToTD * 3600 / (0.5* MainDriver.ROPen);
		
		txtBitVD.setText((new DecimalFormat("######.0#")).format(temp_VD));

		// ROP change
		MainDriver.Torque_Base = 70;
		propertyModule.calcBaseProp(temp_VD);
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		
		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0; 
			//MainDriver.ROPen = 13.5/0.3048*2;
			MainDriver.oMud = 13.5;
			MainDriver.oMud_save = MainDriver.oMud;			
			MainDriver.fc_Base = 0;			
			MainDriver.fc_now = 0;
			MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
			MainDriver.d_cutting = 1.0/8.0;//1/8; //in
			MainDriver.n_HC = 0.58;
			MainDriver.K_HC = 548;
			
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;
			MainDriver.numCutting = 0;
		}
		MainDriver.tstep_problem = Double.parseDouble(txtTstep_Problem.getText());
	}

	void FluidBitPanelSetting(){		
		int PnlDataTypeSrtX = 10, PnlDataTypeSrtY = 20, PnlDataTypeSizeX = 240, PnlDataTypeSizeY = 70;
		int FirstComponentSrtX = 5, FirstComponentSrtY = PnlDataTypeSrtY+PnlDataTypeSizeY+10, ComponentSizeX=70, ComponentSizeY = 23, ComponenentIntvX = 5, ComponentIntvY = 5;
		int PnlFluidDataSrtX = 20, PnlFluidDataSrtY = 30, PnlFluidDataSizeX = 300, PnlFluidDataSizeY = FirstComponentSrtY+12*ComponentSizeY+10*ComponentIntvY;		
		int pnlGasKickSizeX = 330;
		int PnlBitNozzleSrtX = PnlFluidDataSrtX+PnlFluidDataSizeX+20, PnlBitNozzleSrtY = PnlFluidDataSrtY, PnlBitNozzleSizeX = pnlGasKickSizeX, pnlBitNozzleCompSrtY = 20, PnlBitNozzleSizeY=pnlBitNozzleCompSrtY+ComponentSizeY+10;
		int pnlGasKickSrtX = PnlBitNozzleSrtX, pnlGasKickSrtY=PnlBitNozzleSrtY+PnlBitNozzleSizeY+20, pnlGasComponentSrtY=15, pnlGasKickSizeY = pnlGasComponentSrtY+3*ComponentSizeY+4*ComponentIntvY;

		// Gas Kick Data
		lblGasKick.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblGasKick.setBounds(pnlGasKickSrtX+10, pnlGasKickSrtY-7, 93, 15);	
		lblGasKick.setOpaque(true);
		PnlFluidBit.add(lblGasKick);

		PnlGasKick.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlGasKick.setBounds(pnlGasKickSrtX, pnlGasKickSrtY, pnlGasKickSizeX, pnlGasKickSizeY);		
		PnlFluidBit.setLayout(null);
		PnlFluidBit.add(PnlGasKick);
		PnlGasKick.setLayout(null);

		OKButton[4].setBounds(400,510,100,23);
		ApplyButton[4].setBounds(510,510,100,23);
		CancelButton[4].setBounds(620,510,100,23);
		PnlFluidBit.add(OKButton[4]);
		PnlFluidBit.add(ApplyButton[4]);
		//PnlFluidBit.add(CancelButton[4]);

		txtGasGravity.setBounds(FirstComponentSrtX, pnlGasComponentSrtY, ComponentSizeX, ComponentSizeY);	
		txtGasGravity.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlGasKick.add(txtGasGravity);
		txtCO2fraction.setBounds(FirstComponentSrtX, pnlGasComponentSrtY+ComponentSizeY+ComponentIntvY, ComponentSizeX, ComponentSizeY);	
		txtCO2fraction.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlGasKick.add(txtCO2fraction);
		txtH2Sfraction.setBounds(FirstComponentSrtX, pnlGasComponentSrtY+2*(ComponentSizeY+ComponentIntvY), ComponentSizeX, ComponentSizeY);
		txtH2Sfraction.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlGasKick.add(txtH2Sfraction);
		lblGasGravity.setBounds(FirstComponentSrtX+ComponentSizeX+ComponenentIntvX, pnlGasComponentSrtY, 174, ComponentSizeY);		
		PnlGasKick.add(lblGasGravity);
		lblCO2fraction.setBounds(FirstComponentSrtX+ComponentSizeX+ComponenentIntvX, pnlGasComponentSrtY+ComponentSizeY+ComponentIntvY, 209, ComponentSizeY);		
		PnlGasKick.add(lblCO2fraction);
		lblH2Sfraction.setBounds(FirstComponentSrtX+ComponentSizeX+ComponenentIntvX, pnlGasComponentSrtY+2*(ComponentSizeY+ComponentIntvY), 209, ComponentSizeY);		
		PnlGasKick.add(lblH2Sfraction);				
		//

		// But Bizzle Diameter, in/32nd		
		lblBitNozzle.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblBitNozzle.setOpaque(true);
		lblBitNozzle.setBounds(PnlBitNozzleSrtX+10, PnlBitNozzleSrtY-7, 190, 15);
		PnlFluidBit.add(lblBitNozzle);

		PnlBitNozzle.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlBitNozzle.setBounds(PnlBitNozzleSrtX, PnlBitNozzleSrtY , PnlBitNozzleSizeX, PnlBitNozzleSizeY);		
		PnlFluidBit.add(PnlBitNozzle);
		PnlBitNozzle.setLayout(null);

		txtNozzleDia0.setBounds(FirstComponentSrtX, pnlBitNozzleCompSrtY, ComponentSizeX, ComponentSizeY);	
		txtNozzleDia0.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlBitNozzle.add(txtNozzleDia0);
		txtNozzleDia1.setBounds(FirstComponentSrtX+(ComponentSizeX+ComponenentIntvX), pnlBitNozzleCompSrtY, ComponentSizeX, ComponentSizeY);	
		txtNozzleDia1.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlBitNozzle.add(txtNozzleDia1);
		txtNozzleDia2.setBounds(FirstComponentSrtX+(ComponentSizeX+ComponenentIntvX)*2, pnlBitNozzleCompSrtY, ComponentSizeX, ComponentSizeY);
		txtNozzleDia2.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlBitNozzle.add(txtNozzleDia2);
		txtNozzleDia3.setBounds(FirstComponentSrtX+(ComponentSizeX+ComponenentIntvX)*3, pnlBitNozzleCompSrtY, ComponentSizeX, ComponentSizeY);	
		txtNozzleDia3.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		PnlBitNozzle.add(txtNozzleDia3);		
		//
		// Fluid Data
		lblFluidData.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblFluidData.setOpaque(true);
		lblFluidData.setBounds(PnlFluidDataSrtX+10, PnlFluidDataSrtY-7, 66, 15);		
		PnlFluidBit.add(lblFluidData);
		PnlFluidData.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		PnlFluidData.setBounds(PnlFluidDataSrtX, PnlFluidDataSrtY, PnlFluidDataSizeX, PnlFluidDataSizeY);
		PnlFluidBit.add(PnlFluidData);	
		PnlFluidData.setLayout(null);

		// Mud Property Input Data Type		
		lblDataType.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblDataType.setBounds(PnlDataTypeSrtX+10, PnlDataTypeSrtY-7, 197, 15);
		lblDataType.setOpaque(true);		
		PnlFluidData.add(lblDataType);

		pnlDataType.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlDataType.setBounds(PnlDataTypeSrtX, PnlDataTypeSrtY, PnlDataTypeSizeX, PnlDataTypeSizeY);		
		PnlFluidData.add(pnlDataType);		
		pnlDataType.setLayout(null);
		ShearPlasticGroup.add(optShearRate);
		ShearPlasticGroup.add(optViscosity);
		optShearRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iData = 1;
				txtSS600.setText((new DecimalFormat("##0.#")).format(MainDriver.S600));
				txtSS300.setText((new DecimalFormat("##0.#")).format(MainDriver.S300));
				lblSS600.setText("Shear Stress Reading at 600 rpm");
				lblSS300.setText("Shear Stress Reading at 300 rpm");
			}
		});
		optShearRate.setBounds(FirstComponentSrtX+10, 15, 149, 20);		
		pnlDataType.add(optShearRate);
		optViscosity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iData = 2;
				pViscos = MainDriver.S600 - MainDriver.S300;
				Yp = MainDriver.S300 - pViscos;
				txtSS600.setText((new DecimalFormat("##0.#")).format(pViscos));
				txtSS300.setText((new DecimalFormat("##0.#")).format(Yp));
				lblSS600.setText("Plastic Viscosity, cp");
				lblSS300.setText("Yield Stress, lbf/100 sq. ft");
			}
		});
		optViscosity.setBounds(FirstComponentSrtX+10, 15+20, 215, 20);	 
		pnlDataType.add(optViscosity);				
		txtSS600.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSS600.setText("0");
		//		
		txtSS600.setBounds(PnlDataTypeSrtX, FirstComponentSrtY, ComponentSizeX, ComponentSizeY);		
		txtSS600.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblSS600.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY, 200, ComponentSizeY);	
		txtSS300.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSS300.setText("0");
		txtSS300.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+ComponentSizeY+ComponentIntvY, ComponentSizeX, ComponentSizeY);		
		txtSS300.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblSS300.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+ComponentSizeY+ComponentIntvY, 200, ComponentSizeY);	
		txtSS100.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+2*(ComponentSizeY+ComponentIntvY), ComponentSizeX, ComponentSizeY);	
		txtSS100.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblSS100.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+2*(ComponentSizeY+ComponentIntvY), 190, ComponentSizeY);	
		txtSS3.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+3*(ComponentSizeY+ComponentIntvY), ComponentSizeX, ComponentSizeY);		
		txtSS3.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblSS3.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+3*(ComponentSizeY+ComponentIntvY), 185, ComponentSizeY);
		txtMudDensity.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+4*(ComponentSizeY+ComponentIntvY), ComponentSizeX, ComponentSizeY);		
		txtMudDensity.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblMudDensity.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+4*(ComponentSizeY+ComponentIntvY), 123, ComponentSizeY);
		txtMudComp.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+5*(ComponentSizeY+ComponentIntvY), ComponentSizeX, ComponentSizeY);		
		txtMudComp.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblMudComp.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+5*(ComponentSizeY+ComponentIntvY), 184, ComponentSizeY);
		txtCritReyNumber.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+6*(ComponentSizeY+ComponentIntvY), ComponentSizeX, ComponentSizeY);		
		txtCritReyNumber.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblCritReyNumber.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+6*(ComponentSizeY+ComponentIntvY), 145, ComponentSizeY);
		txtRoughness.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+7*(ComponentSizeY+ComponentIntvY), ComponentSizeX, ComponentSizeY);		
		txtRoughness.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblRoughness.setBounds(85, 296, 163, 35);
		txtSurfaceTemp.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+9*ComponentSizeY+8*ComponentIntvY, ComponentSizeX, ComponentSizeY);	
		txtSurfaceTemp.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblSurfaceTemp.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+9*ComponentSizeY+8*ComponentIntvY, 138, ComponentSizeY);	
		txtTempGrad.setBounds(PnlDataTypeSrtX, FirstComponentSrtY+10*ComponentSizeY+9*ComponentIntvY, ComponentSizeX, ComponentSizeY);	
		txtTempGrad.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblTempGrad.setBounds(PnlDataTypeSrtX+ComponentSizeX+ComponenentIntvX, FirstComponentSrtY+10*ComponentSizeY+9*ComponentIntvY, 152, 35);

		PnlFluidData.add(txtSS600);
		PnlFluidData.add(txtSS300);
		PnlFluidData.add(txtSS100);
		PnlFluidData.add(txtSS3);
		PnlFluidData.add(txtMudDensity);
		PnlFluidData.add(txtMudComp);
		PnlFluidData.add(txtCritReyNumber);
		PnlFluidData.add(txtRoughness);
		PnlFluidData.add(txtSurfaceTemp);
		PnlFluidData.add(txtTempGrad);

		PnlFluidData.add(lblSS600);			
		PnlFluidData.add(lblSS300);			
		PnlFluidData.add(lblSS100);				
		PnlFluidData.add(lblSS3);				
		PnlFluidData.add(lblMudDensity);				
		PnlFluidData.add(lblMudComp);				
		PnlFluidData.add(lblCritReyNumber);				
		PnlFluidData.add(lblRoughness);			
		PnlFluidData.add(lblSurfaceTemp);				
		PnlFluidData.add(lblTempGrad);		
		//		
	}

	void MudDataPanelSetting(){
		int PnlMudOilTypeSrtX= 50, PnlMudOilTypeSrtY = 15, PnlMudOilDieselSrtX = 20, PnlMudOilDieselSrtY = 25, PnlMudOilDieselSizeY=23, PnlMudOilDieselIntvY=10;
		int PnlMudOilTypeSizeX= 226, PnlMudOilTypeSizeY = PnlMudOilDieselSrtY+4*PnlMudOilDieselSizeY+4*PnlMudOilDieselIntvY;

		int PnlMudCompSrtX= PnlMudOilTypeSrtX, PnlMudCompSrtY = PnlMudOilTypeSrtY+PnlMudOilTypeSizeY+80, TxtSrtX = 20, TxtSrtY = 20, TxtSizeX= 80, TxtSizeY=17, TxtIntvX = 10, TxtIntvY=10;
		int PnlMudCompSizeX= TxtSrtX+TxtSizeX+TxtIntvX+200, PnlMudCompSizeY = TxtSrtY+3*TxtSizeY+3*TxtIntvY;

		int BaseOilSrtX = 20, BaseOilSrtY = 15, BaseOinIntvX = 50, BaseOilIntvY = 5, BaseOilSizeX = TxtSizeX, BaseOilSizeY=TxtSizeY;	
		int PnlOilCompSrtX = 435, PnlOilCompSrtY = PnlMudOilTypeSrtY, PnlOilCompSizeX =265, PnlOilCompSizeY=BaseOilSrtY+18*BaseOilSizeY+18*BaseOilIntvY;		
		PnlMudData.setLayout(null);
		// OBM Composition
		lblMudOBMComp.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblMudOBMComp.setBounds(PnlMudCompSrtX+5, PnlMudCompSrtY-7, 116, 15);
		lblMudOBMComp.setOpaque(true);		
		PnlMudData.add(lblMudOBMComp);	
		pnlMudOBMComp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		OKButton[5].setBounds(400,510,100,23);
		ApplyButton[5].setBounds(510,510,100,23);
		CancelButton[5].setBounds(620,510,100,23);
		PnlMudData.add(OKButton[5]);
		PnlMudData.add(ApplyButton[5]);
		//PnlMudData.add(CancelButton[5]);

		pnlMudOBMComp.setBounds(PnlMudCompSrtX, PnlMudCompSrtY, PnlMudCompSizeX, PnlMudCompSizeY);		
		PnlMudData.add(pnlMudOBMComp);
		pnlMudOBMComp.setLayout(null);

		txtbaseoilf.setBounds(TxtSrtX, TxtSrtY, TxtSizeX, TxtSizeY);	
		txtbaseoilf.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlMudOBMComp.add(txtbaseoilf);
		txtbrinef.setBounds(TxtSrtX, TxtSrtY+TxtSizeY+TxtIntvY, TxtSizeX, TxtSizeY);
		txtbrinef.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlMudOBMComp.add(txtbrinef);
		txtbrinef.setBackground(Color.YELLOW);
		txtAdditivef.setBackground(Color.YELLOW);
		txtAdditivef.setBounds(TxtSrtX, TxtSrtY+2*TxtSizeY+2*TxtIntvY, TxtSizeX, TxtSizeY);	
		txtAdditivef.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pnlMudOBMComp.add(txtAdditivef);
		lblBaseOil.setBounds(TxtSrtX+TxtSizeX+TxtIntvX, TxtSrtY, 176, 15);		
		pnlMudOBMComp.add(lblBaseOil);
		lblBrine.setBounds(TxtSrtX+TxtSizeX+TxtIntvX, TxtSrtY+TxtSizeY+TxtIntvY, 176, 15);		
		pnlMudOBMComp.add(lblBrine);
		lblAdditives.setBounds(TxtSrtX+TxtSizeX+TxtIntvX, TxtSrtY+2*TxtSizeY+2*TxtIntvY, 183, 15);		
		pnlMudOBMComp.add(lblAdditives);		

		// Base Oil Type
		lblMudOilType.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblMudOilType.setOpaque(true);
		lblMudOilType.setBounds(PnlMudOilTypeSrtX+5, PnlMudOilTypeSrtY-7, 108, 15);		
		PnlMudData.add(lblMudOilType);
		PnlMudOilType.setLayout(null);
		PnlMudData.add(PnlMudOilType);		
		PnlMudOilType.setBounds(PnlMudOilTypeSrtX, PnlMudOilTypeSrtY, PnlMudOilTypeSizeX, PnlMudOilTypeSizeY);		
		PnlMudOilType.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		OilMudGroup.add(optdiesel);
		OilMudGroup.add(optmentor);
		OilMudGroup.add(optconoco);
		OilMudGroup.add(optuseroil);
		optdiesel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.ibaseoil = 0;
				OBMcomposition(); //OBM Á¶¼º ÀÔ·Â
				txtbasecom8.setEnabled(false); //hs
				txtbasecom9.setEnabled(false);
				txtbasecom10.setEnabled(false);
				txtbasecom11.setEnabled(false);
				txtbasecom12.setEnabled(false);
				txtbasecom13.setEnabled(false);
				txtbasecom14.setEnabled(false);
				txtbasecom15.setEnabled(false);
				txtbasecom16.setEnabled(false);
				txtbasecom17.setEnabled(false);
				txtbasecom18.setEnabled(false);
				txtbasecom19.setEnabled(false);
				txtbasecom20.setEnabled(false);
				txtbasecom21.setEnabled(false);
				txtbasecom22.setEnabled(false);
				txtbasecom23.setEnabled(false);
				txtbasecom24.setEnabled(false);
				txtbasecom25.setEnabled(false);
			}
		});
		optdiesel.setBounds(PnlMudOilDieselSrtX, PnlMudOilDieselSrtY, 100, PnlMudOilDieselSizeY);		
		PnlMudOilType.add(optdiesel);
		optmentor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.ibaseoil = 1;
				OBMcomposition(); //OBM Á¶¼º ÀÔ·Â
				txtbasecom8.setEnabled(false); //hs
				txtbasecom9.setEnabled(false);
				txtbasecom10.setEnabled(false);
				txtbasecom11.setEnabled(false);
				txtbasecom12.setEnabled(false);
				txtbasecom13.setEnabled(false);
				txtbasecom14.setEnabled(false);
				txtbasecom15.setEnabled(false);
				txtbasecom16.setEnabled(false);
				txtbasecom17.setEnabled(false);
				txtbasecom18.setEnabled(false);
				txtbasecom19.setEnabled(false);
				txtbasecom20.setEnabled(false);
				txtbasecom21.setEnabled(false);
				txtbasecom22.setEnabled(false);
				txtbasecom23.setEnabled(false);
				txtbasecom24.setEnabled(false);
				txtbasecom25.setEnabled(false);
				
				MainDriver.foil = Double.parseDouble(txtbaseoilf.getText()); 
				if(MainDriver.imud==1) utilityModule.OBM_Composition_SK();
				txtbaseoilf.setText((new DecimalFormat("0.000")).format(MainDriver.foil));
				txtbrinef.setText((new DecimalFormat("0.000")).format(MainDriver.fbrine));
				txtAdditivef.setText((new DecimalFormat("0.000")).format(MainDriver.fadditive));
			}
		});
		optmentor.setBounds(PnlMudOilDieselSrtX, PnlMudOilDieselSrtY+PnlMudOilDieselSizeY+PnlMudOilDieselIntvY, 90, PnlMudOilDieselSizeY);		
		PnlMudOilType.add(optmentor);
		optconoco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.ibaseoil = 2;
				OBMcomposition(); //OBM Á¶¼º ÀÔ·Â
				txtbasecom8.setEnabled(false); //hs
				txtbasecom9.setEnabled(false);
				txtbasecom10.setEnabled(false);
				txtbasecom11.setEnabled(false);
				txtbasecom12.setEnabled(false);
				txtbasecom13.setEnabled(false);
				txtbasecom14.setEnabled(false);
				txtbasecom15.setEnabled(false);
				txtbasecom16.setEnabled(false);
				txtbasecom17.setEnabled(false);
				txtbasecom18.setEnabled(false);
				txtbasecom19.setEnabled(false);
				txtbasecom20.setEnabled(false);
				txtbasecom21.setEnabled(false);
				txtbasecom22.setEnabled(false);
				txtbasecom23.setEnabled(false);
				txtbasecom24.setEnabled(false);
				txtbasecom25.setEnabled(false);
				
				MainDriver.foil = Double.parseDouble(txtbaseoilf.getText()); 
				if(MainDriver.imud==1) utilityModule.OBM_Composition_SK();
				txtbaseoilf.setText((new DecimalFormat("0.000")).format(MainDriver.foil));
				txtbrinef.setText((new DecimalFormat("0.000")).format(MainDriver.fbrine));
				txtAdditivef.setText((new DecimalFormat("0.000")).format(MainDriver.fadditive));
			}
		});
		optconoco.setBounds(PnlMudOilDieselSrtX, PnlMudOilDieselSrtY+2*PnlMudOilDieselSizeY+2*PnlMudOilDieselIntvY, 97, PnlMudOilDieselSizeY);		
		PnlMudOilType.add(optconoco);
		optuseroil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.ibaseoil = 4; //À¯Àú°¡ Á÷Á¢ Á¶¼ºÀ» °áÁ¤ÇÔ
				txtbasecom8.setEnabled(true); //hs
				txtbasecom9.setEnabled(true);
				txtbasecom10.setEnabled(true);
				txtbasecom11.setEnabled(true);
				txtbasecom12.setEnabled(true);
				txtbasecom13.setEnabled(true);
				txtbasecom14.setEnabled(true);
				txtbasecom15.setEnabled(true);
				txtbasecom16.setEnabled(true);
				txtbasecom17.setEnabled(true);
				txtbasecom18.setEnabled(true);
				txtbasecom19.setEnabled(true);
				txtbasecom20.setEnabled(true);
				txtbasecom21.setEnabled(true);
				txtbasecom22.setEnabled(true);
				txtbasecom23.setEnabled(true);
				txtbasecom24.setEnabled(true);
				txtbasecom25.setEnabled(true);
				OBMcomposition();
				
				MainDriver.foil = Double.parseDouble(txtbaseoilf.getText()); 
				if(MainDriver.imud==1) utilityModule.OBM_Composition_SK();
				txtbaseoilf.setText((new DecimalFormat("0.000")).format(MainDriver.foil));
				txtbrinef.setText((new DecimalFormat("0.000")).format(MainDriver.fbrine));
				txtAdditivef.setText((new DecimalFormat("0.000")).format(MainDriver.fadditive));
			}
		});
		optuseroil.setBounds(PnlMudOilDieselSrtX, PnlMudOilDieselSrtY+3*PnlMudOilDieselSizeY+3*PnlMudOilDieselIntvY, 97, PnlMudOilDieselSizeY);		
		PnlMudOilType.add(optuseroil);		
		//

		// Base Oil Composition
		lblMudOilComp.setBackground(new Color(240, 240, 240));		
		lblMudOilComp.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblMudOilComp.setBounds(PnlOilCompSrtX+5, PnlOilCompSrtY-7, 134, 15);
		lblMudOilComp.setOpaque(true);		
		PnlMudData.add(lblMudOilComp);
		pnlMudOilComp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlMudOilComp.setBounds(PnlOilCompSrtX, PnlOilCompSrtY, PnlOilCompSizeX, PnlOilCompSizeY);		
		PnlMudData.add(pnlMudOilComp);
		pnlMudOilComp.setLayout(null);		

		lblC8.setBounds(BaseOilSrtX, BaseOilSrtY, 15, BaseOilSizeY);
		txtbasecom8.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY, 97, BaseOilSizeY);
		txtbasecom8.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblC9.setBounds(BaseOilSrtX, BaseOilSrtY+BaseOilSizeY+BaseOilIntvY, 15, BaseOilSizeY);
		txtbasecom9.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+BaseOilSizeY+BaseOilIntvY, 97, BaseOilSizeY);
		txtbasecom9.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		lblC10.setBounds(BaseOilSrtX, BaseOilSrtY+2*BaseOilSizeY+2*BaseOilIntvY, 23, BaseOilSizeY);	
		txtbasecom10.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+2*BaseOilSizeY+2*BaseOilIntvY, 97, BaseOilSizeY);
		lblC11.setBounds(BaseOilSrtX, BaseOilSrtY+3*BaseOilSizeY+3*BaseOilIntvY, 23, BaseOilSizeY);		
		txtbasecom11.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+3*BaseOilSizeY+3*BaseOilIntvY, 97, BaseOilSizeY);
		lblC12.setBounds(BaseOilSrtX, BaseOilSrtY+4*BaseOilSizeY+4*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom12.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+4*BaseOilSizeY+4*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC13.setBounds(BaseOilSrtX, BaseOilSrtY+5*BaseOilSizeY+5*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom13.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+5*BaseOilSizeY+5*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC14.setBounds(BaseOilSrtX, BaseOilSrtY+6*BaseOilSizeY+6*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom14.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+6*BaseOilSizeY+6*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC15.setBounds(BaseOilSrtX, BaseOilSrtY+7*BaseOilSizeY+7*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom15.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+7*BaseOilSizeY+7*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC16.setBounds(BaseOilSrtX, BaseOilSrtY+8*BaseOilSizeY+8*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom16.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+8*BaseOilSizeY+8*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC17.setBounds(BaseOilSrtX, BaseOilSrtY+9*BaseOilSizeY+9*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom17.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+9*BaseOilSizeY+9*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC18.setBounds(BaseOilSrtX, BaseOilSrtY+10*BaseOilSizeY+10*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom18.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+10*BaseOilSizeY+10*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC19.setBounds(BaseOilSrtX, BaseOilSrtY+11*BaseOilSizeY+11*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom19.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+11*BaseOilSizeY+11*BaseOilIntvY, 97, BaseOilSizeY);
		lblC20.setBounds(BaseOilSrtX, BaseOilSrtY+12*BaseOilSizeY+12*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom20.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+12*BaseOilSizeY+12*BaseOilIntvY, 97, BaseOilSizeY);
		lblC21.setBounds(BaseOilSrtX, BaseOilSrtY+13*BaseOilSizeY+13*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom21.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+13*BaseOilSizeY+13*BaseOilIntvY, 97, BaseOilSizeY);
		lblC22.setBounds(BaseOilSrtX, BaseOilSrtY+14*BaseOilSizeY+14*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom22.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+14*BaseOilSizeY+14*BaseOilIntvY, 97, BaseOilSizeY);
		lblC23.setBounds(BaseOilSrtX, BaseOilSrtY+15*BaseOilSizeY+15*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom23.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+15*BaseOilSizeY+15*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC24.setBounds(BaseOilSrtX, BaseOilSrtY+16*BaseOilSizeY+16*BaseOilIntvY, 23, BaseOilSizeY);
		txtbasecom24.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+16*BaseOilSizeY+16*BaseOilIntvY, 97, BaseOilSizeY);		
		lblC25.setBounds(BaseOilSrtX, BaseOilSrtY+17*BaseOilSizeY+17*BaseOilIntvY, 30, BaseOilSizeY);
		txtbasecom25.setBounds(BaseOilSrtX+20+BaseOinIntvX, BaseOilSrtY+17*BaseOilSizeY+17*BaseOilIntvY, 97, BaseOilSizeY);		

		txtbasecom10.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom11.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom12.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom13.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom14.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom15.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom16.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom17.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom18.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom19.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom20.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom21.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom22.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom23.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom24.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		txtbasecom25.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));



		pnlMudOilComp.add(lblC8);		
		pnlMudOilComp.add(lblC9);
		pnlMudOilComp.add(lblC10);
		pnlMudOilComp.add(lblC11);
		pnlMudOilComp.add(lblC12);
		pnlMudOilComp.add(lblC13);
		pnlMudOilComp.add(lblC14);
		pnlMudOilComp.add(lblC15);
		pnlMudOilComp.add(lblC16);
		pnlMudOilComp.add(lblC17);
		pnlMudOilComp.add(lblC18);
		pnlMudOilComp.add(lblC19);
		pnlMudOilComp.add(lblC20);
		pnlMudOilComp.add(lblC21);
		pnlMudOilComp.add(lblC22);
		pnlMudOilComp.add(lblC23);
		pnlMudOilComp.add(lblC24);
		pnlMudOilComp.add(lblC25);

		pnlMudOilComp.add(txtbasecom8);	
		pnlMudOilComp.add(txtbasecom9);		
		pnlMudOilComp.add(txtbasecom10);		
		pnlMudOilComp.add(txtbasecom11);
		pnlMudOilComp.add(txtbasecom12);
		pnlMudOilComp.add(txtbasecom13);		
		pnlMudOilComp.add(txtbasecom14);		
		pnlMudOilComp.add(txtbasecom15);	
		pnlMudOilComp.add(txtbasecom16);		
		pnlMudOilComp.add(txtbasecom17);		
		pnlMudOilComp.add(txtbasecom18);		
		pnlMudOilComp.add(txtbasecom19);		
		pnlMudOilComp.add(txtbasecom20);		
		pnlMudOilComp.add(txtbasecom21);		
		pnlMudOilComp.add(txtbasecom22);	
		pnlMudOilComp.add(txtbasecom23);
		pnlMudOilComp.add(txtbasecom24);
		pnlMudOilComp.add(txtbasecom25);	
		//	
	}		

	void OptionPanelSetting(){
		int OptPnlSrtX = 20, OptPnlSrtY = 60, PnlIntvX = 30, PnlIntvY=20, OptBtnSrtX = 15, OptBtnSrtY=15, OptPnlSizeX1=200, OptPnlSizeX2=230, OptPnlSizeX3=240, OptPnlSizeY1=80, OptPnlSizeY2=90, OptPnlSizeY3=74;
		int btnSizeY= 20;
		PnlOption.setLayout(null);			

		OKButton[6].setBounds(400,510,100,23);
		ApplyButton[6].setBounds(510,510,100,23);
		CancelButton[6].setBounds(620,510,100,23);
		PnlOption.add(OKButton[6]);
		PnlOption.add(ApplyButton[6]);
		//PnlOption.add(CancelButton[6]);

		lblOptBlowout.setOpaque(true);
		lblOptBlowout.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptBlowout.setFont(new Font("±¼¸²", Font.BOLD, 12));		
		PnlOption.add(lblOptBlowout);		
		lblOptBlowout.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY+OptPnlSizeY3+PnlIntvY-7, 122, 15);		
		lblOptMultiKick.setOpaque(true);
		lblOptMultiKick.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptMultiKick.setFont(new Font("±¼¸²", Font.BOLD, 12));			
		PnlOption.add(lblOptMultiKick);		
		lblOptMultiKick.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY+OptPnlSizeY3+PnlIntvY-7, 110, 15);	
		lblOptGasDev.setOpaque(true);
		lblOptGasDev.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptGasDev.setFont(new Font("±¼¸²", Font.BOLD, 12));			
		PnlOption.add(lblOptGasDev);	
		lblOptGasDev.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY-7, 137, 15);		
		lblOptMudComp.setOpaque(true);
		lblOptMudComp.setHorizontalAlignment(SwingConstants.LEFT);
		lblOptMudComp.setFont(new Font("±¼¸²", Font.BOLD, 12));				
		PnlOption.add(lblOptMudComp);
		lblOptMudComp.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+10, OptPnlSrtY-7, 145, 30);	

		lblOptFluid.setOpaque(true);
		lblOptFluid.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptFluid.setFont(new Font("±¼¸²", Font.BOLD, 12));			
		PnlOption.add(lblOptFluid);
		lblOptFluid.setBounds(OptPnlSrtX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY-7, 77, 15);	
		//lblOptHeat.setForeground(Color.LIGHT_GRAY); //for disabled

		lblOptHeat.setOpaque(true);
		lblOptHeat.setHorizontalAlignment(SwingConstants.LEFT);
		lblOptHeat.setFont(new Font("±¼¸²", Font.BOLD, 12));				
		PnlOption.add(lblOptHeat);				
		lblOptHeat.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY-7, 89, 15);

		lblOptFric.setOpaque(true);
		lblOptFric.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptFric.setFont(new Font("±¼¸²", Font.BOLD, 12));				
		PnlOption.add(lblOptFric);		
		lblOptFric.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY-7, 150, 30);
		PnlOption.add(lblOptMethod);
		lblOptMethod.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblOptMethod.setOpaque(true);
		lblOptMethod.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptMethod.setBounds(OptPnlSrtX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY-7, 110, 15);
		lblOptMud.setForeground(Color.BLACK);
		lblOptMud.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblOptMud.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptMud.setOpaque(true);			
		PnlOption.add(lblOptMud);
		lblOptMud.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX+10, OptPnlSrtY-7, 66, 15);	
		lblOptNumWell.setForeground(Color.LIGHT_GRAY);
		lblOptNumWell.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblOptNumWell.setHorizontalAlignment(SwingConstants.LEFT);
		lblOptNumWell.setOpaque(true);				
		PnlOption.add(lblOptNumWell);		
		lblOptNumWell.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX+10, OptPnlSrtY+OptPnlSizeY1+PnlIntvY-7, 195, 15);
		lblOptWellLoc.setFont(new Font("±¼¸²", Font.BOLD, 12));
		lblOptWellLoc.setOpaque(true);		
		PnlOption.add(lblOptWellLoc);	
		lblOptWellLoc.setBounds(OptPnlSrtX+10, OptPnlSrtY-7, 88, 15);

		PnlOptWellLoc.setLayout(null);
		PnlOptWellLoc.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));		
		PnlOption.add(PnlOptWellLoc);		
		WellLocGroup.add(optOnshore);
		WellLocGroup.add(optOffshore);
		optOffshore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iOnshore = 2;
				frameOffshore.setVisible(true);
				lblOffshore.setVisible(true);
				if(MainDriver.Dwater < 1.1) MainDriver.Dwater = 1000;
			}
		});

		PnlOptWellLoc.add(optOffshore);
		optOnshore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.iOnshore = 1;
				frameOffshore.setVisible(false);
				lblOffshore.setVisible(false);
				MainDriver.Dwater = 0;
			}
		});
		PnlOptWellLoc.add(optOnshore);		

		PnlOptNumWell.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));			
		PnlOption.add(PnlOptNumWell);
		PnlOptNumWell.setLayout(null);		
		SingleMultiWellGroup.add(optERD);
		SingleMultiWellGroup.add(optML);		
		optERD.setEnabled(false);
		optERD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.igERD = 0;
				JTabInputData.setEnabledAt(12, false);
				JTabInputData.setBackgroundAt(12, Color.gray);
				lblROP.setText("Rate of Penetration (ROP), ft/hr");
				txtROP.setText((new DecimalFormat("###0.0#")).format(MainDriver.ROPen));
				FrameTrip.setVisible(false);
				lblTripData.setVisible(false);
			}
		});
		PnlOptNumWell.add(optERD);				
		optML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.igERD = 1;
				JTabInputData.setEnabledAt(10, true);
				JTabInputData.setBackgroundAt(10, new Color(240,240,240));
				Assign_Formation(); //Deactivated
			}
		});
		optML.setEnabled(false); // deactivated 20131217
		PnlOptNumWell.add(optML);

		PnlOptMud.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));			
		PnlOption.add(PnlOptMud);
		PnlOptMud.setLayout(null);
		MudTypeGroup.add(optOBM);
		MudTypeGroup.add(optWBM);
		optOBM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.imud = 1;
				JTabInputData.setEnabledAt(10, true);
				JTabInputData.setBackgroundAt(10, new Color(240,240,240));
				optwithoutmultikick.setSelected(true);
				optwithmultikick.setEnabled(false);
				optwithoutmultikick.setEnabled(false);
				MainDriver.imultikick = 0; //In OBM, only double kick is considered
				//Frame1.Enabled = True
				//Frame2.Enabled = True
				//txtbaseoilf.Enabled = True
				//txtbrinef.Enabled = True
				//txtAdditivef.Enabled = Trued				
				OBMcomposition();
				utilityModule.OBM_Composition_SK();
				Assign_mud();
			}
		});
		PnlOptMud.add(optOBM);
		optWBM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.imud = 0;

				JTabInputData.setEnabledAt(10, false);
				JTabInputData.setBackgroundAt(10, Color.gray);
				optwithmultikick.setEnabled(true);
				optwithoutmultikick.setEnabled(true);

				//dFrame(28).Enabled = True
				//Frame1.Enabled = False
				//Frame2.Enabled = False

				//txtbaseoilf.Enabled = False
				//txtbrinef.Enabled = False
				//txtAdditivef.Enabled = False
			}
		});

		PnlOptMud.add(optWBM);			
		PnlOptMethod.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));		
		PnlOption.add(PnlOptMethod);
		PnlOptMethod.setLayout(null);		
		MethodGroup.add(optDrillerMethod);		
		optDrillerMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.Method = 1;
			}
		});

		PnlOptMethod.add(optDrillerMethod);
		MethodGroup.add(optEngMethod);		
		optEngMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.Method = 2;
			}
		});
		PnlOptMethod.add(optEngMethod);		
		PnlOptMethod.add(lblOptEngMethod);

		PnlOptFric.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));				
		PnlOption.add(PnlOptFric);
		PnlOptFric.setLayout(null);		
		FrictionGroup.add(optWithDP);		
		optWithDP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iDelp = 1;
			}
		});
		PnlOptFric.add(optWithDP);
		FrictionGroup.add(optWithoutDP);		
		optWithoutDP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iDelp = 2;
			}
		});
		PnlOptFric.add(optWithoutDP);

		PnlOptHeat.setLayout(null);
		PnlOptHeat.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));		
		PnlOption.add(PnlOptHeat);		
		HeatGroup.add(optWithHT);
		HeatGroup.add(optWithoutHT);		
		optWithHT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Added by Ty
				MainDriver.iHeattrans = 1;
				JTabInputData.setEnabledAt(11, true);	
				JTabInputData.setBackgroundAt(11, new Color(240,240,240));
				//Assign_heat
			}
		});
		optWithHT.setEnabled(true);
		optWithoutHT.setEnabled(true);
		PnlOptHeat.add(optWithHT);			
		optWithoutHT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Added by Ty
				MainDriver.iHeattrans = 2;
				JTabInputData.setEnabledAt(11, false);
				JTabInputData.setBackgroundAt(11, Color.gray);
			}
		});

		PnlOptHeat.add(optWithoutHT);

		PnlOptFluid.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));				
		PnlOption.add(PnlOptFluid);
		PnlOptFluid.setLayout(null);
		FluidModelGroup.add(optAPIRP13D);
		FluidModelGroup.add(optPower);
		FluidModelGroup.add(optNewtonian);
		FluidModelGroup.add(optBingham);		
		optAPIRP13D.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.Model = 0;
				txtSS100.setEnabled(true);
				txtSS3.setEnabled(true);
				txtRoughness.setEnabled(false);
			}
		});
		PnlOptFluid.add(optAPIRP13D);				
		optPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.Model = 1;
				txtSS600.setEnabled(true);
				txtSS300.setEnabled(true);
				txtSS100.setEnabled(false);
				txtSS3.setEnabled(false);
				txtRoughness.setEnabled(false);
			}
		});		
		PnlOptFluid.add(optPower);				
		optNewtonian.addActionListener(new ActionListener() {//hs
			public void actionPerformed(ActionEvent e) {
				MainDriver.Model = 3;
				txtSS600.setEnabled(false);
				txtSS300.setEnabled(true);
				txtSS100.setEnabled(false);
				txtSS3.setEnabled(false); //hs
				txtRoughness.setEnabled(false);
			}
		});
		PnlOptFluid.add(optNewtonian);			
		optBingham.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.Model = 2;
				txtSS600.setEnabled(true); 
				txtSS300.setEnabled(true);
				txtSS100.setEnabled(false); 
				txtSS3.setEnabled(false);
				txtRoughness.setEnabled(true);
			}
		});
		PnlOptFluid.add(optBingham);

		PnlOptMudComp.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));			
		PnlOption.add(PnlOptMudComp);
		PnlOptMudComp.setLayout(null);
		MudCompGroup.add(OptWithMudComp);
		MudCompGroup.add(optWithoutMudComp);				
		OptWithMudComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iMudComp = 1;
				MainDriver.iOilComp = 1; // 150919
				txtMudComp.setEnabled(true);
			}
		});
		PnlOptMudComp.add(OptWithMudComp);		
		optWithoutMudComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iMudComp = 0;   //ignored
				MainDriver.iOilComp = 0; // 150919
				txtMudComp.setEnabled(false);
			}
		});
		PnlOptMudComp.add(optWithoutMudComp);

		PnlOptGasDev.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));			
		PnlOption.add(PnlOptGasDev);
		PnlOptGasDev.setLayout(null);		
		optRealGas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iZfact = 1;
			}
		});
		PnlOptGasDev.add(optRealGas);				
		optIdealGas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iZfact = 2;
			}
		});
		PnlOptGasDev.add(optIdealGas);		

		PnlOptMultiKick.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));			
		PnlOption.add(PnlOptMultiKick);
		PnlOptMultiKick.setLayout(null);
		MultiKickGroup.add(optwithmultikick);
		MultiKickGroup.add(optwithoutmultikick);
		GasDeviationGroup.add(optRealGas);
		GasDeviationGroup.add(optIdealGas);				
		optwithmultikick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.imultikick = 1; //consider multi-kicks during kick circulations
			}
		});
		PnlOptMultiKick.add(optwithmultikick);				
		optwithoutmultikick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Added by Ty
				MainDriver.imultikick = 0; //ignore multi-kicks during kick circulations
			}
		});
		PnlOptMultiKick.add(optwithoutmultikick);

		PnlOptBlowout.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		PnlOption.add(PnlOptBlowout);
		PnlOptBlowout.setLayout(null);
		BlououtGroup.add(optWithBlowout);
		BlououtGroup.add(optWithoutBlowout);		
		optWithBlowout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iBlowout = 1;
			}
		});
		PnlOptBlowout.add(optWithBlowout);				
		optWithoutBlowout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDriver.iBlowout = 0;
			}
		});
		PnlOptBlowout.add(optWithoutBlowout);		

		lblOptCaution.setFont(new Font("±¼¸²", Font.BOLD, 11));
		lblOptCaution.setForeground(new Color(0, 0, 255));				
		PnlOption.add(lblOptCaution);				
		PnlOptWellLoc.setBounds(OptPnlSrtX, OptPnlSrtY, OptPnlSizeX1, OptPnlSizeY1);
		optOnshore.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY-5, 80, btnSizeY);
		optOffshore.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY*2-5, 80, btnSizeY);

		PnlOptNumWell.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY, OptPnlSizeX3, OptPnlSizeY2);	
		optERD.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY, 211, btnSizeY);
		optML.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY+btnSizeY, 123, btnSizeY);

		PnlOptMud.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX, OptPnlSrtY, OptPnlSizeX3, OptPnlSizeY1);	
		optOBM.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY-5, 53, btnSizeY);
		optWBM.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY*2-5, 55, btnSizeY);

		PnlOptMethod.setBounds(OptPnlSrtX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY, OptPnlSizeX1, OptPnlSizeY2);
		optDrillerMethod.setBounds(OptBtnSrtX, OptBtnSrtY+5, 115, btnSizeY);
		optEngMethod.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY+5, 170, btnSizeY);
		optEngMethod.setOpaque(false);
		lblOptEngMethod.setBounds(35, 50+5, 160, 20);
		lblOptEngMethod.setOpaque(false);

		PnlOptFric.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY, OptPnlSizeX2, OptPnlSizeY2);
		optWithDP.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY, 77, btnSizeY);
		optWithoutDP.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY+btnSizeY, 61, btnSizeY);

		PnlOptHeat.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY, OptPnlSizeX3, OptPnlSizeY3);	
		optWithHT.setBounds(OptBtnSrtX, OptBtnSrtY+5, 77, btnSizeY);
		optWithoutHT.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY+5, 61, btnSizeY);

		PnlOptMudComp.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX, OptPnlSrtY, OptPnlSizeX2, OptPnlSizeY1);
		OptWithMudComp.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY-5, 77, btnSizeY);
		optWithoutMudComp.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY*2-5, 61, btnSizeY);		


		PnlOptFluid.setBounds(OptPnlSrtX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY, OptPnlSizeX1, OptPnlSizeY3*2+PnlIntvY);
		optAPIRP13D.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY, 95, btnSizeY);
		optPower.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY*2+5*1, 95, btnSizeY);
		optNewtonian.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY*3+5*2, 85, btnSizeY);
		optBingham.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY*4+5*3, 117, btnSizeY);		


		PnlOptGasDev.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY, OptPnlSizeX2, OptPnlSizeY3);		
		optRealGas.setBounds(OptBtnSrtX, OptBtnSrtY+5, 77, btnSizeY);		
		optIdealGas.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY+5, 61, btnSizeY);

		PnlOptMultiKick.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY+OptPnlSizeY3+PnlIntvY, OptPnlSizeX2, OptPnlSizeY3);	
		optwithmultikick.setBounds(OptBtnSrtX, OptBtnSrtY+5, 77, btnSizeY);	
		optwithoutmultikick.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY+5, 61, btnSizeY);

		PnlOptBlowout.setBounds(OptPnlSrtX+OptPnlSizeX1+PnlIntvX+OptPnlSizeX2+PnlIntvX, OptPnlSrtY+OptPnlSizeY1+PnlIntvY+OptPnlSizeY2+PnlIntvY+OptPnlSizeY3+PnlIntvY, OptPnlSizeX3, OptPnlSizeY3);
		optWithBlowout.setBounds(OptBtnSrtX, OptBtnSrtY+5, 77, btnSizeY);		
		optWithoutBlowout.setBounds(OptBtnSrtX, OptBtnSrtY+btnSizeY+5, 61, btnSizeY);

		lblOptCaution.setBounds(156, 1, 563, 32);
	}

	void Assign_mud(){
		// Added by TY
		//-------------------------------------------Mud type
		//If imud = 1 Then
		//optOBM.value = True: optWBM.value = False
		//Else
		//optWBM.value = True: optOBM.value = False
		//End If
		//-------------------------------------------Base oil type
		if(MainDriver.ibaseoil == 0){
			optdiesel.setSelected(true); 
			optmentor.setSelected(false);
			optconoco.setSelected(false);
			OBMcomposition();
		}
		else if(MainDriver.ibaseoil == 1){
			optmentor.setSelected(true);
			optdiesel.setSelected(false);
			optconoco.setSelected(false);
			OBMcomposition();
		}
		else if(MainDriver.ibaseoil == 2){
			optconoco.setSelected(true);
			optdiesel.setSelected(false);
			optmentor.setSelected(false);
			OBMcomposition();
		}
		else{
			optuseroil.setSelected(true);
		}

		//-------------------------------------------OBM fraction
		txtbaseoilf.setText((new DecimalFormat("0.000")).format(MainDriver.foil));
		txtbrinef.setText((new DecimalFormat("0.000")).format(MainDriver.fbrine));
		txtAdditivef.setText((new DecimalFormat("0.000")).format(MainDriver.fadditive));

		txtbasecom8.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[7]));	
		txtbasecom9.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[8]));		
		txtbasecom10.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[9]));		
		txtbasecom11.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[10]));
		txtbasecom12.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[11]));
		txtbasecom13.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[12]));		
		txtbasecom14.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[13]));		
		txtbasecom15.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[14]));	
		txtbasecom16.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[15]));		
		txtbasecom17.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[16]));		
		txtbasecom18.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[17]));		
		txtbasecom19.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[18]));		
		txtbasecom20.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[19]));		
		txtbasecom21.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[20]));		
		txtbasecom22.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[21]));	
		txtbasecom23.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[22]));
		txtbasecom24.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[23]));
		txtbasecom25.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[24]));

		//Mfrac_C(i + 8) = txtbasecom(i)

		txtbasecom8.setEnabled(true);	
		txtbasecom9.setEnabled(true);		
		txtbasecom10.setEnabled(true);		
		txtbasecom11.setEnabled(true);
		txtbasecom12.setEnabled(true);
		txtbasecom13.setEnabled(true);		
		txtbasecom14.setEnabled(true);		
		txtbasecom15.setEnabled(true);	
		txtbasecom16.setEnabled(true);		
		txtbasecom17.setEnabled(true);		
		txtbasecom18.setEnabled(true);		
		txtbasecom19.setEnabled(true);		
		txtbasecom20.setEnabled(true);		
		txtbasecom21.setEnabled(true);		
		txtbasecom22.setEnabled(true);	
		txtbasecom23.setEnabled(true);
		txtbasecom24.setEnabled(true);
		txtbasecom25.setEnabled(true);	
	}

	// Added by Ty
	void OBMcomposition(){

		// ºÐÀÚ º° ÀÓ°è¿Âµµ ÀÔ·Â
		MainDriver.Tc_C[0] = 343.1;
		MainDriver.Tc_C[7] = 1007.5;
		MainDriver.Tc_C[8] = 1049.9;
		MainDriver.Tc_C[9] = 1090.8;
		MainDriver.Tc_C[10] = 1125.4;
		MainDriver.Tc_C[11] = 1156.7;
		MainDriver.Tc_C[12] = 1185.7;
		MainDriver.Tc_C[13] = 1212.4;
		MainDriver.Tc_C[14] = 1237.7;
		MainDriver.Tc_C[15] = 1261.1;
		MainDriver.Tc_C[16] = 1283.2;
		MainDriver.Tc_C[17] = 1303.9;
		MainDriver.Tc_C[18] = 1323.5;
		MainDriver.Tc_C[19] = 1342.2;
		MainDriver.Tc_C[20] = 1360.7;
		MainDriver.Tc_C[21] = 1378.7;
		MainDriver.Tc_C[22] = 1395.3;
		MainDriver.Tc_C[23] = 1410.6;
		MainDriver.Tc_C[24] = 1426.5;

		// ºÐÀÚ º° ÀÓ°è¾Ð·Â ÀÔ·Â
		MainDriver.Pc_C[0] = 667.8;
		MainDriver.Pc_C[7] = 381.8;
		MainDriver.Pc_C[8] = 350.4;
		MainDriver.Pc_C[9] = 326.4;
		MainDriver.Pc_C[10] = 304.7;
		MainDriver.Pc_C[11] = 285.4;
		MainDriver.Pc_C[12] = 268.1;
		MainDriver.Pc_C[13] = 253.4;
		MainDriver.Pc_C[14] = 244.4;
		MainDriver.Pc_C[15] = 227.1;
		MainDriver.Pc_C[16] = 216.4;
		MainDriver.Pc_C[17] = 207.1;
		MainDriver.Pc_C[18] = 197.7;
		MainDriver.Pc_C[19] = 190.7;
		MainDriver.Pc_C[20] = 182.4;
		MainDriver.Pc_C[21] = 175.4;
		MainDriver.Pc_C[22] = 169;
		MainDriver.Pc_C[23] = 163;
		MainDriver.Pc_C[24] = 157;

		// ºÐÀÚ º° Acentric factor ÀÔ·Â
		MainDriver.Ac_C[0] = 0.0115;
		MainDriver.Ac_C[7] = 0.332;
		MainDriver.Ac_C[8] = 0.373;
		MainDriver.Ac_C[9] = 0.411;
		MainDriver.Ac_C[10] = 0.448;
		MainDriver.Ac_C[11] = 0.484;
		MainDriver.Ac_C[12] = 0.518;
		MainDriver.Ac_C[13] = 0.551;
		MainDriver.Ac_C[14] = 0.682;
		MainDriver.Ac_C[15] = 0.612;
		MainDriver.Ac_C[16] = 0.641;
		MainDriver.Ac_C[17] = 0.668;
		MainDriver.Ac_C[18] = 0.694;
		MainDriver.Ac_C[19] = 0.719;
		MainDriver.Ac_C[20] = 0.744;
		MainDriver.Ac_C[21] = 0.767;
		MainDriver.Ac_C[22] = 0.789;
		MainDriver.Ac_C[23] = 0.811;
		MainDriver.Ac_C[24] = 0.832;

		// ºÐÀÚ º° methane°úÀÇ binary coefficient ÀÔ·Â
		MainDriver.Bi_C[0] = 0;
		MainDriver.Bi_C[7] = 0.0381;
		MainDriver.Bi_C[8] = 0.0407;
		MainDriver.Bi_C[9] = 0.0427;
		MainDriver.Bi_C[10] = 0.0442;
		MainDriver.Bi_C[11] = 0.0458;
		MainDriver.Bi_C[12] = 0.0473;
		MainDriver.Bi_C[13] = 0.0488;
		MainDriver.Bi_C[14] = 0.0502;
		MainDriver.Bi_C[15] = 0.0512;
		MainDriver.Bi_C[16] = 0.0523;
		MainDriver.Bi_C[17] = 0.05;
		MainDriver.Bi_C[18] = 0.0537;
		MainDriver.Bi_C[19] = 0.0544;
		MainDriver.Bi_C[20] = 0.0551;
		MainDriver.Bi_C[21] = 0.0558;
		MainDriver.Bi_C[22] = 0.0565;
		MainDriver.Bi_C[23] = 0.0571;
		MainDriver.Bi_C[24] = 0.0575;

		if(MainDriver.ibaseoil == 0){
			//Mole fraction ÀÔ·Â
			MainDriver.Mfrac_C[7] = 0.22;
			MainDriver.Mfrac_C[8] = 0.88;
			MainDriver.Mfrac_C[9] = 3.79;	
			MainDriver.Mfrac_C[10] = 10.68;
			MainDriver.Mfrac_C[11] = 13.45;
			MainDriver.Mfrac_C[12] = 13.73;
			MainDriver.Mfrac_C[13] = 16.01;
			MainDriver.Mfrac_C[14] = 15.18;
			MainDriver.Mfrac_C[15] = 9.10;
			MainDriver.Mfrac_C[16] = 8.53;
			MainDriver.Mfrac_C[17] = 4.19;
			MainDriver.Mfrac_C[18] = 2.40;
			MainDriver.Mfrac_C[19] = 1.16;
			MainDriver.Mfrac_C[20] = 0.42;
			MainDriver.Mfrac_C[21] = 0.12;
			MainDriver.Mfrac_C[22] = 0.11;
			MainDriver.Mfrac_C[23] = 0.02;
			MainDriver.Mfrac_C[24] = 0.01;

			txtbasecom8.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[7]));	
			txtbasecom9.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[8]));		
			txtbasecom10.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[9]));		
			txtbasecom11.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[10]));
			txtbasecom12.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[11]));
			txtbasecom13.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[12]));		
			txtbasecom14.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[13]));		
			txtbasecom15.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[14]));	
			txtbasecom16.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[15]));		
			txtbasecom17.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[16]));		
			txtbasecom18.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[17]));		
			txtbasecom19.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[18]));		
			txtbasecom20.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[19]));		
			txtbasecom21.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[20]));		
			txtbasecom22.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[21]));	
			txtbasecom23.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[22]));
			txtbasecom24.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[23]));
			txtbasecom25.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[24]));
		}

		else if(MainDriver.ibaseoil == 1){
			//ÃÊ±âÈ­
			for(int i = 7; i<25; i++){
				MainDriver.Mfrac_C[i] = 0;
			}

			MainDriver.Mfrac_C[11] = 1.4187;
			MainDriver.Mfrac_C[12] = 2.224;
			MainDriver.Mfrac_C[13] = 6.0817;
			MainDriver.Mfrac_C[14] = 10.692;
			MainDriver.Mfrac_C[15] = 9.4953;
			MainDriver.Mfrac_C[16] = 31.837;
			MainDriver.Mfrac_C[17] = 28.187;
			MainDriver.Mfrac_C[18] = 10.0643;

			txtbasecom8.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[7]));	
			txtbasecom9.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[8]));		
			txtbasecom10.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[9]));		
			txtbasecom11.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[10]));
			txtbasecom12.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[11]));
			txtbasecom13.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[12]));		
			txtbasecom14.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[13]));		
			txtbasecom15.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[14]));	
			txtbasecom16.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[15]));		
			txtbasecom17.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[16]));		
			txtbasecom18.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[17]));		
			txtbasecom19.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[18]));		
			txtbasecom20.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[19]));		
			txtbasecom21.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[20]));		
			txtbasecom22.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[21]));	
			txtbasecom23.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[22]));
			txtbasecom24.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[23]));
			txtbasecom25.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[24]));
		}

		else if(MainDriver.ibaseoil == 2){ //hs
			//ÃÊ±âÈ­
			for(int i = 7; i<25; i++){
				MainDriver.Mfrac_C[i] = 0;
			}
			MainDriver.Mfrac_C[7] = 1.1736;
			MainDriver.Mfrac_C[8] = 11.2037;
			MainDriver.Mfrac_C[9] = 24.1092;
			MainDriver.Mfrac_C[10] = 16.5396;
			MainDriver.Mfrac_C[11] = 11.8951;
			MainDriver.Mfrac_C[12] = 17.6742;
			MainDriver.Mfrac_C[13] = 15.2455;
			MainDriver.Mfrac_C[14] = 1.4253;
			MainDriver.Mfrac_C[15] = 0.7338;

			txtbasecom8.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[7]));	
			txtbasecom9.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[8]));		
			txtbasecom10.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[9]));		
			txtbasecom11.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[10]));
			txtbasecom12.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[11]));
			txtbasecom13.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[12]));		
			txtbasecom14.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[13]));		
			txtbasecom15.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[14]));	
			txtbasecom16.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[15]));		
			txtbasecom17.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[16]));		
			txtbasecom18.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[17]));		
			txtbasecom19.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[18]));		
			txtbasecom20.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[19]));		
			txtbasecom21.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[20]));		
			txtbasecom22.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[21]));	
			txtbasecom23.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[22]));
			txtbasecom24.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[23]));
			txtbasecom25.setText((new DecimalFormat("0.####")).format(MainDriver.Mfrac_C[24]));
		}

		else{ // user input

			//for(int i = 0; i<=17; i++){	    
			//txtbasecom[i] = 0

			//Next i

		}
	}

	class PnlWellTrajt extends JPanel {

		int[] pmoveTop = new int[6];
		int[] pmoveLeft = new int[6];
		float DrawWidth = 0;

		private JPanel contentPane;	
		JPanel wtFrame0 = new JPanel();
		JPanel wtFrame1 = new JPanel();
		JPanel wtFrame2 = new JPanel();
		JLabel lblVMD = new JLabel("Vertical & Measured Depth");
		JLabel lblvd = new JLabel("ft, True Vertical Depth");
		JLabel lbltotmd = new JLabel("ft, Total Measured Depth");
		JLabel lblhorizon = new JLabel("ft, Horizontal Distance to Target Depth");
		JLabel lblFirstBuildOr = new JLabel("First Build or Hold Section");	
		JLabel lblDepthOfKickoff = new JLabel("ft, Depth of Kick-Off Point");
		JLabel lblBuilduprateDegreeFt = new JLabel("degree/100 ft, Build-Up-Rate");
		JLabel lblRadiusOfCurvature = new JLabel("ft, Radius of Curvature");
		JLabel lblAngleAtThe = new JLabel("deg., Angle at the End of First Build");	
		JLabel lblSecondBuildOr = new JLabel("Second Build or Hold Section");	
		JLabel lblsecbup = new JLabel("degree/100 ft, Build-Up-Rate");
		JLabel lblseccurv = new JLabel("ft, Radius of Curvature");
		JLabel lblsecangle = new JLabel("deg., Angle at the End of Second Build");
		JLabel lblFinalHold = new JLabel("ft, Length of Final Hold");	
		JLabel lblVerticalDepthFt = new JLabel("ft, Vertical Depth");	
		JLabel lblHorizonMouse = new JLabel("ft, Horizontal Departure");
		paintPanel pntpnl = new paintPanel();

		//int PicSizeX=2000, PicSizeY=1000;
		int PanelSizeX=FrameXsize, PanelSizeY=FrameYsize-120;//1000,1000
		//int PicSizeX=PanelSizeX*2, PicSizeY=PanelSizeY;
		int PicSizeX=PanelSizeX/2, PicSizeY=PanelSizeY;
		double scx1=0, scy1=0, scx2=PicSizeX, scy2=PicSizeY, negLeft=0, posLeft=0, tMDwell=0;
		int setScaleOn=0, intv=10;

		JTextField wttext0 = new JTextField();
		JTextField wttext1 = new JTextField();
		JTextField wttext2 = new JTextField();
		JTextField wttext3 = new JTextField();
		JTextField wttext4 = new JTextField();
		JTextField wttext5 = new JTextField();
		JTextField wttext6 = new JTextField();
		JTextField wttext7 = new JTextField();
		JTextField wttext8 = new JTextField();
		JTextField wttext9 = new JTextField();
		JTextField wttext10 = new JTextField();
		JTextField wttext11 = new JTextField();
		JTextField txtVD = new JTextField();
		JTextField txtHD = new JTextField();

		DecimalFormat format2 = new DecimalFormat(".#");

		PnlWellTrajt() {						

			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//setBounds(0, 0, PanelSizeX, PanelSizeY);	
			setLayout(null);
			contentPane = new JPanel();		
			add(contentPane);
			contentPane.setLayout(null);
			contentPane.setBounds(0, 0, PanelSizeX, PanelSizeY);

			wttext0.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext1.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext2.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext3.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext4.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext5.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext6.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext7.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext8.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext9.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext10.setHorizontalAlignment(SwingConstants.RIGHT);
			wttext11.setHorizontalAlignment(SwingConstants.RIGHT);
			txtVD.setHorizontalAlignment(SwingConstants.RIGHT);
			txtHD.setHorizontalAlignment(SwingConstants.RIGHT);

			wtFrame0.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			wtFrame0.setBounds(PanelSizeX-360, 60, 340, 99);
			contentPane.add(wtFrame0);
			wtFrame0.setLayout(null);

			lblVMD.setHorizontalAlignment(SwingConstants.CENTER);
			lblVMD.setFont(new Font("±¼¸²", Font.BOLD, 17));
			lblVMD.setBounds(0, 5, 340, 15);
			wtFrame0.add(lblVMD);

			wttext0.setBounds(12, 30, 80, 18);
			wtFrame0.add(wttext0);

			wttext1.setBounds(12, 52, 80, 18);
			wtFrame0.add(wttext1);

			wttext2.setBounds(12, 74, 80, 18);
			wtFrame0.add(wttext2);

			lblvd.setBounds(104, 30, 190, 18);
			wtFrame0.add(lblvd);

			lbltotmd.setBounds(104, 52, 190, 18);
			wtFrame0.add(lbltotmd);

			lblhorizon.setBounds(104, 74, 226, 18);
			wtFrame0.add(lblhorizon);

			wtFrame1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			wtFrame1.setBounds(PanelSizeX-360, 169, 340, 149);
			contentPane.add(wtFrame1);
			wtFrame1.setLayout(null);

			lblFirstBuildOr.setHorizontalAlignment(SwingConstants.CENTER);
			lblFirstBuildOr.setFont(new Font("±¼¸²", Font.BOLD, 17));
			lblFirstBuildOr.setBounds(0, 5, 340, 15);
			wtFrame1.add(lblFirstBuildOr);

			wttext3.setBounds(12, 30, 80, 18);
			wtFrame1.add(wttext3);

			wttext4.setBounds(12, 52, 80, 18);
			wtFrame1.add(wttext4);

			wttext5.setBounds(12, 74, 80, 18);
			wtFrame1.add(wttext5);

			wttext6.setBounds(12, 96, 80, 18);
			wtFrame1.add(wttext6);

			wtFrame1.add(wttext7);		
			wttext7.setBounds(12, 118, 80, 18);

			lblDepthOfKickoff.setBounds(104, 30, 190, 18);
			wtFrame1.add(lblDepthOfKickoff);

			lblBuilduprateDegreeFt.setBounds(104, 52, 190, 18);
			wtFrame1.add(lblBuilduprateDegreeFt);

			lblRadiusOfCurvature.setBounds(104, 74, 190, 18);
			wtFrame1.add(lblRadiusOfCurvature);

			lblAngleAtThe.setBounds(104, 96, 224, 18);
			wtFrame1.add(lblAngleAtThe);

			JLabel lblLengthOfThe = new JLabel("Length of the First Hold, ft");
			lblLengthOfThe.setBounds(104, 118, 190, 18);
			wtFrame1.add(lblLengthOfThe);

			wtFrame2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			wtFrame2.setBounds(PanelSizeX-360, 328, 340, 121);
			contentPane.add(wtFrame2);
			wtFrame2.setLayout(null);

			lblSecondBuildOr.setHorizontalAlignment(SwingConstants.CENTER);
			lblSecondBuildOr.setFont(new Font("±¼¸²", Font.BOLD, 17));
			lblSecondBuildOr.setBounds(0, 5, 340, 15);
			wtFrame2.add(lblSecondBuildOr);

			wttext8.setBounds(10, 30, 80, 18);
			wtFrame2.add(wttext8);

			wttext9.setBounds(10, 52, 80, 18);
			wtFrame2.add(wttext9);

			wttext10.setBounds(10, 74, 80, 18);
			wtFrame2.add(wttext10);

			wttext11.setBounds(10, 96, 80, 18);
			wtFrame2.add(wttext11);

			lblsecbup.setBounds(102, 30, 190, 18);
			wtFrame2.add(lblsecbup);

			lblseccurv.setBounds(102, 52, 190, 18);
			wtFrame2.add(lblseccurv);

			lblsecangle.setBounds(102, 74, 224, 18);
			wtFrame2.add(lblsecangle);

			lblFinalHold.setBounds(102, 96, 190, 18);
			wtFrame2.add(lblFinalHold);

			txtVD.setBounds(PanelSizeX-210-100, 10, 80, 18);
			contentPane.add(txtVD);

			lblVerticalDepthFt.setBounds(PanelSizeX-210, 10, 190, 18);
			contentPane.add(lblVerticalDepthFt);

			txtHD.setBounds(PanelSizeX-210-100, 32, 80, 18);
			contentPane.add(txtHD);

			lblHorizonMouse.setBounds(PanelSizeX-210, 32, 190, 18);
			contentPane.add(lblHorizonMouse);

			PicSizeX=PanelSizeX/2;
			PicSizeY=PanelSizeY-20;
			pntpnl.setBounds(0, 0, PicSizeX, PicSizeY);

			contentPane.add(pntpnl);


			pntpnl.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseMoved(MouseEvent e) {
					double x=e.getX();
					double y=e.getY();
					txtHD.setText( format2.format((x-intv/2)/(PicSizeX-intv)*(scx2-scx1)+scx1));
					txtVD.setText( format2.format((y-intv/2)/(PicSizeY-intv)*(scy2-scy1)+scy1));
				}
			});

			txtVD.setText("0.0");
			txtHD.setText("0.0"); 
			setScale();
			pntpnl.repaint();
		}		

		class paintPanel extends JPanel{

			paintPanel(){
				//this.setBackground(new Color(240,240,240));		
			}

			public void paintComponent(Graphics g){
				//Private Sub DrawWellTrajt()
				super.paintComponent(g);
				int dummy=0;
				double delTop=0, delBtm=0, delYTop=0, delYBtm=0, delleft=0, rigright=0, rigtop=0, righeit=0, rigmid=0, drawDepth=0;
				double rad1=0, rad2=0, xx1=0, xx2=0, yy1=0, yy2=0;			
				Graphics2D g2d = (Graphics2D)g;			
				setScale();				
				if(setScaleOn==1){
					DrawWidth = 1;
					g2d.setStroke(new BasicStroke(DrawWidth));
					g.drawLine (scX(negLeft), scY(MainDriver.Vdepth), scX(0.5), scY(MainDriver.Vdepth));
				}

				if (MainDriver.Vdepth > MainDriver.Hdisp){
					delTop = -MainDriver.Vdepth * 0.1;
					delBtm = MainDriver.Vdepth * (1 + 0.05);
				}
				else{
					delTop = -MainDriver.Hdisp * 0.1;
					delBtm = MainDriver.Hdisp * (1 + 0.05);
				}			

				DrawWidth = 1;
				g2d.setStroke(new BasicStroke(DrawWidth));
				delleft = delTop;
				g.drawLine(scX(3*delleft), scY(0), scX(-3*delleft), scY(0));
				if (MainDriver.iOnshore == 2) g.drawLine(scX(3*delleft), scY(MainDriver.Dwater), scX(-3*delleft), scY(MainDriver.Dwater));

				// draw the simple rig
				DrawWidth = 1.5f;
				g2d.setStroke(new BasicStroke(DrawWidth));
				rigright = delTop * 0.9; 
				righeit = 0.8 * delTop;
				rigtop = delTop * 0.18;
				rigmid = 0.72 * righeit;
				g.drawLine(scX(rigright), scY(0), scX(rigtop), scY(righeit));
				g.drawLine(scX(rigtop), scY(righeit), scX(-rigtop), scY(righeit));
				g.drawLine(scX(-rigtop), scY(righeit), scX(-rigright), scY(0));
				g.drawLine (scX(0.5 * delTop), scY(rigmid), scX(-(0.5 * delTop)), scY(rigmid));

				// draw wellbore trajectory - vertical to horizontal wells
				DrawWidth = 3;
				g2d.setStroke(new BasicStroke(DrawWidth));
				if (MainDriver.iWell == 0) g.drawLine (scX(0), scY(0), scX(0), scY(MainDriver.Vdepth));

				if (MainDriver.iWell >= 1) { // draw KOP & First Build Section
					g.drawLine (scX(0), scY(0), scX(0), scY(MainDriver.DepthKOP));
					drawDepth = MainDriver.DepthKOP;
					dummy = DrawArc(0.0, drawDepth, 0.0, MainDriver.angEOB, MainDriver.Rbur, g);
					g.drawLine(scX(0)-7, scY(drawDepth), scX(0)+7, scY(drawDepth));
				}

				if (MainDriver.iWell >= 2) {   // draw First Hold Section
					rad1 = MainDriver.angEOB * MainDriver.radConv;
					xx1 = MainDriver.Rbur * (1 - Math.cos(rad1)); 
					yy1 = MainDriver.DepthKOP + MainDriver.Rbur * Math.sin(rad1);
					xx2 = xx1 + MainDriver.xHold * Math.sin(rad1);
					yy2 = yy1 + MainDriver.xHold * Math.cos(rad1);
					g.drawLine(scX(xx1), scY(yy1), scX(xx2), scY(yy2));
				}

				if (MainDriver.iWell >= 3) dummy = DrawArc(xx2, yy2, MainDriver.angEOB, MainDriver.ang2EOB, MainDriver.R2bur, g);

				if (MainDriver.iWell == 4){
					rad2 = MainDriver.ang2EOB * MainDriver.radConv;
					xx1 = xx2 + MainDriver.R2bur * (Math.cos(rad1) - Math.cos(rad2));   //old format
					yy1 = yy2 + MainDriver.R2bur * (Math.sin(rad2) - Math.sin(rad1));
					xx2 = xx1 + MainDriver.x2Hold * Math.sin(rad2); 
					yy2 = yy1 + MainDriver.x2Hold * Math.cos(rad2);
					g.drawLine(scX(xx1), scY(yy1), scX(xx2), scY(yy2));
				}
			}

			int DrawArc(double xx, double yy, double ang1, double ang2, double rr, Graphics g){
				// draw the arc using the given starting point, angle, and radius
				//  xx- ft, starting x-position   //1/3/03- legend addition
				//  yy- ft, starting y-position   //also modified for negative BUR
				//  ang1, ang2- start and end of angle
				//  rr- ft, radius of curvature

				super.paintComponents(g);
				if(Math.abs(ang1 - ang2) < 0.08) return 0;

				double angMin = (ang2 - ang1) * 0.25;
				if(Math.abs(angMin) < 2) angMin = 2 * angMin / Math.abs(angMin);

				double xx1 = xx, xx2=0;
				double yy1 = yy, yy2=0;
				double anglei = ang1; 
				double angOld = ang1;
				anglei = anglei + angMin;
				while(Math.abs(anglei) < Math.abs(ang2)){
					xx2 = xx1 - rr * (Math.cos(anglei * MainDriver.radConv) - Math.cos(angOld * MainDriver.radConv));
					yy2 = yy1 + rr * (Math.sin(anglei * MainDriver.radConv) - Math.sin(angOld * MainDriver.radConv));
					g.drawLine(scX(xx1), scY(yy1), scX(xx2), scY(yy2));
					angOld = anglei; 
					xx1 = xx2; 
					yy1 = yy2;
					anglei = anglei + angMin;
				}
				xx2 = xx - rr * (Math.cos(ang2 * MainDriver.radConv) - Math.cos(ang1 * MainDriver.radConv));
				yy2 = yy + rr * (Math.sin(ang2 * MainDriver.radConv) - Math.sin(ang1 * MainDriver.radConv));
				g.drawLine(scX(xx1), scY(yy1), scX(xx2), scY(yy2));
				//...... assign current value for future references   //1/4/03

				xx = xx2;
				yy = yy2;
				//
				return 1;
			}
		}

		void setScale(){
			//  Jan. 3, 2003
			//...... set a scale for the form for possible ML with negative BUR
			double xLarge = Math.max(MainDriver.Vdepth, MainDriver.Hdisp);    //good foe DD & ERD
			double delTop=0, delBtm=0, delleft=0, rigright=0, rigtop=0, righeit=0, rigmid=0, drawDepth=0, MLradius=0, ML2radius=0;
			double rad1=0, rad2=0, radX=0;
			double negValue=0, posValue=0, vdValue=0, startX=0, startVD=0, xAngle=0, xHdepart=0;

			delTop = -xLarge * 0.1; 
			delBtm = xLarge * 1.05;
			//WellTrajt.Scale (delTop, delTop, delBtm, delBtm)
			scx1=delTop*3;
			scy1=delTop;
			scx2=delBtm;
			scy2=delBtm;

			if (MainDriver.igERD == 1){
				negValue = 0;
				posValue = 0;
				vdValue = MainDriver.Vdepth;	
				for(int i = 0; i < MainDriver.igMLnumber; i++){
					startX = MainDriver.mlKOP[i];
					startVD = utilityModule.getVD(startX);
					xAngle = utilityModule.getAngle(startX);
					xHdepart = utilityModule.getHorizDeparture(startX);

					radX = xAngle * MainDriver.radConv;
					rad1 = MainDriver.mlEOB[i] * MainDriver.radConv;
					rad2 = MainDriver.mlEOB2nd[i] * MainDriver.radConv;
					MLradius = 18000 / (MainDriver.pai * MainDriver.mlBUR[i]);
					ML2radius = 18000 / (MainDriver.pai * MainDriver.mlBUR2nd[i]);
					xHdepart = xHdepart + MLradius * (Math.cos(radX) - Math.cos(rad1)) + MainDriver.mlHold[i] * Math.sin(rad1);
					xHdepart = xHdepart + ML2radius * (Math.cos(rad1) - Math.cos(rad2)) +  MainDriver.mlHold2nd[i] * Math.sin(rad2);
					if (xHdepart < negValue) negValue = xHdepart;
					if (xHdepart > posValue) posValue = xHdepart;		         
					startVD = startVD + MLradius * (Math.sin(rad1) - Math.sin(radX)) +  MainDriver.mlHold[i] * Math.cos(rad1);
					startVD = startVD + ML2radius * (Math.sin(rad2) - Math.sin(rad1)) +  MainDriver.mlHold2nd[i] * Math.sin(rad2);
					if (startVD > vdValue) vdValue = startVD;
				}
				negLeft = Math.min(delTop, negValue);
				posLeft = Math.max(xLarge, posValue);
				scx1 = negLeft * 1.05;
				scy1 = -vdValue * 0.1;
				scx2 = posLeft * 1.05;
				scy2 = vdValue * 1.05;
			}			
			setScaleOn=1;
		}

		int scX (double x){
			return (int)((x-scx1)/(scx2-scx1)*(PicSizeX-intv)+intv/2);
		}


		int scY (double y){
			return (int)((y-scy1)/(scy2-scy1)*(PicSizeY-intv)+intv/2);
		}
	}
}

