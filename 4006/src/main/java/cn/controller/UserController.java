package cn.controller;

import cn.domain.User;
import cn.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        //设置请求字符编码为UTF-8
        //request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        User userToAdd = JSON.parseObject(degree_json, User.class);
        System.out.println(userToAdd);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Degree对象
        try {
            UserService.getInstance().add(userToAdd);
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request.setCharacterEncoding("UTF-8");
        String user_json = JSONUtil.getJSON(request);
        User userToAdd = JSON.parseObject(user_json, User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            UserService.getInstance().update(userToAdd);
            message.put("message", "更改成功");
        }catch (SQLException e){
            e.printStackTrace();
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //response.setContentType("html/text;charset=UTF8");
        //response.getWriter().println(message);
        //login
        /*response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JSONObject message = new JSONObject();
        User user = null;
        try {
            user = UserService.getInstance().login(username,password);
            if (user != null){

                message.put("message","登录成功");
                response.getWriter().println(message);
            }else {

                message.put("message","账户或密码错误");
                response.getWriter().println(message);
            }

        } catch (SQLException e) {

            message.put("message", "数据库操作异常");
            e.printStackTrace();
            response.getWriter().println(message);
        } catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }*/
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
           UserService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            e.printStackTrace();
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            e.printStackTrace();
            message.put("message", "网络异常");
        }

        //响应
        //response.setContentType("html/text;charset=UTF8");
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseUsers(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseUser(id, response);
            }
            message.put("message", "查询成功");
        }catch (SQLException e){
            e.printStackTrace();
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            e.printStackTrace();
            message.put("message", "网络异常");
        }
    }
    //响应一个学位对象
    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        User user = UserService.getInstance().find(id);
        String user_json = JSON.toJSONString(user);
        //响应
        response.getWriter().println(user_json);
    }
    //响应所有学位对象
    private void responseUsers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<User> users = UserService.getInstance().findAll();
        String users_json = JSON.toJSONString(users);
        //响应
        response.getWriter().println(users_json);
    }
}
