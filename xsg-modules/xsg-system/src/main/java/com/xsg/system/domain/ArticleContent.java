package com.xsg.system.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xsg.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 稿件内容信息对象 article_content
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_content")
public class ArticleContent implements Serializable {

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
     * 文章内容
     */
    @Excel(name = "文章内容")
    private String content;

    /**
     * 文章描述
     */
    @Excel(name = "文章描述")
    private String description;

}