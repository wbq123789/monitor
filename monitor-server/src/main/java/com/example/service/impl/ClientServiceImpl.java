package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Client;
import com.example.mapper.ClientMapper;
import com.example.service.ClientService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: monitor
 * @description: 客户端服务接口实现类
 * @author: 王贝强
 * @create: 2024-07-13 16:24
 */
@Slf4j
@Service
public class ClientServiceImpl  extends ServiceImpl<ClientMapper, Client> implements ClientService {

    private String registerToken=this.createNewToken();

    private final Map<Integer,Client> clientIdCache = new ConcurrentHashMap<>();
    private final Map<String,Client> clientTokenCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void initClientCache(){
        clientIdCache.clear();
        clientTokenCache.clear();
        this.list().forEach(this::addClientCache);
    }

    @Override
    public boolean registerClient(String token) {
        if(this.registerToken.equals(token)){
            int id=this.randomClientId();
            Client client=new Client(id,"未命名主机",token,new Date());
            if (this.save(client)){
                this.registerToken=this.createNewToken();
                this.addClientCache(client);
                log.info("主机注册成功，Id：{}",id);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getToken() {
        return registerToken;
    }

    @Override
    public Client findClientById(int id) {
        return clientIdCache.get(id);
    }

    @Override
    public Client findClientByToken(String token) {
        return clientTokenCache.get(token);
    }

    private void addClientCache(Client client){
        clientIdCache.put(client.getId(),client);
        clientTokenCache.put(client.getToken(), client);
    }
    private int randomClientId(){
        return new Random().nextInt(90000000)+10000000;
    }
    private String createNewToken(){
        String CHARACTERS ="abcdefghijhlmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random=new SecureRandom();
        StringBuilder builder=new StringBuilder(24);
        for (int i = 0; i < 24; i++) {
            builder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        System.out.println(builder);
        return builder.toString();
    }
}
