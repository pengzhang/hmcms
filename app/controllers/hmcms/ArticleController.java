package controllers.hmcms;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import controllers.ActionIntercepter;
import controllers.BaseController;
import models.hmcms.Article;
import models.hmcms.Comment;
import models.hmcms.Tag;
import models.hmcms.enumtype.Quality;
import models.hmcms.enumtype.Recommend;
import models.hmcore.common.ResponseData;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.With;
import plugins.router.Get;
import plugins.router.Post;

@With({ActionIntercepter.class})
public class ArticleController extends BaseController {
	
	@Inject
	static Article service;
	
	@Get("/article")
	public static void article(long id) {
		Article article = Article.findById(id);
		article.addView();
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
		Article article = Article.findById(id);
		comment = article.addComment(comment,currentUser());
		render("/hmcms/ArticleController/sectionComment.html",comment);
	}
	
	@Get("/article/get/comments")
	public static void getCommentList(long id, int page, int size) {
		Article article = Article.findById(id);
		List<Comment> comments = article.getComments(page, size);
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
	private static void nav() {
		renderArgs.put("nav", "article");
	}
	
	@Before
	private static void page() {
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
			tags = service.getArticleTags(10);
			Cache.set("article_tags", tags, "1h");
		}
		renderArgs.put("article_tags", tags);
	}
	
	@Before(only= {"article","articleList"})
	private static void getRecommendArticles(){
		List<Article> recommendArticles = (List<Article>) Cache.get("article_recommends"); 
		if(recommendArticles == null) {
			recommendArticles = service.getArticleRecommend(3);
			Cache.set("article_recommends", recommendArticles, "1h");
		}
		renderArgs.put("article_recommends", recommendArticles);
	}

}
