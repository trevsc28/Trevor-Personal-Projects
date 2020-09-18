import java.util.Random;
import java.util.ArrayList;

public class MemoryRobot extends Robot
{
  private Random r;
  private boolean went;
  private ArrayList<String> directions;
  private int[][] grid;
  private final int CHECKED = 4, FILL = 5;

  public MemoryRobot(int[][] grid, int size, int row, int col, Box[][] list, String str)
  {
    super(size, row, col, list, str);
    r = new Random();
    this.grid = grid;
    went = false;
    directions = new ArrayList<String>();
  }
  public void move()//Path = 0(down), 1(up), 2(left), 3(right)
  {
    went = false;
    while(!went && !list[getBX()][getBY()].isFinishFlag())
    {
      if (checkD() && (grid[getBX()][getBY()+1] <= 3))
        directions.add("down");
      if (checkU() && (grid[getBX()][getBY()-1] <= 3))
        directions.add("up");
      if (checkR() && (grid[getBX()+1][getBY()] <= 3))
        directions.add("right");
      if (checkL()&& (grid[getBX()-1][getBY()] <= 3))
        directions.add("left");

      if (directions.size() >= 1)
        makeMove(directions.get(r.nextInt(directions.size())), 1);
      else if (directions.size() == 0)// no untried square remaining
      {//check all checked squares;
        if (checkD())
          directions.add("down");
        if (checkU())
          directions.add("up");
        if (checkR())
          directions.add("right");
        if (checkL())
          directions.add("left");

        if (directions.size() == 1)//exclusing already tried spaces
          makeMove(directions.get(0), 3);
        else
          makeMove(directions.get(r.nextInt(directions.size())), 2);//fill the wall
      }
      if (list[getBX()][getBY()].isFinishFlag())
        System.out.println("MemoryRobot found the end of the maze.");
    }
  }


  public void makeMove(String st, int i)
  {
    if (i == 1 || i == 2)
    {
      if (st.equalsIgnoreCase("down"))
      {
        tag();
        addBY();
        went = true;
        directions.clear();
      }
      else if (st.equalsIgnoreCase("up"))
      {
        tag();
        subBY();
        went = true;
        directions.clear();
      }
      else if (st.equalsIgnoreCase("right"))
      {
        tag();
        addBX();
        went = true;
        directions.clear();
      }
      else if (st.equalsIgnoreCase("left"))
      {
        tag();
        subBX();
        went = true;
        directions.clear();
      }
    }
    else if (i == 3)
    {
      if (st.equalsIgnoreCase("down"))
      {
        fillWall();
        addBY();
        went = true;
        directions.clear();
      }
      else if (st.equalsIgnoreCase("up"))
      {
        fillWall();
        subBY();
        went = true;
        directions.clear();
      }
      else if (st.equalsIgnoreCase("right"))
      {
        fillWall();
        addBX();
        went = true;
        directions.clear();
      }
      else if (st.equalsIgnoreCase("left"))
      {
        fillWall();
        subBX();
        went = true;
        directions.clear();
      }
    }
  }
  public void tag()
  {
    grid[getBX()][getBY()] = CHECKED;
  }
  public void fillWall()
  {
    grid[getBX()][getBY()] = FILL;
  }

   public boolean checkR()
    {
      if((getBX()+1 <= col-1) && (list[getBX()+1][getBY()].isNotSolid(0)))
        return true;
      else
        return false;
    }
    public boolean checkD()
    {
      if((getBY()+1 <= row-1) && (list[getBX()][getBY()+1].isNotSolid(0)))
        return true;
      else
        return false;
    }
    public boolean checkU()
    {
      if ((getBY()-1 >= 0) && (list[getBX()][getBY()-1].isNotSolid(0)))
        return true;
      else
        return false;
    }
    public boolean checkL()
    {
      if ((getBX()-1 >= 0) && (list[getBX()-1][getBY()].isNotSolid(0)))
        return true;
      else
        return false;
    }
}

