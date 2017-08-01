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
   
         task(type:'awsS3',description:'Upload web-client-native.zip to s3',
          scriptBody:'''\

#Env vars for the aws cli
role_arn=${bamboo.hipchat.aws.stg.role}
session_uuid=$(/usr/bin/uuidgen)
creds=$(aws sts assume-role \
        --role-arn "$role_arn" \
        --role-session-name "$session_uuid"
    )
export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID="$(echo $creds | jq -r .Credentials.AccessKeyId)"
export AWS_SECRET_ACCESS_KEY="$(echo $creds | jq -r .Credentials.SecretAccessKey)"
export AWS_SESSION_TOKEN="$(echo $creds | jq -r .Credentials.SessionToken)"

aws s3 cp web-client-native.zip s3://hipchat-ops/hipchat4/webpackages/nightly/web-client-native.zip --acl public-read
''')
     
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
