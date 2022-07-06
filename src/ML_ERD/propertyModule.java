package ML_ERD;

import javax.swing.JOptionPane;

class propertyModule {
	//============================================================ AFLDfor.bas
	//  This is the complete version for the calculation of
	//  The subroutines related to the bubble rise velocities
	//  based on the published papers
	//
	//   F - Beggs
	//   F - Beggs2 - with Hl input
	//   F - ditpl
	//   F - ditpldes
	//   F - DPbit
	//   F - dpdl1p
	//   S - FractGrad(pcasing,pbhp)
	//   F - eaton / prentice
	//   S - gasprop
	//   S - getf
	//   S - getLL2
	//   F - harmathy
	//   S - Hasan
	//   S - hasanang
	//   S - holdup
	//   F - pren (No. Rey. for power-law)
	//   S - range
	//   S - surfEqData()    //moved here on 7/29/2003
	//   F - surft
	//   F - taylor
	//   S - velocity
	//   F - zfact
	//                         Programmed by Dr. Jonggeun Choe, Seoul National University

	static blowout m3;
	
	public static double calcROP(double RPM, double BHP, double WOB, double Q, double vdBit){
		// Calculate ROP
		// 150125 AJW
		double ROP_calc = 0;
		double[] PoreFrac = new double[2];
		double REN = 0, REN_base=0;
		double[] a = new double[6];
		double[] b = new double[6];
		double[] x = new double[6];
		
		calcBaseProp(vdBit);
		PoreFrac = propertyModule.calcPoreFrac(vdBit);
		MainDriver.PoreP_now = PoreFrac[0];
		MainDriver.FracP_now = PoreFrac[1];
		REN_base = reynolds_fun(MainDriver.Q_base);
		REN = reynolds_fun(Q);
		MainDriver.Torque_now = calcTorque(WOB, vdBit);
		
		a[0] = 0.0680; a[1] = -9.4961*Math.pow(10, -5); a[2] = 0.2886; a[3] = 0.5983; a[4] = -0.0503; a[5]=0.3509;
		b[0] = -0.4173; b[1] = 2.0794; b[2] = -0.0269; b[3] = 0.5416; b[4] = 2.3217;
		x[0] = Math.pow(WOB/MainDriver.WOB_Base, a[0]); x[1] = Math.exp(a[1]*((BHP-MainDriver.PoreP_now)-(MainDriver.BHP_Base-MainDriver.PoreP_Base))); x[2]=Math.pow(RPM/MainDriver.RPM_Base,a[2]);
		x[3] = Math.pow(MainDriver.Torque_now / MainDriver.Torque_Base, a[3]); x[4] = Math.pow((MainDriver.FracP_now / MainDriver.FracP_Base),a[4]); x[5] = Math.pow((REN / REN_base),a[5]);
				
		ROP_calc = MainDriver.ROP_Base;
		for(int i=0; i<=5; i++){
			ROP_calc = ROP_calc*x[i];
			}
		
		//ROP_calc = MainDriver.ROP_Base*Math.exp(-(BHP-Pore)/(MainDriver.BHP_Base-MainDriver.PoreP_Base))*Math.exp(Torq/MainDriver.Torque_Base)*Math.exp(RPM/MainDriver.RPM_Base)*Math.exp(WOB/MainDriver.WOB_Base);
		//ROP_calc = ROP_calc * Math.exp(-fP/MainDriver.FracP_Base);
		return ROP_calc;
	}
	
	static void calcBaseProp(double vd){
		for(int i=0; i<MainDriver.numLayer; i++){
			if(MainDriver.layerVDfrom[i]<=vd && vd<=MainDriver.layerVDto[i]){
				MainDriver.ROP_Base = MainDriver.layerROP[i]; // base for sandstone
				//MainDriver.RPM_Base = (int)MainDriver.layerRPM[i];
				//MainDriver.WOB_Base = MainDriver.layerWOB[i];
				MainDriver.RPM_Base = 50;
				MainDriver.WOB_Base = 50;
				MainDriver.PoreP_Base = MainDriver.layerPoreP[i];
				MainDriver.FracP_Base = MainDriver.layerFracP[i];
				if(MainDriver.iProblem[3]==1) MainDriver.BHP_Base = 0.052*MainDriver.oMud_save*(MainDriver.layerVDfrom[i]+MainDriver.layerVDto[i])/2+14.7;
				else MainDriver.BHP_Base = 0.052*MainDriver.oMud*(MainDriver.layerVDfrom[i]+MainDriver.layerVDto[i])/2+14.7;
				MainDriver.CF_Base = calcCF(MainDriver.WOB_Base, (MainDriver.layerVDfrom[i]+MainDriver.layerVDto[i])/2); // base for sandstone
				break;
			}
		}		
	}
	
	static double calcTorque(double WOB, double vd){
		double result = 0;
		MainDriver.CF_now = calcCF(WOB, vd);
		result = MainDriver.Torque_Base*MainDriver.CF_now/MainDriver.CF_Base;
		return result;
	}
	
	static double calcCF(double WOB, double vd){
		double result=0;
		double A_bit = Math.PI/4*Math.pow(MainDriver.doDC, 2);
		double S_cohesion = 0;
		double fricangle = 0;
		double coeff_fric = 0;
		//
		
		for(int i=0; i<MainDriver.numLayer; i++){
			if(MainDriver.layerVDfrom[i]<=vd && vd<=MainDriver.layerVDto[i]){
				MainDriver.iStone = MainDriver.layerRock[i];
				break;
			}
		}
		if(MainDriver.iStone==0){ //  Bartlesville Sandstone,
			S_cohesion = 8; //MPA
			fricangle = 37.2;
		}
		else if(MainDriver.iStone==1){ // Muddy shale
			S_cohesion = 38.4; //MPA
			fricangle = 14.4;
		}
		else if(MainDriver.iStone==2){ //  Indiana limestone,
			S_cohesion = 6.72; //MPA
			fricangle = 42;
		}
		
		S_cohesion = S_cohesion*Math.pow(10, 6)/101325*14.7 * A_bit;
		coeff_fric = Math.tan(fricangle*Math.PI/180);
		
		result = coeff_fric*WOB+S_cohesion;
		return result;
	}
	
	static double[] calc_Area_Cut(int ii, double angle){
		double OR = 0, IR = 0;
		double A_tot = 0, A_cut = 0, A_cri = 0, A_cri2=0, A_cri3=0, h_cutting = 0;
		double err = 1, a1 = 0, a2 = 0, a_mid = 0, a_out = 0, a_in=0, aP = 0, temp = 0, dis_critical = 0;
		int iter = 0;
		double[] result = new double[5]; // 0: area, 1: local fraction of cutting, 2: OUTER ANGLE, 3: INNER ANGLE. 4: h_cutting
		
		IR = MainDriver.Di2p[ii]/2/12;
		OR = MainDriver.Do2p[ii]/2/12;
		
		A_tot = Math.PI*(OR*OR-IR*IR); // in^2		
		
		// ANNULUS 하단에 모두 축적되 있는 경우
		aP = Math.acos(IR/OR)*2;
		A_cri = OR*OR/2*aP - OR*OR/2*Math.sin(aP);
		A_cri2 = A_tot/2;
		A_cri3 = A_tot - A_cri;
		A_cut = MainDriver.V_cutting[ii]*5.6145/(MainDriver.TMD_cutting[ii-1]-MainDriver.TMD_cutting[ii]); // ft^2
		
		// PIPE 아래에 존재하는 경우: Assume that there is no inner pipe.
		a1 = 0; a2 = Math.PI;
		err = 1; iter = 0;
		if(A_cut==0){
			result[0] = 0;
			result[1] = 0;
			result[2] = 0;
			result[3] = 0;
			result[4] = 0;

			return result;
		}
		
		if(Math.abs(angle)<0.001){
			result[0] = A_tot;
			result[1] = MainDriver.f_cutting[ii];
			result[2] = 2*Math.PI;
			result[3] = 2*Math.PI;
			result[4] = 0;

			return result;
		}
		
		if(A_cut<=A_cri){		
			while(Math.abs(err)>0.001 && iter<100){
				iter++;
				a_mid = (a1+a2)/2;
				err = (a_mid - Math.sin(a_mid)) - A_cut*2/(OR*OR);
				if(err>0) a2 = a_mid;
				else a1 = a_mid;
			}	
			a_out = a_mid;
			a_in = 0;
			h_cutting = OR - OR*Math.cos(a_out/2);
		}
		// ANNULUS 넓이 반을 넘어가기 직전
		else if(A_cut<=A_cri2){
			while(Math.abs(err)>0.001 && iter<100){
				iter++;
				a_mid = (a1+a2)/2;
				a_out = a_mid;
				a_in = 2*Math.acos(OR/IR*Math.cos(a_out/2));
				err = (1.0/2.0*OR*OR*(a_out-Math.sin(a_out)) - 1.0/2.0*IR*IR*(a_in-Math.sin(a_in)))-A_cut;
				if(err>0) a2 = a_mid;
				else a1 = a_mid;
			}
			h_cutting = OR - OR*Math.cos(a_out/2);
		}
		else if(A_cut<=A_cri3){
			while(Math.abs(err)>0.001 && iter<100){
				iter++;
				a_mid = (a1+a2)/2;
				a_out = a_mid;
				a_in = 2*Math.acos(OR/IR*Math.cos(a_out/2));
				err = (A_tot - 1.0/2.0*OR*OR*(a_out-Math.sin(a_out)) + 1.0/2.0*IR*IR*(a_in-Math.sin(a_in)))-A_cut;
				if(err<0) a2 = a_mid;
				else a1 = a_mid;
			}
			a_out = 2*Math.PI - a_out;
			a_in = 2*Math.PI - a_in;
			h_cutting = OR + OR*Math.cos(Math.PI-a_out/2);
		}
		else if(A_cut<A_tot){
			while(Math.abs(err)>0.001 && iter<100){
				iter++;
				a_mid = (a1+a2)/2;
				err = (a_mid - Math.sin(a_mid)) - (A_tot-A_cut)*2/(OR*OR);
				if(err>0) a2 = a_mid;
				else a1 = a_mid;
			}	
			a_out = 2*Math.PI - a_mid;
			a_in = 2*Math.PI;
			h_cutting = OR + OR*Math.cos(Math.PI-a_out/2);
		}
		else{
			A_cut = A_tot;
			a_out = 2*Math.PI;
			a_in = 2*Math.PI;
			h_cutting = 2*OR;
		}
		dis_critical = 2*OR - h_cutting;
		// Annulus 하단에 모두 축적이 되 있는 경우

		// 1) Cutting이 inner pipe 하단에 모두 축적
		
		if(MainDriver.dis_cut_from_top[ii]>=dis_critical){
			result[0] = A_cut;
			result[1] = MainDriver.f_cutting[ii]*A_tot/A_cut;
			result[2] = a_out;
			result[3] = a_in;
			result[4] = h_cutting;

			return result;
		}
		
		// 2) 아닌 경우
		else{
			a1 = 0; a2 = Math.PI;
			err = 1; iter = 0;
			h_cutting = 2*OR - MainDriver.dis_cut_from_top[ii];
			if(MainDriver.dis_cut_from_top[ii]<=OR-IR){
				a_out = 2*Math.PI - Math.acos((OR-MainDriver.dis_cut_from_top[ii])/OR)*2;
				a_in = 2*Math.PI;
				A_cut = 1.0/2.0*OR*OR*((2*Math.PI - a_out)-Math.sin(2*Math.PI - a_out));
				A_cut = A_tot - A_cut;
			}
			else if(MainDriver.dis_cut_from_top[ii]<=OR){
				a_out = 2*Math.PI - Math.acos((OR-MainDriver.dis_cut_from_top[ii])/OR)*2;
				a_in = 2*Math.PI - Math.acos((OR-MainDriver.dis_cut_from_top[ii])/IR)*2;
				A_cut = 1.0/2.0*OR*OR*((2*Math.PI - a_out)-Math.sin(2*Math.PI - a_out))-1.0/2.0*IR*IR*((2*Math.PI - a_in)-Math.sin(2*Math.PI - a_in));
				A_cut = A_tot - A_cut;
			}
			else if(MainDriver.dis_cut_from_top[ii]<=OR+IR){
				a_out = Math.acos((OR-h_cutting)/OR)*2;
				a_in = Math.acos((OR-h_cutting)/IR)*2;
				A_cut = 1.0/2.0*OR*OR*(a_out-Math.sin(a_out))-1.0/2.0*IR*IR*(a_in-Math.sin(a_in));
			}
			else if(MainDriver.dis_cut_from_top[ii]<=2*OR){
				a_out = Math.acos((OR-h_cutting)/OR)*2;
				a_in = 0;
				A_cut = 1.0/2.0*OR*OR*(a_out-Math.sin(a_out)); //ft2
			}
			else{
				h_cutting = 0;
				a_out = 0;
				a_in = 0;
				A_cut = 0; //ft2
			}
			
			result[0] = A_cut;
			result[1] = MainDriver.f_cutting[ii]*A_tot/A_cut;
			result[2] = a_out;
			result[3] = a_in;
			result[4] = h_cutting;

			return result;
		}
	}
	
