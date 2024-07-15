package org.monitorclient.utils;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.monitorclient.entity.BaseDetail;
import org.monitorclient.entity.ConnectionConfig;
import org.monitorclient.entity.Response;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @program: monitor
 * @description: 向服务端发送http请求的工具类
 * @author: 王贝强
 * @create: 2024-07-14 18:36
 */
@Slf4j
@Component
public class NetUtils {

    @Lazy
    @Resource
    ConnectionConfig config;

    private final HttpClient client=HttpClient.newHttpClient();

    public boolean registerToServer(String address,String token){
        log.info("正在向服务端注册，请稍等。。。");
        Response response = this.doGet("/register", address, token);
        if (response.success()){
            log.info("客户端注册已完成！");
        }else {
            log.error("客户端注册失败：{}",response.message());
        }
        return response.success();
    }
    public void updateBaseDetails(BaseDetail detail){
        Response response = this.doPost("/detail", detail);
        if (response.success()){
            log.info("系统基本信息更新完成！");
        }else {
            log.error("系统基本信息更新失败：{}",response.message());
        }
    }
    private Response doGet(String url){
        return this.doGet(url,config.getAddress(),config.getToken());
    }
    private Response doGet(String url,String address,String token){
        try{
            HttpRequest request=HttpRequest.newBuilder().GET()
                    .uri(new URI(address+"/monitor" +url))
                    .header("Authorization",token)
                    .build();
            HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
            return JSONObject.parseObject(response.body()).to(Response.class);
        }catch (Exception e){
            log.error("向服务端发起GET请求出现问题",e);
            return Response.errorResponse(e);
        }
    }
    private Response doPost(String url,Object data){
        try{
            String rawData=JSONObject.from(data).toJSONString();
           HttpRequest request=HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(rawData))
                   .uri(new URI(config.getAddress()+"/monitor"+url))
                   .header("Authorization",config.getToken())
                   .header("Content-Type","application/json")
                   .build();
           HttpResponse<String> response =client.send(request,HttpResponse.BodyHandlers.ofString());
           return JSONObject.parseObject(response.body()).to(Response.class);
        }catch (Exception e){
            log.error("向服务端发起POST请求出现问题",e);
            return Response.errorResponse(e);
        }
    }
}
