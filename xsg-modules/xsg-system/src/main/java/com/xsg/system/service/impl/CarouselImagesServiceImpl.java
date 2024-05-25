package com.xsg.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsg.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsg.system.mapper.CarouselImagesMapper;
import com.xsg.system.domain.CarouselImages;
import com.xsg.system.service.ICarouselImagesService;

/**
 * 轮播图数据库管理，用于存储轮播图信息Service业务层处理
 *
 * @author Chenpi
 * @date 2024-04-16
 */
@Service
public class CarouselImagesServiceImpl extends ServiceImpl<CarouselImagesMapper, CarouselImages> implements ICarouselImagesService
{
    @Autowired
    private CarouselImagesMapper carouselImagesMapper;

    /**
     * 查询轮播图数据库管理，用于存储轮播图信息
     *
     * @param id 轮播图数据库管理，用于存储轮播图信息主键
     * @return 轮播图数据库管理，用于存储轮播图信息
     */
    @Override
    public CarouselImages selectCarouselImagesById(Long id)
    {
        return carouselImagesMapper.selectCarouselImagesById(id);
    }

    /**
     * 查询轮播图数据库管理，用于存储轮播图信息列表
     *
     * @param carouselImages 轮播图数据库管理，用于存储轮播图信息
     * @return 轮播图数据库管理，用于存储轮播图信息
     */
    @Override
    public List<CarouselImages> selectCarouselImagesList(CarouselImages carouselImages)
    {
        return carouselImagesMapper.selectCarouselImagesList(carouselImages);
    }

    /**
     * 新增轮播图数据库管理，用于存储轮播图信息
     *
     * @param carouselImages 轮播图数据库管理，用于存储轮播图信息
     * @return 结果
     */
    @Override
    public int insertCarouselImages(CarouselImages carouselImages)
    {
                carouselImages.setCreateTime(DateUtils.getNowDate());
            return carouselImagesMapper.insertCarouselImages(carouselImages);
    }

    /**
     * 修改轮播图数据库管理，用于存储轮播图信息
     *
     * @param carouselImages 轮播图数据库管理，用于存储轮播图信息
     * @return 结果
     */
    @Override
    public int updateCarouselImages(CarouselImages carouselImages)
    {
                carouselImages.setUpdateTime(DateUtils.getNowDate());
        return carouselImagesMapper.updateCarouselImages(carouselImages);
    }

    /**
     * 批量删除轮播图数据库管理，用于存储轮播图信息
     *
     * @param ids 需要删除的轮播图数据库管理，用于存储轮播图信息主键
     * @return 结果
     */
    @Override
    public int deleteCarouselImagesByIds(Long[] ids)
    {
        return carouselImagesMapper.deleteCarouselImagesByIds(ids);
    }

    /**
     * 删除轮播图数据库管理，用于存储轮播图信息信息
     *
     * @param id 轮播图数据库管理，用于存储轮播图信息主键
     * @return 结果
     */
    @Override
    public int deleteCarouselImagesById(Long id)
    {
        return carouselImagesMapper.deleteCarouselImagesById(id);
    }
}