package com.xsg.system.domain.vo;

import com.xsg.system.domain.ReviewRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRecordVo extends ReviewRecord {
    /**
     * 文章编号
     */
    private String articleNumber;
}
