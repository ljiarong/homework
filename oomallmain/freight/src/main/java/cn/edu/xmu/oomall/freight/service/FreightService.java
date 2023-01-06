package cn.edu.xmu.oomall.freight.service;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.javaee.core.utils.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.vo.BeginAndEndTimeVo;
import cn.edu.xmu.oomall.freight.controller.vo.LogisticsVo;
import cn.edu.xmu.oomall.freight.controller.vo.ShopLogisticsVo;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseVo;
import cn.edu.xmu.oomall.freight.dao.*;
import cn.edu.xmu.oomall.freight.dao.bo.*;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseLogisticsPo;
import cn.edu.xmu.oomall.freight.service.dto.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FreightService {
    @Autowired
    private WarehouseDao warehouseDao;
    @Autowired
    private WarehouseRegionDao warehouseRegionDao;
    @Autowired
    private RegionDao regionDao;
    @Autowired
    private WarehouseLogisticsDao warehouseLogisticsDao;
    @Autowired
    private ShopLogisticsDao shopLogisticsDao;
    @Autowired
    private LogisticsDao logisticsDao;
    @Autowired
    private ExpressDao expressDao;
    @Autowired
    private UndeliverableDao undeliverableDao;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 商户或管理员查询某个地区可以配送的所有仓库
     *
     * @param id 地区Id
     * @return 仓库对象
     */
    public PageDto<FullWarehouseDto> retrieveWarehouseByRegionId(Long shopId, Long id, Integer page, Integer pageSize) {
        List<FullWarehouseDto> ret = null;
//        1.查出region下的仓库id
        List<WarehouseRegion> warehouseRegionList = warehouseRegionDao.retrieveByRegionId(id, page, pageSize);
        if (warehouseRegionList != null && warehouseRegionList.size() > 0) {
            ret = warehouseRegionList.stream().map(bo -> {
                FullWarehouseDto fullWarehouseDto = CloneFactory.copy(new FullWarehouseDto(), bo);
                Warehouse warehouse = null;
                if (null != bo.getWarehouse()) {
                    warehouse = new Warehouse();
                    Warehouse warehouse1 = bo.getWarehouse();
                    warehouse.setId(warehouse1.getId());
                    warehouse.setName(warehouse1.getName());
                    warehouse.setInvalid(warehouse1.getInvalid());
                    warehouse.setPriority(warehouse1.getPriority());
                }
                fullWarehouseDto.setWarehouse(warehouse);

                UserDto creator = new UserDto() {
                    {
                        setId(bo.getCreatorId());
                        setName(bo.getCreatorName());
                    }
                };
                UserDto modifier = new UserDto() {
                    {
                        setId(bo.getModifierId());
                        setName(bo.getModifierName());
                    }
                };
                fullWarehouseDto.setCreator(creator);
                fullWarehouseDto.setModifier(modifier);
                return fullWarehouseDto;
            }).collect(Collectors.toList());
//            根据仓库的优先级排序
        } else {
            ret = new ArrayList<>();
        }
//        根据仓库的优先级排序
        if (ret != null && ret.size() > 0) {
          ret=  ret.stream().sorted(Comparator.comparingInt(FullWarehouseDto -> FullWarehouseDto.getWarehouse().getPriority())).collect(Collectors.toList());

        }
        return new PageDto<>(ret, page, pageSize);
    }

    /**
     * 商户新增仓库配送地区
     */
    public ReturnObject addWarehouseRegionById(Long shopId, Long wid, Long id, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
//1.判断仓库是否存在
        if (wid != null) {
            Warehouse byId = warehouseDao.findById(wid);
            if (byId == null)
                throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSE_NOEXIST, String.format(ReturnNo.FREIGHT_WAREHOUSE_NOEXIST.getMessage(), wid));
        }
//        2.判断地区是否存在
        if (id != null) {
            Region byId = regionDao.findById(id);
            if (byId == null)
                throw new BusinessException(ReturnNo.FREIGHT_REGION_NOEXIST, String.format(ReturnNo.FREIGHT_REGION_NOEXIST.getMessage(), id));
        }
        //       3 判断该仓库下是否已拥有该配送地区
        WarehouseRegion warehouseRegion = warehouseRegionDao.findByIdAndRegionId(id, wid);
        if (null != warehouseRegion) {
            throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST, String.format(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST.getMessage(), id));
        }
