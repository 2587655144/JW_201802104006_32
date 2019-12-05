package login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/showCookies")
public class CookieServlet extends HttpServlet {
    /**
     * 通过读取cookie对象间接验证是否创建了session
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //得到所有cookie信息
        Cookie[] allCookies = req.getCookies();
        if (allCookies == null){
            resp.getWriter().println("no cookies available");
        }else {
            //如果有cookie,则把name和value显示到客户端
            for (Cookie cookie:allCookies){
                resp.getWriter().println(cookie.getName() + "=" + cookie.getValue());
            }
        }
    }
}
