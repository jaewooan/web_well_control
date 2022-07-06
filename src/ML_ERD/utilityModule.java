package ML_ERD;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

class utilityModule {
	//=======================================================================
	//..... Originally from module Calculation (m6calc.bas)
	//      This sub is merged into module Utility (m6util.bas)
	//  Alternative Well Control SIMulator for All Cases:AWCfor.bas
	//------------------------------------------------------------- AWCfor.bas
	//  Last Modification by Jonggeun Choe 6-23-94, 7/29/03
	//  This is the updating version for alternative model Modified from AWC.for
	//---------------------------------------------------------------========
	//  PROGRAM FOR SIMULATING THE PRESSURE BEHAVIOR DURING GAS WELL-CONTROL
	//
	//  Modified Trial and Error Method are used for Determining Annulus
	//  Pressure during Circulating Gas Kick out of Hole by
	//  Driller//s method or Engineer//s (Wait & Weight) method.
	//  This is good for on- or off-shore and directional well control operations
	//---------------------------------------------------------------------
	// This is the complete version.
	// Date:                         PROGRAMMED BY
	//                               JONGGEUN CHOE
	//                               Aggie Drilling Research (ADR)
	//                               TEXAS A&M UNIVERSITY
	//  M.S. from Seoul National University, Rep. of Korea
	//  Ph.D from Texas A&M University
	//---------------------------------------------------------------------
	//      np        = index
	//      x         = location of the top of kick fluid, ft
	//      xtop(i)   = location of the top of kick fluid (MD), ft
	//      xbot(i)   = location of the bottom of kick fluid (MD), ft
	//      pxtop(i)  = average pressure in the top of kick at x, psia
	//      vpit(i)   = volume of kick at x, bbls
	//      tx        = average temperature in the top of kick at x, Rankin
	//      rhok(i)   = density of kick fluid at x, ppg
	//      pchk(i)   = surface casing pressure to maintain costant BHP, psia
	//      volcir(i) = circulating volume, bbls
	//      stroke(i) = number of strokes, #
	//      ppump(i)  = pump pressure at surface to circulate the mud, psia
	//      psp(i)    = stand pipe pressure, psia
	//      pcsg(i)   = pressure at casing seat, psia
	//      ttsec(i)  = total simulation time, sec
	//      pb2p(i)   = bottomhole pressure, psi
	//      QmcfDay(i)  = gas influx rate, Mscf/D
	//      chkopen(i)= choke open % by diameter ratio
	//-----------------------------------------------------------------------
	// subroutines in the module
	//     bkup
	//     calcdt (xnew,xold,hgx,timeint)
	//     dpsconn (qflow,volpump,dpconn,dpsp)
	//     getlines (qflow,d2,d1,qeff,capeff,voleff)
	//     GetXnew (xold, timeadd, hgx, qflow, xnew, vgf)
	// F - asin(valux)
	// F - temperature
	//
	//================================================================ AUTfor.bas
	////
	//............................(last modification=6-23-94) by Jonggeun CHOE
	//  subroutines and functions that are related two-phase well control
	//  simulation.
	//  subroutines in the module
	//   calcCHKOpen(qtflow, hgX, rhoL, rhoG, dpchoke, CHKpcent)
	//   calcpeff (psurf, qtop, qbot, ql, qg, rho, xloc, hkmd, hg2p, px, pbeff, gasden)
	//   get2pdp (xloc, ql, qg, hg2p, rhol, rhog, gasvis)
	//   getboth (volt, xloc, hhm)
	//   getdp (qflow, xloc, rho)
	//   getdpinside (pbeff, qflow, volpump, SPP, pp2)
	//   getgrid (xlength, delx2, xstart, d2, d1, hgs, hge, nstart As Integer, ncount As Integer)
	//   gettoph (volt, xloc, hhm)
	//   getvd(xloc, xvert)
	//   node2p (xstart, voltot, hgcell, dxcell, nstart As Integer, ncount As Integer)
	//   pxbottom (volpump, xloc, pbtm)
	//   slipshutin (d2, d1, rhol, rhog, surften, angle, Hg, vl, vg, vslip, vgf)
	//   xposition (xloc, ipos)
	//

	static double aasin(double sinv){
		double resul=0, tmpx=0;
		if(Math.abs(sinv) > 1.001){
			JOptionPane.showMessageDialog(null,"Input Geometry is mathematically impossible");
			resul = 120 * MainDriver.radConv;
			return resul;
		}
		tmpx = 1 - sinv * sinv;
		if(tmpx <= 0.00000001) resul = 90 * MainDriver.radConv;
		else resul = Math.atan(sinv / Math.sqrt(tmpx));
		return resul;   
	}



	static double GASinflux(double Pbeff, double tsecGas, double hft){    // in Mscf/Day
		int NOintv;
		double Pintv=0, gasVlo=0, gasVup=0, zlo=0, zup=0, grpConst=0, grp2Const=0;
		double result=0, dp5intv=0, ts=0, ps=0, puzSum=0, grp2Conv=0;
		double[] GasPropEX = new double[3];
		//.....  Check the minimum conditions to have gas influx - 2.5 sec and 0.000001 ft
		if (tsecGas < 1.5 || hft < 0.00001)
			return 0; //130802 JW Then Exit Function
		if (Pbeff < 15.2 || Pbeff > MainDriver.Pform + 0.001)
			return 0; //130802 JW Then Exit Function    //2003/7/15
		//
		//........ calculate the pseudo-pressure
		NOintv = 5; ts = 520; ps = 14.7;   // use the SC(standard conditions, not input)
		dp5intv = (MainDriver.Pform - Pbeff) / NOintv;
		Pintv = Pbeff;
		puzSum = 0;

		for(int i=0; i<(NOintv-1); i++){
			Pintv =  Pintv + dp5intv;
			GasPropEX = propertyModule.GasProp(Pintv, MainDriver.TbRankin);
			puzSum = puzSum + 2 * Pintv / (GasPropEX[0] * GasPropEX[2]);//  0 : vis, 1 : den, 2:zz
		}
		//Call GasProp(Pbeff, MainDriver.TbRankin, gasVlo, gasDen, zlo)
		//Call GasProp(MainDriver.Pform, MainDriver.TbRankin, gasVup, gasDen, zup) 20130820 ajw

		gasVlo = propertyModule.GasProp(Pbeff, MainDriver.TbRankin)[0];
		zlo = propertyModule.Zfact(Pbeff, MainDriver.TbRankin);
		gasVup=propertyModule.GasProp(MainDriver.Pform, MainDriver.TbRankin)[0];
		zup = propertyModule.Zfact(MainDriver.Pform, MainDriver.TbRankin);

		puzSum = puzSum + (Pbeff / (gasVlo * zlo) + MainDriver.Pform / (gasVup * zup));
		puzSum = puzSum * dp5intv;

		grpConst =  ts * MainDriver.Perm * hft / (50300 * ps * MainDriver.TbRankin);
		grp2Const =  MainDriver.Perm * tsecGas / (1688 * MainDriver.Porosity * gasVup * MainDriver.Cti * Math.pow(MainDriver.DiaHole,2));
		grp2Conv = 4.0 * 144.0 / 3600.0;	

		result = grpConst * puzSum / (1.151 * Math.log10(grp2Const * grp2Conv) + MainDriver.Skins);

		if(result < 0) return 0;
		return result;
	}

	static int Xposition(double xLoc){
		//...... sub to find the location of xloc; only limited to TMD
		// tmd(ipos + 1) < xloc <= tmd(ipos)
		//
		int iPos=0;
		for(int i = MainDriver.NwcS-1; i<(MainDriver.NwcE - 1); i++){   // identify the location of the given x
			iPos = i;
			if(xLoc>MainDriver.TMD[i + 1] && xLoc<=MainDriver.TMD[i]) return iPos;
		}
		if (xLoc > MainDriver.TMD[MainDriver.NwcS-1]) return (MainDriver.NwcS-1);
		else if (xLoc <= MainDriver.TMD[MainDriver.NwcE-1]) return (MainDriver.NwcE - 2);
		else {
			System.out.println("too big number");
			return (MainDriver.NwcS-1);		    	
		}
		//
	}

	static int Xposition_cutting(double xLoc){
		//...... sub to find the location of xloc; only limited to TMD
		// tmd(ipos + 1) < xloc <= tmd(ipos)
		//
		int iPos=0;
		for(int i = MainDriver.NwcS_cutting-1; i<(MainDriver.NwcE_cutting - 1); i++){   // identify the location of the given x
			iPos = i;
			if(xLoc>MainDriver.TMD_cutting[i + 1] && xLoc<=MainDriver.TMD_cutting[i]) return iPos;
		}
		if (xLoc > MainDriver.TMD_cutting[MainDriver.NwcS_cutting-1]) return (MainDriver.NwcS_cutting-1);
		else if (xLoc <= MainDriver.TMD_cutting[MainDriver.NwcE_cutting-1]) return (MainDriver.NwcE_cutting - 2);
		else {
			System.out.println("too big number");
			return (MainDriver.NwcS_cutting-1);		    	
		}
		//
	}

	static double getAngle(double xLoc){
		//...... the program to calculate the angle from total MD
		// xLoc;MD from surface, xAngle; angle from vertical, degree
		//
		int iLoc=0;
		double delAng=0, aAng=0, xAngle=0, angleBUR=0, xdx=0; //delTmp=0, <= unused variables(20130806 ajw) 
		iLoc=Xposition(xLoc);
		//delTmp = xLoc - MainDriver.TMD[iLoc + 1];
		delAng = MainDriver.ang2p[iLoc] - MainDriver.ang2p[iLoc + 1];
		aAng = Math.abs(delAng);

		if(aAng < 0.5) xAngle =  (0.5 * (MainDriver.ang2p[iLoc] + MainDriver.ang2p[iLoc + 1]));
		else{
			angleBUR = MainDriver.BUR2;
			if (MainDriver.ang2p[iLoc + 1] < MainDriver.angEOB - 0.01) angleBUR = MainDriver.BUR;
			xdx = xLoc - MainDriver.TMD[iLoc + 1];
			xAngle =  (MainDriver.ang2p[iLoc + 1] + 0.01 * xdx * angleBUR);
		}
		if (xLoc > MainDriver.TMD[MainDriver.NwcS-1]) xAngle = MainDriver.ang2p[MainDriver.NwcS-1];
		else if (xLoc < MainDriver.TMD[MainDriver.NwcE-1]) xAngle = MainDriver.ang2p[MainDriver.NwcE-1]; // 나중에 다시 확인!!! nwce-2일수도 있음

		return xAngle;
		//
	}	

