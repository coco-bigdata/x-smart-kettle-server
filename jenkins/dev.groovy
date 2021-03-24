     def label = "mydev-${UUID.randomUUID().toString()}"
    def img_version = '${img_version}'
    def project_name = "smart-kettle"
    def img_name = project_name
    def package_name = "yaukie"
    def setting_name = "settings"
    def repo_url = "registry.cn-hangzhou.aliyuncs.com"
    def user_name = "yaukie@163.com"
    def pass_word = "wst123456"

     node {

         stage('Preparation') {
             echo '准备拉取仓库源码....'
             checkout(
                     [$class: 'GitSCM', branches: [[name: '*/master']],
                       doGenerateSubmoduleConfigurations: false,
                       extensions: [],
                       submoduleCfg: [],
                       userRemoteConfigs: [[credentialsId: '67383ec2-d738-4632-b4b8-36c70180efb0',
                       url: 'https://gitee.com/yaukie/x-smart-kettle-server.git']]]
             )
             echo '仓库源码拉取成功....'
         }

         stage('Build') {
              echo '准备构建项目...... '
              sh 'mvn clean install -f  pom.xml -U -Dmaven.frame.skip=true -s settings/' + setting_name + '.xml'
             echo '项目'+project_name+'构建成功..... '
          }

         stage('DockerBuild') {
             echo '准备推送镜像....'
             dir("docker/"){
                 sh 'ls -al '
                 sh 'rm -f  *.tar.gz'
                 echo project_name+'.jar删除成功,准备登录阿里云....'
             }
               sh 'docker login -u '+user_name+ ' -p '+pass_word+' registry.cn-hangzhou.aliyuncs.com'
             echo '阿里云镜像仓库登录成功....'
             sh 'cp target/*.tar.gz  docker/'
             def img_url = repo_url + '/'+ package_name+'/'+project_name+':'+img_version
             sh 'cd  docker/ && docker build -t '+ img_url + ' .'
             sh 'docker push '+ img_url
             echo '镜像已成功推送至阿里云....'

         }

     }
