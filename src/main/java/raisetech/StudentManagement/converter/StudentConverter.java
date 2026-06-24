package raisetech.StudentManagement.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentConverter {

  /**
   * 一覧用：Student + Course List → StudentDetail List
   */
  public List<StudentDetail> convertStudentDetails(
      List<Student> studentList,
      List<StudentCourse> studentCourseList) {

    List<StudentDetail> studentDetails = new ArrayList<>();

    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> courses = studentCourseList.stream()
          .filter(course -> student.getId().equals(course.getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(courses);
      studentDetails.add(studentDetail);
    });

    return studentDetails;
  }

  /**
   * 単体用：Student + Course → StudentDetail
   */
  public StudentDetail toStudentDetail(
      Student student,
      List<StudentCourse> courses) {

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentCourseList(courses);
    return detail;
  }
}
