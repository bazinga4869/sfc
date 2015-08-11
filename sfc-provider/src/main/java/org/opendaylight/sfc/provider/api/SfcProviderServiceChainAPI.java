/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.sfc.provider.api;

import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.sfc.provider.OpendaylightSfc;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.ServiceFunctionChains;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.ServiceFunctionChainsState;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.service.function.chain.grouping.ServiceFunctionChain;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.service.function.chain.grouping.ServiceFunctionChainKey;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.service.function.chains.state.ServiceFunctionChainState;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.service.function.chains.state.ServiceFunctionChainStateKey;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.service.function.chains.state.service.function.chain.state.SfcServicePath;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.service.function.chains.state.service.function.chain.state.SfcServicePathBuilder;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.sfc.rev140701.service.function.chains.state.service.function.chain.state.SfcServicePathKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.opendaylight.sfc.provider.SfcProviderDebug.printTraceStart;
import static org.opendaylight.sfc.provider.SfcProviderDebug.printTraceStop;


/**
 * This class has the APIs to operate on the ServiceFunctionChain
 * datastore.
 * <p/>
 * It is normally called from onDataChanged() through a executor
 * service. We need to use an executor service because we can not
 * operate on a datastore while on onDataChanged() context.
 *
 * @author Reinaldo Penno (rapenno@gmail.com)
 * @author Konstantin Blagov (blagov.sk@hotmail.com)
 * @author Vladimir Lavor (vladimir.lavor@pantheon.sk)
 * @version 0.1
 * @see org.opendaylight.sfc.provider.SfcProviderSfEntryDataListener
 * <p/>
 * <p/>
 * <p/>
 * @since 2014-06-30
 */
public class SfcProviderServiceChainAPI extends SfcProviderAbstractAPI {

    private static final Logger LOG = LoggerFactory.getLogger(SfcProviderServiceChainAPI.class);
    private static final OpendaylightSfc ODL_SFC = OpendaylightSfc.getOpendaylightSfcObj();
    private static final String FAILED_TO_STR = "failed to ...";

    SfcProviderServiceChainAPI(Object[] params, String m) {
        super(params, m);
    }

    SfcProviderServiceChainAPI(Object[] params, Class[] paramsTypes, String m) {
        super(params, paramsTypes, m);
    }

