package com.htche.crm.controller.admin;

import com.htche.crm.model.AjaxResult;
import com.htche.crm.util.ImageUtil;
import com.htche.crm.util.RandUtil;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;

/**
 * Created by guozhihao on 2017/1/17.
 */
@Controller
@RequestMapping("")
public class UploadController {

    /**
     * 图片上传，返回名称
     *
     * @param file 图片文件
     * @return 真实路径
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult upload(
            @RequestPart(required = false, value = "file") MultipartFile file,
            @RequestParam(value = "cate", required = true) String cate,
            @RequestParam(value = "subCate", required = false) String subCate) {
        AjaxResult result = ImageUtil.upload(file, cate, subCate);

        return result;
    }
}
