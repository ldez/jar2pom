# JAR2POM #


## Résumé ##

Outils en ligne de commande permattant de produire un POM à partir d'un ou plusieurs JAR.

Basé sur l'idée [Provenance](https://github.com/armhold/Provenance) de George Armhold.


## Explication ##

    jar → maven search → pom


Détail:

- Parcours de l'ensemble des jars.
- Pour chaque fichier JAR, calcule du SHA1.
- Interogation de services REST des dépôts Maven.
- Création d'un POM (sortie console ou fichier)
- ? : recherche avec utilisation du nom du jar pour artifact Id
- ? : gestion de proxy

### Jar Analysis ###

2 options :

- une seul jar
- un dossier contenant des jars (récursif ?)


### Maven Search ###

Nexus :

- oss.sonatype.org : 2.9.0-04
- repository.sonatype.org : 2.9.0-04
- maven.java.net : 2.8.1-01
- maven.atlassian.com : 2.8.1-01
- nexus.codehaus.org : 2.8.1-01
- repository.apache.org : 2.7.2-03


#### Checksum ####

Calcul du SHA1 et recherche, le résultat est forcément unique ou null. 

#### Artifact id ####

Récuppération du nom de fichier, le résultat est une liste de 0 a n éléments.

#### Class name ####

Question : comment faire pour trouver la classe la plus pertinente ? 


### POM build ###

2 sorties possible :

- console (default)
- fichier : POM, text, ... > à voir quel type choisir suivant les différents types de recherche.

Pour les fichiers, je n'ai pas encore décidé comment faire précisement pour les cas de recherche par artifactId et ClassName.


## Technologies ##

- `Java 7` -> `Java 8` ?
- `NIO2` : pour la recherche des fichiers JAR.
- `Guava` : calcul de SHA1 du contenu des fichiers JAR.
- `args4j` : interface et option pour la ligne de commande
- ? : Rest client


## REST API ##

Il y a un bug dans `Nexus Indexer Lucene Plugin REST API`, les reponses ne sont pas valides : [NEXUS-6755](https://issues.sonatype.org/browse/NEXUS-6755)

### Maven Central ###

- [Maven Central - Sonatype - API](http://search.maven.org/#api)

Le service supporte des réponses en XML ou JSON.
Aucune définition XSD n'est disponible pour le XML.

Attention : la response ne correspond pas à l'artifact mais au "bundle", ie toutes les checksum d'un ensemble retourne toujours la même réponse.

ex : [search](http://search.maven.org/solrsearch/select?q=1:"35379fb6526fd019f331542b4e9ae2e566c57933"&rows=20&wt=json)


### Sonatype OSS - Nexus ###

- [Sonatype OSS - Nexus Core API (Restlet 1.x Plugin) REST API](https://oss.sonatype.org/nexus-restlet1x-plugin/default/docs/index.html)
- [Sonatype OSS - Nexus Indexer Lucene Plugin REST API](https://oss.sonatype.org/nexus-indexer-lucene-plugin/default/docs/index.html)


Nexus Indexer Lucene Plugin REST API

- Le service supporte des réponses en XML ou JSON uniquement pour certaines ressources.
- La définition XSD est disponible pour le XML unquement pour une partie des ressources.

**Attention** : da39a3ee5e6b4b0d3255bfef95601890afd80709 qui est le sha1 d'un fichier vide correspond à plus de 100 jars mais `identify` ne retourne qu'une seule valeur sans cohérence.

Example :

- [search](https://oss.sonatype.org/service/local/lucene/search?sha1=35379fb6526fd019f331542b4e9ae2e566c57933) en XML uniquement
- [search](https://oss.sonatype.org/service/local/identify/sha1/35379fb6526fd019f331542b4e9ae2e566c57933) en XML ou JSON (Header : Accept=application/json)


### Sonatype RSO - Nexus ###

- [Sonatype RSO - Nexus Core API (Restlet 1.x Plugin) REST API](https://repository.sonatype.org/nexus-restlet1x-plugin/default/docs/index.html)
- [Sonatype RSO - Nexus Indexer Lucene Plugin REST API](https://repository.sonatype.org/nexus-indexer-lucene-plugin/default/docs/index.html)


Le service supporte des réponses en XML ou JSON uniquement pour certaines ressources.
La définition XSD est disponible pour le XML unquement pour une partie des ressources.


Example :

- [search](https://repository.sonatype.org/service/local/lucene/search?sha1=35379fb6526fd019f331542b4e9ae2e566c57933) en XML uniquement
- [search](https://repository.sonatype.org/service/local/identify/sha1/35379fb6526fd019f331542b4e9ae2e566c57933) en XML ou JSON (Header : Accept=application/json)


## Descriptor ##

- nom de la source (url)
- nom du fichier d'origine (avec dossier)
- artifact :
 - groupId (groupIdDefault: unknownGroupId)
 - artifactId (nom du jar par défault)
 - version (versionDefault: 0.0.0)
 - package (packageDefault: jar)



