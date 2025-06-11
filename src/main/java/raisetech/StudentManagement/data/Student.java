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
  private Boolean iDeleted;

  public Boolean getIDeleted() {
    return iDeleted;
  }

  public void setIDeleted(Boolean iDeleted) {
    this.iDeleted = iDeleted;
  }
}
