package crm06.entity;

public class RoleEntity {
	private int id;
	private String name;
	private String description;

	// Default constructor
	public RoleEntity() {
	}

	// Parameterized constructor
	public RoleEntity(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	// Getter and setter methods
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Optional: Override toString for better readability
	@Override
	public String toString() {
		return "RoleEntity{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
	}
}
