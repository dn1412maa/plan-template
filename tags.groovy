plan(key:'AWSTAGS',name:'LambdaCI AWS Tags checking and modifying') {
  project(key:'HCLC',name:'HipChat Lambda CI')
  
  stage(name:'Default Stage') {
    job(key:'JOB1',name:'Build',description:'Build artifacts for AWSTags') {
      artifactDefinition(name:'updater53.py',location:'scripts/lambda/awslego/awslego',pattern:'updater53.py',shared:'true')
      artifactDefinition(name:'deploy-helper.sh',location:'scripts/lambda/awslego',pattern:'deploy-helper.sh',shared:'true')
      task(type:'jUnitParser',description:'Parse test results',resultsDirectory:'**/nosetests.xml')
    }
  }
}

deployment(name:'AWSTags CI deployment',planKey:'HCLC-AWSTAGS') {

  environment(name:'Update AWSTags lambda functions to STG') {
    task(type:'checkout',description:'Code') {
         repository(name:'tags-repo')
      }
    task(type: 'script', description: 'Update AWS Lambda functions',
     scriptBody : '''
     virtualenv venv
     . venv/bin/activate

      # Install required packages
      #pip install --upgrade pip
      #pip install awscli
      
      set -x
      export AWS_ACCESS_KEY_ID=${bamboo_hc_awslego_stg_aws_access_key}
      export AWS_SECRET_ACCESS_KEY=${bamboo_hc_awslego_stg_aws_password}
      export AWS_DEFAULT_REGION=${bamboo_hc_awslego_stg_aws_region}

      sed --help
     ''')
  }

  environment(name:'Update AWSTags lambda functions to PROD') {
    task(type: 'script', description: 'Update AWS Lambda functions ',
         scriptBody : '''
         virtualenv venv
         ''')
  }
}    
