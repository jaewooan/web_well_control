package ML_ERD;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;


class MainModule {

	static void SetControl() {
		//..... set the control variables and determine minimum BHP as a well control guidelines
		//  modified for multilateral wells.  2003/7/24
		double DPmaxOver=0;   //maximum over pressure amoung exposed formations
		double DPdiff=0;
		double veow=0, heow=0, psia=14.7;
		double d1=0, d2=0, temp=0;		

		MainDriver.Pcon = 0;

		if(MainDriver.iDelp==1) MainDriver.Pcon = 1;
		if(MainDriver.iOnshore==1) MainDriver.Dwater = 0;
		if(MainDriver.Model==3){
			MainDriver.S600 = 2*MainDriver.S300;
			MainDriver.Model = 1;
		}


		//ideft2 = 1 // this is not necessary because SIDPP & SICP are not used
		//................. calculate liquid viscosity for 2-phase friction loss
		//................. BOP is open (1), because it//s continuous drilling
		//................. velocity of sound = 340 m/sec = 1117 ft/s
		//		                                = 4890 ft/sec in the water @ S.C.
		MainDriver.iBOP = 1;
		MainDriver.Vsound = 4890 * Math.sqrt(8.33 / MainDriver.oMud) * 0.5;  //use the half velocity
		MainDriver.visL = MainDriver.S600 - MainDriver.S300;
		if(MainDriver.Model==1) MainDriver.visL = MainDriver.S300;
		MainDriver.pN = Math.log10(MainDriver.S600 /MainDriver.S300) / Math.log10(2);   //pn & pk are required for all time
		MainDriver.pK = 510 * MainDriver.S300 / Math.pow(511,MainDriver.pN);
		if (MainDriver.Model==1) MainDriver.visL = MainDriver.S300;
		setMDvd();
		veow = MainDriver.Vdepth;//?
		heow = MainDriver.Hdisp; //?
		psia =  14.7;//?

		//............ calculate area(sq in), volume of DS and annulus (bbls)
		//		         use the general approach:nwcs-starting , nwce-ending 
		MainDriver.VOLinn = 0;
		MainDriver.VOLout = 0;
		MainDriver.iChoke = 1;   //set the temporary value

		for(int i=MainDriver.NwcS; i<MainDriver.NwcE; i++){ //연구노트 참고... 이상함..ㅜㅜ
			d2 = MainDriver.Do2p[i];
			d1 = MainDriver.Di2p[i];
			temp =d2 * d2 - d1 * d1;
			MainDriver.VOLann[i] = MainDriver.C12 * temp * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
			d2 = MainDriver.DiDS[i];
			MainDriver.volDS[i] = MainDriver.C12 * d2 * d2 * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
			MainDriver.VOLinn = MainDriver.VOLinn + MainDriver.volDS[i];
			MainDriver.VOLout = MainDriver.VOLout + MainDriver.VOLann[i];
		}
		//............................ calculate surface connections
		//nwce+2 =   12: surface line from pump to stand pipe
		//nwce+1 =   11: stand pipe, rotary hose, swivel, kelly for DP

		temp = MainDriver.DiaSP * MainDriver.DiaSP * MainDriver.LengthSP + MainDriver.DiaHose*MainDriver.DiaHose * MainDriver.LengthHose + MainDriver.DiaSwivel*MainDriver.DiaSwivel * MainDriver.LengthSwivel + MainDriver.DiaKelly*MainDriver.DiaKelly * MainDriver.LengthKelly;
		MainDriver.volDS[MainDriver.NwcE] = MainDriver.C12 * temp;
		MainDriver.volDS[MainDriver.NwcE+1] = MainDriver.C12 * MainDriver.DiaSurfLine * MainDriver.DiaSurfLine * MainDriver.LengthSurfLine;
		MainDriver.VOLinn = MainDriver.VOLinn + MainDriver.volDS[MainDriver.NwcE] + MainDriver.volDS[MainDriver.NwcE+1];
		//------------------ Initial Calculation and Assignment are Conpleted
		//........................ calculate P_formation, SIDPP, and Kill mud

		MainDriver.overP = 0.052 * MainDriver.KICKintens * MainDriver.Vdepth;
		MainDriver.Pform = 0.052 * MainDriver.oMud * MainDriver.Vdepth + MainDriver.overP + psia;
		MainDriver.Pb = MainDriver.Pform;
		MainDriver.Kmud = MainDriver.oMud + MainDriver.KICKintens;

		if(MainDriver.igERD==1 && MainDriver.Kmud < MainDriver.oMud) MainDriver.Kmud = MainDriver.oMud ;    //for general practice

		MainDriver.SIDPP = MainDriver.overP + psia;
		MainDriver.gMudOld = 0.052 * MainDriver.oMud;
		MainDriver.gMudKill = 0.052 * MainDriver.Kmud;
		//..................... assign circulating variables depending on method
		//		                  (1:Driller//s method  2:Engineer//s method)
		MainDriver.gMudCirc = MainDriver.gMudKill;

		if(MainDriver.Method==1) MainDriver.gMudCirc = MainDriver.gMudOld;
		MainDriver.Cmud = MainDriver.gMudCirc / 0.052;
		MainDriver.iWshow = 0;
		MainDriver.volMix = 0;
		MainDriver.Xb = 0;
		//
		//-------------------8/01/02 - calculation of pore and fracture pressure
		if(MainDriver.iFG==1||MainDriver.iFG==2){
			//Eaton//s methid (1)
			//	        //John Barker//s method (2)
			MainDriver.iFGnum = 11;
			inputData.calcPoreFracture();   //call sub in a form: 1/24/03
			//
			//Else               //user input: we have all data already !
		}
		if(MainDriver.iProblem[3]==1){
			double mdBitOff = 0;
			double AeobRad = 0;
			double vdBit = 0;
			double hdBit = 0;
			double[] result = new double[2];

			mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;     //5 minutes to get TD	
			AeobRad = MainDriver.ang2EOB * MainDriver.radConv;  //later use 2.5 ft or 5 mins
			vdBit = MainDriver.Vdepth - mdBitOff * Math.cos(AeobRad);
			hdBit = MainDriver.Hdisp - mdBitOff * Math.sin(AeobRad);   
			result = propertyModule.calcPoreFrac(vdBit);
			MainDriver.PoreP_Base = result[0];
			MainDriver.FracP_Base = result[1];
			MainDriver.PoreP_now = result[0];
			MainDriver.FracP_now = result[1];
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;

			MainDriver.Vsound = 4890 * Math.sqrt(8.33 / MainDriver.oMud_save) * 0.5;  //use the half velocity
			MainDriver.Pform = 0.052 * MainDriver.oMud_save * MainDriver.Vdepth + MainDriver.overP + psia;
			MainDriver.Kmud = MainDriver.oMud_save + MainDriver.KICKintens;

			MainDriver.V_ann_cutting_tot=0;
			for(int ii=MainDriver.NwcS; ii<MainDriver.NwcE; ii++){ // 150127 AJW
				MainDriver.f_cutting[ii] = MainDriver.fc_Base;
				if(ii>MainDriver.NwcS){
					MainDriver.V_ann_cutting[ii] = MainDriver.VOLann[ii];
					MainDriver.TMD_cutting[ii] = MainDriver.TMD[ii];
					MainDriver.TVD_cutting[ii] = utilityModule.getVD(MainDriver.TMD_cutting[ii]);
					MainDriver.Area_ann[ii] = MainDriver.V_ann_cutting[ii]*5.6145/(MainDriver.TMD_cutting[ii-1]-MainDriver.TMD_cutting[ii]);
				}
				else{
					MainDriver.TMD_cutting[ii] = MainDriver.TMD[ii];
					MainDriver.TVD_cutting[ii] = utilityModule.getVD(MainDriver.TMD_cutting[ii]);
					MainDriver.TMD_cutting[ii-1] = MainDriver.TMD[ii-1]-mdBitOff;
					MainDriver.TVD_cutting[ii-1] = utilityModule.getVD(MainDriver.TMD_cutting[ii-1]);
					MainDriver.V_ann_cutting[ii] = MainDriver.VOLann[ii]*(MainDriver.TMD_cutting[ii-1]-MainDriver.TMD_cutting[ii])/(MainDriver.TMD[ii-1]-MainDriver.TMD[ii]); //bbl
					MainDriver.Area_ann[ii] = MainDriver.V_ann_cutting[ii]*5.6145/(MainDriver.TMD_cutting[ii-1]-MainDriver.TMD_cutting[ii]); // ft2
				}
				MainDriver.V_ann_cutting_tot = MainDriver.V_ann_cutting_tot + MainDriver.V_ann_cutting[ii];
				MainDriver.f_cutting[ii] = MainDriver.fc_now;
				MainDriver.V_cutting[ii] = MainDriver.V_ann_cutting[ii]*MainDriver.f_cutting[ii];
				MainDriver.oMudCutting[ii] = MainDriver.f_cutting[ii]*MainDriver.dens_cutting + (1-MainDriver.f_cutting[ii])*MainDriver.oMud_save; 
				MainDriver.V_Mud[ii] = MainDriver.V_ann_cutting[ii]*(1-MainDriver.f_cutting[ii]);
				MainDriver.V_Cut_mix[ii] = MainDriver.V_cell_cut[ii] + MainDriver.V_Mud[ii];
			}

			MainDriver.overP = 0.052 * MainDriver.KICKintens * MainDriver.Vdepth;
			MainDriver.Pform = 0.052 * MainDriver.oMud_save * MainDriver.Vdepth + MainDriver.overP + psia;
			MainDriver.Pb = MainDriver.Pform;
			MainDriver.Kmud = MainDriver.oMud_save + MainDriver.KICKintens;

			if(MainDriver.igERD==1 && MainDriver.Kmud < MainDriver.oMud) MainDriver.Kmud = MainDriver.oMud ;    //for general practice

			MainDriver.SIDPP = MainDriver.overP + psia;
			MainDriver.gMudOld = 0.052 * MainDriver.oMud;
			MainDriver.gMudKill = 0.052 * MainDriver.Kmud;
			//..................... assign circulating variables depending on method
			//			                  (1:Driller//s method  2:Engineer//s method)
			MainDriver.gMudCirc = MainDriver.gMudKill;

			if(MainDriver.Method==1) MainDriver.gMudCirc = MainDriver.gMudOld;
			MainDriver.Cmud = MainDriver.gMudCirc / 0.052;
			MainDriver.iWshow = 0;
			MainDriver.volMix = 0;
			MainDriver.Xb = 0;
			MainDriver.numCutting = 0;
		}
	}

	/*static void setMDvd_cutting(double mdBit){
		//..................... calculate total MD and VD then assign in the array
		//need to assign TMD, TVD, Angle, OD, ID
		//calculate wellbore path based on input data (Nov.2, OGJ, 1992)
		//This is good for from vertical to horizontal (0.0 to 90.0)
		//
		int i , i2, i3;
		double delAng, aAng;
		double angRad, ang2rad;
		double xdssum=0, delTmp;
		double rr, xdx, xangl, temp;
		//

		int iBtm = 0;

		MainDriver.NwcS_cutting = utilityModule.Xposition(mdBit)+1;//속해있는 cell의 하부. 이 값이 새로운 지침표로 작용한다. 2는 idc, ihwdp위해
		MainDriver.NwcE_cutting = MainDriver.NwcE;

		for(i=MainDriver.NwcS-1; i<=MainDriver.NwcE-1; i++){
			MainDriver.Do2p_cut[i] = MainDriver.Do2p[i];
			MainDriver.Di2p_cut[i]= MainDriver.Di2p[i];
			MainDriver.TMD_cutting[i] = MainDriver.TMD[i];
			MainDriver.TVD_cutting[i] = MainDriver.TVD[i];
			MainDriver.ang2p_cutting[i] = MainDriver.ang2p[i];			
		}
		MainDriver.iHWDP_cutting = MainDriver.iHWDP;
		MainDriver.iDC_cutting = MainDriver.iDC;
		MainDriver.iCsg_cutting = MainDriver.iCsg;			

		MainDriver.TMD_cutting[MainDriver.NwcS_cutting-1] = mdBit;
		MainDriver.TVD_cutting[MainDriver.NwcS_cutting-1] = MainDriver.vdbit_now;

		for(i=MainDriver.NwcS_cutting-2; i>=MainDriver.NwcS-1; i--){
			MainDriver.Do2p_cut[i] = 0;
			MainDriver.Di2p_cut[i]= 0;
			MainDriver.TMD_cutting[i] = 0;
			MainDriver.TVD_cutting[i] = 0;
			MainDriver.ang2p_cutting[i] = 0;
		}

		if(MainDriver.TMD[MainDriver.iDC]<=mdBit){
			MainDriver.NwcS_cutting = MainDriver.NwcS_cutting+1;
			MainDriver.Do2p_cut[MainDriver.NwcS_cutting-1] = MainDriver.Do2p_cut[MainDriver.NwcS_cutting-2]; MainDriver.Do2p_cut[MainDriver.NwcS_cutting-2]=0;
			MainDriver.Di2p_cut[MainDriver.NwcS_cutting-1]= MainDriver.Di2p_cut[MainDriver.NwcS_cutting-2]; MainDriver.Di2p_cut[MainDriver.NwcS_cutting-2]=0;
			MainDriver.TMD_cutting[MainDriver.NwcS_cutting-1] = MainDriver.TMD_cutting[MainDriver.NwcS_cutting-2]; MainDriver.TMD_cutting[MainDriver.NwcS_cutting-2]=0;
			MainDriver.TVD_cutting[MainDriver.NwcS_cutting-1] = MainDriver.TVD_cutting[MainDriver.NwcS_cutting-2]; MainDriver.TVD_cutting[MainDriver.NwcS_cutting-2]=0;
			MainDriver.ang2p_cutting[MainDriver.NwcS_cutting-1] = MainDriver.ang2p_cutting[MainDriver.NwcS_cutting-2]; MainDriver.ang2p_cutting[MainDriver.NwcS_cutting-2]=0;
		}
		if(MainDriver.TMD[MainDriver.iHWDP]<=mdBit){
			MainDriver.NwcS_cutting = MainDriver.NwcS_cutting+1;
			MainDriver.Do2p_cut[MainDriver.NwcS_cutting-1] = MainDriver.Do2p_cut[MainDriver.NwcS_cutting-2]; MainDriver.Do2p_cut[MainDriver.NwcS_cutting-2]=0;
			MainDriver.Di2p_cut[MainDriver.NwcS_cutting-1]= MainDriver.Di2p_cut[MainDriver.NwcS_cutting-2]; MainDriver.Di2p_cut[MainDriver.NwcS_cutting-2]=0;
			MainDriver.TMD_cutting[MainDriver.NwcS_cutting-1] = MainDriver.TMD_cutting[MainDriver.NwcS_cutting-2]; MainDriver.TMD_cutting[MainDriver.NwcS_cutting-2]=0;
			MainDriver.TVD_cutting[MainDriver.NwcS_cutting-1] = MainDriver.TVD_cutting[MainDriver.NwcS_cutting-2]; MainDriver.TVD_cutting[MainDriver.NwcS_cutting-2]=0;
			MainDriver.ang2p_cutting[MainDriver.NwcS_cutting-1] = MainDriver.ang2p_cutting[MainDriver.NwcS_cutting-2]; MainDriver.ang2p_cutting[MainDriver.NwcS_cutting-2]=0;
		}

		//.................................. find the top of DC
		xdssum = 0;  //: i2start = nwcs + 3
		for(i2 = (MainDriver.NwcS_cutting-1); i2<MainDriver.NwcE_cutting-1; i2++){
			xdssum = xdssum + MainDriver.TMD_cutting[i2] - MainDriver.TMD_cutting[i2 + 1];
			MainDriver.iDC_cutting = i2;

			if(xdssum > MainDriver.LengthDC) break;
		}
		MainDriver.NwcS_cutting = MainDriver.NwcS_cutting-1;
		for(i3 = MainDriver.NwcS_cutting-1; i3<MainDriver.iDC_cutting; i3++){ //나중에 다시 확인	
			MainDriver.TMD_cutting[i3] = MainDriver.TMD_cutting[i3 + 1];
			MainDriver.TVD_cutting[i3] = MainDriver.TVD_cutting[i3 + 1];
			MainDriver.Di2p_cut[i3] = MainDriver.doDC;
			MainDriver.Do2p_cut[i3] = MainDriver.Do2p_cut[i3 + 1];
			MainDriver.ang2p_cutting[i3] = MainDriver.ang2p_cutting[i3 + 1];
		}

		//......................... calculate information about DC

		MainDriver.TMD_cutting[MainDriver.iDC_cutting] = mdBit - MainDriver.LengthDC;
		MainDriver.TVD_cutting[MainDriver.iDC_cutting] = utilityModule.getVD(MainDriver.TMD_cutting[MainDriver.iDC_cutting]);
		MainDriver.Di2p_cut[MainDriver.iDC_cutting] = MainDriver.doDC;
		if(MainDriver.TMD_cutting[MainDriver.iDC_cutting]<MainDriver.DepthCasing){
			MainDriver.Do2p_cut[MainDriver.iDC_cutting] = MainDriver.IDcasing;
			MainDriver.iCsg_cutting = MainDriver.iDC_cutting - 1;
		}
		else MainDriver.Do2p_cut[MainDriver.iDC_cutting] = MainDriver.DiaHole;

		delAng = MainDriver.ang2p[MainDriver.iDC_cutting-1 ] - MainDriver.ang2p[MainDriver.iDC_cutting + 1];
		aAng = Math.abs(delAng);
		if(aAng < 0.1) MainDriver.ang2p_cutting[MainDriver.iDC_cutting] = MainDriver.ang2p_cutting[MainDriver.iDC_cutting + 1];
		else{
			xdx = MainDriver.TMD_cutting[MainDriver.iDC_cutting - 1] - MainDriver.TMD_cutting[MainDriver.iDC_cutting + 1];
			delTmp = xdssum - MainDriver.LengthDC;
			MainDriver.ang2p_cutting[MainDriver.iDC_cutting] = MainDriver.ang2p_cutting[MainDriver.iDC_cutting + 1] + delAng * delTmp / xdx;
		}


		//..................................... find the top of HWDP

		xdssum = 0;  //: i2start = nwcs + 3
		for(i2 = (MainDriver.NwcS_cutting-1); i2<MainDriver.NwcE_cutting-1; i2++){
			xdssum = xdssum + MainDriver.TMD_cutting[i2] - MainDriver.TMD_cutting[i2 + 1];
			MainDriver.iHWDP_cutting = i2;

			if(xdssum > MainDriver.LengthHWDP + MainDriver.LengthDC) break;
		}
		MainDriver.NwcS_cutting = MainDriver.NwcS_cutting-1;
		MainDriver.iDC_cutting = MainDriver.iDC_cutting - 1;

		for(i3 = MainDriver.NwcS_cutting-1; i3<MainDriver.iHWDP_cutting; i3++){ //나중에 다시 확인	
			MainDriver.TMD_cutting[i3] = MainDriver.TMD_cutting[i3 + 1];
			MainDriver.TVD_cutting[i3] = MainDriver.TVD_cutting[i3 + 1];
			if(i3<=MainDriver.iDC_cutting) MainDriver.Di2p_cut[i3] = MainDriver.doDC;
			else MainDriver.Di2p_cut[i3] = MainDriver.doHWDP;
			if(MainDriver.TMD_cutting[MainDriver.iHWDP_cutting]<MainDriver.DepthCasing){
				MainDriver.Do2p_cut[MainDriver.iHWDP_cutting] = MainDriver.IDcasing;
				MainDriver.iCsg_cutting = MainDriver.iHWDP_cutting - 1;
			}
			else MainDriver.Do2p_cut[MainDriver.iHWDP_cutting] = MainDriver.DiaHole;
			MainDriver.ang2p_cutting[i3] = MainDriver.ang2p_cutting[i3 + 1];
		}

		//......................... calculate information about HWDP

		MainDriver.TMD_cutting[MainDriver.iHWDP_cutting] = mdBit - MainDriver.LengthHWDP - MainDriver.LengthDC;
		MainDriver.TVD_cutting[MainDriver.iHWDP_cutting] = utilityModule.getVD(MainDriver.TMD_cutting[MainDriver.iHWDP_cutting]);
		MainDriver.Di2p_cut[MainDriver.iHWDP_cutting] = MainDriver.doHWDP;
		if(MainDriver.TMD_cutting[MainDriver.iHWDP_cutting]<MainDriver.DepthCasing){
			MainDriver.Do2p_cut[MainDriver.iHWDP_cutting] = MainDriver.IDcasing;
			MainDriver.iCsg_cutting = MainDriver.iHWDP_cutting - 1;
		}
		else MainDriver.Do2p_cut[MainDriver.iHWDP_cutting] = MainDriver.DiaHole;

		delAng = MainDriver.ang2p[MainDriver.iHWDP_cutting-1 ] - MainDriver.ang2p[MainDriver.iHWDP_cutting + 1];
		aAng = Math.abs(delAng);
		if(aAng < 0.1) MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting] = MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting + 1];
		else{
			xdx = MainDriver.TMD_cutting[MainDriver.iHWDP_cutting - 1] - MainDriver.TMD_cutting[MainDriver.iHWDP_cutting + 1];
			delTmp = xdssum - MainDriver.LengthHWDP-MainDriver.LengthDC;
			MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting] = MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting + 1] + delAng * delTmp / xdx;
		}

		for(i=MainDriver.NwcS_cutting; i<=MainDriver.NwcE_cutting-1; i++){
			MainDriver.Area_ann[i] = (Math.pow(MainDriver.Do2p_cut[i],2)-Math.pow(MainDriver.Di2p_cut[i], 2))*Math.PI/4/144; // ft2
			MainDriver.V_ann_cutting[i] = (Math.pow(MainDriver.Do2p_cut[i],2)-Math.pow(MainDriver.Di2p_cut[i], 2))/1029.4; //bbl/ft
			MainDriver.V_ann_cutting[i] = MainDriver.V_ann_cutting[i]*(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]);//bbl		
		}
	}*/

