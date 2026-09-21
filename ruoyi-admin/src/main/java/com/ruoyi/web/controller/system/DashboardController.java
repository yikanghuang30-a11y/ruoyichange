package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/system/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 核心统计卡片
     */
    @GetMapping("/stats")
    public AjaxResult stats() {
        Map<String, Object> result = new HashMap<>();

        // 用若依 Service 查所有用户
        List<SysUser> users = sysUserService.selectUserList(new SysUser());
        Long totalUsers = (long) users.size();
        result.put("totalUsers", totalUsers);

        // 今日新增（按创建日期过滤）
        String today = LocalDate.now().toString();
        long todayNew = users.stream()
                .filter(u -> u.getCreateTime() != null
                        && u.getCreateTime().toString().startsWith(today))
                .count();
        result.put("todayNewUsers", todayNew);

        // 登录次数（示例，用用户总数近似）
        result.put("loginCount", totalUsers);

        return AjaxResult.success(result);
    }

    /**
     * 近 7 日新增用户趋势
     */
    @GetMapping("/trend")
    public AjaxResult trend() {
        List<SysUser> users = sysUserService.selectUserList(new SysUser());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");

        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = date.toString();   // yyyy-MM-dd
            dates.add(date.format(fmt));

            long count = users.stream()
                    .filter(u -> u.getCreateTime() != null
                            && u.getCreateTime().toString().startsWith(dateStr))
                    .count();
            counts.add(count);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("counts", counts);
        return AjaxResult.success(result);
    }

    /**
     * 角色分布
     */
    @GetMapping("/roleDist")
    public AjaxResult roleDist() {
        // 简化：写死示例数据，后续可接入角色表统计
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Map.of("name", "管理员", "value", 1));
        list.add(Map.of("name", "普通角色", "value", 2));
        return AjaxResult.success(list);
    }

    /**
     * 最近用户
     */
    @GetMapping("/recent")
    public AjaxResult recent() {
        return AjaxResult.success(Collections.emptyList());
    }
}