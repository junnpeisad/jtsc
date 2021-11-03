package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 关于SpringMVC接收JS数组的说明:
     * 在JS中定义的数组只能在JS中进行业务操作.数据传递到
     * SpringMVC中时,由于类型问题,需要把js的数组转化为通用的
     * 字符串
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public SysResult upload(MultipartFile file) throws IOException {

        ImageVO imageVO = fileService.upload(file);
        if(imageVO == null){//说明业务执行有误
            return SysResult.fail();
        }
        return SysResult.success(imageVO);
    }

    /**
     * 业务: 删除图片
     * URL:  http://localhost:8091/file/deleteFile?virtualPath=xxxxx
     * 参数:  virtualPath 虚拟路径
     * 返回值: SysResult对象
     */
    @DeleteMapping("/deleteFile")
    public SysResult deleteFile(String virtualPath){

        fileService.deleteFile(virtualPath);
        return SysResult.success();
    }




    /**
     * 业务: 实现文件上传
     * url: /file/upload
     * 请求类型: POST
     * 参数: file
     * 返回值: SysResult(imageVO)
     * 高级API:MultipartFile 自动维护了缓存流/自动开关
     *
     * 文件上传步骤:
     *      1.获取文件名称.
     *      2.准备上传文件的目录
     *      3.封装文件全路径  目录/文件名称
     *      4.实现文件上传
     */
    /*@PostMapping("/upload")
    public SysResult upload(MultipartFile file) throws IOException {
        //1.获取文件名称  a.jpg
        String fileName = file.getOriginalFilename();
        //2.准备文件目录
        String fileDir = "G:/images/";
        //2.1 判断目录是否存在
        File dir = new File(fileDir);
        if(!dir.exists()){
            //如果目录不存在,则创建多级目录
            dir.mkdirs();
        }
        //3.准备文件全路径
        String localPath = fileDir + fileName;
        //4.实现文件输出
        file.transferTo(new File(localPath));
        System.out.println("文件上传成功!!!!");
        return SysResult.success();
    }*/


}
