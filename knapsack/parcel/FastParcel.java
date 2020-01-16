package knapsack.parcel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Function;

import javafx.scene.shape.Box;

import javafxstuff.Point3D;
import knapsack.Edge3D;
import knapsack.Knapsack;
import knapsack.Plane3D;
import knapsack.Size3D;

public class FastParcel implements Parcel {	
	
	/** contains all parcel rotations on all different possible locations */
	private static ArrayList<int[][]> stored_parcel_positions;
	/** contains all parcel rotations at (0,0,0) */
	private static ArrayList<ID_Labeled_ParcelCore> stored_parcel_rotations;
	private static boolean initiated = false;
	private static Knapsack reference;
	private static final int DATA_INDEX = 0;
	private static final int SHAPE_INDEX = 1;
	private static final int COORD_DATA = 0;
	private static final int PARCEL_ID_DATA = 1;
	private static final String INITIATE_ERROR = "To create FastParcel objects, the FastParcel.initiate method has to executed at least once";
	private static final String EXISTENCE_ERROR = "The requested parcel does not exist in the FastParcel database";
	private static final String POSITION_ERROR = "The requested position is out of bounds or its corresponding parcel has not properly been recorded";
	
	public static boolean isInitiated() {
		return initiated;
	}
	
	public static void default_initiation() {
		initiate(new Knapsack(), new ParcelCore[] {Parcels.A, Parcels.B, Parcels.C, Parcels.P, Parcels.L, Parcels.T});
	}
	public static void parcel_initiation() {
		initiate(new Knapsack(), new ParcelCore[] {Parcels.P, Parcels.L, Parcels.T});
	}
	public static void simple_initiation() {
		initiate(new Knapsack(), new ParcelCore[] {Parcels.A, Parcels.B, Parcels.C});
	}
	
	public static int max_parcel_ID() {
		return stored_parcel_rotations.size();
	}
	
	public static void sortByParcelFunction(Function<? super ParcelCore, Double> function) {
		Function<ID_Labeled_ParcelCore, ParcelCore> convert = a->{return a.parcel.copy();};
		Parcels.sortByFunction(stored_parcel_rotations, convert.andThen(function));
		// TODO complete sort of big list
	}
	
	public static void initiate(Knapsack reference_knapsack, ParcelCore[] basic_form_parcels) {
		if (initiated) de_initiate();
		reference = reference_knapsack.getEmpty();
		ParcelCore[] permutations = Parcels.createParcelPermutations(basic_form_parcels);
		stored_parcel_rotations = new ArrayList<ID_Labeled_ParcelCore>(permutations.length);
		stored_parcel_positions = new ArrayList<int[][]>(permutations.length*reference.getLength()*reference.getWidth()*reference.getHeight());
		for (int ID=0; ID < permutations.length; ID++) stored_parcel_rotations.add(new ID_Labeled_ParcelCore(permutations[ID],ID));
		int index = 0;
		for (int ID=0; ID < permutations.length; ID++) {
			Size3D size = permutations[ID].getHitBox();
			for (int z=0; z < reference.getHeight(); z++) {
				if (z + size.height > reference.getHeight()) break;
				for (int y=0; y < reference.getWidth(); y++) {
					if (y + size.width > reference.getWidth()) break;
					for (int x=0; x < reference.getLength(); x++) {
						if (x + size.length > reference.getLength()) break;
						if (!stored_parcel_rotations.get(ID).hasMinIndex()) stored_parcel_rotations.get(ID).setMinIndex(index);
						stored_parcel_rotations.get(ID).setMaxIndex(index);
						ParcelCore adept = permutations[ID].copy();
						adept.setOrigin(x,y,z);
						int[] shape = generateParcelShape(adept);
						int[] data = {reference.to1DCoord(x, y, z), ID};
						stored_parcel_positions.add(new int[][]{data, shape});
						index++;
					}
				}
			}
		}
		initiated = true;
	}
	
	private static void de_initiate() {
		reference = null;
		stored_parcel_rotations = null;
		stored_parcel_positions = null;
		initiated = false;
		Runtime.getRuntime().gc();
	}
	
	private static void giveError(String message) {
		@SuppressWarnings("serial")
		class InvalidParcelStorageException extends Exception { InvalidParcelStorageException(String message) {super(message);}}
		InvalidParcelStorageException e = new InvalidParcelStorageException(message);
		e.printStackTrace();
		System.exit(0);
	}
	
