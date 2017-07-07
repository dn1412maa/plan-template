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
   
                 task(type:'awsS3',description:'Upload webcore to s3',pluginVersionOnSave:'2.10.5',
                resourceAction:'Upload',pluginConfigVersionOnSave:'6',
                targetBucketName:'hipchat-ops',artifactToUpload:'LOCAL_FILES',
                metadataConfigurationJson:'''\
{
    "x-amz-acl": "public-read"
}\
''',
                secretKey:'${bamboo.hipchat.aws.hipchat_ops.secret_key.password}',sourceLocalPath:'web-client-*.zip',
                resourceRegion:'us-east-1',accessKey:'${bamboo.hipchat.aws.hipchat_ops.access_key}',
                awsCredentialsSource:'INLINE',awsConnectorId:'-1',
                targetObjectKey:'hipchat4/webpackages/internal/')
      
     awstagsRunTests()
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
