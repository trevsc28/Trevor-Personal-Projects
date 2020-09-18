import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Main extends JApplet implements ActionListener
{
  private Grid gr;
  private Robot rand, memory, righthand;
  private Timer t, movement;
  private int row, col, size, appSize;
  private boolean draw;
  private Box[][] list;
  private int[][] gridX;
  private MazeReader mr;
  private ArrayList<Robot> griddedRobots;

  public void init()
  {
    //Set up JApplet
    appSize = 800;
    draw = false;
    t = new Timer(82, this);
    movement = new Timer(50, this);
    setContentPane(new drawingPanel());
    setFocusable(true);
    setSize(appSize+10, appSize+10);

    //Set up maze creator
    mr = new MazeReader("maze.txt");
    mr.importGrid();
    gridX = mr.getGridXO();

    //Set up grid variables
    row = mr.getRowCoord();
    col = mr.getColCoord();
    size = appSize/col;

    //Set up grid creator
    gr = new Grid(row, col, size, gridX);
    list = gr.getGrid();

    //Set up arraylist of robots
    griddedRobots = new ArrayList<Robot>();
    rand = new RandomRobot(size, row, col, list, "RandomRobotSheet.png");
    memory = new MemoryRobot(gridX, size, row, col, list, "MemorySpriteSheet.png");
    righthand = new RightHandRobot(size, row, col, list, "RightHandSpriteSheet.png");
    griddedRobots.add(rand);
    griddedRobots.add(memory);
    griddedRobots.add(righthand);

    //Start drawing
    movement.start();
    t.start();
    draw = true;
  }
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == t)
    {
      for (int i = 0; i <= 2; i++)
        griddedRobots.get(i).animate();//animate robots
      repaint();
    }
    if (e.getSource() == movement)
    {
      for (int i = 0; i <= 2; i++)
        griddedRobots.get(i).move();//move robots
      repaint();
    }
  }
  public class drawingPanel extends JPanel
  {
    public void paintComponent(Graphics g)
    {
      if (draw)
      {
        gr.createGrid(g);//draw grid
        for (int i = 0; i <= 2; i++)
          griddedRobots.get(i).draw(g);//draw robots
      }
    }
  }
}

