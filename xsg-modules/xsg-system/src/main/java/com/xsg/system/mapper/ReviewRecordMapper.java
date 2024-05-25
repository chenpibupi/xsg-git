package com.xsg.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsg.system.domain.ReviewRecord;

/**
 * 审核记录信息Mapper接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface ReviewRecordMapper extends BaseMapper<ReviewRecord>
{
    /**
     * 删除审核记录信息
     *
     * @param id 审核记录信息主键
     * @return 结果
     */
    public int deleteReviewRecordById(Long id);

    /**
     * 批量删除审核记录信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteReviewRecordByIds(Long[] ids);

    /**
     * 查询每个角色的最新审核记录
     * @param articleId
     * @return
     */
    List<ReviewRecord> selectLatestRecords(Long articleId);
}