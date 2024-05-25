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

/**
 * 终稿文章信息对象 final_article
 *
 * @author Chenpi
 * @date 2024-03-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("final_article")
public class FinalArticle implements Serializable {

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
     * 终稿文件名称
     */
    @Excel(name = "终稿文件名称")
    private String fileName;

    /**
     * 终稿文件路径
     */
    @Excel(name = "终稿文件路径")
    private String filePath;

    /**
     * 终稿上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "终稿上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadTime;

}