package net.ptidej.valhalla.valueclass;

public value class Substring implements CharSequence {
	private String str;
	private int start;
	private int end;

	public Substring(final String str, final int start, final int end) {
		Substring.checkBounds(start, end, str.length());
		this.str = str;
		this.start = start;
		this.end = end;
	}

	public int length() {
		return this.end - this.start;
	}

	public char charAt(final int i) {
		checkBounds(0, i, length());
		return str.charAt(start + i);
	}

	public Substring subSequence(final int s, final int e) {
		Substring.checkBounds(s, e, length());
		return new Substring(str, start + s, start + e);
	}

	public String toString() {
		return str.substring(start, end);
	}

	private static void checkBounds(final int start, final int end, final int length) {
		if (start < 0 || end < start || length < end) {
			throw new IndexOutOfBoundsException();
		}
	}
}