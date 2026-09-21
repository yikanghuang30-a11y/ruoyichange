<template>
  <div class="dashboard-container">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">总用户数</div>
          <div class="stat-value">{{ stats.totalUsers || 0 }}</div>
          <div class="stat-icon blue">👤</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">今日新增</div>
          <div class="stat-value">{{ stats.todayNewUsers || 0 }}</div>
          <div class="stat-icon green">➕</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">登录次数</div>
          <div class="stat-value">{{ stats.loginCount || 0 }}</div>
          <div class="stat-icon orange">🔑</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">在线状态</div>
          <div class="stat-value">运行中</div>
          <div class="stat-icon green">✅</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card>
          <div slot="header"><span>近 7 日用户增长趋势</span></div>
          <div ref="trendChart" style="height: 320px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header"><span>角色分布</span></div>
          <div ref="roleChart" style="height: 320px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardStats, getDashboardTrend, getDashboardRoleDist } from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {},
      trendChart: null,
      roleChart: null
    }
  },
  mounted() {
    this.loadStats()
    this.loadTrend()
    this.loadRoleDist()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.trendChart && this.trendChart.dispose()
    this.roleChart && this.roleChart.dispose()
  },
  methods: {
    async loadStats() {
      const res = await getDashboardStats()
      this.stats = res.data || {}
    },
    async loadTrend() {
      const res = await getDashboardTrend()
      const { dates = [], counts = [] } = res.data || {}
      this.$nextTick(() => {
        this.trendChart = echarts.init(this.$refs.trendChart)
        this.trendChart.setOption({
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: dates },
          yAxis: { type: 'value' },
          series: [{
            name: '新增用户',
            type: 'line',
            smooth: true,
            data: counts,
            itemStyle: { color: '#4caf50' },
            areaStyle: { color: 'rgba(76,175,80,0.15)' }
          }]
        })
      })
    },
    async loadRoleDist() {
      const res = await getDashboardRoleDist()
      const list = res.data || []
      this.$nextTick(() => {
        this.roleChart = echarts.init(this.$refs.roleChart)
        this.roleChart.setOption({
          tooltip: { trigger: 'item' },
          series: [{
            type: 'pie',
            radius: ['40%', '70%'],
            data: list,
            label: { formatter: '{b}: {c}' }
          }]
        })
      })
    },
    handleResize() {
      this.trendChart && this.trendChart.resize()
      this.roleChart && this.roleChart.resize()
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}
.stat-card {
  position: relative;
  overflow: hidden;
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}
.stat-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}
.stat-icon.blue { background: #e3f2fd; }
.stat-icon.green { background: #e8f5e9; }
.stat-icon.orange { background: #fff3e0; }
</style>
