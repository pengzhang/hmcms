package models.cms;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import models.BaseModel;
import models.core.user.User;

@Entity
@Table(name="cms_like")
@org.hibernate.annotations.Table(comment = "用户点赞管理", appliesTo = "cms_like")
public class UserLike extends BaseModel {
	
	@OneToOne
	@JoinColumn(name="user_id", columnDefinition="bigint default 0 comment '点赞用户'")
	public User user;
	
	@ManyToOne
	@JoinColumn(name="article_id", columnDefinition="bigint default 0 comment '文章ID'")
	public Article article;
	
	@ManyToOne
	@JoinColumn(name="video_id", columnDefinition="bigint default 0 comment '视频ID'")
	public Video video;
	
	@ManyToOne
	@JoinColumn(name="comment_id", columnDefinition="bigint default 0 comment '评论ID'")
	public Comment comment;
	
	public UserLike addUser(User user) {
		this.user = user;
		return this;
	}
	
	public UserLike addArticle(Article article) {
		this.article = article;
		return this;
	}
	
	public UserLike addVideo(Video video) {
		this.video = video;
		return this;
	}
	
	public UserLike addComment(Comment comment) {
		this.comment = comment;
		return this;
	}
	
	@Override
	public String toString() {
		return "点赞";
	}
	
}
