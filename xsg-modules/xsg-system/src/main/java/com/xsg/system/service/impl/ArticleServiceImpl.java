package com.xsg.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.xsg.common.core.utils.uuid.UUID;
import com.xsg.common.security.utils.SecurityUtils;
import com.xsg.system.api.domain.SysRole;
import com.xsg.system.api.domain.SysUser;
import com.xsg.system.api.model.LoginUser;
import com.xsg.system.domain.Article;
import com.xsg.system.domain.ArticleContent;
import com.xsg.system.domain.ArticleType;
import com.xsg.system.domain.FinalArticle;
import com.xsg.system.domain.dto.ArticleDto;
import com.xsg.system.domain.vo.ArticleVo;
import com.xsg.system.mapper.ArticleMapper;
import com.xsg.system.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章信息Service业务层处理
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    /*@Autowired
    private ArticleTypeMapper articleTypeMapper;*/

    /**
     * 查询文章信息
     *
     * @param id 文章信息主键
     * @return 文章信息
     */
    @Override
    public Article selectArticleById(Long id) {
        // articleMapper.selectById(id);
        return articleMapper.selectById(id);
    }

    /**
     * 查询文章信息列表
     *
     * @param article 文章信息
     * @return 文章信息
     */
    @Override
    public List<ArticleVo> selectArticleList(Article article) {
        Long articleId = article.getId();
        String articleNumber = article.getArticleNumber();
        String articleTitle = article.getArticleTitle();
        String authorName = article.getAuthorName();
        Long articleType1 = article.getArticleType();
        Long status = article.getStatus();
        // 使用 LambdaQueryWrapper 进行查询
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(articleTitle), Article::getArticleTitle, articleTitle)
                .like(StringUtils.isNotBlank(authorName), Article::getAuthorName, authorName)
                .eq(StringUtils.isNotBlank(articleNumber), Article::getArticleNumber, articleNumber)
                // todo 添加了类型和状态的查询
                .eq(articleType1 != null, Article::getArticleType, articleType1)
                .eq(status != null, Article::getStatus, status)
                .orderByDesc(Article::getUpdateTime);

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
                    // qw.ne(Article::getStatus, 1);
                    qw.ne(Article::getStatus, 0);

                } else if ("zhuanjia".equals(roleKey)) {
                    // 如果角色是专家，则根据用户ID查询其专家所分配的文章
                    // 查询状态不为0和1的
                    qw.ne(Article::getStatus, 1)
                            .ne(Article::getStatus, 0)
                            .eq(Article::getExpertId, userId);
                }
                if (!"zuozhe".equals(roleKey)) {
                    // 不查未投递的稿件
                    qw.ne(Article::getStatus, 0);
                }
            } else {
                qw.ne(Article::getStatus, 0);
            }
            System.out.println("adminadminadminadminadminadminadmin");
        }

        List<Article> articleList = articleMapper.selectList(qw);


        List<ArticleVo> articleVoList = articleList.stream().map(item -> {
            ArticleVo articleVo = new ArticleVo();
            // 将Article对象拷贝给ArticleVO对象
            BeanUtils.copyProperties(item, articleVo);
            // 获取categoryId对应的categoryName
            Long articleTypeId = item.getArticleType();

            ArticleType articleType = Db.getById(articleTypeId, ArticleType.class);

            // 查询终稿信息
            LambdaQueryWrapper<FinalArticle> fqw = Wrappers.lambdaQuery(FinalArticle.class);
            fqw.eq(FinalArticle::getArticleId, item.getId());
            FinalArticle one = Db.getOne(fqw);

            // ArticleType articleType = articleTypeMapper.selectArticleTypeById(articleTypeId);
            if (articleType != null) {
                articleVo.setArticleTyName(articleType.getTypeName());
            }
            if (one != null) {
                articleVo.setFileName(one.getFileName());
                articleVo.setFilePath(one.getFilePath());
                articleVo.setUploadTime(one.getUploadTime());
            }
            return articleVo;
        }).collect(Collectors.toList());

        return articleVoList;
    }

    /**
     * 新增文章信息
     *
     * @param articleDto 文章信息
     * @return 结果
     */
    @Override
    public int insertArticle(ArticleDto articleDto) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userid = loginUser.getUserid();
        log.info("当前操作用户 = {}", userid);

        Article article = BeanUtil.copyProperties(articleDto, Article.class);

        article.setCreateUser(userid);
        article.setUpdateUser(userid);
        String authorName = loginUser.getSysUser().getNickName();
        article.setAuthorName(authorName);
        article.setPhoneNumber(loginUser.getSysUser().getPhonenumber());

        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        // uuid生成文章编号
        article.setArticleNumber(UUID.randomUUID().toString());

        // 插入文章记录
        int insertResult = articleMapper.insert(article);

        // 获取刚刚插入的文章的ID
        Long articleId = article.getId();

        // 插入文章内容记录
        ArticleContent articleContent = new ArticleContent();
        articleContent.setArticleId(articleId);
        articleContent.setDescription(articleDto.getDescription());
        boolean saveResult = Db.save(articleContent); // 插入文章内容记录

        // 将布尔值转换为整数值，并返回
        return insertResult > 0 && saveResult ? 1 : 0;
    }

    /**
     * 修改文章信息
     *
     * @param articleDto 文章信息
     * @return 结果
     */
    @Override
    public int updateArticle(ArticleDto articleDto) {

        Article article = BeanUtil.copyProperties(articleDto, Article.class);
        article.setUpdateTime(LocalDateTime.now());
        article.setUpdateUser(SecurityUtils.getLoginUser().getUserid());
        article.setPhoneNumber(SecurityUtils.getLoginUser().getSysUser().getPhonenumber());
        int update01 = articleMapper.updateById(article);
        // 插入文章内容记录
        ArticleContent articleContent = new ArticleContent();
        Long articleId = article.getId();
        articleContent.setArticleId(articleId);

        LambdaQueryWrapper<ArticleContent> qw = Wrappers.lambdaQuery(ArticleContent.class);
        qw.eq(ArticleContent::getArticleId, articleId);
        ArticleContent one = Db.getOne(qw);
        // todo 删除 articleContent 中的 ArticleTitle 字段
        // one.setArticleTitle(article.getArticleTitle());
        one.setDescription(articleDto.getDescription());
        boolean update02 = Db.updateById(one);// 更新文章内容记录

        // 将布尔值转换为整数值，并返回
        return update01 > 0 && update02 ? 1 : 0;
    }

    /**
     * 批量删除文章信息
     *
     * @param ids 需要删除的文章信息主键
     * @return 结果
     */
    @Override
    public int deleteArticleByIds(Long[] ids) {
        return articleMapper.deleteArticleByIds(ids);
    }

    /**
     * 删除文章信息信息
     *
     * @param id 文章信息主键
     * @return 结果
     */
    @Override
    public int deleteArticleById(Long id) {
        return articleMapper.deleteArticleById(id);
    }
}