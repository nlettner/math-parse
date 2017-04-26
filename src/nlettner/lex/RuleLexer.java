package nlettner.lex;

import com.sun.istack.internal.NotNull;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class RuleLexer<I, // Input type
		TT extends Enum<TT>, // Token type
		RT, // Match results
		S extends MatchState<RT, RL>, // Stateful matcher type
		RL> // Rule type
		implements Lexer<I, TT, RT> {
	protected List<Rule<TT, RL>> rules;
	protected boolean ignoreNullTokenTypes;

	public RuleLexer(Collection<? extends Rule<TT, RL>> rules,
					 boolean ignoreNullTokenTypes) {
		this.rules = new ArrayList<>(rules);
		this.ignoreNullTokenTypes = ignoreNullTokenTypes;
	}

	public RuleLexer(Collection<? extends Rule<TT, RL>> rules) {
		this(rules, true);
	}

	protected abstract S getMatchState(I input);

	protected Pair<TT, RT> getNextToken(S state) {
		Pair<TT, RT> token = null;
		boolean nullTokenType = true;

		for (int i = 0; i < rules.size(); i++) {
			Rule<TT, RL> rule = rules.get(i);

			if (state.checkRule(rule.getRule())) {
				token = new Pair<>(rule.getType(), state.getMatch());
				nullTokenType = rule.getType() == null;
				break;
			}
		}

		boolean noTokenFound = token == null;
		if (noTokenFound || !nullTokenType || !ignoreNullTokenTypes) {
			return token;
		} else {
			// Current token was ignored; get next token in the input.
			return getNextToken(state);
		}
	}

	protected class TokenIterator implements Iterator<Pair<TT, RT>> {
		protected Pair<TT, RT> nextToken;
		protected RuleLexer<I, TT, RT, S, RL> lexer;
		protected S matchState;

		public TokenIterator(RuleLexer<I, TT, RT, S, RL> lexer, I input) {
			this.lexer = lexer;
			this.matchState = lexer.getMatchState(input);
		}

		@Override
		public boolean hasNext() {
			if (nextToken == null) {
				nextToken = lexer.getNextToken(matchState);
			}
			return nextToken != null;
		}

		@Override
		public Pair<TT, RT> next() {
			Pair<TT, RT> out = nextToken;

			if (out == null) {
				// Perhaps .hasNext() wasn't called?
				out = lexer.getNextToken(matchState);
			} else {
				nextToken = null;
			}

			return out;
		}
	}

	@Override @NotNull
	public Iterator<Pair<TT, RT>> lex(I input) {
		return new TokenIterator(this, input);
	}
}
