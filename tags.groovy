plan(key:'PLANTEMPLATES', name:'Deploy Plan Templates', description:'Deploy plan templates') {  
   project(key:'MYPROJECT', name:'My Project')
    
   // this should be a linked repository
   repository(name:'my-plan-templates')
      
   trigger(type:'stash') {     
      repository(name:'my-plan-templates')  
   }
    
   notification(type:'Failed Builds and First Successful', recipient:'committers')
    
   stage(name:'Plan templates') {     
      job(key:'DEPLOY', name:'Deploy plan templates') {        
         task(type:'checkout', description:'Checkout plan templates') {           
            repository(name:'my-plan-templates')           
         }
 
         task(type:'custom',
            createTaskKey:'com.atlassian.bamboo.plugin.bamboo-plan-templates:com.atlassian.bamboo.plugin.plantemplates',
            description:'Deploy plan template',
           
            // only one plan template per task ...
            template:'plan-templates/baseboxes.groovy',
            // ... but several shortcut files
            shortcuts:'plan-templates/shortcuts/common.groovy, plan-templates/shortcuts/more.groovy',
            
            // only deploy on master; on branches only validate the code
            executionStrategy:'executionStrategy.executeOnMaster',
 
            // target bamboo server + credentials
            bambooServer:'https://staging-bamboo.internal.atlassian.com',
            username:'${bamboo.plan.template.rollout.bot.username}')        
            passwordVariable:'${bamboo.plan.template.rollout.bot.password}',
            passwordVariableCheck:'true',
         }
   }
 
   branchMonitoring(notificationStrategy:'INHERIT') {     
      createBranch(matchingPattern:'.*')   
      inactiveBranchCleanup(periodInDays:'10')     
      deletedBranchCleanup(periodInDays:'1')
   }
}
