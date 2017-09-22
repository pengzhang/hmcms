package controllers.hmcms;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import controllers.ActionIntercepter;
import controllers.BaseController;
import models.hmcms.Article;
import models.hmcms.Comment;
import models.hmcms.Tag;
import models.hmcore.common.ResponseData;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.With;
import plugins.router.Get;
import plugins.router.Post;
import services.hmcms.ArticleService;

@With({ActionIntercepter.class})
public class ArticleController extends BaseController {
	
	@Inject
	static ArticleService service;
	
	@Get("/article.{format}")
	public static void article(long id) {
		Article article = Article.findById(id);
		service.addView(id, article.view_total);
		if(request.format.equals("json")) {
			renderJSON(article);
		}else if(request.format.equals("xml")) {
			renderXml(article);
		}
		render(article);
	}
	
	@Get("/article/like")
	public static void articleLike(long id) {
		//TODO 关联点赞表
		if(StringUtils.isNotEmpty(session.get("uid"))) {
			Article article = Article.findById(id);
			article.like_total += 1;
			article.save();
			renderJSON(ResponseData.response(true, "点赞成功"));
		}else {
			renderJSON(ResponseData.response(false, "请登录后点赞"));
		}
	}
	
	@Post("/article/add/comment")
	public static void addComment(long id, Comment comment) {
		comment = service.addComment(id, comment,currentUser());
		render("/hmcms/ArticleController/sectionComment.html",comment);
	}
	
	@Get("/article/get/comments")
	public static void getCommentList(long id, int page, int size) {
		Article article = Article.findById(id);
		List<Comment> comments = service.getComments(id, page, size);
		render("/hmcms/ArticleController/sectionCommentList.html", article, comments, page, size);
		
	}

	@Get("/articles")
	public static void articleList(int page, int size, int ajax) {
		List<Article> articles = service.getNewestList(page, size);
		if(ajax == 1) {
			render("/hmcms/ArticleController/sectionArticles.html",articles,page,size);
		}
		render(articles,page,size);
	}
	
	@Get("/articles/hot")
	public static void articleByHot(int page, int size) {
		List<Article> articles = service.articleByHot(page, size);
		page = page + 2;
		render("/hmcms/ArticleController/sectionArticles.html", articles, page, size);
	}
	
	@Get("/articles/focus")
	public static void articleByFocus(int page, int size) {
		List<Article> articles =  service.articleByFocus(page, size);
		render("/hmcms/ArticleController/sectionArticles.html", articles, page, size);
	}

	@Get("/articles/by/category")
	public static void articleByCategoryList(long categoryId, int page, int size) {
		List<Article> articles = service.articleByCategoryList(categoryId, page, size);
		render(articles, categoryId, page, size);
	}

	@Get("/articles/by/tag")
	public static void articleByTagList(long tagId, int page, int size, int ajax) {
		Tag tag = Tag.findById(tagId);
		List<Article> articles = service.articleByTagList(tagId, page, size);
		if(ajax == 1) {
			render("/hmcms/ArticleController/sectionArticles.html", articles, tagId, tag, page, size);
		}
		render(articles, tagId, tag, page, size);
	}
	
	@Before
	static void nav() {
		renderArgs.put("nav", "article");
	}
	
	@Before()
	static void getTags(){
		List<Tag> tags = (List<Tag>) Cache.get("article_tags");
		if(tags == null) {
			tags = service.getArticleTags(10);
			Cache.set("article_tags", tags, "1h");
		}
		renderArgs.put("article_tags", tags);
	}
	
	@Before(only= {"article","articleList"})
	static void getRecommendArticles(){
		List<Article> recommendArticles = (List<Article>) Cache.get("article_recommends"); 
		if(recommendArticles == null) {
			recommendArticles = service.getArticleRecommend(3);
			Cache.set("article_recommends", recommendArticles, "1h");
		}
		renderArgs.put("article_recommends", recommendArticles);
	}
	
	@Before(only="article")
	static void getTop10Comment() {
		String id = request.params.get("id");
		List<Comment> comments = service.getComments(Long.parseLong(id), 1, 10);
		renderArgs.put("comments", comments);
	}

}
