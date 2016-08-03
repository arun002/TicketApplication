package com.ticket.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TicketApplicationUtil {
	
	 public static Timestamp covertToSQLTimestamp(Date date){
        if(date==null){
            return null;
        }
        return new Timestamp(date.getTime());
	 }
	 
	 public static long getTimeDiffInMinutes(Date date){
        long diffSec= (System.currentTimeMillis() - date.getTime())/1000;
        return diffSec/60;
	 }

}
