import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite
{
  public BufferedImage currentImage, image;
  private int currentCol, imgHeight, imgWidth, currentRow;
  private final int rows = 4, cols = 4;

  public Sprite(String str)
  {
    currentCol = 0;
    currentRow = 0;

    try
    {
      image = ImageIO.read(getClass().getResource(str));
      imgHeight = image.getHeight();
      imgWidth = image.getWidth();
      currentImage = image.getSubimage(0, 0, imgWidth/cols, imgHeight/rows);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void animate()
  {
    currentImage = image.getSubimage(currentCol*imgWidth/cols, currentRow*imgHeight/rows, imgWidth/cols, imgHeight/rows);
    currentCol++;
    if (currentCol == cols)
    {
      currentCol = 0;
      currentRow++;
      if (currentRow == rows)
      	currentRow = 0;
 	 }
  }
}
