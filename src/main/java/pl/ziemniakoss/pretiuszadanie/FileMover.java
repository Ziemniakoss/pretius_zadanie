package pl.ziemniakoss.pretiuszadanie;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

public class FileMover implements Runnable {
	private final WatchService watchService;
	HashMap<String, IDirectoryNameProvider> dirNameProviders;


	public FileMover() throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		Path home = Paths.get("HOME");
		home.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		dirNameProviders = new HashMap<>();
		dirNameProviders.put("jar", fileAttributes -> {
			FileTime fileCreationTime = fileAttributes.creationTime();
			int creationHour = LocalDateTime.ofInstant(fileCreationTime.toInstant(), ZoneId.systemDefault()).getHour();
			if (creationHour % 2 == 0) {
				return "DEV";
			} else {
				return "TEST";
			}
		});
		dirNameProviders.put("xml", fileAttributes -> "DEV");
	}


	private String extractExtension(String filename) {
		return filename.substring(filename.lastIndexOf('.') + 1);
	}

	@Override
	public void run() {
		WatchKey watchKey;
		try {
			while ((watchKey = watchService.take()) != null) {
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					handleEvent(event);
				}
				watchKey.reset();
			}
		} catch (InterruptedException e) {
			try {
				watchService.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private void handleEvent(WatchEvent<?> event) {
		String extension = extractExtension(event.context().toString());
		if (dirNameProviders.containsKey(extension)) {
			Path current = Paths.get("HOME", event.context().toString());
			BasicFileAttributes attributes;
			try {
				attributes = Files.readAttributes(current, BasicFileAttributes.class);
			} catch (IOException e) {
				System.err.println("Błąd przy wczytwyaniu atrybutów pliku: " + e.getMessage());
				return;
			}
			String directoryName = dirNameProviders.get(extension).getDirectoryName(attributes);
			Path newPath = Paths.get(directoryName, event.context().toString());
			try {
				Files.move(current, newPath);
			} catch (IOException e) {
				System.err.println(String.format("Błąd przy przenoszeniu pliku %s do %s: %s: ",
						current.toString(),newPath.toString(),e.getMessage()));
			}
		}
	}
}
