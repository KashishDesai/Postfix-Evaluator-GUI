import java.util.ArrayList;

public class MementoOriginator {

	private ArrayList<String> data = new ArrayList<String>();
	
	public void set(ArrayList<String> data) {
		this.data = data;
	}

	public ArrayList<String> get() {
		return this.data;
	}

	// Creates a new Memento Object to store data

	public Memento saveInMemento() {
		return new Memento(data);
	}
	
	public void restoreFromMemento(Memento memento) {

		this.data = memento.getState();
	}
}