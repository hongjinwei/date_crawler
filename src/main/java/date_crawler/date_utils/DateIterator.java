package date_crawler.date_utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateIterator {

	private int startYear = 1;
	private int statrtMonth = 2;
	private int statrtDay = 11;

	private int endYear = 2101;
	private int endMonth = 1;
	private int endDay = 28;

	public boolean nothingLeft = false;

	// 每隔28天
	private int dayGap = 28;

	private MyDate now;
	private MyDate end = new MyDate(endYear, endMonth, endDay);
	private MyDate start = new MyDate(startYear, statrtMonth, statrtDay);

	private DateIterator() {
	}

	private DateIterator(int dayGap) {
		this.dayGap = dayGap;
	}

	public void setStartDate(int y, int m, int d) {
		this.startYear = y;
		this.statrtMonth = m;
		this.statrtDay = d;
		start.set(y, m, d);
	}

	public void setEndDate(int y, int m, int d) {
		this.endYear = y;
		this.endMonth = m;
		this.endDay = d;
		end.set(y, m, d);
	}

	public void setDate(int y, int m, int d) {
		now.set(y, m, d);
	}

	public void setDate(MyDate date) {
		int y = date.getYear();
		int m = date.getMonth();
		int d = date.getDate();
		setDate(y, m, d);
	}

	public int getYear() {
		return now.getYear();
	}

	public int getMonth() {
		return now.getMonth();
	}

	public int getDate() {
		return now.getDate();
	}

	public static DateIterator custom() {
		DateIterator con = new DateIterator();
		return con;
	}

	public static DateIterator custom(int dayGap) {
		DateIterator con = new DateIterator(dayGap);
		return con;
	}

	public boolean hasNext() {
		if (now == null) {
			return start.compareTo(end) <= 0;
		}
		return now.compareTo(end) <= 0 && !nothingLeft;
	}

	public DateIterator next() {
		if (now == null) {
			now = new MyDate(startYear, statrtMonth, statrtDay);
		} else {
			now.nextNDay(dayGap);
			if (nothingLeft) {
				return null;
			}
			if (now.after(end)) {
				this.setDate(end);
				nothingLeft = true;
			}
		}
		return this;
	}

	public MyDate getNow() {
		return now;
	}

	public static void showDate(Calendar cal2) {
		int mon = cal2.get(Calendar.MONTH) + 1;
		System.out.println(cal2.get(Calendar.YEAR) + " " + mon + " " + cal2.get(Calendar.DATE));
	}

	public static void test1() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Date date = new Date();
		// System.out.println(date);
		// System.out.println(date.getDate());
		// String time = sdf.format(new Date());
		// time = "1-2-3";
		// System.out.println(time);

		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal2.set(1, 1, 11);
		// cal.setTime(sdf.parse(time));
		cal.set(2001, 0, 28);
		// cal.add(Calendar.MONTH, 3);
		// System.out.println(cal2.after(cal));
		showDate(cal2);
		cal2.add(Calendar.DATE, 28);
		showDate(cal2);
	}

	public static void test2() {
		DateIterator iter = DateIterator.custom();

		while (iter.hasNext()) {
			DateIterator cal = iter.next();
			System.out.println(cal.getYear() + " " + cal.getMonth() + " " + cal.getDate());
		}
	}

	public static void main(String[] args) throws Exception {

		test2();

	}
}
