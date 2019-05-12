package spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import spider.inner.Content;
import spider.inner.ContentData;
import spider.inner.DocData;
import spider.inner.NewStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ibu on 2019/5/12.
 */
public class Spider {

    private List<NewStream> newStreamList;
    private NewStream lastItem;
    private List<New> newList;

    private Spider(){
        newStreamList = new ArrayList<NewStream>();
        newList = new ArrayList<New>();
    }
    private static Spider newInstance(){
        return new Spider();
    }
    // 获取首页urls
    private Spider loadFirstUrls() {
        // 访问凤凰网资讯首页
        Connection conn = Jsoup.connect("https://news.ifeng.com/");
        Document doc = null;
        try{
            doc = conn.get();
        }catch (IOException e){
            e.printStackTrace();
        }
        // 从srcipt中获取allData
        String allData = allData(doc);
        JSONObject allDataObj = (JSONObject)JSON.parse(allData);
        // 获取新闻数据newsstream
        JSONArray array = (JSONArray)allDataObj.get("newsstream");
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject)array.get(i);
            NewStream newStream = JSONObject.parseObject(obj.toJSONString(), NewStream.class);
            newStreamList.add(newStream);
//            System.out.println("find:<"+newStream.getUrl()+">");
            lastItem = newStream;
        }
        return this;
    }

    private String allData(Document doc){
        // 获取HTML,参考doc/index.html，doc/detail.html
        String outerHtml = doc.outerHtml();
        // 使用正则表达式获取首批url
        Pattern pattern = Pattern.compile("<script>(.*?)</script>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(outerHtml);
        // 从srcipt中获取allData,参考doc/index.js，doc/detail.js
        String allData = "";
        while(matcher.find()){
            String innerScriptContent = matcher.group(1).trim();
            if(innerScriptContent.startsWith("var allData =")){
                // 获取第一行allData数据
                innerScriptContent =  innerScriptContent.substring(0, innerScriptContent.indexOf("\n"));
                innerScriptContent = innerScriptContent.substring(innerScriptContent.indexOf("=")+1, innerScriptContent.length()-1).trim();
                allData = innerScriptContent;
                break;
            }
        }
        return allData;
    }
    // 访问新闻详情
    private void visitPageDetail(){
        for (NewStream newStream: newStreamList) {
            String url = newStream.getUrl();
//            System.out.println("visit:<"+url+">");
            // 访问凤凰网资讯新闻详情页
            Connection conn = Jsoup.connect(url);
            Document doc = null;
            try{
                doc = conn.get();
            }catch (IOException e){
                e.printStackTrace();
            }
            // 从srcipt中获取allData
            String allData = allData(doc);
            JSONObject allDataObj = (JSONObject)JSON.parse(allData);
            // 获取新闻数据docData
            JSONObject docDataObj = (JSONObject)allDataObj.get("docData");
            DocData docData = JSONObject.parseObject(docDataObj.toJSONString(), DocData.class);
            // 设置contents
            JSONObject contentDataObj = (JSONObject)docDataObj.get("contentData");
            ContentData contentData = JSONObject.parseObject(contentDataObj.toJSONString(), ContentData.class);
            docData.setsContentData(contentData);
            // 设置contentList
            String contentList = contentData.getContentList();
            JSONArray contentArray = JSONArray.parseArray(contentList);
            for (int i = 0; i < contentArray.size(); i++) {
                Content content = JSONObject.parseObject(contentArray.get(i).toString(), Content.class);
                New newObj = new New();
                newObj.setNewsTime(docData.getNewsTime());
                newObj.setTitle(docData.getTitle());
                newObj.setContent(content.getData());
                newObj.setSource(docData.getSource());
                newObj.setSourceUrl(docData.getSourceUrl());
                System.out.println(newObj.toString());
                newList.add(newObj);
            }
        }
    }
    // 爬取首页新闻
    private void crawlFirstPage(){
        loadFirstUrls();
        visitPageDetail();
    }
    // 爬取更多页新闻，每调用一次就爬取写一页
    private void crawlNextPage(){
        if(newStreamList.size() == 0){
            crawlFirstPage();
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("https://shankapi.ifeng.com/shanklist/_/getColumnInfo/_/dynamicFragment/")
                .append(lastItem.getId()).append("/")
                .append(System.currentTimeMillis()).append("/")
                .append("20/3-35191-/getColumnInfoCallback");
        String nextPageUrl = builder.toString();
        // 清空 newStreamList和lastItem
        newStreamList = new ArrayList<NewStream>();
        lastItem = null;
        // 访问凤凰网资讯下一页
        Connection conn = Jsoup.connect(nextPageUrl);
        Document doc = null;
        try{
            doc = conn.get();
        }catch (IOException e){
            e.printStackTrace();
        }
        // 获取HTML,参考doc/getColumnInfo.html
        String outerHtml = doc.outerHtml();
        // 使用正则表达式
        Pattern pattern = Pattern.compile("getColumnInfoCallback\\((.*?)\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(outerHtml);
        String columnInfo = "";
        while(matcher.find()){
            columnInfo = matcher.group(1).trim();
            JSONObject columnInfoObj = (JSONObject)JSON.parse(columnInfo);
            // 获取新闻数据newsstream
            JSONObject dataObj = (JSONObject)columnInfoObj.get("data");
            JSONArray array = (JSONArray)dataObj.get("newsstream");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject)array.get(i);
                NewStream newStream = JSONObject.parseObject(obj.toJSONString(), NewStream.class);
                newStreamList.add(newStream);
//                System.out.println("find:<"+newStream.getUrl()+">");
                lastItem = newStream;
            }
            visitPageDetail();
        }
    }

    /**
     * 爬取指定页数的新闻
     * @param page 页数
     */
    public void crawlFixedPage(int page){
        while(page>0){
            crawlNextPage();
            page=page-1;
        }
    }

    /**
     * 将资讯内容写入文件
     * @param dir 资讯输出目录
     * @throws IOException IO异常
     */
    public void writeToFile(String dir) throws IOException{
        if(!Files.exists(Paths.get(dir))){
            throw new FileNotFoundException("资讯输出目录找不到");
        }
        for (int i = 0; i < newList.size(); i++) {
            FileOutputStream fos = new FileOutputStream(new File(dir+"//"+i+".txt"));
            fos.write(newList.get(i).getContent().getBytes());
            fos.close();
        }
    }

    /**
     * 获取资讯列表
     * @return 资讯对象集合
     */
    public List<New> getNewList(){
        return newList;
    }
    public static void main(String[] args){
        Spider spider = Spider.newInstance();
        spider.crawlFixedPage(3);
        try {
            spider.writeToFile("d://crawls");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
