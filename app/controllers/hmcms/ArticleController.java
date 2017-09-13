package controllers.hmcms;

import java.util.List;

import com.qiniu.util.Json;

import controllers.ActionIntercepter;
import models.hmcms.Article;
import models.hmcms.Tag;
import models.hmcms.enumtype.Quality;
import models.hmcms.enumtype.Recommend;
import play.Logger;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import plugins.router.Get;

@With({ActionIntercepter.class})
public class ArticleController extends Controller {
	
	@Get("/article")
	public static void article(long id) {
		Article article = Article.findById(id);
		render(article);
	}

	@Get("/articles")
	public static void articleList(int page, int size) {
		List<Article> articles = Article.find("order by createDate desc").fetch(page, size);
		render(articles,page,size);
	}
	
	@Get("/articles/new")
	public static void articleByNew(int page, int size) {
		List<Article> articles = Article.find("order by createDate desc").fetch(page, size);
		render("/hmcms/ArticleController/sectionArticles.html",articles,page,size);
	}
	
	@Get("/articles/hot")
	public static void articleByHot(int page, int size) {
		List<Article> articles = Article.find("select a from Article a left join a.comments c group by a.id order by count(c.id) desc, a.createDate desc").fetch(page, size);
		page = page + 2;
		render("/hmcms/ArticleController/sectionArticles.html",articles, page, size);
	}
	
	@Get("/articles/focus")
	public static void articleByFocus(int page, int size) {
		List<Article> articles = Article.find("recommend=? and quality=? order by createDate desc", Recommend.recommend, Quality.quality).fetch(page, size);
		render("/hmcms/ArticleController/sectionArticles.html",articles, page, size);
	}

	@Get("/articles/by/category")
	public static void articleByCategoryList(long categoryId, int page, int size) {
		List<Article> articles = Article.find("select a from Article a left join a.categories c where c.id=?", categoryId).fetch(page, size);
		render(articles, categoryId, page, size);
	}

	@Get("/articles/by/tag")
	public static void articleByTagList(long tagId, int page, int size) {
		List<Article> articles = Article.find("select a from Article a left join a.tags t where t.id=?", tagId).fetch(page, size);
		render(articles, tagId, page, size);
	}
	
	@Before
	public static void page() {
		String page = request.params.get("page");
		if(page == null) {
			request.params.put("page", "1");
		}else if(Integer.parseInt(page) < 1) {
			request.params.put("page", "1");
		}
		
		String size = request.params.get("size");
		if(size == null) {
			request.params.put("size", "10");
		}
	}
	
	@Before()
	private static void getTags(){
		List<Tag> tags = (List<Tag>) Cache.get("article_tags");
		if(tags == null) {
			tags = Tag.find("select t from Article a left join a.tags as t group by t.tag order by t.createDate desc").fetch(10);
			Cache.set("article_tags", tags);
		}
		renderArgs.put("article_tags", tags);
	}
	
	@Before(unless="article")
	private static void getRecommendArticles(){
		List<Article> recommendArticles = (List<Article>) Cache.get("article_recommends"); 
		if(recommendArticles == null) {
			recommendArticles = Article.find("recommend=? order by createDate desc", Recommend.recommend).fetch(3);
		}
		renderArgs.put("article_recommends", recommendArticles);
	}

}
