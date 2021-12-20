# Use architectural decision records for documentation
 
* Status: accepted
* Date: 2021-11-01

# Context

It is necessary to keep all architectural decisions persisted. If not, in future might not be understandable.
For this a consistent template should be used.

# Decision

All decisions regarding architecture should be documented and placed by the project files it belongs to.
To be sure ids are unique all architectural decision records for the project should be managed by the plugin.

For this the plugin provides the necessary goals.

![use-cases](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/ronnyfriedland/adr-maven-plugin/master/adr-maven-plugin/src/main/resources/adr/000_usecases.uml)

# Consequences

To ensure consistent records all new decisions should be managed using this plugin. Otherwise, the 
numbering could result in duplicate ids or the index may not be complete.

# References


