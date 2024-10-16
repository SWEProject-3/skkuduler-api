package com.skku.skkuduler.common.exception;

public class DepartmentNotFoundException extends RuntimeException {
  public DepartmentNotFoundException() {
    super("department does not exist");
  }
}
