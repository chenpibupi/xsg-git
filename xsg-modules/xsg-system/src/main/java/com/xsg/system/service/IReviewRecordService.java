package com.xsg.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsg.system.domain.ReviewRecord;
import com.xsg.system.domain.dto.ReviewRecordDto;
import com.xsg.system.domain.vo.ReviewRecordVo;

/**
 * 审核记录信息Service接口
 *
 * @author Chenpi
 * @date 2024-03-24
 */
public interface IReviewRecordService extends IService<ReviewRecord>
{
    /**
     * 查询审核记录信息
     *
     * @param id 审核记录信息主键
     * @return 审核记录信息
     */
    public List<ReviewRecord> selectReviewRecordById(Long id);

    /**
     * 查询审核记录信息列表
     *
     * @param reviewRecordDto 审核记录信息
     * @return 审核记录信息集合
     */
    public List<ReviewRecordVo> selectReviewRecordList(ReviewRecordDto reviewRecordDto);

    /**
     * 新增审核记录信息
     *
     * @param reviewRecord 审核记录信息
     * @return 结果
     */
    public int insertReviewRecord(ReviewRecord reviewRecord);

    /**
     * 修改审核记录信息
     *
     * @param reviewRecord 审核记录信息
     * @return 结果
     */
    public int updateReviewRecord(ReviewRecord reviewRecord);

    /**
     * 批量删除审核记录信息
     *
     * @param ids 需要删除的审核记录信息主键集合
     * @return 结果
     */
    public int deleteReviewRecordByIds(Long[] ids);

    /**
     * 删除审核记录信息信息
     *
     * @param id 审核记录信息主键
     * @return 结果
     */
    public int deleteReviewRecordById(Long id);
}