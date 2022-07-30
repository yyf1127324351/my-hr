package com.myhr.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 日期工具类
 * 
 */
@Slf4j
public class DateUtils {

	private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

	public static final LocalDate DOOMSDAY = LocalDate.of(9999, 12, 31);

	public static final Integer ONE_DAY_TIMESTAMP = 24 * 3600 * 1000;

	public static final String year_pattern = "yyyy";
	public static final String date_pattern = "MM-dd";
	public static final String time_pattern = "HH:mm";
	public static final String default_pattern = "yyyy-MM-dd HH:mm:ss";
	public static final String default_pattern1 = "yyyy-MM-dd";
	public static final String default_pattern2 = "yyyy-M-d";
	public static final String default_pattern3 = "yyyy年MM月dd日";
	public static final String default_YEAR_MONTH = "yyyy-MM";//年月
	public static final String default_pattern4="yyyy/MM/dd HH:mm:ss";
	public static final String default_pattern5 = "yyyyMMddHHmmss";
	public static final String default_pattern6 = "yyyyMMddHHmm";
	public static final String default_pattern7 = "yyyyMMdd";
	public static final String default_pattern8="yyyy-MM-dd HH:mm";
	public static final String default_pattern9="yyyy/MM/dd";
	public static final String default_pattern10 = "yyyyMMddHHmmssSSS";

	/**
	 * 获取当月
	 * */
	public static String getYearMonth(){
		String yearMonth = formatDate(new Date(),default_YEAR_MONTH);
		return yearMonth;
	}
	/**
	 * 获取上一个月
	* */
	public static String getLastMonth(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		Date time = calendar.getTime();
		String lastMonth = formatDate(time,default_YEAR_MONTH);
		return lastMonth;

	}
	public static String getCurrentMonth(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date time = calendar.getTime();
		String currentMonth = formatDate(time,default_YEAR_MONTH);
		return currentMonth;

	}

