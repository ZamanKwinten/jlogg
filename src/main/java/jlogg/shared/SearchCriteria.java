package jlogg.shared;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SearchCriteria {
	protected String pattern;
	protected SearchOptions searchOptions;

	public SearchCriteria(String pattern, SearchOptions searchOptions) {
		this.pattern = pattern;
		this.searchOptions = searchOptions;
	}

	private Pattern getPattern() {
		if (searchOptions.ignoreCase()) {
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

	public SearchOptions getSearchOptions() {
		return searchOptions;
	}

	public boolean matches(String value) {
		try {
			// if the pattern is compilable it's a regex => handle it as such
			if (getPattern().matcher(value).find()) {
				return true;
			}
		} catch (PatternSyntaxException e) {
			// regex was not valid => handle it as a strict match
			if (searchOptions.ignoreCase()) {
				return value.toUpperCase().contains(pattern.toUpperCase());
			} else {
				return value.toUpperCase().contains(pattern.toUpperCase());
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SearchCriteria) {
			SearchCriteria other = (SearchCriteria) obj;
			return Objects.equals(pattern, other.pattern) && Objects.equals(searchOptions, other.searchOptions);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pattern, searchOptions);
	}
}
