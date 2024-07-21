package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.service.ClientService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: monitor
 * @description: 监控信息接口
 * @author: 王贝强
 * @create: 2024-07-18 15:28
 */

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Resource
    ClientService clientService;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient(){
        return RestBean.success(clientService.listClients());
    }
    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO client){
        clientService.renameClient(client);
        return RestBean.success();
    }
    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO vo){
        clientService.renameNode(vo);
        return RestBean.success();
    }
    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId){
        return RestBean.success(clientService.clientDetails(clientId));
    }
}
