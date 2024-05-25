package com.xsg.system.domain.vo;

import com.xsg.system.domain.FinalArticle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalArticleVo extends FinalArticle {
    /*
     * 文章标题
     * */
    private String articleTitle;
}
