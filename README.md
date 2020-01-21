# The Spark Application from the Jan 2020 Mini-code camp.
---

## Part 1: Multi Module Spark Project Setup

### Step 1:  Intellij SBT Project Setup
**Main deps:**
1.  Apache Spark
2.  hadoop-client
3.  Scallop (https://github.com/scallop/scallop)
### Step 2:  SBT Command line build
1.  build.sbt review
2.  assembly plugin
3.  SBT command line

```shell script
sbt clean compile 

sbt clean compile test

sbt clean compile test assembly
```

---

## Part 2: Spark Job Logic + Unit Tests
### Step 1:  Organize common code
**Typical common code:**
1.  Common utils
2.  Common UDF's
3.  unit tests
### Step 2:  Organize Spark code + Unit Tests
1.  Word count Spark job with Command Line Interface (CLI) style params
2.  Unit tests
3.  Integration tests

### Step 3:  IDE + SBT run unit tests
---

## Part 3: Spark Job Local Integration Test + Package Assembly
### Step 2: What is shading and assembly?
Local integration test (IT)

### Step 1: What is shading and assembly?
1.  When to use shading?
2.  When to use assembly?

# References







