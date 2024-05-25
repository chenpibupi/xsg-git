import request from '@/utils/request'

// 查询稿件内容信息列表
export function listArticleContent(query) {
  return request({
    url: '/gaojian/articleContent/list',
    method: 'get',
    params: query
  })
}

// 查询稿件内容信息详细
export function getArticleContent(id) {
  return request({
    url: '/gaojian/articleContent/' + id,
    method: 'get'
  })

}// 根据稿件文章articleId查询稿件内容信息详细
// export function getArticleContentByArticleId(id) {
//   return request({
//     url: '/gaojian/articleContent/' + id,
//     method: 'get'
//   })
// }

// 新增稿件内容信息
export function addArticleContent(data) {
  return request({
    url: '/gaojian/articleContent',
    method: 'post',
    data: data
  })
}

// 修改稿件内容信息
export function updateArticleContent(data) {
  return request({
    url: '/gaojian/articleContent',
    method: 'put',
    data: data
  })
}

// 删除稿件内容信息
export function delArticleContent(id) {
  return request({
    url: '/gaojian/articleContent/' + id,
    method: 'delete'
  })
}
