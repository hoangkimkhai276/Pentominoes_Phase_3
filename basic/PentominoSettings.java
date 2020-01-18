package basic;

public class PentominoSettings {
	public int vL,vP,vT,qL,qP,qT;
	boolean steps;
	
	public PentominoSettings(int vL,int vP,int vT,int qL,int qP,int qT, boolean steps) {
		this.vL=vL;this.vP=vP;this.vT=vT;
		this.qL=qL;this.qP=qP;this.qT=qT;
		this.steps=steps;
	}
	
	public String toString() {
		String x="\t Values \t \n"+"L:\t"+vL+"\t"+qL;
		x+="\nP:\t"+vP+"\t"+qP;
		x+="\nT:\t"+vT+"\t"+qT;
		return x;
	}
}
