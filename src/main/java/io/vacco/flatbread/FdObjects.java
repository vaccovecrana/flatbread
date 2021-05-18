package io.vacco.flatbread;

import java.util.List;

public class FdObjects {

  public FdTarget<?> init(Class<?> tClass) {
    if (List.class.isAssignableFrom(tClass)) {
      System.out.println("lul now wut??");
    } else if (tClass.isArray()) {
      System.out.println("lol now wut??");
    } else {
      return new FdTarget<>("").set((Class<Object>) tClass);
    }
    return null;
  }

}
