import request from '@/utils/request'

// 查询终稿文章信息列表
export function listFinalArticle(query) {
  return request({
    url: '/gaojian/finalArticle/list',
    method: 'get',
    params: query
  })
}

// 查询终稿文章信息详细
export function getFinalArticle(id) {
  return request({
    url: '/gaojian/finalArticle/' + id,
    method: 'get'
  })
}

// 新增终稿文章信息
export function addFinalArticle(data) {
  return request({
    url: '/gaojian/finalArticle',
    method: 'post',
    data: data
  })
}

// 修改终稿文章信息
export function updateFinalArticle(data) {
  return request({
    url: '/gaojian/finalArticle',
    method: 'put',
    data: data
  })
}

// 删除终稿文章信息
export function delFinalArticle(id) {
  return request({
    url: '/gaojian/finalArticle/' + id,
    method: 'delete'
  })
}
