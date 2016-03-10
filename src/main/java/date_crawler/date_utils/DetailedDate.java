package date_crawler.date_utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailedDate {

	// 公历日期
	private MyDate date = new MyDate();

	// 一个公历日可以对应多个阴历日子，主要是年号可能有多个
	List<LunarDate> lunarDates = new ArrayList<LunarDate>();


	public int getYear() {
		return date.getYear();
	}

	public void setYear(int year) {
		this.date.setYear(year);
	}

	public int getMonth() {
		return date.getMonth();
	}

	public void setMonth(int month) {
		this.date.setMonth(month);
	}

	public int getDate() {
		return this.date.getDate();
	}

	public void setDate(int date) {
		this.date.setDate(date);
	}

	public void setNow(MyDate date) {
		MyDate md = new MyDate(date);
		this.date = md;
	}

	public MyDate getNow() {
		return this.date;
	}

	public void addLunarDate(LunarDate ld) {
		this.lunarDates.add(ld);
	}

	public void show() {
		for (LunarDate ld : lunarDates) {
			System.out.println(getYear() + "年" + getMonth() + "月" + getDate() + "日 " + ld);
		}
	}

	public String toString() {
		return getYear() + "年" + getMonth() + "月" + getDate() + "日 " + lunarDates;
	}

	public LunarDate lookupLunarDateByEmpireName(String empireName) {
		for (LunarDate ld : lunarDates) {
			if (ld.getEmpireName().equals(empireName)) {
				return ld;
			}
		}
		return null;
	}

	public boolean equalEmpireNameTo(DetailedDate anotherdd) {
		if (this.lunarDates.size() != anotherdd.lunarDates.size()) {
			return false;
		}

		Set<String> empireNameSet = new HashSet<String>();
		for (LunarDate ld : lunarDates) {
			empireNameSet.add(ld.getEmpireName());
		}

		for (LunarDate ld : anotherdd.lunarDates) {
			if (!empireNameSet.contains(ld.getEmpireName())) {
				return false;
			}
		}
		return true;
	}

	public boolean SameEmpireNameAndHsebTo(DetailedDate anotherdd) {
		if (this.lunarDates.size() != anotherdd.lunarDates.size()) {
			return false;
		}

		Set<String> empireNameSet = new HashSet<String>();
		Set<String> hsebSet = new HashSet<String>();
		for (LunarDate ld : lunarDates) {
			empireNameSet.add(ld.getEmpireName());
			hsebSet.add(ld.getHseb());
		}

		for (LunarDate ld : anotherdd.lunarDates) {
			if (!empireNameSet.contains(ld.getEmpireName()) || !hsebSet.contains(ld.getHseb())) {
				return false;
			}
		}
		return true;
	}
}
