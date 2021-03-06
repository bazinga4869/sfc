/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.sfc.sbrest.json;

public class RspExporterFactory implements ExporterFactory {

    public static final String RENDERED_SERVICE_PATH = RspExporter.RENDERED_SERVICE_PATH;
    public static final String NAME = RspExporter.NAME;
    public static final String SERVICE_FUNCTION_FORWARDER = RspExporter.SERVICE_FUNCTION_FORWARDER;
    public static final String RENDERED_SERVICE_PATH_HOP = RspExporter.RENDERED_SERVICE_PATH_HOP;

    @Override
    public Exporter getExporter() {
        return new RspExporter();
    }
}
