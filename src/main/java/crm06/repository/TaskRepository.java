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
import crm06.entity.StatusEntity;
import crm06.entity.TaskEntity;
import crm06.entity.UserEntity;

public class TaskRepository {
	public List<TaskEntity> getAll() {
		List<TaskEntity> taskList = new ArrayList<TaskEntity>();
		try {
			String query = "SELECT t.id, t.name, p.name as project, t.start_date, t.end_date, s.name as status, "
					+ "u.first_name as user_first_name, u.last_name as user_last_name  FROM task t JOIN project p "
					+ "ON t.project_id = p.id JOIN status s ON s.id = t.status_id JOIN users u ON u.id = t.user_id";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				TaskEntity taskEntity = new TaskEntity();
				taskEntity.setName(result.getString("name"));
				taskEntity.setId(result.getInt("id"));

				UserEntity userEntity = new UserEntity();
				userEntity.setFirstName(result.getString("user_first_name"));
				userEntity.setLastName(result.getString("user_last_name"));
				taskEntity.setUser(userEntity);

				StatusEntity statusEntity = new StatusEntity();
				statusEntity.setName(result.getString("status"));
				taskEntity.setStatus(statusEntity);

				ProjectEntity projectEntity = new ProjectEntity();
				projectEntity.setName(result.getString("project"));
				taskEntity.setProject(projectEntity);

				taskEntity.setStartDate(result.getDate("start_date"));
				taskEntity.setEndDate(result.getDate("end_date"));
				taskList.add(taskEntity);
			}

			connection.close();
		} catch (Exception e) {
			System.out.println("error get all task: " + e.getMessage());
		}

