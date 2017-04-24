plan(key:'AWSTAGS',name:'LambdaCI AWS Tags checking and modifying') {
  project(key:'HCLC',name:'HipChat Lambda CI')
  
  stage(name:'Default Stage') {
    job(key:'JOB1',name:'Build',description:'Build artifacts for AWSLego') {
      artifactDefinition(name:'updater53.py',location:'scripts/lambda/awslego/awslego',pattern:'updater53.py',shared:'true')
      artifactDefinition(name:'deploy-helper.sh',location:'scripts/lambda/awslego',pattern:'deploy-helper.sh',shared:'true')
      task(type:'jUnitParser',description:'Parse test results',resultsDirectory:'**/nosetests.xml')
    }
  }
}

deployment(name:'AWSLego CI deployment',planKey:'HCLC-AWSTAGS') {

  environment(name:'Deploy AWSLego to STG') {
    task(type: 'script', description: 'Deployment AWS Stack ',
     scriptBody : '''
     virtualenv venv
     ''')
  }

  environment(name:'Deploy AWSLego to PROD') {
    task(type: 'script', description: 'Deployment AWS Stack ',
         scriptBody : '''
         virtualenv venv
         ''')
  }
}    
