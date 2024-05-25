package com.xsg.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsg.system.domain.FinalArticle;
import com.xsg.system.domain.dto.FinalArticleDto;
import com.xsg.system.domain.vo.FinalArticleVo;

/**
 * 终稿文章信息Service接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface IFinalArticleService extends IService<FinalArticle>
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
     * @param finalArticleDto 终稿文章信息
     * @return 终稿文章信息集合
     */
    public List<FinalArticleVo> selectFinalArticleList(FinalArticleDto finalArticleDto);

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
     * 批量删除终稿文章信息
     *
     * @param ids 需要删除的终稿文章信息主键集合
     * @return 结果
     */
    public int deleteFinalArticleByIds(Long[] ids);

    /**
     * 删除终稿文章信息信息
     *
     * @param id 终稿文章信息主键
     * @return 结果
     */
    public int deleteFinalArticleById(Long id);
}