import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Register
{
  BufferedImage image;
  int y;
 
  public Register(String str)
  {
    try
    {
      image = ImageIO.read(getClass().getResource(str));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    this.y = y;
  }

  public void setY(int newY)
  {
  	y = newY;
  }

  public void draw(Graphics g)
  {
    g.drawImage(image, 800, y, null);
  }
}
