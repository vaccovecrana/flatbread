package io.vacco.flatbread;

import java.util.List;
import java.util.Map;

public class MyConfig {

  public enum MyRuntimeType { API_SERVER, BACKGROUND_JOB }

  public enum MyActiveFeatures {
    CACHE_RESPONSES, FILTER_ZERO_RESULTS, BLUE_DEPLOYMENT
  }

  public static class MyServiceConfig {
    public String host;
    public int port;
    public String userFdMask, passwordFdMask;
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

  public List<MyActiveFeatures> activeFeatures;

  public Map<String, Integer> priceConfig;

}
