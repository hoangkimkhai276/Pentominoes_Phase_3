package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * This class provides methods for generating colors for use in the visual representation of the knapsack problem.
 *
 * @author anema
 */
public final class Colors {

	/** The minimal distance in bits two colors need to be from each other
	 *  in order to be considered as two seperate colors.<br>This is used when generating multiple random colors at once
	 *  and makes it so that the generated colors differ more from each other than normal randomly generated colors would be.
	 *  <br><br>this value should be at least around 10 and at most around 500 (the theoretically most distant two colors can be is
	 *  765: black (0,0,0) to white (255,255,255))<br><br>By default, {@code minimal_color_distance} equals {@code 80}*/
	public static int minimal_color_distance = 80;

	/**
	 * Generates a random color based on the {@link #minimal_color_distance} as step_size per R, G or B value to gradually move towards the specified
	 * minimal brightness. The color also gets the specified transparency after generating the RGB values.
	 * @param min_brightness the minimal brightness a generated color should be, with the brightness of a color being calculated as the
	 *   sum of all RGB integer values divided by {@code (255x2)}, thereby giving a maximum brightness of {@code 765/520 = 1.5f}
	 * @param transparency is equal to {@code 1.0f} minus the {@code alpha} value of the color to be generated
	 * @return a random color generated using the specified criteria
	 */
	public static Color randomColor(float min_brightness, float transparency) {
		Color result = new Color(0,0,0);
		int desired_brightness = (int)(255 * 2 * min_brightness);
		int total_brightness = 0;
		while (total_brightness < desired_brightness) {
			double choice = Math.random() * 3;
			int red = result.getRed(), green = result.getGreen(), blue = result.getBlue();
			if (choice > 2) 	 red   = result.getRed() + (int)(Math.random() * minimal_color_distance)+1;
			else if (choice > 1) green = result.getGreen() + (int)(Math.random() * minimal_color_distance)+1;
			else 				 blue  = result.getBlue() + (int)(Math.random() * minimal_color_distance)+1;
			if (red > 255) red = 255;
			if (green > 255) green = 255;
			if (blue > 255) blue = 255;
			result = new Color(red, green, blue);
			total_brightness = result.getRed() + result.getGreen() + result.getBlue();
		} return new Color(result.getRed(), result.getGreen(), result.getBlue(), (int)(255 - 255*transparency));
	}

	/**
	 * Generates a certain amount of random colors using the {@link #randomColor} method while keeping to
	 *  the {@link #minimal_color_distance} between each color.
	 * @see #getRandomColors(int, float, float, boolean)
	 */
	public static Color[] getRandomColors(int amount, float brightness, float transparency) {
		return getRandomColors(amount, brightness, transparency, false);
	}

	/**
	 * Generates a certain amount of random colors using the {@link #randomColor} method while keeping to
	 *  the {@link #minimal_color_distance} between each color.<br>It can also apply a rudimentary sorting algorithm to the resulting colors.
	 * @param amount amount of colors to be generated; could generate less if no color can be found that is the right
	 *  distance away from the other generated colors.
	 * @param brightness minimal brightness value for the colors generated as specified by the {@code Colors.randomColor} method
	 * @param transparency the transparency value given to the generated colors as specified by the {@code Colors.randomColor} method
	 * @param sorted specifies whether the resulting array of colors should be sorted or not
	 * @return An array of colors, generated randomly using the specified criteria (length of the array is equal to the amount of colors
	 * generated and thus not always equal to {@code amount})
	 */
	public static Color[] getRandomColors(int amount, float brightness, float transparency, boolean sorted) {
		class CompColor {
			public Color color;
			public CompColor(Color color) { this.color = color; }
			public boolean equals(Object o) {
				if (o==this) return true;
				if (!(o instanceof CompColor)) return false;
				CompColor other = (CompColor)o;
				return distance(other.color) <= minimal_color_distance;
			}
			double distance(Color other) {
				return Math.sqrt(Math.pow(color.getRed()-other.getRed(), 2)
						+ Math.pow(color.getGreen()-other.getGreen(), 2) + Math.pow(color.getBlue()-other.getBlue(), 2));
			}
			double distance() {
				double brightness = distance(Color.BLACK);
				return brightness * (distance(Color.RED) - distance(Color.GREEN));
			}
		}
		ArrayList<CompColor> colors = new ArrayList<CompColor>(amount);
		long start_time = System.currentTimeMillis();
		long delta = 0;
		while (colors.size() < amount && delta < 150) {
			CompColor comp = new CompColor(randomColor(brightness, transparency));
			if (!colors.contains(comp)) colors.add(comp);
			delta = System.currentTimeMillis() - start_time;
		}
		if (sorted) colors.sort((a,b)->{
			if (a.distance() > b.distance()) return 1;
			if (a.distance() == b.distance()) return 0;
			return -1;
		});
		Color[] result = new Color[colors.size()];
		for (int i=0; i < colors.size(); i++) result[i] = colors.get(i).color;
		return result;
	}

	/**
	 * Showcases a little GUI that shows randomly generated colors in a grid pattern<br>
	 * Clicking with the mouse re-generates the colors.
	 * @param args can be used to specify the criteria for generating colors:<blockquote>
	 * <b>#1</b>) ({@code int}) {@code count}<br>
	 * <b>#2</b>) ({@code float}) {@code brightness}<br>
	 * <b>#3</b>) ({@code float}) {@code transparency}<br>
	 * <b>#4</b>) ({@code boolean}) {@code sorted}<br>
	 * <b>#5</b>) ({@code int}) {@link #minimal_color_distance}
	 * </blockquote>They default to: ({@code 10 0.8 0.0 false 80})
	 * @see #getRandomColors(int, float, float, boolean)
	 */
	public static void main(String[] args) {
		int count;
		float brightness, transparency;
		boolean sorted;
		
		if (args.length >= 1) count = Integer.parseInt(args[0]);
		else count = 10;
		if (args.length >= 2) brightness = (float)Double.parseDouble(args[1]);
		else brightness = 0.8f;
		if (args.length >= 3) transparency = (float)Double.parseDouble(args[2]);
		else transparency = 0f;
		if (args.length >= 4) sorted = Boolean.parseBoolean(args[3]);
		else sorted = false;
		if (args.length >= 5) try {
			minimal_color_distance = Integer.parseInt(args[4]);
		} catch (Exception e) {}

		Color[] colors = getRandomColors(count, brightness, transparency, sorted);
		JFrame frame = new JFrame("colors!!!");
		frame.setSize(500,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new GridLayout(5, 5));
		for (Color c : colors) {
			JPanel j = new JPanel();
			j.setBackground(c);
			panel.add(j);
		}
		frame.add(panel);
		frame.setVisible(true);
		frame.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Color[] colors = getRandomColors(count, brightness, transparency, sorted);
				Component[] comps = panel.getComponents();
				for (int i=0; i < comps.length; i++) {
					try { comps[i].setBackground(colors[i]); }
					catch (Exception ex) {
						comps[i].setVisible(false);
						panel.remove(comps[i]);
						panel.add(new JPanel());
					}
				}
			}
		});
	}

}
