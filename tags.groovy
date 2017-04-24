plan(key:'AWSTAGS2',name:'LambdaCI AWS Tags checking and modifying2') {
  project(key:'HCLC',name:'HipChat Lambda CI')
    repository(name:'tags-repo')
     
    stage(name:"stage1", description: "stage1", manual: "true"){
        job(name: "job1", key: 'job11Key', description: 'hellojob' ){           
            // checkout --------------------------------------------------------
            task(type:'checkout', description:'checkout 1'){
                repository(name:'tags-repo')
            }
             
            task(type:'checkout', description:'checkout 2', cleanCheckout: 'true'){
                repository(name:'tags-repo', checkoutDirectory:'./test1directory')
            }
             
            // script  --------------------------------------------------------
            task(type:'script', description:'task1', scriptBody:'''
                    echo hello1
                    echo hello2
                '''
            )
                        
        }
    }
}
