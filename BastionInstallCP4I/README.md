# Bastion Install of CloudPak for Integration #

This is a simple solution for having a bastion installation of IBM's Cloud Pak for Integration by using a Docker container with all the necessary tools and a script to make it a very simple process.  Naturally, the simple that is going to run this must have Docker installed to build and run.

The Dockerfile needs to have the location of the `oc` tool to be downloaded.  This location can be located by going to the OpenShift Container Platform console and clicking on the question mark (?) in the upper right and then selecting the `Command line tools`.  Copy the hostname and port and update the Dockerfile's `OC_LOCATION` with that information.

Next, update the `installcp4i.sh`1 file.  There are several fields at the top of the file that need information.  Also, the project/namespace that is to be used can be changed along with the version of CP4I that will be used.  Those fields are `ENTTITLEMENT_KEY`, `LOCAL_DOCKER_REGISTRY_HOST`, `LOCAL_DOCKER_USER`, and `LOCAL_DOCKER_PASSWORD`.  

To build the Docker image use this command: `docker build -t cp4i:latest -f Dockerfile .`

Then to run it, `docker run -i -t cp4i bash`.  This will bring the user into the command prompt of the container.  Signon to the target OCP environment using the `oc login` command, then run the install script `/root/installcp4i.sh`.

If all goes well, then the local registry will be populated with the CP4I images and the CatalogSources will be added pointing to those repositories.  
