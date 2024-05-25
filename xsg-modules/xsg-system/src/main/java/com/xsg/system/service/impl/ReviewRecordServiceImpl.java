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
import com.xsg.system.domain.ReviewRecord;
import com.xsg.system.domain.dto.ReviewRecordDto;
import com.xsg.system.domain.vo.ReviewRecordVo;
import com.xsg.system.mapper.ArticleMapper;
import com.xsg.system.mapper.ReviewRecordMapper;
import com.xsg.system.service.IReviewRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审核记录信息Service业务层处理
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Slf4j
@Service
public class ReviewRecordServiceImpl extends ServiceImpl<ReviewRecordMapper, ReviewRecord> implements IReviewRecordService {
    @Autowired
    private ReviewRecordMapper reviewRecordMapper;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 查询当前文章审核记录信息
     *
     * @param id 文章主键
     * @return 审核记录信息
     */
    @Override
    public List<ReviewRecord> selectReviewRecordById(Long id) {
        List<ReviewRecord> list = this.lambdaQuery()
                .eq(ReviewRecord::getArticleId, id)
                .orderByDesc(ReviewRecord::getUpdateTime)
                .list();
        // return reviewRecordMapper.selectReviewRecordById(id);
        return list;
    }

    /**
     * 查询所有审核记录信息列表
     *
     * @param reviewRecordDto 审核记录信息
     * @return 审核记录信息
     */
    @Override
    public List<ReviewRecordVo> selectReviewRecordList(ReviewRecordDto reviewRecordDto) {

        String createName = reviewRecordDto.getCreateName();
        String articleNumber = reviewRecordDto.getArticleNumber();

        //
        LambdaQueryWrapper<Article> queryWrapper = Wrappers.lambdaQuery();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (com.xsg.common.core.utils.StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getSysUser();
            // 如果是超级管理员，则不过滤数据
            String dataScope = null;
            // 代表是非超级管理员，则进行数据过滤
            if (com.xsg.common.core.utils.StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                dataScope = currentUser.getRoles().get(0).getDataScope();
                System.out.println("dataScope ========= " + dataScope);

                Long userId = currentUser.getUserId();
                List<SysRole> roles = currentUser.getRoles();
                SysRole sysRole = roles.get(0);
                String roleKey = sysRole.getRoleKey();

                /**
                 * 根据角色键值查询文章
                 * @param roleKey 角色的键值，用于确定查询条件，可选值为"zuozhe"(作者)、"bianji"(编辑)、"zhuanjia"(专家)
                 * @param userId 用户的ID，根据不同的角色用于构建不同的查询条件
                 */
                if ("zuozhe".equals(roleKey)) {
                    // 如果角色是作者，则根据用户ID查询其文章
                    queryWrapper.eq(Article::getCreateUser, userId);
                } else if ("bianji".equals(roleKey)) {
                    // 如果角色是编辑，则查询状态不为0和1(即接收)的文章
                    queryWrapper.ne(Article::getStatus, 1);
                    queryWrapper.ne(Article::getStatus, 0);

                } else if ("zhuanjia".equals(roleKey)) {
                    // 如果角色是专家，则根据用户ID查询其专家所分配的文章
                    queryWrapper.eq(Article::getExpertId, userId);
                }
                if (!"zuozhe".equals(roleKey)) {
                    queryWrapper.ne(Article::getStatus, 0);
                }
            }

            System.out.println("adminadminadminadminadminadminadmin");
        }
        queryWrapper.eq(StringUtils.isNotEmpty(articleNumber), Article::getArticleNumber, articleNumber);
        // 获取每个角色下可以看见的文章
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        if (articleList.isEmpty()){
            return new ArrayList<>();
        }

        // 查询文章对应的审核记录信息
        List<Long> articleIds = articleList.stream().map(Article::getId).collect(Collectors.toList());

        LambdaQueryWrapper<ReviewRecord> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.in(ReviewRecord::getArticleId, articleIds)
                .like(StringUtils.isNotEmpty(createName), ReviewRecord::getCreateName, createName)
                .orderByDesc(ReviewRecord::getUpdateTime);

        if (!"zuozhe".equals(loginUser.getSysUser().getRoles().get(0).getRoleKey())){
            queryWrapper1.eq(!loginUser.getSysUser().isAdmin(),
                    ReviewRecord::getCreateUser, loginUser.getSysUser().getUserId());
        }

        List<ReviewRecord> list = this.list(queryWrapper1);
        // List<ReviewRecord> list = this.lambdaQuery()
        //         .in(ReviewRecord::getArticleId, articleIds)
        //         .like(StringUtils.isNotEmpty(createName), ReviewRecord::getCreateName, createName)
        //         // todo 作者查看失败
        //         .eq(!loginUser.getSysUser().isAdmin(), ReviewRecord::getCreateUser, loginUser.getSysUser().getUserId())
        //         .orderByDesc(ReviewRecord::getUpdateTime)
        //         .list();


        // Long articleId = null;
        // // 查出文章数据库中编号为 articleNumber 的文章，并获取其 id
        // if (StringUtils.isNotEmpty(articleNumber)) {
        //     LambdaQueryWrapper<Article> qw = Wrappers.lambdaQuery(Article.class);
        //     qw.eq(Article::getArticleNumber, articleNumber);
        //     Article one = Db.getOne(qw);
        //     articleId = one.getId();
        // }

        // 条件查询审核记录信息
        // List<ReviewRecord> list = this.lambdaQuery()
        //         .eq(articleId != null, ReviewRecord::getArticleId, articleId)
        //         .like(StringUtils.isNotEmpty(createName), ReviewRecord::getCreateName, createName)
        //         // todo 根据更新时间排序--降序
        //         .orderByDesc(ReviewRecord::getUpdateTime)
        //         .list();

        List<ReviewRecordVo> voList = list.stream().map(item -> {
            ReviewRecordVo reviewRecordVo = new ReviewRecordVo();
            BeanUtils.copyProperties(item, reviewRecordVo, "articleNumber");
            Article byId = Db.getById(item.getArticleId(), Article.class);
            reviewRecordVo.setArticleNumber(byId.getArticleNumber());
            return reviewRecordVo;
        }).collect(Collectors.toList());

        return voList;
    }

