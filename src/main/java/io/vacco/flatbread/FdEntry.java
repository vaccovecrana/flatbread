package io.vacco.flatbread;

import java.util.Map;

public class FdEntry {

  public FdPath path;
  public FdTarget<?> target;

  public static FdEntry of(FdPath path, FdTarget<?> target) {
    FdEntry e = new FdEntry();
    e.path = path;
    e.target = target;
    return e;
  }

  public static FdEntry of(Map.Entry<FdPath, FdTarget<?>> e) {
    return of(e.getKey(), e.getValue());
  }

  @Override public String toString() { return String.format("%s -> %s", path, target); }

}
