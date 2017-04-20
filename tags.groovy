plan(key:'AWSTAGS',name:'AwsTagsLambdaCI') {
  project(key:'HCLC',name:'HipChat Lambda CI')

    stage(name:'Default Stage') {
      job(key:'JOB1',name:'Build',description:'Build artifacts for AWSLego') {
        task(type:'checkout',description:'Checkout Default Repository',cleanCheckout:'true') {
          repository(name:'ops')
        }
      }
    }
}
deployment(name:'AWSLego CI deployment',planKey:'HCLC-AWSLEGO') {
  versioning(version:'${bamboo.buildResultKey}')

  environment(name:'Deploy AWSLego to STG') {
    task(type:'addRequirement',description:'Linux Agent') {
      requirement(key:'os',condition:'equals',value:'Linux')
    }
    task(type:'cleanWorkingDirectory')
  }

  environment(name:'Deploy AWSLego to PROD') {
    task(type:'addRequirement',description:'Linux Agent') {
      requirement(key:'os',condition:'equals',value:'Linux')
    }
    task(type:'cleanWorkingDirectory')
  }
}
