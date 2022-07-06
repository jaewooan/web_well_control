package ML_ERD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import ML_ERD.resultPlot.ChkBoxTimerTask;
import ML_ERD.resultPlot.createKillSheetPanel;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.Timer;

import javax.swing.border.LineBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

class FrmAnalysis extends JFrame {

	JPanel contentPane;		
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
	
	JLabel lblTitle = new JLabel("Analysis on Your Performance");
	int lblTitleSizeX = 590;
	int lblTitleSizeY = 40;
	int lblTitleSrtY = 10;
	
	titlePanel pnlTheory ;
	//String[] TheoryTitle= new String[2];
	String[] TheoryTitle= new String[1];
	titlePanel pnlExp ;
	//String[] ExpTitle= new String[2];
	String[] ExpTitle= new String[1];
	titlePanel pnlTF ;
	//String[] TFTitle = new String[2];
	String[] TFTitle = new String[1];
	titlePanel pnlKMW ;
	String[] KMWTitle = new String[2];
	titlePanel pnlICP ;
	String[] ICPTitle = new String[2];
	titlePanel pnlFCP ;
	String[] FCPTitle = new String[2];
	titlePanel pnlZero ;
	String[] ZeroTitle = new String[2];
	titlePanel pnlKPP ;
	String[] KPPTitle = new String[5];
	
	valuePanel KMWThPnl;
	valuePanel KMWExpPnl;
	valuePanel ICPThPnl;
	valuePanel ICPExpPnl;
	valuePanel FCPThPnl;
	valuePanel FCPExpPnl;
	valuePanel ZeroThPnl;
	valuePanel ZeroExpPnl;
	
	CheckPanel KMWchkPnl;
	CheckPanel ICPchkPnl;
	CheckPanel FCPchkPnl;
	CheckPanel KPPchkPnl;
	CheckPanel ZerochkPnl;
	
	JPanel dummyPanel = new JPanel();
	int pnlSrtX = 10;
	int pnlSrtY = lblTitleSrtY*2+lblTitleSizeY;
	int pnlSizeX = 220;
	int pnlSizeY = 50;
	int lineStroke = 1;
	int FontSize = 15;
	
	int ChartPnlXsize=400;
	int ChartPnlYsize=250;
	int ChartPnlSrtX = pnlSrtX+pnlSizeX;
	int ChartPnlSrtY = pnlSrtY+5*pnlSizeY;
	
	int FrmSrtX = 200;
	int FrmSrtY = 0;	
	
	int pnlSizeX2 = ChartPnlXsize / 2;
	int pnlSizeX3 = 230;
	
	int FrmSizeX = pnlSrtX*2+pnlSizeX+pnlSizeX2*2+pnlSizeX3+15;
	int FrmSizeY = pnlSrtY+pnlSizeY*5+ChartPnlYsize+70;
	
	JMenuBar menuBar = new JMenuBar();
	JMenu Menus = new JMenu("Menus");
	JMenuItem MenuMainmenu = new JMenuItem("Back to the Main Menu");
	JMenuItem mntmResetTheData = new JMenuItem("Reset the Data");
	//JMenuItem MenuReset = new JMenuItem("Reset the Data");
	JMenuItem mntmHelp = new JMenuItem("Help");
	JTextField txtTest1 = new JTextField();
	JTextField txtTest2 = new JTextField();
	
	JLabel lblMultiKick1 = new JLabel("Number of Additional Kicks");
	JLabel lblMultiKick2 = new JLabel("during Well Control");
	JLabel lblMultiKick3 = new JLabel(": ");
	
