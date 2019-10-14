import pkg.*;
import java.util.ArrayList;
import java.io.File;

import javax.swing.JOptionPane;

public class ImageToHex{

	public static void main(String args[])
	{
		Picture image;
		String filename = (String)JOptionPane.showInputDialog(null, "Filename: ", "File Input", JOptionPane.PLAIN_MESSAGE);
		try {
			image = new Picture(filename);
		} catch (Exception e) { return; }
		
		double scale = Double.parseDouble((String)JOptionPane.showInputDialog(null,
			String.format("Scale Factor: \n    Original Dimensions: [%1$d, %2$d]", image.getWidth(), image.getHeight()), 
			"Scale Input", JOptionPane.PLAIN_MESSAGE, null, null, 1));
			
		double size = Double.parseDouble((String)JOptionPane.showInputDialog(null, 
			// "Size of Hexagons (radius): ", 
			String.format("Size of Hexagons (radius): \n    Current Dimensions: [%1$d, %2$d]", (int)(image.getWidth() * scale), (int)(image.getHeight() * scale)), 
			"Size Input", JOptionPane.PLAIN_MESSAGE));
			
		// size *= scale;
		// System.out.println(image);
		image = image.scale(scale);
		// System.out.println(image);
		
		// System.out.println(image.getColorAt(7682, 0));
		// if (true)
			// return;
		
		Picture output = new Picture(image.getWidth(), image.getHeight());
		int[][][] pixels = new int[image.getHeight()][image.getWidth()][2];
		
		
		// create hexagon array 
		// maybe possible to remove
		// double size = 5; // good for under 1000x1000
		// double size = 8; // smaller in between
		// double size = 12; // anything in between
		// double size = 20; // good for 4k
		// double size = 30; // good for 8k
		// double size = 50; // good for 69k
		Hexagon[][] hexPixels = new Hexagon[
			(int)(image.getHeight() / (Math.sqrt(3) * size / 2) + 2)
		][
			(int)(image.getWidth() / (size * 3) + 1)
		];
		
		// fills hexagon array and makes color arrayLIST
		ArrayList<Color>[][] colors = new ArrayList[hexPixels.length][hexPixels[0].length];
		for (int i = 0; i < hexPixels.length; i++) {
			for (int j = 0; j < hexPixels[0].length; j++) {
				double x = size * 0.5 + j * size * 3;
				if (i % 2 == 1)
					x += 1.5 * size;
				// x += size * 3;
				hexPixels[i][j] = new Hexagon(x, i * Math.sqrt(3) * size / 2, size);
				
				colors[i][j] = new ArrayList<Color>();
			}
		}
		
		// checks which hexagon a pixel is in
		// ip and jp iterate through pixels
		for (int ip = 0; ip < pixels.length; ip++) {
			if (ip % 10 == 0) 
				System.out.println("Row: " + ip);
			for (int jp = 0; jp < pixels[0].length; jp++) {
				// System.out.println(ip + " | " + jp);
				// ih and jh iterate through hexagons
				// once found, it breaks out
				// only iterates through about 4 next to the actual one
				hexIter:
				for (int ih = (int)(ip / (Math.sqrt(3) * size / 2) - 2); ih < (int)(ip / (Math.sqrt(3) * size / 2) + 2); ih++) {
					if (ih >= hexPixels.length || ih < 0)
						continue;
				// for (int ih = 0; ih < hexPixels.length; ih++) {
					for (int jh = (int)(jp / (size * 3) - 1); jh < (int)(jp / (size * 3) + 2); jh++) {
						if (jh >= hexPixels[0].length || jh < 0)
							continue;
						if (hexPixels[ih][jh].contains(jp, ip)) {
							pixels[ip][jp][0] = ih;
							pixels[ip][jp][1] = jh;
							try {
								colors[ih][jh].add(image.getColorAt(jp, ip));
							}
							catch (Exception e) {
								System.out.println(e);
							}
							break hexIter;
						}
					}
				}
			}
		}
		
		/** Working */
		// Avgs colors
		// Color[][] avgColor = new Color[hexPixels.length][hexPixels[0].length];
		for (int i = 0; i < hexPixels.length; i++) {
			for (int j = 0; j < hexPixels[0].length; j++) {
				hexPixels[i][j].setColor(avgColor(colors[i][j]));
			}
		}
				
		// sets colors of image to these
		for (int i = 0; i < output.getHeight(); i++) {
			for (int j = 0; j < output.getWidth(); j++) {
				output.setColorAt(j, i, hexPixels[pixels[i][j][0]][pixels[i][j][1]].getColor());
			}
		}
		
		
		// Generates a unique filename
		String output_filename = "output";
		int i = 0;
		while (true) {
			String temp = output_filename;
			if (i > 0)
				temp += Integer.toString(i);
			File f = new File(temp + ".png");
			i += 1;
			
			if (!f.exists()) {
				output_filename = temp;
				break;
			}
		}
		output.write("png", output_filename + ".png");
	}
	
	public static Color avgColor(ArrayList<Color> c)
	{
		if (c.size() > 0) {
			int r = 0, g = 0, b = 0;
			for (int i = 0; i < c.size(); i++) {
				r += c.get(i).getRed();
				g += c.get(i).getGreen();
				b += c.get(i).getBlue();
			}
			return new Color(r / c.size(), g / c.size(), b / c.size());
		}
		return Color.WHITE;
	}
}

