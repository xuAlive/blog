package com.xu.blog.controller;

import com.xu.blog.utils.BaZiUtil;
import com.xu.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 生辰八字计算接口
 */
@Slf4j
@RequestMapping("/blog/bazi")
@RestController
public class BaZiController {

    /**
     * 根据出生日期计算生辰八字
     *
     * @param year  出生年份
     * @param month 出生月份（1-12）
     * @param day   出生日期
     * @param hour  出生小时（0-23），可选参数
     * @return 八字信息
     */
    @GetMapping("/calculate")
    public Response<Map<String, Object>> calculate(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("day") Integer day,
            @RequestParam(value = "hour", required = false) Integer hour) {

        // 参数校验
        if (year == null || year < 1 || year > 9999) {
            return Response.error("年份参数无效");
        }
        if (month == null || month < 1 || month > 12) {
            return Response.error("月份参数无效，应为1-12");
        }
        if (day == null || day < 1 || day > 31) {
            return Response.error("日期参数无效，应为1-31");
        }
        if (hour != null && (hour < 0 || hour > 23)) {
            return Response.error("小时参数无效，应为0-23");
        }

        // 校验日期合法性
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            daysInMonth[1] = 29;
        }
        if (day > daysInMonth[month - 1]) {
            return Response.error("日期超出该月最大天数");
        }

        try {
            Map<String, Object> result = BaZiUtil.calculate(year, month, day, hour);
            log.info("计算八字: {}-{}-{} {}时, 结果: {}", year, month, day, hour, result.get("baZi"));
            return Response.success(result);
        } catch (Exception e) {
            log.error("计算八字出错", e);
            return Response.error("计算八字出错: " + e.getMessage());
        }
    }
}
