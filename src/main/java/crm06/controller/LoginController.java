package crm06.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.service.LoginService;

@WebServlet(name = "loginController", urlPatterns = { "/login" })

public class LoginController extends HttpServlet {
	private LoginService loginService = new LoginService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String prevPath = "";
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equalsIgnoreCase("prevPath")) {
						prevPath = cookies[i].getValue();
						break;
					}
				}
			}

			String email = req.getParameter("email");
			String password = req.getParameter("password");
			boolean checkUserExist = loginService.userIsExist(email, password);

			String context = req.getContextPath();
			if (checkUserExist) {
				int timeLife = 60 * 60 * 24;
				Cookie isLogin = new Cookie("isLogin", "true");
				Cookie userEmail = new Cookie("userEmail", email);
				Cookie userPasword = new Cookie("userPasword", password);

				isLogin.setMaxAge(timeLife);
				userEmail.setMaxAge(timeLife);
				userPasword.setMaxAge(timeLife);
				resp.addCookie(isLogin);
				resp.addCookie(userEmail);
				resp.addCookie(userPasword);

				if (prevPath.isBlank()) {
					resp.sendRedirect(context);

				} else {
					resp.sendRedirect(context + prevPath);

				}

			} else {
				System.out.println("Dang nhap that bai");
				resp.sendRedirect(context + "/login");
			}

		} catch (Exception e) {
			System.out.println("Loi login" + e.getMessage());
		}
	}
}