	static double calc_Area_intv(int ii, double rr_start, double dr, double h_cutting){
		double OR = 0, OR2=0, IR2=0, h_cutting2=0;
		double A_tot2=0, A_cut = 0;
		double a_out = 0, a_in=0;
		
		OR = MainDriver.Do2p[ii]/2/12;
		IR2 = rr_start;
		OR2 = rr_start + dr;
		h_cutting2 = h_cutting-(OR-OR2);
		if(h_cutting2>2*OR2) h_cutting2 = 2*OR2;
		A_tot2 = Math.PI*(OR2*OR2-IR2*IR2); //ft2
		if(h_cutting2<=0) return 0;
				
		if(2*OR2-h_cutting2<=OR2-IR2){
			a_out = 2*Math.PI - Math.acos((OR2-(2*OR2-h_cutting2))/OR2)*2;
			a_in = 2*Math.PI;
			A_cut = 1.0/2.0*OR2*OR2*((2*Math.PI - a_out)-Math.sin(2*Math.PI - a_out));
			A_cut = A_tot2 - A_cut;
		}
		else if(2*OR2-h_cutting2<=OR2){
			a_out = 2*Math.PI - Math.acos((OR2-(2*OR2-h_cutting2))/OR2)*2;
			a_in = 2*Math.PI - Math.acos((OR2-(2*OR2-h_cutting2))/IR2)*2;
			A_cut = 1.0/2.0*OR2*OR2*((2*Math.PI - a_out)-Math.sin(2*Math.PI - a_out))-1.0/2.0*IR2*IR2*((2*Math.PI - a_in)-Math.sin(2*Math.PI - a_in));
			A_cut = A_tot2 - A_cut;
		}
		else if(2*OR2-h_cutting2<=OR2+IR2){
			a_out = Math.acos((OR2-h_cutting2)/OR2)*2;
			a_in = Math.acos((OR2-h_cutting2)/IR2)*2;
			A_cut = 1.0/2.0*OR2*OR2*(a_out-Math.sin(a_out))-1.0/2.0*IR2*IR2*(a_in-Math.sin(a_in));
		}
		else if(2*OR2-h_cutting2<=2*OR2){
			a_out = Math.acos((OR2-h_cutting2)/OR2)*2;
			a_in = 0;
			A_cut = 1.0/2.0*OR2*OR2*(a_out-Math.sin(a_out)); //ft2
		}
		
		return A_cut;
	}
	
	static double calc_V_local(int ii, double vel_a, double rr){
		double dPfdL = 0;
		double nflow = 0, kflow = 0;
		double v_local = 0;
		double VISp = 0, ss=0, rp=0;
		double h = (MainDriver.Do2p[ii] - MainDriver.Di2p[ii])/2; //inch
		double y = MainDriver.Do2p[ii]/2 - rr*12;
		
		if(MainDriver.Model==1 || MainDriver.Model==0){//POWER LAW & APIRP 13D
			if(MainDriver.iHuschel!=0){nflow = 0.58; kflow = 548;}
			else{nflow = MainDriver.pN; kflow = MainDriver.pK;}
			dPfdL = kflow*Math.pow(vel_a, nflow)/144000/Math.pow(MainDriver.Do2p[ii]-MainDriver.Di2p[ii], 1+nflow)*Math.pow(((2+1/nflow)/0.0208), nflow);
			dPfdL = dPfdL /14.7*101325/30.48*1000;//mPa/cm
			v_local = 1/(1+1/nflow)*Math.pow(dPfdL/kflow, 1/nflow);			
			v_local = (Math.pow(h/2/12*30.48, 1+1/nflow)-Math.pow(Math.abs(h/2/12*30.48-y/12*30.48), 1+1/nflow))*v_local;
			v_local = v_local/30.48;//annular
			/*dPfdL = kflow*Math.pow(vel_a, nflow)/144000/Math.pow(MainDriver.Do2p[ii], 1+nflow)*Math.pow(((3+1/nflow)/0.0416), nflow);
			dPfdL = dPfdL /14.7*101325/30.48*1000;//mPa/cm
			v_local = 1/(1+1/nflow)*Math.pow(dPfdL/2/kflow, 1/nflow);			
			v_local = (Math.pow(MainDriver.Do2p[ii]/2/12*30.48, 1+1/nflow)-Math.pow(Math.abs(rr*12*2/2/12*30.48), 1+1/nflow))*v_local;
			v_local = v_local/30.48;*/ //pipe
		}
		
		else if(MainDriver.Model == 2){//BINGHAM PLASTIC
		    VISp = MainDriver.S600 - MainDriver.S300;
		    ss = MainDriver.S300 - VISp;
		    dPfdL = (VISp*vel_a/1000/Math.pow(MainDriver.Do2p[ii]-MainDriver.Di2p[ii], 2))+(ss/200/(MainDriver.Do2p[ii]-MainDriver.Di2p[ii])); //psi/ft
		    rp = h/2-ss/(1200*dPfdL);
		    
		    if(y>=rp && y<=h-rp)  v_local = (-40*ss*rp-47867*dPfdL*(rp*rp-h*rp)/2)/VISp;
		    else if(y<rp) v_local = (-40*ss*y-47867*dPfdL*(y*y-h*y)/2)/VISp;
		    else v_local = (-40*ss*(h-y)-47867*dPfdL*((h-y)*(h-y)-h*(h-y))/2)/VISp;
			}
		
		else if(MainDriver.Model == 3){//NEWTONIAN
			dPfdL = MainDriver.visL*vel_a/1000/Math.pow(MainDriver.Do2p[ii]-MainDriver.Di2p[ii], 1+nflow);
			v_local = dPfdL/2/MainDriver.visL*(h*y-y)*23900; //23900: conversion factor
		}
		return v_local;
	}
	
