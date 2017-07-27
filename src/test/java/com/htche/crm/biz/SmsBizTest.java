package com.htche.crm.biz;

import com.alibaba.fastjson.JSON;
import com.htche.crm.WebBootstrap;
import com.htche.crm.model.SmsHuyiResult;
import com.htche.crm.util.SmsHuyiUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jankie on 2017/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebBootstrap.class)
public class SmsBizTest {

    @Autowired
    SmsBiz smsBiz;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void sendSms() throws Exception {
        SmsHuyiResult smsHuyiResult = SmsHuyiUtil.sendSms("13810583852"
                , "尊敬的李胜喜，在这个属于你的精彩日子里，送上我最诚挚的祝福，祝你生日快乐！愿你生活如意，爱情甜蜜，工作顺利！"
                , "cf_mtgg", "d551472451952c45d8d06e8350ac802d");
        System.out.println(JSON.toJSON(smsHuyiResult));

//        Map<String, String> cookieMap = new HashMap<>();
//        cookieMap.put("ASP.NET_SessionId", "iqpfbx55xbuebvmawniori45");
//        cookieMap.put(".ASPXAUTH", "62013DD8D76AE9ABEEE84DB32A79C61D8B1AD092A37588F8754F73AF0150C5E5F31A9D965E202A82989DD3C791BBD4FDB3549EE9C20D59304BD1D0B1C49A9FEE2466B44A24497E1B5527648AA7F00F2373239B3E846AFDC64C1EC9A30BE7DC62FB623EA8AFF0657A2CACC841C11E0A835A851804");
//        Connection connection = Jsoup.connect("http://insight.ctcai.com.cn/Olap/MyOlap.aspx?MenuType=CoustomsImports").cookies(cookieMap);
//
//        connection.header("Accept", "text/html, application/xhtml+xml, */*");
//        connection.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        connection.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
//
//        Map<String, String> dataMap = new HashMap<>();
//        dataMap.put("__EVENTTARGET", "");
//        dataMap.put("__EVENTARGUMENT", "");
//        dataMap.put("__VIEWSTATE", "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FgJmDxUBDOmDqOmXqOaVsOaNrmQCAg8VAQE3ZAIDDxYCHwECAhYEZg9kFgICAQ8WAh8CBSp+L09sYXAvTXlPbGFwLmFzcHg/TWVudVR5cGU9RGVwYXJ0bWVudERhdGEWAmYPFQEM57uf6K6h5YiG5p6QZAIBD2QWAgIBDxYCHwIFLX4vT2xhcC9GaXhlZE9sYXAuYXNweD9NZW51VHlwZT1EZXBhcnRtZW50RGF0YRYCZg8VAQzlm7rlrprmiqXooahkAggPZBYGAgEPFgQfAgUrfi9PbGFwL015T2xhcC5hc3B4P01lbnVUeXBlPU92ZXJTZWFzTWFya2V0cx8DBRxxaWVodWFuKHRoaXMsInFoX2NvbjgiLHRydWUpFgJmDxUBDOa1t+WkluW4guWcumQCAg8VAQE4ZAIDDxYCHwECAhYEZg9kFgICAQ8WAh8CBSt+L09sYXAvTXlPbGFwLmFzcHg/TWVudVR5cGU9T3ZlclNlYXNNYXJrZXRzFgJmDxUBDOe7n+iuoeWIhuaekGQCAQ9kFgICAQ8WAh8CBS5+L09sYXAvRml4ZWRPbGFwLmFzcHg/TWVudVR5cGU9T3ZlclNlYXNNYXJrZXRzFgJmDxUBDOWbuuWumuaKpeihqGQCCQ9kFgYCAQ8WBB8CBSl+L09sYXAvTXlPbGFwLmFzcHg/TWVudVR5cGU9RG9tZXN0aWNTYWxlcx8DBRxxaWVodWFuKHRoaXMsInFoX2NvbjkiLHRydWUpFgJmDxUBDOWbveS6p+mUgOmHj2QCAg8VAQE5ZAIDDxYCHwECAhYEZg9kFgICAQ8WAh8CBSl+L09sYXAvTXlPbGFwLmFzcHg/TWVudVR5cGU9RG9tZXN0aWNTYWxlcxYCZg8VAQznu5/orqHliIbmnpBkAgEPZBYCAgEPFgIfAgUsfi9PbGFwL0ZpeGVkT2xhcC5hc3B4P01lbnVUeXBlPURvbWVzdGljU2FsZXMWAmYPFQEM5Zu65a6a5oql6KGoZAIFD2QWBAIDD2ZkAgUPZhYGZg9mZAIBD2QWAmYPDxYCHgpDb2x1bW5TcGFuAgJkZAICD2QWBGYPDxYEHg1WZXJ0aWNhbEFsaWduCyonU3lzdGVtLldlYi5VSS5XZWJDb250cm9scy5WZXJ0aWNhbEFsaWduAR4EXyFTQgKAgAhkZAIBDw8WBB4FV2lkdGgbAAAAAAAAWUAHAAAAHwYCgAJkZAIHDw9kDxAWAWYWARYCHg5QYXJhbWV0ZXJWYWx1ZWQWAQIDZGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgMFMWN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjEkT2xhcENsaWVudDFDdWJlQnJvd3NlclQFImN0bDAwJG1haW5tZW51MSRMb2dpblN0YXR1czEkY3RsMDEFImN0bDAwJG1haW5tZW51MSRMb2dpblN0YXR1czEkY3RsMDM2jnhrqe/WLSfyCANQdmVd+nhPLA==");
//        dataMap.put("__VIEWSTATEGENERATOR", "EA756AB7");
//        dataMap.put("ctl00$ContentPlaceHolder1$OlapClient1CubeSelector", "海关分车型进口量");
//        dataMap.put("ctl00$ContentPlaceHolder1$OlapClient1CubeBrowserT_SelectedNode", "");
//        dataMap.put("ctl00$ContentPlaceHolder1$OlapClient1CubeBrowserT_ExpandedNodes", "{Measures};");
//        dataMap.put("ctl00$ContentPlaceHolder1$OlapClient1CubeBrowserT_ScrollTo", "0;0");
//        dataMap.put("ctl00$ContentPlaceHolder1$OlapClient1CubeBrowserTValue", "");
//        dataMap.put("S_ctl00_ContentPlaceHolder1_OlapClient1Grid", "526px;380px;True;True;2;2");
//        dataMap.put("__CALLBACKID", "ctl00$ContentPlaceHolder1$OlapClient1Manager");
//        dataMap.put("__CALLBACKPARAM", "[ctl00$ContentPlaceHolder1$OlapClient1Grid::OlapGrid,OlapChart,OlapPager[!drillDown&D1E9F62F/6314162B!]]");
//        dataMap.put("__EVENTVALIDATION", "/wEWDAKHiamHBgK3sekeAquk/IUIAuqitscBAtyntNEGAua3jBwCgcaOlg0Cn6ab4gcCpI+isA0CjeKuuA0CvJjHvwcCzODimgmw6wra8KfRceBmswpI37358/38Tg==");
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            //添加参数
//            connection.data(entry.getKey(), entry.getValue());
//        }
//        Document doc = connection.post();
//        System.out.println(doc);
    }
}