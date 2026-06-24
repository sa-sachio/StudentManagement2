package raisetech.StudentManagement.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentSearchCondition;
import raisetech.StudentManagement.data.ApplicationStatusEntity;

@Mapper
public interface StudentMapper {

  // students
  List<Student> search(StudentSearchCondition condition);
  List<Student> searchAll();
  Student searchById(String id);

  // courses
  List<StudentCourse> searchStudentCourse(@Param("studentId") String studentId);

  // 登録
  void insertStudent(Student student);
  void insertCourse(StudentCourse course);

  // 更新
  void updateStudent(Student student);

  // 検索条件
  List<Student> searchByCondition(StudentSearchCondition condition);

  // 削除
  void deleteStudent(@Param("id") int id);

  void insertApplicationStatus(int courseId, String status);

  @Select("""
SELECT
  id,
  course_id AS courseId,
  application_status AS status
FROM students_application_status
WHERE course_id = #{studentCourseId}
""")
  ApplicationStatusEntity findByStudentCourseId(int studentCourseId);

  @Update("""
UPDATE students_application_status
SET application_status = #{status}
WHERE id = #{statusId}
""")
  void updateStatus(@Param("statusId") int statusId,
      @Param("status") String status);


  @Select("""
SELECT * FROM students_courses
""")
  List<StudentCourse> searchAllCourses();
}

