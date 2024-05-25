package com.xsg.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsg.system.domain.dto.ReviewRecordDto;
import com.xsg.system.domain.vo.ReviewRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xsg.common.log.annotation.Log;
import com.xsg.common.log.enums.BusinessType;
import com.xsg.common.security.annotation.RequiresPermissions;
import com.xsg.system.domain.ReviewRecord;
import com.xsg.system.service.IReviewRecordService;
import com.xsg.common.core.web.controller.BaseController;
import com.xsg.common.core.web.domain.AjaxResult;
import com.xsg.common.core.utils.poi.ExcelUtil;
import com.xsg.common.core.web.page.TableDataInfo;

/**
 * 审核记录信息Controller
 * 
 * @author Chenpi
 * @date 2024-03-24
 */
@RestController
@RequestMapping("/articleRecord")
public class ReviewRecordController extends BaseController
{
    @Autowired
    private IReviewRecordService reviewRecordService;

    /**
     * 查询所有审核记录信息列表
     */
    @RequiresPermissions("gaojian:articleRecord:list")
    @GetMapping("/list")
    public TableDataInfo list(ReviewRecordDto reviewRecordDto)
    {
        startPage();
        List<ReviewRecordVo> list = reviewRecordService.selectReviewRecordList(reviewRecordDto);
        return getDataTable(list);
    }

    /**
     * 导出审核记录信息列表
     */
    @RequiresPermissions("gaojian:articleRecord:export")
    @Log(title = "审核记录信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ReviewRecordDto reviewRecordDto)
    {
        List<ReviewRecordVo> list = reviewRecordService.selectReviewRecordList(reviewRecordDto);

        ExcelUtil<ReviewRecordVo> util = new ExcelUtil<ReviewRecordVo>(ReviewRecordVo.class);
        util.exportExcel(response, list, "审核记录信息数据");
    }

    /**
     * 获取某一个文章 审核记录信息详细信息
     */
    @RequiresPermissions("gaojian:articleRecord:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(reviewRecordService.selectReviewRecordById(id));
    }

    /**
     * 新增审核记录信息
     */
    @RequiresPermissions("gaojian:articleRecord:add")
    @Log(title = "审核记录信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReviewRecord reviewRecord)
    {
        return toAjax(reviewRecordService.insertReviewRecord(reviewRecord));
    }

    /**
     * 修改审核记录信息
     */
    @RequiresPermissions("gaojian:articleRecord:edit")
    @Log(title = "审核记录信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReviewRecord reviewRecord)
    {
        return toAjax(reviewRecordService.updateReviewRecord(reviewRecord));
    }

    /**
     * 删除审核记录信息
     */
    @RequiresPermissions("gaojian:articleRecord:remove")
    @Log(title = "审核记录信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(reviewRecordService.deleteReviewRecordByIds(ids));
    }
}
