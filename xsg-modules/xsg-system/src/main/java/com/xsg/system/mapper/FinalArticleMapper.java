package com.xsg.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsg.system.domain.FinalArticle;

/**
 * 终稿文章信息Mapper接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface FinalArticleMapper extends BaseMapper<FinalArticle>
{
    /**
     * 查询终稿文章信息
     *
     * @param id 终稿文章信息主键
     * @return 终稿文章信息
     */
    public FinalArticle selectFinalArticleById(Long id);

    /**
     * 查询终稿文章信息列表
     *
     * @param finalArticle 终稿文章信息
     * @return 终稿文章信息集合
     */
    public List<FinalArticle> selectFinalArticleList(FinalArticle finalArticle);

    /**
     * 新增终稿文章信息
     *
     * @param finalArticle 终稿文章信息
     * @return 结果
     */
    public int insertFinalArticle(FinalArticle finalArticle);

    /**
     * 修改终稿文章信息
     *
     * @param finalArticle 终稿文章信息
     * @return 结果
     */
    public int updateFinalArticle(FinalArticle finalArticle);

    /**
     * 删除终稿文章信息
     *
     * @param id 终稿文章信息主键
     * @return 结果
     */
    public int deleteFinalArticleById(Long id);

    /**
     * 批量删除终稿文章信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinalArticleByIds(Long[] ids);
}