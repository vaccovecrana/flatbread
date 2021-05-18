package io.vacco.flatbread;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FdTarget<T> {

  public String rawValue;

  public Class<T> type;
  public T value;
  public Map<String, Field> uncasedFields;

  public FdTarget(String rawValue) {
    this.rawValue = rawValue;
  }

  public Field getField(String anyCaseName) {
    return uncasedFields == null
        ? null
        : uncasedFields.get(anyCaseName.toLowerCase());
  }

  public FdTarget<T> set(Class<T> type) {
    try {
      this.type = type;
      this.value = type.getConstructor().newInstance();
      this.uncasedFields = new TreeMap<>(
          Arrays.stream(type.getFields()).collect(Collectors.toMap(
              f -> f.getName().toLowerCase(), Function.identity()
          ))
      );
      return this;
    } catch (Exception e) { throw new IllegalStateException(e); }
  }

  @Override public String toString() { return rawValue; }
}
