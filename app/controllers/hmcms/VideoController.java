package controllers.hmcms;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import controllers.ActionIntercepter;
import controllers.BaseController;
import models.hmcms.Video;
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
public class VideoController extends BaseController {
	
	@Get("/video")
	public static void video(long id) {
		Video video = Video.findById(id);
		video.addView();
		render(video);
	}
	
	@Get("/video/like")
	public static void videoLike(long id) {
		//TODO 关联点赞表
		if(StringUtils.isNotEmpty(session.get("uid"))) {
			Video video = Video.findById(id);
			video.like_total += 1;
			video.save();
			renderJSON(ResponseData.response(true, "点赞成功"));
		}else {
			renderJSON(ResponseData.response(false, "请登录后点赞"));
		}
	}
	
	@Post("/video/add/comment")
	public static void addComment(long id, Comment comment) {
		Video video = Video.findById(id);
		comment = video.addComment(comment,currentUser());
		render("/hmcms/VideoController/sectionComment.html",comment);
	}
	
	@Get("/video/get/comments")
	public static void getCommentList(long id, int page, int size) {
		Video video = Video.findById(id);
		List<Comment> comments = video.getComments(page, size);
		render("/hmcms/VideoController/sectionCommentList.html", video, comments, page, size);
		
	}

	@Get("/videos")
	public static void videoList(int page, int size, int ajax) {
		List<Video> videos = Video.find("status=? order by updateDate desc",false).fetch(page, size);
		if(ajax == 1) {
			render("/hmcms/VideoController/sectionVideos.html",videos,page,size);
		}
		render(videos,page,size);
	}
	
	@Get("/videos/hot")
	public static void videoByHot(int page, int size) {
		List<Video> videos = Video.find("select a from Video a left join a.comments c where a.status=? group by a.id order by count(c.id) desc, a.updateDate desc",false).fetch(page, size);
		page = page + 2;
		render("/hmcms/VideoController/sectionVideos.html", videos, page, size);
	}
	
	@Get("/videos/focus")
	public static void videoByFocus(int page, int size) {
		List<Video> videos = Video.find("recommend=? and quality=? and status=? order by updateDate desc", Recommend.recommend, Quality.quality, false).fetch(page, size);
		render("/hmcms/VideoController/sectionVideos.html", videos, page, size);
	}

	@Get("/videos/by/category")
	public static void videoByCategoryList(long categoryId, int page, int size) {
		List<Video> videos = Video.find("select a from Video a left join a.categories c where c.id=? and a.status=?", categoryId, false).fetch(page, size);
		render(videos, categoryId, page, size);
	}

	@Get("/videos/by/tag")
	public static void videoByTagList(long tagId, int page, int size, int ajax) {
		Tag tag = Tag.findById(tagId);
		List<Video> videos = Video.find("select a from Video a left join a.tags t where t.id=? and a.status=? order by a.updateDate desc", tagId, false).fetch(page, size);
		if(ajax == 1) {
			render("/hmcms/VideoController/sectionVideos.html", videos, tagId, tag, page, size);
		}
		render(videos, tagId, tag, page, size);
	}
	
	@Before
	private static void nav() {
		renderArgs.put("nav", "video");
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
		List<Tag> tags = (List<Tag>) Cache.get("video_tags");
		if(tags == null) {
			tags = Tag.find("select t from Video a left join a.tags as t where a.status=? group by t.tag order by t.createDate desc", false).fetch(10);
			Cache.set("video_tags", tags, "1h");
		}
		renderArgs.put("video_tags", tags);
	}
	
	@Before(only= {"video","videoList"})
	private static void getRecommendVideos(){
		List<Video> recommendVideos = (List<Video>) Cache.get("video_recommends"); 
		if(recommendVideos == null) {
			recommendVideos = Video.find("recommend=? and status=?order by createDate desc", Recommend.recommend, false).fetch(3);
			Cache.set("video_recommends", recommendVideos, "1h");
		}
		renderArgs.put("video_recommends", recommendVideos);
	}
	

}
