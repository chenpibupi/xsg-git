package com.xsg.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsg.system.domain.Article;

/**
 * 文章信息Mapper接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface ArticleMapper extends BaseMapper<Article>
{
    /**
     * 查询文章信息
     *
     * @param id 文章信息主键
     * @return 文章信息
     */
    public Article selectArticleById(Long id);

    /**
     * 查询文章信息列表
     *
     * @param article 文章信息
     * @return 文章信息集合
     */
    public List<Article> selectArticleList(Article article);

    /**
     * 新增文章信息
     *
     * @param article 文章信息
     * @return 结果
     */
    public int insertArticle(Article article);

    /**
     * 修改文章信息
     *
     * @param article 文章信息
     * @return 结果
     */
    public int updateArticle(Article article);

    /**
     * 删除文章信息
     *
     * @param id 文章信息主键
     * @return 结果
     */
    public int deleteArticleById(Long id);

    /**
     * 批量删除文章信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteArticleByIds(Long[] ids);
}