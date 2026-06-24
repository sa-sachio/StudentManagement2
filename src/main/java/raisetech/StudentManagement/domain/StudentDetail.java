package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class StudentDetail {

  @Valid
  private Student student;
  @Valid
  private List<StudentCourse> studentCourseList;

  public Student getStudent() {
    return student;
  }

  public List<StudentCourse> getStudentCourseList() {
    return studentCourseList;
  }

  public void setStudentCourseList(List<StudentCourse> studentCourseList) {
    this.studentCourseList = studentCourseList;
  }
}
