package basic;

public class ParcelSettings {
	public int vA,vB,vC,qA,qB,qC;
	boolean steps;
	
	public ParcelSettings(int vA,int vB,int vC,int qA,int qB,int qC, boolean steps) {
		this.vA=vA;this.vB=vB;this.vC=vC;
		this.qA=qA;this.qB=qB;this.qC=qC;
		this.steps=steps;
	}
	
	public String toString() {
		String x="\t Values \t \n"+"A:\t"+vA+"\t"+qA;
		x+="\nB:\t"+vB+"\t"+qB;
		x+="\nC:\t"+vC+"\t"+qC;
		return x;
	}
}
