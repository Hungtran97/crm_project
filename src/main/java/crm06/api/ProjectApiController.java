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
import crm06.service.ProjectService;

@WebServlet(name = "projectApiController", urlPatterns = { "/api/project" })
public class ProjectApiController extends HttpServlet {

	ProjectService projectService = new ProjectService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idProject = Integer.parseInt(req.getParameter("id"));

		BaseResponse baseResponse = new BaseResponse();
		boolean isDeleted = projectService.deleteProject(idProject);

		baseResponse.setStatusCode(isDeleted ? 200 : 500);
		baseResponse.setMessage(isDeleted ? "Project deleted successfully" : "Failed to delete project");
		baseResponse.setData(isDeleted);

		Gson gson = new Gson();
		String jsonData = gson.toJson(baseResponse);

		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.append(jsonData);
	}
}