	static double calc_V_average(int ii, double vel_a, double angle){ // 150818
		double result;
		int n_intv = MainDriver.n_area_intv;
		double h_cutting=0;
		double OR = MainDriver.Do2p[ii]/12/2, IR = MainDriver.Di2p[ii]/12/2, dr = (OR-IR)/n_intv;
		double[] rr_mid = new double[n_intv];
		double[] v_mid = new double[n_intv];
		double[] A_mid = new double[n_intv];
		double[] A_border = new double[n_intv-1];
		double[] temp = new double[5];
		double temp_top_cutting = 0;
		double d_move = 0;
		int i_move = 0;
		double Atot = 0;
		double AV = 0;
		double[] Q_in = new double[n_intv];
		double[] Q_out = new double[n_intv];
		double Q_in_tot = 0, Q_out_tot = 0, V_tot=0, f_check=0;
		double vel_result = 0;
		
		Atot = Math.PI*(OR*OR-IR*IR); // FT^2
		rr_mid[0] = IR+dr/2;		
		
		V_tot = 0;
		for(int i=0; i<MainDriver.n_area_intv; i++){
			V_tot = V_tot + MainDriver.V_cutting_area_intv[ii][i];
		}
		
		if(V_tot==0 && MainDriver.V_cutting[ii]!=0){
			V_tot = MainDriver.V_cutting[ii];
			for(int i=0; i<MainDriver.n_area_intv; i++){
				MainDriver.V_cutting_area_intv[ii][i]=MainDriver.V_cutting[ii]*MainDriver.f_cutting_area_intv[ii][i];
			}
		}
		
		if(MainDriver.f_cutting_area_intv[ii][MainDriver.n_area_intv-1]==0){
			for(int i=0; i<MainDriver.n_area_intv; i++){
				v_mid[i] = (calc_V_local(ii, vel_a, rr_mid[i]-dr/2)+calc_V_local(ii, vel_a, rr_mid[i]+dr/2))/2; //ft/s
				A_mid[i] = Math.PI*((rr_mid[i]+dr/2)*(rr_mid[i]+dr/2)-(rr_mid[i]-dr/2)*(rr_mid[i]-dr/2));
				if(i<MainDriver.n_area_intv-1) rr_mid[i+1] = rr_mid[i] + dr;
				AV = AV+v_mid[i]*A_mid[i];
			}
			for(int i=0; i<MainDriver.n_area_intv; i++){		
				if(AV!=0) MainDriver.f_cutting_area_intv_ini[ii][i]=v_mid[i]*A_mid[i]/AV;
				else MainDriver.f_cutting_area_intv_ini[ii][i]=0;
				MainDriver.f_cutting_area_intv[ii][i]=MainDriver.f_cutting_area_intv_ini[ii][i];
				MainDriver.V_cutting_area_intv[ii][i]=MainDriver.V_cutting[ii]*MainDriver.f_cutting_area_intv[ii][i];
				f_check = f_check + MainDriver.f_cutting_area_intv[ii][i];
			}
			return vel_a;
		}
		else if(Math.abs(angle)<0.001){		//수직구간	
			for(int i=0; i<MainDriver.n_area_intv; i++){
				MainDriver.f_cutting_area_intv[ii][i]=MainDriver.f_cutting_area_intv_ini[ii][i];
				f_check = f_check + MainDriver.f_cutting_area_intv[ii][i];
			}
			return vel_a;
		}
		else{
			d_move = (MainDriver.vfall_cutting[ii]*MainDriver.gDelT);//얼마만큼 이동하는지..
			i_move = (int)(d_move/dr);
			if(i_move>MainDriver.n_area_intv){
				i_move = MainDriver.n_area_intv-1;
				d_move = (MainDriver.n_area_intv-1)*dr;
			}
			
			V_tot = 0;
			for(int i=0; i<MainDriver.n_area_intv; i++){
				v_mid[i] = (calc_V_local(ii, vel_a, rr_mid[i]-dr/2)+calc_V_local(ii, vel_a, rr_mid[i]+dr/2))/2; //ft/s
				A_mid[i] = Math.PI*((rr_mid[i]+dr/2)*(rr_mid[i]+dr/2)-(rr_mid[i]-dr/2)*(rr_mid[i]-dr/2));
				if(i<MainDriver.n_area_intv-1) rr_mid[i+1] = rr_mid[i] + dr;
				for(int j=0; j<MainDriver.n_area_intv; j++){
					if(i==j-i_move){
						if(i!=0){
							Q_in[i] = Q_in[i] + MainDriver.V_cutting_area_intv[ii][j]*(1-(d_move-i_move*dr)/dr)/2; //위로부터 얻는 Q
							Q_in[i-1] = Q_in[i-1] + MainDriver.V_cutting_area_intv[ii][j]*(d_move-i_move*dr)/dr/2;
						}
						else Q_in[i] = Q_in[i] + MainDriver.V_cutting_area_intv[ii][j]/2;
						Q_out[j] = Q_out[j] + MainDriver.V_cutting_area_intv[ii][j]/2;
					}			
					else if(i==j+i_move){
						if(i!=MainDriver.n_area_intv-1){
							Q_in[i] = Q_in[i] + MainDriver.V_cutting_area_intv[ii][j]*(1-(d_move-i_move*dr)/dr)/2; //위로부터 얻는 Q
							Q_in[i+1] = Q_in[i+1] + MainDriver.V_cutting_area_intv[ii][j]*(d_move-i_move*dr)/dr/2;
						}
						else Q_in[i] = Q_in[i] + MainDriver.V_cutting_area_intv[ii][j]/2;
						Q_out[j] = Q_out[j] + MainDriver.V_cutting_area_intv[ii][j]/2; //아래로부터 얻는 Q
					}
					else if(j-i_move<0 && j!=0 && i==0){
						Q_in[0] = Q_in[0] + MainDriver.V_cutting_area_intv[ii][j]/2;
						Q_out[j] = Q_out[j] + MainDriver.V_cutting_area_intv[ii][j]/2;
					}
					else if(j+i_move>MainDriver.n_area_intv-1 && j!=MainDriver.n_area_intv-1 && i==MainDriver.n_area_intv-1){
						Q_in[MainDriver.n_area_intv-1] = Q_in[MainDriver.n_area_intv-1] + MainDriver.V_cutting_area_intv[ii][j]/2;
						Q_out[j] = Q_out[j] + MainDriver.V_cutting_area_intv[ii][j]/2; //아래로부터 얻는 Q
					}
				}
				V_tot = V_tot + MainDriver.V_cutting_area_intv[ii][i];
			}
			
			if(V_tot!=0){
				for(int i=MainDriver.n_area_intv-1; i>=0; i--){
					MainDriver.V_cutting_area_intv[ii][i] = MainDriver.V_cutting_area_intv[ii][i] + Q_in[i] - Q_out[i];
					MainDriver.f_cutting_area_intv[ii][i] = MainDriver.V_cutting_area_intv[ii][i]/V_tot;
					vel_result = vel_result + v_mid[i]*MainDriver.f_cutting_area_intv[ii][i];
					Q_in_tot = Q_in_tot + Q_in[i];
					Q_out_tot = Q_out_tot + Q_out[i];
					f_check = f_check + MainDriver.f_cutting_area_intv[ii][i];
				}
				Q_in_tot = Q_in_tot - Q_out_tot;
			}
			else if(MainDriver.V_cutting[ii]==0) vel_result = vel_a;			
			return vel_result;
		}
		
	}
	
	static double[] Calc_vslip_cut(double vel_a_old, double d1, double d2, int iCutting, double angle){ //150818
		double vslip = 0; double vslip_prior = 0; double err=1;
		double re = 0; int iter=0;		
		double ff = 0;
		double vis=0;
		double[] result = new double[6];
		double calc_method = 1; // 0: 기존, 1: Naganawa
		double nflow = 0.58;
		double kflow = 548;
		double F_mdvdt_mud = 0, F_gravity_mud = 0, F_fric_vslip_mud = 0, F_fric_casing_mud = 0, F_diff_mud=0;
		double F_mdvdt = 0, F_gravity = 0, F_fric_vslip = 0, F_fric_casing = 0, F_diff=0;
		double CF_sliding = 0.2;
		double m_cutting = 0, n_cutting = 0 ;
		double angle_h = 0, angle_p = 0, dH = 0, dP = 0;
		double vel_cut = 0, vel_up = vel_a_old, vel_down = 0, vel_a = vel_a_old;
		double[] temp = new double[2];
		double ed=0, CON = 0;
		double A_mix = 0, f_temp=0;
		double OR = MainDriver.Do2p[iCutting]/12/2, IR = MainDriver.Di2p[iCutting]/12/2;
		
		// F_mdvdt = Ffriction by vslip - Fgravity - Ffriction by casing wall
		
		MainDriver.A_cutting = 4*Math.PI*Math.pow((MainDriver.d_cutting/2/12),2); // ft2
		MainDriver.Vol_particle_cutting = Math.PI*4/3*Math.pow((MainDriver.d_cutting/2/12), 3) / 5.6145; //bbl
		m_cutting = Math.PI*4/3*Math.pow((MainDriver.d_cutting/2/12), 3) / 5.6145 * 42 * MainDriver.dens_cutting; // lbm
		n_cutting = MainDriver.V_cutting[iCutting]*42*MainDriver.dens_cutting/m_cutting;
		
		if(vel_a_old!=0){
			angle = angle * Math.PI / 180;
			vis = calc_vis_app(vel_a, d1, d2);
			//vslip = 138*(MainDriver.dens_cutting - MainDriver.oMud_save)*Math.pow(MainDriver.d_cutting,2)/vis; // laminar flow: Stokes' law
			vslip = 138*(MainDriver.dens_cutting - MainDriver.oMud_save)*Math.pow(MainDriver.d_cutting,2)/vis; // laminar flow: Stokes' law	
			re = 928*MainDriver.oMud_save*vslip*MainDriver.d_cutting/vis;
			if(re>0.1){
				while(err>0.001 && iter<100){
					iter++;
					vslip_prior = vslip;
					ff = calc_fric_moore(re);
					vslip = 1.89*Math.sqrt(MainDriver.d_cutting/ff*(MainDriver.dens_cutting-MainDriver.oMud_save)/(MainDriver.oMud_save));					
					re = 928*MainDriver.oMud_save*vslip*MainDriver.d_cutting/vis;
					err = Math.abs(vslip_prior-vslip); 
					
				}
			}
			MainDriver.vfall_cutting[iCutting] = vslip*Math.sin(angle);
			vel_a = calc_V_average(iCutting, vel_a_old, angle);
			A_mix = Math.PI/4*(MainDriver.Do2p[iCutting]*MainDriver.Do2p[iCutting]-MainDriver.Di2p[iCutting]*MainDriver.Di2p[iCutting])/144;
			f_temp = MainDriver.f_cutting[iCutting];
			
			result[0] = vslip*Math.cos(angle);
			result[1] = ff*Math.cos(angle);
			result[2] = vslip*Math.sin(angle);
			result[3] = vel_a;
			result[4] = A_mix;
			result[5] = f_temp;
					
		}
		else{
			result[0] = 0;
			result[1] = 0;
			result[2] = 0;
			result[3] = vel_a_old;
			result[4] = Math.PI/4*(MainDriver.Do2p[iCutting]*MainDriver.Do2p[iCutting]-MainDriver.Di2p[iCutting]*MainDriver.Di2p[iCutting])/144;
			result[5] = MainDriver.f_cutting[iCutting];
		}
		return result;
		}
	
	static double[] calcAhAp(int iP){
		double OD = 0, ID = 0;
		double x_md = MainDriver.TMD_cutting[iP-1];
		double x_vd = utilityModule.getVD(x_md);
		double A_tot = 0, A_cut = 0, h_cutting = 0;
		double err = 1, a1 = 0, a2 = 0, a_mid = 0, aP = 0;
		int iter = 0;
		double[] result = new double[4];
		
		ID = MainDriver.doDP;
		if(MainDriver.Dwater>0 && x_vd<=MainDriver.Dwater) OD = MainDriver.Driser;
		else if(x_vd<=MainDriver.DepthCasing) OD = MainDriver.IDcasing;
		else OD = MainDriver.DiaHole;
		
		A_tot = Math.PI/4*(OD*OD-ID*ID);
		A_cut = A_tot * MainDriver.f_cutting[iP];
		
		// Assume that there is no inner pipe.
		a1 = 0; a2 = Math.PI/2;
		while(Math.abs(err)>0.001 && iter<100){
			a_mid = (a1+a2)/2;
			err = A_cut - Math.pow((OD/2),2)*(a_mid - Math.cos(a_mid)*Math.sin(a_mid));
			if(err>0) a1 = a_mid;
			else a2 = a_mid;
			iter = iter + 1;
		}
		h_cutting = OD/2 - (OD/2)*Math.cos(a_mid);
		if(h_cutting+ID>OD){
			iter = 0; err = 1; a1 = 0; a2 = Math.PI/2;
			while(Math.abs(err)>0.001 && iter<100){
				a_mid = (a1+a2)/2; // AH 가정
				aP = Math.acos(1-OD/ID*(1-Math.cos(a_mid)));
				err = A_cut - Math.pow(OD/2, 2)*(a_mid-Math.cos(a_mid)*Math.sin(a_mid))+Math.pow(ID/2, 2)*(aP-Math.cos(aP)*Math.sin(aP));
				if(err>0) a1 = a_mid;
				else a2 = a_mid;
				iter = iter+1;
			}
			result[0] = a_mid; //AH
			result[1] = aP;  // AP
		}
		
		else{
			result[0] = a_mid; //AH
			result[1] = 0;  // AP
		}
		result[2] = OD;
		result[3] = ID;
		return result;
	}
	
