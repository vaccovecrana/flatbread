package io.vacco.flatbread;

import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static j8spec.J8Spec.*;

@RunWith(J8SpecRunner.class)
public class FdSpec {
  static {
    it("Can print an object with masked fields.", () -> {
      MyConfig cfg = new MyConfig();

      cfg.dbConfig = new MyConfig.MyServiceConfig();
      cfg.dbConfig.host = "localhost";
      cfg.dbConfig.port = 3306;
      cfg.dbConfig.userFdMask = "dbUser";
      cfg.dbConfig.passwordFdMask = "dbP4$$w0rD";

      cfg.instanceName = "my-service-04fd439b";
      cfg.runtimeType = MyConfig.MyRuntimeType.API_SERVER;

      cfg.activeFeatures = new ArrayList<>();
      cfg.activeFeatures.add(MyConfig.MyActiveFeatures.BLUE_DEPLOYMENT);
      cfg.activeFeatures.add(MyConfig.MyActiveFeatures.CACHE_RESPONSES);

      cfg.myApiCacheTimeoutMs = 30000;
      cfg.myApiDistanceTolerance = 45.0;
      cfg.myApiMaxRequestsPerSec = 50;
      cfg.myApiKeyFdMask = "momo-api-0dfb39abfd";
      cfg.myApiEndPoints = new String[] {
          "momo-api-us-east-0.gopher.io",
          "momo-api-us-east-1.gopher.io"
      };

      cfg.priceConfig = new LinkedHashMap<>();
      cfg.priceConfig.put("minPrice", 100);
      cfg.priceConfig.put("maxPrice", 1000);
      cfg.priceConfig.put("avgPrice", 500);

      cfg.bitFlags = new boolean[] {true, false, false, true};

      System.out.println(new FdLog().apply(cfg));
    });
  }
}
