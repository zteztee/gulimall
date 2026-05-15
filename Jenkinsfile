pipeline {
    agent { label 'maven-jdk1.8' } // 就用你能跑的这个


    stages {
      stage('clone-code') {
            steps {
                container('maven') {
                    // ✅ 强制修复 JAVA_HOME —— 这两行是关键！！！
                    sh 'export JAVA_HOME=/opt/java/openjdk/jre'
                    sh 'export PATH=$JAVA_HOME/bin:$PATH'

                    sh 'echo $PROJECT_VERSION'
                    sh 'echo $PROJECT_NAME'
                    git(url: 'https://gitee.com/zang-tianen/gulimall.git', credentialsId: 'gitee', branch: 'master', changelog: true, poll: false)
                    sh "echo 正在完整编译项目"
                    sh "echo 项目名称：$PROJECT_NAME"
                    sh "mvn -gs `pwd`/mvn-settings.xml clean install -Dmaven.test.skip=true"

                }
            }
        }

      stage('sonarqube analysis') {
          steps {
              container('maven') {
                  // ✅ 这里也要加
                  sh 'export JAVA_HOME=/opt/java/openjdk/jre'
                  sh 'export PATH=$JAVA_HOME/bin:$PATH'

                  // withCredentials([string(credentialsId: "$SONAR_CREDENTIAL_ID", variable: 'SONAR_TOKEN')]) {
                  //     withSonarQubeEnv('sonar') {
                  //         sh "echo 当前目录 `pwd`"
                  //         sh "mvn sonar:sonar -gs `pwd`/mvn-settings.xml  -Dsonar.login=$SONAR_TOKEN"
                  //     }
                  // }
              }
          }
      }


      stage ('build & push') {
        steps {
            // 1. 编译（不变）
            container('maven') {
                sh 'mvn -Dmaven.test.skip=true -gs `pwd`/mvn-settings.xml clean package'
            }

            // 2. KANIKO 构建 + 推送（极简版！）
            container('kaniko') {
                withCredentials([usernamePassword(
                    credentialsId: "$DOCKER_CREDENTIAL_ID",
                    passwordVariable: 'DOCKER_PASSWORD',
                    usernameVariable: 'DOCKER_USERNAME'
                )]) {
                     sh '''
                        mkdir -p /kaniko/.docker
                        AUTH=$(echo -n "$DOCKER_USERNAME:$DOCKER_PASSWORD" | base64 -w 0)
                        echo "{\\\"auths\\\":{\\\"https://index.docker.io/v1/\\\":{\\\"auth\\\":\\\"$AUTH\\\"}}}" > /kaniko/.docker/config.json
                    '''


                   sh '''
                    if [ "$BRANCH_NAME" = "master" ]; then
                        echo "当前是 master 分支，推送版本号 + latest"
                        /kaniko/executor \
                        --context ./$PROJECT_NAME \
                        --dockerfile Dockerfile \
                        --destination $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:$PROJECT_VERSION \
                        --destination $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:latest \
                        --verbosity info \
                        --skip-tls-verify
                        else
                            echo "当前是开发分支，仅推送版本号"
                            /kaniko/executor \
                            --context ./$PROJECT_NAME \
                            --dockerfile Dockerfile \
                            --destination $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER \
                            --verbosity info \
                            --skip-tls-verify
                        fi
                    '''
                }
            }
        }
    }

      stage ('部署到k8s') {
        steps {
          input(id: "deploy-to-dev-$PROJECT_NAME",message:"是否将 $PROJECT_NAME 部署到集群中?")
//           kubernetesDeploy(config: "$PROJECT_NAME/deploy/**",enableConfigSubstitution: true,
//             kubeconfigId: "$KUBECONFIG_CREDENTIAL_ID"
//           )

          container('base') {
                      withCredentials([usernamePassword(credentialsId : 'gitee' ,passwordVariable : 'PASS' ,usernameVariable : 'USER')]) {
                        sh '''ks app update --app-name gulimall-cart-cd \
           --app-namespace gulimallqvp9f \
           --name docker.io/zte020415/gulimall-cart \
           --newName $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME \
           --newTag $PROJECT_VERSION \
           --git-password $PASS --git-username=$USER \
           --git-target-branch master'''
                      }

                    }

        }
      }
         stage ('发布版本') {
          when {
              expression {
                  params.PROJECT_VERSION != null && params.PROJECT_VERSION.startsWith("v")
              }
          }
           steps {
              input(id: 'release-image-with-tag', message: '发布当前版本镜像?')
                container('maven') {
                  withCredentials([usernamePassword(
                      credentialsId: GITEE_CREDENTIAL_ID,
                      usernameVariable: 'GIT_USERNAME',
                      passwordVariable: 'GIT_PASSWORD'
                  )]) {
                      sh '''
                          git config --global --add safe.directory '*'
                          git config --global user.email "958394162@qq.com"
                          git config --global user.name "958394162@qq.com"
                          git tag -a $PROJECT_VERSION -m "$PROJECT_VERSION"
                          git push http://$GIT_USERNAME:$GIT_PASSWORD@gitee.com/$GITEE_ACCOUNT/gulimall.git --tags --ipv4
                      '''
                  }
              }
          }
      }


  }

    parameters {
        string(name: 'PROJECT_VERSION', defaultValue: 'v0.0.1cta', description: '')
        string(name: 'PROJECT_NAME', defaultValue: 'gulimall-cart', description: '')
    }

    environment {
        DOCKER_CREDENTIAL_ID = 'dockerhub-id'
        GITEE_CREDENTIAL_ID = 'gitee'
        KUBECONFIG_CREDENTIAL_ID = 'demo-kubeconfig'
        REGISTRY = 'docker.io'
        DOCKERHUB_NAMESPACE = 'zte020415'
        GITEE_ACCOUNT = '958394162@qq.com'
        SONAR_CREDENTIAL_ID = 'sonar-qube'
        BRANCH_NAME = 'master'
    }
}