import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;

public class Table {
	private JTable table;
	private JButton undoButton;
	private JToggleButton toggleButton;
	
	// stores cell names and their objects:  (key: "$A" value: $A (TableCellData Object))
	private Map<String, TableCellData> tableCellReference = new HashMap<String, TableCellData>();

	// data to be displayed in the JTable
	private Object[][] data = new Object[1][9];
	private String[] columnNames = { "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I" };
	
	private JFrame frame;

	public Table() {
		loadTableCellReferences();
		frame = new JFrame();
		table = new JTable(data, columnNames);
		table.setBounds(30, 40, 200, 300);
		frame.setTitle("Postfix Value/Expresion View");
		JScrollPane scrollPane = new JScrollPane(table);

		DataAndUndoListener dataAndUndoListener = new DataAndUndoListener(tableCellReference, data, frame, table);
		table.getModel().addTableModelListener(dataAndUndoListener);

		undoButton = new JButton("Undo");
		undoButton.addActionListener(dataAndUndoListener);

		toggleButton = new JToggleButton("Toggle Button");
		ToggleButtonListener toggleButtonListner = new ToggleButtonListener(tableCellReference, frame, data, table);
		toggleButton.addItemListener(toggleButtonListner);

		frame.add(undoButton, BorderLayout.LINE_END);
		frame.add(toggleButton, BorderLayout.SOUTH);
		frame.add(scrollPane);
		frame.setSize(1000, 200);
		frame.setVisible(true);
	}

	// stores cell names and their objects:  (key: "$A" value: $A (TableCellData Object))
	private void loadTableCellReferences() {
		for (int i = 0; i < columnNames.length; i++) {
			String tableCellKey = columnNames[i];
			tableCellReference.put(tableCellKey, new TableCellData(tableCellKey));
		}
	}

	public static void main(String[] args) {
		new Table();
	}
}
