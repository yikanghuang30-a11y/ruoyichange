import request from '@/utils/request'

export function getDashboardStats() {
  return request({
    url: '/system/dashboard/stats',
    method: 'get'
  })
}

export function getDashboardTrend() {
  return request({
    url: '/system/dashboard/trend',
    method: 'get'
  })
}

export function getDashboardRoleDist() {
  return request({
    url: '/system/dashboard/roleDist',
    method: 'get'
  })
}

export function getDashboardRecent() {
  return request({
    url: '/system/dashboard/recent',
    method: 'get'
  })
}
