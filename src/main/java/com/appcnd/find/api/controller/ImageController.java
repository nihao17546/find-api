package com.appcnd.find.api.controller;

import com.appcnd.find.api.pojo.result.SearchResult;
import com.appcnd.find.api.pojo.vo.FavoImageVO;
import com.appcnd.find.api.pojo.vo.ImageVO;
import com.appcnd.find.api.pojo.vo.ListVO;
import com.appcnd.find.api.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nihao
 * @create 2018/11/24
 **/
@RestController
@RequestMapping("/image")
public class ImageController extends BaseController {

    @Autowired
    private IImageService imageService;

    @RequestMapping("/random")
    public String random(@RequestParam(required = false, defaultValue = "15") Integer limit) {
        List<ImageVO> list = imageService.random(limit);
        return ok().pull("response", list).json();
    }

    @RequestMapping("/{keyword}/{page}/{rows}")
    public String queryImage(@PathVariable String keyword, @PathVariable Integer page, @PathVariable Integer rows,
                             @RequestParam(required = false, defaultValue = "image_date") String sort,
                             @RequestParam(required = false, defaultValue = "desc") String asc) {
        SearchResult searchResult = imageService.query(keyword, page, rows, sort, asc);
        return ok().pull("response", searchResult.getData())
                .pull("curPage", searchResult.getCurPage())
                .pull("pageCount", searchResult.getPageCount())
                .pull("recordCount", searchResult.getRecordCount()).json();
    }

    @RequestMapping("/fave")
    public String fave(@Value("#{request.getAttribute('uid')}") Long uid,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        ListVO<FavoImageVO> listVO = imageService.getFavo(uid, curPage, pageSize);
        return ok().pull("response", listVO).json();
    }

}
