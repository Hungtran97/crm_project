package crm06.service;

import java.util.List;

import crm06.entity.StatusEntity;
import crm06.entity.TaskEntity;
import crm06.repository.StatusRepository;
import crm06.repository.TaskRepository;

public class TaskService {
	TaskRepository taskRepository = new TaskRepository();
	StatusRepository statusRepository = new StatusRepository();

	public List<TaskEntity> getAllTask() {
		return taskRepository.getAll();
	}

	public List<int[]> getQuantityStatusTaskList() {
		return taskRepository.getStatusQuantity();
	}

	public List<int[]> getQuantityStatusTaskListById(int id) {
		return taskRepository.getStatusQuantityById(id);
	}

	public int getTotalTask() {
		int totalTask = 0;
		List<int[]> quantityStatusTaskList = taskRepository.getStatusQuantity();
		for (int[] is : quantityStatusTaskList) {
			totalTask += is[1];
		}
		return totalTask;
	}

	public int getTotalUserTask(int id) {
		int totalTask = 0;
		List<int[]> quantityStatusTaskList = taskRepository.getStatusQuantityById(id);
		for (int[] is : quantityStatusTaskList) {
			totalTask += is[1];
		}
		return totalTask;
	}

	public List<TaskEntity> getAllTaskByUserId(int id) {

		return taskRepository.getAllByUserId(id);
	}

	public List<TaskEntity> getAllTaskByProjectId(int id) {

		return taskRepository.getAllByProjectId(id);
	}

	public TaskEntity getTaskById(int id) {
		return taskRepository.getById(id);

	}

	public List<StatusEntity> getAllStatus() {

		return statusRepository.getAll();
	}

	public boolean updateTask(TaskEntity task) {
		return taskRepository.update(task) > 0;
	}

	public boolean addTask(TaskEntity task) {
		return taskRepository.add(task) > 0;
	}

	public boolean deleteTaskById(int taskId) {
		return taskRepository.deleteById(taskId) > 0;
	}

}
