package edu.gatech.gtri.trustmark.trpt.service.partnerSystemCandidate;

import edu.gatech.gtri.trustmark.trpt.domain.PartnerSystemCandidate;
import edu.gatech.gtri.trustmark.v1_0.model.TrustmarkBindingRegistrySystemType;

import static edu.gatech.gtri.trustmark.trpt.service.trustmarkBindingRegistry.TrustmarkBindingRegistryUtility.trustmarkBindingRegistryResponse;

public final class PartnerSystemCandidateUtility {

    public static PartnerSystemCandidateResponse partnerSystemCandidateResponse(final PartnerSystemCandidate partnerSystemCandidate) {

        return new PartnerSystemCandidateResponse(
                partnerSystemCandidate.idHelper(),
                partnerSystemCandidate.getName(),
                partnerSystemCandidate.getUri(),
                partnerSystemCandidate.getUriEntityDescriptor(),
                partnerSystemCandidateTypeResponse(partnerSystemCandidate.getType()),
                partnerSystemCandidate.getRequestLocalDateTime(),
                partnerSystemCandidate.getSuccessLocalDateTime(),
                partnerSystemCandidate.getFailureLocalDateTime(),
                partnerSystemCandidate.getFailureMessage(),
                partnerSystemCandidate.trustmarkBindingRegistryUriTypeHelper().trustmarkBindingRegistryUriHelper().trustmarkBindingRegistrySetHelper().isEmpty() ?
                        null :
                        trustmarkBindingRegistryResponse(partnerSystemCandidate.trustmarkBindingRegistryUriTypeHelper().trustmarkBindingRegistryUriHelper().trustmarkBindingRegistrySetHelper().head()));
    }

    public static PartnerSystemCandidateTypeResponse partnerSystemCandidateTypeResponse(final TrustmarkBindingRegistrySystemType partnerSystemCandidateType) {

        return new PartnerSystemCandidateTypeResponse(
                partnerSystemCandidateType.name(),
                partnerSystemCandidateType.getName());
    }
}
