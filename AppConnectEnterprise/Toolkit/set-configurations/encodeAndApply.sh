#!/bin/bash

# Set configurations for ACE

base64 setdbparms.jks | sed -e 's/^/    /' >> ocp/ace-setdbparms.yaml
base64 server.conf.yaml | sed -e 's/^/    /' >> ocp/ace-serverconf.yaml
base64 truststore.jks | sed -e 's/^/    /' >> ocp/ace-truststore.yaml

zip bindings.zip .bindings
base64 bindings.zip | sed -e 's/^/    /' >> ocp/ace-bindings.yaml

jar -cvf jndi.jar jndi.properties
zip jndi.zip jndi.jar
base64 jndi.zip | sed -e 's/^/    /' >> ocp/ace-jndiproperties.yaml
