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
import org.springframework.stereotype.Service;

import annotations.Exclude;
import annotations.Upload;
import models.hmcms.enumtype.Open;
import models.hmcms.enumtype.Quality;
import models.hmcms.enumtype.Recommend;
import models.hmcore.user.User;
import play.data.validation.MaxSize;
import play.db.jpa.JPA;
import plugins.router.Get;

/**
 * 文章
 * @author zp
 *
 */
@Entity
@Table(name="cms_article")
@org.hibernate.annotations.Table(comment = "文章管理", appliesTo = "cms_article")
public class Article extends CmsModel implements Serializable {

	@Column(columnDefinition = "varchar(500) comment '文章标题'")
	public String title;
	
	@MaxSize(value=50000)
	@Column(columnDefinition = "text comment '文章内容'")
	public String content;
	
	@Upload
	@Column(columnDefinition = "varchar(500) comment '封面图'")
	public String cover;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(50) comment '类型:0:私有,1:公开'")
	public Open open = Open.common;
	
	@ManyToMany(cascade=CascadeType.REFRESH)
	public List<Category> categories = new ArrayList<>();
	
	@Exclude
	@OneToMany(cascade=CascadeType.ALL,mappedBy="article")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	public List<Comment> comments = new ArrayList<>();
	
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Tag> tags = new ArrayList<>();

	@Override
	public String toString() {
		return title;
	}
	
}
