import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite
{
  public BufferedImage currentImage, image;
  private int counter, imgHeight, imgWidth;
  private final int rows = 2, cols = 8;

  public Sprite(String str)
  {
    counter = 0;

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
    currentImage = image.getSubimage(counter*imgWidth/cols, 0, imgWidth/cols, imgHeight/rows);
    counter++;
    if (counter == cols)
      counter = 0;
  }
}