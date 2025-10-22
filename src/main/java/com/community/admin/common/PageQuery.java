package com.community.admin.common;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页查询基类
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码(从1开始)
     */
    @Min(value = 1, message = "页码最小为1")
    private Integer page = 1;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 100, message = "每页数量最大为100")
    private Integer size = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式(asc/desc)
     */
    private String sortOrder = "desc";
}
