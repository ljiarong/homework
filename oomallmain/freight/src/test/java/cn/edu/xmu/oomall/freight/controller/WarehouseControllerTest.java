package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class WarehouseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    private static String adminToken;


    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void addWarehouse() throws Exception {
        String requestJson = "{\"name\": \"测试\"," +
                "\"address\": \"测试\"," +
                "\"regionId\": \"7\"," +
                "\"senderName\": \"张三\"," +
                "\"senderMobile\": \"1323232323\"," +
                "\"priority\": \"1232\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses", 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("测试")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address", is("测试")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.region.id", is(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.region.name", is("东厂社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.senderName", is("张三")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.senderMobile", is("1323232323")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.priority", is(1232)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void retrieveWarehouse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/warehouses", 1L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.id", is(1043)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.name", is("朝阳新城第二社区居民委员会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("朝阳新城第二仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].address", is("北京,朝阳,东坝,朝阳新城第二曙光路14号")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].senderName", is("阮杰")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].senderMobile", is("139542562579")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].priority", is(1000)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateWarehouse() throws Exception {

        String requestJson = "{\"name\": \"测试\"," +
                "\"address\": \"测试\"," +
                "\"regionId\": \"7\"," +
                "\"senderName\": \"张三\"," +
                "\"senderMobile\": \"1323232323\"," +
                "\"priority\": \"1232\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/warehouses/{id}", 1l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void deleteWarehouseRegionById() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/warehouses/{id}", 1l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void suspendWarehouse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/warehouse/{id}/suspend", 1l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void resumeWarehouse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/warehouse/{id}/resume", 1l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}
