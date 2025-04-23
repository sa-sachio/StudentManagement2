package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

    @Insert(
        "INSERT INTO students (name, kana_name, nickname, email, area, age, sex, remark)"
      + " VALUES (#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  @Insert("INSERT INTO students_courses (student_id, course_name, course_start_at, course_end_at) "
      + "VALUES (#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  void insertCourse(StudentsCourses course);
}

