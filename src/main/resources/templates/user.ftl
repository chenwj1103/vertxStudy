<#ftl outputFormat="JSON">
{
    <#if context.result??>
        "success" : ${context.result.success?string('true','false')},
        <#if context.result.success>
            "data" : {
                 "weMediaId": "${context.result.data.weMediaId!}",
                 "eAccountId": ${context.result.data.eAccountId!},
                 "weMediaName":"${context.result.data.weMediaName!}",
                 "weMediaImg":"${context.result.data.weMediaImg!}",
                 "systemOfflineReason":"${context.result.data.systemOfflineReason!}"
                    }
        <#else>
            "errorMsg" : "${context.result.errorMsg!}"
        </#if>
    </#if>
}