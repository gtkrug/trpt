package edu.gatech.gtri.trustmark.trpt.service.job.resolver;

import edu.gatech.gtri.trustmark.trpt.domain.TrustmarkBindingRegistryOrganizationTrustmarkMapUri;
import edu.gatech.gtri.trustmark.trpt.service.job.urisynchronizer.UriSynchronizerForTrustmarkBindingRegistryOrganizationTrustmarkMap;
import edu.gatech.gtri.trustmark.v1_0.FactoryLoader;
import edu.gatech.gtri.trustmark.v1_0.io.TrustmarkBindingRegistryOrganizationTrustmarkMapResolver;
import edu.gatech.gtri.trustmark.v1_0.model.trustmarkBindingRegistry.TrustmarkBindingRegistryOrganizationTrustmarkMap;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.gtri.fj.data.Option.fromNull;

public final class DatabaseCacheTrustmarkBindingRegistryOrganizationTrustmarkMapResolver extends DatabaseCacheResolver<TrustmarkBindingRegistryOrganizationTrustmarkMap, TrustmarkBindingRegistryOrganizationTrustmarkMapUri> implements TrustmarkBindingRegistryOrganizationTrustmarkMapResolver {

    public DatabaseCacheTrustmarkBindingRegistryOrganizationTrustmarkMapResolver() {
        super(
                FactoryLoader.getInstance(TrustmarkBindingRegistryOrganizationTrustmarkMapResolver.class),
                (uriString) -> TrustmarkBindingRegistryOrganizationTrustmarkMapUri.withTransactionHelper(() -> TrustmarkBindingRegistryOrganizationTrustmarkMapUri.findByUriHelper(uriString)),
                uri -> fromNull(uri.getDocument()),
                (uriString) -> UriSynchronizerForTrustmarkBindingRegistryOrganizationTrustmarkMap.INSTANCE.synchronizeUri(LocalDateTime.now(ZoneOffset.UTC), uriString));
    }
}
