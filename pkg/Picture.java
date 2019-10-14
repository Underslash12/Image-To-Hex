// HIDE
package pkg;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

/**
 * A picture from an image file.
 */
public class Picture
{
    private BufferedImage image;
    private JLabel label = new JLabel();    
    private String source;
    private double x;
    private double y;

    /**
     * Constructs a picture with no image.
     */
    public Picture()
    {
    }

    /**
     * Constructs a picture with a given width and height.
     * @param width the desired width
     * @param height the desired height
    */
	public Picture(double width, double height)
	{
		image = new BufferedImage((int) Math.round(width), 
			(int) Math.round(height), BufferedImage.TYPE_INT_RGB);
		label.setIcon(new ImageIcon(image));
		label.setText("");      
	}
   
	/**
     * Constructs a picture from a BufferedImage.
	 * @param b the BufferedImage input
     */
    public Picture(BufferedImage b)
    {
		image = b;
    }

    /**
     * Constructs an image from a given file or URL.
     * @param source the filename or URL
     */
    public Picture(String source)
    {
        load(source);
    }

    /**
     * Loads a new image from a given file or URL.
     * @param source the filename or URL
     */
    public void load(String source)
    {
        try
        {
            this.source = source;
            if (source.startsWith("http://"))
                image = ImageIO.read(new URL(source).openStream());
            else
                image = ImageIO.read(new File(source));

            label.setIcon(new ImageIcon(image));
            label.setText("");
        }
        catch (Exception ex)
        {
            image = null;
            label.setIcon(null);
            ex.printStackTrace();
        }
        // Canvas.getInstance().repaint();
    }
	
	/**
	 * Writes an image to a file
	 * @param fileFormat type of file (ex: "jpg")
	 * @param filename name of the output file
	*/
	public void write(String fileFormat, String filename)
	{
		try {
			File out = new File(filename);
			try {
				ImageIO.write(image, fileFormat, out);
			} catch (Exception e) { System.out.println(e); }
		} catch (Exception e) { System.out.println(e); }
	}

    /**
     * Gets the width of this picture.
     */
    public int getWidth()
    {
       return (int) Math.round(
          (image == null ? 0 : image.getWidth()));
    }

    /**
     * Gets the height of this picture.
     */
    public int getHeight()
    {
       return (int) Math.round(
          (image == null ? 0 : image.getHeight()));
    }
	
    public String toString()
    {
       return "Picture[width=" + getWidth() + ",height=" + getHeight() + ",source=" + source + "]";
    }

    /**
     * Gets the color of a pixel.
     * @param x the x-coordinate (column) of the pixel
     * @param y the y-coordinate (row) of the pixel
     * @param color the new color for the pixel
     */
    public Color getColorAt(int x, int y)
    {
        if (image == null || x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
        {
           throw new IndexOutOfBoundsException("(" + x + "," + y + ")");
        }
        else
        {
            int rgb = image.getRGB(x, y) & 0xFFFFFF;
            return new Color(rgb / 65536, (rgb / 256) % 256, rgb % 256);
        }
    }

    /**
     * Sets the color of a pixel.
     * @param x the x-coordinate (column) of the pixel
     * @param y the y-coordinate (row) of the pixel
     * @param the color of the pixel at the given row and column
     */
    public void setColorAt(int x, int y, Color color)
    {
        if (image == null || x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
        {
           throw new IndexOutOfBoundsException("(" + x + "," + y + ")");
        }
        else
        {
            image.setRGB(x, y, ((int) color.getRed()) * 65536 + ((int) color.getGreen()) * 256 + (int) color.getBlue());
            // Canvas.getInstance().repaint();
        }
    }
	
	/**
	 * Returns a scaled output
	 * @param scale scale factor
	*/
	public Picture scale(double scale)
	{
		// Image img = image.getScaledImage(image.getWidth() * scale, image.getHeight() * scale, Image.SCALE_FAST);
		BufferedImage bi = new BufferedImage((int)(scale * (image.getWidth(null))), (int)(scale * (image.getHeight(null))), BufferedImage.TYPE_INT_RGB);

		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(scale, scale);

		grph.drawImage(image, 0, 0, null);
		grph.dispose();
		
		return new Picture(bi);
	}
}
