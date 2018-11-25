package com.appcnd.find.api.pojo.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nihao on 17/4/15.
 */
@Data
public class SearchResult {
    private List data;
    private long recordCount;
    private long pageCount;
    private long curPage;

    public SearchResult() {
        this.data = new ArrayList();
        this.recordCount = 0;
        this.pageCount = 0;
        this.curPage = 0;
    }
}
