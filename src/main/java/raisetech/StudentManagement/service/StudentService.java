package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentSearchCondition;
import raisetech.StudentManagement.data.ApplicationStatusEntity;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.mapper.StudentCourseMapper;
import raisetech.StudentManagement.mapper.StudentMapper;
import raisetech.StudentManagement.converter.StudentConverter;

@Service
public class StudentService {

  private final StudentMapper mapper;
  private final StudentCourseMapper courseMapper;
  private final StudentConverter converter;

  public StudentService(StudentMapper mapper,
      StudentCourseMapper courseMapper,
      StudentConverter converter) {
    this.mapper = mapper;
    this.courseMapper = courseMapper;
    this.converter = converter;
  }

  // ★ 受講生詳細（コース込み）
  public StudentDetail searchStudent(String id) {
    Student student = mapper.searchById(id);
    if (student == null) {
      return null;
    }

    List<StudentCourse> courses = mapper.searchStudentCourse(id);

    if (courses == null || courses.isEmpty()) {
      courses = List.of(new StudentCourse());
    }

    return converter.toStudentDetail(student, courses);
  }

  // ★ 受講生単体取得（ID検索用）
  public Student searchById(String id) {
    return mapper.searchById(id);
  }

  // --- 新規登録 ---
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    mapper.insertStudent(student);

    if (studentDetail.getStudentCourseList() != null) {
      for (StudentCourse course : studentDetail.getStudentCourseList()) {
        course.setStudentId(student.getId());
        mapper.insertCourse(course);
      }
    }
  }

  // --- 更新 ---
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    Student student = studentDetail.getStudent();
    mapper.updateStudent(student);

    if (studentDetail.getStudentCourseList() != null
        && !studentDetail.getStudentCourseList().isEmpty()) {

      StudentCourse course = studentDetail.getStudentCourseList().get(0);
      course.setStudentId(student.getId());
      courseMapper.update(course);
    }
  }

  public List<StudentDetail> searchStudentList() {
    List<Student> students = mapper.searchAll();
    return students.stream()
        .map(student -> {
          List<StudentCourse> courses =
              mapper.searchStudentCourse(student.getId());
          return converter.toStudentDetail(student, courses);
        })
        .toList();
  }

  public List<StudentDetail> searchStudentDetailList(StudentSearchCondition condition) {
    List<Student> students = mapper.search(condition);

    return students.stream()
        .map(student -> {
          List<StudentCourse> courses =
              mapper.searchStudentCourse(student.getId());
          return converter.toStudentDetail(student, courses);
        })
        .toList();
  }

  @Transactional
  public void updateStudent(Student student) {
    mapper.updateStudent(student);
  }
  @Transactional
  public void deleteStudent(int id) {
    mapper.deleteStudent(id);
  }

  @Transactional
  public void updateStatus(int statusId, String status) {
    mapper.updateStatus(statusId, status);
  }

  @Transactional
  public void addApplicationStatus(int courseId, String status) {
    mapper.insertApplicationStatus(courseId, status);
  }


}

