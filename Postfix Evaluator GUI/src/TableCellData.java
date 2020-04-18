
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableCellData implements Observer, Observable {
	private String postfix = "";
	private final static Map<String, Double> valueOfDataCells = new HashMap<String, Double>();
	private double result;
	private String $key;
	private List<Observer> listOfObservers = new ArrayList<Observer>();
	private ArrayList<String> stringListOfObservers;

	TableCellData(String $key) {
		this.$key = $key;
		valueOfDataCells.put($key, 0.0);
	}

	public void evaluatePostfix(String postfixExpression) {
		this.postfix = postfixExpression;
		PostfixInterpreter i = new PostfixInterpreter();
		Expression expr = i.parse(postfix);
		result = expr.interpret(valueOfDataCells);
		valueOfDataCells.replace($key, result);
		this.notifyObservers();
	}

	public ArrayList<String> evaluateObserversToAdd() {
		stringListOfObservers = new ArrayList<String>();
		for (String observer : postfix.split(" ")) {
			if (observer.equals("+") || observer.equals("-") || observer.equals("*") || observer.equals("/")
					|| observer.equals("log") || observer.equals("sin") || observer.matches("-?[0-9]+")
					|| observer.equals("")) {
				continue;
			} else {
				stringListOfObservers.add(observer);
			}
		}
		return stringListOfObservers;
	}

	public void setValue(String value) {
		Double intValue = Double.parseDouble(value);
		
		valueOfDataCells.replace($key, intValue);
		postfix = "";
		this.notifyObservers();
	}

	public String getValueView() {
		return valueOfDataCells.get($key).toString();
	}

	public String getExpressionView() {
		return postfix;
	}

	@Override
	public String toString() {
		return $key;
	}

	@Override
	public void add(Observer o) {
		listOfObservers.add(o);
	}

	@Override
	public void remove(Observer o) {
		listOfObservers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : listOfObservers) {

			observer.update();
		}
	}

	@Override
	public void update() {
		evaluatePostfix(this.postfix);
	}
}
