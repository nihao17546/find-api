package com.appcnd.find.api.service;

import com.appcnd.find.api.pojo.result.SearchResult;
import com.appcnd.find.api.pojo.vo.ImageVO;
import com.appcnd.find.api.pojo.vo.ListVO;

import java.util.List;

/**
 * @author nihao
 * @create 2018/11/24
 **/
public interface IImageService {

    List<ImageVO> random(Integer limit);

    SearchResult query(String name, String key, int page, int rows, String sort, String asc);

    SearchResult query(String key,int page, int rows,String sort,String asc);

    ListVO<ImageVO> getFavo(Long uid,Integer curPage,Integer pageSize);
}
