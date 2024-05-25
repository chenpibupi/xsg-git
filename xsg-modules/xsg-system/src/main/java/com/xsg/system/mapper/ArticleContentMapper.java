package com.xsg.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsg.system.domain.ArticleContent;

/**
 * 稿件内容信息Mapper接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface ArticleContentMapper extends BaseMapper<ArticleContent>
{
    /**
     * 查询稿件内容信息
     *
     * @param id 稿件内容信息主键
     * @return 稿件内容信息
     */
    public ArticleContent selectArticleContentById(Long id);

    /**
     * 查询稿件内容信息列表
     *
     * @param articleContent 稿件内容信息
     * @return 稿件内容信息集合
     */
    public List<ArticleContent> selectArticleContentList(ArticleContent articleContent);

    /**
     * 新增稿件内容信息
     *
     * @param articleContent 稿件内容信息
     * @return 结果
     */
    public int insertArticleContent(ArticleContent articleContent);

    /**
     * 修改稿件内容信息
     *
     * @param articleContent 稿件内容信息
     * @return 结果
     */
    public int updateArticleContent(ArticleContent articleContent);

    /**
     * 删除稿件内容信息
     *
     * @param id 稿件内容信息主键
     * @return 结果
     */
    public int deleteArticleContentById(Long id);

    /**
     * 批量删除稿件内容信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteArticleContentByIds(Long[] ids);
}