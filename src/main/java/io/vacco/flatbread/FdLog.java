package io.vacco.flatbread;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.*;

import static java.lang.String.format;
import static java.util.Arrays.stream;

import static io.vacco.flatbread.FdReflect.*;

public class FdLog {

  private static final String objFormat = "{%n%s%n%s}";

  private String indent(int level) {
    return " ".repeat(Math.max(0, 2 * level));
  }

  private String fieldValueOf(String fName, Object v, int level) {
    String val = doApply(v, level);
    return format(
        "%s%s: %s", indent(level), fName,
        fName.toLowerCase().contains("fdmask") ? format("\"%s\"", "*".repeat(val.length())) : val
    );
  }

  private String fieldValueOf(Field f, Object v, int level) {
    return fieldValueOf(f.getName(), v, level);
  }

  private String seqValueOf(Object o, int level) {
    Stream<?> os = isArray(o) ? stream(wrap(o)) : ((Collection<?>) o).stream();
    return format("[%s]", os.map(i -> doApply(i, level)).collect(Collectors.joining(", ")));
  }

  private Object extract(Object o, Field f) {
    try { return f.get(o); }
    catch (IllegalAccessException e) { throw new IllegalStateException(e); }
  }

  public String doApply(Object o, int level) {
    if (o == null) return "null";
    if (isPrimitiveOrWrapper(o.getClass())) return o.toString();
    if (isTextual(o) || isEnum(o)) return format("\"%s\"", o);
    if (isCollection(o)) {
      if (isList(o) || isSet(o) || isArray(o)) return seqValueOf(o, level);
      Map<?, ?> m = (Map<?, ?>) o;
      return format(objFormat,
          m.entrySet().stream()
              .map(e -> fieldValueOf(e.getKey().toString(), e.getValue(), level + 1))
              .collect(Collectors.joining(",\n")),
          indent(level)
      );
    }
    Class<?> cl = o.getClass();
    return format(objFormat,
        stream(cl.getFields())
            .map(f -> fieldValueOf(f, extract(o, f), level + 1))
            .collect(Collectors.joining(",\n")),
        indent(level)
    );
  }

  public String apply(Object o) {
    return doApply(o, 0);
  }

}
