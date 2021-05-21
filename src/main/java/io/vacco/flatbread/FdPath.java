package io.vacco.flatbread;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class FdPath implements Comparable<FdPath> {

  public final String key, attribute;
  public final String[] components;

  public Field field;
  public Object target;
  public FdPath parent;
  public String rawValue;

  public FdPath(String key) {
    this.key = Objects.requireNonNull(key);
    this.components = key.split("\\.");
    this.attribute = components[components.length - 1];
  }

  public String parentKey() {
    if (components.length == 1) return null;
    String[] parent = new String[components.length - 1];
    System.arraycopy(components, 0, parent, 0, parent.length);
    return String.join(".", parent);
  }

  public List<FdPath> seqChildren(Set<FdPath> targets) {
    String parentRegex = format("%s.\\d+", key).replace(".", "\\.");
    return targets.stream()
        .filter(k -> k.key.matches(parentRegex))
        .collect(Collectors.toList());
  }

  public FdPath withRawValue(String rv) {
    this.rawValue = rv;
    return this;
  }

  public int level() { return components.length - 1; }

  @Override public int compareTo(FdPath o) { return this.key.compareTo(o.key); }
  @Override public boolean equals(Object o) {
    return o instanceof FdPath && ((FdPath) o).key.equals(key);
  }
  @Override public int hashCode() { return key.hashCode(); }
  @Override public String toString() { return String.format("(%s) %s -> [%s, %s]", level(), key, field, target); }

}
