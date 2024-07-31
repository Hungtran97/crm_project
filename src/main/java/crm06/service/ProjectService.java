package crm06.service;

import java.util.List;

import crm06.entity.ProjectEntity;
import crm06.repository.ProjectRepository;
import crm06.repository.TaskRepository;

public class ProjectService {
	ProjectRepository projectRepository = new ProjectRepository();
	TaskRepository taskRepository = new TaskRepository();

	public List<ProjectEntity> getAllProject() {
		return projectRepository.getAll();
	}

	public boolean addProject(ProjectEntity project) {
		return projectRepository.add(project) > 0;
	}

	public ProjectEntity getProjectById(int id) {
		return projectRepository.getById(id);
	}

	public boolean isUpdated(ProjectEntity project) {
		return projectRepository.update(project) > 0;
	}

	public boolean deleteProject(int id) {
		int deleteTaskTable = taskRepository.deleteByProjectId(id);
		int deleteProjectTable = projectRepository.deleteById(id);
		return deleteTaskTable > 0 || deleteProjectTable > 0;
	}
}
