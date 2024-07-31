package crm06.service;

import java.util.List;

import crm06.entity.UserEntity;
import crm06.repository.RolePermissionRepository;
import crm06.repository.UserRepository;

public class LoginService {
	private UserRepository userRepository = new UserRepository();
	private RolePermissionRepository rolePermissionRepository = new RolePermissionRepository();

	public boolean userIsExist(String email, String password) {
		boolean isExist = false;
		UserEntity userEntity = userRepository.getUser(email, password);
		if (userEntity.getId() > 0) {
			isExist = true;
		}
		return isExist;
	}

	public boolean userIsPermission(String email, String path) {

		int roleId = userRepository.getRoleId(email);

		List<String> pathPermissionList = rolePermissionRepository.getPathPermission(roleId);

		return pathPermissionList.contains(path);
	}
}
