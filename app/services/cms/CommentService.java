package services.cms;

import org.springframework.stereotype.Component;

import models.cms.Comment;
import models.cms.UserLike;
import models.core.user.User;

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