    public static SfcProviderServiceChainAPI getPut(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "putServiceFunctionChain");
    }

    public static SfcProviderServiceChainAPI getRead(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "readServiceFunctionChain");
    }

    public static SfcProviderServiceChainAPI getDelete(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "deleteServiceFunctionChain");
    }

    public static SfcProviderServiceChainAPI getPutAll(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "putAllServiceFunctionChains");
    }

    public static SfcProviderServiceChainAPI getReadAll(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "readAllServiceFunctionChains");
    }

    public static SfcProviderServiceChainAPI getDeleteAll(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "deleteAllServiceFunctionChains");
    }

    public static SfcProviderServiceChainAPI getSfcRef(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "getServiceFunctionChainsRef");
    }

    public static SfcProviderServiceChainAPI getSfcStateRef(Object[] params, Class[] paramsTypes) {
        return new SfcProviderServiceChainAPI(params, paramsTypes, "getServiceFunctionChainsStateRef");
    }

    /**
     * This method creates a service chain by Executor, it includes
     * Executor creation and response management.
     * <p/>
     * <p/>
     *
     * @param serviceFunctionChain a ServiceFunctionChain object
     * @return true if serviceFunctionChain was created, false otherwise
     */
    @SuppressWarnings("unused")
    public static boolean putServiceFunctionChainExecutor(ServiceFunctionChain serviceFunctionChain) {
        boolean ret = false;
        Object[] sfcParameters = {serviceFunctionChain};
        Class[] sfcParameterTypes = {ServiceFunctionChain.class};

        printTraceStart(LOG);
        try {
            Object result = ODL_SFC.getExecutor().submit(SfcProviderServiceChainAPI.getPut(sfcParameters, sfcParameterTypes)).get();
            ret = (boolean) result;
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn(FAILED_TO_STR, e);
        }
        printTraceStop(LOG);
        return ret;
    }

    /**
     * This method reads the service function chain specified by the given name from
     * the datastore
     * <p/>
     *
     * @param serviceFunctionChainName SF name
     * @return A ServiceFunctionState object that is a list of all paths using
     * this service function, null otherwise
     */
    public static ServiceFunctionChain readServiceFunctionChain(String serviceFunctionChainName) {
        printTraceStart(LOG);
        ServiceFunctionChain sfc;
        InstanceIdentifier<ServiceFunctionChain> sfcIID;
        ServiceFunctionChainKey serviceFunctionChainKey =
                new ServiceFunctionChainKey(serviceFunctionChainName);
        sfcIID = InstanceIdentifier.builder(ServiceFunctionChains.class)
                .child(ServiceFunctionChain.class, serviceFunctionChainKey).build();

        sfc = SfcDataStoreAPI.readTransactionAPI(sfcIID, LogicalDatastoreType.CONFIGURATION);

        printTraceStop(LOG);
        return sfc;
    }

    /**
     * This method reads the operational state for a service function.
     * <p/>
     *
     * @param serviceFunctionName SF name
     * @return A ServiceFunctionState object that is a list of all paths using
     * this service function, null otherwise
     */
    public static ServiceFunctionChain readServiceFunctionChainExecutor(String serviceFunctionName) {

        printTraceStart(LOG);
        ServiceFunctionChain ret = null;
        Object[] servicePathObj = {serviceFunctionName};
        Class[] servicePathClass = {String.class};
        SfcProviderServiceChainAPI sfcProviderServiceChainAPI = SfcProviderServiceChainAPI
                .getRead(servicePathObj, servicePathClass);
        Future future = ODL_SFC.getExecutor().submit(sfcProviderServiceChainAPI);
        try {
            ret = (ServiceFunctionChain) future.get();
            LOG.debug("getRead: {}", future.get());
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("failed to ....", e);
        }
        printTraceStop(LOG);
        return ret;
    }

    /**
     * This method deletes a single service path name from the Service Function
     * Chain operational data store
     * <p/>
     *
     * @param serviceFunctionChainName sfc name
     * @param servicePathName          service path name
     * @return A ServiceFunctionState object that is a list of all paths using
     * this service function, null otherwise
     */
    @SuppressWarnings("unused")
    public static boolean deletePathFromServiceFunctionChainState(String serviceFunctionChainName,
                                                                  String servicePathName) {

        printTraceStart(LOG);
        boolean ret = false;
        ServiceFunctionChainStateKey serviceFunctionChainStateKey = new
                ServiceFunctionChainStateKey(serviceFunctionChainName);
        InstanceIdentifier<SfcServicePath> sfcoIID = InstanceIdentifier
                .builder(ServiceFunctionChainsState.class)
                .child(ServiceFunctionChainState.class, serviceFunctionChainStateKey)
                .child(SfcServicePath.class, new SfcServicePathKey(servicePathName)).build();

        if (SfcDataStoreAPI.deleteTransactionAPI(sfcoIID, LogicalDatastoreType.OPERATIONAL)) {
            ret = true;
        }

        printTraceStop(LOG);
        return ret;
    }

    /**
     * This method adds a single service path name to the Service Function
     * Chain operational data store
     * <p/>
     *
     * @param serviceFunctionChainName sfc name
     * @param servicePathName          service path name
     * @return A ServiceFunctionState object that is a list of all paths using
     * this service function, null otherwise
     */
    @SuppressWarnings("unused")
    public static boolean addPathToServiceFunctionChainState(String serviceFunctionChainName,
                                                             String servicePathName) {

        printTraceStart(LOG);
        boolean ret = false;
        ServiceFunctionChainStateKey serviceFunctionChainStateKey = new
                ServiceFunctionChainStateKey(serviceFunctionChainName);
        InstanceIdentifier<SfcServicePath> sfcoIID = InstanceIdentifier
                .builder(ServiceFunctionChainsState.class)
                .child(ServiceFunctionChainState.class, serviceFunctionChainStateKey)
                .child(SfcServicePath.class, new SfcServicePathKey(servicePathName)).build();

        SfcServicePathBuilder sfcServicePathBuilder = new SfcServicePathBuilder();
        sfcServicePathBuilder.setKey(new SfcServicePathKey(servicePathName));
        sfcServicePathBuilder.setName(servicePathName);

        if (SfcDataStoreAPI.writeMergeTransactionAPI(sfcoIID, sfcServicePathBuilder.build(),
                LogicalDatastoreType.OPERATIONAL)) {
            ret = true;
        }

        printTraceStop(LOG);
        return ret;
    }

    public static ServiceFunctionChains getServiceFunctionChainsRefExecutor() {
        printTraceStart(LOG);
        ServiceFunctionChains ret = null;
        Object[] servicePathObj = {};
        Class[] servicePathClass = {};
        SfcProviderServiceChainAPI sfcProviderServiceChainAPI = SfcProviderServiceChainAPI
                .getSfcRef(servicePathObj, servicePathClass);
        Future future = ODL_SFC.getExecutor().submit(sfcProviderServiceChainAPI);
        try {
            ret = (ServiceFunctionChains) future.get();
            LOG.debug("getRead: {}", future.get());
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("failed to ....", e);
        }
        printTraceStop(LOG);
        return ret;
    }

    public static ServiceFunctionChainsState getServiceFunctionChainsStateRefExecutor() {
        printTraceStart(LOG);
        ServiceFunctionChainsState ret = null;
        Object[] servicePathObj = {};
        Class[] servicePathClass = {};
        SfcProviderServiceChainAPI sfcProviderServiceChainAPI = SfcProviderServiceChainAPI
                .getSfcStateRef(servicePathObj, servicePathClass);
        Future future = ODL_SFC.getExecutor().submit(sfcProviderServiceChainAPI);
        try {
            ret = (ServiceFunctionChainsState) future.get();
            LOG.debug("getRead: {}", future.get());
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("failed to ....", e);
        }
        printTraceStop(LOG);
        return ret;
    }

    /**
     * This method creates a SFC from the datastore.
     * <p/>
     *
     * @param serviceFunctionChain SFC object
     * @return true if SFC was created, false otherwise
     */
    protected boolean putServiceFunctionChain(ServiceFunctionChain serviceFunctionChain) {
        boolean ret = false;
        printTraceStart(LOG);

        InstanceIdentifier<ServiceFunctionChain> sfcEntryIID =
                InstanceIdentifier.builder(ServiceFunctionChains.class)
                        .child(ServiceFunctionChain.class, serviceFunctionChain.getKey())
                        .build();

        if (SfcDataStoreAPI.writeMergeTransactionAPI(sfcEntryIID, serviceFunctionChain, LogicalDatastoreType.CONFIGURATION)) {
            ret = true;
        } else {
            LOG.error("Failed to create Service Function Chain: {}", serviceFunctionChain);
        }

        printTraceStop(LOG);
        return ret;
    }

    /**
     * This method deletes a SFC from the datastore
     * <p/>
     *
     * @param serviceFunctionChainName SFC name
     * @return true if SF was deleted, false otherwise
     */
    @SuppressWarnings("unused")
    protected boolean deleteServiceFunctionChain(String serviceFunctionChainName) {
        boolean ret = false;
        printTraceStart(LOG);
        ServiceFunctionChainKey serviceFunctionChainKey =
                new ServiceFunctionChainKey(serviceFunctionChainName);
        InstanceIdentifier<ServiceFunctionChain> sfcEntryIID =
                InstanceIdentifier.builder(ServiceFunctionChains.class)
                        .child(ServiceFunctionChain.class, serviceFunctionChainKey).build();

        if (SfcDataStoreAPI.deleteTransactionAPI(sfcEntryIID, LogicalDatastoreType.CONFIGURATION)) {
            ret = true;
        } else {
            LOG.error("Could not delete SFC: {}", serviceFunctionChainName);
        }
        printTraceStop(LOG);
        return ret;
    }

    /**
     * This method puts all service functions from the given object in the
     * datastore
     * <p/>
     *
     * @param sfcs Service Functions Object
     * @return A ServiceFunctionState object that is a list of all paths using
     * this service function, null otherwise
     */
    @SuppressWarnings("unused")
    protected boolean putAllServiceFunctionChains(ServiceFunctionChains sfcs) {
        boolean ret = false;
        printTraceStart(LOG);

        InstanceIdentifier<ServiceFunctionChains> sfcsIID =
                InstanceIdentifier.builder(ServiceFunctionChains.class).build();

        if (SfcDataStoreAPI.writePutTransactionAPI(sfcsIID, sfcs, LogicalDatastoreType.CONFIGURATION)) {
            ret = true;
        }

        printTraceStop(LOG);
        return ret;
    }

    /**
     * This method reads all service function chains from datastore
     * <p/>
     *
     * @return ServiceFunctionChains A object that contains all Service Functions Object
     */
    @SuppressWarnings("unused")
    protected ServiceFunctionChains readAllServiceFunctionChains() {
        ServiceFunctionChains sfcs;
        printTraceStart(LOG);
        InstanceIdentifier<ServiceFunctionChains> sfcsIID = InstanceIdentifier
                .builder(ServiceFunctionChains.class).build();

        sfcs = SfcDataStoreAPI.readTransactionAPI(sfcsIID, LogicalDatastoreType.CONFIGURATION);

        printTraceStop(LOG);
        return sfcs;
    }

    protected boolean deleteAllServiceFunctionChains() {

        printTraceStart(LOG);
        boolean ret = false;

        InstanceIdentifier<ServiceFunctionChains> sfcsIID =
                InstanceIdentifier.builder(ServiceFunctionChains.class).build();

        if (SfcDataStoreAPI.deleteTransactionAPI(sfcsIID, LogicalDatastoreType.CONFIGURATION)) {
            ret = true;
        }
        printTraceStop(LOG);
        return ret;
    }

    public ServiceFunctionChains getServiceFunctionChainsRef() {
        printTraceStart(LOG);

        InstanceIdentifier<ServiceFunctionChains> sfcsIID;
        sfcsIID = InstanceIdentifier.builder(ServiceFunctionChains.class).build();

        ServiceFunctionChains sfc = SfcDataStoreAPI.readTransactionAPI(sfcsIID, LogicalDatastoreType.CONFIGURATION);

        printTraceStop(LOG);

        return sfc;
    }

    protected ServiceFunctionChainsState getServiceFunctionChainsStateRef() {
        printTraceStart(LOG);

        InstanceIdentifier<ServiceFunctionChainsState> sfcsIID;
        sfcsIID = InstanceIdentifier.builder(ServiceFunctionChainsState.class).build();

        ServiceFunctionChainsState sfc = SfcDataStoreAPI.readTransactionAPI(sfcsIID, LogicalDatastoreType.OPERATIONAL);

        printTraceStop(LOG);

        return sfc;
    }
}
