include(path:'awstags-shortcuts.groovy')

plan(key:'AWSTAGS',name:'LambdaCI AWS Tags checking and modifying') {
  project(key:'HCLC',name:'HipChat Lambda CI')
  repository(name:'tags-repo')
  
  stage(name:'Default Stage') {
    job(key:'JOB1',name:'Build',description:'Build artifacts for AWSTags') {
    artifactDefinition(name:'lambda-tags_checker.py',location:'lambda/LambdaTagChecker',pattern:'*',shared:'true')
    task(type:'checkout',description:'Checkout Default Repository',cleanCheckout:'true') {
      repository(name:'tags-repo')
    }      
     awstagsRunTests()
    }
  }
}

deployment(name:'AWSTags CI deployment',planKey:'HCLC-AWSTAGS') {

  environment(name:'Update AWSTags lambda functions to STG') {
    awstagsArtifactDownload()
    awstagsUpdateLambdaVariables(environment:'stg')
    awstagsUpdateLambdaFunctions(environment:'stg')
  }

  environment(name:'Update AWSTags lambda functions to PROD') {
    task(type: 'script', description: 'Update AWS Lambda functions ',
         scriptBody : '''
         virtualenv venv
         ''')
  }
}    
