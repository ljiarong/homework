package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * 内部的接口
 */
@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
public class InternalFreightController {
    @Autowired
    private ExpressService expressService;

    @PostMapping("/shops/{shopId}/packages")
    @Audit(departName = "shops")
    public ReturnObject createExpress(@PathVariable Long shopId, @Validated @RequestBody(required = true) ExpressVo expressVo, @LoginUser UserDto user){
        //  logger.debug("createExpress: shopId = {},  vo = {}",shopId, vo);

        return expressService.addExpressById(shopId,expressVo, user);
    }


    /**
     * 根据运单id获得运单信息
     */
    @GetMapping("/packages/{id}")
    public ReturnObject getExpressById(@PathVariable(value = "id", required = true) Long id,@LoginUser UserDto user){
        Express express=expressService.getExpressById(id);
        return new ReturnObject(ReturnNo.OK, express);
    }
    /**
     * 商户验收快递
     */
    @PutMapping("/shops/{shopId}/packages/{id}/confirm")
    @Audit(departName = "shops")
    public ReturnObject updateExpress(@PathVariable Long shopId,@PathVariable Long id, @Validated @RequestBody(required = true) Byte status, @LoginUser UserDto user){
        return expressService.updateExpress(shopId,id,status, user);
    }

    /**
     * 商户取消运单
     */
    @PutMapping("/shops/{shopId}/packages/{id}/cancel")
    @ResponseBody
    @Audit(departName = "shops")
    public ReturnObject updateExpress(@PathVariable Long shopId,@PathVariable Long id, @LoginUser UserDto user){
        return expressService.cancelExpress(shopId,id,user);
    }
}
