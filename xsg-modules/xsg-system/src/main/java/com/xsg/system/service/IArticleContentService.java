package com.xsg.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsg.system.domain.ArticleContent;
import com.xsg.system.domain.dto.ArticleContentDto;
import com.xsg.system.domain.vo.ArticleContentVo;

/**
 * 稿件内容信息Service接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface IArticleContentService extends IService<ArticleContent>
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
    public List<ArticleContentVo> selectArticleContentList(ArticleContentDto articleContent);

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
     * 批量删除稿件内容信息
     *
     * @param ids 需要删除的稿件内容信息主键集合
     * @return 结果
     */
    public int deleteArticleContentByIds(Long[] ids);

    /**
     * 删除稿件内容信息信息
     *
     * @param id 稿件内容信息主键
     * @return 结果
     */
    public int deleteArticleContentById(Long id);
}