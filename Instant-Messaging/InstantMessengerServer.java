import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InstantMessengerServer
{
  static ArrayList<ClientHandler> clients;
  static HashMap<String, InetAddress> map;

  public static void main(String[] args) throws Exception
  {
    clients = new ArrayList<ClientHandler>();
    ServerSocket port = new ServerSocket(4747);
    map = new HashMap<String, InetAddress>();

    while (true)
    {
      Socket s = port.accept();
      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      DataOutputStream dout = new DataOutputStream(s.getOutputStream());

      ClientHandler ch = new ClientHandler(s, dout, in, s.getInetAddress());
      clients.add(ch);
    }
  }
}

class ClientHandler implements Runnable
{
  Socket s;
  BufferedReader in;
  DataOutputStream dout;
  InetAddress ip;
  Thread t;
  boolean handle, user;
  String username, str, sendTo, PM;
  int i;

  public ClientHandler(Socket s, DataOutputStream dout, BufferedReader in, InetAddress ip)
  {
    this.s = s;
    this.in = in;
    this.dout = dout;
    this.ip = ip;
    handle = true;
    user = true;
    start();
  }

  public void start()
  {
    t = new Thread(this);
    t.start();
  }

  public void writeMessage(String msg)
  {
    try
    {
      System.out.println(msg);
      dout.writeBytes(msg);
      dout.writeByte('\n');
      dout.flush();
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
  }

  public void messageToAll(String msg)
  {
    try
    {
      for (ClientHandler ch : InstantMessengerServer.clients)
      {
        if (ch != this)
        {
          ch.dout.writeBytes(msg);
          ch.dout.writeByte('\n');
          ch.dout.flush();
        }
      }
    }
    catch(IOException e)
    {
      System.out.println(e);
    }
  }

  public void run()
  {
    if (s != null && in != null)
    {
      try
      {
        dout.writeBytes("Hello. Please enter a nickname\nChat '-exit' to leave");
        dout.writeByte('\n');
        dout.flush();

        while(handle && (str = in.readLine()) != null)
        {

          String[] tokens = str.split("[ ]+");

          if (user)//input a screen name
          {
            messageToAll("Welcome " + (username = str) + " to the chat room!");
            InstantMessengerServer.map.put(username, ip);
            user = false;
          }
          else if (str.equalsIgnoreCase("-exit"))//exit the client
          {
            InstantMessengerServer.map.values().remove(username);
            messageToAll(username + " has left the chat room.");
            handle = false;
          }
          else if (tokens[0].equalsIgnoreCase("/pm"))//create a private message with a user
          {
            if(InstantMessengerServer.map.containsKey(tokens[1]))
            {
              PM = "";
              InetAddress userIP = InstantMessengerServer.map.get(tokens[1]);

              for (int x = 2; x < tokens.length; x++)
                PM = PM + " " + tokens[x];

              for (ClientHandler ch: InstantMessengerServer.clients)
              {
                if (ch.ip == userIP)
                {
                  try
                  {
                    ch.dout.writeBytes("PM from " + username + ": " + PM);
                    ch.dout.writeByte('\n');
                    ch.dout.flush();
                  }
                  catch (IOException e)
                  {
                    System.out.println(e);
                  }
                }
              }
            }
            else
            {
              dout.writeBytes("ERROR: User " + tokens[1] + " does not exist.");
              dout.writeByte('\n');
              dout.flush();
            }
            dout.writeBytes("Message sent to @ " + tokens[1]);
            dout.writeByte('\n');
            dout.flush();
          }
          else//send a general message to all clients
            messageToAll(username + ": " + str);
        }
        dout.close();
        s.close();
      }
      catch (IOException e)
      {
        System.out.println(e);
      }
    }
  }
}

