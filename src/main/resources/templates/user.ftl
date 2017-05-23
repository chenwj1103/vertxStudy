<#ftl outputFormat="JSON">
{
    <#if context.result??>
        "success" : ${context.result.success?string('true','false')},
        <#if context.result.success>
            "data" : {
                 "id": "${context.result.data.id!}",
                 "name": "${context.result.data.name!}",
                 "age": ${context.result.data.age!},
                 "address":"${context.result.data.address!}",
                 "phone":"${context.result.data.phone!}",
                 "createTime":"${(context.result.data.createTime?string('yyyy-MM-dd HH:mm:ss'))!}",
                 "updateTime":"${(context.result.data.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}",
                 "birthday":"${(context.result.data.birthday?string('yyyy-MM-dd HH:mm:ss'))!}"
                    }
        <#else>
            "errorMsg" : "${context.result.errorMsg!}"
        </#if>
    </#if>
}