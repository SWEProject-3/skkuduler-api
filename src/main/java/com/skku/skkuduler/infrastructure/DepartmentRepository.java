package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
