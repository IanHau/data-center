package com.ian.controller;

import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 文件上传相关接口
 * @author ianhau
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("/Users/ianhau/StudyProject/data-center/src/main/resources/file/")
    private String fileUploadPath;

    /**
     * 文件下载接口   <a href="http://localhost:9090/file/">...</a>{fileUUID}
     *
     * @param response 请求文件
     * @throws IOException IO 异常
     */
    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File file = new File(fileUploadPath + fileName);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");
        // 读取文件的字节流
        os.write(FileUtil.readBytes(file));
        os.flush();
        os.close();
    }
}
