/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Marek
 */
public class TimeFunctions {
    public static long timestampToLong(Timestamp time){
        long ans = time.getTime();
        return ans;
    }
    public static Timestamp longToTimestamp(long number){
        Timestamp ans = new Timestamp(number);
        return ans;
    }
    public static long nowTimestamp(){
    	 Date date = new Date();	 
         long timeLong = date.getTime();
         return timeLong;
    }
    public static boolean passTime(long changeDate){
        long expirationTime = 30*24*60*60*1000;
        boolean ans = false;
        if ((nowTimestamp()-changeDate > expirationTime)){
            ans =  true;
        }    
        return ans;
    }
    public static int nowYear(){
        Date date = new Date();
        int year = date.getYear()+1900;
        return year;
    }
    public static int nowMonth(){
        Date date = new Date();
        int month = date.getMonth()+1;
        return month;
    }
    public static int nowDay(){
        Date date = new Date();
        int day = date.getDate();
        return day;
    }
    public static int howManyDays(int selM, int selY){
        int days = 0;
        if(selM ==1 || selM ==3 || selM ==5 || selM ==7 || selM ==8 || selM ==10 || selM ==12){
            days = 31;
        }
        if(selM == 4 || selM ==6 || selM ==9 || selM ==11){
            days = 30;
        }
        if(selM == 2){
            int count=28;
            if(selY % 4 == 0){
                count++;
            }
            days = count;
        }
        return days;
    }
    public static String monthNumber(String monthName){
        String mNo = "";
        switch (monthName) {
            case "Styczeń": mNo = "01";
                            break;
            case "Luty": mNo = "02";
                            break;
            case "Marzec": mNo = "03";
                            break;
            case "Kwiecień": mNo = "04";
                            break;
            case "Maj": mNo = "05";
                            break;
            case "Czerwiec": mNo = "06";
                            break;
            case "Lipiec": mNo = "07";
                            break;
            case "Sierpień": mNo = "08";
                            break;
            case "Wrzesień": mNo = "09";
                            break;
            case "Październik": mNo = "10";
                            break;
            case "Listopad": mNo = "11";
                            break;
            case "Grudzień": mNo = "12";
                            break;
        }
        return mNo;
    }
}
