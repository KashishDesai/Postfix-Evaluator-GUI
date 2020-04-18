import java.util.ArrayList;

class Caretaker {

	// All the Mementos are saved here
	private ArrayList<Memento> history;
	private int currentState = -1;
	
	public Caretaker() {
		this.history = new ArrayList<Memento>();
	}

	public void addMemento(Memento m) {
		history.add(m);
		currentState = this.history.size() -1;
	}

	public Memento getMemento(int index) {
		return history.get(index);
	}
	
	public Memento undo() {
		if(currentState <= 0) {
			currentState = 0;
			return getMemento(currentState);
		}
		return getMemento(currentState--);
	}
}