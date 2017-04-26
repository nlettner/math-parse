package nlettner.lex;

public class Rule<TT, R> {
	protected TT type;
	protected R rule;

	public Rule(TT type, R rule) {
		this.type = type;
		this.rule = rule;
	}

	public TT getType() {
		return type;
	}

	public R getRule() {
		return rule;
	}
}
