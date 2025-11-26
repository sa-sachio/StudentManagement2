package raisetech.StudentManagement.controller.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut; // System Under Test（テスト対象）

  @BeforeEach
  void setUp() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生ごとに対応するコースが正しくマッピングされること() {
    // --- 準備 ---
    Student student1 = createStudent();

    Student student2 = new Student();
    student2.setId("2");
    student2.setName("佐藤花子");

    List<Student> students = List.of(student1, student2);

    StudentCourse course1 = new StudentCourse();
    course1.setStudentId("1");
    course1.setCourseName("Java基礎");

    StudentCourse course2 = new StudentCourse();
    course2.setStudentId("1");
    course2.setCourseName("Spring入門");

    StudentCourse course3 = new StudentCourse();
    course3.setStudentId("2");
    course3.setCourseName("Python基礎");

    List<StudentCourse> courses = List.of(course1, course2, course3);

    // --- 実行 ---
    List<StudentDetail> result = sut.convertStudentDetails(students, courses);

    // --- 検証 ---
    assertEquals(2, result.size());

    // 田中太郎（ID=1）のコースを確認
    StudentDetail detail1 = result.stream()
        .filter(d -> d.getStudent().getId().equals("1"))
        .findFirst().orElseThrow();
    assertEquals(2, detail1.getStudentCourseList().size());
    assertTrue(detail1.getStudentCourseList().stream()
        .anyMatch(c -> c.getCourseName().equals("Java基礎")));

    // 佐藤花子（ID=2）のコースを確認
    StudentDetail detail2 = result.stream()
        .filter(d -> d.getStudent().getId().equals("2"))
        .findFirst().orElseThrow();
    assertEquals(1, detail2.getStudentCourseList().size());
    assertEquals("Python基礎", detail2.getStudentCourseList().get(0).getCourseName());
  }

  @Test
  void 受講生リストが空なら空の結果が返ること() {
    List<Student> emptyStudents = new ArrayList<>();
    List<StudentCourse> courses = new ArrayList<>();

    List<StudentDetail> result = sut.convertStudentDetails(emptyStudents, courses);

    assertTrue(result.isEmpty());
  }

  @Test
  void コースリストが空でも受講生情報は返ること() {
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");

    List<Student> students = List.of(student);
    List<StudentCourse> emptyCourses = new ArrayList<>();

    List<StudentDetail> result = sut.convertStudentDetails(students, emptyCourses);

    assertEquals(1, result.size());
    assertEquals(student, result.get(0).getStudent());
    assertTrue(result.get(0).getStudentCourseList().isEmpty());
  }

  @Test
  void 該当する受講生が存在しない場合はマッピングされないこと() {
    Student student = new Student();
    student.setId("999");
    student.setName("山田一郎");

    List<Student> students = List.of(student);

    StudentCourse course = new StudentCourse();
    course.setStudentId("1");
    course.setCourseName("Java基礎");

    List<StudentCourse> courses = List.of(course);

    List<StudentDetail> result = sut.convertStudentDetails(students, courses);

    assertEquals(1, result.size());
    assertTrue(result.get(0).getStudentCourseList().isEmpty());
  }
  private static Student createStudent() {
    Student student1 = new Student();
    student1.setId("1");
    student1.setName("田中太郎");
    return student1;
  }
}
