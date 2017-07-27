package com.htche.crm.util;

import com.htche.crm.model.AjaxResult;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by jankie on 2017/5/23.
 */
public class ImageUtil {

    public static String getRealPicUrl(String picUrl, boolean def) {
        String realPicUrl = def ? "/images/user2-160x160.jpg" : "";
        if (!StringUtils.isEmpty(picUrl)) {
            realPicUrl = String.format("/upload/%s", picUrl);
        }
        return realPicUrl;
    }

    public static AjaxResult upload(MultipartFile file, String cate, String subCate) {
        AjaxResult result = new AjaxResult();
        result.setSuccess(false);

        LocalDate localDate = LocalDate.now();
        if (StringUtils.isBlank(subCate)) {
            subCate = String.format("%s/%s/%s"
                    , localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        }
        String imagePath = String.format("%s/static/upload/%s/%s/"
                , Thread.currentThread().getContextClassLoader().getResource("").getPath()
                , cate, subCate);
        File fileDir = new File(imagePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileName = String.format("%s.%s", RandUtil.stringId18(), ext);
        try {

            BufferedInputStream in = new BufferedInputStream(file.getInputStream());
            //文件存储在D:/upload/images目录下,这个目录也得存在
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(new File(imagePath + fileName)));
            Streams.copy(in, out, true);

            result.setSuccess(true);
            result.setValue(String.format("%s/%s/%s", cate, subCate, fileName));
        } catch (IOException ex) {
            result.setError(ex.getMessage());
        }
        return result;
    }
}
