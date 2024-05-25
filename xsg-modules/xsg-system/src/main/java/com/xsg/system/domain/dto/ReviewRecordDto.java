package com.xsg.system.domain.dto;

import com.xsg.system.domain.Article;
import com.xsg.system.domain.ReviewRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRecordDto extends ReviewRecord {
    /*
    * 文章编号*/
    private String articleNumber;
}
