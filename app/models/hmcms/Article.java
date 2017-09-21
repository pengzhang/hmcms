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
@Component
@Table(name="cms_article")
@org.hibernate.annotations.Table(comment = "文章管理", appliesTo = "cms_article")
public class Article extends CmsModel implements Serializable {

	//文章部分
	
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
	
	public Article() {
		super();
	}
	
	public Comment addComment(Comment comment, User user){
		comment.addArticleComment(this, user);
		return comment;
	}
	
	public void addView(){
		JPA.em().createQuery("update Article a set a.view_total=:total where a.id=:id").setParameter("total", ++this.view_total).setParameter("id", this.id).executeUpdate();
	}
	
	public List<Article> getNewestList(int page, int size) {
		return Article.find("status=? order by updateDate desc",false).fetch(page, size);
	}
	
	public List<Article> articleByHot(int page, int size) {
		return Article.find("select a from Article a left join a.comments c where a.status=? group by a.id order by count(c.id) desc, a.updateDate desc",false).fetch(page, size);
	}
	
	public List<Article> articleByFocus(int page, int size) {
		return Article.find("recommend=? and quality=? and status=? order by updateDate desc", Recommend.recommend, Quality.quality, false).fetch(page, size);
	}

	public List<Article> articleByCategoryList(long categoryId, int page, int size) {
		return Article.find("select a from Article a left join a.categories c where c.id=? and a.status=?", categoryId, false).fetch(page, size);
	}

	public List<Article> articleByTagList(long tagId, int page, int size) {
		return Article.find("select a from Article a left join a.tags t where t.id=? and a.status=? order by a.updateDate desc", tagId, false).fetch(page, size);
	}
	
	public List<Comment> getComments(int page, int size){
		return Comment.find("select c from Article a left join a.comments c where a.id=? order by c.createDate desc", this.id).fetch(page, size);
	}
	
	public List<Article> getArticleRecommend(int max){
		return Article.find("recommend=? and status=?order by createDate desc", Recommend.recommend, false).fetch(max);
	}
	
	public List<Tag> getArticleTags(int max){
		 return Tag.find("select t from Article a left join a.tags as t where a.status=? group by t.tag order by t.createDate desc", false).fetch(max);
	}

	@Override
	public String toString() {
		return title;
	}
	
}
