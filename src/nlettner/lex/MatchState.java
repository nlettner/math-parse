package nlettner.lex;

public interface MatchState<O, R> {
	boolean checkRule(R rule);
	O getMatch();
}
