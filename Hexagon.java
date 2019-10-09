import java.awt.geom.Path2D;
// import java.awt.geom.Path2D.Double;

public class Hexagon {
	
	private Path2D.Double pd = new Path2D.Double();
	private double[] x;
	private double[] y;
	
	public Hexagon (double x_offset, double y_offset, double size)
	{
		x = new double[] {
			x_offset - size,
			x_offset - size / 2,
			x_offset + size / 2,
			x_offset + size,
			x_offset + size / 2,
			x_offset - size / 2
		};
		y = new double[] {
			y_offset,
			y_offset + size * Math.sqrt(3) / 2,
			y_offset + size * Math.sqrt(3) / 2,
			y_offset,
			y_offset - size * Math.sqrt(3) / 2,
			y_offset - size * Math.sqrt(3) / 2
		};
		
		pd.moveTo(x[0], y[0]);
		for (int i = 1; i < 6; i++) {
			pd.lineTo(x[i], y[i]);
		}
		pd.closePath();
	}		
		
	public boolean contains (double x, double y)
	{
		return pd.contains(x, y);
	}
	
	public String toString ()
	{
		String s = "Hexagon {\n";
		for (int i = 0; i < 6; i++) {
			s += "    [x : " + x[i] + ", y : " + y[i] + "]\n";
		}
		s += "}";
		return s;
	}
}