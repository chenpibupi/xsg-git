import request from '@/utils/request'

// 查询轮播图数据库管理，用于存储轮播图信息列表
export function listCarouselImages(query) {
  return request({
    url: '/system/carouselImages/list',
    method: 'get',
    params: query
  })
}

// 查询轮播图数据库管理，用于存储轮播图信息详细
export function getCarouselImages(id) {
  return request({
    url: '/system/carouselImages/' + id,
    method: 'get'
  })
}

// 新增轮播图数据库管理，用于存储轮播图信息
export function addCarouselImages(data) {
  return request({
    url: '/system/carouselImages',
    method: 'post',
    data: data
  })
}

// 修改轮播图数据库管理，用于存储轮播图信息
export function updateCarouselImages(data) {
  return request({
    url: '/system/carouselImages',
    method: 'put',
    data: data
  })
}

// 删除轮播图数据库管理，用于存储轮播图信息
export function delCarouselImages(id) {
  return request({
    url: '/system/carouselImages/' + id,
    method: 'delete'
  })
}
