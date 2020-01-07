package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Colors {
	
	public static double minimal_color_distance = 80d;
	
	public static Color randomColor(float min_brightness, float transparency) {
		Color result = new Color(0,0,0);
		int desired_brightness = (int)(255 * 2 * min_brightness);
		int total_brightness = 0;
		while (total_brightness < desired_brightness) {
			double choice = Math.random() * 3;
			int red = result.getRed(), green = result.getGreen(), blue = result.getBlue();
			if (choice > 2) 	 red   = result.getRed() + (int)(Math.random() * minimal_color_distance);
			else if (choice > 1) green = result.getGreen() + (int)(Math.random() * minimal_color_distance);
			else 				 blue  = result.getBlue() + (int)(Math.random() * minimal_color_distance);
			if (red > 255) red = 255;
			if (green > 255) green = 255;
			if (blue > 255) blue = 255;
			result = new Color(red, green, blue);
			total_brightness = result.getRed() + result.getGreen() + result.getBlue();
		} return new Color(result.getRed(), result.getGreen(), result.getBlue(), (int)(255 - 255*transparency));
	}
	
	public static Color[] getRandomColors(int amount, float brightness, float transparency) {
		return getRandomColors(amount, brightness, transparency, false);
	}
	
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
	
	public static void main(String[] args) {
		
		int count = 20;
		float brightness = 0.8f;
		float transparency = 0f;
		boolean sorted = false;
		minimal_color_distance = 80;
		
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
