package net.osc.gin.agama.processer;

import net.osc.gin.agama.core.CrawlConfiger;
import net.osc.gin.agama.core.JCrawler;
import net.osc.gin.agama.entity.BiliBiliEntity;
import net.osc.gin.agama.entity.CNBlog;
import net.osc.gin.agama.proxy.HttpProxy;
import net.osc.gin.agama.site.Page;
import net.osc.gin.agama.sorter.FileDataStorer;
import net.osc.gin.agama.util.BeanUtils;

import java.net.Proxy;
import java.util.List;

/**
 * Created by FSTMP on 2016/8/30.
 */
public class CNBlogProcess implements PageProcess{
    @Override
    public void process(Page page) {
        System.out.println(page.getHtml());

        List<CNBlog> lists = page.getHtml().toEntityList(CNBlog.class);

        page.getRequests().addAll(page.getHtml().xpath("//div[@class='pager']/a").attrs("href"));

        for(CNBlog cnBlog : lists){
            page.getRecords().add(BeanUtils.toCSVRecord(cnBlog));
        }
    }

    public static void main(String[] args) {
        HttpProxy proxy = new HttpProxy(Proxy.Type.HTTP, "10.228.110.21", 80, "panyongjian", "pan240409F");
        CrawlConfiger config = new CrawlConfiger("http://www.cnblogs.com/");
        config.setProxy(proxy);
        //config.setDepth(1);
        config.setThreadNum(2);
        //config.setAjaxModel(true);
        //config.setDriverPath("D:/download/phantomjs-2.1.1-windows/bin/phantomjs.exe");
        JCrawler.create(new CNBlogProcess()).persistBy(new FileDataStorer("D:\\cnblog.csv")).setConfig(config).run();
    }
}