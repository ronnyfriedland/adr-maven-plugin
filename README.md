ADR maven plugin
==============

Maven Plugin to provide basic operation regarding management of Architectural Decision Records (ADR).

See [Link](https://adr.github.io/) for details about ADR.

## Maven repository

```
<dependency>
    <groupId>de.ronnyfriedland.maven</groupId>
    <artifactId>adr-maven-plugin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Configuration properties

Property		|	Description		|	Default value    | Required | Goal
----------------|------------------|--------------------|-------------|------------
templateSourcePath	| Directory which contains the templates | ${project.basedir}/src/main/resources/adr | false | create
templateIndexFile | Template file which represents the entry point containing the list of adrs | index-template.md | false | create,export,index
templateAdrFile | Template file which represents a single adr | adr-template.md | false | create
targetPath | Path where to store the processed templates | ${project.build.directory}/adr | false | create,export,index
filenamePattern | Pattern of the adr file | %03d_%s.md | false | create
dateFormat | How to format date values | yyyy-MM-dd | false | create
format | The export format | html | false | export
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
ADR already existing in the `targetPath`.

### index

Creates a new index file based on the existing ADR files in `targetPath`.

### export

Exports the existing ADR files located in the `targetPath` into the given target format. 
A directory with the type name is created to store the exported files.

## Configuration example

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>de.ronnyfriedland.maven</groupId>
                <artifactId>adr-maven-plugin</artifactId>
                <version>1.0.0-SNAPSHOT</version>
            </plugin>
        </plugins>
    </build>
```

## Execution example

```mvn adr:create -Dsubject=Use adr-maven-plugin to manage architecture decision records in project```

This results in the following files:

* target/adr/000_Use_adr_maven_plugin_to_manage_architecture_decision_records_in_project.md
* target/adr/index.md

Subsequently you can specify the content of the adr.

## Used libraries

Groupid		|  Artifactid		|	License		
------------|--------------|------------------
commons-io | commons-io | Apache License 2.0
org.commonjava.googlecode.markdown4j |markdown4j | APLv2.0
org.freemarker | freemarker | Apache License 2.0