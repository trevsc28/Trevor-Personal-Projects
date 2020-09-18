public class Robot extends GriddedSprite
{
  public Robot(int size, int row, int col, Box[][] list, String str)
  {
    super(size, row, col, list, str);
  }

  public void move(){}

   public boolean checkR()
    {
      if((getBX()+1 <= col-1) && (list[getBX()+1][getBY()].isNotSolid(1)))
        return true;
      else
        return false;
    }
    public boolean checkD()
    {
      if((getBY()+1 <= row-1) && (list[getBX()][getBY()+1].isNotSolid(1)))
        return true;
      else
        return false;
    }
    public boolean checkU()
    {
      if ((getBY()-1 >= 0) && (list[getBX()][getBY()-1].isNotSolid(1)))
        return true;
      else
        return false;
    }
    public boolean checkL()
    {
      if ((getBX()-1 >= 0) && (list[getBX()-1][getBY()].isNotSolid(1)))
        return true;
      else
        return false;
    }
}