    /**
     * 新增审核记录信息
     *
     * @param reviewRecord 审核记录信息
     * @return 结果
     */
    @Override
    public int insertReviewRecord(ReviewRecord reviewRecord) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userid = loginUser.getUserid();
        log.info("当前操作用户 = {}", userid);
        SysUser currentUser = loginUser.getSysUser();
        String roleKey = currentUser.getRoles().get(0).getRoleKey();

        // 获取前端提交的数据
        Long articleId = reviewRecord.getArticleId();
        Integer isApproved = reviewRecord.getIsApproved();
        String reviewContent = reviewRecord.getReviewContent();
        log.info("reviewContent = {}", reviewContent);

        Article article = Db.getById(articleId, Article.class);

        if (isApproved == 1) {
            article.setStatus(3L);
        } else if (isApproved == 0) {
            article.setStatus(6L);
        }
        article.setUpdateTime(LocalDateTime.now());
        if (!roleKey.equals("zhuanjia")){
            Db.updateById(article);
        }


        // set 值，更新数据库
        reviewRecord.setArticleId(articleId);
        reviewRecord.setIsApproved(isApproved);
        reviewRecord.setReviewContent(reviewContent);

        reviewRecord.setCreateUser(userid);
        reviewRecord.setUpdateUser(userid);
        reviewRecord.setCreateName(loginUser.getSysUser().getNickName());
        reviewRecord.setReviewerRole(roleKey);
        reviewRecord.setCreateTime(LocalDateTime.now());
        reviewRecord.setUpdateTime(LocalDateTime.now());
        log.info("reviewRecord = {}", reviewRecord);
        int insert = reviewRecordMapper.insert(reviewRecord);

        isStatusUpdated(reviewRecord);


        return insert;
    }

    /**
     * 修改审核记录信息
     *
     * @param reviewRecord 审核记录信息
     * @return 结果
     */
    @Override
    public int updateReviewRecord(ReviewRecord reviewRecord) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userid = loginUser.getUserid();
        log.info("当前操作用户 = {}", userid);

        Long articleId = reviewRecord.getArticleId();
        Article article = Db.getById(articleId, Article.class);

        reviewRecord.setUpdateUser(userid);
        reviewRecord.setUpdateTime(LocalDateTime.now());

        log.info("reviewRecord = {}", reviewRecord);
        int update = reviewRecordMapper.updateById(reviewRecord);

        article.setUpdateTime(LocalDateTime.now());
        Db.updateById(article);

        isStatusUpdated(reviewRecord);

        return update;
    }

    /**
     * 批量删除审核记录信息
     *
     * @param ids 需要删除的审核记录信息主键
     * @return 结果
     */
    @Override
    public int deleteReviewRecordByIds(Long[] ids) {
        return reviewRecordMapper.deleteReviewRecordByIds(ids);
    }

    /**
     * 删除审核记录信息信息
     *
     * @param id 审核记录信息主键
     * @return 结果
     */
    @Override
    public int deleteReviewRecordById(Long id) {
        return reviewRecordMapper.deleteReviewRecordById(id);
    }

    /**
     * 判断每个角色的最新记录是否所有的都通过审核
     * 如果所有都通过审核，则更新该文章状态为 4L '通过未发布'
     * 反之，则不更新状态
     *
     * @param reviewRecord
     */
    private void isStatusUpdated(ReviewRecord reviewRecord) {
        // 查询每个角色的最新审核记录
        List<ReviewRecord> latestRecords = reviewRecordMapper.selectLatestRecords(reviewRecord.getArticleId());

        // 判断最新审核记录的数量是否大于等于3
        if (latestRecords.size() >= 2) {

            // 遍历最新审核记录
            boolean allApproved = false;
            for (ReviewRecord record : latestRecords) {
                log.info("最新审核记录：{}", record);
                // 检查是否所有记录都通过审核

                if (record.getReviewerRole().equals("bianji")){
                    String reviewContent = record.getReviewContent();
                //     reviewContent是否包含 确认通过
                    if (record.getIsApproved() == 1){
                        if (reviewContent.contains("确认通过")){
                            allApproved = true;
                            break;
                        }
                    }

                }
                // if (record.getIsApproved() != 1) {
                //     allApproved = false;
                //     break;
                // }
            }
            // 通过审核，则输出更新状态
            if (allApproved) {
                log.info("已通过审核，更新状态...");
                // 进行更新操作
                Article article = Db.getById(reviewRecord.getArticleId(), Article.class);
                article.setStatus(4L);
                article.setUpdateTime(LocalDateTime.now());
                Db.updateById(article);
            } else {
                log.info("存在未通过审核的记录，不更新状态...");
            }
        } else {
            log.info("最新审核记录数量不足，不进行状态更新...");
        }
    }


}