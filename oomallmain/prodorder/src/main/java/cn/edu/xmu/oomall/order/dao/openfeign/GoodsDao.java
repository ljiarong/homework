//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OnsaleDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods-service")
public interface GoodsDao {


    @GetMapping("/products/{id}")
    InternalReturnObject<ProductDto> findProductById(@PathVariable("id") Long id) ;


    @GetMapping("/shops/{shopId}/onsales/{id}")
    InternalReturnObject<OnsaleDto> getOnsaleById(@PathVariable Long shopId, @PathVariable Long id);

}
