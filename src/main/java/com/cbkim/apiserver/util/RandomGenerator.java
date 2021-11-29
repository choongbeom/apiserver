package com.cbkim.apiserver.util;

import java.security.SecureRandom;

public class RandomGenerator {

    private static SecureRandom random = new SecureRandom();

    private static String ENGLISH_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static String ENGLISH_UPPER = ENGLISH_LOWER.toUpperCase();
    private static String NUMBER = "0123456789";
    
    /** 랜덤을 생성할 대상 문자열 **/
    private static String DATA_FOR_RANDOM_STRING = ENGLISH_UPPER + NUMBER;
    
    /** 랜덤 문자열 길이 **/
    private static int random_string_length = 10;
    

	/** 랜덤 문자열을 생성한다 **/
    public static String generate() {

       // if (length < 1) throw new IllegalArgumentException("length must be a positive number.");
        StringBuilder sb = new StringBuilder(random_string_length);
        for (int i = 0; i < random_string_length; i++) {
            sb.append( DATA_FOR_RANDOM_STRING.charAt(
            		random.nextInt(DATA_FOR_RANDOM_STRING.length())
            		));
        }
        return sb.toString();
    }

    /** 랜덤 문자열을 생성한다 **/
    public static String generateString(int randomNumber) {

        // if (length < 1) throw new IllegalArgumentException("length must be a positive number.");
         StringBuilder sb = new StringBuilder(randomNumber);
         for (int i = 0; i < randomNumber; i++) {
             sb.append( DATA_FOR_RANDOM_STRING.charAt(
                     random.nextInt(DATA_FOR_RANDOM_STRING.length())
                     ));
         }
         return sb.toString();
     }

    
    // 필요한 자리 숫자 문자열 생성
    public static String generate(int randomNumber) {

        // if (length < 1) throw new IllegalArgumentException("length must be a positive number.");
         StringBuilder sb = new StringBuilder(randomNumber);
         for (int i = 0; i < randomNumber; i++) {
             sb.append( NUMBER.charAt(
                     random.nextInt(NUMBER.length())
                     ));
         }
         return sb.toString();
     }
}
