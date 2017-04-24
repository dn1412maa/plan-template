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

deployment(name:'AWSTags CI deployment',planKey:'HCLC-AWSTAGS') {

  environment(name:'Update AWSTags to STG') {
    task(type: 'script', description: 'Update AWS Lambda ',
     scriptBody : '''
     virtualenv venv
     ''')
  }

  environment(name:'Update AWSTags to PROD') {
    task(type: 'script', description: 'Update AWS Lambda ',
         scriptBody : '''
         virtualenv venv
         ''')
  }
}    
