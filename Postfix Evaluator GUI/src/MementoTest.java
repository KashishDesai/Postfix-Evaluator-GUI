import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class MementoTest {
	private MementoOriginator originator = new MementoOriginator();
	private Caretaker caretaker = new Caretaker();
	ArrayList<String> dataToSave1 = new ArrayList<String>();
	ArrayList<String> dataToSave2 = new ArrayList<String>();
	
	@Test
	void test() {
		dataToSave1.add("$A");
		dataToSave1.add("20");
		
		originator.set(dataToSave1);
		caretaker.addMemento(originator.saveInMemento());
		
		dataToSave2.add("$B");
		dataToSave2.add("40");
		
		originator.set(dataToSave2);
		caretaker.addMemento(originator.saveInMemento());
		
		originator.restoreFromMemento(caretaker.undo());
		ArrayList<String> dataToBeRestored1 = new ArrayList<String>();
		dataToBeRestored1 = originator.get();
		assertEquals(dataToBeRestored1, dataToSave2);
		
		originator.restoreFromMemento(caretaker.undo());
		ArrayList<String> dataToBeRestored2 = new ArrayList<String>();
		dataToBeRestored2 = originator.get();
		assertEquals(dataToBeRestored2, dataToSave1);

	}

}