	// 根据日期根据将date类型的日期转化为string
	public static String formatDate(Date date, String pattern) {
		if (null == date){
			return null;
		}
		if (StringUtils.isBlank(pattern)){
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	public static String format(Date date, String pattern, String defaultValue) {
		if (null == date){
			return defaultValue;
		}else if (StringUtils.isBlank(pattern)){
			return defaultValue;
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	public static String formatDate(Date date, DateFormat dateFormat) {
		if (null == date){
			return null;
		}
		if (dateFormat==null){
			return null;
		}
		return dateFormat.format(date);
	}
	// 根据yyyy-MM-dd HH:mm:ss 将date类型转化为string
	public static String defaultFormatDate(Date date) {
		if (null == date){
			return null;
		}
		return formatDate(date, default_pattern);
	}
	public static String defaultFormatDate5(Date date) {
		if (null == date){
			return null;
		}
		return formatDate(date, default_pattern5);
	}
	public static String defaultFormatDate10(Date date) {
		if (null == date){
			return null;
		}
		return formatDate(date, default_pattern10);
	}

	public static String dateToString3(Date date) {
		if (null == date){
			return null;
		}
		return formatDate(date, default_pattern6);
	}
	public static String defaultFormatDate8(Date date) {
		if (null == date){
			return null;
		}
		return formatDate(date, default_pattern8);
	}
	public static String defaultformatDate(Date date) {
		if (null == date){
			return "";
		}
		return formatDate(date, default_pattern1);
	}

	public static String defaultformatDate3(Date date) {
		if (null == date){
			return "";
		}
		return formatDate(date, default_pattern1);
	}
	public static String formateYearMonth(Date date) {
		if (null == date){
			return "";
		}
		return formatDate(date, default_YEAR_MONTH);
	}

	public static String defaultFormatDate4(Date date) {
		if (null == date){
			return "";
		}
		return formatDate(date, default_pattern3);
	}

	/**
	 * 日期格式为yyyy-MM-dd HH:mm:ss
	 * @param dates
	 * @return
	 */
	public static Date defaultParseDate(String dates){
		if(StringUtils.isBlank(dates)){ return null;}
		return parseDate(dates, default_pattern);
	}

	/***
	 * 格式 yyyy-MM-dd
	 * @param dates
	 * @return
	 */
	//另一种格式 yyyy-MM-dd
	public static Date defaultParseDate1(String dates){
		if(StringUtils.isBlank(dates)){ return null;}
		return parseDate(dates, default_pattern1);
	}
	
	public static Date defaultParseDate2(String dates){
		if(StringUtils.isBlank(dates)){ return null;}
		return parseDate(dates, default_pattern2);
	}
	public static Date defaultformatDate3(String dates) {
		if(StringUtils.isBlank(dates)){ return null;}
		return parseDate(dates, default_YEAR_MONTH);
	}
	public static Date defaultformatDate4(String dates) {
		if(StringUtils.isBlank(dates)){ return null;}
		return parseDate(dates, default_pattern4);
	}
	/**
	 * 指定日期格式
	 * @param dates
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String dates,String pattern){
		if(StringUtils.isBlank(dates)){ return null;}
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			return dateFormat.parse(dates);
		} catch (ParseException e) {
			log.info(e.getMessage(), e);
		}
		return null;
	}
	

	public static boolean compareDate(String date1,String date2){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat(default_pattern1);
		try {
			cal1.setTime(dateFormat.parse(date1));
			cal2.setTime(dateFormat.parse(date2));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}

		if(cal1.getTimeInMillis()<cal2.getTimeInMillis()){
			return true;
		}else{
			return false;
		}

	}
	
	/**
	 * 判断日期是否是某个月一号
	 * @param date
	 * @return
	 */
	public static boolean checkDate(Date date,DateFormat dateFormat){
		if(date==null){
			return true;
		}
		String d1 = dateFormat.format(date);
		if(d1.endsWith("-01")){
			return true;
		}else{
			return false;
		}
	}
	
	// 根据yyyy-MM-dd HH:mm:ss 将date类型转化为string
	public static String stringFormatDate(Date date) {
		if (null == date) {
			return null;
		}
		return formatDate(date, default_YEAR_MONTH);
	}
	
	/**
	 * 获取默认的通知开始时间
	 * 默认为当前时间的前3个月
	 * @return
	 */
	public static String getDefaultNoticeDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -3);
		Date m = c.getTime();
		return sdf.format(m)+"-01";
	}
	
	/** 
     * 时间戳转换成日期格式字符串 
     * @param seconds 精确到秒的字符串 
     * @param
     * @return 
     */  
    public static String timeStamp2Date(String seconds,String format,SimpleDateFormat sdf) {  
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
            return "";  
        }  
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }   
        return sdf.format(new Date(Long.valueOf(seconds+"000")));  
    }  
    /** 
     * 日期格式字符串转换成时间戳 
     * @param
     * @param format 如：yyyy-MM-dd HH:mm:ss 
     * @return 
     */  
    public static String date2TimeStamp(String date_str,String format){  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return String.valueOf(sdf.parse(date_str).getTime()/1000);  
        } catch (Exception e) {
			log.error(e.getMessage(), e);
        }  
        return "";  
    }  
      
    /** 
     * 取得当前时间戳（精确到秒） 
     * @return 
     */  
    public static String timeStamp(){  
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);  
        return t;  
    }

	/**
	 * 获得某月第一天
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfMonth(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return defaultformatDate(cal.getTime());
	}
	/**
	 * 获得某月第一天
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfMonth(String date) {
		if (date.length() < 10) {
			date = date + "-01";
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(defaultParseDate1(date));
		final int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return defaultformatDate(cal.getTime());
	}
	/**
	 * 获得某月最后一天
	 * @param date
	 * @return
	 */
	public static String getLastDayOfMonth(String date) {
		Integer year = Integer.valueOf(date.substring(0, 4));
		Integer month = Integer.valueOf(date.substring(5, 7));
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month - 1);
		//获取某月最小天数
		int firstDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}


	/**
	 * 获得某月最后一天
	 * @param date
	 * @return
	 */
	public static String getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置某月日期
		int year = calendar.get(Calendar.YEAR);//获取年份
		int month = calendar.get(Calendar.MONTH)+1;//获取月份

		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		//获取某月最大天数

		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(calendar.getTime());
		return lastDayOfMonth;
	}

	/**
	 * 获得某月的几个月后的日期
	 * @param
	 * @return
	 */
	public static String getAfterMonthOfMonth(String date , int month) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(DateUtils.parseDate(date,"yyyy-MM-dd"));
		calendar.add(Calendar.MONTH, month);//增加month个月
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String afterMonth = sdf.format(calendar.getTime());
		return afterMonth;
	}
