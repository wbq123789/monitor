package com.example.utils;

import com.example.entity.dto.RuntimeData;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: monitor
 * @description: influxDB工具类
 * @author: 王贝强
 * @create: 2024-07-16 16:54
 */
@Component
public class influxDBUtils {
    @Value("${spring.influx.url}")
    String url;
    @Value("${spring.influx.user}")
    String user;
    @Value("${spring.influx.password}")
    String password;
    String BUCKET = "monitor";
    String ORG= "flying";

    private InfluxDBClient client;

    @PostConstruct
    public void init(){
        client = InfluxDBClientFactory.create(url,user,password.toCharArray());
    }

    public void writeRuntimeData(int clientId, RuntimeDetailVO vo){
        RuntimeData data=new RuntimeData();
        BeanUtils.copyProperties(vo,data);
        data.setClientId(clientId);
        data.setTimestamp(new Date(vo.getTimestamp()).toInstant());
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writeMeasurement(BUCKET,ORG, WritePrecision.NS,data);
    }
}