	static double calc_fric_moore(double Rey){
		double fric_fact = 0;
		
		if(Rey<=3){
			fric_fact = 40/Rey;
		}
		else if(Rey<=300){
			fric_fact = 22/Math.sqrt(Rey);
		}
		else{
			fric_fact = 1.5;
		}
		
		return fric_fact;
	}
	
	static double[] calcPoreFrac(double depthD){
		//.... separate pore and fracture pressures calculation sub
		//     This can be called from outside.
		//     Jan. 24, 2003
		   
			double target_depth=0, bulk_density=0, p_overburden=0, p_seafloor=0, p_seawater=0, poisson=0;
			double[] result = new double[2]; // 0: Pore pressure 1: Fracture pressure
			double Pore = 0, Frac = 0;
		    p_seafloor = 0.052 * MainDriver.swDensity * MainDriver.Dwater;		
		    target_depth = depthD;
		    
		    bulk_density = 5.3 * Math.pow(target_depth, 0.1356);
		    p_overburden = p_seafloor + 0.052 * bulk_density * target_depth;
		    Pore = 0.8 * p_overburden;   //80 % of overburden pressure
		    Frac = 0.9 * p_overburden;   //90 % of overburden pressure
		    p_seawater = 0.052 * MainDriver.swDensity * (target_depth + MainDriver.Dwater);
		    if(Pore<p_seawater) Pore = p_seawater;
		    if(Frac<p_seawater) Frac = p_seawater;
		    
		    if(MainDriver.iFG==1){    //Eaton//s method
		    	if(target_depth < 5000) poisson = 0.3124642857 + 0.000057875 * target_depth - 0.000000006089286 * Math.pow(target_depth, 2);
		    	else poisson = 0.4260341387 + 0.0000072947129 * target_depth - 0.0000000001882 * Math.pow(target_depth, 2);
		    	
		    	if(poisson > 0.5) poisson = 0.5;
		    	if(poisson < 0.25) poisson = 0.25;
		    	Frac =  (poisson / (1 - poisson) * (p_overburden - Pore) + Pore);
		    	}
		    //... assign to the text boxes for PP & FP     
		    result[0] = Pore;
		    result[1] = Frac;		    		
		    return result;
		}
	
	static double[] GasProp(double pp, double tt){//prop
		//  Calculate the Viscosity of gas by Lee et al//s correlation and density of gas by Equation of state.
		//  T;Rankin  P;psia  FCO2 & FH2S;mole fraction of CO2 &H2S respectively
		//  gas viscosity;cp rhog-gm/cc  gasden;ppg
		//  0 : vis, 1 : den, 2:zz
		    double gasVis=0, gasDen=0, zz=0;
		    double [] result = {0,0,0};
			double ww=0, ak=0, xx=0, yy=0;
			ww = MainDriver.gasGravity * 29;
			
		    ak = (9.4 + 0.02 * ww) * Math.pow(tt,1.5) / (209 + 19 * ww + tt);
		    xx = 3.5 + (986 / tt) + 0.01 * ww; yy = 2.4 - 0.2 * xx;
		//
		    zz = Zfact(pp, tt);
		//
		    double CON =  8.345843646;
		    if (pp < 10.5){
		    	String ss= "Pressure is too small to calculate gas density & viscosity."+"\n"+"Please check your INPUT data or your responses on well control and try again.";
		    	JOptionPane.showMessageDialog(null, ss);
		      // MainMenu.Show *show구문 나중에 확인할 것.
		    }//End- Back to the main menu not STOP, 2003/7/24
		    else if(pp>100000){
		    	pp=123456; // added by jaewoo 20140109 to prevent producing the infinite number
		    	gasDen =  0.3611 * MainDriver.gasGravity * pp / (zz * tt);	
		    }
		    else{
		        gasDen =  0.3611 * MainDriver.gasGravity * pp / (zz * tt);		        
		        
		    }
		    double rhoG = gasDen / CON;
	        gasVis =  ak * Math.exp(xx * Math.pow(rhoG,yy)) / 10000.0;	        
	        
		    result[0]=gasVis;
		    result[1]=gasDen;
		    result[2]=zz;
		    
		    return result;
		}
	
	static double Harmathy(double surfTen, double rhoL, double rhoG, double Hg){//prop
		// This is good for the bubble rising velocity for bubble flow and generally accepted by many researchers.
		// surften;dynes/cm  rho;ppg  hg;no slip gas void fraction  v;ft/s
		//
		double dummy=1;
		   double temp = surfTen * (rhoL - rhoG) / (7.48 * rhoL * rhoL);
		   if(temp<=0) temp=0.00001; // added by jaewoo 20140109
		   double vSlip = 1.53 / 1.938 * Math.pow(temp, 0.25);  //; adjust = (1 - Hg) ^ 1.5  //2/10/2004
		   if(vSlip>-10000){
 				dummy=1;							
 			}
 			else{
 				dummy=1;
 			}
		   return vSlip;
		}
	
	static double Zfact(double P, double T){
	//..........  Calculates gas compressibility (z-factor).- P; psia T; Rankin
	//................ izfact = 2- ideal gas check the pressure
		double result = 1;
		double pc=0, tc=0, tr=0, pr=0;
		double a1 = 0.3265,  A2 = -1.07,  a3 = -0.5339, a4 = 0.01569, a5 = -0.05165, a6 = 0.5475, a7 = -0.7361, a8 = 0.1844, a9 = 0.1056,  a10 = 0.6134, a11 = 0.721;
		double c1=0, c2=0, c3=0, c4=0, dr=0, del=0, fun=0, dfun=0, dc4dr=0;//, dzdr=0; unused(20130807) ajw
		double crit = 0.0001; 
		int itcon=0;
		
	    if (MainDriver.iZfact==2 || P < 15.5) return 1;
	//------------- pseudo-critical temp. & press. by Sutton (SPE,1985)
	   
	    pc = 756.8 - 131 * MainDriver.gasGravity - 3.6 * MainDriver.gasGravity * MainDriver.gasGravity;
	    tc = 169.2 + 349.5 * MainDriver.gasGravity - 74 * MainDriver.gasGravity * MainDriver.gasGravity;
	    tr = T / tc; 
	    pr = P / pc;        // pseudo-reduced temp. & press.
	//----------------------------- Dranchuk & Abou-Kassem Eq.(JCP,1975)
	    
	//
        c1 = a1 + A2 / tr + a3 / Math.pow(tr, 3) + a4 / Math.pow(tr, 4) + a5 / Math.pow(tr, 5);
	    c2 = a6 + a7 / tr + a8 / Math.pow(tr, 2);
	    c3 = a9 * (a7 / tr + a8 / Math.pow(tr, 2));
	    	   
	//
	////   crit = .000001; itcon = 0  - use less criteria for fast run
	    
	//20  result = 1;
	while(itcon<=2){
		result=1;
	    for(int iter = 0; iter < 25; iter++){
	      dr = 0.27 * pr / (result * tr);
	      c4 = a10 * (1 + a11 * Math.pow(dr,2)) * (Math.pow(dr,2) /Math.pow(tr,3)) * Math.exp(-a11 * Math.pow(dr,2));
	      dc4dr = 2 * a10 * dr / Math.pow(tr,3);
	      dc4dr = dc4dr * (1 + a11 * Math.pow(dr,2) - Math.pow((a11 *Math.pow(dr,2)) , 2));
	      dc4dr = dc4dr * Math.exp(-a11 * Math.pow(dr,2));
	      //dzdr = c1 + 2 * c2 * dr - 5 * c3 * Math.pow(dr,4) + dc4dr; //unused 20130807 ajw 
	//...... function statement for DAK Eq.
	      fun = result - (1 + c1 * dr + c2 * Math.pow(dr,2) - c3 * Math.pow(dr,5) + c4);
	      dfun = 1 + (c1 + 2 * c2 * dr - 5 * c3 * Math.pow(dr,4) + dc4dr) * dr / result;
	      del = -(fun / dfun);
	      result = result + del;	      
	      if(Math.abs(del) < crit) return result;//40   Zfact = Z
	    }
	   
	    itcon = itcon + 1;
	    crit = crit * 10; //if itcon <= 2 Then GoTo 20
	}
	    return 1;//Zfact = 1
	//
	
	}
	
	static double DPDL1p(double VEL, double d2, double d1, double rho){
		// Calculate the frictional pressure loss for different fluid model. Also consider roughness of the pipe.
		// vel;ft/sec  D;in  RHO;ppg
		// model = 0; API RP 13D, 1; power law, 2; Bingham plastic
		// model = 1 & n = 1; Newtonian
		// CON ; Conversion factor from pipe to annulus,  corrections by; 12/11/02,
		//
			double result=0;
			double ff=0, ed=0, CON=0, VISp=0, VISa=0, ss=0, ren=0, Vcon=0;
			if (VEL < 0.0001) return 0;  // check the velocity
		    ed = MainDriver.Ruf / (d2 - d1);
		    CON = 1;
		    if (d1 > 0.002) CON = 0.816496581;
		    VISp = MainDriver.S600 - MainDriver.S300;
		    ss = MainDriver.S300 - VISp;
		//
		    if(MainDriver.Model==2){
		    	Vcon = 6.645638292;
		    	if (d1 > 0.002) Vcon = 4.984228717;
		    	VISa = VISp + Vcon * ss * (d2 - d1) / VEL;
		    	ren = 928 * CON * rho * VEL * (d2 - d1) / VISa;
		    	}
		    else ren = pRen(d2, d1, VEL, rho);
		    //
		    
		    if(MainDriver.Model==0){   //API RP 13D
		    	ff=getAPIRP13D(VEL, d2, d1, rho, MainDriver.S600, MainDriver.S300, MainDriver.SS100, MainDriver.SS3);
		    	CON = 1;   //1/16/03
		    	}		    
		    else ff=getf(ed, ren);		   
		   
		   result = ff * rho * VEL*VEL / (25.81 * CON * (d2 - d1));
		   return result;
		}
	
