package com.xu.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xu.schedule.domain.Schedule;
import com.xu.schedule.domain.Shift;
import com.xu.schedule.mapper.ScheduleMapper;
import com.xu.schedule.mapper.ShiftMapper;
import com.xu.schedule.param.vo.ScheduleStatisticsVO;
import com.xu.schedule.param.vo.ScheduleStatisticsVO.ChartItem;
import com.xu.schedule.param.vo.ScheduleStatisticsVO.DateStatItem;
import com.xu.schedule.param.vo.ScheduleStatisticsVO.EmployeeStatItem;
import com.xu.schedule.service.ScheduleStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 排班统计服务实现
 */
@Service
public class ScheduleStatisticsServiceImpl implements ScheduleStatisticsService {

    private final ScheduleMapper scheduleMapper;
    private final ShiftMapper shiftMapper;

    private static final Map<Integer, String> STATUS_MAP = new HashMap<>();

    static {
        STATUS_MAP.put(1, "正常");
        STATUS_MAP.put(2, "请假");
        STATUS_MAP.put(3, "调休");
        STATUS_MAP.put(4, "加班");
    }

    public ScheduleStatisticsServiceImpl(ScheduleMapper scheduleMapper, ShiftMapper shiftMapper) {
        this.scheduleMapper = scheduleMapper;
        this.shiftMapper = shiftMapper;
    }

    @Override
    public ScheduleStatisticsVO getWeeklyStatistics(String account, LocalDate date) {
        // 获取本周的开始和结束日期
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        ScheduleStatisticsVO vo = buildStatistics(account, startOfWeek, endOfWeek);
        vo.setPeriodType("week");
        vo.setPeriodDesc(String.format("%d年第%d周", date.getYear(), getWeekOfYear(date)));
        return vo;
    }

    @Override
    public ScheduleStatisticsVO getMonthlyStatistics(String account, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        ScheduleStatisticsVO vo = buildStatistics(account, startOfMonth, endOfMonth);
        vo.setPeriodType("month");
        vo.setPeriodDesc(String.format("%d年%d月", year, month));
        return vo;
    }

    @Override
    public ScheduleStatisticsVO getQuarterlyStatistics(String account, int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = quarter * 3;

        LocalDate startOfQuarter = LocalDate.of(year, startMonth, 1);
        LocalDate endOfQuarter = LocalDate.of(year, endMonth, 1).with(TemporalAdjusters.lastDayOfMonth());

        ScheduleStatisticsVO vo = buildStatistics(account, startOfQuarter, endOfQuarter);
        vo.setPeriodType("quarter");
        vo.setPeriodDesc(String.format("%d年第%d季度", year, quarter));
        return vo;
    }

    @Override
    public ScheduleStatisticsVO getYearlyStatistics(String account, int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        ScheduleStatisticsVO vo = buildStatistics(account, startOfYear, endOfYear);
        vo.setPeriodType("year");
        vo.setPeriodDesc(String.format("%d年", year));
        return vo;
    }

    @Override
    public ScheduleStatisticsVO getCustomStatistics(String account, LocalDate startDate, LocalDate endDate) {
        ScheduleStatisticsVO vo = buildStatistics(account, startDate, endDate);
        vo.setPeriodType("custom");
        vo.setPeriodDesc(String.format("%s 至 %s", startDate, endDate));
        return vo;
    }

    @Override
    public String exportStatistics(String account, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = querySchedules(account, startDate, endDate);
        Map<Long, Shift> shiftMap = getShiftMap();

        StringBuilder sb = new StringBuilder();
        // CSV 头部
        sb.append("日期,员工账号,员工姓名,班次名称,状态,工作时长(小时),备注\n");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Schedule schedule : schedules) {
            sb.append(schedule.getScheduleDate().format(dateFormatter)).append(",");
            sb.append(schedule.getAccount()).append(",");
            sb.append(schedule.getEmployeeName() != null ? schedule.getEmployeeName() : "").append(",");
            sb.append(schedule.getShiftName() != null ? schedule.getShiftName() : "").append(",");
            sb.append(STATUS_MAP.getOrDefault(schedule.getStatus(), "未知")).append(",");
            sb.append(calculateWorkHours(schedule.getShiftId(), shiftMap)).append(",");
            sb.append(schedule.getRemark() != null ? schedule.getRemark().replace(",", "，") : "").append("\n");
        }

