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
import com.xsg.system.domain.FinalArticle;
import com.xsg.system.domain.dto.FinalArticleDto;
import com.xsg.system.domain.vo.FinalArticleVo;
import com.xsg.system.mapper.FinalArticleMapper;
import com.xsg.system.service.IFinalArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 终稿文章信息Service业务层处理
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Service
public class FinalArticleServiceImpl extends ServiceImpl<FinalArticleMapper, FinalArticle> implements IFinalArticleService {
    @Autowired
    private FinalArticleMapper finalArticleMapper;

    /**
     * 查询终稿文章信息
     *
     * @param id 终稿文章信息主键
     * @return 终稿文章信息
     */
    @Override
    public FinalArticle selectFinalArticleById(Long id) {
        return finalArticleMapper.selectById(id);
    }

    /**
     * 查询所有终稿文章信息列表
     *
     * @param finalArticleDto 终稿文章信息
     * @return 终稿文章信息
     */
    @Override
    public List<FinalArticleVo> selectFinalArticleList(FinalArticleDto finalArticleDto) {
        String articleTitle = finalArticleDto.getArticleTitle();

        // 根据文章标题查询文章列表
        LambdaQueryWrapper<Article> qw = Wrappers.lambdaQuery(Article.class);
        qw.like(StringUtils.isNotEmpty(articleTitle), Article::getArticleTitle, articleTitle)
                .orderByAsc(Article::getUpdateTime);

        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (com.xsg.common.core.utils.StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getSysUser();
            // 如果是超级管理员，则不过滤数据
            String dataScope = null;
            // 代表是非超级管理员，则进行数据过滤
            if (com.xsg.common.core.utils.StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
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
            // System.out.println("adminadminadminadminadminadminadmin");
        }
        // 获取文章标题为 articleTitle 的文章列表
        List<Article> list = Db.list(qw);
        /*
        * 获取文章标题信息，封装到 FinalArticleVo 中
        *
        * */
        // 查询文章列表，并关联查询对应的终稿文件信息
        List<FinalArticleVo> voList = list.stream()
                .map(item -> {
                    // 获取文章id
                    Long articleId = item.getId();
                    // 使用文章id，关联查询对应的终稿文件信息
                    FinalArticle one = this.lambdaQuery()
                            .eq(articleId != null, FinalArticle::getArticleId, articleId)
                            .one();
                    // 如果终稿文件信息存在，则将终稿文件信息拷贝到对应的 VO 中
                    if (one != null) {
                        FinalArticleVo vo = new FinalArticleVo();
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
     * 新增终稿文件
     *
     * @param finalArticle 终稿文件
     * @return 结果
     */
    @Override
    public int insertFinalArticle(FinalArticle finalArticle) {
        // 设置上传时间
        finalArticle.setUploadTime(LocalDateTime.now());

        // 查询数据库是否已经上传了终稿文件
        LambdaQueryWrapper<FinalArticle> qw = new LambdaQueryWrapper<>();
        qw.eq(FinalArticle::getArticleId, finalArticle.getArticleId());
        FinalArticle selectOne = finalArticleMapper.selectOne(qw);
        // 判断是否已经上传了终稿文件
        if (selectOne != null) {
            // 更新
            finalArticle.setId(selectOne.getId());
            return finalArticleMapper.updateById(finalArticle);
        }
        // 数据库中没有上传终稿文件，执行新增操作
        return finalArticleMapper.insert(finalArticle);
    }

    /**
     * 修改终稿文件
     *
     * @param finalArticle 终稿文件
     * @return 结果
     */
    @Override
    public int updateFinalArticle(FinalArticle finalArticle) {
        // todo 修改终稿文件
        return finalArticleMapper.updateById(finalArticle);
    }

    /**
     * 批量删除终稿文件
     *
     * @param ids 需要删除的终稿文章信息主键
     * @return 结果
     */
    @Override
    public int deleteFinalArticleByIds(Long[] ids) {
        return finalArticleMapper.deleteFinalArticleByIds(ids);
    }

    /**
     * 删除终稿文件
     *
     * @param id 终稿文章信息主键
     * @return 结果
     */
    @Override
    public int deleteFinalArticleById(Long id) {
        return finalArticleMapper.deleteFinalArticleById(id);
    }
}