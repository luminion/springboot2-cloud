package com.example.alibabaoss.controller;

import com.example.alibabaoss.utils.OSSUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author booty
 */
@RestController
@RequestMapping("file")
public class UploadController {


    @Resource
    private OSSUtil ossUtil;

    /**
     * 文件上传服务
     * @param file  文件
     * @return 访问链接
     */
    @PostMapping("/upload2cloud")
    public String upload2cloud(MultipartFile file) {
        return ossUtil.uploadFile(file);
    }
}
