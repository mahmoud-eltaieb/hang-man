package ver1_4;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * v2.0
 * 
 * This is the second version of hangman in a command line like application.
 * This one uses a Hangman Server and a Hangman Client  
 */
public class HangmanServer2 {
	
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;
	private ArrayList wordsToChooseFrom = new ArrayList();
	
//	private String guessMe;
	Word guessMe;
	private ServerSocket serverSocket;
	private Socket client;
	
	public static void main(String[] args) throws Exception {
		
//		String fileLocation = args[0];
//		int port = Integer.parseInt(args[1]);
		String fileLocation = "C:\\dictionary.txt";
		int port =9999;
		
		HangmanServer2 server = new HangmanServer2();
		try {
			server.chooseWord(fileLocation);
			server.start(port);
			System.out.println("1");
			server.waitOnConnection();
			server.sendChoosenWord();
		} catch(FileNotFoundException e) {
			System.out.println("The file is not found, shutting down...");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the client some greetings
	 */
	public void sendChoosenWord() {
				
		try {
			serverOutputStream.writeObject(guessMe);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Chooses the random word from the file specified
	 * 
	 * @param fileLocation
	 * @throws FileNotFoundException
	 */
	public void chooseWord(String fileLocation) throws FileNotFoundException {
		Scanner fileScanner = new Scanner(new FileReader(fileLocation));
		while (fileScanner.hasNext()) {
			wordsToChooseFrom.add(fileScanner.next().toUpperCase());
		} 
		fileScanner.close();
		
		if( wordsToChooseFrom.isEmpty() ) {
			System.out.println("The file is empty, shutting down...");
			System.exit(0);
		}
		
		Random random = new Random(); 
		int getRandomIndex = random.nextInt(wordsToChooseFrom.size());
//		guessMe = (String) wordsToChooseFrom.get(getRandomIndex);
		guessMe = new Word();
		guessMe.setChoosenServerWord((String) wordsToChooseFrom.get(getRandomIndex));
		
	}
	
	/**
	 * Starts the server on the specified port
	 * 
	 * @param port
	 * @throws IOException
	 */
	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(1000000);
	}
	
	/**
	 * Wait for a connection to come up
	 * 
	 * @throws IOException
	 */
	public void waitOnConnection() throws IOException {
		client = serverSocket.accept();
				
		serverInputStream = new
				ObjectInputStream(client.getInputStream());

		serverOutputStream = new
				ObjectOutputStream(client.getOutputStream());
	}
	
}
