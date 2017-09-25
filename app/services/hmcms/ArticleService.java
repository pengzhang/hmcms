package services.hmcms;

import java.util.List;

import org.springframework.stereotype.Component;

import models.hmcms.Article;
import models.hmcms.Comment;
import models.hmcms.Tag;
import models.hmcms.UserLike;
import models.hmcms.Video;
import models.hmcms.enumtype.Quality;
import models.hmcms.enumtype.Recommend;
import models.hmcore.user.User;
import play.db.jpa.JPA;

/**
 * 文章服务层
 * @author zp
 *
 */
@Component
public class ArticleService {
	
	public Comment addComment(long id, Comment comment, User user){
		return comment.addArticle(Article.findById(id)).addUser(user).save();
	}
	
	public UserLike addLike(Article article, User user) {
		return new UserLike().addArticle(article).addUser(user).save();
	}
	
	public boolean checkUserLike(Article article, User user) {
		return UserLike.find("article=? and user=?", article, user).first() == null;
	}
	
	public void addView(long id, long view_total){
		JPA.em().createQuery("update Article a set a.view_total=:total where a.id=:id").setParameter("total", ++view_total).setParameter("id", id).executeUpdate();
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
	
	public List<Comment> getComments(long id, int page, int size){
		return Comment.find("select c from Article a left join a.comments c where a.id=? order by c.createDate desc", id).fetch(page, size);
	}
	
	public List<Article> getArticleRecommend(int max){
		return Article.find("recommend=? and status=?order by createDate desc", Recommend.recommend, false).fetch(max);
	}
	
	public List<Tag> getArticleTags(int max){
		 return Tag.find("select t from Article a left join a.tags as t where a.status=? group by t.tag order by t.createDate desc", false).fetch(max);
	}
}
