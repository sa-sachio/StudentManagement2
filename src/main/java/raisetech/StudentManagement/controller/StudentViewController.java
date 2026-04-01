package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentSearchCondition;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;


@Controller
@RequestMapping("/view")
public class StudentViewController {

  private final StudentService service;

  public StudentViewController(StudentService service) {
    this.service = service;
  }
  @GetMapping("/top")
  public String showTop() {
    return "top";
  }

  /** 一覧画面 */
  @GetMapping("/studentList")
  public String studentList(
      @ModelAttribute("studentSearchCondition") StudentSearchCondition condition,
      @RequestParam(required = false) Boolean searched,
      Model model) {

    // 常に一覧を取得する
    List<StudentDetail> studentList =
        service.searchStudentDetailList(condition);

    model.addAttribute("studentList", studentList);

    return "studentList";
  }




  /** 新規登録画面 */
  @GetMapping("/newStudent")
  public String newStudent(Model model) {

    StudentDetail detail = new StudentDetail();
    detail.setStudent(new Student());
    detail.setStudentCourseList(
        Arrays.asList(new StudentCourse()));

    model.addAttribute("studentDetail", detail);

    return "registerStudent";
  }

  /** 詳細・更新画面 */
  @GetMapping("/student/{id}")
  public String studentDetail(@PathVariable String id, Model model) {

    StudentDetail studentDetail = service.searchStudent(id);

    if (studentDetail == null) {
      model.addAttribute("errorMessage", "該当する受講生が見つかりません");
      model.addAttribute(
          "studentList",
          service.searchStudentDetailList(new StudentSearchCondition())
      );
      return "studentList";
    }


    if (studentDetail.getStudentCourseList() == null ||
        studentDetail.getStudentCourseList().isEmpty()) {
      studentDetail.setStudentCourseList(
          Arrays.asList(new StudentCourse()));
    }

    model.addAttribute("studentDetail", studentDetail);

    return "updateStudent";
  }


  /** 更新 */
  @PostMapping("/updateStudent")
  public String updateStudent(
      @Valid @ModelAttribute StudentDetail studentDetail,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      return "updateStudent";
    }

    service.updateStudent(studentDetail);
    return "redirect:/view/studentList";
  }


  /** 登録 */
  @PostMapping("/registerStudent")
  public String registerStudent(
      @Valid @ModelAttribute StudentDetail studentDetail,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    // ★ バリデーションエラーがある場合
    if (bindingResult.hasErrors()) {
      return "registerStudent"; // 画面に戻す
    }

    service.registerStudent(studentDetail);

    redirectAttributes.addFlashAttribute("message", "登録完了しました");
    return "redirect:/view/newStudent";
  }


  public String showSearchPage(
      @RequestParam(required = false) String id,
      Model model) {

    if (id == null) {
      return "updateStudentSearch";
    }

    Student student = service.searchById(id);

    if (student == null) {
      model.addAttribute("errorMessage", "該当する受講生が見つかりません");
      return "updateStudentSearch";
    }

    model.addAttribute("student", student);
    return "updateStudentUpdate";
  }


  public String updateStudent(Student student) {
    service.updateStudent(student);
    return "redirect:/";
  }

  @GetMapping("/test")
  @ResponseBody
  public String test() {
    return "OK";
  }

  @ModelAttribute("courseNames")
  public List<String> setUpCourseNames() {
    return Arrays.asList(
        "Javaコース",
        "Web制作コース",
        "AWSコース",
        "デザインコース",
        "マーケティングコース"
    );
  }

    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam int id) {
      service.deleteStudent(id);
      return "redirect:/view/studentList";
    }

  @PostMapping("/updateStatus")
  public String updateStatus(@RequestParam int statusId,
      @RequestParam String status,
      RedirectAttributes redirectAttributes) {

    service.updateStatus(statusId, status);
    redirectAttributes.addFlashAttribute("message", "更新しました");

    return "redirect:/view/applicationStatus";
  }


  @GetMapping("/applicationStatus")
  public String showStatus(Model model) {

    List<StudentDetail> studentList =
        service.searchStudentDetailList(new StudentSearchCondition());

    // StudentCourseを平坦化
    List<StudentCourse> courses = studentList.stream()
        .flatMap(sd -> sd.getStudentCourseList().stream())
        .toList();

    model.addAttribute("courses", courses);

    return "applicationStatus";
  }

  @GetMapping("/addApplicationStatus")
  public String showAddStatus() {
    return "addApplicationStatus";
  }

  @PostMapping("/addApplicationStatus")
  public String addApplicationStatus(@RequestParam int courseId,
      @RequestParam String status,
      RedirectAttributes redirectAttributes) {

    service.addApplicationStatus(courseId, status);

    redirectAttributes.addFlashAttribute("message", "追加しました");

    return "redirect:/view/addApplicationStatus";
  }


}


