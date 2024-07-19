package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientPreviewVO;

import java.util.List;

/**
 * @program: monitor
 * @description: 客户端服务接口
 * @author: 王贝强
 * @create: 2024-07-13 16:22
 */
public interface ClientService extends IService<Client>{
    String getToken();
    boolean registerClient(String token);
    Client findClientById(int id);
    Client findClientByToken(String token);
    void updateClientDetail(ClientDetailVO vo,Client client);
    void updateRuntimeDetail(RuntimeDetailVO vo, Client client);
    List<ClientPreviewVO> listClients();
    void renameClient(RenameClientVO vo);
}
