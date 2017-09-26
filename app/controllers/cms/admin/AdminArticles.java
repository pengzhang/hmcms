package controllers.cms.admin;

import annotations.Check;
import annotations.For;
import annotations.Login;
import controllers.AdminActionIntercepter;
import controllers.CRUD;
import controllers.Secure;
import models.cms.Article;
import play.mvc.With;

@Login
@Check("")
@For(Article.class)
@With({AdminActionIntercepter.class,Secure.class})
public class AdminArticles extends CRUD {

}
