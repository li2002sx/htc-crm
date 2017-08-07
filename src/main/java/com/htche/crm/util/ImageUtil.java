package com.htche.crm.util;

import com.htche.crm.model.AjaxResult;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by jankie on 2017/5/23.
 */
public class ImageUtil {

    public static String getRealPicUrl(String picUrl, boolean def) {
        if (picUrl.startsWith("http://")) {
            return picUrl;
        } else {
            String realPicUrl = def ? "/images/user2-160x160.jpg" : "";
            if (!StringUtils.isEmpty(picUrl)) {
                realPicUrl = String.format("%s/upload/%s", ConfigUtil.WebUrl, picUrl);
            }
            return realPicUrl;
        }
    }

    public static AjaxResult upload(MultipartFile file, String cate, String subCate) {

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            InputStream inputStream = file.getInputStream();
            return savePic(inputStream, ext, cate, subCate);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //保存base64码为图片
    public static AjaxResult saveBase64ToPic(String image, String cate, String subCate) {
        AjaxResult result = new AjaxResult();
        result.setSuccess(false);

        byte[] b = null;
        if (image != null) {
            String base64Content = image.substring(image.indexOf(',') + 1);
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(base64Content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (b != null) {
            InputStream inputStream = new ByteArrayInputStream(b);
            String ext = "jpg";
            if (image.startsWith("data:image/png")) {
                ext = "png";
            }
            result = savePic(inputStream, ext, cate, subCate);
        }
        return result;
    }

    private static AjaxResult savePic(InputStream inputStream, String ext, String cate, String subCate) {
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

        String fileName = String.format("%s.%s", RandUtil.stringId18(), ext);
        try {

            BufferedInputStream in = new BufferedInputStream(inputStream);
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
