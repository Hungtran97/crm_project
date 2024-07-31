package crm06.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.entity.ProjectEntity;
import crm06.entity.TaskEntity;
import crm06.service.ProjectService;
import crm06.service.TaskService;

@WebServlet(name = "ProjectController", urlPatterns = { "/groupwork", "/groupwork-add", "/groupwork-details" })
public class ProjectController extends HttpServlet {
	ProjectService projectService = new ProjectService();
	TaskService taskService = new TaskService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String path = req.getServletPath();
		switch (path) {
		case "/groupwork":
			getAllProjectInfor(req, resp);
			req.getRequestDispatcher("/groupwork.jsp").forward(req, resp);
			break;
		case "/groupwork-add":
			String idParam = req.getParameter("id");
			if (idParam != null && !idParam.isEmpty()) {
				getProjectInfor(req, resp);
			}
			req.getRequestDispatcher("/groupwork-add.jsp").forward(req, resp);
			break;
		case "/groupwork-details":
			getProjectInfor(req, resp);
			getAllTaskInfor(req, resp);
			req.getRequestDispatcher("/groupwork-details.jsp").forward(req, resp);
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
		case "/groupwork":
			req.getRequestDispatcher("/groupwork.jsp").forward(req, resp);
			break;
		case "/groupwork-add":
			addOrUpdateProject(req, resp);
			req.getRequestDispatcher("/groupwork-add.jsp").forward(req, resp);
			break;
		case "/groupwork-details":
			req.getRequestDispatcher("/groupwork-details.jsp").forward(req, resp);
			break;
		default:
			break;
		}
	}

	private void getAllProjectInfor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<ProjectEntity> projectList = projectService.getAllProject();
		req.setAttribute("projectList", projectList);
	}

	private void getAllTaskInfor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int id = Integer.valueOf(req.getParameter("id"));
		List<TaskEntity> taskList = taskService.getAllTaskByProjectId(id);
		List<TaskEntity> notStartedTaskList = new ArrayList<TaskEntity>();
		List<TaskEntity> inProcessingTaskList = new ArrayList<TaskEntity>();
		List<TaskEntity> completedTaskList = new ArrayList<TaskEntity>();
		for (TaskEntity taskEntity : taskList) {
			switch (taskEntity.getStatus().getId()) {
			case 1:
				notStartedTaskList.add(taskEntity);
				break;
			case 2:
				inProcessingTaskList.add(taskEntity);
				break;
			case 3:
				completedTaskList.add(taskEntity);
				break;

			default:
				break;
			}
		}
		req.setAttribute("notStartedTaskList", notStartedTaskList);
		req.setAttribute("inProcessingTaskList", inProcessingTaskList);
		req.setAttribute("completedTaskList", completedTaskList);
		req.setAttribute("taskList", taskList);
	}

	private void getProjectInfor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int id = Integer.valueOf(req.getParameter("id"));
		ProjectEntity project = projectService.getProjectById(id);
		req.setAttribute("project", project);
		String projectName = project.getName();
		Date startDateStr = project.getStartDate();
		Date endDateStr = project.getEndDate();
		req.setAttribute("projectName", projectName);
		req.setAttribute("startDate", startDateStr);
		req.setAttribute("endDate", endDateStr);
	}

	private void addOrUpdateProject(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String projectName = req.getParameter("project-name");
		String startDateStr = req.getParameter("start-date");
		String endDateStr = req.getParameter("end-date");

		ProjectEntity project = new ProjectEntity();
		project.setName(projectName);

		try {
			if (startDateStr != null && !startDateStr.isEmpty()) {
				project.setStartDate(java.sql.Date.valueOf(startDateStr));
			}
			if (endDateStr != null && !endDateStr.isEmpty()) {
				project.setEndDate(java.sql.Date.valueOf(endDateStr));
			}
		} catch (IllegalArgumentException e) {
			// Log the exception
			System.err.println("Invalid date format: " + e.getMessage());
			// Set dates to null in case of format error
			project.setStartDate(null);
			project.setEndDate(null);
		}
		boolean isAdded = false;
		boolean isUpdated = false;
		String idParam = req.getParameter("id");
		if (idParam != null && !idParam.isEmpty()) {
			project.setId(Integer.valueOf(idParam));
			isUpdated = projectService.isUpdated(project);
		} else {
			isAdded = projectService.addProject(project);
		}

		if (isAdded) {
			req.setAttribute("message", "Thêm thành công.");
			req.setAttribute("alertType", "alert-success");
		} else if (isUpdated) {
			req.setAttribute("message", "Update thành công.");
			req.setAttribute("alertType", "alert-success");
			req.setAttribute("projectName", projectName);
			req.setAttribute("startDate", startDateStr);
			req.setAttribute("endDate", endDateStr);
		} else {
			req.setAttribute("message", "Thêm/Update thất bại. Vui lòng thử lại.");
			req.setAttribute("alertType", "alert-danger");
			req.setAttribute("projectName", projectName);
			req.setAttribute("startDate", startDateStr);
			req.setAttribute("endDate", endDateStr);
		}

	}

}
