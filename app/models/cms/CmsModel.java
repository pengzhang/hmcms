package models.cms;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import annotations.Exclude;
import annotations.Hidden;
import annotations.SessionUser;
import models.BaseModel;
import models.cms.enumtype.Quality;
import models.cms.enumtype.Recommend;
/**
 * CMS Model
 * @author zp
 *
 */
@MappedSuperclass
public class CmsModel extends BaseModel {
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(50) comment '类型:0:普通,2:置顶'")
	public Recommend recommend = Recommend.common; 
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(50) comment '类型:0:普通,1:精华'")
	public Quality quality = Quality.common; 
	
	@Exclude
	@SessionUser
	@Column(columnDefinition = "varchar(100) comment '作者'")
	public String author = "";

	//计数部分
	@Hidden
	@Column(columnDefinition = "bigint comment '浏览总数'")
	public long view_total = 0;  //浏览总数

	@Hidden
	@Column(columnDefinition = "bigint comment '评论总数'")
	public long comment_total = 0; //评论总数

	@Hidden
	@Column(columnDefinition = "bigint comment '赞总数'")
	public long like_total = 0; //赞总数
}
