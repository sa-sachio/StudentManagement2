package raisetech.StudentManagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;

@Mapper
public interface StudentMapper {

  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "kanaName", column = "kana_name"),
      @Result(property = "nickname", column = "nickname"),
      @Result(property = "email", column = "email"),
      @Result(property = "area", column = "area"),
      @Result(property = "age", column = "age"),
      @Result(property = "sex", column = "sex"),
      @Result(property = "remark", column = "remark"),
      @Result(property = "iDeleted", column = "i_deleted")
  })
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student getStudentById(String id);

}
