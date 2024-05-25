package com.xsg.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsg.common.core.annotation.Excel;
import com.xsg.system.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVo extends Article {
    /** 文章类型名称 */
    private String articleTyName;
    /** 终稿文件名称 */
    private String fileName;
    /** 终稿文件地址 */
    private String filePath;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadTime;
}
