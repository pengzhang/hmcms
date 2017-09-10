package models.hmcms;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import annotations.Exclude;
import annotations.Hidden;
import annotations.Upload;
import models.BaseModel;
import play.data.validation.MaxSize;

/**
 * 文章
 * @author zp
 *
 */
@Entity
@Table(name="article")
@org.hibernate.annotations.Table(comment = "文章管理", appliesTo = "article")
public class Article extends CmsModel implements Serializable {

	//文章部分
	
	@Column(columnDefinition = "varchar(500) comment '文章标题'")
	public String title;
	
	@MaxSize(value=50000)
	@Column(columnDefinition = "text comment '文章内容'")
	public String content;
	
	@Upload
	@Column(columnDefinition = "varchar(500) comment '封面图'")
	public String cover;
	
	@Column(columnDefinition = "varchar(100) comment '作者'")
	public String author;
	
	@Exclude
	@Column(columnDefinition = "tinyint default 0 comment '类型:0:普通,2:置顶'")
	public boolean recommend = false; 
	
	@Exclude
	@Column(columnDefinition = "tinyint default 1 comment '类型:0:私有,1:公开'")
	public boolean open = true; 
	
	@Exclude
	@Column(columnDefinition = "tinyint default 0 comment '类型:0:普通,1:精华'")
	public boolean quality = false; 
	
	public Article() {
		super();
	}

	@Override
	public String toString() {
		return title;
	}
	
}
