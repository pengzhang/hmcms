package controllers.hmcms;

import annotations.Check;
import annotations.For;
import annotations.Login;
import controllers.AdminActionIntercepter;
import controllers.CRUD;
import controllers.Secure;
import models.hmcms.Video;
import play.mvc.With;

@Login
@Check("")
@For(Video.class)
@With({AdminActionIntercepter.class,Secure.class})
public class AdminVideos extends CRUD {

}
