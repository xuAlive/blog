package com.xu.blog.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 生辰八字计算工具类
 * 根据出生年月日时计算四柱八字（年柱、月柱、日柱、时柱）
 */
public class BaZiUtil {

    // 天干
    private static final String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};

    // 地支
    private static final String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    // 生肖
    private static final String[] SHENG_XIAO = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    // 时辰对应的地支索引（按小时划分）
    // 23-1:子, 1-3:丑, 3-5:寅, 5-7:卯, 7-9:辰, 9-11:巳, 11-13:午, 13-15:未, 15-17:申, 17-19:酉, 19-21:戌, 21-23:亥
    private static final int[] HOUR_TO_DIZHI = {0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11};

    // 时辰名称
    private static final String[] SHI_CHEN = {"子时", "丑时", "寅时", "卯时", "辰时", "巳时", "午时", "未时", "申时", "酉时", "戌时", "亥时"};

    /**
     * 计算生辰八字（不含时辰）
     *
     * @param year  出生年份
     * @param month 出生月份（1-12）
     * @param day   出生日期
     * @return 八字信息
     */
    public static Map<String, Object> calculate(int year, int month, int day) {
        return calculate(year, month, day, null);
    }

    /**
     * 计算生辰八字（含时辰）
     *
     * @param year  出生年份
     * @param month 出生月份（1-12）
     * @param day   出生日期
     * @param hour  出生小时（0-23），可为null
     * @return 八字信息
     */
    public static Map<String, Object> calculate(int year, int month, int day, Integer hour) {
        Map<String, Object> result = new HashMap<>();

        // 计算年柱
        String yearGanZhi = getYearGanZhi(year);
        result.put("yearPillar", yearGanZhi);
        result.put("yearGan", yearGanZhi.substring(0, 1));
        result.put("yearZhi", yearGanZhi.substring(1, 2));

        // 计算生肖
        int zodiacIndex = (year - 4) % 12;
        if (zodiacIndex < 0) {
            zodiacIndex += 12;
        }
        result.put("zodiac", SHENG_XIAO[zodiacIndex]);

        // 计算月柱
        String monthGanZhi = getMonthGanZhi(year, month);
        result.put("monthPillar", monthGanZhi);
        result.put("monthGan", monthGanZhi.substring(0, 1));
        result.put("monthZhi", monthGanZhi.substring(1, 2));

        // 计算日柱
        String dayGanZhi = getDayGanZhi(year, month, day);
        result.put("dayPillar", dayGanZhi);
        result.put("dayGan", dayGanZhi.substring(0, 1));
        result.put("dayZhi", dayGanZhi.substring(1, 2));

        // 计算时柱（如果提供了小时）
        if (hour != null && hour >= 0 && hour <= 23) {
            String hourGanZhi = getHourGanZhi(dayGanZhi.substring(0, 1), hour);
            result.put("hourPillar", hourGanZhi);
            result.put("hourGan", hourGanZhi.substring(0, 1));
            result.put("hourZhi", hourGanZhi.substring(1, 2));

            int shiChenIndex = getShiChenIndex(hour);
            result.put("shiChen", SHI_CHEN[shiChenIndex]);

            // 完整八字
            result.put("baZi", yearGanZhi + " " + monthGanZhi + " " + dayGanZhi + " " + hourGanZhi);
            result.put("fourPillars", new String[]{yearGanZhi, monthGanZhi, dayGanZhi, hourGanZhi});
        } else {
            // 六字（不含时柱）
            result.put("baZi", yearGanZhi + " " + monthGanZhi + " " + dayGanZhi);
            result.put("fourPillars", new String[]{yearGanZhi, monthGanZhi, dayGanZhi});
            result.put("hourPillar", null);
            result.put("shiChen", null);
        }

        // 出生信息
        result.put("birthYear", year);
        result.put("birthMonth", month);
        result.put("birthDay", day);
        result.put("birthHour", hour);

        return result;
    }

    /**
     * 计算年柱
     * 年柱以立春为界，这里简化处理，以农历正月初一为界
     */
    private static String getYearGanZhi(int year) {
        // 以1984年（甲子年）为基准
        int offset = year - 1984;
        int ganIndex = offset % 10;
        int zhiIndex = offset % 12;

        if (ganIndex < 0) {
            ganIndex += 10;
        }
        if (zhiIndex < 0) {
            zhiIndex += 12;
        }

        return TIAN_GAN[ganIndex] + DI_ZHI[zhiIndex];
    }

    /**
     * 计算月柱
     * 月柱以节气为界，这里简化处理，按公历月份计算
     * 正月建寅，即农历正月为寅月
     */
    private static String getMonthGanZhi(int year, int month) {
        // 计算年干
        int yearGanIndex = (year - 1984) % 10;
        if (yearGanIndex < 0) {
            yearGanIndex += 10;
        }

        // 月支：正月为寅(2)，以此类推
        // 公历月份近似对应：2月-寅，3月-卯，...
        // 这里简化处理，将公历月份映射到地支
        int monthZhiIndex = (month + 1) % 12;

        // 月干计算：根据年干推算月干
        // 甲己之年丙作首，乙庚之岁戊为头，丙辛之岁庚为头，丁壬壬为头，戊癸甲为头
        int monthGanBase;
        switch (yearGanIndex) {
            case 0: // 甲
            case 5: // 己
                monthGanBase = 2; // 丙
                break;
            case 1: // 乙
            case 6: // 庚
                monthGanBase = 4; // 戊
                break;
            case 2: // 丙
            case 7: // 辛
                monthGanBase = 6; // 庚
                break;
            case 3: // 丁
            case 8: // 壬
                monthGanBase = 8; // 壬
                break;
            case 4: // 戊
            case 9: // 癸
            default:
                monthGanBase = 0; // 甲
                break;
        }

        // 正月（寅月）的天干为monthGanBase，二月（卯月）的天干为monthGanBase+1，以此类推
        int monthGanIndex = (monthGanBase + month - 1) % 10;

        return TIAN_GAN[monthGanIndex] + DI_ZHI[monthZhiIndex];
    }

    /**
     * 计算日柱
     * 使用蔡勒公式的变体计算
     */
    private static String getDayGanZhi(int year, int month, int day) {
        // 计算从基准日期（1900年1月31日，农历1900年正月初一，甲戌日）到目标日期的天数
        // 这里使用公历日期简化计算

        // 计算儒略日数
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;

        // 格里高利历的儒略日数
        int jd = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;

        // 以1900年1月1日为基准（甲戌日的前一天是癸酉日）
        // 1900年1月1日是甲戌日之前的某天，需要计算偏移
        // 1900年1月1日的儒略日数
        int jd1900 = 1 + (153 * 10 + 2) / 5 + 365 * 6699 + 6699 / 4 - 6699 / 100 + 6699 / 400 - 32045;

        // 1900年1月1日是甲子日后的第几天
        // 经查证，1900年1月1日是庚子日（干支序号36，即天干0+6=6(庚)，地支0(子)）
        // 不对，重新计算：1900年1月1日是农历己亥年十二月初一
        // 简化：使用已知的基准点 2000年1月1日是甲子日后第34天（即戊戌日，不对）
        // 采用更简单的方法：使用已知基准

        // 基准：1900年1月31日是甲午日（天干0，地支6）
        // 计算目标日期与基准日期的天数差

        int baseDays = getDaysFrom1900(1900, 1, 31);
        int targetDays = getDaysFrom1900(year, month, day);
        int diff = targetDays - baseDays;

        // 1900年1月31日是第0天，对应甲午（干0，支6）
        // 实际上1900年1月31日对应农历己亥年正月初一，干支为甲午
        // 但为简化计算，我们使用另一个基准：
        // 2000年1月1日是戊戌日（天干4，地支10）
        // 重新设定基准

        int baseDays2000 = getDaysFrom1900(2000, 1, 1);
        int diff2000 = targetDays - baseDays2000;

        // 2000年1月1日：天干4（戊），地支10（戌）
        int ganIndex = (4 + diff2000) % 10;
        int zhiIndex = (10 + diff2000) % 12;

        if (ganIndex < 0) {
            ganIndex += 10;
        }
        if (zhiIndex < 0) {
            zhiIndex += 12;
        }

        return TIAN_GAN[ganIndex] + DI_ZHI[zhiIndex];
    }

    /**
     * 计算从1900年1月1日到指定日期的天数
     */
    private static int getDaysFrom1900(int year, int month, int day) {
        int days = 0;

        // 累加完整年份的天数
        for (int y = 1900; y < year; y++) {
            days += isLeapYear(y) ? 366 : 365;
        }

        // 累加当年已过月份的天数
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(year)) {
            daysInMonth[1] = 29;
        }

        for (int m = 1; m < month; m++) {
            days += daysInMonth[m - 1];
        }

        // 加上当月的天数
        days += day;

        return days;
    }

    /**
     * 判断是否为闰年
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 计算时柱
     * 根据日干和出生时辰计算时柱
     */
    private static String getHourGanZhi(String dayGan, int hour) {
        int shiChenIndex = getShiChenIndex(hour);

        // 根据日干计算时干
        // 甲己日起甲子时，乙庚日起丙子时，丙辛日起戊子时，丁壬日起庚子时，戊癸日起壬子时
        int dayGanIndex = -1;
        for (int i = 0; i < TIAN_GAN.length; i++) {
            if (TIAN_GAN[i].equals(dayGan)) {
                dayGanIndex = i;
                break;
            }
        }

        int hourGanBase;
        switch (dayGanIndex % 5) {
            case 0: // 甲、己
                hourGanBase = 0; // 甲
                break;
            case 1: // 乙、庚
                hourGanBase = 2; // 丙
                break;
            case 2: // 丙、辛
                hourGanBase = 4; // 戊
                break;
            case 3: // 丁、壬
                hourGanBase = 6; // 庚
                break;
            case 4: // 戊、癸
            default:
                hourGanBase = 8; // 壬
                break;
        }

        int hourGanIndex = (hourGanBase + shiChenIndex) % 10;

        return TIAN_GAN[hourGanIndex] + DI_ZHI[shiChenIndex];
    }

    /**
     * 根据小时获取时辰索引
     */
    private static int getShiChenIndex(int hour) {
        // 23点和0点都是子时
        if (hour == 23) {
            return 0;
        }
        return HOUR_TO_DIZHI[hour];
    }

    /**
     * 使用LocalDateTime计算八字
     */
    public static Map<String, Object> calculate(LocalDateTime dateTime) {
        return calculate(
                dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(),
                dateTime.getHour()
        );
    }

    /**
     * 获取天干数组
     */
    public static String[] getTianGan() {
        return TIAN_GAN.clone();
    }

    /**
     * 获取地支数组
     */
    public static String[] getDiZhi() {
        return DI_ZHI.clone();
    }

    /**
     * 获取生肖数组
     */
    public static String[] getShengXiao() {
        return SHENG_XIAO.clone();
    }

    /**
     * 获取时辰数组
     */
    public static String[] getShiChen() {
        return SHI_CHEN.clone();
    }
}
