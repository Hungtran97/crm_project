package crm06.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import crm06.config.MySQLConfig;
import crm06.entity.RolePermissionEntity;

public class RolePermissionRepository {
	public List<RolePermissionEntity> getAll() {
		List<RolePermissionEntity> rolePermissionList = new ArrayList<RolePermissionEntity>();
		try {
			String queryString = "SELECT * FROM roles_permission rp";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatementRole = connection.prepareStatement(queryString);
			ResultSet resultSet = preparedStatementRole.executeQuery();

			while (resultSet.next()) {
				RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
				rolePermissionEntity.setId(resultSet.getInt("id"));
				rolePermissionEntity.setRoleId(resultSet.getInt("role_id"));
				rolePermissionEntity.setPathPermission(resultSet.getString("path_permission"));
				rolePermissionList.add(rolePermissionEntity);
			}
			connection.close();
		} catch (Exception e) {
			System.out.println("Loi get all role permisstion: " + e.getMessage());
		}

		return rolePermissionList;
	}

	public List<String> getPathPermission(int roleId) {
		List<String> pathPermissionList = new ArrayList<String>();

		try {
			String queryString = "SELECT rp.path_permission FROM roles_permission rp WHERE rp.role_id =" + roleId;
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatementRole = connection.prepareStatement(queryString);
			ResultSet resultSet = preparedStatementRole.executeQuery();

			while (resultSet.next()) {
				pathPermissionList.add(resultSet.getString("path_permission"));
			}
			connection.close();
		} catch (Exception e) {
			System.out.println("Loi get all role permisstion: " + e.getMessage());
		}

		return pathPermissionList;
	}
}
