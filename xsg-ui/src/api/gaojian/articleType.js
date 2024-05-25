import request from '@/utils/request'

// 查询文章类型信息列表
export function listArticleType(query) {
  return request({
    url: '/gaojian/articleType/list',
    method: 'get',
    params: query
  })
}

// 查询文章类型信息详细
export function getArticleType(id) {
  return request({
    url: '/gaojian/articleType/' + id,
    method: 'get'
  })
}

// 新增文章类型信息
export function addArticleType(data) {
  return request({
    url: '/gaojian/articleType',
    method: 'post',
    data: data
  })
}

// 修改文章类型信息
export function updateArticleType(data) {
  return request({
    url: '/gaojian/articleType',
    method: 'put',
    data: data
  })
}

// 删除文章类型信息
export function delArticleType(id) {
  return request({
    url: '/gaojian/articleType/' + id,
    method: 'delete'
  })
}
