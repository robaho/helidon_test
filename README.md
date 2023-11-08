non-scientific test of helidon framework as compared to [robaho httpserver](http://github.com/robaho/httpserver)

start using
```
gradlew runSimpleFileServer
```

test using
```
h2load -n100000 -c50 -m10 --h1 http://localhost:8888/devnull?size=1000000
```

from another terminal session.