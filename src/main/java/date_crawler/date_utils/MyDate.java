package date_crawler.date_utils;

import java.util.Calendar;

public class MyDate {

	private Calendar cal = Calendar.getInstance();

	public MyDate(int y, int m, int d) {
		set(y, m, d);
	}

	public MyDate() {

	}

	public MyDate(MyDate anotherMyDate) {
		set(anotherMyDate.getYear(), anotherMyDate.getMonth(), anotherMyDate.getDate());
	}

	protected Calendar getCalendar() {
		return this.cal;
	}

	public void set(int y, int m, int d) {
		cal.set(y, m - 1, d);
	}

	public void setYear(int y) {
		cal.set(Calendar.YEAR, y);
	}

	public void setMonth(int m) {
		cal.set(Calendar.MONTH, m - 1);
	}

	public void setDate(int d) {
		cal.set(Calendar.DATE, d);
	}

	public void nextDay() {
		nextNDay(1);
	}

	public void nextNDay(int n) {
		cal.add(Calendar.DATE, n);
	}

	public int getYear() {
		return this.cal.get(Calendar.YEAR);
	}

	public int getMonth() {
		return this.cal.get(Calendar.MONTH) + 1;
	}

	public int getDate() {
		return this.cal.get(Calendar.DATE);
	}

	public int compareTo(MyDate anotherMyDate) {
		return this.cal.compareTo(anotherMyDate.getCalendar());
	}

	public boolean before(MyDate anotherMyDate) {
		return this.cal.before(anotherMyDate.getCalendar());
	}

	public boolean after(MyDate anotherMyDate) {
		return this.cal.after(anotherMyDate.getCalendar());
	}

	public String toString() {
		return getYear() + "年" + getMonth() + "月" + getDate() + "日";
	}

	public static void main(String[] args) {
		MyDate d = new MyDate(2011, 2, 28);

		d.setDate(11);
		System.out.println(d);
		d.nextDay();
		System.out.println(d);
	}
}