	static double DPDL1P_cuttings(double VEL, double d2, double d1, double rho){
		// Calculate the frictional pressure loss for different fluid model. Also consider roughness of the pipe.
		// vel;ft/sec  D;in  RHO;ppg
		// model = 0; API RP 13D, 1; power law, 2; Bingham plastic
		// model = 1 & n = 1; Newtonian
		// CON ; Conversion factor from pipe to annulus,  corrections by; 12/11/02,
		//
			double result=0;
			double ff=0, ed=0, CON=0, VISp=0, VISa=0, ss=0, ren=0, Vcon=0;
			if (VEL < 0.0001) return 0;  // check the velocity
		    ed = MainDriver.Ruf / (d2 - d1);
		    CON = 1;
		    if (d1 > 0.002) CON = 0.816496581;
		    VISp = MainDriver.S600 - MainDriver.S300;
		    ss = MainDriver.S300 - VISp;
		//
		    if(MainDriver.Model==2){
		    	Vcon = 6.645638292;
		    	if (d1 > 0.002) Vcon = 4.984228717;
		    	VISa = VISp + Vcon * ss * (d2 - d1) / VEL;
		    	ren = 928 * CON * rho * VEL * (d2 - d1) / VISa;
		    	}
		    else ren = pRen(d2, d1, VEL, rho);
		    //
		    
		    if(MainDriver.Model==0){   //API RP 13D
		    	ff=getAPIRP13D(VEL, d2, d1, rho, MainDriver.S600, MainDriver.S300, MainDriver.SS100, MainDriver.SS3);
		    	CON = 1;   //1/16/03
		    	}		    
		    else ff=getf(ed, ren);		   
		   
		   result = ff * rho * VEL*VEL / (25.81 * CON * (d2 - d1));
		   return result;
		}
	
	static double DPbit(double rho, double Qflow){//property
		//............... Calculate pressure drop through bit nozzle
		  double cd =  0.95;
		  double nozzleArea =  Math.pow(MainDriver.Dnoz[0],2) + Math.pow(MainDriver.Dnoz[1],2) + Math.pow(MainDriver.Dnoz[2],2) + Math.pow(MainDriver.Dnoz[3],2);
		  double areaT = 0.25 * MainDriver.pai * nozzleArea / 32 / 32;
		  double DPbit = 0.00008311 * rho * Qflow * Qflow / Math.pow(cd * areaT, 2);
		  if(MainDriver.Model == 0) DPbit = 156 * rho * Qflow * Qflow / nozzleArea / nozzleArea;
		  
		  return DPbit;
		}
	
	static double calc_vis_app(double vel, double d1, double d2){
		// only for annulus flow 150210 ajw
		double result=0;
		double Vcon=0, VISp=0, ss=0, kFlow=0, nFlow=0, temp=0;
		if(vel == 0){
			result = MainDriver.S300;
			return result;
			}
		if(MainDriver.Model==0){ // APIRP 13D
			nFlow = 0.657 * Math.log10(MainDriver.SS100 / MainDriver.SS3);
			kFlow = 5.11 * MainDriver.SS100 / Math.pow(170.2, nFlow);
			temp = (2 * nFlow + 1) / nFlow / 3;
			result = 100 * kFlow * Math.pow((144 * vel / (d2 - d1)),(nFlow - 1)) * Math.pow(temp, nFlow);
			}
		else if(MainDriver.Model==1){ // Powerlaw
			result = MainDriver.pK/144*Math.pow((d2-d1)/vel,1-MainDriver.pN)*Math.pow(((2 + 1 / MainDriver.pN)/0.0208), MainDriver.pN);
			if(MainDriver.iProblem[3]==1){
				if(MainDriver.iHuschel == 1){ // Huschel-Buckley Model for Nagasawa 1 case
					//nFlow = MainDriver.n_HC;
					//kFlow = MainDriver.K_HC;
					//result = kFlow/144*Math.pow((d2-d1/vel),1-nFlow)*Math.pow(((2 + 1 / nFlow)/0.0208), nFlow);
					result = 928*MainDriver.oMud_save*vel/pRen(d2,d1,vel,MainDriver.oMud_save);
				}
				else if(MainDriver.iHuschel == 2){ // Huschel-Buckley Model for Nagasawa 2 case
					//nFlow = MainDriver.n_HC;
					//kFlow = MainDriver.K_HC;
					//result = kFlow/144*Math.pow((d2-d1/vel),1-nFlow)*Math.pow(((2 + 1 / nFlow)/0.0208), nFlow);
					result = 928*MainDriver.oMud_save*vel/pRen(d2,d1,vel,MainDriver.oMud_save);
				}
			}
			}
		else if(MainDriver.Model==2){ // Bingham
			Vcon = 4.984228717;
			VISp = MainDriver.S600 - MainDriver.S300;
		    ss = MainDriver.S300 - VISp;
	    	result = VISp + Vcon * ss * (d2 - d1) / vel;
		}
		else{ // Newtonian
			result = MainDriver.S300;
		}
		return result;
	}
	static double pRen(double d2, double d1, double VEL, double rho){
		// Calculate Reynold no. for power law fluid
		//
		double result=0, A2=0, nFlow = 0, kFlow = 0;
		if(MainDriver.iHuschel == 1){ // Huschel-Buckley Model for Nagasawa 1 case
			nFlow = MainDriver.n_HC;
			kFlow = MainDriver.K_HC;
			if (d1 < 0.01) A2 = 89100 * Math.pow((0.0416 * d2 / (3 + 1 / nFlow)), nFlow);     //pipe flow
			else A2 = 109000 * Math.pow((0.0208 * (d2 - d1) / (2 + 1 / nFlow)), nFlow); //109000=0.816*144*928
			result = A2 * rho * Math.pow(VEL ,(2 - nFlow))/ kFlow;
		}
		else if(MainDriver.iHuschel == 2){ // Huschel-Buckley Model for Nagasawa 2 case
			nFlow = MainDriver.n_HC;
			kFlow = MainDriver.K_HC;
			if (d1 < 0.01) A2 = 89100 * Math.pow((0.0416 * d2 / (3 + 1 / nFlow)), nFlow);     //pipe flow
			else A2 = 109000 * Math.pow((0.0208 * (d2 - d1) / (2 + 1 / nFlow)), nFlow); //109000=0.816*144*928
			result = A2 * rho * Math.pow(VEL ,(2 - nFlow))/ kFlow;
			}
		else{
			if (d1 < 0.01) A2 = 89100 * Math.pow((0.0416 * d2 / (3 + 1 / MainDriver.pN)), MainDriver.pN);     //pipe flow
			else A2 = 109000 * Math.pow((0.0208 * (d2 - d1) / (2 + 1 / MainDriver.pN)), MainDriver.pN); //109000=0.816*144*928
			result = A2 * rho * Math.pow(VEL ,(2 - MainDriver.pN))/ MainDriver.pK;
		}
		return result;
		}
	
	static double getf(double ed, double ren){
		//  Friction factor by Colebrook eqn for Bingham plastic and Newtonian
		//  by  DODGE AND METZNER for powerlaw - solve by Newton-Raphson Method
		
		int iCount=0;
		double result=0, ff1=0, ff2=0, f2=0, ffun=0, fprime=0, tmpx=0, CONV=0, nFlow = 0, kFlow=0;
		//
		
		if(ren==0) return 0;
		
		result = 16 / ren;
		if (ren < 500.5) return result;
		//
		ff1 = 16 / ren; 
		ff2 = 0;
		
		if(ren > MainDriver.RenC){
			CONV = 0.000002;
			f2 = 0.079 / Math.pow(ren, 0.25);
			while(Math.abs(ff2-f2)>=CONV || iCount==0){
				if(iCount>0) f2=ff2;
				if(MainDriver.Model==2){      //Bingham plastic
					ff2 = -4 * Math.log10(0.269 * ed + 1.255 / ren / Math.sqrt(f2));
					ffun = 1 / Math.sqrt(f2) - ff2;
					tmpx = Math.log(10) * ren * Math.pow(Math.sqrt(f2),3)*(0.269 * ed + 1.255 / ren / Math.sqrt(f2));
					fprime = -0.5 / Math.pow(Math.sqrt(f2),3) - 2 * 1.255 / tmpx;
					ff2 = f2 - ffun / fprime;
					}
				else{                  //Power law and Newtonian (n=1)
					if(MainDriver.iHuschel == 1){ // Huschel-Buckley Model for Nagasawa 1 case
						nFlow = MainDriver.n_HC;
						kFlow = MainDriver.K_HC;
						ff2 = 4 * Math.log10(ren * Math.pow(f2, (1 - 0.5 * nFlow))) / Math.pow(nFlow, 0.75) - 0.395 / Math.pow(nFlow, 1.2);
						ffun = 1 / Math.sqrt(f2) - ff2;
						tmpx = Math.log(10) * f2 * Math.pow(nFlow,0.75);
						fprime = -0.5 / Math.pow(Math.sqrt(f2),3) - 4 * (1 - 0.5 * nFlow) / tmpx;
						ff2 = f2 - ffun / fprime;
					}
					else if(MainDriver.iHuschel == 2){ // Huschel-Buckley Model for Nagasawa 2 case
						nFlow = MainDriver.n_HC;
						kFlow = MainDriver.K_HC;
						ff2 = 4 * Math.log10(ren * Math.pow(f2, (1 - 0.5 * nFlow))) / Math.pow(nFlow, 0.75) - 0.395 / Math.pow(nFlow, 1.2);
						ffun = 1 / Math.sqrt(f2) - ff2;
						tmpx = Math.log(10) * f2 * Math.pow(nFlow,0.75);
						fprime = -0.5 / Math.pow(Math.sqrt(f2),3) - 4 * (1 - 0.5 * nFlow) / tmpx;
						ff2 = f2 - ffun / fprime;
					}
					else{
						ff2 = 4 * Math.log10(ren * Math.pow(f2, (1 - 0.5 * MainDriver.pN))) / Math.pow(MainDriver.pN, 0.75) - 0.395 / Math.pow(MainDriver.pN, 1.2);
						ffun = 1 / Math.sqrt(f2) - ff2;
						tmpx = Math.log(10) * f2 * Math.pow(MainDriver.pN,0.75);
						fprime = -0.5 / Math.pow(Math.sqrt(f2),3) - 4 * (1 - 0.5 * MainDriver.pN) / tmpx;
						ff2 = f2 - ffun / fprime;
					}
					}
				iCount++;
				}
			}	
		//
		result = ff2;
		if (ff1 > ff2) result = ff1;
		
		return result;
		}
	
