package com.community.admin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 *
 * @param <T> 数据类型
 * @author Community Team
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 创建分页结果
     *
     * @param total 总记录数
     * @param list  数据列表
     * @param <T>   数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(Long total, List<T> list) {
        return new PageResult<>(total, list);
    }

    /**
     * 创建空分页结果
     *
     * @param <T> 数据类型
     * @return 空分页结果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L, List.of());
    }
}
