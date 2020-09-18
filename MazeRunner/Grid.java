import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Grid
{
  private int nRow, nCol, nSize, sheetRowCol;
  private Box[][] gridList;
  private int[][] gridXO;
  private BufferedImage spriteSheet, wallImg, floorImg, startImg, finishImg;

  public Grid(int row, int col, int size, int[][] gridX)
  {
    nRow = row;
    nCol = col;
    nSize = size;
    gridXO = gridX;
    gridList = new Box[nRow][nCol];
    sheetRowCol = 162;//size of each sprite (cube)
    try
    {
      spriteSheet = ImageIO.read(getClass().getResource("sheet2.png"));
      floorImg = spriteSheet.getSubimage(0, 0, sheetRowCol, sheetRowCol);
      wallImg = spriteSheet.getSubimage(sheetRowCol, 0, sheetRowCol, sheetRowCol);
      startImg = spriteSheet.getSubimage(sheetRowCol*2, 0, sheetRowCol, sheetRowCol);
      finishImg = spriteSheet.getSubimage(sheetRowCol*3, 0, sheetRowCol, sheetRowCol);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  public void createGrid(Graphics g)
  {
    for(int row = 0; row < nRow; row++)
      for(int col = 0; col < nCol; col++)
      {
         if (gridXO[row][col] == 0)
          gridList[row][col] = new Box(g, row, col, nSize, 0, floorImg);
         else if (gridXO[row][col] == 1)
          gridList[row][col] = new Box(g, row, col, nSize, 1, wallImg);
         else if (gridXO[row][col] == 2)
          gridList[row][col] = new Box(g, row, col, nSize, 2, startImg);
         else if (gridXO[row][col] == 3)
          gridList[row][col] = new Box(g, row, col, nSize, 3, finishImg);
         else if (gridXO[row][col] == 4)//checked
           gridList[row][col] = new Box(g, row, col, nSize, 0, floorImg);
         else if (gridXO[row][col] == 5)//filled
           gridList[row][col] = new Box(g, row, col, nSize, 5, floorImg);
      }
  }
  public Box[][] getGrid()
  {
   return gridList;
  }
}

