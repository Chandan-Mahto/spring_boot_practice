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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Student> getPaginatedStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return repository.findAll(pageable);
    }

    public Student createStudent(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new InvalidRequestException("Name cannot be empty.");
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return repository.save(student);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student updateStudent(Integer id, Student studentDetails) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Safely update username if provided and unique
        String newUsername = studentDetails.getUsername();
        if (newUsername != null && !newUsername.isBlank()) {
            String currentUsername = student.getUsername();
            if ((currentUsername == null || !currentUsername.equals(newUsername)) &&
                    repository.findByUsername(newUsername).isPresent()) {
                throw new InvalidRequestException("Username '" + newUsername + "' is already taken.");
            }
            student.setUsername(newUsername);
        }

        // Safely update password if provided
        if (studentDetails.getPassword() != null && !studentDetails.getPassword().isBlank()) {
            student.setPassword(passwordEncoder.encode(studentDetails.getPassword()));
        }

        // Update name if provided
        if (studentDetails.getName() != null && !studentDetails.getName().isBlank()) {
            student.setName(studentDetails.getName());
        }

        // Update age if provided
        if (studentDetails.getAge() != null) {
            student.setAge(studentDetails.getAge());
        }

        return repository.save(student);
    }

    public void deleteStudent(Integer id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete. Student not found with id: " + id));
        repository.delete(student);
    }
}
