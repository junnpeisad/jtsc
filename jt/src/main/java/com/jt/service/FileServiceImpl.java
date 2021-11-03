package com.jt.service;

import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/image.properties")
public class FileServiceImpl implements FileService{
    //封装路径的前缀
    @Value("${image.localDir}")
    private String localDir;    // = "";
    @Value("${image.preUrl}")
    private String preUrl;      // = "http://image.jt.com";

    /**
     * 完成校验:
     *  1.校验是否为图片
     *  2.木马.exe.jpg 判断是否满足图片固有属性 高度/宽度
     *  3.为了提高查询效率,要求分目录存储.
     *      3.1 按照后缀名分配  jpg,png,gif 效率提升不能满足要求
     *      3.2 按照日期分   yyyy/MM/dd/HH   可以
     *      3.3 商品分类     出现分布不均现象.
     *      3.4 根据名称hash 之后截串
     *          demo: hash(a)=qw|er|as|dg/a.jpg
     *          弊端: hash码可能出现分布不均的现象.
     *  4.防止文件重名  使用uuid代替名称
     * @param file
     * @return
     */
    @Override
    public ImageVO upload(MultipartFile file) {
        //1.获取图片名称    demo: abc.jpg  abc.JPG
        String fileName = file.getOriginalFilename();
        //bug说明: 由于windows系统不区分大小写,所以将字母全部转化为小写
        fileName = fileName.toLowerCase();
        //利用正则判断是否为图片.
        if(!fileName.matches("^.+\\.(jpg|png|gif)$")){
            //如果不是图片,则返回null
            return null;
        }

        //2.检查文件是否为恶意程序.
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if(width == 0 || height == 0){
                //说明文件不是图片.
                return null;
            }

            //3.根据时间实现目录的创建 时间--yyyy/MM/dd
            String dateDir = new SimpleDateFormat("/yyyy/MM/dd/")
                            .format(new Date());
            // "G:/images/2021/11/11
            String localDirPath = localDir + dateDir;
            //创建目录
            File dirFile = new File(localDirPath);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }

            //4. 使用uuid替换文件名称 唯一:系统内部唯一
            String uuid = UUID.randomUUID().toString()
                    .replace("-","");
            //截取文件的后缀  aa.bb.cc.jpg
            int index = fileName.lastIndexOf(".");
            //获取类型  .jpg
            String fileType = fileName.substring(index);
            String newFileName = uuid + fileType;

            //5.实现文件上传操作  目录/文件名称
            String realFilePath = localDirPath + newFileName;
            file.transferTo(new File(realFilePath));
            System.out.println("文件上传成功!!!");

            /*
             *  6.封装返回值
             *  封装虚拟路径 在各个系统之间可以灵活切换,只保存动态变化的目录
             *  path = 时间/uuid.type
             *  网络地址://http://image.jt.com/2021/11/11/a.jpg
             */
            String virtualPath = dateDir + newFileName;
            String url = preUrl + virtualPath;
            System.out.println("磁盘地址:"+realFilePath);
            System.out.println("网络地址:"+url);
            return new ImageVO(virtualPath,url,newFileName);

        } catch (IOException e) {
            e.printStackTrace();
            return null;    //表示程序有问题
        }
    }

    /**
     * 实现思路:
     *      1.根据虚拟地址,拼接磁盘地址
     *      2.判断文件是否存在
     *      3.实现文件删除
     * @param virtualPath
     */
    @Override
    public void deleteFile(String virtualPath) {
        //1.生成本地磁盘地址
        String path = localDir + virtualPath;
        System.out.println(path);
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }
}
