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
public class WarehouseRegionControllerTest {
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
    public void retrieveWarehouseByRegionId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/regions/{id}/warehouses", 1L, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.name", is("朝阳新城第二仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].endTime", is("2023-04-02T14:20:05")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addWarehouseRegionById() throws Exception {
        String requestJson = "{\"beginTime\": \"2022-12-02T14:20:05\"," +
                "\"endTime\": \"2023-04-02T14:20:05\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{wid}/regions/{id}", 1l, 25l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));


    }

    @Test
    public void updateWarehouseRegionById() throws Exception {
        String requestJson = "{\"beginTime\": \"2022-12-02T14:20:05\"," +
                "\"endTime\": \"2027-04-02T14:20:05\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/warehouses/{wid}/regions/{id}", 1l, 1l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));


    }

    @Test
    public void deleteWarehouseRegionById() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/warehouses/{wid}/regions/{id}", 1l, 1l, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void retrieveRegionByWarehouseId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/warehouses/{id}/regions", 1L, 1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.name", is("北京市")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].endTime", is("2023-04-02T14:20:05")))
                .andDo(MockMvcResultHandlers.print());
    }
}
