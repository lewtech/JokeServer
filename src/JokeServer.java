import java.io.*;
import java.net.*;
import java.util.Random;

import org.omg.CORBA.portable.UnknownException;

class Worker extends Thread{
	private static final Boolean True = null;
	Socket sock;
	Worker (Socket s){sock = s;}
	
	String jokes[] = {"A Joke one here...HAHA", "B Why did the Chicken Cross the road...hahaha", "C Knock Knock, Who's There?", "D Joke 4", "E Joke5"};
	String proverbs[] = {"A Proverb: Chase two rabbits, catch none","B Proverb: 1 Bird in the Hand is Better than 2 in the Bush", "C PRoverb", "D Proverb", "E Proverb"};
	public enum Mode { JOKE, PROVERB, MAINTENANCE, SHUTDOWN}
	String testMode ="Joke";
	String output = "";


	
	@Override
	public void run(){
		PrintStream out = null;
		BufferedReader in = null;
		
		//try to read stream, if not print error message
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintStream (sock.getOutputStream());
			//note this branch may not execute when expected
			try {
				String name;
				name = in.readLine();
				System.out.println("Looking up " + name);
				sendJokeOrProverb(name, out);
			} catch (IOException x){
				System.out.println("Server read error");
				x.printStackTrace();
			}
			sock.close(); //close this connection, but not the server
			} catch (IOException ioe) {System.out.println(ioe);}
			}

	private void sendJokeOrProverb(String name, PrintStream out) {
		// TODO Auto-generated method stub
		Random randomNumberGenerator = new Random();
		int randomInt = randomNumberGenerator.nextInt(4);
		if (testMode == "Joke") {output = jokes[randomInt];
		} else if (testMode == "Proverb") {output = proverbs[randomInt];}
		try {
			out.println(name + " " + output);
			InetAddress machine = InetAddress.getByName(name);
			out.println("Host name : " + machine.getHostName()); //To client...
			out.println("Host IP : " + toText (machine.getAddress()));
			
			
		}catch(UnknownException | UnknownHostException ex){
			out.println("Failed in attempt to look up " + name);
		} 
		}
	


//not interesting to us:
static String toText(byte ip[] ) { //make portable for 128 bit format
	StringBuffer result = new StringBuffer ();
	for (int i = 0; i < ip.length; ++i){
		if (i>0) result.append (".");
		result.append (0xff & ip[i]);
	}
	// TODO Auto-generated method stub
	return result.toString();
}


public static class JokeServer {

	public static void main(String a[])throws IOException {
		// TODO Auto-generated method stub

		int q_len = 6; //not interesting. number of requests for opsys to queue
		int port = 8888;
		Socket sock;
		//Boolean run = True;
		ServerSocket servsock = new ServerSocket (port, q_len);
		
		
		System.out.println("Lew Flauta's JokeServer Starting up ver 0.1, listening at port:" + port);
		while (true){ //run loop for the server
			sock = servsock.accept(); //wait for the next client connection
			new Worker(sock).start(); //spawn worker to handle it
			servsock.close();
		}
		
		
	}



}
}