//    public static void main(String[] args) {
//		String lastMonth = getLastMonth();
//		System.out.println(lastMonth);
//        String timeStamp = timeStamp();
//        System.out.println("timeStamp="+timeStamp); //运行输出:timeStamp=1470278082
//        System.out.println(System.currentTimeMillis());//运行输出:1470278082980
//        //该方法的作用是返回当前的计算机时间，时间的表达格式为当前计算机时间和GMT时间(格林威治时间)1970年1月1号0时0分0秒所差的毫秒数
//        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss",sdf);
//        System.out.println("date="+date);//运行输出:date=2016-08-04 10:34:42
//
//        String timeStamp2 = date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
//        System.out.println(timeStamp2);  //运行输出:1470278082
//    }
    //小于等于
	public static boolean compareDengDate(String date1,String date2 ,SimpleDateFormat dateFormat){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		if(dateFormat==null)
			dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal1.setTime(dateFormat.parse(date1));
			cal2.setTime(dateFormat.parse(date2));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}

		if(cal1.getTimeInMillis()<=cal2.getTimeInMillis()){
			return true;
		}else{
			return false;
		}
	}

    public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置某月日期
		int year = calendar.get(Calendar.YEAR);
        return year;
    }


	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate;
		Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
			//System.out.println("相隔的天数="+day);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return day;
	}

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static Integer daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

	/**
	 * 获取当年的最后一天
	 * @param
	 * @return
	 */
	public static String getCurrYearLast(){
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		Date date = getYearLast(currentYear);
		return formatDate(date,default_pattern1);
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static String getYearLastStr(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return formatDate(currYearLast,default_pattern1);
	}

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();

		return currYearFirst;
	}

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static String getYearFirstStr(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();

		return formatDate(currYearFirst,default_pattern1);
	}
	/*
	 * dataString:yyyy-MM-dd  days调整天数
	 * 返回日期类型
	 **/
	public static Date getAddDay(String dateString,int days){
		Date date=defaultParseDate1(dateString);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,days);
		return  calendar.getTime();
	}
	public static Date getAddMonth(String dateString,int months){
		Date date=defaultParseDate1(dateString);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,months);
		return  calendar.getTime();
	}

	public static Date getAddMonth(Date date,int months){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,months);
		return  calendar.getTime();
	}

	public static String getAddMonthStr(String dateString, int months) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar ct = Calendar.getInstance();
			ct.setTime(df.parse(dateString));
			ct.add(Calendar.MONTH, +months);
			return df.format(ct.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	/*
	* dataString:yyyy-MM-dd  days调整天数
	* 返回yyyy-MM-dd
	**/
	public static String getAddDayStr(String dateString,int days){
		Date date=defaultParseDate1(dateString);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,days);
		return  defaultformatDate(calendar.getTime()) ;
	}

	public static Date addOrSubDate(Date date,int num){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,num);
		return calendar.getTime();
	}

	public static Date plus(Date sour, int field, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sour);
		calendar.add(field, value);
		return calendar.getTime();
	}

	public static Date plus(Date sour, int... fieldValues) {
		if(fieldValues.length % 2 ==1) {
			log.error("调用参数不正确");
			return sour;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sour);
		for(int i=0; i<fieldValues.length; i+=2) {
			calendar.add(fieldValues[i], fieldValues[i+1]);
		}
		return calendar.getTime();
	}

	public static Date set(Date sour, int field, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sour);
		calendar.set(field, value);
		return calendar.getTime();
	}

	public static Date set(Date sour, int... fieldValues) {
		if(fieldValues.length % 2 ==1) {
			log.error("调用参数不正确");
			return sour;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sour);
		for(int i=0; i<fieldValues.length; i+=2) {
			calendar.set(fieldValues[i], fieldValues[i+1]);
		}
		return calendar.getTime();
	}

    public static Date zeroHour(Date date) {
		return toDate(date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate());
	}

	public static Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
	}

	public static Date toDate(LocalDateTime datetime) {
		return Date.from(datetime.atZone(DEFAULT_ZONE_ID).toInstant());
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID);
	}

    public static String handleDate(String date) {
		if(StringUtils.isEmpty(date)) {
			return StringUtils.EMPTY;
		}
		date = date.replaceAll("/", "-")
				.replaceAll("\\.", "-")
				.replaceAll("年", "-")
				.replaceAll("月", "-")
				.replaceAll("日", "-");
		if(date.endsWith("-")) {
			return date.substring(0, date.length() - 1);
		}
		return date;
    }

    public static Long getDateTimes(String dates) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(dates);
		} catch (ParseException e) {
			log.error("异常信息:",e);
		}
		return date.getTime();
    }
	public static Long getDateTimes2(String dates) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(dates);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return date.getTime();
	}
	public static Long getDateTimes3(String dates) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = dateFormat.parse(dates);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return date.getTime();
	}


	public static String addHours(String day, int hour) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	public static String addDays(String day, int days,String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}


	public static String handleClockTime(String clockInTime) {
		String timeStr = clockInTime.substring(0,clockInTime.length()-2)+"00";
		return timeStr;
	}

	public static String translateTime(Integer minute) {
		if (null != minute && 0L != minute){
			Integer hour = minute / 60;
			StringBuilder buffer = new StringBuilder(5);
			if (hour < 10L) {
				buffer.append("0");
			}
			buffer.append(hour);

			buffer.append(":");

			Integer remainMinute = minute - hour * 60;
			if(remainMinute < 10L) {
				buffer.append("0");
			}
			buffer.append(remainMinute);
			return buffer.toString();
		}
		return "00:00";
	}

    public static Integer computerHolidays(Date t1,Date t2) {
	    try {
            //初始化第一个日期
            Calendar cal1 = Calendar.getInstance();
            //初始化第二个日期，这里的天数可以随便的设置
            Calendar cal2 = Calendar.getInstance();


            // 设置传入的时间格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 指定一个日期
            Date date1 = dateFormat.parse(dateFormat.format(t1));
            Date date2 = dateFormat.parse(dateFormat.format(t2));
            // 对 calendar 设置为 date 所定的日期
            cal1.setTime(date1);
            cal2.setTime(date2);

            int holidays = 0;
            //确定一个 大日期
            if (cal1.compareTo(cal2) > 0) {
                Calendar temp = cal1;
                cal1 = cal2;
                cal2 = temp;
                temp = null;
            }
            while (cal1.compareTo(cal2) <= 0) {
                if (cal1.get(Calendar.DAY_OF_WEEK) == 7) {
                    holidays++;
                }
                cal1.add(Calendar.DAY_OF_YEAR, 1);

            }
            return holidays;
        }catch (Exception e){
			log.error(e.getMessage(), e);
	        return 0;
        }

    }

	public static String fromLongToDate(Long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(default_pattern);
		Date date = new Date(time);
		return sdf.format(date);
	}

    public static int getSaturdayDays(int month) {
		int day = 0;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		calendar.set(Calendar.YEAR, year);// 不设置的话默认为当年
		calendar.set(Calendar.MONTH, month - 1);// 设置月份
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为当月第一天
		int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月最大天数
		for (int i = 0; i < daySize-1; i++) {
			calendar.add(Calendar.DATE, 1);//在第一天的基础上加1
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.SATURDAY) {// 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
				day = day + 1;
			}
		}
		return day;
    }

    //获取当月所有周日
	public static List<String> getSundayDay(String yearMonth) {
		List<String> sundayList = new ArrayList<>();
		String[] arr = yearMonth.split("-");
		Integer year = Integer.valueOf(arr[0]);
		Integer month = Integer.valueOf(arr[1]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);// 不设置的话默认为当年
		calendar.set(Calendar.MONTH, month - 1);// 设置月份
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为当月第一天
		int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月最大天数
		for (int i = 0; i < daySize-1; i++) {
			calendar.add(Calendar.DATE, 1);//在第一天的基础上加1
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.SUNDAY) {// 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
				int date = calendar.get(Calendar.DATE);
				String dateString = "";
				if (date < 10) {
					dateString = "0" + date;
				}else {
					dateString = ""+date;
				}
				String sunday = yearMonth + "-" + dateString;
				sundayList.add(sunday);
			}
		}
		return sundayList;
	}

	/**
	 * 获取指定月份的星期五的日期数组
	 *
	 * @param yearMonth
	 * @return
	 */
	public static List<String> getFridayDay(String yearMonth) {
		List<String> sundayList = new ArrayList<>();
		String[] arr = yearMonth.split("-");
		Integer year = Integer.valueOf(arr[0]);
		Integer month = Integer.valueOf(arr[1]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);// 不设置的话默认为当年
		calendar.set(Calendar.MONTH, month - 1);// 设置月份
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为当月第一天
		int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月最大天数
		for (int i = 0; i < daySize-1; i++) {
			calendar.add(Calendar.DATE, 1);//在第一天的基础上加1
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.FRIDAY) {// 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
				int date = calendar.get(Calendar.DATE);
				String dateString = "";
				if (date < 10) {
					dateString = "0" + date;
				}else {
					dateString = ""+date;
				}
				String sunday = yearMonth + "-" + dateString;
				sundayList.add(sunday);
			}
		}
		return sundayList;
	}



