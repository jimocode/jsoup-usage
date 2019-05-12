package spider.inner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibu on 2019/5/12.
 */
public class NewStream {
    private String newsTime;
    private String commentUrl;
    private String skey;
    private String id;
    private String title;
    private String type;
    private String thumbnails;
    private String url;

    private Image[] images;

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
        thumbnailsToImages();
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    private void thumbnailsToImages(){
        List<Image> imageList = new ArrayList<Image>();
        JSONObject thumbnailObj = JSON.parseObject(thumbnails);
        JSONArray imageArray = (JSONArray) thumbnailObj.get("image");
        for (int j = 0; j < imageArray.size(); j++) {
            Image image = JSONObject.parseObject(imageArray.get(j).toString(), Image.class);
            imageList.add(image);
        }
        this.images = imageList.toArray(new Image[imageList.size()]);
    }
}
