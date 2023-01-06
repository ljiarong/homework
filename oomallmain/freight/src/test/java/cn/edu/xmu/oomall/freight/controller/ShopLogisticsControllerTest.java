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
public class ShopLogisticsControllerTest {
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
    public void addShoplogistics() throws Exception{
        String requestJson="{\"logisticsId\": \"2\"," +
                "\"secret\": \"secret2\"," +
                "\"priority\": \"5\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/shoplogistics", 11l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.logistics.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.logistics.name", is("中通快递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secret", is("secret2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.priority", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    @Test
    public void retrieveWarehouse() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/shoplogistics", 1L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].logistics.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].logistics.name", is("极兔速递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].secret", is("secret3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].invalid", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].priority", is(2)))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void updateShopLogistics() throws Exception{

        String requestJson="{\"secret\": \"secret3\"," +
                "\"priority\": \"1232\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}", 1l,1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
@Test
    public void suspendShopLogistics()throws Exception{

    this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}/suspend", 1l,1l)
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                   )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
}
    @Test
    public void resumeShopLogistics()throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}/resume", 1l,1l)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}
