# The Spark Application from the Jan 2020 Mini-code camp.
---

## Part 1: Multi Module Spark Project Setup

### Step 1:  Intellij SBT Project Setup
**Review:**
1.  build.sbt
2.  ./project/assembly.sbt
3.  ./project/build.properties
4.  ./project/plugins.sbt
5.  ./project/scalastyle_config.xml

**Main deps:**
1.  Apache Spark
2.  hadoop-client
3.  Scallop (https://github.com/scallop/scallop)
### Step 2:  SBT Command line build
1.  assembly plugin
2.  SBT command line

```shell script
sbt clean compile 

sbt clean compile test

sbt clean compile test assembly
```

---

# References
1.  http://www.gutenberg.org/







