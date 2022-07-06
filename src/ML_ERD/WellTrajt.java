package ML_ERD;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import java.awt.event.ComponentEvent;

class WellTrajt extends JFrame {
	
	int[] pmoveTop = new int[6];
	int[] pmoveLeft = new int[6];
	float DrawWidth = 0;
	
	JMenuBar menuBar = new JMenuBar();
	JMenu mnMenu = new JMenu("Menu");
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
	int PanelSizeX=1000, PanelSizeY=1000;//1000,1000
	int PicSizeX=PanelSizeX*2, PicSizeY=PanelSizeY;
	double scx1=0, scy1=0, scx2=PicSizeX, scy2=PicSizeY, negLeft=0, posLeft=0, tMDwell=0;
	int setScaleOn=0, intv=100;
	
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
	
	WellTrajt() {
		setTitle("Wellbore Trajectory");
		setIconImage(MainDriver.icon.getImage());
		
		addComponentListener(new ComponentAdapter() {			
			public void componentResized(ComponentEvent arg0) {
				PanelSizeX = getWidth();
				PanelSizeY = getHeight();
				PicSizeX=PanelSizeX*2;
				PicSizeY=PanelSizeY;
				pntpnl.setBounds(0, 0, PicSizeX, PicSizeY);
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent arg0) {
				//WellTrajt.WindowState = 2 // maximize
				setScale();   //set a scale for ERD and ML
				pntpnl.repaint();
				//if(MainDriver.igERD == 1) DrawMultilateral   //to draw multilateral trajectories
						//				
				wttext0.setText( format2.format(MainDriver.Vdepth));
				wttext2.setText( format2.format(MainDriver.Hdisp));
				if (MainDriver.iWell == 0) {
					tMDwell = MainDriver.Vdepth; 
					wttext2.setText( "0.0");
					}
				if (MainDriver.iWell >= 1){  // draw KOP & First Build Section
					wtFrame1.setVisible(true);
					tMDwell = MainDriver.DepthKOP + MainDriver.Rbur * MainDriver.angEOB * MainDriver.radConv;
				}
				if (MainDriver.iWell >= 2) tMDwell = tMDwell + MainDriver.xHold;   // draw First Hold Section
				if (MainDriver.iWell >= 3){
					wtFrame2.setVisible(true);
					tMDwell = tMDwell + MainDriver.R2bur * (MainDriver.ang2EOB - MainDriver.angEOB) * MainDriver.radConv;
				}
				if (MainDriver.iWell == 4) tMDwell = tMDwell + MainDriver.x2Hold;
				wttext3.setText( format2.format(MainDriver.DepthKOP));
				wttext4.setText( format2.format(MainDriver.BUR));
				wttext5.setText( format2.format(MainDriver.Rbur));
				wttext6.setText( format2.format(MainDriver.angEOB));
				wttext7.setText( format2.format(MainDriver.xHold));
				if (MainDriver.iWell == 1) wttext7.setText( "0.0");
				wttext8.setText( format2.format(MainDriver.BUR2));
				wttext9.setText( format2.format(MainDriver.R2bur));
				wttext10.setText( format2.format(MainDriver.ang2EOB));
				wttext11.setText( format2.format(MainDriver.x2Hold));
				if (MainDriver.iWell == 3) wttext11.setText( "0.0");
				wttext1.setText(format2.format(tMDwell));
				
				wtFrame1.setVisible(true);
				wtFrame1.setVisible(false);
				wtFrame2.setVisible(false);
				if (MainDriver.iWell >= 1){  // draw KOP & First Build Section
					wtFrame1.setVisible(true);
				}
				if (MainDriver.iWell >= 3){
					wtFrame2.setVisible(true);
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
                MainDriver.iWshow = 0;
            }
        });
		
		
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, PanelSizeX, PanelSizeY);	
		
		contentPane = new JPanel();		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
				
		setJMenuBar(menuBar);		
		menuBar.add(mnMenu);
		JMenuItem mnHide = new JMenuItem("Hide information boxes");
		mnHide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				   wtFrame0.setVisible(false);
				   wtFrame1.setVisible(false);
				   wtFrame2.setVisible(false);				
			}
		});
		JMenuItem mnShow = new JMenuItem("Show information boxes");
		mnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				wtFrame0.setVisible(true);
				if (MainDriver.iWell >= 1) wtFrame1.setVisible(true);
				if (MainDriver.iWell >= 3) wtFrame2.setVisible(true);
				}
		});
		
		JMenu mnPrintScreen = new JMenu("PrintScreen");
		mnPrintScreen.setEnabled(false);
		JMenuItem mnMono = new JMenuItem("Monochrome");
		JMenuItem mnColor = new JMenuItem("Color");
		JMenuItem mnClose = new JMenuItem("Close the Form");
		mnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 menuClose_Click();				
			}
		});
		
		
		mnMenu.add(mnClose);		
		mnMenu.add(mnHide);
		mnMenu.add(mnShow);
		menuBar.add(mnPrintScreen);
		mnPrintScreen.add(mnMono);		
		mnPrintScreen.add(mnColor);		
				
		wtFrame0.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		wtFrame0.setBounds(630, 60, 340, 99);
		contentPane.add(wtFrame0);
		wtFrame0.setLayout(null);
		
		lblVMD.setHorizontalAlignment(SwingConstants.CENTER);
		lblVMD.setFont(new Font("±¼¸²", Font.BOLD, 17));
		lblVMD.setBounds(0, 5, 340, 15);
		wtFrame0.add(lblVMD);
		wttext0.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext0.setBounds(12, 30, 80, 18);
		wtFrame0.add(wttext0);
		wttext1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext1.setBounds(12, 52, 80, 18);
		wtFrame0.add(wttext1);
		wttext2.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext2.setBounds(12, 74, 80, 18);
		wtFrame0.add(wttext2);
		
		lblvd.setBounds(104, 30, 190, 18);
		wtFrame0.add(lblvd);
		
		lbltotmd.setBounds(104, 52, 190, 18);
		wtFrame0.add(lbltotmd);
		
		lblhorizon.setBounds(104, 74, 226, 18);
		wtFrame0.add(lblhorizon);
		
		wtFrame1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		wtFrame1.setBounds(630, 169, 340, 149);
		contentPane.add(wtFrame1);
		wtFrame1.setLayout(null);
		
		lblFirstBuildOr.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirstBuildOr.setFont(new Font("±¼¸²", Font.BOLD, 17));
		lblFirstBuildOr.setBounds(0, 5, 340, 15);
		wtFrame1.add(lblFirstBuildOr);
		wttext3.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext3.setBounds(12, 30, 80, 18);
		wtFrame1.add(wttext3);
		wttext4.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext4.setBounds(12, 52, 80, 18);
		wtFrame1.add(wttext4);
		wttext5.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext5.setBounds(12, 74, 80, 18);
		wtFrame1.add(wttext5);
		wttext6.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext6.setBounds(12, 96, 80, 18);
		wtFrame1.add(wttext6);
		wttext7.setHorizontalAlignment(SwingConstants.RIGHT);
		
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
		
		JLabel lblLengthOfThe = new JLabel("ft, Length of the First Hold");
		lblLengthOfThe.setBounds(104, 118, 190, 18);
		wtFrame1.add(lblLengthOfThe);
		
		wtFrame2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		wtFrame2.setBounds(630, 328, 340, 121);
		contentPane.add(wtFrame2);
		wtFrame2.setLayout(null);
		
		lblSecondBuildOr.setHorizontalAlignment(SwingConstants.CENTER);
		lblSecondBuildOr.setFont(new Font("±¼¸²", Font.BOLD, 17));
		lblSecondBuildOr.setBounds(0, 5, 340, 15);
		wtFrame2.add(lblSecondBuildOr);
		wttext8.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext8.setBounds(10, 30, 80, 18);
		wtFrame2.add(wttext8);
		wttext9.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext9.setBounds(10, 52, 80, 18);
		wtFrame2.add(wttext9);
		wttext10.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wttext10.setBounds(10, 74, 80, 18);
		wtFrame2.add(wttext10);
		wttext11.setHorizontalAlignment(SwingConstants.RIGHT);
		
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
		txtVD.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtVD.setBounds(641, 10, 80, 18);
		contentPane.add(txtVD);
		
		lblVerticalDepthFt.setBounds(733, 10, 190, 18);
		contentPane.add(lblVerticalDepthFt);
		txtHD.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtHD.setBounds(641, 32, 80, 18);
		contentPane.add(txtHD);
		
		lblHorizonMouse.setBounds(733, 32, 190, 18);
		contentPane.add(lblHorizonMouse);
		
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
	}
	
	void menuClose_Click(){
		this.dispose();
	}
	
	class paintPanel extends JPanel{
		
		paintPanel(){
			//this.setBackground(new Color(240,240,240));		
		}
		
		public void paintComponent(Graphics g){
		//Private Sub DrawWellTrajt()
			super.paintComponent(g);
			double[] dummy=new double[2];
			double delTop=0, delBtm=0, delleft=0, rigright=0, rigtop=0, righeit=0, rigmid=0, drawDepth=0;
			double rad1=0, rad2=0, xx1=0, xx2=0, yy1=0, yy2=0;			
			Graphics2D g2d = (Graphics2D)g;			
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
			g.drawLine(scX(delleft), scY(0), scX(-delleft), scY(0));
			if (MainDriver.iOnshore == 2) g.drawLine(scX(delleft), scY(MainDriver.Dwater), scX(-delleft), scY(MainDriver.Dwater));
	
		   // draw the simple rig
			DrawWidth = 1.5f;
			g2d.setStroke(new BasicStroke(DrawWidth));
			rigright = delTop * 0.3; 
			righeit = 0.8 * delTop;
			rigtop = delTop * 0.18;
			rigmid = 0.72 * righeit;
			g.drawLine(scX(rigright), scY(0), scX(rigtop), scY(righeit));
			g.drawLine(scX(rigtop), scY(righeit), scX(-rigtop), scY(righeit));
			g.drawLine(scX(-rigtop), scY(righeit), scX(-rigright), scY(0));
			g.drawLine (scX(0.3 * delTop), scY(rigmid), scX(-(0.3 * delTop)), scY(rigmid));
		   
		   // draw wellbore trajectory - vertical to horizontal wells
			DrawWidth = 3;
			g2d.setStroke(new BasicStroke(DrawWidth));
			if (MainDriver.iWell == 0) g.drawLine (scX(0), scY(0), scX(0), scY(MainDriver.Vdepth));
			
			if (MainDriver.iWell >= 1) { // draw KOP & First Build Section
				g.drawLine (scX(0), scY(0), scX(0), scY(MainDriver.DepthKOP));
				drawDepth = MainDriver.DepthKOP;
				dummy = DrawArc(0.0, drawDepth, 0.0, (double)MainDriver.angEOB, (double)MainDriver.Rbur, g);
				}
		   
			if (MainDriver.iWell >= 2) {   // draw First Hold Section
				rad1 = MainDriver.angEOB * MainDriver.radConv;
				xx1 = MainDriver.Rbur * (1 - Math.cos(rad1)); 
				yy1 = MainDriver.DepthKOP + MainDriver.Rbur * Math.sin(rad1);
				xx2 = xx1 + MainDriver.xHold * Math.sin(rad1);
				yy2 = yy1 + MainDriver.xHold * Math.cos(rad1);
				g.drawLine(scX(xx1), scY(yy1), scX(xx2), scY(yy2));
				}
		   
			if (MainDriver.iWell >= 3) dummy = DrawArc(xx2, yy2, (double)MainDriver.angEOB, (double)MainDriver.ang2EOB, (double)MainDriver.R2bur, g);
		   
			if (MainDriver.iWell == 4){
				rad2 = MainDriver.ang2EOB * MainDriver.radConv;
//		    xx1 = xx2 + MainDriver.R2bur * (Math.cos(rad1) - Math.cos(rad2))   //old format
//		    yy1 = yy2 + MainDriver.R2bur * (Math.sin(rad2) - Math.sin(rad1))
				xx1 = dummy[0];  //This is new format for easy drawing  //1/4/03
				yy1 = dummy[1];
				xx2 = xx1 + MainDriver.x2Hold * Math.sin(rad2); 
				yy2 = yy1 + MainDriver.x2Hold * Math.cos(rad2);
				g.drawLine(scX(xx1), scY(yy1), scX(xx2), scY(yy2));
				}
			}
		
		double[] DrawArc(double xx, double yy, double ang1, double ang2, double rr, Graphics g){
			// draw the arc using the given starting point, angle, and radius
			//  xx- ft, starting x-position   //1/3/03- legend addition
			//  yy- ft, starting y-position   //also modified for negative BUR
			//  ang1, ang2- start and end of angle
			//  rr- ft, radius of curvature
			
			super.paintComponents(g);
			double[] result = new double[2];
			result[0]=0;
			result[1]=0;
			
			if(Math.abs(ang1 - ang2) < 0.08) return result;
			    
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
			
			result[0] = xx2;
			result[1] = yy2;
			//
			return result;
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
		scx1=delTop;
		scy1=delTop;
		scx2=delBtm*2.3;
		scy2=delBtm*1.8;
		
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
	
		/*
	

Private Sub cmdColor_Click()
    msgTmp$ = "You may have a very Dark printout"
    msgTmp$ = msgTmp$ + " if you do not have a COLOR printer. Continue ?"
    resp = MsgBox(msgTmp$, 1, msgTitle)
    if resp = 1 Then
      PrintForm
    else
      Exit Sub
    End if
End Sub

Private Sub cmdMono_Click()
    WellTrajt.BackColor = White
    For i = 0 To 2
        wtFrame[i].BackColor = White
    Next i
    For i = 0 To 11
        wtText[i].BackColor = White
        wtLabel[i].BackColor = White
    Next i
    DrawWellTrajt
    PrintForm
    SetColorBak
    DrawWellTrajt
End Sub


Private Sub DrawMultilateral()
//..... draw multilateral trajectories, 1/3/03
    DrawWidth = 2
    For i = 0 To (igMLnumber - 1)
       startX = mlKOP[i]
       Call getVD(startX, startVD)
       Call getAngle(startX, MLangle)
       Call getHorizDeparture(startX, startHD)    //we need starting HD !
       MLradius = 18000 / (pai * mlBUR[i])
    
       Call DrawArc(startHD, startVD, MLangle, mlEOB[i], MLradius)
       rad1 = MLangle * MainDriver.radConv
       rad2 = mlEOB[i] * MainDriver.radConv
//       xx1 = startHD + MLradius * (Math.cos(rad1) - Math.cos(rad2))
//       yy1 = startX + MLradius * (Math.sin(rad2) - Math.sin(rad1))
       xx1 = startHD
       yy1 = startVD
       xx2 = xx1 + mlHold[i] * Math.sin(rad2)
       yy2 = yy1 + mlHold[i] * Math.cos(rad2)
       WellTrajt.Line (xx1, yy1,(int)xx2, yy2)
       
       if Abs(mlBUR2nd[i]) < 0.0002 Then
          MLradius = 0
       else
          MLradius = 18000 / (pai * mlBUR2nd[i])
       End if
       Call DrawArc(xx2, yy2, mlEOB[i], mlEOB2nd[i], MLradius)
    
       rad1 = mlEOB[i] * MainDriver.radConv
       rad2 = mlEOB2nd[i] * MainDriver.radConv
//       xx1 = xx2 + MLradius * (Math.cos(rad1) - Math.cos(rad2))
//       yy1 = yy2 + MLradius * (Math.sin(rad2) - Math.sin(rad1))
       xx1 = xx2
       yy1 = yy2
       xx2 = xx1 + mlHold2nd[i] * Math.sin(rad2)
       yy2 = yy1 + mlHold2nd[i] * Math.cos(rad2)
       WellTrajt.Line (xx1, yy1,(int)xx2, yy2)
        
    Next i
End Sub

Private Sub HScrlscrn_Change()
    WellTrajt.ScaleMode = 1
    iMove = HScrlscrn.Value
    For i = 0 To 2
        WellTrajt.wtFrame[i].Move (pmoveLeft[i] - iMove)
    Next i
    WellTrajt.txtVD.Move (pmoveLeft(3) - iMove)
    WellTrajt.txtHD.Move (pmoveLeft(4) - iMove)
    WellTrajt.wtLabel(12).Move (pmoveLeft(5) - iMove)
    WellTrajt.wtLabel(13).Move (pmoveLeft(6) - iMove)
End Sub

Private Sub menuClose_Click()
    Unload WellTrajt
End Sub

Private Sub SetColorBak()
Dim bgColor
//--- set all background color
    bgColor = &H808000        //default color for the form !
    WellTrajt.BackColor = bgColor
    For i = 0 To 2
        wtFrame[i].BackColor = bgColor
    Next i
    For i = 0 To 11
        wtText[i].BackColor = &HFFFF&     //bright yellow as output
        wtLabel[i].BackColor = bgColor
    Next i
End Sub

Private Sub VScrlscrn_Change()
    WellTrajt.ScaleMode = 1    //Twips mode, ie 1440 twips in one inch.
    iMove = VScrlscrn.Value
    For i = 0 To 2
        objleft = wtFrame[i].Left
        WellTrajt.wtFrame[i].Move objleft, (pmoveTop[i] - iMove)
    Next i
    objleft = txtVD.Left
    WellTrajt.txtVD.Move objleft, (pmoveTop(3) - iMove)
    objleft = txtHD.Left
    WellTrajt.txtHD.Move objleft, (pmoveTop(4) - iMove)
    objleft = wtLabel(12).Left
    WellTrajt.wtLabel(12).Move objleft, (pmoveTop(5) - iMove)
    objleft = wtLabel(13).Left
    WellTrajt.wtLabel(13).Move objleft, (pmoveTop(6) - iMove)
End Sub

Private Sub wtText_Change(Index As Integer)

End Sub

	 */
}
