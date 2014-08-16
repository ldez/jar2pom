# JAR2POM #

[![Build Status](https://travis-ci.org/ldez/jar2pom.svg?branch=master)](https://travis-ci.org/ldez/jar2pom) 
[![Coverity Scan Build Status](https://scan.coverity.com/projects/2805/badge.svg)

## Summary ##

Command line tools for producing a POM from one or more JAR.

    jar → maven search → pom

Based on the idea of [Provenance](https://github.com/armhold/Provenance) by [George Armhold](http://armhold.com).


## Build ##

In the directory where the pom.xml is, run:

    mvn clean package


## Run like ##

    java -jar jar2pom.jar
    
or

    java -jar jar2pom.jar -host 192.168.0.1 -i "c:\myProjet\source" -o "c:\output" -p -r

Show all options:

    java -jar jar2pom.jar -h  

Options (no options are mandatory):
    
     -h (--help)  : display help.
     -host HOST   : defined custom Nexus host.
                    (ex: oss.sonatype.org)
     -i INPUT     : input path (file or directory).
     -o OUTPUT    : directory output path.
                    (default output is console)
     -p (--proxy) : Use system proxies.
     -r           : inspect input path recursively.


## Technologies ##

- `Java 7`
- [args4j](https://github.com/kohsuke/args4j) : interface and options for the command line
- [NIO.2](http://docs.oracle.com/javase/tutorial/essential/io/fileio.html) : browse JAR files.
- [Guava](https://code.google.com/p/guava-libraries) : calculating SHA1 from the contents of the JAR files.
- [Jersey](https://jersey.java.net/) : Rest client
- [mustache.java](https://github.com/spullara/mustache.java) : generation of the output file


## Explanation ##

- Browse and detect all jars.
- For each JAR file, calculates the SHA1.
- Querying REST Services Maven repositories.
- Creating a POM file.

### Browse Jar ###

- a single jar.
- a folder containing jars (recursive or not).


### Maven Search ###

Nexus:

- [oss.sonatype.org](https://oss.sonatype.org) : 2.9.0-04
- [repository.sonatype.org](https://repository.sonatype.org) : 2.9.0-04
- [maven.java.net](https://maven.java.net) : 2.8.1-01
- [maven.atlassian.com](https://maven.atlassian.com) : 2.8.1-01
- [nexus.codehaus.org](https://nexus.codehaus.org) : 2.8.1-01
- [repository.apache.org](https://repository.apache.org) : 2.7.2-03

Search type:

- Checksum (SHA1).
- ~~Artifact id~~
- ~~Class name~~

### POM build ###

Output as an XML file [Partial POM].


## REST API ##

### Nexus ###

#### Nexus Indexer Lucene Plugin REST API ####

- [Sonatype OSS - Nexus Indexer Lucene Plugin REST API](https://oss.sonatype.org/nexus-indexer-lucene-plugin/default/docs/index.html)
- [Sonatype RSO - Nexus Indexer Lucene Plugin REST API](https://repository.sonatype.org/nexus-indexer-lucene-plugin/default/docs/index.html)

Le service supporte des réponses en XML ou JSON uniquement pour certaines ressources.
La définition XSD est disponible pour le XML uniquement pour une partie des ressources.

##### Warning #####

- Bug with `Nexus Indexer Lucene Plugin REST API` XSD : [NEXUS-6755](https://issues.sonatype.org/browse/NEXUS-6755)
- `da39a3ee5e6b4b0d3255bfef95601890afd80709` qui est le sha1 d'un fichier vide correspond à plus de 100 jars mais `identify` ne retourne qu'une seule valeur sans cohérence.

Example :

- [search OSS](https://oss.sonatype.org/service/local/lucene/search?sha1=35379fb6526fd019f331542b4e9ae2e566c57933) XML only
- [search OSS](https://oss.sonatype.org/service/local/identify/sha1/35379fb6526fd019f331542b4e9ae2e566c57933) XML or JSON (Header : Accept=application/json)
- [search RSO](https://repository.sonatype.org/service/local/lucene/search?sha1=35379fb6526fd019f331542b4e9ae2e566c57933) XML only
- [search RSO](https://repository.sonatype.org/service/local/identify/sha1/35379fb6526fd019f331542b4e9ae2e566c57933) XML or JSON (Header : Accept=application/json)

#### Nexus Core API (Restlet 1.x Plugin) REST API ####

- [Sonatype OSS - Nexus Core API (Restlet 1.x Plugin) REST API](https://oss.sonatype.org/nexus-restlet1x-plugin/default/docs/index.html)
- [Sonatype RSO - Nexus Core API (Restlet 1.x Plugin) REST API](https://repository.sonatype.org/nexus-restlet1x-plugin/default/docs/index.html)

### Maven Central ###

- [Maven Central - Sonatype - API](http://search.maven.org/#api)

Le service supporte des réponses en XML ou JSON.
Aucune définition XSD n'est disponible pour le XML.

**Warning** : la response ne correspond pas à l'artifact mais au "bundle", ie toutes les checksum d'un ensemble retourne toujours la même réponse.

ex : [search](http://search.maven.org/solrsearch/select?q=1:"35379fb6526fd019f331542b4e9ae2e566c57933"&rows=20&wt=json)


## XML file ##

```xml
    <!-- source url / file path -->
    <dependency>
        <groupId>groupId</groupId>
        <artifactId>artifactId</artifactId>
        <version>version</version>
    </dependency>
```

- source url
- absolute input file path
- artifact :
 - groupId (groupId by default: unknownGroupId)
 - artifactId (jar name by default)
 - version (version by default: 0.0.0)
 - ~~classifier~~

