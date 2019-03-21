package springboot.zipkin.controller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author: 程泰恒
 * @date: 2019/3/19 13:39
 */

@RestController
public class ZipkinController {

    @Autowired
    private CloseableHttpClient httpClient;

    @GetMapping("/service4")
    public String sendRequest() throws  Throwable{
        Thread.sleep(100);
//        HttpGet httpGet = new HttpGet("http://192.168.0.109:4444/dict/province");
//        CloseableHttpResponse response = httpClient.execute(httpGet);

        return "你好世界";
    }
}
