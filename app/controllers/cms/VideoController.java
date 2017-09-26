package controllers.cms;

import java.util.List;

import javax.inject.Inject;

import annotations.DefaultPageParam;
import annotations.Login;
import controllers.ActionIntercepter;
import controllers.BaseController;
import models.cms.Comment;
import models.cms.Tag;
import models.cms.Video;
import models.core.common.ResponseData;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.With;
import plugins.router.Get;
import plugins.router.Post;
import services.cms.VideoService;

@Login(only= {"videoLike", "addComment"})
@With({ActionIntercepter.class})
public class VideoController extends BaseController {
	
	@Inject
	static VideoService service;
	
	@Get("/video")
	public static void video(long id) {
		Video video = Video.findById(id);
		service.addView(id, video.view_total);
		render(video);
	}
	
	@Get("/video/like")
	public static void videoLike(long id) {
		Video video = Video.findById(id);
		if(service.checkUserLike(video, currentUser())) {
			service.addLike(video, currentUser());
			renderJSON(ResponseData.response(true, "点赞成功"));
		}else {
			renderJSON(ResponseData.response(true, "已点过赞"));
		}
	}
	
	@Post("/video/add/comment")
	public static void addComment(long id, Comment comment) {
		comment = service.addComment(id, comment,currentUser());
		render("/cms/VideoController/sectionComment.html",comment);
	}
	
	@DefaultPageParam
	@Get("/video/get/comments")
	public static void getCommentList(long id, int page, int size) {
		Video video = Video.findById(id);
		List<Comment> comments = service.getComments(id, page, size);
		render("/cms/VideoController/sectionCommentList.html", video, comments, page, size);
		
	}

	@DefaultPageParam
	@Get("/videos")
	public static void videoList(int page, int size) {
		
		List<Video> videos = service.getNewestList(page, size);
		if(request.isAjax()) {
			render("/cms/VideoController/sectionVideos.html",videos,page,size);
		}
		render(videos,page,size);
	}

	@DefaultPageParam
	@Get("/videos/hot")
	public static void videoByHot(int page, int size) {
		List<Video> videos = service.videoByHot(page, size);
		page = page + 2;
		render("/cms/VideoController/sectionVideos.html", videos, page, size);
	}
	
	@DefaultPageParam
	@Get("/videos/focus")
	public static void videoByFocus(int page, int size) {
		List<Video> videos = service.videoByFocus(page, size);
		render("/cms/VideoController/sectionVideos.html", videos, page, size);
	}

	@DefaultPageParam
	@Get("/videos/by/category")
	public static void videoByCategoryList(long categoryId, int page, int size) {
		List<Video> videos = service.videoByCategoryList(categoryId, page, size);
		render(videos, categoryId, page, size);
	}

	@DefaultPageParam
	@Get("/videos/by/tag")
	public static void videoByTagList(long tagId, int page, int size) {
		Tag tag = Tag.findById(tagId);
		List<Video> videos = service.videoByTagList(tagId, page, size);
		if(request.isAjax()) {
			render("/cms/VideoController/sectionVideos.html", videos, tagId, tag, page, size);
		}
		render(videos, tagId, tag, page, size);
	}
	
	@Before
	static void nav() {
		renderArgs.put("nav", "video");
	}
	
	@Before()
	static void getTags(){
		List<Tag> tags = (List<Tag>) Cache.get("video_tags");
		if(tags == null) {
			tags = service.getVideoTags(10);
			Cache.set("video_tags", tags, "1h");
		}
		renderArgs.put("video_tags", tags);
	}
	
	@Before(only= {"video","videoList"})
	static void getRecommendVideos(){
		List<Video> recommendVideos = (List<Video>) Cache.get("video_recommends"); 
		if(recommendVideos == null) {
			recommendVideos = service.getRecommendVideo(3);
			Cache.set("video_recommends", recommendVideos, "1h");
		}
		renderArgs.put("video_recommends", recommendVideos);
	}
	
	@Before(only="video")
	static void getTop10Comment() {
		String id = request.params.get("id");
		List<Comment> comments = service.getComments(Long.parseLong(id), 1, 10);
		renderArgs.put("comments", comments);
	}

}
