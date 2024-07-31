package crm06.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import crm06.config.MySQLConfig;
import crm06.entity.ProjectEntity;

public class ProjectRepository {
	public List<ProjectEntity> getAll() {
		List<ProjectEntity> projectList = new ArrayList<ProjectEntity>();
		try {
			String query = "SELECT p.id, p.name, p.start_date, p.end_date FROM project p";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				ProjectEntity projectEntity = new ProjectEntity();
				projectEntity.setId(result.getInt("id"));
				projectEntity.setName(result.getString("name"));
				projectEntity.setStartDate(result.getDate("start_date"));
				projectEntity.setEndDate(result.getDate("end_date"));
				projectList.add(projectEntity);
			}

			connection.close();
		} catch (Exception e) {
			System.out.println("error get all project: " + e.getMessage());
		}

		return projectList;
	}

	public ProjectEntity getById(int id) {
		ProjectEntity project = new ProjectEntity();
		try {
			String query = "SELECT p.id, p.name, p.start_date, p.end_date FROM project p WHERE p.id = ?";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				project.setId(result.getInt("id"));
				project.setName(result.getString("name"));
				project.setStartDate(result.getDate("start_date"));
				project.setEndDate(result.getDate("end_date"));
			}

			connection.close();
		} catch (Exception e) {
			System.out.println("error get project by Id: " + e.getMessage());
		}

		return project;
	}

	public int add(ProjectEntity projectEntity) {
		int rowCount = 0;
		try {
			boolean isRequired = !projectEntity.getName().isEmpty() && projectEntity.getStartDate() != null
					&& projectEntity.getEndDate() != null;

			if (isRequired) {
				String query = "INSERT INTO project (name, start_date, end_date) VALUES (?,?,?)";
				Connection connection = MySQLConfig.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, projectEntity.getName());
				Date startDate = (Date) projectEntity.getStartDate();
				Date endDate = (Date) projectEntity.getEndDate();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				preparedStatement.setString(2, formatter.format(startDate));
				preparedStatement.setString(3, formatter.format(endDate));
				rowCount = preparedStatement.executeUpdate();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("add error" + e.getMessage());
		}
		return rowCount;
	}

	public int update(ProjectEntity projectEntity) {

		int rowCount = 0;
		try {
			boolean isRequired = !projectEntity.getName().isEmpty() && projectEntity.getStartDate() != null
					&& projectEntity.getEndDate() != null;

			if (isRequired) {
				String query = "UPDATE project p SET p.name = ?, p.start_date = ?, p.end_date = ? WHERE p.id = ?";

				Connection connection = MySQLConfig.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, projectEntity.getName());
				Date startDate = (Date) projectEntity.getStartDate();
				Date endDate = (Date) projectEntity.getEndDate();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				preparedStatement.setString(2, formatter.format(startDate));
				preparedStatement.setString(3, formatter.format(endDate));
				preparedStatement.setInt(4, projectEntity.getId());
				rowCount = preparedStatement.executeUpdate();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("update error" + e.getMessage());
		}
		return rowCount;
	}

	public int deleteById(int id) {
		int rowCount = 0;
		String query = "DELETE FROM project p WHERE p.id = ?";
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
}
