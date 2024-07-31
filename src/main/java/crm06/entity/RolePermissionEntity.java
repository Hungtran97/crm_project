package crm06.entity;

public class RolePermissionEntity {
	private int id;
	private int roleId;
	private String pathPermission;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getPathPermission() {
		return pathPermission;
	}

	public void setPathPermission(String pathPermission) {
		this.pathPermission = pathPermission;
	}

}