	static void setMDvd(){
		//..................... calculate total MD and VD then assign in the array
		//need to assign TMD, TVD, Angle, OD, ID
		//calculate wellbore path based on input data (Nov.2, OGJ, 1992)
		//This is good for from vertical to horizontal (0.0 to 90.0)
		//
		int i , i2, i3;
		double delAng, aAng;
		double angRad, ang2rad;
		double xdssum=0, delTmp;
		double rr, xdx, xangl, temp;
		//
		MainDriver.NwcE = 9;
		if(MainDriver.iOnshore==2) MainDriver.NwcE = 10;
		if(MainDriver.iWell!=4) MainDriver.x2Hold = 0;

		if(MainDriver.iWell==0){          // regular vertcal well
			MainDriver.NwcS = 5;
			MainDriver.Hdisp = 0;
			MainDriver.DepthKOP = MainDriver.Vdepth;
			MainDriver.BUR = 0;
			MainDriver.BUR2 = 0;
			MainDriver.angEOB = 0;
			MainDriver.ang2EOB = 0;
			MainDriver.Rbur = 0;
			MainDriver.R2bur = 0;
		}
		else if(MainDriver.iWell==1){      // continuous build (DD type 3)
			MainDriver.NwcS = 4;
			MainDriver.ang2EOB = MainDriver.angEOB;
			MainDriver.BUR2 = 0;
			MainDriver.xHold = 0;
			MainDriver.R2bur = 0;
		}
		else if(MainDriver.iWell==2){      // build-hold (DD type 1]
			MainDriver.NwcS = 3;
			MainDriver.BUR2 = 0;
			MainDriver.R2bur = 0;
			MainDriver.ang2EOB = MainDriver.angEOB;
		}
		else{                         // horizontal or near horizontal with 2 build
			MainDriver.NwcS = 1;
			if(MainDriver.iWell==3) MainDriver.NwcS = 2;
		}

		//......... calc total MD,VD(ft),angle from vert(degree),do,di(in) etc
		//		      ignore some variables initialized
		for(i=MainDriver.NwcS-1; i<MainDriver.NwcE; i++){
			MainDriver.Do2p[i] = MainDriver.DiaHole;
			MainDriver.Di2p[i]= MainDriver.doDP; 
			MainDriver.DiDS[i] = MainDriver.diDP;
		}

		MainDriver.Do2p[9] = MainDriver.Dchoke;
		MainDriver.Di2p[9] = 0;
		MainDriver.DiDS[9] = MainDriver.diDP;

		MainDriver.TMD[8] = MainDriver.Dwater;
		MainDriver.TVD[8] = MainDriver.Dwater;

		MainDriver.TMD[7] = MainDriver.DepthKOP; 
		MainDriver.TVD[7] = MainDriver.DepthKOP;
		//.................................... end of first build section
		angRad = MainDriver.angEOB * MainDriver.radConv;
		ang2rad = MainDriver.ang2EOB * MainDriver.radConv;

		if(MainDriver.iProblem[3] == 1){ // 150127 AJW
			MainDriver.ang2p[8] = 0;
			MainDriver.ang2p[7] = 0; 
		}
		MainDriver.TMD[6] = MainDriver.TMD[7] + MainDriver.Rbur * angRad;
		MainDriver.TVD[6] = MainDriver.TVD[7] + MainDriver.Rbur * Math.sin(angRad);
		MainDriver.ang2p[6] = MainDriver.angEOB;
		//.................................... end of tangent section
		MainDriver.TMD[5] = MainDriver.TMD[6] + MainDriver.xHold;
		MainDriver.TVD[5] = MainDriver.TVD[6] + MainDriver.xHold * Math.cos(angRad);
		MainDriver.ang2p[5] = MainDriver.angEOB;
		//..................................... end of second build
		MainDriver.TMD[4] = MainDriver.TMD[5] + MainDriver.R2bur * (ang2rad - angRad);
		MainDriver.TVD[4] = MainDriver.TVD[5] + MainDriver.R2bur * (Math.sin(ang2rad) - Math.sin(angRad));
		MainDriver.ang2p[4] = MainDriver.ang2EOB;
		//..................................... at the bit: bottom hole
		MainDriver.TMD[3] = MainDriver.TMD[4] + MainDriver.x2Hold; 
		MainDriver.TVD[3] = MainDriver.TVD[4] + MainDriver.x2Hold * Math.cos(ang2rad);
		MainDriver.ang2p[3] = MainDriver.ang2EOB;
		//  xldp = tmd(4) - LengthDC - LengthHWDP


		//.................................. find the top of heviwate drill pipe (HWDP)
		xdssum = 0;  //: i2start = nwcs + 3
		for(i2 = (MainDriver.NwcS + 2); i2<8; i2++){
			xdssum = xdssum + MainDriver.TMD[i2] - MainDriver.TMD[i2 + 1];
			MainDriver.iHWDP = i2;

			if(xdssum > (MainDriver.LengthHWDP + MainDriver.LengthDC)) break;
		}

		for(i3 = (MainDriver.NwcS + 1); i3<MainDriver.iHWDP; i3++){ //나중에 다시 확인	
			MainDriver.TMD[i3] = MainDriver.TMD[i3 + 1];
			MainDriver.TVD[i3] = MainDriver.TVD[i3 + 1];
			MainDriver.Di2p[i3] = MainDriver.doHWDP;
			MainDriver.DiDS[i3] = MainDriver.diHWDP;
			MainDriver.ang2p[i3] = MainDriver.ang2p[i3 + 1];
		}

		//......................... calculate information about HWDP

		delAng = MainDriver.ang2p[MainDriver.iHWDP-1 ] - MainDriver.ang2p[MainDriver.iHWDP + 1];
		aAng = Math.abs(delAng);
		delTmp = xdssum - MainDriver.LengthHWDP - MainDriver.LengthDC;
		MainDriver.Di2p[MainDriver.iHWDP] = MainDriver.doHWDP;
		MainDriver.DiDS[MainDriver.iHWDP] = MainDriver.diHWDP;
		if(aAng < 0.1){
			MainDriver.ang2p[MainDriver.iHWDP] = MainDriver.ang2p[MainDriver.iHWDP + 1];
			MainDriver.TVD[MainDriver.iHWDP] = MainDriver.TVD[MainDriver.iHWDP + 1] + delTmp * Math.cos(MainDriver.ang2p[MainDriver.iHWDP] * MainDriver.radConv);
		}
		else{
			rr = MainDriver.R2bur;
			if(MainDriver.ang2p[MainDriver.iHWDP + 1] < MainDriver.angEOB - 0.01)  rr = MainDriver.Rbur;
			xdx = MainDriver.TMD[MainDriver.iHWDP - 1] - MainDriver.TMD[MainDriver.iHWDP + 1];
			xangl = MainDriver.ang2p[MainDriver.iHWDP + 1] + delAng * delTmp / xdx;
			MainDriver.ang2p[MainDriver.iHWDP] = xangl;
			temp = rr * (Math.sin(xangl * MainDriver.radConv) - Math.sin(MainDriver.ang2p[MainDriver.iHWDP + 1] * MainDriver.radConv));
			MainDriver.TVD[MainDriver.iHWDP] = MainDriver.TVD[MainDriver.iHWDP + 1] + temp;
		}
		MainDriver.TMD[MainDriver.iHWDP] = MainDriver.TMD[MainDriver.iHWDP + 1] + delTmp;

		//..................................... find the top of drill collar
		xdssum = 0;

		for(i = (MainDriver.NwcS + 1); i<MainDriver.iHWDP; i++){//나중에 다시 확인
			xdssum = xdssum + MainDriver.TMD[i] - MainDriver.TMD[i + 1];		   
			MainDriver.iDC=i;
			if(xdssum >MainDriver.LengthDC) break;
		}

		for(i = MainDriver.NwcS; i<MainDriver.iDC; i++){
			MainDriver.TMD[i] = MainDriver.TMD[i + 1];
			MainDriver.TVD[i] = MainDriver.TVD[i + 1];
			MainDriver.Di2p[i] = MainDriver.doDC;
			MainDriver.DiDS[i] = MainDriver.diDC;
			MainDriver.ang2p[i]= MainDriver.ang2p[i + 1];
		}		  
		//......................... calculate information about DC

		delAng = MainDriver.ang2p[MainDriver.iDC - 1] - MainDriver.ang2p[MainDriver.iDC + 1];
		aAng = Math.abs(delAng);
		delTmp = xdssum - MainDriver.LengthDC;
		MainDriver.Di2p[MainDriver.iDC] = MainDriver.doDC;
		MainDriver.DiDS[MainDriver.iDC] = MainDriver.diDC;
		if(aAng < 0.1){
			MainDriver.ang2p[MainDriver.iDC] = MainDriver.ang2p[MainDriver.iDC + 1];
			MainDriver.TVD[MainDriver.iDC] =  (MainDriver.TVD[MainDriver.iDC + 1] + delTmp * Math.cos(MainDriver.ang2p[MainDriver.iDC] * MainDriver.radConv));
		}
		else{
			rr = MainDriver.R2bur;
			if(MainDriver.ang2p[MainDriver.iDC + 1] < MainDriver.angEOB - 0.01) rr = MainDriver.Rbur;
			xdx = MainDriver.TMD[MainDriver.iDC - 1] - MainDriver.TMD[MainDriver.iDC + 1];
			xangl = MainDriver.ang2p[MainDriver.iDC + 1] + delAng * delTmp / xdx;
			MainDriver.ang2p[MainDriver.iDC] = xangl;
			temp = rr * (Math.sin(xangl * MainDriver.radConv) - Math.sin(MainDriver.ang2p[MainDriver.iDC + 1] * MainDriver.radConv));
			MainDriver.TVD[MainDriver.iDC] = MainDriver.TVD[MainDriver.iDC + 1] + temp;
		}
		MainDriver.TMD[MainDriver.iDC] = MainDriver.TMD[MainDriver.iDC + 1] + delTmp;
		//find the location of casing setting depth
		for(i = (MainDriver.NwcE - 2); i>MainDriver.NwcS-1; i--){
			MainDriver.iCsg = i;
			if (MainDriver.DepthCasing < MainDriver.TMD[i])  break;
		}
		for(i = MainDriver.NwcS-1; i<MainDriver.iCsg; i++){
			MainDriver.TMD[i] = MainDriver.TMD[i + 1];
			MainDriver.TVD[i] = MainDriver.TVD[i + 1];
			MainDriver.Di2p[i] = MainDriver.Di2p[i + 1];
			MainDriver.DiDS[i] = MainDriver.DiDS[i + 1];
			MainDriver.ang2p[i] = MainDriver.ang2p[i + 1];
		}
		//......................... calculate information about Casing setting depth

		delAng = MainDriver.ang2p[MainDriver.iCsg - 1] - MainDriver.ang2p[MainDriver.iCsg + 1];
		aAng = Math.abs(delAng);
		delTmp = MainDriver.DepthCasing - MainDriver.TMD[MainDriver.iCsg + 1];
		MainDriver.Di2p[MainDriver.iCsg] = MainDriver.Di2p[MainDriver.iCsg + 1];
		MainDriver.DiDS[MainDriver.iCsg] = MainDriver.DiDS[MainDriver.iCsg + 1];
		if (aAng < 0.1){
			MainDriver.ang2p[MainDriver.iCsg] = MainDriver.ang2p[MainDriver.iCsg + 1];
			MainDriver.TVD[MainDriver.iCsg] =  (MainDriver.TVD[MainDriver.iCsg + 1] + delTmp * Math.cos(MainDriver.ang2p[MainDriver.iCsg] * MainDriver.radConv));
		}
		else{
			rr = MainDriver.R2bur;
			if (MainDriver.ang2p[MainDriver.iCsg + 1] < MainDriver.angEOB - 0.01)  rr = MainDriver.Rbur;
			xdx = MainDriver.TMD[MainDriver.iCsg - 1] - MainDriver.TMD[MainDriver.iCsg + 1];
			xangl = MainDriver.ang2p[MainDriver.iCsg + 1] + delAng * delTmp / xdx;
			MainDriver.ang2p[MainDriver.iCsg] =  xangl;
			temp = rr * (Math.sin(xangl * MainDriver.radConv) - Math.sin(MainDriver.ang2p[MainDriver.iCsg + 1] * MainDriver.radConv));
			MainDriver.TVD[MainDriver.iCsg] = MainDriver.TVD[MainDriver.iCsg + 1] + temp;
		}
		MainDriver.TMD[MainDriver.iCsg] = MainDriver.DepthCasing;
		for(i = MainDriver.iCsg + 1; i<9; i++){              // set annulus OD except choke/kill line
			MainDriver.Do2p[i] = MainDriver.IDcasing;
		}
		//....  adjust the locations of HWDP and DC
		if (MainDriver.iCsg >= MainDriver.iHWDP)  MainDriver.iHWDP = MainDriver.iHWDP - 1;
		if (MainDriver.iCsg >= MainDriver.iDC)  MainDriver.iDC = MainDriver.iDC - 1;		

		if(MainDriver.iHuschel==1) MainDriver.TMD[MainDriver.NwcS-1] = 1500/0.3048;
		else if(MainDriver.iHuschel==2) MainDriver.TMD[MainDriver.NwcS-1] = 4500/0.3048;
	}

