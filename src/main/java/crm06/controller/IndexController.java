package crm06.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.service.TaskService;

@WebServlet(name = "indexController", urlPatterns = { "/index", "" })
public class IndexController extends HttpServlet {
	TaskService taskService = new TaskService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String path = req.getServletPath();
		switch (path) {
		case "":
		case "/index":
			List<int[]> quantityStatusTaskList = taskService.getQuantityStatusTaskList();
			int totalTask = taskService.getTotalTask() > 0 ? taskService.getTotalTask() : 1;
			req.setAttribute("taskStatusList", quantityStatusTaskList);
			req.setAttribute("totalTask", totalTask);
			req.getRequestDispatcher("index.jsp").forward(req, resp);
			break;
		default:
			break;
		}

	}
}
