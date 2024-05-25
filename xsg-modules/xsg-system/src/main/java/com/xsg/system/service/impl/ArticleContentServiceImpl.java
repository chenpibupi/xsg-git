package com.xsg.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.xsg.common.core.utils.StringUtils;
import com.xsg.common.security.utils.SecurityUtils;
import com.xsg.system.api.domain.SysRole;
import com.xsg.system.api.domain.SysUser;
import com.xsg.system.api.model.LoginUser;
import com.xsg.system.domain.Article;
import com.xsg.system.domain.ArticleContent;
import com.xsg.system.domain.dto.ArticleContentDto;
import com.xsg.system.domain.vo.ArticleContentVo;
import com.xsg.system.mapper.ArticleContentMapper;
import com.xsg.system.service.IArticleContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 稿件内容信息Service业务层处理
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Service
@Slf4j
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContent> implements IArticleContentService {
    @Autowired
    private ArticleContentMapper articleContentMapper;

    /**
     * 查询稿件内容信息
     *
     * @param id 稿件内容信息主键
     * @return 稿件内容信息
     */
    @Override
    public ArticleContent selectArticleContentById(Long id) {
        return articleContentMapper.selectArticleContentById(id);
    }

    /**
     * 查询稿件内容信息列表
     *
     * @param articleContentDto 稿件内容信息
     * @return 稿件内容信息
     */
    @Override
    public List<ArticleContentVo> selectArticleContentList(ArticleContentDto articleContentDto) {
        log.info("articleContentDto:{}", articleContentDto);
        Long articleId = articleContentDto.getArticleId();
        String articleTitle = articleContentDto.getArticleTitle();

        // List<ArticleContent> list = this.lambdaQuery()
        //         .eq(articleId != null, ArticleContent::getArticleId, articleId)
        //         .like(StringUtils.isNotEmpty(articleTitle), ArticleContent::getArticleTitle, articleTitle)
        //         .orderByDesc(ArticleContent::getId)
        //         .list();

        // 根据文章id查询文章列表
        LambdaQueryWrapper<Article> qw = Wrappers.lambdaQuery(Article.class);
        qw.eq(articleId != null, Article::getId, articleId)
                .like(StringUtils.isNotEmpty(articleTitle), Article::getArticleTitle, articleTitle)
                .orderByAsc(Article::getUpdateTime);

        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (com.xsg.common.core.utils.StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getSysUser();
            // 如果是超级管理员，则不过滤数据
            String dataScope = null;
            // 代表是非超级管理员，则进行数据过滤
            if (com.xsg.common.core.utils.StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                // String permission = com.xsg.common.core.utils.StringUtils.defaultIfEmpty(controllerDataScope.permission(), SecurityContextHolder.getPermission());
                // dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                //         controllerDataScope.userAlias(), permission);
                //
                dataScope = currentUser.getRoles().get(0).getDataScope();
                System.out.println("dataScope ========= " + dataScope);


                Long userId = currentUser.getUserId();
                List<SysRole> roles = currentUser.getRoles();
                SysRole sysRole = roles.get(0);
                String roleKey = sysRole.getRoleKey();

                /**
                 * 根据角色键值查询文章条件
                 * @param roleKey 角色的键值，用于确定查询条件，可选值为"zuozhe"(作者)、"bianji"(编辑)、"zhuanjia"(专家)
                 * @param userId 用户的ID，根据不同的角色用于构建不同的查询条件
                 */
                if ("zuozhe".equals(roleKey)) {
                    // 如果角色是作者，则根据用户ID查询其文章
                    qw.eq(Article::getCreateUser, userId);
                } else if ("bianji".equals(roleKey)) {
                    // 如果角色是编辑，则查询状态不为0和1(即接收)的文章
                    qw.ne(Article::getStatus, 1);
                    qw.ne(Article::getStatus, 0);

                } else if ("zhuanjia".equals(roleKey)) {
                    // 如果角色是专家，则根据用户ID查询其专家所分配的文章
                    qw.eq(Article::getExpertId, userId);
                }
                if (!"zuozhe".equals(roleKey)) {
                    qw.ne(Article::getStatus, 0);
                }
            } else {
                qw.ne(Article::getStatus, 0);
            }
            System.out.println("adminadminadminadminadminadminadmin");
        }
        List<Article> list = Db.list(qw);

        List<ArticleContentVo> voList = list.stream().map(item -> {
                    // 获取文章id
                    Long itemId = item.getId();
                    // 使用文章id，关联查询对应的终稿文件信息
                    ArticleContent one = this.lambdaQuery()
                            .eq(itemId != null, ArticleContent::getArticleId, itemId)
                            .one();
                    // 如果终稿文件信息存在，则将终稿文件信息拷贝到对应的 VO 中
                    if (one != null) {
                        ArticleContentVo vo = new ArticleContentVo();
                        vo.setArticleTitle(item.getArticleTitle());
                        BeanUtils.copyProperties(one, vo);
                        return vo;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull) // 过滤掉空的结果
                .collect(Collectors.toList());

        return voList;
    }

    /**
     * 新增稿件内容信息
     *
     * @param articleContent 稿件内容信息
     * @return 结果
     */
    @Override
    public int insertArticleContent(ArticleContent articleContent) {
        return articleContentMapper.insertArticleContent(articleContent);
    }

    /**
     * 修改稿件内容信息
     *
     * @param articleContent 稿件内容信息
     * @return 结果
     */
    @Override
    public int updateArticleContent(ArticleContent articleContent) {
        LambdaQueryWrapper<ArticleContent> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleContent::getArticleId, articleContent.getArticleId());
        ArticleContent temp = articleContentMapper.selectOne(qw);
        String content = articleContent.getContent();
        String description = articleContent.getDescription();
        temp.setContent(content);
        temp.setDescription(description);

        return articleContentMapper.updateById(temp);
    }

    /**
     * 批量删除稿件内容信息
     *
     * @param ids 需要删除的稿件内容信息主键
     * @return 结果
     */
    @Override
    public int deleteArticleContentByIds(Long[] ids) {
        return articleContentMapper.deleteArticleContentByIds(ids);
    }

    /**
     * 删除稿件内容信息信息
     *
     * @param id 稿件内容信息主键
     * @return 结果
     */
    @Override
    public int deleteArticleContentById(Long id) {
        return articleContentMapper.deleteArticleContentById(id);
    }
}