package com.fazil.learn_spring.learnspring_jpa.controller;

import com.fazil.learn_spring.learnspring_jpa.TimeMonitor;
import com.fazil.learn_spring.learnspring_jpa.dto.PageResponse;
import com.fazil.learn_spring.learnspring_jpa.dto.StudentRegistrationDto;
import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import com.fazil.learn_spring.learnspring_jpa.service.StudentService;
import com.fazil.learn_spring.learnspring_jpa.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Value("${default.id}")
    int defaultID;

    @Autowired
    EntityManager entityManager;

    private final  StudentRepository studentRepository;
    private final StudentService studentService;

    public StudentController(StudentRepository studentRepository, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    @Operation(summary = "Get all students", description = "Returns all students in the datasource")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "404", description = "There are no students", content = @Content)
    })
    @GetMapping("/all")
    public List<Student> getAllStudents() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter").setParameter("isDeleted", false);
        return studentRepository.findAll();
    }

    @Operation(summary = "Get all students by sorting parameters", description = "Returns a paginated list of sorted students(sort parameters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "404", description = "There are no paginated students", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("/page/all")
    public ResponseEntity<PageResponse<Student>> getPaginatedStudents(@Parameter(description = "page number", example = "0") @RequestParam(defaultValue = "0") int page,@Parameter(description = "page size", example = "10") @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "percentage") String sortByPARAMETER1, @RequestParam(defaultValue = "name") String sortByPARAMETER2) {
        Page<Student> studentPage = studentService.getAllProducts(page, size, sortByPARAMETER1, sortByPARAMETER2);
        PageResponse<Student> pageStudent = new PageResponse<>(
                studentPage.getContent(),
                studentPage.getNumber(),
                studentPage.getSize(),
                studentPage.getTotalElements(),
                studentPage.getTotalPages(),
                studentPage.isLast()
        );
        return ResponseEntity.ok(pageStudent);
    }

    @GetMapping("/page")
    public ResponseEntity<List<Student>> getStudentsByGrade(@RequestParam(defaultValue = "A") String category) {
          return new ResponseEntity<>(studentService.getStudentByStudentGrade(category), HttpStatus.OK);
    }

    @GetMapping("/pageResponse")
    public ResponseEntity<PageResponse<Student>> getStudents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(page, size));

        PageResponse<Student> pageResponse = new PageResponse<>(
                studentPage.getContent(),
                studentPage.getNumber(),
                studentPage.getSize(),
                studentPage.getTotalElements(),
                studentPage.getTotalPages(),
                studentPage.isLast()
        );
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{rollNo}")
    public ResponseEntity<Student> getStudentByRollNo(@PathVariable int rollNo) {
        Optional<Student> savedUser = studentRepository.findByRollNo(rollNo);
        return   savedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
//
//    @GetMapping("/{rollNo}")
//    public ResponseEntity<Student> getStudentByRollNo(@PathVariable int rollNo) {
//        return new ResponseEntity<>(studentService.getStudentByRollNo(rollNo), HttpStatus.OK);
//    }

//    @GetMapping()
//    public Student getStudentByName(@RequestParam String name) {
//        return studentRepository.findByStudentName(name).orElseThrow(() -> new RuntimeException("Student does not exist"));
//    }
//
//    @GetMapping
//    public List<Student> getStudentGreaterThanPercentage(@RequestParam double percentage) {
//        return studentService.findStudentBasedOnPercentage(percentage);
//    }

    @GetMapping("/{name}/{percentage}")
    public List<Student> getStudentByNameOrPercentage(@PathVariable String name, @PathVariable double percentage) {
       return studentRepository.findByNameOrPercentage(name, percentage);
    }

    @GetMapping("/NameAndStudent")
    public ResponseEntity<List<Student>> getStudentByNameAndPercentage(@RequestParam String name, @RequestParam double percentage) {
       List<Student> students = studentRepository.findByNameAndPercentage(name, percentage);
       return ResponseUtil.emptyListToNoContent(students);
    }

    @GetMapping("/list-students")
    public List<Student> findStudentByPercentageGreaterThan(@PathVariable double percentage) {
        return studentRepository.findByPercentageGreaterThan(percentage);
    }

    @PostMapping("/add")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Student> createStudent(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Student registration dto") @Valid @RequestBody StudentRegistrationDto registrationDto) {
      return new ResponseEntity<>(studentService.createStudent(registrationDto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StudentRegistrationDto> updateStudent(@PathVariable int id, @RequestBody StudentRegistrationDto studentRegistrationDto) {
        studentService.updateStudent(id, studentRegistrationDto);
        return new ResponseEntity<>(studentRegistrationDto, HttpStatus.OK);
    }

//    @DeleteMapping("/delete/{id}")
//    public void deleteStudentById(@PathVariable int id) {
//        Student student = studentRepository.findById(id).get();
//        studentRepository.delete(student);
//    }

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable int id) {
        Student student = studentRepository.findById(id).get();
         studentRepository.delete(student);
         return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") List<MultipartFile> files) {
//        studentService.upload(file);
        files.forEach(file -> studentService.upload(file));
        return ResponseEntity.ok("UPLOADED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
