package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.TestException;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import org.springframework.ui.Model;
import jakarta.validation.constraints.Size;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

/**
 *  受講生の検索や登録、更新などを行うREST API として実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;


  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。全件検索を行うので、条件検索は行いません。
   *
   * @return 受講生詳細一覧(全件)
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() throws TestException {
    throw new TestException(
        "現在のこのAPIは利用できません。URLは「studentList」ではなく「students」を利用してください。");
  }

  public void readFile(String path) throws IOException {
    throw new IOException("ファイルが見つかりません: " + path);
  }

  @GetMapping("/readFile")
  public void triggerIOException() throws IOException {
    readFile("dummy.txt");
  }

  /**
   * 受講生詳細の検索です。IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/student/form/{id}/")
  public StudentDetail getStudent(@PathVariable String id){
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return　実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
//    if(result.hasErrors()){
//      return "registerStudent";
//    }
//    System.out.println("studentDetail: " + studentDetail);
//    System.out.println("student: " + studentDetail.getStudent());
//    System.out.println("courses: " + studentDetail.getCourses());
    //System.out.println(studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    // サービスを通じて新しい学生を保存する
//    service.updateStudent(studentDetail);
    StudentDetail savedDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(savedDetail);
  }

  /**
   * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います(論理削除)
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, Model model) {
    service.updateStudent(studentDetail);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent"; // または "redirect:/somewhere"
  }

  /**
  @GetMapping("/newStudent")
  public String newStudentForm(Model model) {
    StudentDetail detail = new StudentDetail();
    detail.setStudent(new Student());
    detail.setCourses(Arrays.asList(new StudentCourse())); // 最低1つコースが必要な場合

    model.addAttribute("studentDetail", detail);
    return "registerStudent";
  }
   */
  
  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable String id, Model model) {
    StudentDetail studentDetail = service.searchStudent(id);
    if (studentDetail.getStudentCourseList() == null || studentDetail.getStudentCourseList().isEmpty()) {
      studentDetail.setStudentCourseList(Arrays.asList(new StudentCourse()));
    }

    // コース名一覧
    List<String> courseNames = Arrays.asList(
        "JavaCourse", "WebCreatingCourse", "AWSCourse", "designCourse", "marketingCourse");

    model.addAttribute("studentDetail", studentDetail);
    model.addAttribute("courseNames", courseNames); // <- 追加
    return "updateStudent";
  }

  @GetMapping("/searchStudent")
  public StudentDetail searchStudentByQuery(@RequestParam String id) {
    System.out.println("検索ID = " + id);
    StudentDetail detail = service.searchStudent(id);
    if (detail == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID=" + id + " の受講生は存在しません");
    }
    return detail;
  }

  @PutMapping("/student/{id}")
  public ResponseEntity<StudentDetail> updateStudent(
      @PathVariable String id,
      @RequestBody StudentDetail studentDetail) {
    studentDetail.getStudent().setId(id); // IDを設定
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(studentDetail);
  }

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}


