streaming-media-exporter
========================

This java web application is a front-end to python command line streaming media exporters.

It currently supports youtube-dl and groove-dl (both projects are on github)

Some modifications had to be made to groove-dl in order to communicate with the process, if you plan to export from grooveshark, use my fork (m-ryan/groove-dl)

Installing
----------

Build with maven
Deploy the .war in a webapp container such as tomcat

Configuration
-------------

- Make sure your container supports UTF-8 encoded urls, in tomcat's server.xml, just add URIEncoding="UTF-8" to the "Connecor" tag

- A config property file is read from /var/streaming-media-exporter/conf.properties
here's an example configuration :

- restart the webapp after changing the configuration


