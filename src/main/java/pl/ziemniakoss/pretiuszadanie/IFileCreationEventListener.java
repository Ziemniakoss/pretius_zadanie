package pl.ziemniakoss.pretiuszadanie;

import java.nio.file.Path;

public interface IFileCreationEventListener {
	void onCreate(Path path);
}
