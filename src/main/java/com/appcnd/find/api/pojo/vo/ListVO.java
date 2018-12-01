package com.appcnd.find.api.pojo.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nihao on 18/5/10.
 */
@Data
public class ListVO<T> {
    private int curPage;// 当前页码
    private long totalCount;// 总条目数
    private int pageSize;// 每页显示条目数
    private int totalPage;// 总页数
    private List<T> list;

    public ListVO() {
    }

    public ListVO(int curPage, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.list = new ArrayList<T>();
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        if (this.curPage > 0 && this.pageSize > 0 && totalCount > 0) {
            this.totalPage = (int)(totalCount / pageSize) + ((totalCount % pageSize) > 0 ? 1 : 0);
        }
    }
}
