# Use case: push app via Cloud Foundry Client Library from Spring Boot app

* Problem is uploading bits (zips) > 16 kb (static_big.zip)
* zip < 16 kb works fine (static_small.zip)

## Setting
* apiHost: api.run.pivotal.io

## Config

* application.yml: options to activate wiretap and proxy for debugging
* logback.xml: set reactor.netty to debug if you use wiretap
* set environment-variables to run the code: CF_PASSWORD, CF_USERNAME, CF_APIHOST, CF_ORGANIZATION, CF_SPACE

## Tests
### createApp_Mini works

### createApp_Bigger throws

* either: 
```
reactor.core.Exceptions$ReactiveException: reactor.netty.http.client.PrematureCloseException: Connection has been closed BEFORE response, while sending request body
```
* or: 
```
org.cloudfoundry.client.v2.ClientV2Exception: CF-AppBitsUploadInvalid(160001): The app upload is invalid: Invalid zip archive.
```

Maybe it partially depends on the loglevel and wiretap which exception appears. 

### create app in AppService - @PostConstruct 
* Uncomment the @PostContruct-Code and use static_big.zip - file to see the same problem as in test-class´



