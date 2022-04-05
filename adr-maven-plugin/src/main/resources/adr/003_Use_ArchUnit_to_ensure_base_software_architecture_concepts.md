# Use ArchUnit to ensure base software architecture concepts
 
* Status: accepted
* Date: 2022-04-04

# Context

Short description of the context of the ADR.

## Package dependencies

Dependencies between packages are described using the `packages.puml` diagram.

New packages has to be added to the diagram manually. 
If new diagrams are missing unfortunately the test will actually not fail. 

## Naming conventions

Naming conventions and package restrictions must be covered by rules - like:
* Enumerations
* Processor classes
* Mojos
* Exceptions

# Decision

Existing rules must not be modified unless the reason is well documented.
New rules can be added if they meet the conventions.

# Consequences

For now it seems that new packages are not considered out of the box for the package dependency rule.

# References


