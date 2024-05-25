import request from '@/utils/request'

// 查询审核记录信息列表
export function listArticleRecord(query) {
  return request({
    url: '/gaojian/articleRecord/list',
    method: 'get',
    params: query
  })
}

// 查询审核记录信息详细
export function getArticleRecord(id) {
  return request({
    url: '/gaojian/articleRecord/' + id,
    method: 'get'
  })
}

// 新增审核记录信息
export function addArticleRecord(data) {
  return request({
    url: '/gaojian/articleRecord',
    method: 'post',
    data: data
  })
}

// 修改审核记录信息
export function updateArticleRecord(data) {
  return request({
    url: '/gaojian/articleRecord',
    method: 'put',
    data: data
  })
}

// 删除审核记录信息
export function delArticleRecord(id) {
  return request({
    url: '/gaojian/articleRecord/' + id,
    method: 'delete'
  })
}
