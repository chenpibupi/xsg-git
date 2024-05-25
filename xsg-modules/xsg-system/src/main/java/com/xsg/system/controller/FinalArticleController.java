package com.xsg.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsg.system.domain.dto.FinalArticleDto;
import com.xsg.system.domain.vo.FinalArticleVo;
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
import com.xsg.system.domain.FinalArticle;
import com.xsg.system.service.IFinalArticleService;
import com.xsg.common.core.web.controller.BaseController;
import com.xsg.common.core.web.domain.AjaxResult;
import com.xsg.common.core.utils.poi.ExcelUtil;
import com.xsg.common.core.web.page.TableDataInfo;

/**
 * 终稿文章信息Controller
 * 
 * @author Chenpi
 * @date 2024-03-24
 */
@RestController
@RequestMapping("/finalArticle")
public class FinalArticleController extends BaseController
{
    @Autowired
    private IFinalArticleService finalArticleService;

    /**
     * 查询终稿文章信息列表
     */
    @RequiresPermissions("gaojian:finalArticle:list")
    @GetMapping("/list")
    public TableDataInfo list(FinalArticleDto finalArticleDto)
    {
        startPage();
        List<FinalArticleVo> list = finalArticleService.selectFinalArticleList(finalArticleDto);
        return getDataTable(list);
    }

    /**
     * 导出终稿文章信息列表
     */
    @RequiresPermissions("gaojian:finalArticle:export")
    @Log(title = "终稿文章信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinalArticleDto finalArticleDto)
    {
        List<FinalArticleVo> list = finalArticleService.selectFinalArticleList(finalArticleDto);
        ExcelUtil<FinalArticleVo> util = new ExcelUtil<FinalArticleVo>(FinalArticleVo.class);
        util.exportExcel(response, list, "终稿文章信息数据");
    }

    /**
     * 获取终稿文章信息详细信息
     */
    @RequiresPermissions("gaojian:finalArticle:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(finalArticleService.selectFinalArticleById(id));
    }

    /**
     * 新增终稿文章信息
     */
    @RequiresPermissions("gaojian:finalArticle:add")
    @Log(title = "终稿文章信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinalArticle finalArticle)
    {
        return toAjax(finalArticleService.insertFinalArticle(finalArticle));
    }

    /**
     * 修改终稿文章信息
     */
    @RequiresPermissions("gaojian:finalArticle:edit")
    @Log(title = "终稿文章信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinalArticle finalArticle)
    {
        return toAjax(finalArticleService.updateFinalArticle(finalArticle));
    }

    /**
     * 删除终稿文章信息
     */
    @RequiresPermissions("gaojian:finalArticle:remove")
    @Log(title = "终稿文章信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(finalArticleService.deleteFinalArticleByIds(ids));
    }
}
