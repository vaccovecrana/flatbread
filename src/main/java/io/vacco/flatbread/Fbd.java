package io.vacco.flatbread;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.vacco.flatbread.FdObjects.*;
import static io.vacco.flatbread.FdReflect.*;
import static java.lang.String.format;

public class Fbd<T> {

  private final Map<FdPath, FdTarget<?>> targets = new TreeMap<>();

  public Fbd(Class<T> tClass, Map<String, String> dotKeys, String keyPrefix) {
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
      targets.entrySet().stream()
          .filter(e -> e.getKey().level() == 0)
          .findFirst().ifPresent(
          e -> targets.put(e.getKey(), new FdTarget<T>("").set(tClass)));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public int pathSeqLength(FdPath p) {
    Optional<FdPath> pp = p.parent();
    if (pp.isPresent()) {
      String parentRegex = format("%s.\\d+", pp.get().key).replace(".", "\\.");
      return (int) targets.keySet().stream().filter(k -> k.key.matches(parentRegex)).count();
    }
    throw new IllegalArgumentException(format("Path [%s] has no parent path", p));
  }

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

  public void set(FdTarget<T> target, String field, String value) {
    try {
      Field f = target.getField(field);
      Object v = init(f, value);
      f.set(target, v);
    } catch (Exception e) {
      throw new IllegalStateException(
          format("Failed to set field [%s] in class [%s] with value [%s]", field, target.type, value)
      );
    }
  }

  public Collection<FdEntry> pathsAt(int level) {
    return targets.entrySet().stream()
        .filter(e -> e.getKey().level() == level)
        .map(FdEntry::of)
        .collect(Collectors.toList());
  }

  public void link(Collection<FdEntry> lp1) {
    lp1.forEach(e -> e.path.parent().ifPresent(p0 -> {
      FdTarget<?> t0 = targets.get(p0);
      FdTarget<?> t1 = e.target;
      if (t1 == null) {

        Class<?> targetType = t0.getField(e.path.attribute).getType();



        System.out.println(t1);
      }
      System.out.println(t0);
    }));
  }

  public T load() {
    int k = 0;
    Collection<FdEntry> lp0 = pathsAt(k);
    while (!lp0.isEmpty()) {
      link(lp0);
      lp0 = pathsAt(++k);
    }
    return null;
  }

}
