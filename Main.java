import pkg.*;
import java.util.ArrayList;
import java.io.File;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String... args)
	{
		Picture image;
		String filename = (String)JOptionPane.showInputDialog(null, "Filename: ", "File Input", JOptionPane.PLAIN_MESSAGE);
		
		try {
			image = new Picture(filename);
		} catch (Exception e) {
			System.out.println(e);
			return;
		}
		
		double scale = Double.parseDouble((String)JOptionPane.showInputDialog(null,
			String.format("Scale Factor: \n    Original Dimensions: [%1$d, %2$d]", image.getWidth(), image.getHeight()), 
			"Scale Input", JOptionPane.PLAIN_MESSAGE, null, null, 1));
			
		double size = Double.parseDouble((String)JOptionPane.showInputDialog(null, 
			// "Size of Hexagons (radius): ", 
			String.format("Size of Hexagons (radius): \n    Current Dimensions: [%1$d, %2$d]", (int)(image.getWidth() * scale), (int)(image.getHeight() * scale)), 
			"Size Input", JOptionPane.PLAIN_MESSAGE));
			
		ImageToHex ih = new ImageToHex(filename, filename, size, scale);
	}
	
}