awstagsRunTests(){
  task(type:'script',description:'Setup awstags and Run tests',
     scriptBody:'''
# Change current directory
# cd lambda/LambdaTagChecker/tests

# Create virtual environment and activate it
virtualenv venv
#. venv/bin/activate

# Install required packages
#pip install nose
#python setup.py install

# AWS_DEFAULT_REGION variable required for tests
export AWS_DEFAULT_REGION=us-east-1

# Run awstags tests
# python ../setup.py nosetests --with-xunit
''')
}

getAwsCred(){
 export AWS=111111
 export AWS_KEY=22222
}


awstagsUpdateLambdaVariables(['environment']){
  task(type:'script',description:'Update variables in lambda functions',
     scriptBody:'''
# Update variables for lambda function
#      cd LambdaTagChecker
#      sed -i -e "s/<HC_ROOM_ID>/${bamboo_hc_awstags_#environment_hc_room_id}/g" config.yml
#      sed -i -e "s/<HC_AUTH_TOKEN>/${bamboo_hc_awstags_#environment_hc_auth_token}/g" config.yml
#      sed -i -e "s/<BUCKET_NAME>/${bamboo_hc_awstags_#environment_bucket_name}/g" config.yml
#      cat config.yml

''')
}

awstagsArtifactDownload(){
  task(type:'artifactDownload',description:'Download release contents',planKey:'HCLC-AWSTAGS') {
    artifact(name:'LambdaTagChecker',localPath:'./LambdaTagChecker')
    artifact(name:'LambdaTagModification',localPath:'./LambdaTagModification')
  }
}


awstagsUpdateLambdaFunctions(['environment', 'aws_access_key_name', 'aws_secret_key_name']) {
  task(type:'script',description:'Update functions',
     scriptBody:'''
#virtualenv venv     
#. venv/bin/activate

# Install required packages
#pip install --upgrade pip
#pip install awscli

# set -x
#export AWS_ACCESS_KEY_ID=${bamboo.hc.awslego.#aws_access_key_name}
# export AWS_SECRET_ACCESS_KEY=${bamboo.hc.awslego.#aws_secret_key_name}
# export AWS_DEFAULT_REGION=${bamboo.hc.awslego.#environment.aws.region}

echo #aws_access_key_name
#zip -jr lambda_tagmodification.zip LambdaTagModification
#cd LambdaTagChecker
#zip -r ../lambda_tagchecker.zip *

#aws lambda update-function-code --function-name ${bamboo_hc_awslego_#environment_lambda_name_checker} --zip-file fileb://../lambda_tagchecker.zip

''')
}
