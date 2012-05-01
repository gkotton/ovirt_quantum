package org.ovirt.engine.core.bll.storage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;

import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.AddNetworkStoragePoolParameters;
import org.ovirt.engine.core.common.businessentities.network;
import org.ovirt.engine.core.common.validation.group.CreateEntity;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dal.VdcBllMessages;
import org.ovirt.engine.core.dal.dbbroker.DbFacade;
import org.ovirt.engine.core.utils.linq.LinqUtils;
import org.ovirt.engine.core.utils.linq.Predicate;

public class AddNetworkCommand<T extends AddNetworkStoragePoolParameters> extends NetworkCommon<T> {
    public AddNetworkCommand(T parameters) {
        super(parameters);
    }

    @Override
    protected void executeCommand() {
        getParameters().getNetwork().setId(Guid.NewGuid());
        DbFacade.getInstance().getNetworkDAO().save(getParameters().getNetwork());
        getReturnValue().setActionReturnValue(getParameters().getNetwork().getId());
        if (getParameters().getNetwork().getname().startsWith("Q_")) {
            String command = String.format("/tmp/ovirt.sh network create %s", getParameters().getNetwork().getname());
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(new String[] { "/bin/bash", "-c", command});
                int exitValue = process.waitFor();
                System.out.println("exit value: " + exitValue);
                BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = buf.readLine()) != null) {
                    System.out.println("exec response: " + line);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        setSucceeded(true);
    }

    @Override
    protected boolean canDoAction() {
        addCanDoActionMessage(VdcBllMessages.VAR__ACTION__ADD);
        addCanDoActionMessage(VdcBllMessages.VAR__TYPE__NETWORK);

        // check that network name not start with 'bond'
        if (getParameters().getNetwork().getname().toLowerCase().startsWith("bond")) {
            addCanDoActionMessage(VdcBllMessages.NETWORK_CANNOT_CONTAIN_BOND_NAME);
            return false;
        }

        // we return ok only if the network not exists
        List<network> all;
        if (getParameters().getNetwork().getstorage_pool_id() != null
                && !getParameters().getNetwork().getstorage_pool_id().getValue().equals(Guid.Empty)) {
            all = DbFacade.getInstance().getNetworkDAO().getAllForDataCenter(
                    getParameters().getNetwork().getstorage_pool_id().getValue());
        } else {
            all = DbFacade.getInstance().getNetworkDAO().getAll();
        }
        boolean exists = null != LinqUtils.firstOrNull(all, new Predicate<network>() {
            @Override
            public boolean eval(network net) {
                return net.getname().equals(getParameters().getNetwork().getname());
            }
        });
        if (exists) {
            getReturnValue().getCanDoActionMessages()
                    .add(VdcBllMessages.NETWROK_NAME_ALREADY_EXISTS.toString());
            return false;
        }

        if (getParameters().getNetwork().getvlan_id() != null) {
            if (!IsVlanInRange(getParameters().getNetwork().getvlan_id())) {
                addCanDoActionMessage(VdcBllMessages.NETWROK_VLAN_OUT_OF_RANGE);
                return false;
            }
            else if (null != LinqUtils.firstOrNull(all, new Predicate<network>() {
                @Override
                public boolean eval(network n) {
                    if (n.getvlan_id() != null) {
                        return n.getvlan_id().equals(getParameters().getNetwork().getvlan_id())
                                && n.getstorage_pool_id().equals(getParameters().getNetwork().getstorage_pool_id());
                    }
                    return false;
                }
            })) {
                addCanDoActionMessage(VdcBllMessages.NETWROK_VLAN_IN_USE);
                return false;
            }
        }
        return true;
    }

    public static boolean IsVlanInRange(int vlanId) {
        return (vlanId >= 0 && vlanId <= 4095);
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return getSucceeded() ? AuditLogType.NETWORK_ADD_NETWORK : AuditLogType.NETWORK_ADD_NETWORK_FAILED;
    }

    @Override
    protected List<Class<?>> getValidationGroups() {
        addValidationGroup(CreateEntity.class);
        return super.getValidationGroups();
    }
}
