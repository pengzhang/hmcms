package models.hmcms;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import models.BaseModel;
import models.hmcore.user.User;
import play.data.validation.MaxSize;
/**
 * 评论
 * @author zp
 *
 */
@Entity
@Table(name="cms_comment")
@org.hibernate.annotations.Table(comment = "评论管理", appliesTo = "cms_comment")
public class Comment extends BaseModel implements Serializable{
	
	@MaxSize(value=500)
	@Column(columnDefinition="varchar(1000) comment '评论内容'")
	public String comment;
	
	@OneToOne
	@JoinColumn(name="user_id", columnDefinition="bigint default 0 comment '评论用户'")
	public User user;
	
	@ManyToOne
	@JoinTable(name="cms_article_comment", joinColumns=@JoinColumn(name = "comment_id"), inverseJoinColumns=@JoinColumn(name = "article_id"))
	public Article article;
	
	public Comment add(Article article, User user){
		this.article = article;
		this.user = user;
		create();
		return this;
	}

	@Override
	public String toString() {
		return "评论";
	}
	
	
	
}
