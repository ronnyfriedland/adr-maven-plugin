# Support different export formats
 
* Status: accepted
* Date: 2022-01-05

# Context

The project started using `org.commonjava.googlecode.markdown4j:markdown4j` whose last update was published in 2016.
Additionally only exports in HTML format are supported.

We should switch to another library which meets the following criterias:
* Apache 2.0 license compatible 
* Support at least HTML and PDF exports 
* maintained 

# Decision

The library `com.vladsch.flexmark:flexmark` matches best:
* BSD 2-clause
* Support eg. HTML, PDF and DOCX export formats

# Consequences

If flexmark library will not be maintained in future we must change library again. For now there is no real option.

# References
