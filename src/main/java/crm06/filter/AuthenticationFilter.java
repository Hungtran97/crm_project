package crm06.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.service.LoginService;

// urlPatterns: Duong dan client goi filter se kich hoat
@WebFilter(filterName = "authenFilter", urlPatterns = { "/user-add", "/profile", "/login", "/api/project", "/api/user",
		"/api/task", "/groupwork-add", "/task-add", "/role-add" })
public class AuthenticationFilter implements Filter {
	LoginService loginService = new LoginService();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)

			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String path = req.getServletPath();
		String context = req.getContextPath();

		switch (path) {
		case "/api/project":
		case "/api/user":
		case "/api/task":
		case "/groupwork-add":
		case "/task-add":
		case "/user-add":
		case "/role-add":
			if (checkLogin(req, resp)) {
				if (checkPermission(req, resp)) {
					chain.doFilter(request, response);
				} else {
					resp.sendRedirect(context + "/404");
				}
			} else {
				Cookie prevPath = new Cookie("prevPath", path);
				prevPath.setMaxAge(60 * 60);
				resp.addCookie(prevPath);
			}
			break;
		case "/profile":
			if (checkLogin(req, resp)) {
				chain.doFilter(request, response);
			} else {
				Cookie prevPath = new Cookie("prevPath", path);
				prevPath.setMaxAge(60 * 60);
				resp.addCookie(prevPath);
				resp.sendRedirect(context + "/login");
			}
			break;
		case "/login":
			if (checkLogin(req, resp)) {
				resp.sendRedirect(context);
			} else {
				chain.doFilter(request, response);
			}
			break;
		default:
			System.out.println("loi login");
		}

	}

	private boolean checkPermission(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = req.getServletPath();
		Cookie[] cookies = req.getCookies();
		String email = "";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("userEmail")) {
					email = cookies[i].getValue();
					break;
				}
			}

		}

		return loginService.userIsPermission(email, path);
	}

	private boolean checkLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean isLogin = false;
		Cookie[] cookies = req.getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("isLogin") && cookies[i].getValue().equals("true")) {
					isLogin = true;
					break;
				}
			}
		}

		return isLogin;
	}
}
