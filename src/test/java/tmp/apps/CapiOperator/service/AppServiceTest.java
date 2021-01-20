package tmp.apps.CapiOperator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class AppServiceTest {

    @Autowired
    AppService appService;

    @Test
    void createApp_MiniJar()  {
        try {
            appService.createStaticApp("jar_small",
                    new ClassPathResource("sample1.jar").getFile().getAbsolutePath());
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    void createApp_Mini()  {
        try {
            appService.createStaticApp("static_small",
                    new ClassPathResource("static_small.zip").getFile().getAbsolutePath());
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    void createApp_Bigger() {
        try {
            appService.createStaticApp("static_big",
                    new ClassPathResource("static_big.zip").getFile().getAbsolutePath());
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    void deleteApp() {
        try {
            appService.deleteApp("static_small");
        } catch (Throwable e) {
            fail();
        }
    }
}

