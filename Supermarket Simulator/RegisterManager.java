import java.util.Deque;
import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.awt.*;

public class RegisterManager implements ActionListener
{
  Deque registers, temp, newLine;
  int registerY = 0, MAX_SIZE = 0, lineY = 50, startX = 100, MIN_SIZE = 10000, rFlow = 2;
  Deque<Deque<Customer>> lineManager;
  Register newReg;
  Timer t;
  
  public RegisterManager(Deque registers,  Deque<Deque<Customer>>  lineManager)
  {
    this.registers = registers;
    this.lineManager = lineManager;
    t = new Timer(4000, this);
    t.start();
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == t)
    {
      manageCustomers();
    }
  }
  public void helpRate()
  {
    t.setDelay(10000/rFlow);
  }

  public void manageCustomers()
  {
    for (Deque<Customer> c: lineManager)
    {
      c.poll();
    }
  }

  public int getFlow()
  {
    return rFlow;
  }
  public void decFlow()
  {
    if (rFlow > 1)
      rFlow--;
  }
  
  public void push(int num)
  {
    newReg = new Register("ccashmoney.png");
    registers.push(newReg);
    if (num == 1)//MULTI-LINE PUSH
    {
      for (Deque<Customer> c: lineManager)//GETS MOST POPULATED LINE
      {
        if (c.size() > MAX_SIZE)
        {
          MAX_SIZE = c.size();
          temp = c;
        }
      }
      newLine = new LinkedList<Customer>();

      for (int i = 0; i < MAX_SIZE/2; i++)//TAKES HALF OFF POPULATED LINE, ADDS HALF TO NEW LINE
      {
        temp.poll();
        newLine.offer(new Customer("smurf_sprite.png", startX));
        startX = startX - 15;
      }
      lineManager.offer(newLine);      

      //RESET ALL VARIABLES
      MAX_SIZE = 0;
      startX = 50;
    }
    else if (num == 0)//SINGLE-LINE PUSH
    {
      //RegisterManager.setflow(currentFlow + x amount)
      rFlow++;
    }
  }
  
  public void pop()
  {
    if (registers.size() > 1)//INBOUNDS
    {
      registers.pop();
      if (lineManager.size() > 1)
      {
        for (Deque<Customer> c: lineManager)//gets the line/size of line with the most people;
        {
          if (c.size() <= MIN_SIZE)//if the current line is smaller than the smallest one
          {
            MIN_SIZE = c.size();
            temp = c;
          }
        }
        int sizeUp = lineManager.peek().size();
        lineManager.poll();
        for (int i = 0; i < sizeUp; i++)//adds half of the old to the new line
        {
          temp.offer(new Customer("smurf_sprite.png", startX));
          startX = startX - 10;
        }
        
        MIN_SIZE = 10000;
        startX = 100;
      }
    }
  }  
}
