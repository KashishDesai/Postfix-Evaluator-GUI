import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

class PostfixInterpreterTest extends TestCase{
	Map<String, Double> context = new HashMap<String, Double>();
	String columns[] = { "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I" };
	
	@Test
	void testPostfixUsingNumbers() {
		for(String column : columns) {
			context.put(column, 0.0);
		}
		String postfix = "8 10 +";
		PostfixInterpreter i = new PostfixInterpreter();
		Expression expr = i.parse(postfix);
		double result = expr.interpret(context);
		assertEquals(result, 18.0);
	}
	
	@Test
	void testPostfixUsingVariables() {
		for(String column : columns) {
			context.put(column, 0.0);
		}
		context.replace("$A", 10.0);
		context.replace("$B", 20.0);
		context.replace("$C", 30.0);
		context.replace("$D", 40.0);

		String postfix = "$A $B * 3 sin 64 log / -";
		PostfixInterpreter i = new PostfixInterpreter();
		Expression expr = i.parse(postfix);
		double result = expr.interpret(context);
		assertEquals(result, 199.9660678107003);
	}
	
	@Test
	void testPostfixUsingEmptyExpression() {
		for(String column : columns) {
			context.put(column, 0.0);
		}
		context.replace("$A", 10.0);
		context.replace("$B", 20.0);
		context.replace("$C", 30.0);
		context.replace("$D", 40.0);

		String postfix = "";
		PostfixInterpreter i = new PostfixInterpreter();
		Expression expr = i.parse(postfix);
		double result = expr.interpret(context);
		assertEquals(result, 0.0);
	}
}
