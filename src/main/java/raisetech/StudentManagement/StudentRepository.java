import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentSearchCondition;

@Mapper
public interface StudentRepository {

  Student findById(String id);
  List<Student> searchByCondition(StudentSearchCondition condition);
  List<Student> search();


  Student searchStudent(String id);

  List<StudentCourse> searchStudentCourseList();

  List<StudentCourse> searchStudentCourse(String studentId);

  void insertStudent(Student student);

  void insertCourse(StudentCourse course);

  void updateStudent(Student student);

  void updateCourse(StudentCourse course);

  void deleteStudent(String id);
}
