package knapsack;

import knapsack.parcel.Parcel;

class TempSolution extends Knapsack {
	private int value = -1;
	
	@Override
	public void add(Parcel to_add) {
		super.add(to_add);
		addValue(to_add.getValue());
	}
	private void addValue(int value) {
		if (this.value==-1) value = 0;
		this.value += value;
	}
	public int getValue() {
		parcels.forEach(p->addValue(p.getValue()));
		return value;
	}
	public int getFilledVolume() {
		return occupied_cubes.bitCount();
	}
	public int getEmptyVolume() {
		return getVolume() - getFilledVolume();
	}
}