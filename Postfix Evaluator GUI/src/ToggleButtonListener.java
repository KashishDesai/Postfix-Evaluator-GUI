import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTable;

public class ToggleButtonListener implements ItemListener {
	private JFrame frame;
	private Object[][] dataToDisplayInTable = new Object[1][9];
	private String columnNames[] = { "$A", "$B", "$C", "$D", "$E", "$F", "$G", "$H", "$I" };
	private Map<String, TableCellData> tableCellReference = new HashMap<String, TableCellData>();
	private JTable table;
	private int stateOfToggleButton;

	public ToggleButtonListener(Map<String, TableCellData> tableCellReference, JFrame frame, Object[][] dataToDisplayInTable,
			JTable table) {
		this.tableCellReference = tableCellReference;
		this.frame = frame;
		this.dataToDisplayInTable = dataToDisplayInTable;
		this.table = table;
	}

	@Override
	public void itemStateChanged(ItemEvent itemEvent) {
		stateOfToggleButton = itemEvent.getStateChange();
		displayTable(stateOfToggleButton);
		table.repaint();
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
			// Data to be displayed in the JTable
			for (int i = 0; i < columnNames.length; i++) {
				if (dataToDisplayInTable[0][i] == "Error")
					continue;
				dataToDisplayInTable[0][i] = tableCellReference.get(columnNames[i]).getExpressionView();
				frame.setTitle("Postfix Expresion View");
			}
		}

	}
}
