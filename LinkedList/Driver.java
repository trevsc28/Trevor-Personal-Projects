public class Driver
{
  public static void main(String[] args)
  {
	
    ScottLinkedList<String> list = new ScottLinkedList<String>();
    list.add("Trevor");
    list.add("Mary");
    list.addFirst("Evan");
    list.add("Billy");
    list.add("Jill");
    list.add("Jackson");
    list.add("Micheal");
    list.add("Adam");
    list.addLast("Joe");

    System.out.println("List Size: " + list.size());
    System.out.println("Trevor located at: " + list.indexOf("Trevor"));
    System.out.println("Tre5or located at: " + list.indexOf("Tre5or"));
    System.out.println("First is: " + list.getFirst());
    System.out.println("Last is: " + list.getLast());
    System.out.println("Does list contain Jackson?: " + list.contains("Jackson"));
    list.set(2, "Not Mary");
    System.out.println("Does list contain Not Mary?: " + list.contains("Not Mary"));
    list.removeFirst();
    list.remove();
    list.add(0, "Added ");
    list.add(3, "Added 22 ");
    list.remove(2);


	System.out.println("-LIST-");
    for (int i = 0; i < list.size(); i++)
      System.out.println(list.get(i));

    list.clear();

	System.out.println("-LIST-");
    for (int i = 0; i < list.size(); i++)
      System.out.println(list.get(i));
  }
}
