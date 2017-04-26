package nlettner.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nlettner.regex.MathRegex.integer;
import static nlettner.regex.MathRegex.plus;
import static nlettner.regex.MathRegex.answer;
import static nlettner.regex.MathRegex.whitespace;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MathRegexUnitTest {

	protected final String INT_MAX_STRING = Integer.toString(Integer.MAX_VALUE);
	protected final String INT_MIN_STRING = Integer.toString(Integer.MIN_VALUE);

	protected final String LONG_MAX_STRING = Long.toString(Long.MAX_VALUE);
	protected final String LONG_MIN_STRING = Long.toString(Long.MIN_VALUE);

	public void regexTest(Pattern pattern, String input, boolean shouldMatch) {
		Matcher matcher = pattern.matcher(input);
		if (shouldMatch) {
			assertTrue(matcher.matches());
		} else {
			assertFalse(matcher.matches());
		}
	}

	interface Converter<T> {
		T convert(String input) throws Exception;
	}

	public static class IntConverter implements Converter<Integer> {
		@Override
		public Integer convert(String input) {
			return Integer.parseInt(input);
		}
	}

	public void canParse(String input, Converter c) throws Exception {
		c.convert(input);
	}

	public void valid_integer_test(String input) throws Exception{
		regexTest(integer, input, true);
		canParse(input, new IntConverter());
	}

	@Test
	public void integer_neg2() throws Exception {
		valid_integer_test("-2");
	}

	@Test
	public void integer_3() throws Exception {
		valid_integer_test("3");
	}

	@Test
	public void integer_INT_MAX() throws Exception {
		valid_integer_test(INT_MAX_STRING);
	}

	@Test
	public void integer_INT_MIN() throws Exception {
		valid_integer_test(INT_MIN_STRING);
	}

	@Test
	public void integer_0() throws Exception {
		valid_integer_test("0");
	}

	@Test(expected = NumberFormatException.class)
	public void integer_Long_MAX_VALUE() throws Exception {
		valid_integer_test(LONG_MAX_STRING);
	}

	@Test(expected = NumberFormatException.class)
	public void integer_Long_MIN_VALUE() throws Exception {
		valid_integer_test(LONG_MIN_STRING);
	}

	@Test
	public void integer_octalNumber() {
		regexTest(integer, "042573", false);
	}

	@Test
	public void integer_0dot1() {
		regexTest(integer, "0.1", false);
	}

	@Test
	public void integer_imaginary() {
		regexTest(integer, "5i", false);
	}

	@Test
	public void integer_word() {
		regexTest(integer, "test", false);
	}

	@Test
	public void integer_whitespace() {
		regexTest(integer, "\t", false);
	}

	@Test
	public void integer_symbol() {
		regexTest(integer, "!", false);
	}

	@Test
	public void plus_plus() {
		regexTest(plus, "+", true);
	}

	@Test
	public void plus_dash() {
		regexTest(plus, "-", false);
	}

	@Test
	public void plus_word() {
		regexTest(plus, "test", false);
	}

	@Test
	public void plus_star() {
		regexTest(plus, "*", false);
	}

	@Test
	public void answer_underscore() {
		regexTest(answer, "_", true);
	}

	@Test
	public void answer_dash() {
		regexTest(answer, "-", false);
	}

	@Test
	public void answer_bang() {
		regexTest(answer, "!", false);
	}

	@Test
	public void answer_word() {
		regexTest(answer, "test", false);
	}

	@Test
	public void whitespace_space() {
		regexTest(whitespace, " ", true);
	}

	@Test
	public void whitespace_multiple_space() {
		regexTest(whitespace, "			  ", true);
	}

	@Test
	public void whitespace_tab() {
		regexTest(whitespace, "\t", true);
	}

	@Test
	public void whitespace_newline() {
		regexTest(whitespace, "\n", true);
	}

	@Test
	public void whitespace_multiple_various() {
		regexTest(whitespace, " \t\n\t \n", true);
	}

	@Test
	public void whitespace_letter() {
		regexTest(whitespace, "a", false);
	}

	@Test
	public void whitespace_number() {
		regexTest(whitespace, "3", false);
	}

	@Test
	public void whitespace_symbol() {
		regexTest(whitespace, "!", false);
	}
}
