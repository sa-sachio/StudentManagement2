package raisetech.StudentManagement.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import org.springframework.ui.Model;


@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studnetsCourses = service.searchStudentsCourseList();
    return converter.convertStudentDetails(students, studnetsCourses);
  }


  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if(result.hasErrors()){
      return "registerStudent";
    }
    System.out.println("studentDetail: " + studentDetail);
    System.out.println("student: " + studentDetail.getStudent());
    System.out.println("courses: " + studentDetail.getCourses());
    //System.out.println(studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    // サービスを通じて新しい学生を保存する
    service.updateStudent(studentDetail);
    return "redirect:/studentList";
  }

  @PostMapping("/updateStudent")
  public ResponseEntity<String/*StudentDetai*/> updateStudent(@RequestBody StudentDetail studentDetail) {
//    if (studentDetail.getCourses() == null || studentDetail.getCourses().isEmpty()) {
//      studentDetail.setCourses(Arrays.asList(new StudentsCourses()));
//    }
    // チェックボックスで送信された値（true/false）
//    Boolean deleted = studentDetail.getStudent().getIDeleted();
//    System.out.println("キャンセル状態（deleted）: " + deleted);
    service.updateStudent(studentDetail);
    return ResponseEntity.ok ("更新処理が成功しました。");
  }

  @GetMapping("/newStudent")
  public String newStudentForm(Model model) {
    StudentDetail detail = new StudentDetail();
    detail.setStudent(new Student());
    detail.setCourses(Arrays.asList(new StudentsCourses())); // 最低1つコースが必要な場合

    model.addAttribute("studentDetail", detail);
    return "registerStudent";
  }
  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable String id, Model model) {
    StudentDetail studentDetail = service.searchStudent(id);
    if (studentDetail.getCourses() == null || studentDetail.getCourses().isEmpty()) {
      studentDetail.setCourses(Arrays.asList(new StudentsCourses()));
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
    return service.searchStudent(id);
  }

}


