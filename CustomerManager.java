import java.util.Deque;
import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;
//TREVOR SCOTT
public class CustomerManager implements ActionListener
{
  Deque<Deque<Customer>>  lineManager;
  Timer t;
  int y, MIN_SIZE = 10000, finalTemp = 0;
  Deque<Customer> tempLine;
  Random r;
  final int startX = 40;
  
  public CustomerManager(Deque<Deque<Customer>> lineManager)
  {
    this.lineManager = lineManager;
    t = new Timer(1500, this);
    t.start();
    y = 500;
    r = new Random();
  }
  
  public void timedOffer()
  {
    MIN_SIZE = 10000;
    for (Deque<Customer> c: lineManager)
    {
      if (c.size() <= MIN_SIZE)
      {
        MIN_SIZE = c.size();
        tempLine = c;//GETS THE LINE WITH THE SMALLEST SIZE
      }
    }
    tempLine.offer(new Customer("smurf_sprite.png", startX));
  }
  
  public void poll()
  {
    lineManager.pollFirst();
  }
  public void setFlow(int flow)//flow customers are added to the shortest line
  {
    if (flow == 0)
      t.stop();
    else if (flow > 0 && flow < 50)
    {
      t.start();
      t.setDelay(70*(110-flow));
    }
    else
    {
      t.start();
      t.setDelay(90*(110-flow));
    }
  }
  
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == t)
      timedOffer();//periodically add a customer to the shortest line
  }
}