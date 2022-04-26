# webflux-buffer-overflow demo application

This is a demo application for a spring framework issue to demonstrate the 

```
LEAK: ByteBuf.release() was not called before it's garbage-collected. See https://netty.io/wiki/reference-counted-objects.html for more information.
Recent access records: 
#1:
        io.netty.buffer.AdvancedLeakAwareByteBuf.writeBytes(AdvancedLeakAwareByteBuf.java:611)
        org.springframework.core.io.buffer.NettyDataBuffer.write(NettyDataBuffer.java:177)
        org.springframework.core.io.buffer.NettyDataBuffer.write(NettyDataBuffer.java:43)
        org.springframework.http.codec.json.AbstractJackson2Encoder.encodeStreamingValue(AbstractJackson2Encoder.java:286)
        org.springframework.http.codec.json.AbstractJackson2Encoder.lambda$encode$1(AbstractJackson2Encoder.java:168)
        reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:106)
...
```
leak report in the logs.

## Launch

Required JDK: 11

Launched on JRE 11 and JRE 17

Docker must be installed

This app automatically starts a mongodb container via testcontainers and generates 
some test data - around 70KB per mongodb document.

To start app, execute following command in the root directory of the app:

`./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-XX:MaxDirectMemorySize=80M -Xmx512m"
`

## Reproduce the error

After the app successfully started, call a few times 

`curl http://localhost:8080/stream`

Also call it concurrently and abort a few curl calls before finished... 
You should see the leak report in the logs.
Also if you look with JVisualVM (Buffer Pools Plugin installed) at the App
you should see that the buffer never gets freed.
