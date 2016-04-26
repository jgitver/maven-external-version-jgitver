# maven-external-version-jgitver

This module is an extension of the [maven-external-version](https://github.com/bdemers/maven-external-version) maven plugin. This extension uses [jgitver](https://github.com/McFoggy/jgitver) in order to calculate the maven project version from the git repository.

> As project dependencies have no public releases yet, please build them first by hand.  
> Then as the project is eating its own food and has no public release yet, you also need to build it first by hand.  
> To build locally the inital version, comment the usage of `maven-external-version-plugin``in the pom and launch `mvn install`.  
> Once done, you can revert your change and build the project normally.  
> This paragraph will disappear once a first public reachable release will be out.

## Why should I use it

Because you:

- think commits should contain only useful information
- do not support to see those ugly commits just to change versions in pom files
- don't want to have to take care anymore of conflicting merges due to versions in pom files
- would be able to deploy in CI every artifact in any branch without erasing the ones produced in another branch
- want to finally get rid of [maven-release-plugin](https://maven.apache.org/maven-release/maven-release-plugin/) in a clean way
- just find the project cool !

## What it does

This module, together with [maven-external-version](https://github.com/bdemers/maven-external-version) maven plugin, will analyze your git repository and from there it will build and inject dynamically into the POM (Project Object Model) the version to use for your project.

For example, having the following project tree:

![Standard git example](src/images/standard-example.png?raw=true "Standard git example")

versions reported by maven will be:

- on `master`: 2.0.0-2
- on `cool-feature`: 2.0.0-2-cool_feature
- on standard tag `1.0` (after `git checkout 1.0`): 1.0

Want to do some trials and/or integrate in your pom, follow the [reproduction paragraph](#reproduce-the-test-case).

## Usage

The module is an extension of [maven-external-version](https://github.com/bdemers/maven-external-version), add to your pom something like the following:   

```
<build>
    <plugins>
    ...
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-external-version-plugin</artifactId>
            <version>X.Y.Z</version>
            <extensions>true</extensions>
            <configuration>
                <strategy hint="jgitver">
          SPECIFIC CONFIGURATION IF NEEDED
                </strategy>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>fr.brouillard.oss.maven</groupId>
                    <artifactId>maven-external-version-jgitver</artifactId>
                    <version>A.B.C</version>
                </dependency>
            </dependencies>
        </plugin>
    ...
    </plugins>
</build>
```

## Parameters

This extension accepts several parameters

```
...
<configuration>
    <strategy hint="jgitver">
      <nonQualifierBranches></nonQualifierBranches>
      <appendGitCommitId></appendGitCommitId>
      <gitCommitIdLength></gitCommitIdLength>
    </strategy>
</configuration>
...
```

- _nonQualifierBranches_: comma separated list of git branch name that will not be appended to the calculated version, default `master`  
- _appendGitCommitId_: true/false flag to control that git SHA1 is appended to the calculated version or not, default `false`
- _nonQualifierBranches_: length of SHA1 (if _appendGitCommitId_ is true), must be between [8, 40], default ``8`   
 
## Reproduce the test case

```
mkdir /d/demo-jgitver-external-version
cd /d/demo-jgitver-external-version
git init
cat > pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>fr.brouillard.oss.demo</groupId>
    <artifactId>demo-external-version-jgitver</artifactId>
    <version>0</version>
    <packaging>pom</packaging>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-external-version-plugin</artifactId>
                <version>[0.0.0-SNAPSHOT,)</version>
                <extensions>true</extensions>
                <configuration>
                    <strategy hint="jgitver" />
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>fr.brouillard.oss.maven</groupId>
                        <artifactId>maven-external-version-jgitver</artifactId>
                        <version>[0.0.0-SNAPSHOT,)</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>
^D
echo A > content
git add pom.xml
git add content
git commit -m "initial commit"
echo B > content && git add -u && git commit -m "added B data"
git tag 1.0 -m "release 1.0"
git tag 2.0
echo C > content && git add -u && git commit -m "added C data"
git checkout -b cool-feature
echo D > content && git add -u && git commit -m "added D data"
git checkout master
echo E > content && git add -u && git commit -m "added E data"
```


 