	static void setMDvd_cutting(double vd, double md){
		//..................... calculate total MD and VD then assign in the array
		//need to assign TMD, TVD, Angle, OD, ID
		//calculate wellbore path based on input data (Nov.2, OGJ, 1992)
		//This is good for from vertical to horizontal (0.0 to 90.0)
		//
		int i , i2, i3;
		double delAng, aAng;
		double angRad, ang2rad;
		double xdssum=0, delTmp;
		double rr, xdx, xangl, temp;
		//
		MainDriver.NwcE_cutting = 9;
		if(MainDriver.iOnshore==2) MainDriver.NwcE_cutting = 10;
		if(MainDriver.iWell!=4) MainDriver.x2Hold = 0;

		if(MainDriver.iWell==0){          // regular vertcal well
			MainDriver.NwcS_cutting = 5;
			MainDriver.Hdisp = 0;
			MainDriver.DepthKOP = MainDriver.Vdepth;
			MainDriver.BUR = 0;
			MainDriver.BUR2 = 0;
			MainDriver.angEOB = 0;
			MainDriver.ang2EOB = 0;
			MainDriver.Rbur = 0;
			MainDriver.R2bur = 0;
		}
		else if(MainDriver.iWell==1){      // continuous build (DD type 3)
			MainDriver.NwcS_cutting = 4;
			MainDriver.ang2EOB = MainDriver.angEOB;
			MainDriver.BUR2 = 0;
			MainDriver.xHold = 0;
			MainDriver.R2bur = 0;
		}
		else if(MainDriver.iWell==2){      // build-hold (DD type 1]
			MainDriver.NwcS_cutting = 3;
			MainDriver.BUR2 = 0;
			MainDriver.R2bur = 0;
			MainDriver.ang2EOB = MainDriver.angEOB;
		}
		else{                         // horizontal or near horizontal with 2 build
			MainDriver.NwcS_cutting = 1;
			if(MainDriver.iWell==3) MainDriver.NwcS_cutting = 2;
		}

		//......... calc total MD,VD(ft),angle from vert(degree),do,di(in) etc
		//		      ignore some variables initialized
		for(i=MainDriver.NwcS_cutting-1; i<MainDriver.NwcE_cutting; i++){
			MainDriver.Do2p_cut[i] = MainDriver.DiaHole;
			MainDriver.Di2p_cut[i]= MainDriver.doDP; 
			MainDriver.DiDS_cut[i] = MainDriver.diDP;
		}

		MainDriver.Do2p_cut[9] = MainDriver.Dchoke;
		MainDriver.Di2p_cut[9] = 0;
		MainDriver.DiDS_cut[9] = MainDriver.diDP;

		MainDriver.TMD_cutting[8] = MainDriver.Dwater;
		MainDriver.TVD_cutting[8] = MainDriver.Dwater;

		MainDriver.TMD_cutting[7] = MainDriver.DepthKOP; 
		MainDriver.TVD_cutting[7] = MainDriver.DepthKOP;
		//.................................... end of first build section
		angRad = MainDriver.angEOB * MainDriver.radConv;
		ang2rad = MainDriver.ang2EOB * MainDriver.radConv;

		if(MainDriver.iProblem[3] == 1){ // 150127 AJW
			MainDriver.ang2p_cutting[8] = 0;
			MainDriver.ang2p_cutting[7] = 0; 
		}
		MainDriver.TMD_cutting[6] = MainDriver.TMD_cutting[7] + MainDriver.Rbur * angRad;
		MainDriver.TVD_cutting[6] = MainDriver.TVD_cutting[7] + MainDriver.Rbur * Math.sin(angRad);
		MainDriver.ang2p_cutting[6] = MainDriver.angEOB;

		//.................................... end of tangent section
		MainDriver.TMD_cutting[5] = MainDriver.TMD_cutting[6] + MainDriver.xHold;
		MainDriver.TVD_cutting[5] = MainDriver.TVD_cutting[6] + MainDriver.xHold * Math.cos(angRad);
		MainDriver.ang2p_cutting[5] = MainDriver.angEOB;

		//..................................... end of second build
		MainDriver.TMD_cutting[4] = MainDriver.TMD_cutting[5] + MainDriver.R2bur * (ang2rad - angRad);
		MainDriver.TVD_cutting[4] = MainDriver.TVD_cutting[5] + MainDriver.R2bur * (Math.sin(ang2rad) - Math.sin(angRad));
		MainDriver.ang2p_cutting[4] = MainDriver.ang2EOB;

		//..................................... at the bit: bottom hole
		MainDriver.TMD_cutting[3] = MainDriver.TMD_cutting[4] + MainDriver.x2Hold; 
		MainDriver.TVD_cutting[3] = MainDriver.TVD_cutting[4] + MainDriver.x2Hold * Math.cos(ang2rad);
		MainDriver.ang2p_cutting[3] = MainDriver.ang2EOB;

		if(md<=MainDriver.TMD_cutting[7]){
			MainDriver.NwcS_cutting = 7-3+1;
			MainDriver.TMD_cutting[6] = md; 
			MainDriver.TVD_cutting[6] = vd;
		}

		else if(md<=MainDriver.TMD_cutting[6]){
			MainDriver.NwcS_cutting = 6-3+1;
			MainDriver.TMD_cutting[6] = md; 
			MainDriver.TVD_cutting[6] = vd;
		}

		else if(md<=MainDriver.TMD_cutting[5]){
			MainDriver.NwcS_cutting = 5-3+1;
			MainDriver.TMD_cutting[5] = md; 
			MainDriver.TVD_cutting[5] = vd;
		}

		else if(md<=MainDriver.TMD_cutting[4]){
			MainDriver.NwcS_cutting = 4-3+1;
			MainDriver.TMD_cutting[4] = md; 
			MainDriver.TVD_cutting[4] = vd;
		}

		else if(md<=MainDriver.TMD_cutting[3]){
			MainDriver.NwcS_cutting = 3-3+1;
			MainDriver.TMD_cutting[3] = md; 
			MainDriver.TVD_cutting[3] = vd;
		}
		//  xldp = tmd(4) - LengthDC - LengthHWDP


		//.................................. find the top of heviwate drill pipe (HWDP)
		xdssum = 0;  //: i2start = nwcs + 3
		for(i2 = (MainDriver.NwcS + 2); i2<8; i2++){
			xdssum = xdssum + MainDriver.TMD_cutting[i2] - MainDriver.TMD_cutting[i2 + 1];
			MainDriver.iHWDP_cutting = i2;

			if(xdssum > (MainDriver.LengthHWDP + MainDriver.LengthDC)) break;
		}

		for(i3 = (MainDriver.NwcS_cutting + 1); i3<MainDriver.iHWDP_cutting; i3++){ //나중에 다시 확인	
			MainDriver.TMD_cutting[i3] = MainDriver.TMD_cutting[i3 + 1];
			MainDriver.TVD_cutting[i3] = MainDriver.TVD_cutting[i3 + 1];
			MainDriver.Di2p_cut[i3] = MainDriver.doHWDP;
			MainDriver.DiDS_cut[i3] = MainDriver.diHWDP;
			MainDriver.ang2p_cutting[i3] = MainDriver.ang2p_cutting[i3 + 1];
		}

		//......................... calculate information about HWDP

		delAng = MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting-1] - MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting + 1];
		aAng = Math.abs(delAng);
		delTmp = xdssum - MainDriver.LengthHWDP - MainDriver.LengthDC;
		MainDriver.Di2p_cut[MainDriver.iHWDP_cutting] = MainDriver.doHWDP;
		MainDriver.DiDS_cut[MainDriver.iHWDP_cutting] = MainDriver.diHWDP;
		if(aAng < 0.1){
			MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting] = MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting + 1];
			MainDriver.TVD_cutting[MainDriver.iHWDP_cutting] = MainDriver.TVD_cutting[MainDriver.iHWDP_cutting + 1] + delTmp * Math.cos(MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting] * MainDriver.radConv);
		}
		else{
			rr = MainDriver.R2bur;
			if(MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting + 1] < MainDriver.angEOB - 0.01)  rr = MainDriver.Rbur;
			xdx = MainDriver.TMD_cutting[MainDriver.iHWDP_cutting - 1] - MainDriver.TMD_cutting[MainDriver.iHWDP_cutting + 1];
			xangl = MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting + 1] + delAng * delTmp / xdx;
			MainDriver.ang2p_cutting[MainDriver.iHWDP] = xangl;
			temp = rr * (Math.sin(xangl * MainDriver.radConv) - Math.sin(MainDriver.ang2p_cutting[MainDriver.iHWDP_cutting + 1] * MainDriver.radConv));
			MainDriver.TVD_cutting[MainDriver.iHWDP_cutting] = MainDriver.TVD_cutting[MainDriver.iHWDP_cutting + 1] + temp;
		}
		MainDriver.TMD_cutting[MainDriver.iHWDP_cutting] = MainDriver.TMD_cutting[MainDriver.iHWDP_cutting + 1] + delTmp;

		//..................................... find the top of drill collar
		xdssum = 0;

		for(i = (MainDriver.NwcS_cutting + 1); i<MainDriver.iHWDP_cutting; i++){//나중에 다시 확인
			xdssum = xdssum + MainDriver.TMD_cutting[i] - MainDriver.TMD_cutting[i + 1];		   
			MainDriver.iDC_cutting=i;
			if(xdssum >MainDriver.LengthDC) break;
		}

		for(i = MainDriver.NwcS_cutting; i<MainDriver.iDC_cutting; i++){
			MainDriver.TMD_cutting[i] = MainDriver.TMD_cutting[i + 1];
			MainDriver.TVD_cutting[i] = MainDriver.TVD_cutting[i + 1];
			MainDriver.Di2p_cut[i] = MainDriver.doDC;
			MainDriver.DiDS_cut[i] = MainDriver.diDC;
			MainDriver.ang2p_cutting[i]= MainDriver.ang2p_cutting[i + 1];
		}		  
		//......................... calculate information about DC

		delAng = MainDriver.ang2p_cutting[MainDriver.iDC_cutting - 1] - MainDriver.ang2p_cutting[MainDriver.iDC_cutting + 1];
		aAng = Math.abs(delAng);
		delTmp = xdssum - MainDriver.LengthDC;
		MainDriver.Di2p_cut[MainDriver.iDC_cutting] = MainDriver.doDC;
		MainDriver.DiDS_cut[MainDriver.iDC_cutting] = MainDriver.diDC;
		if(aAng < 0.1){
			MainDriver.ang2p_cutting[MainDriver.iDC_cutting] = MainDriver.ang2p_cutting[MainDriver.iDC_cutting + 1];
			MainDriver.TVD_cutting[MainDriver.iDC_cutting] =  (MainDriver.TVD_cutting[MainDriver.iDC_cutting + 1] + delTmp * Math.cos(MainDriver.ang2p_cutting[MainDriver.iDC_cutting] * MainDriver.radConv));
		}
		else{
			rr = MainDriver.R2bur;
			if(MainDriver.ang2p_cutting[MainDriver.iDC_cutting + 1] < MainDriver.angEOB - 0.01) rr = MainDriver.Rbur;
			xdx = MainDriver.TMD_cutting[MainDriver.iDC_cutting - 1] - MainDriver.TMD_cutting[MainDriver.iDC_cutting + 1];
			xangl = MainDriver.ang2p_cutting[MainDriver.iDC_cutting + 1] + delAng * delTmp / xdx;
			MainDriver.ang2p_cutting[MainDriver.iDC_cutting] = xangl;
			temp = rr * (Math.sin(xangl * MainDriver.radConv) - Math.sin(MainDriver.ang2p_cutting[MainDriver.iDC_cutting + 1] * MainDriver.radConv));
			MainDriver.TVD_cutting[MainDriver.iDC_cutting] = MainDriver.TVD_cutting[MainDriver.iDC_cutting + 1] + temp;
		}
		MainDriver.TMD_cutting[MainDriver.iDC_cutting] = MainDriver.TMD_cutting[MainDriver.iDC_cutting + 1] + delTmp;
		//find the location of casing setting depth
		for(i = (MainDriver.NwcE - 2); i>MainDriver.NwcS_cutting-1; i--){
			MainDriver.iCsg_cutting = i;
			if (MainDriver.DepthCasing < MainDriver.TMD_cutting[i])  break;
		}
		for(i = MainDriver.NwcS_cutting-1; i<MainDriver.iCsg_cutting; i++){
			MainDriver.TMD_cutting[i] = MainDriver.TMD_cutting[i + 1];
			MainDriver.TVD_cutting[i] = MainDriver.TVD_cutting[i + 1];
			MainDriver.Di2p_cut[i] = MainDriver.Di2p_cut[i + 1];
			MainDriver.DiDS_cut[i] = MainDriver.DiDS_cut[i + 1];
			MainDriver.ang2p_cutting[i] = MainDriver.ang2p_cutting[i + 1];
		}
		//......................... calculate information about Casing setting depth

		delAng = MainDriver.ang2p_cutting[MainDriver.iCsg_cutting - 1] - MainDriver.ang2p_cutting[MainDriver.iCsg_cutting + 1];
		aAng = Math.abs(delAng);
		delTmp = MainDriver.DepthCasing - MainDriver.TMD_cutting[MainDriver.iCsg_cutting + 1];
		MainDriver.Di2p_cut[MainDriver.iCsg_cutting] = MainDriver.Di2p_cut[MainDriver.iCsg_cutting + 1];
		MainDriver.DiDS_cut[MainDriver.iCsg_cutting] = MainDriver.DiDS_cut[MainDriver.iCsg_cutting + 1];
		if (aAng < 0.1){
			MainDriver.ang2p_cutting[MainDriver.iCsg_cutting] = MainDriver.ang2p_cutting[MainDriver.iCsg_cutting + 1];
			MainDriver.TVD_cutting[MainDriver.iCsg_cutting] =  (MainDriver.TVD_cutting[MainDriver.iCsg_cutting + 1] + delTmp * Math.cos(MainDriver.ang2p_cutting[MainDriver.iCsg_cutting] * MainDriver.radConv));
		}
		else{
			rr = MainDriver.R2bur;
			if (MainDriver.ang2p_cutting[MainDriver.iCsg_cutting + 1] < MainDriver.angEOB - 0.01)  rr = MainDriver.Rbur;
			xdx = MainDriver.TMD_cutting[MainDriver.iCsg_cutting - 1] - MainDriver.TMD_cutting[MainDriver.iCsg_cutting + 1];
			xangl = MainDriver.ang2p_cutting[MainDriver.iCsg_cutting + 1] + delAng * delTmp / xdx;
			MainDriver.ang2p_cutting[MainDriver.iCsg_cutting] =  xangl;
			temp = rr * (Math.sin(xangl * MainDriver.radConv) - Math.sin(MainDriver.ang2p_cutting[MainDriver.iCsg_cutting + 1] * MainDriver.radConv));
			MainDriver.TVD_cutting[MainDriver.iCsg_cutting] = MainDriver.TVD_cutting[MainDriver.iCsg_cutting + 1] + temp;
		}
		MainDriver.TMD_cutting[MainDriver.iCsg_cutting] = MainDriver.DepthCasing;
		for(i = MainDriver.iCsg_cutting + 1; i<9; i++){              // set annulus OD except choke/kill line
			MainDriver.Do2p_cut[i] = MainDriver.IDcasing;
		}
		//....  adjust the locations of HWDP and DC
		if (MainDriver.iCsg_cutting >= MainDriver.iHWDP_cutting)  MainDriver.iHWDP_cutting = MainDriver.iHWDP_cutting - 1;
		if (MainDriver.iCsg_cutting >= MainDriver.iDC_cutting)  MainDriver.iDC_cutting = MainDriver.iDC_cutting - 1;	

		for(i=MainDriver.NwcS_cutting; i<=MainDriver.NwcE_cutting-1; i++){
			MainDriver.Area_ann[i] = (Math.pow(MainDriver.Do2p_cut[i],2)-Math.pow(MainDriver.Di2p_cut[i], 2))*Math.PI/4/144; // ft2
			MainDriver.V_ann_cutting[i] = (Math.pow(MainDriver.Do2p_cut[i],2)-Math.pow(MainDriver.Di2p_cut[i], 2))/1029.4; //bbl/ft
			MainDriver.V_ann_cutting[i] = MainDriver.V_ann_cutting[i]*(MainDriver.TMD_cutting[i-1]-MainDriver.TMD_cutting[i]);//bbl
		}

	}

	static void DefaultData(){ // for user's input
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 2; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25; MainDriver.oMud = 12; MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 10000; 
		MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 8000;
		MainDriver.Dwater = 1000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 5; MainDriver.BUR2 = 10; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 2000; MainDriver.x2Hold = 2000;
		MainDriver.IDcasing = 11; MainDriver.DiaHole =  9.875; MainDriver.doDP = 5; MainDriver.diDP =  4.214;
		MainDriver.doDC =  7.5; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0; //140728
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//

		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 22.1;	
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	// input data for well control

	static void DefaultData_case1_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 2;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25; MainDriver.Ruf = 0;
		//MainDriver.oMud = 12; //원래 교수님 
		MainDriver.oMud = 13.5;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 5000; MainDriver.LengthDC = 300; MainDriver.DepthKOP = 0;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 0; MainDriver.x2Hold = 0;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.mud_calc = 1;  //0: preos , 1:standing katz
		MainDriver.iOilComp = 0; //0: 고려x, 1:고려o
		MainDriver.ibaseoil = 1;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;

		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;
		//

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case2_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 2;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12; // 교수님이 주신 값
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;		
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY
		MainDriver.Vdepth = 12000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 10000;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 2000; MainDriver.x2Hold = 0;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60; 
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case3_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 2;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;

		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 4; //Modified by TY
		MainDriver.Vdepth = 15000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		MainDriver.angEOB = 67.17470522; //140718 ajw
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60; MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg	
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case4_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 15.5;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 10000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 800;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case5_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		MainDriver.oMud = 12; 
		MainDriver.oMud = 17.5;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 10000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 3000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case7_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY
		MainDriver.Vdepth = 12000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 8000;
		MainDriver.Dwater = 1000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 2000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case8_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY
		MainDriver.Vdepth = 12000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 8000;
		MainDriver.Dwater = 3000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 2000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 4000;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case9_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16.5;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 4; //Modified by TY
		MainDriver.Vdepth = 15000;  MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 1000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 2000;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		MainDriver.angEOB = 45; //140718 ajw
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case10_dr(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 19;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 4; //Modified by TY
		MainDriver.Vdepth = 15000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 6000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 2000;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		MainDriver.angEOB = 45; //140718 ajw
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
			MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	// input data for well control

	static void DefaultData_case1_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 2;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25; MainDriver.Ruf = 0;
		//MainDriver.oMud = 12; //원래 교수님 
		MainDriver.oMud = 13.5;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 5000; MainDriver.LengthDC = 300; MainDriver.DepthKOP = 0;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 0; MainDriver.x2Hold = 0;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.mud_calc = 1;  //0: preos , 1:standing katz
		MainDriver.iOilComp = 0; //0: 고려x, 1:고려o
		MainDriver.ibaseoil = 1;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;

		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;
		//

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case2_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 2;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12; // 교수님이 주신 값
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;		
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY
		MainDriver.Vdepth = 12000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 10000;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 2000; MainDriver.x2Hold = 0;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60; 
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case3_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 2;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;

		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 4; //Modified by TY
		MainDriver.Vdepth = 15000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		MainDriver.angEOB = 67.17470522; //140718 ajw
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60; MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg	
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case4_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 15.5;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 10000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 800;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case5_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		MainDriver.oMud = 12; 
		MainDriver.oMud = 17.5;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 10000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 3000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}
	static void DefaultData_case6_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		MainDriver.oMud = 12;
		MainDriver.oMud = 17.5;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 10000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 3000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case7_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY
		MainDriver.Vdepth = 12000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 8000;
		MainDriver.Dwater = 1000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 2000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case8_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY
		MainDriver.Vdepth = 12000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 8000;
		MainDriver.Dwater = 1000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 2000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 4000;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case9_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 16.5;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 4; //Modified by TY
		MainDriver.Vdepth = 15000;  MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 1000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 2000;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		MainDriver.angEOB = 45; //140718 ajw
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_case10_wc(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25;
		//MainDriver.oMud = 12;
		MainDriver.oMud = 19;
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 4; //Modified by TY
		MainDriver.Vdepth = 15000; MainDriver.LengthDC = 600*MainDriver.Vdepth/10000; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 6000;
		MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 2000;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.75; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  7; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000*MainDriver.Vdepth/10000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		MainDriver.angEOB = 45; //140718 ajw
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}


	// for validation
	static void DefaultData_Comparison(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration

		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; On-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 2; //MainDriver.iPump = 3;d
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 1; //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25; MainDriver.oMud = 13.5; MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY

		MainDriver.LengthDC = 150; 
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 1000;	

		MainDriver.BUR = 5.08;
		MainDriver.Vdepth = 682/0.3048; // 2700m
		MainDriver.Hdisp = 1092/0.3048; 
		MainDriver.DepthKOP = 200/0.3048;		

		/*
				//30도
				MainDriver.BUR = 2.307692308;
				MainDriver.Vdepth = 4539.484767; // 2700m
				MainDriver.Hdisp = 1832.63442; 
				MainDriver.DepthKOP = 700;	*/




		//10도
		MainDriver.BUR = 0.769230769;
		MainDriver.Vdepth = 4947.83326; // 2700m
		MainDriver.Hdisp = 634.1032454; 
		MainDriver.DepthKOP = 700;

		//20도
		MainDriver.BUR = 1.538461538;
		MainDriver.Vdepth = 4792.838059; // 2700m
		MainDriver.Hdisp = 1250.65872; 
		MainDriver.DepthKOP = 700;

		//40도
		MainDriver.BUR = 3.076923077;
		MainDriver.Vdepth = 4195.076387; // 2700m
		MainDriver.Hdisp = 2364.014474; 
		MainDriver.DepthKOP = 700;

		//50도
		MainDriver.BUR = 3.846153846;
		MainDriver.Vdepth = 3769.53178; // 2700m
		MainDriver.Hdisp = 2830.269151; 
		MainDriver.DepthKOP = 700;	

		//70도
		MainDriver.BUR = 5.384615385;
		MainDriver.Vdepth = 2725.953967; // 2700m
		MainDriver.Hdisp = 3519.210854; 
		MainDriver.DepthKOP = 700;

		//80도
		MainDriver.BUR = 6.153846154;
		MainDriver.Vdepth = 2137.856111; // 2700m
		MainDriver.Hdisp = 3723.803426; 
		MainDriver.DepthKOP = 700;

		//60도
		MainDriver.BUR = 4.615384615;
		MainDriver.Vdepth = 3275.091346; // 2700m
		MainDriver.Hdisp = 3218.780489; 
		MainDriver.DepthKOP = 700;	

		//60도
		MainDriver.BUR = 4.615384615;
		MainDriver.Vdepth = 3275.091346; // 2700m
		MainDriver.Hdisp = 3218.780489; 
		MainDriver.DepthKOP = 700;	

		//30도
		MainDriver.BUR = 2.307692308;
		MainDriver.Vdepth = 4539.484767; // 2700m
		MainDriver.Hdisp = 1832.63442; 
		MainDriver.DepthKOP = 700;

		//90도
		MainDriver.BUR = 6.923076923;
		MainDriver.Vdepth = 1527.605704; // 2700m
		MainDriver.Hdisp = 3827.605704; 
		MainDriver.DepthKOP = 700;

		//수직
		/*MainDriver.iWell = 0;
				MainDriver.Vdepth = 5000; // 2700m
				MainDriver.Hdisp = 0; 
				MainDriver.DepthKOP = 0;*/

		MainDriver.BUR2 = 0; MainDriver.ang2EOB = 0; MainDriver.x2Hold = 1163/0.3048;
		MainDriver.IDcasing = 12; MainDriver.DiaHole =  10; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.doDC =  6.75; MainDriver.diDC = 2+1/4; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 500; MainDriver.doHWDP = 5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 500;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 500;
		MainDriver.angEOB = 10; 
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
				MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0; //140728
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//

		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		MainDriver.iProblem[0]=0;MainDriver.iProblem[1]=0;MainDriver.iProblem[2]=0;MainDriver.iProblem[3]=0;
		MainDriver.iProblem_occured[0]=0;MainDriver.iProblem_occured[1]=0;MainDriver.iProblem_occured[2]=0;MainDriver.iProblem_occured[3]=0;
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;		

		MainDriver.ROPen = 60;
		//MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.mdBitOff = 200;
		MainDriver.timeToTD = MainDriver.mdBitOff/(0.5*MainDriver.ROPen)*3600;

		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 50;//80;
		MainDriver.WOB_Base = 50;//70;
		MainDriver.fc_Base = 0;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 17;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/15.0; //in
		MainDriver.iProblem[3] = 1;	
		MainDriver.n_HC = 0.58;
		MainDriver.K_HC = 548;

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0; 
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;

	}

	static void DefaultData_Nagasawa1(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration

		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; On-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 1; MainDriver.Method = 2; //MainDriver.iPump = 3;d
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 1; //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25; MainDriver.oMud = 1.18*8.33; MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 2; //Modified by TY
		MainDriver.Vdepth = 682/0.3048; // 2700m 
		MainDriver.LengthDC = 50/0.3048;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = 577/0.3048; MainDriver.DepthKOP = 200/0.3048;
		MainDriver.ang2EOB = 0; MainDriver.Hdisp = 1092/0.3048; MainDriver.x2Hold = 1163/0.3048;
		MainDriver.IDcasing = 8.835; MainDriver.DiaHole =  8.5; MainDriver.doDP = 5; MainDriver.diDP =  4.276;
		MainDriver.BUR = 5.08; MainDriver.BUR2 = 0; 
		MainDriver.doDC =  6.75; MainDriver.diDC = 2+1/4; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 470/0.3048; MainDriver.doHWDP = 5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 300;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 500;
		MainDriver.angEOB = 80; 
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0; //140728
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//

		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		MainDriver.iProblem[0]=0;MainDriver.iProblem[1]=0;MainDriver.iProblem[2]=0;MainDriver.iProblem[3]=0;
		MainDriver.iProblem_occured[0]=0;MainDriver.iProblem_occured[1]=0;MainDriver.iProblem_occured[2]=0;MainDriver.iProblem_occured[3]=0;
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 13.5/0.3048*2;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 80;
		MainDriver.WOB_Base = 70;
		MainDriver.fc_Base = 0;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0;//1/8; //in
		MainDriver.iProblem[3] = 1;	
		MainDriver.n_HC = 0.58;
		MainDriver.K_HC = 548;

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 1; 
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;
			MainDriver.numCutting = 0;
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}

	static void DefaultData_Nagasawa2(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration

		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; On-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 0; MainDriver.Method = 2; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 1; //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25; MainDriver.oMud = 1.18*8.33; MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 4; //Modified by TY
		MainDriver.Vdepth = 1349/0.3048; // 2700m 
		MainDriver.LengthDC = 50/0.3048; MainDriver.DepthKOP = 500/0.3048;
		MainDriver.Dwater = 0;
		MainDriver.DepthCasing = MainDriver.Vdepth/3;
		MainDriver.BUR = 4.064; MainDriver.BUR2 = 1.016; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 3719/0.3048; MainDriver.x2Hold = 800/0.3048;
		MainDriver.IDcasing = 8.535; MainDriver.DiaHole =  8.5; MainDriver.doDP = 5.5; MainDriver.diDP =  4.670;
		MainDriver.doDC =  6.75; MainDriver.diDC = 2+1/4; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 800/0.3048; MainDriver.doHWDP = 5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 300;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 500;
		MainDriver.angEOB = 80; //140718 ajw
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 0; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0; //140728
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//

		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		MainDriver.iProblem[0]=0;MainDriver.iProblem[1]=0;MainDriver.iProblem[2]=0;MainDriver.iProblem[3]=0;
		MainDriver.iProblem_occured[0]=0;MainDriver.iProblem_occured[1]=0;MainDriver.iProblem_occured[2]=0;MainDriver.iProblem_occured[3]=0;
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 13.5/0.3048*2;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 80;
		MainDriver.WOB_Base = 70;
		MainDriver.fc_Base = 0;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in
		MainDriver.iProblem[3] = 1;	
		MainDriver.n_HC = 0.58;
		MainDriver.K_HC = 548;

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 2; 
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}


	static void DefaultData_VB(){
		//=====================================================================================
		// The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//                           April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		// This is a general sub program to;
		//   Open data file
		//   Save data file
		//   Save output file before and after well control simulation
		//--------------------------------------------------------------
		//  This module has the following subs, 7/29/2003
		//       Sub DefaultData()
		//       Sub getGeometry()
		//       Sub OpenData()
		//       Sub SaveAsFile()
		//       Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax = 0.85; 
		MainDriver.iType = 0;
		MainDriver.iChoke = 1; 
		MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer//s method, Duplex, Power-law model, DP considered.
		MainDriver.iOnshore = 2; 
		MainDriver.Method = 2;
		MainDriver.iDelp = 1; 
		MainDriver.iZfact = 1; 
		MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; 
		MainDriver.S300 = 25; 
		MainDriver.oMud = 14; 
		MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; 
		MainDriver.Dnoz[1] = 12; 
		MainDriver.Dnoz[2] = 12; 
		MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(태엽수정) build-hold case(기존)
		MainDriver.iWell = 0;
		MainDriver.Vdepth = 20000;
		MainDriver.DepthCasing = 6000; 
		MainDriver.LengthDC = 600; 
		MainDriver.DepthKOP = 8000;
		MainDriver.Dwater = 2000; 
		MainDriver.DepthSurfCsg = 4000; 
		MainDriver.DepthCduct = 2800;
		MainDriver.BUR = 5; 
		MainDriver.BUR2 = 10; 
		MainDriver.ang2EOB = 90; 
		MainDriver.Hdisp = 2000; 
		MainDriver.x2Hold = 2000;
		MainDriver.IDcasing = 11; 
		MainDriver.DiaHole = 9.875; 
		MainDriver.doDP = 5; 
		MainDriver.diDP = 4.214;
		MainDriver.doDC = 7.5; 
		MainDriver.diDC = 2; 
		MainDriver.Dchoke = 4; 
		MainDriver.Dkill = 3; 
		MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000; 
		MainDriver.doHWDP = 5.5; 
		MainDriver.diHWDP = 3;

		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; 
		MainDriver.ODcduct = 20;
		MainDriver.iSurfCsg = 1; 
		MainDriver.ODsurfCsg = 13.375;
		//------ Pump data
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		//------ Shut-in data and Others
		MainDriver.gasGravity = 0.554; 
		MainDriver.tWgrad = -0.9; 
		MainDriver.fCO2 = 0; 
		MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; 
		MainDriver.Tgrad = 1.1; 
		MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; 
		MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; 
		MainDriver.Perm = 250; 
		MainDriver.Porosity = 0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; 
		MainDriver.ROPen = 60; 
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 3;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; 
		MainDriver.leftX = 4035; 
		MainDriver.topX = 1170; 
		MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//    formColor = &HC0C000    //old background color of light blue
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 1; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.mud_calc = 0;  //0: preos , 1:standing katz
		MainDriver.ibaseoil = 0;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}


	static void DefaultData_test(){
		//=====================================================================================
		//The following subs are from IO modlue (m6inout.bas) by Dr. Jonggeun CHOE
		//		                       April 26, 1996/ Aug. 3, 2003/
		//--------------------------------------------------------------
		//This is a general sub program to;
		//Open data file
		//Save data file
		//Save output file before and after well control simulation
		//--------------------------------------------------------------
		//This module has the following subs, 7/29/2003
		//   Sub DefaultData()
		//   Sub getGeometry()
		//   Sub OpenData()
		//   Sub SaveAsFile()
		//   Sub SaveData()
		//
		//=== sub DefaultData() ;Set default INPUT Data for easy demonstration
		MainDriver.HgMax =  0.85; MainDriver.iType = 0; MainDriver.iChoke = 1; MainDriver.iKill = 0;
		//------ Control data ; Off-shore, Engineer's Method, Duplex, Power-law MainDriver.Model, DP considered.
		MainDriver.iOnshore = 2; MainDriver.Method = 1; //MainDriver.iPump = 3;
		MainDriver.iDelp = 1; MainDriver.iZfact = 1; MainDriver.Model = 0;  //API RP 13D
		MainDriver.iData = 1; //input as shear stress reading
		//------ Fluid data Plastic viscosity=10 Yield stress=15 lb/100 sq ft (n=0.336)
		MainDriver.S600 = 35; MainDriver.S300 = 25; MainDriver.oMud = 11; MainDriver.Ruf = 0;
		MainDriver.Dnoz[0] = 12; MainDriver.Dnoz[1] = 12; MainDriver.Dnoz[2] = 12; MainDriver.Dnoz[3] = 0;
		MainDriver.RenC = 2100;
		//------ Well geometry data, default wellbore trajectory is vertical(Modified by TY) build-hold case
		MainDriver.iWell = 0; //Modified by TY
		MainDriver.Vdepth = 10000; MainDriver.LengthDC = 600; MainDriver.DepthKOP = 12000;
		MainDriver.Dwater = 2000;
		//MainDriver.DepthCasing = 5000*(MainDriver.Vdepth-MainDriver.Dwater)/10000+MainDriver.Dwater;
		MainDriver.DepthCasing = 5000;
		MainDriver.BUR = 3; MainDriver.BUR2 = 3; MainDriver.ang2EOB = 90; MainDriver.Hdisp = 5000; MainDriver.x2Hold = 500;
		MainDriver.IDcasing = 10; MainDriver.DiaHole =  9.875; MainDriver.doDP = 5; MainDriver.diDP =  4.214;
		MainDriver.doDC =  7.5; MainDriver.diDC = 2; MainDriver.Dchoke = 4; MainDriver.Dkill = 3; MainDriver.Driser = 19;
		MainDriver.LengthHWDP = 1000; MainDriver.doHWDP =  5.5; MainDriver.diHWDP = 3;
		//------ Conductor or surface casing information
		MainDriver.iCduct = 1; MainDriver.ODcduct = 20; MainDriver.DepthCduct = 1800;
		MainDriver.iSurfCsg = 1; MainDriver.ODsurfCsg =  13.375; MainDriver.DepthSurfCsg = 3000;
		//------ Pump data
		MainDriver.Qcapacity1=0.133765401480195;
		MainDriver.Qcapacity2=0.133765401480195;
		MainDriver.spMinD1 = 60; MainDriver.spMin1 = 30; MainDriver.spMinD2 = 0; MainDriver.spMin2 = 0;
		MainDriver.initspMin1 = MainDriver.spMin1; MainDriver.initspMin2 = MainDriver.spMin2;
		MainDriver.initQkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;
		/*MainDriver.DiaLiner = 6; MainDriver.Drod =  2.5; MainDriver.strokeLength = 18; MainDriver.Effcy =  0.85;
		MainDriver.spMinD = 60; MainDriver.spMin = 30;*/
		//------ Shut-in data and Others
		MainDriver.gasGravity =  0.554; MainDriver.tWgrad =  -0.9; MainDriver.fCO2 = 0; MainDriver.fH2S = 0;
		MainDriver.Tsurf = 70; MainDriver.Tgrad =  1.1; MainDriver.SimRate = 10; // 10 times faster
		//------ Other control data
		MainDriver.iBOP = 1; MainDriver.iHresv = 0;  // hit high pressured H. Res.; iddata = 0
		MainDriver.pitWarn = 10; MainDriver.Perm = 250; MainDriver.Porosity =  0.25;   //k = 100 --> 250 for fast stablization
		MainDriver.Skins = 2; MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.KICKintens = 1.5;
		MainDriver.DchkControl = 1;
		//------ Set WellPic.frm//s position
		MainDriver.heightX = 6075; MainDriver.leftX = 4035; MainDriver.topX = 1170; MainDriver.widthX = 3200;  //2700
		//------ Set the colors for PrintForm control
		//MainDriver.formColor = &HC0C000    //old background color of light MainDriver.Blue
		//MainDriver.formColor = &H404080   //light maroon; Aggie Color !
		//MainDriver.Blue = &HFF0000; MainDriver.White = &HFFFFFF
		//MainDriver.Yellow = &HFFFF&; MainDriver.Gray = &HC0C0C0; MainDriver.Black = &H0&
		//----- for fracture gradient
		MainDriver.iFG = 1;       //Eaton//s MainDriver.Method
		MainDriver.iCHKcontrol = 1;  //automatic/perfect control

		//Added by TY
		MainDriver.imud = 1; // 0:WBM, 1: OBM 'WBM is default
		MainDriver.mud_calc = 1;  //0: preos , 1:standing katz
		MainDriver.iOilComp = 1; //0: 고려x, 1:고려o
		MainDriver.ibaseoil = 1;
		MainDriver.foil = 0.7;
		MainDriver.fbrine = 0.1;
		MainDriver.fadditive = 0.1;
		//----- for heat transfer
		MainDriver.iHeattrans = 2;
		MainDriver.TconF = 1.16;
		MainDriver.SheatF = 0.24;
		MainDriver.HtrancF = 1;
		MainDriver.TconM = 0.58;
		MainDriver.SheatM = 0.4;
		MainDriver.HtrancM = 30;
		MainDriver.InjmudT = 75;
		//----- for multikicks
		MainDriver.imultikick = 0; //ignore multikicks

		//----------------------------------------------------------------- Since July 1, 2002
		//........... for ERD and ML		
		MainDriver.swDensity =  8.6; //ppg for general sea water density
		MainDriver.iMudComp = 1;    //1;considered, 0;ignored		
		MainDriver.MudComp =  0.000006; //1/psi, typical value = 6.0E-6
		MainDriver.iBlowout = 0;   //Blowout animation; with(1), without[0]
		MainDriver.iFGnum = 11;    //no. of data for pore and fracture pressures
		MainDriver.SS100 = 10; MainDriver.SS3 = 2;  //for API RP13D
		MainDriver.igERD = 0;  //single well[0], multilateral well(1)
		if(MainDriver.igERD == 1){
			MainDriver.KICKintens = 0; MainDriver.Perm = 40; MainDriver.Method = 1;
		}
		MainDriver.igMLnumber = 1;  //one(1) ML trajectory as a default
		MainDriver.gPayLength = 20;
		MainDriver.gTripTankVolume = 5 ; //bbls
		MainDriver.gTripTankHeight = 10; //ft
		MainDriver.gJointLength = 30;  //ft
		MainDriver.igJointNumber = 3; //
		MainDriver.gConnTime = 20;  //sec for joint connections
		MainDriver.gBleedPressure = 100;
		MainDriver.gBleedTime = 60;
		//.......................... Default multilateral data/ 2004.8.4.
		MainDriver.mlPlug[0] = 0;  //plugged(1)-checked/unplugged[0]-unchecked
		MainDriver.mlKOP[0] = 7400;
		MainDriver.mlKOPvd[0] = 7400; MainDriver.mlKOPang[0] = 0;
		MainDriver.mlBUR[0] = 4; MainDriver.mlEOB[0] = 40; MainDriver.mlHold[0] = 200;
		MainDriver.mlBUR2nd[0] = 5; MainDriver.mlEOB2nd[0] = 80; MainDriver.mlHold2nd[0] = 800;

		MainDriver.mlDia[0] =  9.5; MainDriver.mlDiaMD[0] = 1200; MainDriver.mlDia2nd[0] = 9;
		MainDriver.mlPform[0] = 6570; MainDriver.mlPerm[0] = 100; MainDriver.mlPorosity[0] =  0.25;
		MainDriver.mlSkin[0] =  2.5; MainDriver.mlHeff[0] =  12.5;
		//
		//MainDriver.WellColor = &HC0FFC0
		//MainDriver.MudColor = &HC0C0C0     //light MainDriver.Gray color as old mud
		//MainDriver.KillColor = &H808080    //dark MainDriver.Gray as a kill mud color
		//MainDriver.KickColor = QBColor(4)  //red (4) or MainDriver.Yellow(6) color
		MainDriver.volPump=0; //added by jaewoo, 20140205
		MainDriver.volPumpKill=0; //added by jaewoo, 20140205

		MainDriver.test_KMW = 0;
		MainDriver.test_ICP = 0;
		MainDriver.test_FCP = 0;

		MainDriver.test_KMW_Theory = 0;
		MainDriver.test_ICP_Theory = 0;
		MainDriver.test_FCP_Theory = 0;
		//
		MainDriver.PointRef=0;
		MainDriver.PointRefPrior=0;
		MainDriver.MinusPoint=0;

		MainDriver.set0_Starttime = 0;
		MainDriver.set0_Finishtime = 0;

		MainDriver.iKickDetect=0;
		MainDriver.kickTimeStart = 0;
		MainDriver.kickTimeFinish = 0;

		// 20150125		
		if(MainDriver.iProblem[0]==0 && MainDriver.iProblem[1]==0){
			MainDriver.probability_problem[1]=91.95/495.0; // differential
			//MainDriver.probability_problem[0]=calc_Pro_Mech_Stk(); // mechanical
			MainDriver.probability_problem[0]=MainDriver.probability_problem[1]/5; // mechanical
		}
		MainDriver.t_problem[0] = 0;
		MainDriver.t_problem[1] = 0;

		MainDriver.ROPen = 60;
		MainDriver.mdBitOff = MainDriver.timeToTD * (0.5 * MainDriver.ROPen) / 3600;
		MainDriver.Torque_Base = 70;
		MainDriver.RPM_Base = 60;
		MainDriver.WOB_Base = 40;
		MainDriver.fc_Base = 0.1;
		MainDriver.Torque_now = MainDriver.Torque_Base;
		MainDriver.RPM_now = MainDriver.RPM_Base;
		MainDriver.WOB_now = MainDriver.WOB_Base;
		MainDriver.fc_now = 0;
		MainDriver.dens_cutting = 2400/0.4536*Math.pow(0.3048, 3)*5.6145/42;//kg/m3 => ppg
		MainDriver.d_cutting = 1.0/8.0; //in

		if(MainDriver.iProblem[3]==1){
			MainDriver.iHuschel = 0;
			MainDriver.numCutting = 0;
			MainDriver.oMud_save = MainDriver.oMud;
			MainDriver.oMud = MainDriver.oMud*(1-MainDriver.fc_Base)+MainDriver.dens_cutting*MainDriver.fc_Base;			
		}

		MainDriver.PermRock[0] = 500;
		MainDriver.PermRock[1] = 5;
		MainDriver.PermRock[2] = 500;
		MainDriver.PoroRock[0] = 0.25;
		MainDriver.PoroRock[1] = 0.25;
		MainDriver.PoroRock[2] = 0.25;
	}


	static void surfEqData(){
		//....Pre-classified data for the surface equipments based on equivalent length
		//		               re-located from property module to here, 8/3/2003
		if(MainDriver.iType==1){
			MainDriver.DiaSP = 4; MainDriver.LengthSP = 45; MainDriver.DiaHose = 3; MainDriver.LengthHose = 55;
			MainDriver.DiaSwivel = 3; MainDriver.LengthSwivel = 6;  MainDriver.DiaKelly = 4; MainDriver.LengthKelly = 40;
			MainDriver.Heqval = 100;
		}
		else if (MainDriver.iType==2){
			MainDriver.DiaSP = 4;  MainDriver.LengthSP = 45; MainDriver.DiaHose = 3;   MainDriver.LengthHose = 55;
			MainDriver.DiaSwivel =  2.5; MainDriver.LengthSwivel = 5;  MainDriver.DiaKelly =  3.25; MainDriver.LengthKelly = 40;
			MainDriver.Heqval = 150;
		}
		else if (MainDriver.iType==3){
			MainDriver.DiaSP =  3.5; MainDriver.LengthSP = 40; MainDriver.DiaHose =  2.5;  MainDriver.LengthHose = 55;
			MainDriver.DiaSwivel =  2.5; MainDriver.LengthSwivel = 5;  MainDriver.DiaKelly =  3.25; MainDriver.LengthKelly = 40;
			MainDriver.Heqval = 250;
		}
		else if (MainDriver.iType==4){
			MainDriver.DiaSP = 3; MainDriver.LengthSP = 40; MainDriver.DiaHose = 2;   MainDriver.LengthHose = 45;
			MainDriver.DiaSwivel = 2; MainDriver.LengthSwivel = 4;  MainDriver.DiaKelly =  2.25; MainDriver.LengthKelly = 40;
			MainDriver.Heqval = 650;
		}
		else{
			MainDriver.DiaSP = 3; MainDriver.LengthSP = 0; MainDriver.DiaHose = 3; MainDriver.LengthHose = 0;
			MainDriver.DiaSwivel = 3; MainDriver.LengthSwivel = 0; MainDriver.DiaKelly = 3; MainDriver.LengthKelly = 0;
			MainDriver.Heqval = 0; MainDriver.DiaSurfLine = 3; MainDriver.LengthSurfLine = 0;
		}
		//
	}

	static void getGeometry(){
		double tempVal=0;
		double x4=0, dd=0, angval=0, rr=0, Xtmp=0, xx=0, angRad=0, ang2rad=0, d4=0, vdarc=0, delVD=0;
		//The followings are expected to simulate directly, from default data or new data file.
		//It is necessary because computer does not calculate all the required variables.
		//
		try{
			MainDriver.pai =  3.1415926535; MainDriver.radConv = MainDriver.pai / 180;
			MainDriver.C12 = 0.25 * MainDriver.pai * 12 / (231 * 42);
			if (MainDriver.iOnshore==1) MainDriver.Dwater = 0;
			//........ Calculate the pump capacity and flow rate-Duplex case
			//if(MainDriver.iPump==2) tempVal = 2 * (2 * Math.pow(MainDriver.DiaLiner,2)- Math.pow(MainDriver.Drod,2));
			//else tempVal = 3 * (Math.pow(MainDriver.DiaLiner,2));

			//MainDriver.Qcapacity =  (MainDriver.C12 * tempVal * MainDriver.strokeLength * MainDriver.Effcy / 12);
			if(MainDriver.iHuschel==1){
				MainDriver.Qdrill = 530;
			}
			else if(MainDriver.iHuschel==2){
				MainDriver.Qdrill = 450;
			}
			else{
				MainDriver.Qdrill = MainDriver.Qcapacity1 * 42 * MainDriver.spMinD1 + MainDriver.Qcapacity2 * 42 * MainDriver.spMinD2;
			}
			MainDriver.Qkill = MainDriver.Qcapacity1 * 42 * MainDriver.spMin1+MainDriver.Qcapacity2 * 42 * MainDriver.spMin2;

			//Well geometry data for vertical, B, BH, BHB, and BHBH cases
			//if MainDriver.iWell==0 Then    //vertical well - no calculation
			if(MainDriver.iWell==1){ //continuous build (DD type 3)
				x4 = MainDriver.Hdisp;       //kick off point & MainDriver.BUR data are compatible
				dd = MainDriver.Vdepth - MainDriver.DepthKOP;
				MainDriver.Rbur =  ((dd * dd + x4 * x4) / 2 / x4);
				MainDriver.BUR = 18000 / (MainDriver.pai * MainDriver.Rbur);
				angval = (MainDriver.Vdepth - MainDriver.DepthKOP) / MainDriver.Rbur;
				MainDriver.angEOB =  (utilityModule.aasin( angval) / MainDriver.radConv);
			}
			//   MainDriver.Hdisp = MainDriver.Rbur * (1 - Math.cos(MainDriver.angEOB * MainDriver.radConv))
			else if (MainDriver.iWell==2){    //build-hold case (DD type II)
				MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR);
				rr = MainDriver.Rbur;
				dd = MainDriver.Vdepth - MainDriver.DepthKOP;
				xx = MainDriver.Hdisp - MainDriver.Rbur;
				Xtmp = dd * dd + xx * xx - rr * rr;
				MainDriver.xHold =  Math.sqrt(Xtmp);
				angval = (rr * dd + xx * MainDriver.xHold) / (rr * rr + Math.pow(MainDriver.xHold,2));
				MainDriver.angEOB =  (utilityModule.aasin(angval) / MainDriver.radConv);
			}

			else if (MainDriver.iWell==3){    //build-hold-build upto 90 deg
				MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR);
				MainDriver.R2bur = 18000 / (MainDriver.pai * MainDriver.BUR2);
				ang2rad = MainDriver.ang2EOB * MainDriver.radConv;
				d4 = MainDriver.Vdepth + MainDriver.R2bur * (1 - Math.sin(ang2rad));
				x4 = MainDriver.Hdisp + MainDriver.R2bur * Math.cos(ang2rad);
				rr = MainDriver.Rbur - MainDriver.R2bur; dd = d4 - MainDriver.DepthKOP - MainDriver.R2bur;
				xx = x4 - MainDriver.Rbur;
				Xtmp = dd * dd + xx * xx - rr * rr;
				MainDriver.xHold =  Math.sqrt(Xtmp);
				angval = (rr * dd + xx * MainDriver.xHold) / (rr * rr + MainDriver.xHold*MainDriver.xHold);
				MainDriver.angEOB =  (utilityModule.aasin(angval) / MainDriver.radConv);
			}
			else if (MainDriver.iWell==4){    //build-hold-build-hold including ERD		        	        
				if(MainDriver.iCase==3 || MainDriver.iCase == 9 || MainDriver.iCase == 10 || MainDriver.iCase==13 || MainDriver.iCase==19 || MainDriver.iCase==20 || MainDriver.iHuschel !=0){	
					MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR);
					MainDriver.R2bur = 18000 / (MainDriver.BUR2*MainDriver.pai);
					angRad = MainDriver.angEOB * MainDriver.radConv;   ang2rad = MainDriver.ang2EOB * MainDriver.radConv;
					MainDriver.HDeob = MainDriver.Hdisp - MainDriver.x2Hold * Math.sin(ang2rad);
					//MainDriver.xHold = (MainDriver.HDeob - (MainDriver.Rbur * (1 - Math.cos(angRad)) + MainDriver.R2bur * (Math.cos(angRad) - Math.cos(ang2rad))))/Math.sin(angRad);		        	
					MainDriver.xHold = (MainDriver.Vdepth-MainDriver.DepthKOP-MainDriver.R2bur)*(MainDriver.Vdepth-MainDriver.DepthKOP-MainDriver.R2bur);
					MainDriver.xHold = MainDriver.xHold + (MainDriver.Hdisp-MainDriver.x2Hold-MainDriver.Rbur)*(MainDriver.Hdisp-MainDriver.x2Hold-MainDriver.Rbur)-(MainDriver.R2bur-MainDriver.Rbur)*(MainDriver.R2bur-MainDriver.Rbur);
					MainDriver.xHold = Math.sqrt(MainDriver.xHold);
					//delVD = MainDriver.Vdepth - MainDriver.DepthKOP;
					//vdarc = delVD - MainDriver.xHold * Math.cos(angRad) - MainDriver.x2Hold * Math.cos(ang2rad);

				}
				else{
					MainDriver.Rbur = 18000 / (MainDriver.pai * MainDriver.BUR);
					MainDriver.R2bur = 18000 / (MainDriver.pai * MainDriver.BUR2);
					angRad = MainDriver.angEOB * MainDriver.radConv;   ang2rad = MainDriver.ang2EOB * MainDriver.radConv;
					vdarc = MainDriver.Rbur * Math.sin(angRad) + MainDriver.R2bur * (Math.sin(ang2rad) - Math.sin(angRad));		     
					delVD = vdarc + MainDriver.xHold * Math.cos(angRad) + MainDriver.x2Hold * Math.cos(ang2rad);
					MainDriver.DepthKOP =  (MainDriver.Vdepth - delVD);
					MainDriver.HDeob =  (MainDriver.Rbur * (1 - Math.cos(angRad)) + MainDriver.R2bur * (Math.cos(angRad) - Math.cos(ang2rad)));
					MainDriver.HDeob =  (MainDriver.HDeob + MainDriver.xHold * Math.sin(angRad));
					MainDriver.Hdisp =  (MainDriver.HDeob + MainDriver.x2Hold * Math.sin(ang2rad));
				}		        
			}
			//-------------------------------------------------------------------------------------
			MainDriver.iKsheet = 0;    //call initial calculation sub. to plot kill sheet
		} catch(Exception e){
			String s ="Input data have been changed and they are not compatible."+"\n"+"You have to review and correct all input data using the Change Input Data command"+"\n"+"from the Main Menu. The sub quits here.";
			JOptionPane.showMessageDialog(null, s);
		}
	}

	static void autoUnitWriter(BufferedWriter bfw, int strLength, int totalLength, String unit){
		try{
			for(int j=0; j<totalLength-strLength; j++) bfw.append(" ");
			bfw.append(unit);
			bfw.newLine();
		}
		catch(IOException Ioe){
			System.out.println("autoUnitWriter Error");
			Ioe.printStackTrace();
		}
	}

	static void autoUnitWriterWOnewLine(BufferedWriter bfw, int strLength, int totalLength, String unit){
		try{
			for(int j=0; j<totalLength-strLength; j++) bfw.append(" ");
			bfw.append(unit);
		}
		catch(IOException Ioe){
			System.out.println("autoUnitWriter Error");
			Ioe.printStackTrace();
		}
	}

	static void autoUnitTextAreaWriter(textResult txtExample, int strLength, int totalLength, String unit){
		//for(int j=0; j<totalLength-strLength; j++) txtExample.textArea.append(" ");
		txtExample.textArea.append("\t");
		txtExample.textArea.append(unit);
		txtExample.textArea.append("\n");
	}

	static void TextAreaAutoUnitWriterWOnewLine(textResult txtExample, int strLength, int totalLength, String unit){
		//for(int j=0; j<totalLength-strLength; j++) txtExample.textArea.append(" ");
		txtExample.textArea.append("\t");
		txtExample.textArea.append(unit);
	}

	static int SaveInTextArea(textResult txtExamp){
		double psia=14.7;
		double tempx=0;

		String s;
		int j=0;

		//----------------------------------  Write the output and plot data
		////if (MainDriver.iKsheet = 0) Then SetMDVD
		//Open fileDat For Output As 6
		txtExamp.textArea.append("\t"+"===== OutPut from SNU/TAMU Well Control Simulator by Dr. Choe =====");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"===================================================================");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\n");

		txtExamp.textArea.append("\t"+"\t"+"--- Input Data ---");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"\t"+"==================");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\n");

		txtExamp.textArea.append("\t"+"Trajectory Type"+"\t\t"+"(1:Multilatrl.)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.igERD));
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Rig Location"+"\t\t"+"(1:Onshore, 2:)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.iOnshore));
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Selected Method"+"\t\t"+"(1:Driller, 2:)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.Method));
		txtExamp.textArea.append("\n");
		/*txtExamp.textArea.append("\t"+"Pump Type"+"\t\t"+"(2:Duplex, 3:)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.iPump));
		txtExamp.textArea.append("\n");*/
		txtExamp.textArea.append("\t"+"Friction Loss"+"\t\t"+"(1:Condsider)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.iDelp));
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Fluid Model"+"\t\t"+"(1:Power law)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.Model)); 
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Gas Deviation"+"\t\t"+"(1:Consider)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.iZfact)); 
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Direc. Well Type"+"\t\t"+"(0:Vertical)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.iWell)); 
		txtExamp.textArea.append("\n");
		//............. for ERD and ML   //after 7/1/02
		txtExamp.textArea.append("\t"+"Mud compressib."+"\t"+"(1:used, 0:not)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.iMudComp));
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Blowout animat."+"\t\t"+"(1:used, 0:not)"+"\t"+"="+"\t"+(new DecimalFormat("#0")).format(MainDriver.iBlowout));
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Shear Stress @ 600 rpm"+"\t\t\t"+"="+"\t"+(new DecimalFormat("###0.##")).format(MainDriver.S600));
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Shear Stress @ 300 rpm"+"\t\t\t"+"="+"\t"+(new DecimalFormat("###0.##")).format(MainDriver.S300)); 
		txtExamp.textArea.append("\n");
		if(MainDriver.Model == 0){
			txtExamp.textArea.append("\t"+"Shear Stress @ 100 rpm"+"\t\t\t"+"="+"\t"+(new DecimalFormat("###0.##")).format(MainDriver.SS100));
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"Shear Stress @ 3 rpm"+"\t\t\t"+"="+"\t"+(new DecimalFormat("###0.##")).format(MainDriver.SS3)); 
			txtExamp.textArea.append("\n");
		}
		s = (new DecimalFormat("##0.##")).format(MainDriver.oMud);
		txtExamp.textArea.append("\t"+"Old Mud Density"+"\t\t\t\t"+"="+"\t"+s+"\t"+"ppg"); 
		txtExamp.textArea.append("\n");
		//
		if (MainDriver.iMudComp == 1){
			s=(new DecimalFormat("#####.0#")).format(MainDriver.MudComp * 1000000);
			txtExamp.textArea.append("\t"+"Mud compressibility"+"\t\t\t"+"="+"\t"+s+"E-06"+"\t\t"+"1/psi");
			txtExamp.textArea.append("\n");
		}

		txtExamp.textArea.append("\t"+"Critical Reynolds number"+"\t\t\t"+"="+"\t"+(new DecimalFormat("####0.##")).format(MainDriver.RenC));
		txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[0]);
		txtExamp.textArea.append("\t"+"Bit Nozzle Size 1"+"\t\t\t"+"="+"\t"+s+"\t\t"+"/32nd in");
		txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[1]);
		txtExamp.textArea.append("\t"+"Bit Nozzle Size 2"+"\t\t\t"+"="+"\t"+s+"\t\t"+"/32nd in");
		txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[2]);
		txtExamp.textArea.append("\t"+"Bit Nozzle Size 3"+"\t\t\t"+"="+"\t"+s+"\t\t"+"/32nd in");
		txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[3]);
		txtExamp.textArea.append("\t"+"Bit Nozzle Size 4"+"\t\t\t"+"="+"\t"+s+"\t\t"+"/32nd in"); 
		txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#0.######")).format(MainDriver.Ruf);
		txtExamp.textArea.append("\t"+"Roughness of Pipe"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in");
		txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\n");

		s=(new DecimalFormat("#####0.#")).format(MainDriver.Vdepth);
		txtExamp.textArea.append("\t"+"True Vertical Depth of Well"+"\t\t"+"="+"\t"+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#####0.#")).format(MainDriver.DepthCasing);
		txtExamp.textArea.append("\t"+"Measured Depth of Casing Seat"+"\t\t"+"="+"\t"+s+"\t\t"+"ft");txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#####0.#")).format(MainDriver.LengthHWDP);
		txtExamp.textArea.append("\t"+"Measured Length of HWDP"+"\t\t"+"="+"\t"+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#####0.#")).format(MainDriver.LengthDC);
		txtExamp.textArea.append("\t"+"Measured Length of DC"+"\t\t\t"+"="+"\t"+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");

		if (MainDriver.iCduct == 1){
			txtExamp.textArea.append("\n");
			s=(new DecimalFormat("#####0.##")).format(MainDriver.DepthCduct);
			txtExamp.textArea.append("\t"+"Depth of Conductor Seat"+"\t\t\t"+"="+"\t"+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");

			s=(new DecimalFormat("##0.###")).format(MainDriver.ODcduct);
			txtExamp.textArea.append("\t"+"OD of Conductor"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		}

		if (MainDriver.iSurfCsg == 1){
			if (MainDriver.iCduct == 0) txtExamp.textArea.append("\n");
			s=(new DecimalFormat("#####0.##")).format(MainDriver.DepthSurfCsg); 
			txtExamp.textArea.append("\t"+"Depth of Surf. Csg Seat"+"\t\t\t"+"="+"\t"+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.ODsurfCsg);
			txtExamp.textArea.append("\t"+"OD of Surface Casing Seat"+"\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		}

		txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.IDcasing);
		txtExamp.textArea.append("\t"+"Inner Diameter of Casing"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.DiaHole);
		txtExamp.textArea.append("\t"+"Diameter of Open Hole"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.doDP);
		txtExamp.textArea.append("\t"+"OD of Drill Pipe"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.diDP);
		txtExamp.textArea.append("\t"+"ID of Drill Pipe"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.doHWDP);
		txtExamp.textArea.append("\t"+"OD of HeviWate DP"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.diHWDP);
		txtExamp.textArea.append("\t"+"ID of HeviWate DP"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.doDC);
		txtExamp.textArea.append("\t"+"OD of Drill Collar"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.diDC);
		txtExamp.textArea.append("\t"+"ID of Drill Collar"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\n");

		s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity1);
		txtExamp.textArea.append("\t"+"Capacity of Pump #1"+"\t\t\t"+"="+"\t"+s+"\t\t"+"bbl/str"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity2);
		txtExamp.textArea.append("\t"+"Capacity of Pump #2"+"\t\t\t"+"="+"\t"+s+"\t\t"+"bbls/str"); txtExamp.textArea.append("\n");
		/*s=(new DecimalFormat("##0.###")).format(MainDriver.DiaLiner);
		txtExamp.textArea.append("\t"+"Liner Size of Pump"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.Drod);
		txtExamp.textArea.append("\t"+"Rod Size of Pump"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n"); 
		s=(new DecimalFormat("##0.###")).format(MainDriver.strokeLength);
		txtExamp.textArea.append("\t"+"Stroke Length of Pump"+"\t\t\t"+"="+"\t"+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#0.####")).format(MainDriver.Effcy);
		txtExamp.textArea.append("\t"+"Pump Efficiency"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"fraction"); txtExamp.textArea.append("\n");*/
		txtExamp.textArea.append("\n");

		s=(new DecimalFormat("####.#")).format(MainDriver.spMinD1);
		txtExamp.textArea.append("\t"+"Strokes  @ Pump 1 Drilling Rate"+"\t\t\t"+"="+"\t"+s+"\t\t"+"st/min"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("####.#")).format(MainDriver.spMin1);
		txtExamp.textArea.append("\t"+"Strokes  @ Pump 1 Kill Rate"+"\t\t\t"+"="+"\t"+s+"\t\t"+"st/min"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("####.#")).format(MainDriver.spMinD2);
		txtExamp.textArea.append("\t"+"Strokes  @ Pump 2 Drilling Rate"+"\t\t\t"+"="+"\t"+s+"\t\t"+"st/min"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("####.#")).format(MainDriver.spMin2);
		txtExamp.textArea.append("\t"+"Strokes  @ Pump 2 Kill Rate"+"\t\t\t"+"="+"\t"+s+"\t\t"+"st/min"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("####0.##")).format(MainDriver.Qdrill);
		txtExamp.textArea.append("\t"+"Flow Rate @ Drilling"+"\t\t\t"+"="+"\t"+s+"\t\t"+"gal/min"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("####0.##")).format(MainDriver.Qkill);
		txtExamp.textArea.append("\t"+"Flow Rate @ Kill"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"st/min"); txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\n");

		s=(new DecimalFormat("###0.###")).format(MainDriver.pitWarn);
		txtExamp.textArea.append("\t"+"Pit Warning Level"+"\t\t\t"+"="+"\t"+s+"\t\t"+"bbls"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.KICKintens);
		txtExamp.textArea.append("\t"+"Kick Intensity"+"\t\t\t\t"+"="+"\t"+s+"\t\t"+"ppg"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#0.###")).format(MainDriver.gasGravity);
		txtExamp.textArea.append("\t"+"Specific Gravity of Gas"+"\t\t\t"+"="+"\t"+s+"\t\t"+"(air=1)");  txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#0.###")).format(MainDriver.fCO2);
		txtExamp.textArea.append("\t"+"Mole Fraction of CO2"+"\t\t\t"+"="+"\t"+s+"\t\t"+"fraction"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#0.###")).format(MainDriver.fH2S);
		txtExamp.textArea.append("\t"+"Mole Fraction of H2S"+"\t\t\t"+"="+"\t"+s+"\t\t"+"fraction"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("##0.###")).format(MainDriver.Tsurf);
		txtExamp.textArea.append("\t"+"Surface Temperature"+"\t\t\t"+"="+"\t"+s+"\t\t"+"'F"); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#0.###")).format(MainDriver.Tgrad);
		txtExamp.textArea.append("\t"+"Mud Temperature Gradient"+"\t\t"+"="+"\t"+s+"\t\t"+"'F/100 ft"); txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\n");

		s=(new DecimalFormat("####0.###")).format(MainDriver.Perm);
		txtExamp.textArea.append("\t"+"Formation Permeability"+"\t\t\t"+"="+"\t"+s+"\t\t"+"md"); txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\t"+"Formation Skin Factor"+"\t\t\t"+"="+"\t"+(new DecimalFormat("###0.###")).format(MainDriver.Skins)); txtExamp.textArea.append("\n");
		s=(new DecimalFormat("#0.###")).format(MainDriver.Porosity);
		txtExamp.textArea.append("\t"+"Formation Porosity"+"\t\t\t"+"="+"\t"+s+"\t\t"+"fraction"); txtExamp.textArea.append("\n");
		if(MainDriver.igERD == 1){
			s=(new DecimalFormat("##0.###")).format(MainDriver.gPayLength);
			txtExamp.textArea.append("\t"+"Drilled Pay Zone Length"+"\t\t\t"+"="+"\t"+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
		}
		else{
			s=(new DecimalFormat("##0.###")).format(MainDriver.ROPen);
			txtExamp.textArea.append("\t"+"Rate of Penetration"+"\t\t\t"+"="+"\t"+s+"\t\t"+"ft/hr"); txtExamp.textArea.append("\n");
		}
		txtExamp.textArea.append("\t"+"PP/FP(0:InPut,1:Eaton,2:Barker)"+"\t\t"+"="+"\t"+(new DecimalFormat("##0")).format(MainDriver.iFG)); txtExamp.textArea.append("\n");
		txtExamp.textArea.append("\n");

		if(MainDriver.iFG == 0){   //for user input
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"Number of input data"+"\t\t\t\t"+"="+"\t"+" "+(new DecimalFormat("#0")).format(MainDriver.iFGnum)); 
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("    ---------  ---------  ---------");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"Depth BML"+"\t"+"Pore P."+"\t"+"Frac. P.");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"(ft)"+"\t"+"(psi)"+"\t"+"(psi)");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"---------"+"\t"+"---------"+"\t"+"---------");
			txtExamp.textArea.append("\n");
			for(int i = 0; i < MainDriver.iFGnum; i++){
				s = (new DecimalFormat("#####0.#")).format(MainDriver.PPdepth[i]);
				txtExamp.textArea.append("    " + s);
				for(j=0; j<9-s.length(); j++){
					txtExamp.textArea.append(" ");
				}
				s=(new DecimalFormat("#####0.#")).format(MainDriver.PoreP[i]);
				txtExamp.textArea.append("  " + s);
				for(j=0; j<9-s.length(); j++){
					txtExamp.textArea.append(" ");
				}
				txtExamp.textArea.append("  " + (new DecimalFormat("#####0.#")).format(MainDriver.FracP[i]));
				txtExamp.textArea.append("\n");
			}
		}

		if (MainDriver.iType >= 1 && MainDriver.iType <= 4){
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"Type of Connections"+"\t\t\t"+"="+"\t"+" "+(new DecimalFormat("##")).format(MainDriver.iType)); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("####.#")).format(MainDriver.Heqval);
			txtExamp.textArea.append("\t"+"3 in Equiv. Length"+"\t\t\t"+"="+"\t"+" "+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("###0.##")).format(MainDriver.LengthSurfLine);				
			txtExamp.textArea.append("\t"+"Length of Surface Line"+"\t\t\t"+"="+ s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.DiaSurfLine);
			txtExamp.textArea.append("\t"+"ID of Surface Line"+"\t\t\t"+"="+"\t"+" "+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
			tempx = MainDriver.volDS[MainDriver.NwcE + 1];
			s=(new DecimalFormat("###0.###")).format(tempx);
			txtExamp.textArea.append("\t"+"Volume(Pump to SP)"+"\t\t\t"+"="+"\t"+" "+s+"\t\t"+"bbls"); txtExamp.textArea.append("\n");
			tempx = MainDriver.volDS[MainDriver.NwcE];
			s=(new DecimalFormat("###0.###")).format(tempx);
			txtExamp.textArea.append("\t"+"Volume(SP to Kelly)"+"\t\t\t"+"="+"\t"+" "+s+"\t\t"+"bbls"); txtExamp.textArea.append("\n");
		}

		if (MainDriver.iOnshore == 2){
			txtExamp.textArea.append("\n");
			s=(new DecimalFormat("####0.#")).format(MainDriver.Dwater); 
			txtExamp.textArea.append("\t"+"Depth of Water"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("#0.###")).format(MainDriver.tWgrad);
			txtExamp.textArea.append("\t"+"Offshore Temperature Gradient"+"\t\t"+"="+"\t"+" "+s+"\t\t"+"'F/100 ft"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("#0")).format(MainDriver.iChoke);
			txtExamp.textArea.append("\t"+"Choke Valve Status (1:open)"+"\t\t"+"="+"\t"+" "+s);
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"Kill Valve Status  (1:open)"+"\t\t"+"="+"\t"+" "+(new DecimalFormat("#0")).format(MainDriver.iKill));
			txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Dchoke); 
			txtExamp.textArea.append("\t"+"ID of Choke Line"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Dkill);
			txtExamp.textArea.append("\t"+"ID of Kill Line"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Driser);
			txtExamp.textArea.append("\t"+"ID of Marine Riser"+"\t\t\t"+"="+"\t"+" "+s+"\t\t"+"in"); txtExamp.textArea.append("\n");
		}

		if (MainDriver.iDatPrn == 1){  //just print out the input data only
			MainDriver.iDatPrn = 0;
			return 0;
		}
		//................................ print out initial calculation results
		//
		if (MainDriver.Np >= 2){
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"--- Results from Initial Calculations ---");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"=========================================");
			txtExamp.textArea.append("\n");
			s=(new DecimalFormat("#####0.##")).format(MainDriver.Pb - psia);
			txtExamp.textArea.append("\t"+"Bottomhole Pressure Maintained"+"\t\t"+"="+"\t"+" "+s+"\t\t"+"psig"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Kmud);
			txtExamp.textArea.append("\t"+"Kill Mud Weight"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"ppg"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity1);
			txtExamp.textArea.append("\t"+"Pump 1 Capacity"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"bbl/str"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity2);
			txtExamp.textArea.append("\t"+"Pump 2 Capacity"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"bbl/str"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("####0.##")).format(MainDriver.SIDPP - psia);
			txtExamp.textArea.append("\t"+"Shut-in Drill Pipe Pressure"+"\t\t"+"="+"\t"+" "+s+"\t\t"+"psig"); txtExamp.textArea.append("\n");
			s=(new DecimalFormat("####0.##")).format(MainDriver.SICP - psia);
			txtExamp.textArea.append("\t"+"Shut-in Casing Pressure"+"\t\t\t"+"="+"\t"+" "+s+"\t\t"+"psig"); txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\n");
			//---------------------Directional Data Print Out !

			if (MainDriver.iWell >= 1){
				txtExamp.textArea.append("\n");
				txtExamp.textArea.append("\t"+"--- Directional Wellbore Information ---");
				txtExamp.textArea.append("\n");
				s=(new DecimalFormat("##0.##")).format(MainDriver.BUR);
				txtExamp.textArea.append("\t"+"First Build-Up Rate (BUR)"+"\t\t"+"="+"\t"+" "+s+"\t\t"+"deg/100 ft"); txtExamp.textArea.append("\n");
				s=(new DecimalFormat("#####0.##")).format(MainDriver.Rbur);
				txtExamp.textArea.append("\t"+"Radius of Curvature @ 1st"+"\t\t"+"="+"\t"+" "+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			}
			if (MainDriver.iWell >= 2){
				s=(new DecimalFormat("####0.##")).format(MainDriver.xHold);
				txtExamp.textArea.append("\t"+"First Hold Length"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			}
			if (MainDriver.iWell >= 3){
				s=(new DecimalFormat("##0.##")).format(MainDriver.BUR2);
				txtExamp.textArea.append("\t"+"Second Build-Up Rate"+"\t\t"+"="+"\t"+" "+s+"\t\t"+"deg/100 ft"); txtExamp.textArea.append("\n");
				s=(new DecimalFormat("#####0.##")).format(MainDriver.R2bur);
				txtExamp.textArea.append("\t"+"Radius of Curvature @ 2nd"+"\t\t"+"="+"\t"+" "+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			}
			if (MainDriver.iWell == 4){
				s=(new DecimalFormat("#####0.##")).format(MainDriver.x2Hold);
				txtExamp.textArea.append("\t"+"Second Hold Length"+"\t\t\t\t"+"="+"\t"+" "+s+"\t\t"+"ft"); txtExamp.textArea.append("\n");
			}

			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"Total MD"+"\t"+"True VD"+"\t\t"+"Angle"+"\t\t"+"ID of DS"+"\t"+"OD of Ann"+"\t"+"ID of Ann");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"(ft)"+"\t\t"+"(ft)"+"\t\t"+"(degree)"+"\t"+"(inch)"+"\t\t"+"(inch)"+"\t\t"+"(inch)");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"--------"+"\t"+"-------"+"\t\t"+"--------"+"\t"+"---------"+"\t"+"----------"+"\t"+"---------");
			txtExamp.textArea.append("\n");
			for(int i = MainDriver.NwcE-1; i>MainDriver.NwcS-2; i--){
				s=(new DecimalFormat("#####0.#")).format(MainDriver.TMD[i]);
				txtExamp.textArea.append("\t"+s);
				s=(new DecimalFormat("#####0.#")).format(MainDriver.TVD[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.##")).format(MainDriver.ang2p[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("####0.###")).format(MainDriver.DiDS[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("####0.###")).format(MainDriver.Do2p[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("####0.###")).format(MainDriver.Di2p[i]);
				txtExamp.textArea.append("\t\t"+s);
				txtExamp.textArea.append("\n");
			}
			txtExamp.textArea.append("\t"+"--------"+"\t"+"-------"+"\t\t"+"--------"+"\t"+"---------"+"\t"+"----------"+"\t"+"---------");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\n");

			//.............................. ........ print out final results
			txtExamp.textArea.append("\t"+"--- Results from SNU/TAMU Well Control Simulator ---");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"====================================================");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\n");

			txtExamp.textArea.append("\t"+"Time"+"\t\t"+"Xtop"+"\t\t"+"Xbotm"+"\t\t"+"Px@Top"+"\t"+"Pit Vol"+"\t\t"+"Kick Density");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"(mins)"+"\t\t"+"(ft)"+"\t\t"+"(ft)"+"\t\t"+"(psig)"+"\t\t"+"(bbls)"+"\t\t"+"(ppg)");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"------"+"\t\t"+"----"+"\t\t"+"-----"+"\t\t"+"------"+"\t\t"+"-------"+"\t\t"+"------------");
			txtExamp.textArea.append("\n");
			for(int i = 0; i <= MainDriver.Np; i++){
				s=(new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60);
				txtExamp.textArea.append("\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.xTop[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.xBot[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.pxTop[i] - psia);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.00")).format(MainDriver.Vpit[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.00")).format(MainDriver.rhoK[i]);
				txtExamp.textArea.append("\t\t"+s);
				txtExamp.textArea.append("\n");
				//	    if (Int(i / 56) * 56 = i) Then
				//	    End if
			}
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\n");

			txtExamp.textArea.append("\t"+"Time"+"\t\t"+"Pump P"+"\t\t"+"Stand PP"+"\t"+"Choke P"+"\t"+"CsgSeat P"+"\t"+"BHP"+"\t\t"+"P@Mudline");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"(mins)"+"\t\t"+"(psig)"+"\t\t"+"(psig)"+"\t\t"+"(psig)"+"\t\t"+"(psig)"+"\t\t"+"(psig)"+"\t\t"+"(psig)");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"------"+"\t\t"+"------"+"\t\t"+"--------"+"\t"+"-------"+"\t\t"+"---------"+"\t"+"------"+"\t\t"+"---------");
			txtExamp.textArea.append("\n");
			for(int i = 0; i<=MainDriver.Np; i++){
				s=(new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60);
				txtExamp.textArea.append("\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.Ppump[i] - psia);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.Psp[i] - psia);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.Pchk[i] - psia);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.Pcsg[i] - psia);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.Pb2p[i] - psia);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.PmLine[i] - psia);
				txtExamp.textArea.append("\t\t"+s);
				txtExamp.textArea.append("\n");
				//	    if (Int(i / 56) * 56 = i) Then
				//	    End if
			}
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\n");

			txtExamp.textArea.append("\t"+"Time"+"\t\t"+"Strokes"+"\t\t"+"Vol Circ"+"\t\t"+"ChK Open"+"\t"+"InFlux"+"\t\t"+"Mud Rate"+"\t"+"Gas Rate");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"(mins)"+"\t\t"+"(#)"+"\t\t"+"(bbls)"+"\t\t"+"Dia Ratio"+"\t"+"(Mcf/D)"+"\t\t"+"(gpm)"+"\t\t"+"(Mcf/Day)");
			txtExamp.textArea.append("\n");
			txtExamp.textArea.append("\t"+"------"+"\t\t"+"-------"+"\t\t"+"--------"+"\t"+"---(%)---"+"\t"+"--------"+"\t"+"--------"+"\t"+"---------");
			txtExamp.textArea.append("\n");
			for(int i = 0; i<=MainDriver.Np; i++){
				s=(new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60);
				txtExamp.textArea.append("\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.Stroke[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.00")).format(MainDriver.VOLcir[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.CHKopen[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.QmcfDay[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("#####0.0")).format(MainDriver.mudflow[i]);
				txtExamp.textArea.append("\t\t"+s);
				s=(new DecimalFormat("######0.0")).format(MainDriver.gasflow[i]);
				txtExamp.textArea.append("\t\t"+s);
				txtExamp.textArea.append("\n");
				//	    if (Int(i / 56) * 56 = i) Then
				//	    End if
			}
		}
		return 1;		
		//
	}

	static int SaveAsFile(){
		double psia=14.7;
		double tempx=0;

		String s;
		int j=0;
		try{
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Sample.txt", false));
			//----------------------------------  Write the output and plot data
			////if (MainDriver.iKsheet = 0) Then SetMDVD
			//Open fileDat For Output As 6
			bufferedWriter.append("      ===== OutPut from SNU/TAMU Well Control Simulator by Dr. Choe =====");
			bufferedWriter.newLine();
			bufferedWriter.append("      ===================================================================");
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.append("          --- Input Data ---");
			bufferedWriter.newLine();
			bufferedWriter.append("          ==================");
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.append("     Trajectory Type (1;Multilatrl.)="+"      "+(new DecimalFormat("#0")).format(MainDriver.igERD));
			bufferedWriter.newLine();
			bufferedWriter.append("     Rig Location    (1:Onshore, 2:)="+"      "+(new DecimalFormat("#0")).format(MainDriver.iOnshore));
			bufferedWriter.newLine();
			bufferedWriter.append("     Selected Method (1:Driller, 2:)="+"      "+(new DecimalFormat("#0")).format(MainDriver.Method));
			bufferedWriter.newLine();
			/*bufferedWriter.append("     Pump Type       (2:Duplex, 3:) ="+"      "+(new DecimalFormat("#0")).format(MainDriver.iPump));
			bufferedWriter.newLine();*/
			bufferedWriter.append("     Friction Loss   (1:Condsider)  ="+"      "+(new DecimalFormat("#0")).format(MainDriver.iDelp));
			bufferedWriter.newLine();
			bufferedWriter.append("     Fluid Model     (1:Power law)  ="+"      "+(new DecimalFormat("#0")).format(MainDriver.Model)); 
			bufferedWriter.newLine();
			bufferedWriter.append("     Gas Deviation   (1:Consider)   ="+"      "+(new DecimalFormat("#0")).format(MainDriver.iZfact)); 
			bufferedWriter.newLine();
			bufferedWriter.append("     Direc. Well Type(0:Vertical)   ="+"      "+(new DecimalFormat("#0")).format(MainDriver.iWell)); 
			bufferedWriter.newLine();
			//............. for ERD and ML   //after 7/1/02
			bufferedWriter.append("     Mud compressib. (1:used, 0:not)="+"      "+(new DecimalFormat("#0")).format(MainDriver.iMudComp));
			bufferedWriter.newLine();
			bufferedWriter.append("     Blowout animat. (1:used, 0:not)="+"      "+(new DecimalFormat("#0")).format(MainDriver.iBlowout));
			bufferedWriter.newLine();
			bufferedWriter.append("     Shear Stress @ 600 rpm         ="+"      "+(new DecimalFormat("###0.##")).format(MainDriver.S600));
			bufferedWriter.newLine();
			bufferedWriter.append("     Shear Stress @ 300 rpm         ="+"      "+(new DecimalFormat("###0.##")).format(MainDriver.S300)); 
			bufferedWriter.newLine();
			if(MainDriver.Model == 0){
				bufferedWriter.append("     Shear Stress @ 100 rpm         ="+"      "+(new DecimalFormat("###0.##")).format(MainDriver.SS100));
				bufferedWriter.newLine();
				bufferedWriter.append("     Shear Stress @ 3 rpm           ="+"      "+(new DecimalFormat("###0.##")).format(MainDriver.SS3)); 
				bufferedWriter.newLine();
			}
			s = (new DecimalFormat("##0.##")).format(MainDriver.oMud);
			bufferedWriter.append("     Old Mud Density                ="+"      "+s); 
			autoUnitWriter(bufferedWriter, s.length(), 14, "ppg");
			//
			if (MainDriver.iMudComp == 1){
				s=(new DecimalFormat("#####.0#")).format(MainDriver.MudComp * 1000000);
				bufferedWriter.append("     Mud compressibility            ="+"      "+s+"E-06");
				autoUnitWriter(bufferedWriter, s.length()+4, 14, "1/psi");
			}
			bufferedWriter.append("     Critical Reynolds number       ="+"      "+(new DecimalFormat("####0.##")).format(MainDriver.RenC));
			bufferedWriter.newLine();
			s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[0]);
			bufferedWriter.append("     Bit Nozzle Size 1              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "/32nd in"); 
			s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[1]);
			bufferedWriter.append("     Bit Nozzle Size 2              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "/32nd in");
			s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[2]);
			bufferedWriter.append("     Bit Nozzle Size 3              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "/32nd in");
			s=(new DecimalFormat("##0.##")).format(MainDriver.Dnoz[3]);
			bufferedWriter.append("     Bit Nozzle Size 4              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "/32nd in"); 
			s=(new DecimalFormat("#0.######")).format(MainDriver.Ruf);
			bufferedWriter.append("     Roughness of Pipe              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			bufferedWriter.newLine();

			s=(new DecimalFormat("#####0.#")).format(MainDriver.Vdepth);
			bufferedWriter.append("     True Vertical Depth of Well    ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "ft"); 
			s=(new DecimalFormat("#####0.#")).format(MainDriver.DepthCasing);
			bufferedWriter.append("     Measured Depth of Casing Seat  ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
			s=(new DecimalFormat("#####0.#")).format(MainDriver.LengthHWDP);
			bufferedWriter.append("     Measured Length of HWDP        ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "ft"); 
			s=(new DecimalFormat("#####0.#")).format(MainDriver.LengthDC);
			bufferedWriter.append("     Measured Length of DC          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "ft");

			if (MainDriver.iCduct == 1){
				bufferedWriter.newLine();
				s=(new DecimalFormat("#####0.##")).format(MainDriver.DepthCduct);
				bufferedWriter.append("     Depth of Conductor Seat        ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				s=(new DecimalFormat("##0.###")).format(MainDriver.ODcduct);
				bufferedWriter.append("     OD of Conductor                ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			}

			if (MainDriver.iSurfCsg == 1){
				if (MainDriver.iCduct == 0) bufferedWriter.newLine();
				s=(new DecimalFormat("#####0.##")).format(MainDriver.DepthSurfCsg);
				bufferedWriter.append("     Depth of Surf. Csg Seat        ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "ft"); 
				s=(new DecimalFormat("##0.###")).format(MainDriver.ODsurfCsg);
				bufferedWriter.append("     OD of Surface Casing Seat      ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "in"); 
			}

			bufferedWriter.newLine();
			s=(new DecimalFormat("##0.###")).format(MainDriver.IDcasing);
			bufferedWriter.append("     Inner Diameter of Casing       ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in"); 
			s=(new DecimalFormat("##0.###")).format(MainDriver.DiaHole);
			bufferedWriter.append("     Diameter of Open Hole          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			s=(new DecimalFormat("##0.###")).format(MainDriver.doDP);
			bufferedWriter.append("     OD of Drill Pipe               ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in"); 
			s=(new DecimalFormat("##0.###")).format(MainDriver.diDP);
			bufferedWriter.append("     ID of Drill Pipe               ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			s=(new DecimalFormat("##0.###")).format(MainDriver.doHWDP);
			bufferedWriter.append("     OD of HeviWate DP              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			s=(new DecimalFormat("##0.###")).format(MainDriver.diHWDP);
			bufferedWriter.append("     ID of HeviWate DP              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			s=(new DecimalFormat("##0.###")).format(MainDriver.doDC);
			bufferedWriter.append("     OD of Drill Collar             ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			s=(new DecimalFormat("##0.###")).format(MainDriver.diDC);
			bufferedWriter.append("     ID of Drill Collar             ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			bufferedWriter.newLine();

			s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity1);
			bufferedWriter.append("     Pump 1 Capacity                  ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "bbl/str");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity2);
			bufferedWriter.append("     Pump 2 Capacity                  ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "bbl/str");
			/*s=(new DecimalFormat("##0.###")).format(MainDriver.DiaLiner);
			bufferedWriter.append("     Liner Size of Pump             ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Drod);
			bufferedWriter.append("     Rod Size of Pump               ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in"); 
			s=(new DecimalFormat("##0.###")).format(MainDriver.strokeLength);
			bufferedWriter.append("     Stroke Length of Pump          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			s=(new DecimalFormat("#0.####")).format(MainDriver.Effcy);
			bufferedWriter.append("     Pump Efficiency                ="+"      "+s);*/
			autoUnitWriter(bufferedWriter, s.length(), 14, "fraction");
			bufferedWriter.newLine();

			s=(new DecimalFormat("####.#")).format(MainDriver.spMinD1);
			bufferedWriter.append("     Strokes  @ Pump 1 Drilling Rate      ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "st/min");
			s=(new DecimalFormat("####.#")).format(MainDriver.spMin1);
			bufferedWriter.append("     Strokes  @ Pump 1 Kill Rate          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "st/min");
			s=(new DecimalFormat("####.#")).format(MainDriver.spMinD2);
			bufferedWriter.append("     Strokes  @ Pump 2 Drilling Rate      ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "st/min");
			s=(new DecimalFormat("####.#")).format(MainDriver.spMin2);
			bufferedWriter.append("     Strokes  @ Pump 2 Kill Rate          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "st/min");

			s=(new DecimalFormat("####0.##")).format(MainDriver.Qdrill);
			bufferedWriter.append("     Flow Rate @ Drilling          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "gal/min");
			s=(new DecimalFormat("####0.##")).format(MainDriver.Qkill);
			bufferedWriter.append("     Flow Rate @ Kill              ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "st/min");
			bufferedWriter.newLine();

			s=(new DecimalFormat("###0.###")).format(MainDriver.pitWarn);
			bufferedWriter.append("     Pit Warning Level             ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "bbls");
			s=(new DecimalFormat("##0.###")).format(MainDriver.KICKintens);
			bufferedWriter.append("     Kick Intensity                ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "ppg");
			s=(new DecimalFormat("#0.###")).format(MainDriver.gasGravity);
			bufferedWriter.append("     Specific Gravity of Gas       ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "(air=1)"); 
			s=(new DecimalFormat("#0.###")).format(MainDriver.fCO2);
			bufferedWriter.append("     Mole Fraction of CO2          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "fraction"); 
			s=(new DecimalFormat("#0.###")).format(MainDriver.fH2S);
			bufferedWriter.append("     Mole Fraction of H2S          ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "fraction");
			s=(new DecimalFormat("##0.###")).format(MainDriver.Tsurf);
			bufferedWriter.append("     Surface Temperature           ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "'F");
			s=(new DecimalFormat("#0.###")).format(MainDriver.Tgrad);
			bufferedWriter.append("     Mud Temperature Gradient      ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "'F/100 ft");
			bufferedWriter.newLine();

			s=(new DecimalFormat("####0.###")).format(MainDriver.Perm);
			bufferedWriter.append("     Formation Permeability        ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "md");
			bufferedWriter.append("     Formation Skin Factor         ="+"      "+(new DecimalFormat("###0.###")).format(MainDriver.Skins));
			bufferedWriter.newLine();
			s=(new DecimalFormat("#0.###")).format(MainDriver.Porosity);
			bufferedWriter.append("     Formation Porosity            ="+"      "+s);
			autoUnitWriter(bufferedWriter, s.length(), 14, "fraction");
			if(MainDriver.igERD == 1){
				s=(new DecimalFormat("##0.###")).format(MainDriver.gPayLength);
				bufferedWriter.append("     Drilled Pay Zone Length        ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
			}
			else{
				s=(new DecimalFormat("##0.###")).format(MainDriver.ROPen);
				bufferedWriter.append("     Rate of Penetration            ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "ft/hr");
			}
			bufferedWriter.append("     PP/FP(0:InPut,1:Eaton,2:Barker)="+"      "+(new DecimalFormat("##0")).format(MainDriver.iFG)); 
			bufferedWriter.newLine();

			if(MainDriver.iFG == 0){   //for user input
				bufferedWriter.newLine();
				bufferedWriter.append("     Number of input data           ="+"      "+(new DecimalFormat("#0")).format(MainDriver.iFGnum)); 
				bufferedWriter.newLine();
				bufferedWriter.append("    ---------  ---------  ---------");
				bufferedWriter.newLine();
				bufferedWriter.append("    Depth BML   Pore P.    Frac. P.");
				bufferedWriter.newLine();
				bufferedWriter.append("      (ft)       (psi)      (psi)  ");
				bufferedWriter.newLine();
				bufferedWriter.append("    ---------  ---------  ---------");
				bufferedWriter.newLine();
				for(int i = 0; i < MainDriver.iFGnum; i++){
					s = (new DecimalFormat("#####0.#")).format(MainDriver.PPdepth[i]);
					bufferedWriter.append("    " + s);
					for(j=0; j<9-s.length(); j++){
						bufferedWriter.append(" ");
					}
					s=(new DecimalFormat("#####0.#")).format(MainDriver.PoreP[i]);
					bufferedWriter.append("  " + s);
					for(j=0; j<9-s.length(); j++){
						bufferedWriter.append(" ");
					}
					bufferedWriter.append("  " + (new DecimalFormat("#####0.#")).format(MainDriver.FracP[i]));
					bufferedWriter.newLine();
				}
			}

			if (MainDriver.iType >= 1 && MainDriver.iType <= 4){
				bufferedWriter.newLine();
				bufferedWriter.append("     Type of Connections            ="+"      "+(new DecimalFormat("##")).format(MainDriver.iType));
				s=(new DecimalFormat("####.#")).format(MainDriver.Heqval);
				bufferedWriter.append("     3 in Equiv. Length             ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				s=(new DecimalFormat("###0.##")).format(MainDriver.LengthSurfLine);				
				bufferedWriter.append("     Length of Surface Line         ="+ s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				s=(new DecimalFormat("##0.###")).format(MainDriver.DiaSurfLine);
				bufferedWriter.append("     ID of Surface Line             ="+"      "+s); 
				autoUnitWriter(bufferedWriter, s.length(), 14, "in");
				tempx = MainDriver.volDS[MainDriver.NwcE + 1];
				s=(new DecimalFormat("###0.###")).format(tempx);
				bufferedWriter.append("     Volume(Pump to SP)             ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "bbls");
				tempx = MainDriver.volDS[MainDriver.NwcE];
				s=(new DecimalFormat("###0.###")).format(tempx);
				bufferedWriter.append("     Volume(SP to Kelly)            ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "bbls");
			}

			if (MainDriver.iOnshore == 2){
				bufferedWriter.newLine();
				s=(new DecimalFormat("####0.#")).format(MainDriver.Dwater);
				bufferedWriter.append("     Depth of Water                 ="+"      "+s); 
				autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				s=(new DecimalFormat("#0.###")).format(MainDriver.tWgrad);
				bufferedWriter.append("     Offshore Temperature Gradient  ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "'F/100 ft");
				s=(new DecimalFormat("#0")).format(MainDriver.iChoke);
				bufferedWriter.append("     Choke Valve Status (1:open)    ="+"      "+s);
				bufferedWriter.newLine();
				bufferedWriter.append("     Kill Valve Status  (1:open)    ="+"      "+(new DecimalFormat("#0")).format(MainDriver.iKill)); 
				bufferedWriter.newLine();
				s=(new DecimalFormat("##0.###")).format(MainDriver.Dchoke);
				bufferedWriter.append("     ID of Choke Line               ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "in");
				s=(new DecimalFormat("##0.###")).format(MainDriver.Dkill);
				bufferedWriter.append("     ID of Kill Line                ="+"      "+s); 
				autoUnitWriter(bufferedWriter, s.length(), 14, "in");
				s=(new DecimalFormat("##0.###")).format(MainDriver.Driser);
				bufferedWriter.append("     ID of Marine Riser             ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "in");
			}
			//................. ML data, 2003/8/02
			/*if(MainDriver.igERD == 1){
		Print 6, //===>> Multilateral & Trip data"
		bufferedWriter.append("     Trip Tank Volume               ="+"      "+(new DecimalFormat("")).format(MainDriver.gTripTankVolume); autoUnitWriter(bufferedWriter, s.length(), 14, "bbls");
		bufferedWriter.append("     Trip Tank Height               ="+"      "+(new DecimalFormat("")).format(MainDriver.gTripTankHeight); autoUnitWriter(bufferedWriter, s.length(), 14, "bbls");
		bufferedWriter.append("     Trip Joint Connection Time     ="+"      "+(new DecimalFormat("")).format(MainDriver.gConnTime); autoUnitWriter(bufferedWriter, s.length(), 14, "sec");
		bufferedWriter.append("     Trip Joint Length              ="+"      "+(new DecimalFormat("")).format(MainDriver.gJointLength); autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
		bufferedWriter.append("     Trip Joint Number per Stand    ="+"      "+(new DecimalFormat("")).format(MainDriver.igJointNumber)); bufferedWriter.newLine();
		bufferedWriter.append("     Min P. Increase before Bleed   ="+"      "+(new DecimalFormat("")).format(MainDriver.gBleedPressure); autoUnitWriter(bufferedWriter, s.length(), 14, "psi");
		bufferedWriter.append("     Time Duration for P. Bleeding  ="+"      "+(new DecimalFormat("")).format(MainDriver.gBleedTime); autoUnitWriter(bufferedWriter, s.length(), 14, "sec");
		Print 6,
		bufferedWriter.append("     No. of Multilateral Trajectory ="+"      "+(new DecimalFormat("")).format(MainDriver.igMLnumber)); bufferedWriter.newLine();
		bufferedWriter.append("     -----": Tab(12): "------": Tab(21): "------": Tab(30): "-----": Tab(37): "------": Tab(45): "-----": Tab(52): "-----": Tab(60): "------": Tab(68): "------": Tab(76): "-----"
		bufferedWriter.append("     Plug ": Tab(12): "KOP MD": Tab(21): " TVD  ": Tab(30): "Angle": Tab(37): " BUR  ": Tab(45): "Angle": Tab(52): "Hold ": Tab(60): " MainDriver.BUR2 ": Tab(68): "Angle2": Tab(76): "Hold2"
		bufferedWriter.append("     (1/0)": Tab(12): "  ft  ": Tab(21): " ft   ": Tab(30): "deg. ": Tab(37): "/100ft": Tab(45): "deg. ": Tab(52): " ft  ": Tab(60): "/100ft": Tab(68): " deg. ": Tab(76): " ft  "
		bufferedWriter.append("     -----": Tab(12): "------": Tab(21): "------": Tab(30): "-----": Tab(37): "------": Tab(45): "-----": Tab(52): "-----": Tab(60): "------": Tab(68): "------": Tab(76): "-----"
		For ii = 0 To (MainDriver.igMLnumber - 1)
		Print 6, Tab(6): Format(MainDriver.mlPlug[ii))); bufferedWriter.newLine();:
		Print 6, Tab(12): Format(MainDriver.mlKOP[ii));
		Print 6, Tab(21): Format(MainDriver.mlKOPvd(ii));
		Print 6, Tab(30): Format(MainDriver.mlKOPang(ii));
		Print 6, Tab(38): Format(MainDriver.mlBUR[ii));
		Print 6, Tab(45): Format(MainDriver.mlEOB[ii));
		Print 6, Tab(52): Format(MainDriver.mlHold[ii));
		Print 6, Tab(61): Format(MainDriver.mlBUR2nd[ii));
		Print 6, Tab(68): Format(MainDriver.mlEOB2nd[ii));
		Print 6, Tab(76): Format(MainDriver.mlHold2nd[ii));
		Next ii
		Print 6,
		bufferedWriter.append("     -------": Tab(14): "-------": Tab(23): "------": Tab(32): "------": Tab(40): "------": Tab(48): "-------------"
		bufferedWriter.append("     Hole ID": Tab(14): "Form. P": Tab(23): "Perm. ": Tab(32): "Poros.": Tab(40): "MainDriver.Skins ": Tab(48): "Flow Interval"
		bufferedWriter.append("      inch  ": Tab(14): "  psig ": Tab(23): " md   ": Tab(32): "fract.": Tab(40): "D_less": Tab(48): " ft "
		bufferedWriter.append("     -------": Tab(14): "-------": Tab(23): "------": Tab(32): "------": Tab(40): "------": Tab(48): "-------------"
		For ii = 0 To (MainDriver.igMLnumber - 1)
		Print 6, Tab(6): Format(MainDriver.mlDia(ii));
		Print 6, Tab(14): Format(MainDriver.mlPform(ii));
		Print 6, Tab(23): Format(MainDriver.mlPerm(ii));
		Print 6, Tab(32): Format(MainDriver.mlPorosity(ii));
		Print 6, Tab(40): Format(MainDriver.mlSkin(ii));
		Print 6, Tab(49): Format(MainDriver.mlHeff(ii));
		Next ii
			}   //..... of ERD = 1
		//*/
			if (MainDriver.iDatPrn == 1){  //just print out the input data only
				MainDriver.iDatPrn = 0;
				bufferedWriter.close();
				return 0;
			}
			//................................ print out initial calculation results
			//
			if (MainDriver.Np >= 2){
				bufferedWriter.newLine();
				bufferedWriter.append("     --- Results from Initial Calculations ---");
				bufferedWriter.newLine();
				bufferedWriter.append("     =========================================");
				bufferedWriter.newLine();
				s=(new DecimalFormat("#####0.##")).format(MainDriver.Pb - psia);
				bufferedWriter.append("     Bottomhole Pressure Maintained ="+"      "+s); 
				autoUnitWriter(bufferedWriter, s.length(), 14, "psig");
				s=(new DecimalFormat("##0.###")).format(MainDriver.Kmud);
				bufferedWriter.append("     Kill Mud Weight                ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "ppg");
				s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity1);
				bufferedWriter.append("     Pump Capacity                  ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "bbl/str");
				s=(new DecimalFormat("##0.###")).format(MainDriver.Qcapacity2);
				bufferedWriter.append("     Pump Capacity                  ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "bbl/str");
				s=(new DecimalFormat("####0.##")).format(MainDriver.SIDPP - psia);
				bufferedWriter.append("     Shut-in Drill Pipe Pressure    ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "psig");
				s=(new DecimalFormat("####0.##")).format(MainDriver.SICP - psia);
				bufferedWriter.append("     Shut-in Casing Pressure        ="+"      "+s);
				autoUnitWriter(bufferedWriter, s.length(), 14, "psig");
				//---------------------Directional Data Print Out !

				if (MainDriver.iWell >= 1){
					bufferedWriter.newLine();
					bufferedWriter.append("     --- Directional Wellbore Information ---");
					bufferedWriter.newLine();
					s=(new DecimalFormat("##0.##")).format(MainDriver.BUR);
					bufferedWriter.append("     First Build-Up Rate (BUR)      ="+"      "+s); 
					autoUnitWriter(bufferedWriter, s.length(), 14, "deg/100 ft");
					s=(new DecimalFormat("#####0.##")).format(MainDriver.Rbur);
					bufferedWriter.append("     Radius of Curvature @ 1st      ="+"      "+s);
					autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				}
				if (MainDriver.iWell >= 2){
					s=(new DecimalFormat("####0.##")).format(MainDriver.xHold);
					bufferedWriter.append("     First Hold Length              ="+"      "+s); 
					autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				}
				if (MainDriver.iWell >= 3){
					s=(new DecimalFormat("##0.##")).format(MainDriver.BUR2);
					bufferedWriter.append("     Second Build-Up Rate           ="+"      "+s); 
					autoUnitWriter(bufferedWriter, s.length(), 14, "deg/100 ft");
					s=(new DecimalFormat("#####0.##")).format(MainDriver.R2bur);
					bufferedWriter.append("     Radius of Curvature @ 2nd      ="+"      "+s);
					autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				}
				if (MainDriver.iWell == 4){
					s=(new DecimalFormat("#####0.##")).format(MainDriver.x2Hold);
					bufferedWriter.append("     Second Hold Length             ="+"      "+s);
					autoUnitWriter(bufferedWriter, s.length(), 14, "ft");
				}

				bufferedWriter.newLine();
				bufferedWriter.append("   Total MD  True VD   Angle     ID of DS  OD of Ann ID of Ann");
				bufferedWriter.newLine();
				bufferedWriter.append("     (ft)      (ft)    (degree)   (inch)    (inch)    (inch)  ");
				bufferedWriter.newLine();
				bufferedWriter.append("   --------  -------   --------  ------------------- ---------");
				bufferedWriter.newLine();
				for(int i = MainDriver.NwcE-1; i>MainDriver.NwcS-2; i--){
					s=(new DecimalFormat("#####0.#")).format(MainDriver.TMD[i]);
					bufferedWriter.append("    "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 7, "");
					s=(new DecimalFormat("#####0.#")).format(MainDriver.TVD[i]);
					bufferedWriter.append("   "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 6, "");
					s=(new DecimalFormat("#####0.##")).format(MainDriver.ang2p[i]);
					bufferedWriter.append("    "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 7, "");
					s=(new DecimalFormat("####0.###")).format(MainDriver.DiDS[i]);
					bufferedWriter.append("   "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 7, "");
					s=(new DecimalFormat("####0.###")).format(MainDriver.Do2p[i]);
					bufferedWriter.append("   "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 8, "");
					s=(new DecimalFormat("####0.###")).format(MainDriver.Di2p[i]);
					bufferedWriter.append("  "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 8, "");
					bufferedWriter.newLine();
				}
				bufferedWriter.append("   --------  -------   --------  ------------------- ---------");
				bufferedWriter.newLine();
				bufferedWriter.newLine();

				//.............................. ........ print out final results
				bufferedWriter.append("         --- Results from SNU/TAMU Well Control Simulator ---");
				bufferedWriter.newLine();
				bufferedWriter.append("         ====================================================");
				bufferedWriter.newLine();
				bufferedWriter.newLine();

				bufferedWriter.append("     Time     Xtop      Xbotm     Px@Top    Pit Vol   Kick Density");
				bufferedWriter.newLine();
				bufferedWriter.append("    (mins)    (ft)      (ft)      (psig)    (bbls)      (ppg)");
				bufferedWriter.newLine();
				bufferedWriter.append("    ------    ----      -----     ------    -------   ------------");
				bufferedWriter.newLine();
				for(int i = 0; i <= MainDriver.Np; i++){
					s=(new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60);
					bufferedWriter.append("    "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.xTop[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.xBot[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.pxTop[i] - psia);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.00")).format(MainDriver.Vpit[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.00")).format(MainDriver.rhoK[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 12, "");
					bufferedWriter.newLine();
					//		    if (Int(i / 56) * 56 = i) Then
					//		    End if
				}
				bufferedWriter.newLine();
				bufferedWriter.newLine();

				bufferedWriter.append("     Time     Pump P    Stand PP  Choke P   CsgSeat P   BHP     P@Mudline");
				bufferedWriter.newLine();
				bufferedWriter.append("    (mins)    (psig)     (psig)   (psig)     (psig)   (psig)     (psig)");
				bufferedWriter.newLine();
				bufferedWriter.append("    ------    ------    --------  -------   --------- ------    ---------");
				bufferedWriter.newLine();
				for(int i = 0; i<=MainDriver.Np; i++){
					s=(new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60);
					bufferedWriter.append("    "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.Ppump[i] - psia);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.Psp[i] - psia);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.Pchk[i] - psia);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.Pcsg[i] - psia);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.Pb2p[i] - psia);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.PmLine[i] - psia);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 9, "");
					bufferedWriter.newLine();
					//		    if (Int(i / 56) * 56 = i) Then
					//		    End if
				}
				bufferedWriter.newLine();
				bufferedWriter.newLine();

				bufferedWriter.append("     Time     Strokes   Vol Circ  ChK Open   InFlux   Mud Rate  Gas Rate");
				bufferedWriter.newLine();
				bufferedWriter.append("    (mins)      (#)      (bbls)   Dia Ratio  (Mcf/D)   (gpm)    (Mcf/Day)");
				bufferedWriter.newLine();
				bufferedWriter.append("    ------    -------   --------  ---(%)--- --------  --------  ---------");
				bufferedWriter.newLine();
				for(int i = 0; i<=MainDriver.Np; i++){
					s=(new DecimalFormat("#####0.00")).format(MainDriver.TTsec[i] / 60);
					bufferedWriter.append("    "+s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.Stroke[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.00")).format(MainDriver.VOLcir[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.CHKopen[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.QmcfDay[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("#####0.0")).format(MainDriver.mudflow[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					s=(new DecimalFormat("######0.0")).format(MainDriver.gasflow[i]);
					bufferedWriter.append(s);
					autoUnitWriterWOnewLine(bufferedWriter, s.length(), 10, "");
					bufferedWriter.newLine();

					//		    if (Int(i / 56) * 56 = i) Then
					//		    End if
				}
			}
			bufferedWriter.close();
			return 1;
		}
		catch(IOException ei){
			System.out.println("Text file doesn't exist.");
			ei.printStackTrace();
			return -1;
		}		
		//
	}
}