//	public static void main(String[] args) {
//
//		Date start = getAddMonth("2021-07-01", -1);
//		Date end = getAddMonth("2021-07-01", -12);
//		String startMonth = defaultformatDate(start);
//		String endMonth = defaultformatDate(end);
//
//		List<String> sundayList = getSundayDay("2020-08");
//
//		sundayList.forEach(System.out::println);
//	}



	public static boolean isSunday(String calculateDay) {

		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format1.parse(calculateDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                return true;
            } else{
                return false;
            }
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public static boolean isMonday(String calculateDay) {

		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format1.parse(calculateDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
				return true;
			} else{
				return false;
			}
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public static boolean compareLess(String start, String end) {
		Long startTimes = DateUtils.getDateTimes2(start);
		Long endTimes = DateUtils.getDateTimes2(end);
		if (startTimes.longValue() < endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}

	public static boolean compareLessEqual(String start, String end) {
		Long startTimes = DateUtils.getDateTimes2(start);
		Long endTimes = DateUtils.getDateTimes2(end);
		if (startTimes.longValue() <= endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}
	public static boolean compareLessDate(String start, String end) {
		Long startTimes = DateUtils.getDateTimes(start);
		Long endTimes = DateUtils.getDateTimes(end);
		if (startTimes.longValue() < endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}
	public static boolean compareLessEqualDate(String start, String end) {
		Long startTimes = DateUtils.getDateTimes(start);
		Long endTimes = DateUtils.getDateTimes(end);
		if (startTimes.longValue() <= endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}

	public static boolean compareBigEqual(String start, String end) {
		Long startTimes = DateUtils.getDateTimes2(start);
		Long endTimes = DateUtils.getDateTimes2(end);
		if (startTimes.longValue() >= endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}
	public static boolean compareBig(String start, String end) {
		Long startTimes = DateUtils.getDateTimes2(start);
		Long endTimes = DateUtils.getDateTimes2(end);
		if (startTimes.longValue() > endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}
	public static boolean compareBigDate(String start, String end) {
		Long startTimes = DateUtils.getDateTimes(start);
		Long endTimes = DateUtils.getDateTimes(end);
		if (startTimes.longValue() > endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}
	public static boolean compareBigEqualDate(String start, String end) {
		Long startTimes = DateUtils.getDateTimes(start);
		Long endTimes = DateUtils.getDateTimes(end);
		if (startTimes.longValue() >= endTimes.longValue()){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 获取昨天
	 * @return String yyyy-MM-dd
	 * */
	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date time = cal.getTime();
		return new SimpleDateFormat(DateUtils.default_pattern1).format(time);
	}

	public static String getAddDateStr(String dateString,int days,String pattern){
		Date date = parseDate(dateString,pattern);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,days);
		return  formatDate(calendar.getTime(),pattern) ;
	}

	public static Date getAddDay(Date date,int days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,days);
		return  calendar.getTime();
	}


	/**
	 * 获取传入日期所在月的第一天 返回yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getFirstDayDateOfMonth(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, last);
		return defaultformatDate(cal.getTime());
	}

	/**
	 * 获取两个日期之间的所有日期(字符串格式, 按天计算)
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getBetweenDays(Date start, Date end) {
		List<String> result = new ArrayList<String>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.default_pattern1);
		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		result.add(sdf.format(start));
		while (tempStart.before(tempEnd)) {
			result.add(sdf.format(tempStart.getTime()));
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}
	public static List<String> getBetweenDays(String startTime, String endTime) {
		Date start = DateUtils.defaultParseDate1(startTime);
		Date end = DateUtils.defaultParseDate1(endTime);
		List<String> result = new ArrayList<String>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.default_pattern1);
		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		result.add(sdf.format(start));
		while (tempStart.before(tempEnd)) {
			result.add(sdf.format(tempStart.getTime()));
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	public static List<String> getYearMonth(String startTime, String endTime) {
		List<String> list = new ArrayList<>();
		try {
			Date startDate = new SimpleDateFormat("yyyy-MM").parse(startTime);
			Date endDate = new SimpleDateFormat("yyyy-MM").parse(endTime);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			// 获取开始年份和开始月份
			int startYear = calendar.get(Calendar.YEAR);
			int startMonth = calendar.get(Calendar.MONTH);
			// 获取结束年份和结束月份
			calendar.setTime(endDate);
			int endYear = calendar.get(Calendar.YEAR);
			int endMonth = calendar.get(Calendar.MONTH);
			//

			for (int i = startYear; i <= endYear; i++) {
				String date = "";
				if (startYear == endYear) {
					for (int j = startMonth; j <= endMonth; j++) {
						if (j < 9) {
							date = i + "-0" + (j + 1);
						} else {
							date = i + "-" + (j + 1);
						}
						list.add(date);
					}

				} else {
					if (i == startYear) {
						for (int j = startMonth; j < 12; j++) {
							if (j < 9) {
								date = i + "-0" + (j + 1);
							} else {
								date = i + "-" + (j + 1);
							}
							list.add(date);
						}
					} else if (i == endYear) {
						for (int j = 0; j <= endMonth; j++) {
							if (j < 9) {
								date = i + "-0" + (j + 1);
							} else {
								date = i + "-" + (j + 1);
							}
							list.add(date);
						}
					} else {
						for (int j = 0; j < 12; j++) {
							if (j < 9) {
								date = i + "-0" + (j + 1);
							} else {
								date = i + "-" + (j + 1);
							}
							list.add(date);
						}
					}

				}

			}

			// 所有的月份已经准备好
//			System.out.println(list);
//			for(int i = 0;i < list.size();i++){
//				System.out.println(list.get(i));
//			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;

	}

	public static Integer calculateAge(String birthday) {
		try {
			if(StringUtils.isBlank(birthday)) {
				return 0;
			}
			birthday = birthday.trim();
			int selectYear = 0;
			int selectMonth = 1;
			int selectDay = 1;
			birthday = birthday.replaceAll("年", "-").replaceAll("月", "-").replaceAll("日", "");
			String separtorChar = "";
			if(birthday.contains("-")) {
				separtorChar = "-";
			}else if(birthday.contains("/")) {
				separtorChar = "/";
			}else if(NumberUtils.isDigits(birthday)) {
				if(Integer.valueOf(birthday) > 1900) {
					selectYear = Integer.valueOf(birthday);
				}else {
					log.warn("无法分割日期数据，值:" + birthday);
					return 0;
				}
			}else {
				log.warn("无法分割日期数据，值:" + birthday);
				return 0;
			}

			String[] tmp = StringUtils.split(birthday, separtorChar);
			Integer[] value = new Integer[tmp.length];
			for(int i=0; i< tmp.length; i++) {
				if(NumberUtils.isDigits(tmp[i])) {
					value[i] = Integer.valueOf(tmp[i]);
				}else {
					log.warn("解析日期出错，值:" + birthday);
					return 0;
				}
			}
			if(value.length >= 3) {
				if(value[0] > 999) {
					selectYear = value[0];
					selectMonth = value[1];
					selectDay = value[2];
				}else {
					selectYear = value[2];
					selectMonth = value[1];
					selectDay = value[0];
				}
			}else if(value.length == 2) {
				if(value[0] > 999) {
					selectYear = value[0];
					selectMonth = value[1];
				}else {
					selectYear = value[1];
					selectMonth = value[0];
				}
			}else if(value.length == 1) {
				if(value[0] > 999) {
					selectYear = value[0];
				}else {
					log.warn("解析日期出错，值:" + birthday);
					return 0;
				}
			}

			// 得到当前时间的年、月、日
			Calendar cal = Calendar.getInstance();
			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayNow = cal.get(Calendar.DATE);

			// 用当前年月日减去生日年月日
			int yearMinus = yearNow - selectYear;
			int monthMinus = monthNow - selectMonth;
			int dayMinus = dayNow - selectDay;

			if (yearMinus <= 0) {// 选了未来的年份
				return 0;
			} else {
				if (monthMinus < 0) {// 当前月>生日月
					return yearMinus - 1;
				} else if (monthMinus == 0 && dayMinus < 0) {// 同月份的，再根据日期计算年龄
					return yearMinus - 1;
				}
			}
			return yearMinus;
		} catch (Exception e) {
			return 0;
		}

	}

	public static boolean isWeek(String calculateDay) {
		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format1.parse(calculateDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)){
				return true;
			} else{
				return false;
			}
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}
	public static boolean isSaturday(String calculateDay) {
		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format1.parse(calculateDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)){
				return true;
			} else{
				return false;
			}
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public static Long getBetweenTimes(String end, String start) {
		Long endTimes = DateUtils.getDateTimes2(end);
		Long startTimes = DateUtils.getDateTimes2(start);
		return endTimes - startTimes;

	}

	/**
	 * 获取传入时间是周几
	 * @param date
	 * @return
	 */
	public static String getDayOfWeek(Date date) {
		String[] arr = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return arr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

    public static String getCurrentDate() {
        return defaultformatDate(new Date());
    }
	public static String getCurrentTime() {
		return defaultFormatDate(new Date());
	}
	public static String getCurrentDateTime() {
		String date = defaultformatDate(new Date())+" 00:00:00";
 		return date;
	}

//	public static void main(String[] args) {
//		List<String> list = getBetweenDays(DateUtils.parseDate("2019-12-30 10:00:00))",DateUtils.default_pattern),DateUtils.parseDate("2020-01-03 11:00:00))",DateUtils.default_pattern));
//		System.out.println(JSON.toJSONString(list));
//	}

	//获取本周的开始时间
	public static Date getBeginDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	//获取本周的结束时间
	public static Date getEndDayOfWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek(date));
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	//获取某个日期的开始时间
	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if(null != d) calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	//获取某个日期的结束时间
	public static Timestamp getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if(null != d) calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Timestamp(calendar.getTimeInMillis());
	}

	public static String getFirstDayDateOfLastMonth() {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH,-1);
		calendar.set(Calendar.DAY_OF_MONTH,1);

		calendar.set(Calendar.HOUR_OF_DAY,0);
		String date = dft.format(calendar.getTime());
		return date;
	}

	/**
	 * 两个日期时间相减 算出差值（分钟）
	 * @param endTime 结束时间
	 * @param startTime 开始时间
	 * */
	public static Long subtractDate(String endTime,String startTime) {
		Long startTimes = DateUtils.getDateTimes2(startTime); //毫秒
		Long endTimes = DateUtils.getDateTimes2(endTime); //毫秒
		Long betweenMinutes = endTimes - startTimes;
		if (betweenMinutes.intValue() <= 0) {
			betweenMinutes = 0L;
		}else {
            betweenMinutes = betweenMinutes/1000/60;
        }
		return betweenMinutes;
	}

	public static String addDateMinute(String day,Integer mins,Integer dateFormat) {
		//入参的格式
		// 24小时制
		String dateFormats = "yyyy-MM-dd HH:mm";
		if (dateFormat == 1) {
			dateFormats = "yyyy-MM-dd";
		}
		SimpleDateFormat format = new SimpleDateFormat(dateFormats);
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 24小时制
		cal.add(Calendar.MINUTE, mins);

		date = cal.getTime();
		if (dateFormat == 1) {
			return DateUtils.defaultformatDate(date);
		}else {
			return DateUtils.defaultFormatDate8(date);
		}

	}

    public static int getYearDaysNum(int year) {
		if(year==0){
			return LocalDate.now().lengthOfYear();
		}else{
			return LocalDate.of(year,1,1).lengthOfYear();
		}
    }


	public static List<String> getMonthBetween(String minDate, String maxDate) {
		List<String> result = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月
		try {
			Calendar min = Calendar.getInstance();
			Calendar max = Calendar.getInstance();

			min.setTime(sdf.parse(minDate));
			min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

			max.setTime(sdf.parse(maxDate));
			max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

			Calendar curr = min;
			while (curr.before(max)) {
				result.add(sdf.format(curr.getTime()));
				curr.add(Calendar.MONTH, 1);
			}

			// 实现排序方法
			Collections.sort(result, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					String str1 = (String) o1;
					String str2 = (String) o2;
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
					Date date1 = null;
					Date date2 = null;
					try {
						date1 = format.parse(str1);
						date2 = format.parse(str2);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (date2.compareTo(date1) > 0) {
						return -1;
					}
					return 1;
				}
			});
		}catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 得到本周周一-周六日期 0为周一 6为周日
	 * @return
	 */
	public static String getWeek(int day){
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=new GregorianCalendar();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(new Date());
//        cal.setTime(DateUtils.defaultParseDate1("2021-08-08"));
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+ day);
		Date first=cal.getTime();
		return formater.format(first);
	}

	// 毫秒时间戳转换为日、时、分、秒
	public static String timeStampToDhms(long milliseconds) {
		long day = TimeUnit.MILLISECONDS.toDays(milliseconds);
		long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
		StringBuilder sb = new StringBuilder();
		if (day != 0) {
			sb.append(day + "天");
		}
		sb.append(hours + "小时");
		sb.append(minutes + "分");
		sb.append(seconds + "秒");
		return sb.toString();
	}

	public static void main(String[] args) {
		List<String> list = getMonthBetween("2021-03", "2021-07");
		System.out.println("aa");
	}

}
