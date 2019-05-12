## jsoup-usage   基于jsoup的凤凰网新闻资讯爬取工具
### 1. 导入Jsoup
[Jsoup](https://jsoup.org/) 是一个用于处理真实HTML的Java库。它提供了一个非常方便的API，用于提取和操作数据，使用最好的DOM，CSS和类似jquery的方法。<br>
maven依赖：
```
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.11.3</version>
</dependency>
```
gradle依赖：
```
compile 'org.jsoup:jsoup:1.11.3'
 ```
### 2. 导入Fastjson
[Fastjson](https://github.com/alibaba/fastjson/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98) 是一个性能很好的 Java 语言实现的 JSON 解析器和生成器，来自阿里巴巴的工程师开发。<br>
maven依赖：
```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.54</version>
</dependency>
```
gradle依赖：
```
compile 'com.alibaba:fastjson:1.2.54'
```
### 3. 使用说明
```
// 第一步，生成Spider对象
Spider spider = Spider.newInstance();
// 第二步，爬取指定页数的资讯，一页有20条资讯，如下爬取了三页资讯
spider.crawlFixedPage(3);
// 第三步，输出新闻到本地目录中
try {
    spider.writeToFile("d://crawls");
} catch (IOException e) {
    e.printStackTrace();
}
```
