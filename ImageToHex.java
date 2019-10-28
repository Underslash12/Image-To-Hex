import pkg.*;
import java.util.ArrayList;
import java.io.File;

public class ImageToHex{

	double size;
	double scale;

	Picture input;
	Picture output;
	
	Hexagon[][] hexPixels;
	int[][][] pixels;
	
	ArrayList<Color>[][] colors;

	public ImageToHex(String inputfilename, String outputfilename, double size, double scale)
	{
		input = new Picture(inputfilename);
		
		this.size = size;
		this.scale = scale;
		
		input = input.scale(scale);
		
		output = new Picture(input.getWidth(), input.getHeight());
		pixels = new int[input.getHeight()][input.getWidth()][2];
		
		// create hexagon array 
		// maybe possible to remove
		hexPixels = new Hexagon[
			(int)(input.getHeight() / (Math.sqrt(3) * size / 2) + 2)
		][
			(int)(input.getWidth() / (size * 3) + 1)
		];
		
		// all needed methods
		createHexPixels();
		createColors();
		assignHexToPixels();
		avgColors();
		setAvg();
		
		// Generates a unique filename
		int i = 0;
		while (true) {
			String temp = outputfilename;
			if (i > 0)
				temp += Integer.toString(i);
			File f = new File(temp + ".png");
			i += 1;
			
			if (!f.exists()) {
				outputfilename = temp;
				break;
			}
		}

		if (outputfilename.substring(outputfilename.length() - 4, outputfilename.length()).equals(".png")) 
			output.write("png", outputfilename);
		else
			output.write("png", outputfilename + ".png");
	}
	
	
	// fills hexagon array
	private void createHexPixels()
	{
		for (int i = 0; i < hexPixels.length; i++) {
			for (int j = 0; j < hexPixels[0].length; j++) {
				double x = size * 0.5 + j * size * 3;
				if (i % 2 == 1)
					x += 1.5 * size;
				// x += size * 3;
				hexPixels[i][j] = new Hexagon(x, i * Math.sqrt(3) * size / 2, size);
			}
		}
	}
	
	
	// makes color arraylist
	private void createColors()
	{
		colors = new ArrayList[hexPixels.length][hexPixels[0].length];
		for (int i = 0; i < hexPixels.length; i++) {
			for (int j = 0; j < hexPixels[0].length; j++) {
				colors[i][j] = new ArrayList<Color>();
			}
		}
	}
	
	
	// checks which hexagon a pixel is in
	private void assignHexToPixels()
	{
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
								colors[ih][jh].add(input.getColorAt(jp, ip));
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
	}
	
	
	// avgs the colors in all specific hexagons
	private void avgColors()
	{
		/** Working */
		// Avgs colors
		// Color[][] avgColor = new Color[hexPixels.length][hexPixels[0].length];
		for (int i = 0; i < hexPixels.length; i++) {
			for (int j = 0; j < hexPixels[0].length; j++) {
				hexPixels[i][j].setColor(avgColor(colors[i][j]));
			}
		}
	}
	
	
	// sets output pixels to their corresponding avgcolor
	private void setAvg()
	{
		// sets colors of image to these
		for (int i = 0; i < output.getHeight(); i++) {
			for (int j = 0; j < output.getWidth(); j++) {
				output.setColorAt(j, i, hexPixels[pixels[i][j][0]][pixels[i][j][1]].getColor());
			}
		}
	}
	
	
	// averages a list of colors
	private Color avgColor(ArrayList<Color> c)
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

