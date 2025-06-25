package com.practice.practice.service;

import com.practice.practice.entity.Student;
import com.practice.practice.exception.InvalidRequestException;
import com.practice.practice.exception.ResourceNotFoundException;
import com.practice.practice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public Page<Student> getPaginatedStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return repository.findAll(pageable);
    }

    public Student createStudent(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new InvalidRequestException("Name cannot be empty.");
        }
        return repository.save(student);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student updateStudent(Integer id, Student studentDetails) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        student.setName(studentDetails.getName());
        student.setAge(studentDetails.getAge());
        return repository.save(student);
    }

    public void deleteStudent(Integer id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete. Student not found with id: " + id));
        repository.delete(student);
    }
}
