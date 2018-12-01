package com.appcnd.find.api.util;

import com.appcnd.find.api.exception.FindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nihao on 17/4/22.
 */
public class BaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(BaseUtil.class);

    public static List split(List list,Integer pageSize){
        int listSize = list.size();
        int page = (listSize + (pageSize-1))/ pageSize;
        List<List> result = new ArrayList<>();
        for(int i=0;i<page;i++){
            List subList = new ArrayList();
            for(int j=0;j<listSize;j++){
                int pageIndex = ( (j + 1) + (pageSize-1) ) / pageSize;
                if(pageIndex == (i + 1)) {
                    subList.add(list.get(j));
                }

                if( (j + 1) == ((j + 1) * pageSize) ) {
                    break;
                }
            }
            result.add(subList);
        }
        return result;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("Cdn-Src-Ip");
        if (!Strings.isEmpty(ip)) {
            if ("unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }

            if ("unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if ("unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if ("unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }

            if ("unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }

            if ("unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }   
        }

        return ip;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    private static BASE64Encoder encoder = new BASE64Encoder();
    public static String getBase64(InputStream inputStream) throws FindException {
        try {
            return encoder.encode(toByteArray(inputStream));
        } catch (Exception e) {
            logger.error("转base64错误", e);
            throw new FindException("获取BASE64异常");
        }finally {
            closeInputStream(inputStream);
        }
    }
    public static String getBase64(byte[] bytes) {
        return encoder.encode(bytes);
    }

    public static void copyInputStreamToFile(InputStream source, File file) throws FindException {
        int index;
        byte[] bytes = new byte[1024];
        try (FileOutputStream downloadFile = new FileOutputStream(file)) {
            while ((index = source.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
        } catch (IOException e) {
            logger.error("转File错误", e);
            throw new FindException("保存FILE异常");
        } finally {
            closeInputStream(source);
        }
    }

    public static void closeInputStream(InputStream inputStream) {
        if(inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("close InputStream error", e);
            }
        }
    }
}
