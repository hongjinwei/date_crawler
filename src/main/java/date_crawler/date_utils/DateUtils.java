package date_crawler.date_utils;

import java.util.ArrayList;
import java.util.List;

import com.peony.util.TimerUtils;

import date_crawler.crawler.DateCrawler;

public class DateUtils {

	/**
	 * 
	 * @param res
	 *            like:宋理宗端平元年(歲次甲午)1月12日 金哀宗正大11年(歲次甲午)1月12日 元太宗6年(歲次甲午)1月12日
	 * @return
	 */
	public static DetailedDate digestRes(MyDate now, String res) {
		DetailedDate dd = new DetailedDate();

		dd.setDate(now.getDate());
		dd.setMonth(now.getMonth());
		dd.setYear(now.getYear());

		String[] ans = res.split("\\s+");

		for (int i = 0; i < ans.length; i++) {
			LunarDate ld = new LunarDate();
			String tmp = ans[i];
			StringBuilder sb = new StringBuilder();
			int k = 0;
			while (k < tmp.length() && tmp.charAt(k) != '(') {
				sb.append(tmp.charAt(k));
				k++;
			}
			k++;
			String empireStr = sb.toString();

			int lunarYear = -1;
			String empireName = null;
			int index = empireStr.indexOf("元年");
			if (index != -1) {
				empireName = empireStr.substring(0, index);
				lunarYear = 1;
			} else {
				int p = 0;
				StringBuilder sb2 = new StringBuilder();
				while (p < empireStr.length() && !(empireStr.charAt(p) >= '0' && empireStr.charAt(p) <= '9')) {
					sb2.append(empireStr.charAt(p));
					p++;
				}
				empireName = sb2.toString();

				sb2.delete(0, sb.length());
				while (p < empireStr.length() && empireStr.charAt(p) != '年') {
					sb2.append(empireStr.charAt(p));
					p++;
				}
				lunarYear = Integer.parseInt(sb2.toString());
			}
			ld.setLunarYear(lunarYear);
			ld.setEmpireName(empireName);

			sb.delete(0, sb.length());

			while (k < tmp.length() && tmp.charAt(k) != ')') {
				sb.append(tmp.charAt(k));
				k++;
			}
			k++;
			String hseb = sb.toString();
			ld.setHseb(hseb);
			sb.delete(0, sb.length());

			while (k < tmp.length() && tmp.charAt(k) != '月') {
				sb.append(tmp.charAt(k));
				k++;
			}
			k++;

			int lunarMon = Integer.parseInt(sb.toString());
			ld.setLunarMonth(lunarMon);
			sb.delete(0, sb.length());

			while (k < tmp.length() && tmp.charAt(k) != '日') {
				sb.append(tmp.charAt(k));
				k++;
			}

			int lunarDate = Integer.parseInt(sb.toString());
			ld.setLunarDate(lunarDate);
			sb.delete(0, sb.length());
			dd.addLunarDate(ld);
		}

		return dd;
	}

	public static List<DetailedDate> generateGapDate(DetailedDate begin, DetailedDate end, int dayGap) throws Exception {

		List<DetailedDate> ans = new ArrayList<DetailedDate>();

		// 如果年号相同
		if (begin.equalEmpireNameTo(end)) {
			for (int i = 0; i < begin.lunarDates.size(); i++) {
				LunarDate beginLunarDate = begin.lunarDates.get(i);
				int beginMon = beginLunarDate.getLunarMonth();
				int beginDate = beginLunarDate.getLunarDate();

				LunarDate endLunarDate = end.lookupLunarDateByEmpireName(beginLunarDate.getEmpireName());

				if (endLunarDate == null) {
					System.out.println("error no lunarEndDate found!");
					return ans;
				}

				int endMon = endLunarDate.getLunarMonth();
				int endDate = endLunarDate.getLunarDate();

				int generatedMon = 0;
				MyDate now = new MyDate(begin.getNow());
				// 如果月份相同 说明在同一个月 可以直接生成期间日期
				if (endMon == beginMon) {
					generatedMon = endMon;
					for (int j = beginDate + 1; j < endDate; j++) {
						now.nextDay();
						DetailedDate generatedDD = new DetailedDate();
						generatedDD.setNow(now);

						int generatedDate = j;
						for (LunarDate ld : begin.lunarDates) {
							String en = ld.getEmpireName();
							LunarDate generatedLD = new LunarDate(en, beginLunarDate.getLunarYear(), beginLunarDate.getHseb(), generatedMon, generatedDate);
							generatedDD.addLunarDate(generatedLD);
						}
					}
				} else {
					int endDayOfMonth = dayGap + beginDate - endDate;
					generatedMon = beginMon;
					for (int j = beginDate + 1; j <= endDayOfMonth; j++) {
						now.nextDay();
						DetailedDate generatedDD = new DetailedDate();
						generatedDD.setNow(now);

						int generatedDate = j;
						for (LunarDate ld : begin.lunarDates) {
							String en = ld.getEmpireName();
							LunarDate generatedLD = new LunarDate(en, beginLunarDate.getLunarYear(), beginLunarDate.getHseb(), generatedMon, generatedDate);
							generatedDD.addLunarDate(generatedLD);
						}
						ans.add(generatedDD);
					}
					generatedMon = endMon;
					for (int j = 1; j < endDate; j++) {
						now.nextDay();
						DetailedDate generatedDD = new DetailedDate();
						generatedDD.setNow(now);

						int generatedDate = j;
						for (LunarDate ld : begin.lunarDates) {
							String en = ld.getEmpireName();
							LunarDate generatedLD = new LunarDate(en, endLunarDate.getLunarYear(), endLunarDate.getHseb(), generatedMon, generatedDate);
							generatedDD.addLunarDate(generatedLD);
						}
						ans.add(generatedDD);
					}
				}
			}

		} else {
			MyDate now = new MyDate(begin.getNow());
			now.nextDay();
			while (now.before(end.getNow())) {
				DateCrawler crawler = new DateCrawler();
				DetailedDate generatedDD = digestRes(now, crawler.craw(now.getYear(), now.getMonth(), now.getDate()));
				ans.add(generatedDD);
				TimerUtils.delayForSeconds(1);
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		// String res =
		// "宋理宗端平元年(歲次甲午)1月12日 金哀宗正大11年(歲次甲午)1月12日 元太宗6年(歲次甲午)1月12日";
		// DateIterator iter = DateIterator.custom();
		// iter.next();
		// DetailedDate dd = DateUtils.digestRes(iter, res);
		// System.out.println(dd);
		// dd.show();

		System.out.println(28 + 8 - 6);

	}
}
