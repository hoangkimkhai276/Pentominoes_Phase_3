package basic;

import greedy_algorithm.SimpleStartingCode;
import knapsack.Knapsack;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.Parcels;

public class ParcelInterface {

	private int A_value = 3;
	private int B_value = 4;
	private int C_value = 5;
	private int P_value = 3;
	private int T_value = 4;
	private int L_value = 5;
	
	private int A_quantity = 5000;
	private int B_quantity = 5000;
	private int C_quantity = 5000;
	private int P_quantity = 5000;
	private int T_quantity = 5000;
	private int L_quantity = 5000;
	
	public ParcelInterface() {
		super();
	}
	public ParcelInterface(PentominoSettings default_pentos, ParcelSettings default_parcels) {
		putSettings(default_pentos);
		putSettings(default_parcels);
	}
	
	public ParcelCore[] getPentominoParcels() {
		ParcelCore[] result = new ParcelCore[3];
		result[0] =  Parcels.P.changeValue(P_value);
		result[1] =  Parcels.T.changeValue(T_value);
		result[2] =  Parcels.L.changeValue(L_value);
		return result;
	}
	public int[] getPentominoLimits() {
		return new int[]{P_quantity, T_quantity, L_quantity};
	}
	public void setPentominoLimits(int P_limit, int T_limit, int L_limit) {
		P_quantity = P_limit; T_quantity = T_limit; L_quantity = L_limit;
	}
	public void setPentominoValues(int P_value, int T_value, int L_value) {
		this.P_value = P_value; this.T_value = T_value; this.L_value = L_value;
	}
	
	public ParcelCore[] getSimpleParcels() {
		ParcelCore[] result = new ParcelCore[3];
		result[0] =  Parcels.A.changeValue(A_value);
		result[1] =  Parcels.B.changeValue(B_value);
		result[2] =  Parcels.C.changeValue(C_value);
		return result;
	}
	public int[] getSimpleLimits() {
		return new int[]{A_quantity, B_quantity, C_quantity};
	}
	public void setSimpleLimits(int A_limit, int B_limit, int C_limit) {
		A_quantity = A_limit; B_quantity = B_limit; C_quantity = C_limit;
	}
	public void setSimpleValues(int A_value, int B_value, int C_value) {
		this.A_value = A_value; this.B_value = B_value; this.C_value = C_value;
	}
	
	public void putSettings(PentominoSettings settings) {
		setPentominoValues(settings.vP, settings.vT, settings.vL);
		setPentominoLimits(settings.qP, settings.qT, settings.qL);
	}
	public void putSettings(ParcelSettings settings) {
		setSimpleValues(settings.vA, settings.vB, settings.vC);
		setSimpleLimits(settings.qC, settings.qB, settings.qC);
	}
	
	public Knapsack greedyPentominoesNotDynamic(Knapsack toSolve) {
		Knapsack copy = toSolve.getEmpty();
		greedyPentominoesDynamic(copy);
		return copy;
	}
	public void greedyPentominoesDynamic(Knapsack toSolve) {
		SimpleStartingCode.simpleStochasticGreedy(toSolve, getPentominoParcels(), getPentominoLimits());
	}
	
	public Knapsack greedySimpleNotDynamic(Knapsack toSolve) {
		Knapsack copy = toSolve.getEmpty();
		greedySimpleDynamic(copy);
		return copy;
	}
	public void greedySimpleDynamic(Knapsack toSolve) {
		SimpleStartingCode.simpleStochasticGreedy(toSolve, getSimpleParcels(), getSimpleLimits());
	}
	
}
