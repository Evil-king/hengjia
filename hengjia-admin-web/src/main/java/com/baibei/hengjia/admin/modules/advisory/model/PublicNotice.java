package com.baibei.hengjia.admin.modules.advisory.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_advisory_public_notice")
public class PublicNotice {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    private String title;

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
     * 标签
     */
    private String tag;

    /**
     * 生效开始时间
     */
    @Column(name = "effective_begin_time")
    private Date effectiveBeginTime;

    /**
     * 生效结束时间
     */
    @Column(name = "effective_end_time")
    private Date effectiveEndTime;

    /**
     * 状态(1:正常，0:已删除)
     */
    private Byte flag;

    /**
     * 是否开启(1:隐藏，0:取消
     */
    private String hidden;

    /**
     * 是否置顶(1:开启，0:关闭
     */
    private Integer top;

    /**
     * 类型(mall:商场公告，trade:交易公告
     */
    @Column(name = "public_type")
    private String publicType;

    /**
     * 发布类型(now:立即生效，appoint:指定生效)
     */
    @Column(name = "publish_type")
    private String publishType;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 公共链接
     */
    private String link;

    private String image;

    /**
     * 内容
     */
    private String content;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
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

    /**
     * 获取标签
     *
     * @return tag - 标签
     */
    public String getTag() {
        return tag;
    }

    /**
     * 设置标签
     *
     * @param tag 标签
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 获取生效开始时间
     *
     * @return effective_begin_time - 生效开始时间
     */
    public Date getEffectiveBeginTime() {
        return effectiveBeginTime;
    }

    /**
     * 设置生效开始时间
     *
     * @param effectiveBeginTime 生效开始时间
     */
    public void setEffectiveBeginTime(Date effectiveBeginTime) {
        this.effectiveBeginTime = effectiveBeginTime;
    }

    /**
     * 获取生效结束时间
     *
     * @return effective_end_time - 生效结束时间
     */
    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    /**
     * 设置生效结束时间
     *
     * @param effectiveEndTime 生效结束时间
     */
    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    /**
     * 获取状态(1:正常，0:已删除)
     *
     * @return flag - 状态(1:正常，0:已删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:已删除)
     *
     * @param flag 状态(1:正常，0:已删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    /**
     * 获取是否开启(1:隐藏，0:取消
     *
     * @return hidden - 是否开启(1:隐藏，0:取消
     */
    public String getHidden() {
        return hidden;
    }

    /**
     * 设置是否开启(1:隐藏，0:取消
     *
     * @param hidden 是否开启(1:隐藏，0:取消
     */
    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    /**
     * 获取是否置顶(1:开启，0:关闭
     *
     * @return top - 是否置顶(1:开启，0:关闭
     */
    public Integer getTop() {
        return top;
    }

    /**
     * 设置是否置顶(1:开启，0:关闭
     *
     * @param top 是否置顶(1:开启，0:关闭
     */
    public void setTop(Integer top) {
        this.top = top;
    }

    /**
     * 获取类型(mall:商场公告，trade:交易公告
     *
     * @return public_type - 类型(mall:商场公告，trade:交易公告
     */
    public String getPublicType() {
        return publicType;
    }

    /**
     * 设置类型(mall:商场公告，trade:交易公告
     *
     * @param publicType 类型(mall:商场公告，trade:交易公告
     */
    public void setPublicType(String publicType) {
        this.publicType = publicType;
    }

    /**
     * 获取发布类型(now:立即生效，appoint:指定生效)
     *
     * @return publish_type - 发布类型(now:立即生效，appoint:指定生效)
     */
    public String getPublishType() {
        return publishType;
    }

    /**
     * 设置发布类型(now:立即生效，appoint:指定生效)
     *
     * @param publishType 发布类型(now:立即生效，appoint:指定生效)
     */
    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    /**
     * 获取排序
     *
     * @return seq - 排序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置排序
     *
     * @param seq 排序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * 获取公共链接
     *
     * @return link - 公共链接
     */
    public String getLink() {
        return link;
    }

    /**
     * 设置公共链接
     *
     * @param link 公共链接
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}