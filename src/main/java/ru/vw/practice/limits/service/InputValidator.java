package ru.vw.practice.limits.service;

public interface InputValidator {
  <T> void checkValidationErrors(T item);
}
