package controllers.hmcms.admin;

import annotations.Check;
import annotations.For;
import annotations.Login;
import controllers.AdminActionIntercepter;
import controllers.CRUD;
import controllers.Secure;
import models.hmcms.Article;
import models.hmcore.adminuser.AdminUser;
import play.mvc.With;

@Login
@Check("")
@For(Article.class)
@With({AdminActionIntercepter.class,Secure.class})
public class AdminArticles extends CRUD {

}
