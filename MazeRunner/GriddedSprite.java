import java.awt.*;
import java.awt.geom.AffineTransform;

public class GriddedSprite extends Sprite
{
  public AffineTransform a;
  public int size, bx, by, row, col;
  public Box[][] list;

  public GriddedSprite(int size, int row, int col, Box[][] list, String str)
  {
    super(str);
    this.size = size;
    a = new AffineTransform();
    bx = 1;
    by = 1;
    this.list = list;
    this.row = row;
    this.col = col;
  }

  public void draw(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    a.setToScale((double)size/(double)getWidth(), (double)size/(double)getHeight());
    g2.setTransform(a);
    g2.drawImage(currentImage, (int)((double)list[bx][by].getX()/(double)a.getScaleX()), (int)((double)list[bx][by].getY()/(double)a.getScaleY()), null);
  }

  public int getHeight()
  {
    return currentImage.getHeight();
  }
  public int getWidth()
  {
    return currentImage.getWidth();
  }
  public void addBX()
  {
   if (!(row-1 == bx) && list[bx+1][by].isNotSolid(1))
     bx++;
  }
  public void addBY()
  {
    if (!(col-1 == by) && list[bx][by+1].isNotSolid(1))
      by++;
  }
  public void subBX()
  {
    if (!(bx <= 0) && list[bx-1][by].isNotSolid(1))
     bx--;
  }
  public void subBY()
  {
    if (!(by <= 0) && list[bx][by-1].isNotSolid(1))
      by--;
  }
  public int getBX()
  {
    return bx;
  }
  public int getBY()
  {
    return by;
  }
}