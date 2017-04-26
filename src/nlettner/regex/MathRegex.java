package nlettner.regex;

import java.util.regex.Pattern;

public class MathRegex {
	private static final String INTEGER_REGEX = "-?([1-9]\\d*|[0])";
	public static final Pattern integer = Pattern.compile(INTEGER_REGEX);

	private static final String PLUS_SIGN = "\\+";
	public static final Pattern plus = Pattern.compile(PLUS_SIGN);

	private static final String UNDERSCORE = "_";
	public static final Pattern answer = Pattern.compile(UNDERSCORE);

	private static final String WHITESPACE = "\\s+";
	public static final Pattern whitespace = Pattern.compile(WHITESPACE);
}