	static double reynolds_fun(double Q){
		double d_avg=0, CON=0, VISp=0, VISa=0, ss=0, ren=0, Vcon=0, VEL=0, rho=MainDriver.oMud_save;
		double nFlow = 0, kFlow = 0;
		if(MainDriver.iProblem[3]==0) rho = MainDriver.oMud;
		if (Q < 0.0001) return 0;  // check the velocity
	    CON = 1;
	    VISp = MainDriver.S600 - MainDriver.S300;
	    ss = MainDriver.S300 - VISp;
	    
	    for(int i=0; i<4; i++){
	    	d_avg = d_avg+MainDriver.Dnoz[i];
	    	}
	    d_avg = d_avg / 4;
	    VEL = Q/4/42*5.6145/60/(Math.PI/4*d_avg*d_avg/144);
	    
	    if(MainDriver.Model==2){
	    	Vcon = 6.645638292;
	    	VISa = VISp + Vcon * ss * d_avg / VEL;
	    	ren = 928 * CON * rho * VEL * d_avg / VISa;
	    	}
	    
	    else{
	    	double A2=0;
	    	if(MainDriver.iHuschel==1){
	    		nFlow = MainDriver.n_HC;
				kFlow = MainDriver.K_HC;
	    		A2 = 89100 * Math.pow((0.0416 * d_avg / (3 + 1 / nFlow)), nFlow);     //pipe flow
	    		ren = A2 * rho * Math.pow(VEL ,(2 - nFlow))/ kFlow;
	    	}
	    	else if(MainDriver.iHuschel == 2){ // Huschel-Buckley Model for Nagasawa 2 case
	    		nFlow = MainDriver.n_HC;
				kFlow = MainDriver.K_HC;
	    		A2 = 89100 * Math.pow((0.0416 * d_avg / (3 + 1 / nFlow)), nFlow);     //pipe flow
	    		ren = A2 * rho * Math.pow(VEL ,(2 - nFlow))/ kFlow;
	    	}
	    	else{
	    		A2 = 89100 * Math.pow((0.0416 * d_avg / (3 + 1 / MainDriver.pN)), MainDriver.pN);     //pipe flow
	    		ren = A2 * rho * Math.pow(VEL ,(2 - MainDriver.pN))/ MainDriver.pK;
	    	}
	    }
	    
	    if(MainDriver.Model==0){   //API RP 13D
	    	double temp=0, viscosity=0, Reynolds=0, aTurb=0, bTurb=0;
	    	nFlow = 3.32 * Math.log10(MainDriver.S600 / MainDriver.S300);
	    	kFlow = 5.11 * MainDriver.S600 / Math.pow(1022,nFlow);
	    	temp = 0.25 * (3 * nFlow + 1) / nFlow;
	    	viscosity = 100 * kFlow * Math.pow((96 * VEL / d_avg),(nFlow - 1)) *Math.pow(temp,nFlow);
	    	ren = 928 * d_avg * VEL * rho / viscosity;
	    	}
	    return ren;
	    }
	
	static double getAPIRP13D(double VEL, double Dia2, double Dia1, double rho, double S600, double S300, double s100, double s3){
		//...... calcuate friction factor using APIRD13D formula
		// vel; ft/s, dia; in, rho; ppg, s100, s3; 100, 3 rpm reading, modified by 12/11/02,
		//
		   double nFlow=0, kFlow=0, temp=0, viscosity=0, Reynolds=0, result=0, aTurb=0, bTurb=0;
		   if(Dia1 < 0.002){    //pipe flow
		      nFlow = 3.32 * Math.log10(S600 / S300);
		      kFlow = 5.11 * S600 / Math.pow(1022,nFlow);
		      temp = 0.25 * (3 * nFlow + 1) / nFlow;
		      viscosity = 100 * kFlow * Math.pow((96 * VEL / Dia2),(nFlow - 1)) *Math.pow(temp,nFlow);
		   }
		   else{   //annulus flow
		      nFlow = 0.657 * Math.log10(s100 / s3);
		      kFlow = 5.11 * s100 / Math.pow(170.2, nFlow);
		      temp = (2 * nFlow + 1) / nFlow / 3;
		      viscosity = 100 * kFlow * Math.pow((144 * VEL / (Dia2 - Dia1)),(nFlow - 1)) * Math.pow(temp, nFlow);
		   }
		   Reynolds = 928 * (Dia2 - Dia1) * VEL * rho / viscosity;
		//
		//... calc. of frictional factor
		   if(Reynolds < MainDriver.RenC){   //laminar flow, commonly 2100 is used
		         result = 24 / Reynolds;
		         if(Dia1 < 0.002) result = 16 / Reynolds;
		   }
		   else{
		         aTurb = 0.02 * (Math.log10(nFlow) + 3.93);
		         bTurb = (1.75 - Math.log10(nFlow)) / 7;
		         result = aTurb / Math.pow(Reynolds, bTurb);
		         
		   }
		   return result;
		}
	
	static double range (double Cval, double Cmin, double Cmax){
		//  the subroutine to check the range of any variable based on given values of minimum and maximum.
		//
		   if (Cval < Cmin) Cval = Cmin;
		   if (Cval > Cmax) Cval = Cmax;
		   return Cval;
		}
	
	static double[] Velocity(double ql, double Qg, double d2, double d1){//prop
		//  The subroutine to calculate liquid and gas velocities
		//  q:gpm  d:in  v:ft/sec
		// 0 : Liquid Velocity, 1 : gas velocity
			 double[] result = {0,0};
			 result[0] = 0.408375954 * ql / (d2 * d2 - d1 * d1);
			 result[1] = 0.408375954 * Qg / (d2 * d2 - d1 * d1);
			 return result;	 
		 }
	
	static double Beggs2(double vL, double vG, double d2, double d1, double hL, double rhoL, double rhoG, double visG){//prop
		//  Function to calculate 2-phase friction factor based on
		//  Beggs & Brill method with input insitu liquid fraction (Hl).
		//  v;ft/sec  d;in  angle;degree  rho;ppg  p;psia  t;Rankin  vis;cp
		//
			double fn=0, ss=0, Y=0, sy=0;
			double ed = MainDriver.Ruf / (d2 - d1);
			double Beggs2=0, vm=0, raml=0, rhon=0, visn=0, ren=0;
		    double CON = 1; 
		    if (d1 < 0.001) CON = 0.816496581;
		    Beggs2 = 0;
		    vm = vL + vG;
		    if (vL < 0||vm < 0.0001) return Beggs2;
		//
		    raml = vL / vm;
		    rhon = rhoL * raml + rhoG * (1 - raml);
		    if (raml > 0.95||raml < 0.01) visn = MainDriver.visL * raml + visG * (1 - raml);
		    else visn = Math.pow(MainDriver.visL, raml) * Math.pow(visG, (1 - raml));
		    ren = 928 * CON * rhon * vm * (d2 - d1) / visn;
		    fn = getf(ed, ren);
		//
		    if (hL < 0.00001||raml < 0.00001) ss = 0;
		    else{
		      Y = raml / Math.pow(hL,2); 
		      sy = Math.log(Y); //in vb log() means natural logarithm
		      ss = sy / (-0.0523 + 3.182 * sy - 0.8725 * Math.pow(sy,2) + 0.01853 * Math.pow(sy, 4));
		      if (Y < 1.2 && Y > 1) ss = Math.log(2.2 * Y - 1.2);
		    }
		//
		   Beggs2 = fn * Math.exp(ss) * rhon * vm * vm / (25.8 * CON * (d2 - d1));
		   
		   return Beggs2;
		}
	
	static double Ditpl(double[] xAry, double[] yAry, double xVal, int n){ //prop
		//  Function to calculate linear interploation by searching a table
		//
			double xgrad=0, Ditpl=0;
			for(int i=0; i<(n - 1); i++){
				if (xVal >= xAry[i] && xVal < (xAry[i + 1] + 0.05)) {
					xgrad = (yAry[i + 1] - yAry[i]) / (xAry[i + 1] - xAry[i]);
					Ditpl = yAry[i] + xgrad * (xVal - xAry[i]);
					return Ditpl;
					}
				}
			if (xVal < xAry[0]) Ditpl = yAry[0];
			if (xVal > xAry[n-1]) Ditpl = yAry[n-1];
			return Ditpl;
		}
	
	static double DitplDes(double x2){//PROP
		//  Function to calculate linear interploation by searching a table
		//  in desending order  : ditpldes (xx, yy, x2, n As Integer)
		// this is only good for casing seat pressure calculation
		//
			int n = MainDriver.N2phase;
			double DitplDes=0, Pgrad=0;
			for(int i = 0; i<(n - 1); i++){
				if (x2 < MainDriver.Xnd[i] + 0.0001 && x2 >= MainDriver.Xnd[i+1]) {
					Pgrad = (MainDriver.Pnd[i+1] - MainDriver.Pnd[i]) / (MainDriver.Xnd[i+1] - MainDriver.Xnd[i]);
					DitplDes = MainDriver.Pnd[i] + Pgrad * (x2 - MainDriver.Xnd[i]);
					return DitplDes;
					}
				}
			if (x2 < MainDriver.Xnd[0]) DitplDes = MainDriver.Pnd[0];
			if (x2 > MainDriver.Xnd[n-1]) DitplDes = MainDriver.Pnd[n-1];
			return DitplDes;
		}
	
