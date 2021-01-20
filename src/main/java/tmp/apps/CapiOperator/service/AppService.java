package tmp.apps.CapiOperator.service;

import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

@Component
public class AppService {

    Logger log = LoggerFactory.getLogger(AppService.class);

    private DefaultCloudFoundryOperations cfOperations;

    public AppService(DefaultCloudFoundryOperations cfOperations) {
        this.cfOperations = cfOperations;
    }

    public void createStaticApp(String name, String absoluteFilePath) throws InterruptedException {
        ApplicationManifest manifest = ApplicationManifest.builder()
                .name(name)
                .buildpack("staticfile_buildpack")
                .disk(1024)
                .memory(1024)
                .instances(1)
                .noRoute(true)
                .stack("cflinuxfs3")
                .healthCheckType(ApplicationHealthCheck.NONE)
                .path(Paths.get(absoluteFilePath))
                .build();

        PushApplicationManifestRequest request = PushApplicationManifestRequest
                .builder()
                .manifest(manifest)
                .noStart(false)
                .build();

        Mono<Void> createRequest = cfOperations.applications().pushManifest(request)
                .doOnSubscribe(subscription -> log.info("pushing manifest for application"))
                .doOnSuccess(aVoid -> log.info("pushed manifest for application"));

//        CountDownLatch latch = new CountDownLatch(2);
//        createRequest.subscribe(System.out::println, t -> {
//            t.printStackTrace();
//            latch.countDown();
//        }, latch::countDown);
//
//        latch.await();

        try {
            createRequest.block();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteApp(String appName) {
        DeleteApplicationRequest request = DeleteApplicationRequest
                .builder()
                .name(appName)
                .build();

        Mono<Void> deleteRequest = cfOperations.applications()
                .delete(request)
                .doOnSubscribe(aVoid -> log.info("Removing application: " + appName))
                .doOnSuccess(aVoid -> log.info("Removed application: " + appName))
                .onErrorStop();

        deleteRequest.block();
    }

//    @PostConstruct
//    void testAppServiceMethodsAfterStartup() throws IOException {
//        createStaticApp("static_small",
//                new ClassPathResource("static_big.zip").getFile().getAbsolutePath());
//    }
}
