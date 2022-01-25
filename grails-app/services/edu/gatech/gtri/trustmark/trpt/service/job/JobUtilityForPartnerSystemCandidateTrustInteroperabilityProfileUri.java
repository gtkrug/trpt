package edu.gatech.gtri.trustmark.trpt.service.job;

import edu.gatech.gtri.trustmark.trpt.domain.PartnerSystemCandidate;
import edu.gatech.gtri.trustmark.trpt.domain.PartnerSystemCandidateMailEvaluationUpdate;
import edu.gatech.gtri.trustmark.trpt.domain.PartnerSystemCandidateTrustInteroperabilityProfileUri;
import edu.gatech.gtri.trustmark.trpt.domain.PartnerSystemCandidateTrustmarkUri;
import edu.gatech.gtri.trustmark.trpt.domain.ProtectedSystem;
import edu.gatech.gtri.trustmark.trpt.domain.TrustInteroperabilityProfileUri;
import edu.gatech.gtri.trustmark.trpt.domain.TrustmarkUri;
import edu.gatech.gtri.trustmark.trpt.service.job.resolver.DatabaseCacheTrustInteroperabilityProfileResolver;
import edu.gatech.gtri.trustmark.trpt.service.job.resolver.DatabaseCacheTrustmarkDefinitionResolver;
import edu.gatech.gtri.trustmark.trpt.service.job.resolver.DatabaseCacheTrustmarkResolver;
import edu.gatech.gtri.trustmark.trpt.service.job.resolver.DatabaseCacheTrustmarkStatusReportResolver;
import edu.gatech.gtri.trustmark.v1_0.FactoryLoader;
import edu.gatech.gtri.trustmark.v1_0.impl.tip.evaluator.TrustExpressionEvaluatorImpl;
import edu.gatech.gtri.trustmark.v1_0.impl.tip.evaluator.TrustmarkDefinitionRequirementEvaluatorImpl;
import edu.gatech.gtri.trustmark.v1_0.impl.tip.parser.TrustExpressionParserImpl;
import edu.gatech.gtri.trustmark.v1_0.impl.trust.XmlSignatureValidatorImpl;
import edu.gatech.gtri.trustmark.v1_0.io.TrustInteroperabilityProfileResolver;
import edu.gatech.gtri.trustmark.v1_0.io.TrustmarkDefinitionResolver;
import edu.gatech.gtri.trustmark.v1_0.io.TrustmarkResolver;
import edu.gatech.gtri.trustmark.v1_0.io.TrustmarkStatusReportResolver;
import edu.gatech.gtri.trustmark.v1_0.io.json.JsonManager;
import edu.gatech.gtri.trustmark.v1_0.io.json.JsonProducer;
import edu.gatech.gtri.trustmark.v1_0.tip.evaluator.TrustExpressionEvaluation;
import edu.gatech.gtri.trustmark.v1_0.tip.evaluator.TrustExpressionEvaluator;
import edu.gatech.gtri.trustmark.v1_0.tip.evaluator.TrustmarkDefinitionRequirementEvaluation;
import edu.gatech.gtri.trustmark.v1_0.tip.evaluator.TrustmarkDefinitionRequirementEvaluator;
import edu.gatech.gtri.trustmark.v1_0.tip.parser.TrustExpressionParser;
import edu.gatech.gtri.trustmark.v1_0.trust.TrustmarkVerifier;
import edu.gatech.gtri.trustmark.v1_0.trust.TrustmarkVerifierFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gtri.fj.data.List;
import org.gtri.fj.data.Validation;
import org.gtri.fj.function.Try;
import org.gtri.fj.function.TryEffect;
import org.gtri.fj.product.Unit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static edu.gatech.gtri.trustmark.trpt.service.job.RetryTemplateUtility.retry;
import static java.lang.String.format;
import static org.gtri.fj.data.Either.reduce;
import static org.gtri.fj.data.List.iterableList;
import static org.gtri.fj.data.List.nil;

