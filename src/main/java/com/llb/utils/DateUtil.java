package com.llb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式化工具
 * @Author llb
 * Date on 2020/3/13
 */

public class DateUtil {

    /**
     * 日期格式化，将日期转为固定格式
     * @param date
     * @param format 日期的格式，比如：yyyy-MM-dd
     * @return
     */
    public String formatDate(Date date, String format){
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

}
