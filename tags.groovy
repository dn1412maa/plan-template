include(path:'awstags-shortcuts.groovy')

plan(key:'AWSTAGS',name:'LambdaCI AWS Tags checking and modifying') {
  project(key:'HCLC',name:'HipChat Lambda CI')
  
  stage(name:'Default Stage') {
    job(key:'JOB1',name:'Build',description:'Build artifacts for AWSTags') {
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
      cd lambda/LambdaTagChecker/
      sed -i -e "s/<HC_ROOM_ID>/${bamboo_hc_awstags_stg_hc_room_id}/g" config.yml
      sed -i -e "s/<HC_AUTH_TOKEN>/${bamboo_hc_awstags_stg_hc_auth_token}/g" config.yml
      sed -i -e "s/<BUCKET_NAME>/${bamboo_hc_awstags_stg_bucket_name}/g" config.yml
      cat config.yml
      zip -r lambda_tagchecker.zip *

      set -x
      export AWS_ACCESS_KEY_ID=${bamboo_hc_awstags_stg_aws_access_key}
      export AWS_SECRET_ACCESS_KEY=${bamboo_hc_awstags_stg_aws_password}
      export AWS_DEFAULT_REGION=${bamboo_hc_awstags_stg_aws_region}
      aws lambda update-function-code --function-name ${bamboo_hc_awstags_stg_lambda_name_checker} --zip-file fileb://lambda.zip
     ''')
  }

  environment(name:'Update AWSTags lambda functions to PROD') {
    task(type: 'script', description: 'Update AWS Lambda functions ',
         scriptBody : '''
         virtualenv venv
         ''')
  }
}    
