package crm06.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.entity.TaskEntity;
import crm06.entity.UserEntity;
import crm06.service.RoleService;
import crm06.service.TaskService;
import crm06.service.UserService;

@WebServlet(name = "userController", urlPatterns = { "/user-add", "/user-table", "/user-details" })
public class UserController extends HttpServlet {
	private UserService userService = new UserService();
	private RoleService roleService = new RoleService();
	private TaskService taskService = new TaskService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String path = req.getServletPath();
		switch (path) {
		case "/user-add":
			getRoleList(req, resp);
			req.getRequestDispatcher("user-add.jsp").forward(req, resp);
			break;
		case "/user-table":
			getUserList(req, resp);
			req.getRequestDispatcher("user-table.jsp").forward(req, resp);
			break;
		case "/user-details":
			getUserInfor(req, resp);
			getUserTaskDetails(req, resp);
			req.getRequestDispatcher("user-details.jsp").forward(req, resp);
			break;
		default:
			break;
		}

	}

	private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("roleList", roleService.getAllRoles());

	}

	private void getUserList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("userList", userService.getUser());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String path = req.getServletPath();
		switch (path) {
		case "/user-add":
			getRoleList(req, resp);
			addUser(req, resp);
			req.getRequestDispatcher("user-add.jsp").forward(req, resp);
			break;
		case "/user-table":
//			getUserList(req, resp);
			break;
		case "/user-details":
//			req.getRequestDispatcher("user-details.jsp").forward(req, resp);
			break;
		default:
			break;
		}
	}

	private void getUserInfor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int id = Integer.valueOf(req.getParameter("id"));
		if (id > 0) {
			UserEntity userEntity = userService.getUserInforById(id);
			String fullName = userEntity.getFirstName() + " " + userEntity.getLastName();
			req.setAttribute("fullName", fullName);
			req.setAttribute("userEmail", userEntity.getEmail());
		}
	}

	private void getUserTaskDetails(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String idParam = req.getParameter("id");
			if (idParam == null) {
				throw new ServletException("Missing parameter: id");
			}

			int id = Integer.valueOf(idParam);
			if (id > 0) {
				List<TaskEntity> taskList = taskService.getAllTaskByUserId(id);

				if (taskList == null) {
					throw new ServletException("Task list is null");
				}

				List<TaskEntity> notStartedTaskList = new ArrayList<>();
				List<TaskEntity> inProcessingTaskList = new ArrayList<>();
				List<TaskEntity> completedTaskList = new ArrayList<>();

				for (TaskEntity taskEntity : taskList) {
					if (taskEntity.getStatus() == null) {
						System.err.println("TaskEntity with null status: " + taskEntity);
						continue;
					}
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
		} catch (NumberFormatException e) {
			throw new ServletException("Invalid parameter: id must be an integer", e);
		} catch (Exception e) {
			throw new ServletException("An error occurred while getting user task details", e);
		}
	}

	private void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("first-name");
		String lastName = req.getParameter("last-name");
		String userName = req.getParameter("user-name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String phone = req.getParameter("phone");
		int roleId = Integer.valueOf(req.getParameter("role-id"));

		UserEntity user = new UserEntity(email, password, firstName, lastName, userName, phone, roleId);

		String[] messageList = new String[4];
		String[] alertType = { "alert-danger", "alert-success" };
		req.setAttribute("messageList", messageList);
		req.setAttribute("alertType", alertType);
		if (email.isBlank()) {
			messageList[0] = "Vui lòng nhập email";
		}
		if (password.isBlank()) {
			messageList[1] = "Vui lòng nhập password";
		}
		if (roleId == 0) {
			messageList[2] = "Vui lòng chọn role";
		}
		boolean isSuccess = userService.addUser(user);
		if (isSuccess) {
			messageList[3] = "Thêm user thành công";
		} else {
			messageList[3] = "Thêm user thất bại";
			req.setAttribute("prevFirstName", firstName);
			req.setAttribute("prevLastName", lastName);
			req.setAttribute("prevUserName", userName);
			req.setAttribute("prevEmail", email);
			req.setAttribute("prevPassword", password);
			req.setAttribute("prevPhone", phone);

		}
		req.setAttribute("isSuccess", isSuccess);
	}
}
