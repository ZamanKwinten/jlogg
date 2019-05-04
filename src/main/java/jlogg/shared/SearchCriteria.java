package jlogg.shared;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SearchCriteria {
	protected String pattern;
	protected boolean ignoreCase;

	public SearchCriteria(String pattern, boolean ignoreCase) {
		this.pattern = pattern;
		this.ignoreCase = ignoreCase;
	}

	private Pattern getPattern() {
		if (ignoreCase) {
			return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		} else {
			return Pattern.compile(pattern);
		}
	}

	public String getPatternString() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public boolean matches(String value) {
		try {
			// if the pattern is compilable it's a regex => handle it as such
			if (getPattern().matcher(value).find()) {
				return true;
			}
		} catch (PatternSyntaxException e) {
			// regex was not valid => handle it as a strict match
			if (ignoreCase) {
				return value.toUpperCase().contains(pattern.toUpperCase());
			} else {
				return value.toUpperCase().contains(pattern.toUpperCase());
			}
		}
		return false;
	}
}
