package com.xsg.system.domain.dto;

import com.xsg.system.domain.FinalArticle;
import com.xsg.system.domain.ReviewRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalArticleDto extends FinalArticle {
    /*
     * 文章标题
     * */
    private String articleTitle;
}
