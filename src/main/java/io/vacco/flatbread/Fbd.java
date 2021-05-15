package io.vacco.flatbread;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.vacco.flatbread.FdReflect.*;
import static java.lang.String.format;

public class Fbd {

  private final Map<FdPath, FdTarget<?>> targets = new TreeMap<>();

  public Object init(Field f, String rawValue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> fType = f.getType();
    if (isPrimitiveOrWrapper(fType)) {
      fType = toWrapperClass(fType);
      Method vOf = fType.getMethod("valueOf", String.class);
      return vOf.invoke(null, rawValue);
    } else if (Enum.class.isAssignableFrom(fType)) {
      Method vOf = fType.getMethod("valueOf", Class.class, String.class);
      return vOf.invoke(null, fType, rawValue);
    } else if (String.class.isAssignableFrom(fType)) {
      return rawValue;
    }
    throw new IllegalStateException("lol");
  }

  public <T> void set(Class<T> type, T target, String field, String value) {
    try {
      Field f = type.getField(field);
      Object v = init(f, value);
      f.set(target, v);
    } catch (Exception e) {
      throw new IllegalStateException(
          format("Failed to set field [%s] in class [%s] with value [%s]", field, type, value)
      );
    }
  }

  public <T> T init(Class<T> root, FdPath path, String v) {
    System.out.printf("%s %s %s%n", root, path, v);
    // if (path.length == 1) { set(tClass, target, path[0], v); }
    return null;
  }

  public <T> T apply(Class<T> tClass, Map<String, String> dotKeys, String keyPrefix) {
    try {
      targets.putAll(
          dotKeys.entrySet().stream().collect(Collectors.toMap(
              e -> new FdPath(e.getKey().replace(format("%s.", keyPrefix), "")),
              e -> new FdTarget<>(e.getValue())
          ))
      );

      Set<FdPath> allPaths = new TreeSet<>(targets.keySet());
      targets.keySet().forEach(p -> {
        Optional<FdPath> parent = p.parent();
        while (parent.isPresent()) {
          allPaths.add(parent.get());
          parent = parent.get().parent();
        }
      });

      for (FdPath p : allPaths) {
        if (!targets.containsKey(p)) { targets.put(p, null); }
      }

      targets.entrySet().stream().filter(e -> e.getKey().level() == 0).findFirst().ifPresent(e -> {
        targets.put(e.getKey(), new FdTarget<T>("").set(tClass));
      });

      return null;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
