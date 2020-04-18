public interface Observable {
	void add(Observer o);

	void remove(Observer o);

	void notifyObservers();
}
