package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.ProductDto;
import cn.edu.xmu.oomall.shop.controller.vo.FreightPriceVo;
import cn.edu.xmu.oomall.shop.controller.vo.ProductItemVo;
import cn.edu.xmu.oomall.shop.controller.vo.TemplateVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "shop-service")
public interface ShopDao {

    @GetMapping("/shops/{shopId}/templates")
    InternalReturnObject<TemplateVo> retrieveTemplateByName(
            @PathVariable("shopId")Long shopId
    );
    @PostMapping("/internal/templates/{id}/regions/{rid}/freightprice")
     InternalReturnObject<FreightPriceVo> getFreight(
            @PathVariable("id")Long tid,
            @PathVariable("rid")Long rid,
            @Validated @RequestBody List<ProductItemVo> voList
    );
}

