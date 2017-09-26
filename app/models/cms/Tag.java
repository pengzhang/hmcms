package models.cms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import models.BaseModel;

@Entity
@Table(name="cms_tag")
@org.hibernate.annotations.Table(comment = "标签管理", appliesTo = "cms_tag")
public class Tag extends BaseModel {
	
	@Column(columnDefinition="varchar(255) comment '标签名称'")
	public String tag;
	
	@Column(columnDefinition="varchar(50) comment '标签类型'")
	public String color;
	
	@Override
	public String toString() {
		return tag;
	}
	
}
