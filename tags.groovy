// this is a comment :)
// It will create or update a plan.
// If you deleted the plan, wait a few minutes because deletion is asynchronous in bamboo
plan(key:'PLANTEST', name:'Testing plan templates', description:'A sensible description goes here. Don't edit the plan!') {
 
    // it's a good idea to mark
    label(name:"plan-templates")
     
    // your default repository. You can add more than one.
    repository(name:'tags-repo')
 
    // Then your build will be triggered
    trigger(description:'trigger1', type:'polling', strategy:'periodically', frequency:'180'){
        repository(name:'tags-repo')
    }
     
    // The 'notifications' tab in your plan. Check https://extranet.atlassian.com/x/FBeSfg for all options
 
    // The project your plan belongs to.
    project(key: 'PERSONAL', name:'Z Personal', description:'a project description')
 
    stage(name:"Default stage", description: "another sensible description", manual: "false"){
        job(name: "Compile stuff", key: 'COMPILE', description: 'A nice description' ){
  
            // You can see the full name of the requirements https://extranet.atlassian.com/display/RELENG/Register+-+Capabilites
            // or clicking on its name and checking the URL
            requirement(key:"os", condition:"equals", value:"Windows")
            requirement(key:"system.git.executable", condition:"exists")
 
            // Check the code and https://extranet.atlassian.com/x/FBeSfg for all options
            // A task needs a description. It's mandatory.
            task(type:'checkout', description:'task05'){
                repository(name:'test')
            }
 
            task(type:'script', description:'task1', scriptBody:'''
                    echo hello world
                '''
            )
        }
    }
}
