package com.xsg.system.domain;

import com.xsg.common.core.annotation.Excel;
import com.xsg.common.core.web.domain.BaseEntity;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 轮播图数据库管理，用于存储轮播图信息对象 carousel_images
 *
 * @author Chenpi
 * @date 2024-04-16
 */
@Data
@TableName("carousel_images")
public class CarouselImages extends BaseEntity {

private static final long serialVersionUID=1L;

    /** 图片唯一标识符 */
        @TableId(type = IdType.AUTO)
    private Long id;

    /** 图片名称 */
            @Excel(name = "图片名称")
    private String imageName;

    /** 图片路径 */
            @Excel(name = "图片路径")
    private String imagePath;

    /** 更新者 */
            @Excel(name = "更新者")
    private String updater;

}