package raisetech.StudentManagement.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import org.springframework.ui.Model;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studnetsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studnetsCourses));
    return "studentList";
  }

  @GetMapping("/studentsCourseList")
  public List<StudentsCourses> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    //model.addAttribute("studentDetail", new StudentDetail());
    StudentDetail detail = new StudentDetail();
    detail.setStudentsCourses(Arrays.asList(new StudentsCourses())); // ← ここを修正
    detail.setStudent(new Student()); // ← これを追加して、nullを防ぐ
    model.addAttribute("studentDetail", detail);
    return "registerStudent";
  }

  /*
  @GetMapping("/java-courses")
  public List<StudentsCourses> getJavaCourses() {
    return service.searchStudentsJavaCourseList();
  }
  */

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
    service.registerStudent(studentDetail);

    return "redirect:/studentList";
  }
}


