package nlettner.lex;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

import static nlettner.util.test.Matchers.hasItemsInOrder;
import static org.junit.Assert.assertThat;

public class MathLexerIntegrationTest {

	protected MathLexer lex;

	@Before
	public void setup() {
		lex = new MathLexer(MathLexer.getRules());
	}

	public ArrayList<Pair<TokenType, MatchResult>> lexExpression(String expression) {
		ArrayList<Pair<TokenType, MatchResult>> tokenList = new ArrayList<>();
		Iterator<Pair<TokenType, MatchResult>> tokenIterator = lex.lex(expression);

		while (tokenIterator.hasNext()) {
			tokenList.add(tokenIterator.next());
		}

		return tokenList;
	}

	public static List<TokenType> getTokenTypeList(
			List<Pair<TokenType, MatchResult>> tokenList) {
		return tokenList.stream().map(Pair::getKey).collect(Collectors.toList());
	}

	public static List<MatchResult> getMatchResults(
			List<Pair<TokenType, MatchResult>> tokenList) {
		return tokenList.stream().map(Pair::getValue).collect(Collectors.toList());
	}

	public static List<String> getMatchGroup(List<MatchResult> matches) {
		return matches.stream().map(MatchResult::group).collect(Collectors.toList());
	}

	public void tokenTypeTest(String expression, TokenType[] tokenTypes) {
		assertThat(
				getTokenTypeList(lexExpression(expression)),
				hasItemsInOrder(tokenTypes));
	}

	@Test
	public void lex_2_plus_3() {
		tokenTypeTest("2 + 3", new TokenType[] {
				TokenType.INTEGER,
				TokenType.ADDITION_OP,
				TokenType.INTEGER});
	}

	@Test
	public void lex_5_plus_7_plus_11() {
		tokenTypeTest("5 + 7 + 11", new TokenType[] {
				TokenType.INTEGER,
				TokenType.ADDITION_OP,
				TokenType.INTEGER,
				TokenType.ADDITION_OP,
				TokenType.INTEGER});
	}

	public void matchResultTest(String expression, String[] expectedMatches) {
		assertThat(
				getMatchGroup(getMatchResults(lexExpression(expression))),
				hasItemsInOrder(expectedMatches));
	}

	@Test
	public void lex_11_plus_13() {
		matchResultTest("11 + 13", new String[] {"11", "+", "13"});
	}

	@Test
	public void lex_17_plus_19_plus_23() {
		matchResultTest("17 + 19 + 23", new String[] {"17", "+", "19", "+", "23"});
	}
}
