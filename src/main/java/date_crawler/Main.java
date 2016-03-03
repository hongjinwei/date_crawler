package date_crawler;

import java.util.List;

import com.peony.util.TimerUtils;

import date_crawler.crawler.DateCrawler;
import date_crawler.date_utils.DateIterator;
import date_crawler.date_utils.DateUtils;
import date_crawler.date_utils.DetailedDate;
import date_crawler.date_utils.MyDate;

public class Main {

	public static final String url = "http://sinocal.sinica.edu.tw/cgi-bin/sinocal/luso.utf8.cgi";

	public static DateCrawler crawler = new DateCrawler(url);

	private static int dayGap = 28;

	public static DateIterator iter = DateIterator.custom(dayGap);

	public static void main(String[] args) {

		DetailedDate begin = null;
		DetailedDate end = null;

		if (args.length == 3) {
			int startyear = Integer.parseInt(args[0]);
			int startmon = Integer.parseInt(args[1]);
			int startdate = Integer.parseInt(args[2]);
			iter.setStartDate(startyear, startmon, startdate);
		}

		while (iter.hasNext()) {

			try {
				DateIterator cal = iter.next();
				String res = crawler.craw(cal);

				DetailedDate dd = DateUtils.digestRes(iter.getNow(), res);
				if (begin == null && end == null) {
					begin = dd;
					dd.show();
				} else if (end == null) {
					end = dd;
					List<DetailedDate> ans = DateUtils.generateGapDate(begin, end, dayGap);
					for (int i = 0; i < ans.size(); i++) {
						ans.get(i).show();
					}
					end.show();
				} else {
					begin = end;
					end = dd;
					List<DetailedDate> ans = DateUtils.generateGapDate(begin, end, dayGap);
					for (int i = 0; i < ans.size(); i++) {
						ans.get(i).show();
					}
					end.show();
				}
				TimerUtils.delayForSeconds(1);
			} catch (Exception e) {
				System.out.println("error 网络错误");
			}
		}
	}
}
