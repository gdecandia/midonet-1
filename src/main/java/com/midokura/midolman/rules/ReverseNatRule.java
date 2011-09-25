package com.midokura.midolman.rules;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midokura.midolman.layer4.NwTpPair;
import com.midokura.midolman.packets.IPv4;
import com.midokura.midolman.rules.RuleResult.Action;

public class ReverseNatRule extends NatRule {

    private final static Logger log = LoggerFactory
            .getLogger(ReverseNatRule.class);

    public ReverseNatRule(Condition condition, Action action, boolean dnat) {
        super(condition, action, dnat);
    }

    // default constructor for the JSON serialization.
    public ReverseNatRule() {
        super();
    }

    public ReverseNatRule(Condition condition, Action action, UUID chainId,
            int position, boolean dnat) {
        super(condition, action, chainId, position, dnat);
    }

    @Override
    public void apply(UUID inPortId, UUID outPortId, RuleResult res) {
        if (dnat)
            applyReverseDnat(inPortId, outPortId, res);
        else
            applyReverseSnat(inPortId, outPortId, res);
    }

    private void applyReverseDnat(UUID inPortId, UUID outPortId, RuleResult res) {
        if (null == natMap)
            return;
        NwTpPair origConn = natMap.lookupDnatRev(res.match
                .getNetworkDestination(), res.match.getTransportDestination(),
                res.match.getNetworkSource(), res.match.getTransportSource());
        if (null == origConn)
            return;
        log.debug("Found reverse DNAT. Use SRC {}:{} for flow from {}:{} to "
                + "{}:{}", new Object[] {
                IPv4.fromIPv4Address(origConn.nwAddr), origConn.tpPort,
                IPv4.fromIPv4Address(res.match.getNetworkSource()),
                res.match.getTransportSource(),
                IPv4.fromIPv4Address(res.match.getNetworkDestination()),
                res.match.getTransportDestination() });
        res.match.setNetworkSource(origConn.nwAddr);
        res.match.setTransportSource(origConn.tpPort);
        res.action = action;
    }

    private void applyReverseSnat(UUID inPortId, UUID outPortId, RuleResult res) {
        if (null == natMap)
            return;
        NwTpPair origConn = natMap.lookupSnatRev(res.match
                .getNetworkDestination(), res.match.getTransportDestination(),
                res.match.getNetworkSource(), res.match.getTransportSource());
        if (null == origConn)
            return;
        log.debug("Found reverse SNAT. Use DST {}:{} for flow from {}:{} to "
                + "{}:{}", new Object[] {
                IPv4.fromIPv4Address(origConn.nwAddr), origConn.tpPort,
                IPv4.fromIPv4Address(res.match.getNetworkSource()),
                res.match.getTransportSource(),
                IPv4.fromIPv4Address(res.match.getNetworkDestination()),
                res.match.getTransportDestination() });
        res.match.setNetworkDestination(origConn.nwAddr);
        res.match.setTransportDestination(origConn.tpPort);
        res.action = action;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 29 + "ReverseNatRule".hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof ReverseNatRule))
            return false;
        return super.equals(other);
    }
}
