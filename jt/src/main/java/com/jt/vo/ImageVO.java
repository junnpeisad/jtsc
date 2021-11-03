package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO implements Serializable {

    private String virtualPath; //虚拟地址,不包含磁盘地址
    private String urlPath;     //URL地址信息.
    private String fileName;    //文件名称
}
