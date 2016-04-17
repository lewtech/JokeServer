import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import javax.print.DocFlavor.STRING;

import org.omg.CORBA.portable.UnknownException;

class JokeWorker extends Thread{
	private static final Boolean True = null;
	ServerSocket serverSock;
	Socket sock;
	JokeWorker (ServerSocket s){serverSock = s;}
	boolean isRunning = false;
	
	JokeProverbState jokeProverbState1 = new JokeProverbState("", false, false, false, false, false, false, false, false, false, false);
	JokeProverbState jokeProverbState2 = new JokeProverbState(null, false, false, false, false, false, false, false, false, false, false);
	JokeProverbState jokeProverbState3 = new JokeProverbState(null, false, false, false, false, false, false, false, false, false, false);
	JokeProverbState jokeProverbState4 = new JokeProverbState(null, false, false, false, false, false, false, false, false, false, false);
	JokeProverbState jokeProverbState5 = new JokeProverbState(null, false, false, false, false, false, false, false, false, false, false);


	
	//setup arrays of jokes, proverbs, and maintenance messages
	String jokes[] = {"A Joke one here...HAHA", "B Why did the Chicken Cross the road...hahaha", "C Knock Knock, Who's There?", "D Joke 4", "E Joke5"};
	String proverbs[] = {"A Proverb: Chase two rabbits, catch none","B Proverb: 1 Bird in the Hand is Better than 2 in the Bush", "C PRoverb", "D Proverb", "E Proverb"};
	String maintenances[] = {"Maintenance: Sorry System is Down","Maintenance: Try again in 5000 minutes", "Maintenance: Out to lunch", "Maintenance: Hamsters ran out of steam.", "Maintenance: Installing 23 of 2347 updates"};
	//enum modes of the server
	public enum Mode { JOKE, PROVERB, MAINTENANCE, SHUTDOWN}
	String output = ""; //initialize output string
	Mode mode = Mode.JOKE; //set default mode to JOKEMode


	
	@Override
	public void run(){
		if(!isRunning) {return;}
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
				//sendJokeOrProverb(name, out);
				
				if (!setModeFromRequest(name, out)){ //if doesn't get set, process request
					processRequest( out, name);
				}
				
			} catch (IOException x){
				System.out.println("Server read error");
				x.printStackTrace();
			}
			sock.close(); //close this connection, but not the server
			} catch (IOException ioe) {System.out.println(ioe);}
			}
	
	
	
	private JokeProverbState checkForState (String name) {
//		System.out.println("checking state");
//		if (jokeProverbState1 == null){
//			jokeProverbState1.setName(name);
//			System.out.println("assigning name to jokeproverbstate1 " + name);
//			return jokeProverbState1;
//		} else if (name.equals(jokeProverbState1.getName())){
//		return jokeProverbState1;
//		} else if (name.equals(jokeProverbState2.getName())){
//			return jokeProverbState2;	
//		} else if (name.equals(jokeProverbState3.getName())){
//			return jokeProverbState3;
//		} else if (name.equals(jokeProverbState4.getName())){
//			return jokeProverbState4;
//		}
//		return jokeProverbState5;
	return jokeProverbState1;
	}
	
	private boolean setModeFromRequest (String mode, PrintStream out){
		boolean result = false;
		try {
			this.mode = Mode.valueOf(mode); //if it finds a mode it will change it, otherwise keep state
			out.println("Changed to [" + mode.toString() + "] mode");
			result=true;
		}catch (Exception e){
			
		}
		
		return result;
	}
	
	private void processRequest ( PrintStream out, String name){
		switch (mode){
		case JOKE:
			out.println("Jokemode ");
			sendJoke(out,name);
			break;
		case PROVERB:
			out.println("ProverbMode ");
			sendProverb(out,name);
			break;
		case MAINTENANCE:
			out.println("MaintenanceMode ");
			sendMaintenance(out,name);
			break;
		case SHUTDOWN:
			out.println("ShutdownMode");
			out.println("Shutting down Server...");
			stopListening();
			break;
		
		default:
			out.println("unsupported mode");
		}
	}
	
	private void sendJoke( PrintStream out, String name) {
		// TODO Auto-generated method stub
		System.out.println(checkForState(name).toString());
		Random randomNumberGenerator = new Random();
		int randomInt = randomNumberGenerator.nextInt(4);
		jokeProverbState1.setName(name);
		System.out.println("joke1 " + jokeProverbState1.getJoke1());
		System.out.println(jokeProverbState1.getName());
		jokeProverbState1.setJoke1(true);
		System.out.println("joke1 " + jokeProverbState1.getJoke1());
		output = jokes[randomInt];
		out.println( name+" "+ output);
	}
	
	private void sendMaintenance( PrintStream out, String name) {
		// TODO Auto-generated method stub
		Random randomNumberGenerator = new Random();
		int randomInt = randomNumberGenerator.nextInt(4);
		

		output = maintenances[randomInt];
		out.println(output);
	}
	
	private void sendProverb( PrintStream out, String name) {
		// TODO Auto-generated method stub
		Random randomNumberGenerator = new Random();
		int randomInt = randomNumberGenerator.nextInt(4);
		
		output = proverbs[randomInt];
		out.println( name+" "+ output);
	}
	
	public boolean listen() {
		boolean results = false;
		try {
			sock = serverSock.accept();
			results = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	//shutdown server
	public void stopListening() {
		try {
			this.serverSock.close();
			this.isRunning = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//force thread to continue running so that it doesn't die
	public void start() {
		if(!isRunning) {
			isRunning = true;
			super.start();
		} else {
			this.run();
		}
	}
	
	public void setFromAdmin(STRING name){
		
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


}

public class JokeServer {
	public static void main(String a[])throws IOException {
		// TODO Auto-generated method stub
		int q_len = 6; //not interesting. number of requests for opsys to queue
		int port = 8888;
		int portAdmin = 9999;
		Socket sock;
		

		//Boolean run = True;
		ServerSocket servsock = new ServerSocket (port, q_len);
		//ServerSocket servsockAdmin = new ServerSocket (portAdmin, q_len);
		JokeWorker jokeWorker = new JokeWorker(servsock);
		//JokeWorker adminWorker = new JokeWorker(servsockAdmin);
		AdminLooper AL = new AdminLooper();
		Thread t = new Thread(AL);
			t.start();
			

		
		System.out.println("Lew Flauta's JokeServer Starting up ver 0.8, listening at port:" + port);
		while (jokeWorker.listen()){ //run loop for the server
			jokeWorker.start();
			//adminWorker.start();
			//moved the thread out of this loop, because each reconnection it was spawning a new thread, and losing the state of the worker thread.
			//sock = servsock.accept(); //wait for the next client connection
			//new Worker(sock).start(); //spawn worker to handle it
			//servsock.close();
		}
		jokeWorker.stopListening();
	}
	
	public class JokeHashMap{
		HashMap <String, Boolean> jokeProverbMap = new HashMap <String, Boolean>();
	}
	

}




	class AdminWorker extends Thread{
		private static final Boolean True = null;
		ServerSocket serverSock;
		Socket sock;
		AdminWorker (Socket s){sock = s;}
		boolean isRunning = false;
		
		//setup arrays of jokes, proverbs, and maintenance messages
		
		//enum modes of the server
		public enum Mode { JOKE, PROVERB, MAINTENANCE, SHUTDOWN}
		String output = ""; //initialize output string
		Mode mode = Mode.JOKE; //set default mode to JOKEMode


		
		@Override
		public void run(){
			if(!isRunning) {return;}
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
					processRequest( out, name);
					
					if (!setModeFromRequest(name, out)){ //if doesn't get set, process request
						//processRequest( out, name);
						
					}
					
				} catch (IOException x){
					System.out.println("Server read error");
					x.printStackTrace();
				}
				sock.close(); //close this connection, but not the server
				} catch (IOException ioe) {System.out.println(ioe);}
				}
		
		private boolean setModeFromRequest (String mode, PrintStream out){
			boolean result = false;
			try {
				this.mode = Mode.valueOf(mode); //if it finds a mode it will change it, otherwise keep state
				out.println("Changed to [" + mode.toString() + "] mode");
				result=true;
			}catch (Exception e){
				
			}
			
			return result;
		}
		
		private static void sendMode(String name, String serverName) {
			// TODO Auto-generated method stub
			 Socket sock;
			 BufferedReader fromServer;
			 PrintStream toServer;
			 String textFromServer;
			 int port = 8888;
			 try{
			 /* Open our connection to server port, choose your own port number.. */
			 sock = new Socket(serverName, port);

			 // Create filter I/O streams for the socket:
			 fromServer =
			 new BufferedReader(new InputStreamReader(sock.getInputStream()));
			 toServer = new PrintStream(sock.getOutputStream());
			 // Send machine name or IP address to server:
			 toServer.println(name); toServer.flush();
			 }catch (IOException x) {
				 System.out.println ("Socket error.");
				 x.printStackTrace ();
				 }}
		
		private void processRequest ( PrintStream out, String name){
			switch (mode){
			case JOKE:
				out.println("AdminJokemode ");
				//sendJoke(out,name);
				toJokeWorker("PROVERB", "127.0.0.1");
				break;
			case PROVERB:
				out.println("AdminProverbMode ");
				//toJokeWorker("PROVERB", "localhost");
				//sendProverb(out, name);
				
				break;
			case MAINTENANCE:
				out.println("AdminMaintenanceMode ");
				//sendMaintenance(out, name);
				break;
			case SHUTDOWN:
				out.println("AdminShutdownMode");
				//out.println("Shutting down Server...");
				stopListening();
				break;
			
			default:
				out.println("unsupported mode");
			}
		}
		
		private void sendJoke( PrintStream out, String name) {
			// TODO Auto-generated method stub
			Random randomNumberGenerator = new Random();
			int randomInt = randomNumberGenerator.nextInt(4);

//			output = jokes[randomInt];
			out.println( name+" "+ output);
		}
		
		private void sendMaintenance( PrintStream out, String name) {
			// TODO Auto-generated method stub
			Random randomNumberGenerator = new Random();
			int randomInt = randomNumberGenerator.nextInt(4);

//			output = maintenances[randomInt];
			out.println(output);
		}
		
		private void sendProverb( PrintStream out, String name) {
			// TODO Auto-generated method stub
			Random randomNumberGenerator = new Random();
			int randomInt = randomNumberGenerator.nextInt(4);

//			output = proverbs[randomInt];
			out.println( name+" "+ output);
		}
		
		public boolean listen() {
			boolean results = false;
			try {
				sock = serverSock.accept();
				results = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return results;
		}
		
		//shutdown server
		public void stopListening() {
			try {
				this.serverSock.close();
				this.isRunning = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//force thread to continue running so that it doesn't die
		public void start() {
			if(!isRunning) {
				isRunning = true;
				super.start();
			} else {
				this.run();
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
	
	private static void toJokeWorker(String name, String serverName) {
		// TODO Auto-generated method stub
		
		 Socket sock;
		 BufferedReader fromServer;
		 PrintStream toServer;
		 String textFromServer;
		 int port = 8888;
		 try{
		 /* Open our connection to server port, choose your own port number.. */
		 sock = new Socket(serverName, port);
		 //System.out.println("**** adminClient ****" + name + " " + serverName);
		 // Create filter I/O streams for the socket:
		 fromServer =
		 new BufferedReader(new InputStreamReader(sock.getInputStream()));
		 toServer = new PrintStream(sock.getOutputStream());
		 // Send machine name or IP address to server:
		 toServer.println(name); toServer.flush();

		 // Read two or three lines of response from the server,
		 // and block while synchronously waiting:
		 for (int i = 1; i <=3; i++){
		 textFromServer = fromServer.readLine();
		 if (textFromServer != null) System.out.println(textFromServer);
		 }
		 sock.close();
		 } catch (IOException x) {
		 System.out.println ("Socket error.");
		 x.printStackTrace ();
		 }
		 }
	}
	
	//runs in a separate thread and opens socket for listening to the JokeAdminClient
	class AdminLooper implements Runnable {
		public static boolean adminControlSwitch = true;

		  public void run(){ // Running the Admin listen loop
		    System.out.println("In the admin looper thread");
		    
		    int q_len = 6; /* Number of requests for OpSys to queue */
		    int port = 9999;  // We are listening at a different port for Admin clients
		    Socket sock;

		    try{
		      ServerSocket servsock = new ServerSocket(port, q_len);
		      while (adminControlSwitch) {
				// wait for the next ADMIN client connection:
				sock = servsock.accept();
				new AdminWorker (sock).start(); 
		      }
		      servsock.close();
		    }catch (IOException ioe) {System.out.println(ioe);}
		 }
	}
	

	