	FrmAnalysis() {
		txtTest2.setBounds(150, 546, 116, 21);
		txtTest2.setColumns(10);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				arg0.getWindow().dispose();
				arg0.getWindow().setVisible(false);
                menuClose();
			}
		});
		
		setTitle("Analysis on Your Performance");
		setIconImage(MainDriver.icon.getImage());
		setBounds(FrmSrtX, FrmSrtY, FrmSizeX, FrmSizeY);
		
		
		setJMenuBar(menuBar);		
		menuBar.add(Menus);
		MenuMainmenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuClose();
			}
		});		
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String helpContent = "  You can check the performance of your well control."+"\n"+
									 "  If you want to renew the graph and data, click the Reset the Data command button under Menus menu.";
				JOptionPane.showMessageDialog(null, helpContent, "Help", JOptionPane.INFORMATION_MESSAGE);	
			}
		});
		
		Menus.add(mntmHelp);
		
		
		//Menus.add(MenuReset);
		
		getContentPane().setLayout(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		//contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		

		setPlot();
		cp0.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		lblTitle.setBounds(FrmSizeX/2-lblTitleSizeX/2, lblTitleSrtY, lblTitleSizeX, lblTitleSizeY);		
		dummyPanel.setBackground(new Color(135, 206, 250));
		
		dummyPanel.setBounds(pnlSrtX, pnlSrtY, pnlSizeX, pnlSizeY);
		getContentPane().add(dummyPanel);
		dummyPanel.setBorder(new LineBorder(new Color(0, 0, 0), lineStroke));
		
		//ExpTitle[0] = "Your Input";
		//ExpTitle[1] = "Value";
		ExpTitle[0] = "Your Input Value";
		pnlExp = new titlePanel(pnlSrtX+pnlSizeX, pnlSrtY, pnlSizeX2, pnlSizeY, ExpTitle, lineStroke, FontSize);
		
		//TheoryTitle[0] = "True";
		//TheoryTitle[1] = "Value";
		TheoryTitle[0] = "True Value";
		pnlTheory = new titlePanel(pnlSrtX+pnlSizeX+pnlSizeX2, pnlSrtY,pnlSizeX2, pnlSizeY, TheoryTitle, lineStroke, FontSize);
		
		TFTitle[0] = "Evaluation";
		//TFTitle[0] = "True or";
		//TFTitle[1] = "False";
		pnlTF = new titlePanel(pnlSrtX+pnlSizeX+pnlSizeX2*2, pnlSrtY, pnlSizeX3, pnlSizeY, TFTitle, lineStroke, FontSize);
		
		KMWTitle[0] = "Kill Mud";
		KMWTitle[1] = "Weight (ppg)";
		pnlKMW = new titlePanel(pnlSrtX, pnlSrtY+pnlSizeY, pnlSizeX, pnlSizeY, KMWTitle, lineStroke, FontSize);
		
		ICPTitle[0] = "Initial Circulating";
		ICPTitle[1] = "Pressure (psig)";
		pnlICP = new titlePanel(pnlSrtX, pnlSrtY+pnlSizeY*2, pnlSizeX, pnlSizeY, ICPTitle, lineStroke, FontSize);
		
		FCPTitle[0] = "Final Circulating";
		FCPTitle[1] = "Pressure (psig)";
		pnlFCP = new titlePanel(pnlSrtX, pnlSrtY+pnlSizeY*3, pnlSizeX, pnlSizeY, FCPTitle, lineStroke, FontSize);
		
		ZeroTitle[0] = "Set Pump Stroke zero";
		ZeroTitle[1] = "in Time (second)";
		pnlZero = new titlePanel(pnlSrtX, pnlSrtY+pnlSizeY*4, pnlSizeX, pnlSizeY, ZeroTitle, lineStroke, FontSize);
		
		KPPTitle[0] = "Comparison of Theoretical ";
		KPPTitle[1] = "Kill Pump Pressure (psig)";
		KPPTitle[2] = "and";
		KPPTitle[3] = "Actual Pump Pressure (psig)";
		KPPTitle[4] = "by the User or the Computer";
		pnlKPP = new titlePanel(pnlSrtX, pnlSrtY+pnlSizeY*5, pnlSizeX, ChartPnlYsize, KPPTitle, lineStroke, FontSize);
		
		//KMWExpPnl = new valuePanel(pnlSrtX+pnlSizeX, pnlSrtY+pnlSizeY*2, pnlSizeX, pnlSizeY, MainDriver.test_KMW, lineStroke, FontSize);		
		//KMWThPnl = new valuePanel(pnlSrtX+pnlSizeX*2, pnlSrtY+pnlSizeY*2, pnlSizeX, pnlSizeY, MainDriver.test_KMW_Theory, lineStroke, FontSize);
		
		KMWExpPnl = new valuePanel(pnlSrtX+pnlSizeX, pnlSrtY+pnlSizeY, pnlSizeX2, pnlSizeY, MainDriver.test_KMW, lineStroke, FontSize);		
		KMWThPnl = new valuePanel(pnlSrtX+pnlSizeX+pnlSizeX2, pnlSrtY+pnlSizeY, pnlSizeX2, pnlSizeY, MainDriver.test_KMW_Theory, lineStroke, FontSize);
		ICPExpPnl = new valuePanel(pnlSrtX+pnlSizeX, pnlSrtY+pnlSizeY*2, pnlSizeX2, pnlSizeY, MainDriver.test_ICP, lineStroke, FontSize);		
		ICPThPnl = new valuePanel(pnlSrtX+pnlSizeX+pnlSizeX2, pnlSrtY+pnlSizeY*2, pnlSizeX2, pnlSizeY, MainDriver.test_ICP_Theory, lineStroke, FontSize);
		FCPExpPnl = new valuePanel(pnlSrtX+pnlSizeX, pnlSrtY+pnlSizeY*3, pnlSizeX2, pnlSizeY, MainDriver.test_FCP, lineStroke, FontSize);		
		FCPThPnl = new valuePanel(pnlSrtX+pnlSizeX+pnlSizeX2, pnlSrtY+pnlSizeY*3, pnlSizeX2, pnlSizeY, MainDriver.test_FCP_Theory, lineStroke, FontSize);
		if(MainDriver.set0_Finishtime-MainDriver.set0_Starttime<0) {
			ZeroExpPnl = new valuePanel(pnlSrtX+pnlSizeX, pnlSrtY+pnlSizeY*4, pnlSizeX2, pnlSizeY, 0, lineStroke, FontSize);	
		}
		else{
			ZeroExpPnl = new valuePanel(pnlSrtX+pnlSizeX, pnlSrtY+pnlSizeY*4, pnlSizeX2, pnlSizeY, MainDriver.set0_Finishtime-MainDriver.set0_Starttime, lineStroke, FontSize);	
		}				
		ZeroThPnl = new valuePanel(pnlSrtX+pnlSizeX+pnlSizeX2, pnlSrtY+pnlSizeY*4, pnlSizeX2, pnlSizeY, 60, lineStroke, FontSize);
		
		KMWchkPnl = new CheckPanel(pnlSrtX+pnlSizeX+pnlSizeX2*2, pnlSrtY+pnlSizeY, pnlSizeX3, pnlSizeY, 1, lineStroke, FontSize);
		ICPchkPnl = new CheckPanel(pnlSrtX+pnlSizeX+pnlSizeX2*2, pnlSrtY+pnlSizeY*2, pnlSizeX3, pnlSizeY, 2, lineStroke, FontSize);
		FCPchkPnl = new CheckPanel(pnlSrtX+pnlSizeX+pnlSizeX2*2, pnlSrtY+pnlSizeY*3, pnlSizeX3, pnlSizeY, 3, lineStroke, FontSize);
		ZerochkPnl = new CheckPanel(pnlSrtX+pnlSizeX+pnlSizeX2*2, pnlSrtY+pnlSizeY*4, pnlSizeX3, pnlSizeY, 5, lineStroke, FontSize);
		KPPchkPnl = new CheckPanel(pnlSrtX+pnlSizeX+pnlSizeX2*2, pnlSrtY+pnlSizeY*5, pnlSizeX3, ChartPnlYsize, 4, lineStroke, FontSize);
		
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 35));		
		contentPane.add(lblTitle);
		
		
		txtTest1.setBounds(10, 551, 125, 10);
		contentPane.add(txtTest1);
		txtTest1.setColumns(10);
		
		contentPane.add(txtTest2);
		txtTest1.setVisible(false);
		txtTest2.setVisible(false);
		
		mntmResetTheData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				KMWExpPnl.setValue(KMWExpPnl.lblt, MainDriver.test_KMW);
				KMWThPnl.setValue(KMWThPnl.lblt, MainDriver.test_KMW_Theory);
				ICPExpPnl.setValue(ICPExpPnl.lblt, MainDriver.test_ICP);	
				ICPThPnl.setValue(ICPThPnl.lblt, MainDriver.test_ICP_Theory);
				FCPExpPnl.setValue(FCPExpPnl.lblt, MainDriver.test_FCP);		
				FCPThPnl.setValue(FCPThPnl.lblt, MainDriver.test_FCP_Theory);
				if(MainDriver.set0_Finishtime-MainDriver.set0_Starttime<0) ZeroExpPnl.setValue(ZeroExpPnl.lblt, 0);
				else ZeroExpPnl.setValue(ZeroExpPnl.lblt, MainDriver.set0_Finishtime-MainDriver.set0_Starttime);		
				ZeroThPnl.setValue(ZeroThPnl.lblt, 60);
				
				KMWchkPnl.checkValue(KMWchkPnl.lblt, 1);
				ICPchkPnl.checkValue(ICPchkPnl.lblt, 2);
				FCPchkPnl.checkValue(FCPchkPnl.lblt, 3);
				KPPchkPnl.checkValue(KPPchkPnl.lblt, 4);
				ZerochkPnl.checkValue(ZerochkPnl.lblt, 5);
				
				nData = MainDriver.NwcE - MainDriver.NwcS + 5 + MainDriver.NumQcapeq;
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
					 cp0 = new createKillSheetPanel(xData, yData, "Pump Pressures during Well Control", "Theoretical", "Stroke", "P. (psig)");
					 cp0.setBounds(ChartPnlSrtX, ChartPnlSrtY, ChartPnlXsize, ChartPnlYsize);
					 contentPane.add(cp0);	
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
				cp0.resetGraph(xData, xData2, yData, yData2);
				}
			}
		});
		Menus.add(mntmResetTheData);
		
		Menus.add(MenuMainmenu);
		/*MenuReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KMWExpPnl.setValue(KMWExpPnl.lblt, MainDriver.test_KMW);
				KMWThPnl.setValue(KMWThPnl.lblt, MainDriver.test_KMW_Theory);
				ICPExpPnl.setValue(ICPExpPnl.lblt, MainDriver.test_ICP);	
				ICPThPnl.setValue(ICPThPnl.lblt, MainDriver.test_ICP_Theory);
				FCPExpPnl.setValue(FCPExpPnl.lblt, MainDriver.test_FCP);		
				FCPThPnl.setValue(FCPThPnl.lblt, MainDriver.test_FCP_Theory);
				
				KMWchkPnl.checkValue(KMWchkPnl.lblt, 1);
				ICPchkPnl.checkValue(ICPchkPnl.lblt, 2);
				FCPchkPnl.checkValue(FCPchkPnl.lblt, 3);
				KPPchkPnl.checkValue(KPPchkPnl.lblt, 4);
			}
		});*/
	}
	
	void menuClose(){
		MainDriver.FrmAnalysisOn=0;
		this.setVisible(false);
		this.dispose();
		}
	
	class CheckPanel extends JPanel{
		JLabel lblt = new JLabel("0");
		int lblNum=1;
		int lblSizeX=1;
		int lblSizeY=1;
		int fontSize=1;
		int upIntv= 1;
		int intvTempX=2;
		int intvTempY=2;
		int fontRevise=2;
		CheckPanel(){			
			setLayout(null);
			contentPane.add(this);			
		}
		CheckPanel(int SrtX, int SrtY, int SizeX, int SizeY, int checkNum, int LineStroke, int fontSize){
			// checkNum 1:KMW, 2:ICP, 3:FCP
			setLayout(null);
			contentPane.add(this);
			setBounds(SrtX, SrtY, SizeX, SizeY);
					
			lblSizeX = SizeX-intvTempX;
			lblSizeY = fontSize + fontRevise;
			upIntv = (int)((SizeY-lblSizeY)/2);			
			
			lblt.setHorizontalAlignment(SwingConstants.CENTER);
			lblt.setFont(new Font("±¼¸²", Font.BOLD, fontSize));
			lblt.setBounds(intvTempX/2, upIntv, lblSizeX, lblSizeY);
			
			upIntv = (int)((SizeY - lblSizeY*3 - intvTempY*2 )/2);
			lblMultiKick1.setHorizontalAlignment(SwingConstants.CENTER);
			lblMultiKick1.setFont(new Font("±¼¸²", Font.BOLD, fontSize));
			lblMultiKick1.setBounds(intvTempX/2, upIntv, lblSizeX, lblSizeY);
			add(lblMultiKick1);
			lblMultiKick2.setHorizontalAlignment(SwingConstants.CENTER);
			lblMultiKick2.setFont(new Font("±¼¸²", Font.BOLD, fontSize));
			lblMultiKick2.setBounds(intvTempX/2, upIntv+lblSizeY+intvTempY, lblSizeX, lblSizeY);
			add(lblMultiKick2);
			lblMultiKick3.setHorizontalAlignment(SwingConstants.CENTER);
			lblMultiKick3.setFont(new Font("±¼¸²", Font.BOLD, fontSize));
			lblMultiKick3.setBounds(intvTempX/2, upIntv+lblSizeY*2+intvTempY*2, lblSizeX, lblSizeY);
			add(lblMultiKick3);
			
			checkValue(lblt, checkNum);
			add(lblt);
			setBorder(new LineBorder(new Color(0, 0, 0), LineStroke));
		}
		
		void checkValue(JLabel ll, int nn){
			double theory = 0;
			double test = 0;
			if(nn==1){ //KMW
				ll.setVisible(true);
				lblMultiKick1.setVisible(false);
				lblMultiKick2.setVisible(false);
				lblMultiKick3.setVisible(false);
				theory = MainDriver.test_KMW_Theory;
				test = MainDriver.test_KMW;
				//txtTest1.setText((new DecimalFormat("####.0")).format((double)MainDriver.test_KMW_Theory));
				//txtTest2.setText((new DecimalFormat("####.0")).format((double)MainDriver.test_KMW));
				//theory = Double.parseDouble(txtTest1.getText());
				//test = Double.parseDouble(txtTest2.getText());
				
				if(test<=0){
					ll.setText("Not Available");
					}
				else if(test<=theory+0.05 && test>=theory ){
					ll.setText("True");
					}
				else if(test<=theory+0.5 && test>theory+0.05){
					ll.setText("OK");
					}
				else{
					ll.setText("False");
					}
				}
			else if(nn==2){//ICP
				ll.setVisible(true);
				lblMultiKick1.setVisible(false);
				lblMultiKick2.setVisible(false);
				lblMultiKick3.setVisible(false);
				theory = MainDriver.test_ICP_Theory;
				test = MainDriver.test_ICP;
				//txtTest1.setText((new DecimalFormat("####.0")).format((double)MainDriver.test_ICP));
				//txtTest2.setText((new DecimalFormat("####.0")).format((double)MainDriver.test_ICP_Theory));
				//theory = Double.parseDouble(txtTest1.getText());
				//test = Double.parseDouble(txtTest2.getText());
				
				if(test<=0){
					ll.setText("Not Available");
					}
				else if(test<=theory+1 && test>theory-1){
					ll.setText("True");
					}
				else if(test<=theory+5 && test>theory+1){
					ll.setText("OK");
					}
				else{
					ll.setText("False");
					}
			}
			else if(nn==3){ //FCP
				ll.setVisible(true);
				lblMultiKick1.setVisible(false);
				lblMultiKick2.setVisible(false);
				lblMultiKick3.setVisible(false);
				theory = MainDriver.test_FCP_Theory;
				test = MainDriver.test_FCP;
				//txtTest1.setText((new DecimalFormat("####.0")).format((double)MainDriver.test_FCP));
				//txtTest2.setText((new DecimalFormat("####.0")).format((double)MainDriver.test_FCP_Theory));
				//theory = Double.parseDouble(txtTest1.getText());
				//test = Double.parseDouble(txtTest2.getText());
				
				if(test<=0){
					ll.setText("Not Available");
					}
				else if(test<=theory+1 && test>theory-0.55){
					ll.setText("True");
					}
				else if(test<=theory+5 && test>theory+1){
					ll.setText("OK");
					}
				else{
					ll.setText("False");
					}
			}
			else if(nn==4){  // Kill pump pressure
				ll.setVisible(false);
				lblMultiKick1.setVisible(true);
				lblMultiKick2.setVisible(true);
				lblMultiKick3.setVisible(true);
				
				String msg = ": "+Integer.toString(MainDriver.MinusPoint);
				lblMultiKick3.setText(msg);
				}
			else{  // Set stroke zero
				if(MainDriver.gTcum<=0){
					ll.setText("Not Available");
					}
				else if(MainDriver.set0_Finishtime-MainDriver.set0_Starttime<=60 && MainDriver.set0_Finishtime>0){
					ll.setText("Yes");
					}
				else{
					ll.setText("No");
					}
				}
			
		}
	}
	
	class valuePanel extends JPanel{
		JLabel lblt = new JLabel("0");
		int lblNum=1;
		int lblSizeX=1;
		int lblSizeY=1;
		int fontSize=1;
		int upIntv= 1;
		int intvTempX=2;
		int intvTempY=1;
		int fontRevise=2;
		valuePanel(){			
			setLayout(null);
			contentPane.add(this);			
		}
		valuePanel(int SrtX, int SrtY, int SizeX, int SizeY, double target, int LineStroke, int fontSize){			
			setLayout(null);
			contentPane.add(this);
			setBounds(SrtX, SrtY, SizeX, SizeY);
					
			lblSizeX = SizeX-intvTempX;
			lblSizeY = fontSize + fontRevise;
			upIntv = (int)((SizeY-lblSizeY)/2);			
			
			lblt.setHorizontalAlignment(SwingConstants.CENTER);
			lblt.setFont(new Font("±¼¸²", Font.BOLD, fontSize));
			lblt.setBounds(intvTempX/2, upIntv, lblSizeX, lblSizeY);
			setValue(lblt, target);
			add(lblt);
			setBorder(new LineBorder(new Color(0, 0, 0), LineStroke));
		}
		
		void setValue(JLabel ll, double tt){
			ll.setText((new DecimalFormat("###,##0.0")).format(tt));
		}
	}
	
	class titlePanel extends JPanel{
		JLabel[] lblt;
		int lblNum=1;
		int lblSizeX=1;
		int lblSizeY=1;
		int fontSize=1;
		int upIntv= 1;
		int intvTempX=2;
		int intvTempY=1;
		int fontRevise=2;
		titlePanel(){			
			setLayout(null);
			contentPane.add(this);			
			setBackground(new Color(135, 206, 250));
		}
		titlePanel(int SrtX, int SrtY, int SizeX, int SizeY, String[] name, int LineStroke, int fontSize){			
			setLayout(null);
			contentPane.add(this);
			setBounds(SrtX, SrtY, SizeX, SizeY);
			setBackground(new Color(135, 206, 250));
			lblNum=name.length;
			
			lblSizeX = SizeX-intvTempX;
			lblSizeY = fontSize + fontRevise;
			upIntv = (int)((SizeY-lblSizeY*lblNum-intvTempY*(lblNum-1))/2);			
			
			lblt = new JLabel[lblNum];
			
			for(int i=0; i<lblNum; i++){
				lblt[i] = new JLabel(name[i]);
				lblt[i].setHorizontalAlignment(SwingConstants.CENTER);
				lblt[i].setFont(new Font("Franklin Gothic Medium", Font.BOLD, fontSize));
				lblt[i].setBounds(intvTempX/2, upIntv+(lblSizeY+intvTempY)*i, lblSizeX, lblSizeY);
				add(lblt[i]);
			}
			setBorder(new LineBorder(new Color(0, 0, 0), LineStroke));
		}
	}
	
	void setPlot(){
		nData = MainDriver.NwcE - MainDriver.NwcS + 5 + MainDriver.NumQcapeq;
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
	    
	   /* if(MainDriver.NumQcapeq==0){
	    	for(int i = (MainDriver.NwcE + 2); i>MainDriver.NwcS; i--){
		    	volks = volks + MainDriver.volDS[i-1];
		    	getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.Qkill, volks);
			    psp3 = getDPInsideEX[0];
			    pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
		        ppks[i] = pp3;
		        stks[i] = (int)(volks*(MainDriver.spMin1+MainDriver.spMin2) / (MainDriver.Qcapacity1*MainDriver.spMin1+MainDriver.Qcapacity2*MainDriver.spMin2)); 
		        }
	    	}		    
	    
	    else if(MainDriver.NumQcapeq>0){
	    	for(int i = (MainDriver.NwcE + 2); i>MainDriver.NwcS; i--){	  
	    		dummy=0;
	    		
	    		while(checkChangeNum<=MainDriver.NumQcapeq){
	    			if(MainDriver.KillVolChange[checkChangeNum]>volks && MainDriver.KillVolChange[checkChangeNum]<volks+MainDriver.volDS[i-1]){
	    				getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], MainDriver.KillVolChange[checkChangeNum]);
	    				psp3 = getDPInsideEX[0];
	    			    pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
	    			    checkChangeNum++;
	    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))] = pp3;
	    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))] = MainDriver.StrokeChange[checkChangeNum-1];
	    		        
	    		        getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], MainDriver.KillVolChange[checkChangeNum-1]);
	    				psp3 = getDPInsideEX[0];
	    			    pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
	    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] = pp3;
	    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] = MainDriver.StrokeChange[checkChangeNum-1];
	    		        
	    		        if(MainDriver.KillVolChange[checkChangeNum]<=volks || MainDriver.KillVolChange[checkChangeNum]>=volks+MainDriver.volDS[i-1]){
	    		        	volks = volks + MainDriver.volDS[i-1];
		    		    	getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], volks);
		    			    psp3 = getDPInsideEX[0];
		    			    pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
		    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = pp3;
		    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] + (int)((volks-MainDriver.KillVolChange[checkChangeNum-1])/MainDriver.Qcapeq[checkChangeNum-1]);
		    		        dummy=1;
		    		        break;		    		        
		    		        }
	    		        }
	    			
	    			else{
	    				volks = volks + MainDriver.volDS[i-1];
	    		    	getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], volks);
	    			    psp3 = getDPInsideEX[0];
	    			    pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
	    		        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = pp3;
	    		        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] + (int)(MainDriver.volDS[i-1]/MainDriver.Qcapeq[checkChangeNum-1]);    
	    		        dummy=1;
	    		        break;	    		        
	    		        }
	    			}	    	
	    		if(checkChangeNum>MainDriver.NumQcapeq && dummy==0){
	    			volks = volks + MainDriver.volDS[i-1];
	    			getDPInsideEX = utilityModule.getDPinside(MainDriver.Pb, MainDriver.QKillChange[checkChangeNum-1], volks);
				    psp3 = getDPInsideEX[0];
				    pp3 = getDPInsideEX[1]; //SPP : 0 , pp2 : 1
			        ppks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = pp3;
			        stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 1))] = stks[i + 2*(MainDriver.NumQcapeq-(checkChangeNum - 2))-1] + (int)(MainDriver.volDS[i-1]/MainDriver.Qcapeq[checkChangeNum-1]);    
			        }
	    		}
	    	}*/
	    
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
			 cp0 = new createKillSheetPanel(xData, yData, "Pump Pressures during Well Control", "Theoretical", "Stroke", "P. (psig)");
			 cp0.setBounds(ChartPnlSrtX, ChartPnlSrtY, ChartPnlXsize, ChartPnlYsize);
			 contentPane.add(cp0);	
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
			 cp0 = new createKillSheetPanel("Stroke", "P. (psig)", "Pump Pressures during Well Control", xData, xData2, yData, yData2, "Theoretical", "Actual");		
			 cp0.setBounds(ChartPnlSrtX, ChartPnlSrtY, ChartPnlXsize, ChartPnlYsize);
			 contentPane.add(cp0);
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
		JFreeChart chart;
		XYPlot plot;
		
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
	    	plot = new XYPlot(dataset, xAxis, yAxis, renderer);
	    	plot.setOutlineVisible(false);
	    	JFreeChart chart = new JFreeChart(ChartTitle, plot);
	    	chart.removeLegend();
	    	
	    	cp = new ChartPanel(chart);
	    	cp.setBounds(5,20,ChartPnlXsize-10, ChartPnlYsize-40);
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
			
			xAxis = new NumberAxis(xAxisTitle);
	    	yAxis = new NumberAxis(yAxisTitle);
	    	
	    	renderer.setSeriesPaint(0, Color.red);
	    	renderer2.setSeriesPaint(0, Color.blue);
	    	renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	renderer2.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	
	    	plot = new XYPlot(dataset, xAxis, yAxis, renderer);
	    	//plot.setDomainAxis(1, xAxis);
	    	//plot.setRangeAxis(1, yAxis);
	    	plot.setDataset(1, dataset2);
	    	plot.setRenderer(1, renderer2);
	    	plot.mapDatasetToRangeAxis(2, 1);
	    	plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
	    	plot.setOutlineVisible(false);
	    	chart = new JFreeChart(ChartTitle, plot);	    	
	    	
	    	cp = new ChartPanel(chart);
	    	cp.setBounds(5,20,ChartPnlXsize-10, ChartPnlYsize-40);
	    	add(cp);
		}
		
		void resetGraph(double[] Xdata, double[] Xdata2, double[] Ydata, double[] Ydata2){
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
			
			xAxis = new NumberAxis(xAxisTitle);
	    	yAxis = new NumberAxis(yAxisTitle);
	    	
	    	renderer.setSeriesPaint(0, Color.red);
	    	renderer2.setSeriesPaint(0, Color.blue);
	    	renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	renderer2.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
	    	
	    	plot = new XYPlot(dataset, xAxis, yAxis, renderer);
	    	//plot.setDomainAxis(1, xAxis);
	    	//plot.setRangeAxis(1, yAxis);
	    	plot.setDataset(1, dataset2);
	    	plot.setRenderer(1, renderer2);
	    	plot.mapDatasetToRangeAxis(2, 1);
	    	plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
	    	plot.setOutlineVisible(false);
	    	chart = new JFreeChart(ChartTitle, plot);	    	
	    	
	    	cp = new ChartPanel(chart);
	    	cp.setBounds(5,20,ChartPnlXsize-10, ChartPnlYsize-40);
	    	add(cp);
		}
		
		void resizeKillPnl(){			
			cp.setBounds(0,0,ChartPnlXsize-10, ChartPnlYsize-40);
		}
	}
}
