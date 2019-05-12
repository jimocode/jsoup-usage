package spider;

/**
 * 资讯对象类
 */
public class New {
    private String newsTime;    // 时间
    private String title;       // 标题
    private String content;     // 标题
    private String source;      // 来源
    private String sourceUrl;   // 来源url

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n")
                .append("   newsTime:").append(newsTime).append("\n")
                .append("   title:").append(title).append("\n")
                .append("   content:").append(content).append("\n")
                .append("   source:").append(source).append("\n")
                .append("   sourceUrl:").append(sourceUrl).append("\n")
                .append("}\n");
        return builder.toString();
    }
}
