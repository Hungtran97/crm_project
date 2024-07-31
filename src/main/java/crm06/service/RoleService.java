package crm06.service;

import java.util.List;

import crm06.entity.RoleEntity;
import crm06.repository.RoleRepository;

public class RoleService {
	RoleRepository roleRepository = new RoleRepository();

	public List<RoleEntity> getAllRoles() {
		return roleRepository.getAll();
	}

	public boolean addRole(RoleEntity role) {

		return roleRepository.add(role) > 0;
	}

	public RoleEntity getRoleById(int id) {
		return roleRepository.getById(id);
	}

	public boolean updateRole(RoleEntity role) {
		return roleRepository.update(role) > 0;
	}
}
