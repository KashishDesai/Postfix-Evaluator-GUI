import java.util.ArrayDeque;
import java.util.Map;

public class PostfixInterpreter {

	private char characterAtPostitionOne;

	private Expression parseToken(String token, ArrayDeque<Expression> stack) {
		Expression left, right;
		try {
			characterAtPostitionOne = token.charAt(1);
		} catch (StringIndexOutOfBoundsException e) {

		}

		switch (token) {
		case "+":
			right = stack.pop();
			left = stack.pop();
			return new Expression() {
				@Override
				public double interpret(Map<String, Double> context) {
					return left.interpret(context) + right.interpret(context);
				}
			};
		case "-":
			right = stack.pop();
			left = stack.pop();
			return new Expression() {
				@Override
				public double interpret(Map<String, Double> context) {
					return left.interpret(context) - right.interpret(context);
				}
			};

		case "*":
			right = stack.pop();
			left = stack.pop();
			return new Expression() {
				@Override
				public double interpret(Map<String, Double> context) {
					return left.interpret(context) * right.interpret(context);
				}
			};
		case "/":
			right = stack.pop();
			left = stack.pop();
			return new Expression() {
				@Override
				public double interpret(Map<String, Double> context) {
					return left.interpret(context) / right.interpret(context);
				}
			};
		case "log":
			right = stack.pop();
			return new Expression() {
				@Override
				public double interpret(Map<String, Double> context) {
					return Math.log(right.interpret(context));
				}
			};
		case "sin":
			right = stack.pop();
			return new Expression() {
				@Override
				public double interpret(Map<String, Double> context) {
					return Math.sin(right.interpret(context));
				}
			};
		case "":
			return new Expression() {
				@Override
				public double interpret(Map<String, Double> context) {
					return context.getOrDefault(token, 0.0);
				}
			};
		default:
			if (token.matches("-?[0-9]+")) {
				return new Expression() {
					@Override
					public double interpret(Map<String, Double> context) {
						return context.getOrDefault(token, Double.parseDouble(token));
					}
				};
			} else if (wrongCellName(token)) {
				return new Expression() {
					@Override
					public double interpret(Map<String, Double> context) {
						return context.getOrDefault(token, 0.0);
					}
				};
			} else {
				throw new IllegalArgumentException("Invalid Variable, Can only put Variable from $A - $I");
			}

		}
	}

	private boolean wrongCellName(String token) {
		if (token.length() == 2)
			return token.charAt(1) >= 'A' && characterAtPostitionOne <= 'I'
					|| (characterAtPostitionOne >= 'a' && characterAtPostitionOne <= 'i');
		return false;
	}

	public Expression parse(String expression) {
		ArrayDeque<Expression> stack = new ArrayDeque<Expression>();
		for (String token : expression.split(" ")) {
			stack.push(parseToken(token, stack));
		}
		return stack.pop();
	}
}
