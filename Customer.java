import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//TREVOR SCOTT
public class Customer extends Sprite implements ActionListener
{
  Timer t;
  int x;
  int y;

  public Customer(String str, int x)
  {
    super(str);
    t = new Timer(50, this);
    this.x = x;
    t.start();
    this.y = y;
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == t)
    {
      animate();
      x++;
    }
  }
  public void setY(int newY)
  {
  	y = newY;
  }

  public void draw(Graphics g)
  {
    g.drawImage(currentImage, x, y, null);
  }
}