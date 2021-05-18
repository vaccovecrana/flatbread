package io.vacco.flatbread;

import java.util.*;

public class FdPath implements Comparable<FdPath> {

  public final String key, attribute;
  public final String[] components;

  public FdPath(String key) {
    this.key = Objects.requireNonNull(key);
    this.components = key.split("\\.");
    this.attribute = components[components.length - 1];
  }

  public Optional<FdPath> parent() {
    if (components.length == 1) return Optional.empty();
    String[] parent = new String[components.length - 1];
    System.arraycopy(components, 0, parent, 0, parent.length);
    return Optional.of(new FdPath(String.join(".", parent)));
  }

  public int level() { return components.length - 1; }
  @Override public int compareTo(FdPath o) { return this.key.compareTo(o.key); }
  @Override public String toString() { return String.format("(%s) %s", level(), key); }

}
