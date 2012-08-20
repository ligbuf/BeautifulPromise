package com.beautifulpromise.common.utils;

/**
 * @description 타입 변환을 위한 클래스
 * @author immk
 *
 */
public class TypeUtils {
	
	/**
	 * Boolean을 int형으로 변환
	 * @param bData ture/false
	 * @return 1/0
	 */
	public static int BooleanToInteger(boolean bData){
		return (bData) ? 1 : 0;  // true = 1 , false = 0 
	}
	
	/**
	 * int를 Boolean으로 변환
	 * @param value 1/others
	 * @return true/false
	 */
	public static boolean IntegerToBoolean(int value){
		return (value==1) ? true : false;  // true = 1 , false = 0 
	}
}
