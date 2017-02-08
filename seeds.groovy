import org.kohsuke.github.GitHub

def gh = GitHub.connectAnonymously()
gh.getOrganization('chef-cookbooks').listRepositories().each { repo ->
  job(repo.name) {
    scm { github(repo.fullName) }
    steps { 
      shell('KITCHEN_LOCAL_YAML=.kitchen.docker.yml /opt/chefdk/embedded/bin/kitchen verify')
    } 
    triggers {
        cron('@daily')
    }
  }
}