	static double getBotH(double VOLt, double xLoc){
		//.....  sub to calculate the height of any fluid based on the given volume and location.
		//  this is generally good.
		// xloc;the given bottom, ft  volt;total volume, bbls, hh;height, ft
		// hhm;measured depth from xloc, ft
		//

		int iPos=0;
		double Htmp=0, volSum=0, voldown=0, Qflow=0, capcty=0, hhm=0;
		iPos = Xposition(xLoc);   //identify the location of the given x
		//.............................. calculate MD by the given volume, bbls
		Htmp = xLoc - MainDriver.TMD[iPos + 1];			    
		volSum = 0; voldown = 0; hhm = 0; Qflow = 0;
		//
		for(int i=(iPos + 1); i<MainDriver.NwcE; i++){
			capcty =  MainDriver.C12 * (Math.pow(MainDriver.Do2p[i],2) - Math.pow(MainDriver.Di2p[i], 2));
			if (i==9) capcty=getLines(Qflow)[1];
			volSum = volSum + capcty * Htmp;
			if (VOLt < volSum){
				hhm=hhm + (VOLt - voldown) / capcty;
				return hhm;
			}
			voldown = volSum; Htmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1]; hhm = xLoc - MainDriver.TMD[i];
		}
		hhm = xLoc - MainDriver.TMD[MainDriver.NwcE-1];   // inside marine riser or choke/kill line
		return hhm;
	}

	static double getVol(double xcenter){
		//.....  sub to calculate the height of any fluid based on the given volume and location.
		//  this is generally good.
		// xloc;the given bottom, ft  volt;total volume, bbls, hh;height, ft
		// hhm;measured depth from xloc, ft
		//

		int iPos=0;
		double Htmp=0, volSum=0, voldown=0, volup=0, Qflow=0, capcty=0, hhm=0, h_half=0;
		iPos = Xposition(xcenter);   //identify the location of the given x
		//.............................. calculate MD by the given volume, bbls
		Htmp = xcenter - MainDriver.TMD[iPos + 1];
		h_half = MainDriver.TMD[MainDriver.NwcS-1]-xcenter;
		volSum = 0; voldown = 0; hhm = 0; Qflow = 0;
		//
		for(int i=(iPos + 1); i<MainDriver.NwcE; i++){
			capcty =  MainDriver.C12 * (Math.pow(MainDriver.Do2p[i],2) - Math.pow(MainDriver.Di2p[i], 2));
			if (i==9) capcty=getLines(Qflow)[1];
			volup = volup + capcty * Htmp;
			if (xcenter - MainDriver.TMD[i] > h_half){
				volup = volup - capcty * (xcenter - MainDriver.TMD[i] - h_half);
				break;
			}
			Htmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1]; hhm = xcenter - MainDriver.TMD[i];
		}
		voldown=0;
		Htmp = MainDriver.TMD[iPos] - xcenter;
		for(int i=iPos; i>=MainDriver.NwcS-1; i--){
			capcty =  MainDriver.C12 * (Math.pow(MainDriver.Do2p[i],2) - Math.pow(MainDriver.Di2p[i], 2));
			voldown = voldown + capcty * Htmp;
			Htmp = MainDriver.TMD[i] - MainDriver.TMD[i + 1]; hhm = MainDriver.TMD[i] - xcenter;
		}
		volSum = volup + voldown;
		return volSum;
	}

	static double[] getLines(double Qflow){
		// sub to choose the correct values at the choke lines and kill lines, and marine riser.
		//.............................. inside marine riser||choke/kill line
		//                     iBOP = 1 - BOP open, iBOP = 0 - BOP closed
		// 20130802 ajw : index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
		double Qeff=0, d1=0, d2=0, f=0, capEff=0, volEff=0;
		double[] result= new double[5];
		Qeff = Qflow; d1 = 0;
		if(MainDriver.iBOP== 1){
			d2 = MainDriver.Driser;
			d1 = MainDriver.doDP;
			capEff = MainDriver.C12 * (d2 * d2 - d1 * d1);
		}
		else{
			//..................... ichoke=1-flow through choke line, ichoke=2-kill line
			d2 = MainDriver.Dchoke;
			if (MainDriver.iKill==1) d2 = MainDriver.Dkill;
			capEff = MainDriver.C12 * (d2 * d2 - d1 * d1);
			//..................... if both lines are open, adjust flow rate to calc. DP
			if(MainDriver.iChoke==1&&MainDriver.iKill ==1){
				f =  Math.pow((MainDriver.Dkill / MainDriver.Dchoke),(2 + 1.25 / 1.75));
				Qeff = Qflow / (1 + f);
				d2 = MainDriver.Dchoke;
				capEff = MainDriver.C12 * (d2 * d2 + MainDriver.Dkill * MainDriver.Dkill);
			}
		}
		volEff = capEff * MainDriver.Dwater;

		result[0]=Qeff;
		result[1]=capEff;
		result[2]=volEff;
		result[3]=d1;
		result[4]=d2;

		return result;
	}

	static void getDP(double Qflow, double xLoc, double rho){
		//....... sub to calculate frictional pressure drop at a given point below (DPbot)||above (MainDriver.DPtop).
		//
		double[] DPf = new double[10];
		double[] getLinesEX= new double[5];
		//............................. calculate DP for all section of annulus
		double dpall = 0;
		double d1=0, d2 = 0, vls=0;
		for(int i=MainDriver.NwcS; i<9; i++){
			d2 = MainDriver.Do2p[i];
			d1 = MainDriver.Di2p[i];
			vls = Qflow / 2.448 / (d2 * d2 - d1 * d1);
			DPf[i] = propertyModule.DPDL1p(vls, d2, d1, rho);
			dpall = dpall + DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
		}
		//.............................. inside marine riser||choke/kill line
		//                           iBOP = 1 - BOP open, iBOP = 0 - BOP closed

		if (MainDriver.iOnshore==2){
			getLinesEX = getLines(Qflow);// 20130802 ajw : index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
			d2 = getLinesEX[4];
			d1 = getLinesEX[3];
			vls = getLinesEX[0] / 2.448 / (getLinesEX[4] * getLinesEX[4] - getLinesEX[3] * getLinesEX[3]); //d1, d2도 getlines에서 나오는 거임..
			DPf[MainDriver.NwcE-1] = propertyModule.DPDL1p(vls, d2, d1, rho);
			dpall = dpall + DPf[MainDriver.NwcE-1] * (MainDriver.TMD[MainDriver.NwcE - 2] - MainDriver.TMD[MainDriver.NwcE-1]);
		}
		//...................... calculatee frictional pressure drop in annulus
		MainDriver.DPbot = 0;
		for(int i = MainDriver.NwcS; i<MainDriver.NwcE; i++){
			if (xLoc > MainDriver.TMD[i] && xLoc < (MainDriver.TMD[i - 1])+0.01){
				MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD[i - 1] - xLoc);
				break;
			}
			MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
		}
		if (xLoc > MainDriver.TMD[MainDriver.NwcS-1]) MainDriver.DPbot = 0;
		else if (xLoc <= MainDriver.TMD[MainDriver.NwcE-1]) MainDriver.DPbot = dpall;
		MainDriver.DPtop = dpall - MainDriver.DPbot;
	}

	static void getDP_cutting(double Qflow, double xLoc, double rho){
		//....... sub to calculate frictional pressure drop at a given point below (DPbot)||above (MainDriver.DPtop).
		//
		double[] DPf = new double[10];
		//............................. calculate DP for all section of annulus
		double dpall = 0;
		double d1=0, d2 = 0, vls=0;
		for(int i=MainDriver.NwcS_cutting; i<MainDriver.NwcE_cutting; i++){
			d2 = MainDriver.Do2p_cut[i];
			d1 = MainDriver.Di2p_cut[i];
			vls = Qflow / 2.448 / (d2 * d2 - d1 * d1);
			DPf[i] = propertyModule.DPDL1p(vls, d2, d1, rho);
			dpall = dpall + DPf[i] * (MainDriver.TMD_cutting[i - 1] - MainDriver.TMD_cutting[i]);
		}

		//...................... calculatee frictional pressure drop in annulus
		MainDriver.DPbot = 0;
		for(int i = MainDriver.NwcS_cutting; i<MainDriver.NwcE_cutting; i++){
			if (xLoc > MainDriver.TMD_cutting[i] && xLoc < (MainDriver.TMD_cutting[i - 1])+0.01){
				MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD_cutting[i - 1] - xLoc);
				break;
			}
			MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD_cutting[i - 1] - MainDriver.TMD_cutting[i]);
		}
		if (xLoc > MainDriver.TMD_cutting[MainDriver.NwcS_cutting-1]) MainDriver.DPbot = 0;
		else if (xLoc <= MainDriver.TMD_cutting[MainDriver.NwcE_cutting-1]) MainDriver.DPbot = dpall;
		MainDriver.DPtop = dpall - MainDriver.DPbot;
	}

	static double[] getDP2(double Qgasflow, double Qmudflow, double pumpDen, double xLoc, double vxLoc){
		//....... sub to calculate frictional pressure drop at a given point below (DPbot)||above (MainDriver.DPtop) in an OBM 1-phase circumstance.
		//20140317 AJW
		double[] DPf = new double[10];
		double[] getLinesEX= new double[5];
		//............................. calculate DP for all section of annulus
		double dpall = 0;
		double d1=0, d2 = 0, vls=0;
		double Qt=0;
		double HydroAll=0, HydrodpTop=0, HydrodpBtm=0;
		double totalP=0;

		double[] result = new double[3];

		Qt=Qgasflow+Qmudflow*pumpDen/MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1];

		for(int i=MainDriver.NwcS; i<9; i++){ //9: onshore surface or offshore water base
			d2 = MainDriver.Do2p[i];
			d1 = MainDriver.Di2p[i];
			vls = Qt*MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1]/MainDriver.OBMAnnDensity_Drilling[i] / 2.448 / (d2 * d2 - d1 * d1);
			DPf[i] = propertyModule.DPDL1p(vls, d2, d1, MainDriver.OBMAnnDensity_Drilling[i]);
			dpall = dpall + DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
			HydroAll = HydroAll + 0.052* MainDriver.OBMAnnDensity_Drilling[i] * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]);
		}
		//.............................. inside marine riser||choke/kill line
		//                           iBOP = 1 - BOP open, iBOP = 0 - BOP closed

		if (MainDriver.iOnshore==2){
			getLinesEX = getLines(Qt*MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1]/MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcE-1]);// 20130802 ajw : index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
			d2 = getLinesEX[4];
			d1 = getLinesEX[3];
			vls = getLinesEX[0] / 2.448 / (getLinesEX[4] * getLinesEX[4] - getLinesEX[3] * getLinesEX[3]); //d1, d2도 getlines에서 나오는 거임..
			DPf[MainDriver.NwcE-1] = propertyModule.DPDL1p(vls, d2, d1, MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS-1]);
			dpall = dpall + DPf[MainDriver.NwcE-1] * (MainDriver.TMD[MainDriver.NwcE-2] - MainDriver.TMD[MainDriver.NwcE-1]);
			HydroAll = HydroAll + 0.052* MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcE-1] * (MainDriver.TVD[MainDriver.NwcE-2] - MainDriver.TVD[MainDriver.NwcE-1]);
		}
		//...................... calculatee frictional pressure drop in annulus

		MainDriver.DPbot = 0;
		for(int i = MainDriver.NwcS; i<MainDriver.NwcE; i++){
			if (xLoc > MainDriver.TMD[i] && xLoc < (MainDriver.TMD[i - 1])+0.01){
				MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD[i - 1] - xLoc);
				HydrodpBtm = HydrodpBtm + 0.052* MainDriver.OBMAnnDensity_Drilling[i] * (MainDriver.TVD[i - 1] - vxLoc);
				break;
			}
			MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
			HydrodpBtm = HydrodpBtm + 0.052* MainDriver.OBMAnnDensity_Drilling[i] * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]);
		}

		if (xLoc > MainDriver.TMD[MainDriver.NwcS-1]){
			MainDriver.DPbot = 0;
			HydrodpBtm=0;
		}
		else if (xLoc <= MainDriver.TMD[MainDriver.NwcE-1]){
			MainDriver.DPbot = dpall;
			HydrodpBtm=HydroAll;
		}
		MainDriver.DPtop = dpall - MainDriver.DPbot;
		HydrodpTop = HydroAll - HydrodpBtm;
		totalP = 14.7+HydrodpTop+MainDriver.DPtop;

		result[0] = HydrodpBtm;
		result[1] = HydrodpTop;
		result[2] = totalP;

		return result;
	}		

	static double[] getPbeff_OBM_Welldrill(double Qa, double rho, double vdbit, double mdbit, double pumpPressure){
		double[] result=new double[3];
		double[] DPf = new double[10];
		double[] ptop = new double[10];
		double[] getLinesEX= new double[5];
		double dpall = 0;
		double d1=0, d2 = 0, vls=0;
		double pmid=0;
		double densityErr=1;
		double dens1=0, dens2=0;
		int dummy=0;
		int iVDbit=0;
		double pumpPdensity=0;
		double Qflow=0;

		ptop[MainDriver.NwcE-1]=14.7;//psi
		MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcE-1]=rho;
		pumpPdensity = rho*Math.exp(MainDriver.MudComp*(pumpPressure-ptop[MainDriver.NwcE-1]));			

		for(int i=MainDriver.NwcE-1; i>=MainDriver.NwcS; i--){ //9: onshore surface or offshore water base
			Qflow=Qa;
			d2 = MainDriver.Do2p[i];
			d1 = MainDriver.Di2p[i];

			if(i==MainDriver.NwcE-1){
				ptop[i]=14.7;					
				dens1 = rho;
				getLinesEX = getLines(Qflow * pumpPdensity / dens1);
				Qflow = getLinesEX[0];
				d1 = getLinesEX[3];
				d2 = getLinesEX[4];
			}
			else{
				ptop[i]=pmid + 0.052 * MainDriver.OBMAnnDensity_Drilling[i+1] * (MainDriver.TVD[i] - MainDriver.TVD[i + 1]) / 2 + DPf[i+1] * (MainDriver.TMD[i] - MainDriver.TMD[i + 1]) / 2;
				dens1=MainDriver.OBMAnnDensity_Drilling[i+1];
			}
			densityErr=1;
			dummy=0;
			while(densityErr>0.000001 && dummy<1000){
				dummy++;
				vls = Qflow * pumpPdensity / dens1 / 2.448 / (d2 * d2 - d1 * d1);
				DPf[i] = propertyModule.DPDL1p(vls, d2, d1, dens1);
				if(vdbit>=MainDriver.TVD[i] && vdbit<MainDriver.TVD[i-1]) pmid=ptop[i] + 0.052 * dens1 * (vdbit - MainDriver.TVD[i]) / 2 + DPf[i] * (mdbit - MainDriver.TMD[i]) / 2;
				else pmid=ptop[i] + 0.052 * dens1 * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]) / 2 - DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]) / 2;
				dens2 = rho * Math.exp(MainDriver.MudComp*(pmid-ptop[MainDriver.NwcE-1]));
				densityErr = Math.abs(dens2-dens1);
				dens1 = dens2;
			}

			if(vdbit>=MainDriver.TVD[i] && vdbit<MainDriver.TVD[i-1]) pmid=ptop[i] + 0.052 * dens1 * (vdbit - MainDriver.TVD[i]) / 2 + DPf[i] * (mdbit - MainDriver.TMD[i]) / 2;
			else pmid=ptop[i] + 0.052 * dens1 * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]) / 2 - DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]) / 2;

			MainDriver.OBMAnnDensity_Drilling[i]=dens1;

			if(vdbit>=MainDriver.TVD[i] && vdbit<MainDriver.TVD[i-1]){
				iVDbit = i-1;
				break;
			}
			else if(vdbit>=MainDriver.Vdepth){
				iVDbit = MainDriver.NwcS-1;
				break;
			}
			iVDbit = MainDriver.NwcS;
		}

		ptop[iVDbit] = pmid + 0.052 * MainDriver.OBMAnnDensity_Drilling[MainDriver.NwcS]*(vdbit - MainDriver.TVD[MainDriver.NwcS])/2 + DPf[MainDriver.NwcS]*(mdbit-MainDriver.TMD[MainDriver.NwcS])/2;
		MainDriver.OBMAnnDensity_Drilling[iVDbit]= MainDriver.OBMAnnDensity_Drilling[iVDbit+1];

		result[0] = iVDbit;
		result[1] = ptop[iVDbit];
		result[2] = pumpPdensity;

		return result;
	}

	static double[] OBMdensityCalc_Drilling(double Qflow, double pb, double old_rho, double new_rho, double vdbit, double mdbit, double ibit, double ppDensity){
		// 20140317 ajw, calculate the mud density at the middle in the pipe.
		// rho: surface density
		double[] DPf = new double[12];
		double[] pup = new double[12];
		double[] getLinesEX= new double[5];
		double dpall = 0;
		double d1=0, d2 = 0, vls=0;
		double pmid=0;
		double densityErr=1;
		double dens1=0, dens2=0;
		int iVDbit=0;
		int dummy=0;			
		double pumpPdensity = ppDensity;
		double[] result = new double[2]; //0: standpipe pressure, 1: pump pressure.

		iVDbit=(int)ibit;
		pup[iVDbit]=pb;//psi
		MainDriver.OBMPipeDensity_Drilling[iVDbit]=MainDriver.OBMAnnDensity_Drilling[iVDbit];

		for(int i=iVDbit+1; i<MainDriver.NwcE; i++){ //9: onshore surface or offshore water base
			d2 = MainDriver.DiDS[i];
			d1 = 0;
			dens1=MainDriver.OBMPipeDensity_Drilling[i-1];			

			densityErr=1;
			dummy=0;

			while(densityErr>0.000001 && dummy<1000){
				dummy++;
				vls = Qflow * pumpPdensity / dens1 / 2.448 / (d2 * d2 - d1 * d1);
				DPf[i] = propertyModule.DPDL1p(vls, d2, d1, dens1);			
				if(i==iVDbit+1){
					pmid = pup[i-1] - 0.052 * dens1 * (vdbit - MainDriver.TVD[i]) / 2 + DPf[i] * (mdbit - MainDriver.TMD[i]) / 2;
					pmid = pmid + propertyModule.DPbit(dens1, Qflow * pumpPdensity/dens1);
				}
				else pmid = pup[i-1] - 0.052 * dens1 * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]) / 2 + DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]) / 2;					
				dens2 = old_rho * Math.exp(MainDriver.MudComp*(pmid-14.7));
				densityErr = Math.abs(dens2-dens1);
				dens1 = dens2;
			}
			if(i==iVDbit+1){
				pmid = pup[i-1] - 0.052 * dens1 * (vdbit - MainDriver.TVD[i]) / 2 + DPf[i] * (mdbit - MainDriver.TMD[i]) / 2;
				pmid = pmid + propertyModule.DPbit(dens1, Qflow * pumpPdensity/dens1);
				pup[i]=pmid - 0.052 * dens1 * (vdbit - MainDriver.TVD[i]) / 2 + DPf[i] * (mdbit - MainDriver.TMD[i]) / 2;	
			}
			else{
				pmid = pup[i-1] - 0.052 * dens1 * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]) / 2 + DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]) / 2;
				pup[i]=pmid - 0.052 * dens1 * (MainDriver.TVD[i-1] - MainDriver.TVD[i]) / 2 + DPf[i] * (MainDriver.TMD[i-1] - MainDriver.TMD[i]) / 2;	
			}

			MainDriver.OBMPipeDensity_Drilling[i]=dens1;					
			result[0] = pup[i];
			result[1] = pup[i];
		}								

		if (MainDriver.iType >= 1 && MainDriver.iType <= 4){
			// stand pipe to end of kelly				
			d1 = 0;
			d2 = 3;
			dens1 = MainDriver.OBMPipeDensity_Drilling[MainDriver.NwcS-1];

			densityErr=1;
			dummy=0;

			while(densityErr>0.000001 && dummy<1000){
				vls = Qflow * pumpPdensity / dens1 / 2.448 / (d2 * d2);	
				DPf[MainDriver.NwcS] = propertyModule.DPDL1p(vls, d2, d1, dens1);
				pmid = pup[MainDriver.NwcS-1]+DPf[MainDriver.NwcS]*MainDriver.Heqval /2;
				dens2 = old_rho * Math.exp(MainDriver.MudComp*(pmid-14.7));
				densityErr = Math.abs(dens2-dens1);
				dens1 = dens2;
			}

			pmid = pup[MainDriver.NwcS-1]+DPf[MainDriver.NwcS]*MainDriver.Heqval /2;				
			MainDriver.OBMPipeDensity_Drilling[MainDriver.NwcS]=dens1;
			pup[MainDriver.NwcS]=pmid + DPf[MainDriver.NwcS] * MainDriver.Heqval / 2;
			result[0] = pup[MainDriver.NwcS];

			//pump to stand pipe				
			dens1 = MainDriver.OBMPipeDensity_Drilling[MainDriver.NwcS];				
			densityErr=1;
			dummy=0;				

			while(densityErr>0.000001 && dummy<1000){
				vls = Qflow * pumpPdensity / dens1 / 2.448 / (d2 * d2);	
				DPf[MainDriver.NwcS+1] = propertyModule.DPDL1p(vls, MainDriver.DiaSurfLine, d1, dens1);
				pmid = pup[MainDriver.NwcS]+DPf[MainDriver.NwcS+1]*MainDriver.LengthSurfLine/2;
				dens2 = old_rho * Math.exp(MainDriver.MudComp*(pmid-14.7));
				densityErr = Math.abs(dens2-dens1);
				dens1 = dens2;
			}

			pmid = pup[MainDriver.NwcS]+DPf[MainDriver.NwcS+1]*MainDriver.LengthSurfLine/2;
			MainDriver.OBMPipeDensity_Drilling[MainDriver.NwcS+1]=dens1;
			pup[MainDriver.NwcS+1]=pmid + DPf[MainDriver.NwcS+1] * MainDriver.LengthSurfLine / 2;
			result[1]=pup[MainDriver.NwcS+1];
			return result;
		}			
		return result;			
	}


	static double getTopH(double VOLt, double xLoc){//util
		// sub to calculate the height of any fluid based on the given volume and location.
		// this is generally good.
		// xloc;the given top, ft  volt;total volume, bbls, hh;height, ft
		//
		int iPos=0;
		iPos=Xposition(xLoc);  //identify the location of the given x
		//............................. calculate MD by the given volume, bbls
		double volSum = 0, volup = 0, hhm = 0, Qflow = 0;
		double Htmp = MainDriver.TMD[iPos] - xLoc;
		double capcty=0;
		//index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
		for(int i = iPos; i > MainDriver.NwcS-2; i--){    // do 44 i
			capcty = MainDriver.C12 * (Math.pow(MainDriver.Do2p[i + 1],2) - Math.pow(MainDriver.Di2p[i + 1],2));
			//............................. inside marine riser||choke/kill line
			if (i==8) capcty=getLines(Qflow)[1]; 
			volSum = volSum + capcty * Htmp;
			if (VOLt < volSum){
				hhm = hhm + (VOLt - volup) / capcty;
				return hhm;
			}
			volup = volSum; 
			if(i>0) Htmp = MainDriver.TMD[i - 1] - MainDriver.TMD[i];
			else Htmp = -MainDriver.TMD[i];
			hhm = MainDriver.TMD[i] - xLoc;
		}
		hhm = MainDriver.TMD[MainDriver.NwcS-1] - xLoc;
		return hhm;
		//
	}

	static double getTopH2(double VOLt, double xLoc){//util
		// sub to calculate the height of any fluid based on the given volume and location.
		// this is generally good.
		// xloc;the given top, ft  volt;total volume, bbls, hh;height, ft
		//
		int iPos=0;
		iPos=Xposition(xLoc);  //identify the location of the given x
		//............................. calculate MD by the given volume, bbls
		double volSum = 0, volup = 0, hhm = 0, Qflow = 0;
		double Htmp = MainDriver.TMD[iPos] - xLoc;
		double capcty=0;
		//index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
		for(int i = iPos; i > MainDriver.NwcS-2; i--){    // do 44 i
			capcty = MainDriver.C12 * (Math.pow(MainDriver.Do2p[i + 1],2) - Math.pow(MainDriver.Di2p[i + 1],2));
			//............................. inside marine riser||choke/kill line
			if (i==8) capcty=getLines(Qflow)[1]; 
			volSum = volSum + capcty * Htmp;
			if (VOLt < volSum){
				hhm = hhm + (VOLt - volup) / capcty;
				return hhm;
			}
			volup = volSum; 
			if(i>0) Htmp = MainDriver.TMD[i - 1] - MainDriver.TMD[i];
			else Htmp = -MainDriver.TMD[i];
			hhm = MainDriver.TMD[i] - xLoc;
		}
		hhm = MainDriver.TMD[MainDriver.NwcS-1] - xLoc;
		return hhm;
		//
	}

	static double[] DPsConn(double Qflow, double volPump){//, DPconn, DPsp) //util
		//----------------------------- all the Subs below originally from m6calc.bas module
		//                              7/29/2003
		// sub to calculate pressure loss for surface connections
		// qflow:gpm, volpump:bbls, dp:psi
		//
		double[] DPf = {0,0};
		double [] dpfc={0,0};
		double[] result = {0,0};
		double vls=0, d1=0, d2=0, volsurf=0, capcty=0, xmud=0, fract=0;
		//................ pump to stand pipe (1) & SP to end of Kelly (2)
		result[0] = 0;//dpconn : 0 
		result[1] = 0;// dpsp : 1
		if (MainDriver.iType < 1 || MainDriver.iType > 4) return result;
		d1 = 0; d2 = 3; vls = Qflow / 2.448 / (d2 * d2);
		DPf[0] = propertyModule.DPDL1p(vls, MainDriver.DiaSurfLine, d1, MainDriver.oMud);// pump to stand pipe
		DPf[1] = propertyModule.DPDL1p(vls, d2, d1, MainDriver.oMud);// SP to end of kelly
		dpfc[0] = propertyModule.DPDL1p(vls, MainDriver.DiaSurfLine, d1, MainDriver.Cmud);// pump to stand pipe
		dpfc[1] = propertyModule.DPDL1p(vls, d2, d1, MainDriver.Cmud);// SP to end of kelly
		//............... calculate pressure loss based on pumping volume
		volsurf = MainDriver.volDS[MainDriver.NwcE] + MainDriver.volDS[MainDriver.NwcE + 1];
		if (volPump < MainDriver.volDS[MainDriver.NwcE + 1]){
			capcty = MainDriver.C12 * MainDriver.DiaSurfLine * MainDriver.DiaSurfLine;
			xmud = volPump / capcty;
			result[0] = MainDriver.Heqval * DPf[1];
			result[1] = xmud * dpfc[0] + (MainDriver.LengthSurfLine - xmud) * DPf[0];
		}
		else if (volPump < volsurf){
			fract = (volPump - MainDriver.volDS[MainDriver.NwcE + 1]) / MainDriver.volDS[MainDriver.NwcE];
			result[0] = MainDriver.Heqval * (dpfc[1] * fract + DPf[1] * (1 - fract));
			result[1] = MainDriver.LengthSurfLine * dpfc[0];		        
		}
		else{
			result[0]= MainDriver.Heqval * dpfc[1];
			result[1] = MainDriver.LengthSurfLine * dpfc[0];		        
		}
		return result;
		//
	}

	static double[] getDPinside(double Pbeff, double Qflow, double vol2pump){//, SPP, pp2){//util 20130805 ajw
		//....... the program to calculate surface stand pipe and pump pressures
		// based on pumping volume.  partly consider surface connections.
		// P; psi q; gmp SPP; Standpipe pressure, pp2; pump pressure
		// Check the Pump off case with BOP open (iBOP = 1)
		// SPP : 0 , pp2 : 1

		int iLoc;
		double capcty=0, delMD=0, dpall=0, dphy=0, d1=0, d2=0, vls=0, dpcal=0, temp=0;
		double[] DPsConnEX, result = new double[2];
		if (MainDriver.iBOP==1 && Qflow<0.1){
			result[0]= 14.7; result[1] = 14.7;
			return result;
		}
		//......................... identify the location of circulating mud
		double xLoc = 0, xVert = 0, volSum=0, volnew=0, dvcmud=0, dvomud=0;
		volSum = MainDriver.volDS[MainDriver.NwcE] + MainDriver.volDS[MainDriver.NwcE + 1];
		volnew = vol2pump;
		vol2pump = volnew - 0.001;
		if (vol2pump < volSum) xLoc = 0.005;  //주입된 Kill mud가 11, 12 구간 즉 Stand pipe, rotary hose, swivel, kelly for D.P, Surface line(pump -> stand pipe)에 위치할 때
		else if (vol2pump < MainDriver.VOLinn){ //주입된 Kill mud가 D.S 내부에 위치할 때
			for(int i = MainDriver.NwcE-1; i>(MainDriver.NwcS - 1); i--){
				volSum = volSum + MainDriver.volDS[i];
				if (vol2pump < volSum){
					capcty = (MainDriver.C12 * Math.pow(MainDriver.DiDS[i],2));
					delMD = (vol2pump - volSum + MainDriver.volDS[i]) / capcty;
					xLoc = MainDriver.TMD[i] + delMD;
					break;
				}
			}
		}
		else xLoc = MainDriver.TMD[MainDriver.NwcS-1] + 0.4;

		//dummy = 0; 

		//............................ calculate friction & hydrostatic P.
		DPsConnEX = DPsConn(Qflow, vol2pump); //0: DPconn, 1: DPsp)
		dpall = 0; dphy = 0; d1 = 0;
		for(int i=MainDriver.NwcE-1; i>(MainDriver.NwcS - 1); i--){               // do 66
			d2 = MainDriver.DiDS[i]; vls = (Qflow / (2.448 * d2 * d2));
			dpcal = propertyModule.DPDL1p(vls, d2, d1, MainDriver.Cmud);
			if (xLoc >= MainDriver.TMD[i] && xLoc < MainDriver.TMD[i - 1]){
				iLoc = i; temp = xLoc - MainDriver.TMD[iLoc];
				dpall = dpall + temp * dpcal;
				dpcal = propertyModule.DPDL1p(vls, d2, d1, MainDriver.oMud);
				dpall = dpall + (MainDriver.TMD[i - 1] - xLoc) * dpcal; // friction pressure loss
				//............................. hydrostatic pressure at interface
				xVert = getVD(xLoc);
				dvcmud = xVert - MainDriver.TVD[iLoc];
				dvomud = MainDriver.TVD[iLoc - 1] - MainDriver.TVD[iLoc] - dvcmud;
				dphy = dphy + MainDriver.gMudCirc * dvcmud + MainDriver.gMudOld * dvomud;//728 // hydrostatic pressure

				for(int j = (iLoc - 1); j>(MainDriver.NwcS - 1); j--){      // do 44 8~3
					d2 = MainDriver.DiDS[j]; vls =  (Qflow / (2.448 * d2 * d2));
					dpcal = propertyModule.DPDL1p(vls, d2, d1, MainDriver.oMud);
					dpall = dpall + (MainDriver.TMD[j - 1] - MainDriver.TMD[j]) * dpcal;
					dphy = dphy + MainDriver.gMudOld * (MainDriver.TVD[j - 1] - MainDriver.TVD[j]);
				}   // continue 44
				dpall = dpall + propertyModule.DPbit(MainDriver.oMud, Qflow);
				//GoTo 333
				vol2pump = volnew;
				result[0] = Pbeff - dphy + dpall + DPsConnEX[0];
				result[1] = result[0] + DPsConnEX[1];
				return result;
			}
			dpall = dpall + dpcal * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
			dphy = dphy + MainDriver.gMudCirc * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]);
		}       // continue 66
		//.................................................. Bit nozzle, psi
		dpall = dpall + propertyModule.DPbit(MainDriver.Cmud, Qflow);
		vol2pump = volnew;
		result[0] = Pbeff - dphy + dpall + DPsConnEX[0];
		result[1] = result[0] + DPsConnEX[1];
		return result;
		//			    	
	}
	
	static double[] getDPinside_cutting(double Pbeff, double Qflow, double vol2pump){//, SPP, pp2){//util 20130805 ajw
		//....... the program to calculate surface stand pipe and pump pressures
		// based on pumping volume.  partly consider surface connections.
		// P; psi q; gmp SPP; Standpipe pressure, pp2; pump pressure
		// Check the Pump off case with BOP open (iBOP = 1)
		// SPP : 0 , pp2 : 1

		int iLoc;
		double capcty=0, delMD=0, dpall=0, dphy=0, d1=0, d2=0, vls=0, dpcal=0, temp=0;
		double[] DPsConnEX, result = new double[2];
		
		double gMC = 0, gMO = 0.052*MainDriver.oMud_save;
		if((MainDriver.gMudCirc-0.052*MainDriver.Kmud)>0.001) gMC = 0.052*MainDriver.oMud_save;
		else gMC = 0.052*MainDriver.Kmud;
		if (MainDriver.iBOP==1 && Qflow<0.1){
			result[0]= 14.7; result[1] = 14.7;
			return result;
		}
		//......................... identify the location of circulating mud
		double xLoc = 0, xVert = 0, volSum=0, volnew=0, dvcmud=0, dvomud=0;
		volSum = MainDriver.volDS[MainDriver.NwcE] + MainDriver.volDS[MainDriver.NwcE + 1];
		volnew = vol2pump;
		vol2pump = volnew - 0.001;
		if (vol2pump < volSum) xLoc = 0.005;  //주입된 Kill mud가 11, 12 구간 즉 Stand pipe, rotary hose, swivel, kelly for D.P, Surface line(pump -> stand pipe)에 위치할 때
		else if (vol2pump < MainDriver.VOLinn){ //주입된 Kill mud가 D.S 내부에 위치할 때
			for(int i = MainDriver.NwcE-1; i>(MainDriver.NwcS - 1); i--){
				volSum = volSum + MainDriver.volDS[i];
				if (vol2pump < volSum){
					capcty = (MainDriver.C12 * Math.pow(MainDriver.DiDS[i],2));
					delMD = (vol2pump - volSum + MainDriver.volDS[i]) / capcty;
					xLoc = MainDriver.TMD[i] + delMD;
					break;
				}
			}
		}
		else xLoc = MainDriver.TMD[MainDriver.NwcS-1] + 0.4;

		//dummy = 0; 

		//............................ calculate friction & hydrostatic P.
		DPsConnEX = DPsConn(Qflow, vol2pump); //0: DPconn, 1: DPsp)
		dpall = 0; dphy = 0; d1 = 0;
		for(int i=MainDriver.NwcE-1; i>(MainDriver.NwcS - 1); i--){               // do 66
			d2 = MainDriver.DiDS[i]; vls = (Qflow / (2.448 * d2 * d2));
			dpcal = propertyModule.DPDL1p(vls, d2, d1, MainDriver.Cmud);
			if (xLoc >= MainDriver.TMD[i] && xLoc < MainDriver.TMD[i - 1]){
				iLoc = i; temp = xLoc - MainDriver.TMD[iLoc];
				dpall = dpall + temp * dpcal;
				dpcal = propertyModule.DPDL1p(vls, d2, d1, MainDriver.oMud);
				dpall = dpall + (MainDriver.TMD[i - 1] - xLoc) * dpcal; // friction pressure loss
				//............................. hydrostatic pressure at interface
				xVert = getVD(xLoc);
				dvcmud = xVert - MainDriver.TVD[iLoc];
				dvomud = MainDriver.TVD[iLoc - 1] - MainDriver.TVD[iLoc] - dvcmud;
				dphy = dphy + gMC * dvcmud + gMO * dvomud;//728 // hydrostatic pressure

				for(int j = (iLoc - 1); j>(MainDriver.NwcS - 1); j--){      // do 44 8~3
					d2 = MainDriver.DiDS[j]; vls =  (Qflow / (2.448 * d2 * d2));
					dpcal = propertyModule.DPDL1p(vls, d2, d1, MainDriver.oMud);
					dpall = dpall + (MainDriver.TMD[j - 1] - MainDriver.TMD[j]) * dpcal;
					dphy = dphy + gMO * (MainDriver.TVD[j - 1] - MainDriver.TVD[j]);
				}   // continue 44
				dpall = dpall + propertyModule.DPbit(MainDriver.oMud, Qflow);
				//GoTo 333
				vol2pump = volnew;
				result[0] = Pbeff - dphy + dpall + DPsConnEX[0];
				result[1] = result[0] + DPsConnEX[1];
				return result;
			}
			dpall = dpall + dpcal * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
			dphy = dphy + gMC * (MainDriver.TVD[i - 1] - MainDriver.TVD[i]);
		}       // continue 66
		//.................................................. Bit nozzle, psi
		dpall = dpall + propertyModule.DPbit(MainDriver.Cmud, Qflow);
		vol2pump = volnew;
		result[0] = Pbeff - dphy + dpall + DPsConnEX[0];
		result[1] = result[0] + DPsConnEX[1];
		return result;
		//			    	
	}

	static double getVD(double xLoc){//util
		//........... the program to calculate the vertical depth from total MD
		// xloc;MD from surface, xvert;total vertical depth from surface, angle from vertical, degree
		//
		int iLoc=0;
		double delTmp=0, delAng=0, aAng=0, rr=0, xdx=0, xangl=0, temp=0, xVert=0;
		iLoc = Xposition(xLoc);//util
		delTmp = xLoc - MainDriver.TMD[iLoc + 1];
		delAng = MainDriver.ang2p[iLoc] - MainDriver.ang2p[iLoc + 1];
		aAng = Math.abs(delAng);
		if (aAng < 1) xVert = MainDriver.TVD[iLoc + 1] + delTmp * Math.cos(MainDriver.ang2p[iLoc] * MainDriver.radConv);
		else{
			rr = MainDriver.R2bur;
			if (MainDriver.ang2p[iLoc + 1] < MainDriver.angEOB - 0.01) rr = MainDriver.Rbur;
			xdx = MainDriver.TMD[iLoc] - MainDriver.TMD[iLoc + 1];
			xangl = MainDriver.ang2p[iLoc + 1] + delAng * delTmp / xdx;
			temp =  (Math.sin(xangl * MainDriver.radConv) - Math.sin(MainDriver.ang2p[iLoc + 1] * MainDriver.radConv));
			xVert = MainDriver.TVD[iLoc + 1] + rr * temp;
		}
		if (xLoc >= MainDriver.TMD[MainDriver.NwcS-1]) xVert = MainDriver.TVD[MainDriver.NwcS-1];
		else if (xLoc <= MainDriver.TMD[MainDriver.NwcE-1]) xVert = MainDriver.TVD[MainDriver.NwcE-1];
		return xVert;
		//
	}

	static double getMD(double xVD){//util
		//........... the program to calculate the vertical depth from total MD
		// xloc;MD from surface, xvert;total vertical depth from surface, angle from vertical, degree
		//
		int iLoc=0;
		double delTmp=0, delAng=0, aAng=0, rr=0, xdx=0, xangl=0, temp=0, xVert=0;
		double dVD = 0;
		double xMD = 0;
		iLoc = Xposition_VD(xVD);//util
		delTmp = xVD - MainDriver.TVD[iLoc + 1];
		delAng = MainDriver.ang2p[iLoc] - MainDriver.ang2p[iLoc + 1];
		aAng = Math.abs(delAng);
		if(aAng==0 && Math.abs(MainDriver.ang2p[iLoc+1]-Math.PI/2)<0.001){
			xMD = MainDriver.TMD[iLoc+1];
		}
		else if (aAng < 1) xMD = MainDriver.TMD[iLoc+1]+delTmp/Math.cos(MainDriver.ang2p[iLoc] * MainDriver.radConv);
		else{
			rr = MainDriver.R2bur;
			if (MainDriver.ang2p[iLoc + 1] < MainDriver.angEOB - 0.01) rr = MainDriver.Rbur;
			dVD = rr*Math.sin(MainDriver.ang2p[iLoc+1] * MainDriver.radConv);
			dVD = dVD+(xVD-MainDriver.TVD[iLoc+1]);
			xangl = Math.acos(dVD/rr);
			xMD = (MainDriver.TMD[iLoc]-MainDriver.TMD[iLoc+1])*(delAng-xangl)/delAng+MainDriver.TMD[iLoc+1];			      
		}
		return xMD;
		//
	}

	static int Xposition_VD(double xVD){
		//...... sub to find the location of xloc; only limited to TMD
		// tmd(ipos + 1) < xloc <= tmd(ipos)
		//
		int iPos=0;
		for(int i = MainDriver.NwcS-1; i<(MainDriver.NwcE - 1); i++){   // identify the location of the given x
			iPos = i;
			if(xVD>MainDriver.TVD[i + 1] && xVD<=MainDriver.TVD[i]) return iPos;
		}
		if (xVD > MainDriver.TVD[MainDriver.NwcS-1]) return (MainDriver.NwcS-1);
		else if (xVD <= MainDriver.TVD[MainDriver.NwcE-1]) return (MainDriver.NwcE - 2);
		else {
			System.out.println("too big number");
			return (MainDriver.NwcS-1);		    	
		}
		//
	}

	static double getHorizDeparture(double xLoc){ //util
		// the program to calculate the horizontal departure from total MD
		// xloc;MD from surface, xHdepart; calculated HD from MD, ft
		//

		int iLoc=1;
		double xHdepart = 0, xAngle = 0, radX=0, rad1=0, rad2=0, delTmp=0;//, radY=0  an unused variable(20130806 ajw)
		if (xLoc < MainDriver.DepthKOP) return xHdepart;
		if (MainDriver.iWell == 0) return xHdepart;

		iLoc = Xposition(xLoc);
		xAngle = getAngle(xLoc);

		if(xAngle < (MainDriver.angEOB - 0.001)){  //HD of First Build Section
			radX = xAngle * MainDriver.radConv;
			xHdepart = (MainDriver.Rbur * (1 - Math.cos(radX)));
		}
		else if(xAngle < (MainDriver.angEOB + 0.001)){
			rad1 = MainDriver.angEOB * MainDriver.radConv;
			delTmp = xLoc - MainDriver.TMD[iLoc + 1];
			xHdepart =  (MainDriver.Rbur * (1 - Math.cos(rad1)) + delTmp * Math.sin(rad1));
		}
		else if(xAngle < (MainDriver.ang2EOB - 0.001)){
			rad1 = MainDriver.angEOB * MainDriver.radConv;
			xHdepart =  (MainDriver.Rbur * (1 - Math.cos(rad1)) + MainDriver.xHold * Math.sin(rad1));
			radX = xAngle * MainDriver.radConv;
			xHdepart =  (xHdepart + MainDriver.R2bur * (Math.cos(rad1) - Math.cos(radX)));
		}
		else{
			rad1 = MainDriver.angEOB * MainDriver.radConv;
			rad2 = MainDriver.ang2EOB * MainDriver.radConv;
			xHdepart =  (MainDriver.Rbur * (1 - Math.cos(rad1)) + MainDriver.xHold * Math.sin(rad1));
			xHdepart =  (xHdepart + MainDriver.R2bur * (Math.cos(rad1) - Math.cos(rad2)));
			delTmp = xLoc - MainDriver.TMD[iLoc + 1];
			xHdepart =  (xHdepart + delTmp * Math.sin(rad2));
		}

		return xHdepart;

		//
	}

	static int node2pW(double vGtot, double vLtot, double tmp_T){
		//
		//sub to assine values for WBM: Modified by TY
		// = sum of the following two subs, because volg(),hgnd() are known
		// sub node2P(xstart, voltot, hgcell, dxcell, nstart, ncount)
		//     sub to assining nodal information for two-phase zone
		// sub getGrid(xlength,delx2,xstart,d2,d1)
		//     sub to calculate location and gas fraction at each node
		//

		double volTot=0, hgCell=0, hhm=0;
		MainDriver.Ncount = MainDriver.Nstart;
		//--- adjust the gas fraction to avoid to high choke pressure for single-phase
		volTot = vGtot + vLtot;
		if(volTot < 0.005) return -1;
		hgCell = vGtot / volTot;

		if (hgCell > 0.6) hgCell = 0.6 + (hgCell - 0.6) * 0.2 / 0.4;
		//

		if (MainDriver.Nstart==1) {
			MainDriver.Hgnd[0] = 0; MainDriver.volG[0] = 0;
			MainDriver.volL[0] = 0; MainDriver.Xnd[0] = MainDriver.Xstart;
			MainDriver.delta_T[0] = 0;
		}

		hhm = getBotH(volTot, MainDriver.Xstart);
		MainDriver.Ncount = MainDriver.Nstart + 1;
		MainDriver.Nstart = MainDriver.Ncount;
		MainDriver.Xstart = MainDriver.Xstart - hhm;
		//
		MainDriver.Hgnd[MainDriver.Ncount-1] = hgCell; MainDriver.Xnd[MainDriver.Ncount-1] = MainDriver.Xstart;
		MainDriver.volG[MainDriver.Ncount-1] = vGtot;  MainDriver.volL[MainDriver.Ncount-1] = vLtot;
		//Added by TY
		MainDriver.volGold[MainDriver.Ncount-1] = MainDriver.volG[ MainDriver.Ncount-1];
		MainDriver.delta_T[MainDriver.Ncount-1] = tmp_T;

		return 0;
	}

	static int node2p(double vGtot, double vLtot, double vLinc, double gortemp){//util , dummy로 받아서 0으로 만드는 걸로...
		//
		//sub to assine values for OBM: Modified by TY
		//
		// = sum of the following two subs, because volg(),hgnd() are known
		// sub node2P(xstart, voltot, hgcell, dxcell, nstart, ncount)
		//     sub to assining nodal information for two-phase zone
		// sub getGrid(xlength,delx2,xstart,d2,d1)
		//     sub to calculate location and gas fraction at each node
		//
		double volTot=0, hgCell=0, hhm=0;

		MainDriver.Ncount = MainDriver.Nstart;
		//--- adjust the gas fraction to avoid to high choke pressure for single-phase
		volTot = vGtot + vLtot;
		if(volTot < 0.005) return 0;

		hgCell = vGtot / volTot;

		if (hgCell > 0.6) hgCell = (0.6 + (hgCell - 0.6) * 0.2 / 0.4);
		//
		if (MainDriver.Nstart==1) {
			MainDriver.Hgnd[0] = 0; MainDriver.volG[0] = 0;
			MainDriver.volL[0] = 0; MainDriver.Xnd[0] = MainDriver.Xstart;

			//MODIFIED BY TY
			MainDriver.gor[0] = 0;
			MainDriver.PVTZ_sol[0] = 0;
			MainDriver.PVTZ_Gas[0] = 0;
			MainDriver.Vdiff_old[0] = 0;
			MainDriver.Kicknd[0] = MainDriver.Xnd[0];
			MainDriver.delta_T[0] = 0;
		}
		//
		hhm = getBotH(volTot, MainDriver.Xstart);
		MainDriver.Ncount = MainDriver.Nstart + 1;
		MainDriver.Nstart = MainDriver.Ncount;
		MainDriver.Xstart = MainDriver.Xstart - hhm;
		//
		MainDriver.Hgnd[MainDriver.Ncount-1] = hgCell; MainDriver.Xnd[MainDriver.Ncount-1] = MainDriver.Xstart;
		MainDriver.volG[MainDriver.Ncount-1] = vGtot;  MainDriver.volL[MainDriver.Ncount-1] = vLtot;

		MainDriver.gor[MainDriver.Ncount-1] = gortemp;
		MainDriver.PVTZ_sol[MainDriver.Ncount-1] = MainDriver.tmp_solgas;
		MainDriver.PVTZ_free[MainDriver.Ncount-1] = MainDriver.tmp_freegas;
		MainDriver.Vdiff_old[MainDriver.Ncount-1] = vLinc;
		MainDriver.PVTZ_Gas[MainDriver.Ncount-1] = MainDriver.tmp_gas;
		MainDriver.delta_T[MainDriver.Ncount-1] = MainDriver.tmp_T;

		return 0;//dummy로 받아줄 것..
		//
	}

	static void OBM_Fraction(double GasFrac, double OBMFrac){

		MainDriver.MW_C[0] = 16.04;
		MainDriver.MW_C[7] = 108;
		MainDriver.MW_C[8] = 121;
		MainDriver.MW_C[9] = 134;
		MainDriver.MW_C[10] = 147;
		MainDriver.MW_C[11] = 161;
		MainDriver.MW_C[12] = 175;
		MainDriver.MW_C[13] = 190;
		MainDriver.MW_C[14] = 206;
		MainDriver.MW_C[15] = 222;
		MainDriver.MW_C[16] = 237;
		MainDriver.MW_C[17] = 251;
		MainDriver.MW_C[18] = 263;
		MainDriver.MW_C[19] = 275;
		MainDriver.MW_C[20] = 291;
		MainDriver.MW_C[21] = 305;
		MainDriver.MW_C[22] = 318;
		MainDriver.MW_C[23] = 331;
		MainDriver.MW_C[24] = 344;

		//PPG
		MainDriver.C_density[0] = 2.5;
		MainDriver.C_density[7] = 0.749*8.33;
		MainDriver.C_density[8] = 0.768*8.33;
		MainDriver.C_density[9] = 0.782*8.33;
		MainDriver.C_density[10] = 0.793*8.33;
		MainDriver.C_density[11] = 0.804*8.33;
		MainDriver.C_density[12] = 0.815*8.33;
		MainDriver.C_density[13] = 0.826*8.33;
		MainDriver.C_density[14] = 0.836*8.33;
		MainDriver.C_density[15] = 0.843*8.33;
		MainDriver.C_density[16] = 0.851*8.33;
		MainDriver.C_density[17] = 0.856*8.33;
		MainDriver.C_density[18] = 0.861*8.33;
		MainDriver.C_density[19] = 0.866*8.33;
		MainDriver.C_density[20] = 0.871*8.33;
		MainDriver.C_density[21] = 0.876*8.33;
		MainDriver.C_density[22] = 0.881*8.33;
		MainDriver.C_density[23] = 0.885*8.33;
		MainDriver.C_density[24] = 0.888*8.33;

		if(MainDriver.ibaseoil == 0){ //no.2 disel
			MainDriver.Mfrac_C[0] = GasFrac * 100;
			MainDriver.Mfrac_C[7] = 0.22 * OBMFrac * 100;
			MainDriver.Mfrac_C[8] = 0.88 * OBMFrac * 100;
			MainDriver.Mfrac_C[9] = 3.79 * OBMFrac * 100;
			MainDriver.Mfrac_C[10] = 10.68 * OBMFrac * 100;
			MainDriver.Mfrac_C[11] = 13.45 * OBMFrac * 100;
			MainDriver.Mfrac_C[12] = 13.73 * OBMFrac * 100;
			MainDriver.Mfrac_C[13] = 16.01 * OBMFrac * 100;	
			MainDriver.Mfrac_C[14] = 15.18 * OBMFrac * 100;
			MainDriver.Mfrac_C[15] = 9.10 * OBMFrac * 100;
			MainDriver.Mfrac_C[16] = 8.53 * OBMFrac * 100;
			MainDriver.Mfrac_C[17] = 4.19 * OBMFrac * 100;
			MainDriver.Mfrac_C[18] = 2.40 * OBMFrac * 100;
			MainDriver.Mfrac_C[19] = 1.16 * OBMFrac * 100;
			MainDriver.Mfrac_C[20] = 0.42 * OBMFrac * 100;
			MainDriver.Mfrac_C[21] = 0.12 * OBMFrac * 100;
			MainDriver.Mfrac_C[22] = 0.11 * OBMFrac * 100;
			MainDriver.Mfrac_C[23] = 0.02 * OBMFrac * 100;
			MainDriver.Mfrac_C[24] = 0.01 * OBMFrac * 100;
		}
		//		   

		else if(MainDriver.ibaseoil == 1){ //mentor 28
			//
			//initialize
			for(int i = 7; i<25; i++){
				MainDriver.Mfrac_C[i] = 0;
			}
			//
			MainDriver.Mfrac_C[0] = GasFrac * 100;
			MainDriver.Mfrac_C[11] = 1.4187 * OBMFrac * 100;
			MainDriver.Mfrac_C[12] = 2.2240 * OBMFrac * 100;
			MainDriver.Mfrac_C[13] = 6.0817 * OBMFrac * 100;
			MainDriver.Mfrac_C[14] = 10.692 * OBMFrac * 100;
			MainDriver.Mfrac_C[15] = 9.4953 * OBMFrac * 100;
			MainDriver.Mfrac_C[16] = 31.837 * OBMFrac * 100;
			MainDriver.Mfrac_C[17] = 28.187 * OBMFrac * 100;
			MainDriver.Mfrac_C[18] = 10.0643 * OBMFrac * 100;
		}
		//
		else{ //conoco LVT
			//
			//initialize
			for(int i = 7; i<25; i++){
				MainDriver.Mfrac_C[i] = 0;
			}
			//
			MainDriver.Mfrac_C[0] = GasFrac * 100;
			MainDriver.Mfrac_C[7] = 1.1736 * OBMFrac * 100;
			MainDriver.Mfrac_C[8] = 11.2037 * OBMFrac * 100;
			MainDriver.Mfrac_C[9] = 24.1092 * OBMFrac * 100;
			MainDriver.Mfrac_C[10] = 16.5396 * OBMFrac * 100;
			MainDriver.Mfrac_C[11] = 11.8951 * OBMFrac * 100;
			MainDriver.Mfrac_C[12] = 17.6742 * OBMFrac * 100;
			MainDriver.Mfrac_C[13] = 15.2455 * OBMFrac * 100;
			MainDriver.Mfrac_C[14] = 1.4253 * OBMFrac * 100;
			MainDriver.Mfrac_C[15] = 0.7338 * OBMFrac * 100;
			//
		}
	}

	static double PREOS(double pp, double tt, double GasFrac, double OBMFrac, double mole_solgas, double mole_OBM){ // return cont_v
		//Added by TY
		//calculate z-factor of OBM+gas mixture
		double EOSZ=0, EOSY=0, EOSA=0, EOSC=0;
		double a =0;
		double result =0;
		double dummy=0;
		MainDriver.Mw_total = 0;
		//Molecular weights
		MainDriver.MW_C[0] = 16.04;
		MainDriver.MW_C[7] = 108;
		MainDriver.MW_C[8] = 121;
		MainDriver.MW_C[9] = 134;
		MainDriver.MW_C[10] = 147;
		MainDriver.MW_C[11] = 161;
		MainDriver.MW_C[12] = 175;
		MainDriver.MW_C[13] = 190;
		MainDriver.MW_C[14] = 206;
		MainDriver.MW_C[15] = 222;
		MainDriver.MW_C[16] = 237;
		MainDriver.MW_C[17] = 251;
		MainDriver.MW_C[18] = 263;
		MainDriver.MW_C[19] = 275;
		MainDriver.MW_C[20] = 291;
		MainDriver.MW_C[21] = 305;
		MainDriver.MW_C[22] = 318;
		MainDriver.MW_C[23] = 331;
		MainDriver.MW_C[24] = 344;
		//
		//Critical temperatures
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
		//
		//Critical pressures
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
		//
		//Accentric factors at a critical temperature
		MainDriver.Ac_C[0] = 0.0115;
		MainDriver.Ac_C[7] = 0.332;
		MainDriver.Ac_C[8] = 0.373;
		MainDriver.Ac_C[9] = 0.411;
		MainDriver.Ac_C[10] = 0.448;
		MainDriver.Ac_C[11] = 0.484;
		MainDriver.Ac_C[12] = 0.518;
		MainDriver.Ac_C[13] = 0.551;
		MainDriver.Ac_C[14] = 0.582;
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
		//
		//Binary coefficient at a critical temperature with methane
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
		MainDriver.Bi_C[17] = 0.0530;
		MainDriver.Bi_C[18] = 0.0537;
		MainDriver.Bi_C[19] = 0.0544;
		MainDriver.Bi_C[20] = 0.0551;
		MainDriver.Bi_C[21] = 0.0558;	
		MainDriver.Bi_C[22] = 0.0565;
		MainDriver.Bi_C[23] = 0.0571;
		MainDriver.Bi_C[24] = 0.0575;
		//
		if(MainDriver.ibaseoil == 0){ //no.2 disel
			MainDriver.Mfrac_C[0] = GasFrac;
			MainDriver.Mfrac_C[7] = 0.22 * OBMFrac;
			MainDriver.Mfrac_C[8] = 0.88 * OBMFrac;
			MainDriver.Mfrac_C[9] = 3.79 * OBMFrac;
			MainDriver.Mfrac_C[10] = 10.68 * OBMFrac;
			MainDriver.Mfrac_C[11] = 13.45 * OBMFrac;
			MainDriver.Mfrac_C[12] = 13.73 * OBMFrac;
			MainDriver.Mfrac_C[13] = 16.01 * OBMFrac;	
			MainDriver.Mfrac_C[14] = 15.18 * OBMFrac;
			MainDriver.Mfrac_C[15] = 9.10 * OBMFrac;
			MainDriver.Mfrac_C[16] = 8.53 * OBMFrac;
			MainDriver.Mfrac_C[17] = 4.19 * OBMFrac;
			MainDriver.Mfrac_C[18] = 2.40 * OBMFrac;
			MainDriver.Mfrac_C[19] = 1.16 * OBMFrac;
			MainDriver.Mfrac_C[20] = 0.42 * OBMFrac;
			MainDriver.Mfrac_C[21] = 0.12 * OBMFrac;
			MainDriver.Mfrac_C[22] = 0.11 * OBMFrac;
			MainDriver.Mfrac_C[23] = 0.02 * OBMFrac;
			MainDriver.Mfrac_C[24] = 0.01 * OBMFrac;
		}
		//		   

		else if(MainDriver.ibaseoil == 1){ //mentor 28
			//
			//initialize
			for(int i = 7; i<25; i++){
				MainDriver.Mfrac_C[i] = 0;
			}
			//
			MainDriver.Mfrac_C[0] = GasFrac;
			MainDriver.Mfrac_C[11] = 1.4187 * OBMFrac;
			MainDriver.Mfrac_C[12] = 2.2240 * OBMFrac;
			MainDriver.Mfrac_C[13] = 6.0817 * OBMFrac;
			MainDriver.Mfrac_C[14] = 10.692 * OBMFrac;
			MainDriver.Mfrac_C[15] = 9.4953 * OBMFrac;
			MainDriver.Mfrac_C[16] = 31.837 * OBMFrac;
			MainDriver.Mfrac_C[17] = 28.187 * OBMFrac;
			MainDriver.Mfrac_C[18] = 10.0643 * OBMFrac;
		}
		//
		else{ //conoco LVT
			//
			//initialize
			for(int i = 7; i<25; i++){
				MainDriver.Mfrac_C[i] = 0;
			}
			//
			MainDriver.Mfrac_C[0] = GasFrac;
			MainDriver.Mfrac_C[7] = 1.1736 * OBMFrac;
			MainDriver.Mfrac_C[8] = 11.2037 * OBMFrac;
			MainDriver.Mfrac_C[9] = 24.1092 * OBMFrac;
			MainDriver.Mfrac_C[10] = 16.5396 * OBMFrac;
			MainDriver.Mfrac_C[11] = 11.8951 * OBMFrac;
			MainDriver.Mfrac_C[12] = 17.6742 * OBMFrac;
			MainDriver.Mfrac_C[13] = 15.2455 * OBMFrac;
			MainDriver.Mfrac_C[14] = 1.4253 * OBMFrac;
			MainDriver.Mfrac_C[15] = 0.7338 * OBMFrac;
			//
		}
		//
		for(int i = 0; i<25; i++){
			if(MainDriver.Pc_C[i] == 0) MainDriver.Pc_C[i] = 1;
			if(MainDriver.Tc_C[i] == 0) MainDriver.Tc_C[i] = 1;
		}
		//
		//calculate a(Tc)
		for(int i = 0; i<25; i++){
			MainDriver.aTc[i] = 0.45724 * Math.pow(MainDriver.R, 2) * Math.pow(MainDriver.Tc_C[i], 2) / MainDriver.Pc_C[i];		        
		}
		//
		//calculate a(T)
		for(int i = 0; i<25; i++){
			MainDriver.aT[i] = MainDriver.aTc[i] * Math.pow((1 + (0.37464 + (1.54226 *MainDriver.Ac_C[i]) - (0.26992 * Math.pow(MainDriver.Ac_C[i], 2))) * (1 - Math.sqrt(tt / MainDriver.Tc_C[i]))), 2);
		}
		//
		//calculate b
		for(int i = 0; i<25; i++){
			MainDriver.b[i] = 0.0778 * MainDriver.R * MainDriver.Tc_C[i] / MainDriver.Pc_C[i];
		}
		//
		//calculate FiBi
		for(int i = 0; i<25; i++){
			MainDriver.fibi[i] = MainDriver.Mfrac_C[i] * MainDriver.b[i];
		}
		//calculate Aij
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				MainDriver.Aij[i][j] = Math.sqrt(MainDriver.aT[i] * MainDriver.aT[j]);
			}
		}
		//

		//cosidering binary coefficient with c1, recalculate Aij
		for(int i = 0; i<25; i++){
			MainDriver.Aij[0][i] = (1 - MainDriver.Bi_C[i]) * Math.sqrt(MainDriver.aT[0]) * Math.sqrt(MainDriver.aT[i]);
		}
		//
		for(int i = 0; i<25; i++){
			MainDriver.Aij[i][0] = (1 - MainDriver.Bi_C[i]) * Math.sqrt(MainDriver.aT[i]) * Math.sqrt(MainDriver.aT[0]);
		}
		//
		//calculate fifj
		dummy=0;
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				MainDriver.fifj[i][j] = MainDriver.Mfrac_C[i] * MainDriver.Mfrac_C[j];
				dummy=dummy+MainDriver.fifj[i][j];
			}
		}
		dummy=0;
		//
		//calculate a by mixing rule
		a = 0;
		//
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				a = a + (MainDriver.Aij[i][j] * MainDriver.fifj[i][j]);
			}
		}
		//
		//calculate b by mixing rule
		double bsum = 0;
		//
		for(int i = 0; i<25; i++){
			bsum = bsum + MainDriver.fibi[i];
		}
		//
		//calculate A, C
		EOSA = a * pp / (Math.pow(MainDriver.R, 2) * Math.pow(tt, 2));
		EOSC = bsum * pp / (MainDriver.R * tt);
		MainDriver.eqA = 1;
		MainDriver.eqB = -(1 - EOSC);
		MainDriver.eqC = EOSA - 3 * Math.pow(EOSC, 2) - 2 * EOSC;
		MainDriver.eqD = -(EOSA * EOSC - Math.pow(EOSC, 2) - Math.pow(EOSC, 3));
		//
		EOSZ = Solveeqn(MainDriver.eqA, MainDriver.eqB, MainDriver.eqC, MainDriver.eqD); //sub to solve 3-dimension eq.
		//Assume mass 1lb
		//
		//if(MainDriver.EOSZ2 != 0 && MainDriver.EOSZ1 > MainDriver.EOSZ2) EOSZ = MainDriver.EOSZ2;
		//
		MainDriver.Mw_total = GasFrac * 16.04 + OBMFrac * 100 * MainDriver.OBMwt;
		MainDriver.mole_mixture = (mole_solgas * 16.04 + MainDriver.OBMwt * mole_OBM) / MainDriver.Mw_total; //total mass / molecular weight
		//ContV = (EOSZ * R * tt / pp) * (mole_solgas + mole_OBM) / 5.615
		result = (EOSZ * MainDriver.R * tt / pp) * (MainDriver.mole_mixture) / 5.615;
		return result;
		//
	}

	static void OBM_Composition_SK(){
		double  target_density = 0;
		for(int i = 0; i<25; i++){
			MainDriver.Mfrac_C[i] = 0;
			MainDriver.MW_C[i] = 0;
			MainDriver.C_density[i] = 0;
		}

		MainDriver.MW_C[0] = 16.04;
		MainDriver.MW_C[7] = 108;
		MainDriver.MW_C[8] = 121;
		MainDriver.MW_C[9] = 134;
		MainDriver.MW_C[10] = 147;
		MainDriver.MW_C[11] = 161;
		MainDriver.MW_C[12] = 175;
		MainDriver.MW_C[13] = 190;
		MainDriver.MW_C[14] = 206;
		MainDriver.MW_C[15] = 222;
		MainDriver.MW_C[16] = 237;
		MainDriver.MW_C[17] = 251;
		MainDriver.MW_C[18] = 263;
		MainDriver.MW_C[19] = 275;
		MainDriver.MW_C[20] = 291;
		MainDriver.MW_C[21] = 305;
		MainDriver.MW_C[22] = 318;
		MainDriver.MW_C[23] = 331;
		MainDriver.MW_C[24] = 344;

		//PPG
		MainDriver.C_density[0] = 2.5;
		MainDriver.C_density[7] = 0.749*8.33;
		MainDriver.C_density[8] = 0.768*8.33;
		MainDriver.C_density[9] = 0.782*8.33;
		MainDriver.C_density[10] = 0.793*8.33;
		MainDriver.C_density[11] = 0.804*8.33;
		MainDriver.C_density[12] = 0.815*8.33;
		MainDriver.C_density[13] = 0.826*8.33;
		MainDriver.C_density[14] = 0.836*8.33;
		MainDriver.C_density[15] = 0.843*8.33;
		MainDriver.C_density[16] = 0.851*8.33;
		MainDriver.C_density[17] = 0.856*8.33;
		MainDriver.C_density[18] = 0.861*8.33;
		MainDriver.C_density[19] = 0.866*8.33;
		MainDriver.C_density[20] = 0.871*8.33;
		MainDriver.C_density[21] = 0.876*8.33;
		MainDriver.C_density[22] = 0.881*8.33;
		MainDriver.C_density[23] = 0.885*8.33;
		MainDriver.C_density[24] = 0.888*8.33;

		if(MainDriver.ibaseoil == 0){ //No.2 diesel
			//Mole fraction 입력
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
		}

		else if(MainDriver.ibaseoil == 1){  // Mentor 28		    	
			MainDriver.Mfrac_C[11] = 1.4187;
			MainDriver.Mfrac_C[12] = 2.224;
			MainDriver.Mfrac_C[13] = 6.0817;
			MainDriver.Mfrac_C[14] = 10.692;
			MainDriver.Mfrac_C[15] = 9.4953;
			MainDriver.Mfrac_C[16] = 31.837;
			MainDriver.Mfrac_C[17] = 28.187;
			MainDriver.Mfrac_C[18] = 10.0643;
		}

		else if(MainDriver.ibaseoil == 2){  //Conoco LVT
			MainDriver.Mfrac_C[7] = 1.1736;
			MainDriver.Mfrac_C[8] = 11.2037;
			MainDriver.Mfrac_C[9] = 24.1092;
			MainDriver.Mfrac_C[10] = 16.5396;
			MainDriver.Mfrac_C[11] = 11.8951;
			MainDriver.Mfrac_C[12] = 17.6742;
			MainDriver.Mfrac_C[13] = 15.2455;
			MainDriver.Mfrac_C[14] = 1.4253;
			MainDriver.Mfrac_C[15] = 0.7338;		    	
		}

		//60F, 14.7 조건에서 mud 생성 후 주입 가정
		MainDriver.oilDensity_sc = calcSKdensity_oil(520, 0);
		MainDriver.BariteDensity = 4.22*8.33;
		MainDriver.BrineDensity = 1.3*8.33;
		MainDriver.OBMDensity_sc = MainDriver.oMud;

		if(MainDriver.icase_mode==0){ //case mode
			MainDriver.vOil = 298;//gal
			MainDriver.vBrine = 52;
			MainDriver.vBarite = (MainDriver.OBMDensity_sc*(MainDriver.vOil+MainDriver.vBrine)-MainDriver.vOil*MainDriver.oilDensity_sc-MainDriver.vBrine*MainDriver.BrineDensity)/(MainDriver.BariteDensity - MainDriver.OBMDensity_sc);
			MainDriver.foil = MainDriver.vOil / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			MainDriver.fbrine = MainDriver.vBrine / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			MainDriver.fadditive = MainDriver.vBarite / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			
			target_density = MainDriver.OBMDensity_sc + MainDriver.KICKintens;
			
			MainDriver.vOil = 298; //gal
			MainDriver.vBrine = 52;
			MainDriver.vBarite2 = (target_density * (MainDriver.vOil + MainDriver.vBrine) - MainDriver.vOil * MainDriver.oilDensity_sc - MainDriver.vBrine * MainDriver.BrineDensity) / (MainDriver.BariteDensity - target_density);
			MainDriver.foil_kill = MainDriver.vOil / (MainDriver.vOil + MainDriver.vBrine + MainDriver.vBarite2);
			MainDriver.fbrine_kill = MainDriver.vBrine / (MainDriver.vOil + MainDriver.vBrine + MainDriver.vBarite2);
			MainDriver.fadditive_kill = MainDriver.vBarite2 / (MainDriver.vOil + MainDriver.vBrine + MainDriver.vBarite2);
		}
		else{ //user mode
			MainDriver.vOil = 298;//gal
			MainDriver.vBrine = ((MainDriver.OBMDensity_sc-MainDriver.BariteDensity)*MainDriver.vOil*(1/MainDriver.foil-1))/(MainDriver.BrineDensity-MainDriver.BariteDensity);
			MainDriver.vBarite = MainDriver.vOil/MainDriver.foil-MainDriver.vOil-MainDriver.vBrine;
			MainDriver.fbrine = MainDriver.vBrine / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			MainDriver.fadditive = MainDriver.vBarite / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			
			target_density = MainDriver.OBMDensity_sc + MainDriver.KICKintens;
			
			MainDriver.vOil = 298;//gal
			MainDriver.foil_kill = MainDriver.foil;
			MainDriver.vBrine = ((target_density-MainDriver.BariteDensity)*MainDriver.vOil*(1/MainDriver.foil_kill-1))/(MainDriver.BrineDensity-MainDriver.BariteDensity);
			MainDriver.vBarite2 = MainDriver.vOil/MainDriver.foil_kill-MainDriver.vOil-MainDriver.vBrine;
			MainDriver.fbrine_kill = MainDriver.vBrine / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite2);
			MainDriver.fadditive_kill = MainDriver.vBarite2 / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite2);
		}		
	}		

	static void OBM_Composition_PREOS(){
		double target_density=0;

		for(int i = 0; i<25; i++){
			MainDriver.Mfrac_C[i] = 0;
			MainDriver.MW_C[i] = 0;
			MainDriver.C_density[i] = 0;
		}

		MainDriver.MW_C[0] = 16.04;
		MainDriver.MW_C[7] = 108;
		MainDriver.MW_C[8] = 121;
		MainDriver.MW_C[9] = 134;
		MainDriver.MW_C[10] = 147;
		MainDriver.MW_C[11] = 161;
		MainDriver.MW_C[12] = 175;
		MainDriver.MW_C[13] = 190;
		MainDriver.MW_C[14] = 206;
		MainDriver.MW_C[15] = 222;
		MainDriver.MW_C[16] = 237;
		MainDriver.MW_C[17] = 251;
		MainDriver.MW_C[18] = 263;
		MainDriver.MW_C[19] = 275;
		MainDriver.MW_C[20] = 291;
		MainDriver.MW_C[21] = 305;
		MainDriver.MW_C[22] = 318;
		MainDriver.MW_C[23] = 331;
		MainDriver.MW_C[24] = 344;

		//PPG
		MainDriver.C_density[0] = 2.5;
		MainDriver.C_density[7] = 0.749*8.33;
		MainDriver.C_density[8] = 0.768*8.33;
		MainDriver.C_density[9] = 0.782*8.33;
		MainDriver.C_density[10] = 0.793*8.33;
		MainDriver.C_density[11] = 0.804*8.33;
		MainDriver.C_density[12] = 0.815*8.33;
		MainDriver.C_density[13] = 0.826*8.33;
		MainDriver.C_density[14] = 0.836*8.33;
		MainDriver.C_density[15] = 0.843*8.33;
		MainDriver.C_density[16] = 0.851*8.33;
		MainDriver.C_density[17] = 0.856*8.33;
		MainDriver.C_density[18] = 0.861*8.33;
		MainDriver.C_density[19] = 0.866*8.33;
		MainDriver.C_density[20] = 0.871*8.33;
		MainDriver.C_density[21] = 0.876*8.33;
		MainDriver.C_density[22] = 0.881*8.33;
		MainDriver.C_density[23] = 0.885*8.33;
		MainDriver.C_density[24] = 0.888*8.33;

		if(MainDriver.ibaseoil == 0){ //No.2 diesel
			//Mole fraction 입력
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
		}

		else if(MainDriver.ibaseoil == 1){  // Mentor 28		    	
			MainDriver.Mfrac_C[11] = 1.4187;
			MainDriver.Mfrac_C[12] = 2.224;
			MainDriver.Mfrac_C[13] = 6.0817;
			MainDriver.Mfrac_C[14] = 10.692;
			MainDriver.Mfrac_C[15] = 9.4953;
			MainDriver.Mfrac_C[16] = 31.837;
			MainDriver.Mfrac_C[17] = 28.187;
			MainDriver.Mfrac_C[18] = 10.0643;
		}

		else if(MainDriver.ibaseoil == 2){  //Conoco LVT
			MainDriver.Mfrac_C[7] = 1.1736;
			MainDriver.Mfrac_C[8] = 11.2037;
			MainDriver.Mfrac_C[9] = 24.1092;
			MainDriver.Mfrac_C[10] = 16.5396;
			MainDriver.Mfrac_C[11] = 11.8951;
			MainDriver.Mfrac_C[12] = 17.6742;
			MainDriver.Mfrac_C[13] = 15.2455;
			MainDriver.Mfrac_C[14] = 1.4253;
			MainDriver.Mfrac_C[15] = 0.7338;		    	
		}

		//60F, 14.7 조건에서 mud 생성 후 주입 가정
		MainDriver.oilDensity_sc = calcPREOSdensity(520, 14.7);
		MainDriver.BariteDensity = 4.22*8.33;
		MainDriver.BrineDensity = 1.3*8.33;
		MainDriver.OBMDensity_sc = MainDriver.oMud;

		if(MainDriver.icase_mode==0){ //case mode
			MainDriver.vOil = 298;//gal
			MainDriver.vBrine = 52;
			MainDriver.vBarite = (MainDriver.OBMDensity_sc*(MainDriver.vOil+MainDriver.vBrine)-MainDriver.vOil*MainDriver.oilDensity_sc-MainDriver.vBrine*MainDriver.BrineDensity)/(MainDriver.BariteDensity - MainDriver.OBMDensity_sc);
			MainDriver.foil = MainDriver.vOil / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			MainDriver.fbrine = MainDriver.vBrine / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			MainDriver.fadditive = MainDriver.vBarite / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			
			target_density = MainDriver.OBMDensity_sc + MainDriver.KICKintens;
			
			MainDriver.vOil = 298; //gal
			MainDriver.vBrine = 52;
			MainDriver.vBarite2 = (target_density * (MainDriver.vOil + MainDriver.vBrine) - MainDriver.vOil * MainDriver.oilDensity_sc - MainDriver.vBrine * MainDriver.BrineDensity) / (MainDriver.BariteDensity - target_density);
			MainDriver.foil_kill = MainDriver.vOil / (MainDriver.vOil + MainDriver.vBrine + MainDriver.vBarite2);
			MainDriver.fbrine_kill = MainDriver.vBrine / (MainDriver.vOil + MainDriver.vBrine + MainDriver.vBarite2);
			MainDriver.fadditive_kill = MainDriver.vBarite2 / (MainDriver.vOil + MainDriver.vBrine + MainDriver.vBarite2);
		}
		else{ //user mode
			MainDriver.vOil = 298;//gal
			MainDriver.vBrine = (MainDriver.OBMdensity*MainDriver.vOil/MainDriver.foil-MainDriver.BariteDensity*MainDriver.vOil*(1/MainDriver.foil-1))/(MainDriver.BrineDensity-MainDriver.BariteDensity);
			MainDriver.vBarite = MainDriver.vOil/MainDriver.foil-MainDriver.vOil-MainDriver.vBrine;
			MainDriver.fbrine = MainDriver.vBrine / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			MainDriver.fadditive = MainDriver.vBarite / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite);
			
			target_density = MainDriver.OBMDensity_sc + MainDriver.KICKintens;
			
			MainDriver.vOil = 298;//gal
			MainDriver.foil_kill = MainDriver.foil;
			MainDriver.vBrine = (target_density*MainDriver.vOil/MainDriver.foil_kill-MainDriver.BariteDensity*MainDriver.vOil*(1/MainDriver.foil_kill-1))/(MainDriver.BrineDensity-MainDriver.BariteDensity);
			MainDriver.vBarite2 = MainDriver.vOil/MainDriver.foil_kill-MainDriver.vOil-MainDriver.vBrine;
			MainDriver.fbrine_kill = MainDriver.vBrine / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite2);
			MainDriver.fadditive_kill = MainDriver.vBarite2 / (MainDriver.vOil+MainDriver.vBrine+MainDriver.vBarite2);
		}		
	}		

	static double calcMW(){
		double result = 0;
		if(MainDriver.ibaseoil == 0){ //No.2 diesel
			//Mole fraction 입력
			MainDriver.Mfrac_C_oil[7] = 0.22;
			MainDriver.Mfrac_C_oil[8] = 0.88;
			MainDriver.Mfrac_C_oil[9] = 3.79;	
			MainDriver.Mfrac_C_oil[10] = 10.68;
			MainDriver.Mfrac_C_oil[11] = 13.45;
			MainDriver.Mfrac_C_oil[12] = 13.73;
			MainDriver.Mfrac_C_oil[13] = 16.01;
			MainDriver.Mfrac_C_oil[14] = 15.18;
			MainDriver.Mfrac_C_oil[15] = 9.10;
			MainDriver.Mfrac_C_oil[16] = 8.53;
			MainDriver.Mfrac_C_oil[17] = 4.19;
			MainDriver.Mfrac_C_oil[18] = 2.40;
			MainDriver.Mfrac_C_oil[19] = 1.16;
			MainDriver.Mfrac_C_oil[20] = 0.42;
			MainDriver.Mfrac_C_oil[21] = 0.12;
			MainDriver.Mfrac_C_oil[22] = 0.11;
			MainDriver.Mfrac_C_oil[23] = 0.02;
			MainDriver.Mfrac_C_oil[24] = 0.01;
		}

		else if(MainDriver.ibaseoil == 1){  // Mentor 28		    	
			MainDriver.Mfrac_C_oil[11] = 1.4187;
			MainDriver.Mfrac_C_oil[12] = 2.224;
			MainDriver.Mfrac_C_oil[13] = 6.0817;
			MainDriver.Mfrac_C_oil[14] = 10.692;
			MainDriver.Mfrac_C_oil[15] = 9.4953;
			MainDriver.Mfrac_C_oil[16] = 31.837;
			MainDriver.Mfrac_C_oil[17] = 28.187;
			MainDriver.Mfrac_C_oil[18] = 10.0643;
		}

		else if(MainDriver.ibaseoil == 2){  //Conoco LVT
			MainDriver.Mfrac_C_oil[7] = 1.1736;
			MainDriver.Mfrac_C_oil[8] = 11.2037;
			MainDriver.Mfrac_C_oil[9] = 24.1092;
			MainDriver.Mfrac_C_oil[10] = 16.5396;
			MainDriver.Mfrac_C_oil[11] = 11.8951;
			MainDriver.Mfrac_C_oil[12] = 17.6742;
			MainDriver.Mfrac_C_oil[13] = 15.2455;
			MainDriver.Mfrac_C_oil[14] = 1.4253;
			MainDriver.Mfrac_C_oil[15] = 0.7338;		    	
		}

		for(int i = 7; i<=24; i++){
			result = result + MainDriver.MW_C[i] * MainDriver.Mfrac_C_oil[i];
		}
		result = result / 100;
		//if(MainDriver.ibaseoil == 0) result = 199;
		//else if(MainDriver.ibaseoil == 1) result = 252;
		//else result = 177;

		return result;
	}

	static double pxBottom_densChange(double volPump, double xLoc){
		//...... sub to calculate the pressure at the given position using given BHP, pb
		// this is good for on- or off-shore locations and driller//s method
		// and engineer//s method for variable geometries.
		// pbtm ; psi at the xloc, xloc;ft, volpump;total pumping kill mud volume, bbls
		//getDP_kill_auto(Qflow, xB, xT, bottomP, rho, targetP)
		double Pbtm = 0;
		double px = 0;
		double h = 0;
		double xm2vd=0;
		double xlocvd = 0;
		double hmo = 0;
		double hmk = 0;
		double volkill = 0;
		double hmud=0;
		double xm2=0;

		if (volPump <= MainDriver.VOLinn){
			px = getDP_kill_auto(MainDriver.Qkill, MainDriver.TMD[MainDriver.NwcS-1], xLoc, MainDriver.Pform, MainDriver.oMud);
			Pbtm = px;
		}
		else{
			h = MainDriver.TMD[MainDriver.NwcS-1];
			xm2vd = MainDriver.TVD[MainDriver.NwcS-1];
			xlocvd = getVD(xLoc);
			//....................................... old mud only in the annulus
			hmo = h - xLoc;
			hmk = 0;
			volkill = volPump - MainDriver.VOLinn;
			hmud = getBotH(volkill, h);
			hmk = hmud;
			hmk = Math.min(hmk, hmo);
			hmo = h - hmk - xLoc; //old mud height
			xm2 = h - hmk; // kill mud location
			xm2vd = getVD(xm2); //xm2vd; kill mud vertical location
			px = getDP_kill_auto(MainDriver.Qkill, MainDriver.TMD[MainDriver.NwcS-1], xm2, MainDriver.Pform, MainDriver.Cmud);
			Pbtm = getDP_kill_auto(MainDriver.Qkill, xm2, xLoc, px, MainDriver.oMud);
		}
		return Pbtm;
	}

	static double getDP_kill_auto(double Qflow, double xB, double xT, double bottomP, double rho){ //Drilling mode에서 oil의 밀도변화 고려할 때 getdp대신 사용하여 마찰손실 계산
		//....... sub to calculate frictional pressure drop at a given point below (DPbot) or above (DPtop).
		//Qflow; total flow rate at standard condition
		//xLoc ; 기준위치, 이 위치를 기준으로 위, 아래에서 손실되는 마찰 계산
		//rho ; mud density at standard condition
		double targetP = 0;
		double[] DPf = new double[10];
		double pxcell = 0;
		double tx = 0;
		double[] depth_temp = new double[MainDriver.Nwc];
		double oilDen_ref = 0;
		double hydroP = 0;
		double hydroAll = 0;
		double tbottom = 0;
		double pbottom = 0;
		double px1 = 0;
		double px2 = 0;
		double pmid = 0;
		double pbtm_try = 0;
		int iter =0;
		double Den_boundary = 0;
		int iBtm = 0;
		int iTop = 0;
		double dpall = 0;
		double xBvd = 0;
		double xTvd = 0;
		double Err=0;
		double Qeff=0;
		double[] getLinesEx = new double[5];
		double d1 = 0;
		double d2 = 0;
		double vls = 0;
		double Ptop_temp = 0;
		double oilDen = 0;
		//............................. calculate DP for all section of annulus
		dpall = 0;
		xBvd = getVD(xB);
		xTvd = getVD(xT);
		iBtm = Xposition(xB);
		iTop = Xposition(xT);
		if(xT==0){
			iTop = MainDriver.NwcE - 1;
		}
		for(int i = MainDriver.NwcS-1; i<MainDriver.NwcE; i++){
			MainDriver.TMD_tmp[i] = MainDriver.TMD[i];
			MainDriver.TVD_tmp[i] = MainDriver.TVD[i];
			MainDriver.Do2p_tmp[i] = MainDriver.Do2p[i];
			MainDriver.Di2p_tmp[i] = MainDriver.Di2p[i];
			if(xT<MainDriver.TMD[i] && xT>MainDriver.TMD[i+1]){
				MainDriver.TMD_tmp[iTop + 1] = xT;		    
				MainDriver.TVD_tmp[iTop + 1] = xTvd;
				MainDriver.Do2p_tmp[iTop + 1] = MainDriver.Do2p[i+1];
				MainDriver.Di2p_tmp[iTop + 1] = MainDriver.Di2p[i+1];
				break;
			}
			else if(xT==MainDriver.TMD[i]){
				iTop = iTop-1;
			}
		}
		MainDriver.TMD_tmp[iBtm] = xB;
		MainDriver.TVD_tmp[iBtm] = xBvd;	
		MainDriver.Do2p_tmp[iBtm] = MainDriver.Do2p[iBtm+1];
		MainDriver.Di2p_tmp[iBtm] = MainDriver.Di2p[iBtm+1];
		pxcell = 14.7; //표면
		tx = temperature(0);

		if(MainDriver.mud_calc == 0){
			oilDen_ref = calcPREOSdensity(tx, pxcell);
		}
		else{
			oilDen_ref = calcSKdensity_oil(tx, pxcell - 14.7); //ppg
		}

		pxcell = bottomP; //bottomhole		    

		if(MainDriver.iOnshore == 2 && iTop == MainDriver.NwcE-2){		            
			for(int i = iBtm + 1; i<=iTop; i++){
				Err = 1;
				iter = 1;
				px1 = pxcell - 0.052 * (2 * rho) * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]);
				px2 = pxcell;

				while(Math.abs(Err) > 0.000001 && iter < 51){
					iter = iter + 1;
					pmid = (px1 + px2) / 2;
					tx = temperature((MainDriver.TMD_tmp[i-1]+MainDriver.TMD_tmp[i])/2);

					if(MainDriver.mud_calc == 0){
						oilDen = calcPREOSdensity(tx, pmid);
					}
					else{
						oilDen = calcSKdensity_oil(tx, pmid - 14.7); //ppg
					}

					if(rho == MainDriver.oMud){
						MainDriver.Den_Ann[i] = rho / (1 + MainDriver.foil * (oilDen_ref / oilDen - 1));
					}
					else{
						MainDriver.Den_Ann[i] = rho / (1 + MainDriver.foil_kill * (oilDen_ref / oilDen - 1));
					}

					d2 = MainDriver.Do2p_tmp[i];
					d1 = MainDriver.Di2p_tmp[i];
					Qeff = Qflow * rho / MainDriver.Den_Ann[i];
					vls = Qeff / 2.448 / (d2 * d2 - d1 * d1);
					DPf[i] = propertyModule.DPDL1p(vls, d2, d1, MainDriver.Den_Ann[i]);
					pbtm_try = pmid + 0.052 * MainDriver.Den_Ann[i] * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]) / 2 + DPf[i] * (MainDriver.TMD_tmp[i-1] - MainDriver.TMD_tmp[i]) / 2;
					Err = (pbtm_try - pxcell) / pxcell;

					if(Err < 0){
						px1 = pmid;
					}
					else{
						px2 = pmid;
					}
				}
				if(iter>=51){
					double dummy=1;
				}
				pxcell = pxcell - 0.052 * MainDriver.Den_Ann[i] * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]) - DPf[i] * (MainDriver.TMD_tmp[i-1] - MainDriver.TMD_tmp[i]);
				dpall = dpall + DPf[i] * (MainDriver.TMD_tmp[i-1] - MainDriver.TMD_tmp[i]);
				hydroAll = hydroAll + 0.052 * MainDriver.Den_Ann[i] * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]);
			}

			Err = 1;
			iter = 1;
			px1 = pxcell - 0.052 * (2 * rho) * (MainDriver.TVD_tmp[MainDriver.NwcE-2] - MainDriver.TVD_tmp[MainDriver.NwcE-1]);
			px2 = pxcell;
			while(Math.abs(Err) > 0.000001 && iter < 51){
				iter = iter + 1;
				pmid = (px1 + px2) / 2;
				tx = temperature((MainDriver.TMD_tmp[MainDriver.NwcE-1]+MainDriver.TMD_tmp[MainDriver.NwcE-2])/2);

				if(MainDriver.mud_calc == 0){
					oilDen = calcPREOSdensity(tx, pmid);
				}
				else{
					oilDen = calcSKdensity_oil(tx, pmid - 14.7); //ppg
				}

				if(rho == MainDriver.oMud){
					MainDriver.Den_Ann[MainDriver.NwcE-1] = rho / (1 + MainDriver.foil * (oilDen_ref / oilDen - 1));
				}
				else{
					MainDriver.Den_Ann[MainDriver.NwcE-1] = rho / (1 + MainDriver.foil_kill * (oilDen_ref / oilDen - 1));
				}

				Qeff = Qflow * rho / MainDriver.Den_Ann[MainDriver.NwcE-1];
				//index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
				getLinesEx = getLines(Qeff);
				Qeff = getLinesEx[0];
				d1 = getLinesEx[3];
				d2 = getLinesEx[4];
				vls = Qeff / 2.448 / (d2 * d2 - d1 * d1);
				DPf[MainDriver.NwcE-1] = propertyModule.DPDL1p(vls, d2, d1, MainDriver.Den_Ann[MainDriver.NwcE-1]);
				pbtm_try = pmid + 0.052 * MainDriver.Den_Ann[MainDriver.NwcE-1] * (MainDriver.TVD_tmp[MainDriver.NwcE-2] - MainDriver.TVD_tmp[MainDriver.NwcE-1]) / 2 + DPf[MainDriver.NwcE-1] * (MainDriver.TMD_tmp[MainDriver.NwcE-2] - MainDriver.TMD_tmp[MainDriver.NwcE-1]) / 2;
				Err = (pbtm_try - pxcell) / pxcell;

				if(Err < 0){
					px1 = pmid;
				}
				else{
					px2 = pmid;
				}
			}
			pxcell = pxcell - 0.052 * MainDriver.Den_Ann[MainDriver.NwcE-1] * (MainDriver.TVD_tmp[MainDriver.NwcE-2] - MainDriver.TVD_tmp[MainDriver.NwcE-1]) - DPf[MainDriver.NwcE-1] * (MainDriver.TMD_tmp[MainDriver.NwcE-2] - MainDriver.TMD_tmp[MainDriver.NwcE-1]);
			dpall = dpall + DPf[MainDriver.NwcE-1] * (MainDriver.TMD_tmp[MainDriver.NwcE - 2] - MainDriver.TMD_tmp[MainDriver.NwcE-1]);
			hydroAll = hydroAll + 0.052 * MainDriver.Den_Ann[MainDriver.NwcE-1] * (MainDriver.TVD_tmp[MainDriver.NwcE-2] - MainDriver.TVD_tmp[MainDriver.NwcE-1]);
		}
		else{
			for(int i = iBtm + 1; i<=iTop + 1; i++){
				Err = 1;
				iter = 1;
				px1 = pxcell - 0.052 * (2 * rho) * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]);
				px2 = pxcell;

				while(Math.abs(Err) > 0.000001 && iter < 51){
					iter = iter + 1;
					pmid = (px1 + px2) / 2;
					tx = temperature((MainDriver.TMD_tmp[i-1]+MainDriver.TMD_tmp[i])/2);

					if(MainDriver.mud_calc == 0){
						oilDen = calcPREOSdensity(tx, pmid);
					}
					else{
						oilDen = calcSKdensity_oil(tx, pmid - 14.7); //ppg
					}

					if(rho == MainDriver.oMud){
						MainDriver.Den_Ann[i] = rho / (1 + MainDriver.foil * (oilDen_ref / oilDen - 1));
					}
					else{
						MainDriver.Den_Ann[i] = rho / (1 + MainDriver.foil_kill * (oilDen_ref / oilDen - 1));
					}

					d2 = MainDriver.Do2p_tmp[i];
					d1 = MainDriver.Di2p_tmp[i];
					Qeff = Qflow * rho / MainDriver.Den_Ann[i];
					vls = Qeff / 2.448 / (d2 * d2 - d1 * d1);
					DPf[i] = propertyModule.DPDL1p(vls, d2, d1, MainDriver.Den_Ann[i]);
					pbtm_try = pmid + 0.052 * MainDriver.Den_Ann[i] * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]) / 2 + DPf[i] * (MainDriver.TMD_tmp[i-1] - MainDriver.TMD_tmp[i]) / 2;
					Err = (pbtm_try - pxcell) / pxcell;

					if(Err < 0){
						px1 = pmid;
					}
					else{
						px2 = pmid;
					}
				}
				if(iter>=51){
					double dummy=1;
				}
				pxcell = pxcell - 0.052 * MainDriver.Den_Ann[i] * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]) - DPf[i] * (MainDriver.TMD_tmp[i-1] - MainDriver.TMD_tmp[i]);
				dpall = dpall + DPf[i] * (MainDriver.TMD_tmp[i-1] - MainDriver.TMD_tmp[i]);
				hydroAll = hydroAll + 0.052 * MainDriver.Den_Ann[i] * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]);
			}
		}


		Ptop_temp = pxcell;

		//.............................. inside marine riser or choke/kill line
		//                           iBOP = 1 - BOP open, iBOP = 0 - BOP closed

		//...................... calculatee frictional pressure drop in annulus
		MainDriver.DPbot = 0;
		hydroP = 0;
		for(int i = iBtm + 1; i<=iTop + 1; i++){
			hydroP = hydroP + 0.052 * MainDriver.Den_Ann[i] * (MainDriver.TVD_tmp[i-1] - MainDriver.TVD_tmp[i]);
			MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD_tmp[i-1] - MainDriver.TMD_tmp[i]);
		}
		targetP = bottomP - MainDriver.DPbot - hydroP;
		if (xT > MainDriver.TMD_tmp[MainDriver.NwcS-1])  MainDriver.DPbot = 0;
		if (xT < MainDriver.TMD_tmp[MainDriver.NwcE-1])  MainDriver.DPbot = dpall;
		MainDriver.DPtop = dpall - MainDriver.DPbot;
		//
		return targetP;
	}
	static double calcPREOSdensity_2p(double temperature, double pressure, double GasFrac, double OBMFrac){
		double T_standard = 0;
		double EOSZ = 0;
		double EOSY = 0;
		double EOSA = 0;
		double EOSA_standard = 0;
		double EOSC = 0;
		double eqA = 0;
		double eqB = 0;
		double eqC = 0;
		double eqC_standard = 0;
		double eqD = 0;
		double eqD_standard = 0;
		double MW = 0;
		double[] Tboiling = new double[25];
		double [] VO = new double[25]; //Closest packed volume
		double a = 0;
		double a_standard = 0;
		double bsum = 0;	
		double zfactor = 0;
		double zfactor_standard = 0;
		double density = 0;
		double dummy = 0;
		double[] SG_exp= new double[25];
		double[] SG_calc= new double[25];
		double[] Tc_C_calc= new double[25];
		double[] ShiftingFactor= new double[25];
		double Vol = 0;
		double Vol_standard = 0;
		double VolChange = 0;

		MainDriver.R = 10.73; //psi-ft3/lbmole-R
		MW = calcMW();

		MainDriver.Mfrac_C_oil[0] = GasFrac * 100;
		for(int i=7; i<25; i++){
			MainDriver.Mfrac_C_oil[i] = MainDriver.Mfrac_C_oil[i] * OBMFrac * 100;
		}

		T_standard = 520;

		//Critical temperatures
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
		//
		//Boiling Temperature
		Tboiling[0] = -258.73+460;
		Tboiling[7] = 702;
		Tboiling[8] = 748;
		Tboiling[9] = 791;
		Tboiling[10] = 829;
		Tboiling[11] = 867;
		Tboiling[12] = 901;
		Tboiling[13] = 936;
		Tboiling[14] = 971;
		Tboiling[15] = 1002;
		Tboiling[16] = 1032;
		Tboiling[17] = 1055;
		Tboiling[18] = 1077;
		Tboiling[19] = 1101;
		Tboiling[20] = 1124;
		Tboiling[21] = 1146;
		Tboiling[22] = 1167;
		Tboiling[23] = 1187;
		Tboiling[24] = 1207;

		//Critical pressures
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
		//
		//Accentric factors at a critical temperature
		MainDriver.Ac_C[0] = 0.0115;
		MainDriver.Ac_C[7] = 0.332;
		MainDriver.Ac_C[8] = 0.373;
		MainDriver.Ac_C[9] = 0.411;
		MainDriver.Ac_C[10] = 0.448;
		MainDriver.Ac_C[11] = 0.484;
		MainDriver.Ac_C[12] = 0.518;
		MainDriver.Ac_C[13] = 0.551;
		MainDriver.Ac_C[14] = 0.582;
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
		//
		//Binary coefficient at a critical temperature with methane
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
		MainDriver.Bi_C[17] = 0.0530;
		MainDriver.Bi_C[18] = 0.0537;
		MainDriver.Bi_C[19] = 0.0544;
		MainDriver.Bi_C[20] = 0.0551;
		MainDriver.Bi_C[21] = 0.0558;	
		MainDriver.Bi_C[22] = 0.0565;
		MainDriver.Bi_C[23] = 0.0571;
		MainDriver.Bi_C[24] = 0.0575;

		//Specific Gravity at 60F
		SG_exp[0] = 0.300;
		SG_exp[7] = 0.749;
		SG_exp[8] = 0.768;
		SG_exp[9] = 0.782;
		SG_exp[10] = 0.793;
		SG_exp[11] = 0.804;
		SG_exp[12] = 0.815;
		SG_exp[13] = 0.826;
		SG_exp[14] = 0.836;
		SG_exp[15] = 0.843;
		SG_exp[16] = 0.851;
		SG_exp[17] = 0.856;
		SG_exp[18] = 0.861;
		SG_exp[19] = 0.866;
		SG_exp[20] = 0.871;
		SG_exp[21] = 0.876;
		SG_exp[22] = 0.881;
		SG_exp[23] = 0.885;
		SG_exp[24] = 0.888;
		//
		for(int i = 0; i<25; i++){
			if(MainDriver.Pc_C[i] == 0) MainDriver.Pc_C[i] = 1;
			if(MainDriver.Tc_C[i] == 0) MainDriver.Tc_C[i] = 1;
		}
		//
		//calculate a(Tc)
		for(int i = 0; i<25; i++){
			MainDriver.aTc[i] = 0.45724 * Math.pow(MainDriver.R, 2) * Math.pow(MainDriver.Tc_C[i], 2) / MainDriver.Pc_C[i];		        
		}
		//
		//calculate a(T)
		for(int i = 0; i<25; i++){
			MainDriver.aT[i] = MainDriver.aTc[i] * Math.pow((1 + (0.37464 + (1.54226 *MainDriver.Ac_C[i]) - (0.26992 * Math.pow(MainDriver.Ac_C[i], 2))) * (1 - Math.sqrt(temperature / MainDriver.Tc_C[i]))), 2);
		}
		//
		//calculate b
		for(int i = 0; i<25; i++){
			MainDriver.b[i] = 0.0778 * MainDriver.R * MainDriver.Tc_C[i] / MainDriver.Pc_C[i];
		}
		//
		//calculate FiBi
		for(int i = 0; i<25; i++){
			MainDriver.fibi[i] = MainDriver.Mfrac_C_oil[i] * MainDriver.b[i] / 100;
		}
		//calculate Aij
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				MainDriver.Aij[i][j] = Math.sqrt(MainDriver.aT[i] * MainDriver.aT[j]);
			}
		}
		//

		//cosidering binary coefficient with c1, recalculate Aij
		for(int i = 0; i<25; i++){
			MainDriver.Aij[0][i] = (1 - MainDriver.Bi_C[i]) * Math.sqrt(MainDriver.aT[0]) * Math.sqrt(MainDriver.aT[i]);
		}
		//
		for(int i = 0; i<25; i++){
			MainDriver.Aij[i][0] = (1 - MainDriver.Bi_C[i]) * Math.sqrt(MainDriver.aT[i]) * Math.sqrt(MainDriver.aT[0]);
		}
		//
		//calculate fifj
		dummy=0;
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				MainDriver.fifj[i][j] = MainDriver.Mfrac_C_oil[i] / 100 * MainDriver.Mfrac_C_oil[j] / 100;
				dummy=dummy+MainDriver.fifj[i][j];
			}
		}
		dummy=0;
		//
		//calculate a by mixing rule
		a = 0;
		//
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				a = a + (MainDriver.Aij[i][j] * MainDriver.fifj[i][j]);
			}
		}
		//
		//calculate b by mixing rule
		bsum = 0;
		//
		for(int i = 0; i<25; i++){
			bsum = bsum + MainDriver.fibi[i];
		}
		//
		//calculate A, C
		EOSA = a * pressure / (Math.pow(MainDriver.R, 2) * Math.pow(temperature, 2));
		EOSC = bsum * pressure / (MainDriver.R * temperature);
		MainDriver.eqA = 1;
		MainDriver.eqB = -(1 - EOSC);
		MainDriver.eqC = EOSA - 3 * Math.pow(EOSC, 2) - 2 * EOSC;
		MainDriver.eqD = -(EOSA * EOSC - Math.pow(EOSC, 2) - Math.pow(EOSC, 3));
		zfactor = Solveeqn(MainDriver.eqA, MainDriver.eqB, MainDriver.eqC, MainDriver.eqD); //sub to solve 3-dimension eq.
		//Assume mass 1lb
		Vol = zfactor * MainDriver.R * temperature / pressure;
		VolChange = 0;

		Tc_C_calc[0] = Tboiling[0] * Math.pow((0.533272 + 0.191017 * Math.pow(10, -3) * Tboiling[0] + 0.779681 * Math.pow(10, -7) * Math.pow(Tboiling[0], 2) - 0.284376 * Math.pow(10, -10) * Math.pow(Tboiling[0], 3) + 0.959468 * Math.pow(10, 28) /  Math.pow(Tboiling[0], 13)),-1);
		SG_calc[0] = 0.843593 - 0.128624 * (1 - Tboiling[0] / Tc_C_calc[0]) - 3.36159 * Math.pow((1 - Tboiling[0] / Tc_C_calc[0]), 3) - 13749.5 * Math.pow((1 - Tboiling[0] / Tc_C_calc[0]), 12);
		VO[0] = 6.528 + 0.7851 * MainDriver.MW_C[0] + 0.8094 * MainDriver.MW_C[0] * (SG_calc[0] - SG_exp[0]) / SG_exp[0]; //cc/gmol
		VO[0] = VO[0] * 453.6 / Math.pow(30.48, 3); //cc/gmol->ft3/lbmol
		ShiftingFactor[0] = 1.077 - 1.644 * VO[0] / MainDriver.b[0] + (0.4878 * Math.pow(10, -4) - 0.5156 * Math.pow(10, -7) * MainDriver.MW_C[0]) * MainDriver.MW_C[0] / MainDriver.b[0];
		VolChange = VolChange + ShiftingFactor[0] * MainDriver.b[0] * MainDriver.Mfrac_C_oil[0] / 100;

		//Derive Shifting fator s
		for(int i = 7; i<25; i++){
			Tc_C_calc[i] = Tboiling[i] * Math.pow((0.533272 + 0.191017 * Math.pow(10, -3) * Tboiling[i] + 0.779681 * Math.pow(10, -7) * Math.pow(Tboiling[i], 2) - 0.284376 * Math.pow(10, -10) * Math.pow(Tboiling[i], 3) + 0.959468 * Math.pow(10, 28) /  Math.pow(Tboiling[i], 13)),-1);
			SG_calc[i] = 0.843593 - 0.128624 * (1 - Tboiling[i] / Tc_C_calc[i]) - 3.36159 * Math.pow((1 - Tboiling[i] / Tc_C_calc[i]), 3) - 13749.5 * Math.pow((1 - Tboiling[i] / Tc_C_calc[i]), 12);
			VO[i] = 6.528 + 0.7851 * MainDriver.MW_C[i] + 0.8094 * MainDriver.MW_C[i] * (SG_calc[i] - SG_exp[i]) / SG_exp[i]; //cc/gmol
			VO[i] = VO[i] * 453.6 / Math.pow(30.48, 3); //cc/gmol->ft3/lbmol
			ShiftingFactor[i] = 1.077 - 1.644 * VO[i] / MainDriver.b[i] + (0.4878 * Math.pow(10, -4) - 0.5156 * Math.pow(10, -7) * MainDriver.MW_C[i]) * MainDriver.MW_C[i] / MainDriver.b[i];
			VolChange = VolChange + ShiftingFactor[i] * MainDriver.b[i] * MainDriver.Mfrac_C_oil[i] / 100;
		}
		Vol = Vol - VolChange; //ft3/lbmole
		density = MW / Vol; // lb/ft^3
		return density * 5.6145 / 42;
	}

	static double calcPREOSdensity(double temperature, double pressure){
		double T_standard = 0;
		double EOSZ = 0;
		double EOSY = 0;
		double EOSA = 0;
		double EOSA_standard = 0;
		double EOSC = 0;
		double eqA = 0;
		double eqB = 0;
		double eqC = 0;
		double eqC_standard = 0;
		double eqD = 0;
		double eqD_standard = 0;
		double MW = 0;
		double[] Tboiling = new double[25];
		double [] VO = new double[25]; //Closest packed volume
		double a = 0;
		double a_standard = 0;
		double bsum = 0;	
		double zfactor = 0;
		double zfactor_standard = 0;
		double density = 0;
		double dummy = 0;
		double[] SG_exp= new double[25];
		double[] SG_calc= new double[25];
		double[] Tc_C_calc= new double[25];
		double[] ShiftingFactor= new double[25];
		double Vol = 0;
		double Vol_standard = 0;
		double VolChange = 0;

		MainDriver.R = 10.73; //psi-ft3/lbmole-R
		MainDriver.Mfrac_C_oil[0]=0;
		MW = calcMW();

		T_standard = 520;

		//Critical temperatures
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
		//
		//Boiling Temperature
		Tboiling[7] = 702;
		Tboiling[8] = 748;
		Tboiling[9] = 791;
		Tboiling[10] = 829;
		Tboiling[11] = 867;
		Tboiling[12] = 901;
		Tboiling[13] = 936;
		Tboiling[14] = 971;
		Tboiling[15] = 1002;
		Tboiling[16] = 1032;
		Tboiling[17] = 1055;
		Tboiling[18] = 1077;
		Tboiling[19] = 1101;
		Tboiling[20] = 1124;
		Tboiling[21] = 1146;
		Tboiling[22] = 1167;
		Tboiling[23] = 1187;
		Tboiling[24] = 1207;

		//Critical pressures
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
		//
		//Accentric factors at a critical temperature
		MainDriver.Ac_C[0] = 0.0115;
		MainDriver.Ac_C[7] = 0.332;
		MainDriver.Ac_C[8] = 0.373;
		MainDriver.Ac_C[9] = 0.411;
		MainDriver.Ac_C[10] = 0.448;
		MainDriver.Ac_C[11] = 0.484;
		MainDriver.Ac_C[12] = 0.518;
		MainDriver.Ac_C[13] = 0.551;
		MainDriver.Ac_C[14] = 0.582;
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
		//
		//Binary coefficient at a critical temperature with methane
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
		MainDriver.Bi_C[17] = 0.0530;
		MainDriver.Bi_C[18] = 0.0537;
		MainDriver.Bi_C[19] = 0.0544;
		MainDriver.Bi_C[20] = 0.0551;
		MainDriver.Bi_C[21] = 0.0558;	
		MainDriver.Bi_C[22] = 0.0565;
		MainDriver.Bi_C[23] = 0.0571;
		MainDriver.Bi_C[24] = 0.0575;

		//Specific Gravity at 60F
		SG_exp[7] = 0.749;
		SG_exp[8] = 0.768;
		SG_exp[9] = 0.782;
		SG_exp[10] = 0.793;
		SG_exp[11] = 0.804;
		SG_exp[12] = 0.815;
		SG_exp[13] = 0.826;
		SG_exp[14] = 0.836;
		SG_exp[15] = 0.843;
		SG_exp[16] = 0.851;
		SG_exp[17] = 0.856;
		SG_exp[18] = 0.861;
		SG_exp[19] = 0.866;
		SG_exp[20] = 0.871;
		SG_exp[21] = 0.876;
		SG_exp[22] = 0.881;
		SG_exp[23] = 0.885;
		SG_exp[24] = 0.888;
		//
		for(int i = 0; i<25; i++){
			if(MainDriver.Pc_C[i] == 0) MainDriver.Pc_C[i] = 1;
			if(MainDriver.Tc_C[i] == 0) MainDriver.Tc_C[i] = 1;
		}
		//
		//calculate a(Tc)
		for(int i = 0; i<25; i++){
			MainDriver.aTc[i] = 0.45724 * Math.pow(MainDriver.R, 2) * Math.pow(MainDriver.Tc_C[i], 2) / MainDriver.Pc_C[i];		        
		}
		//
		//calculate a(T)
		for(int i = 0; i<25; i++){
			MainDriver.aT[i] = MainDriver.aTc[i] * Math.pow((1 + (0.37464 + (1.54226 *MainDriver.Ac_C[i]) - (0.26992 * Math.pow(MainDriver.Ac_C[i], 2))) * (1 - Math.sqrt(temperature / MainDriver.Tc_C[i]))), 2);
		}
		//
		//calculate b
		for(int i = 0; i<25; i++){
			MainDriver.b[i] = 0.0778 * MainDriver.R * MainDriver.Tc_C[i] / MainDriver.Pc_C[i];
		}
		//
		//calculate FiBi
		for(int i = 0; i<25; i++){
			MainDriver.fibi[i] = MainDriver.Mfrac_C_oil[i] * MainDriver.b[i] / 100;
		}
		//calculate Aij
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				MainDriver.Aij[i][j] = Math.sqrt(MainDriver.aT[i] * MainDriver.aT[j]);
			}
		}
		//

		//cosidering binary coefficient with c1, recalculate Aij
		for(int i = 0; i<25; i++){
			MainDriver.Aij[0][i] = (1 - MainDriver.Bi_C[i]) * Math.sqrt(MainDriver.aT[0]) * Math.sqrt(MainDriver.aT[i]);
		}
		//
		for(int i = 0; i<25; i++){
			MainDriver.Aij[i][0] = (1 - MainDriver.Bi_C[i]) * Math.sqrt(MainDriver.aT[i]) * Math.sqrt(MainDriver.aT[0]);
		}
		//
		//calculate fifj
		dummy=0;
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				MainDriver.fifj[i][j] = MainDriver.Mfrac_C_oil[i] / 100 * MainDriver.Mfrac_C_oil[j] / 100;
				dummy=dummy+MainDriver.fifj[i][j];
			}
		}
		dummy=0;
		//
		//calculate a by mixing rule
		a = 0;
		//
		for(int i = 0; i<25; i++){
			for(int j = 0; j<25; j++){
				a = a + (MainDriver.Aij[i][j] * MainDriver.fifj[i][j]);
			}
		}
		//
		//calculate b by mixing rule
		bsum = 0;
		//
		for(int i = 0; i<25; i++){
			bsum = bsum + MainDriver.fibi[i];
		}
		//
		//calculate A, C
		EOSA = a * pressure / (Math.pow(MainDriver.R, 2) * Math.pow(temperature, 2));
		EOSC = bsum * pressure / (MainDriver.R * temperature);
		MainDriver.eqA = 1;
		MainDriver.eqB = -(1 - EOSC);
		MainDriver.eqC = EOSA - 3 * Math.pow(EOSC, 2) - 2 * EOSC;
		MainDriver.eqD = -(EOSA * EOSC - Math.pow(EOSC, 2) - Math.pow(EOSC, 3));
		zfactor = Solveeqn(MainDriver.eqA, MainDriver.eqB, MainDriver.eqC, MainDriver.eqD); //sub to solve 3-dimension eq.
		//Assume mass 1lb
		Vol = zfactor * MainDriver.R * temperature / pressure;
		VolChange = 0;

		//Derive Shifting fator s
		for(int i = 7; i<25; i++){
			Tc_C_calc[i] = Tboiling[i] * Math.pow((0.533272 + 0.191017 * Math.pow(10, -3) * Tboiling[i] + 0.779681 * Math.pow(10, -7) * Math.pow(Tboiling[i], 2) - 0.284376 * Math.pow(10, -10) * Math.pow(Tboiling[i], 3) + 0.959468 * Math.pow(10, 28) /  Math.pow(Tboiling[i], 13)),-1);
			SG_calc[i] = 0.843593 - 0.128624 * (1 - Tboiling[i] / Tc_C_calc[i]) - 3.36159 * Math.pow((1 - Tboiling[i] / Tc_C_calc[i]), 3) - 13749.5 * Math.pow((1 - Tboiling[i] / Tc_C_calc[i]), 12);
			VO[i] = 6.528 + 0.7851 * MainDriver.MW_C[i] + 0.8094 * MainDriver.MW_C[i] * (SG_calc[i] - SG_exp[i]) / SG_exp[i]; //cc/gmol
			VO[i] = VO[i] * 453.6 / Math.pow(30.48, 3); //cc/gmol->ft3/lbmol
			ShiftingFactor[i] = 1.077 - 1.644 * VO[i] / MainDriver.b[i] + (0.4878 * Math.pow(10, -4) - 0.5156 * Math.pow(10, -7) * MainDriver.MW_C[i]) * MainDriver.MW_C[i] / MainDriver.b[i];
			VolChange = VolChange + ShiftingFactor[i] * MainDriver.b[i] * MainDriver.Mfrac_C_oil[i] / 100;
		}
		Vol = Vol - VolChange; //ft3/lbmole
		density = MW / Vol; // lb/ft^3
		return density * 5.6145 / 42;
	}

	static double Solveeqn(double aaa, double bbb, double ccc, double ddd){
		// Find a right-end point for bisection
		double rightEnd = 0;
		double leftEnd = 0;
		double center = 0;
		double result = 0;
		double fl = 0;
		double fr = 0;
		int iter = 0;

		iter = 0;

		if(ddd < 0){
			leftEnd = 0;
			rightEnd = leftEnd;
			center = 0;
			fl = aaa * Math.pow(leftEnd, 3) + bbb * Math.pow(leftEnd, 2) + ccc * leftEnd + ddd;
			fr = aaa * Math.pow(rightEnd, 3) + bbb * Math.pow(rightEnd, 2) + ccc * rightEnd + ddd;
			while(result * fl >= 0 && iter < 10000){
				iter = iter + 1;
				rightEnd = rightEnd + 0.1;
				result = aaa * Math.pow(rightEnd, 3) + bbb * Math.pow(rightEnd, 2) + ccc * rightEnd + ddd;
			}
		}
		else{
			leftEnd = 20;
			rightEnd = leftEnd;
			center = 0;
			fl = aaa * Math.pow(leftEnd, 3) + bbb * Math.pow(leftEnd, 2) + ccc * leftEnd + ddd;
			fr = aaa * Math.pow(rightEnd, 3) + bbb * Math.pow(rightEnd, 2) + ccc * rightEnd + ddd;
			while(result * fl >= 0 && iter < 10000){
				iter = iter + 1;
				rightEnd = rightEnd - 0.1;
				result = aaa * Math.pow(rightEnd, 3) + bbb * Math.pow(rightEnd, 2) + ccc * rightEnd + ddd;
			}
		}

		if (iter == 10000) return 0;
		iter = 0;
		result = 1;
		while(Math.abs(result) > 0.00000001 && iter < 1000){
			fl = aaa * Math.pow(leftEnd, 3) + bbb * Math.pow(leftEnd, 2) + ccc * leftEnd + ddd;
			fr = aaa * Math.pow(rightEnd, 3) + bbb * Math.pow(rightEnd, 2) + ccc * rightEnd + ddd;
			center = (rightEnd + leftEnd) / 2;
			result = aaa * Math.pow(center, 3) + bbb * Math.pow(center, 2) + ccc * center + ddd;
			if (fr * result > 0) rightEnd = center;
			else leftEnd = center;
			iter = iter + 1;
		}
		if(iter == 1000) return 0;
		return center; // smallest real positive root
	}

	static double calcSKdensity(double temperature, double pressure){
		double densityOil=0;
		double densityWater=0;
		double deltapP=0;
		double deltapT=0;
		double mc1plus = 0;
		double densityc2plus = 0;
		double densityc3plus = 0;
		double v3cplus = 0;
		double mc3plus = 0;
		densityWater = 8.33 * 42 / 5.6145; //ppg at 39F
		// T: R, P:psig

		for(int i=0; i<25; i++){
			mc1plus = mc1plus + MainDriver.Mfrac_C[i]*MainDriver.MW_C[i] / 100;
		}
		mc1plus = MainDriver.Mfrac_C[0] / 100 *MainDriver.MW_C[0] / mc1plus;


		for(int i=7; i<25; i++){
			mc3plus = mc3plus +  MainDriver.Mfrac_C[i] / 100 *MainDriver.MW_C[i];
			v3cplus = v3cplus + MainDriver.Mfrac_C[i] / 100 *MainDriver.MW_C[i]/(MainDriver.C_density[i]*42/5.6145);
		}

		densityc3plus = mc3plus / v3cplus;
		densityc2plus= densityc3plus;
		densityOil = densityc2plus*(1-0.012*mc1plus - 0.000158*Math.pow(mc1plus,2))+0.0133*mc1plus+0.00058*Math.pow(mc1plus, 2);

		deltapP = (0.000167 + 0.016181 * Math.pow(10, (-0.0425 * densityOil))) * pressure - Math.pow(10, -8) * (0.299 + 263 * Math.pow(10, (-0.0603 * densityOil))) * Math.pow(pressure, 2); //psia
		deltapT = (temperature - 520) * (0.0133 + 152.4 * Math.pow((densityOil + deltapP), -2.45)) - Math.pow((temperature - 520), 2)* (8.1 * Math.pow(10, -6) - 0.0622 * Math.pow(10, (-0.0764 * (densityOil + deltapP)))); //R

		return (densityOil + deltapP - deltapT) * 5.6145 / 42;
	}

	static double calcSKdensity_oil(double temperature, double pressure){
		double densityOil=0;
		double densityWater=0;
		double deltapP=0;
		double deltapT=0;
		double mc1plus = 0;
		double densityc2plus = 0;
		double densityc3plus = 0;
		double v3cplus = 0;
		double mc3plus = 0;
		double MW = 0;

		MW = calcMW();
		densityWater = 8.33 * 42 / 5.6145; //ppg at 39F
		// T: R, P:psig

		for(int i=0; i<25; i++){
			mc1plus = mc1plus + MainDriver.Mfrac_C_oil[i]*MainDriver.MW_C[i] / 100;
		}
		mc1plus = MainDriver.Mfrac_C_oil[0] / 100 *MainDriver.MW_C[0] / mc1plus;


		for(int i=7; i<25; i++){
			mc3plus = mc3plus +  MainDriver.Mfrac_C_oil[i] / 100 *MainDriver.MW_C[i];
			v3cplus = v3cplus + MainDriver.Mfrac_C_oil[i] / 100 *MainDriver.MW_C[i]/(MainDriver.C_density[i]*42/5.6145);
		}

		densityc3plus = mc3plus / v3cplus;
		densityc2plus= densityc3plus;
		densityOil = densityc2plus*(1-0.012*mc1plus - 0.000158*Math.pow(mc1plus,2))+0.0133*mc1plus+0.00058*Math.pow(mc1plus, 2);

		deltapP = (0.000167 + 0.016181 * Math.pow(10, (-0.0425 * densityOil))) * pressure - Math.pow(10, -8) * (0.299 + 263 * Math.pow(10, (-0.0603 * densityOil))) * Math.pow(pressure, 2); //psia
		deltapT = (temperature - 520) * (0.0133 + 152.4 * Math.pow((densityOil + deltapP), -2.45)) - Math.pow((temperature - 520), 2)* (8.1 * Math.pow(10, -6) - 0.0622 * Math.pow(10, (-0.0764 * (densityOil + deltapP)))); //R

		return (densityOil + deltapP - deltapT) * 5.6145 / 42;
	}


	static double Cubiceqsolver(double aaa, double bbb, double ccc, double ddd, double nnn){
		//Modified by Ty
		//sub to calculate PREOS z-factor
		double realcuberoot=0;
		double f=0, df=0, aa=0, bb=0, real=0, disc=0;
		MainDriver.xold = 1;
		MainDriver.iter = 0;
		MainDriver.Error=1;
		while(MainDriver.iter<1000 && Math.abs(MainDriver.Error)>0.000001){
			MainDriver.iter = MainDriver.iter + 1;
			f = aaa * Math.pow(MainDriver.xold, 3) + bbb * Math.pow(MainDriver.xold, 2) + ccc * MainDriver.xold + ddd;
			df = 3 * aaa * Math.pow(MainDriver.xold, 2) + 2 * bbb * MainDriver.xold + ccc;
			MainDriver.EOSZ1 = MainDriver.xold - f / df;
			MainDriver.Error = MainDriver.EOSZ1 - MainDriver.xold;
			MainDriver.xold = MainDriver.EOSZ1;
		}
		//
		if(nnn == 1) realcuberoot = MainDriver.EOSZ1;
		else{
			aa = bbb / aaa;
			bb = ccc / aaa;
			real = -(aa + MainDriver.EOSZ1) / 2;
			disc = -3 * Math.pow(MainDriver.EOSZ1, 2) - 2 * aa * MainDriver.EOSZ1 + Math.pow(aa, 2) - 4 * bb;

			if(disc < -0.0000001) realcuberoot = 0;
			else{
				disc = Math.abs(disc);
				if(nnn == 2) realcuberoot = real + Math.sqrt(disc) / 2;
				else realcuberoot = real - Math.sqrt(disc) / 2;
			}
		}

		return realcuberoot;
	}

	static double calcRs2(double pp, double tt){ //sub to calc gas solubility by PVTi's solubility curve
		double calcRs_100=0, calcRs_200=0, calcRs_300=0, Rs=0;
		//
		if(MainDriver.ibaseoil == 0){ //No.2 diesel
			calcRs_100 = 3*Math.pow(10, -16) * Math.pow(pp, 5) - 5.1878*Math.pow(10, -12) * Math.pow(pp, 4) + 3.18600438E-08 * Math.pow(pp, 3) - 6.43391048265E-05 * Math.pow(pp, 2) + 0.224909450428868 * pp;
			calcRs_200 = 7*Math.pow(10, -16) * Math.pow(pp, 5) - 1.18693*Math.pow(10,-11) * Math.pow(pp, 4) + 7.15585397E-08 * Math.pow(pp, 3) - 1.549305280673*Math.pow(10,-4) * Math.pow(pp, 2) + 0.252976590501021 * pp;
			calcRs_300 = 1.6*Math.pow(10, -15) * Math.pow(pp, 5) - 2.69196*Math.pow(10,-11) * Math.pow(pp, 4) + 1.539842889*Math.pow(10,-7) * Math.pow(pp, 3) - 3.298603014912*Math.pow(10,-4) * Math.pow(pp, 2) + 0.354972548910609 * pp;
			//
			if(tt >= 300) Rs = calcRs_300;
			else if(tt >= 200) Rs = calcRs_200 + (calcRs_300 - calcRs_200) * (tt - 200) / 100;
			else if(tt >= 100) Rs = calcRs_100 + (calcRs_200 - calcRs_100) * (tt - 100) / 100;
			else Rs = calcRs_100;
		}
		//
		else if(MainDriver.ibaseoil == 1){ //Mentor 28
			//
			calcRs_100 = 0.000000003689253 * Math.pow(pp, 3) - 0.000023056958764 * Math.pow(pp, 2) + 0.204737295258884 * pp;
			calcRs_200 = 0.000000006341424 * Math.pow(pp, 3) - 0.000043760722313 * Math.pow(pp, 2) + 0.214558085464191 * pp;
			calcRs_300 = 0.000000000002229 * Math.pow(pp, 4) - 0.000000030152215 * Math.pow(pp, 3) + 0.0001496348984 * Math.pow(pp, 2) - 0.129342134722204 * pp + 99.5069444282216;
			//
			if(tt >= 300) Rs = calcRs_300;
			else if(tt >= 200) Rs = calcRs_200 + (calcRs_300 - calcRs_200) * (tt - 200) / 100;
			else if(tt >= 100) Rs = calcRs_100 + (calcRs_200 - calcRs_100) * (tt - 100) / 100;
			else Rs = calcRs_100;
		}
		//
		else if(MainDriver.ibaseoil == 2){ //Conoco LVT
			//
			calcRs_100 = 1.38706*Math.pow(10, -15) * Math.pow(pp, 5) - 1.842228773*Math.pow(10,-11) * Math.pow(pp, 4) + 8.643802658684E-08 * Math.pow(pp, 3) - 1.39248703050932*Math.pow(10,-4) * Math.pow(pp, 2) + 0.277769745662226 * pp;
			calcRs_200 = 7.6*Math.pow(10, -19) * Math.pow(pp, 6) - 1.281191E-14 * Math.pow(pp, 5) + 8.085167842*Math.pow(10,-11) * Math.pow(pp, 4) - 2.2823197188757*Math.pow(10,-4) * Math.pow(pp, 3) + 2.97409593351006*Math.pow(10,-4) * Math.pow(pp, 2) + 3.69018951241742E-02 * pp;
			calcRs_300 = 6*Math.pow(10, -18) * Math.pow(pp, 6) - 1.17079*Math.pow(10, -13) * Math.pow(pp, 5) + 8.18373191*Math.pow(10,-10) * Math.pow(pp, 4) - 2.749340009774E-06 * Math.pow(pp, 3) + 4.58141694155216E-03 * Math.pow(pp, 2) - 3.27480976332247 * pp + 855.543631282293;
			//
			if(tt >= 300) Rs = calcRs_300;
			else if(tt >= 200) Rs = calcRs_200 + (calcRs_300 - calcRs_200) * (tt - 200) / 100;
			else if(tt >= 100) Rs = calcRs_100 + (calcRs_200 - calcRs_100) * (tt - 100) / 100;
			else Rs = calcRs_100;
		}
		return Rs;
	}

	static double pxBottom(double volPump, double xLoc){//, Pbtm){//util
		//...... sub to calculate the pressure at the given position using given BHP, pb
		// this is good for on-||off-shore locations and driller//s method
		// and engineer//s method for variable geometries.
		// pbtm ; psi at the xloc, xloc;ft, volpump;total pumping kill mud volume, bbls
		//
		double  DPtotal=0, h=0, xm2vd=0, xlocvd=0, hmo=0, hmk=0, volkill=0, hmud=0, xm2=0; //xzero = 0, an unused variable
		double Pbtm=0;//결과값
		getDP(MainDriver.Qkill, xLoc, MainDriver.oMud);
		DPtotal = MainDriver.DPbot;
		h = MainDriver.TMD[MainDriver.NwcS-1]; xm2vd = MainDriver.TVD[MainDriver.NwcS-1];
		xlocvd = getVD(xLoc);
		//....................................... old mud only in the annulus
		hmo = h - xLoc; hmk = 0;
		if (volPump > MainDriver.VOLinn){
			//................................ old mud and kill mud in the annulus
			volkill = volPump - MainDriver.VOLinn;
			hmud=getBotH(volkill, h);
			hmk = hmud; hmk = Math.min(hmk, hmo);
			hmo = h - hmk - xLoc; xm2 = h - hmk;
			xm2vd=getVD(xm2);
			getDP(MainDriver.Qkill, xm2, MainDriver.oMud);
			DPtotal = DPtotal - MainDriver.DPbot;
			getDP(MainDriver.Qkill, xm2, MainDriver.Cmud);
			DPtotal = DPtotal + MainDriver.DPbot;
		}
		Pbtm = MainDriver.Pb - MainDriver.gMudOld * (xm2vd - xlocvd) - MainDriver.gMudCirc * (MainDriver.TVD[MainDriver.NwcS-1] - xm2vd) - DPtotal * MainDriver.Pcon;
		return Pbtm;
		//
	}

	static double temperature(double Xgiven){//util
		//  from function temperature (Xgiven, tsurf, hmid=dwater, tg=tgrad, tw=twgrad)
		//  calculate the temperature at the given depth in Rankin ==> Modified on 5-29-94
		double result=0, temp=0, tmp2=0;
		//Modified by TY
		double contA=0, contB=0, contC=0, contD=0, xi1=0, xi2=0;
		double w=0, rc=0, rt=0;
		int imethod=0;
		double C1=0, C2=0, C3=0, C4=0;
		double K1=0, K2=0;
		//
		rc = MainDriver.DiaHole / 12 / 2; //unit : ft
		rt = MainDriver.diDP / 12 / 2; //unit : ft
		//
		MainDriver.td2 = 10 / rc * rc;
		//
		if(MainDriver.iHeattrans == 1){ //Holmes & swift method
			w = 168.54 * 14 * 60; //w : Mass flow rate of the fluid (lbm/hr)
			contA = (w * MainDriver.SheatM) / (2 * 3.141592 * rt * MainDriver.HtrancM);
			contB = (rc * MainDriver.HtrancF) / (rt * MainDriver.HtrancM);

			C1 = (contB / 2 / contA) * (1 + Math.sqrt(1 + 4 / contB)); //Solution of 2nd-equation
			C2 = (contB / 2 / contA) * (1 - Math.sqrt(1 + 4 / contB));
			C3 = 1 + (contB / 2) * (1 + Math.sqrt(1 + 4 / contB));
			C4 = 1 + (contB / 2) * (1 - Math.sqrt(1 + 4 / contB));
			//
			K2 = (MainDriver.Tgrad / 100 * contA - (MainDriver.InjmudT - MainDriver.Tsurf + MainDriver.Tgrad / 100 * contA) * Math.exp(C1 * MainDriver.Vdepth) * (1 - C3)) / (Math.exp(C2 * MainDriver.Vdepth) * (1 - C4) - Math.exp(C1 * MainDriver.Vdepth) * (1 - C3));
			K1 = MainDriver.InjmudT - K2 - MainDriver.Tsurf + MainDriver.Tgrad / 100 * contA;
			//
			result = K1 * C3 * Math.exp(C1 * Xgiven) + K2 * C4 * Math.exp(C2 * Xgiven) + MainDriver.Tgrad / 100 * Xgiven + MainDriver.Tsurf;
			result = result + 460; //Convert to Rankine unit
			return result;
		}
		else{ //Geothermal gradient	
			if (Xgiven < MainDriver.Dwater) temp = MainDriver.tWgrad * Xgiven;
			else temp = MainDriver.tWgrad * MainDriver.Dwater + MainDriver.Tgrad * (Xgiven - MainDriver.Dwater);
			tmp2 = temp / 100 + MainDriver.Tsurf; tmp2 = Math.max(tmp2, 32);
			result = tmp2 + 460;
			return result;
		}			
	}

	static double[] slipShutin(double d2, double d1, double rhoL, double rhoG, double surfTen, double angle, double Hg, double vL){//, Vgf) //util//  0: vSlip, 1: vGf
		//--- sub to calculate the gas slip velocity during shutin based on Hasan//s correlation
		//  surften;dynes/cm  v;in-situ velocity, ft/s
		//  0.0<Hg<0.25-bubble, 0.55<Hg<0.75-slug, 0.9<Hg<1.0-annular flow
		//  Vl;average mixture velocity, i.e. Vl = Vn

		double Cmin = 1, Cmax =  1.5, anginc = 5;
		double angRad = angle * MainDriver.pai / 180;
		double costmp =  Math.abs(Math.cos(angRad));
		double atemp =  Math.sqrt(costmp) * Math.pow((1 + Math.sin(angRad)),1.2);
		double ctemp=0, temp=0, a=0, c=0, c2=0, ca=0, ca2=0, v=0, v2=0, Vgf=0;
		double vbubb=0, cbubb=0, caslu=0; //cabub=0,  an unused variable
		double vslug=0, cslug=0;
		double vSlip=0;
		double dummy=1;
		double[] result = {0,0};
		//....... determine the coefficient to calculate slip velocity of slug flow
		//        for vertical and inclined pipes||annuli
		if (angle > anginc) ctemp =  (0.345 + 0.1 * d1 / d2);
		else ctemp =  (0.3 + 0.22 * d1 / d2);
		//...................................................... for bubble flow
		vbubb = propertyModule.Harmathy(surfTen, rhoL, rhoG, Hg);
		cbubb =  (1.2 + 0.371 * d1 / d2); //cabub = cbubb; an unused statement
		//........................................................ for slug flow
		temp =  32.17 * (d2 - d1) * (rhoL - rhoG) / rhoL / 12;
		vslug =  ctemp * Math.sqrt(temp) * atemp;
		cslug =  1.15 + 0.9 * d1 / d2; 
		caslu =  1.15 + 0.7 * d1 / d2;
		//............................ calculate slip velocity each flow pattern
		if (Hg < 0.25){
			vSlip = vbubb; c = cbubb; ca = c;}
		else if (Hg < 0.55){
			c = cbubb; ca = c; v = vbubb; c2 = cslug;
			ca2 = caslu; v2 = vslug;
			a =  (Hg - 0.25) / 0.3; c = (1 - a) * c + a * c2;
			ca = (1 - a) * ca + a * ca2;
			vSlip = (1 - a) * v + a * v2;
		}
		else if (Hg < 0.75){
			vSlip = vslug; c = cslug; ca = caslu;}
		else if (Hg < 0.9){
			c = cslug; ca = caslu; v = vslug; c2 = 1; ca2 = 1;
			v2 = 0; a = (Hg - 0.75) / 0.15; c = (1 - a) * c + a * c2;
			ca = (1 - a) * ca + a * ca2;
			vSlip = (1 - a) * v + a * v2;
		}
		else{
			c = 1; ca = 1; vSlip = 0;}

		if (angle > anginc)  c = ca;
		if (angle > 87)  vSlip = 0;
		c = propertyModule.range(c, Cmin, Cmax);
		//.................... set c=1.0, later use the general approach.
		//                     this is good for Alternative WC
		c = 1;
		if(Hg < 0.00002) vSlip = 0;   //1-phase flow, 2/5/2004
		//      vgs = vG * Hg   //unused, 2/4/2004
		Vgf = c * vL + vSlip;
		if (vL < 0) Vgf =  (0.5 * (vbubb + vslug));	    	      
		//
		result[0] = vSlip;
		result[1] = Vgf;
		return result;
	}	 

	static void get2pDP(double xLoc, double ql, double Qg, double hg2p, double rhoL, double rhoG, double gasVis){ //util
		//  sub to calculate frictional pressure drop at the given point below (dpbot) or above (dptop).
		//
		double[] DPf = new double[10];
		double[] getLinesEX1, getLinesEX2 = new double[5]; 
		//c............................. calculate DP for all section of annulus
		double dpall = 0, hl2p = 1 - hg2p;
		double d1=0, d2=0, vls=0, vgs=0;

		for(int i = MainDriver.NwcS; i<9; i++){
			d2 = MainDriver.Do2p[i];
			d1 = MainDriver.Di2p[i];
			vls = propertyModule.Velocity(ql, Qg, d2, d1)[0]; //0 : liquid velocity, 1: gas velocity
			vgs = propertyModule.Velocity(ql, Qg, d2, d1)[1];
			DPf[i] = propertyModule.Beggs2(vls, vgs, d2, d1, hl2p, rhoL, rhoG, gasVis);
			dpall = dpall + DPf[i] * (MainDriver.TMD[i - 1] - MainDriver.TMD[i]);
		}
		//.............................. inside marine riser or choke/kill line
		//                           iBOP = 1 - BOP open, iBOP = 0 - BOP closed
		if (MainDriver.iOnshore == 2){
			getLinesEX1 = getLines(ql);//call getLines(ql, d2, d1, Qeff, capEff, volEff) //index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
			d1=getLinesEX1[3];
			d2=getLinesEX1[4];
			vls = getLinesEX1[0] / 2.448 / (d2 * d2 - d1 * d1);
			getLinesEX2 = getLines(Qg);//Call getLines(Qg, d2, d1, qgeff, capEff, volEff)
			d1=getLinesEX2[3];
			d2=getLinesEX2[4];		      
			vgs = getLinesEX2[0] / 2.448 / (d2 * d2 - d1 * d1);
			DPf[9] = propertyModule.Beggs2(vls, vgs, d2, d1, hl2p, rhoL, rhoG, gasVis);
			dpall = dpall + DPf[9] * (MainDriver.TMD[8] - MainDriver.TMD[9]);
		}
		//.................... calculatee frictional pressure drop in annulus
		MainDriver.DPbot = 0;
		for(int i = MainDriver.NwcS; i<MainDriver.NwcE; i++){
			if (xLoc > MainDriver.TMD[i] && xLoc < MainDriver.TMD[i - 1] + 0.01){
				MainDriver.DPbot = MainDriver.DPbot + DPf[i] * (MainDriver.TMD[i-1] - xLoc);
				break;
			}
			MainDriver.DPbot =MainDriver. DPbot + DPf[i] * (MainDriver.TMD[i-1] - MainDriver.TMD[i]);
		}
		if (xLoc > MainDriver.TMD[MainDriver.NwcS-1]) MainDriver.DPbot = 0;
		if (xLoc < MainDriver.TMD[MainDriver.NwcE-1]) MainDriver.DPbot = dpall;
		MainDriver.DPtop = dpall - MainDriver.DPbot;
	}

	static double[] calcPeff(double psurf, double qtop, double qbot, double ql, double Qg, double rho, double xLoc, double hkmd, double hg2p, double delta_rho){
		//......... sub to calculate effective kick pressure (px)  and
		// resultant bottom hole pressure (pbeff) by mid point iteration.
		// q:flow rate-gpm  X,h:MD-ft  Hg:gas fraction
		//
		//Modified by TY
		//sub for OBM case
		// 0 :px 1:pbeff 2:gasDen 
		double xVert=0, px=0, pxm2=0, xmid=0, xmvert=0, hmid=0, tmid=0, gasDen=0, gasden2=0, avgden2=0, gasVis=0, dp2ptop=0, dp2pbot=0, dp2pall=0;
		double dptmp=0, denerr=1, x2end=0, x2vert=0, Pbeff=0;
		double[] result = {0,0,0};
		double[] GasPropEX = {0,0,0};
		double[] GasPropEX2 = {0,0,0};

		getDP(qtop, xLoc, rho);
		xVert = getVD(xLoc);
		px =  0.052 * rho * xVert + MainDriver.DPtop + psurf;
		xmid = xLoc + 0.5 * hkmd;
		xmvert=getVD(xmid);
		hmid = xmvert; 
		tmid = temperature(hmid);
		GasPropEX= propertyModule.GasProp(px, tmid); // 0 :gasVis, 1: gasDen, 2: z Call GasProp(px, tmid, gasVis, gasDen, zz)
		gasVis = GasPropEX[0];
		gasDen = GasPropEX[1];
		gasden2 = gasDen; //2 means 2-phase section
		//avgden2 = rho * (1# - hg2p) + gasden2 * hg2p
		avgden2 = (rho + delta_rho) * (1 - hg2p) + gasden2 * hg2p;
		//c............................ calculate frictional drop for 2-phase
		//444   Call get2pDP(xLoc, ql, Qg, hg2p, rho + delta_rho, gasDen, gasVis)
		//Call get2pDP(xLoc, ql, Qg, hg2p, rho, gasDen, gasVis)

		while(denerr > 0.0001){
			get2pDP(xLoc, ql, Qg, hg2p,rho + delta_rho, gasDen, gasVis);
			dptmp = MainDriver.DPtop;
			get2pDP(xmid, ql, Qg, hg2p, rho + delta_rho, gasDen, gasVis);
			//Call get2pDP(xLoc, ql, Qg, hg2p, rho, gasDen, gasVis)
			dp2ptop = MainDriver.DPtop - dptmp;
			//................................ do the iterations for gas density
			pxm2 = px + 0.052 * avgden2 * (xmvert - xVert) + dp2ptop;
			//Call GasProp(pxm2, tmid, gasVis, gasden2, zz);

			GasPropEX2 = propertyModule.GasProp(pxm2, tmid);
			gasVis=GasPropEX2[0];
			gasden2=GasPropEX2[1];
			avgden2 = (rho + delta_rho) * (1 - hg2p) + gasden2 * hg2p;
			//avgden2 = rho * (1# - hg2p) + gasden2 * hg2p		  
			denerr = Math.abs(gasDen - gasden2);
			gasDen = gasden2;
		}
		// If (denerr > 0.0001) Then GoTo 444
		//
		dp2pbot = MainDriver.DPbot;
		x2end = xLoc + hkmd;
		get2pDP(x2end, ql, Qg, hg2p, rho + delta_rho, gasDen, gasVis);
		dp2pbot = dp2pbot - MainDriver.DPbot;
		dp2pall = dp2ptop + dp2pbot;
		x2vert=getVD(x2end);
		getDP(qbot, x2end, rho);
		Pbeff = px + 0.052 * avgden2 * (x2vert - xVert);
		//Pbeff = Pbeff + 0.052 * rho * (TVD(NwcS) - x2vert) + DPbot + dp2pall
		Pbeff = Pbeff + 0.052 * avgden2 * (MainDriver.TVD[MainDriver.NwcS-1] - x2vert) + MainDriver.DPbot + dp2pall;
		//
		result[0]=px;
		result[1]=Pbeff;
		result[2]=gasDen;

		return result;
	}




	static double[] calcPeff2(double psurf, double qtop, double qbot, double ql, double Qg, double rho, double xLoc, double hkmd, double hg2p){ //util
		//......... sub to calculate effective kick pressure (px)  and
		// resultant bottom hole pressure (pbeff) by mid point iteration.
		// q;flow rate-gpm  X,h;MD-ft  Hg;gas fraction
		//Modified by TY
		//sub for WBM case
		// 0 :px 1:pbeff 2:gasDen

		double xVert=0, px=0, pxm2=0, xmid=0, xmvert=0, hmid=0, tmid=0, gasDen=0, gasden2=0, avgden2=0, gasVis=0, dp2ptop=0, dp2pbot=0, dp2pall=0;
		double dptmp=0, denerr=1, x2end=0, x2vert=0, Pbeff=0;
		double[] result = {0,0,0};
		double[] GasPropEX = {0,0,0};
		double[] GasPropEX2 = {0,0,0};
		getDP(qtop, xLoc, rho);
		xVert = getVD(xLoc);
		px =  0.052 * rho * xVert + MainDriver.DPtop + psurf;
		xmid = xLoc + 0.5 * hkmd;
		xmvert=getVD(xmid);
		hmid = xmvert; 
		tmid = temperature(hmid);
		GasPropEX= propertyModule.GasProp(px, tmid); // 0 :gasVis, 1: gasDen, 2: z Call GasProp(px, tmid, gasVis, gasDen, zz)
		gasVis = GasPropEX[0];
		gasDen = GasPropEX[1];			    
		gasden2 = gasDen;
		avgden2 = rho * (1 - hg2p) + gasden2 * hg2p;
		//c............................ calculate frictional drop for 2-phase
		//444  Call get2pDP(xLoc, ql, Qg, hg2p, rho, gasDen, gasVis)(20130806 ajw 수정)
		while(denerr > 0.0001){
			get2pDP(xLoc, ql, Qg, hg2p, rho, gasDen, gasVis);
			dptmp = MainDriver.DPtop;
			get2pDP(xmid, ql, Qg, hg2p, rho, gasDen, gasVis);
			dp2ptop = MainDriver.DPtop - dptmp;
			//................................ do the iterations for gas density
			pxm2 = px + 0.052 * avgden2 * (xmvert - xVert) + dp2ptop;
			//Call GasProp(pxm2, tmid, gasVis, gasden2, zz);
			GasPropEX2 = propertyModule.GasProp(pxm2, tmid);
			gasVis=GasPropEX2[0];
			gasden2=GasPropEX2[1];

			avgden2 = rho * (1 - hg2p) + gasden2 * hg2p;
			denerr = Math.abs(gasDen - gasden2);
			gasDen = gasden2;
		}
		//if (denerr > 0.0001) Then GoTo 444}
		//
		dp2pbot = MainDriver.DPbot;
		x2end = xLoc + hkmd;
		get2pDP(x2end, ql, Qg, hg2p, rho, gasDen, gasVis);
		dp2pbot = dp2pbot - MainDriver.DPbot;
		dp2pall = dp2ptop + dp2pbot;
		x2vert=getVD(x2end);
		getDP(qbot, x2end, rho);
		Pbeff = px + 0.052 * avgden2 * (x2vert - xVert);
		Pbeff = Pbeff + 0.052 * rho * (MainDriver.TVD[MainDriver.NwcS-1] - x2vert) + MainDriver.DPbot + dp2pall;
		// pbeff구하기 ㅜㅜㅜ 
		result[0]=px;
		result[1]=Pbeff;
		result[2]=gasDen;

		return result;
		//
	}

	static double[] GetXnew(double Xold, double timeAdd, double hgX, double Qflow){ //util
		//... sub to calculate new position from the given time modified from subs CalcDT and DrawKick
		// xold; ft, time;sec, hgx; gas fraction, qflow;gpm
		//........................................ calculate gas front velocity 0: Xnew, 1: Vgf
		double Xnew=0, Vgf=0, xavg=0, xavgvd=0, tx=0, px=0, surfTen=0;
		double Htmp=0, timeSum=0, timeDown=0, d1=0, d2=0, vmix=0, Qeff=0, avgAng=0, gasDen=0;
		double[] GasPropEX = new double[3];//0 : gasVis, 1 : gasDen, 2: z
		double[] getLinesEX = new double[5]; //index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
		double[] result = new double[2];
		int dummy=0;

		int iPold=0;
		xavg = Xold;  
		xavgvd = getVD(xavg);
		tx = temperature(xavgvd); 
		px = MainDriver.Pnd[MainDriver.N2phase-1];
		surfTen = propertyModule.surfT(px, tx);
		GasPropEX = propertyModule.GasProp(px,tx);//(px, tx, gasVis, gasDen, zx)
		gasDen=GasPropEX[1];
		iPold=Xposition(Xold);
		//................... modify gas velocity to match with 2-phase model
		////      xdmin = 1000
		////      xdmin = max(xdmin, dwater)
		////      if (xdmin > 2 And xnew < xdmin) Then
		////         vgnew = 1.2 * vlnew + vsnew
		////      End if
		////      if(xold < xdmin) vgnew = vlnew + 0.5*vsnew
		//.............................. calculate MD by the given volume, bbls
		Htmp = Xold - MainDriver.TMD[iPold + 1]; timeSum = 0; timeDown = 0;
		Xnew = Xold;
		//
		for(int i = iPold; i< MainDriver.NwcE - 1; i++){
			d2 = MainDriver.Do2p[i + 1]; d1 = MainDriver.Di2p[i + 1]; vmix = Qflow / 2.448 / (d2 * d2 - d1 * d1);
			if (i==8){
				getLinesEX = getLines(Qflow);//, d2, d1, Qeff, capcty, volEff)
				Qeff=getLinesEX[0];
				d1=getLinesEX[3];
				d2=getLinesEX[4];
				vmix = Qeff / 2.448 / (d2 * d2 - d1 * d1);
			}
			avgAng =  0.5 * (MainDriver.ang2p[i] + MainDriver.ang2p[i + 1]);
			Vgf = slipShutin(d2, d1, MainDriver.oMud, gasDen, surfTen, avgAng, hgX, vmix)[1]; //vmix + vs
			timeSum = timeSum + Htmp / Vgf;
			if(timeAdd < timeSum + 0.1){
				Xnew = Xnew - (timeAdd - timeDown) * Vgf;
				result[0] = Xnew;
				result[1] = Vgf;
				return result;
			}
			timeDown = timeSum; Xnew = MainDriver.TMD[i + 1]; Htmp = MainDriver.TMD[i + 1] - MainDriver.TMD[i + 2];
		}
		Xnew = -(timeAdd - timeDown) * Vgf;   // Kick Top is out of Hole !
		//
		result[0]=Xnew;			
		result[1]=Vgf;
		return result;
	}


	/*  //there are max, min functions in Java

	Function Max(dum1, dum2)
	   Max = dum1
	   if (dum2 > dum1) Then Max = dum2
	End Function

	Function Min(dum1, dum2)
	   Min = dum1
	   if (dum2 < dum1) Then Min = dum2
	End Function
	 */


	static void Bkup(){
		//............  Assign the old value for slip volume calculation
		for(int i = 0;  i<MainDriver.N2phase; i++) {MainDriver.HgndOld[i] = MainDriver.Hgnd[i];}
	}


	static double calcDt(double Xnew, double Xold, double hgX){
		//...  sub to calculate time to move specified length
		//      TimeInt;sec, Delx;ft
		//
		int iPold=0, iPnew=0;
		//........................................ calculate gas front velocity
		double timeint = 0, xavg=0, xavgvd=0, tx=0, px2=0, surfTen=0, gasDen=0,  d1old = 0, d2old=0;//  gasVis=0,zx=0, vsold=0,an unused variable
		double d1=0, d2=0, Qeff=0,  vlold=0, vlnew=0; //cap2=0,vol2=0,  unused
		double avgold=0, avgnew=0, hgslip=0,  vgold=0, vsnew = 0, VgNew=0, xdmin=0, xmove2=0, xrest=0;
		double[] GasPropEX = new double[3]; // 0 :gasVis, 1: gasDen, 2: z
		double[] getLinesEX = new double[5];
		double[] slipEX, slipEX2 = new double[2];
		if (Xnew >= Xold) return timeint;
		//
		xavg =  0.5 * (Xold + Xnew);
		xavgvd = getVD(xavg);
		tx = temperature(xavgvd); 
		px2 = MainDriver.Pnd[MainDriver.N2phase-1];
		surfTen = propertyModule.surfT(px2, tx);
		GasPropEX = propertyModule.GasProp(px2, tx);//, gasVis, gasDen, zx)
		// gasVis = GasPropEX[0];
		gasDen = GasPropEX[1];
		//zx = GasPropEX[2];
		iPold = Xposition(Xold);
		iPnew = Xposition(Xnew);
		d2old = MainDriver.Do2p[iPold + 1]; 
		d1old = MainDriver.Di2p[iPold + 1];
		d2 = MainDriver.Do2p[iPnew + 1];  
		d1 = MainDriver.Di2p[iPnew + 1];
		Qeff = MainDriver.QtMix;
		if (Xnew < MainDriver.Dwater){
			getLinesEX = getLines(MainDriver.QtMix);
			Qeff=getLinesEX[0];
			//cap2=getLinesEX[1];
			//vol2=getLinesEX[2];
			d1=getLinesEX[3];
			d2=getLinesEX[4];
		}
		//Then Call getLines(QtMix, d2, d1, Qeff, Cap2, vol2)// 20130802 ajw ; index 0=> Qeff, index 1=> capEff, index 2 => volEff,  index 3=> d1, index 4 =>d2
		vlold =  MainDriver.QtMix / 2.448 / (Math.pow(d2old,2) - Math.pow(d1old, 2));
		vlnew =  Qeff / 2.448 / (d2 * d2 - d1 * d1);
		avgold = 0.5 * (MainDriver.ang2p[iPold] + MainDriver.ang2p[iPold + 1]);
		avgnew = 0.5 * (MainDriver.ang2p[iPnew] + MainDriver.ang2p[iPnew + 1]);
		hgslip =  Math.min(hgX, 0.45);
		slipEX = slipShutin(d2old, d1old, MainDriver.oMud, gasDen, surfTen, avgold, hgslip, vlold);//, xNull, vsold, vgold);
		// vsold = slipEX[0];
		vgold = slipEX[1];//  0: vSlip, 1: vGf
		slipEX2 = slipShutin(d2, d1, MainDriver.oMud, gasDen, surfTen, avgnew, hgslip, vlnew);//, xNull, vsnew, VgNew);
		vsnew = slipEX2[0];
		VgNew = slipEX2[1];
		//................... modify gas velocity to match with 2-phase model
		xdmin = 1000; 
		xdmin = Math.max(xdmin, MainDriver.Dwater);
		if (xdmin > 2 && Xnew < xdmin) VgNew =  1.2 * vlnew + vsnew;
		//............................................... calculate gDelT, sec
		xmove2 = Xold - Xnew;
		if (iPold == iPnew) timeint = xmove2 / VgNew;
		else{
			xrest = Xold - MainDriver.TMD[iPold + 1];
			timeint = xrest / vgold + (xmove2 - xrest) / VgNew;
		}
		if(timeint <= 0) timeint = 0;

		return timeint;
		//
	}

	static double calcCHKOpen(double qtflow, double hgX, double rhoL, double rhoG, double dpchoke){
		int iterChk=0;
		double CHKpcent = 100, cd2 = 0.6 * 0.6, xx=0, xx2=0, dchk=0, dchk2=0, dchkerr=1, kcpcv=0,  rhom=0, yy=0;//pup=0, unused

		if (dpchoke < 14.7) return CHKpcent;	   
		if (hgX < 0.0001){  //single-phase flow
			dpchoke = dpchoke - 14.7; // added by jaewoo 20140217
			xx =  0.00008311 * rhoL * qtflow * qtflow / dpchoke / cd2;
			xx2 =  Math.sqrt(xx); 
			dchk =  Math.sqrt(xx2 * 4 / MainDriver.pai);
		}
		else{
			dpchoke = dpchoke - 14.7;  // added by jaewoo 20140217
			kcpcv =  1.4; //pup = dpchoke;
			rhom = rhoG * hgX + rhoL * (1 - hgX);
			dchk2 =  0.5 * MainDriver.DchkControl;
			iterChk = 0;
			while(dchkerr > 0.0005 && iterChk <= 100){
				iterChk = iterChk + 1;			//2345
				yy =  1 - (0.41 + 0.35 * Math.pow((dchk2 / MainDriver.DchkControl), 4)) / kcpcv;
				if (yy < 0.5)  yy =  0.5;   //DP is too big for small yy
				xx =  0.00008311 * rhom * qtflow * qtflow / (dpchoke * cd2 * Math.pow(yy, 2));
				xx2 =  Math.sqrt(xx);
				dchk =  Math.sqrt(xx2 * 4 / MainDriver.pai);
				dchkerr = Math.abs(dchk2 - dchk) / MainDriver.DchkControl;
				dchk2 = dchk;
			}
			//if (dchkerr > 0.0005 && iterChk <= 100) Then GoTo 2345
		}
		////    chkpcent = 100 * (dchk / DchkControl) ^ 2  // % by Area
		CHKpcent = 100 * (dchk*dchk / (MainDriver.DchkControl*MainDriver.DchkControl));        // % by Area ratio
		CHKpcent = propertyModule.range(CHKpcent, 0, 100);

		return CHKpcent;
	}
}