        return sb.toString();
    }

    /**
     * 构建统计数据
     */
    private ScheduleStatisticsVO buildStatistics(String account, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = querySchedules(account, startDate, endDate);
        Map<Long, Shift> shiftMap = getShiftMap();

        ScheduleStatisticsVO vo = new ScheduleStatisticsVO();
        vo.setStartDate(startDate.toString());
        vo.setEndDate(endDate.toString());
        vo.setTotalCount(schedules.size());

        // 计算总工作时长
        double totalHours = schedules.stream()
                .mapToDouble(s -> calculateWorkHours(s.getShiftId(), shiftMap))
                .sum();
        vo.setTotalHours(Math.round(totalHours * 100.0) / 100.0);

        // 按班次统计
        Map<String, Integer> shiftCountMap = schedules.stream()
                .filter(s -> s.getShiftName() != null)
                .collect(Collectors.groupingBy(
                        Schedule::getShiftName,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
        vo.setShiftStats(shiftCountMap.entrySet().stream()
                .map(e -> new ChartItem(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));

        // 按状态统计
        Map<String, Integer> statusCountMap = schedules.stream()
                .collect(Collectors.groupingBy(
                        s -> STATUS_MAP.getOrDefault(s.getStatus(), "未知"),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
        vo.setStatusStats(statusCountMap.entrySet().stream()
                .map(e -> new ChartItem(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));

        // 按员工统计
        Map<String, List<Schedule>> employeeScheduleMap = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getAccount));

        List<EmployeeStatItem> employeeStats = new ArrayList<>();
        for (Map.Entry<String, List<Schedule>> entry : employeeScheduleMap.entrySet()) {
            EmployeeStatItem item = new EmployeeStatItem();
            item.setAccount(entry.getKey());
            List<Schedule> empSchedules = entry.getValue();

            if (!empSchedules.isEmpty()) {
                item.setEmployeeName(empSchedules.get(0).getEmployeeName());
            }
            item.setScheduleCount(empSchedules.size());

            double empHours = empSchedules.stream()
                    .mapToDouble(s -> calculateWorkHours(s.getShiftId(), shiftMap))
                    .sum();
            item.setWorkHours(Math.round(empHours * 100.0) / 100.0);

            // 按状态细分
            Map<Integer, Long> statusCount = empSchedules.stream()
                    .collect(Collectors.groupingBy(Schedule::getStatus, Collectors.counting()));
            item.setNormalCount(statusCount.getOrDefault(1, 0L).intValue());
            item.setLeaveCount(statusCount.getOrDefault(2, 0L).intValue());
            item.setAdjustCount(statusCount.getOrDefault(3, 0L).intValue());
            item.setOvertimeCount(statusCount.getOrDefault(4, 0L).intValue());

            employeeStats.add(item);
        }
        vo.setEmployeeStats(employeeStats);

        // 按日期统计（用于趋势图）
        Map<LocalDate, Long> dateCountMap = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getScheduleDate, Collectors.counting()));

        List<DateStatItem> dateStats = dateCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new DateStatItem(e.getKey().toString(), e.getValue().intValue()))
                .collect(Collectors.toList());
        vo.setDateStats(dateStats);

        return vo;
    }

    /**
     * 查询排班数据
     */
    private List<Schedule> querySchedules(String account, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Schedule::getIsDelete, 0)
                .ge(Schedule::getScheduleDate, startDate)
                .le(Schedule::getScheduleDate, endDate);

        if (StringUtils.hasText(account)) {
            wrapper.eq(Schedule::getAccount, account);
        }

        wrapper.orderByAsc(Schedule::getScheduleDate);
        return scheduleMapper.selectList(wrapper);
    }

    /**
     * 获取班次映射
     */
    private Map<Long, Shift> getShiftMap() {
        List<Shift> shifts = shiftMapper.selectList(
                new LambdaQueryWrapper<Shift>().eq(Shift::getIsDelete, 0)
        );
        return shifts.stream().collect(Collectors.toMap(Shift::getId, s -> s));
    }

    /**
     * 计算工作时长
     */
    private double calculateWorkHours(Long shiftId, Map<Long, Shift> shiftMap) {
        if (shiftId == null || !shiftMap.containsKey(shiftId)) {
            return 0;
        }
        Shift shift = shiftMap.get(shiftId);
        if (shift.getStartTime() == null || shift.getEndTime() == null) {
            return 0;
        }

        LocalTime start = shift.getStartTime();
        LocalTime end = shift.getEndTime();

        // 处理跨天情况（如晚班 22:00 - 08:00）
        long minutes;
        if (end.isBefore(start)) {
            // 跨天
            minutes = (24 * 60 - start.toSecondOfDay() / 60) + end.toSecondOfDay() / 60;
        } else {
            minutes = (end.toSecondOfDay() - start.toSecondOfDay()) / 60;
        }

        return minutes / 60.0;
    }

    /**
     * 获取周数
     */
    private int getWeekOfYear(LocalDate date) {
        return date.get(java.time.temporal.WeekFields.of(Locale.CHINA).weekOfYear());
    }
}
