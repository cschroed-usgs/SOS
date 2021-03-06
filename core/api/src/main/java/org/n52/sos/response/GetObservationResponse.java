/**
 * Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.response;

import java.util.LinkedList;
import java.util.List;

import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.sos.SosConstants;

/**
 * @since 4.0.0
 * 
 */
public class GetObservationResponse extends AbstractObservationResponse {

    public void mergeObservationsWithSameConstellation() {
        // TODO merge all observations with the same observationContellation
        // FIXME Failed to set the observation type to sweArrayObservation for
        // the merged Observations
        // (proc, obsProp, foi)
        if (getObservationCollection() != null) {
            final List<OmObservation> mergedObservations = new LinkedList<OmObservation>();
            int obsIdCounter = 1;
            for (final OmObservation sosObservation : getObservationCollection()) {
                if (mergedObservations.isEmpty()) {
                    sosObservation.setObservationID(Integer.toString(obsIdCounter++));
                    mergedObservations.add(sosObservation);
                } else {
                    boolean combined = false;
                    for (final OmObservation combinedSosObs : mergedObservations) {
                        if (combinedSosObs.getObservationConstellation().equals(
                                sosObservation.getObservationConstellation())) {
                            combinedSosObs.setResultTime(null);
                            combinedSosObs.mergeWithObservation(sosObservation);
                            combined = true;
                            break;
                        }
                    }
                    if (!combined) {
                        mergedObservations.add(sosObservation);
                    }
                }
            }
            setObservationCollection(mergedObservations);
        }
    }

    /*
     * TODO uncomment when WaterML support is activated public
     * Collection<SosObservation> mergeObservations(boolean
     * mergeObservationValuesWithSameParameters) { Collection<SosObservation>
     * combinedObsCol = new ArrayList<SosObservation>(); int obsIdCounter = 1;
     * for (SosObservation sosObservation : observationCollection) { if
     * (combinedObsCol.isEmpty()) {
     * sosObservation.setObservationID(Integer.toString(obsIdCounter++));
     * combinedObsCol.add(sosObservation); } else { boolean combined = false;
     * for (SosObservation combinedSosObs : combinedObsCol) { if
     * (mergeObservationValuesWithSameParameters) { if
     * (combinedSosObs.getObservationConstellation().equals(
     * sosObservation.getObservationConstellation())) {
     * combinedSosObs.mergeWithObservation(sosObservation, false); combined =
     * true; break; } } else { if
     * (combinedSosObs.getObservationConstellation().equalsExcludingObsProp(
     * sosObservation.getObservationConstellation())) {
     * combinedSosObs.mergeWithObservation(sosObservation, true); combined =
     * true; break; } } } if (!combined) { combinedObsCol.add(sosObservation); }
     * } } return combinedObsCol; }
     */

    @Override
    public String getOperationName() {
        return SosConstants.Operations.GetObservation.name();
    }

}
