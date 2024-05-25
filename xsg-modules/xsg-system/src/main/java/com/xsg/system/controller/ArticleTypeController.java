package com.xsg.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
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
import com.xsg.system.domain.ArticleType;
import com.xsg.system.service.IArticleTypeService;
import com.xsg.common.core.web.controller.BaseController;
import com.xsg.common.core.web.domain.AjaxResult;
import com.xsg.common.core.utils.poi.ExcelUtil;
import com.xsg.common.core.web.page.TableDataInfo;

/**
 * 文章类型信息Controller
 * 
 * @author Chenpi
 * @date 2024-03-24
 */
@RestController
@RequestMapping("/articleType")
public class ArticleTypeController extends BaseController
{
    @Autowired
    private IArticleTypeService articleTypeService;

    /**
     * 查询文章类型信息列表
     */
    @RequiresPermissions("gaojian:articleType:list")
    @GetMapping("/list")
    public TableDataInfo list(ArticleType articleType)
    {
        startPage();
        List<ArticleType> list = articleTypeService.selectArticleTypeList(articleType);
        return getDataTable(list);
    }

    /**
     * 导出文章类型信息列表
     */
    @RequiresPermissions("gaojian:articleType:export")
    @Log(title = "文章类型信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ArticleType articleType)
    {
        List<ArticleType> list = articleTypeService.selectArticleTypeList(articleType);
        ExcelUtil<ArticleType> util = new ExcelUtil<ArticleType>(ArticleType.class);
        util.exportExcel(response, list, "文章类型信息数据");
    }

    /**
     * 获取文章类型信息详细信息
     */
    @RequiresPermissions("gaojian:articleType:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(articleTypeService.selectArticleTypeById(id));
    }

    /**
     * 新增文章类型信息
     */
    @RequiresPermissions("gaojian:articleType:add")
    @Log(title = "文章类型信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ArticleType articleType)
    {
        return toAjax(articleTypeService.insertArticleType(articleType));
    }

    /**
     * 修改文章类型信息
     */
    @RequiresPermissions("gaojian:articleType:edit")
    @Log(title = "文章类型信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ArticleType articleType)
    {
        return toAjax(articleTypeService.updateArticleType(articleType));
    }

    /**
     * 删除文章类型信息
     */
    @RequiresPermissions("gaojian:articleType:remove")
    @Log(title = "文章类型信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(articleTypeService.deleteArticleTypeByIds(ids));
    }
}
