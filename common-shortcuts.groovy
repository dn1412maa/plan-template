  defaultPlanPermissions(){
  permissions() {
    user(name:'buildeng-bd-bot',permissions:'read,write,build,clone,administration')
    anonymous(permissions:'read')
    loggedInUser(permissions:'read,build,write,administration')
  }
}
