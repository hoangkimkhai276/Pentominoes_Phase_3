package basic;

import depth_first_fill.MiniSimpleParcel;
import greedy_algorithm.SimpleStartingCode;
import knapsack.Knapsack;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.Parcels;

import java.util.Arrays;

public class ParcelInterface {

    private int A_value = 3;
    private int B_value = 4;
    private int C_value = 5;
    private int P_value = 3;
    private int T_value = 4;
    private int L_value = 5;

    public int A_quantity_used = 0;
	public int B_quantity_used = 0;
	public int C_quantity_used = 0;
	public int P_quantity_used = 0;
	public int T_quantity_used = 0;
	public int L_quantity_used = 0;

    private int A_quantity = 5000;
    private int B_quantity = 5000;
    private int C_quantity = 5000;
    private int P_quantity = 5000;
    private int T_quantity = 5000;
    private int L_quantity = 5000;

    private boolean pentoMode = true;

    public ParcelInterface() {
        super();
    }

    public ParcelInterface(PentominoSettings default_pentos, ParcelSettings default_parcels, boolean pentoMode) {
        putSettings(default_pentos);
        putSettings(default_parcels);
        this.pentoMode = pentoMode;
    }

    public MiniSimpleParcel[] getMiniPentominoParcels() {
        MiniSimpleParcel[] result = new MiniSimpleParcel[3];
        result[0] = MiniSimpleParcel.LL.changeValue(L_value * 2);
        result[1] = MiniSimpleParcel.PTP.changeValue(T_value + P_value * 2);
        result[2] = MiniSimpleParcel.PP.changeValue(P_value * 2);
        return result;
    }
    public MiniSimpleParcel[] getMiniSimpleParcels() {
        MiniSimpleParcel[] result = new MiniSimpleParcel[3];
        result[0] = MiniSimpleParcel.A.changeValue(A_value);
        result[1] = MiniSimpleParcel.B.changeValue(B_value);
        result[2] = MiniSimpleParcel.C.changeValue(C_value);
        return result;
    }

    public ParcelCore[] getPentominoParcels() {
        ParcelCore[] result = new ParcelCore[3];
        result[0] = Parcels.P.changeValue(P_value);
        result[1] = Parcels.T.changeValue(T_value);
        result[2] = Parcels.L.changeValue(L_value);
        return result;
    }

    public int[] getPentominoLimits() {
        return new int[]{P_quantity, T_quantity, L_quantity};
    }

    public void setPentominoLimits(int P_limit, int T_limit, int L_limit) {
        P_quantity = P_limit;
        T_quantity = T_limit;
        L_quantity = L_limit;
    }

    public void setPentominoValues(int P_value, int T_value, int L_value) {
        this.P_value = P_value;
        this.T_value = T_value;
        this.L_value = L_value;
    }

    public ParcelCore[] getSimpleParcels() {
        ParcelCore[] result = new ParcelCore[3];
        result[0] = Parcels.A.changeValue(A_value);
        result[1] = Parcels.B.changeValue(B_value);
        result[2] = Parcels.C.changeValue(C_value);
        return result;
    }

    public int[] getSimpleLimits() {
        return new int[]{A_quantity, B_quantity, C_quantity};
    }

    public void setSimpleLimits(int A_limit, int B_limit, int C_limit) {
        A_quantity = A_limit;
        B_quantity = B_limit;
        C_quantity = C_limit;
    }

    public void setSimpleValues(int A_value, int B_value, int C_value) {
        this.A_value = A_value;
        this.B_value = B_value;
        this.C_value = C_value;
    }

    public int[] getTotalLimits() {
        return new int[]{A_quantity, B_quantity, C_quantity, P_quantity, T_quantity, L_quantity};
    }

    public Knapsack augmentedPentominoes(Knapsack toSolve) {
    	return augmentedPentominoesDynamic(toSolve.getEmpty());
    }
    
    public Knapsack augmentedPentominoesDynamic(Knapsack toSolve) {
		int[] limits = getTotalLimits();
        int[] used = getTotalLimits();
		MiniSimpleParcel result = MiniSimpleParcel.maximizeKnapsackValue(MiniSimpleParcel.getFromKnapsack(toSolve), getMiniPentominoParcels(), used);
		result.adjustLimits(used);
		result.putInKnapsack(toSolve);
		calculateUsed(limits, used);
        P_quantity_used = used[3];
		T_quantity_used = used[4];
		L_quantity_used = used[5];
		return toSolve;
    }
    
    public Knapsack greedyPentominoes(Knapsack toSolve) {
        return greedyPentominoesDynamic(toSolve.getEmpty());
    }

    public Knapsack greedyPentominoesDynamic(Knapsack toSolve) {
        int[] limits = getPentominoLimits();
        int[] used = getPentominoLimits();
        SimpleStartingCode.simpleStochasticGreedy(toSolve, getPentominoParcels(), used);
        calculateUsed(limits, used);
        P_quantity_used = used[0];
		T_quantity_used = used[1];
		L_quantity_used = used[2];
		return toSolve;
    }

    public Knapsack augmentedSimple(Knapsack toSolve) {
    	return augmentedSimpleDynamic(toSolve.getEmpty());
    }
    
    public Knapsack augmentedSimpleDynamic(Knapsack toSolve) {
		int[] limits = getTotalLimits();
        int[] used = getTotalLimits();
		MiniSimpleParcel result = MiniSimpleParcel.maximizeKnapsackValue(MiniSimpleParcel.getFromKnapsack(toSolve),getMiniSimpleParcels(), used);
		result.adjustLimits(used);
		result.putInKnapsack(toSolve);
		calculateUsed(limits, used);
        A_quantity_used = used[0];
		B_quantity_used = used[1];
		C_quantity_used = used[2];
		return toSolve;
    }
    
    public Knapsack greedySimple(Knapsack toSolve) {
        return greedySimpleDynamic(toSolve.getEmpty());
    }

    public Knapsack greedySimpleDynamic(Knapsack toSolve) {
        int[] limits = getSimpleLimits();
        int[] used = getSimpleLimits();
        SimpleStartingCode.simpleStochasticGreedy(toSolve, getSimpleParcels(), used);
        calculateUsed(limits, used);
		A_quantity_used = used[0];
		B_quantity_used = used[1];
		C_quantity_used = used[2];
        return toSolve;
    }

    public void calculateUsed(int[] limits, int[] used) {
        for (int i = 0; i < used.length; i++) {
            used[i] = limits[i] - used[i];
        }
    }

    public void putSettings(PentominoSettings settings) {
        setPentominoValues(settings.vP, settings.vT, settings.vL);
        setPentominoLimits(settings.qP, settings.qT, settings.qL);
        pentoMode = true;
    }

    public void putSettings(ParcelSettings settings) {
        setSimpleValues(settings.vA, settings.vB, settings.vC);
        setSimpleLimits(settings.qA, settings.qB, settings.qC);
        pentoMode = false;
    }

    public Knapsack greedy(Knapsack input) {
        if (pentoMode) return greedyPentominoes(input);
        return greedySimple(input);
    }
    
    public Knapsack augmented(Knapsack input) {
    	if (pentoMode) return augmentedPentominoes(input);
    	return augmentedSimple(input);
    }
    
}
