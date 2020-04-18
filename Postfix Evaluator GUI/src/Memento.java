import java.util.ArrayList;

public class Memento {

	private ArrayList<String> data = new ArrayList<String>();

	public Memento(ArrayList<String> data) {
		 this.data = data;
	}

	public ArrayList<String> getState() {
		return data;
	}
}