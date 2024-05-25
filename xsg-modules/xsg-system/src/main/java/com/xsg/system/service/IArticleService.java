package com.xsg.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsg.system.domain.Article;
import com.xsg.system.domain.dto.ArticleDto;
import com.xsg.system.domain.vo.ArticleVo;

/**
 * 文章信息Service接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface IArticleService extends IService<Article>
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
    public List<ArticleVo> selectArticleList(Article article);

    /**
     * 新增文章信息
     *
     * @param articleDto 文章信息
     * @return 结果
     */
    public int insertArticle(ArticleDto articleDto);

    /**
     * 修改文章信息
     *
     * @param articleDto 文章信息
     * @return 结果
     */
    public int updateArticle(ArticleDto articleDto);

    /**
     * 批量删除文章信息
     *
     * @param ids 需要删除的文章信息主键集合
     * @return 结果
     */
    public int deleteArticleByIds(Long[] ids);

    /**
     * 删除文章信息信息
     *
     * @param id 文章信息主键
     * @return 结果
     */
    public int deleteArticleById(Long id);
}