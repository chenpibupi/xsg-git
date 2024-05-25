package com.xsg.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsg.system.domain.ArticleType;
import com.xsg.system.mapper.ArticleTypeMapper;
import com.xsg.system.service.IArticleTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章类型信息Service业务层处理
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Slf4j
@Service
public class ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeMapper, ArticleType> implements IArticleTypeService {
    @Autowired
    private ArticleTypeMapper articleTypeMapper;

    /**
     * 查询文章类型信息
     *
     * @param id 文章类型信息主键
     * @return 文章类型信息
     */
    @Override
    public ArticleType selectArticleTypeById(Long id) {
        return articleTypeMapper.selectById(id);
    }

    /**
     * 查询文章类型信息列表
     *
     * @param articleType 文章类型信息
     * @return 文章类型信息
     */
    @Override
    public List<ArticleType> selectArticleTypeList(ArticleType articleType) {
        String typeName = articleType.getTypeName();
        List<ArticleType> list = this.lambdaQuery()
                .like(typeName != null, ArticleType::getTypeName, typeName)
                .list();
        return list;
    }

    /**
     * 新增文章类型信息
     *
     * @param articleType 文章类型信息
     * @return 结果
     */
    @Override
    public int insertArticleType(ArticleType articleType) {
        articleType.setCreateTime(LocalDateTime.now());
        articleType.setUpdateTime(LocalDateTime.now());
        return articleTypeMapper.insert(articleType);
    }

    /**
     * 修改文章类型信息
     *
     * @param articleType 文章类型信息
     * @return 结果
     */
    @Override
    public int updateArticleType(ArticleType articleType) {
        return articleTypeMapper.updateById(articleType);
    }

    /**
     * 批量删除文章类型信息
     *
     * @param ids 需要删除的文章类型信息主键
     * @return 结果
     */
    @Override
    public int deleteArticleTypeByIds(Long[] ids) {
        return articleTypeMapper.deleteArticleTypeByIds(ids);
    }

    /**
     * 删除文章类型信息信息
     *
     * @param id 文章类型信息主键
     * @return 结果
     */
    @Override
    public int deleteArticleTypeById(Long id) {
        return articleTypeMapper.deleteArticleTypeById(id);
    }
}