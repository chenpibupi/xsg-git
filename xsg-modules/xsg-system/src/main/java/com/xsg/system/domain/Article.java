package com.xsg.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xsg.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章信息对象 article
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * 雪花算法
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 文章编号
     */
    @Excel(name = "文章编号")
    private String articleNumber;

    /**
     * 文章标题
     */
    @Excel(name = "文章标题")
    private String articleTitle;

    /**
     * 文章类型Id
     */
    @Excel(name = "文章类型")
    private Long articleType;

    /**
     * 封面图片路径
     */
    @Excel(name = "封面图片路径")
    private String coverImageUrl;

    /**
     * 作者账号
     */
    @Excel(name = "作者账号")
    private String authorAccount;

    /**
     * 作者姓名
     */
    @Excel(name = "作者姓名")
    private String authorName;

    /**
     * 电话号码
     */
    @Excel(name = "电话号码")
    private String phoneNumber;

    /**
     * 文章状态: 1-待审, 2-审稿中, 3-通过未发布, 4-已发布, 5-未通过
     */
    @Excel(name = "文章状态:0-待接收,1-待审, 2-审稿中, 3-通过未发布, 4-已发布, 5-未通过")
    private Long status;

    /**
     * 专家ID
     */
    @Excel(name = "专家ID")
    private Long expertId;

    /**
     * 创建者
     */
    @Excel(name = "创建者")
    private Long createUser;
    /**
     * 更新者
     */
    @Excel(name = "更新者")
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

}