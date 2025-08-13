package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.StudentRepository;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
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
   * @return　受講生詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studnetsCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studnetsCourseList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  public List<StudentCourse> searchStudentCourseList()
  {
    // すべてのコース情報を取得
    return repository.searchStudentCourseList();
  }

  public StudentDetail searchStudent(String id){
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());
    return new StudentDetail(student, studentCourse);
  }

  /**
   * 受講生詳細の登録を行います。受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    if (student.getIDeleted() == null) {
      student.setIDeleted(false);  // ← これが重要
    }
    // students テーブルにINSERT
    repository.insertStudent(student);
    for (StudentCourse course : studentDetail.getStudentCourseList()) {
      course.setStudentId(student.getId());
      repository.insertCourse(course);
    }
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentsCourse 受講生コース情報
   * @param student 受講生
   */

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    if (student == null) {
      throw new IllegalArgumentException("studentがnullです");
    }

    repository.updateStudent(student);

    List<StudentCourse> courses = studentDetail.getStudentCourseList();
    if (courses == null || courses.isEmpty()) {
      throw new IllegalArgumentException("coursesがnullまたは空です");
    }

    for (StudentCourse course : courses) {
      if (course != null) {
        course.setStudentId(student.getId());
        repository.updateCourse(course);
      }
    }

    /**
     * 受講生詳細の更新を行います。受講生と受講生コース情報をそれぞれ更新します。
     *
     * @param studentDetail 受講生詳細
     */
  }

}