//        封装一个bo存入数据库
        if (null != beginAndEndTimeVo) {
            warehouseRegionDao.save(shopId, wid, id, beginAndEndTimeVo, user);
        }
        return new ReturnObject(ReturnNo.OK);

    }

    /**
     * 商户修改仓库配送地区 (如果仓库没有此地区设置，则不做任何操作)
     */
    public ReturnObject updateWarehouseRegionById(Long shopId, Long wid, Long id, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
        //       1 判断该仓库下是否已拥有该配送地区
        WarehouseRegion warehouseRegion = warehouseRegionDao.findByIdAndRegionId(id, wid);
        if (null == warehouseRegion) {
            return new ReturnObject(ReturnNo.OK);
        } else {
            warehouseRegion.setBeginTime(beginAndEndTimeVo.getBeginTime());
            warehouseRegion.setEndTime(beginAndEndTimeVo.getEndTime());
//            2.更新并删除redis记录
            String key = warehouseRegionDao.update(warehouseRegion, user);
            if (null != key)
                redisUtil.del(key);
            return new ReturnObject(ReturnNo.OK);
        }

    }

    /**
     * 商户或管理员取消仓库对某个地区的配送 (直接物理删除)
     */

    public ReturnObject deleteWarehouseRegionById(Long shopId, Long wid, Long id) {
        if (null != wid && null != id) {
            warehouseRegionDao.deleByWarehouseIdAndRegionId(wid, id);
        }
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户或管理员查询某个仓库的配送地区
     */
    public PageDto<WarehouseRegionDto> retrieveRegionByWarehouseId(Long shopId, Long id, Integer page, Integer pageSize) {
        List<WarehouseRegionDto> ret = null;
        //        1.查出warehouse下关联的地区id
        List<WarehouseRegion> list = warehouseRegionDao.retrieveByWarehouseId(id, page, pageSize);

//2.封装dto
        if (null != list && list.size() > 0) {
            ret = list.stream().map(warehouseRegion -> {
                WarehouseRegionDto warehouseRegionDto = CloneFactory.copy(new WarehouseRegionDto(), warehouseRegion);
                Region region = warehouseRegion.getRegion();
                if (null != region) {
                    Region region1 = new Region();
                    region1.setId(region.getId());
                    region1.setName(region.getName());
                    warehouseRegionDto.setRegion(region1);
                }

                UserDto creator = new UserDto() {
                    {
                        setId(warehouseRegion.getCreatorId());
                        setName(warehouseRegion.getCreatorName());
                    }
                };
                UserDto modifier = new UserDto() {
                    {
                        setId(warehouseRegion.getModifierId());
                        setName(warehouseRegion.getModifierName());
                    }
                };
                warehouseRegionDto.setCreator(creator);
                warehouseRegionDto.setModifier(modifier);
                return warehouseRegionDto;
            }).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }
        return new PageDto<>(ret, page, pageSize);
    }

    /**
     * 商户新建仓库 只有商户才可以建立
     */
    public CreateWarehouseDto createWarehouse(Long shopId, WarehouseVo warehouseVo, UserDto user) {
        Warehouse ret = CloneFactory.copy(new Warehouse(), warehouseVo);
//        1.在仓库表保存该仓库
        Warehouse warehouse = warehouseDao.saveWarehouse(shopId, ret, user);
        //        4.封装dto
        CreateWarehouseDto createWarehouseDto = null;
        if (null != warehouse) {
            Region region = warehouse.getRegion();

            createWarehouseDto = CloneFactory.copy(new CreateWarehouseDto(), warehouse);
            if (null != region) {
                Region region1 = new Region();
                region1.setId(region.getId());
                region1.setName(region.getName());
                createWarehouseDto.setRegion(region1);
            }

            UserDto creator = new UserDto() {
                {
                    setId(warehouse.getCreatorId());
                    setName(warehouse.getCreatorName());
                }
            };
            UserDto modifier = new UserDto() {
                {
                    setId(warehouse.getModifierId());
                    setName(warehouse.getModifierName());
                }
            };
            createWarehouseDto.setCreatedBy(creator);
            createWarehouseDto.setModifiedBy(modifier);


        }

        return createWarehouseDto;
    }

    /**
     * 获得仓库
     */
    public PageDto<CreateWarehouseDto> retrieveWarehouse(Long shopId, Integer page, Integer pageSize) {
        PageInfo<Warehouse> warehouses = warehouseDao.retrieveWarehouseByShopId(shopId, page, pageSize);
        List<Warehouse> list = warehouses.getList();
        List<CreateWarehouseDto> ret = null;
        if (null != list && list.size() > 0) {
            //        封装dto
            ret = list.stream().map(bo -> {

                CreateWarehouseDto createWarehouseDto = CloneFactory.copy(new CreateWarehouseDto(), bo);
                Region region = bo.getRegion();

                if (null != region) {
                    Region region1 = new Region();
                    region1.setId(region.getId());
                    region1.setName(region.getName());
                    createWarehouseDto.setRegion(region1);
                }

                UserDto creator = new UserDto() {
                    {
                        setId(bo.getCreatorId());
                        setName(bo.getCreatorName());
                    }
                };
                UserDto modifier = new UserDto() {
                    {
                        setId(bo.getModifierId());
                        setName(bo.getModifierName());
                    }
                };
                createWarehouseDto.setCreatedBy(creator);
                createWarehouseDto.setModifiedBy(modifier);

                return createWarehouseDto;
            }).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }
        return new PageDto<>(ret, page, pageSize);
    }

    /**
     * 商户修改仓库信息。
     */
    public ReturnObject updateWarehouse(Long shopId, Long id, WarehouseVo warehouseVo, UserDto user) {
        Warehouse warehouse = CloneFactory.copy(new Warehouse(), warehouseVo);

        warehouseDao.saveById(warehouse, shopId, id, user);
        redisUtil.del(String.format(WarehouseDao.KEY, id));
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户删除仓库 需删除仓库与物流，仓库与地区的关系
     */
    public ReturnObject delWarehouse(Long shopId, Long id, UserDto user) {
//        1.删除仓库和地区表的记录
        warehouseRegionDao.deleByWarehouseId(id);
//     2.删除仓库和物流表的记录
        warehouseLogisticsDao.deleByWarehouseId(id);

//        3.在仓库表中删除记录
        warehouseDao.delById(id);
        return new ReturnObject(ReturnNo.OK);

    }

    /**
     * 商铺暂停某个仓库发货
     */
    public ReturnObject suspendWarehouse(Long shopId, Long id, UserDto user) {
//将仓库表的invalid字段值设为1
        warehouseDao.suspendWarehouse(shopId, id, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商铺恢复某个仓库发货
     */
    public ReturnObject resumeWarehouse(Long shopId, Long id, UserDto user) {
        warehouseDao.resumeWarehouse(shopId, id, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户新建仓库物流。 重复 出998出错误
     */
    public ReturnObject addWarehousepLogisticsById(Long shopId, Long id, Long lid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
//        1 判断该仓库下是否已拥有该物流
        List<WarehouseLogisticsPo> list = warehouseLogisticsDao.findByIdAndLogisticsId(id, lid);
        if (null != list && list.size() > 0) {
            throw new BusinessException(ReturnNo.FREIGHT_LOGISTIC_EXIST, String.format(ReturnNo.FREIGHT_LOGISTIC_EXIST.getMessage(), lid));
        }
//      2.封装bo存入数据库
        if (null != beginAndEndTimeVo) {
            warehouseLogisticsDao.save(id, lid, beginAndEndTimeVo, user);
        }
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户修改仓库物流信息 504
     * 修改仓库物流时发现该仓库或者仓库物流并不存在
     */

    public ReturnObject updateWarehousepLogisticsById(Long shopId, Long id, Long lid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
//        1 判断修改仓库物流时判断该仓库物流是否存在
        List<WarehouseLogisticsPo> list = warehouseLogisticsDao.findByIdAndLogisticsId(id, lid);
        if (null == list) {
            throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSEORLOGISTIC_EXIST, ReturnNo.FREIGHT_WAREHOUSEORLOGISTIC_EXIST.getMessage());
        }
//        2.判断该仓库是否存在
        Warehouse warehouse = warehouseDao.findById(id);
        if (null == warehouse) {
            throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSEORLOGISTIC_EXIST, ReturnNo.FREIGHT_WAREHOUSEORLOGISTIC_EXIST.getMessage());
        }
        //      3.封装bo存入数据库
        if (null != beginAndEndTimeVo) {
            warehouseLogisticsDao.update(id, lid, beginAndEndTimeVo, user);
        }
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户删除仓库物流配送关系
     */
    public ReturnObject delWarehousepLogisticsById(Long shopId, Long id, Long lid) {
//       有记录才可以删除 删除需要主键
        List<WarehouseLogisticsPo> byIdAndLogisticsId = warehouseLogisticsDao.findByIdAndLogisticsId(id, lid);
        if (null != byIdAndLogisticsId) {
            warehouseLogisticsDao.deleByWarehouseIdAndLogisticsId(id, lid, byIdAndLogisticsId);
        }
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 获得仓库物流 按照优先级从小往大排序
     */
    public PageDto<ShopLogisticsDto> retrieveShoplogistics(Long shopId, Long id, Integer page, Integer pageSize) {
        List<ShopLogisticsDto> ret = null;
//1.现在仓库物流表中查出仓库对应的物流
        List<WarehouseLogistics> warehouseLogistics = warehouseLogisticsDao.retrieveByWarehouseId(id, page, pageSize);
        if (null != warehouseLogistics && warehouseLogistics.size() > 0) {
            ret = warehouseLogistics.stream().map(bo -> {
                ShopLogisticsDto shopLogisticsDto = new ShopLogisticsDto();

                shopLogisticsDto.setBeginTime(bo.getBeginTime());
                shopLogisticsDto.setEndTime(bo.getEndTime());
                shopLogisticsDto.setInvalid(bo.getInvalid());
                shopLogisticsDto.setGmtCreate(bo.getGmtCreate());
                shopLogisticsDto.setGmtModified(bo.getGmtModified());
                UserDto creator = new UserDto() {
                    {
                        setId(bo.getCreatorId());
                        setName(bo.getCreatorName());
                    }
                };
                UserDto modifier = new UserDto() {
                    {
                        setId(bo.getModifierId());
                        setName(bo.getModifierName());
                    }
                };
                shopLogisticsDto.setCreator(creator);
                shopLogisticsDto.setModifier(modifier);

                if (bo.getShopLogistics() != null) {
                    SimpleShopLogisticsDto shopLogistics = CloneFactory.copy(new SimpleShopLogisticsDto(), bo.getShopLogistics());
                    UserDto creator1 = new UserDto() {
                        {
                            setId(bo.getShopLogistics().getCreatorId());
                            setName(bo.getShopLogistics().getCreatorName());
                        }
                    };
                    UserDto modifier1 = new UserDto() {
                        {
                            setId(bo.getShopLogistics().getModifierId());
                            setName(bo.getShopLogistics().getModifierName());
                        }
                    };
                    shopLogistics.setCreator(creator1);
                    shopLogistics.setModifier(modifier1);

                    if (bo.getShopLogistics().getLogisticsId() != null) {
                        Logistics logistics = logisticsDao.findById(bo.getShopLogistics().getLogisticsId());
                        Logistics logistics1 = new Logistics();
                        logistics1.setId(logistics.getId());
                        logistics1.setName(logistics.getName());
                        shopLogistics.setLogistics(logistics1);
                    }
                    shopLogisticsDto.setShopLogistics(shopLogistics);
                }
                return shopLogisticsDto;
            }).collect(Collectors.toList());


        } else {
            ret = new ArrayList<>();
        }
        //          排序
        if (null!=ret&&ret.size()>0){
           ret= ret.stream().sorted(Comparator.comparingInt(ShopLogisticsDto -> ShopLogisticsDto.getShopLogistics().getPriority())).collect(Collectors.toList());
        }
        return new PageDto<ShopLogisticsDto>(ret, page, pageSize);


    }

    /**
     * 店家新增物流合作
     * 999
     * 商铺已存在物流(id=%d)
     * 物流合作已经存在，无法重复新增
     */
    public SimpleShopLogisticsDto addShoplogistics(Long shopId, ShopLogisticsVo shopLogisticsVo, UserDto user) {
//1.判断物流合作是否已经存在
        ShopLogistics shopLogistics = shopLogisticsDao.findByLogisticsAndShopId(shopId, shopLogisticsVo.getLogisticsId());
        if (null != shopLogistics) {
            throw new BusinessException(ReturnNo.FREIGHT_SHOPLOGISTIC_EXIST, String.format(ReturnNo.FREIGHT_SHOPLOGISTIC_EXIST.getMessage(), shopLogisticsVo.getLogisticsId()));
        }
//2添加

        ShopLogistics bo = shopLogisticsDao.save(shopLogisticsVo, shopId, user);
//        3.封装dto
        SimpleShopLogisticsDto shopLogisticsDto = null;
        if (null != bo) {
            shopLogisticsDto = CloneFactory.copy(new SimpleShopLogisticsDto(), bo);
            if (null != bo.getLogistics()) {
                Logistics logistics = new Logistics();
                logistics.setId(shopLogisticsVo.getLogisticsId());
                logistics.setName(bo.getLogistics().getName());
                shopLogisticsDto.setLogistics(logistics);
            }
        }
        UserDto creator = new UserDto() {
            {
                setId(bo.getCreatorId());
                setName(bo.getCreatorName());
            }
        };
        UserDto modifier = new UserDto() {
            {
                setId(bo.getModifierId());
                setName(bo.getModifierName());
            }
        };
        shopLogisticsDto.setCreator(creator);
        shopLogisticsDto.setModifier(modifier);
        return shopLogisticsDto;
    }

    /**
     * 店家获得物流合作信息 按照优先级从小到大返回
     */

    public PageDto<SimpleShopLogisticsDto> getShopLoistics(Long shopId, Integer page, Integer pageSize) {
// 查出shopId的物流
        List<ShopLogistics> shopLogistics = shopLogisticsDao.retrieveByShopId(shopId,page,pageSize);
        List<SimpleShopLogisticsDto> ret = null;
//     封装dto
        if (null != shopLogistics && shopLogistics.size() > 0) {
            ret = shopLogistics.stream().map(bo -> {
                SimpleShopLogisticsDto shopLogisticsDto = CloneFactory.copy(new SimpleShopLogisticsDto(), bo);
                Logistics logistics = bo.getLogistics();
                if (null != logistics) {
                    Logistics logistics1 = new Logistics();
                    logistics1.setId(logistics.getId());
                    logistics1.setName(logistics.getName());
                    shopLogisticsDto.setLogistics(logistics1);
                }
                UserDto creator = new UserDto() {
                    {
                        setId(bo.getCreatorId());
                        setName(bo.getCreatorName());
                    }
                };
                UserDto modifier = new UserDto() {
                    {
                        setId(bo.getModifierId());
                        setName(bo.getModifierName());
                    }
                };
                shopLogisticsDto.setCreator(creator);
                shopLogisticsDto.setModifier(modifier);


                return shopLogisticsDto;
            }).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }

        return new PageDto<SimpleShopLogisticsDto>(ret, page, pageSize);

    }

    /**
     * 店家更新物流合作信息
     */
    public ReturnObject updateShopLogistics(Long shopId, Long id, LogisticsVo logisticsVo, UserDto user) {
        ShopLogistics shopLogistics = shopLogisticsDao.findByShopLogisticsAndShopId(shopId, id);
        if (logisticsVo.getSecret() != null) {
            shopLogistics.setSecret(logisticsVo.getSecret());
        }
        if (logisticsVo.getPriority() != null) {
            shopLogistics.setPriority(logisticsVo.getPriority());
        }
        String key = shopLogisticsDao.update(shopId, shopLogistics, user);
//        在redis中删除
        redisUtil.del(key);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商铺停用某个物流合作
     */
    public ReturnObject suspendShopLogistics(Long shopId, Long id, UserDto user) {
        shopLogisticsDao.suspendShopLogistics(shopId, id, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商铺恢复某个物流合作
     */
    public ReturnObject resumeShopLogistics(Long shopId, Long id, UserDto user) {
        shopLogisticsDao.resumeShopLogistics(shopId, id, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 根据物流单号查询属于哪家物流公司 如果不给条件则返回平台所有支持的物流公司
     */
    public ReturnObject getLogistics(String billCode) {
//        如果不给条件则返回平台所有支持的物流公司
//        1.给了billcode
        if (!billCode.equalsIgnoreCase("")) {
//            1.1查询
            Express express = expressDao.findByBillCode(billCode);
            SimpleLogisticsDto simpleLogisticsDto = null;
            if (null != express) {
                // 1.2封装dto
                Long id = express.getShopLogistics().getLogisticsId();
                Logistics logistics = logisticsDao.findById(id);

                if (null != logistics) {
                    simpleLogisticsDto = new SimpleLogisticsDto();
                    simpleLogisticsDto.setId(logistics.getId());
                    simpleLogisticsDto.setName(logistics.getName());
                }
            }
            return new ReturnObject(ReturnNo.OK, simpleLogisticsDto);

        } else {
//            2.1查询平台支持的物流公司
            List<Logistics> logistics = logisticsDao.retrieve();
            List<SimpleLogisticsDto> ret = null;
//          2.2封装
            if (null != logistics && logistics.size() > 0) {
                ret = logistics.stream().map(bo -> {
                    SimpleLogisticsDto simpleLogisticsDto = new SimpleLogisticsDto();
                    simpleLogisticsDto.setId(bo.getId());
                    simpleLogisticsDto.setName(bo.getName());
                    return simpleLogisticsDto;
                }).collect(Collectors.toList());
            } else {
                ret = new ArrayList<>();
            }
            return new ReturnObject(ReturnNo.OK, ret);
        }
    }

    /**
     * 商户指定快递公司无法配送某个地区
     */
    public ReturnObject addUndeliverable(Long shopId, Long id, Long rid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
        undeliverableDao.addUndeliverable(shopId, id, rid, beginAndEndTimeVo, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户更新不可达信息
     */

    public ReturnObject updateUndeliverable(Long shopId, Long id, Long rid, BeginAndEndTimeVo beginAndEndTimeVo, UserDto user) {
        undeliverableDao.updateUndeliverable(shopId, id, rid, beginAndEndTimeVo, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户删除某个不可达信息
     */
    public ReturnObject delUndeliverable(Long shopId, Long id, Long rid, UserDto user) {
        undeliverableDao.delUndeliverable(shopId, id, rid, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户查询快递公司无法配送的地区
     */
    public PageDto<UndeliverableDto> getUndeliverable(Long shopId, Long id, Integer page, Integer pageSize) {
//        1.查询
        List<Undeliverable> undeliverables = undeliverableDao.retrieveByShopLogisticsId(id, page, pageSize);
//      封装
        List<UndeliverableDto> ret = null;
        if (null != undeliverables && undeliverables.size() > 0) {
            ret = undeliverables.stream().map(bo -> {
                UndeliverableDto undeliverableDto = new UndeliverableDto();
                undeliverableDto.setId(bo.getId());
                if (bo.getRegion() != null) {
                    Region region = new Region();
                    region.setId(bo.getRegionId());
                    region.setName(bo.getRegion().getName());
                    undeliverableDto.setRegion(region);
                }
                undeliverableDto.setBeginTime(bo.getBeginTime());
                undeliverableDto.setEndTime(bo.getEndTime());
                UserDto creator = new UserDto() {
                    {
                        setId(bo.getCreatorId());
                        setName(bo.getCreatorName());
                    }
                };
                UserDto modifier = new UserDto() {
                    {
                        setId(bo.getModifierId());
                        setName(bo.getModifierName());
                    }
                };
                undeliverableDto.setCreator(creator);
                undeliverableDto.setModifier(modifier);
                undeliverableDto.setGmtCreate(bo.getGmtCreate());
                undeliverableDto.setGmtModified(bo.getGmtModified());
                return undeliverableDto;
            }).collect(Collectors.toList());
        } else {
            ret = new ArrayList<>();
        }
        return new PageDto<UndeliverableDto>(ret, page, pageSize);
    }

}
