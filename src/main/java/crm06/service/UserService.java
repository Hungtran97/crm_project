package crm06.service;

import java.util.List;

import crm06.entity.UserEntity;
import crm06.repository.RoleRepository;
import crm06.repository.TaskRepository;
import crm06.repository.UserRepository;

public class UserService {
	private UserRepository userRepository = new UserRepository();
	private RoleRepository roleRepository = new RoleRepository();
	private TaskRepository taskRepository = new TaskRepository();

	public List<UserEntity> getUser() {
		return userRepository.getUser();

	}

	public boolean deleteUser(int id) {
		int deleteTaskTable = taskRepository.deleteByUserId(id);
		int deleteUserTable = userRepository.deleteById(id);
		return deleteTaskTable > 0 || deleteUserTable > 0;
	}

	public boolean addUser(UserEntity user) {
		boolean isSuccess = false;
		if (user.getUserName().isBlank()) {
			user.setUserName("Un-set");
		}
		boolean isRequired = !user.getEmail().isBlank() && !user.getPassword().isBlank() && user.getRole().getId() != 0;

		if (isRequired) {
			isSuccess = userRepository.addUser(user) > 0;
		}

		return isSuccess;

	}

	public int getIdByEmail(String email) {
		int userId = -1;
		userId = userRepository.getUserIdByEmail(email);
		return userId;
	}

	public UserEntity getUserInforById(int id) {
		UserEntity userEntity = userRepository.getUser(id);
		return userEntity;
	}

}
