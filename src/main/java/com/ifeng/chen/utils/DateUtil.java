package com.ifeng.chen.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public class DateUtil {

    private static final Logger LOGGER = LogManager.getLogger(DateUtil.class);

    public static final int FIELD_DATE = Calendar.DATE;
    public static final int FIELD_HOUR = Calendar.HOUR;
    public static final int FIELD_YEAR = Calendar.YEAR;

    public static final String MONGO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String MONGO_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String MONGO_DATE_FORMAT = "yyyy-MM-dd'T'00:00:00.000'Z'";

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String SHORT_FORMAT = "yyyy-MM-dd";

    public static final String SIMPLE_FORMAT = "yyyyMMddHHmmss";

    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");

    public static final TimeZone LOCAL_TIMEZONE = TimeZone.getDefault();

    public static final TimeZone CALENDAR_TIMEZONE = Calendar.getInstance().getTimeZone();

    public static Date parse(String source) {
        return parse(source, DEFAULT_FORMAT);
    }

    public static Date parse(String source, String format) {
        return parse(source, format, DEFAULT_TIMEZONE);
    }

    public static Date parse(String source, String format, TimeZone timeZone) {
        format = utcMatchFormat(source);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timeZone);
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            LOGGER.error("date format error illegal source");
        }
        return null;
    }

    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    public static String format(Date date, String format) {
        return format(date, format, DEFAULT_TIMEZONE);
    }

    public static String format(Date date, String format, TimeZone timeZone) {
        if (date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat.format(date);
    }

    public static String mongo2Default(String mongo) {
        Date mongoDate = parse(mongo, MONGO_FORMAT);
        return format(mongoDate, DEFAULT_FORMAT);
    }

    public static String mongo2DefaultCALENDAR(String mongo) {
        Date mongoDate = parse(mongo, MONGO_FORMAT1);
        return format(mongoDate, DEFAULT_FORMAT, CALENDAR_TIMEZONE);
    }

    public static String mongo2ShortFormat(String mongo) {
        Date mongoDate = parse(mongo, MONGO_FORMAT1);
        return format(mongoDate, SHORT_FORMAT, CALENDAR_TIMEZONE);
    }



    /**
     * 把mongodb类型的日期转成Date
     *
     * @param mongo mongodb类型的日期
     * @return Date
     */
    public static Date mongo2Date(String mongo) {
        Date mongoDate = parse(mongo, utcMatchFormat(mongo));
        return parse(format(mongoDate, DEFAULT_FORMAT), DEFAULT_FORMAT);
    }

    /**
     * 得到距离今天是i天的天数 1代表明天 2代表后天
     *
     * @param i
     * @return
     */
    public static Date getDay(int i) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }

    //获取当前时间（mongo格式的）
    public static Map<String, String> getMongoTime() {
        Map<String, String> mongoTime = new HashMap<>();
        mongoTime.put("$date", DateUtil.format(new Date(), DateUtil.MONGO_FORMAT, DateUtil.DEFAULT_TIMEZONE));
        return mongoTime;
    }

    /**
     * 获取时间字符串（mongo格式的）
     *
     * @param date 日期
     * @return 时间字符串（mongo格式的）
     */
    public static String getMongoTimeStr(Date date) {
        return DateUtil.format(date, DateUtil.MONGO_FORMAT, DateUtil.DEFAULT_TIMEZONE);
    }


    /**
     * 获取时间字符串（mongo格式的）
     *
     * @param date 日期
     * @return 时间字符串（mongo格式的）
     */
    public static String getMongoTimeStr2(Date date) {
        return DateUtil.format(date, DateUtil.MONGO_FORMAT, DateUtil.LOCAL_TIMEZONE);
    }

    /**
     * 字符串转换成Mong格式的String
     *
     * @param dateTime(yyyy-MM-dd 的字符串时间)
     * @return
     */
    public static String strToMongoFormatStr(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = getMongoTimeStr2(date);
        return str;
    }

    //获取5分钟前的时间（Date）
    public static Date getDateBefore(Integer time) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MILLISECOND, -time);// 5分钟之前的时间
        Date beforeD = beforeTime.getTime();
        return beforeD;
    }

    /**
     * 字符串转换成Mong格式的map
     *
     * @param dateTime(yyyy-MM-dd 的字符串时间)
     * @return
     */
    public static Map<String, String> strToMongoTime(String dateTime) {
        Map<String, String> mongoTime = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = getMongoTimeStr(date);
        mongoTime.put("$date", str);

        return mongoTime;
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param sDate
     * @param eDate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String sDate, String eDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(sDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(eDate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    //获取当前时间的n天前的日期
    public static String DateBeforeNDays(String sDate,Integer n){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date sDateD ;
        try {
            sDateD = format.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
            sDateD=new Date();
        }
        c.setTime(sDateD);
        c.add(Calendar.DATE, -n);
        Date date = c.getTime();
        return format.format(date);
    }



    //date 转 mongo
    public static Map<String, String> date2MongoTime(String date) {
        Map<String, String> mongoTime = new HashMap<>();
        Date mongoDate = parse(date, DEFAULT_FORMAT, LOCAL_TIMEZONE);
        mongoTime.put("$date", DateUtil.format(mongoDate, DateUtil.MONGO_FORMAT, DateUtil.DEFAULT_TIMEZONE));
        return mongoTime;
    }

    //获取当前时间（mongo格式的"yyyy-MM-dd'T'00:00:00.000'Z'"）
    public static Map<String, String> getMongoDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date target = calendar.getTime();
        Map<String, String> mongoTime = new HashMap<>();
        mongoTime.put("$date", DateUtil.format(target, DateUtil.MONGO_FORMAT, DateUtil.DEFAULT_TIMEZONE));
        return mongoTime;
    }

    /**
     * 获取昨天的日期 yyyy-MM-dd
     * @return
     */
    public static String getLastDay(){
        Date dNow = new Date();   //当前时间
        Date dBefore;
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        return sdf.format(dBefore);
    }


    //毫秒数转mongo
    public static Map<String, String> getMongoMsecTime(Long msec) {
        Map<String, String> mongoTime = new HashMap<>();
        mongoTime.put("$date", DateUtil.format(new Date(msec), DateUtil.MONGO_FORMAT, DateUtil.DEFAULT_TIMEZONE));
        return mongoTime;
    }

    /**
     * 将mongo的timeObject转成String类型的
     *
     * @param timeObject
     * @return
     */
    public static String object2MongoStr(Object timeObject) {

        SimpleDateFormat formatmongodb = new SimpleDateFormat(MONGO_FORMAT);
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        formatmongodb.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "UTC")));
        String mongoTime = "";
        if (Objects.nonNull(timeObject)) {
            try {
                mongoTime = format.format(formatmongodb.parse(timeObject.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return mongoTime;
        } else {
            return "";
        }
    }


    /**
     * 将mongo的timeObject转成Data类型的
     *
     * @param timeObject
     * @return
     */
    public static Date object2Date(Object timeObject) {

        SimpleDateFormat formatmongodb = new SimpleDateFormat(MONGO_FORMAT);
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        formatmongodb.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "UTC")));
        Date date = null;
        if (Objects.nonNull(timeObject)) {
            try {
                date = formatmongodb.parse(timeObject.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return date;
        }
    }

    /**
     * 毫秒转日期
     */
    public static String ms2Date(Long longTime) {

        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        return format.format(longTime);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return days
     */
    public static int differentDays(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    public static String utcMatchFormat(String date) {

        Objects.requireNonNull(date);

        for (UTCMatchFormat format : UTCMatchFormat.values()) {
            String regex = format.getPattern();
            if (date.matches(regex)) {
                return format.getFormat();
            }
        }
        return null;
    }

    public static String utcMatchFormatToDate(String date){

        Objects.requireNonNull(date);

        for (UTCMatchFormat format : UTCMatchFormat.values()) {
            String regex = format.getPattern();
            if (date.matches(regex)) {
                return format.getFormat();
            }
        }
        return null;
    }

}
