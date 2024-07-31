package crm06.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.entity.StatusEntity;
import crm06.entity.TaskEntity;
import crm06.entity.UserEntity;
import crm06.service.TaskService;
import crm06.service.UserService;

@WebServlet(name = "profileController", urlPatterns = { "/profile", "/profile-edit" })
public class ProfileController extends HttpServlet {
	TaskService taskService = new TaskService();
	UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String path = req.getServletPath();
		switch (path) {
		case "/profile":
			getUserInfor(req, resp);
			getTotalTaskInfor(req, resp);
			getProfileTaskDetails(req, resp);
			req.getRequestDispatcher("profile.jsp").forward(req, resp);
			break;
		case "/profile-edit":
			getTaskInfor(req, resp);
			getAllStatus(req, resp);
			req.getRequestDispatcher("profile-edit.jsp").forward(req, resp);

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
		case "/profile":

			break;
		case "/profile-edit":
			updateTask(req, resp);
			doGet(req, resp);
			break;
		default:
			break;
		}
	}

	private void getTotalTaskInfor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		String userEmail = new String();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("userEmail")) {
					userEmail = cookies[i].getValue();
					break;
				}
			}
		}
		int id = userService.getIdByEmail(userEmail);
		if (id > 0) {
			List<int[]> taskStatusUserList = taskService.getQuantityStatusTaskListById(id);
			int totalUserTask = taskService.getTotalUserTask(id) > 0 ? taskService.getTotalUserTask(id) : 1;
			req.setAttribute("taskStatusUserList", taskStatusUserList);
			req.setAttribute("totalUserTask", totalUserTask);
		}

	}

	private void getProfileTaskDetails(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		String userEmail = new String();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("userEmail")) {
					userEmail = cookies[i].getValue();
					break;
				}
			}
		}
		int id = userService.getIdByEmail(userEmail);
		if (id > 0) {
			List<TaskEntity> taskList = taskService.getAllTaskByUserId(id);
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

	}

	private void getUserInfor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		String userEmail = new String();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("userEmail")) {
					userEmail = cookies[i].getValue();
					break;
				}
			}
		}
		int id = userService.getIdByEmail(userEmail);
		if (id > 0) {
			UserEntity userEntity = userService.getUserInforById(id);
			String fullName = userEntity.getFirstName() + " " + userEntity.getLastName();
			req.setAttribute("fullName", fullName);
			req.setAttribute("userEmail", userEmail);
		}
	}

	private void getTaskInfor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idTask = Integer.parseInt(req.getParameter("id"));
		TaskEntity taskEntity = taskService.getTaskById(idTask);
		req.setAttribute("task", taskEntity);
	}

	public void getAllStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<StatusEntity> statusList = taskService.getAllStatus();
		req.setAttribute("statusList", statusList);
	}

	private void updateTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TaskEntity task = taskService.getTaskById(Integer.valueOf(req.getParameter("id")));
		task.setName(req.getParameter("task-name"));
		StatusEntity statusEntity = new StatusEntity();
		statusEntity.setId(Integer.valueOf(req.getParameter("status-id")));
		task.setStatus(statusEntity);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = req.getParameter("start-date");
		String endDate = req.getParameter("end-date");

		try {
			task.setStartDate((java.sql.Date.valueOf(startDate)));
			task.setEndDate((java.sql.Date.valueOf(endDate)));

		} catch (Exception e) {
			task.setStartDate(null);
			task.setEndDate(null);

		}
		boolean isUpdated = taskService.updateTask(task);
		if (isUpdated) {
			req.setAttribute("message", "Update thành công.");
			req.setAttribute("alertType", "alert-success");
		} else {
			req.setAttribute("message", "Update thất bại. Vui lòng thử lại.");
			req.setAttribute("alertType", "alert-danger");
		}

	}
}
