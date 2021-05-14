package io.vacco.flatbread;

import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.util.*;

import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class FdSpec {

  static String
      host = "localhost", user = "dbUser", pass = "dbP4$$w0rD",
      instance = "my-service-04fd439b", apiKey = "momo-api-0dfb3d9afc",
      apiHost0 = "momo-api-us-east-0.gopher.io", apiHost1 = "momo-api-us-east-1.gopher.io";

  static int port = 3306, cacheTimeout = 30000, maxReq = 50;
  static double dist = 45.0;

  static String key0 = "minPrice", key1 = "maxPrice", key2 = "avgPrice";
  static int val0 = 100, val1 = 1000, val2 = 500;

  static {
    it("Can hydrate a non-cyclic object from dot notation property strings.", () -> {
      Map<String, Object> flats = new LinkedHashMap<>();

      flats.put("cfg.dbConfig.host", host);
      flats.put("cfg.dbConfig.port", port);
      flats.put("cfg.dbConfig.userFdMask", user);
      flats.put("cfg.dbConfig.passwordFdMask", pass);

      flats.put("cfg.instanceName", instance);
      flats.put("cfg.runtimeType", MyConfig.MyRuntimeType.API_SERVER);

      flats.put("cfg.activeFeatures.0", MyConfig.MyActiveFeatures.BLUE_DEPLOYMENT);
      flats.put("cfg.activeFeatures.1", MyConfig.MyActiveFeatures.CACHE_RESPONSES);

      flats.put("cfg.myApiCacheTimeoutMs", cacheTimeout);
      flats.put("cfg.myApiDistanceTolerance", dist);
      flats.put("cfg.myApiMaxRequestsPerSec", maxReq);
      flats.put("cfg.myApiKeyFdMask", apiKey);

      flats.put("cfg.myApiEndPoints.0", apiHost0);
      flats.put("cfg.myApiEndPoints.1", apiHost1);

      flats.put("cfg.priceConfig.minPrice", val0);
      flats.put("cfg.priceConfig.maxPrice", val1);
      flats.put("cfg.priceConfig.avgPrice", val2);

      flats.put("cfg.bitFlags.0", true);
      flats.put("cfg.bitFlags.1", false);
      flats.put("cfg.bitFlags.2", false);
      flats.put("cfg.bitFlags.3", true);

      MyConfig cfg = new Fbd<MyConfig>().apply(flats);
      System.out.println(new FdLog().apply(cfg));
    });

    it("Can print an object with masked fields.", () -> {
      MyConfig cfg = new MyConfig();

      cfg.dbConfig = new MyConfig.MyServiceConfig();
      cfg.dbConfig.host = host;
      cfg.dbConfig.port = port;
      cfg.dbConfig.userFdMask = user;
      cfg.dbConfig.passwordFdMask = pass;

      cfg.instanceName = instance;
      cfg.runtimeType = MyConfig.MyRuntimeType.API_SERVER;

      cfg.activeFeatures = new ArrayList<>();
      cfg.activeFeatures.add(MyConfig.MyActiveFeatures.BLUE_DEPLOYMENT);
      cfg.activeFeatures.add(MyConfig.MyActiveFeatures.CACHE_RESPONSES);

      cfg.myApiCacheTimeoutMs = cacheTimeout;
      cfg.myApiDistanceTolerance = dist;
      cfg.myApiMaxRequestsPerSec = maxReq;
      cfg.myApiKeyFdMask = apiKey;
      cfg.myApiEndPoints = new String[] {apiHost0, apiHost1};

      cfg.priceConfig = new LinkedHashMap<>();
      cfg.priceConfig.put(key0, val0);
      cfg.priceConfig.put(key1, val1);
      cfg.priceConfig.put(key2, val2);

      cfg.bitFlags = new boolean[] {true, false, false, true};

      System.out.println(new FdLog().apply(cfg));
    });
  }
}
