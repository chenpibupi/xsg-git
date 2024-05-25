package com.xsg.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xsg.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 审核记录信息对象 review_record
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("review_record")
public class ReviewRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID
     */
    @Excel(name = "文章ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    /**
     * 审核人姓名
     */
    @Excel(name = "审核人姓名")
    private String createName;

    /**
     * 审核人角色
     */
    @Excel(name = "审核人角色")
    private String reviewerRole;

    /**
     * 审核内容
     */
    @Excel(name = "审核内容")
    private String reviewContent;

    /**
     * 创建者（审稿人）
     */
    @Excel(name = "创建者（审稿人）")
    private Long createUser;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private Long updateUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否通过
     */
    @Excel(name = "是否通过")
    private Integer isApproved;


}