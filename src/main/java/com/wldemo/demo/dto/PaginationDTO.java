package com.wldemo.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private Boolean showPrevious;//控制上一页下一页首末页的按钮显示
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;
    private Integer page;//当前页
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();//所包含的页数

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        //设置当前页
        this.page = page;
        pages.add(page);
        for (int i = 1; i <= 3; ++i) {
            if (page - i > 0) {
                pages.add(0,page - i);//从头插入
            }
            if (page + i <= totalPage) {    //不越界就插入
                pages.add(page + i);
            }
        }
        //是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        //是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }
        //是否展示首页(当前页所有条目是否包含第一个和最后一个来判断)
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //是否展示末页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
