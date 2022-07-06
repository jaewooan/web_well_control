package ML_ERD;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

class wellpic extends JFrame {

	Timer TimerRot;
	Timer TimerRot2;
	TRTask Task1 = new TRTask();
	private JPanel contentPane;
	int drawwidth=5;
	int iRotary=0;
	int leftX=0, topX=0, heightX=600, widthX=200; //4245,1065,6915,3075
	int frameYintvSize1 = 30, frameYintvSize2=8, frameXintvSize=8;
	int showKickIndexLoad =0;
	int paintIndex1=0, paintIndex2=1, paintIndex3 =0, showKickIndex=0, drawOmudIndex=0, drawKmudIndex=0, drawKickIndex=0, drawWellboreIndex=0; //paint안에서 어떠한 component를 실행시킬지에 대한 index, 0은 꺼진 것, 1은 켜진 것.. index1는 일단 TimerRot에 대한 TRdraw... , index2 ; form_load
	int drawMultikickIndex=0, kickNumber=0;
	double[] multiQtotMixWP=new double[1000];
	double[] multixDepthWP= new double[1000];
	double xx1, xx2, yy1, yy2; // coordinates (x1,y1) in the JFRAME is eqaul to  ((x-xx1)/(xx2-xx1)*widthX+leftX, (y-yy1)/(yy2-yy1)*heightX+topX where (x,y) is the coordinate in Visual Basic Wellpic)
	int TimerRotOn=0;
	int TimerRotOn2=0;
	int TRTaskOn=1;
	double volkillmud=0, QtotMixWP=0, xDepthWP=0;

	paintPanel pp = new paintPanel();

