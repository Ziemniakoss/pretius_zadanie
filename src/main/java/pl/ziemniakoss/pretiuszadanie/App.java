package pl.ziemniakoss.pretiuszadanie;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class App {

	public App() {
		new File("HOME").mkdir();
		new File("TEST").mkdir();
		new File("DEV").mkdir();
	}

	public void start() throws IOException, InterruptedException {
		Thread homeWatcher = new Thread(new FileMover());
		homeWatcher.setName("fileMover");

		Thread movedFilesCounter = new Thread(new MovedFilesLogger());
		movedFilesCounter.setName("movedFilesCounter");

		movedFilesCounter.start();
		homeWatcher.start();

		Scanner in = new Scanner(System.in);
		System.out.println("Nasłuchuję na zmiany...");
		System.out.println("Aby zakończyć wciśnij ENTER");
		in.nextLine();
		System.out.println("Zamykam...");
		homeWatcher.interrupt();
		movedFilesCounter.interrupt();
		homeWatcher.join();
		movedFilesCounter.join();
		System.exit(0);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		new App().start();
	}
}
