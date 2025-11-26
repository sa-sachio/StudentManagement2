package raisetech.StudentManagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual).isNotEmpty();
    assertThat(actual.size()).isEqualTo(12);
  }

  @Test
  void ID検索が行えること() {
    Student student = sut.searchStudent("1");
    assertThat(student).isNotNull();
    assertThat(student.getId()).isEqualTo("1");
  }

  @Test
  void コース情報の全件検索が行えること() {
    List<StudentCourse> courses = sut.searchStudentCourseList();
    assertThat(courses).isNotEmpty();
  }

  @Test
  void 受講生IDに紐づくコース検索が行えること() {
    List<StudentCourse> courses = sut.searchStudentCourse("1");
    assertThat(courses).isNotEmpty();
    assertThat(courses.get(0).getStudentId()).isEqualTo("1");
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("cccc2222@example.com");
    student.setArea("奈良県");
    student.setSex("男性");
    student.setAge(30);
    student.setRemark("テスト登録");

    sut.insertStudent(student);

    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(13);
  }

  @Test
  void 受講生の更新が行えること() {
    Student student = sut.searchStudent("1");
    student.setName("名前変更テスト");
    sut.updateStudent(student);

    Student updated = sut.searchStudent("1");
    assertThat(updated.getName()).isEqualTo("名前変更テスト");
  }

  @Test
  void コースの登録が行えること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId("1");
    course.setCourseName("Java基礎");
    course.setCourseStartAt(LocalDateTime.of(2025, 1, 1, 0, 0));
    course.setCourseEndAt(LocalDateTime.of(2025, 3, 31, 0, 0));

    sut.insertCourse(course);

    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual).anyMatch(c -> c.getCourseName().equals("Java基礎"));
  }

  @Test
  void コースの更新が行えること() {
    List<StudentCourse> courses = sut.searchStudentCourse("1");
    StudentCourse course = courses.get(0);

    course.setCourseName("更新後コース名");
    sut.updateCourse(course);

    StudentCourse updated = sut.searchStudentCourse("1").get(0);
    assertThat(updated.getCourseName()).isEqualTo("更新後コース名");
  }

  @Test
  void 受講生の削除が行えること() {
    sut.deleteStudent("1");

    Student student = sut.searchStudent("1");
    assertThat(student.getIDeleted()).isTrue();
  }
}
