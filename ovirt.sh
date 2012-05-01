#!/bin/bash
#set -x
echo "Quantum invoke - $@" >> /tmp/ovirt.txt
case "$1" in
"network")
	if [ "create" == "$2" ]; then
		echo "Running quantum create_net default $3"  >> /tmp/ovirt.txt
		echo $(quantum create_net default $3| awk '{print $8}') > /tmp/network.$3
	fi
	if [ "remove" == "$2" ]; then
                NET_UUID=`cat /tmp/network.$3`
		echo "Running quantum delete_net default $3 ($NET_UUID)"  >> /tmp/ovirt.txt
		quantum delete_net default $NET_UUID >> /tmp/ovirt.txt
                rm /tmp/network.$3
	fi
	;;
"port")
	if [ "create" == "$2" ]; then
		NET_UUID=`cat /tmp/network.$3`
		echo "Running quantum create_port default $NET_UUID"  >> /tmp/ovirt.txt
		echo $(quantum create_port default $NET_UUID| awk '{print $7}') > /tmp/network.$3.$4.port
		PORT_UUID=`cat /tmp/network.$3.$4.port`
		echo "quantum update_port default $NET_UUID $PORT_UUID state=ACTIVE" >> /tmp/ovirt.txt
		quantum update_port default $NET_UUID $PORT_UUID state=ACTIVE
                uuidgen > /tmp/network.$3.$4.port.attach
		ATTACH_UUID=`cat /tmp/network.$3.$4.port.attach`
		echo "quantum plug_iface default $NET_UUID $PORT_UUID $ATTACH_UUID" >> /tmp/ovirt.txt
		quantum plug_iface default $NET_UUID $PORT_UUID $ATTACH_UUID
		quantum show_net_detail default $NET_UUID >> /tmp/ovirt.txt
		echo "$3" > /tmp/network.$4
	fi
	if [ "remove" == "$2" ]; then
        	NET_NAME=`cat /tmp/network.$3`
		PORT_UUID=`cat /tmp/network.$NET_NAME.$3.port`
		NET_UUID=`cat /tmp/network.$NET_NAME`
                echo "Running quantum unplug_iface default $NET_UUID $PORT_UUID" >> /tmp/ovirt.txt
                quantum unplug_iface default $NET_UUID $PORT_UUID >> /tmp/ovirt.txt
                echo "Running quantum delete_port default $NET_UUID $PORT_UUID"  >> /tmp/ovirt.txt
                quantum delete_port default $NET_UUID $PORT_UUID >> /tmp/ovirt.txt
		rm /tmp/network.$NET_NAME.$3.port.attach
                rm /tmp/network.$NET_NAME.$3.port
                rm /tmp/network.$3
		quantum show_net_detail default $NET_UUID >> /tmp/ovirt.txt
	fi
	;;
"")
	echo "Invalid command"  >> /tmp/ovirt.txt
	;;
esac

exit 0
