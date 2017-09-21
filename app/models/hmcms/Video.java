package models.hmcms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import annotations.Exclude;
import annotations.Upload;
import models.hmcms.enumtype.Quality;
import models.hmcms.enumtype.Recommend;
import models.hmcms.enumtype.VideoType;
import models.hmcore.user.User;
import play.data.validation.MaxSize;
import play.db.jpa.JPA;
import plugins.router.Get;

/**
 * 视频
 * @author zp
 *
 */
@Entity
@Component
@Table(name="cms_video")
@org.hibernate.annotations.Table(comment = "视频管理", appliesTo = "cms_video")
public class Video extends CmsModel implements Serializable {
	
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
	
	@ManyToMany(cascade=CascadeType.REFRESH)
	public List<Category> categories = new ArrayList<>();
	
	@Exclude
	@OneToMany(cascade=CascadeType.ALL,mappedBy="video")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	public List<Comment> comments = new ArrayList<>();
	
	@ManyToMany(cascade=CascadeType.REFRESH)
	public List<Tag> tags = new ArrayList<>();

	public Video() {
		super();
	}
	
	public Comment addComment(Comment comment, User user){
		comment.addVideoComment(this, user);
		return comment;
	}
	
	public void addView(){
		JPA.em().createQuery("update Video a set a.view_total=:total where a.id=:id").setParameter("total", ++this.view_total).setParameter("id", this.id).executeUpdate();
	}
	
	public List<Comment> getComments(int page, int size){
		return Comment.find("select c from Video a left join a.comments c where a.id=? order by c.createDate desc", this.id).fetch(page, size);
	}
	
	public List<Video> getNewestList(int page, int size) {
		return Video.find("status=? order by updateDate desc",false).fetch(page, size);
	}
	
	public List<Video> videoByHot(int page, int size) {
		return Video.find("select a from Video a left join a.comments c where a.status=? group by a.id order by count(c.id) desc, a.updateDate desc",false).fetch(page, size);
	}
	
	public List<Video> videoByFocus(int page, int size) {
		return Video.find("recommend=? and quality=? and status=? order by updateDate desc", Recommend.recommend, Quality.quality, false).fetch(page, size);
	}

	public List<Video> videoByCategoryList(long categoryId, int page, int size) {
		return Video.find("select a from Video a left join a.categories c where c.id=? and a.status=?", categoryId, false).fetch(page, size);
	}

	public List<Video> videoByTagList(long tagId, int page, int size) {
		return Video.find("select a from Video a left join a.tags t where t.id=? and a.status=? order by a.updateDate desc", tagId, false).fetch(page, size);
	}
	
	public List<Video> getRecommendVideo(int max){
		return Video.find("recommend=? and status=?order by createDate desc", Recommend.recommend, false).fetch(max);
	}
	
	public List<Tag> getVideoTags(int max){
		return Tag.find("select t from Video a left join a.tags as t where a.status=? group by t.tag order by t.createDate desc", false).fetch(max);
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	
	
}
