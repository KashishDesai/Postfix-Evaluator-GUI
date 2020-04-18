import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.junit.jupiter.api.Test;

class DataAndUndoListenerTest {
	private JTable table;
	private JButton undoBut;
	private Map<String, TableCellData> tableCellReference = new HashMap<String, TableCellData>();

	private Object[][] data = new Object[1][9];
	private String[] columnNames = { "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I" };
	private JFrame frame;

	@Test
	void test() {
		frame = new JFrame();
		table = new JTable(data, columnNames);
		table.setBounds(30, 40, 200, 300);
		frame.setTitle("Postfix Value/Expresion View");
		JScrollPane scrollPane = new JScrollPane(table);
		for (int i = 0; i < columnNames.length; i++) {
			String tableCellKey = columnNames[i];
			tableCellReference.put(tableCellKey, new TableCellData(tableCellKey));
		}
		DataAndUndoListener dataAndUndoListener = new DataAndUndoListener(tableCellReference, data, frame, table);
		table.getModel().addTableModelListener(dataAndUndoListener);

		undoBut = new JButton("Undo");
		undoBut.addActionListener(dataAndUndoListener);

		frame.add(undoBut, BorderLayout.LINE_END);
		frame.add(scrollPane);
		frame.setSize(1000, 200);
		frame.setVisible(true);

		table.setValueAt(30, 0, 0);
		assertEquals(table.getValueAt(0, 0), "30.0");

		table.setValueAt("$A", 0, 0);
		assertEquals(table.getValueAt(0, 0), "Error");

		table.setValueAt(20, 0, 1);
		assertEquals(table.getValueAt(0, 1), "20.0");

		table.setValueAt("", 0, 2);
		assertEquals(table.getValueAt(0, 2), "0.0");

		table.setValueAt(0.8, 0, 3);
		assertEquals(table.getValueAt(0, 3), "0.8");

		table.setValueAt(-20, 0, 4);
		assertEquals(table.getValueAt(0, 4), "-20.0");

		table.setValueAt(30, 0, 5);
		assertEquals(table.getValueAt(0, 5), "30.0");

		table.setValueAt("$B $F +", 0, 6);
		assertEquals(table.getValueAt(0, 6), "50.0");

		table.setValueAt("$G", 0, 7);
		assertEquals(table.getValueAt(0, 7), "50.0");
		
		table.setValueAt("$B", 0, 6);
		assertEquals(table.getValueAt(0, 7), "20.0");
	}
}
