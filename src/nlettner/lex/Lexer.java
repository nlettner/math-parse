package nlettner.lex;

import javafx.util.Pair;

import java.util.Iterator;

public interface Lexer<I, TT extends Enum<TT>, R> {
	Iterator<Pair<TT, R>> lex(I input);
}
