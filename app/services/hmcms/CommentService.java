package services.hmcms;

import org.springframework.stereotype.Component;

import models.hmcms.Comment;
import models.hmcms.UserLike;
import models.hmcore.user.User;

/**
 * 评论
 * @author zp
 *
 */
@Component
public class CommentService {

	public UserLike addLike(long id, User user) {
		return new UserLike().addComment(Comment.findById(id)).addUser(user).save();
	}
}
