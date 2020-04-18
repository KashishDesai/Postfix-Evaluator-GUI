import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class DataAndUndoListener implements TableModelListener, ActionListener {
	//stores key: "$A" value: $A (TableCellData Object reference) 
	private Map<String, TableCellData> tableCellReference;
	
	// stores key: "$A" value: 0(index)
	private Map<String, Integer> tableCellIndexReference = new HashMap<String, Integer>();
	
	//handles Memento (Undo Functionality)
	private MementoOriginator mementoOriginator;
	private Caretaker caretaker;
	
	private ArrayList<String> listOfObserversToAdd = new ArrayList<String>();
	private String columnNames[] = { "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I" };
	
	private CyclicDependencyGraph cyclicDependencyGraph = new CyclicDependencyGraph(columnNames); 
	private Object[][] dataToDisplayInTable = new Object[1][9];
	private JFrame frame;
	private JTable table;
	private int indexToIterate = 0;

	public DataAndUndoListener(Map<String, TableCellData> tableCellReference,
			 Object[][] data, JFrame frame, JTable table) {
		this.tableCellReference = tableCellReference;
		this.dataToDisplayInTable = data;
		this.frame = frame;
		this.table = table;
		for(String column : columnNames) {
			tableCellIndexReference.put(column, indexToIterate);
			indexToIterate += 1;
		}
		mementoOriginator = new MementoOriginator();
		caretaker = new Caretaker();
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
		TableModel model = (TableModel) e.getSource();
		String columnName = model.getColumnName(column);
		String changedData = model.getValueAt(row, column).toString();

		savingDataInMemento(columnName);

		handleRemovingObservers(columnName);

		updateNewDataValue(columnName, changedData);

		handleObserversCheckCycleDependency(columnName);

		table.repaint();
		displayTable(1);
	}

	private void handleObserversCheckCycleDependency(String columnName) {
		// Adding Observers
		listOfObserversToAdd = tableCellReference.get(columnName).evaluateObserversToAdd();
		for (String temp : listOfObserversToAdd) {
			cyclicDependencyGraph.addEdge(tableCellIndexReference.get(columnName), tableCellIndexReference.get(temp));
			tableCellReference.get(temp).add(tableCellReference.get(columnName));
		}
		//Checking Cyclic Dependencies Before Notifying Observers
		if (cyclicDependencyGraph.isCyclic()) {
			for (String cell : cyclicDependencyGraph.getNodesInCycle()) {
				dataToDisplayInTable[0][tableCellIndexReference.get(cell)] = "Error";
			}
		} else {
			tableCellReference.get(columnName).notifyObservers();
		}
	}

	private void updateNewDataValue(String columnName, String changedData) {
		if (changedData.matches("-?[0-9]*\\.[0-9]*") || changedData.matches("-?[0-9]+")) {
			tableCellReference.get(columnName).setValue(changedData);
		} else {
			tableCellReference.get(columnName).evaluatePostfix(changedData);
		}
	}

	private void handleRemovingObservers(String columnName) {
		// Removing Observers
		String previousPostfix = tableCellReference.get(columnName).getExpressionView();
		for (String removeObserver : previousPostfix.split(" ")) {
			if (removeObserver.equals("+") || removeObserver.equals("-") || removeObserver.equals("*")
					|| removeObserver.equals("/") || removeObserver.equals("log") || removeObserver.equals("sin")
					|| removeObserver.matches("-?[0-9]+") || removeObserver.equals("")) {
				continue;
			} else {
				tableCellReference.get(removeObserver).remove(tableCellReference.get(columnName));
			}
		}
	}

	private void savingDataInMemento(String columnName) {
		ArrayList<String> dataToSaveInMemento = new ArrayList<String>();
		dataToSaveInMemento.add(columnName);
		if (tableCellReference.get(columnName).getExpressionView() == ""
				&& tableCellReference.get(columnName).getValueView() == "0.0") {
			dataToSaveInMemento.add("0.0");
		} else if ((tableCellReference.get(columnName).getExpressionView() == "")
				&& (tableCellReference.get(columnName).getValueView() != "0.0")) {
			dataToSaveInMemento.add(tableCellReference.get(columnName).getValueView() + "");
		} else {
			dataToSaveInMemento.add(tableCellReference.get(columnName).getExpressionView());
		}
		mementoOriginator.set(dataToSaveInMemento);
		caretaker.addMemento(mementoOriginator.saveInMemento());
	}

	private void displayTable(int state) {
		// toggle between Value and Expression view is button is selected
		if (state == ItemEvent.SELECTED) {
			for (int i = 0; i < columnNames.length; i++) {
				if (dataToDisplayInTable[0][i] == "Error")
					continue;
				dataToDisplayInTable[0][i] = tableCellReference.get(columnNames[i]).getValueView();
				frame.setTitle("Postfix Value View");
			}
		} else {
			for (int i = 0; i < columnNames.length; i++) {
				if(dataToDisplayInTable[0][i] == "Error")
					continue;
				dataToDisplayInTable[0][i] = tableCellReference.get(columnNames[i]).getExpressionView();
				frame.setTitle("Postfix Expresion View");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mementoOriginator.restoreFromMemento(caretaker.undo());
		ArrayList<String> data = new ArrayList<String>();
		data = mementoOriginator.get();
		if ((data.get(1).matches("-?[0-9]*\\.[0-9]*") || data.get(1).matches("[0-9]+")))
			tableCellReference.get(data.get(0)).setValue(data.get(1));
		else {
			tableCellReference.get(data.get(0)).evaluatePostfix(data.get(1));
		}
		tableCellReference.get(data.get(0)).notifyObservers();
		table.repaint();
		displayTable(1);
	}

}
