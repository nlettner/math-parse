package nlettner.lex;

import javafx.util.Pair;
import nlettner.lex.mock.MockState;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNot.not;
import static nlettner.util.test.Matchers.hasAnyOf;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class MathLexerUnitTest {

	protected MathLexer lex;
	protected MathLexer simple;
	protected static final Pattern[] patterns = new Pattern[]{
			Pattern.compile("1"),
			Pattern.compile("\\+"),
			Pattern.compile("a"),
			Pattern.compile(" "),
			Pattern.compile("!")};

	@Before
	public void setup() {
		lex = new MathLexer(MathLexer.getRules());
		List<MathLexer.MathRule> simpleRules = new ArrayList<>();
		simpleRules.add(new MathLexer.MathRule(TokenType.INTEGER, patterns[0]));
		simpleRules.add(new MathLexer.MathRule(null, patterns[1]));
		simpleRules.add(new MathLexer.MathRule(null, patterns[2]));
		simpleRules.add(new MathLexer.MathRule(null, patterns[3]));
		simpleRules.add(new MathLexer.MathRule(TokenType.ADDITION_OP, patterns[4]));
		simple = new MathLexer(simpleRules);
	}

	@Test
	public void tokenIterator_isNotNull() {
		assertNotEquals(null, lex.lex("4+4"));
	}

	@Test
	public void tokenIterator_hasNext_notCalled() {
		Iterator<Pair<TokenType, MatchResult>> tokens = lex.lex("4 + 4");
		assertNotEquals(null, tokens.next());
	}

	public static MockState<MatchResult, Pattern> getTrueMockState(Integer ... totalTrueReturnsPerRule) {
		Map<Pattern, Integer> map = new HashMap<>();

		for (int index = 0; index < totalTrueReturnsPerRule.length; index++) {
			map.put(patterns[index], totalTrueReturnsPerRule[index]);
		}

		return new MockState<>(map);
	}

	@Test
	public void getNextToken_firstRule() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(1, 1, 1, 1, 1);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);

		assertEquals(TokenType.INTEGER, token.getKey());

		assertThat(mockState.matchedRules(), hasItems(patterns[0]));
		assertThat(mockState.matchedRules(),
				not(hasAnyOf(patterns[1], patterns[2], patterns[3], patterns[4])));
	}

	@Test
	public void getNextToken_lastRule() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(0, 0, 0, 0, 1);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);
		assertEquals(TokenType.ADDITION_OP, token.getKey());

		assertThat(mockState.matchedRules(), hasItems(patterns[4]));
		assertThat(mockState.matchedRules(),
				not(hasAnyOf(patterns[0], patterns[1], patterns[2], patterns[3])));
	}

	@Test
	public void getNextToken_bypassOneNullRule() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(0, 0, 0, 1, 1);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);
		assertEquals(TokenType.ADDITION_OP, token.getKey());

		assertThat(mockState.matchedRules(), hasItems(patterns[3], patterns[4]));
		assertThat(mockState.matchedRules(),
				not(hasAnyOf(patterns[0], patterns[1], patterns[2])));
	}

	@Test
	public void getNextToken_bypassMultipleNullRules() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(0, 1, 1, 1, 1);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);
		assertEquals(TokenType.ADDITION_OP, token.getKey());

		assertThat(mockState.matchedRules(),
				hasItems(patterns[1], patterns[2], patterns[3], patterns[4]));
		assertThat(mockState.matchedRules(), not(hasAnyOf(patterns[0])));
	}

	@Test
	public void getNextToken_noRules() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(0, 0, 0, 0, 0);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);
		assertEquals(null, token);

		assertThat(mockState.matchedRules(),
				not(hasAnyOf(patterns[0], patterns[1], patterns[2], patterns[3], patterns[4])));
	}

	@Test
	public void getNextToken_acceptNullTokenType() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(0, 1, 0, 0, 0);
		simple.ignoreNullTokenTypes = false;
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);
		assertEquals(null, token.getKey());

		assertThat(mockState.matchedRules(), hasItems(patterns[1]));
	}

	@Test
	public void getNextToken_noRules_whileAcceptingNullTokenTypes() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(0, 0, 0, 0, 0);
		simple.ignoreNullTokenTypes = false;
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);
		assertEquals(null, token);

		assertThat(mockState.matchedRules(),
				not(hasAnyOf(patterns[0], patterns[1], patterns[2], patterns[3], patterns[4])));
	}

	@Test
	public void getNextToken_multipleCalls() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(1, 0, 1, 0, 1);
		simple.getNextToken(mockState);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);

		assertEquals(TokenType.ADDITION_OP, token.getKey());

		assertThat(mockState.matchedRules(),
				hasItems(patterns[0], patterns[2], patterns[4]));
	}

	@Test
	public void getNextToken_repeatedTokens() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(2, 0, 0, 0, 0);
		simple.getNextToken(mockState);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);

		assertEquals(TokenType.INTEGER, token.getKey());

		assertThat(mockState.matchedRules(), hasItem(patterns[0]));
		assertThat(mockState.matchedRules(),
				not(hasAnyOf(patterns[1], patterns[2], patterns[3], patterns[4])));
	}

	@Test
	public void getNextToken_exhaustedInput() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(1, 0, 0, 0 ,0);
		simple.getNextToken(mockState);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);

		assertEquals(null, token);

		assertThat(mockState.matchedRules(), hasItem(patterns[0]));
	}

	@Test
	public void getNextToken_multipleNullTokenTypes() {
		MockState<MatchResult, Pattern> mockState = getTrueMockState(0, 1, 1, 0, 0);
		simple.ignoreNullTokenTypes = false;
		simple.getNextToken(mockState);
		Pair<TokenType, MatchResult> token = simple.getNextToken(mockState);

		assertEquals(null, token.getKey());
		assertThat(mockState.matchedRules(), hasItems(patterns[1], patterns[2]));
	}
}