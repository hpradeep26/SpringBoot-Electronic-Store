package com.lcwd.electronic.store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FileUtil {


	public static void deletefile(String filePath) {
		try {
			Path path = Path.of(filePath);
			Files.deleteIfExists(path);
		}catch(NoSuchFileException exception) {
			exception.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
