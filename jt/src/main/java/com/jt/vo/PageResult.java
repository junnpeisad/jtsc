package com.jt.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
public class PageResult implements Serializable {
    private String query;
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Object rows;
}
