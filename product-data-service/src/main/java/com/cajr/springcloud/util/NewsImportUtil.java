package com.cajr.springcloud.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cajr.springcloud.service.NewsModulesService;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Newsmodules;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/9 12:32
 */
@Component
public class NewsImportUtil {

    @Resource
    NewsModulesService newsModulesService;

    @Resource
    NewsService newsService;


    private  String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private int getModuleId(String name){
        Newsmodules newsmodules =  newsModulesService.selectByName(name);
        return newsmodules.getId();
    }

    public  JSONObject postRequestFromUrl(String url) throws IOException{
        URL realUrl = new URL(url+"apikey=UKFAJISMHsXwYuJge5ZET2i55pHMar1A9spMXaN99RR3Ttqh9oI5JTsGSkD32fMq");
        URLConnection conn = realUrl.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);

        InputStream inStream = conn.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = JSON.parseObject(jsonText);
            return json;
        } finally {
            inStream.close();
        }
    }

    public  void getNewsList() throws IOException, InterruptedException {

        List<News> newsList = new ArrayList<>();
        String urlModules = "http://api01.idataapi.cn:8000/catid/wangyi?";
        JSONObject jsonM = null;
        jsonM = postRequestFromUrl(urlModules);
        JSONArray jsonArrayModule = jsonM.getJSONArray("data");
        for (Object o : jsonArrayModule) {
            try {
                // 请求示例 url 默认请求参数已经做URL编码
                JSONObject objectM = (JSONObject) o;
                String keyModules =  objectM.getString("key");
                String url= "http://api01.idataapi.cn:8000/news/wangyi?detail=1&catid="+keyModules+"&";
                Integer mId = getModuleId(objectM.getString("value"));

                JSONObject json = null;
                json = postRequestFromUrl(url);
                System.out.println(json.toString());
                JSONArray jsonArray = json.getJSONArray("data");
                System.out.println(jsonArray);
                int n = 0;

                for (Object o1 : jsonArray){
                    JSONObject jsonObject = (JSONObject) o1;
                    News news = new News();

                    news.setModuleId(mId);
                    news.setUrl(jsonObject.getJSONArray("imageUrls").getString(0));
                    news.setTitle(jsonObject.getString("title"));
                    news.setNewsTime(jsonObject.getDate("publishDateStr"));
                    news.setContent(jsonObject.getString("html"));

                    this.newsService.add(news);
                    newsList.add(news);
                }
                Thread.sleep(2000);
            }catch (Exception e){
                continue;
            }
        }

//        while (json.getBoolean("hasNext")){
//            n++;
//            url = "http://api01.idataapi.cn:8000/news/wangyi?detail=1&catid=T1348649580692&pageToken="+n+"&";
//            System.out.println(url);
//            json = postRequestFromUrl(url);
//            jsonArray = json.getJSONArray("data");
//
//            for (Object o : jsonArray){
//                JSONObject jsonObject = (JSONObject) o;
//                News news = new News();
//                news.setModuleId(69);
//                news.setUrl(jsonObject.getString("url"));
//                news.setTitle(jsonObject.getString("title"));
//                news.setNewsTime(jsonObject.getDate("publishDateStr"));
//                news.setContent(jsonObject.getString("html"));
//
//                newsList.add(news);
//            }
//            Thread.sleep(500);
//        }
//        for (Object o : jsonArray) {
//            JSONObject jsonObject = (JSONObject) o;
//            System.out.println(jsonObject.getString("value"));
//            String url1 = "http://api01.idataapi.cn:8000/news/wangyi?detail=1";
//            url1 = url1 + "&catid=" + jsonObject.getString("key") + "&";
//            JSONObject jsonObject1 = postRequestFromUrl(url1);
//            int i = 0;
//            while (jsonObject1.getString("pageToken") != null && i <= 5) {
//                String pageToken = jsonObject1.getString("pageToken");
//                if (!url1.contains("pageToken")){
//                    url1 = url1 + "pageToken=" + pageToken;
//                }
//                System.out.println(url1);
//                jsonObject1 = postRequestFromUrl(url1);
//                System.out.println(jsonObject1.toString());
//                i++;
//            }
//            i=0;
//        }

    }

    public  List<Newsmodules> getNewsModulesList() throws IOException {
        List<Newsmodules> newsmodules = new ArrayList<>();
        String url = "http://api01.idataapi.cn:8000/catid/wangyi?";
        JSONObject json = null;
        json = postRequestFromUrl(url);
        JSONArray jsonArray = json.getJSONArray("data");
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            Newsmodules newsmodules1 = new Newsmodules();
            newsmodules1.setName(jsonObject.getString("value"));
            newsmodules.add(newsmodules1);
        }
        return newsmodules;
    }
}
