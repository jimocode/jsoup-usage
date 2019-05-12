package spider.inner;

import spider.New;

/**
 * Created by ibu on 2019/5/12.
 */
public class ContentData {
    private int pageSize;
    private int currentPage;
    private String contentList;

    private New[] sContentList;
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getContentList() {
        return contentList;
    }

    public void setContentList(String contentList) {
        this.contentList = contentList;
    }
}
