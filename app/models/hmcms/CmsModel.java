package models.hmcms;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import annotations.Hidden;
import models.BaseModel;
/**
 * CMS Model
 * @author zp
 *
 */
@MappedSuperclass
public class CmsModel extends BaseModel {

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
