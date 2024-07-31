package crm06.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import crm06.config.MySQLConfig;
import crm06.entity.RoleEntity;

public class RoleRepository {
	public List<RoleEntity> getAll() {
		List<RoleEntity> roleList = new ArrayList<RoleEntity>();

		try {
			String queryString = "SELECT * FROM roles r";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatementRole = connection.prepareStatement(queryString);
			ResultSet resultSet = preparedStatementRole.executeQuery();

			while (resultSet.next()) {
				RoleEntity role = new RoleEntity();
				role.setId(resultSet.getInt("id"));
				role.setName(resultSet.getString("name"));
				role.setDescription(resultSet.getString("description"));
				roleList.add(role);
			}
			connection.close();
		} catch (Exception e) {
			System.out.println("Loi get all role" + e.getMessage());
		}

		return roleList;
	}

	public int add(RoleEntity role) {
		int rowCount = 0;
		try {
			boolean isRequired = !role.getName().isEmpty() && !role.getDescription().isEmpty();
			String query = "INSERT INTO roles (name, description) VALUES (?,?)";
			if (isRequired) {
				Connection connection = MySQLConfig.getConnection();
				PreparedStatement preparedStatementRole = connection.prepareStatement(query);
				preparedStatementRole.setString(1, role.getName());
				preparedStatementRole.setString(2, role.getDescription());
				rowCount = preparedStatementRole.executeUpdate();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("add error" + e.getMessage());
		}
		return rowCount;
	}

	public int update(RoleEntity role) {
		int rowCount = 0;
		try {
			boolean isRequired = role.getId() != 0 && !role.getName().isEmpty() && !role.getDescription().isEmpty();
			String query = "UPDATE roles r SET r.name = ?, r.description = ? WHERE r.id = ?";
			if (isRequired) {
				Connection connection = MySQLConfig.getConnection();
				PreparedStatement preparedStatementRole = connection.prepareStatement(query);
				preparedStatementRole.setString(1, role.getName());
				preparedStatementRole.setString(2, role.getDescription());
				preparedStatementRole.setInt(3, role.getId());
				rowCount = preparedStatementRole.executeUpdate();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("update error" + e.getMessage());
		}
		return rowCount;
	}

	public RoleEntity getById(int id) {
		RoleEntity role = new RoleEntity();
		try {
			String queryString = "SELECT * FROM roles r WHERE r.id =?";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				role.setId(resultSet.getInt("id"));
				role.setName(resultSet.getString("name"));
				role.setDescription(resultSet.getString("description"));
			}
			connection.close();
		} catch (Exception e) {
			System.out.println("Loi get role by Id" + e.getMessage());
		}

		return role;
	}

}
