package pl.ziemniakoss.pretiuszadanie;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

public class MovedFilesLogger implements Runnable {
	private final String FILE_COUNT_LOG = "HOME" + File.separator + "count.txt";
	private WatchService watchService;

	public MovedFilesLogger() throws IOException {
		this.watchService = FileSystems.getDefault().newWatchService();
		Path test = Paths.get("TEST");
		Path dev = Paths.get("DEV");
		test.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		dev.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
	}

	@Override
	public void run() {
		WatchKey watchKey;
		try {
			while ((watchKey = watchService.take()) != null) {
				updateCountFile();
			}
		} catch (InterruptedException e) {
			try {
				watchService.close();
			} catch (IOException ioException) {
				System.err.println("Bład przy zamykaniu Watchservice: " + ioException.getMessage());
			}
		}

	}

	private void updateCountFile() {
		int filesInDev = new File("DEV").list().length;
		int filesInTest = new File("TEST").list().length;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_COUNT_LOG, false));
			writer.write("Łącznie: " + (filesInDev + filesInTest)+"\n");
			writer.write("DEV: " + filesInDev+"\n");
			writer.write("TEST: " + filesInTest+"\n");
			writer.flush();
			writer.close();
			System.out.println("Log zaktualizowany");
		} catch (IOException e) {
			System.err.println("Błąd przy aktualizowaniu " + FILE_COUNT_LOG + ": " + e.getMessage());
		}
	}
}
