package crm06.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import crm06.response.BaseResponse;
import crm06.service.UserService;

@WebServlet(name = "userApiController", urlPatterns = { "/api/user" })
public class UserApiController extends HttpServlet {

	UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idUser = Integer.parseInt(req.getParameter("id"));

		BaseResponse baseResponse = new BaseResponse();
		boolean isDeleted = userService.deleteUser(idUser);

		baseResponse.setStatusCode(isDeleted ? 200 : 500);
		baseResponse.setMessage(isDeleted ? "User deleted successfully" : "Failed to delete user");
		baseResponse.setData(isDeleted);

		Gson gson = new Gson();
		String jsonData = gson.toJson(baseResponse);

		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.append(jsonData);
	}
}
