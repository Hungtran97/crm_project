package crm06.entity;

import java.sql.Date;

public class TaskEntity {
	private int id;
	private UserEntity user;
	private ProjectEntity project;
	private String name;
	private StatusEntity status;
	private Date startDate;
	private Date endDate;

	// Default constructor
	public TaskEntity() {
	}

	// Parameterized constructor

	public TaskEntity(int id, UserEntity user, ProjectEntity project, String name, StatusEntity status, Date startDate,
			Date endDate) {
		super();
		this.id = id;
		this.user = user;
		this.project = project;
		this.name = name;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// Getter and setter methods
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public ProjectEntity getProject() {
		return project;
	}

	public void setProject(ProjectEntity project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusEntity getStatus() {
		return status;
	}

	public void setStatus(StatusEntity status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
