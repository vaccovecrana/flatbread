package io.vacco.flatbread;

import java.lang.reflect.*;
import java.util.*;

public class FdReflect {

  public static Object[] wrap(Object o) {
    Class<?> type = o.getClass();
    if (Object[].class.isAssignableFrom(type)) return (Object[]) o;
    int al;
    Object[] oa = new Object[] {};
    if (int[].class.equals(type))     { al = ((int[]) o).length;      oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((int[]) o)[i]; } }
    if (double[].class.equals(type))  { al = ((double[]) o).length;   oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((double[]) o)[i]; } }
    if (char[].class.equals(type))    { al = ((char[]) o).length;     oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((char[]) o)[i]; } }
    if (boolean[].class.equals(type)) { al = ((boolean[]) o).length;  oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((boolean[]) o)[i]; } }
    if (long[].class.equals(type))    { al = ((long[]) o).length;     oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((long[]) o)[i]; } }
    if (float[].class.equals(type))   { al = ((float[]) o).length;    oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((float[]) o)[i]; } }
    if (short[].class.equals(type))   { al = ((short[]) o).length;    oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((short[]) o)[i]; } }
    if (byte[].class.equals(type))    { al = ((byte[]) o).length;     oa = new Object[al]; for (int i = 0; i < al; i++) { oa[i] = ((byte[]) o)[i]; } }
    return oa;
  }

  public static void arrSet(int idx, Object arr, Object val) {
    if (arr instanceof int[]) { ((int[]) arr)[idx] = (int) val; }
    else if (arr instanceof double[])   { ((double[]) arr)[idx]   = (double) val; }
    else if (arr instanceof char[])     { ((char[]) arr)[idx]     = (char) val; }
    else if (arr instanceof boolean[])  { ((boolean[]) arr)[idx]  = (boolean) val; }
    else if (arr instanceof long[])     { ((long[]) arr)[idx]     = (long) val; }
    else if (arr instanceof float[])    { ((float[]) arr)[idx]    = (float) val; }
    else if (arr instanceof short[])    { ((short[]) arr)[idx]    = (short) val; }
    else if (arr instanceof byte[])     { ((byte[]) arr)[idx]     = (byte) val; }
    else if (arr instanceof Object[])   { ((Object[]) arr)[idx]   = val; }
  }

  public static Class<?> toWrapperClass(Class<?> type) {
    if (!type.isPrimitive()) return type;
    else if (int.class.equals(type)) { return Integer.class; }
    else if (double.class.equals(type)) { return Double.class; }
    else if (char.class.equals(type)) { return Character.class; }
    else if (boolean.class.equals(type)) { return Boolean.class; }
    else if (long.class.equals(type)) { return Long.class; }
    else if (float.class.equals(type)) { return Float.class; }
    else if (short.class.equals(type)) { return Short.class; }
    else if (byte.class.equals(type)) { return Byte.class; }
    return type;
  }

  public static boolean isWrapperType(Class<?> type) {
    return type == Boolean.class
        || type == Integer.class
        || type == Character.class
        || type == Byte.class
        || type == Short.class
        || type == Double.class
        || type == Long.class
        || type == Float.class;
  }

  public static boolean isPrimitiveOrWrapper(final Class<?> type) {
    if (type == null) { return false; }
    return type.isPrimitive() || isWrapperType(toWrapperClass(type));
  }

  public static boolean isEnum(Object o) {
    return o instanceof Enum<?>;
  }

  public static boolean isTextual(Object o) {
    return o instanceof String || o instanceof Character;
  }

  public static boolean isList(Object o) { return o instanceof List<?>; }
  public static boolean isMap(Object o) { return o instanceof Map<?, ?>; }
  public static boolean isSet(Object o) { return o instanceof Set<?>; }
  public static boolean isArray(Object o) { return o != null && o.getClass().isArray(); }

  public static boolean isList(Class<?> c) { return List.class.isAssignableFrom(c); }
  public static boolean isMap(Class<?> c) { return Map.class.isAssignableFrom(c); }

  public static boolean isCollection(Object o) {
    return isList(o) || isMap(o) || isSet(o) || isArray(o);
  }

  public static Type[] genericTypesOf(Field f) {
    if (f.getGenericType() instanceof ParameterizedType) {
      return ((ParameterizedType) f.getGenericType()).getActualTypeArguments();
    }
    return new Type[0];
  }

}
