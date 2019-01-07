package com.fancy.retrofit_client.entity;

/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2019\1\4 0004
 * @since JDK 1.7
 */
public class ListEntity {

    /**
     * title : 二到没治了
     * content : 　　第一次去学校医院是因为发烧，医生大妈二话不说递给我一根体温计，我也二话不说就含在了嘴里……<br/><br/>　　大妈看了我一眼，温柔地说道：“腋下。”<br/><br/>　　我想了想，还是听话地叫了声：“耶！”
     * poster :
     * url : http://www.laifudao.com/wangwen/96292.htm
     */

    private String title;
    private String content;
    private String poster;
    private String url;

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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
