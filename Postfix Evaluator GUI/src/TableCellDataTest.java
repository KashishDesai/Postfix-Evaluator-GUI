import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

class TableCellDataTest extends TestCase {

	TableCellData $A = new TableCellData("$A");
	TableCellData $B = new TableCellData("$B");
	TableCellData $C = new TableCellData("$C");
	TableCellData $D = new TableCellData("$D");

	@Test
	void testTableCellDataFun() {
		$A.setValue("100");
		assertEquals($A.getValueView(), "100.0");

		$B.setValue("200");
		assertEquals($B.getValueView(), "200.0");

		$A.add($C);
		$B.add($C);
		$C.evaluatePostfix("$A $B +");
		assertEquals($C.getValueView(), "300.0");
		assertEquals($C.getExpressionView(), "$A $B +");

		$C.add($D);
		$D.evaluatePostfix("$C");
		assertEquals($C.getValueView(), $D.getValueView());

		assertEquals($D.toString(), "$D");
		
		$A.setValue("10");
		assertEquals($C.getValueView(), "210.0");
		assertEquals($D.getValueView(), "210.0");
		
		ArrayList<String> observersToAdd = $C.evaluateObserversToAdd();
		ArrayList<String> actual = new ArrayList<String>();
		actual.add("$A");
		actual.add("$B");
		assertEquals(observersToAdd, actual);
	}

}
