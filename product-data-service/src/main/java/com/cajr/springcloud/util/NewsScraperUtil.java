package com.cajr.springcloud.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cajr.springcloud.service.NewsModulesService;
import com.cajr.springcloud.service.NewsService;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Newsmodules;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author CAJR
 * @create 2019/9/5 19:59
 */
@Component
public class NewsScraperUtil {
    public static final Logger logger=Logger.getLogger(NewsScraperUtil.class);

    @Autowired
    NewsService newsService;

    @Autowired
    NewsModulesService newsModulesService;
    /**
     * 从新闻门户抓取一次新闻
     * 目前使用的新闻门户是网易新闻
     * @throws IOException
     * @throws SQLException
     */

    public  void importNewsData() throws IOException, SQLException {
        String url="http://www.163.com/";
        Document docu1=Jsoup.connect(url).get();
        Elements lis=docu1.getElementsByTag("li");
        for(Element li: lis) {
            if(li.getElementsByTag("a").size()==0){
                continue;
            }
            else {
                Element a=li.getElementsByTag("a").get(0);
                String title=a.text();
                //去除标题小于5个字的、非新闻的<li>标签
                String regex=".{10,}";
                Pattern pattern=Pattern.compile(regex);
                Matcher match=pattern.matcher(title);
                if(!match.find())
                    continue;
                String newsUrl=a.attr("href");


                //图集类忽略，Redirect表示广告类忽略
                if(newsUrl.contains("photoview") || newsUrl.contains("Redirect") || newsUrl.contains("{"))
                    continue;

                try
                {
                    Document docu2=Jsoup.connect(newsUrl).get();
                    Elements eles=docu2.getElementsByClass("post_crumb");
                    //没有面包屑导航栏的忽略：不是正规新闻
                    if(eles.size()==0){
                        continue;
                    }
                    String moduleName=eles.get(0).getElementsByTag("a").get(1).text();

                    System.out.println(title+"("+moduleName+"):"+newsUrl);

                    News news=new News();
                    news.setTitle(title);
                    news.setModuleId(getModuleID(moduleName));
                    news.setUrl(newsUrl);
                    news.setNewsTime(new Date());
                    newsService.add(news);

                }
                catch (SocketTimeoutException e)
                {
                    continue;
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("本次新闻抓取完毕！");
    }

    /**
     * 初次使用，填充新闻模块信息：将默认RSS源所有模块填入。
     */
    private  int getModuleID(String moduleName) {
        int mododuleID=-1;
        Newsmodules newsmodules = newsModulesService.selectByName(moduleName);
        if (newsmodules == null){
            Newsmodules newsmodules1 = new Newsmodules();
            newsmodules.setName(moduleName);
            newsmodules = newsModulesService.selectByName(moduleName);
            mododuleID = newsmodules.getId();
        }else {
            mododuleID = newsmodules.getId();
        }

        return mododuleID;
    }

}
