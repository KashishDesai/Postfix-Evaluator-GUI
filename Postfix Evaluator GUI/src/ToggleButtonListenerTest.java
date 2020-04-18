import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;

import org.junit.jupiter.api.Test;

class ToggleButtonListenerTest {
	private JTable table;
	private JToggleButton toggleButton;
	// stores key: "$A" value: $A
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
		ToggleButtonListener toggleButtonListner = new ToggleButtonListener(tableCellReference, frame, data, table);
		toggleButton = new JToggleButton();
		toggleButton.addItemListener(toggleButtonListner);
		
		frame.add(toggleButton, BorderLayout.SOUTH);
		frame.add(scrollPane);
		frame.setSize(1000, 200);
		frame.setVisible(true);
		
		table.setValueAt(30, 0, 0);
		assertEquals(table.getValueAt(0, 0), 30);
		
		table.setValueAt("$A", 0, 0);
		assertEquals(table.getValueAt(0, 0), "$A");
		
		table.setValueAt("", 0, 0);
		assertEquals(table.getValueAt(0, 0), 30);
	}

}
