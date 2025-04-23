package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.StudentRepository;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import java.util.stream.Collectors;
import raisetech.StudentManagement.domain.StudentDetail;


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
//    List<Student> filteredStudents = allStudents.stream()
//        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
//        .collect(Collectors.toList());

    return allStudents;
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

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    // students テーブルにINSERT
    repository.insertStudent(student);

    // StudentDetailからコース一覧を取得（↓このために後述のgetterが必要）
    List<StudentsCourses> courses = studentDetail.getCourses();

    // 各コースに studentId をセットし、登録
    for (StudentsCourses course : courses) {
      course.setStudentId(student.getId()); // 自動採番されたIDをセット
      repository.insertCourse(course);
    }
  }

}
