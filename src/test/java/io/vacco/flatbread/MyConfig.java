package io.vacco.flatbread;

import java.util.List;
import java.util.Map;

public class MyConfig {

  public enum MyRuntimeType { API_SERVER, BACKGROUND_JOB }
  public enum MyActiveFeatures { CACHE_RESPONSES, FILTER_ZERO_RESULTS, BLUE_DEPLOYMENT }

  public static class MyServiceConfig {
    public String host;
    public int port;
    public String userFdMask, passwordFdMask;
  }

  public static class MyKeyPair {
    public String pubKey;
    public String privKeyFdMask;

    public static MyKeyPair from(String pubKey, String privKeyFdMask) {
      MyKeyPair kp = new MyKeyPair();
      kp.pubKey = pubKey;
      kp.privKeyFdMask = privKeyFdMask;
      return kp;
    }
  }

  public static class MyRoute {
    public String path;
    public String backend;
    public int priority;

    public static MyRoute from(String path, String backend, int priority) {
      MyRoute r = new MyRoute();
      r.path = path;
      r.backend = backend;
      r.priority = priority;
      return r;
    }
  }

  public MyServiceConfig dbConfig;

  public String instanceName;
  public MyRuntimeType runtimeType;

  public String   myApiKeyFdMask;
  public int      myApiMaxRequestsPerSec;
  public long     myApiCacheTimeoutMs;
  public double   myApiDistanceTolerance;
  public String[] myApiEndPoints;

  public boolean[] bitFlags;

  public MyRoute[] routes;
  public List<MyKeyPair> keyPairs;
  public List<MyActiveFeatures> activeFeatures;
  public Map<String, Integer> priceConfig;

}
