package models.hmcms;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import annotations.Upload;
import models.hmcms.enumtype.VideoType;
import play.data.validation.MaxSize;

/**
 * 视频
 * @author zp
 *
 */
@Entity
@Table(name="video")
@org.hibernate.annotations.Table(comment = "视频管理", appliesTo = "video")
public class Video extends CmsModel implements Serializable {
	
	//文章部分
	@Column(columnDefinition = "varchar(500) comment '视频标题'")
	public String title;
	
	@MaxSize(value=5000)
	@Column(columnDefinition = "varchar(5000) comment '视频简介'")
	public String video_desc;
	
	@Upload
	@Column(columnDefinition = "varchar(2000) comment '视频链接'")
	public String video_url;
	
	@Upload
	@Column(columnDefinition = "varchar(500) comment '封面图'")
	public String cover;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(50) comment '视频播放类型:1-免费,2-vip,3-单独购票'")
	public VideoType vtype ;
	
	@Column(columnDefinition="int default 60 comment '试播时长'")
	public int tryout_time;
	
	@Column(columnDefinition="int default 0 comment '视频展示顺序'")
	public int v_seq;

	public Video() {
		super();
	}

	@Override
	public String toString() {
		return title;
	}
	
	
	
}
