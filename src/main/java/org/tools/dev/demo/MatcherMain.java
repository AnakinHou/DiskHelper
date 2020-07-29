package org.tools.dev.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherMain {

	public static void main(String[] args) {
//		String regex = "[1-9]\\d*.+[1-9]\\d*=?";
//        Pattern pattern = Pattern.compile(regex);
//        System.out.println(pattern.pattern());
//        System.out.println(pattern.flags());

//        String regex = "[+\\-*/]";
//        Pattern pattern = Pattern.compile(regex);
////        String[] nums = pattern.split("13+29-44*55/22");
//        String[] nums = pattern.split("13+29-44*55/22", 2);
//        for (String num : nums) {
//            System.out.print(num + " ");
//        }
        
//	       String regex = "[1-9]\\d*[+\\-*/][1-9]\\d*=?";
//	        System.out.println(Pattern.matches(regex, "2315+5555="));
//	        System.out.println(Pattern.matches(regex, "123456*4556"));
//	        System.out.println(Pattern.matches(regex, "0112897+440="));
//	        System.out.println(Pattern.matches(regex, "22+23"));
		
		
//		   String regex = "[1-9]\\d*[+\\-*/][1-9]\\d*=?";
//	        Pattern pattern = Pattern.compile(regex);
//	        Matcher matcher = pattern.matcher("1+2");
//	        System.out.println(matcher.pattern());
	        
	        
		String regex = "([1-9]\\d*)([+\\-*/])([1-9]\\d*=?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("a11+2dasfdsa11+22esdfxca");

        System.out.println(matcher.find());
        matcher.find();

        System.out.println(matcher.groupCount());

        for (int i = 0; i <= matcher.groupCount(); i++) {
            System.out.println("The index of the final one character is " + matcher.start(i));
            System.out.println("The index of the last one character is " + matcher.end(i));
            System.out.println("The content of the corresponding substring of this string is " + matcher.group(i));
            System.out.println();
        }
	        
	}

}
