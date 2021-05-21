package io.vacco.flatbread;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.vacco.flatbread.FdReflect.*;

public class FdObjects {

  public static List<FdPath> pathsAt(int level, Set<FdPath> targets) {
    return targets.stream()
        .filter(e -> e.level() == level)
        .collect(Collectors.toList());
  }

  public static boolean isInteger(String strNum) {
    if (strNum == null) { return false; }
    try { Integer.parseInt(strNum); }
    catch (NumberFormatException nfe) { return false; }
    return true;
  }

  public static Optional<Field> getField(Class<?> c, String fName) {
    return Arrays.stream(c.getFields())
        .filter(f -> f.getName().equalsIgnoreCase(fName))
        .findFirst();
  }

  public static Object newOf(Class<?> c) {
    try {
      return c.getConstructor().newInstance();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static void assign(Field f, Object o, Object v) {
    try {
      if (o != null) { f.set(o, v); }
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static Optional<Object> instance(Class<?> fType, String rawValue) {
    try {
      if (isPrimitiveOrWrapper(fType)) {
        fType = toWrapperClass(fType);
        Method vOf = fType.getMethod("valueOf", String.class);
        return Optional.of(vOf.invoke(null, rawValue));
      } else if (Enum.class.isAssignableFrom(fType)) {
        Method vOf = fType.getMethod("valueOf", Class.class, String.class);
        return Optional.of(vOf.invoke(null, fType, rawValue));
      } else if (String.class.isAssignableFrom(fType)) {
        return Optional.of(rawValue);
      } else if (isList(fType)) {
        return Optional.of(new ArrayList<>());
      } else if (isMap(fType)) {
        return Optional.of(new LinkedHashMap<>());
      } else if (!fType.isArray()) {
        return Optional.of(newOf(fType));
      }
      return Optional.empty();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
