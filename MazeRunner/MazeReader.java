import java.io.FileReader;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class MazeReader
{
  private Scanner s;
  private int[][] GridXO;
  private int rowcoord, colcoord;

  public MazeReader(String str)
  {
    try
    {
      s = new Scanner(new FileReader(str));
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  public void importGrid()
  {
    rowcoord = s.nextInt();
    colcoord = s.nextInt();
    GridXO = new int[rowcoord][colcoord];

    for(int row = 0; row < GridXO.length; row++)
      for(int col = 0; col < GridXO[row].length; col++)
    {
      GridXO[col][row] = s.nextInt();
    }
  }
  public int[][] getGridXO()
  {
    return GridXO;
  }
  public int getRowCoord()
  {
    return rowcoord;
  }
  public int getColCoord()
  {
    return colcoord;
  }
}