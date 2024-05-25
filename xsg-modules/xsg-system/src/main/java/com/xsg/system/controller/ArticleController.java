package com.xsg.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsg.system.domain.dto.ArticleDto;
import com.xsg.system.domain.vo.ArticleVo;
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
import com.xsg.system.domain.Article;
import com.xsg.system.service.IArticleService;
import com.xsg.common.core.web.controller.BaseController;
import com.xsg.common.core.web.domain.AjaxResult;
import com.xsg.common.core.utils.poi.ExcelUtil;
import com.xsg.common.core.web.page.TableDataInfo;

/**
 * 文章信息Controller
 * 
 * @author Chenpi
 * @date 2024-03-24
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController
{
    @Autowired
    private IArticleService articleService;

    /**
     * 查询文章信息列表
     */
    @RequiresPermissions("gaojian:article:list")
    @GetMapping("/list")
    public TableDataInfo list(Article article)
    {
        startPage();
        List<ArticleVo> list = articleService.selectArticleList(article);
        return getDataTable(list);
    }

    /**
     * 导出文章信息列表
     */
    @RequiresPermissions("gaojian:article:export")
    @Log(title = "文章信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Article article)
    {
        List<ArticleVo> list = articleService.selectArticleList(article);
        ExcelUtil<ArticleVo> util = new ExcelUtil<ArticleVo>(ArticleVo.class);
        util.exportExcel(response, list, "文章信息数据");
    }

    /**
     * 获取文章信息详细信息
     */
    @RequiresPermissions("gaojian:article:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(articleService.selectArticleById(id));
    }

    /**
     * 新增文章信息
     */
    @RequiresPermissions("gaojian:article:add")
    @Log(title = "文章信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ArticleDto articleDto)
    {
        return toAjax(articleService.insertArticle(articleDto));
    }

    /**
     * 修改文章信息
     */
    @RequiresPermissions("gaojian:article:edit")
    @Log(title = "文章信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ArticleDto articleDto)
    {
        return toAjax(articleService.updateArticle(articleDto));
    }

    /**
     * 删除文章信息
     */
    @RequiresPermissions("gaojian:article:remove")
    @Log(title = "文章信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(articleService.deleteArticleByIds(ids));
    }
}
