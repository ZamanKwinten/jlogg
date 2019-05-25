package jlogg.datahandlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import jlogg.shared.LogLine;

public class FileLineReader {
	private static final Logger log = Logger.getLogger(FileLineReader.class.getName());

	public static String readLineFromFile(LogLine line) {

		try (RandomAccessFile raf = new RandomAccessFile(line.getFile(), "r")) {
			raf.seek(line.getStart());
			byte[] buffer = new byte[line.getSize()];
			raf.read(buffer);
			return new String(buffer, StandardCharsets.ISO_8859_1);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "FileLineReader::readLineFromFile", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "FileLineReader::readLineFromFile", e);
		}
		return "Could not read line";
	}
}
