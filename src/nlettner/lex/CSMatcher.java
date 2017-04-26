package nlettner.lex;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSMatcher implements MatchState<MatchResult, Pattern> {
	public Matcher matcher;
	protected MatchResult latestMatch;

	public CSMatcher(Matcher matcher) {
		this.matcher = matcher;
		this.latestMatch = null;
	}

	protected void updateStartRegion(int matchLength) {
			matcher.region(matcher.regionStart() + matchLength, matcher.regionEnd());
	}

	@Override
	public boolean checkRule(Pattern rule) {
		matcher.usePattern(rule);
		boolean matches = matcher.lookingAt();

		if (matches) {
			latestMatch = new StoredResult(matcher);
			updateStartRegion(matcher.group().length());
		}

		return matches;
	}

	@Override
	public MatchResult getMatch() {
		return latestMatch;
	}
}
