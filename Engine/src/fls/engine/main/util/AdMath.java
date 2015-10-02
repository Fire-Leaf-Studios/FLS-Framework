package fls.engine.main.util;

public class AdMath {

	
	public static int POW(int base,int exp){
		int res = base;
		while(exp-- > 0){
			res *= base;
		}
		return res;
	}
}
