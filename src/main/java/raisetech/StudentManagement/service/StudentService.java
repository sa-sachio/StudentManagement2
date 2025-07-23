package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.StudentRepository;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import java.util.stream.Collectors;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生の一覧検索です。
   * 全件検索お行うので、条件取得は行いません。
   *
   * @return　受講生一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentsCourses> studnetsCoursesList = repository.searchStudentsCoursesList();
    return converter.convertStudentDetails(studentList, studnetsCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  public List<StudentsCourses> searchStudentsCourseList() {
    // すべてのコース情報を取得
    return repository.searchStudentsCoursesList();
  }

  public StudentDetail searchStudent(String id){
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(student.getId());
    return new StudentDetail(student,studentsCourses);
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    if (student.getIDeleted() == null) {
      student.setIDeleted(false);  // ← これが重要
    }
    // students テーブルにINSERT
    repository.insertStudent(student);
    for (StudentsCourses course : studentDetail.getCourses()) {
      course.setStudentId(student.getId());
      repository.insertCourse(course);
    }
    return studentDetail;
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
