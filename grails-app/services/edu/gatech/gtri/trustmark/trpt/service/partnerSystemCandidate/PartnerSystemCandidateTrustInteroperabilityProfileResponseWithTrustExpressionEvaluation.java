package edu.gatech.gtri.trustmark.trpt.service.partnerSystemCandidate;

import edu.gatech.gtri.trustmark.trpt.service.trustInteroperabilityProfile.TrustInteroperabilityProfileResponse;

import java.util.List;

public class PartnerSystemCandidateTrustInteroperabilityProfileResponseWithTrustExpressionEvaluation {

    private final TrustInteroperabilityProfileResponse trustInteroperabilityProfile;
    private final List<EvaluationResponseWithTrustExpressionEvaluation> evaluationList;

    public PartnerSystemCandidateTrustInteroperabilityProfileResponseWithTrustExpressionEvaluation(
            final TrustInteroperabilityProfileResponse trustInteroperabilityProfile,
            final List<EvaluationResponseWithTrustExpressionEvaluation> evaluationList) {

        this.trustInteroperabilityProfile = trustInteroperabilityProfile;
        this.evaluationList = evaluationList;
    }

    public TrustInteroperabilityProfileResponse getTrustInteroperabilityProfile() {
        return trustInteroperabilityProfile;
    }

    public List<EvaluationResponseWithTrustExpressionEvaluation> getEvaluationList() {
        return evaluationList;
    }
}