	static double surfT(double P, double T){//prop
		//...........CALCULATE WATER SURFACE TENSION
		//           p; psia t; Fahrenheit
			double surfT=0, STW74=0, STW280=0, temp=0;
			double[] STVA={0,0,0,0,0,0,0,0,0,0};
			double[] STV74 ={0,0,0,0,0,0,0,0,0,0};
			double[] STV280 = {0,0,0,0,0,0,0,0,0,0};
		//....... ENTER DATA ARRAYS for INTERPOLATION,METHANE-WATER SURFACE TENSION
		//..      AT 74 AND 280 DEG-F VS PRESSURE CURVES
		//
		    STVA[0] = 0;   STVA[1] = 1000; STVA[2] = 2000;
		    STVA[3] = 3000; STVA[4] = 4000; STVA[5] = 5000;
		    STVA[6] = 6000; STVA[7] = 7000; STVA[8] = 8000;
		    STVA[9] = 9000;
		    STV74[0] = 75; STV74[1] = 63; STV74[2] = 59;
		    STV74[3] = 57; STV74[4] = 54; STV74[5] = 52;
		    STV74[6] = 52; STV74[7] = 51; STV74[8] = 50;
		    STV74[9] = 49;
		    STV280[0] = 53; STV280[1] = 46; STV280[2] = 40;
		    STV280[3] = 33; STV280[4] = 26; STV280[5] = 21;
		    STV280[6] = 21; STV280[7] = 22; STV280[8] = 23;
		    STV280[9] = 24;
		//      DATA STV74 /75.,63.,59.,57.,54.,52.,52.,51.,50.,49./
		//     DATA STV280/53.,46.,40.,33.,26.,21.,21.,22.,23.,24./
		//..CALCULATE GAS-WATER SURFACE TENSIONS AT 74 AND 280 DEG-F,DYNES/CM.
		    STW74 = Ditpl(STVA, STV74, P, 10);
		//   n = 10
		//   for i = 1 To n - 1
		//     if (p >= STVA(i) And p < STVA(i + 1) + .0001) Then
		//      xgrad = (STV74(i + 1) - STV74(i)) / (STVA(i + 1) - STVA(i))
		//      STW74 = STV74(i) + xgrad * (p - STVA(i))
		 //     Exit for
		//     End if
		//   Next i
		//   if (p < STVA(1)) Then STW74 = STV74(1)
		//   if (p > STVA(n)) Then STW74 = STV74(n)
		//
		    STW280 = Ditpl(STVA, STV280, P, 10);
		//    for i = 1 To n - 1
		//     if (p >= STVA(i) And p < STVA(i + 1) + .0001) Then
		//      xgrad = (STV280(i + 1) - STV280(i)) / (STVA(i + 1) - STVA(i))
		//      STW280 = STV280(i) + xgrad * (p - STVA(i))
		//      Exit for
		//     End if
		//   Next i
		//   if (p < STVA(1)) Then STW280 = STV280(1)
		//   if (p > STVA(n)) Then STW280 = STV280(n)
		//
		//..PERforM LINEAR INTERPOLATION TO DETERMINE GAS-WATER SURFACE TENSION
		//..for TEMPERATURE BETWEEN 74 AND 280 DEG-F, DYNES/CM.
		//..DO NOT EXTRAPOLATE BEYOND RANGE OF DATA.
		//
		    temp = T - 460;
		    surfT = (STW280 - STW74) / (280 - 74) * (temp - 74) + STW74;
		    if (temp < 74) surfT = STW74;
		    if (temp > 280) surfT = STW280;
		    
		    return surfT;
		}
	
	
	static double holdup(double vL, double vG, double d2, double d1, double rhoL, double surfTen, double angle){
		//   To calculate the liquid holdup by Beggs-Brill correlation
		//   v;superficial velocity-no slip
		//
			double hL=0, vm=0, raml=0, frn=0, al1=0, al2=0, al3=0, al4=0, sten=0, alvn=0, angRad=0, iregime=0, rl=0, phi=0, hlhz=0, hlseg=0, hlint=0, ab=0;
			double CON=1, dprim = 0, e = 0, f = 0, g = 0, a = 0, b = 0, c = 0, c2=0, temp=0;
		
			if (d1 > 0.001) CON = 0.816496581;
			vm = vL + vG;
			raml = vL / vm;
			frn =  12 / 32.17 * vm * vm / (CON * (d2 - d1));
			al1 = 316 * Math.pow(raml, 0.302); 
			al2 = 0.0009252 / Math.pow(raml, 2.4684);
			al3 = 0.1 / Math.pow(raml, 1.4516); 
			al4 = 0.5 / Math.pow(raml, 6.738);
			sten = surfTen;

			alvn = 1.938 * vL * Math.pow((rhoL * 7.48 / sten), 0.25);
			angRad = MainDriver.pai * angle / 180;
		//............................................Determine flow regime;
			iregime = 0; 
			rl = raml;
		//........................................................Segregated
			if ((rl > 0.01 && frn < al1) || (rl > 0.01 && frn < al2)){
		         dprim = 0.011; e = -3.768; f = 3.539; g = -1.614;
		         a = 0.98; b = 0.4846; c = 0.0868;
			}
		//....................................................... Transition
		      else if ((rl > 0.01) && (frn > al2 && frn < al3)){
		         iregime = 1;
		//.............................. Interpolation by Seg.
		         dprim = 0.011; e = -3.768; f = 3.539; g = -1.614;
		         a = 0.98; b = 0.4846; c = 0.0868;
		         temp = dprim * Math.pow(raml, e) * Math.pow(alvn, f) * Math.pow(frn, g);
		         c = (1 - raml) * Math.log(temp);
		         phi = 1 + c * (Math.sin(1.8 * angRad) - 1 / 3 * Math.pow(Math.sin(1.8 * angRad),3));
		         hlhz = a * Math.pow(raml, b) / Math.pow(frn, c); 
		         if (hlhz < rl) hlhz = rl;
		         hlseg = hlhz * phi;
		//...................................... and Intermittent
		         dprim = 2.96; e = 0.305; f = -0.4473; g = 0.0978;
		         a = 0.845; b = 0.5351; c = 0.0173;
		         temp = dprim * Math.pow(raml, e) * Math.pow(alvn, f) * Math.pow(frn, g);
		         c = (1 - raml) * Math.log(temp);
		         phi =  (1 + c * (Math.sin(1.8 * angRad) - 1 / 3 * (Math.pow(Math.sin(1.8 * angRad), 3))));
		         hlhz =  (a * Math.pow(raml, b) / Math.pow(frn, c));
		         if (hlhz < rl) hlhz = rl;
		         hlint = hlhz * phi;
		//
		         ab = (al3 - frn) / (al3 - al2);
		         hL = ab * hlseg + (1 - ab) * hlint;
		      }
		//..................................................... Distributed
		      else if ((rl < 0.4 && frn > al1) || (rl > 0.4 && frn > al4)) {
		         iregime = 2;
		         a = 1.065; b = 0.5824; c = 0.0609;
		         hlhz = a * Math.pow(raml, b) / Math.pow(frn, c); 
		         if (hlhz < rl) hlhz = rl;
		         phi = 1; 
		         hL = hlhz * phi;
		      }
		//.................................................... Intermittent
		      else{
		//         if(((rl>0.01 and rl<0.4) and
		//     &   (frn>al3 and frn<al1))
		//     &   or (rl>0.4 and (frn>al3 and frn<al4))) then
		         dprim = 2.96; e = 0.305; f = -0.4473; g = 0.0978;
		         a = 0.845; b = 0.5351; c = 0.0173;
		      }
		      if (iregime == 0){
		        temp = dprim * Math.pow(raml, e) * Math.pow(alvn , f) * Math.pow(frn , g);
		        c2 = (1 - raml) * Math.log(temp);
		        phi = 1 + c2 * (Math.sin(1.8 * angRad) - 1 / 3 * Math.pow(Math.sin(1.8 * angRad), 3));
		        hlhz = a * Math.pow(raml , b) / Math.pow(frn , c);
		        if (hlhz < rl) hlhz = rl;
		        hL = hlhz * phi;
		      }
		//
		      return hL;
		}

	
	static double Beggs(double vL, double vG, double d2, double d1, double angle, double rhoL, double rhoG, double visG, double surfTen){//prop
		//  Function to calculate 2-phase friction factor based on Beggs & Brill method
		// v; gpm d; in angle; degree rho; ppg p; psia t; Rankin vis; cp, v; superficial velocity
		//
			double Beggs=0, ed=0, vm=0, raml=0, rhon=0, visn=0, ren=0, hL, ss=0, sy=0, Y=0;
			double fn=0, CON=0;
			
			ed = MainDriver.Ruf / (d2 - d1);
			CON = 1; 
			if (d1 < 0.001) CON = 0.816496581;
			vm = vL + vG;
			raml = vL / vm;
		//
			rhon = rhoL * raml + rhoG * (1 - raml);
			if (raml > 0.95 || raml < 0.01)  visn = MainDriver.visL * raml + visG * (1 - raml);
			else visn = Math.pow(MainDriver.visL, raml) * Math.pow(visG, (1 - raml));
			
			ren = 928 * CON * rhon * vm * (d2 - d1) / visn;
		//
		   fn = getf(ed, ren);
		   hL = holdup(vL, vG, d2, d1, rhoL, surfTen, angle);
		//
		   if (hL < 0.00001 || raml < 0.00001) ss = 0;
		   else{
		      Y = raml / Math.pow(hL, 2); 
		      sy =  Math.log(Y);
		      ss = sy / (-0.0523 + 3.182 * sy - 0.8725 * Math.pow(sy, 2) + 0.01853 * Math.pow(sy, 4));
		      if (Y < 1.2 && Y > 1) ss =  Math.log(2.2 * Y - 1.2);
		   }
		//
		   Beggs = fn * Math.exp(ss) * rhon * vm * vm / (25.8 * CON * (d2 - d1));
		   return Beggs;
		
		}	

	static double Eaton(double Depth){
	// Calculation of fromation fracture gradient & pressure by Eaton method
	// pb;BHP,psi  dt;total depth,ft  Depth;depth of interest,ft, Eaton; psia
		double fpp=0, s=0, v=0, f=0, Eaton=0; 
		if(Depth > (MainDriver.Vdepth - 5.5)) fpp = MainDriver.gMudKill;    //fpp = pform / vdepth
		else fpp = MainDriver.gMudOld;
	//
		s = 0.83969 + 0.000014963 * Depth - 0.00000000045241 * Depth * Depth + 5.2436E-15 * Math.pow(Depth, 3);
		v = -0.19786 + 0.16132 * Math.log10(Depth);
	//...................................... Check the range of regression
	   s = range(s, 0.8, 1);
	   v= range(v, 0.25, 0.5);
	   f = (s - fpp) * v / (1 - v) + fpp;
	   Eaton = f * Depth;
	//
	   return Eaton;
	}

