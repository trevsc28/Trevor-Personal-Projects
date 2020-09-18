import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public class Box
{
  private int X, Y, Size, wall;
  private BufferedImage current;
  private AffineTransform a;

  public Box(Graphics g, int X, int Y, int Size, int wall, BufferedImage b)
  {
    this.Size = Size;
    this.X = X * Size;
    this.Y = Y * Size;
    this.wall = wall;
    current = b;
    a = new AffineTransform();
    this.draw(g);
  }
  public void draw(Graphics g)//0 = empty, 1, 5 = wall, 2 = start, 3 = finish
  {
    Graphics2D g2 = (Graphics2D) g;
    a.setToScale((double)Size/(double)current.getWidth(), (double)Size/(double)current.getHeight());
    g2.setTransform(a);
    g2.drawImage(current, (int)((double)X/(double)a.getScaleX()),(int)((double)Y/(double)a.getScaleY()), null);
  }
  public int getX()
  {
    return X;
  }
  public int getY()
  {
    return Y;
  }
  public boolean isNotSolid(int x)
  {
    if (x == 0)
    {
   		 if (wall == 1 || wall == 5)
     		 return false;
   		 else
     		 return true;
    }
    else if (x == 1)
    {
   		 if (wall == 1)
     		 return false;
   		 else
     		 return true;
	}
	else
		return true;
  }
  public boolean isFinishFlag()
  {
    if (wall == 3)
      return true;
    else
      return false;
  }
  public int getType()
  {
    return wall;
  }
}