package date_crawler;

public class Test {

	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			System.out.println(Integer.parseInt(args[i]));
		}

		StringBuilder sb = new StringBuilder();
		String s = "你好啊13";

		int p = 0;
		while (p < s.length() && !(s.charAt(p) >= '0' && s.charAt(p) <= '9')) {
			System.out.println(s.charAt(p));
			sb.append(s.charAt(p));
			p++;
		}

		System.out.println(sb.toString());
	}
}
