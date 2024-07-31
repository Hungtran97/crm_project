package crm06.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.entity.ProjectEntity;
import crm06.entity.StatusEntity;
import crm06.entity.TaskEntity;
import crm06.entity.UserEntity;
import crm06.service.ProjectService;
import crm06.service.TaskService;
import crm06.service.UserService;

@WebServlet(name = "taskController", urlPatterns = { "/task", "/task-add" })
public class TaskController extends HttpServlet {
	TaskService taskService = new TaskService();
	ProjectService projectService = new ProjectService();
	UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String path = req.getServletPath();
		switch (path) {
		case "/task":
			getAllTaskInfor(req, resp);
			req.getRequestDispatcher("task.jsp").forward(req, resp);
			break;
		case "/task-add":
			getAllProjectInfor(req, resp);
			getAllUserInfor(req, resp);
			String idParam = req.getParameter("id");
			if (idParam != null && !idParam.isEmpty()) {
				getTaskInfor(req, resp);
			}

			req.getRequestDispatcher("task-add.jsp").forward(req, resp);
			break;
		default:
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String path = req.getServletPath();
		switch (path) {
		case "/task":
//			getAllTaskInfor(req, resp);
//			req.getRequestDispatcher("task.jsp").forward(req, resp);
			break;
		case "/task-add":
			addOrUpdateTask(req, resp);
			doGet(req, resp);
			break;
		default:
			break;
		}
	}

	private void getAllTaskInfor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("taskList", taskService.getAllTask());

	}

	private void getAllProjectInfor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("projectList", projectService.getAllProject());
	}

	private void getAllUserInfor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setAttribute("userList", userService.getUser());
	}

	private void getTaskInfor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.valueOf(req.getParameter("id"));
		TaskEntity task = taskService.getTaskById(id);
		req.setAttribute("task", task);
		String taskName = task.getName();
		int statusId = task.getStatus().getId();
		Date startDateStr = task.getStartDate();
		Date endDateStr = task.getEndDate();
		req.setAttribute("taskName", taskName);
		req.setAttribute("statusId", statusId);
		req.setAttribute("startDate", startDateStr);
		req.setAttribute("endDate", endDateStr);
	}

	private void addOrUpdateTask(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String taskName = req.getParameter("task-name");
		int projectId = Integer.valueOf(req.getParameter("project-id"));
		int userId = Integer.valueOf(req.getParameter("user-id"));
		String startDateStr = req.getParameter("start-date");
		String endDateStr = req.getParameter("end-date");

		TaskEntity task = new TaskEntity();
		task.setName(taskName);

		ProjectEntity projectEntity = new ProjectEntity();
		projectEntity.setId(projectId);
		task.setProject(projectEntity);

		UserEntity userEntity = new UserEntity();
		userEntity.setId(userId);
		task.setUser(userEntity);

		StatusEntity statusEntity = new StatusEntity();
		statusEntity.setId(1);
		task.setStatus(statusEntity);

		try {
			if (startDateStr != null && !startDateStr.isEmpty()) {
				task.setStartDate(java.sql.Date.valueOf(startDateStr));
			}
			if (endDateStr != null && !endDateStr.isEmpty()) {
				task.setEndDate(java.sql.Date.valueOf(endDateStr));
			}
		} catch (IllegalArgumentException e) {
			// Log the exception
			System.err.println("Invalid date format: " + e.getMessage());
			// Set dates to null in case of format error
			task.setStartDate(null);
			task.setEndDate(null);
		}
		boolean isAdded = false;
		boolean isUpdated = false;
		String idParam = req.getParameter("id");
		if (idParam != null && !idParam.isEmpty()) {
			task.setId(Integer.valueOf(idParam));
			int statusId = Integer.valueOf(req.getParameter("status-id"));
			statusEntity.setId(statusId);
			task.setStatus(statusEntity);
			isUpdated = taskService.updateTask(task);
		} else {
			isAdded = taskService.addTask(task);

		}

		if (isAdded) {
			req.setAttribute("message", "Thêm thành công.");
			req.setAttribute("alertType", "alert-success");
		} else if (isUpdated) {
			req.setAttribute("message", "Update thành công.");
			req.setAttribute("alertType", "alert-success");
			req.setAttribute("taskName", taskName);
			req.setAttribute("startDate", startDateStr);
			req.setAttribute("endDate", endDateStr);
		} else {
			req.setAttribute("message", "Thêm/Update thất bại. Vui lòng thử lại.");
			req.setAttribute("alertType", "alert-danger");
		}
	}
}
