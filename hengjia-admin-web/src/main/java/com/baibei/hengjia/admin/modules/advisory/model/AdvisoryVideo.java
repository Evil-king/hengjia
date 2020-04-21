package com.baibei.hengjia.admin.modules.advisory.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_advisory_video")
public class AdvisoryVideo {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 导航id
     */
    @Column(name = "navigation_id")
    private String navigationId;

    /**
     * 视频/音频标题
     */
    @Column(name = "video_title")
    private String videoTitle;

    /**
     * 视频/音频封面
     */
    @Column(name = "video_index")
    private String videoIndex;

    /**
     * 视频/音频类型,video-视频,audio-音频
     */
    @Column(name = "video_type")
    private String videoType;

    /**
     * 视频/音频链接
     */
    @Column(name = "video_url")
    private String videoUrl;

    /**
     * 排序
     */
    @Column(name = "video_sort")
    private Integer videoSort;

    /**
     * 是否显示,show-显示,hidden-隐藏
     */
    @Column(name = "video_display")
    private String videoDisplay;

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
     * 获取导航id
     *
     * @return navigation_id - 导航id
     */
    public String getNavigationId() {
        return navigationId;
    }

    /**
     * 设置导航id
     *
     * @param navigationId 导航id
     */
    public void setNavigationId(String navigationId) {
        this.navigationId = navigationId;
    }

    /**
     * 获取视频/音频标题
     *
     * @return video_title - 视频/音频标题
     */
    public String getVideoTitle() {
        return videoTitle;
    }

    /**
     * 设置视频/音频标题
     *
     * @param videoTitle 视频/音频标题
     */
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    /**
     * 获取视频/音频封面
     *
     * @return video_index - 视频/音频封面
     */
    public String getVideoIndex() {
        return videoIndex;
    }

    /**
     * 设置视频/音频封面
     *
     * @param videoIndex 视频/音频封面
     */
    public void setVideoIndex(String videoIndex) {
        this.videoIndex = videoIndex;
    }

    /**
     * 获取视频/音频类型,vedio-视频,audio-音频
     *
     * @return video_type - 视频/音频类型,vedio-视频,audio-音频
     */
    public String getVideoType() {
        return videoType;
    }

    /**
     * 设置视频/音频类型,vedio-视频,audio-音频
     *
     * @param videoType 视频/音频类型,vedio-视频,audio-音频
     */
    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    /**
     * 获取视频/音频链接
     *
     * @return video_url - 视频/音频链接
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * 设置视频/音频链接
     *
     * @param videoUrl 视频/音频链接
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * 获取排序
     *
     * @return video_sort - 排序
     */
    public Integer getVideoSort() {
        return videoSort;
    }

    /**
     * 设置排序
     *
     * @param videoSort 排序
     */
    public void setVideoSort(Integer videoSort) {
        this.videoSort = videoSort;
    }

    /**
     * 获取是否显示,show-显示,hidden-隐藏
     *
     * @return video_display - 是否显示,show-显示,hidden-隐藏
     */
    public String getVideoDisplay() {
        return videoDisplay;
    }

    /**
     * 设置是否显示,show-显示,hidden-隐藏
     *
     * @param videoDisplay 是否显示,show-显示,hidden-隐藏
     */
    public void setVideoDisplay(String videoDisplay) {
        this.videoDisplay = videoDisplay;
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