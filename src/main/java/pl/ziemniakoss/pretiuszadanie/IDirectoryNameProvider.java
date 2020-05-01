package pl.ziemniakoss.pretiuszadanie;

import java.nio.file.attribute.BasicFileAttributes;

public interface IDirectoryNameProvider {
	String getDirectoryName(BasicFileAttributes fileAttributes);
}
