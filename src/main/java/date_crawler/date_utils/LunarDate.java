package date_crawler.date_utils;

public class LunarDate {

	// 年号 如：西漢平帝元始元年
	private String empireName;

	// 天干地支 如：歲次辛酉
	private String hseb;

	// 阴历日期

	// 元年（1年） 2年 3年
	private int lunarYear;

	private String lunarMonth;

	private int lunarDate;

	public LunarDate() {

	}

	public LunarDate(String empirename, int lunaryear, String hseb, String lunarmon, int lunarday) {
		this.empireName = empirename;
		this.lunarYear = lunaryear;
		this.hseb = hseb;
		this.lunarMonth = lunarmon;
		this.lunarDate = lunarday;
	}

	public String getEmpireName() {
		return empireName;
	}

	public void setEmpireName(String empireName) {
		this.empireName = empireName;
	}

	public String getHseb() {
		return hseb;
	}

	public void setHseb(String hseb) {
		this.hseb = hseb;
	}

	public String getLunarMonth() {
		return lunarMonth;
	}

	public void setLunarMonth(String lunarMonth) {
		this.lunarMonth = lunarMonth;
	}

	public int getLunarDate() {
		return lunarDate;
	}

	public void setLunarDate(int lunarDate) {
		this.lunarDate = lunarDate;
	}

	public int getLunarYear() {
		return lunarYear;
	}

	public void setLunarYear(int lunarYear) {
		this.lunarYear = lunarYear;
	}

	public String toString() {
		String year = (lunarYear == 1) ? "元" : lunarYear + "";

		return empireName + year + "年" + "(" + hseb + ")" + lunarMonth + "月" + lunarDate + "日";
	}
}
