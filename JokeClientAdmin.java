import java.io.*;
import java.net.Socket;
//testing changes

public class JokeClientAdmin {
	public static void main (String args[]){
		String serverName;
		String userName;
		if (args.length < 1) serverName = "localhost";
		else serverName = args[0];
		System.out.println("JokeClient");
		System.out.println("Using server: " + serverName + ", Port: 9999");
		 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		 try {
		 String name;
		 do {
		 System.out.print
		 ("Enter a JOKE, PROVERB, MAINTENANCE, SHUTDOWN or your name, (quit) to end: ");
		 System.out.flush ();
		 name = in.readLine ();
		 if (name.indexOf("quit") < 0)
		 getRemoteAddress(name, serverName);
		 } while (name.indexOf("quit") < 0); // quitSmoking.com?
		 System.out.println ("Cancelled by user request.");
		 } catch (IOException x) {x.printStackTrace ();}
	}

	private static void getRemoteAddress(String name, String serverName) {
		// TODO Auto-generated method stub
		
		 Socket sock;
		 BufferedReader fromServer;
		 PrintStream toServer;
		 String textFromServer;
		 int port = 9999;
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
		
	
	
	 static String toText (byte ip[]) { /* Make portable for 128 bit format */
		 StringBuffer result = new StringBuffer ();
		 for (int i = 0; i < ip.length; ++ i) {
		 if (i > 0) result.append (".");
		 result.append (0xff & ip[i]);
		 }
		 return result.toString ();
		 }
}