	wellpic() {		
		setTitle("Wellbore Vertical Profile");
		setIconImage(MainDriver.icon.getImage());

		paintIndex1=0;
		paintIndex2=1;
		paintIndex3 =0;
		showKickIndex=0;
		drawOmudIndex=0;
		drawKmudIndex=0;
		drawKickIndex=0;
		drawWellboreIndex=0;
		TimerRotOn=0;
		TimerRotOn2=0;
		TRTaskOn=1;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				e.getWindow().dispose();
				e.getWindow().setVisible(false);
				MainDriver.iWshow = 0;
				if(TimerRotOn==1){
					TimerRot.cancel();
					TimerRotOn=0;
				}
				if(TimerRotOn2==1){
					TimerRot2.cancel();
					TimerRotOn2=0;
				}
			}
		});

		setBounds(leftX, topX, widthX, heightX);
		contentPane = new JPanel();
		setContentPane(contentPane);
		pp.setBounds(0, 0, widthX-frameXintvSize*2, heightX-frameYintvSize1-frameYintvSize2);
		contentPane.add(pp);	
		contentPane.setLayout(null);
		form_Load();


	}

	void form_Load(){
		// find the maximum casing diameter for onshore or offshore
		// use diameter rather than radius. It means I use 2 times scale up
		//    windowstate = 0 //Normal size
		//WellPic.Height = heightX; WellPic.Left = leftX
		//WellPic.Top = topX; WellPic.Width = widthX
		double temp=0;
		double diaAdd=0, diamax=0, delBtm=0;
		paintIndex2=1;
		showKickIndexLoad =1;			

		if(MainDriver.iCduct == 1) temp = MainDriver.ODcduct;
		else if (MainDriver.iSurfCsg == 1) temp = MainDriver.ODsurfCsg;
		else temp = MainDriver.IDcasing + 3;

		diaAdd = Math.max(MainDriver.Dchoke, MainDriver.Dkill);
		diamax = MainDriver.Driser + diaAdd;
		if (MainDriver.iOnshore == 2 && temp < diamax) temp = diamax;
		delBtm = MainDriver.Vdepth / 20;

		xx1 = 1.2 * (-1 - temp);
		xx2 = -xx1;
		yy1 = -delBtm; 
		yy2 = MainDriver.Vdepth + MainDriver.Vdepth / 40;
		// Draw wellbore geometry based on input data
		//WellPic.Scale (xx1, yy1,xx2, yy2) // vb in scale (x,y,x1,y1) =>x의 최소는 x, 최대는 x1, y의 최소는 y y의 최대는 y1으로 자동 좌표가 설정됨.
		// draw casings, wellbore, and the drilling fluid
		MainModule.setMDvd();     // calculate & assign directional data	    

		//
		//  txtDepth.Text = ""; txtDia.Text = "" //pass
		//setBounds(leftX, topX, widthX, heightX);
	}

	int scX (double x){
		return (int)((x-xx1)/(xx2-xx1)*((widthX-20)-20)+10);
	}

	int scY (double y){
		return (int)((y-yy1)/(yy2-yy1)*((heightX-30-10)-20)+10);
	}

	class TRTask extends TimerTask{
		public void run(){
			if(TRTaskOn==1){
				paintIndex1=1;			
				if (MainDriver.iBOP == 0){
					paintIndex1=0;
					if(TimerRotOn==1) TimerRot.cancel();
					if(TimerRotOn2==1) TimerRot2.cancel();
				}				
				pp.repaint();
			}
		}
	}

	void menuclose(){
		if(TimerRotOn == 1){
			TimerRot.cancel();
			TimerRotOn=0;
		}
		this.dispose();
		this.setVisible(false);
	}

	class paintPanel extends JPanel{

		int dummy=0;

		paintPanel(){
			this.setBackground(MainDriver.WellColor);
		}

		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			drawwidth = 1;			

			if(paintIndex1==1&& MainDriver.iBOP==1){//TR TIMER ON, TRDraw
				//draw the wellbore profile based on actual geometry
				//draw conductor or surface casing

				g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 
				double ohwidth = 2.5, dicd=0, docd=0, delta=0;
				double DrotTab = 0.5 * (MainDriver.doDP + MainDriver.IDcasing);
				double HrotTab = -MainDriver.Vdepth / 40;
				int iLocation=0;
				double Xconst=0;

				g.fillRect(scX(-DrotTab), scY(HrotTab), scX(DrotTab)-scX(-DrotTab), scY(0)-scY(HrotTab));//, Black, BF
				iRotary = iRotary + 1;
				iLocation = iRotary % 6;
				if(iLocation == 1) Xconst = 0.75 * DrotTab;
				else if(iLocation == 2) Xconst = 0;
				else if(iLocation == 3) Xconst = -0.75 * DrotTab;

				drawwidth = 3;
				g2d.setStroke(new BasicStroke(drawwidth));
				if (iLocation >= 1 && iLocation <= 3){
					g.setColor(Color.WHITE);
					g.drawLine (scX(Xconst), scY(HrotTab * 0.9), scX(Xconst), scY(HrotTab * 0.05));
				}
				//
				double x1 = 1.2 * (-1 - MainDriver.ODcduct);
				double x2 = -x1;	    
				g.setColor(Color.BLACK);	    
				g.drawLine(scX(x1), scY(0), scX(x2), scY(0));
				//xx1 = 1.2 * (-1 - ODcduct); xx2 = -xx1 <=일단은 forMLOAD에서 XX1, XX2만큼 좌표변환하고 모든걸 표현하고 있는중임...
				//drawwidth = 3
				//g.drawLine (xx1, 0,xx2, 0)				
			} //Befor BOP

			if(paintIndex2==1){ // form_Load
				drawConductor(g);
				DrawOMud(g);
				drawWellbore(g);
				showKick(g); //draw the kick in the drillsi module 
				//
				g.setColor(Color.BLACK);

				drawwidth = 5;
				g2d.setStroke(new BasicStroke(drawwidth));
				g.drawLine (scX(xx1), scY(0), scX(xx2), scY(0));

				drawwidth = 4;
				g2d.setStroke(new BasicStroke(drawwidth));
				g.drawLine (scX(xx1), scY(MainDriver.Dwater), scX(-MainDriver.doDP), scY(MainDriver.Dwater));
				g.drawLine (scX(xx2), scY(MainDriver.Dwater), scX(MainDriver.doDP), scY(MainDriver.Dwater));
				//

				drawwidth = 2;
				g2d.setStroke(new BasicStroke(drawwidth));
				double DrotTab = 0.5 * (MainDriver.doDP + MainDriver.IDcasing); 
				double HrotTab = -MainDriver.Vdepth / 40;

				//simdis.iROP=1;
				if (MainDriver.iBOP == 0) closeBop(g);  //ie. BOP is OPEN (1)
				else if(simdis.iROP==0) g.fillRect (scX(-DrotTab), scY(HrotTab), scX(DrotTab)-scX(-DrotTab), scY(0)-scY(HrotTab));//, Black), BF
			}	

			if(showKickIndex==1) {
				//	showKick(g);
				showKickIndex=0;
			}	

			if(drawOmudIndex==1){
				DrawOMud(g);
				//drawOmudIndex=0;
			}

			//if (MainDriver.iHresv == 1 || MainDriver.igERD == 1) Call DrawMultiKick
			//else Call DrawKick(QtotMix, Xb);	
			if(drawWellboreIndex==1){				
				drawWellbore(g);
				drawConductor(g);
				//drawWellboreIndex=0;
			}				

			if(drawKickIndex==1){
				if(drawMultikickIndex!=1){
					dummy=DrawKick(QtotMixWP, xDepthWP, g);
				}
				else{
					for(int i=0; i<kickNumber; i++){
						dummy=DrawKick(multiQtotMixWP[i], multixDepthWP[i], g);
					}
				}
				if(drawKmudIndex==1){
					dummy = DrawKmud(g);
					//drawKmudIndex=0;
				}
				//drawKickIndex=0;
			}

		}


		int DrawKick(double VOLt, double xDepth, Graphics g){
			//  sub to draw the kick mixture based on the given volume and location.
			// this is generally good and modified from GetBotH
			// xloc;the given bottom, ft  volTotal;total volume, bbls, hh;height, ft
			// hhm;measured depth from xloc, ft
			//
			super.paintComponents(g);
			Graphics2D g2d = (Graphics2D)g;
			int iPos=0;
			double volTotal = VOLt;//val    //line input reads it as character
			double xLoc = xDepth;//Val(xDepth)
			double Xup=0;
			double xVDup=0;
			if (volTotal < 0.001) return 0;  //no draw if mixture vol < 0.001

			iPos=utilityModule.Xposition(xLoc);   // identify the location of the given x
			//.............................. calculate MD by the given volume, bbls
			double Htmp = xLoc - MainDriver.TMD[iPos + 1];
			double volSum = 0, voldown = 0, hhm = 0, Qflow = 0, Xdown = xLoc, xVDdown=0;
			double d2=0, d1=0, capcty=0, d95=0;
			double volteff=0, kickbot = 0, kickVD=0;
			double intv=0; //20140310 ajw

			xVDdown = utilityModule.getVD(Xdown);
			//
			drawwidth = 2;
			g2d.setStroke(new BasicStroke(drawwidth));

			for(int i=(iPos + 1); i< 9; i++){    // annulus except choke or kill lines		    	
				d2 = MainDriver.Do2p[i];
				d1 = MainDriver.Di2p[i];
				capcty = MainDriver.C12 * (d2 * d2 - d1 * d1);
				d95 = d2 * 0.95;
				volSum = volSum + capcty * Htmp;		    			    	
				if(volSum > volTotal){		    		
					hhm = hhm + (volTotal - voldown) / capcty;
					Xup = xLoc - hhm;
					xVDup = utilityModule.getVD(Xup);
					g.setColor(MainDriver.KickColor);
					g.fillRect(scX(-d95), scY(xVDup), scX(-d1)-scX(-d95) , scY(xVDdown)-scY(xVDup)+2);//, KickColor, BF
					g.fillRect(scX(d95)-Math.abs(scX(d1)-scX(d95)), scY(xVDup), Math.abs(scX(d1)-scX(d95)), Math.abs(scY(xVDdown)-scY(xVDup))+2);//, KickColor, BF  the highest right kicks in annulus
					xVDdown = xVDup;
					MainDriver.xVDupSI = xVDup; 
					return 0;		    		
				}

				voldown = volSum; 
				Htmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1]; 
				hhm = xLoc - MainDriver.TMD[i];
				Xup = xLoc - hhm;
				xVDup = utilityModule.getVD(Xup);

				g.setColor(MainDriver.KickColor);
				g.fillRect(scX(-d95), scY(xVDup), scX(-d1)-scX(-d95), scY(xVDdown)-scY(xVDup));//, KickColor, BF
				g.fillRect(scX(d95)-Math.abs(scX(d1)-scX(d95)), scY(xVDup), Math.abs(scX(d1)-scX(d95)), Math.abs(scY(xVDdown)-scY(xVDup)));//, KickColor, BF, the bottom right kicks in annulus
				xVDdown = xVDup;
			}

			if (MainDriver.iOnshore == 1){
				MainDriver.xVDupSI = xVDup;
				return 0;  // or Choke or Kill lines
			}

			double[] glEX = new double[5];
			glEX = utilityModule.getLines(Qflow);//, d2, d1, Qeff, capcty, volEff) // index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
			capcty = (double)glEX[1];
			d1=(double)glEX[3];
			d2=(double)glEX[4];	    

			volteff = volTotal - volSum;
			kickbot = Math.min(xLoc, MainDriver.Dwater);
			kickVD = kickbot - volteff / capcty;
			if (kickVD < 0) kickVD = 0.05;

			double rleft=0, rright=0;
			g.setColor(MainDriver.KickColor);
			MainDriver.xVDupSI = kickVD;

			if (MainDriver.iChoke == 1) {
				rleft = MainDriver.Driser * 1.02; 
				rright = (0.95 * MainDriver.Dchoke + MainDriver.Driser);
				g.fillRect (scX(rleft), scY(kickVD), scX(rright)-scX(rleft), scY(kickbot)-scY(kickVD));//, KickColor, BF
			}
			if (MainDriver.iKill == 1){
				rleft = -MainDriver.Driser * 1.02; 
				rright = -(0.97 * MainDriver.Dkill + MainDriver.Driser);
				g.fillRect (scX(rleft)-Math.abs(scX(rright)-scX(rleft)), scY(kickVD), Math.abs(scX(rright)-scX(rleft)), scY(kickbot)-scY(kickVD));//, KickColor, BF
			}
			if (MainDriver.iBOP == 0) closeBop(g);

			return 0;		  
		}

		void showKick(Graphics g){
			super.paintComponents(g);
			int dummy=0;
			if (simdis.PVgain > 0.001){
				double volMix = simdis.QtotVol;
				DrawOMud(g);				
				double Kstart = simdis.holeMD - simdis.dxSlip;
				QtotMixWP = simdis.QtotVol;
				if (simdis.nHorizon == 2) {
					QtotMixWP = simdis.QtotVol - simdis.v2Hold;
					Kstart = simdis.holeMD - MainDriver.x2Hold - simdis.dxSlip;
				}
				dummy=DrawKick(QtotMixWP, Kstart, g);
				drawWellbore(g);
			}
		}

		void drawConductor(Graphics g){
			super.paintComponents(g);
			//draw the wellbore profile based on actual geometry
			//draw conductor or surface casing
			Graphics2D g2d = (Graphics2D)g;
			g2d.setStroke(new BasicStroke(6)); //g.gdrawwidth = 4; 
			double ohwidth = 2.5, dicd=0, docd=0, delta=0;

			if (MainDriver.iCduct == 1){
				dicd = MainDriver.ODcduct; 
				docd = dicd + ohwidth;
				delta = MainDriver.DepthCduct / 15;
				g.drawLine(scX(dicd), scY(MainDriver.Dwater), scX(dicd), scY(MainDriver.DepthCduct));
				g.drawLine (scX(dicd), scY(MainDriver.DepthCduct-delta), scX(docd), scY(MainDriver.DepthCduct));
				g.drawLine (scX(dicd), scY(MainDriver.DepthCduct), scX(docd), scY(MainDriver.DepthCduct));
				g.drawLine (scX(-dicd), scY(MainDriver.Dwater),scX(-dicd), scY(MainDriver.DepthCduct));	
				g.drawLine (scX(-dicd), scY(MainDriver.DepthCduct-delta), scX(-docd), scY(MainDriver.DepthCduct));
				g.drawLine (scX(-dicd), scY(MainDriver.DepthCduct), scX(-docd), scY(MainDriver.DepthCduct));
			}
			if (MainDriver.iSurfCsg == 1){
				dicd = MainDriver.ODsurfCsg; 
				docd = dicd + ohwidth;
				delta = MainDriver.DepthSurfCsg / 20;
				g.drawLine (scX(dicd), scY(MainDriver.Dwater), scX(dicd), scY(MainDriver.DepthSurfCsg));
				g.drawLine (scX(dicd), scY(MainDriver.DepthSurfCsg-delta), scX(docd), scY(MainDriver.DepthSurfCsg));
				g.drawLine (scX(dicd), scY(MainDriver.DepthSurfCsg), scX(docd), scY(MainDriver.DepthSurfCsg));
				g.drawLine (scX(-dicd), scY(MainDriver.Dwater), scX(-dicd), scY(MainDriver.DepthSurfCsg));
				g.drawLine (scX(-dicd), scY(MainDriver.DepthSurfCsg-delta), scX(-docd), scY(MainDriver.DepthSurfCsg));
				g.drawLine (scX(-dicd), scY(MainDriver.DepthSurfCsg), scX(-docd), scY(MainDriver. DepthSurfCsg));
			}
			//
		}

		void closeBop(Graphics g){
			super.paintComponents(g);
			Graphics2D g2d = (Graphics2D)g;			
			double yytop = -0.4 * MainDriver.Vdepth / 20;

			drawwidth = 1;
			g2d.setStroke(new BasicStroke(drawwidth));
			Color bopColor = MainDriver.Blue; //QBColor(2) // &HFF0000     //MainDriver.Blue(1) or green(2) color
			g.setColor(bopColor);
			if(MainDriver.iOnshore == 1){
				g.fillRect (Math.abs(scX(-MainDriver.IDcasing)), scY(yytop), Math.abs(scX(-MainDriver.doDP)-scX(-MainDriver.IDcasing)), scY(yytop * 0.05)-scY(yytop));
				g.fillRect (scX(MainDriver.doDP), scY(yytop), scX(MainDriver.IDcasing)-scX(MainDriver.doDP), scY(yytop * 0.05)-scY(yytop));
			}
			else{
				//double xtopbop = MainDriver.Dwater + yytop;
				double xtopbop = MainDriver.Dwater*0.95;
				if (xtopbop < 0) xtopbop = 0;
				g.fillRect (scX(-MainDriver.Driser), scY(xtopbop), scX(-MainDriver.doDP)-scX(-MainDriver.Driser), scY(MainDriver.Dwater * 0.99)-scY(xtopbop));
				g.fillRect (scX(MainDriver.doDP), scY(xtopbop), scX(MainDriver.Driser)-scX(MainDriver.doDP), scY(MainDriver.Dwater * 0.99)-scY(xtopbop));
				g.fillRect (scX(MainDriver.Driser), scY(yytop), scX(MainDriver.Driser+MainDriver.Dchoke)-scX(MainDriver.Driser), scY(yytop * 0.05)-scY(yytop));
				g.fillRect (scX(-MainDriver.Driser)-Math.abs(scX(-MainDriver.Driser-MainDriver.Dkill)-scX(-MainDriver.Driser)), scY(yytop), Math.abs(scX(-MainDriver.Driser-MainDriver.Dkill)-scX(-MainDriver.Driser)), scY(yytop * 0.05)-scY(yytop));
			}
		}		

		void drawWellbore(Graphics g){
			super.paintComponents(g);
			Graphics2D g2d = (Graphics2D)g;
			//------------- draw the wellbore profile based on actual geometry
			drawwidth = 5;
			g2d.setStroke(new BasicStroke(drawwidth));

			double ohwidth = 2.5; 
			double docs = MainDriver.IDcasing + ohwidth;
			double dcsetvd = MainDriver.TVD[MainDriver.iCsg];
			double delta = dcsetvd / 50;
			//
			g.setColor(Color.BLACK);
			g.drawLine (scX(MainDriver.IDcasing), scY(MainDriver.Dwater),scX(MainDriver.IDcasing), scY(dcsetvd)); //body
			g.drawLine (scX(-MainDriver.IDcasing), scY(MainDriver.Dwater),scX(-MainDriver.IDcasing), scY(dcsetvd));
			g.drawLine (scX(MainDriver.DiaHole), scY(dcsetvd),scX(MainDriver.DiaHole), scY(MainDriver.Vdepth));
			g.drawLine (scX(-MainDriver.DiaHole), scY(dcsetvd),scX(-MainDriver.DiaHole), scY(MainDriver.Vdepth));

			drawwidth = 4;
			g2d.setStroke(new BasicStroke(drawwidth));
			g.drawLine (scX(MainDriver.IDcasing), scY(dcsetvd - delta), scX(docs), scY(dcsetvd));
			g.drawLine (scX(docs), scY(dcsetvd),scX(MainDriver.DiaHole), scY(dcsetvd));
			g.drawLine (scX(-docs), scY(dcsetvd), scX(-MainDriver.IDcasing), scY(dcsetvd - delta));
			g.drawLine (scX(-docs), scY(dcsetvd),scX(-MainDriver.DiaHole), scY(dcsetvd));
			//g.drawLine (scX(-dicd), scY(MainDriver.DepthCduct-delta), scX(-docd), scY(MainDriver.DepthCduct));
			drawwidth = 3; //; dscolor = QBColor(0)
			g2d.setStroke(new BasicStroke(drawwidth));
			double vdpipe = MainDriver.TVD[MainDriver.iHWDP];
			double vdhwdp = MainDriver.TVD[MainDriver.iDC];
			//
			g.fillRect (scX(MainDriver.diDP), scY(0), scX(MainDriver.doDP)-scX(MainDriver.diDP), scY(vdpipe)-scY(0));        // draw DP
			g.fillRect (scX(-MainDriver.doDP), scY(0), scX(-MainDriver.diDP)-scX(-MainDriver.doDP), scY(vdpipe)-scY(0));
			g.fillRect (scX(MainDriver.diHWDP), scY(vdpipe), scX(MainDriver.doHWDP)-scX(MainDriver.diHWDP), scY(vdhwdp)-scY(vdpipe));  // draw HWDP
			g.fillRect (scX(-MainDriver.doHWDP), scY(vdpipe), scX(-MainDriver.diHWDP)-scX(-MainDriver.doHWDP), scY(vdhwdp)-scY(vdpipe));
			g.fillRect (scX(MainDriver.diDC), scY(vdhwdp), scX(MainDriver.doDC)-scX(MainDriver.diDC), scY(MainDriver.Vdepth)-scY(vdhwdp));      // draw DC
			g.fillRect (scX(-MainDriver.doDC), scY(vdhwdp), scX(-MainDriver.diDC)-scX(-MainDriver.doDC), scY(MainDriver.Vdepth)-scY(vdhwdp));
			//--------- draw off-shore case; marine riser), kill(L) & choke(R) lines
			drawwidth = 4;
			g2d.setStroke(new BasicStroke(drawwidth));

			if (MainDriver.iOnshore == 2){
				g.drawLine (scX(MainDriver.Driser), scY(0), scX(MainDriver.Driser), scY(MainDriver.Dwater));
				g.drawLine (scX(-MainDriver.Driser), scY(0), scX(-MainDriver.Driser), scY(MainDriver.Dwater));

				drawwidth = 3;
				g2d.setStroke(new BasicStroke(drawwidth));
				g.drawLine (scX(MainDriver.Driser + MainDriver.Dchoke), scY(0),scX(MainDriver.Driser + MainDriver.Dchoke), scY(MainDriver.Dwater));
				g.drawLine (scX(-MainDriver.Driser - MainDriver.Dkill), scY(0),scX(-MainDriver.Driser - MainDriver.Dkill), scY(MainDriver.Dwater));
			}
			//----------------- Draw the bit
			double btm = MainDriver.Vdepth * 1.003; 
			double diam = MainDriver.doDC + 0.5 * (MainDriver.DiaHole - MainDriver.doDC);
			drawwidth = 5;
			g2d.setStroke(new BasicStroke(drawwidth));
			g.drawLine (scX(-diam), scY(btm),scX(diam), scY(btm));
			//
		}

		void DrawOMud(Graphics g){
			super.paintComponents(g);
			Graphics2D g2d = (Graphics2D)g;
			//omcolor = &HC0C0C0     // light MainDriver.Gray color as old mud
			Color omColor = MainDriver.MudColor;
			g.setColor(omColor);		 
			drawwidth = 1;
			g2d.setStroke(new BasicStroke(drawwidth));
			double depcsg = MainDriver.TVD[MainDriver.iCsg];   // depcsg = MainDriver.DepthCasing 

			g.fillRect (scX(-MainDriver.IDcasing), scY(MainDriver.Dwater), scX(MainDriver.IDcasing)-scX(-MainDriver.IDcasing), scY(depcsg)-scY(MainDriver.Dwater));
			g.fillRect (scX(-MainDriver.DiaHole), scY(depcsg), scX(MainDriver.DiaHole)-scX(-MainDriver.DiaHole), scY(MainDriver.Vdepth)-scY(depcsg));
			if (MainDriver.iOnshore == 2){
				double rleft = -(0.9 * MainDriver.Dkill + MainDriver.Driser);
				double rright = (0.9 * MainDriver.Dchoke + MainDriver.Driser);
				g.fillRect(scX(rleft), scY(MainDriver.Dwater * 0.01), scX(rright)-scX(rleft)+1, scY(MainDriver.Dwater)-scY(MainDriver.Dwater * 0.01));
			}

			//--------------------- Draw the surface line and sea floor lines if any.
			//xx1 = 1.2 * (-1 - MainDriver.ODcduct); xx2 = -xx1 //XX1는 일단 forM LOAD에 지정해두었음... 나머지는 X1으로 수정
			double x1 = 1.2 * (-1 - MainDriver.ODcduct);
			double x2 = -x1;
			drawwidth = 5;
			g2d.setStroke(new BasicStroke(drawwidth));
			g.setColor(Color.black);
			g.drawLine (scX(x1), scY(0), scX(x2), scY(0));

			drawwidth = 5;
			g2d.setStroke(new BasicStroke(drawwidth));

			g.drawLine (scX(x1), scY(MainDriver.Dwater), scX(-MainDriver.doDP), scY(MainDriver.Dwater));
			g.drawLine (scX(x2), scY(MainDriver.Dwater), scX(MainDriver.doDP), scY(MainDriver.Dwater));
			//------------------------------ Draw BOP if BOP is closed
			if (MainDriver.iBOP == 0) closeBop(g);

			//
		}

		int DrawKmud(Graphics g){
			super.paintComponents(g);
			Graphics2D g2d = (Graphics2D)g;
			int iExit=0;
			double[] getLinesEX= new double[5];
			double volconn=0, volSum=0, voltop=0, d1=0, d2=0, capcty=0, killtop=0, killbot=0, killmd=0, killvd=0, Qflow=0, volkmeff=0, volEff=0, rleft=0, rright=0;
			volconn = MainDriver.volDS[MainDriver.NwcE] + MainDriver.volDS[MainDriver.NwcE + 1];
			if (volkillmud < volconn + 0.05) {
				return 0;
			}		   
			//		    kcolor = &H808080    //dark gray as a kill mud color
			Color kColor = MainDriver.KillColor;
			drawwidth = 1;
			g2d.setStroke(new BasicStroke(drawwidth));
			iExit = 0;
			volSum = volconn; 
			voltop = volconn;
			for(int ik = MainDriver.NwcE-1; ik>(MainDriver.NwcS - 1); ik--){       // inside Drill String
				volSum = volSum + MainDriver.volDS[ik];
				if (volSum > volkillmud) {
					d2 = MainDriver.DiDS[ik];
					capcty = MainDriver.C12 * d2 * d2;
					killtop = MainDriver.TVD[ik];     //depth increased
					killmd = MainDriver.TMD[ik] + (volkillmud - voltop) / capcty;
					killvd=utilityModule.getVD(killmd);

					g.setColor(kColor);
					g.fillRect(scX(-d2), scY(killtop), scX(d2)-scX(-d2), scY(killvd)-scY(killtop));
					iExit = 1;
					break;
				}
				d2 = MainDriver.DiDS[ik];
				killtop = MainDriver.TVD[ik];
				killvd = MainDriver.TVD[ik - 1];
				g.setColor(kColor);
				g.fillRect(scX(-d2), scY(killtop), scX(d2)-scX(-d2), scY(killvd)-scY(killtop));
				voltop = volSum;
			}
			if (iExit == 1) return 0;
			for(int ik = MainDriver.NwcS; ik<9; ik++){               // Annulus except choke line(s)
				volSum = volSum + MainDriver.VOLann[ik];
				if (volSum > volkillmud){
					d2 = MainDriver.Do2p[ik];
					d1 = MainDriver.Di2p[ik];
					capcty = MainDriver.C12 * (d2 * d2 - d1 * d1);
					killbot = MainDriver.TVD[ik - 1];   //depth decreased
					killmd = MainDriver.TMD[ik - 1] - (volkillmud - voltop) / capcty;
					killvd = utilityModule.getVD(killmd);
					g.setColor(kColor);
					g.fillRect(scX(-d2), scY(killbot)-Math.abs(scY(killvd)-scY(killbot)), Math.abs(scX(-d1)-scX(-d2)), Math.abs(scY(killvd)-scY(killbot)));
					g.fillRect(scX(d2)-Math.abs(scX(d1)-scX(d2)), scY(killbot)-Math.abs(scY(killvd)-scY(killbot)), Math.abs(scX(d1)-scX(d2)), Math.abs(scY(killvd)-scY(killbot)));
					//g.fillRect(scX(-d2), scY(killbot), scX(-d1)-scX(-d2), scY(killvd)-scY(killbot));
					//g.fillRect(scX(d2), scY(killbot), scX(d1)-scX(d2), scY(killvd)-scY(killbot));
					iExit = 1; 
					break;
				}
				d2 = MainDriver.Do2p[ik];
				d1 = MainDriver.Di2p[ik];
				killbot = MainDriver.TVD[ik - 1];
				killvd = MainDriver.TVD[ik];
				g.setColor(kColor);
				g.fillRect(scX(-d2), scY(killbot)-Math.abs(scY(killvd)-scY(killbot)), Math.abs(scX(-d1)-scX(-d2)), Math.abs(scY(killvd)-scY(killbot)));
				g.fillRect(scX(d2)-Math.abs(scX(d1)-scX(d2)), scY(killbot)-Math.abs(scY(killvd)-scY(killbot)), Math.abs(scX(d1)-scX(d2)), Math.abs(scY(killvd)-scY(killbot)));
				//g.fillRect(scX(-d2), scY(killbot), scX(-d1)-scX(-d2), scY(killvd)-scY(killbot));
				// g.fillRect(scX(d2), scY(killbot), scX(d1)-scX(d2), scY(killvd)-scY(killbot));
				voltop = volSum;
			}
			if (iExit == 1) return 0;
			if (MainDriver.iOnshore == 1) return 0;  // or Choke or Kill lines
			getLinesEX = utilityModule.getLines(Qflow);//, d2, d1, Qeff, capcty, volEff) //index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
			volEff=getLinesEX[2];
			capcty=getLinesEX[1];
			volkmeff = volkillmud - volSum;
			if (volkmeff > volEff) killvd = 0;
			else killvd = MainDriver.Dwater - volkmeff / capcty;

			g.setColor(kColor);
			if (MainDriver.iChoke == 1){
				rleft = MainDriver.Driser * 1.02; 
				rright = (0.97 * MainDriver.Dchoke + MainDriver.Driser);
				g.fillRect(scX(rleft), scY(killvd), scX(rright)-scX(rleft), scY(MainDriver.Dwater)-scY(killvd));
			}
			if (MainDriver.iKill == 1){
				rleft = -MainDriver.Driser * 1.02; 
				rright = -(0.97 * MainDriver.Dkill + MainDriver.Driser);
				g.fillRect(scX(rright), scY(killvd), scX(rleft)-scX(rright), scY(MainDriver.Dwater)- scY(killvd));
			}
			return 1;
			//
		}

		/*public void TRdraw(Graphics g){
		super.paintComponents(g);
		//draw the wellbore profile based on actual geometry
		//draw conductor or surface casing
		Graphics2D g2d = (Graphics2D)g;
		drawwidth = 1;
		g2d.setStroke(new BasicStroke(drawwidth)); //g.gdrawwidth = 1; 
		double ohwidth = 2.5, dicd=0, docd=0, delta=0;
	    double DrotTab = 0.5 * (MainDriver.doDP + MainDriver.IDcasing);
	    double HrotTab = -MainDriver.Vdepth / 40;
	    int iLocation=0;
		double Xconst=0;



	    g.drawRect(scX(-DrotTab), scY(HrotTab), scX(DrotTab)-scX(-DrotTab), scY(0)-scY(HrotTab));//, Black, BF
	    System.out.println(DrotTab+" "+HrotTab);
	    iRotary = iRotary + 1;
	    iLocation = iRotary % 6;
	    if(iLocation == 1) Xconst = 0.75 * DrotTab;
	    else if(iLocation == 2) Xconst = 0;
	    else if(iLocation == 3) Xconst = -0.75 * DrotTab;

	    drawwidth = 3;
	    g2d.setStroke(new BasicStroke(drawwidth));
	    if (iLocation >= 1 && iLocation <= 3){
	    	g.setColor(Color.WHITE);
	    	g.drawLine (scX(Xconst), scY(HrotTab * 0.9), scX(Xconst), scY(HrotTab * 0.05));
	    //g2d.setStroke(new BasicStroke(drawwidth));	   
	    }
	//
	    double x1 = 1.2 * (-1 - MainDriver.ODcduct);
	    double x2 = -x1;	    
	    g.setColor(Color.BLACK);	    
	    g.drawLine(scX(x1), scY(0), scX(x2), scY(0));
	    //xx1 = 1.2 * (-1 - ODcduct); xx2 = -xx1 <=일단은 forMLOAD에서 XX1, XX2만큼 좌표변환하고 모든걸 표현하고 있는중임...
	    //drawwidth = 3
	    //g.drawLine (xx1, 0,xx2, 0)


		//
		}



		 */
	}
}


