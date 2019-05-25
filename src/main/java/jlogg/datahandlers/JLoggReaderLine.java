package jlogg.datahandlers;

public class JLoggReaderLine {
	private final String lineText;
	private final short amountOfDelimiterCharacters;

	public JLoggReaderLine(String lineText, short amountOfDelimiterCharacters) {
		this.lineText = lineText;
		this.amountOfDelimiterCharacters = amountOfDelimiterCharacters;
	}

	public String lineText() {
		return lineText;
	}

	public short amountOfDelimiterCharacters() {
		return amountOfDelimiterCharacters;
	}
}