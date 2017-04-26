package nlettner.lex;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public class StoredResult implements MatchResult {
	protected int[][] groups;
	protected String match;
	protected int _groupCount;

	protected void putGroupBoundary(int boundary, int group, Bound bound) {
		groups[group][bound.index] = boundary;
	}

	protected int getGroupBoundary(int group, Bound bound) {
		return groups[group][bound.index];
	}

	public StoredResult(Matcher matcher) {
		_groupCount = matcher.groupCount();
		match = matcher.group();

		groups = new int[_groupCount + 1][2];
		for (int group = 0; group <= _groupCount; group++) {
			putGroupBoundary(matcher.start(group), group, Bound.START);
			putGroupBoundary(matcher.end(group), group, Bound.END);
		}
	}

	protected enum Bound{
		START(0),
		END(1);

		public int index;

		Bound(int index) {
			this.index = index;
		}
	}

	public int end() {
		return end(0);
	}

	public int end(int group) {
		return getGroupBoundary(group, Bound.END);
	}


	public int start() {
		return start(0);
	}

	public int start(int group) {
		return getGroupBoundary(group, Bound.START);
	}

	public String group() {
		return match;
	}

	public String group(int group) {
		int matchStart = getGroupBoundary(0, Bound.START);
		int _start = getGroupBoundary(group, Bound.START) - matchStart;
		int _end = getGroupBoundary(group, Bound.END) - matchStart;
		return match.substring(_start, _end);
	}

	public int groupCount() {
		return _groupCount;
	}
}
