package com.appcnd.find.api.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nihao
 * @create 2018/12/1
 **/
@Component
public class AsynUtil {

    private ExecutorService executorService = null;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
