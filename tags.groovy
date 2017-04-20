plan(key:'AWSTAGS2',name:'LambdaCI AWS Tags checking and modifying2') {
  project(key:'HCLC',name:'HipChat Lambda CI')
    repository(name:'test')
    repository(name:'test2')
     
    stage(name:"stage1", description: "stage1", manual: "false"){
        job(name: "job1", key: 'job11Key', description: 'hellojob' ){           
            // checkout --------------------------------------------------------
            task(type:'checkout', description:'checkout 1'){
                repository(name:'test')
            }
             
            task(type:'checkout', description:'checkout 2', cleanCheckout: 'true'){
                repository(name:'test', checkoutDirectory:'./test1directory')
                repository(name:'test2', checkoutDirectory:'./test2directory')
            }
             
            // script  --------------------------------------------------------
            task(type:'script', description:'task1', scriptBody:'''
                    echo hello1
                    echo hello2
                '''
            )
                         
            task(type:'script', description:'task2', script:'test1.sh', argument: 'testArg', environmentVariables: 'one', workingSubDirectory: '/one')
             
            // Maven 2 and 3 --------------------------------------------------------
            task(type:'maven3', description:'task3 Simple Maven', goal:'clean install')
            task(type:'maven3', description:'task3Maven', goal:'clean install', hasTests:'true', useMavenReturnCode:'true')
            task(type:'maven3', description:'task3Maven more Args', goal:'clean install', hasTests:'true', useMavenReturnCode:'true',
                environmentVariables: 'one', workingSubDirectory: '/one', testDirectory: '**/target/my_custom/*.xml')
            task(type:'maven3', description:'task3MavenWithJdk', goal:'clean install', buildJdk:'jdk1.7', mavenExecutable:'maven 3')
            task(type:'maven2', description:'task2Maven', goal:'clean install', hasTests:'true', useMavenReturnCode:'true')
                       
            // junit parser --------------------------------------------------------
            task(type:'jUnitParser', description:'parse test')
            task(type:'jUnitParser', description:'parse cucumber results', final:'true', resultsDirectory:'**/TEST-*.xml')
             
            // deploy plugin --------------------------------------------------------
            task(type:'deployBambooPlugin', description:'deploy plugin', url:'http://mybamboo:1990/bamboo',
                username:'${bamboo.username}', passwordVariable:'${bamboo.password}', artifact:'artifactName')
             
      
             
            // make a task final with -> final: 'true' --------------------------------------------------------
            task(type:'deployBambooPlugin', description:'deploy plugin', url:'http://mybamboo:1990/bamboo',
                username:'${bamboo.username}', passwordVariable:'${bamboo.password}', artifact:'artifactName', final: 'true')
             
            task(type:'jUnitParser', description:'parse cucumber results', resultsDirectory:'**/TEST-*.xml', final:'true')
             
        }
    }
}
