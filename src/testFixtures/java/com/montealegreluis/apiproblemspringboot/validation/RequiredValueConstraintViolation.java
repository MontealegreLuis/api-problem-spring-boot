package com.montealegreluis.apiproblemspringboot.validation;

import java.util.Iterator;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

public final class RequiredValueConstraintViolation implements ConstraintViolation<String> {
  private final String propertyPath;

  public RequiredValueConstraintViolation(String propertyPath) {
    this.propertyPath = propertyPath;
  }

  @Override
  public String getMessage() {
    return "value is required";
  }

  @Override
  public Path getPropertyPath() {
    return new Path() {
      @Override
      public Iterator<Node> iterator() {
        return null;
      }

      @Override
      public String toString() {
        return propertyPath;
      }
    };
  }

  @Override
  public String getMessageTemplate() {
    return null;
  }

  @Override
  public String getRootBean() {
    return null;
  }

  @Override
  public Class<String> getRootBeanClass() {
    return null;
  }

  @Override
  public Object getLeafBean() {
    return null;
  }

  @Override
  public Object[] getExecutableParameters() {
    return new Object[0];
  }

  @Override
  public Object getExecutableReturnValue() {
    return null;
  }

  @Override
  public Object getInvalidValue() {
    return null;
  }

  @Override
  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return null;
  }

  @Override
  public <U> U unwrap(Class<U> type) {
    return null;
  }
}
