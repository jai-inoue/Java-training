package ch03.ex12;

public class SimpleSortHarness extends SortHarness {

	SimpleSortHarness(Object[] values) {
		super(values);
	}

	@Override
	protected void doSort() {
		for (int i = 0; i < getDataLength(); i++) {
			for (int j = i + 1; j < getDataLength(); j++) {
				if (compare(i, j) > 0) {
					swap(i, j);				
				}
			}
		}
	}
}
