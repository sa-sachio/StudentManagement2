package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.StudentRepository;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import java.util.stream.Collectors;


@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 全ての学生データを取得
    List<Student> allStudents = repository.search();

    // 30代の学生のみを抽出
    List<Student> filteredStudents = allStudents.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .collect(Collectors.toList());

    return filteredStudents;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    // すべてのコース情報を取得
    return repository.searchStudentsCourses();
  }

  public List<StudentsCourses> searchStudentsJavaCourseList() {
    // すべてのコース情報を取得し、JAVAコースのみを抽出
    return repository.searchStudentsCourses().stream()
        .filter(course -> "Javaコース".equals(course.getCourseName()))
        .collect(Collectors.toList());
  }


}
