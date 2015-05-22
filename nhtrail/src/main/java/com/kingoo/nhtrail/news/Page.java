package com.kingoo.nhtrail.news;

/**
 * Created by Administrator on 2015/5/22.
 */
public class Page {
    private int page = 1; // 记录页面数
    private int contentMutiPages; //
    public boolean contentFirstPage = true; // 内容第一页

    // 设置开始页面
    public void setPageStart() {
        page = 2;
    }

    // 设置页
    public void setPage(int num) {
        page = num;
    }

    // 获取当前页
    public String getCurrentPage() {
        return page + "";
    }

    // 添加页面
    public void addPage() {
        page++;
    }
}
