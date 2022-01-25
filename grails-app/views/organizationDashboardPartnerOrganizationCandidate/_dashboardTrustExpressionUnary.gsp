<%@ page import="edu.gatech.gtri.trustmark.v1_0.tip.TrustExpressionType.TrustExpressionTypeBoolean" contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.gatech.gtri.trustmark.v1_0.tip.TrustExpressionType.TrustExpressionTypeDateTimeStamp" contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.gatech.gtri.trustmark.v1_0.tip.TrustExpressionType.TrustExpressionTypeDecimal" contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.gatech.gtri.trustmark.v1_0.tip.TrustExpressionType.TrustExpressionTypeString" contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.gatech.gtri.trustmark.v1_0.tip.TrustExpressionType.TrustExpressionTypeStringList" contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.gatech.gtri.trustmark.v1_0.tip.TrustExpressionType.TrustExpressionTypeNone" contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONObject" contentType="text/html;charset=UTF-8" %>
<%@ page import="org.json.JSONArray" contentType="text/html;charset=UTF-8" %>

<g:set var="trustExpressionType" value="${(trustExpression as JSONObject).get("\$Type")}"/>

<g:set var="trustExpressionData" value="${(trustExpression as JSONObject).get("TrustExpressionData") as JSONObject}"/>

<g:if test="${trustExpressionData.has("TrustExpressionFailureList")}">

    <g:set var="trustExpressionFailureList" value="${trustExpressionData.get("TrustExpressionFailureList") as JSONArray}"/>
    <g:set var="trustExpressionFailure" value="${trustExpressionFailureList[0] as JSONObject}"/>
    <g:set var="trustExpressionFailureType" value="${trustExpressionFailure.get("\$Type")}"/>
    <g:set var="trustInteroperabilityProfileList" value="${trustExpressionFailure.get("TrustInteroperabilityProfileList") as JSONArray}"/>
    <g:set var="trustInteroperabilityProfile" value="${trustInteroperabilityProfileList[0] as JSONObject}"/>
    <g:set var="trustInteroperabilityProfileURI" value="${trustInteroperabilityProfile.get("Identifier")}"/>
    <g:set var="trustInteroperabilityProfileName" value="${trustInteroperabilityProfile.get("Name")}"/>

    <div class="${trustInteroperabilityProfileParentURI == "" ? "TrustExpressionTop" : trustInteroperabilityProfileParentURI != trustInteroperabilityProfileURI ? "TrustExpressionSub" : "TrustExpression"} FAILURE">
        <g:if test="${trustInteroperabilityProfileParentURI != trustInteroperabilityProfileURI}">
            <g:set var="id" value="${UUID.randomUUID().toString()}"/>
            <input type="checkbox" id="id-${id}" ${trustInteroperabilityProfileParentURI != "" ? "checked" : ""}>
            <label class="TrustInteroperabilityProfileInner" for="id-${id}"><span class="glyphicon bi-list-ul"></span>${trustInteroperabilityProfileName}
                <span class="EvaluationLocalDateTime">${evaluationLocalDateTime}</span>
            </label>
        </g:if>

        <div class="TrustExpressionOperatorUnary">
            <div><div>
                <g:if test="${trustExpressionType.equals("TrustExpressionOperatorNoop")}"></g:if>
                <g:elseif test="${trustExpressionType.equals("TrustExpressionOperatorNot")}">NOT</g:elseif>
                <g:elseif test="${trustExpressionType.equals("TrustExpressionOperatorExists")}">EXISTS</g:elseif>
                <g:else>?</g:else>
            </div></div>

            <div>
                <g:render template="dashboardTrustExpression"
                          model="${[trustExpression                      : (trustExpression as JSONObject).get("TrustExpression"),
                                    trustInteroperabilityProfileParentURI: trustInteroperabilityProfileURI]}"/>
            </div>
        </div>
    </div>
</g:if>
<g:else>
    <g:set var="trustInteroperabilityProfileList" value="${(trustExpressionData.get("TrustExpressionEvaluatorDataSource") as JSONObject).get("TrustInteroperabilityProfileList") as JSONArray}"/>
    <g:set var="trustInteroperabilityProfile" value="${trustInteroperabilityProfileList[0] as JSONObject}"/>
    <g:set var="trustInteroperabilityProfileURI" value="${trustInteroperabilityProfile.get("Identifier")}"/>
    <g:set var="trustInteroperabilityProfileName" value="${trustInteroperabilityProfile.get("Name")}"/>

    <g:set var="trustExpressionEvaluatorDataType" value="${trustExpressionData.get("TrustExpressionEvaluatorDataType")}"/>
    <g:set var="trustExpressionEvaluatorDataValue" value="${trustExpressionData.get("TrustExpressionEvaluatorDataValue")}"/>
    <g:set var="trustExpressionEvaluatorState" value="${trustInteroperabilityProfileParentURI == "" ? (trustExpressionEvaluatorDataType == TrustExpressionTypeBoolean.TYPE_BOOLEAN.getClass().getSimpleName() ? (trustExpressionEvaluatorDataValue ? "TRUE" : "FALSE") : "FAILURE") : (trustExpressionEvaluatorDataType == TrustExpressionTypeBoolean.TYPE_BOOLEAN.getClass().getSimpleName() ? (trustExpressionEvaluatorDataValue ? "TRUE" : "FALSE") : "UNKNOWN")}"/>

    <g:if test="${trustExpressionType.equals("TrustExpressionOperatorNoop") && trustInteroperabilityProfileParentURI == trustInteroperabilityProfileURI}">
        <g:render template="dashboardTrustExpression"
                  model="${[trustExpression                      : (trustExpression as JSONObject).get("TrustExpression"),
                            trustInteroperabilityProfileParentURI: trustInteroperabilityProfileURI]}"/>
    </g:if>
    <g:else>
        <div class="${trustInteroperabilityProfileParentURI == "" ? "TrustExpressionTop" : trustInteroperabilityProfileParentURI != trustInteroperabilityProfileURI ? "TrustExpressionSub" : "TrustExpression"} ${trustExpressionEvaluatorState}">
            <g:if test="${trustInteroperabilityProfileParentURI != trustInteroperabilityProfileURI}">
                <g:set var="id" value="${UUID.randomUUID().toString()}"/>
                <input type="checkbox" id="id-${id}" ${trustInteroperabilityProfileParentURI != "" ? "checked" : ""}>
                <label class="TrustInteroperabilityProfileInner" for="id-${id}"><span class="glyphicon bi-list-ul"></span>${trustInteroperabilityProfileName}
                    <span class="EvaluationLocalDateTime">${evaluationLocalDateTime}</span>
                </label>
            </g:if>

            <div class="TrustExpressionOperatorUnary">
                <div><div>
                    <g:if test="${trustExpressionType.equals("TrustExpressionOperatorNoop")}"></g:if>
                    <g:elseif test="${trustExpressionType.equals("TrustExpressionOperatorNot")}">NOT</g:elseif>
                    <g:elseif test="${trustExpressionType.equals("TrustExpressionOperatorExists")}">EXISTS</g:elseif>
                    <g:else>?</g:else>
                </div></div>

                <div>
                    <g:render template="dashboardTrustExpression"
                              model="${[trustExpression                      : (trustExpression as JSONObject).get("TrustExpression"),
                                        trustInteroperabilityProfileParentURI: trustInteroperabilityProfileURI]}"/>
                </div>
            </div>
        </div>
    </g:else>
</g:else>

