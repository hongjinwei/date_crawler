package date_crawler;

import java.util.Calendar;

public class Test {

	static void test(){
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal.set(1, 0, 1);
		cal2.set(1,0,10);
		
		System.out.println(cal2.compareTo(cal));
	}
	
	
	public static void main(String[] args) {

//		for (int i = 0; i < args.length; i++) {
//			System.out.println(Integer.parseInt(args[i]));
//		}
//
//		StringBuilder sb = new StringBuilder();
//		String s = "你好啊13";
//
//		int p = 0;
//		while (p < s.length() && !(s.charAt(p) >= '0' && s.charAt(p) <= '9')) {
//			System.out.println(s.charAt(p));
//			sb.append(s.charAt(p));
//			p++;
//		}
//
//		System.out.println(sb.toString());
		test();
	}
}
