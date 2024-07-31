package crm06.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import crm06.config.MySQLConfig;
import crm06.entity.StatusEntity;

public class StatusRepository {

	public List<StatusEntity> getAll() {
		List<StatusEntity> statusList = new ArrayList<StatusEntity>();

		try {
			String query = "SELECT *  FROM status";
			Connection connection = MySQLConfig.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				StatusEntity entity = new StatusEntity(result.getInt(1), result.getString(2));
				statusList.add(entity);
			}
			connection.close();

		} catch (Exception e) {
			System.out.println("error get all status: " + e.getMessage());
		}
		return statusList;
	}
}
