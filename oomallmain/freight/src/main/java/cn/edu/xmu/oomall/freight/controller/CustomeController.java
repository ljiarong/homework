package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.freight.service.FreightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CustomeController {
    @Autowired
    private FreightService freightService;
/**
 * 根据物流单号查询属于哪家物流公司 如果不给条件则返回平台所有支持的物流公司
 * */
    @GetMapping("/logistics")
    public ReturnObject getLogistics(@RequestParam(required = true) String billCode) {
        return freightService.getLogistics(billCode);
    }
}
