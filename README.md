oVirt and Quantum

Contains support for the POC with oVirt and Quantum.

Currently supports OVS and Linux Bridge

A network is considered a Quantum network if the name starts with "Q_"


1. Copy the following files to the /tmp directory:
ovirt.sh
ovirt.plugin
Update permissions and ownership of the files:

-rwxrwxrwx. 1 jboss-as   jboss-as      12 Apr 25 07:57 ovirt.plugin
-rwxrwxrwx. 1 jboss-as   jboss-as    2097 Apr 23 09:44 ovirt.sh

The plugin file contains the plugin that is currently used. This information is passed to VDSM

2. Update/merge the following files:
    backend/manager/modules/bll/src/main/java/org/ovirt/engine/core/bll/AddVmCommand.java
    backend/manager/modules/bll/src/main/java/org/ovirt/engine/core/bll/AttachNetworkToVdsGroupCommand.java
    backend/manager/modules/bll/src/main/java/org/ovirt/engine/core/bll/storage/AddNetworkCommand.java
    backend/manager/modules/bll/src/main/java/org/ovirt/engine/core/bll/storage/RemoveNetworkCommand.java
    backend/manager/modules/common/src/main/java/org/ovirt/engine/core/common/businessentities/NetworkInterface.java
    backend/manager/modules/vdsbroker/src/main/java/org/ovirt/engine/core/vdsbroker/vdsbroker/DestroyVDSCommand.java
    backend/manager/modules/vdsbroker/src/main/java/org/ovirt/engine/core/vdsbroker/vdsbroker/VdsProperties.java
    backend/manager/modules/vdsbroker/src/main/java/org/ovirt/engine/core/vdsbroker/vdsbroker/VmInfoBuilder.java

Follow the oVirt build instructions:
http://www.ovirt.org/wiki/Building_Ovirt_Engine#Building_oVirt-engine_from_source

Good luck!
