package com.xsg.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsg.system.domain.Article;
import com.xsg.system.domain.dto.ArticleContentDto;
import com.xsg.system.domain.vo.ArticleContentVo;
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
import com.xsg.system.domain.ArticleContent;
import com.xsg.system.service.IArticleContentService;
import com.xsg.common.core.web.controller.BaseController;
import com.xsg.common.core.web.domain.AjaxResult;
import com.xsg.common.core.utils.poi.ExcelUtil;
import com.xsg.common.core.web.page.TableDataInfo;

/**
 * 稿件内容信息Controller
 * 
 * @author Chenpi
 * @date 2024-03-24
 */
@RestController
@RequestMapping("/articleContent")
public class ArticleContentController extends BaseController
{
    @Autowired
    private IArticleContentService articleContentService;

    /**
     * 查询稿件内容信息列表
     */
    @RequiresPermissions("gaojian:articleContent:list")
    @GetMapping("/list")
    public TableDataInfo list(ArticleContentDto articleContent)
    {
        startPage();
        List<ArticleContentVo> list = articleContentService.selectArticleContentList(articleContent);
        return getDataTable(list);
    }

    /**
     * 导出稿件内容信息列表
     */
    @RequiresPermissions("gaojian:articleContent:export")
    @Log(title = "稿件内容信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ArticleContentDto articleContent)
    {
        List<ArticleContentVo> list = articleContentService.selectArticleContentList(articleContent);
        ExcelUtil<ArticleContentVo> util = new ExcelUtil<ArticleContentVo>(ArticleContentVo.class);
        util.exportExcel(response, list, "稿件内容信息数据");
    }

    /**
     * 获取稿件内容信息详细信息
     */
    @RequiresPermissions("gaojian:articleContent:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        // Todo 根据文章ID 获取文章内容
        LambdaQueryWrapper<ArticleContent> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleContent::getArticleId,id);
        // articleContentService.getOne(qw);

        // articleContentService.lambdaQuery().eq(ArticleContent::getArticleId,id).one();
        return success(articleContentService.getOne(qw));

    }

    /**
     * 新增稿件内容信息
     */
    @RequiresPermissions("gaojian:articleContent:add")
    @Log(title = "稿件内容信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ArticleContent articleContent)
    {
        return toAjax(articleContentService.insertArticleContent(articleContent));
    }

    /**
     * 修改稿件内容信息
     */
    @RequiresPermissions("gaojian:articleContent:edit")
    @Log(title = "稿件内容信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ArticleContent articleContent)
    {
        return toAjax(articleContentService.updateArticleContent(articleContent));
    }

    /**
     * 删除稿件内容信息
     */
    @RequiresPermissions("gaojian:articleContent:remove")
    @Log(title = "稿件内容信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(articleContentService.deleteArticleContentByIds(ids));
    }
}
