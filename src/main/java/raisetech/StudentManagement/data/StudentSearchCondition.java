package raisetech.StudentManagement.data;
import jakarta.validation.constraints.Pattern;

public class StudentSearchCondition {

  @Pattern(regexp = "^[0-9]*$", message = "IDが全角です。半角数字で入力してください。")
  private String id;

  private String name;
  private String email;
  private String area;
  private String gender;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }
}
