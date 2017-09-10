package models.hmcms;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import annotations.Upload;
import models.hmcms.enumtype.Open;
import models.hmcms.enumtype.Quality;
import models.hmcms.enumtype.Recommend;
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
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(50) comment '类型:0:私有,1:公开'")
	public Open open = Open.common;
	
	public Article() {
		super();
	}

	@Override
	public String toString() {
		return title;
	}
	
}
