package edu.gatech.gtri.trustmark.trpt.domain

import org.gtri.fj.data.Option

import java.time.LocalDateTime

import static org.gtri.fj.data.List.iterableList
import static org.gtri.fj.data.Option.fromNull

class TrustInteroperabilityProfileUri {

    String uri
    String name
    String description
    LocalDateTime publicationLocalDateTime
    String issuerName
    String issuerIdentifier
    String hash
    String json
    LocalDateTime requestLocalDateTime
    LocalDateTime successLocalDateTime
    LocalDateTime failureLocalDateTime
    LocalDateTime changeLocalDateTime
    String failureMessage

    static constraints = {
        uri nullable: true
        name nullable: true
        description nullable: true
        publicationLocalDateTime nullable: true
        issuerName nullable: true
        issuerIdentifier nullable: true
        hash nullable: true
        json nullable: true
        requestLocalDateTime nullable: true
        successLocalDateTime nullable: true
        failureLocalDateTime nullable: true
        changeLocalDateTime nullable: true
        failureMessage nullable: true
    }

    static hasMany = [
            partnerSystemCandidateTrustInteroperabilityProfileUriSet: PartnerSystemCandidateTrustInteroperabilityProfileUri,
            protectedSystemTrustInteroperabilityProfileUriSet       : ProtectedSystemTrustInteroperabilityProfileUri
    ]

    static mapping = {
        table 'trust_interoperability_profile_uri'
        uri length: 200
        name length: 200
        description length: 200
        issuerName length: 200
        issuerIdentifier length: 200
        hash length: 200
        json type: 'text'
        failureMessage length: 200
    }

    long idHelper() { id }

    org.gtri.fj.data.List<PartnerSystemCandidateTrustInteroperabilityProfileUri> partnerSystemCandidateTrustInteroperabilityProfileUriSetHelper() { fromNull(getPartnerSystemCandidateTrustInteroperabilityProfileUriSet()).map({ list -> iterableList(list) }).orSome(org.gtri.fj.data.List.<PartnerSystemCandidateTrustInteroperabilityProfileUri> nil()) }

    void partnerSystemCandidateTrustInteroperabilityProfileUriSetHelper(final org.gtri.fj.data.List<PartnerSystemCandidateTrustInteroperabilityProfileUri> partnerSystemCandidateTrustInteroperabilityProfileUriSet) { setPartnerSystemCandidateTrustInteroperabilityProfileUriSet(new HashSet<>(partnerSystemCandidateTrustInteroperabilityProfileUriSet.toJavaList())) }

    org.gtri.fj.data.List<ProtectedSystemTrustInteroperabilityProfileUri> protectedSystemTrustInteroperabilityProfileUriSetHelper() { fromNull(getProtectedSystemTrustInteroperabilityProfileUriSet()).map({ list -> iterableList(list) }).orSome(org.gtri.fj.data.List.<ProtectedSystemTrustInteroperabilityProfileUri> nil()) }

    void protectedSystemTrustInteroperabilityProfileUriSetHelper(final org.gtri.fj.data.List<ProtectedSystemTrustInteroperabilityProfileUri> protectedSystemTrustInteroperabilityProfileUriSet) { setProtectedSystemTrustInteroperabilityProfileUriSet(new HashSet<>(protectedSystemTrustInteroperabilityProfileUriSet.toJavaList())) }

    void deleteHelper() { delete(failOnError: true) }

    void deleteAndFlushHelper() { delete(flush: true, failOnError: true) }

    TrustInteroperabilityProfileUri saveHelper() { save(failOnError: true) }

    TrustInteroperabilityProfileUri saveAndFlushHelper() { save(flush: true, failOnError: true) }

    static final org.gtri.fj.data.List<TrustInteroperabilityProfileUri> findAllHelper() { fromNull(findAll()).map({ List<TrustInteroperabilityProfileUri> list -> iterableList(list) }) orSome(org.gtri.fj.data.List.<TrustInteroperabilityProfileUri> nil()) }

    static final Option<TrustInteroperabilityProfileUri> findByIdHelper(long id) { fromNull(findById(id)) }

    static final Option<TrustInteroperabilityProfileUri> findByUriHelper(final String uri) { fromNull(findByUri(uri)) }

}
