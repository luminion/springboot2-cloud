package com.example.alibabaoss.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.example.alibabaoss.config.OSSConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * @author booty
 */
@Slf4j
@Component
public class OSSUtil {

    @Resource
    private OSSConfig constantProperties;
    private final String fileDir = "cloudFile/";

    /**
     * 文件上传
     * @param file 文件
     * @return 访问链接
     */
    public String uploadFile(MultipartFile file) {
        //验证并上传
        String fileUrl = uploadImg2Oss(file);
        //获取访问链接
        String str = getFileUrl(fileUrl);
        return str.trim();
    }

    /**
     * 验证文件大小并重命名
     * @param file 文件
     * @return
     */
    private String uploadImg2Oss(MultipartFile file) {
        //1、限制最大文件为20M
        if (file.getSize() > 1024 * 1024 * 20) {
            return "图片太大";
        }
        //2、重命名文件
//        String originalFilename = file.getOriginalFilename();
//        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
//        Random random = new Random();
//        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;

        //文件名
        String fileName = file.getOriginalFilename();
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        //使用uuid拼接新文件名
        String uuid = UUID.randomUUID().toString();
        String name = uuid + suffix;

        try {
            //读取文件
            InputStream inputStream = file.getInputStream();
            //上传文件
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            return "上传失败";
        }
    }


    /**
     * 将文件上传到阿里云oss
     * @param inputStream 上传文件的输入流
     * @param fileName 新文件名
     * @return 访问链接
     */
    private String uploadFile2OSS(InputStream inputStream, String fileName) {
        String ret = "";
        try {

            //通过文件后缀获取上传请求类型
            String contentType = getContentType(fileName.substring(fileName.lastIndexOf(".")));

            //创建上传Object的Metadata元数据
            ObjectMetadata objectMetadata = new ObjectMetadata();
            //设置文件内容长度
            objectMetadata.setContentLength(inputStream.available());
            //是否缓存
            objectMetadata.setCacheControl("no-cache");
            //设置请求头
            objectMetadata.setHeader("Pragma", "no-cache");
            //设置请求类型
            objectMetadata.setContentType(contentType);
            //设置数据类型和文件名
            objectMetadata.setContentDisposition("inline;filename=" + fileName);

            // 创建OSS上传客户端（服务器域名，账户，密钥）
            OSSClient ossClient = new OSSClient(constantProperties.getEndpoint(), constantProperties.getAccessKeyId(), constantProperties.getAccessKeySecret());

            // 创建上传结果对象（ 储存的bucket ， 获取文件的key ，输入流 ，OSSClient客户端信息）
            PutObjectResult putResult = ossClient.putObject(constantProperties.getBucketName(), fileDir + fileName, inputStream, objectMetadata);
            // 获取结果
            ret = putResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    /**
     * 获取请求类型
     * @param FilenameExtension 文件扩展名
     * @return 请求类型
     */
    private static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        //PDF
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        return "image/jpeg";
    }




    /**
     * 通过文件名到得到获取阿里云oss文件的key
     * @param fileUrl 文件的本地储存路径
     * @return
     */
    public String getFileUrl(String fileUrl) {
        if (fileUrl != null && fileUrl.length() > 0 && !fileUrl.equals("图片太大") && !fileUrl.equals("上传失败")) {
            String[] split = fileUrl.split("/");
            String url = this.getUrl(this.fileDir + split[split.length - 1]);
            return url;
        }
        return fileUrl;
    }


    /**
     * 生成阿里云oss上的文件访问链接
     * @param key 获取文件的key
     * @return 访问链接
     */
    private String getUrl(String key) {
        // 设置URL过期时间为20年  3600l* 1000*24*365*20
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 20);
        // 生成URL
        OSSClient ossClient = new OSSClient(constantProperties.getEndpoint(), constantProperties.getAccessKeyId(), constantProperties.getAccessKeySecret());
        URL url = ossClient.generatePresignedUrl(constantProperties.getBucketName(), key, expiration);
        if (url != null) {
            return getShortUrl(url.toString());
        }
        return null;
    }

    /**
     * 访问链接去掉前缀
     * @param url 访问链接
     * @return 去前缀后的访问链接
     */
    private String getShortUrl(String url) {
        String[] imgUrls = url.split("\\?");
        return imgUrls[0].trim();
    }
}
