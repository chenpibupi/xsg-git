package com.xsg.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsg.system.domain.CarouselImages;

/**
 * 轮播图数据库管理，用于存储轮播图信息Mapper接口
 *
 * @author Chenpi
 * @date 2024-04-16
 */
public interface CarouselImagesMapper extends BaseMapper<CarouselImages>
{
    /**
     * 查询轮播图数据库管理，用于存储轮播图信息
     *
     * @param id 轮播图数据库管理，用于存储轮播图信息主键
     * @return 轮播图数据库管理，用于存储轮播图信息
     */
    public CarouselImages selectCarouselImagesById(Long id);

    /**
     * 查询轮播图数据库管理，用于存储轮播图信息列表
     *
     * @param carouselImages 轮播图数据库管理，用于存储轮播图信息
     * @return 轮播图数据库管理，用于存储轮播图信息集合
     */
    public List<CarouselImages> selectCarouselImagesList(CarouselImages carouselImages);

    /**
     * 新增轮播图数据库管理，用于存储轮播图信息
     *
     * @param carouselImages 轮播图数据库管理，用于存储轮播图信息
     * @return 结果
     */
    public int insertCarouselImages(CarouselImages carouselImages);

    /**
     * 修改轮播图数据库管理，用于存储轮播图信息
     *
     * @param carouselImages 轮播图数据库管理，用于存储轮播图信息
     * @return 结果
     */
    public int updateCarouselImages(CarouselImages carouselImages);

    /**
     * 删除轮播图数据库管理，用于存储轮播图信息
     *
     * @param id 轮播图数据库管理，用于存储轮播图信息主键
     * @return 结果
     */
    public int deleteCarouselImagesById(Long id);

    /**
     * 批量删除轮播图数据库管理，用于存储轮播图信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCarouselImagesByIds(Long[] ids);
}