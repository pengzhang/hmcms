package controllers.hmcms;

import java.util.List;

import javax.inject.Inject;

import annotations.DefaultPageParam;
import annotations.Login;
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

@Login(only= {"articleLike", "addComment"})
@With({ActionIntercepter.class})
public class ArticleController extends BaseController {
	
	@Inject
	static ArticleService service;
	
	@Get("/article")
	public static void article(long id) {
		Article article = Article.findById(id);
		service.addView(id, article.view_total);
		render(article);
	}
	
	@Get("/article/like")
	public static void articleLike(long id) {
		Article article = Article.findById(id);
		if(service.checkUserLike(article, currentUser())) {
			service.addLike(article, currentUser());
			renderJSON(ResponseData.response(true, "点赞成功"));
		}else {
			renderJSON(ResponseData.response(true, "已点过赞"));
		}
		
	}
	
	@Post("/article/add/comment")
	public static void addComment(long id, Comment comment) {
		comment = service.addComment(id, comment,currentUser());
		render("/hmcms/ArticleController/sectionComment.html",comment);
	}
	
	@DefaultPageParam
	@Get("/article/get/comments")
	public static void getCommentList(long id, int page, int size) {
		Article article = Article.findById(id);
		List<Comment> comments = service.getComments(id, page, size);
		render("/hmcms/ArticleController/sectionCommentList.html", article, comments, page, size);
		
	}

	@DefaultPageParam
	@Get("/articles")
	public static void articleList(int page, int size) {
		List<Article> articles = service.getNewestList(page, size);
		if(request.isAjax()) {
			render("/hmcms/ArticleController/sectionArticles.html",articles,page,size);
		}
		render(articles,page,size);
	}
	
	@DefaultPageParam
	@Get("/articles/hot")
	public static void articleByHot(int page, int size) {
		List<Article> articles = service.articleByHot(page, size);
		page = page + 2;
		render("/hmcms/ArticleController/sectionArticles.html", articles, page, size);
	}
	
	@DefaultPageParam
	@Get("/articles/focus")
	public static void articleByFocus(int page, int size) {
		List<Article> articles =  service.articleByFocus(page, size);
		render("/hmcms/ArticleController/sectionArticles.html", articles, page, size);
	}

	@DefaultPageParam
	@Get("/articles/by/category")
	public static void articleByCategoryList(long categoryId, int page, int size) {
		List<Article> articles = service.articleByCategoryList(categoryId, page, size);
		render(articles, categoryId, page, size);
	}

	@DefaultPageParam
	@Get("/articles/by/tag")
	public static void articleByTagList(long tagId, int page, int size) {
		Tag tag = Tag.findById(tagId);
		List<Article> articles = service.articleByTagList(tagId, page, size);
		if(request.isAjax()) {
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
