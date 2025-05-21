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

    return allStudents;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    // すべてのコース情報を取得
    return repository.searchStudentsCoursesList();
  }

  public StudentDetail searchStudent(String id){
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(student.getId());

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);

    return studentDetail;
  }


  public List<StudentsCourses> searchStudentsJavaCourseList() {
    // すべてのコース情報を取得し、JAVAコースのみを抽出
    return repository.searchStudentsCoursesList().stream()
        .filter(course -> "Javaコース".equals(course.getCourseName()))
        .collect(Collectors.toList());
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    // students テーブルにINSERT
    repository.insertStudent(student);

    // StudentDetailからコースを1件取得
    StudentsCourses course = studentDetail.getCourses().get(0); // 一人一コース前提
    course.setStudentId(student.getId()); // 自動採番されたIDをセット

    // 登録
    repository.insertCourse(course);


  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    if (student == null) {
      throw new IllegalArgumentException("studentがnullです");
    }

    repository.updateStudent(student);

    List<StudentsCourses> courses = studentDetail.getCourses();
    if (courses == null || courses.isEmpty()) {
      throw new IllegalArgumentException("coursesがnullまたは空です");
    }

    for (StudentsCourses course : courses) {
      if (course != null) {
        course.setStudentId(student.getId());
        repository.updateCourse(course);
      }
    }
  }

}
