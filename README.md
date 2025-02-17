ADR maven plugin
==============

Maven Plugin to provide basic operation regarding management of Architectural Decision Records (ADR).

See [Link](https://adr.github.io/) for details about ADR.

[![Java CI with Maven](https://github.com/ronnyfriedland/adr-maven-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/ronnyfriedland/adr-maven-plugin/actions/workflows/maven.yml)
[![CodeQL](https://github.com/ronnyfriedland/adr-maven-plugin/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/ronnyfriedland/adr-maven-plugin/actions/workflows/codeql-analysis.yml)

See [Release overview](https://github.com/ronnyfriedland/adr-maven-plugin/releases) to get latest version of plugin to use.

## Maven repository

```xml
<dependency>
    <groupId>de.ronnyfriedland.maven</groupId>
    <artifactId>adr-maven-plugin</artifactId>
</dependency>
```

## Configuration properties

Property		|	Description		|	Default value    | Required | Goal
----------------|------------------|--------------------|-------------|------------
templateSourcePath	| Directory which contains the templates | ${project.build.directory}/templates/adr | false | create,index
templateIndexFile | Template file which represents the entry point containing the list of adrs | index-template.md | false | create,export,index
templateAdrFile | Template file which represents a single adr | adr-template.md | false | create
targetPath | Path where to store the processed templates | ${project.build.directory}/adr | false | create,export,index,status
filenamePattern | Pattern of the adr file | %03d_%s.md | false | create
dateFormat | How to format date values | yyyy-MM-dd | false | create
format | The export format | docx, html, pdf | false | export
status | The initial status of the adr | proposed | false | create
references | Referenced adrs |  | false | create
subject | The subject of the adr | empty | true | create

## Template placeholders

The following placeholders are available for templates:

* subject
* date
* status
* references

## Goals

### create

Creates a new ADR based on the template with the given subject. The id of the ADR is calculated based on the number of 
ADRs already existing in the `targetPath`.

#### Execution example

```mvn adr:create -Dsubject="Use adr-maven-plugin to manage architecture decision records in project"```

This results in the following files:

* target/adr/000_Use_adr_maven_plugin_to_manage_architecture_decision_records_in_project.md
* target/adr/index.md

Subsequently you can specify the content of the adr.

### index

Creates a new index file based on the existing ADR files in `targetPath`.

#### Execution example

To create an index the following goal can be executed:

```mvn adr:index -DtemplateSourcePath=target/templates/adr```

In this case a (new) index.md file is placed in the `targetPath` using the index template 
from the given template directory.

### export

Exports the existing ADR files located in the `targetPath` into the given target format. 
A directory with the type name is created to store the exported files.

Export types are:
* docx
* html
* pdf

#### Execution example

To produce a pdf export just execute:

```mvn adr:export -Dformat=pdf```

### status

Prints some status information of the ADR files located in `targetPath`.

#### Execution example

```mvn adr:status```

Example output:

```
...
[INFO] Status of available ADRs:
[INFO] - total: 1
[INFO] - proposed: 1
[INFO] - broken links: 0
[INFO] ------------------------------------------------------------------------
...
```

## Configuration example

```xml
<build>
    <plugins>
        <plugin>
            <groupId>de.ronnyfriedland.maven</groupId>
            <artifactId>adr-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

To use the provided templates you must unpack the templates 
(eg. using the [Maven Dependency Plugin](https://maven.apache.org/plugins/maven-dependency-plugin/)).
An example can be found in the `adr-example` project

## Used libraries

Groupid		|  Artifactid		|	License		
------------|--------------|------------------
commons-io | commons-io | Apache License 2.0
com.vladsch.flexmark | flexmark | BSD 2-clause
org.docx4j | docx4j-JAXB-ReferenceImpl | Apache License 2.0
org.freemarker | freemarker | Apache License 2.0

## Implementation details

see [Documentation](adr-maven-plugin/src/main/resources/documentation.md)  
see [ADRs](adr-maven-plugin/src/main/resources/adr/index.md)