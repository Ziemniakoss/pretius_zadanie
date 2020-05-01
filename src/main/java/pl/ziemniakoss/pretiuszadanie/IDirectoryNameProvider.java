package pl.ziemniakoss.pretiuszadanie;

import java.io.File;
import java.nio.file.attribute.BasicFileAttributes;

public interface IDirectoryNameProvider {
	String getDirectoryName(BasicFileAttributes fileAttributes);
}
