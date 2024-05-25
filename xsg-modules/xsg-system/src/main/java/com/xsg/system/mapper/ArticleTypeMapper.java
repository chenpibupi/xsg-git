package com.xsg.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsg.system.domain.ArticleType;

/**
 * 文章类型信息Mapper接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface ArticleTypeMapper extends BaseMapper<ArticleType>
{
    /**
     * 查询文章类型信息
     *
     * @param id 文章类型信息主键
     * @return 文章类型信息
     */
    public ArticleType selectArticleTypeById(Long id);

    /**
     * 查询文章类型信息列表
     *
     * @param articleType 文章类型信息
     * @return 文章类型信息集合
     */
    public List<ArticleType> selectArticleTypeList(ArticleType articleType);

    /**
     * 新增文章类型信息
     *
     * @param articleType 文章类型信息
     * @return 结果
     */
    public int insertArticleType(ArticleType articleType);

    /**
     * 修改文章类型信息
     *
     * @param articleType 文章类型信息
     * @return 结果
     */
    public int updateArticleType(ArticleType articleType);

    /**
     * 删除文章类型信息
     *
     * @param id 文章类型信息主键
     * @return 结果
     */
    public int deleteArticleTypeById(Long id);

    /**
     * 批量删除文章类型信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteArticleTypeByIds(Long[] ids);
}