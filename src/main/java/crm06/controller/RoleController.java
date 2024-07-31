package crm06.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm06.entity.RoleEntity;
import crm06.service.RoleService;

@WebServlet(name = "roleAddController", urlPatterns = { "/role-add", "/role-table" })
public class RoleController extends HttpServlet {
	RoleService roleService = new RoleService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String path = req.getServletPath();
		switch (path) {
		case "/role-add":
			String idParam = req.getParameter("id");
			if (idParam != null && !idParam.isEmpty()) {
				getRoleInfor(req, resp);
			}
			req.getRequestDispatcher("role-add.jsp").forward(req, resp);
			break;
		case "/role-table":
			getRoleList(req, resp);
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
		case "/role-add":
			addOrUpdateRole(req, resp);
			doGet(req, resp);
			break;
		case "/role-table":

			break;
		default:
			break;
		}

	}

	private void getRoleInfor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.valueOf(req.getParameter("id"));
		RoleEntity role = roleService.getRoleById(id);
		req.setAttribute("role", role);
		req.setAttribute("roleName", role.getName());
		req.setAttribute("roleDescription", role.getDescription());
	}

	private void addOrUpdateRole(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RoleEntity role = new RoleEntity();
		String roleName = req.getParameter("role-name");
		String roleDescription = req.getParameter("role-description");
		role.setName(roleName);
		role.setDescription(roleDescription);
		if (roleName.isBlank()) {
			req.setAttribute("message", "Vui lòng nhập tên quyền.");
			req.setAttribute("prevRoleDescription", roleDescription);
			req.setAttribute("alertType", "alert-danger");
		} else {
			boolean isAdded = false;
			boolean isUpdated = false;
			String idParam = req.getParameter("id");
			if (idParam != null && !idParam.isEmpty()) {
				role.setId(Integer.valueOf(idParam));
				isUpdated = roleService.updateRole(role);
			} else {
				isAdded = roleService.addRole(role);

			}
			if (isAdded) {
				req.setAttribute("message", "Thêm role thành công.");
				req.setAttribute("alertType", "alert-success");
			} else if (isUpdated) {
				req.setAttribute("message", "Update role thành công.");
				req.setAttribute("alertType", "alert-success");
				req.setAttribute("roleName", roleName);
				req.setAttribute("roleDescription", roleDescription);
			} else {
				req.setAttribute("message", "Thêm/Update role thất bại. Vui lòng thử lại.");
				req.setAttribute("alertType", "alert-danger");
				req.setAttribute("roleName", roleName);
				req.setAttribute("roleDescription", roleDescription);
			}
		}

	}

	private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("roleList", roleService.getAllRoles());
		req.getRequestDispatcher("role-table.jsp").forward(req, resp);
	}
}
