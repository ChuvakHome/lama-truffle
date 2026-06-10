
# About

Lama implemention with [Graal Truffle framework](https://github.com/oracle/graal/blob/master/truffle/README.md)

# Build

At first, clone the repository.

To build run the following commands in the directory:

```bash
$ mvn package
```

To run simple language using a JDK from JAVA_HOME run ./sl.

To build the native image run in the directory

```bash
$ mvn package -Pnative
```
To run build native execute `./standalone/target/lamanative`.

# Performance tests

Sort.lama (over 5 000 elements)

Interpreter                                 | Time   
:-------------------------------------------|:--------
Source-level Lama recursive interpreter     | 6m 30s  
Bytecode-level Lama recursive interpreter   | 1m 56s
Lama truffle native image                   |    19s
