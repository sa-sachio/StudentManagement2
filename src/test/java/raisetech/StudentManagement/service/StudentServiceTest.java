package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.StudentRepository;
import raisetech.StudentManagement.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before(){
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること(){
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList,studentCourseList);
  }

  @Test
  void 受講生検索_IDに紐づく受講生とコース情報が取得できること() {
    // --- 準備 ---
    String id = "1";
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");

    StudentCourse course = new StudentCourse();
    course.setStudentId("1");
    course.setCourseName("Java基礎");

    List<StudentCourse> courseList = new ArrayList<>();
    courseList.add(course);

    // モック設定
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(courseList);

    // --- 実行 ---
    StudentDetail result = sut.searchStudent(id);

    // --- 検証 ---
    assertNotNull(result);
    assertEquals(student, result.getStudent());
    assertEquals(1, result.getStudentCourseList().size());
    assertEquals("Java基礎", result.getStudentCourseList().get(0).getCourseName());

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(student.getId());
  }

  @Test
  void 受講生検索_存在しないIDの場合は例外が発生すること() {
    // --- 準備 ---
    String id = "999";
    when(repository.searchStudent(id)).thenReturn(null);

    // --- 実行・検証 ---
    assertThrows(NullPointerException.class, () -> sut.searchStudent(id));

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(0)).searchStudentCourse(Mockito.anyString());
  }

  @Test
  void 受講生登録_IDeletedがnullのときfalseが設定され_リポジトリが正しく呼ばれること() {
    // --- 準備 ---
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");
    student.setIDeleted(null);  // null の場合、false に補正される想定

    StudentCourse course1 = new StudentCourse();
    course1.setCourseName("Java基礎");

    StudentCourse course2 = new StudentCourse();
    course2.setCourseName("Spring入門");

    List<StudentCourse> courseList = new ArrayList<>();
    courseList.add(course1);
    courseList.add(course2);

    StudentDetail studentDetail = new StudentDetail(student, courseList);

    // モック設定：insertStudent() と insertCourse() は void メソッドなので何も返さない
    Mockito.doNothing().when(repository).insertStudent(student);
    Mockito.doNothing().when(repository).insertCourse(Mockito.any(StudentCourse.class));

    // --- 実行 ---
    StudentDetail result = sut.registerStudent(studentDetail);

    // --- 検証 ---
    // IDeletedがfalseに補正されていること
    assertFalse(student.getIDeleted());

    // コースにstudentIdが設定されていること
    assertEquals("1", course1.getStudentId());
    assertEquals("1", course2.getStudentId());

    // リポジトリ呼び出し検証
    verify(repository, times(1)).insertStudent(student);
    verify(repository, times(2)).insertCourse(Mockito.any(StudentCourse.class));

    // 戻り値が同じオブジェクトであること
    assertSame(studentDetail, result);
  }

  @Test
  void 受講生登録_studentがnullの場合はNullPointerExceptionが発生すること() {
    StudentDetail detail = new StudentDetail(null, new ArrayList<>());

    assertThrows(NullPointerException.class, () -> sut.registerStudent(detail));
  }


  @Test
  void 受講生更新_受講生とコースが正常に更新されること() {
    // --- 準備 ---
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");

    StudentCourse course1 = new StudentCourse();
    course1.setCourseName("Java基礎");
    StudentCourse course2 = new StudentCourse();
    course2.setCourseName("Spring入門");

    List<StudentCourse> courses = new ArrayList<>();
    courses.add(course1);
    courses.add(course2);

    StudentDetail detail = new StudentDetail(student, courses);

    // モック設定（voidメソッドなのでdoNothingでOK）
    Mockito.doNothing().when(repository).updateStudent(student);
    Mockito.doNothing().when(repository).updateCourse(Mockito.any(StudentCourse.class));

    // --- 実行 ---
    sut.updateStudent(detail);

    // --- 検証 ---
    // studentIdが正しくセットされている
    assertEquals("1", course1.getStudentId());
    assertEquals("1", course2.getStudentId());

    // updateStudentが呼ばれている
    verify(repository, times(1)).updateStudent(student);
    // updateCourseが2回呼ばれている
    verify(repository, times(2)).updateCourse(Mockito.any(StudentCourse.class));
  }

  @Test
  void 受講生更新_studentがnullの場合は例外が発生すること() {
    // --- 準備 ---
    StudentDetail detail = new StudentDetail(null, new ArrayList<>());

    // --- 実行・検証 ---
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> sut.updateStudent(detail));

    assertEquals("studentがnullです", exception.getMessage());
    verify(repository, times(0)).updateStudent(Mockito.any());
    verify(repository, times(0)).updateCourse(Mockito.any());
  }

  @Test
  void 受講生更新_coursesがnullの場合は例外が発生すること() {
    // --- 準備 ---
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");

    StudentDetail detail = new StudentDetail(student, null);

    // --- 実行・検証 ---
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> sut.updateStudent(detail));

    assertEquals("coursesがnullまたは空です", exception.getMessage());
    verify(repository, times(1)).updateStudent(student); // studentは更新される
    verify(repository, times(0)).updateCourse(Mockito.any());
  }

  @Test
  void 受講生更新_coursesが空の場合は例外が発生すること() {
    // --- 準備 ---
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");

    StudentDetail detail = new StudentDetail(student, new ArrayList<>());

    // --- 実行・検証 ---
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> sut.updateStudent(detail));

    assertEquals("coursesがnullまたは空です", exception.getMessage());
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(0)).updateCourse(Mockito.any());
  }


}