		return taskList;
	}

	public List<TaskEntity> getAllByUserId(int id) {
		List<TaskEntity> taskList = new ArrayList<TaskEntity>();
		try {
			String query = "SELECT t.*, p.name as project, s.name as status FROM task t JOIN project p ON t.project_id = p.id JOIN status s ON s.id = t.status_id WHERE t.user_id = ? ";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				TaskEntity taskEntity = new TaskEntity();
				taskEntity.setName(result.getString("name"));
				taskEntity.setId(result.getInt("id"));

				StatusEntity statusEntity = new StatusEntity();
				statusEntity.setName(result.getString("status"));
				statusEntity.setId(result.getInt("status_id"));
				taskEntity.setStatus(statusEntity);

				ProjectEntity projectEntity = new ProjectEntity();
				projectEntity.setName(result.getString("project"));
				taskEntity.setProject(projectEntity);

				taskEntity.setStartDate(result.getDate("start_date"));
				taskEntity.setEndDate(result.getDate("end_date"));
				taskList.add(taskEntity);
			}

			connection.close();

		} catch (Exception e) {
			System.out.println("error get all task by user Id: " + e.getMessage());
		}

		return taskList;
	}

	public List<TaskEntity> getAllByProjectId(int id) {
		List<TaskEntity> taskList = new ArrayList<TaskEntity>();
		try {
			String query = "SELECT t.*, u.first_name as user_first_name, u.last_name as user_last_name, s.name as status FROM task t JOIN users u ON t.user_id = u.id JOIN status s ON s.id = t.status_id WHERE t.project_id = ? ";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				TaskEntity taskEntity = new TaskEntity();
				taskEntity.setName(result.getString("name"));
				taskEntity.setId(result.getInt("id"));

				StatusEntity statusEntity = new StatusEntity();
				statusEntity.setName(result.getString("status"));
				statusEntity.setId(result.getInt("status_id"));
				taskEntity.setStatus(statusEntity);

				UserEntity userEntity = new UserEntity();
				userEntity.setFirstName(result.getString("user_first_name"));
				userEntity.setLastName(result.getString("user_last_name"));
				taskEntity.setUser(userEntity);

				taskEntity.setStartDate(result.getDate("start_date"));
				taskEntity.setEndDate(result.getDate("end_date"));
				taskList.add(taskEntity);
			}

			connection.close();

		} catch (Exception e) {
			System.out.println("error get all task by project Id: " + e.getMessage());
		}

		return taskList;
	}

	public TaskEntity getById(int id) {
		TaskEntity task = new TaskEntity();
		try {
			String query = "SELECT t.id, t.name, t.user_id, t.project_id, t.status_id, p.name as project, t.start_date, t.end_date, s.name as status FROM task t JOIN project p ON t.project_id = p.id JOIN status s ON s.id = t.status_id WHERE t.id = ? ";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				task.setName(result.getString("name"));
				task.setId(result.getInt("id"));

				UserEntity userEntity = new UserEntity();
				userEntity.setId(result.getInt("user_id"));
				task.setUser(userEntity);

				StatusEntity statusEntity = new StatusEntity();
				statusEntity.setId(result.getInt("status_id"));
				statusEntity.setName(result.getString("status"));
				task.setStatus(statusEntity);

				ProjectEntity projectEntity = new ProjectEntity();
				projectEntity.setName(result.getString("project"));
				projectEntity.setId(result.getInt("project_id"));
				task.setProject(projectEntity);

				task.setStartDate(result.getDate("start_date"));
				task.setEndDate(result.getDate("end_date"));
			}

			connection.close();

		} catch (Exception e) {
			System.out.println("error get all task by Id: " + e.getMessage());
		}

		return task;
	}

	public List<int[]> getStatusQuantity() {
		List<int[]> statusQuantityList = new ArrayList<int[]>();
		for (int i = 0; i < 3; i++) {
			int[] arr = new int[2];
			arr[0] = i + 1; // Status ID (1, 2, 3)
			arr[1] = 0; // Initial quantity
			statusQuantityList.add(arr);
		}

		try {
			String query = "SELECT t.status_id, COUNT(t.status_id) as quantity_status FROM task t GROUP BY t.status_id";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				int statusId = result.getInt("status_id");
				int statusQuantity = result.getInt("quantity_status");
				if (statusId >= 1 && statusId <= 3) {
					statusQuantityList.get(statusId - 1)[1] = statusQuantity;
				}
			}
			connection.close();

		} catch (Exception e) {
			System.out.println("error get all status quantity: " + e.getMessage());
		}
		return statusQuantityList;

	}

	public List<int[]> getStatusQuantityById(int id) {
		List<int[]> statusQuantityList = new ArrayList<int[]>();
		for (int i = 0; i < 3; i++) {
			int[] arr = new int[2];
			arr[0] = i + 1; // Status ID (1, 2, 3)
			arr[1] = 0; // Initial quantity
			statusQuantityList.add(arr);
		}

		try {
			String query = "SELECT t.status_id, COUNT(t.status_id) as quantity_status FROM task t WHERE t.user_id = ? GROUP BY t.status_id";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				int statusId = result.getInt("status_id");
				int statusQuantity = result.getInt("quantity_status");
				if (statusId >= 1 && statusId <= 3) {
					statusQuantityList.get(statusId - 1)[1] = statusQuantity;
				}
			}
			connection.close();

		} catch (Exception e) {
			System.out.println("error get status quantity by ID: " + e.getMessage());
		}
		return statusQuantityList;

	}

	public int deleteByUserId(int id) {
		int rowCount = 0;
		String query = "DELETE FROM task t WHERE t.user_id = ?";
		try {
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			rowCount = statement.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println("Error delete task by ID: " + e.getMessage());
		}
		return rowCount;
	}

	public int deleteByProjectId(int id) {
		int rowCount = 0;
		String query = "DELETE FROM task t WHERE t.project_id = ?";
		try {
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			rowCount = statement.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println("Error delete task by ID: " + e.getMessage());
		}
		return rowCount;
	}

	public int deleteById(int id) {
		int rowCount = 0;
		String query = "DELETE FROM task t WHERE t.id = ?";
		try {
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			rowCount = statement.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println("Error delete task by ID: " + e.getMessage());
		}
		return rowCount;
	}

	public int update(TaskEntity task) {
		int rowUpdate = 0;
		boolean isRequired = task.getId() != 0 && !task.getName().isBlank() && task.getStatus().getId() != 0
				&& task.getStartDate() != null && task.getEndDate() != null;
		String query = "UPDATE task t SET t.name = ?, t.user_id =?, t.project_id = ?, status_id = ?, t.start_date = ?, t.end_date = ? WHERE t.id = ?";
		try {
			if (isRequired) {
				Connection connection = MySQLConfig.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, task.getName());
				statement.setInt(2, task.getUser().getId());
				statement.setInt(3, task.getProject().getId());
				statement.setInt(4, task.getStatus().getId());
				Date startDate = (Date) task.getStartDate();
				Date endDate = (Date) task.getEndDate();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				statement.setString(5, formatter.format(startDate));
				statement.setString(6, formatter.format(endDate));
				statement.setInt(7, task.getId());
				rowUpdate = statement.executeUpdate();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("Error update task: " + e.getMessage());
		}
		return rowUpdate;
	}

	public int add(TaskEntity task) {
		int rowUpdate = 0;
		try {
			boolean isRequired = task.getProject().getId() != 0 && task.getUser().getId() != 0
					&& !task.getName().isBlank() && task.getStatus().getId() != 0 && task.getStartDate() != null
					&& task.getEndDate() != null;
			String query = "INSERT INTO task (user_id, project_id, name, status_id, start_date, end_date) VALUES (?,?,?,?,?,?)";
			if (isRequired) {
				Connection connection = MySQLConfig.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, task.getUser().getId());
				statement.setInt(2, task.getProject().getId());
				statement.setString(3, task.getName());
				Date startDate = (Date) task.getStartDate();
				Date endDate = (Date) task.getEndDate();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				statement.setInt(4, task.getStatus().getId());
				statement.setString(5, formatter.format(startDate));
				statement.setString(6, formatter.format(endDate));
				rowUpdate = statement.executeUpdate();
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("Error add task: " + e.getMessage());
		}
		return rowUpdate;
	}

}
