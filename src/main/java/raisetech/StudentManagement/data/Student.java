package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;


@Schema(description = "受講生詳細")
@Getter
@Setter
public class Student {

  private String id;

  @NotBlank(message = "記入してください")
  private String name;

  @NotBlank(message = "記入してください")
  private String kanaName;

  @NotBlank(message = "記入してください")
  private String nickname;

  @NotBlank(message = "記入してください")
  @Email
  private String email;

  @NotBlank(message = "記入してください")
  private String area;
  @NotNull(message = "記入してください")
  @Min(value = 0, message = "0以上を入力してください")
  private Integer age;


  @NotBlank
  private String sex;

  private String remark;


}
