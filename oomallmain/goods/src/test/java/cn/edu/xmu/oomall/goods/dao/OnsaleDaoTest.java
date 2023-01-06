package cn.edu.xmu.oomall.goods.dao;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.goods.GoodsApplication;
import cn.edu.xmu.oomall.goods.GoodsTestApplication;
import cn.edu.xmu.oomall.goods.dao.bo.Onsale;
import cn.edu.xmu.oomall.goods.dao.bo.Shop;
import cn.edu.xmu.oomall.goods.dao.openfeign.ShopDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cn.edu.xmu.oomall.goods.dao.bo.Onsale.NORMAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author keyu zhu
 * @date 2022/12/11
 */
@SpringBootTest(classes = GoodsTestApplication.class)
@Transactional
public class OnsaleDaoTest {

    @Autowired
    private OnsaleDao onsaleDao;
    @MockBean
    ShopDao shopDao;

    @MockBean
    RedisUtil redisUtil;

    @Test
    public void testInsert1(){

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Shop shop = Shop.builder().id(10L).name("测试商铺10").build();
        Mockito.when(shopDao.getShopById(1L)).thenReturn(new InternalReturnObject<>(shop));
        Onsale onsale = Onsale.builder().invalid(Byte.valueOf("1")).type(NORMAL).maxQuantity(200).quantity(2000).shopId(10L).price(10L).
                endTime(LocalDateTime.parse("2040-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME)).beginTime(LocalDateTime.parse("2030-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME))
                .productId(1550L).build();
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setUserLevel(1);
        userDto.setName("admin2");
        userDto.setDepartId(1L);
        Onsale insert = onsaleDao.insert(onsale, userDto);
        assertEquals(10L, insert.getPrice());

        assertEquals(200,insert.getMaxQuantity());
    }

    @Test
    public void testSave1(){

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Shop shop = Shop.builder().id(10L).name("测试商铺10").build();
        Mockito.when(shopDao.getShopById(1L)).thenReturn(new InternalReturnObject<>(shop));
        Onsale onsale = Onsale.builder().id(1L).invalid(Byte.valueOf("1")).type(NORMAL).maxQuantity(200).quantity(2000).shopId(10L).price(10L).
                endTime(LocalDateTime.parse("2040-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME)).beginTime(LocalDateTime.parse("2030-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME))
                .productId(1550L).build();
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setUserLevel(1);
        userDto.setName("admin2");
        userDto.setDepartId(1L);
        String key = onsaleDao.save(onsale, userDto);
        assertEquals("O1",key);

    }

    @Test
    public void testRetrieveByProductId(){

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Shop shop = Shop.builder().id(10L).name("测试商铺10").build();
        Mockito.when(shopDao.getShopById(1L)).thenReturn(new InternalReturnObject<>(shop));
        List<Onsale> onsales = onsaleDao.retrieveByProductId(1550L, 0, 10);
        assertEquals(1,onsales.size());

        assertEquals(53295L,onsales.get(0).getPrice());
    }



}
