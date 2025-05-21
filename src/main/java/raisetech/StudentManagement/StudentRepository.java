package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = false")
  @Results(id = "studentResultMap", value ={
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "kanaName", column = "kana_name"),
      @Result(property = "nickname", column = "nickname"),
      @Result(property = "email", column = "email"),
      @Result(property = "area", column = "area"),
      @Result(property = "age", column = "age"),
      @Result(property = "sex", column = "sex"),
      @Result(property = "remark", column = "remark"),
      @Result(property = "is_deleted", column = "is_deleted")
  })
  List<Student> search();

  @Select("SELECT * FROM students WHERE id = #{id}")
  @ResultMap("studentResultMap")
  Student searchStudent(String id);

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCoursesList();

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentsCourses(String studentId);

  @Insert("INSERT INTO students (name, kana_name, nickname, email, area, age, sex, remark, is_deleted) " +
      "VALUES (#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark}, #{is_deleted})")
  void insertStudent(Student student);

  @Insert("INSERT INTO students_courses (student_id, course_name, course_start_at, course_end_at) " +
      "VALUES (#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  void insertCourse(StudentsCourses course);

  @Update("UPDATE students SET name=#{name}, kana_name=#{kanaName}, nickname=#{nickname}, " +
      "email=#{email}, area=#{area}, age=#{age}, sex=#{sex}, remark=#{remark}, is_deleted=#{is_deleted} " +
      "WHERE id = #{id}")
  void updateStudent(Student student);

  @Update("UPDATE students_courses SET course_name = #{courseName}, course_start_at = #{courseStartAt}, course_end_at = #{courseEndAt} WHERE id = #{id}")
  void updateCourse(StudentsCourses course);

  @Update("UPDATE students SET is_deleted = true WHERE id = #{id}")
  void deleteStudent(String id);
}
