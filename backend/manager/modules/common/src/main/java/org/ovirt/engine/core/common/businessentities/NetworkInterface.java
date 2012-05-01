package org.ovirt.engine.core.common.businessentities;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.ovirt.engine.core.compat.Guid;

/**
 * <code>NetworkInterface</code> represents a network interface device.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "NetworkInterface")
public abstract class NetworkInterface<T extends NetworkStatistics> extends IVdcQueryable
        implements BusinessEntity<Guid>, Nameable {
    private static final long serialVersionUID = -4926026587466645571L;

    @XmlElement(name = "Id")
    protected Guid id;

    @Size(min = 1, max = BusinessEntitiesDefinitions.NETWORK_NAME_SIZE)
    @XmlElement(name = "Name")
    private String name;

    @Size(max = BusinessEntitiesDefinitions.GENERAL_MAC_ADDR_SIZE)
    @XmlElement(name = "MacAddress")
    private String macAddress;

    @Size(max = BusinessEntitiesDefinitions.NETWORK_NAME_SIZE)
    @XmlElement(name = "NetworkName")
    private String networkName;

    @XmlElement(name = "Type", nillable = true)
    private Integer type;

    @XmlElement(name = "Speed", nillable = true)
    private Integer speed;

    //Quantum specific
    @XmlElement(name = "Q_NetworkUUID")
    private String Q_NetworkUUID;

    @XmlElement(name = "Q_PortUUID")
    private String Q_PortUUID;

    @XmlElement(name = "Q_AttachmentUUID")
    private String Q_AttachmentUUID;

    @XmlElement(name = "Statistics")
    protected T statistics;

    public NetworkInterface() {
    }

    public NetworkInterface(T statistics, int type) {
        this.statistics = statistics;
        this.type = type;
    }

    /**
     * Sets the instance id.
     *
     * @param id
     *            the id
     */
    public void setId(Guid id) {
        this.id = id;
        this.statistics.setId(id);
    }

    /**
     * Returns the instance id.
     *
     * @return the id
     */
    public Guid getId() {
        return id;
    }

    /**
     * Sets the interface's name.
     *
     * @param name
     *            the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the interface's name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the MAC address.
     *
     * @param macAddress
     *            the MAC address
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * Returns the device's MAC address.
     *
     * @return the MAC address
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Sets the name of the network.
     *
     * @param networkName
     *            the network name
     */
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    /**
     * Returns the name of the network.
     *
     * @return the network name
     */
    public String getNetworkName() {
        return networkName;
    }

    /**
     * Sets the speed of the network device in megabits per second.
     *
     * @param speed
     *            the speed.
     */
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    /**
     * Returns the speed of the network device in megabits per second.
     *
     * @return the speed
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * Sets the type of network device.
     *
     * @param type
     *            the type
     */
    public void setType(Integer type) {
        this.type = type != null ? type : 0;
    }

    /**
     * Returns the type of network device.
     *
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the statistics for the network device.
     *
     * @param statistics
     *            the statistics
     */
    public void setStatistics(T statistics) {
        this.statistics = statistics;
    }

    /**
     * Returns the statistics for the network device.
     *
     * @return the statistics
     */
    public T getStatistics() {
        return statistics;
    }

    /**
     * Returns the Quantum network UUID.
     *
     * @return the uuid
     */
    public String getQNetworkUUID() {
        return Q_NetworkUUID;
    }

    /**
     * Sets the Quantum network UUID.
     *
     * @param uuid
     *            the Quantum network UUID
     */
    public void setQNetworkUUID(String uuid) {
        this.Q_NetworkUUID = uuid;
    }

    /**
     * Returns the Quantum port UUID.
     *
     * @return the uuid
     */
    public String getQPortUUID() {
        return Q_PortUUID;
    }

    /**
     * Sets the Quantum port UUID.
     *
     * @param uuid
     *            the Quantum port UUID
     */
    public void setQPortUUID(String uuid) {
        this.Q_PortUUID = uuid;
    }

    /**
     * Returns the Quantum attachment UUID.
     *
     * @return the uuid
     */
    public String getQAttachmentUUID() {
        return Q_AttachmentUUID;
    }

    /**
     * Sets the Quantum attachment UUID.
     *
     * @param uuid
     *            the Quantum attachment UUID
     */
    public void setQAttachmentUUID(String uuid) {
        this.Q_AttachmentUUID = uuid;
    }
}
