package filter;

import javax.servlet.*;
import javax.servlet.annotation.*;

import java.io.IOException;

@WebFilter(filterName = "EmployeeFilter",urlPatterns = {"/acceptorder","/addfood","/order-list-manager","/food-add.jsp"})
public class EmployeeFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(request, response);
        System.out.println("ảo");
    }
}
