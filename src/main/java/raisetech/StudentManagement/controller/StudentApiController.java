package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.InvalidFullWidthCharacterException;
import raisetech.StudentManagement.service.StudentService;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class StudentApiController {

  private final StudentService service;

  public StudentApiController(StudentService service) {
    this.service = service;
  }

  // 一覧取得（API）
  @Operation(summary = "受講生一覧取得")
  @GetMapping("/students")
  public List<StudentDetail> getStudents() {
    return service.searchStudentList();
  }

  // ID検索（API）
  @Operation(summary = "受講生検索（ID指定）")
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id) {

    // 全角チェック
    if (id.matches(".*[\\u3000-\\u9FFF\\uFF00-\\uFFEF].*")) {
      throw new InvalidFullWidthCharacterException("全角文字が含まれています");
    }

    StudentDetail detail = service.searchStudent(id);
    if (detail == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "ID=" + id + " の受講生は存在しません");
    }
    return detail;
  }

  // 登録（API）
  @Operation(summary = "受講生登録")
  @PostMapping("/student")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {

    service.registerStudent(studentDetail);
    return ResponseEntity.ok(studentDetail);
  }

  // 更新（API）
  @Operation(summary = "受講生更新")
  @PutMapping("/student/{id}")
  public ResponseEntity<StudentDetail> updateStudent(
      @PathVariable String id,
      @RequestBody StudentDetail studentDetail) {

    studentDetail.getStudent().setId(id);
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(studentDetail);
  }


}
