package com.practice.practice.controller;

import com.practice.practice.entity.Student;
import com.practice.practice.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/MyStudent")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/paginated")
    public Page<Student> getPaginatedStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return service.getPaginatedStudents(page, size);
    }

    @GetMapping
    public List<Student> getAll(){
        return service.getAllStudents();
    }
    @PostMapping
    public Student saveDetails(@RequestBody Student student){
        return service.createStudent(student);
    }
    @PutMapping("/{id}")
    public Student StudentupdateDetails(@PathVariable Integer id, @RequestBody Student student){

        return service.updateStudent(id,student);
    }
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id){
        service.deleteStudent(id);
    }


}
