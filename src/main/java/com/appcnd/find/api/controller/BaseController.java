package com.appcnd.find.api.controller;

import com.appcnd.find.api.pojo.result.JsonResult;

/**
 * @author nihao 2018/11/22
 */
public abstract class BaseController {
    protected JsonResult fail(String message){
        return JsonResult.fail(message);
    }
    protected JsonResult ok(String message){
        return JsonResult.success(message);
    }
    protected JsonResult fail(){
        return JsonResult.fail();
    }
    protected JsonResult ok(){
        return JsonResult.success();
    }
}
