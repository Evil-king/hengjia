package com.baibei.hengjia.admin.modules.advisory.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_advisory_article")
public class AdvisoryArticle {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  '导航ID',
     */
    @Column(name = "navigation_id")
    private String navigationId;

    /**
     * 文章标题
     */
    @Column(name = "article_title")
    private String articleTitle;

    /**
     * 文章图片
     */
    @Column(name = "article_image")
    private String articleImage;

    /**
     * 文章类型,url-链接,content-内容
     */
    @Column(name = "article_type")
    private String articleType;

    /**
     * 文章链接
     */
    @Column(name = "article_url")
    private String articleUrl;

    /**
     * 文章内容
     */
    @Column(name = "article_content")
    private String articleContent;

    /**
     * 是否显示,show-显示,hidden-隐藏
     */
    @Column(name = "article_display")
    private String articleDisplay;

    /**
     * 状态(1:正常，0:禁用)
     */
    private Byte flag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 '导航ID',
     *
     * @return navigation_id -  '导航ID',
     */
    public String getNavigationId() {
        return navigationId;
    }

    /**
     * 设置 '导航ID',
     *
     * @param navigationId  '导航ID',
     */
    public void setNavigationId(String navigationId) {
        this.navigationId = navigationId;
    }

    /**
     * 获取文章标题
     *
     * @return article_title - 文章标题
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * 设置文章标题
     *
     * @param articleTitle 文章标题
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * 获取文章图片
     *
     * @return article_image - 文章图片
     */
    public String getArticleImage() {
        return articleImage;
    }

    /**
     * 设置文章图片
     *
     * @param articleImage 文章图片
     */
    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    /**
     * 获取文章类型,url-链接,content-内容
     *
     * @return article_type - 文章类型,url-链接,content-内容
     */
    public String getArticleType() {
        return articleType;
    }

    /**
     * 设置文章类型,url-链接,content-内容
     *
     * @param articleType 文章类型,url-链接,content-内容
     */
    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    /**
     * 获取文章链接
     *
     * @return article_url - 文章链接
     */
    public String getArticleUrl() {
        return articleUrl;
    }

    /**
     * 设置文章链接
     *
     * @param articleUrl 文章链接
     */
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    /**
     * 获取文章内容
     *
     * @return article_content - 文章内容
     */
    public String getArticleContent() {
        return articleContent;
    }

    /**
     * 设置文章内容
     *
     * @param articleContent 文章内容
     */
    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    /**
     * 获取是否显示,show-显示,hidden-隐藏
     *
     * @return article_display - 是否显示,show-显示,hidden-隐藏
     */
    public String getArticleDisplay() {
        return articleDisplay;
    }

    /**
     * 设置是否显示,show-显示,hidden-隐藏
     *
     * @param articleDisplay 是否显示,show-显示,hidden-隐藏
     */
    public void setArticleDisplay(String articleDisplay) {
        this.articleDisplay = articleDisplay;
    }

    /**
     * 获取状态(1:正常，0:禁用)
     *
     * @return flag - 状态(1:正常，0:禁用)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:禁用)
     *
     * @param flag 状态(1:正常，0:禁用)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}