public class JobUtilityForPartnerSystemCandidateTrustInteroperabilityProfileUri {

    private JobUtilityForPartnerSystemCandidateTrustInteroperabilityProfileUri() {
    }

    private static final Log log = LogFactory.getLog(JobUtilityForPartnerSystemCandidateTrustInteroperabilityProfileUri.class);

    private static final TrustInteroperabilityProfileResolver trustInteroperabilityProfileResolver = new DatabaseCacheTrustInteroperabilityProfileResolver();
    private static final TrustmarkDefinitionResolver trustmarkDefinitionResolver = new DatabaseCacheTrustmarkDefinitionResolver();
    private static final TrustmarkResolver trustmarkResolver = new DatabaseCacheTrustmarkResolver();
    private static final TrustmarkStatusReportResolver trustmarkStatusReportResolver = new DatabaseCacheTrustmarkStatusReportResolver();

    private static final TrustExpressionParser trustExpressionParser = new TrustExpressionParserImpl(trustInteroperabilityProfileResolver, trustmarkDefinitionResolver);
    private static final TrustmarkDefinitionRequirementEvaluator trustmarkDefinitionRequirementEvaluator = new TrustmarkDefinitionRequirementEvaluatorImpl(trustmarkResolver, trustExpressionParser);

    private static final JsonManager jsonManager = FactoryLoader.getInstance(JsonManager.class);
    private static final JsonProducer<TrustExpressionEvaluation, JSONObject> jsonProducerForTrustExpressionEvaluation = jsonManager.findProducerStrict(TrustExpressionEvaluation.class, JSONObject.class).some();
    private static final JsonProducer<TrustmarkDefinitionRequirementEvaluation, JSONObject> jsonProducerForTrustmarkDefinitionRequirementEvaluation = jsonManager.findProducerStrict(TrustmarkDefinitionRequirementEvaluation.class, JSONObject.class).some();

