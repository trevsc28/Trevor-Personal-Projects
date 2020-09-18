import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
  public static void main(String[] args) throws Exception
  {
    Scanner sc = new Scanner(System.in);
    Socket s = new Socket("localhost", 4747);
    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    OutgoingServer out = new OutgoingServer(s);
    String msg;

    if (dout != null && s != null)
    {
      while ((msg = sc.nextLine()) != null && in != null)
        out.writeMessage(msg);
      dout.close();
      s.close();
    }
  }
}

class OutgoingServer implements Runnable
{
  Socket s;
  Thread t;
  BufferedReader in;
  DataOutputStream dout;

  public OutgoingServer(Socket s)
  {
    this.s = s;
    start();
  }
  public void start()
  {
    try
    {
      in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      dout = new DataOutputStream(s.getOutputStream());
      t = new Thread(this);
      t.start();
    }
    catch (IOException e)
    {
      System.out.println(e);
    }
  }
  public void writeMessage(String msg)
  {
    try
    {
      dout.writeBytes(msg);
      dout.writeByte('\n');
      dout.flush();
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
  }
  public void run()
  {
    try
    {
      String msg;
      while ((msg = in.readLine()) != null)
      {
        System.out.println(msg);
      }
    }
    catch (IOException e)
    {
      System.out.println(e);
    }
  }
}