package controllers.hmcms;

import controllers.ActionIntercepter;
import models.hmcms.Article;
import models.hmcms.Comment;
import models.hmcore.user.User;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import plugins.router.Post;

@With({ActionIntercepter.class})
public class CommentController extends Controller {
	
	@Post("/create/comment")
	public static void createComment(long articleId, Comment comment) {
		comment.user = User.findById(Long.parseLong(session.get("uid")));
		Article article = Article.findById(articleId);
		article.comments.add(comment);
		article.comment_total += 1;
		article.save();
		render("/hmcms/ArticleController/sectionComment.html",comment);
	}
	
}
