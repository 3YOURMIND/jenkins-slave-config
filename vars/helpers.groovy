def isTagBuild() {
  gitOut = sh (
    script: "git --no-pager describe --exact-match || true",
    returnStdout: true
  ).trim()

  return !gitOut.startsWith("fatal:")
}

def getTagName() {
  gitOut = sh (
    script: "git --no-pager describe --exact-match || true",
    returnStdout: true
  ).trim()

  return gitOut.startsWith("fatal:") ? "" : gitOut.replaceAll('/', '-').stripIndent()
}

def ECRLogin(){
    sh "eval \$(aws ecr get-login --no-include-email --region eu-central-1)"
}

def retagAndPushImage(String project, String branch_name, String tag){
  if (tag != "") {
    sh "docker tag ${project}:${branch_name} ${env.ECR_ENDPOINT}/${project}:${tag}"
    sh "docker push ${env.ECR_ENDPOINT}/${project}:${tag}"
  } else {
    sh "docker tag ${project}:${branch_name} ${env.ECR_ENDPOINT}/${project}:${branch_name}"
    sh "docker push ${env.ECR_ENDPOINT}/${project}:${branch_name}"
  }
}

return this
