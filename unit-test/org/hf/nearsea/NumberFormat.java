package org.hf.nearsea;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.*;
import static org.junit.Assert.*;

public class NumberFormat  {

	@Test
	public void testNumberFormat() throws Exception {
		DecimalFormat decimalFormat = new DecimalFormat ("####,####.##");
		Number parse = decimalFormat.parse("5,419,456.98");
		assertTrue (parse.floatValue() > 0);
	}

	@Test
	public void testMe() throws Exception {
		String f = "%1$-20s %2$10s %3$10s %4$10s";
		String[] nums = new String[]{"Total Revenue", "6.9%", "-3.3%", "218.2%"};
		List<String> a = new ArrayList<String>();
		for (String str : nums) {
			a.add(str);
		}
		System.out.format(f, a.toArray());

	}


	@Test
	public void testRegExp() {

		assertTrue (Pattern.matches("ref_[0-9]+_l", "ref_358464_l"));
		assertFalse (Pattern.matches("ref_[0-9]+_l", "ref__l"));
		assertFalse(Pattern.matches("ref_[0-9]+_l", "ref2_358464_l"));

		assertTrue(Pattern.matches("\\d+.\\d+/\\d+.\\d+", "3.36/2.3"));
		assertTrue(Pattern.matches("[0-9]+.[0-9]+/[0-9]+.[0-9]+[*]?", "3.36/2.34"));
		assertTrue(Pattern.matches("[0-9]+.[0-9]+/[0-9]+.[0-9]+[*]?", "3.36/2.34*"));
		assertFalse(Pattern.matches("[0-9]+.[0-9]+/[0-9]+.[0-9]+[*]?", "3.36"));
		assertFalse(Pattern.matches("[0-9]+.[0-9]+/[0-9]+.[0-9]+[*]?", "336"));

		String string = "3.36/2.34*";
		String pattern = "[0-9]+.[0-9]+/[0-9]+.[0-9]+[*]?";

		Matcher matcher = null;

		if(Pattern.matches(pattern, string)){
			Pattern compile = Pattern.compile("\\d+.\\d+[/]?");
			matcher = compile.matcher(string);
			matcher.matches();

			for (int i = 0; i <= matcher.groupCount(); ++i) {
				System.out.println(matcher.group(i));
			}

		}



	}
}
