package com.flickcrit.app;

import com.flickcrit.app.config.TestcontainersConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"it", "cache-redis"})
@Import(TestcontainersConfig.class)
public abstract class BaseSpringBootIT {


}