	public static int[] generateParcelShape(Parcel parcel) {
		Point3D[] points = parcel.getOccupiedGrids();
		int[] shape = new int[parcel.getVolume()];
		for (int i=0; i < shape.length; i++) shape[i] = reference.to1DCoord(points[i].getX(), points[i].getY(), points[i].getZ());
		return shape;
	}
	
	public int[] getParcelShape() {
		return stored_parcel_positions.get(ID)[SHAPE_INDEX];
	}
	public int getPosition() {
		return stored_parcel_positions.get(ID)[DATA_INDEX][COORD_DATA];
	}
	public int getParcelID() {
		return parcel_ID;
	}
	public int getID() {
		return ID;
	}
	
	public static int getParcelID(int ID) {
		return stored_parcel_positions.get(ID)[DATA_INDEX][PARCEL_ID_DATA];
	}
	
	private static ID_Labeled_ParcelCore getParcel(int parcel_ID) {
		return stored_parcel_rotations.get(parcel_ID);
	}
	
	private ParcelCore getParcelCore() {
		ParcelCore result = getParcel(parcel_ID).getParcel();
		result.setOrigin(reference.toPoint(getPosition()));
		return result;
	}
	private void computeSize() {
		setSize(getParcelCore().getHitBox());
	}
	private void setSize(Size3D size) {
		length = size.length;
		width = size.width;
		height = size.height;
		calculated_size = true;
	}
	
	private int ID;
	private int parcel_ID;
	private int length;
	private int width;
	private int height;
	private boolean calculated_size = false;
	
	public FastParcel(int ID) {
		this.ID = ID;
		this.parcel_ID = getParcelID(ID);
	}
	public FastParcel(int parcel_ID, Point3D origin) {
		this(parcel_ID, origin.getX(), origin.getY(), origin.getZ());
	}
	public FastParcel(int parcel_ID, int x, int y, int z) {
		this.parcel_ID = parcel_ID;
		ReferenceSize size = getParcel(parcel_ID).getRef();
		this.ID = size.getOriginID() +size.to1DCoord(x, y, z);
	}
	public FastParcel(ParcelCore parcel) {
		if (!initiated) giveError(INITIATE_ERROR);
		ParcelCore adj = parcel.copy(); adj.setOrigin(Point3D.ZERO);
		int parcel_ID = stored_parcel_rotations.indexOf(new ID_Labeled_ParcelCore(adj, -1));
		if (parcel_ID==-1) giveError(EXISTENCE_ERROR+" for "+parcel);
		ID_Labeled_ParcelCore label = stored_parcel_rotations.get(parcel_ID);
		if (!label.getRef().isContained(parcel.getOrigin())) giveError(POSITION_ERROR+" at <"+parcel.getOrigin()+">");
		this.parcel_ID = parcel_ID;
		this.ID = label.getMinIndex() + label.getRef().to1DCoord(parcel.getOrigin());
	}
	private FastParcel(int ID, int parcel_ID) {
		this.ID = ID;
		this.parcel_ID = parcel_ID;
	}
	
	public boolean isValid() {
		return ID < stored_parcel_positions.size() && parcel_ID < stored_parcel_rotations.size();
	}

	@Override
	public void moveParcel(Point3D delta) {
		ReferenceSize size = getParcel(parcel_ID).getRef();
		int delt = size.to1DCoord(delta);
		int pos = getPosition();
		if (!size.isContained(delt + pos)) giveError(POSITION_ERROR+" for adding <"+size.toPoint(getPosition())+"> and <"+delta+">");
		this.ID = size.getOriginID() + pos + delt;
	}

	@Override
	public Point3D getOrigin() {
		return reference.toPoint(getPosition());
	}

	@Override
	public void setOrigin(Point3D origin) {
		ReferenceSize size = getParcel(parcel_ID).getRef();
		int pos = size.to1DCoord(origin);
		if (!size.isContained(pos)) giveError(POSITION_ERROR+" for <"+origin+">");
		this.ID = size.getOriginID() + pos;
	}

	@Override
	public int getValue() {
		return getParcelCore().getValue();
	}

