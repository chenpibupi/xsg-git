package com.xsg.system.domain.dto;

import com.xsg.system.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto extends Article {
    /*
    * 描述信息*/
    private String description;

}
