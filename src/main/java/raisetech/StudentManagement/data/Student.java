package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Getter
@Setter
public class Student {

  private String id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String area;
  private Integer age;
  private String sex;
  private String remark;
  private boolean is_deleted;

  public boolean is_deleted() {
    return is_deleted;
  }

  public void setIs_deleted(boolean deleted) {
    is_deleted = deleted;
  }

  public interface StudentMapper {

    @Results({
        @Result(property = "is_deleted", column = "is_deleted")
    })
    @Select("SELECT * FROM students WHERE id = #{id}")
    Student getStudentById(String id);
  }

}