    public static void synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(
            final Duration duration,
            final List<PartnerSystemCandidate> partnerSystemCandidateList,
            final List<TrustInteroperabilityProfileUri> trustInteroperabilityProfileUriList,
            final List<ProtectedSystem> protectedSystemList) {

        final List<PartnerSystemCandidateTrustInteroperabilityProfileUri> partnerSystemCandidateTrustInteroperabilityProfileUriList = retry(() -> PartnerSystemCandidateTrustInteroperabilityProfileUri.withTransactionHelper(() -> PartnerSystemCandidateTrustInteroperabilityProfileUri.findByPartnerSystemCandidateAndTrustInteroperabilityProfileUriAndProtectedSystemHelper(partnerSystemCandidateList, trustInteroperabilityProfileUriList, protectedSystemList)
                .map(p -> p._3().orSome(() -> {
                    final PartnerSystemCandidateTrustInteroperabilityProfileUri partnerSystemCandidateTrustInteroperabilityProfileUri = new PartnerSystemCandidateTrustInteroperabilityProfileUri();
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper(p._1());
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper(p._2());
                    return partnerSystemCandidateTrustInteroperabilityProfileUri.saveAndFlushHelper();
                }))
                .map(partnerSystemCandidateTrustInteroperabilityProfileUri -> {

                    partnerSystemCandidateTrustInteroperabilityProfileUri
                            .trustInteroperabilityProfileUriHelper()
                            .getDocumentChangeLocalDateTime();

                    partnerSystemCandidateTrustInteroperabilityProfileUri
                            .partnerSystemCandidateHelper()
                            .partnerSystemCandidateTrustmarkUriSetHelper()
                            .map(PartnerSystemCandidateTrustmarkUri::trustmarkUriHelper)
                            .forEach(trustmarkUri -> trustmarkUri.getUri());

                    partnerSystemCandidateTrustInteroperabilityProfileUri
                            .setEvaluationAttemptLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));

                    return partnerSystemCandidateTrustInteroperabilityProfileUri;
                })), log);

        new Thread(() -> partnerSystemCandidateTrustInteroperabilityProfileUriList.forEach(partnerSystemCandidateTrustInteroperabilityProfileUri -> synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(duration, partnerSystemCandidateTrustInteroperabilityProfileUri))).start();
    }

    private static void synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(
            final Duration duration,
            final PartnerSystemCandidateTrustInteroperabilityProfileUri partnerSystemCandidateTrustInteroperabilityProfileUri) {

        if (partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getDocumentChangeLocalDateTime() == null ||
                partnerSystemCandidateTrustInteroperabilityProfileUri
                        .partnerSystemCandidateHelper()
                        .partnerSystemCandidateTrustmarkUriSetHelper()
                        .map(PartnerSystemCandidateTrustmarkUri::trustmarkUriHelper)
                        .exists(trustmarkUri -> trustmarkUri.getDocumentChangeLocalDateTime() == null)) {

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' waiting for dependency.",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

        } else if (partnerSystemCandidateTrustInteroperabilityProfileUri.getEvaluationLocalDateTime() == null) {

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' evaluating...",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

            synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(partnerSystemCandidateTrustInteroperabilityProfileUri, true);

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' evaluated",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

        } else if (partnerSystemCandidateTrustInteroperabilityProfileUri.getEvaluationLocalDateTime().isBefore(partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getDocumentChangeLocalDateTime())) {

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' changed (due to changed trust interoperability profile); re-evaluating...",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

            synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(partnerSystemCandidateTrustInteroperabilityProfileUri, true);

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' changed (due to changed trust interoperability profile); re-evaluated",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

        } else if (partnerSystemCandidateTrustInteroperabilityProfileUri
                .partnerSystemCandidateHelper()
                .partnerSystemCandidateTrustmarkUriSetHelper()
                .map(PartnerSystemCandidateTrustmarkUri::trustmarkUriHelper)
                .exists(trustmarkUri -> partnerSystemCandidateTrustInteroperabilityProfileUri.getEvaluationLocalDateTime().isBefore(trustmarkUri.getDocumentChangeLocalDateTime()))) {

            partnerSystemCandidateTrustInteroperabilityProfileUri
                    .partnerSystemCandidateHelper()
                    .partnerSystemCandidateTrustmarkUriSetHelper()
                    .map(PartnerSystemCandidateTrustmarkUri::trustmarkUriHelper)
                    .filter(trustmarkUri -> partnerSystemCandidateTrustInteroperabilityProfileUri.getEvaluationLocalDateTime().isBefore(trustmarkUri.getDocumentChangeLocalDateTime()))
                    .forEach(trustmarkUri -> log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' changed (due to changed trustmark '%s'); re-evaluating...",
                            partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                            partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName(),
                            trustmarkUri.getUri())));

            synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(partnerSystemCandidateTrustInteroperabilityProfileUri, true);

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' changed (due to changed trustmark); re-evaluated",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

        } else if (partnerSystemCandidateTrustInteroperabilityProfileUri.getEvaluationLocalDateTime().isBefore(LocalDateTime.now(ZoneOffset.UTC).minus(duration))) {

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' expired; re-evaluating...",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

            synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(partnerSystemCandidateTrustInteroperabilityProfileUri, false);

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' expired; re-evaluated.",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

        } else {

            log.info(format("Evaluation for trust interoperability profile '%s' and partner system candidate '%s' unchanged.",
                    partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                    partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName()));

        }
    }

    private static void synchronizePartnerSystemCandidateTrustInteroperabilityProfileUri(
            final PartnerSystemCandidateTrustInteroperabilityProfileUri partnerSystemCandidateTrustInteroperabilityProfileUri,
            final boolean mail) {

        final TrustmarkVerifierFactory trustmarkVerifierFactory = FactoryLoader.getInstance(TrustmarkVerifierFactory.class);
        final TrustmarkVerifier trustmarkVerifier = trustmarkVerifierFactory.createVerifier(
                new XmlSignatureValidatorImpl(),
                trustmarkStatusReportResolver,
                nil(),
                iterableList(new JSONArray(partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getTrustmarkRecipientIdentifierArrayJson()).toList()).map(object -> URI.create((String) object)));

        final TrustExpressionEvaluator trustExpressionEvaluator = new TrustExpressionEvaluatorImpl(trustmarkResolver, trustmarkVerifier, trustExpressionParser);

        final TrustExpressionEvaluation trustExpressionEvaluation = trustExpressionEvaluator.evaluate(
                partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                partnerSystemCandidateTrustInteroperabilityProfileUri
                        .partnerSystemCandidateHelper()
                        .partnerSystemCandidateTrustmarkUriSetHelper()
                        .map(PartnerSystemCandidateTrustmarkUri::trustmarkUriHelper)
                        .map(TrustmarkUri::getUri));

// For now, don't save the trust expression evaluation json.
        final String trustExpressionEvaluationJsonString = jsonProducerForTrustExpressionEvaluation
                .serialize(trustExpressionEvaluation)
                .toString(4);

        final TrustmarkDefinitionRequirementEvaluation trustmarkDefinitionRequirementEvaluation = trustmarkDefinitionRequirementEvaluator.evaluate(
                partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                partnerSystemCandidateTrustInteroperabilityProfileUri
                        .partnerSystemCandidateHelper()
                        .partnerSystemCandidateTrustmarkUriSetHelper()
                        .map(PartnerSystemCandidateTrustmarkUri::trustmarkUriHelper)
                        .map(TrustmarkUri::getUri));

// For now, don't save the trustmark definition requirement evaluation json; it is not currently in use.
//                                        final String trustmarkDefinitionRequirementEvaluationJsonString = jsonProducerForTrustmarkDefinitionRequirementEvaluation
//                                                .serialize(trustmarkDefinitionRequirementEvaluation)
//                                                .toString();

        retry(() -> PartnerSystemCandidateTrustInteroperabilityProfileUri.withTransactionHelper(() -> PartnerSystemCandidateTrustInteroperabilityProfileUri.findByPartnerSystemCandidateAndTrustInteroperabilityProfileUriHelper(
                        partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper(),
                        partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper())
                .forEach(partnerSystemCandidateTrustInteroperabilityProfileUriInner -> {

                    log.info(format("(trust interoperability profile '%s', partner system candidate '%s')%n%s",
                            partnerSystemCandidateTrustInteroperabilityProfileUri.trustInteroperabilityProfileUriHelper().getUri(),
                            partnerSystemCandidateTrustInteroperabilityProfileUri.partnerSystemCandidateHelper().getName(),
                            trustExpressionEvaluationJsonString));

                    final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

                    partnerSystemCandidateTrustInteroperabilityProfileUriInner.setEvaluationLocalDateTime(now);
// For now, don't save the trust expression evaluation json.
                    partnerSystemCandidateTrustInteroperabilityProfileUriInner.setEvaluationTrustExpression(gzip(trustExpressionEvaluationJsonString).toOption().orSome(new byte[]{}));
// For now, don't save the trustmark definition requirement evaluation json; it is not currently in use.
//                                                    partnerSystemCandidateTrustInteroperabilityProfileUriInner.setEvaluationTrustmarkDefinitionRequirement(trustmarkDefinitionRequirementEvaluationJsonString);

                    partnerSystemCandidateTrustInteroperabilityProfileUriInner.setEvaluationTrustExpressionSatisfied(
                            reduce(trustExpressionEvaluation.getTrustExpression().getData().toEither().bimap(
                                    nel -> null,
                                    data -> data.matchValueBoolean(
                                            value -> value,
                                            () -> null))));

                    partnerSystemCandidateTrustInteroperabilityProfileUriInner.setEvaluationTrustmarkDefinitionRequirementSatisfied(
                            trustmarkDefinitionRequirementEvaluation
                                    .getTrustmarkDefinitionRequirementSatisfaction()
                                    .map(list -> list
                                            .filter(pInner -> pInner._2().isNotEmpty())
                                            .length())
                                    .orSuccess((Integer) null));

                    partnerSystemCandidateTrustInteroperabilityProfileUriInner.setEvaluationTrustmarkDefinitionRequirementUnsatisfied(
                            trustmarkDefinitionRequirementEvaluation
                                    .getTrustmarkDefinitionRequirementSatisfaction()
                                    .map(list -> list
                                            .filter(pInner -> pInner._2().isEmpty())
                                            .length())
                                    .orSuccess((Integer) null));

                    if (mail) {

                        final PartnerSystemCandidateMailEvaluationUpdate partnerSystemCandidateMailEvaluationUpdate = new PartnerSystemCandidateMailEvaluationUpdate();
                        partnerSystemCandidateMailEvaluationUpdate.partnerSystemCandidateTrustInteroperabilityProfileUriHelper(partnerSystemCandidateTrustInteroperabilityProfileUriInner);
                        partnerSystemCandidateMailEvaluationUpdate.setRequestDateTime(now);
                        partnerSystemCandidateMailEvaluationUpdate.saveHelper();

                        partnerSystemCandidateTrustInteroperabilityProfileUriInner.partnerSystemCandidateMailEvaluationUpdateSetHelper(
                                partnerSystemCandidateTrustInteroperabilityProfileUriInner.partnerSystemCandidateMailEvaluationUpdateSetHelper().snoc(partnerSystemCandidateMailEvaluationUpdate));
                    }

                    partnerSystemCandidateTrustInteroperabilityProfileUriInner.saveAndFlushHelper();
                })), log);
    }

    public static Validation<IOException, byte[]> gzip(final String trustExpressionEvaluationJsonString) {

        final ByteArrayOutputStream trustExpressionEvaluationByteArrayOutputStream = new ByteArrayOutputStream();

        return Try.<GZIPOutputStream, IOException>f(() -> new GZIPOutputStream(trustExpressionEvaluationByteArrayOutputStream))._1()
                .bind(gzipOutputStream -> TryEffect.<Unit, IOException>f(() -> gzipOutputStream.write(trustExpressionEvaluationJsonString.getBytes(StandardCharsets.UTF_8)))._1()
                        .map(unit -> gzipOutputStream))
                .bind(gzipOutputStream -> TryEffect.<Unit, IOException>f(() -> gzipOutputStream.flush())._1()
                        .map(unit -> gzipOutputStream))
                .bind(gzipOutputStream -> TryEffect.<Unit, IOException>f(() -> gzipOutputStream.close())._1()
                        .map(unit -> gzipOutputStream))
                .map(gzipOutputStream -> trustExpressionEvaluationByteArrayOutputStream.toByteArray());
    }

    public static Validation<IOException, String> gunzip(final byte[] trustExpressionEvaluationByteArray) {

        final ByteArrayOutputStream trustExpressionEvaluationByteArrayOutputStream = new ByteArrayOutputStream();

        final Validation<IOException, String> validation = Try.<GZIPInputStream, IOException>f(() -> new GZIPInputStream(new ByteArrayInputStream(trustExpressionEvaluationByteArray)))._1()
                .bind(gzipOutputStream -> TryEffect.<Unit, IOException>f(() -> {
                    int read;
                    while ((read = gzipOutputStream.read()) != -1) {
                        trustExpressionEvaluationByteArrayOutputStream.write(read);
                    }
                })._1().map(unit -> gzipOutputStream))
                .bind(gzipInputStream -> TryEffect.<Unit, IOException>f(() -> gzipInputStream.close())._1()
                        .map(unit -> gzipInputStream))
                .map(gzipInputStream -> new String(trustExpressionEvaluationByteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));

        return validation;
    }
}
