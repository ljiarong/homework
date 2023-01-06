//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 分页对象
 *
 * @author Ming Qiu
 **/
@Data
@Getter
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageInfoDto<T> {
    public static final int DEFAULTTOTAL = 0;
    public static final int DEFAULTPAGES = 1;
    /**
     * 对象列表
     */
    private List<T> list;
    /**
     * 第几页
     */
    private int page;
    /**
     * 每页数目
     */
    private int pageSize;
    private int total;
    private int pages;



    public PageInfoDto(List<T> list, int page, int pageSize) {
        this.list = list;
        this.page = page;
        this.pageSize = pageSize;
    }
}
