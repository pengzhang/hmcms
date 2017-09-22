package services.hmcms;

import java.util.List;

import org.springframework.stereotype.Component;

import models.hmcms.Comment;
import models.hmcms.Tag;
import models.hmcms.Video;
import models.hmcms.enumtype.Quality;
import models.hmcms.enumtype.Recommend;
import models.hmcore.user.User;
import play.db.jpa.JPA;

/**
 * 视频
 * @author zp
 *
 */
@Component
public class VideoService {
	
	public Comment addComment(long id, Comment comment, User user){
		Video video = Video.findById(id);
		comment.video = video;
		comment.user = user;
		comment.save();
		return comment;
	}
	
	public void addView(long id, long view_total){
		JPA.em().createQuery("update Video a set a.view_total=:total where a.id=:id").setParameter("total", ++view_total).setParameter("id", id).executeUpdate();
	}
	
	public List<Comment> getComments(long id, int page, int size){
		return Comment.find("select c from Video a left join a.comments c where a.id=? order by c.createDate desc", id).fetch(page, size);
	}
	
	public List<Video> getNewestList(int page, int size) {
		return Video.find("status=? order by updateDate desc",false).fetch(page, size);
	}
	
	public List<Video> videoByHot(int page, int size) {
		return Video.find("select a from Video a left join a.comments c where a.status=? group by a.id order by count(c.id) desc, a.updateDate desc",false).fetch(page, size);
	}
	
	public List<Video> videoByFocus(int page, int size) {
		return Video.find("recommend=? and quality=? and status=? order by updateDate desc", Recommend.recommend, Quality.quality, false).fetch(page, size);
	}

	public List<Video> videoByCategoryList(long categoryId, int page, int size) {
		return Video.find("select a from Video a left join a.categories c where c.id=? and a.status=?", categoryId, false).fetch(page, size);
	}

	public List<Video> videoByTagList(long tagId, int page, int size) {
		return Video.find("select a from Video a left join a.tags t where t.id=? and a.status=? order by a.updateDate desc", tagId, false).fetch(page, size);
	}
	
	public List<Video> getRecommendVideo(int max){
		return Video.find("recommend=? and status=?order by createDate desc", Recommend.recommend, false).fetch(max);
	}
	
	public List<Tag> getVideoTags(int max){
		return Tag.find("select t from Video a left join a.tags as t where a.status=? group by t.tag order by t.createDate desc", false).fetch(max);
	}
	
}
