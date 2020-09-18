public class RightHandRobot extends Robot
{
  private int face;
  private Box[][] list;
  private boolean went;

  public RightHandRobot(int size, int row, int col, Box[][] list, String str)
  {
    super(size, row, col, list, str);
    this.list = list;
    face = 2;
  }

  public void move()  //Face (0 = up)(1 = right) (2 = down) (3 = left)
  {
    went = false;
    while (!went && !(list[getBX()][getBY()].isFinishFlag()))
    {
      //went = true;
      if (face == 0)//up
      {
        if (checkU() && !checkR())
        {
          went = true;
          subBY();
        }
        else if (checkR())
        {
          went = true;
          addBX();
          face = 1;
        }
        else if (!checkU() && !checkL() && !checkR())
        {
          went = true;
          addBY();
          face = 2;
        }
        else if (!checkU() && !checkR() && checkL())
        {
          went = true;
          subBX();
          face = 3;
        }
        else
          face++;
      }
      else if (face == 1)//right
      {
        if (checkR() && !checkD())
        {
          went = true;
          addBX();
        }
        else if (checkD())
        {
          went = true;
          addBY();
          face = 2;
        }
        else if (!checkR() && !checkD() && !checkU())
        {
          went = true;
          subBX();
          face = 3;
        }
        else if (!checkR() && !checkD() && checkU())
        {
          went = true;
          subBY();
          face = 0;
        }
        else
          face++;
      }
      else if (face == 2)//down
      {
        if (checkD() && !checkL())
        {
          went = true;
          addBY();
        }
        else if (checkL())
        {
          went = true;
          subBX();
          face = 3;
        }
        else if (!checkL() && !checkD() && checkR())
        {
          went = true;
          addBX();
          face = 1;
        }
        else if (!checkL() && !checkD() && !checkR())
        {
          went = true;
          subBY();
          face = 0;
        }
        else
          face++;
      }
      else if (face == 3)//left
      {
        if (checkL() && !checkU())
        {
          went = true;
          subBX();
        }
        else if (checkU())
        {
          went = true;
          subBY();
          face = 0;
        }
        else if (!checkL() && !checkU() && checkD())
        {
          went = true;
          addBY();
          face = 2;
        }
        else if (!checkL() && !checkU() && !checkD())
        {
          went = true;
          addBX();
          face = 1;
        }
        else
          face = 0;
      }
      if (list[getBX()][getBY()].isFinishFlag())
        System.out.println("RightHandRobot found the end of the maze.");
    }
  }
}