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

  static String key0 = "minPrice", key1 = "maxPrice", key2 = "avgPrice", key3 = "sum_price";
  static int val0 = 100, val1 = 1000, val2 = 500, val3 = 1001;

  static String
      kpPub0 = "abcdef0123", kpPrv0 = "cdf32142345345345345",
      kpPub1 = "deffbd3543", kpPrv1 = "def39478538451230192";

  static {
    beforeEach(() -> System.out.println("============================================"));

    it("Can traverse a path upwards.", () -> {
      FdPath p = new FdPath("myApp.keyPairs.1.privKeyFdMask");
      String pKey = p.parentKey();
      while (pKey != null) {
        FdPath parent = new FdPath(pKey);
        System.out.println(parent);
        pKey = parent.parentKey();
      }
    });

    it("Can hydrate a non-cyclic object from dot notation property strings.", () -> {
      Properties flats = new Properties();

      flats.put("myApp.dbConfig.host", host);
      flats.put("myApp.dbConfig.port", Integer.toString(port));
      flats.put("myApp.dbConfig.userFdMask", user);
      flats.put("myApp.dbConfig.passwordFdMask", pass);

      flats.put("myApp.instanceName", instance);
      flats.put("myApp.runtimeType", MyConfig.MyRuntimeType.API_SERVER.toString());

      flats.put("myApp.activeFeatures.0", MyConfig.MyActiveFeatures.BLUE_DEPLOYMENT.toString());
      flats.put("myApp.activeFeatures.1", MyConfig.MyActiveFeatures.CACHE_RESPONSES.toString());

      flats.put("myApp.myApiCacheTimeoutMs", Long.toString(cacheTimeout));
      flats.put("myApp.myApiDistanceTolerance", Double.toString(dist));
      flats.put("myApp.myApiMaxRequestsPerSec", Integer.toString(maxReq));
      flats.put("myApp.myApiKeyFdMask", apiKey);

      flats.put("myApp.myApiEndPoints.0", apiHost0);
      flats.put("myApp.myApiEndPoints.1", apiHost1);

      flats.put("myApp.priceConfig.minPrice", Integer.toString(val0));
      flats.put("myApp.priceConfig.maxPrice", Integer.toString(val1));
      flats.put("myApp.priceConfig.avgPrice", Integer.toString(val2));
      flats.put("myApp.priceConfig.sum___price", Integer.toString(val2));

      flats.put("myApp.bitFlags.0", Boolean.toString(true));
      flats.put("myApp.bitFlags.1", Boolean.toString(false));
      flats.put("myApp.bitFlags.2", Boolean.toString(false));
      flats.put("myApp.bitFlags.3", Boolean.toString(true));

      flats.put("myApp.keyPairs.0.pubKey", kpPub0);
      flats.put("myApp.keyPairs.0.privKeyFdMask", kpPrv0);
      flats.put("myApp.keyPairs.1.pubKey", kpPub1);
      flats.put("myApp.keyPairs.1.privKeyFdMask", kpPrv1);

      flats.put("myApp.routes.0.path", "/");
      flats.put("myApp.routes.0.backend", "wordpress");
      flats.put("myApp.routes.0.priority", "0");
      flats.put("myApp.routes.1.path", "/api");
      flats.put("myApp.routes.1.backend", "api");
      flats.put("myApp.routes.1.priority", "1");

      flats.put("myApp.voteLimits.Like.min", "5");
      flats.put("myApp.voteLimits.Like.max", "5");
      flats.put("myApp.voteLimits.Dislike.min", "5");
      flats.put("myApp.voteLimits.Dislike.max", "5");

      flats.put("myApp.bitFlagIdx.1000", "600");
      flats.put("myApp.bitFlagIdx.1001", "455");

      Fbd<MyConfig> fbd = new Fbd<>(MyConfig.class, flats, "myApp");
      MyConfig cfg = fbd.load();
      int arrayMatches = new FdPath("cfg.bitFlags").seqChildren(fbd.paths).size();

      System.out.println(arrayMatches);
      System.out.println(new FdLog().apply(cfg));
    });

    it("Can hydrate a non-cyclic object from environment notation property strings.", () -> {
      Map<String, String> env = new LinkedHashMap<>();

      env.put("MYAPP_DBCONFIG_HOST", host);
      env.put("MYAPP_DBCONFIG_PORT", Integer.toString(port));
      env.put("MYAPP_DBCONFIG_USERFDMASK", user);
      env.put("MYAPP_DBCONFIG_PASSWORDFDMASK", pass);

      env.put("MYAPP_INSTANCENAME", instance);
      env.put("MYAPP_RUNTIMETYPE", MyConfig.MyRuntimeType.API_SERVER.toString());

      env.put("MYAPP_ACTIVEFEATURES_0", MyConfig.MyActiveFeatures.BLUE_DEPLOYMENT.toString());
      env.put("MYAPP_ACTIVEFEATURES_1", MyConfig.MyActiveFeatures.CACHE_RESPONSES.toString());

      env.put("MYAPP_MYAPICACHETIMEOUTMS", Long.toString(cacheTimeout));
      env.put("MYAPP_MYAPIDISTANCETOLERANCE", Double.toString(dist));
      env.put("MYAPP_MYAPIMAXREQUESTSPERSEC", Integer.toString(maxReq));
      env.put("MYAPP_MYAPIKEYFDMASK", apiKey);

      env.put("MYAPP_MYAPIENDPOINTS_0", apiHost0);
      env.put("MYAPP_MYAPIENDPOINTS_1", apiHost1);

      env.put("MYAPP_PRICECONFIG_minPrice", Integer.toString(val0));
      env.put("MYAPP_PRICECONFIG_maxPrice", Integer.toString(val1));
      env.put("MYAPP_PRICECONFIG_avgPrice", Integer.toString(val2));
      env.put("MYAPP_PRICECONFIG_sum___price", Integer.toString(val2));

      env.put("MYAPP_BITFLAGS_0", Boolean.toString(true));
      env.put("MYAPP_BITFLAGS_1", Boolean.toString(false));
      env.put("MYAPP_BITFLAGS_2", Boolean.toString(false));
      env.put("MYAPP_BITFLAGS_3", Boolean.toString(true));

      env.put("MYAPP_KEYPAIRS_0_PUBKEY", kpPub0);
      env.put("MYAPP_KEYPAIRS_0_PRIVKEYFDMASK", kpPrv0);
      env.put("MYAPP_KEYPAIRS_1_PUBKEY", kpPub1);
      env.put("MYAPP_KEYPAIRS_1_PRIVKEYFDMASK", kpPrv1);

      env.put("MYAPP_ROUTES_0_PATH", "/");
      env.put("MYAPP_ROUTES_0_BACKEND", "wordpress");
      env.put("MYAPP_ROUTES_0_PRIORITY", "0");
      env.put("MYAPP_ROUTES_1_PATH", "/api");
      env.put("MYAPP_ROUTES_1_BACKEND", "api");
      env.put("MYAPP_ROUTES_1_PRIORITY", "1");

      env.put("MYAPP_VOTELIMITS_LIKE_MIN", "5");
      env.put("MYAPP_VOTELIMITS_LIKE_MAX", "5");
      env.put("MYAPP_VOTELIMITS_DISLIKE_MIN", "5");
      env.put("MYAPP_VOTELIMITS_DISLIKE_MAX", "5");

      env.put("MYAPP_BITFLAGIDX_1000", "600");
      env.put("MYAPP_BITFLAGIDX_1001", "455");

      env.put("SOME_OTHER_ENV_VAR", "999");

      Fbd<MyConfig> fbd = new Fbd<>(MyConfig.class, env, "MYAPP");
      MyConfig cfg = fbd.load();
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
      cfg.priceConfig.put(key3, val2);

      cfg.voteLimits = new LinkedHashMap<>();
      cfg.voteLimits.put(MyConfig.MyVoteType.Like, MyConfig.MyVoteConfig.from(5, 5));
      cfg.voteLimits.put(MyConfig.MyVoteType.Dislike, MyConfig.MyVoteConfig.from(5, 5));

      cfg.bitFlagIdx = new LinkedHashMap<>();
      cfg.bitFlagIdx.put(val1, 600);
      cfg.bitFlagIdx.put(val3, 455);

      cfg.bitFlags = new boolean[] {true, false, false, true};

      cfg.keyPairs = new ArrayList<>();
      cfg.keyPairs.add(MyConfig.MyKeyPair.from(kpPub0, kpPrv0));
      cfg.keyPairs.add(MyConfig.MyKeyPair.from(kpPub1, kpPrv1));

      cfg.routes = new MyConfig.MyRoute[2];
      cfg.routes[0] = MyConfig.MyRoute.from("/", "wordpress", 0);
      cfg.routes[1] = MyConfig.MyRoute.from("/api", "api", 1);

      System.out.println(new FdLog().apply(cfg));
    });
  }
}
