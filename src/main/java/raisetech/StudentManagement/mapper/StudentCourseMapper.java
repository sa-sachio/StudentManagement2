package raisetech.StudentManagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.StudentCourse;

@Mapper
public interface StudentCourseMapper {

  @Update("""
  UPDATE students_courses
  SET course_name = #{courseName}
  WHERE id = #{id}
""")
  void update(StudentCourse course);
}
