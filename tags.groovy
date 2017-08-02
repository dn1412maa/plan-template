include(path:'awstags-shortcuts.groovy')

plan(key:'AWSTAGS',name:'LambdaCI AWS Tags checking and modifying') {
  project(key:'HCLC',name:'HipChat Lambda CI')
  repository(name:'tags-repo')
  
  stage(name:'Default Stage') {
    job(key:'JOB1',name:'Build',description:'Build artifacts for AWSTags') {
    artifactDefinition(name:'LambdaTagChecker',location:'lambda/LambdaTagChecker',pattern:'**',shared:'true')
    artifactDefinition(name:'LambdaTagModification',location:'lambda/LambdaTagModification',pattern:'**',shared:'true')
    
    task(type:'checkout',description:'Checkout Default Repository',cleanCheckout:'true') {
      repository(name:'tags-repo')
    }      
         exportAwsCred(environment:'prod',region:'us-west-1')
         exportAwsCred(environment:'stg',region:'us-west-1')
         task(type: 'script',description:'Upload web-client-native.zip to s3',
          scriptBody:'''\
          
chmod +x export_aws_cred_prod_us-west-1.sh && . ./export_aws_cred_prod_us-west-1.sh
echo $AWS_DEFAULT_REGION
echo $AWS_ACCESS_KEY_ID
echo $AWS_SECRET_ACCESS_KEY
rm -f export_aws_cred_prod_us-west-1.sh
printenv
'''
             )
     
    }
  }
}

deployment(name:'AWSTags CI deployment',planKey:'HCLC-AWSTAGS') {

  environment(name:'Update AWSTags lambda functions to STG') {
    awstagsArtifactDownload()
    awstagsUpdateLambdaVariables(environment:'stg')
    awstagsUpdateLambdaFunctions(environment:'stg', aws_access_key_name: 'asd.ACC.access_key', aws_secret_key_name:'asd.SEC')

  }

  environment(name:'Update AWSTags lambda functions to PROD') {
    task(type: 'script', description: 'Update AWS Lambda functions ',
         scriptBody : '''
         virtualenv venv
         ''')
  }
} 
