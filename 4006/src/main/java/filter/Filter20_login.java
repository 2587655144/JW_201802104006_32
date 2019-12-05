package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter 20",urlPatterns = "/*")
public class Filter20_login implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter 20 begins");
        HttpServletRequest request1 = (HttpServletRequest)request;
        HttpServletResponse response1 = (HttpServletResponse)response;
        //如果当前请求对应着服务器内存中的一个session对象，则返回该对象
        //如果服务器内存中没有session对象与当前请求对应，则返回null
        HttpSession session = request1.getSession(false);
        String path = request1.getRequestURI();
        if (path.contains("/login.ctl") || path.contains("/logout.ctl")){
            //放行
            chain.doFilter(request,response);
            System.out.println("Filter 20 ends");
            //User对象不为空
        } else if (session != null && session.getAttribute("currentUser") != null){
            chain.doFilter(request,response);
            System.out.println("Filter 20 ends");
        } else {
            response1.getWriter().println("您没有登陆，请登录");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
