package nlettner.lex;

import nlettner.regex.MathRegex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class MathLexer extends RuleLexer<CharSequence, TokenType, MatchResult, MatchState<MatchResult, Pattern>, Pattern> {

	public static class MathRule extends Rule<TokenType, Pattern> {
		public MathRule(TokenType type, Pattern rule) {
			super(type, rule);
		}
	}

	@Override
	protected CSMatcher getMatchState(CharSequence input) {
		return new CSMatcher(Pattern.compile(".").matcher(input));
	}

	public MathLexer(Collection<MathRule> rules) {
		super(rules);
	}

	public static ArrayList<MathRule> getRules() {
		ArrayList<MathRule> rules = new ArrayList<>();
		rules.add(new MathRule(TokenType.INTEGER, MathRegex.integer));
		rules.add(new MathRule(TokenType.ADDITION_OP, MathRegex.plus));
		rules.add(new MathRule(null, MathRegex.whitespace));
		return rules;
	}
}
