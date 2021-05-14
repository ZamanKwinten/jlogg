package jlogg.datahandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jlogg.plugin.LogLine;

public class FileLineReader {
	private static final Logger log = Logger.getLogger(FileLineReader.class.getName());

	public static String readLineFromFile(LogLine line) {
		return readLinesFromFile(Collections.singletonList(line)).get(0);
	}

	public static List<String> readLinesFromFile(List<LogLine> lines) {
		Map<File, RandomAccessFile> openedFiles = new HashMap<>();
		try {
			List<String> result = new ArrayList<>();
			for (LogLine line : lines) {
				RandomAccessFile raf = openedFiles.computeIfAbsent(line.getFile(), file -> {
					try {
						return new RandomAccessFile(file, "r");
					} catch (FileNotFoundException e) {
						log.log(Level.SEVERE, "FileLineReader::readLineFromFile", e);
						return null;
					}
				});

				raf.seek(line.getStart());
				byte[] buffer = new byte[line.getSize()];
				raf.read(buffer);
				result.add(new String(buffer, StandardCharsets.ISO_8859_1));
			}
			return result;
		} catch (Exception e) {
			log.log(Level.SEVERE, "FileLineReader::readLineFromFile", e);
		} finally {
			for (RandomAccessFile raf : openedFiles.values()) {
				try {
					raf.close();
				} catch (IOException e) {
					log.log(Level.SEVERE, "FileLineReader::readLineFromFile", e);
				}
			}
		}

		return Collections.singletonList("Could not read line");
	}
}
