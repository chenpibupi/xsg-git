package com.xsg.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsg.common.core.annotation.Excel;
import com.xsg.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章类型信息对象 article_type
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_type")
public class ArticleType implements Serializable {

private static final long serialVersionUID=1L;

    /** 主键 */
        @TableId(type = IdType.AUTO)
    private Long id;

    /** 类型名称 */
            @Excel(name = "类型名称")
    private String typeName;

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