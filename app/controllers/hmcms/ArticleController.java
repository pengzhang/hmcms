package controllers.hmcms;

import java.util.List;

import annotations.Api;
import annotations.Api.HttpType;
import annotations.Param;
import annotations.Return;
import controllers.ActionIntercepter;
import models.hmcms.Article;
import play.mvc.Controller;
import play.mvc.With;
import plugins.router.Get;
import plugins.router.Gets;

@With({ActionIntercepter.class})
public class ArticleController extends Controller {
	
	@Api(name="获取文章列表", param= {@Param(clazz=int.class,name="page"),@Param(clazz=int.class,name="size")}, type = HttpType.GET, url = "/articles.json|xml", ret= {@Return(clazz=Article.class)})
	@Gets({@Get("/articles"),@Get("/articles.{format}")})
	public static void articleList(int page, int size) {
		
		List<Article> articles = Article.all().fetch(page, size);
		renderArgs.put("status", true);
		renderArgs.put("message", "获取文章信息成功");
		renderArgs.put("data", articles);
		
	}
	
	@Api(name="根据分类获取文章列表", param= {@Param(clazz=long.class,name="categoryId"),@Param(clazz=int.class,name="page"),@Param(clazz=int.class,name="size")}, type = HttpType.GET, url = "/articles/by/category.json|xml", ret= {@Return(clazz=Article.class)})
	@Gets({@Get("/articles/by/category"),@Get("/articles/by/category.{format}")})
	public static void articleByCategoryList(long categoryId, int page, int size) {
		
		List<Article> articles = Article.find("from Article a , Category c where c.id=?", categoryId).fetch(page, size);
		renderArgs.put("status", true);
		renderArgs.put("message", "获取文章信息成功");
		renderArgs.put("data", articles);
	}

}
