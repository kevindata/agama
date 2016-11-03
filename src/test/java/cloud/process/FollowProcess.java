package cloud.process;

import cloud.Constant;
import cloud.RequestUtil;
import cloud.Singleton;
import cloud.entity.YunUser;
import com.github.gin.agama.core.CrawlConfiger;
import com.github.gin.agama.core.JCrawler;
import com.github.gin.agama.processer.PageProcess;
import com.github.gin.agama.proxy.HttpProxy;
import com.github.gin.agama.proxy.ProxyPool;
import com.github.gin.agama.site.Page;
import com.github.gin.agama.site.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by FSTMP on 2016/10/27.
 */
public class FollowProcess implements PageProcess {
    Logger logger = LoggerFactory.getLogger(FollowProcess.class);

    @Override
    public void process(Page page) {
        Matcher m = Constant.USER_PATTERN.matcher(page.getUrl());
        if(m.find()){
            long uk = Long.parseLong(m.group(1));
            int start = Integer.parseInt(m.group(3));

            //设置用户的订阅者是否获取完毕
            int followCount = page.getRender().renderToJson().getJson().getInteger("total_count");
            if((start + Constant.LIMIT) >= followCount){
                Singleton.getYunUserService().updateFollowCrawled(uk);

                List<YunUser> yunUserList = Singleton.getYunUserService().findFollowUnCrawled();
                for(YunUser yunUser : yunUserList){
                    Request request = RequestUtil.createRequest();
                    request.setUrl(String.format(Constant.FOLLOW_URL, yunUser.getUk(), Constant.LIMIT, 0));
                    page.getRequests().add(request);
                }
            } else {
                Request request = RequestUtil.createRequest();
                request.setUrl(String.format(Constant.FOLLOW_URL, uk, Constant.LIMIT, start + Constant.LIMIT));
                page.getRequests().add(request);
            }
        }

        //为了follow和YunUser可以重用
        page.setRawText(page.getRawText().replace("follow_list", "list").replace("follow_uname", "uname").replace("follow_uk", "uk"));
        List<YunUser> userList = page.getRender().renderToJson().toEntityList(YunUser.class);
        if(logger.isDebugEnabled())
            logger.debug("follow:"+page.getRawText());

        //保存订阅者
        page.getResultItems().add(userList);
    }

}