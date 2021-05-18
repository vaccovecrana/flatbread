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

  static String
      kpPub0 = "abcdef0123", kpPrv0 = "cdf32142345345345345",
      kpPub1 = "deffbd3543", kpPrv1 = "def39478538451230192";

  static {
    beforeEach(() -> System.out.println("============================================"));

    it("Can traverse a path upwards.", () -> {
      FdPath p = new FdPath("myApp.cfg.keyPairs.1.privKeyFdMask");
      Optional<FdPath> parent = p.parent();
      while (parent.isPresent()) {
        System.out.println(parent);
        parent = parent.get().parent();
      }
    });

    it("Can hydrate a non-cyclic object from dot notation property strings.", () -> {
      Map<String, String> flats = new LinkedHashMap<>();

      flats.put("myApp.cfg.dbConfig.host", host);
      flats.put("myApp.cfg.dbConfig.port", Integer.toString(port));
      flats.put("myApp.cfg.dbConfig.userFdMask", user);
      flats.put("myApp.cfg.dbConfig.passwordFdMask", pass);

      flats.put("myApp.cfg.instanceName", instance);
      flats.put("myApp.cfg.runtimeType", MyConfig.MyRuntimeType.API_SERVER.toString());

      flats.put("myApp.cfg.activeFeatures.0", MyConfig.MyActiveFeatures.BLUE_DEPLOYMENT.toString());
      flats.put("myApp.cfg.activeFeatures.1", MyConfig.MyActiveFeatures.CACHE_RESPONSES.toString());

      flats.put("myApp.cfg.myApiCacheTimeoutMs", Long.toString(cacheTimeout));
      flats.put("myApp.cfg.myApiDistanceTolerance", Double.toString(dist));
      flats.put("myApp.cfg.myApiMaxRequestsPerSec", Integer.toString(maxReq));
      flats.put("myApp.cfg.myApiKeyFdMask", apiKey);

      flats.put("myApp.cfg.myApiEndPoints.0", apiHost0);
      flats.put("myApp.cfg.myApiEndPoints.1", apiHost1);

      flats.put("myApp.cfg.priceConfig.minPrice", Integer.toString(val0));
      flats.put("myApp.cfg.priceConfig.maxPrice", Integer.toString(val1));
      flats.put("myApp.cfg.priceConfig.avgPrice", Integer.toString(val2));

      flats.put("myApp.cfg.bitFlags.0", Boolean.toString(true));
      flats.put("myApp.cfg.bitFlags.1", Boolean.toString(false));
      flats.put("myApp.cfg.bitFlags.2", Boolean.toString(false));
      flats.put("myApp.cfg.bitFlags.3", Boolean.toString(true));

      flats.put("myApp.cfg.keyPairs.0.pubKey", kpPub0);
      flats.put("myApp.cfg.keyPairs.0.privKeyFdMask", kpPrv0);
      flats.put("myApp.cfg.keyPairs.1.pubKey", kpPub1);
      flats.put("myApp.cfg.keyPairs.1.privKeyFdMask", kpPrv1);

      Fbd<MyConfig> fbd = new Fbd<>(MyConfig.class, flats, "myApp");
      MyConfig cfg = fbd.load();
      int arrayMatches = fbd.pathSeqLength(new FdPath("cfg.bitFlags.0"));

      System.out.println(arrayMatches);
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

      cfg.keyPairs = new ArrayList<>();
      cfg.keyPairs.add(MyConfig.MyKeyPair.from(kpPub0, kpPrv0));
      cfg.keyPairs.add(MyConfig.MyKeyPair.from(kpPub1, kpPrv1));

      System.out.println(new FdLog().apply(cfg));
    });
  }
}
