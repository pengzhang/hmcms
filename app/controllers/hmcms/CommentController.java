package controllers.hmcms;

import controllers.ActionIntercepter;
import models.hmcms.Article;
import models.hmcms.Comment;
import play.mvc.Controller;
import play.mvc.With;
import plugins.router.Post;

@With({ActionIntercepter.class})
public class CommentController extends Controller {
	
	@Post("/create/comment")
	public static void createComment(long articleId, Comment comment) {
		Article article = Article.findById(articleId);
		article.comments.add(comment);
		article.save();
	}

}
