package io.vacco.flatbread;

import java.lang.reflect.Array;
import java.util.*;
import static io.vacco.flatbread.FdObjects.*;
import static io.vacco.flatbread.FdReflect.*;
import static java.lang.String.format;

public class Fbd<T> {

  public final Class<T> rootClass;
  public final Set<FdPath> paths = new TreeSet<>();

  public Fbd(Class<T> tClass, Map<String, String> dotKeys, String keyPrefix) {
    try {
      this.rootClass = Objects.requireNonNull(tClass);
      dotKeys.forEach((k, v) -> paths.add(
          new FdPath(k.replace(format("%s.", keyPrefix), "")).withRawValue(v))
      );
      new TreeSet<>(paths).forEach(p -> {
        String pKey = p.parentKey();
        while (pKey != null) {
          FdPath parent = new FdPath(pKey);
          paths.add(parent);
          pKey = parent.parentKey();
        }
      });
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void init(FdPath p0, FdPath p1) {
    p1.parent = p0;
    if (isMap(p0.target)) {
      instance(((Class<?>) genericTypesOf(p0.field)[1]), p1.rawValue)
          .ifPresent(v -> ((Map) p0.target).put(p1.attribute, v));
    } else if (isInteger(p0.attribute)) {
      getField(p0.target.getClass(), p1.attribute)
          .ifPresent(f -> instance(f.getType(), p1.rawValue)
              .ifPresent(o -> assign(f, p0.target, o)));
    } else if (isInteger(p1.attribute)) {
      if (isList(p0.target)) {
        instance(((Class<?>) genericTypesOf(p0.field)[0]), p1.rawValue).ifPresent(o -> {
          ((List) p0.target).add(Integer.parseInt(p1.attribute), o);
          p1.target = o;
        });
      } else if (p0.field.getType().isArray()) {
        if (p0.target == null) {
          p0.target = Array.newInstance(p0.field.getType().getComponentType(), p0.seqChildren(paths).size());
          assign(p0.field, p0.parent.target, p0.target);
        }
        instance(p0.target.getClass().getComponentType(), p1.rawValue).ifPresent(o -> {
          arrSet(Integer.parseInt(p1.attribute), p0.target, o);
          p1.target = o;
        });
      }
    } else {
      Class<?> pc = p0.target != null ?  p0.target.getClass() : p0.field.getType();
      getField(pc, p1.attribute).ifPresent(f -> {
        p1.field = f;
        instance(p1.field.getType(), p1.rawValue).ifPresent(o -> {
          p1.target = o;
          assign(p1.field, p0.target, p1.target);
        });
      });
    }
  }

  @SuppressWarnings("unchecked")
  public T load() {
    int k = 0;
    List<FdPath> lPaths = pathsAt(k, paths);
    while (!lPaths.isEmpty()) {
      if (lPaths.size() == 1 && lPaths.get(0).level() == 0) {
        lPaths.get(0).target = newOf(rootClass);
      } else {
        lPaths.stream()
            .filter(p -> p.parentKey() != null)
            .forEach(p -> paths.stream()
                .filter(p0 -> p0.key.equals(p.parentKey()))
                .findFirst().ifPresent(pr -> init(pr, p))
            );
      }
      lPaths = pathsAt(++k, paths);
    }
    return (T) pathsAt(0, paths).get(0).target;
  }

}
