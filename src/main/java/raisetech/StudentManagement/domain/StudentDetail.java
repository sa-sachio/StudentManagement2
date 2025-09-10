package raisetech.StudentManagement.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourseList;

  // getter / setter

  //public Student getStudent() {
    //return student;
  }

  /**
   * public void setStudent(Student student) {
    this.student = student;
  }

  public List<StudentsCourses> getCourses() {
    return courses;
  }

  public void setCourses(List<StudentsCourses> courses) {
    this.courses = courses;
  }

  public void setStudentsCourses(List<StudentsCourses> courses) {
    this.courses = courses;
  }

}
   */
