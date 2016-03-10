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

			ld.setLunarMonth(sb.toString());
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

	private static int calcGap(DetailedDate begin, DetailedDate end) {
		MyDate be = new MyDate(begin.getNow());
		MyDate en = new MyDate(end.getNow());
		int k = 0;
		while (be.before(en)) {
			k++;
			be.nextDay();
		}
		return k;
	}

	private static void generateSameEmpireNameGap(DetailedDate begin, DetailedDate end, List<DetailedDate> ans) {
		int dayGap = calcGap(begin, end);
		// 如果年号相同
		if (begin.equalEmpireNameTo(end)) {
			LunarDate beginLunarDate = begin.lunarDates.get(0);
			String beginMon = beginLunarDate.getLunarMonth();
			int beginDate = beginLunarDate.getLunarDate();

			LunarDate endLunarDate = end.lookupLunarDateByEmpireName(beginLunarDate.getEmpireName());

			if (endLunarDate == null) {
				System.out.println("error no lunarEndDate found!");
			}

			String endMon = endLunarDate.getLunarMonth();
			int endDate = endLunarDate.getLunarDate();

			String generatedMon = "";
			MyDate now = new MyDate(begin.getNow());
			// 如果月份相同 说明在同一个月 可以直接生成期间日期
			if (endMon.equals(beginMon)) {
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
					ans.add(generatedDD);
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
	}

	public static List<DetailedDate> generateGapDate(DetailedDate begin, DetailedDate end) throws Exception {
		int dayGap = calcGap(begin, end);
		List<DetailedDate> ans = new ArrayList<DetailedDate>();

		// 如果年号相同
		if (begin.equalEmpireNameTo(end)) {
			LunarDate beginLunarDate = begin.lunarDates.get(0);
			String beginMon = beginLunarDate.getLunarMonth();
			int beginDate = beginLunarDate.getLunarDate();

			LunarDate endLunarDate = end.lookupLunarDateByEmpireName(beginLunarDate.getEmpireName());

			if (endLunarDate == null) {
				System.out.println("error no lunarEndDate found!");
				return ans;
			}

			String endMon = endLunarDate.getLunarMonth();
			int endDate = endLunarDate.getLunarDate();

			String generatedMon = "";
			MyDate now = new MyDate(begin.getNow());
			// 如果月份相同 说明在同一个月 可以直接生成期间日期
			if (endMon.equals(beginMon)) {
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
					ans.add(generatedDD);
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

		} else {
			// System.out.println("pass");
			try {
				DateCrawler crawler = new DateCrawler();
				MyDate beginDate = new MyDate(begin.getNow());

				int beginLunarDate = begin.lunarDates.get(0).getLunarDate();

				int gapDay = 28 - beginLunarDate;
				MyDate gapDate = new MyDate(beginDate);
				gapDate.nextNDay(gapDay);

				DetailedDate gapDD = digestRes(gapDate, crawler.craw(gapDate));
				if (gapDD.equalEmpireNameTo(begin)) {
					generateSameEmpireNameGap(begin, gapDD, ans);
				} else {
					System.out.println("error not 28 day");
					return ans;
				}

				gapDate.nextDay();
				DetailedDate tmp = digestRes(gapDate, crawler.craw(gapDate));
				while (begin.equalEmpireNameTo(tmp)) {
					ans.add(tmp);
					gapDate.nextDay();
					tmp = digestRes(gapDate, crawler.craw(gapDate));
				}
				ans.add(tmp);
				DetailedDate newBegin = tmp;
				if(newBegin.equalEmpireNameTo(end)){
					generateSameEmpireNameGap(newBegin, end,ans);
				}else{
					System.out.println("error not same");
				}

				// while (now.before(end.getNow())) {
				//
				// DetailedDate generatedDD = digestRes(now,
				// crawler.craw(now.getYear(), now.getMonth(), now.getDate()));
				// ans.add(generatedDD);
				// // TimerUtils.delayForSeconds(1);
				// }
			} catch (Exception e) {
				System.out.println("pass!");
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
