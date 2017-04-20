plan(key:'REST', name:'rest test', description:'A sensible description goes here...') {
    variable(key:"key1", value:"value1")
    variable(key:"variable1", value:"1")
    variable(key:"variable2", value:"22")
 
    project(key: 'PERSONAL', name:'Z Personal', description:'a project description')
 
    stage(name:"stage1", description: "stage11111Description", manual: "false"){
        job(name: "job1", key: 'job11Key', description: 'hellojob' ){
            requirement(key:"awesome", condition:"exists")
            artifactDefinition(name:'myartifact', location:'./logs', pattern:'*.txt', shared:'true')
            task(type:'script', description:'task2', script:'test1', argument: 'testArg', environmentVariables: 'one', workingSubDirectory: '/one')
        }
    }
}
 
deployment(planKey:"PERSONAL-REST", name: "deploymentTest", description: "a better deployment project"){
 
    versioning(version:'release-1-${bamboo.variable1}-${bamboo.variable2}', autoIncrementNumber: 'true') {
        variableToAutoIncrement(key: 'variable1')
        variableToAutoIncrement(key: 'variable2')
    }
 
    environment(name:"Staging", description:"") {
        variable(key:"key1", value:"value1")
        variable(key:"key2", value:"value2")
 
        task(type:'cleanWorkingDirectory', description:'clean')
 
        task(type:'script', description:'task2', script:'test1', argument: 'testArg', environmentVariables: 'one', workingSubDirectory: '/one')
 
        task(type:'artifactDownload', planKey: 'PERSONAL-REST', description: 'description') {
            artifact(name:'myartifact', localPath:'./logs')
        }
 
        task(type:'addRequirement', description:'description') {
            requirement(key:"req_key", condition:"equals", value:"blah blah")
        }
         
        task(type:'deployBambooPlugin', description:'Upload the plugin', artifact:'myartifact', url:'http://bambooserver', username:'${bamboo.username}', passwordVariable:'${bamboo.password}')
  
        notification(type:'Deployment Started and Finished', recipient:'email', email:'test@test.com')
        notification(type:'Deployment Finished', recipient:'email', email:'test2@test.com')
    }
 
    environment(name:"Production", description:"") {
        trigger(description:'triggerAfter', type:'afterSuccessfulPlan', planKey: 'PERSONAL-REST')
        variable(key:"key3", value:"value3")
 
        task(type:'script', description:'task2', script:'test1', argument: 'testArg', environmentVariables: 'one', workingSubDirectory: '/one')
 
    }          
}
