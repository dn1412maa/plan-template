plan(key:'AWSTAGS',name:'LambdaCI AWS Tags checking and modifying') {
  project(key:'HCLC',name:'HipChat Lambda CI')

    stage(name:'Default Stage') {
      job(key:'JOB1',name:'Build',description:'Build artifacts for AWSLego') {
        task(type:'checkout',description:'Checkout Default Repository',cleanCheckout:'true') {
          repository(name:'tags-repo')
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
    task(type:'script',description:'Setup venv and create or update stack',
         scriptBody:'''\
# Create virtual environment and activate it
virtualenv venv
. venv/bin/activate

# Install required packages
pip install --upgrade pip
pip install awscli

set -x
# Stack setup
echo "Checking if stack lego exists"
''')
  }

  environment(name:'Deploy AWSLego to PROD') {
    task(type:'addRequirement',description:'Linux Agent') {
      requirement(key:'os',condition:'equals',value:'Linux')
    }
    task(type:'cleanWorkingDirectory')
  }
}
