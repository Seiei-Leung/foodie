package top.seiei.service;

import top.seiei.pojo.Carousel;

import java.util.List;

public interface CarouselService {

    /**
     * 首页获取轮播图列表
     * @param isShow 是否显示轮播图
     * @return
     */
    public List<Carousel> getAll(Integer isShow);
}
