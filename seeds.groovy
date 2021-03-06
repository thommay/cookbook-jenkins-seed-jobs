import org.kohsuke.github.GitHub

def gh = GitHub.connectAnonymously()
  gh.getOrganization('chef-cookbooks').listRepositories().each { repo ->
    job(repo.name) {
      scm { github(repo.fullName, 'refs/heads/master') }
      steps { 
        shell('KITCHEN_LOCAL_YAML=.kitchen.docker.yml /opt/chefdk/embedded/bin/kitchen test')
      } 
      publishers {
        postBuildScript {
          buildStep {
            scriptOnlyIfFailure(false)
            scriptOnlyIfSuccess(false)
            markBuildUnstable(false)
            executeOn("BOTH")
            shell {
              command('KITCHEN_LOCAL_YAML=.kitchen.docker.yml /opt/chefdk/embedded/bin/kitchen destroy')
            }
          }
        }
      }
      triggers {
        cron('@daily')
      }
    }
  }
