     def label = "mydev-${UUID.randomUUID().toString()}"
    def img_version = '${img_version}'
    def project_name = "smart-kettle"
    def img_name = project_name
    def name_space = "yaukie"
    def setting_name = "settings"
    def repo_url = "registry.cn-qingdao.aliyuncs.com"
    def user_name = '${docker_user_name}'
    def pass_word = '${docker_pass_word}'
     def deploy_server_name = '101.132.24.211'
     def deploy_server_host = '101.132.24.211'
     def deploy_server_user = '${deploy_server_user}'
     def deploy_server_password = '${deploy_server_password}'
     node {

         stage('Preparation') {
             echo '准备拉取仓库源码....'
             checkout(
                     [$class                           : 'GitSCM', branches: [[name: '*/master']],
                      doGenerateSubmoduleConfigurations: false,
                      extensions                       : [],
                      submoduleCfg                     : [],
                      userRemoteConfigs                : [[credentialsId: '67383ec2-d738-4632-b4b8-36c70180efb0',
                                                           url          : 'https://gitee.com/yaukie/x-smart-kettle-server.git']]]
             )
             echo '仓库源码拉取成功....'
         }

         stage('Build') {
             echo '准备构建项目...... '
             sh 'mvn clean install -f  pom.xml -U -Dmaven.frame.skip=true -s settings/' + setting_name + '.xml'
             echo '项目' + project_name + '构建成功..... '
         }

         stage('DockerBuild') {
             echo '准备推送镜像....'
             dir("docker/") {
                 sh 'ls -al '
                 sh 'rm -f  *.tar.gz'
                 echo project_name + '.jar删除成功,准备登录阿里云....'
             }
             sh 'docker login -u ' + user_name + ' -p ' + pass_word + ' ' + repo_url
             echo '阿里云镜像仓库登录成功....'
             //  echo '准备删除本地镜像文件 smart-kettle ...'
             //  sh 'docker stop $(docker ps -a  | awk  \'{print $1,$NF}\' | grep -v grep | grep \'smart-kettle\'| awk -F \' \' \'{print $1}\') && docker rm $(docker ps -a  | awk  \'{print $1,$NF}\' | grep -v grep | grep \'smart-kettle\'| awk -F \' \' \'{print $1}\')'
             //     echo '准备删除本地镜像文件 smart-kettle-'+img_version
             // sh 'docker rmi `docker images | grep -v grep | grep \'smart-kettle\' | awk \'{print $3}\'`'
             //  echo '本地镜像文件删除成功...!'
             sh 'cp target/*.tar.gz  docker/'
             def img_url = repo_url + '/' + name_space + '/' + project_name + ':' + img_version
             sh 'cd  docker/ && docker build -t ' + img_url + ' .'
             sh 'docker push ' + img_url
             echo '镜像已成功推送至阿里云....'
             echo '准备启动 smart-kettle....'
             sh 'cd /opt/docker/smart-kettle && docker-compose pull ' + img_name + '  && docker-compose stop ' + img_name + ' && docker-compose up -d ' + img_name
             echo 'smart-kettle 启动成功!'
         }

//         def remote = [:]
//         remote.name = deploy_server_name
//         remote.host = deploy_server_host
//         remote.user = deploy_server_user
//         remote.password = deploy_server_password
//         remote.allowAnyHosts = true
//         stage('remote-deploy') {
//             sshCommand remote: remote, command: 'cd /opt/docker/smart-kettle && docker-compose pull ' + img_name + '  && docker-compose stop ' + img_name + ' && docker-compose up -d ' + img_name;
//         }


     }