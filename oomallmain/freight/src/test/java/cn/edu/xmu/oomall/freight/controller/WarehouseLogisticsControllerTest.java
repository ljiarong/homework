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
public class WarehouseLogisticsControllerTest {
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
    public void addWarehousepLogisticsById() throws Exception {
        String requestJson = "{\"beginTime\": \"2022-12-02T14:20:05\"," +
                "\"endTime\": \"2023-04-02T14:20:05\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 1l, 25l, 4l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));


    }

    @Test
    public void updateWarehousepLogisticsById() throws Exception {
        String requestJson = "{\"beginTime\": \"2022-12-02T14:20:05\"," +
                "\"endTime\": \"2023-04-02T14:20:05\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 1l, 25l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void delWarehousepLogisticsById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 1l, 25l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    @Test
    public void getShopLogistics() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/warehouses/{id}/shoplogistics", 1L, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("2022-12-02T13:57:43")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].endTime", is("2023-04-02T13:57:43")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.logistics.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.logistics.name", is("极兔速递" )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.invalid", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.secret", is("secret3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.priority", is(2)))
                .andDo(MockMvcResultHandlers.print());
    }
}