	static double[] hasanAng(double vL, double vG, double d2, double d1, double rhoL, double rhoG, double surfTen, double angle){
	//  This is good for pipe and annulus flow based on flow pattern recognition.
	//  v;ft/s  d;in  rho;ppg  vis;cp  surften;dynes/cm, angle;degree from vertical
	//  0: vslip 1: vgf
		double vSlip=0, Vgf=0, c=0, CON=0;
		double[] result={0, 0};
		double angRad = angle * MainDriver.pai / 180;
		double cbub = 1.2, hbub = 0.25, con2 = hbub / (1 - cbub * hbub);
		double vn = vL + vG; 
		double Hg = vG / vn;
		double vsbub = Harmathy(surfTen, rhoL, rhoG, Hg);
		double vbub =  (cbub * vL + vsbub) * con2 * Math.cos(angRad);
		double temp =  32.17 * d2 * (rhoL - rhoG) / rhoL / 12;
		double costmp =  Math.abs(Math.cos(angRad));
		double temp2 =  Math.sqrt(costmp) * Math.pow((1 + Math.sin(angRad)), 1.2);
		double v2slip =  (0.345 + 0.1 * d1 / d2) * Math.sqrt(temp) * temp2;
		double vbubble = (cbub * vL + v2slip) * con2;
		double vtemp = 0.5 * (vbub + vbubble);
		double rholv2 = 7.48 * rhoL * vL * vL;
		double rhogv2 = 7.48 * rhoG * vG * vG;
		double cslug = 0.00673 * Math.pow(rholv2, 1.7);
		if (rholv2 > 50) cslug = 17.1 * Math.log10(rholv2) - 23.2;
		temp = surfTen * (rhoL - rhoG) / (7.48 * Math.pow(rhoG, 2));
		double vann = 3.1 * Math.pow(temp, 0.25) / 1.938;
	//.................... flow pattern map basedon Hasan et al.//s proposal
		if (vG > vann){            // annula flow
	      vSlip = 0;
	      c = 1;
		}
	   else if (vG > vtemp){
	      if (rhogv2 > cslug){      // churn flow
	         CON = 0.345;
	         if (d1 > 0.001) CON = 0.3;
	         temp = 32.17 * (d2 - d1) * (rhoL - rhoG) / rhoL / 12;
	         vSlip = (CON + 0.22 * d1 / d2) * Math.sqrt(temp);
	         c = 1.15 + 0.7 * d1 / d2;
	      }
	      else{                          // slug flow
	         vSlip = v2slip; 
	         c = 1.2 + 0.7 * d1 / d2;
	      }
	   }
	   else {
		   vSlip = vsbub; 
		   c = 1.2 + 0.371 * d1 / d2; // bubble flow
	   }
	    
	   c= range(c, 1, 1.5);
	   if (angle > 87){
	      vSlip = 0;
	      c = 1;
	   }
	   Vgf = c * vn + vSlip;
	   result[0]=vSlip;
	   result[1]=Vgf;
	   return result;
	//	   
	}


/*
	Function log10(xVal) //this function exists in Java.
	  if (xVal <= 0) Then
	     MsgBox "Value in Log is negative ==> STOP the program !", 0, msgTitle
	     Stop
	  else
	    log10 = Log(xVal) / Log(10)
	  End if
	End Function

*/	

	static double prentice(double Depth){
	//  Calculate the fracture gradient at offshore by Christman & Prentice using Eaton//s data
	//  p; psi d; ft dw; water depth, ft
	//.................... Assume average salty water density 64.4 lb/cu ft
		double prentice=0, rhow = 0.052 * 64.4 / 7.48, Pseaf = rhow * MainDriver.Dwater;
		double fg = Eaton(Depth) / Depth;
		double Dweq = Pseaf / fg;
		double Deq = Dweq + Depth;
		prentice = Eaton(Deq);
		
		return prentice;
	}

	

	static double Taylor(double d2, double rhoL, double rhoG){
	//.....  This is good for the rising velocity of large-sized bubble, named "Taylor" bubble.
	// D2; in RHO; ppg
	//
		double temp = 32.17 * d2 * (rhoL - rhoG) / 12 / rhoL;
		double Taylor = 0.345 * Math.sqrt(temp);
		return Taylor;
	}

	static double[] getLL2(double dn){//, xL1, xL2)
	//  calculate the flow regime numbers L1 and L2 in the function of pipe diameter number, Nd
	//  This is simplified stepwise linear interpolation.
	//
		double xL1=0, xL2=0;
		double[] result={0,0};
		
		if (dn > 65) xL1 = 1;
		else if (dn > 30) xL1 = 2 - Math.log10(dn / 30) / Math.log10(65 / 30);
		else xL1 = 2;
		
	//
		if (dn > 60) xL2 = 1.0976;
		else if (dn > 15) xL2 = 0.4 + 0.6976 * Math.log10(dn / 15) / Math.log10(60 / 15);
		else xL2 = 0.4;
		
		result[0]=xL1;
		result[1]=xL2;
		
		return result;
		
	}

	

	static double[] Hasan(double vL, double vG, double d2, double d1, double rhoL, double rhoG, double surfTen){//, vSlip, Vgf)
	//  This is based on Hasan//s paper for bubble velocity for vertical well (SPE 15138)
	//  This is good for pipe and annulus flow based on flow pattern recognition.
	//  v;ft/s  d;in  rho;ppg  vis;cp  surften;dynes/cm
	//
	
		double vSlip=0, Vgf=0, temp=0, vann=0, c=0, CON=0;
		double[] result={0,0};
		double cbub = 1.2, hbub = 0.25;
		double con2 = hbub / (1 - cbub * hbub);
		double vn = vL + vG, Hg = vG / vn, hL = 1 - Hg;
		//......................................... check for single-phase case
		int ivelocity = 0;
		if (Hg < 0.00001){
			ivelocity = 1;
			vSlip = 0;
			Vgf = 0;
	   }
	   else if (hL < 0.00001){
		   ivelocity = 1;
		   vSlip = 0;
		   Vgf = vG;
	   }
	   if (ivelocity == 1){
		   result[0]=vSlip;
		   result[1]=Vgf;
		   return result;
	   }
	//
	   double vsbub = Harmathy(surfTen, rhoL, rhoG, Hg);
	   double vbub = (cbub * vL + vsbub) * con2;
	   double rholv2 = 7.48 * rhoL * vL * vL; 
	   double rhogv2 = 7.48 * rhoG * vG * vG;
	   double cslug = 0.00673 * Math.pow(rholv2, 1.7);
	   if (rholv2 > 50) cslug = 17.1 * Math.log10(rholv2) - 23.2;
	   temp = surfTen * (rhoL - rhoG) / (7.48 * Math.pow(rhoG, 2));
	   vann = 3.1 * Math.pow(temp, 0.25) / 1.938;
	//........................................ flow pattern map by Hasan te al.
	   if (vG > vann) {             // annula flow
	      vSlip = 0;
	      c = 1;}
	   else if (vG > vbub){             // churn flow
	      CON = 0.345;
	      if (d1 > 0.001) CON = 0.3;
	      temp = 32.17 * (d2 - d1) * (rhoL - rhoG) / rhoL / 12;
	      vSlip = (CON + 0.22 * d1 / d2) * Math.sqrt(temp);
	      if (rhogv2 > cslug) c = 1.15 + 0.9 * d1 / d2;      // slug flow
	      else c = 1.182 + 0.9 * d1 / d2;
	   }
	   else{                           // bubble flow
		   vSlip = vsbub;
		   c = 1.2 + 0.371 * d1 / d2; 
	   }
	   
	   c = range(c, 1, 1.5);
	   Vgf = c * vn + vSlip;
	//
	   result[0]=vSlip;
	   result[1]=Vgf;
	   return result;
	}

	
	static int FractGrad(double PatCsg, double PatBHP){ // 나중에 다시 확인할 것!
	//........ the sub to check the formation fracture and underground blowout
	//--- check  the underground blowout !
		
	    if (PatCsg > MainDriver.PfgCsg) MainDriver.timeCsg = MainDriver.timeCsg + MainDriver.gDelT;
	    else MainDriver.timeCsg = 0;
	    
	    if (PatBHP > MainDriver.PfgBHP) MainDriver.timeBHP = MainDriver.timeBHP + MainDriver.gDelT;
	    else MainDriver.timeBHP = 0;

	    if (MainDriver.timeCsg > 2000 || MainDriver.timeBHP > 2000){ //300->2000 //20140306 ajw
	      if(MainDriver.iBlowout !=0 && MainDriver.BlowOutPnlVisible==0){
	    	  //
	    	  //BlowOut.Show; 
	    	  //BlowOut.TimerBO.Enabled = True
	    	  //Unload DrillSI
	    	  //Unload SimManual
	    	  //Unload SimAuto
	    	  m3 = new blowout();
	    	  m3.setVisible(true);
	    	  MainDriver.BlowOutPnlVisible=1;
	    	  return -1;//2013 12 09 ajw blow out!	    	 
	      }
	      return 0; //2013 12 09  without animation
	    }	    
	    return 1; //2013 12 09 Blowout doesn't appear
	}
	
	static void surfEqData(){
	//....Pre-classified data for the surface equipments based on equivalent length
	//                   re-located from property module to here, 8/3/2003
	   if (MainDriver.iType == 1) {
	      MainDriver.DiaSP = 4; MainDriver.LengthSP = 45; MainDriver.DiaHose = 3; MainDriver.LengthHose = 55;
	      MainDriver.DiaSwivel = 3; MainDriver.LengthSwivel = 6;  MainDriver.DiaKelly = 4; MainDriver.LengthKelly = 40;
	      MainDriver.Heqval = 100;
	   }
	   else if (MainDriver.iType == 2) {
	      MainDriver.DiaSP = 4;  MainDriver.LengthSP = 45; MainDriver.DiaHose = 3;   MainDriver.LengthHose = 55;
	      MainDriver.DiaSwivel = 2.5; MainDriver.LengthSwivel = 5;  MainDriver.DiaKelly = 3.25; MainDriver.LengthKelly = 40;
	      MainDriver.Heqval = 150;
	   }
	   else if (MainDriver.iType == 3) {
	      MainDriver.DiaSP = 3.5; MainDriver.LengthSP = 40; MainDriver.DiaHose = 2.5;  MainDriver.LengthHose = 55;
	      MainDriver.DiaSwivel = 2.5; MainDriver.LengthSwivel = 5;  MainDriver.DiaKelly = 3.25; MainDriver.LengthKelly = 40;
	      MainDriver.Heqval = 250;
	   }
	   else if (MainDriver.iType == 4) {
	      MainDriver.DiaSP = 3; MainDriver.LengthSP = 40; MainDriver.DiaHose = 2;   MainDriver.LengthHose = 45;
	      MainDriver.DiaSwivel = 2; MainDriver.LengthSwivel = 4;  MainDriver.DiaKelly = 2.25; MainDriver.LengthKelly = 40;
	      MainDriver.Heqval = 650;
	   }
	   else{
	      MainDriver.DiaSP = 3; MainDriver.LengthSP = 0; MainDriver.DiaHose = 3; MainDriver.LengthHose = 0;
	      MainDriver.DiaSwivel = 3; MainDriver.LengthSwivel = 0; MainDriver.DiaKelly = 3; MainDriver.LengthKelly = 0;
	      MainDriver.Heqval = 0; MainDriver.DiaSurfLine = 3; MainDriver.LengthSurfLine = 0;
	   }
	}
}