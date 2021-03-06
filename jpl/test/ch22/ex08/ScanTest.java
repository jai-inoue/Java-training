package ch22.ex08;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.junit.Test;

public class ScanTest {
	@Test
	public void normalTest_blankLine() {
		FileReader fr = null;
		List<String[]> actualResult = null;
		try {
			fr = new FileReader("/Users/inoue-keiichi/git/Java-training/jpl/src/ch22/ex08/sample_blankLine.csv");
			actualResult = Scan.readCSVTable(fr, 5);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		for (String[] strs : actualResult) {
			assertEquals(5, strs.length);
		}

		assertEquals("Ghana", actualResult.get(0)[0]);
		assertEquals("Greece", actualResult.get(0)[1]);
		assertEquals("India", actualResult.get(0)[2]);
		assertEquals("Mexico", actualResult.get(0)[3]);
		assertEquals("Korea", actualResult.get(0)[4]);
		assertEquals("Japan", actualResult.get(1)[0]);
		assertEquals("Germany", actualResult.get(1)[1]);
		assertEquals("Poland", actualResult.get(1)[2]);
		assertEquals("Russian", actualResult.get(1)[3]);
		assertEquals("Sweden", actualResult.get(1)[4]);
	}

	@Test
	public void errorTest_tooManyKanma() {
		FileReader fr = null;
		List<String[]> actualResult = null;
		try {
			fr = new FileReader(
					"/Users/inoue-keiichi/git/Java-training/jpl/src/ch22/ex08/sample_error_tooManyKanma.csv");
			actualResult = Scan.readCSVTable(fr, 5);
			fail();
		} catch (IOException e) {
			assertTrue(Objects.equals("too many kanma", e.getMessage()));
		}
	}
}
