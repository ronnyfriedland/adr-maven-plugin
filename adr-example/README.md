# Example of plugin usage

## Extract templates to target

First copy templates and existing adr files to the target directory.

`mvn package -Pprepare`

## Manage your adr

Create or manage the adr files. See available [goals](../README.md).

## Copy adr files to ressources directory

Finally copy the adr files back to the resource directory - eg. to be checked in into version control.

`mvn package -Pfinish`

