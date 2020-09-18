import java.util.Random;

public class RandomRobot extends Robot
{
  private int newBox, row, col;
  private Box[][] list;
  private Random r;
  private boolean went;

  public RandomRobot(int size, int row, int col, Box[][] list, String str)
  {
    super(size, row, col, list, str);
    this.list = list;
    this.row = row;
    this.col = col;
    r = new Random();
    went = false;
  }//going left check up

  public void move()
  {
    went = false;
    while(!went && !list[getBX()][getBY()].isFinishFlag())
    {
        newBox = r.nextInt(4)+1; //1(up), 2(right), 3(down), or 4(left) (DIRECTION)
        if (newBox == 1 && (getBY()-1 >= 0) && (list[getBX()][getBY()-1].isNotSolid(1)))//up
        {
          subBY();
          went = true;
        }
        else if (newBox == 2 && (getBX()+1 <= col-1) && (list[getBX()+1][getBY()].isNotSolid(1)))//right
        {
          addBX();
          went = true;
        }
        else if (newBox == 3 && (getBY()+1 <= row-1) && (list[getBX()][getBY()+1].isNotSolid(1)))//down
        {
          addBY();
          went = true;
        }
        else if (newBox == 4 && (getBX()-1 >= 0) && (list[getBX()-1][getBY()].isNotSolid(1))) //left
        {
          subBX();
          went = true;
        }
        if (list[getBX()][getBY()].isFinishFlag())
          System.out.println("RandomRobot found the end of the maze.");
    }
  }
}