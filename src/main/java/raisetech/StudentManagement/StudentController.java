//package raisetech.StudentManagement.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import jakarta.validation.Valid;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import raisetech.StudentManagement.TestException;
//import raisetech.StudentManagement.data.Student;
//import raisetech.StudentManagement.data.StudentSearchCondition;
//import raisetech.StudentManagement.exception.InvalidFullWidthCharacterException;
//import raisetech.StudentManagement.data.StudentCourse;
//import raisetech.StudentManagement.domain.StudentDetail;
//import raisetech.StudentManagement.exception.InvalidFullWidthCharacterException;
//import raisetech.StudentManagement.service.StudentService;
//import org.springframework.ui.Model;
//import jakarta.validation.constraints.Size;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.http.HttpStatus;
//
///**
// *  受講生の検索や登録、更新などを行うREST API として実行されるControllerです。
// */
//@Validated
//@Controller
//public class StudentController {
//
//  private StudentService service;
//
//
//  @Autowired
//  public StudentController(StudentService service) {
//    this.service = service;
//  }
//
//  @Operation(summary = "受講生一覧取得", description = "全受講生の詳細情報を取得します。")
//  @GetMapping("/students")
//  public List<StudentDetail> getStudents() {
//    return service.searchStudentList();
//  }
//
//
//
//  /**
//   * 受講生詳細の一覧検索です。全件検索を行うので、条件検索は行いません。
//   *
//   * @return 受講生詳細一覧(全件)
//   */
//  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
// /**
//  * @GetMapping("/studentList")
//  public List<StudentDetail> getStudentList() throws TestException {
//    throw new TestException(
//        "現在のこのAPIは利用できません。URLは「studentList」ではなく「students」を利用してください。");
//  }
//  */
//
//  public void readFile(String path) throws IOException {
//    throw new IOException("ファイルが見つかりません: " + path);
//  }
//  @Operation(summary = "throwsメソッド",
//              description = "エラー時に返されるthrowsメソッドです。")
//  @GetMapping("/readFile")
//  public void triggerIOException() throws IOException {
//    readFile("dummy.txt");
//  }
//
//  /**
//   * 受講生詳細の検索です。IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
//   *
//   * @param id 受講生ID
//   * @return 受講生詳細
//   */
//
//  @Operation(
//      summary = "受講生検索（ID指定）",
//      description = "指定したIDに紐づく受講生の詳細を返します。",
//      responses = {
//          @ApiResponse(responseCode = "200", description = "成功"),
//          @ApiResponse(responseCode = "500", description = "該当する受講生が見つからない", content = @Content)
//      }
//  )
//  @GetMapping("/student/form/{id}/")
//  public StudentDetail getStudent(@PathVariable String id){
//    return service.searchStudent(id);
//  }
//
//  /**
//   * 受講生詳細の登録を行います。
//   *
//   * @param studentDetail 受講生詳細
//   * @return　実行結果
//   */
//  @Operation(
//      summary = "受講生登録",
//      description = "受講生を新規登録します。",
//      responses = {
//          @ApiResponse(responseCode = "200", description = "登録成功"),
//          @ApiResponse(responseCode = "400", description = "不正なリクエスト", content = @Content)
//      }
//  )
//  @PostMapping("/registerStudent")
//  public ResponseEntity<StudentDetail> registerStudent(
//      @RequestBody @Valid StudentDetail studentDetail) {
//
//    service.registerStudent(studentDetail);
//    return ResponseEntity.ok(studentDetail);
//  }
//
//
//  /**
//   * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います(論理削除)
//   *
//   * @param studentDetail 受講生詳細
//   * @return 実行結果
//   */
//  @Operation (summary = "受講生詳細更新", description = "受講生詳細情報の更新と、キャンセルフラグの更新を行います。(論理削除)",
//              responses = {
//                @ApiResponse(responseCode = "200", description = "更新成功")
//              }
//              )
//  @PostMapping("/updateStudent")
//  public String updateStudent(@ModelAttribute StudentDetail studentDetail, Model model) {
//    service.updateStudent(studentDetail);
//    model.addAttribute("studentDetail", studentDetail);
//    return "updateStudent";
//  }
//
//
//
//  @GetMapping("/newStudent")
//  public String newStudentForm(Model model) {
//    StudentDetail detail = new StudentDetail();
//    detail.setStudent(new Student());
//    detail.setStudentCourseList(Arrays.asList(new StudentCourse())); // 最低1つコースが必要な場合
//
//    model.addAttribute("studentDetail", detail);
//    return "registerStudent";
//  }
//
//  @Operation(
//      summary = "受講生TCP接続等表示(ID指定)",
//      description = "指定IDのTCP接続状況、文字数、書体の種類、更新日時、TCPの最大接続維持時間がわかります。",
//      responses = {
//          @ApiResponse(responseCode = "200", description = "検索成功"),
//          @ApiResponse(responseCode = "500", description = "該当する受講生が見つからない", content = @Content)
//      }
//  )
//  @GetMapping("/student/{id}")
//  public String getStudent(@PathVariable String id, Model model) {
//    StudentDetail studentDetail = service.searchStudent(id);
//    if (studentDetail.getStudentCourseList() == null || studentDetail.getStudentCourseList().isEmpty()) {
//      studentDetail.setStudentCourseList(Arrays.asList(new StudentCourse()));
//    }
//
//    // コース名一覧
//    List<String> courseNames = Arrays.asList(
//        "JavaCourse", "WebCreatingCourse", "AWSCourse", "designCourse", "marketingCourse");
//
//    model.addAttribute("studentDetail", studentDetail);
//    model.addAttribute("courseNames", courseNames); // <- 追加
//    return "updateStudent";
//  }
//
//  @Operation(
//      summary = "受講生検索メソッド",
//      description = "受講生検索(ID指定)利用時にreturnで呼び出されるメソッドです。"
//  )
//  @GetMapping("/searchStudent")
//  public StudentDetail searchStudentByQuery(@RequestParam String id) {
//    System.out.println("検索ID = " + id);
//
//    // 全角文字が含まれているかチェック
//    if (id.matches(".*[\\u3000-\\u9FFF\\uFF00-\\uFFEF].*")) {
//      throw new InvalidFullWidthCharacterException("全角文字が含まれているので無効です。");
//    }
//
//    StudentDetail detail = service.searchStudent(id);
//    if (detail == null) {
//      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID=" + id + " の受講生は存在しません");
//    }
//    return detail;
//  }
//
//  @Operation(
//      summary = "受講生更新",
//      description = "指定IDの受講生を更新します。キャンセルフラグによる論理削除も可能です。",
//      responses = {
//          @ApiResponse(responseCode = "200", description = "更新成功"),
//          @ApiResponse(responseCode = "404", description = "該当する受講生が見つからない", content = @Content)
//      }
//  )
//  @PutMapping("/student/{id}")
//  public ResponseEntity<StudentDetail> updateStudent(
//      @PathVariable String id,
//      @RequestBody StudentDetail studentDetail) {
//    studentDetail.getStudent().setId(id); // IDを設定
//    service.updateStudent(studentDetail);
//    return ResponseEntity.ok(studentDetail);
//  }
//
//  @ExceptionHandler(TestException.class)
//  public ResponseEntity<String> handleTestException(TestException ex){
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//  }
//
//  @ExceptionHandler(InvalidFullWidthCharacterException.class)
//  public ResponseEntity<String> handleInvalidFullWidthCharacter(InvalidFullWidthCharacterException ex) {
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//  }
//
//  @GetMapping("/studentList")
//  public String searchStudentList(
//      @ModelAttribute StudentSearchCondition condition,
//      Model model) {
//
//    List<StudentDetail> studentList = service.searchStudentDetailList(condition);
//    model.addAttribute("studentList", studentList);
//    model.addAttribute("condition", condition);
//    return "studentList";
//  }
//
//}
//
//
