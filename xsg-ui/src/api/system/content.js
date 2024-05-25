import request from '@/utils/request'

// 查询初稿内容信息列表
export function listContent(query) {
  return request({
    url: '/system/content/list',
    method: 'get',
    params: query
  })
}

// 查询初稿内容信息详细
export function getContent(id) {
  return request({
    url: '/system/content/' + id,
    method: 'get'
  })
}

// 新增初稿内容信息
export function addContent(data) {
  return request({
    url: '/system/content',
    method: 'post',
    data: data
  })
}

// 修改初稿内容信息
export function updateContent(data) {
  return request({
    url: '/system/content',
    method: 'put',
    data: data
  })
}

// 删除初稿内容信息
export function delContent(id) {
  return request({
    url: '/system/content/' + id,
    method: 'delete'
  })
}
