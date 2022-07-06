package ML_ERD;

import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;

import javax.swing.ImageIcon;

//===========================================================
//
//  This is a conventional Well Control Simulator in Visual Basic v.6.0 ---> 6.5
//  Updated from VB v.3 to v.6.
//
//             Developed by
//
//             Dr. Jonggeun Choe
//                School of Civil and Geosystems Engineering
//                Seoul National University, Seoul 151-742, S. Korea
//                (johnchoe@snu.ac.kr)
//
//             Under supervision of
//             Dr. Hans C. Juvkam-Wold
//                Petroleum Engineering Department
//                Texas A&M University, College Station, TX 77843-3116
//
//Last Major Modifications
//     7/27/2001.
//     7/1/2002 - To major upgrade ERD, Shut in data retrieve, Random operation, etc
//                Most variables follows "long name" conventions.
//     7/1/2003 - To simulate tripping, kicking, tripping back, well kill, file I/O, etc (v. 6.3)
//==========================================================================================================================
//.............. Declare variable: nwc = 15

class MainDriver{

static final int Npt = 100000;
static final int Ntot = 500;
static final int Nwc = 12;
static final String msgTitle = "SNU/TAMU Well Control and Trip Siumlator for ERD and Multilateral Wells v. 6.5";
static String fileOut=""; 
static String fileDat ="";
static String fileWell="";
//.......................... all integer variables for the control purpose
//    Integer(%) = -32,768 - 32,767, Long(&) = -2,147,483,648 - 2,147,483,647
//    Single(!) = -3.40E-45 to 3.40E+38
static int iOnshore=0, MethodOrig=0;
static int Method=0, Np=0;
//static int iPump=0; // 20140205 ajw
static int Model=0, iDelp=0, iData=0;
static int iType=0, iWell=0, iKill=0;
static int iChoke=0, iZfact=0;
static int iSlip=0, iDC=0, N2phase=0; //N2phase: total number of 2-phase mixture slug
static int NwcS=0, NwcE=0, iHgMax=0;
static int nHgCon=0, SimRate=0;
static int iDP2p=0, iBOP=0, NpSi=0; //Npsi: Data saving index of the results"
static int iCduct=0, iSurfCsg=0;
//----------------------------------------------------------- after June 28, 2002
//.......................Additional variables for ERD and multilateral wells (ML)
static double swDensity=0;
static int iMudComp=0;
static double MudComp=0;
static double PayLength=0;
static int igERD=0, igMLnumber=0;
static int iBlowout=0;
static double[] gasflow = new double[Npt];
static double[] mudflow = new double[Npt];  //arrays for outflows
static double[] PmLine = new double[Npt];     //p. at mud line or sea floor
static Color WellColor = Color.CYAN;
static Color MudColor=Color.lightGray;
static Color KillColor=Color.darkGray;
static Color KickColor=new Color(156,93,82);
static double[] PPdepth = new double[11], PoreP = new double[11], FracP = new double[11];
static int iFGnum=0;
static double pore_Casing=0, pore_BHP=0; //여기까지 바꿈
static double SS100=0, SS3=0;   //for API RD 13D
static int[] mlPlug = new int[5];
static double[] mlKOP = new double[5];
static double[] mlBUR = new double[5], mlEOB = new double[5], mlHold = new double[5];
static double[] mlBUR2nd = new double[5], mlEOB2nd = new double[5], mlHold2nd = new double[5];
static double[] mlDia = new double[5], mlDiaMD = new double[5], mlDia2nd = new double[5];
static double[] mlPform = new double[5], mlPerm = new double[5], mlPorosity = new double[5];
static double[] mlSkin = new double[5], mlHeff = new double[5];
static double[] mlKOPang = new double[5], mlKOPvd = new double[5]; //2003/7/16  for ML kicks
static double[] mlTVD = new double[5], mlMD = new double[5], mlTsecGas = new double[5];
static double[] mlPbeff = new double[5], mlCti = new double[5];
static double[] mlBg = new double[5], mlQgTotM = new double[5];
static double[] mlPkop = new double[5];    //psia, p. at the KOP of ML
static int[] iMLflow = new int[5];  //flow indicator: with flow(1), without flow(0)
//................................................. for Tripping
static int igJointNumber=0;
static double gJointLength=0;
static double gTripTankVolume=0, gTripTankHeight=0;
static double gConnTime=0;
static double gBleedTime=0, gBleedPressure=0;
//----------------------------------------------------------------------------------------
//.................  input data with well geometry data
static double Tsurf=0, Tgrad=0, gasGravity=0;
static double Dchoke=0, Dkill=0, Dwater=0, Heqval=0;
static double fCO2=0, fH2S=0, tWgrad=0;
static double DepthKOP=0, BUR=0, BUR2=0, angEOB=0;
static double ang2EOB=0, Vdepth=0, Hdisp=0;
static double DepthCasing=0, IDcasing=0, DiaHole=0, doDP=0, diDP=0;
static double doDC=0, diDC=0, LengthDC=0;
static double LengthHWDP=0, doHWDP=0, diHWDP=0;
static double xHold=0, x2Hold=0;
static double ODcduct=0, DepthCduct=0, ODsurfCsg=0, DepthSurfCsg=0;
static double delX=0, SIDPP=0, SICP=0, Vpiti=0, Pcon=0, Rbur=0, R2bur=0; //나중에 다시 체크할 것
static double DiaSP=0, LengthSP=0, DiaHose=0, LengthHose=0, DiaSwivel=0, LengthSwivel=0, DiaKelly=0, LengthKelly=0;
static double DiaSurfLine=0, LengthSurfLine=0, Driser=0; //As Single
//..................... pump data and basic calculations
//static double DiaLiner=0, Drod=0, strokeLength=0, Effcy=0;
//static double spMinD=0, spMin=0; //removed by jw, 20140205
static double spMinD1=0, spMin1=0, spMinD2=0, spMin2=0;
static double RenC=0, Ruf=0;
static double[] Dnoz = new double[4];
static double Qdrill=0, Qkill=0, QtMix=0, pN=0, pK=0;   //power-law parameters
static double initQkill = 0; //20140225 ajw
//static double Qcapacity=0;//removed by jw
static double S600=0, S300=0, pai=0, C12=0, Pb=0;
static double gMudOld=0, gMudKill=0, gMudCirc=0, Kmud=0, Cmud=0, oMud=0;
static double VOLinn=0, VOLout=0, VOLkick=0, radConv=0, Xb=0, DPtop=0, DPbot=0; //As Single
//........................... variables for two-phase calculations
static double Pform=0, visL=0, gTcum=0, overP=0, Vsound=0; //As Single
static double gDelT=0, Qinflux=0, HgMax=0;   //As Single
//........................... the array for output
static double[] xTop = new double[Npt], xBot = new double[Npt], Ppump = new double[Npt], rhoK = new double[Npt];
static double[] Psp = new double[Npt], VOLcir = new double[Npt], pxTop = new double[Npt], Pcsg = new double[Npt], Pchk = new double[Npt];
static double[] Vpit = new double[Npt], TTsec = new double[Npt], Pb2p = new double[Npt], QmcfDay = new double[Npt], CHKopen = new double[Npt];
static int[] Stroke = new int[Npt], StrokePump1 = new int[Npt], StrokePump2 = new int[Npt];

//..........................  the array for two-phase calculation
static double[] TMD = new double[Nwc], TVD = new double[Nwc], Do2p = new double[Nwc], Di2p = new double[Nwc];  //A2p(Nwc), as single
static double[] DiDS = new double[Nwc], ang2p = new double[Nwc], volDS = new double[Nwc], VOLann = new double[Nwc];      //as single
static double[] OBMAnnDensity_Drilling = new double[Nwc]; //20140317 ajw
static double[] OBMPipeDensity_Drilling = new double[Nwc]; //20140317 ajw
//
static double[] volL = new double[Ntot], volG = new double[Ntot], Pnd = new double[Ntot], Xnd = new double[Ntot], Hgnd = new double[Ntot];//volL : Volume of Liquid
static double[] gor = new double[Ntot], volGold = new double[Ntot]; // Modified by TY
static double[] freegasmole = new double[Ntot], gasmole = new double[Ntot], solgasmole = new double[Ntot], OBMmole = new double[Ntot];// Modified by TY
static double[] pvtZb = new double[Ntot], HgndOld = new double[Ntot]; //Hg = Gas Fraction
//................... set other control parameters to control Visual Basic Program and Options
static int iHresv=0, iHWDP=0;
static int iCsg=0, iWshow=0, iKsheet=0;
static int iCHKcontrol=0;   //choke control method: auto(1), manual(2)
static int iDatPrn=0;       //to know save or save as
static double pitWarn=0, Perm=0, Porosity=0, Skins=0;
static double ROPen=0, DchkControl=0;
static double VDeob=0, HDeob=0, Cti=0, TbRankin=0, gasVisi=0, KICKintens=0, volMix=0;
static int heightX=0, leftX=0, topX=0, widthX=0;     //to save wellbore pic sizes before unloading
//................... for the color control
static Color formColor = new Color(102,0,0), Blue = Color.blue, Yellow=Color.YELLOW, White=Color.WHITE, Black=Color.BLACK, Gray=Color.gray;
//................... for fracture gradient calculation and underground blowout detection
static int iFG=0;
static double timeCsg=0, timeBHP=0, PfgCsg=0, PfgBHP=0, Hdrled=0, fvfGas=0;
//................... to plot user's choke control results as pump pressure
static int iDone=0;
static int[] vKmudSt = new int[181];
static double[] Pcontrol = new double[181];
static int screnHt=0, screnWd=0, screnMgin=0;
//..................Variables of Main Module In VB .........................
static int Ncount=0, Nstart=0;
static double Xstart=0;
static int gPayLength=0;

//Added by TY
//new variables for OBM calculation
static int imud=0, ibaseoil=0, iHeattrans=0, imultikick=0;
static double OBMdensity=0, OBMwt=0;
static double V_cont=0, V_cont_ref=0, volkx=0;
static double foil=0, fbrine=0, fadditive=0;
static int imode=0; //Simulation mode 1:Manual, 2:Automatic
static double R=10.73, ContV=0, moleOBM=0;
static double Rsk=0, RsM=0, Rsn=0, Rs=0;
static double[] Tc_C= new double[Ntot], Pc_C = new double[Ntot], Ac_C = new double[Ntot], Bi_C = new double[Ntot], Mfrac_C = new double[Ntot], MW_C = new double[Ntot];
static double[] OBMFrac = new double[Ntot], GasFrac = new double[Ntot];
static double[] aTc = new double[Ntot], aT = new double[Ntot], b = new double[Ntot], fibi = new double[Ntot];
static double[][] Aij = new double[25][25], fifj = new double[25][25];
static double OBMFr=0, GasFr=0, mole_solgas=0, mole_OBM=0, contdensity=0;
static double[] Vsol = new double[Ntot];
static double gortemp=0;
static double Pmis=0;
static double tmp_solgas=0, tmp_freegas=0, tmp_gas=0, tmp_T=0;
static double[] PVTZ_Gas = new double[Ntot], PVTZ_sol = new double[Ntot], PVTZ_free = new double[Ntot], Vdiff_old = new double[Ntot], delta_T = new double[Ntot];
static double Vdiff_l=0, qgtbbl_gas=0;
//static Rs_old = new double[Ntot]
static double Den_ref=0, Den_tmp=0;
static double[] Dendiff = new double[Ntot];
static double delta_rho=0;
static double[] newfreegasmole = new double[Ntot], Kicknd = new double[Ntot];
static double Rs_old=0;
static double Qtmix1=0, Qtmix2=0;
static double eqA=0, eqB=0, eqC=0, eqD=0, EOSZ1=0, EOSZ2=0;
static double aaa=0, bbb=0, ccc=0, ddd=0, n=0, xold=0, xnew=0, realcuberoot=0, iter=0, aa=0, bb=0, real=0, disc=0, fff=0, df=0;

//..................... Heat transfer variables
static double TconF=0, SheatF=0, HtrancF=0;
static double TconM=0, SheatM=0, HtrancM=0, InjmudT=0;
static double TD=0, td2=0; //Dimensionless temperature
static double Error=0;
static double tx=0;
static double delta_Vdiff=0, pumping_default=0;
static double Mw_total=0, mole_mixture=0;
static double VolL_default=0, qLinc=0;

//.................. Added by JW 2013 10 25
static String operMsg =" ";
static int BlowOutTimerOn=0;

//
static Image img;
static MediaTracker tr;
static URL base;

static double Qcapacity1=0, Qcapacity2=0; //20140205 ajw for a 2-pump operation.
static double volPump=0, volPump2=0, volPumpKill=0;
static double reset_Stroke1 = 0;
static double reset_Stroke2 = 0;

static int NumQcapeq = 0;
static double[] KillVolChange = new double[1000];
static double[] StrokeChange = new double[1000];
static double[] QKillChange = new double[1000];
static double[] Qcapeq = new double[1000];
static double initspMin1=0,initspMin2=0; 

static int MainMenuOn=0; //0: off 1: on
static int MenuSelected=0; // 0: to the main menu, 1: to the simulation panel, 2: Not selected yet.
static int BlowOutOccurred=0; //0: not occurred 1: occurred
static int BlowOutPnlVisible=0;
static int RPMenuSelected = 0; // 0: to the main menu, 1: to the simulation panel, 2: Not selected yet.
static int drillSimOn=0; // 0: drillSim closed 1: drillsim opened 
static int AutoOn=0; // 0: drillSim closed 1: drillsim opened 
static int ManualOn=0; // 0: drillSim closed 1: drillsim opened 
static int inputOn=0; // 0: input closed, 1: input opened
static int MainMenuVisible=0; //0: setting invisible , 1: setting visible.
static int SItaskOn2=0; // 0: Drilling 1: Shut-in to give information to wellpic.
static double xVDupSI=0;
static ImageIcon icon;

static double pumpDensity=0;
static double test_ICP = 0; 
static double test_FCP = 0;
static double test_KMW = 0;
static double test_ICP_Theory = 0;
static double test_FCP_Theory = 0;
static double test_KMW_Theory = 0;
static double temp_ICP=0;
static double temp_FCP=0;
static double temp_KMW=0;

static int KMWsettingOn = 0; // 0: KMW setting이 안됨.

static int iCaseSelection = 0; //140617 AJW, indicating whether a program is progressed through case selection. 0: not selecting a case / 1: selecting a case
static int iCase = 0; //140617 AJW, indicating which case is selected.
static double Slow_PumpP=0;

static int FrmAnalysisOn = 0; //0: close, 1: open
static int plot=0;

static int PointRef=0;
static int PointRefPrior=0;
static int MinusPoint=0;
static double set0_Starttime = 0;
static double set0_Finishtime = 0;

static double iKickDetect=0;
static double kickTimeStart = 0;
static double kickTimeFinish = 0;

//-----------------------------For Standing-Katz
static double OBMDensity_sc = 0;
static double oilDensity_sc = 0;
static double BariteDensity = 35.1526;
static double BrineDensity = 10.829;
static double vOil = 298;//gal
static double vBrine = 52;
static double vBarite = 0;
static double vBarite2 = 0;
static double foil_kill=0, fbrine_kill=0, fadditive_kill=0;
static double[] C_density = new double[100];
static double[] Vfree = new double[Ntot];

static int mud_calc = 0; //0: preos , 1:standing katz
static int iOilComp = 0; //0: 변화고려 x, 1: 변화고려 o
static double[] Den_Pipe = new double[Nwc];
static double[] Den_Ann = new double[Nwc];
static double[] Mfrac_C_oil = new double[Ntot];

static double[] TMD_tmp = new double[Nwc];
static double[] TVD_tmp = new double[Nwc];

//20140912
static int[] iProblem = new int[4]; //2: mud loss, 3: cuttings accumulation
static int[] iProblem_occured = new int[4];
static int iSelectCorrect = 1; // 0: correct 1: False
static int[] iSolutionCorrect = new int[3]; // 0: correct 1: False
static double[] Do2p_tmp = new double[Nwc];
static double[] Di2p_tmp = new double[Nwc];
static double[] probability_problem = new double[2];
static double[] t_problem = new double[2];//sec
static double tstep_problem = 10;//sec
static int layer_mud_loss = 0;

//20150123
static double Torque_Base = 7000; // lbf*ft
static double Torque_now = Torque_Base; // lbf*ft
static int RPM_Base = 1;
static int RPM_now = 1;
static double ROP_Base = 60;
static double ROP_now = 1;
static double PoreP_Base = 1;
static double PoreP_now = 1;
static double FracP_Base = 1;
static double FracP_now = 1;
static double CF_Base = 0.45;
static double CF_now = CF_Base;
static double Q_base = 337.088811730091;
static double WOB_Base = 1;
static double WOB_now = 1;
static double fc_Base = 0.1;
static double fc_now = 1;
static double dens_cutting = 1;
static double BHP_Base = 1;
static double timeToTD = 10*60;//30*60;  //sec, total time to drill to TD //later = 300 sec (5min) = 3600 * mdbitoff / (.5 * ropen)
static double mdBitOff = 0;
static double oMud_save;
static double oMud_avg;
static double d_cutting = 1; //inch
static int iHuschel = 0; // Nagasawa 검증용
static double fc_tot = 1; //inch

static double[] V_ann_cutting = new double[Nwc];
static double V_ann_cutting_tot = 0;
static double[] TMD_cutting = new double[Nwc];
static double[] TMD_cutting_old = new double[Nwc];
static double[] TVD_cutting = new double[Nwc];
static double[] V_cutting = new double[Nwc];
static double[] V_cutting_old = new double[Nwc];
static double V_cutting_tot = 0;
static double[] Area_ann = new double[Nwc];
static double[] bhp_ex = new double[1000];
static double[] time_ex = new double[1000];
static double[] fx_ex = new double[1000];
static double[] den_ex = new double[1000];
static double[] ROP_ex = new double[1000];
static double[] depth_ex = new double[1000];
static double[] pore_ex = new double[1000];
static double[] frac_ex = new double[1000];

static int i_ex=0;
static int iStone = 1; // 0: sandstone, 1: shale, 2: limestone
static double n_HC = 1;
static double K_HC = 1;
static double vdbit_now = 0; 
static double i_ROPVERSION = 2; //1: CONSTANT, 2: 바뀜


static int sizeResult = 3000;
static double[] V_cell_cut= new double[sizeResult];
static double[] V_Mud= new double[sizeResult];
static double[] V_Cut_mix= new double[sizeResult];
static double[] xTopCutting= new double[sizeResult];
static double[] h_cutting= new double[sizeResult];
static double[][] P_ann = new double[500][Nwc];
static double[] P_ann_ex = new double[sizeResult];
static double[][] D_result = new double[500][Nwc];
static double[][] VD_result = new double[500][Nwc];
static double[] f_cutting = new double[sizeResult];
static double[] f_cutting_old = new double[sizeResult];
static double[] oMudCutting = new double[sizeResult];
static double[] ff_cutting =  new double[sizeResult];
static double[] vsl_cutting = new double[sizeResult];
static double[] vslip_cutting = new double[sizeResult];
static double[] vfall_cutting = new double[sizeResult];
static double[] f_local = new double[sizeResult];
static double[] v_real_cutting = new double[sizeResult];
static double[] v_real_cutting_old = new double[sizeResult];
static double[] v_average = new double[sizeResult];
static double[] Area_local = new double[sizeResult];
static double[] dis_cut_from_top = new double[sizeResult];
static double[] dis_cut_from_top_old = new double[sizeResult];
static double A_cutting = 0;
static double Vol_particle_cutting = 0;
static double[] layerVDfrom = new double[5];
static double[] layerVDto = new double[5];
static double[] layerROP = new double[5];
static double[] layerRPM = new double[5];
static double[] layerWOB = new double[5];
static double[] layerFracP = new double[5];
static double[] layerPoreP = new double[5];
static double[] layerPerm = new double[5];
static double[] layerPoro = new double[5];
static int[] layerRock = new int[5]; //0: sandstone, 1: shale, 2: limestone
static double[] PermRock = new double[3];
static double[] PoroRock = new double[3];

static int iCuttingOut = 1; //0: 초기 생성된 cutting이 surface에 닿지 않음,  1: 닿아서 배출 시작
static double f_cutting_ini = 0;
static double TopCutting = 0;

static int numCutting = 0;
static int numLayer = 1;
static double R_reservoir = 1500; //ft
static double Q_loss = 0; // GPM
static double V_loss = 0; // BBL
static double MD_Loss_center = 0;
static double MD_Loss_length = 0;

static double dP_pump_mech = 0;
static double[] v_ac = new double[1000];
static double[] f_tot = new double[1000];
static double[] Do2p_cut = new double[Nwc], Di2p_cut = new double[Nwc], DiDS_cut = new double[Nwc];

static int NwcS_cutting = 0;
static int NwcE_cutting = 0;
static int NwcS_cutting_old = 0;
static int NwcE_cutting_old = 0;

static double[] ang2p_cutting = new double[Nwc];
static int iHWDP_cutting = 0;
static int iDC_cutting = 0;
static int iCsg_cutting = 0;	

static int n_area_intv = 20;
static double[][] V_cutting_area_intv = new double[Nwc][n_area_intv];
static double[][] f_cutting_area_intv = new double[Nwc][n_area_intv];
static double[][] f_cutting_area_intv_ini = new double[Nwc][n_area_intv];

static double Vdepth_casing = 0;

static int icase_mode = 0; // 0: manual 1: user
static int icase_selected=0; // 0: drilling & well-control , 1: well-control
static int iCHKcontrol2=0;//1: auto, 2:manual
}
//--------------------------------------------------------------end of file

