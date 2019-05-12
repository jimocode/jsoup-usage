package spider.inner;

/**
 * Created by ibu on 2019/5/12.
 */
public class DocData {
    private String newsTime;
    private String title;
    private String contentData;
    private String source;
    private String sourceUrl;

    private ContentData sContentData;

    public ContentData getsContentData() {
        return sContentData;
    }

    public void setsContentData(ContentData sContentData) {
        this.sContentData = sContentData;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
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

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentData() {
        return contentData;
    }

    public void setContentData(String contentData) {
        this.contentData = contentData;
    }
}
