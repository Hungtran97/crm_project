package crm06.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import crm06.config.MySQLConfig;
import crm06.entity.RoleEntity;
import crm06.entity.UserEntity;

public class UserRepository {
	public int deleteById(int id) {
		int rowCount = 0;
		String query = "DELETE FROM users u WHERE u.id = ?";
		try {
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			rowCount = statement.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println("error delete user: " + e.getMessage());
		}
		return rowCount;
	}

	public UserEntity getUser(String email, String password) {
		UserEntity userEntity = new UserEntity();
		try {
			String query = "SELECT u.id, u.first_name, u.last_name, r.name as role FROM users u JOIN roles r ON u.role_id = r.id WHERE u.email='"
					+ email + "' AND u.password='" + password + "'";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				userEntity.setId(resultSet.getInt("id"));
				userEntity.setFirstName(resultSet.getString("first_name"));
				userEntity.setLastName(resultSet.getString("last_name"));
				RoleEntity roleEntity = new RoleEntity();
				roleEntity.setName(resultSet.getString("role"));
				userEntity.setRole(roleEntity);

			}
			connection.close();

		} catch (Exception e) {
			System.out.println("error get user by email and password: " + e.getMessage());
		}

		return userEntity;
	}

	public List<UserEntity> getUser() {
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		try {
			String query = "SELECT  u.id, u.first_name, u.last_name, u.user_name, r.name as role FROM users u JOIN roles r ON u.role_id = r.id";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				userEntity.setId(resultSet.getInt("id"));
				userEntity.setFirstName(resultSet.getString("first_name"));
				userEntity.setLastName(resultSet.getString("last_name"));
				userEntity.setUserName(resultSet.getString("user_name"));
				RoleEntity roleEntity = new RoleEntity();
				roleEntity.setName(resultSet.getString("role"));
				userEntity.setRole(roleEntity);

				userEntities.add(userEntity);

			}
			connection.close();
		} catch (Exception e) {
			System.out.println("error get all user: " + e.getMessage());
		}

		return userEntities;
	}

	public UserEntity getUser(int id) {
		UserEntity userEntity = new UserEntity();
		try {
			String query = "SELECT u.*, r.name as role FROM users u JOIN roles r ON u.role_id = r.id WHERE u.id = ?";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				userEntity.setFirstName(resultSet.getString("first_name"));
				userEntity.setLastName(resultSet.getString("last_name"));
				userEntity.setUserName(resultSet.getString("user_name"));
				userEntity.setPassword(resultSet.getString("password"));
				userEntity.setPhone(resultSet.getString("phone"));
				userEntity.setEmail(resultSet.getString("email"));
				RoleEntity roleEntity = new RoleEntity();
				roleEntity.setId(resultSet.getInt("role_id"));
				roleEntity.setName(resultSet.getString("role"));
				userEntity.setRole(roleEntity);
			}
			connection.close();
		} catch (Exception e) {
			System.out.println("error get user by ID: " + e.getMessage());
		}

		return userEntity;
	}

	public int update(UserEntity user) {
		int rowUpdate = 0;
		boolean isRequired = user.getId() != 0 && !user.getFirstName().isBlank() && !user.getLastName().isBlank()
				&& !user.getUserName().isBlank() && !user.getPassword().isBlank() && user.getRole().getId() != 0;
		String query = "UPDATE users u SET u.first_name = ?, u.last_name =?, u.user_name = ?, u.password = ?, u.phone = ?, u.role_id = ? WHERE u.id = ?";
		try {
			if (isRequired) {
				Connection connection = MySQLConfig.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, user.getFirstName());
				statement.setString(2, user.getLastName());
				statement.setString(3, user.getUserName());
				statement.setString(4, user.getPassword());
				statement.setString(5, user.getPhone());
				statement.setInt(6, user.getRole().getId());
				statement.setInt(7, user.getId());
				rowUpdate = statement.executeUpdate();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("Error update user: " + e.getMessage());
		}
		return rowUpdate;
	}

	public int getRoleId(String email) {
		int roleId = -1;
		try {
			String query = "SELECT u.role_id FROM users u WHERE u.email='" + email + "'";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				roleId = resultSet.getInt("role_id");
			}
			connection.close();

		} catch (Exception e) {
			System.out.println("error get role id by email: " + e.getMessage());
		}
		return roleId;
	}

	public int getUserIdByEmail(String email) {
		int id = -1;
		try {
			String query = "SELECT u.id FROM users u WHERE u.email='" + email + "'";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getInt("id");
			}
			connection.close();

		} catch (Exception e) {
			System.out.println("error get id user by email: " + e.getMessage());
		}

		return id;
	}

	public int addUser(UserEntity userEntity) {
		int rowCount = 0;
		try {
			String query = "INSERT INTO users (email, password, first_name, last_name, user_name, phone, role_id ) VALUES (?, ?, ?, ?, ?, ?, ?)";

			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userEntity.getEmail());
			statement.setString(2, userEntity.getPassword());
			statement.setString(3, userEntity.getFirstName());
			statement.setString(4, userEntity.getLastName());
			statement.setString(5, userEntity.getUserName());
			statement.setString(6, userEntity.getPhone());
			statement.setInt(7, userEntity.getRole().getId());
			rowCount = statement.executeUpdate();
			connection.close();

		} catch (Exception e) {
			System.out.println("Error add user: " + e.getMessage());
		}
		return rowCount;
	}

}
