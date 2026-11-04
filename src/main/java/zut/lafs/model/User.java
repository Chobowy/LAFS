package zut.lafs.model;

public class User {
    private String studentId;
    private String name;
    private Role role;

    public User(String studentId, String name, Role role) {
        this.studentId = studentId;
        this.name = name;
        this.role = role;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public Role getRole() { return role; }
}
