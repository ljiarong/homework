package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.controller.vo.LogisticsVo;
import cn.edu.xmu.oomall.freight.controller.vo.ShopLogisticsVo;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseVo;
import cn.edu.xmu.oomall.freight.service.FreightService;
import cn.edu.xmu.oomall.freight.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 管理人员的接口
 */
@RestController
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class AdminFreightController {
    @Autowired
    private FreightService freightService;

    /**
     * 商户或管理员查询某个地区可以配送的所有仓库
     */
    @GetMapping("/regions/{id}/warehouses")
    public ReturnObject retrieveWarehouseByRegionId(@PathVariable(value = "shopId", required = true) Long shopId,
                                                    @PathVariable(value = "id", required = true) Long id,
                                                    @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        PageDto<FullWarehouseDto> warehouseDtoPageDto = freightService.retrieveWarehouseByRegionId(shopId, id, page, pageSize);

        return new ReturnObject(ReturnNo.OK, warehouseDtoPageDto);
    }

    /**
     * 商户新增仓库配送地区
     */
    @Audit(departName = "shops")
    @PostMapping("/warehouses/{wid}/regions/{id}")
    public ReturnObject addWarehouseRegionById(@PathVariable(value = "shopId", required = true) Long shopId,
                                               @PathVariable(value = "wid", required = true) Long wid,
                                               @PathVariable(value = "id", required = true) Long id,
                                               @Validated @RequestBody BeginAndEndTimeVo beginAndEndTimeVo,
                                               @LoginUser UserDto user) {
        return freightService.addWarehouseRegionById(shopId, wid, id, beginAndEndTimeVo, user);
    }

    /**
     * 商户修改仓库配送地区
     */
    @Audit(departName = "shops")
    @PutMapping("/warehouses/{wid}/regions/{id}")
    public ReturnObject updateWarehouseRegionById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                  @PathVariable(value = "wid", required = true) Long wid,
                                                  @PathVariable(value = "id", required = true) Long id,
                                                  @Validated @RequestBody BeginAndEndTimeVo beginAndEndTimeVo,
                                                  @LoginUser UserDto user) {
        return freightService.updateWarehouseRegionById(shopId, wid, id, beginAndEndTimeVo, user);
    }

    /**
     * 商户或管理员取消仓库对某个地区的配送
     */

    @DeleteMapping("/warehouses/{wid}/regions/{id}")
    public ReturnObject deleteWarehouseRegionById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                  @PathVariable(value = "wid", required = true) Long wid,
                                                  @PathVariable(value = "id", required = true) Long id
    ) {
        return freightService.deleteWarehouseRegionById(shopId, wid, id);
    }

    /**
     * 商户或管理员查询某个仓库的配送地区
     */
    @GetMapping("/warehouses/{id}/regions")
    public ReturnObject retrieveRegionByWarehouseId(@PathVariable(value = "shopId", required = true) Long shopId,
                                                    @PathVariable(value = "id", required = true) Long id,
                                                    @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageDto<WarehouseRegionDto> regionDtoPageDto = freightService.retrieveRegionByWarehouseId(shopId, id, page, pageSize);
        return new ReturnObject(ReturnNo.OK,regionDtoPageDto);
    }

    /**
     * 商户新建仓库 只有商户才可以建立
     */
    @Audit(departName = "shops")
    @PostMapping("/warehouses")
    public ReturnObject addWarehouse(@PathVariable(value = "shopId", required = true) Long shopId,
                                    @Validated @RequestBody WarehouseVo warehouseVo,
                                     @LoginUser UserDto user) {
        if (PLATFORM == shopId) {
            return new ReturnObject(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "shopId"));
        }
        CreateWarehouseDto createWarehouseDto = freightService.createWarehouse(shopId, warehouseVo, user);
        return new ReturnObject(ReturnNo.OK, createWarehouseDto);
    }

    /**
     * 获得仓库
     */
    @GetMapping("/warehouses")
    public ReturnObject retrieveWarehouse(@PathVariable(value = "shopId", required = true) Long shopId,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageDto<CreateWarehouseDto> createWarehouseDtoPageDto = freightService.retrieveWarehouse(shopId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, createWarehouseDtoPageDto);
    }

    /**
     * 商户修改仓库信息。
     */

    @PutMapping("/warehouses/{id}")
    public ReturnObject updateWarehouse(@PathVariable(value = "shopId", required = true) Long shopId,
                                        @PathVariable(value = "id", required = true) Long id,
                                       @Validated @RequestBody WarehouseVo warehouseVo,
                                        @LoginUser UserDto user) {

        return freightService.updateWarehouse(shopId, id, warehouseVo, user);
    }

    /**
     * 商户删除仓库
     */
    @DeleteMapping("/warehouses/{id}")
    public ReturnObject delWarehouse(@PathVariable(value = "shopId", required = true) Long shopId,
                                     @PathVariable(value = "id", required = true) Long id,
                                     @LoginUser UserDto user) {
        return freightService.delWarehouse(shopId, id, user);
    }

    /**
     * 商铺暂停某个仓库发货
     */
    @PutMapping("/warehouse/{id}/suspend")
    public ReturnObject suspendWarehouse(@PathVariable(value = "shopId", required = true) Long shopId,
                                         @PathVariable(value = "id", required = true) Long id,
                                         @LoginUser UserDto user) {

        return freightService.suspendWarehouse(shopId, id, user);
    }

    /**
     * 商铺恢复某个仓库发货
     */
    @PutMapping("/warehouse/{id}/resume")
    public ReturnObject resumeWarehouse(@PathVariable(value = "shopId", required = true) Long shopId,
                                        @PathVariable(value = "id", required = true) Long id,
                                        @LoginUser UserDto user) {
        return freightService.resumeWarehouse(shopId, id, user);
    }

    /**
     * 商户新建仓库物流。
     */
    @PostMapping("/warehouses/{id}/shoplogistics/{lid}")
    public ReturnObject addWarehousepLogisticsById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                   @PathVariable(value = "id", required = true) Long id,
                                                   @PathVariable(value = "lid", required = true) Long lid,
                                                  @Validated @RequestBody BeginAndEndTimeVo beginAndEndTimeVo,
                                                   @LoginUser UserDto user) {
        return freightService.addWarehousepLogisticsById(shopId, id, lid, beginAndEndTimeVo, user);
    }

    /**
     * 商户修改仓库物流信息 504
     * 修改仓库物流时发现该仓库或者仓库物流并不存在
     */

    @PutMapping("/warehouses/{id}/shoplogistics/{lid}")
    public ReturnObject updateWarehousepLogisticsById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                      @PathVariable(value = "id", required = true) Long id,
                                                      @PathVariable(value = "lid", required = true) Long lid,
                                                      @RequestBody BeginAndEndTimeVo beginAndEndTimeVo,
                                                      @LoginUser UserDto user) {
        return freightService.updateWarehousepLogisticsById(shopId, id, lid, beginAndEndTimeVo, user);
    }

    /**
     * 商户删除仓库物流配送关系
     */
    @DeleteMapping("/warehouses/{id}/shoplogistics/{lid}")
    public ReturnObject delWarehousepLogisticsById(@PathVariable(value = "shopId", required = true) Long shopId,
                                                   @PathVariable(value = "id", required = true) Long id,
                                                   @PathVariable(value = "lid", required = true) Long lid,
                                                   @LoginUser UserDto user) {
        return freightService.delWarehousepLogisticsById(shopId, id, lid);
    }

    /**
     * 获得仓库物流 按照优先级从小往大排序
     */
    @GetMapping("/warehouses/{id}/shoplogistics")
    public ReturnObject getShopLogistics(@PathVariable(value = "shopId", required = true) Long shopId,
                                         @PathVariable(value = "id", required = true) Long id,
                                         @RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        PageDto<ShopLogisticsDto>shopLogisticsDtoPageInfo = freightService.retrieveShoplogistics(shopId, id, page, pageSize);
        return new ReturnObject(ReturnNo.OK, shopLogisticsDtoPageInfo);
    }

    /**
     * 店家新增物流合作
     */
    @PostMapping("/shoplogistics")
    public ReturnObject addShoplogistics(@PathVariable(value = "shopId", required = true) Long shopId,
                                        @Validated @RequestBody ShopLogisticsVo shopLogisticsVo,
                                         @LoginUser UserDto user) {
        SimpleShopLogisticsDto shopLogistics = freightService.addShoplogistics(shopId, shopLogisticsVo, user);
        return new ReturnObject(ReturnNo.OK, shopLogistics);
    }

    /**
     * 店家获得物流合作信息 按照优先级从小到大返回
     */
    @GetMapping("/shoplogistics")
    public ReturnObject getShopLogistics(@PathVariable(value = "shopId", required = true) Long shopId,
                                         @RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @LoginUser UserDto user) {
        PageDto<SimpleShopLogisticsDto> shopLoistics = freightService.getShopLoistics(shopId, page, pageSize);

        return new ReturnObject(ReturnNo.OK, shopLoistics);
    }

    /**
     * 店家更新物流合作信息
     */
    @PutMapping("/shoplogistics/{id}")
    public ReturnObject updateShopLogistics(@PathVariable(value = "shopId", required = true) Long shopId,
                                            @PathVariable(value = "id", required = true) Long id,
                                           @Validated @RequestBody LogisticsVo logisticsVo,
                                            @LoginUser UserDto user) {
        return freightService.updateShopLogistics(shopId, id, logisticsVo, user);
    }

    /**
     * 商铺停用某个物流合作
     */
    @PutMapping("/shoplogistics/{id}/suspend")
    public ReturnObject suspendShopLogistics(@PathVariable(value = "shopId", required = true) Long shopId,
                                             @PathVariable(value = "id", required = true) Long id,
                                             @LoginUser UserDto user) {
        return freightService.suspendShopLogistics(shopId, id, user);
    }

    /**
     * 商铺恢复某个物流合作
     */
    @PutMapping("/shoplogistics/{id}/resume")
    public ReturnObject resumeShopLogistics(@PathVariable(value = "shopId", required = true) Long shopId,
                                            @PathVariable(value = "id", required = true) Long id,
                                            @LoginUser UserDto user) {
        return freightService.resumeShopLogistics(shopId, id, user);
    }

    /**
     * 商户指定快递公司无法配送某个地区
     */
    @PostMapping("/shoplogistics/{id}/regions/{rid}/undeliverable")
    public ReturnObject addUndeliverable(@PathVariable(value = "shopId", required = true) Long shopId,
                                         @PathVariable(value = "id", required = true) Long id,
                                         @PathVariable(value = "rid", required = true) Long rid,
                                        @Validated @RequestBody BeginAndEndTimeVo beginAndEndTimeVo,
                                         @LoginUser UserDto user) {
        return freightService.addUndeliverable(shopId, id, rid, beginAndEndTimeVo, user);

    }



    /**
     * 商户更新不可达信息
     */

    @PutMapping("/shoplogistics/{id}/regions/{rid}/undeliverable")
    public ReturnObject updateUndeliverable(@PathVariable(value = "shopId", required = true) Long shopId,
                                            @PathVariable(value = "id", required = true) Long id,
                                            @PathVariable(value = "rid", required = true) Long rid,
                                          @Validated  @RequestBody BeginAndEndTimeVo beginAndEndTimeVo,
                                            @LoginUser UserDto user) {
        return freightService.updateUndeliverable(shopId, id, rid, beginAndEndTimeVo, user);
    }

    /**
     * 商户删除某个不可达信息
     */
    @DeleteMapping("/shoplogistics/{id}/regions/{rid}/undeliverable")
    public ReturnObject delUndeliverable(@PathVariable(value = "shopId", required = true) Long shopId,
                                         @PathVariable(value = "id", required = true) Long id,
                                         @PathVariable(value = "rid", required = true) Long rid,
                                         @LoginUser UserDto user) {
        return freightService.delUndeliverable(shopId, id, rid, user);
    }

    /**
     * 商户查询快递公司无法配送的地区
     */
    @GetMapping("/shoplogistics/{id}/undeliverableregions")
    public ReturnObject getUndeliverable(@PathVariable(value = "shopId", required = true) Long shopId,
                                         @PathVariable(value = "id", required = true) Long id,
                                         @RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        PageDto<UndeliverableDto> undeliverableDtoPageInfoDto = freightService.getUndeliverable(shopId, id, page, pageSize);
        return new ReturnObject(ReturnNo.OK, undeliverableDtoPageInfoDto);
    }
}
