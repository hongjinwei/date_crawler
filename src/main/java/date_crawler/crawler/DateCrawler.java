package date_crawler.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.peony.util.http.HttpQuery;

import date_crawler.date_utils.DateIterator;

public class DateCrawler {

	private static HttpQuery browser = HttpQuery.getInstance();

	private String url = "http://sinocal.sinica.edu.tw/cgi-bin/sinocal/luso.utf8.cgi";

	public DateCrawler() {

	}

	public DateCrawler(String url) {
		this.url = url;
	}

	private String excludeNoise(String html) {

		Document doc = Jsoup.parse(html);

		Elements eles = doc.getElementsByClass("o0");

		Element ele = eles.first();

		if (ele == null) {
			return null;
		}

		String text = ele.ownText();

		return text;

	}

	public String craw(int year, int month, int day) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lstype", "1"));
		params.add(new BasicNameValuePair("swDate", "1582/10/15"));
		params.add(new BasicNameValuePair("yy", year + ""));
		params.add(new BasicNameValuePair("mm", month + ""));
		params.add(new BasicNameValuePair("dd", day + ""));
		String html = browser.post(url, params).asString();

		return excludeNoise(html);
	}

	public String craw(DateIterator iter) throws Exception {
		int year = iter.getYear();
		int mon = iter.getMonth();
		int day = iter.getDate();
		return craw(year, mon, day);
	}

	public static void main(String[] args) throws Exception {

		String uu = "http://sinocal.sinica.edu.tw/cgi-bin/sinocal/luso.utf8.cgi";
		DateCrawler dc = new DateCrawler(uu);

		String res = dc.craw(1234, 2, 11);

		// String res = dc.craw(2011, 2, 11);
		System.out.println(res);
		String[] ans = res.split("\\s+");
		System.out.println(Arrays.toString(ans));
	}

}
