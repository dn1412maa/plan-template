plan(key:'AWSLEGO',name:'LambdaCI') {
  project(key:'HCLC',name:'HipChat Lambda CI')

    stage(name:'Default Stage') {
      task(type:'checkout',description:'Checkout Default Repository',cleanCheckout:'true') {
        repository(name:'ops')
      }
    }
  }
}
deployment(name:'AWSLego CI deployment',planKey:'HCLC-AWSLEGO') {
  defaultSOXDeployPermissions()
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