	@Override
	public Color getColor() {
		return getParcelCore().getColor();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Parcel> T copy() {
		return (T) new FastParcel(ID, parcel_ID);
	}

	@Override
	public Size3D getHitBox() {
		if (!calculated_size) computeSize();
		return new Size3D(length, width, height);
	}

	@Override
	public int getVolume() {
		if (!calculated_size) computeSize();
		return length * width * height;
	}

	@Override
	public Point3D[] getPoints() {
		return getParcelCore().getPoints();
	}

	@Override
	public Edge3D[] getEdges() {
		return getParcelCore().getEdges();
	}

	@Override
	public Plane3D[] getPlanes() {
		return getParcelCore().getPlanes();
	}

	@Override
	public int getLength() {
		if (!calculated_size) computeSize();
		return length;
	}

	@Override
	public int getWidth() {
		if (!calculated_size) computeSize();
		return width;
	}

	@Override
	public int getHeight() {
		if (!calculated_size) computeSize();
		return height;
	}

	@Override
	public Box[] toBoxes(double scale) {
		return getParcelCore().toBoxes(scale);
	}

	@Override
	public Point3D[] getOccupiedGrids() {
		int[] parcel_shape = getParcelShape();
		Point3D[] result = new Point3D[parcel_shape.length];
		for (int i=0; i < result.length; i++) result[i] = reference.toPoint(parcel_shape[i]);
		return result;
	}
	
	public String toString() {
		Point3D p = getOrigin();
		return toString_nocoord()+" at "+"("+p.getX()+", "+p.getY()+", "+p.getZ()+")";
	}
	public String toString_nocoord() {
		return "<FastParcel of "+getParcel(parcel_ID).parcel.toString_nocoord()+">";
	}
	
	private static class ID_Labeled_ParcelCore {
		private final ParcelCore parcel;
		private final int ID;
		private int min_index;
		private int max_index;
		private boolean has_min_index = false;
		private ReferenceSize ref_size;
		ID_Labeled_ParcelCore(ParcelCore parcel, int ID) { this.parcel = parcel; this.ID = ID; }
		void setMinIndex(int min_index) { this.min_index = min_index; has_min_index = true; }
		int getMinIndex() { return min_index; }
		void setMaxIndex(int max_index) { this.max_index = max_index; }
		@SuppressWarnings("unused")
		int getMaxIndex() { return max_index; }
		ParcelCore getParcel() { return parcel; }
		boolean hasMinIndex() { return has_min_index; }
		public ReferenceSize getRef() {
			if (ref_size==null) {
				Size3D size = parcel.getHitBox();
				ref_size = new ReferenceSize(reference.getLength() - size.length + 1,
						reference.getWidth() - size.width + 1, reference.getHeight() - size.height + 1, min_index);
			} return ref_size;
		}
		@Override
		public boolean equals(Object o) {
			if (o==this) return true;
			if (!(o instanceof Parcel || o instanceof ID_Labeled_ParcelCore)) return false;
			if (o instanceof ID_Labeled_ParcelCore) {
				if (this.ID < 0 || ((ID_Labeled_ParcelCore)o).ID < 0)
					return parcel.equals(((ID_Labeled_ParcelCore)o).parcel);
				return ID==((ID_Labeled_ParcelCore)o).ID;
			} return parcel.equals((Parcel)o);
		}
	}
	
	private static class ReferenceSize {
		private int length;
		private int width;
		private int height;
		private int ID_origin;
		
		public ReferenceSize(int length, int width, int height, int origin) {
			this.length = length;
			this.width = width;
			this.height = height;
			this.ID_origin = origin;
		}
		public int getOriginID() {
			return ID_origin;
		}
		public int to1DCoord(int x, int y, int z) {
			return z * width * length + y * length + x;
		}
		public int to1DCoord(Point3D point) {
			return to1DCoord(point.getX(), point.getY(), point.getZ());
		}
		public boolean isContained(int coord) {
			return isContained(reference.toPoint(coord));
		}
		public boolean isContained(Point3D point) {
			return point.getX() < length && point.getY() < width && point.getZ() < height;
		}
		
		public Point3D toPoint(int coord) {
			int z = coord / width / length;
			int y = coord / length - z * width;
			int x = coord - z * length * width - y * length;
			return new Point3D(x,y,z);
		}
	}

}
