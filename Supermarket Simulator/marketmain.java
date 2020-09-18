import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Iterator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

public class marketmain extends JApplet implements ActionListener, ChangeListener
{
  Timer t;
  JButton customerAdd, customerRemove, registerAdd, registerRemove;
  Deque<Deque<Customer>> Lines;
  Deque<Register> registers;
  JSlider slide;
  CustomerManager customerManager;
  RegisterManager registerManager;
  Hashtable lt;
  JRadioButton multi, single;
  ButtonGroup bg;
  int it, customerYLine, registerYLine;
  JLabel spd;
  
  public void init()
  {
    lt = new Hashtable();
    lt.put(0, new JLabel("Empty.."));
    lt.put(100, new JLabel("Busy.."));
    
    t = new Timer(62, this);
    t.start();
    
    customerAdd = new JButton("Add Customer");
    customerRemove = new JButton("Remove Customer");
    registerAdd = new JButton("Add Register");
    registerRemove = new JButton("Remove Register");
    multi = new JRadioButton("Multi-Line");
    single = new JRadioButton("Single-Line");
    spd = new JLabel("Register Speed: -");
    
    bg = new ButtonGroup();
    bg.add(multi);
    bg.add(single);
    multi.setSelected(true);
    
    slide = new JSlider(0, 100);
    slide.setMinorTickSpacing(10);
    slide.setPaintTicks(true);
    slide.setLabelTable(lt);
    slide.setPaintLabels(true);
    slide.addChangeListener(this);
    slide.setSnapToTicks(true);
    
    customerAdd.addActionListener(this);
    customerRemove.addActionListener(this);
    registerAdd.addActionListener(this);
    registerRemove.addActionListener(this);
    single.addActionListener(this);
    multi.addActionListener(this);
    
    setContentPane(new drawingPanel());
    add(customerAdd);
    add(slide);
    add(registerAdd);
    add(registerRemove);
    add(spd);
    add(single);
    add(multi);
     
    customerYLine = 50;
    Lines = new LinkedList<Deque<Customer>>();
    registers = new LinkedList<Register>();
    customerManager = new CustomerManager(Lines);
    registerManager = new RegisterManager(registers, Lines);
    registerManager.push(1);
  }
  
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == t)
    {
      repaint();
      it++;
      if (it == 30)
      {
        it = 0;
        spd.setText("Register Speed: " + registerManager.getFlow());
      }
    }
    if (e.getSource() == customerAdd)
    {
      customerManager.timedOffer();
      repaint();
    }
    
    if (e.getSource() == registerAdd)
    {
      if (single.isSelected())//, faster flow
      {
        registerManager.push(0);
        registerManager.helpRate();
        spd.setText("Register Speed: " + registerManager.getFlow());
        repaint();
      }
      else if (multi.isSelected())//create a new line
      {
        registerManager.push(1);
        repaint();
      }
    }
    if (e.getSource() == registerRemove)
    {
      if (single.isSelected())
      {
       registerManager.decFlow();
       registerManager.helpRate();
       spd.setText("Register Speed: " + registerManager.getFlow());
        repaint();
      }
      else if (multi.isSelected())
      {
        registerManager.pop();
        repaint();
      }
    }
  }
  
  public void stateChanged(ChangeEvent e)
  {
    customerManager.setFlow(slide.getValue());
  }
  
  public class drawingPanel extends JPanel
  {
    public void paintComponent(Graphics g)
    {
      //repaint();
      customerYLine = 50;
      registerYLine = 50;
      for (Deque<Customer> c: Lines)
      {
        customerYLine = customerYLine + 125;
        for (Customer customer: c)
        {
          customer.setY(customerYLine);
          customer.draw(g);
        }
      for (Register reg: registers)
      {
        reg.setY(customerYLine);
        reg.draw(g);
      }      
      }   
    }
  }
}
