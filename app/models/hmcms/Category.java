package models.hmcms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import models.BaseModel;

@Entity
@Table(name="cms_category")
@org.hibernate.annotations.Table(comment = "分类管理", appliesTo = "cms_category")
public class Category extends BaseModel {
	
	@OneToOne
	@JoinColumn(name="pid", columnDefinition="bigint default 0 comment '父ID'")
	public Category parent;
	
	@Column(columnDefinition="varchar(255) comment '分类名称'")
	public String category;
	
	@Column(name="category_type", columnDefinition="varchar(50) comment '分类类型'")
	public int categoryType = 0;
	
	@Override
	public String toString() {
		return category;
	}
	
	
}
