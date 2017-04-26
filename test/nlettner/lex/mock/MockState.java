package nlettner.lex.mock;

import nlettner.lex.MatchState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockState<RT, RL> implements MatchState<RT, RL> {

	protected Map<RL, Integer> trueMap;
	protected Set<RL> _gotMatchFrom;
	protected RL lastChecked;

	public MockState(Map<? extends RL, Integer> map) {
		this.trueMap = new HashMap<>(map);
		_gotMatchFrom = new HashSet<>();
	}

	@Override
	public boolean checkRule(RL rule) {
		Integer remaining = trueMap.get(rule);
		boolean result = false;

		lastChecked = rule;

		if (remaining != null) {
			trueMap.put(rule, remaining - 1);
			result = remaining > 0;
		}

		return result;
	}

	@Override
	public RT getMatch() {
		_gotMatchFrom.add(lastChecked);
		return null;
	}

	public Iterable<RL> matchedRules() {
		return _gotMatchFrom;
	}
}
