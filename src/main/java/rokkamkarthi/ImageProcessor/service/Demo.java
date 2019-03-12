package rokkamkarthi.ImageProcessor.service;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String s = "data:image/gif;base64,R0lGODdhiwXpA/cAAP";
		String ss [] = s.split(",");
		System.out.println(ss[0]);
		System.out.println(ss[1]);
		
		System.out.println(ss[0].substring(ss[0].indexOf("/")+1, ss[0].indexOf(";")));

	}

}
