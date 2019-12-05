package cn.controller;

import cn.domain.User;
import cn.service.UserService;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login.ctl")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //创建JSON对象message,以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            User loggedUser = UserService.getInstance().login(username,password);
            //登陆时在对话中保存User对象
            //若用户名和密码匹配，则得到一个代表当前用户的User对象
            if (loggedUser != null){
                message.put("message","登陆成功");
                //返回与当前请求关联的session对象
                HttpSession session = req.getSession();
                //10分钟没有操作，则使session失效（session中所有属性对象被删除）
                session.setMaxInactiveInterval(10 * 60);
                //将User对象写入session的属性，名称为currentUser,以便在其他请求中使用
                session.setAttribute("currentUser",loggedUser);
                resp.getWriter().println(message);
                //此处应重定向到索引页（菜单页）
                return;
            }else {
                message.put("message","用户名或密码错误");
                resp.getWriter().println(message);
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            e.printStackTrace();
        } catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        resp.getWriter().println(message);
    }
}
