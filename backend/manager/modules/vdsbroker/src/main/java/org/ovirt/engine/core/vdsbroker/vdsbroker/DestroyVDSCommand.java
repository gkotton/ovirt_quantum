package org.ovirt.engine.core.vdsbroker.vdsbroker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.ovirt.engine.core.common.config.Config;
import org.ovirt.engine.core.common.config.ConfigValues;
import org.ovirt.engine.core.common.vdscommands.*;

public class DestroyVDSCommand<P extends DestroyVmVDSCommandParameters> extends VdsBrokerCommand<P> {
    public DestroyVDSCommand(P parameters) {
        super(parameters);
    }

    @Override
    protected void ExecuteVdsBrokerCommand() {
        // quantum cleanup - uses the VM ID to delete the relevnat info - fugly
        String command = String.format("/tmp/ovirt.sh port remove %s", getParameters().getVmId().toString());
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
        if (getParameters().getGracefully()) {
            status = getBroker().shutdown(getParameters().getVmId().toString(),
                    (new Integer(getParameters().getSecondsToWait())).toString(),
                    Config.<String> GetValue(ConfigValues.VmGracefulShutdownMessage));
        } else {
            status = getBroker().destroy(getParameters().getVmId().toString());
        }
        ProceedProxyReturnValue();
    }
}
