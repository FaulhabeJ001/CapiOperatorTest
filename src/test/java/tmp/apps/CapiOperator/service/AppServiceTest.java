package tmp.apps.CapiOperator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class AppServiceTest {

    @Autowired
    AppService appService;

    @Test
    void createApp_Mini() throws IOException, InterruptedException {
            appService.createStaticApp("static_small",
                    new ClassPathResource("static_small.zip").getFile().getAbsolutePath());

    }

    @Test
    void createApp_Bigger() throws IOException, InterruptedException {
        // This will throw: org.cloudfoundry.client.v2.ClientV2Exception: CF-AppBitsUploadInvalid(160001): The app upload is invalid: Invalid zip archive.
            appService.createStaticApp("static_big",
                    new ClassPathResource("static_big.zip").getFile().getAbsolutePath());
    }

//    @Test
//    void deleteApp() {
//        try {
//            appService.deleteApp("static_small");
 //       } catch (Throwable e) {
 //           fail();
 //       }
 //   }
}

